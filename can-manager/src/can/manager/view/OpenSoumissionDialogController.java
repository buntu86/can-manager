package can.manager.view;

import can.manager.MainApp;
import can.manager.model.CustomImage;
import can.manager.model.TitleSia451;
import java.io.File;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class OpenSoumissionDialogController {

    private MainApp mainApp;
    private Stage dialogStage;
    private Path pathFileCms;

    @FXML
    private TextField cmsFile;
    
    @FXML
    private TableView<TitleSia451> tableTitles;
    @FXML
    private TableColumn<TitleSia451, Image> etatColumn;
    @FXML
    private TableColumn<TitleSia451, Integer> numCatalogColumn;
    @FXML
    private TableColumn<TitleSia451, Integer> yearCatalogColumn;
    @FXML
    private TableColumn<TitleSia451, String> nomCatalogColumn;  
    
    @FXML
    public void initialize() {
        tableTitles.setVisible(false);
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        etatColumn.setCellFactory(param -> new ImageTableCell<>());
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

        fileChooser.setInitialDirectory(mainApp.config.getCatalogDirectory().toFile());
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

    private class ImageTableCell<S> extends TableCell<S, Image> {
        final ImageView imageView = new ImageView();
        
        ImageTableCell(){
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        
        @Override
        protected void updateItem(Image item, boolean empty){
            super.updateItem(item, empty);
            
            if(empty || item==null){
                imageView.setImage(null);
                setText(null);
                setGraphic(null);
            }

            imageView.setImage(item);
            setGraphic(imageView);
        }
    }
}
