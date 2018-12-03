package copysave.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import copysave.singleton.Storage;

public class VersionController{
        
    protected Storage storage = Storage.getInstance();
    protected JSONObject information;
       
    public JSONObject getInformation(){
        if(information == null){
            String url = "https://whereyouapp.nl/software/copybox/update.json";
            HttpURLConnection con = get(url);
            information = json(con);
        }
        return information;
    }
    
    public boolean newVersionAvailable(){
        JSONObject information = getInformation();
        double availableVersion = information.getDouble("version");
        double currentVersion = Double.parseDouble(storage.getItem("version"));
        
        return availableVersion > currentVersion;
    }
    
    public HttpURLConnection get(String url){
        
        try {
            URL obj = new URL(url);
            HttpURLConnection con  = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            
            return con;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public JSONObject json(HttpURLConnection con){
        
        try {
            StringBuffer response = new StringBuffer();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            in.close();
                    
            return new JSONObject(response.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
