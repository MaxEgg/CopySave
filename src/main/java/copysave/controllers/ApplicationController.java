package copysave.controllers;

import com.google.j2objc.annotations.ReflectionSupport.Level;
import copysave.keyboard.KeyboardApi;
import copysave.keyboard.KeyboardEvent;
import copysave.keyboard.KeyboardListener;
import copysave.singleton.Settings;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import copysave.singleton.Stages;
import copysave.version.VersionController;
import org.json.JSONObject;

public class ApplicationController implements KeyboardListener {

    private Settings settings = Settings.getInstance();
    private Stages stages;

    private ClipboardController clipboardController;
    private KeyboardApi keyboardApi;
    private VersionController versionController = new VersionController();

    public ApplicationController() {
        System.out.println("Initiating application");
        initKeyboard();
    }

    public VBox playMainScene() {
        VBox root = new VBox();

        clipboardController = new ClipboardController(root);

        boolean needsUpdating = versionController.newVersionAvailable();

        if (needsUpdating) {
            root.getChildren().add(getUpdateNotification());
        }

        root.getChildren().add(clipboardController.getView());

        settings.root = root;
        //initiate after the root has been set;
        stages = Stages.getInstance();

        return root;
    }

    private void initKeyboard() {
        KeyboardListener lis = (KeyboardEvent e) -> {
            KeyboardTriggerd(e);
        };
        keyboardApi = new KeyboardApi();
        keyboardApi.addKeyboardTriggerListeren(lis);
    }

    public void KeyboardTriggerd(KeyboardEvent e) {
        switch (e.eventName) {
            case "open":
                stages.open();
                clipboardController.animateOpen();
                break;
            case "close":
                clipboardController.animateClose();
                break;
            case "up":
                clipboardController.selectNext(true);
                break;
            case "down":
                clipboardController.selectNext(false);
                break;
            case "delete":
                clipboardController.delete();
                break;
        }
    }

    private FlowPane getUpdateNotification() {
        JSONObject information = versionController.getInformation();
        Hyperlink link = new Hyperlink(information.getString("updateMessage"));

        link.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(information.getString("updateLink")));
                } catch (URISyntaxException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.NATIVE_ONLY, null, ex);
                } catch (IOException ex) {
//                    Logger.getLogger(ApplicationController.class.getName()).log(Level.NATIVE_ONLY, null, ex);
                }
            }
        });

        FlowPane pane = new FlowPane();

        pane.setPrefWidth(200);
        pane.getChildren().add(link);

        return pane;
    }
}