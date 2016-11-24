package can.manager.view;

import can.manager.MainApp;
import can.manager.model.Soum;
import can.manager.model.SoumTitles;
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
import javafx.scene.layout.AnchorPane;

public class Viewer_SoumissionController implements Initializable {

    private MainApp mainApp;
    private Soum sia451 = null;
    
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

        for(SoumTitles title:sia451.getTitlesCan())
        {
            //Set string (numCan nomCan), if > 15 substing and add ..
            String str = title.getNumCan() + " " + title.getNomCan();
            if(str.length()>15)
                str = str.substring(0, 15)+"..";
            
            //Add a new tab
            rootTabPane.getTabs().add(new Tab(str));
            
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
        if(!rootTabPane.getSelectionModel().getSelectedItem().isDisable())
        {            
            AnchorPane table = null;
            try {
                int index=rootTabPane.getSelectionModel().getSelectedIndex();
                mainApp.getSia451().setIndexTabSelected(index);
                
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/Table_Soumission.fxml"));
                table = (AnchorPane) loader.load();
                Table_SoumissionController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setIndexTab(index);
                
                rootTabPane.getSelectionModel().getSelectedItem().setContent(table);
                System.out.println("[ V ] " + mainApp.getSia451().getNumCanSelected());
            }
            catch(IOException e){
                System.out.println("[ X ] Erreur file Table_Soumission.fxml " + e.getMessage());
            }
        }
    }
}


//System.out.println(rootTabPane.getSelectionModel().getSelectedIndex());