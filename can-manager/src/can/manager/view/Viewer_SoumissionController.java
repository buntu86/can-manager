package can.manager.view;

import can.manager.MainApp;
import can.manager.model.Sia451;
import can.manager.model.TitleSia451;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Viewer_SoumissionController implements Initializable {

    private MainApp mainApp;
    private Sia451 sia451 = null;
    
    @FXML
    private TabPane rootTabPane = new TabPane();
    
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
            String str = new String(title.getNumCan() + " " + title.getNomCan());
            if(str.length()>10)
                str = str.substring(0, 15)+"..";
            rootTabPane.getTabs().add(new Tab(str));
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
        System.out.println("Tab Selection changed");
        System.out.println(rootTabPane.getSelectionModel().getSelectedIndex());
        
        //rootTabPane.getSelectionModel().select(3);
    }
}
