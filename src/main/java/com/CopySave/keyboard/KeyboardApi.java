package com.CopySave.keyboard;

import com.CopySave.singleton.Preferences;
import com.CopySave.singleton.Context;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyboardApi implements NativeKeyListener {

    protected Context context = Context.getInstance();
    private Preferences preferences = Preferences.getInstance();

    protected Set<KeyboardListener> listeners;
    protected boolean appActive = false;
    protected ScheduledExecutorService executorService;

    private static Logger logger;

    public KeyboardApi() {
        System.out.println("Initiating keyboard api");
        listeners = new HashSet<KeyboardListener>();
        setLoggerOff();

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
            Thread.currentThread().interrupt();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            try {
                Thread.sleep(2000);
                System.err.println("Trying again");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setLoggerOff() {
        // Get the logger for "org.jnativehook" and set the level to warning.
        logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }


    public void addKeyboardTriggerListeren(KeyboardListener listener) {
        listeners.add(listener);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == getMainKey()) {
            appActive = true;
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                if (appActive) {
                    fireEvent("open");
                }
            }, preferences.getWaitForOpen(), TimeUnit.MILLISECONDS);
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

        if (e.getKeyCode() == getMainKey()) {
            appActive = false;
            executorService.shutdownNow();
            if (context.isOpen()) {
                fireEvent("close");
            }
        }

        if (appActive) {
            if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
                fireEvent("down");
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
                fireEvent("delete");
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_ALT) {
                fireEvent("up");
            }
        }
    }

    //deze is undefined
    public void nativeKeyTyped(NativeKeyEvent e) {
        //	System.out.println("Key Typed: " + e );
    }

    public void fireEvent(String name) {
        KeyboardEvent e = new KeyboardEvent(name);
        for (KeyboardListener listener : listeners) {
            listener.keyboardTriggered(e);
        }
    }

    private int getMainKey() {
        if (Context.isMac() || Context.isLinux()) {
            return NativeKeyEvent.VC_META;
        } else if (Context.isWindows()) {
            return NativeKeyEvent.VC_CONTROL;
        }

        return NativeKeyEvent.VC_META;
    }
}
