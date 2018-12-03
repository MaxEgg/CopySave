
package copysave.singleton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import copysave.version.VersionController;


public class Storage {
    String fileName = "copybox.txt";
    File storageFile;
    
    protected static Storage instance;
    
    public static Storage getInstance(){
        if(instance == null){
            instance = new Storage();
        }
        
        return instance;
    }
    
    public String getItem(String key){
        try {
            FileInputStream stream = new FileInputStream(getFile());
            
            JSONObject json = toJson(stream);
            return (String) json.get(key);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void addItem(String key, String item){
        writeToFile(key, item);
    }
    
    public void removeItem(String key){
        
    }
    
    private File getFile(){
        try {
            File file = new File(fileName);
            
            if(!file.exists()){
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write("{version: \"1.0\"}");
                writer.close();
            }
            
            return file;
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void writeToFile(String key, String value){
        try {
            FileInputStream stream = new FileInputStream(getFile());
            JSONObject json = toJson(stream);
            json.put(key, value);
            String content = json.toString();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject toJson(InputStream stream){
        
        try {
            StringBuffer response = new StringBuffer();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            String inputLine;
            
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            
            in.close();
            
            return new JSONObject(response.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
