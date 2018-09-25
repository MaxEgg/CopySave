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
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.MouseInfo;
import javafx.event.EventType;
import mouse.MouseFollower;
import singleton.Settings;

public class ClipboardController {

    private ScrollPane scrollPane;
    private VBox vbox;
    private List<ClipboardItem> clipboardItems = new ArrayList<>();;
    private Clipboard clipboard;
    private Stage stage;
    private AnchorPane root;
    Rectangle2D bounds;
    private MouseFollower mouseFollower;
    private Settings settings = Settings.getInstance();
    /**
     * Controller for the clipboard object;
     */
    public ClipboardController(AnchorPane root) {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        initView();
        initListener();
        this.stage = settings.stage;
        this.root = root;
        bounds = Screen.getPrimary().getVisualBounds();
    }

    /**
     * Get the view
     *
     * @return
     */
    public ScrollPane getView() {
        return scrollPane;
    }

    /**
     * Initiate the view component.
     */
    private void initView() {
        this.scrollPane = new ScrollPane();
//        scrollPane.setPrefViewportHeight(10000);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        this.vbox = new VBox();
        vbox.setStyle("-fx-background-color: transparent;");
        vbox.setSpacing(2);
        scrollPane.setContent(vbox);
    }

    /**
     * Initiate the clipboard content listener. When the event fires the addItem
     * function is called.
     */
    private void initListener() {
        ClipboardListener lis = (ClipboardEvent e) -> {
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (e.getClipboardItem().getDataType() == DataType.IMAGELIST) {
                        List<Image> images = e.getClipboardItem().getImages();
                        for (int x = 0; x < images.size(); ++x) {
                            ClipboardItem clipboardItem = new ClipboardItem(DataType.IMAGE);
                            clipboardItem.setImage(images.get(x));
                            clipboardItems.add(clipboardItem);
                            addItem(clipboardItem);
                        }
                    } else {
                        clipboardItems.add(e.getClipboardItem());
                        addItem(e.getClipboardItem());
                    }
                }
            });
        };

        ClipboardContent cc = new ClipboardContent();
        cc.addListener(lis);
        new Thread(cc).start();
    }

    /**
     * The add item function created a new view and adds the content to the
     * clipboard
     *
     * @param index id of the object so it can be found inside the
     * clipboardItems list.
     * @param item The object inserted
     */
    public void addItem(ClipboardItem item) {
        int index = clipboardItems.size() - 1;
        ClipboardItemView clipboardItemView = new ClipboardItemView(index, item);
        AnchorPane itemView = clipboardItemView.getView();

        itemView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() % 2 == 0){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AnchorPane anchorPane = (AnchorPane) event.getSource();
                            int id = Integer.parseInt(anchorPane.getId().trim());
                            setClipboardContent(id);
                        }
                    });
                }
            }
        });

        vbox.getChildren().add(0, itemView);
        vbox.applyCss();
        vbox.layout();

        double height = vbox.getParent().getBoundsInParent().getHeight();
        System.out.println("height "+ height +" / "+ bounds.getHeight() );
        if (height > bounds.getHeight()) {
            height = bounds.getHeight();
        }
        Settings.getInstance().stageHeight = height;
        this.stage.setHeight(height);
    }

    /**
     * Set the clipboard content to the clicked object
     *
     * @param index
     */
    public void setClipboardContent(int index) {
        ClipboardItem clipboardItem = clipboardItems.get(index);
        DataType dataType = clipboardItem.getDataType();

        switch (dataType) {
            case TEXT:
                System.out.println("TEXT" + clipboardItem.getText());
                StringSelection stringclip = new StringSelection(clipboardItem.getText());
                clipboard.setContents(stringclip, null);
                break;
            case IMAGE:
                String imgText = clipboardItem.getText();
                ImageTransferable imageTransferable;
                
                if (imgText == null) {
                    imageTransferable = new ImageTransferable(clipboardItem.getImage());
                } else {
                    imageTransferable = new ImageTransferable(clipboardItem.getImage(), clipboardItem.getText());
                }
                
                System.out.println("IMAGE" + clipboardItem.getText() + " " + clipboardItem.getImage() );
                clipboard.setContents(imageTransferable, null);
                break;
            case HTML:
                System.out.println("HMTL" + clipboardItem.getText() + " " + clipboardItem.getHtml() );
                HtmlTransferable htmlTransferable = new HtmlTransferable(clipboardItem.getHtml(), clipboardItem.getText());
                clipboard.setContents(htmlTransferable, null);
                break;
        }
    }

    /**
     * Get the clipboard list
     *
     * @return
     */
    public List<ClipboardItem> getList() {
        return clipboardItems;
    }
}
