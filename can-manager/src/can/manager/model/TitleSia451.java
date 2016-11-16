package can.manager.model;

import can.manager.MainApp;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import can.manager.data.Config;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class TitleSia451 {
    private Connection conn = null;
    private final Path listCatalogCanFile, catalogDirectory;
    private final StringProperty nomCan;
    private final IntegerProperty numCan, yearCan;
    private final SimpleObjectProperty<ImageView> etatCan;
    private final SimpleObjectProperty<Button> buttonConvert;
    private int idTabPane = 0;
    
    public TitleSia451(String fromCms) /*112 15*/{
        Config config = new Config();
        this.listCatalogCanFile = config.getListCatalogCanFile();
        this.catalogDirectory = config.getCatalogDirectory();
        connect(); 
        this.numCan = new SimpleIntegerProperty(Integer.parseInt(fromCms.substring(0, 3)));
        this.yearCan = new SimpleIntegerProperty(Integer.parseInt(fromCms.substring(4, 6)));
        this.nomCan = new SimpleStringProperty(constNomCan());
        this.etatCan = new SimpleObjectProperty(constEtatCan());
        this.buttonConvert = new SimpleObjectProperty(constButtonConvert());
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
            System.out.println("[ X ] File listCatalogCan.db don't exist");
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
    public ObjectProperty<ImageView> etatCanProperty(){
        return this.etatCan;
    }

    private ImageView constEtatCan() {
        Image img;
        if(Files.exists(Paths.get(catalogDirectory.toString() + "\\F" + getNumCan() + getYearCan() + ".cmc")))
            img = new Image("can/manager/img/true.png"); 
        else
            img = new Image("can/manager/img/false.png"); 
        
        ImageView imgView = new ImageView(img);
        
        return imgView;
    }
    
    //BUTTON CONVERT
    private Button constButtonConvert() {
        Button btn = null;
        if(!Files.exists(Paths.get(catalogDirectory.toString() + "\\F" + getNumCan() + getYearCan() + ".cmc")))
        {
            btn = new Button("Convertir");
            btn.setOnAction((event) -> {                
                MainApp main = null;
                try {
                    main = new MainApp();
                } catch (SQLException ex) {
                    Logger.getLogger(TitleSia451.class.getName()).log(Level.SEVERE, null, ex);
                }
                main.showConvertDialog(catalogDirectory.toString() + "\\F" + getNumCan() + getYearCan() + ".dbf");
            });
        }
        return btn;
    }
    public ObjectProperty<Button> buttonConvertProperty(){
        return this.buttonConvert;
    }

    public void setIdTabPane(int i) {
        this.idTabPane=i;
    }
}
