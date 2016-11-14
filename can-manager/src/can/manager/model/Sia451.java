package can.manager.model;

import can.manager.MainApp;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class Sia451 {    

    private final Path pathFileCms;
    private Connection conn = null;
    private final ObservableList<TitleSia451> list = FXCollections.observableArrayList();
    
    public Sia451(Path pathFileCms) {
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
                    TitleSia451 title = new TitleSia451(rs.getString("z02"));
                    list.add(title);
                }
        }
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }

        if(!this.list.isEmpty())
        {
            System.out.println("[ V ] Size of list : " + this.list.size());
            for(TitleSia451 temp : list)
                    System.out.println(temp.getNumCan() + " - " + temp.getYearCan() + " " + temp.getNomCan());     
        }
        else
            System.out.println("[ X ] list is empty...");
    }
    
    public ObservableList<TitleSia451> getTitlesCan(){
        return this.list;
    }
}