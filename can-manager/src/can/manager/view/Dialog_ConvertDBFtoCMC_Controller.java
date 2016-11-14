package can.manager.view;

import can.manager.ConvertCRBtoCMC;
import can.manager.MainApp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialog_ConvertDBFtoCMC_Controller {

    private Stage dialogStage;
    private MainApp mainApp;
    private Path crbFilePath;
    private Path sqlFilePath;
    
    @FXML
    private TextField crbFile;
    
    @FXML
    private TextField sqlFile;
    
    @FXML
    public void initialize() {        
    }  

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setCrbFile(String file){
        this.crbFile.setText(file);
        this.crbFilePath = Paths.get(file);
    }

    public void setSqlFile(String file){
        this.sqlFile.setText(file);
        this.sqlFilePath = Paths.get(file);
    }
    
    @FXML
    private void handleConvert() throws IOException, InterruptedException, SQLException {
        ConvertCRBtoCMC run = new ConvertCRBtoCMC(crbFile.getText(), sqlFile.getText(), this.dialogStage);
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    @FXML
    private void handleParcourirCrb() {
        String directoryName = new String(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "canManager");
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CRB .dbf", "*.dbf")
        );
        
        File directory = new File(directoryName);
        if(directory.exists())
            fileChooser.setInitialDirectory(directory);
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {   
            this.setCrbFile(selectedFile.getPath());
            this.setSqlFile(selectedFile.getPath().substring(0, selectedFile.getPath().length()-4) + ".cmc");
            
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    }

    @FXML
    private void handleParcourirSql() {
        String directoryName = new String(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "canManager");
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CanManagerCatalog .cmc", "*.cmc")
        );
        
        File directory = new File(directoryName);
        if(directory.exists())
            fileChooser.setInitialDirectory(directory);
        File selectedFile = fileChooser.showSaveDialog(null);

        if(selectedFile != null)
        {   
            this.setSqlFile(selectedFile.getPath());
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    }   
}
