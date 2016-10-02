/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package can.manager.view;

import can.manager.MainApp;
import can.manager.model.Article;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CatalogViewerController  {

    @FXML
    private TableView<Article> articleTable;
    @FXML
    private TableColumn<Article, Integer> numberColumn;
    @FXML
    private TableColumn<Article, Integer> subNumberColumn;
    @FXML
    private TableColumn<Article, String> textColumn;
    
    private MainApp mainApp;
    
    public CatalogViewerController(){
    }
    
    private void initilaize(){
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        subNumberColumn.setCellValueFactory(cellData -> cellData.getValue().subNumberProperty().asObject());
        textColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
    }
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
        articleTable.setItems(mainApp.getArticleData());
    }   
}
