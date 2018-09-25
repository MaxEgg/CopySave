/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import keyboard.KeyboardApi;
import keyboard.KeyboardListener;
import keyboard.KeyboardEvent;
import singleton.Settings;
/**
 *
 * @author max
 */
public class ApplicationController implements KeyboardListener {
    
    private Settings settings = Settings.getInstance();
    private Stage stage = settings.stage;
    private AnchorPane root;
    private ClipboardController clipboardBoardController;
    private KeyboardApi keyboardApi;

    public ApplicationController() {
        initKeyboard();
    }

    public AnchorPane playMainScene() {
        this.root = new AnchorPane();
        VBox vbox = new VBox();

        root.getChildren().add(vbox);
        
        clipboardBoardController = new ClipboardController(root);
        
        root.setStyle("-fx-focus-color: transparent;");
        root.getChildren().add(clipboardBoardController.getView());
   
        return root;
    }
    
    public void KeyboardTriggerd(KeyboardEvent e){
        System.out.println("event " + e.eventName);
        switch(e.eventName){
            case  "open":
                open();
                break;
            case "close":
                close();
                break;
            case "up":
                break;
            case "down":
                break;
        }
    }
       
    private void initKeyboard(){
         KeyboardListener lis = (KeyboardEvent e) -> {
             KeyboardTriggerd(e);
         };
         keyboardApi = new KeyboardApi();
         keyboardApi.addKeyboardTriggerListeren(lis);
    }
       
    private void open(){
        this.stage.setWidth(settings.itemWidth);
        this.stage.setHeight(settings.stageHeight);
        TranslateTransition slide = new TranslateTransition(new Duration(200), this.root);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(-settings.itemWidth);
        slide.play();
    }
    
    private void close(){
        TranslateTransition slide = new TranslateTransition(new Duration(200), this.root);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(0);
        slide.setOnFinished(e -> {
            this.stage.setWidth(0);
        });
        slide.play();
    }
}   
 