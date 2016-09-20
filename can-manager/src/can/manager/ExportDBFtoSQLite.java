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
import java.sql.SQLException;

/**
 *
 * @author Adrien
 */
public class ExportDBFtoSQLite {
    
    private Path path_DBFfile;
    private String fileName;
    private Connection conn = null;    
    
    //Path pathFileDBF = Paths.get(getPath());  

    ExportDBFtoSQLite(String pathString) throws IOException{

        path_DBFfile = Paths.get(pathString);

        if(checkPathDBF() & connect())
        {
            
        
        }
    }

    //Valid if the file exist AND the extension is correct
    private boolean checkPathDBF() throws IOException {
        if(Files.exists(path_DBFfile)){
            String nameFile = path_DBFfile.getFileName().toString();
            String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);
            this.fileName = nameFile.substring(0, nameFile.lastIndexOf('.'));

            if(extensionFile.toLowerCase().equals("dbf")){
                Long sizeFile = Files.size(path_DBFfile);
                System.out.println("[ V ] File DBF exist, name file \"" + this.fileName + "\", extension \"" + extensionFile.toLowerCase() +", size file " + sizeFile + "byte");
                return true;
            }
            
            else{
                System.out.println("[ X ] File is not DBF...");
                return false;                           
            }
        }
        
        else{
            System.out.println("[ X ] File DBF not fund...");
            return false;       
        }
    }
    
    //SQL connexion
    //private Connection connect()
    private boolean connect()
    {
        String dbFile = System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + fileName + (".db");
                
        if(!Files.exists(Paths.get(dbFile)))
        {
            System.out.println("[ V ] OK creating file .db");
            return true;
    /*        String url = "jdbc:sqlite:" + System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + name + (".db");

            try {
                conn = DriverManager.getConnection(url);
                System.out.println("\tConnection OR creation file ./user/Desktop/" + name + ".db");            
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return conn;*/            
        }
        
        else
        {
            System.out.println("[ X ] File .db alreally exist");
            return false;
        }
        
        

    }    
}
