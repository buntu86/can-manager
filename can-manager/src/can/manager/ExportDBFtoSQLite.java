/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package can.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class ExportDBFtoSQLite {
    
    private Path path_DBFfile;
    private String fileName;
    private Connection conn = null;  
    
    //Path pathFileDBF = Paths.get(getPath());  

    ExportDBFtoSQLite(String pathString) throws IOException, InterruptedException{

        path_DBFfile = Paths.get(pathString);

        if(checkPathDBF() && connect() && createNewTable())
        {
            
        
        }
    }

    //Valid if the file exist AND the extension is correct
    private boolean checkPathDBF() throws IOException {
        if(Files.exists(path_DBFfile)) {
            String nameFile = path_DBFfile.getFileName().toString();
            String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);
            this.fileName = nameFile.substring(0, nameFile.lastIndexOf('.'));

            if(extensionFile.toLowerCase().equals("dbf")) {
                Long sizeFile = Files.size(path_DBFfile);
                System.out.println("[ V ] File DBF exist, name file \"" + this.fileName + "\", extension \"" + extensionFile.toLowerCase() +", size file " + sizeFile + "byte");
                return true;
            }
            
            else {
                System.out.println("[ X ] File is not DBF...");
                return false;                           
            }
        }
        
        else {
            System.out.println("[ X ] File DBF not fund...");
            return false;       
        }
    }
    
    //SQL connexion
    //private Connection connect()
    private boolean connect()
    {
        String dbFile = System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + fileName + (".db");
                
        if(!Files.exists(Paths.get(dbFile))) {
            System.out.println("[ V ] OK creating file .db");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
                System.out.println("[ V ] Creation AND connection to file ./user/Desktop/" + fileName + ".db");
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
                return false;
            }
        }
        
        else {
            System.out.println("[ X ] File .db alreally exist");
            return false;
        }
    }    
    
    private boolean createNewTable() throws IOException, InterruptedException{
        String sql = "CREATE TABLE IF NOT EXISTS 'CAN' (\n"
            + "`ID` INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "`position`	INTEGER,\n"
            + "`subPosition`	INTEGER,\n"
            + "`variable`	INTEGER,\n"
            + "`line`	INTEGER,\n"
            + "`alt`	TEXT,\n"
            + "`unit`	TEXT,\n"
            + "`publication`	INTEGER,\n"
            + "`begin`	INTEGER,\n"
            + "`text`	TEXT\n"
            + ");";
        try (Statement stmt  = conn.createStatement();) {
            // create a new table
            stmt.execute(sql);
            System.out.println("[ V ] A new table \"CAN\" has been created");
            return true;

            } catch (SQLException e) {
        System.out.println("[ X ] " + e.getMessage());
        return false;
        }   
    }
}
