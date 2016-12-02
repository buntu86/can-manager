package can.manager.model;

import can.manager.data.Config;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Soum {    

    private final Path pathFileCms;
    private Connection conn = null;
    private final ObservableList<SoumTitles> listTitles = FXCollections.observableArrayList();
    private ObservableList<SoumArticles> listArticles = FXCollections.observableArrayList();
    private int numCanSelected;
    private final Config config = new Config();
    private final Catalog catalog = new Catalog();
    private int yearCanSelected;
    
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
        System.out.println("setArticlesCan : " + sql);
        catalog.initialize(config.getCatalogDirectory().toString()+ System.getProperty("file.separator") + "F" + this.numCanSelected + this.yearCanSelected + ".cmc");
        
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
            
            String sql = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND z11 = 3 AND z03 = " + article.getArticle();
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    //from *.cmc
                    if(!lastVar.equals(rs.getString("z04")))
                        article.addDesc(catalog.getArticleFromArticleSubVar(numArticle, numSubArticle, new Integer(rs.getString("z04"))));            
                    
                    //from *.cms
                    article.addDesc(rs.getString("z23").trim() + "\n");
                    article.addQuantite(rs.getString("z15"));
                    article.addUm(rs.getString("z16"));
                    article.addPrixSoum(rs.getString("z19"));  
                    
                    //ini lastVar for the next loop
                    lastVar = rs.getString("z04");
                }
            }
            catch(SQLException e){
                System.out.println("[ X ] " + e.getMessage());
            }
        }
    }

    private void addTitlesArticles() {
        List<SoumArticles> listTitlesArticles = new ArrayList<SoumArticles>();
        listTitlesArticles.addAll(listArticles);
        
        //loop soum
        for(SoumArticles article : this.listArticles)
        {
            //create titles from each articles
            int x00_000 = (new Integer(article.getArticle()))/100000*100000;
            int xx0_000 = (new Integer(article.getArticle()))/10000*10000;
            int xxx_000 = (new Integer(article.getArticle()))/1000*1000;
            int xxx_x00 = (new Integer(article.getArticle()))/100*100;
            int xxx_xx0 = (new Integer(article.getArticle()))/10*10;
            
            System.out.print(article.getArticle() + " : ");
            
            //search if alreally exist and add if not
            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(x00_000))))
                listTitlesArticles.add(new SoumArticles(x00_000));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(x00_000))))
                listTitlesArticles.add(new SoumArticles(x00_000));            
            
            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xx0_000))))
                listTitlesArticles.add(new SoumArticles(xx0_000));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xx0_000))))
                listTitlesArticles.add(new SoumArticles(xx0_000));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_000))))
                listTitlesArticles.add(new SoumArticles(xxx_000));            

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_x00))))
                listTitlesArticles.add(new SoumArticles(xxx_x00));

            if(!listTitlesArticles.stream().anyMatch(a -> a.getArticle().equals(Integer.toString(xxx_xx0))))
                listTitlesArticles.add(new SoumArticles(xxx_xx0));

            System.out.println(xx0_000 + " " + xxx_000 + " " + xxx_x00 + " " + xxx_xx0);
        }
        
        //loop titles articles 
        for(SoumArticles article : listTitlesArticles){
            //add too soum titles if not exist
            if(!listArticles.stream().anyMatch(a -> a.getArticle().equals(article.getArticle())))
                listArticles.add(article);
        }
    }
}
    /*
    private void setArticlesFromCan() {
        SoumArticles article = null;
        String sql = "SELECT * FROM records WHERE z01='G' AND z02 = " + this.numCanSelected + " AND (z11 = 2 OR z11 = 3)";
        System.out.println("setArticlesCan : " + sql);
        catalog.initialize(config.getCatalogDirectory().toString()+ System.getProperty("file.separator") + "F" + this.numCanSelected + this.yearCanSelected + ".cmc");
        int numArticle=0, numSubArticle=0, var=0;
        
        try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listArticles.clear();
                String lastVarString = new String();                
                
                while(rs.next())
                {
                    //New article
                    if(new Integer(rs.getString("z11")) == 2)
                    {
                        //Add last article
                        if(article!=null)
                            listArticles.add(article);   

                        numArticle = new Integer(rs.getString("z03").substring(0, 3));
                        numSubArticle = new Integer(rs.getString("z03").substring(3, 6));
                        
                        //Add subarticle
                        //subarticle .0x0
                        int numSubArticle0x0 = (numSubArticle/10)*10;
                        if(numSubArticle0x0!=0 && numSubArticle!=numSubArticle0x0)
                        {
                            article = new SoumArticles(((numArticle*1000) + numSubArticle0x0));
                            String check = new String(article.getArticle());
                            if(listArticles.stream().anyMatch(a -> a.getArticle().equals(check))) 
                                listArticles.add(article);
                        }
                        //subarticle .x00
                        int numSubArticlex00 = (numSubArticle/100)*100;
                        if(numSubArticlex00!=0 && numSubArticle!=numSubArticlex00)
                        {
                            article = new SoumArticles(((numArticle*1000) + numSubArticlex00));                               
                            String check = new String(article.getArticle());
                            if(listArticles.stream().anyMatch(a -> a.getArticle().equals(check)))
                                listArticles.add(article);
                        }
                        
                        //Add article
                        //article .00x
                        int numArticle00x = numArticle*1000;
                        article = new SoumArticles(numArticle00x);
                        String check1 = new String(article.getArticle());
                        if(listArticles.stream().anyMatch(a -> a.getArticle().equals(check1)))                         
                            listArticles.add(article);
                        //article .0x0
                        int numArticle0x0 = (numArticle/10)*10;
                        if(numArticle0x0!=0 && numArticle!=numArticle0x0)
                        {
                            article = new SoumArticles(numArticle0x0 * 1000);
                            String check = new String(article.getArticle());
                            if(listArticles.stream().anyMatch(a -> a.getArticle().equals(check))) 
                                listArticles.add(new SoumArticles(numArticle0x0 * 1000));
                        }
                        //article .x00
                        int numArticlex00 = (numArticle/100)*100;
                        if(numArticlex00!=0 && numArticle!=numArticlex00)  
                        {
                            article = new SoumArticles(numArticlex00 * 1000);
                            String check = new String(article.getArticle());
                            if(listArticles.stream().anyMatch(a -> a.getArticle().equals(check))) 
                                listArticles.add(new SoumArticles(numArticlex00 * 1000));
                        }
                        
                        //Create article
                        article = new SoumArticles(rs.getString("z03"));
                        lastVarString = new String();                        
                    }
                    //Add to last article
                    else
                    {
                        var = new Integer(rs.getString("z04"));

                        if(!lastVarString.equals(rs.getString("z04")))
                            article.addDesc(catalog.getArticleFromArticleSubVar(numArticle, numSubArticle, var));
                        
                        article.addDesc(rs.getString("z23").trim() + "\n");
                        article.addQuantite(rs.getString("z15"));
                        article.addUm(rs.getString("z16"));
                        article.addPrixSoum(rs.getString("z19"));
                        
                        lastVarString = rs.getString("z04");
                    }
                }
                
                listArticles.add(article);
        }
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }

        if(!this.listArticles.isEmpty())
        {
            System.out.println("[ V ] Size of listArticles : " + this.listArticles.size());
            System.out.println("[ V ] Size of workListArticles : " + this.workListArticles.size());
        }
        else
            System.out.println("[ X ] list is empty...");          
    }
    

}*/