package clipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClipboardItem;
import websockets.Connections;

public class ClipboardListener implements ClipboardOwner, Runnable{
   
    public final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    ArrayList<ClipboardItem> clipboardItems = new ArrayList<ClipboardItem>(); 
    
    public ClipboardListener(){
        clipboard.addFlavorListener(lis);
    }
    
    public ArrayList<ClipboardItem> getList(){
        return clipboardItems;
    }
    
    @Override
    public void run() {
        boolean running = true;
        
        while(running){
            clipboard.getContents(null);            
            try{
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClipboardListener.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            if (Thread.interrupted()) {
                clipboard.removeFlavorListener(lis);
                return;
            }
        }
    }
    
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        System.out.println("Clipboard contents replaced");   
    }
  
    FlavorListener lis = (FlavorEvent e) -> {
        String clipboardText = "";
        Image clipboardImage = null;
        ClipboardItem clipboardItem = null;
            
        try {
            clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
            clipboardItem = new ClipboardItem(true, clipboardText, null);
        } catch (UnsupportedFlavorException ex) {
             try {
                clipboardImage = (Image) clipboard.getData(DataFlavor.imageFlavor);
                clipboardItem = new ClipboardItem(false, null, clipboardImage);
            } catch (UnsupportedFlavorException | IOException exception ) {
                System.out.println("No image or text #wtf");
            }          
        } catch ( IOException exceptioneqwe ) {
          System.out.println("Everything is going stuk. Time to repair something!");
        }
        
        if(clipboardItem != null){
            this.clipboardItems.add(clipboardItem);
        }
        
        System.out.println("The clipboard contains: " + clipboardText);
    };
}