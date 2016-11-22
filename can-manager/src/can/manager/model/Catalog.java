package can.manager.model;

import can.manager.MainApp;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class Catalog {
    private Connection conn = null;
    private MainApp mainApp;
    private String fileName;
    
    public Catalog() throws SQLException{
        setFileName(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "can2.db");
    }

    public void initialize(){
        connect();
    }
    
    public void initialize(String fileName){
        setFileName(fileName);       
        connect();
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }        
    
    private boolean connect()
    {
        if(Files.exists(Paths.get(fileName))) {
            System.out.println("[ V ] File can.db exist");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                System.out.println("[ V ] Connection to file " + fileName);
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
                return false;
            }
        }
        else {
            System.out.println("[ X ] File can.db don't exist");
            return false;
        }        
    }      
    
    public String getTitleParagraphe(CatalogArticles article){
        int paragraphe = (article.getPosition()/100) * 100;
        return getTitle(paragraphe);
    }

    public String getTitleSousParagraphe(CatalogArticles article){
        int sousParagraphe = (article.getPosition()/10) * 10;
        return getTitle(sousParagraphe);
    }    

    public String getTitleArticle(CatalogArticles article){
        int articleTitle = article.getPosition();
        return getTitle(articleTitle);
    }    
    
    public String getTitle(int position){
        String title = new String();
        String add = new String();
        String sql = "SELECT * FROM CAN WHERE position=" + position;
        int lastLine=0;
        
        try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next() && !rs.getString("text").startsWith("-----") && rs.getInt("line")>lastLine)
                {
                    String text = rs.getString("text").replaceAll("\\s+$", "");

                    if(title.length()>0)
                    {
                        if(title.substring(title.length()-1).equals("-"))
                            title=title.replaceAll("-+$", "");
                        else
                            title=title + " ";
                    }

                    title = title+text;
                    lastLine = rs.getInt("line");
                }
                
                if(rs.getInt("position")<100)
                    if(rs.getInt("position")<10)
                        add = "00";
                    else
                        add = "0";
                
                title = add + rs.getInt("position") + " - " + title;
            } 
        catch(SQLException e){
            System.out.println(e.getMessage());
        }        
        return title;        
    }
    
    public ObservableList<CatalogArticles> getSubPositionFromPosition(CatalogArticles article){
        ObservableList<CatalogArticles> subPositionsFromPosition = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CAN WHERE position=" + article.getPosition();
        CatalogArticles articleTmp = new CatalogArticles();
        try(
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                System.out.println(articleTmp.getSubPosition() + " " + rs.getInt("subPosition"));
                if(rs.getInt("subPosition") == articleTmp.getSubPosition() && rs.getInt("variable") == articleTmp.getVariable())
                {
                    articleTmp.updateArticleText(rs.getString("text"));                
                }
                else
                {
                    articleTmp = new CatalogArticles();                    
                    articleTmp.setArticle(rs.getInt("ID"), rs.getInt("position"), rs.getInt("subPosition"), rs.getInt("variable"), rs.getInt("line"), rs.getString("alt"), rs.getString("unit"), rs.getInt("publication"), rs.getInt("begin"), rs.getString("text"));                                    
                    subPositionsFromPosition.add(articleTmp);                
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return subPositionsFromPosition;        
    }
    
    public ObservableList<CatalogArticles> getAllParagraphe()
    {
        ObservableList<CatalogArticles> articlesAllParagraphe = FXCollections.observableArrayList();;
        String sql = "SELECT * FROM CAN";
        int lastChapter=1;
        
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                if((rs.getInt("position")%100)<1)
                {
                    if(lastChapter != rs.getInt("position"))
                    {
                        articlesAllParagraphe.add(getArticleFromPosition(rs.getInt("position"), 0));
                    }    
                    lastChapter=rs.getInt("position");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return articlesAllParagraphe;
    }

    public ObservableList<CatalogArticles> getSousParagraphe(int paragraphe)
    {
        ObservableList<CatalogArticles> articlesSousParagraphe = FXCollections.observableArrayList();;
        String sql = "SELECT * FROM CAN WHERE position>" + paragraphe + " AND position<=" + (paragraphe+99);
        int lastSousParagraphe=1;
        
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                if((rs.getInt("position")%10)<1)
                {
                    if(lastSousParagraphe != rs.getInt("position"))
                    {
                        articlesSousParagraphe.add(getArticleFromPosition(rs.getInt("position"), 0));
                    }    
                    lastSousParagraphe=rs.getInt("position");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return articlesSousParagraphe;
    }

    public ObservableList<CatalogArticles> getArticle(int sousParagraphe)
    {
        ObservableList<CatalogArticles> articlesArticle = FXCollections.observableArrayList();;
        String sql = "SELECT * FROM CAN WHERE position>" + sousParagraphe + " AND position<=" + (sousParagraphe+9);
        int lastArticle=1;
        
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                if(lastArticle != rs.getInt("position"))
                {
                    articlesArticle.add(getArticleFromPosition(rs.getInt("position"), 0));
                }    
                lastArticle=rs.getInt("position");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return articlesArticle;
    }    
    
    
    public CatalogArticles getArticleFromPosition(int position, int subPosition){
        String sql = "SELECT * FROM CAN WHERE position=" + position + " AND subPosition=" + subPosition;
        CatalogArticles article = new CatalogArticles();
        int lastLine=0;
        
        try(
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            article = new CatalogArticles(rs.getInt("ID"), rs.getInt("position"), rs.getInt("subPosition"), rs.getInt("variable"), rs.getInt("line"), rs.getString("alt"), rs.getString("unit"), rs.getInt("publication"), rs.getInt("begin"), "");    

            while(rs.next() && !rs.getString("text").startsWith("-----") && rs.getInt("line")>lastLine){
                String text = rs.getString("text").replaceAll("\\s+$", "");
                
                if(article.getText().length()>0)
                {
                    if(article.getText().substring(article.getText().length()-1).equals("-"))
                        article.setText(article.getText().replaceAll("-+$", ""));
                    else
                        article.setText(article.getText() + " ");
                }

                article.setText(article.getText() + text);
                lastLine = article.getLine();
            }
        }

        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return article;
    }
    
    public TreeItem<CatalogArticles> getTreeCan()
    {
        TreeItem<CatalogArticles> treeCan = new TreeItem<> ();
        treeCan.setExpanded(true);
        
        for(CatalogArticles artLev01 : getAllParagraphe())
        {
            TreeItem<CatalogArticles> paragraphe = new TreeItem<> (artLev01);
            
            for(CatalogArticles artLev02 : getSousParagraphe(artLev01.getPosition()))
            {
                TreeItem<CatalogArticles> sousParagraphe = new TreeItem<> (artLev02);
                for(CatalogArticles artLev03 : getArticle(artLev02.getPosition()))
                {
                    TreeItem<CatalogArticles> article = new TreeItem<> (artLev03);
                    sousParagraphe.getChildren().add(article);
                }

                paragraphe.getChildren().add(sousParagraphe);
            }
            
            treeCan.getChildren().add(paragraphe);
        }        

        return treeCan;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }
}