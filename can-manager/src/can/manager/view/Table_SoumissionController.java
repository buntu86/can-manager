package can.manager.view;

import can.manager.MainApp;
import can.manager.model.SoumArticles;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Table_SoumissionController implements Initializable {
    private int selectedIndex;

    @FXML
    private TableView<SoumArticles> table;

    @FXML
    private TableColumn<SoumArticles, String> articleColumn;

    @FXML
    private TableColumn<SoumArticles, String> descColumn;
    
    @FXML
    private TableColumn<SoumArticles, Float> quantiteColumn;
    
    @FXML
    private TableColumn<SoumArticles, String> umColumn;
    
    @FXML
    private TableColumn<SoumArticles, Float> prixSoumColumn;
    
    @FXML
    private TableColumn<SoumArticles, Float> totalSoumColumn;
    
    private MainApp mainApp;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        articleColumn.setCellValueFactory(cellData -> cellData.getValue().articleProperty());
        descColumn.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        umColumn.setCellValueFactory(cellData -> cellData.getValue().umProperty());
        prixSoumColumn.setCellValueFactory(cellData -> cellData.getValue().prixSoumProperty().asObject());
        totalSoumColumn.setCellValueFactory(cellData -> cellData.getValue().totalSoumProperty().asObject());
    }
    
    void setIndexTab(int selectedIndex) {
        System.out.println("[ V ] " + selectedIndex);
        this.selectedIndex = selectedIndex;
        
        if(!mainApp.getSia451().getArticles().isEmpty())
            table.setItems(mainApp.getSia451().getArticles());
        else
            table.setItems(null);
    }

    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
