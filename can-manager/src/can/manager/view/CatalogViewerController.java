package can.manager.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import can.manager.MainApp;
import can.manager.model.Article;
import javafx.scene.control.Label;
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
    private Label paragrapheLabel;
    @FXML
    private Label sousParagrapheLabel;
    @FXML
    private Label articleLabel;    

    
    @FXML
    private TreeView<Article> rootTree;
    
    private MainApp mainApp;
    
    public CatalogViewerController() {
    }
    
    @FXML
    private void initialize() {
        rootTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showArticlesDetails(newValue.getValue()));
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty().asObject());
        subNumberColumn.setCellValueFactory(cellData -> cellData.getValue().subPositionProperty().asObject());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        paragrapheLabel.setText("");
        sousParagrapheLabel.setText("");
        articleLabel.setText("");
    }
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }
    
    public void openCatalogViewer(){
        rootTree.setRoot(mainApp.getCatalogCAN().getTreeCan());
        rootTree.setShowRoot(false);
    }
    
    private void showArticlesDetails(Article article) {
        if(article != null){
            paragrapheLabel.setText(mainApp.getCatalogCAN().getTitleParagraphe(article));
            
            if((article.getPosition()/10)%10>0)
                sousParagrapheLabel.setText(mainApp.getCatalogCAN().getTitleSousParagraphe(article));
            else
                sousParagrapheLabel.setText("");

            if(article.getPosition()%10>0)
            {    
                articleTable.setItems(mainApp.getCatalogCAN().getSubPositionFromPosition(article));
                articleLabel.setText(mainApp.getCatalogCAN().getTitleArticle(article));
            }
            else
            {
                articleTable.setItems(null);
                articleLabel.setText("");
            }
        }     
        else{
            paragrapheLabel.setText("");
            sousParagrapheLabel.setText("");
            articleLabel.setText("");
        }
    }
    
    @FXML
    private void handleClose() {
        mainApp.closeCatalogViewer();
        System.out.println("close");
    }
}