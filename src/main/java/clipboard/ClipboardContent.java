package clipboard;

//import javafx.scene.image.Image;
import clipboard.ClipboardItem.DataType;
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

public class ClipboardContent implements Runnable {

    public final Clipboard clipboard;
    private ClipboardItem lastClipboardItem;
    private Set<ClipboardListener> clipboardListeners;

    public ClipboardContent() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        lastClipboardItem = new ClipboardItem(DataType.ONSUPPORTED);
        clipboardListeners = new HashSet<ClipboardListener>();
    }

    /**
     * Add a listener to the clipboard events
     *
     * @param listener
     */
    public synchronized void addListener(ClipboardListener listener) {
        clipboardListeners.add(listener);
    }

    /**
     * Remove a listener from the clipboard events
     *
     * @param listener
     */
    public synchronized void removeListener(ClipboardListener listener) {
        clipboardListeners.remove(listener);
    }

    /**
     * Fire a clipboard events
     *
     * @param listener
     */
    public synchronized void fireClipboardEvent() {
        ClipboardEvent e = new ClipboardEvent(this, lastClipboardItem);
        for (ClipboardListener listener : clipboardListeners) {
            listener.contentChanged(e);
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {

            try {
                Transferable transferable = clipboard.getContents(null);

                if (!transferable.equals(null) && checkContentChanged(transferable)) {
                    fireClipboardEvent();
                }

                Thread.sleep(750);
            } catch (InterruptedException ex) {
                Logger.getLogger(Clipboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(ClipboardContent.class.getName()).log(Level.SEVERE, null, ex);
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
    private boolean checkContentChanged(Transferable transferable) throws UnsupportedFlavorException, IOException, InterruptedException {

        DataType dataType = checkType(transferable);

        switch (dataType) {

            case HTML:
                String html = (String) transferable.getTransferData(DataFlavor.fragmentHtmlFlavor);
                if (!html.equals(lastClipboardItem.getHtml())) {
                    lastClipboardItem = new ClipboardItem(DataType.HTML);
                    lastClipboardItem.setHtml(html);
                    if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        lastClipboardItem.setText(text);
                    }
                    return true;
                }
                break;

            case IMAGE:
                Image img = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                if (!compareImages(img, lastClipboardItem.getImage())) {
                    lastClipboardItem = new ClipboardItem(DataType.IMAGE);
                    lastClipboardItem.setImage(img);
                    if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        lastClipboardItem.setText(text);
                    }
                    return true;
                }
                break;

            case TEXT:
                String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (!str.equals(lastClipboardItem.getText())) {
                    lastClipboardItem = new ClipboardItem(DataType.TEXT);
                    lastClipboardItem.setText(str);
                    return true;
                }
                break;

            default:
                return false;
        }

        return false;
    }

    /**
     * Figure out data flavor of the transferable. The order is important.
     */
    private DataType checkType(Transferable transferable) throws UnsupportedFlavorException, IOException {

        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if(text.indexOf("http") == 0){
                    return DataType.IMAGE;
                }else{
                    return DataType.ONSUPPORTED;
                }
            }
            return DataType.IMAGE;
        }

        if (transferable.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor)) {
            return DataType.HTML;
        }

        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return DataType.TEXT;
        }
        System.out.println("UNSupoorted");
        return DataType.ONSUPPORTED;
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

        return true;
    }
}
