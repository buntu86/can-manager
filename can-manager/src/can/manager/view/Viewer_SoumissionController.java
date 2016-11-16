package can.manager.view;

import can.manager.MainApp;
import can.manager.model.Sia451;
import can.manager.model.TitleSia451;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Viewer_SoumissionController implements Initializable {

    private MainApp mainApp;
    private Sia451 sia451 = null;
    
    @FXML
    private TabPane rootTabPane = new TabPane();
    private TableView tableSoumission;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }  
    
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }    

    public void showTabPane() {
        sia451 = mainApp.getSia451();
        int i=0;

        for(TitleSia451 title:sia451.getTitlesCan())
        {
            //Set string (numCan nomCan), if > 15 substing and add ..
            String str = title.getNumCan() + " " + title.getNomCan();
            if(str.length()>15)
                str = str.substring(0, 15)+"..";
            
            //Add a new tab
            rootTabPane.getTabs().add(new Tab(str));
            rootTabPane.getTabs().get(i).setContent(tableSoumission);
            
            //Set disable tabPan with etatCan *.cmc inexistant
            rootTabPane.getTabs().get(i).setDisable(title.getEtatCanBoolean());
            
            //update title with idTabPane
            title.setIdTabPane(i);            

            i++;
        }
     
        updateViewer();

        //Listener change tab
        rootTabPane.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                    updateViewer();
                }
            }
        );        
    }
    
    public void updateViewer(){
        
    }
}
