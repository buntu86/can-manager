package can.manager;

import can.manager.data.QuerySQL;
import java.io.IOException;
import java.sql.SQLException;

public class CanManager {

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
        
        QuerySQL data = new QuerySQL(fileName);
        

        
        //http://code.makery.ch/library/javafx-8-tutorial/fr/part1/
        
        
   }
}
