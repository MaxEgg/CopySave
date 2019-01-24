/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.CopySave.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author max
 */
public class HtmlTransferable implements Transferable {
    private String html;
    private String plain;
    
    public HtmlTransferable(String html, String plain) {
        this.html = html;
        this.plain = plain;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavor) && flavor == DataFlavor.fragmentHtmlFlavor) {
            return html;
        } else if(isDataFlavorSupported(flavor) && flavor == DataFlavor.stringFlavor){
            return plain;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if(this.plain != null && !this.plain.isEmpty() && flavor == DataFlavor.stringFlavor){
            return true;
        }
        return flavor == DataFlavor.fragmentHtmlFlavor;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.fragmentHtmlFlavor, DataFlavor.stringFlavor};
    }
}
