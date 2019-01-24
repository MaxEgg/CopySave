package com.CopySave.singleton;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Storage {
    private static String fileName = "copybox.txt";
    private static String path = System.getProperty("user.home") + "/Library/CopySave/";

    protected static final Storage instance = new Storage();

    public static Storage getInstance() {
        return instance;
    }

    public static String getItem(String key) {
        try {
            FileInputStream stream = new FileInputStream(getFile());
            JSONObject json = toJson(stream);

            return (String) json.get(key);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void addItem(String key, String item) {
        writeToFile(key, item);
    }

    public void removeItem(String key) {

    }

    private static File getFile() {
        try {
            File file = new File(path + fileName);

            if (!file.exists()) {
                Boolean result = file.createNewFile();
                if(result) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8));
                    writer.write("{version: \"1.0\"}");
                    writer.close();
                }
            }

            return file;
        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void writeToFile(String key, String value) {
        try {
            FileInputStream stream = new FileInputStream(getFile());
            JSONObject json = toJson(stream);
            json.put(key, value);
            String content = json.toString();

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8));
            writer.write(content);
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Storage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static JSONObject toJson(InputStream stream) {

        try {
            StringBuffer response = new StringBuffer();

            BufferedReader in = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
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
