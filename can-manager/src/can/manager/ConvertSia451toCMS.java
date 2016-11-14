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
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ConvertSia451toCMS {

    private final Path siaFilePath, cmsFilePath;
    private String lineTmp;
    private Connection conn = null;
    private final String siaFile;
    private final String cmsFile;
    private Stage dialogStage;    
    
    public ConvertSia451toCMS(String siaFile, String cmsFile, Stage dialogStage) {
        siaFilePath = Paths.get(siaFile);
        cmsFilePath = Paths.get(cmsFile);
        this.siaFile = siaFile;
        this.cmsFile = cmsFile;
        this.dialogStage = dialogStage;
        
        if(checkPathSia())
            if(connect())
                if(createNewTable())
                    addRecordsToTable();
    }   
  
    private boolean checkPathSia() {
        String siaFileName;
        
        try{
            if(Files.exists(siaFilePath)){
                String nameFile = siaFilePath.getFileName().toString();
                String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);
                siaFileName =  nameFile.substring(0, nameFile.lastIndexOf('.'));
                Long sizeFile = Files.size(siaFilePath);
                System.out.println("[ V ] File sia451 exist, name file \"" + siaFileName + "\", extension \"" + extensionFile.toLowerCase() + ", size file " + sizeFile + "byte");
                if(extensionFile.toLowerCase().equals("01s")) {
                    return true;
                }

                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Convertir");
                    alert.setHeaderText(null);
                    alert.setContentText(siaFile + "\nLe fichier n'est pas de type *.01S\nChoisissez un fichier de type *.01S");
                    alert.showAndWait();
                }
            }
        }
        catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(siaFile + "\nFichier introuvable.\nVérifiez le nom du fichier et réessayer.");
                alert.showAndWait();
        }
        
        return false;
    }
    
    private boolean connect()
    {
        if(!Files.exists(cmsFilePath)) {
            System.out.println("[ V ] File sql not exist");

            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + cmsFilePath);
                System.out.println("[ V ] Creation AND connection to file " + cmsFilePath);
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                alert.showAndWait();
            }
        }

        else {
            System.out.println("[ X ] File .db alreally exist");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Convertir");
            alert.setHeaderText(null);
            alert.setContentText(cmsFile + "\nFichier sql déjà existant.\nVeuillez choisir un autre nom de fichier et réessayer.");
            alert.showAndWait();
        }        
        
        return false;
    }       

    private boolean createNewTable() {
        int i=0;
        String sql = "CREATE TABLE IF NOT EXISTS 'records' (\n"
            + "`ID` INTEGER PRIMARY KEY AUTOINCREMENT,\n";
        
        for(i=1; i<24; i++)
        {
            if(i<10)
                sql += "`z0" + i + "`	TEXT,\n";
            else
                sql += "`z" + i + "`	TEXT,\n";
        }
        
        sql += "`z24`	TEXT);";
        
        try (Statement stmt  = conn.createStatement();) {
            stmt.execute(sql);
            System.out.println("[ V ] A new table \"records\" has been created");
            return true;

            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Convertir");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                alert.showAndWait();
        }   
        return false;
    }

    private void addRecordsToTable() {
        Charset stringCharset = Charset.forName("IBM437");


        try {
            BufferedReader in = Files.newBufferedReader(siaFilePath, stringCharset);
            String line=null;

                try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("BEGIN;");

                while ((line = in.readLine()) != null) {
                    int i=0;
                    for(i=line.length(); i<256; i++)
                        line += " ";
                    lineTmp = line;
                    String sqlInsertInto = "INSERT INTO records (";
                    for(i=1; i<24; i++)
                    {
                        if(i<10)
                            sqlInsertInto += "z0" + i + ",";
                        else
                            sqlInsertInto += "z" + i + ",";
                    }
                    sqlInsertInto += "z24) VALUES (";
                    for(i=1; i<24; i++)
                        sqlInsertInto += "?, ";
                    sqlInsertInto += "?)";

                    PreparedStatement stmtPrepared = conn.prepareStatement(sqlInsertInto);                
                    
                    stmtPrepared.setString(1, stringFromLine(0, 1));
                    stmtPrepared.setString(2, stringFromLine(1, 6));
                    stmtPrepared.setString(3, stringFromLine(7, 6));
                    stmtPrepared.setString(4, stringFromLine(13, 2));
                    stmtPrepared.setString(5, stringFromLine(15, 2));
                    stmtPrepared.setString(6, stringFromLine(17, 6));
                    stmtPrepared.setString(7, stringFromLine(23, 6));
                    stmtPrepared.setString(8, stringFromLine(29, 3));
                    stmtPrepared.setString(9, stringFromLine(32, 3));
                    stmtPrepared.setString(10, stringFromLine(35, 6));
                    stmtPrepared.setString(11, stringFromLine(41, 1));
                    stmtPrepared.setString(12, stringFromLine(42, 1));
                    stmtPrepared.setString(13, stringFromLine(43, 1));
                    stmtPrepared.setString(14, stringFromLine(44, 1));
                    stmtPrepared.setString(15, stringFromLine(45, 13));
                    stmtPrepared.setString(16, stringFromLine(58, 2));
                    stmtPrepared.setString(17, stringFromLine(60, 1));
                    stmtPrepared.setString(18, stringFromLine(61, 1));
                    stmtPrepared.setString(19, stringFromLine(62, 12));
                    stmtPrepared.setString(20, stringFromLine(74, 7));
                    stmtPrepared.setString(21, stringFromLine(81, 5));
                    stmtPrepared.setString(22, stringFromLine(86, 6));
                    stmtPrepared.setString(23, stringFromLine(92, 30));
                    stmtPrepared.setString(24, stringFromLine(122, 133));

                    stmtPrepared.executeUpdate();           
                    }
                    stat.executeUpdate("COMMIT;");
                    System.out.println("[ V ] Table \"records\" is populated");

                    dialogStage.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Convertir");
                    alert.setHeaderText(null);
                    alert.setContentText("Le fichier a été converti avec succès.");
                    alert.showAndWait();                       
                }
                catch(SQLException e){
                    System.out.println("[ X ] " + e.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Convertir");
                    alert.setHeaderText(null);
                    alert.setContentText(e.getMessage() + "\nErreur de type SQL.\nVeuillez choisir un autre nom de fichier et réessayer.");
                    alert.showAndWait();
                }
        }
        catch(IOException e){
            System.out.println("[ X ] " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Convertir");
            alert.setHeaderText(null);
            alert.setContentText(siaFilePath.toString() + "\nErreur lors de l'ouverture du fichier\nVeuillez choisir un autre nom de fichier et réessayer.\nErreur : " + e.getMessage());
            alert.showAndWait();
        }    
    }

    private String stringFromLine(int i, int i0) {
        String lineTmpLocal = new String(lineTmp.toCharArray(), i, i0).trim();
        if(lineTmpLocal.isEmpty())
            return "";
        else
            return lineTmpLocal;
    }
}
