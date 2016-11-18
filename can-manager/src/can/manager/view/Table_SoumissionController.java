package can.manager.view;

import can.manager.model.SoumCatalog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Table_SoumissionController implements Initializable {
    private int selectedIndex;

    @FXML
    private TableView<SoumCatalog> table;

    @FXML
    private TableColumn<SoumCatalog, String> articleColumn;

    @FXML
    private TableColumn<SoumCatalog, String> descColumn;
    
    @FXML
    private TableColumn<SoumCatalog, Integer> quantiteColumn;
    
    @FXML
    private TableColumn<SoumCatalog, String> umColumn;
    
    @FXML
    private TableColumn<SoumCatalog, Integer> prixSoumColumn;
    
    @FXML
    private TableColumn<SoumCatalog, Integer> totalSoumColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*articleColumn.setCellValueFactory(cellData -> cellData.getValue().articleProperty());
        descColumn.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        umColumn.setCellValueFactory(cellData -> cellData.getValue().umProperty());
        prixSoumColumn.setCellValueFactory(cellData -> cellData.getValue().prixSoumProperty().asObject());
        totalSoumColumn.setCellValueFactory(cellData -> cellData.getValue().totalSoumProperty().asObject());        */
    }    

    void setIdCan(int selectedIndex) {
        System.out.println("[ V ] " + selectedIndex);
        this.selectedIndex = selectedIndex;
    }
}
