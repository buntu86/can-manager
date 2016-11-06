package can.manager.data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.Alert;

public final class Config {
    private Path catalogDirectory, listCatalogCanFile;
    
    public Config(){
        setCatalogDirectory();
        setListCatalogCanFile();
    }

    public void setCatalogDirectory() {
        this.catalogDirectory = Paths.get(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "canManager" + System.getProperty("file.separator"));
        if(Files.exists(catalogDirectory))
            System.out.println("[ V ] Ok directory canManager exist");
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration");
            alert.setHeaderText(null);
            alert.setContentText(catalogDirectory + "\nLe dossier n'existe pas. Veuillez réinstaller le logiciel.");
            alert.showAndWait();
        }
    }
    
    public Path getCatalogDirectory() {
        return this.catalogDirectory;
    }

    public void setListCatalogCanFile() {
        this.listCatalogCanFile = Paths.get(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop" + System.getProperty("file.separator") + "canManager" + System.getProperty("file.separator") + "config" + System.getProperty("file.separator") + "listCatalogCan.db");
        if(Files.exists(listCatalogCanFile))
            System.out.println("[ V ] Ok directory canManager/config/listCatalogCanFile.db exist");
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Configuration");
            alert.setHeaderText(null);
            alert.setContentText(listCatalogCanFile + "\nLe fichier n'existe pas. Veuillez réinstaller le logiciel.");
            alert.showAndWait();
        }
    }
    
    public Path getListCatalogCanFile() {
        return this.listCatalogCanFile;
    }
}
