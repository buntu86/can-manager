package can.manager;

import can.manager.model.Catalog;
import can.manager.model.CatalogArticles;
import can.manager.model.Soum;
import can.manager.model.SoumTitles;
import can.manager.view.CatalogViewerController;
import can.manager.view.Dialog_Convert451toCMS_Controller;
import can.manager.view.Dialog_ConvertDBFtoCMC_Controller;
import can.manager.view.Dialog_OpenSoum_Controller;
import can.manager.view.RootLayoutController;
import can.manager.view.Viewer_SoumissionController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;
    private BorderPane rootLayout;
    private final Catalog can = new Catalog();
    private Soum sia451;
    private ObservableList<CatalogArticles> articleFromSubPosition = FXCollections.observableArrayList();
    private AnchorPane catalogViewer, soumViewer;
    private String fileName;
    
    public MainApp() {        
    }
    
    public Catalog getCatalogCAN(){
        return this.can;
    }  
        
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Can-manager");
        initRootLayout();
    }
    
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();          

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public BorderPane getRootLayout() {
        return this.rootLayout;
    }

    public void convert451toCMS() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Dialog_Convert451toCMS.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Convertir fichier Sia451 -> cms");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            Dialog_Convert451toCMS_Controller controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if(event.getCode().equals(KeyCode.ESCAPE))
                    dialogStage.close();
            });
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
    public void showSoumissionViewer() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Viewer_Soumission.fxml"));
            soumViewer = (AnchorPane) loader.load();
            rootLayout.setCenter(soumViewer);

            
            Viewer_SoumissionController controller = loader.getController();
            controller.setMainApp(this);
            controller.showTabPane();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    public void openCatalogViewer(String fileName) {
        can.setMainApp(this);
        can.initialize(fileName);
        Path pathFileName = Paths.get(fileName).getFileName();
        SoumTitles title = new SoumTitles(pathFileName.toString().substring(1, 4) + " 00");
        primaryStage.setTitle(title.getNumCan() + " - " + title.getNomCan() + " / CanManager");
        
        try {            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CatalogViewer.fxml"));
            catalogViewer = (AnchorPane) loader.load();
            rootLayout.setCenter(catalogViewer);
            
            CatalogViewerController controller = loader.getController();
            controller.setMainApp(this);
            controller.openCatalogViewer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showConvertDialog(String fileName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Dialog_ConvertDBFtoCMC.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Convertir fichier crb -> sql");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            Dialog_ConvertDBFtoCMC_Controller controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setCrbFileFromSoum(fileName);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if(event.getCode().equals(KeyCode.ESCAPE))
                    dialogStage.close();
            });
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void openSoumissionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Dialog_OpenSoum.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ouvrir soumission");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            Dialog_OpenSoum_Controller controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if(event.getCode().equals(KeyCode.ESCAPE))
                    dialogStage.close();
            });
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
    public void closeCatalogViewer() {
        rootLayout.getChildren().remove(catalogViewer);
    }

    public void closeSoumViewer() {
        rootLayout.getChildren().remove(soumViewer);
    }
    
    public String getFileName(){
        return this.fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }    
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);       
    }

    public void setSia451(Path cmsFile) {
        this.sia451 = new Soum(cmsFile);
    }
    
    public Soum getSia451(){
        return this.sia451;
    }
    
    public ObservableList<SoumTitles> getTitleCan(){
        return this.sia451.getTitlesCan();
    }
    
    public void setPrimaryTitle(String str){
        primaryStage.setTitle(str);
    }
}