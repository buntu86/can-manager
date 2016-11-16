package can.manager.view;

import can.manager.MainApp;
import can.manager.model.TitleSia451;
import can.manager.data.Config;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Dialog_OpenSoum_Controller {

    private MainApp mainApp;
    private Stage dialogStage;
    private Path pathFileCms;
    private final Config config = new Config();

    @FXML
    private TextField cmsFile;
    
    @FXML
    private TableView<TitleSia451> tableTitles;
    @FXML
    private TableColumn<TitleSia451, ImageView> etatColumn;
    @FXML
    private TableColumn<TitleSia451, Integer> numCatalogColumn;
    @FXML
    private TableColumn<TitleSia451, Integer> yearCatalogColumn;
    @FXML
    private TableColumn<TitleSia451, String> nomCatalogColumn; 
    @FXML
    private TableColumn<TitleSia451, Button> buttonConvertColumn;
    
    @FXML
    public void initialize() {
        tableTitles.setVisible(false);      
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etatCan"));
        buttonConvertColumn.setCellValueFactory(new PropertyValueFactory<>("buttonConvert"));
        numCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().numCanProperty().asObject());
        yearCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().yearCanProperty().asObject());
        nomCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().nomCanProperty());
    }
    
    @FXML
    private void handleParcourirSoumission() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CanManagerSoum .cms", "*.cms")
        );       

        fileChooser.setInitialDirectory(config.getCatalogDirectory().toFile());
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null)
        {   
            setPathFileCms(selectedFile);
            System.out.println("Fichier selectionné : " + selectedFile.getPath());
            updateListCan();
            tableTitles.setVisible(true);
            tableTitles.setItems(mainApp.getTitleCan());
        }   
        
        else
            System.out.println("Selection du fichier annulé");
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    @FXML
    private void handleShowSoumissionViewer(){
        if(Files.exists(Paths.get(cmsFile.getText())))
        {
            dialogStage.close();
            this.mainApp.showSoumissionViewer();
        }
        
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Soumission");
            alert.setHeaderText(null);
            alert.setContentText(cmsFile.getText() + "\nLe fichier de soumission est introuvable.");
            alert.showAndWait();        
        }   
    }

    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }    

    private void setPathFileCms(File path){
        this.pathFileCms = path.toPath();
        this.cmsFile.setText(path.getPath());        
    }

    private void updateListCan() {
        mainApp.setSia451(pathFileCms);
    }
}
