package copybox;

import copybox.singleton.Settings;
import copybox.controllers.ApplicationController;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    
    private Settings settings = Settings.getInstance();
    
    public static void main(String[] args) throws InterruptedException, ProtocolException, MalformedURLException, IOException {
        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " +
                Runtime.getRuntime().availableProcessors());
        
        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " +
                (Runtime.getRuntime().freeMemory() / 1024 / 1024));
        
        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        /* Maximum amount of memory the JVM will attempt to use */
        System.out.println("Maximum memory (bytes): " +
                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
        
        /* Total memory currently in use by the JVM */
        System.out.println("Total memory (bytes): " +
                (Runtime.getRuntime().totalMemory()/ 1024 / 1024 ));
        
        /* Get a list of all filesystem roots on this system */
        File[] roots = File.listRoots();
        
        /* For each filesystem root, print some info */
        for (File root : roots) {
            System.out.println("File system root: " + root.getAbsolutePath());
            System.out.println("Total space (bytes): " + (root.getTotalSpace() / 1024 / 1024));
            System.out.println("Free space (bytes): " + (root.getFreeSpace()  / 1024 / 1024));
            System.out.println("Usable space (bytes): " + (root.getUsableSpace() / 1024 / 1024));
        }
               
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        try{
           
            
            System.out.println("start " +  System.getProperty("java.home"));
            
            settings.stage = stage;
            
            Platform.setImplicitExit(false);
            
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Copysnacke");
            
            ApplicationController applicationController = new ApplicationController();
            VBox root = applicationController.playMainScene();
            System.out.println("Playing main scene");
            
            Scene scene = new Scene(root, settings.itemWidth, 0, Color.TRANSPARENT);
            
            scene.setUserAgentStylesheet("application.css");
            stage.setScene(scene);
            
            stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()- settings.itemWidth);;
            stage.setY(primaryScreenBounds.getMinY());
            stage.show();
            System.out.println("Stage is showing");
            
            // Select which window to set level (window at index 0 in this case)
            com.sun.glass.ui.Window.getWindows().get(0).setLevel(3);
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}