package can.manager.model;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import can.manager.data.Config;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class TitleSia451 {
    private Connection conn = null;
    private final Path listCatalogCanFile, catalogDirectory;
    private final StringProperty nomCan;
    private final IntegerProperty numCan, yearCan;
    private final Image etatCan;
    
    public TitleSia451(String fromCms){
        Config config = new Config();
        this.listCatalogCanFile = config.getListCatalogCanFile();
        this.catalogDirectory = config.getCatalogDirectory();
        connect(); 
        this.numCan = new SimpleIntegerProperty(Integer.parseInt(fromCms.substring(0, 3)));
        this.yearCan = new SimpleIntegerProperty(Integer.parseInt(fromCms.substring(4, 6)));
        this.nomCan = new SimpleStringProperty(constNomCan());
        this.etatCan = constEtatCan();
    }

    private boolean connect()
    {
        if(Files.exists(listCatalogCanFile)) {
            System.out.println("[ V ] File listCatalogCan.db exist");
            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:" + listCatalogCanFile);
                System.out.println("[ V ] Connection to file " + listCatalogCanFile);
                return true;
            } catch (SQLException e) {
                System.out.println("[ X ] " + e.getMessage());
            }
        }

        else {
            System.out.println("[ X ] File listCatalogCan.db exist");
        }        
        
        return false;
    }        

    ///NUM CAN
    private void setNumCan(int numCan) {
        this.numCan.set(numCan);
    }
    public int getNumCan(){
        return this.numCan.get();
    }
    public IntegerProperty numCanProperty(){
        return numCan;
    }    
    
    //YEAR CAN
    private void setYearCan(int yearCan) {
        this.yearCan.set(yearCan);
    }
    public int getYearCan(){
        return this.yearCan.get();
    }
    public IntegerProperty yearCanProperty(){
        return yearCan;
    }
    
    //NOM CAN
    private String constNomCan(){
        String sql = "SELECT * FROM titles WHERE num=" + getNumCan();
        String tempNomCan = null;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                tempNomCan = rs.getString("title");
                System.out.println(rs.getString("title"));
            }                        
        }
        catch(SQLException e){
            System.out.println("[ X ] " + e.getMessage());
        }
        return tempNomCan;
    }
    private void setNomCan(String nomCan) {
        this.nomCan.set(nomCan);
    }
    public String getNomCan(){
        return this.nomCan.get();
    }
    public StringProperty nomCanProperty(){
        return nomCan;
    }

    //ETAT CAN
    private Image constEtatCan() {
        Image img = new Image("can/manager/img/false.png");
        return img;
    }
    public Image etatCanProperty(){
        return this.etatCan;
    }
}
