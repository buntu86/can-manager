package can.manager;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlObject {
    
    private Connection conn = null;
    private String pathString = null;
    
    SqlObject(String path) throws SQLException
    {
        //NAME DEFINITION FROM NAME FILE .DBF (NAME = name of dbf file without .dbf extension
        String name = Paths.get(path).getFileName().toString();
        name = name.substring(0, name.lastIndexOf('.'));
        setPath(path);
        
        //CONNECTION DB or CREATION
        System.out.println("** Connection DB **");
        connect(name);

        //CREATION TABLE
        System.out.println("\n** Connection TABLE **");        
        createNewTable();
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
    
    private void createNewTable(){
        try {
            //CHECK IF THE TABLE name exist
            String sqlCheckTable = "SELECT name FROM sqlite_master WHERE type='table' AND name='CAN'";
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCheckTable);
            if(!rs.next())
            {
                //IF NOT TABLE "CAN" CREATION
                String sql = "CREATE TABLE IF NOT EXISTS 'CAN' (\n"
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
                    System.out.println("\t[ V ] A new table \"CAN\" has been created");
                    
                    //ADD NEW ARTICLES FROM .DBF
                    //1° verification if the file exist
                    Path pathFileDBF = Paths.get(getPath());   
                    
                    if(Files.exists(pathFileDBF))
                    {
                        //Long sizeFile = Files.size(pathFileDBF);
                        //System.out.println("\t[ V ] File DBF exist, size file " + sizeFile + "byte");
                        System.out.println("\t[ V ] File DBF exist");
                        Charset stringCharset = Charset.forName("IBM437");
                    }    
                    else
                        System.out.println("\t[ X ] File DBF not exist");
                    
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }            
            }
            else
                //ELSE DO NOTING
                System.out.println("\t[ X ] Table \"CAN\" already exist");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setPath(String path) {
        pathString=path;
    }
    
    private String getPath()
    {
        return this.pathString;
    }
    
}

/*public void selectAll()
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
}*/
