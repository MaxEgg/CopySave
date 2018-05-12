
package clipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author max
 */
public class ClipboardListener2 extends Thread implements ClipboardOwner{
    
    private Clipboard system_clipboard;
    private Set history;
    
    private Transferable lastTransferable = null;
    
    public ClipboardListener2() {
        system_clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        history = new LinkedHashSet<String>(); 
    }

    @Override
    public void run() {
        retakeOwnership();

        boolean running = true;
        while(!Thread.interrupted()){
              
            system_clipboard.getContents(null);
   
             try {
                Thread.sleep(300);
             } catch (InterruptedException ex) {
                 Logger.getLogger(ClipboardListener2.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        final int SLEEP_TIME = 250;
            System.out.println("Lost ownership");
        try {
            sleep(SLEEP_TIME);
        } catch (InterruptedException ex) {
            ex.printStackTrace(); // TODO: change to something else
        }

        retakeOwnership();
    }

    public void removeFromHistory(final String str) {
        history.remove(str);
    }

    public void setCurrentValue(final String str) {
        if (history.contains(str)){
            system_clipboard.setContents(new StringSelection(str), this);
        }
    }

    private void retakeOwnership() {
        Transferable new_contents = system_clipboard.getContents(this);
        addToHistory(new_contents);
        system_clipboard.setContents(new_contents, this);
    }

    private void addToHistory(final Transferable transferable) {
        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                final String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                System.out.println("Add string " + str);

            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace(); // TODO: change to something else
            }
        }
        
        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                final Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace(); // TODO: change to something else
            }
        }
    }

}