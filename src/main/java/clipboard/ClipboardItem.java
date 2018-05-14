/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import java.awt.Image;


public class ClipboardItem {
    
    private Image image; 
    private String text;

    public ClipboardItem(String text, Image image){
        this.text = text;
        this.image = image;
    }
    
    public String getText(){
        return text;
    }
    
    public Image getImage(){
        return image;
    }
}
