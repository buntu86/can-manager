package can.manager;

import java.sql.SQLException;

/**
 *
 * @author adrienpillonel
 */
public class CanManager {

    public static void main(String[] args) throws SQLException{
        String fileName = "C:/Temp/sampleSQL.db";
        
        SqlObject obj = new SqlObject(fileName);
     
/*       
    Charset stringCharset = Charset.forName("IBM437");
    String fileName = "C:/Temp/NPK_tris/F15115.DBF";
    
    Path path = Paths.get(fileName);   
    boolean existe = Files.exists(path);
    FileTime time = Files.getLastModifiedTime(path);
    Long sizeFile = Files.size(path);
    
    System.out.println(path.getFileName()+" "+existe+" "+time+" "+sizeFile+"byte");
       
    BufferedReader in = Files.newBufferedReader(path, stringCharset);
    String line1=null;
    String line2=null;
    
    //value=in.read();   
       
    line1=in.readLine();
    line2=in.readLine();
    
    int nbrChar=78;

    List<String> liste = java.util.Arrays.asList(line2.split("(?<=\\G.{"+nbrChar+"})"));    
    
    Iterator<String> iterator = liste.iterator();
    
   
    while (iterator.hasNext()) {
        String nodes = iterator.next();
        
        if(nodes.length()==78)
       {
            for(int i = 0; i < 1; i++) {
                System.out.print(nodes.charAt(i));
            }

            System.out.print("|");

            for(int i = 1; i < 4; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 4; i < 7; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 7; i < 9; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 9; i < 11; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 11; i < 12; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");    

            for(int i = 12; i < 14; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 14; i < 16; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");    

            for(int i = 16; i < 18; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|");

            for(int i = 18; i < 78; i++) {
                System.out.print(nodes.charAt(i));
            }    
            System.out.print("|\n");
       }
    }    */
   }

    
}
