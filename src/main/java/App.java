import clipboard.ClipboardContent;
import clipboard.ClipboardEvent;
import clipboard.ClipboardListener;
import controllers.ApplicationController;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class App extends Application {

    private Stage stage;
    private ApplicationController applicationController;
    private VBox root;

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setTitle("Copybox");
        
        applicationController = new ApplicationController(stage);
        root = applicationController.playMainScene();      
                
        Scene scene = new Scene(root, 400, primaryScreenBounds.getHeight(), Color.TRANSPARENT);
        scene.setUserAgentStylesheet("/css/application.css");
        stage.setScene(scene);

        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 200);
        stage.setY(primaryScreenBounds.getMinY());
        stage.show();
    }
}