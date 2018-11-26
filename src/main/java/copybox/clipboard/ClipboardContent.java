package copybox.clipboard;

//import javafx.scene.image.Image;
import copybox.clipboard.ClipboardItem.DataType;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import copybox.singleton.Settings;

public class ClipboardContent implements Runnable {

    public final Clipboard clipboard;
    private ClipboardItem lastClipboardItem;
    private Set<ClipboardListener> clipboardListeners;
    private Settings settings = Settings.getInstance();

    public ClipboardContent() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        lastClipboardItem = new ClipboardItem(DataType.ONSUPPORTED);
        clipboardListeners = new HashSet<ClipboardListener>();
    }

    /**
     * Add a listener to the clipboard events
     * @param listener
     */
    public synchronized void addListener(ClipboardListener listener) {
        clipboardListeners.add(listener);
    }

    /**
     * Remove a listener from the clipboard events
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

                Thread.sleep(settings.clipboardTimer);
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
                        System.out.println("string" + text);
                        lastClipboardItem.setText(text);
                    }
                    return true;
                }
                break;

            case TEXT:
                String str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                if (!str.trim().equals("") && !str.equals(lastClipboardItem.getText())) {
                    lastClipboardItem = new ClipboardItem(DataType.TEXT);
                    lastClipboardItem.setText(str);
                    return true;
                }
                break;
                
            case IMAGELIST:
                List files = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                   
                List<File> oldFiles = lastClipboardItem.getFiles();
                String filenames = null;
                
                if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    filenames = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                }
           
                if( oldFiles == null || 
                    (oldFiles.isEmpty() && !files.isEmpty()) ||
                    files.size() != oldFiles.size() ||
                    filenames != lastClipboardItem.getText()
                ){
                    if(filenames != null && !filenames.trim().equals(lastClipboardItem.getText())){
                        List<Image> images = new ArrayList();
                        for(int x = 0; x < files.size(); ++x){
                            Pattern p = Pattern.compile(".jpeg+$|.png+$|.jpg+$");
                            Matcher m = p.matcher(files.get(x).toString());
                            if(m.find()){
                                Image fileImg = ImageIO.read((File)files.get(x));
                                images.add(fileImg);
                            }
                        }
                        
                        if(images.size() > 0 ){
                            lastClipboardItem = new ClipboardItem(DataType.IMAGELIST);
                            lastClipboardItem.setImages(images);
                            lastClipboardItem.setFiles(files);
                            lastClipboardItem.setText(filenames);
                            return true;
                        }
                    }
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
        if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
            return DataType.IMAGELIST;
        }
        else 
        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            return DataType.IMAGE;
        }
        else
        if (transferable.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor)) {
            return DataType.HTML;
        }
        else
        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return DataType.TEXT;
        }

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
