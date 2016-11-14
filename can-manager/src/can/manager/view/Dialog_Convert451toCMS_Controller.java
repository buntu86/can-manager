package can.manager.view;

import can.manager.ConvertSia451toCMS;
import can.manager.MainApp;
import can.manager.data.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Dialog_Convert451toCMS_Controller {

    private Stage dialogStage;
    private MainApp mainApp;
    private Path siaFilePath, cmsFilePath;
    private final Config config = new Config();
    
    @FXML
    private TextField siaFile;
    
    @FXML
    private TextField cmsFile;
    
    @FXML
    public void initialize() {
    }  
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setSiaFile(String file){
        this.siaFile.setText(file);
        this.siaFilePath = Paths.get(file);
    }

    public void setCmsFile(String file){
        this.cmsFile.setText(file);
        this.cmsFilePath = Paths.get(file);
    }
    
    
    @FXML
    private void handleConvert() throws IOException, InterruptedException, SQLException {
        ConvertSia451toCMS run = new ConvertSia451toCMS(siaFile.getText(), cmsFile.getText(), this.dialogStage);
    }
    
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    @FXML
    private void handleParcourirSia() {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SIA451 .01S", "*.01S")
        );
        
        if(config.getCatalogDirectory().toFile().exists())
            fileChooser.setInitialDirectory(config.getCatalogDirectory().toFile());
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {   
            this.setSiaFile(selectedFile.getPath());
            this.setCmsFile(selectedFile.getPath().substring(0, selectedFile.getPath().length()-4) + ".cms");
            
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    }

    @FXML
    private void handleParcourirCms() {       
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CanManagerSoumission .cms", "*.cms")
        );
        
        if(config.getCatalogDirectory().toFile().exists())
            fileChooser.setInitialDirectory(config.getCatalogDirectory().toFile());
        File selectedFile = fileChooser.showSaveDialog(null);

        if(selectedFile != null)
        {   
            this.setCmsFile(selectedFile.getPath());
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    }   
}
