/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import clipboard.ClipboardContent;
import clipboard.ClipboardEvent;
import clipboard.ClipboardItem;
import clipboard.ClipboardItem.DataType;
import clipboard.ClipboardListener;
import clipboard.HtmlTransferable;
import clipboard.ImageTransferable;
import front.components.ClipboardItemView;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ClipboardController {
        
    private ScrollPane scrollPane;
    private VBox vbox;   
    private List<ClipboardItem> clipboardItems;
    private Clipboard clipboard;

    /**
     * Controller for the clipboard object;
     */
    public ClipboardController(){
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.clipboardItems = new ArrayList<>();
        initView();
        initListener();
    }
    
    /**
     * Get the view
     * @return 
     */
    public ScrollPane getView(){
        return scrollPane;
    }
    
    /**
     * Initiate the view component.
     */
    private void initView(){
        this.scrollPane = new ScrollPane();
        scrollPane.setPrefViewportHeight(1000);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);

        this.vbox = new VBox();
        vbox.setStyle("-fx-background-color: transparent;");
        vbox.setSpacing(5);
        scrollPane.setContent(vbox);
    }
    
    /**
     * Initiate the clipboard content listener. When the event fires the addItem function is called.
     */
    private void initListener(){
        ClipboardListener lis = (ClipboardEvent e) -> {
            clipboardItems.add(e.getClipboardItem());
            int index = clipboardItems.size() -1;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addItem(index, e.getClipboardItem());
                }
            });
        };
        
        ClipboardContent cc = new ClipboardContent();
        cc.addListener(lis);
        new Thread(cc).start();
    }
    
    /**
     * The add item function created a new view and adds the content to the clipboard
     * @param index id of the object so it can be found inside the clipboardItems list.
     * @param item The object inserted
     */
    public void addItem(int index, ClipboardItem item){
        ClipboardItemView clipboardItemView = new ClipboardItemView(index, item);
        AnchorPane itemView = clipboardItemView.getView();
       
        itemView.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
                 Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       AnchorPane anchorPane = (AnchorPane) event.getSource();
                       int id = Integer.parseInt(anchorPane.getId().trim());
                       setClipboardContent(id);
                   }
               });
           }
       });
       vbox.getChildren().add(0, itemView);
    }
    
    /**
     * Set the clipboard content to the clicked object
     * @param index 
     */
    public void setClipboardContent(int index){
        ClipboardItem clipboardItem = clipboardItems.get(index);
        
        DataType dataType = clipboardItem.getDataType();
        
        switch(dataType){
            case TEXT:
                StringSelection stringclip = new StringSelection(clipboardItem.getText());
                clipboard.setContents(stringclip, null);
                break;
            case IMAGE:
                ImageTransferable imageTransferable = new ImageTransferable(clipboardItem.getImage(), clipboardItem.getText());
                clipboard.setContents(imageTransferable, null);
                break;
            case HTML:
                HtmlTransferable htmlTransferable = new HtmlTransferable(clipboardItem.getHtml(), clipboardItem.getText());
                clipboard.setContents(htmlTransferable, null);
                break;
        }
    }
    
    /**
     * Get the clipboard list
     * @return 
     */
    public List<ClipboardItem> getList(){
        return clipboardItems;
    }
}

