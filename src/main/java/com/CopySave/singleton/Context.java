package com.CopySave.singleton;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Context {

    protected static final Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private static final double applicationVersion = 0.1;

    private static final int WINDOWS = 0;
    private static final int MAC = 1;
    private static final int LINUX = 2;

    //public states.
    public static final int STATE_OPEN = 0;
    public static final int STATE_CLOSED = 1;

    private static int currentState;

    private static Stage currentStage;
    private static VBox currentRoot;

    private static double stageHeight;

    public static int getOS() {
        int OS = -1;

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows")) {
            OS = WINDOWS;
        } else if (osName.contains("mac")) {
            OS = MAC;
        } else if (osName.contains("linux")) {
            OS = LINUX;
        }

        return OS;
    }

    public static boolean isWindows() {
        return WINDOWS == getOS();
    }

    public static boolean isMac() {
        return MAC == getOS();
    }

    public static boolean isLinux() {
        return LINUX == getOS();
    }

    public static void setApplicationState(int state) {
        currentState = state;
    }

    public static void setStageHeight(double height) {
        stageHeight = height;
    }

    public static double getStageHeight() {
        return stageHeight;
    }

    public static boolean isOpen() {
        return currentState == STATE_OPEN;
    }

    public static void setStage(Stage stage) {
        currentStage = stage;
    }

    public static Stage getStage() {
        return currentStage;
    }

    public static void setRoot(VBox root) {
        currentRoot = root;
    }

    public static VBox getRoot() {
        return currentRoot;
    }

    public static double getApplicationVersion(){
        return applicationVersion;
    }

}
