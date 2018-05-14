package clipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClipboardContent extends Thread {
    
    public final Clipboard clipboard;
    private ClipboardItem lastClipboardItem;
    private Set<ClipboardListener> clipboardListeners; 

    public ClipboardContent() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        lastClipboardItem = new ClipboardItem(null, null);
        clipboardListeners = new HashSet<ClipboardListener>();
    }
    
    /**
     * Add a listener to the clipboard events
     * @param listener 
     */
    public synchronized void addListener(ClipboardListener listener){
        clipboardListeners.add(listener);
    }
    
    /**
     * Remove a listener from the clipboard events
     * @param listener 
     */
    public synchronized void removeListener(ClipboardListener listener){
        clipboardListeners.remove(listener);
    }
    
    /**
     * Fire a clipboard events
     * @param listener 
     */
    public synchronized void fireClipboardEvent(){
       ClipboardEvent e = new ClipboardEvent(this, lastClipboardItem);
       for(ClipboardListener listener : clipboardListeners){
           System.out.println("fire" + listener);
           listener.contentChanged(e);
       }
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Transferable transferable = clipboard.getContents(null);
            if (checkContentChanged(transferable)) {
                //fire event 
                fireClipboardEvent();
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                Logger.getLogger(Clipboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    /**
     * Check if the content of the clipboard has changed and build a new
     * clipboard item.
     *
     * @param transferable
     * @return
     */
    private boolean checkContentChanged(Transferable transferable) {
        boolean textChanged = false;

        try {
            if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor) && !transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                if (!compareImages(img, lastClipboardItem.getImage())) {
                    lastClipboardItem = new ClipboardItem(null, img);
                    return true;
                }
            }

            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (!str.equals(lastClipboardItem.getText())) {
                    textChanged = true;
                }

                if (textChanged && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                    if (!compareImages(img, lastClipboardItem.getImage())) {
                        lastClipboardItem = new ClipboardItem(str, img);
                    } else {
                        lastClipboardItem = new ClipboardItem(str, null);
                    }
                    return true;
                } else if (textChanged) {
                    lastClipboardItem = new ClipboardItem(str, null);
                    return true;
                }
            }
        } catch (UnsupportedFlavorException | IOException | InterruptedException ex) {
            Logger.getLogger(Clipboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Check if images are equal.
     *
     * @param imgA
     * @param imgB
     * @return true if the images are equal
     */
    private boolean compareImages(Image imgA, Image imgB) throws InterruptedException {

        if (imgA == null || imgB == null) {
            return false;
        }

        int heightA = imgA.getHeight(null),
            widthA = imgA.getWidth(null),
            heightB = imgB.getHeight(null),
            widthB = imgB.getWidth(null);

        if (heightA != heightB || widthA != widthB) {
            return false;
        }
        
//       Krijg geen pixels terug van de pixelgrabber. 
//        PixelGrabber grabA = new PixelGrabber(imgA, 0, 0, -1, -1, false);
//        PixelGrabber grabB = new PixelGrabber(imgB, 0, 0, -1, -1, false);
//        
//        int[] dataA = (int[]) grabA.getPixels(),
//              dataB = (int[]) grabB.getPixels();
//
//        System.out.println("dataA: ");
//        System.out.println(imgA);
//        System.out.println(grabA);
//
//        
//        int loop = dataA.length,
//                x;
//
//        for (x = 0; x < loop; ++x) {
//            if (dataA[x] != dataB[x]) {
//                return false;
//            }
//        }

        return true;
    }
}
