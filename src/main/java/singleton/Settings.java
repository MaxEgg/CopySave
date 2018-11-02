package singleton;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Settings {
    protected static Settings instance;
    
    public static Settings getInstance() {
        if(instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSED = 1;
    public static final int STATE_ADDING = 2;
    public int currentState;
        
    public Stage stage;
    public VBox root;
    public AnchorPane itemsHolder;
 
    public int itemWidth = 200;
    public int itemHeight = 100;
    public int itemMaxHeight = 200;
    public int selectedWidth = 20;    
    public int maxItems = 20;
    
    public int waitForOpen = 950;
    public int closeSpeed = 200;
    
    public double stageWidth;
    public double stageHeight;
}
