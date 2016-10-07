package can.manager.model;

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
    
    private final String fileName;
    private Connection conn = null;
    
    public CatalogCAN(String fileName) throws SQLException{
        this.fileName = fileName;
        connect();
    }
    
    public ObservableList<Article> getAllChapter()
    {
        ObservableList<Article> articlesAllChapter = FXCollections.observableArrayList();;
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
                        System.out.println(rs.getInt("position") + "\t" + rs.getString("text"));
                        articlesAllChapter.add(new Article(rs.getInt("ID"), rs.getInt("position"), rs.getInt("subPosition"), rs.getInt("variable"), rs.getInt("line"), rs.getString("alt"), rs.getString("unit"), rs.getInt("publication"), rs.getInt("begin"), rs.getString("text")));
                    }    
                    lastChapter=rs.getInt("position");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return articlesAllChapter;
    }
    
    public TreeItem<String> getTreeCan()
    {
        TreeItem<String> treeCan = new TreeItem<> ("root");
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
                        System.out.println(rs.getInt("position") + "\t" + rs.getString("text"));
                        TreeItem<String> chapter = new TreeItem<> (rs.getString("text"));
                        treeCan.getChildren().add(chapter);
                    }    
                    lastChapter=rs.getInt("position");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return treeCan;
    }


    
    public void getDescribChapter(int chapter)
    {
        String sql = "SELECT * FROM CAN WHERE position=" + chapter;
        
        try(
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                System.out.println(rs.getInt("position") + "\t" + rs.getInt("subPosition") + "\t"+ rs.getString("text"));

            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }        
    }
    
    private boolean connect()
    {
        if(Files.exists(Paths.get(fileName))) {
            System.out.println("[ V ] File can.db exist");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                System.out.println("[ V ] Connection to file can.db");
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
}
