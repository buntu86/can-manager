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
    private TableColumn<SoumCatalog, Integer> totalColumn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        numCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().numCanProperty().asObject());
        yearCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().yearCanProperty().asObject());
        nomCatalogColumn.setCellValueFactory(cellData -> cellData.getValue().nomCanProperty());
        */
        
        
    }    

    void setIdCan(int selectedIndex) {
        System.out.println("[ V ] " + selectedIndex);
        this.selectedIndex = selectedIndex;
    }
}
