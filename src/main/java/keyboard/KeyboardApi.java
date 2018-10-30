package keyboard;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import singleton.Settings;

public class KeyboardApi implements NativeKeyListener {
    
    protected Settings settings = Settings.getInstance();
    protected Set<KeyboardListener> listeners;
    protected boolean ctrl = false;
    protected boolean option = false;
    protected static boolean appActive = false;
    
    public KeyboardApi(){
        listeners = new HashSet<KeyboardListener>();
        
        try {
            // Get the logger for "org.jnativehook" and set the level to warning.
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);
            
            // Don't forget to disable the parent handlers.
            logger.setUseParentHandlers(false);
            listeners = new HashSet<KeyboardListener>();
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
            
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
    
    public void addKeyboardTriggerListeren(KeyboardListener listener){
        listeners.add(listener);
    }
    
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_META){
            appActive = true;
            
            final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(()-> {
                if(appActive){
                    fireEvent("open");
                }
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }
    
    public void nativeKeyReleased(NativeKeyEvent e) {
        
        if (e.getKeyCode() == NativeKeyEvent.VC_META){
            appActive = false;
            if(settings.currentState == settings.STATE_OPEN){
                fireEvent("close");
            }  
        }
        // & settings.currentState == settings.STATE_OPEN
       if(appActive){
            
            if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT){
                fireEvent("down");
            }
            
            if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL ){
                fireEvent("delete");
            }
            
            if(e.getKeyCode() == NativeKeyEvent.VC_ALT ){
                fireEvent("up");
            }
        }
        
        System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) +" key code: " +  e.getKeyCode());
    }
    
    //deze is undefined
    public void nativeKeyTyped(NativeKeyEvent e) {
        //	System.out.println("Key Typed: " + e );
    }
    
    public void fireEvent(String name){
        KeyboardEvent e = new KeyboardEvent(name);
        for (KeyboardListener listener : listeners) {
            listener.KeyboardTriggerd(e);
        }
    }
}
