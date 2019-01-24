package com.CopySave.version;

import com.CopySave.singleton.Context;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionController{
        
    protected JSONObject information;
    private Context context = Context.getInstance();

    public JSONObject getInformation(){

        if(information == null){
//            String url = "https://whereyouapp.nl/software/copybox/update.json";
            String url = "https://drive.google.com/drive/folders/1-RYBB5_v-DwIeCNV455uMerECt_X1WY8";

            HttpURLConnection con = get(url);
            System.out.println("connect : ");
            System.out.println(con);
            information = json(con);
        }

        return information;
    }
    
    public boolean newVersionAvailable(){
        JSONObject information = getInformation();
        double availableVersion = information.getDouble("version");
        double currentVersion = context.getApplicationVersion();
        
        return availableVersion > currentVersion;
    }
    
    public HttpURLConnection get(String url){
        
        try {
            URL obj = new URL(url);
            HttpURLConnection con  = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

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
            
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
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

    public void downloadUpdate(){
//        try {
//            URL url = new URL("https://drive.google.com/uc?export=download&id=14hxaSjRLG6PmX2PSv30NI1gVty9DySSp");
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        InputStream in = connection.getInputStream();
//        FileOutputStream out = new FileOutputStream("test.zip");
//
//        copy(in, out, 1024);
//        out.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String saveTo = "/";
        try {
            URL url = new URL("https://drive.google.com/uc?export=download&id=14hxaSjRLG6PmX2PSv30NI1gVty9DySSp");
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            System.out.println(System.getProperty("user.dir"));
            FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "test.zip");
            byte[] b = new byte[1024];
            int count;
            while ((count = in.read(b)) >= 0) {
                out.write(b, 0, count);
            }
            out.flush(); out.close(); in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void deleteLibFolder(){
//        String userDir = System.getProperty("user.dir");
//
//        System.out.println("test: " + userDir);
//
//        Path directory = Paths.get("$APPDIR");
//        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
//                Files.delete(file); // this will work because it's always a File
//                return FileVisitResult.CONTINUE;
//            }
//
//            @Override
//            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
//                Files.delete(dir); //this will work because Files in the directory are already deleted
//                return FileVisitResult.CONTINUE;
//            }
//        });
//
//    }
}
