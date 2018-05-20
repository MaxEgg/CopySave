/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import clipboard.ClipboardContent;
import clipboard.ClipboardEvent;
import clipboard.ClipboardItem;
import clipboard.ClipboardListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author max
 */
public class ApplicationController {
    
    private Stage stage;
    private VBox root;
    private ClipboardController clipboardBoardController;
    
    
    public ApplicationController(Stage stage) {
        this.stage = stage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth());
    }

    public VBox playMainScene() {
        clipboardBoardController = new ClipboardController();
        this.root = new VBox();
        root.setStyle("-fx-focus-color: transparent; -fx-background-color: linear-gradient(to right, rgba(0,0,0,0) 0% , rgba(0,0,0,0.7));");
        root.getChildren().add(clipboardBoardController.getView());
        return root;
    }
}