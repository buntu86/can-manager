package can.manager;

import can.manager.model.CatalogCAN;
import can.manager.model.Article;
import can.manager.view.CatalogViewerController;
import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private final ObservableList<Article> articleData = FXCollections.observableArrayList();
    private final ObservableList<CatalogCAN> catalogCANData = FXCollections.observableArrayList();
    private final String fileName = System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "can.db";;
    
    public MainApp() throws SQLException{
        CatalogCAN can = new CatalogCAN(fileName);
        
        /*for(int i=0; i<catalogCANData.size(); i++) {
            articleData.add(can.get(i));
        }*/
        articleData.add(new Article(0, 900, 100, 0, 0, null, null, 0, 0, "test"));

    }
    
    public ObservableList<Article> getArticleData(){
        return articleData;
    }
    
    public ObservableList<CatalogCAN> getCANData(){
        return catalogCANData;
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Can-manager");

        initRootLayout();

        showCatalogViewer();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showCatalogViewer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CatalogViewer.fxml"));
            AnchorPane catalogViewer = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(catalogViewer);
            
            CatalogViewerController controller = loader.getController();
            controller.setMainApp(this);
            
            for(int i=0; i<articleData.size(); i++) 
            {
                System.out.print(articleData.get(i).getPosition() + " ");
                System.out.print(articleData.get(i).getSubPosition() + " ");
                System.out.println(articleData.get(i).getText());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
        
    }
}