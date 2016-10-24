package can.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ExportDBFtoSQLite {
    
    private String fileName, crbFile, sqlFile;
    private final Path crbFilePath, sqlFilePath;
    private Connection conn = null;  
    private Stage dialogStage;

    public ExportDBFtoSQLite(String crbFile, String sqlFile, Stage dialogStage) {
        crbFilePath = Paths.get(crbFile);
        sqlFilePath = Paths.get(sqlFile);
        this.crbFile = crbFile;
        this.sqlFile = sqlFile;
        this.dialogStage = dialogStage;
        
        if(checkPathCRB())
            if(connect())
                if(createNewTable())
                    addArticlesOnTable();
    }
    
    private boolean checkPathCRB() {   
        
        try{
            if(Files.exists(crbFilePath)) {
                String nameFile = crbFilePath.getFileName().toString();
                String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);
                this.fileName =  nameFile.substring(0, nameFile.lastIndexOf('.'));
                Long sizeFile = Files.size(crbFilePath);
                System.out.println("[ V ] File DBF exist, name file \"" + this.fileName + "\", extension \"" + extensionFile.toLowerCase() + ", size file " + sizeFile + "byte");

                if(extensionFile.toLowerCase().equals("dbf")) {
                    return true;
                }

                else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Convertir");
                    alert.setHeaderText(null);
                    alert.setContentText(crbFile + "\nLe fichier n'est pas de type *.dbf\nChoisissez un fichier de type *.dbf");
                    alert.showAndWait();
                }
            }

            else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(crbFile + "\nFichier introuvable.\nVérifiez le nom du fichier et réessayer.");
                alert.showAndWait();
            }

            return false;
        } catch (IOException e) {
            System.out.println("[ X ] " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Convertir");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage() + "\nErreur de fichier.\nVeuillez choisir un autre nom de fichier et réessayer.");
            alert.showAndWait();
        }    
        return false;
    }

    //SQL connexion
    //Creating file .db + connection sql
    private boolean connect()
    {
        if(!Files.exists(sqlFilePath)) {
            System.out.println("[ V ] File sql not exist");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + sqlFilePath);
                System.out.println("[ V ] Creation AND connection to file " + this.sqlFilePath);
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                alert.showAndWait();
            }
        }

        else {
            System.out.println("[ X ] File .db alreally exist");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Convertir");
            alert.setHeaderText(null);
            alert.setContentText(sqlFile + "\nFichier sql déjà existant.\nVeuillez choisir un autre nom de fichier et réessayer.");
            alert.showAndWait();
        }        
        
        return false;
    }   
    

    private boolean createNewTable() {
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
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                alert.showAndWait();
        }   
        return false;
    }
    
    private void addArticlesOnTable() {
        Charset stringCharset = Charset.forName("IBM437");

        try {
            BufferedReader in = Files.newBufferedReader(crbFilePath, stringCharset);
            String line1=null;
            String line2=null;                       
            int nbrChar=78;

            line1=in.readLine();
            line2=in.readLine();

            List<String> liste = java.util.Arrays.asList(line2.split("(?<=\\G.{"+nbrChar+"})"));    

            Iterator<String> iterator = liste.iterator(); 

            try {
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
       
                dialogStage.close();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText("Le fichier a été converti avec succès.");
                alert.showAndWait();                
            } catch(SQLException e){
                System.out.println("[ X ] " + e.getMessage());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                alert.showAndWait();
            }
        } catch(IOException e){
            System.out.println("[ X ] " + e.getMessage());
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Convertir");
            alert.setHeaderText(null);
            alert.setContentText(crbFilePath.toString() + "\nErreur lors de l'ouverture du fichier\nVeuillez choisir un autre nom de fichier et réessayer.\nErreur : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
