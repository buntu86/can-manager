/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package can.manager.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Adrien
 */
public class RootLayoutController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handelOpen() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if(selectedFile != null)
            System.out.println("Fichier selectionné : " + selectedFile.getName());
        
        else
            System.out.println("Selection du fichier annulé");
    } 

    @FXML
    private void handleAproposDe() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Can manager");
        alert.setHeaderText("A propos de");
        alert.setContentText("Auteur: Adrien Pillonel");

        alert.showAndWait();
    }    

    @FXML
    private void handleExit(){
        System.exit(0);
    }
    
}
