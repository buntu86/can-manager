/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package can.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Adrien
 */
public class ExportDBFtoSQLite {
    
    private Path path_DBFfile;
    private String fileName;
    
    //Path pathFileDBF = Paths.get(getPath());  

    ExportDBFtoSQLite(String pathString) throws IOException{

        path_DBFfile = Paths.get(pathString);

        if(checkPathDBF())
        {
        
        }
    }

    //Valid if the file exist AND the extension is correct
    private boolean checkPathDBF() throws IOException {
        if(Files.exists(path_DBFfile)){
            String nameFile = path_DBFfile.getFileName().toString();
            String extensionFile = nameFile.substring(nameFile.lastIndexOf('.')+1);
            this.fileName = nameFile.substring(0, nameFile.lastIndexOf('.'));

            if(extensionFile.toLowerCase().equals("dbf")){
                Long sizeFile = Files.size(path_DBFfile);
                System.out.println("[ V ] File DBF exist, name file \"" + this.fileName + "\", extension \"" + extensionFile.toLowerCase() +", size file " + sizeFile + "byte");
                return true;
            }
            
            else{
                System.out.println("[ X ] File is not DBF...");
                return false;                           
            }
        }
        
        else{
            System.out.println("[ X ] File DBF not fund...");
            return false;       
        }
    }
}
