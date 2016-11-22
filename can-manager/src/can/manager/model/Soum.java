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
    
    public void setIndexTabSelected(int index){
        SoumTitles title = this.listTitles.stream()
                .filter(x -> index == x.getIndex())
                .findAny()
                .orElse(null);
        
        this.numCanSelected = title.getNumCan();
        this.setArticlesFromCan(this.numCanSelected);
    }

    private void setArticlesFromCan(int can) {
        
        String sql = "SELECT DISTINCT z02 FROM records WHERE z01='G' AND LENGTH(z02) = 3 AND z02 = " + can;
        System.out.println("setArticlesCan : " + sql);
/*
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
        */        
    }
    
    public int getNumCanSelected(){
        return this.numCanSelected;
    }
}