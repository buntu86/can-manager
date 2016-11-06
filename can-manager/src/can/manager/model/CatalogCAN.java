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

public class CatalogCAN {
    private Connection conn = null;
    private MainApp mainApp;
    private String fileName;
    
    public CatalogCAN() throws SQLException{
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
    
    public String getTitleParagraphe(Article article){
        int paragraphe = (article.getPosition()/100) * 100;
        return getTitle(paragraphe);
    }

    public String getTitleSousParagraphe(Article article){
        int sousParagraphe = (article.getPosition()/10) * 10;
        return getTitle(sousParagraphe);
    }    

    public String getTitleArticle(Article article){
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
    
    public ObservableList<Article> getSubPositionFromPosition(Article article){
        ObservableList<Article> subPositionsFromPosition = FXCollections.observableArrayList();
        String sql = "SELECT * FROM CAN WHERE position=" + article.getPosition();
        Article articleTmp = new Article();
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
                    articleTmp = new Article();                    
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
    
    public ObservableList<Article> getAllParagraphe()
    {
        ObservableList<Article> articlesAllParagraphe = FXCollections.observableArrayList();;
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

    public ObservableList<Article> getSousParagraphe(int paragraphe)
    {
        ObservableList<Article> articlesSousParagraphe = FXCollections.observableArrayList();;
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

    public ObservableList<Article> getArticle(int sousParagraphe)
    {
        ObservableList<Article> articlesArticle = FXCollections.observableArrayList();;
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
    
    
    public Article getArticleFromPosition(int position, int subPosition){
        String sql = "SELECT * FROM CAN WHERE position=" + position + " AND subPosition=" + subPosition;
        Article article = new Article();
        int lastLine=0;
        
        try(
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            article = new Article(rs.getInt("ID"), rs.getInt("position"), rs.getInt("subPosition"), rs.getInt("variable"), rs.getInt("line"), rs.getString("alt"), rs.getString("unit"), rs.getInt("publication"), rs.getInt("begin"), "");    

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
    
    public TreeItem<Article> getTreeCan()
    {
        TreeItem<Article> treeCan = new TreeItem<> ();
        treeCan.setExpanded(true);
        
        for(Article artLev01 : getAllParagraphe())
        {
            TreeItem<Article> paragraphe = new TreeItem<> (artLev01);
            
            for(Article artLev02 : getSousParagraphe(artLev01.getPosition()))
            {
                TreeItem<Article> sousParagraphe = new TreeItem<> (artLev02);
                for(Article artLev03 : getArticle(artLev02.getPosition()))
                {
                    TreeItem<Article> article = new TreeItem<> (artLev03);
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