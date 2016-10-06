package can.manager;

import can.manager.model.CatalogCAN;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CanManager {

    /*
    //http://code.makery.ch/library/javafx-8-tutorial/fr/part1/
    private Stage pimaryStage;
    private BorderPane rootLayout;
    
    @Override
    public void start(Stage primaryStage){
        this.pimaryStage = primaryStage;
        this.pimaryStage.setTitle("CanManager");
        
        initRootLayout();
        
        showCanViewer();
    }
    
    public void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CanManger.class.getResource("view/RootLayout.fxml"));
        }
    }*/
    
    public static void main(String[] args) throws SQLException, IOException, InterruptedException{
        
        String fileName;
        
        /*
        --- USE exportDBFtoSQLite ---

        
        if(args.length > 0)
            fileName = args[0];
        else
            fileName = System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "F24115.DBF";
        
        ExportDBFtoSQLite run = new ExportDBFtoSQLite(fileName);
        
        System.out.println("\nPress ENTER to close de console");
        int read = System.in.read();
        */

        //--- USE querySQL ---
        
        if(args.length > 0)
            fileName = args[0];
        else
            fileName = System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "can.db";
        
        CatalogCAN data = new CatalogCAN(fileName);    
        
   }
}
