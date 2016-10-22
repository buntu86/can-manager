package can.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExportDBFtoSQLite {
    
    private Path path_DBFfile;
    private String fileName;
    private Connection conn = null;  

    public ExportDBFtoSQLite(Path crbFilePath, Path sqlFilePath) throws IOException, InterruptedException, SQLException{
        checkPathCRB(crbFilePath);
        
        /*        if(checkPathDBF(crbFilePath) && connect(sqlFilePath) && createNewTable())
        {
            addArticlesOnTable();        
        }*/
    }


    //Valid if the file exist AND the extension is correct
    private boolean checkPathDBF(Path crbFilePath) throws IOException {
        System.out.println(path_DBFfile.toString());
        
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("File DBF not found");
            alert.setHeaderText(null);
            alert.setContentText("File DBF not found.");

            alert.showAndWait();
            return false;       
        }
    }
    
    //SQL connexion
    //Creating file .db + connection sql
    private boolean connect(Path sqlFilePath)
    {
        if(!Files.exists(sqlFilePath)) {
            System.out.println("[ V ] OK creating file .db");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + sqlFilePath);
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
    
    //Creating table CAN
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

    private void addArticlesOnTable() throws IOException, SQLException {
        Charset stringCharset = Charset.forName("IBM437");

        BufferedReader in = Files.newBufferedReader(path_DBFfile, stringCharset);
        String line1=null;
        String line2=null;                       
        int nbrChar=78;
        
        line1=in.readLine();
        line2=in.readLine();

        List<String> liste = java.util.Arrays.asList(line2.split("(?<=\\G.{"+nbrChar+"})"));    

        Iterator<String> iterator = liste.iterator(); 

        Statement stat = conn.createStatement();
        stat.executeUpdate("BEGIN;");

        while (iterator.hasNext()) {
            String nodes = iterator.next();
            if(nodes.length()==78)
            {
                String sqlInsertInto = ("INSERT INTO CAN (position,subPosition,variable,line,alt,unit,publication,begin,text) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement stmtPrepared = conn.prepareStatement(sqlInsertInto);                
                String position = new String(nodes.toCharArray(), 1, 3);
                String subPosition = new String(nodes.toCharArray(), 4, 2);
                String variable = new String(nodes.toCharArray(), 7, 2);
                String line = new String(nodes.toCharArray(), 9, 2);
                String alt = new String(nodes.toCharArray(), 11, 1);
                String unit = new String(nodes.toCharArray(), 12, 2);
                String publication = new String(nodes.toCharArray(), 14, 2);
                String begin = new String(nodes.toCharArray(), 16, 2);
                String text = new String(nodes.toCharArray(), 18, 60);                        

                stmtPrepared.setString(1, position);
                stmtPrepared.setString(2, subPosition);
                stmtPrepared.setString(3, variable);
                stmtPrepared.setString(4, line);
                stmtPrepared.setString(5, alt);
                stmtPrepared.setString(6, unit);
                stmtPrepared.setString(7, publication);
                stmtPrepared.setString(8, begin);
                stmtPrepared.setString(9, text);

                stmtPrepared.executeUpdate();                
            }
        }
        stat.executeUpdate("COMMIT;");
        System.out.println("[ V ] Table CAN is populated");   
    }

    private boolean checkPathCRB(Path crbFilePath) throws IOException {
        if(Files.exists(crbFilePath)) {
            String nameFile = crbFilePath.getFileName().toString();
            String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);

            if(extensionFile.toLowerCase().equals("dbf")) {
                Long sizeFile = Files.size(crbFilePath);
                System.out.println("[ V ] File DBF exist, name file \"" + this.fileName + "\", extension \"" + extensionFile.toLowerCase() +", size file " + sizeFile + "byte");
                return true;
            }
            
            else {
                System.out.println("[ X ] File is not DBF...");
                return false;
            }
        }
        
        return false;
    }
}
