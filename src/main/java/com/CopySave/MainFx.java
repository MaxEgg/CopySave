package com.CopySave;

import com.CopySave.controllers.ApplicationController;
import com.CopySave.singleton.Context;
import com.CopySave.singleton.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainFx extends Application {

    private Context context = Context.getInstance();
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void start(Stage stage) {

        context.setStage(stage);

        Platform.setImplicitExit(false);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

//        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setTitle("CopySave");

        ApplicationController applicationController = new ApplicationController();
        VBox root = applicationController.playMainScene();
        System.out.println("Playing main scene");

        Scene scene = new Scene(root, preferences.getItemWidth(), 0, Color.TRANSPARENT);
        scene.setUserAgentStylesheet(getClass().getResource("/com/application.css").toExternalForm());
        stage.setScene(scene);

        Image image = new Image("file:/icons/copysaveiconextended_384x384.icns");
        stage.getIcons().add(image);

        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - preferences.getItemWidth());
        stage.setY(primaryScreenBounds.getMinY());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}