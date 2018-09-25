package keyboard;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
/**
 *
 * @author max
 */
public class KeyboardApi implements NativeKeyListener {
    
    protected Set<KeyboardListener> listeners; 
    protected boolean ctrlDown = false;
    protected boolean optionDown = false;
    protected boolean appActive = false;
    
    public KeyboardApi(){
        try {
            // Get the logger for "org.jnativehook" and set the level to warning.
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.WARNING);

            // Don't forget to disable the parent handlers.
            logger.setUseParentHandlers(false);
            listeners = new HashSet<KeyboardListener>();
            GlobalScreen.registerNativeHook();
          } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
          }
          GlobalScreen.addNativeKeyListener(this);
    }
    
    public void addKeyboardTriggerListeren(KeyboardListener listener){
        listeners.add(listener);
    }
    
    public void nativeKeyPressed(NativeKeyEvent e) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode())+ " key code " + e.getKeyCode() );
        
        if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
            this.ctrlDown = true;
        }
        
        if (e.getKeyCode() == NativeKeyEvent.VC_ALT){
            this.optionDown = true;
        }
        
        if(ctrlDown && optionDown){
            fireEvent("open");
            this.appActive = true;
        }
        
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                try {
                        GlobalScreen.unregisterNativeHook();
                } catch (NativeHookException e1) {
                        e1.printStackTrace();
                }
            }
        }

	public void nativeKeyReleased(NativeKeyEvent e) {
            
            if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL){
                this.ctrlDown = false;
                fireEvent("close");
            }
            
            if (e.getKeyCode() == NativeKeyEvent.VC_ALT){
                this.optionDown = false;
                fireEvent("close");
            }
            
            if(ctrlDown && optionDown){
                System.out.println("Key Released with shift: " + NativeKeyEvent.getKeyText(e.getKeyCode()) +" key code: " +  e.getKeyCode());
                
                if(e.getKeyCode() == NativeKeyEvent.VC_Z && this.appActive){
                    System.out.println("go down");
                }

                if(e.getKeyCode() == NativeKeyEvent.VC_A && this.appActive){
                    System.out.println("go up");
                }
            }
            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()) +" key code: " +  e.getKeyCode());
	}
        
        //deze is undefined
	public void nativeKeyTyped(NativeKeyEvent e) {
	//	System.out.println("Key Typed: " + e );
	};
        
        public void handleOpenBar(){
        
        }
        
        public void fireEvent(String name){
            KeyboardEvent e = new KeyboardEvent(name);
            for (KeyboardListener listener : listeners) {
                listener.KeyboardTriggerd(e);
            }
        }
       
        
}