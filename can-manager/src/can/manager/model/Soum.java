package can.manager.model;

import can.manager.data.Config;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Soum {    

    private final Path pathFileCms;
    private Connection conn = null;
    private final ObservableList<SoumTitles> listTitles = FXCollections.observableArrayList();
    private final ObservableList<SoumArticles> listArticles = FXCollections.observableArrayList();
    private int numCanSelected;
    private final Config config = new Config();
    private final Catalog catalog = new Catalog();
    private int yearCanSelected;
    private float totalCahier = 0;
    
    public Soum(Path pathFileCms) {

        this.pathFileCms = pathFileCms;
        if(connect())
        {
            setTitlesCan();
        }
    }   
    
    private boolean connect()
    {
        if(Files.exists(pathFileCms)) {
            System.out.println("[ V ] File cms exist");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + pathFileCms);
                System.out.println("[ V ] Connection to file " + pathFileCms);
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
            }
        }

        else {
            System.out.println("[ X ] File .cms dont exist");
        }        
        
        return false;
    }      
    
    //tabSelected
    public void setIndexTabSelected(int index){
        SoumTitles title = this.listTitles
                .stream()
                .filter(x -> index == x.getIndex())
                .findAny()
                .orElse(null);
        
        this.numCanSelected = title.getNumCan();
        this.yearCanSelected = title.getYearCan();
        setArticles();
        setDescriptions();
        addTitlesArticles();
    }
    public int getNumCanSelected(){
        return this.numCanSelected;
    }    


    //titlesCan
    public ObservableList<SoumTitles> getTitlesCan(){
        return this.listTitles;
    }    
    private void setTitlesCan() {
        String sql = "SELECT DISTINCT z02 FROM records WHERE z01='G' AND z11 = 1";
        
        try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next())
                {
                    SoumTitles title = new SoumTitles(rs.getString("z02"));
                    listTitles.add(title);
                }
        }
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }

        if(!this.listTitles.isEmpty())
        {
            System.out.println("[ V ] Size of list : " + this.listTitles.size());
        }
        else
            System.out.println("[ X ] list is empty...");
    }
    

    //articlesCan
    public ObservableList<SoumArticles> getArticles(){
        return listArticles
                    .stream()
                    .sorted((a1, a2) -> a1.getArticle().compareTo(a2.getArticle()))
                    .collect(collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
    }
    public void setArticles() {
        String sql = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND (z11 = 2)";
        catalog.initialize(config.getCatalogDirectory().toString()+ System.getProperty("file.separator") + "F" + this.numCanSelected + this.yearCanSelected + ".cmc");
        totalCahier = 0;
        
        try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listArticles.clear();
                while(rs.next())
                {
                    listArticles.add(new SoumArticles(rs.getString("z03")));
                }
        }           
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }      
    }

    private void setDescriptions() {
        for(SoumArticles article : this.listArticles)
        {
            int numArticle = new Integer(article.getArticle().substring(0, 3));
            int numSubArticle = new Integer(article.getArticle().substring(3, 6));            
            String lastVar = new String("");
            
            //Description
            String sqlDesc = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND z11 = 3 AND z03 = " + article.getArticle();
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlDesc);
                while(rs.next())
                {
                    //from *.cmc
                    if(!lastVar.equals(rs.getString("z04")))
                        article.addDesc(catalog.getArticleFromArticleSubVar(numArticle, numSubArticle, new Integer(rs.getString("z04"))));            
                    
                    //from *.cms
                    article.addDesc(rs.getString("z23").trim() + "\n");
                    
                    //ini lastVar for the next loop
                    lastVar = rs.getString("z04");
                }
            }
            catch(SQLException e){
                System.out.println("[ X ] " + e.getMessage());
            }

            //Um
            String sqlUm = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND z11 = 5 AND z03 = " + article.getArticle();
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlUm);
                while(rs.next())
                {
                    //from *.cmc
                    article.addUm(catalog.getArticleFromPosition(numArticle, numSubArticle).getUnit());
                    
                    //from *.cms
                    article.addDesc(rs.getString("z23").trim() + "\n");
                }
            }
            catch(SQLException e){
                System.out.println("[ X ] " + e.getMessage());
            }            

            //Quantite + prix
            String sqlQuantite = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND z11 = 6 AND z03 = " + article.getArticle();
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlQuantite);
                while(rs.next())
                {                    
                    //from *.cms
                    article.addQuantite(rs.getString("z15"));
                    article.addPrixSoum(rs.getString("z19"));
                    if(!rs.getString("z19").isEmpty() && !rs.getString("z15").isEmpty())
                        totalCahier = (Float.parseFloat(rs.getString("z19")) * Float.parseFloat(rs.getString("z15"))) + totalCahier;
                }
            }
            catch(SQLException e){
                System.out.println("[ X ] " + e.getMessage());
            }            
        }
    }

    private void addTitlesArticles() {
        List<SoumArticles> listTitlesArticles = new ArrayList<>();
        listTitlesArticles.addAll(listArticles);
        
        //conditions générales
        listTitlesArticles.add(new SoumArticles("000000"));
        
        //loop soum
        for(SoumArticles article : this.listArticles)
        {
            //create num titles from each articles
            int x00_000 = (new Integer(article.getArticle()))/100000*100000;
            int xx0_000 = (new Integer(article.getArticle()))/10000*10000;
            int xxx_000 = (new Integer(article.getArticle()))/1000*1000;
            int xxx_x00 = (new Integer(article.getArticle()))/100*100;
            int xxx_xx0 = (new Integer(article.getArticle()))/10*10;
            
            //search if alreally exist and add if not
            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(x00_000))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(x00_000)));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(x00_000))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(x00_000)));            
            
            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xx0_000))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(xx0_000)));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xx0_000))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(xx0_000)));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_000))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(xxx_000)));            

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_x00))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(xxx_x00)));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_xx0))))
                listTitlesArticles.add(new SoumArticles(Integer.toString(xxx_xx0)));
        }
        
        //loop titles articles 
        for(SoumArticles article : listTitlesArticles){
            //add too soum titles if not exist
            if(!listArticles.stream().anyMatch(a -> a.getArticle().equals(article.getArticle())))
            {
                int numArticle = new Integer(article.getArticle().substring(0, 3));
                int numSubArticle = new Integer(article.getArticle().substring(3, 6));  
                article.addDesc(catalog.getArticleFromArticleSubVar(numArticle, numSubArticle, 0));
                listArticles.add(article);
            }
        }
    }

    public String getTotalCahier() {

        return String.format("%.2f", this.totalCahier / 100000);
                
    }
}
