package model;

import java.awt.Image;


public class ClipboardItem {
    
    boolean isText;
    String textContent;
    Image image;
   
    /**
     * Clipboard item
     * @param isText
     * @param textContent
     * @param image 
     */
    public ClipboardItem(boolean isText, String textContent , Image image){
       this.isText = isText;
       this.textContent = textContent;
       this.image = image;
    }
    
    public ClipboardItem getItem(){
        return this;
    }
    
    public Object getContent(){
       return this.isText ? this.textContent : this.image;
    }
    
    public boolean isText(){
        return this.isText;
    }
}
