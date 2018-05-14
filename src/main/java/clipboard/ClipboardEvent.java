
package clipboard;

import java.awt.Image;

public class ClipboardEvent {
    
    private ClipboardContent source;
    private ClipboardItem clipboardItem;
    
    public ClipboardEvent(ClipboardContent source, ClipboardItem clipboardItem){
        this.source = source;
        this.clipboardItem = clipboardItem;
    }
    
    /**
     * Returns the clipboard item
     * @return ClipboardItem
     */
    public ClipboardItem getClipboardItem(){
        return clipboardItem;
    }
    
    /** 
     * Returns the text from the last clipboardItem 
     * @return 
     */
    public Image getImage(){
        return clipboardItem.getImage();
    }
    
    /** 
     * Returns the text from the last clipboardItem 
     * @return 
     */
    public String getText(){
        return clipboardItem.getText();
    }
    
    /**
     * Get the source from the event
     * @return 
     */
    public ClipboardContent getSource(){
        return source;
    }
}
