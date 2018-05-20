/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author max
 */
public class ImageTransferable implements Transferable {

    private Image image;
    private String text;
    
    public ImageTransferable(Image image, String text) {
        this.image = image;
        this.text = text;
    }

    public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException {
            
        if (isDataFlavorSupported(flavor) && DataFlavor.imageFlavor == flavor){
            return image;
        } else if(isDataFlavorSupported(flavor) && DataFlavor.stringFlavor == flavor){
            return text;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if(this.text != null &&!this.text.equals(null) && !this.text.isEmpty() && flavor == DataFlavor.stringFlavor ){
            return true;
        }
        return flavor == DataFlavor.imageFlavor;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor, DataFlavor.stringFlavor};
    }
}
