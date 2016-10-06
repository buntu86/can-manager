package can.manager.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import can.manager.MainApp;
import can.manager.model.Article;

public class CatalogViewerController {
    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<Article, Integer> numberColumn;
    @FXML
    private TableColumn<Article, Integer> subNumberColumn;
    @FXML
    private TableColumn<Article, String> textColumn;
    
    private MainApp mainApp;
    
    public CatalogViewerController() {
    }
    
    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().positionProperty().asObject());
        subNumberColumn.setCellValueFactory(cellData -> cellData.getValue().subPositionProperty().asObject());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
    }
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        articleTable.setItems(mainApp.getArticleData());
    }
}