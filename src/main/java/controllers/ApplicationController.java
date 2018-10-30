package controllers;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import keyboard.KeyboardApi;
import keyboard.KeyboardListener;
import keyboard.KeyboardEvent;
import singleton.Settings;
import singleton.Stages;

public class ApplicationController implements KeyboardListener {
    
    private Settings settings = Settings.getInstance();
    private Stages stages = Stages.getInstance();
    
    private Stage stage = settings.stage;
    private AnchorPane root;
    private ClipboardController clipboardController;
    private KeyboardApi keyboardApi;

    public ApplicationController() {
        System.out.println("Initiating application");
        initKeyboard();
    }

    public VBox playMainScene() {
        VBox root = new VBox();

        clipboardController = new ClipboardController(root); 
        root.getChildren().add(clipboardController.getView());

        settings.root = root;
        return root;
    }
    
    private void initKeyboard(){
        KeyboardListener lis = (KeyboardEvent e) -> {
            KeyboardTriggerd(e);
        };
        keyboardApi = new KeyboardApi();
        keyboardApi.addKeyboardTriggerListeren(lis);
    }
    
    public void KeyboardTriggerd(KeyboardEvent e){
        switch(e.eventName){
            case  "open":
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
}