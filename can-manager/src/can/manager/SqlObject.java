package can.manager;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlObject {
    
    private Connection conn = null;
    
    SqlObject(String path) throws SQLException
    {
        String name = Paths.get(path).getFileName().toString();
        name = name.substring(0, name.lastIndexOf('.'));
        
        System.out.println("** Connection DB **");
        connect(name);

        System.out.println("\n** Connection TABLE **");        
        this.createNewTable(name);
    }
    
    public void selectAll()
    {
        String sql = "SELECT * FROM orders";
        
        try (
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("OrderID") +  "\t" + 
                                   rs.getString("CustomerID") + "\t" +
                                   rs.getDouble("OrderDate"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private Connection connect(String name)
    {
        String url = "jdbc:sqlite:" + System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + name + (".db");

        try {
            conn = DriverManager.getConnection(url);
            System.out.println("\tConnection OR creation file ./user/Desktop/" + name + ".db");            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    private void createNewTable(String name) {
        //CHECK IF THE TABLE name exist
        String sqlCheckTable = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+name+"'";        
        try {
            // create a new table
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCheckTable);
            if(!rs.next())
            {
                String sql = "CREATE TABLE IF NOT EXISTS '" + name + "' (\n"
                    + "`ID` INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "`position`	INTEGER,\n"
                    + "`sousPosition`	INTEGER,\n"
                    + "`variable`	INTEGER,\n"
                    + "`ligne`	INTEGER,\n"
                    + "`alternative`	TEXT,\n"
                    + "`unite`	TEXT,\n"
                    + "`publication`	INTEGER,\n"
                    + "`debut`	INTEGER,\n"
                    + "`texte`	TEXT\n"
                    + ");";
                try (Statement stmt2  = conn.createStatement();) {
                    // create a new table
                    stmt2.execute(sql);
                    System.out.println("\t[ V ] A new table \"" + name + "\" has been created");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }            
            }
            else
                System.out.println("\t[ X ] Table \"" + name + "\" alrealy exist");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

