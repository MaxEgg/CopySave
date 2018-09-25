import singleton.Settings;
import controllers.ApplicationController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {

    private ApplicationController applicationController;
    private AnchorPane root;

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Settings settings = Settings.getInstance();
        settings.stage = stage;
            
        Platform.setImplicitExit(false);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Copybox");
        
        applicationController = new ApplicationController();
        root = applicationController.playMainScene();              
                
        Scene scene = new Scene(root, settings.itemWidth, 0, Color.TRANSPARENT);

        scene.setUserAgentStylesheet("/css/application.css");
        stage.setScene(scene);
        
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()- settings.itemWidth);;
        stage.setY(primaryScreenBounds.getMinY());
        stage.show();
        
        // Select which window to set level (window at index 0 in this case)
        com.sun.glass.ui.Window.getWindows().get(0).setLevel(3);
    }

}