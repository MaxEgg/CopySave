/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class ClipboardItem {

    private Image image;
    private String text;
    private String html;
    private List<Image> images;
    private List<File> files;

    private DataType dataType;

    public ClipboardItem(DataType dataType) {
        this.dataType = dataType;
    }
    
    /**
     * Get the data type inserted into the clipboard
     * @return 
     */
    public DataType getDataType(){
        return this.dataType;
    }
    
    /**
     * Getter and setter for HTML
     * @return 
     */
    public String getHtml() {
        return html;
    }
    
    public void setHtml(String html){
        this.html = html;
    }
    
    /**
     * Getter and setter for TEXT
     * @return 
     */
    public String getText() {
        return text;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    /**
     * Getter and setter for IMAGE
     * @return 
     */
    public Image getImage() {
        return image;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Getter and setter for a list of files 
     */
    public void setImages(List<Image> images){
        this.images = images;
    }
    
    public void setFiles(List<File> files){
        this.files = files;
    }
    
    public List<Image> getImages(){
        return this.images;
    }
    
    public List<File> getFiles(){
        return this.files;
    }
   
    /**
     * Transform the image into an into for java FX
     * @return 
     */
    public WritableImage getImageFX() {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(this.image, 0, 0, null);
        graphics.dispose();

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    public static enum DataType {
        IMAGE,
        HTML,
        TEXT,
        IMAGELIST,
        ONSUPPORTED
    }
}
