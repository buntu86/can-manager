/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package can.manager.data;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Adrien
 */
public class QuerySQL {
    
    private String fileName;
    private Connection conn = null;  
    
    public QuerySQL(String fileName) throws SQLException{
        this.fileName = fileName;
        connect();
        //getAllTitleChapter();
        getDescribChapter(100);
    }
    
    public void getAllTitleChapter() throws SQLException
    {
        String sql = "SELECT * FROM CAN";
        int lastChapter=1;
        
        try(
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
            
            while(rs.next()){
                if((rs.getInt("position")%100)<1)
                {
                    if(lastChapter != rs.getInt("position"))
                        System.out.println(rs.getInt("position") + "\t" + rs.getString("text"));
                    lastChapter=rs.getInt("position");
                }
                
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void getDescribChapter(int chapter)
    {
        String sql = "SELECT * FROM CAN WHERE position=?";
        
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
