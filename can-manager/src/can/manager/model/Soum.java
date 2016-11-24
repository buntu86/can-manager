package can.manager.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Soum {    

    private final Path pathFileCms;
    private Connection conn = null;
    private final ObservableList<SoumTitles> listTitles = FXCollections.observableArrayList();
    private final ObservableList<SoumArticles> listArticles = FXCollections.observableArrayList();
    private int numCanSelected;
    
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
    
    private void setTitlesCan() {
        String sql = "SELECT DISTINCT z02 FROM records WHERE z01='G' AND LENGTH(z02) = 6";

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
    
    public ObservableList<SoumTitles> getTitlesCan(){
        return this.listTitles;
    }

    public ObservableList<SoumArticles> getArticles(){
        return this.listArticles;
    }
    
    public void setIndexTabSelected(int index){
        SoumTitles title = this.listTitles.stream()
                .filter(x -> index == x.getIndex())
                .findAny()
                .orElse(null);
        
        this.numCanSelected = title.getNumCan();
        this.setArticlesFromCan(this.numCanSelected);
    }

    private void setArticlesFromCan(int can) {
        SoumArticles article = null;
        String sql = "SELECT * FROM records WHERE z01='G' AND LENGTH(z02) = 3 AND z02 = " + can;
        System.out.println("setArticlesCan : " + sql);

        try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                listArticles.clear();
                
                while(rs.next())
                {
                    if(article!=null && rs.getString("z03").equals(article.getArticle()))
                    {
                        article.addDesc(rs.getString("z23"));
                        System.out.println("-- article addDesc : " + article.getArticle() + " " + rs.getString("z23"));
                    }
                    else{
                        if(article!=null)
                            listArticles.add(article);
                        article = new SoumArticles(
                            rs.getString("z03"), 
                            rs.getString("z23"), 
                            rs.getString("z15"), 
                            rs.getString("z16"), 
                            rs.getString("z19"));
                            }
                }
        }
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }

        if(!this.listArticles.isEmpty())
        {
            System.out.println("[ V ] Size of listArticles : " + this.listArticles.size());
        }
        else
            System.out.println("[ X ] list is empty...");          
    }
    
    public int getNumCanSelected(){
        return this.numCanSelected;
    }
}