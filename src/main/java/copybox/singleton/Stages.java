package copybox.singleton;

import copybox.common.OnFinish.OnFinish;
import copybox.controllers.ClipboardController;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import copybox.singleton.Settings;

public class Stages extends OnFinish{
    
    protected static Stages instance;
    
    Settings settings = Settings.getInstance();
    Stage stage;
    ClipboardController clipboardController;
    
    public Stages(){
        stage = settings.stage;
    }
    
    public static Stages getInstance(){
        if(instance == null){
            instance = new Stages();
        }
        
        return instance;
    }
    
    public void open(){
        openDirect();
    }
    
    public Stages close(){
        TranslateTransition slide = new TranslateTransition(new Duration(200), settings.root);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(0);
        slide.setOnFinished(e -> {
            closeDirect();
            handleOnFinish();
        });
        slide.play();
        
        return getInstance();
    }
    
    public void openDirect(){
        settings.currentState = settings.STATE_OPEN;
        
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        
        System.out.println("primaryScreenBounds: " + (primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth()- settings.itemWidth - settings.selectedWidth - 1));
        System.out.println("primaryScreenBounds.getMinX(): " +  primaryScreenBounds.getMinX());
        
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - settings.itemWidth - settings.selectedWidth - settings.shadowSpacing - 1 );
        stage.setWidth((settings.itemWidth + settings.selectedWidth + settings.shadowSpacing));
        stage.setHeight(settings.stageHeight);
    }
    
    public void closeDirect(){
        settings.currentState = settings.STATE_CLOSED;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 1);
        stage.setWidth((double)1);
        stage.setHeight((double)0);
    }
    
}
