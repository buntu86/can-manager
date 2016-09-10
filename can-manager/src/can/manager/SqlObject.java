package can.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class SqlObject {
    
    private Connection conn = null;
    private String pathString = null;
    
    SqlObject(String path) throws SQLException, IOException
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
    
    private void createNewTable() throws IOException{
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
                        Long sizeFile = Files.size(pathFileDBF);
                        System.out.println("\t[ V ] File DBF exist, size file " + sizeFile + "byte");

                        Charset stringCharset = Charset.forName("IBM437");
                        //2° read file
                        BufferedReader in = Files.newBufferedReader(pathFileDBF, stringCharset);
                        String line1=null;
                        String line2=null;                       
                        int nbrChar=78;
                        
                        line1=in.readLine();
                        line2=in.readLine();

                        List<String> liste = java.util.Arrays.asList(line2.split("(?<=\\G.{"+nbrChar+"})"));    

                        Iterator<String> iterator = liste.iterator();   

                        while (iterator.hasNext()) {
                            String nodes = iterator.next();

                            if(nodes.length()==78)
                           {
                               String position = new String(nodes.toCharArray(), 1, 3);
                               String sousPosition = new String(nodes.toCharArray(), 4, 2);
                               String variable = new String(nodes.toCharArray(), 7, 2);
                               String ligne = new String(nodes.toCharArray(), 9, 2);
                               String alternative = new String(nodes.toCharArray(), 11, 1);
                               String unite = new String(nodes.toCharArray(), 12, 2);
                               String publication = new String(nodes.toCharArray(), 14, 2);
                               String debut = new String(nodes.toCharArray(), 16, 2);
                               String texte = new String(nodes.toCharArray(), 18, 60);
                               
                               System.out.println(position + "|" + sousPosition + "|" + variable + "|" + ligne + "|" + alternative + "|" + unite + "|" + publication + "|" + debut + "|" + texte + "|");
                           }
                        }
                        
                        //INSERT INTO `CAN`(`ID`,`position`,`sousPosition`,`variable`,`ligne`,`alternative`,`unite`,`publication`,`debut`,`texte`) VALUES (NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
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
