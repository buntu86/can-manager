package can.manager.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import can.manager.MainApp;
import can.manager.model.Article;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CatalogViewerController {
    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<Article, Integer> numberColumn;
    @FXML
    private TableColumn<Article, Integer> subNumberColumn;
    @FXML
    private TableColumn<Article, String> textColumn;
    @FXML
    private Label position;
    @FXML
    private Label subPosition;
    
    @FXML
    private TreeView<Article> rootTree;
    
    private MainApp mainApp;
    
    public CatalogViewerController() {
    }
    
    @FXML
    private void initialize() {
        showArticlesDetails(null);
        rootTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showArticlesDetails(newValue));
        rootTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.getValue().getID()));
    }
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        rootTree.setRoot(mainApp.getTreeCan());
        rootTree.setShowRoot(false);
    }
    
    private void showArticlesDetails(TreeItem article) {
        if(article != null){
            position.setText(String.valueOf(article.getValue()));
            //subPosition.setText(String.valueOf(article.getPosition()));
        }
        else{
            position.setText("");
            subPosition.setText("");
        }
    }
}