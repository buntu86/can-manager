package can.manager.view;

import can.manager.MainApp;
import can.manager.model.SoumArticles;
import can.manager.model.SoumTitles;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private TableColumn<SoumArticles, String> quantiteColumn;
    
    @FXML
    private TableColumn<SoumArticles, String> umColumn;
    
    @FXML
    private TableColumn<SoumArticles, String> prixSoumColumn;
    
    @FXML
    private TableColumn<SoumArticles, String> totalSoumColumn;
    
    @FXML
    private Label titre;
    
    @FXML
    private Label totalCahier;
    
    private MainApp mainApp;
    private SoumTitles title = new SoumTitles();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        articleColumn.setCellValueFactory(cellData -> cellData.getValue().articleProperty());
        descColumn.setCellValueFactory(cellData -> cellData.getValue().descProperty());
        quantiteColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty());
        umColumn.setCellValueFactory(cellData -> cellData.getValue().umProperty());
        prixSoumColumn.setCellValueFactory(cellData -> cellData.getValue().prixSoumProperty());
        totalSoumColumn.setCellValueFactory(cellData -> cellData.getValue().totalSoumProperty());
    }
    
    void setIndexTab(int selectedIndex) {
        System.out.println("[ V ] " + selectedIndex);
        this.selectedIndex = selectedIndex;
        
        if(!mainApp.getSia451().getArticles().isEmpty())
            table.setItems(mainApp.getSia451().getArticles());
        else
            table.setItems(null);
        
        //set titre
        if(mainApp.getSia451().getNumCanSelected() != 0)
            titre.setText(mainApp.getSia451().getNumCanSelected() + " - " + this.title.constNomCan(mainApp.getSia451().getNumCanSelected()));
        else
            titre.setText("- aucun cahier selectionné -");
        
        //set total cahier
        if(mainApp.getSia451().getNumCanSelected() != 0)
            totalCahier.setText(mainApp.getSia451().getTotalCahier());
        else
            totalCahier.setText("0,00");
    }

    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
