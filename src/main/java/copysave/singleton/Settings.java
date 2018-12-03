package copysave.singleton;

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
    
    public static final int WINDOWS = 0;
    public static final int MAC = 1; 
    public static final int LINUX = 2;
    public static int OS = -1;
    
    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSED = 1;
    public static final int STATE_ADDING = 2;
    public int currentState;
        
    public Stage stage;
    public VBox root;
    public AnchorPane itemsHolder;
 
    public int itemSpacing = 3;
    public int itemWidth = 200;
    public int itemHeight = 100;
    public int itemMaxHeight = 200;
    public int selectedWidth = 20;
    public int shadowSpacing = 4;
    public int maxItems = 30;
    
    public int waitForOpen = 1000;
    public int closeSpeed = 200;
    
    public int clipboardTimer = 1000;
    
    public double stageWidth;
    public double stageHeight;
        
    public static int getOS(){
        if(OS == -1){
            String osName = System.getProperty("os.name").toLowerCase();
           
            if(osName.contains("windows")){
                OS = WINDOWS;
            } else if (osName.contains("mac")){
                OS = MAC;
            } else if (osName.contains("linux")){
                OS = LINUX;
            }
        }
 
        return OS;
    }
    
    public static boolean isWindows(){
        return WINDOWS == getOS();
    }
    
    public static boolean isMac(){
        return MAC == getOS();
    }
    
    public static boolean isLinux(){
        return LINUX == getOS();
    }
}
