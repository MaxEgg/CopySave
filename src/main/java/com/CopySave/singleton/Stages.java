package com.CopySave.singleton;

import com.CopySave.common.OnFinish;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Stages extends OnFinish {

    protected static final Stages instance = new Stages();

    private static Context context = Context.getInstance();
    private static Preferences preferences = Preferences.getInstance();

    private static Stage stage = context.getStage();

    public static Stages getInstance() {
        return instance;
    }

    public void open() {
        context.setApplicationState(context.STATE_OPEN);
        openDirect();
    }

    public static Stages close() {
        TranslateTransition slide = new TranslateTransition(new Duration(200), context.getRoot());
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(0);
        slide.setOnFinished(e -> {
            closeDirect();
//            handleOnFinish();
        });
        slide.play();

        return getInstance();
    }

    public static void openDirect() {

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - preferences.getItemWidth() - preferences.selectedWidth - preferences.getShadowSpacing() - 1);
        stage.setWidth(( preferences.getItemWidth() + preferences.getSelectedWidth() + preferences.getShadowSpacing()));
        stage.setHeight(context.getStageHeight());
    }

    public static void closeDirect() {
        context.setApplicationState(context.STATE_CLOSED);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 1);
        stage.setWidth((double) 1);
        stage.setHeight((double) 0);
    }

}
