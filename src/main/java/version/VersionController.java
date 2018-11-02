package version;

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

public class VersionController {
    
    public double version = 1.0;
    
    public VersionController (){
        
    }
    
    public void checkForUpdate(){
        try {
            String url = "https://whereyouapp.nl/software/copybox/versions/"+ version;
            URL obj = new URL(url);
            HttpURLConnection con  = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
           
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            } 
            
            in.close();
            
            JSONObject json = new JSONObject(response.toString());
               System.out.println(json);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VersionController.class.getName()).log(Level.SEVERE, null, ex);
        }
;
    }
}
