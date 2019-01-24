package com.CopySave.controllers;

import com.CopySave.singleton.Context;
import com.CopySave.keyboard.KeyboardApi;
import com.CopySave.keyboard.KeyboardEvent;
import com.CopySave.keyboard.KeyboardListener;
import com.CopySave.singleton.Stages;
import com.CopySave.version.VersionController;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ApplicationController implements KeyboardListener {

    private Context context = Context.getInstance();
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

        clipboardController = new ClipboardController();
        versionController.downloadUpdate();
        System.out.println("Adding clipboard view to the root");
        root.getChildren().add(clipboardController.getView());

        context.setRoot(root);

        //initiate after the root has been set;
        stages = Stages.getInstance();

        return root;
    }

    private void initKeyboard() {
        KeyboardListener lis = (KeyboardEvent e) -> {
            keyboardTriggered(e);
        };

        keyboardApi = new KeyboardApi();
        keyboardApi.addKeyboardTriggerListeren(lis);
    }

    public void keyboardTriggered(KeyboardEvent e) {
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
            default:
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