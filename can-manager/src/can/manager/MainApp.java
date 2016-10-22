package can.manager;

import can.manager.model.CatalogCAN;
import can.manager.model.Article;
import can.manager.view.CatalogViewerController;
import can.manager.view.ConvertDialogController;
import can.manager.view.RootLayoutController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private CatalogCAN can = new CatalogCAN();  
    private ObservableList<Article> articleFromSubPosition = FXCollections.observableArrayList();
    private AnchorPane catalogViewer;
    private String fileName;
    
    public MainApp() throws SQLException {
        //this.setFileName(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "can2.db");
        can.setMainApp(this);
        can.initialize();
    }
    
    public CatalogCAN getCatalogCAN(){
        return this.can;
    }  
        
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Can-manager");
        initRootLayout();
        openCatalogViewer(""); 
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

    public void openCatalogViewer(String fileName) {
        can.initialize(fileName);
        
        try {            
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CatalogViewer.fxml"));
            catalogViewer = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(catalogViewer);
            
            CatalogViewerController controller = loader.getController();
            controller.setMainApp(this);
            controller.openCatalogViewer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showConvertDialog() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ConvertDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Convertir fichier crb -> sql");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            ConvertDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void closeCatalogViewer() {
        rootLayout.getChildren().remove(catalogViewer);
    }
    
    public String getFileName(){
        return this.fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }    
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);       
    }
}