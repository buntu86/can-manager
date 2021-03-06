package can.manager.view;

import can.manager.MainApp;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


public class RootLayoutController implements Initializable {

    private MainApp mainApp;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }    
    
    @FXML
    private void handelOpen() {

        String directoryName = new String(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "canManager");
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("CanManagerCatalog cmc", "*.cmc")
        );
        
        File directory = new File(directoryName);
        if(directory.exists())
            fileChooser.setInitialDirectory(directory);
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null && Files.exists(Paths.get(selectedFile.getPath())))
        {   
            mainApp.openCatalogViewer(selectedFile.getPath());
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    } 
    
    @FXML
    public void handelConvert() throws IOException {
        mainApp.showConvertDialog(null);
        
    }

    @FXML
    public void handelTest() throws IOException, SQLException {
        MainApp main = new MainApp();
        main.showConvertDialog(null);
    }    
    
    @FXML
    private void handelImportSia451() throws IOException {
        mainApp.convert451toCMS();
    }    

    @FXML
    private void handelOpenSoumission() throws IOException {
        mainApp.openSoumissionDialog();
    }    

    @FXML
    private void handleAproposDe() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("CanManager");
        alert.setHeaderText("A propos de");
        alert.setContentText("Auteur: Adrien Pillonel");

        alert.showAndWait();
    }    

    @FXML
    private void handleExit(){
        System.exit(0);
    }

    @FXML
    private void handleCloseCatalog(){
        mainApp.closeCatalogViewer();
        mainApp.setPrimaryTitle("CanManager");
    }

    @FXML
    private void handleCloseSoum(){
        mainApp.closeSoumViewer();
        mainApp.setPrimaryTitle("CanManager");
    }
}
