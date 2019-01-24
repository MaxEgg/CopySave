package com.CopySave.controllers;

import com.CopySave.clipboard.*;
import com.CopySave.clipboard.ClipboardItem.DataType;
import com.CopySave.front.components.ClipboardItemView;
import com.CopySave.singleton.Context;
import com.CopySave.singleton.Preferences;
import com.CopySave.singleton.Stages;
import com.CopySave.sizedTreeMap.OverFlowEvent;
import com.CopySave.sizedTreeMap.SizedTreeMap;
import com.CopySave.sizedTreeMap.SizedTreeMapListener;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Map.Entry;


public class ClipboardController implements SizedTreeMapListener {

    private Preferences preferences = Preferences.getInstance();
    private Context context = Context.getInstance();
    private Stages stages = Stages.getInstance();

    private ScrollPane scrollPane;
    private VBox vbox;
    private Clipboard clipboard;
    private Stage stage;
    private Rectangle2D bounds;

    private int selectIndex;
    private int itemsAdded;

    private SizedTreeMap<Integer, ClipboardItem> clipboardItems = new SizedTreeMap<Integer, ClipboardItem>(preferences.getMaximumItems());
    private SizedTreeMap<Integer, ClipboardItemView> clipboardItemsViews = new SizedTreeMap<Integer, ClipboardItemView>(preferences.getMaximumItems());

    private ParallelTransition parallelTransition;
    private TranslateTransition translateTransition;

    /**
     * Controller for the clipboard object;
     */
    public ClipboardController() {
        try {
            System.out.println("Initiating clipboard");
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            System.out.println("Get toolkit ");
            System.out.println(toolkit);
            this.clipboard = toolkit.getSystemClipboard();
            System.out.println("Initiating clipboard view");
            initView();
            System.out.println("Initiating clipboard listener");
            initListener();
            this.stage = context.getStage();
            bounds = Screen.getPrimary().getVisualBounds();
            itemsAdded = 0;
            resetSelectedIndex();
            System.out.println("Initiating sized tree map listener");
            initSizedTreeMapListener();
        } catch (Exception e) {
            System.out.println(e);
        }
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

        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("scroll-pane");

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        this.vbox = new VBox();
        vbox.setStyle("-fx-background-color: transparent;");
        vbox.setSpacing(preferences.getItemSpacing());
        scrollPane.setContent(vbox);
    }

    /**
     * Initiate the clipboard content listener. When the event fires the addItem
     * function is called.
     */
    private void initListener() {
        ClipboardListener lis = (ClipboardEvent e) -> Platform.runLater(() -> {
            if (e.getClipboardItem().getDataType() == DataType.IMAGELIST) {
                List<Image> images = e.getClipboardItem().getImages();
                for (int x = 0; x < images.size(); ++x) {
                    ClipboardItem clipboardItem = new ClipboardItem(DataType.IMAGE);
                    clipboardItem.setImage(images.get(x));
                    clipboardItems.put(itemsAdded, clipboardItem);
                    addItem(clipboardItem);
                    ++itemsAdded;
                }
            } else {
                clipboardItems.put(itemsAdded, e.getClipboardItem());
                addItem(e.getClipboardItem());
                ++itemsAdded;
            }
        });

        ClipboardContent cc = new ClipboardContent();
        cc.addListener(lis);
        new Thread(cc).start();
    }

    /**
     * The add item function creates a new view and adds the content to the
     * clipboard
     *
     * @param item ClipboardItem The inserted object
     */
    public void addItem(ClipboardItem item) {
        ClipboardItemView clipboardItemView = new ClipboardItemView(itemsAdded, item);
        clipboardItemsViews.put(itemsAdded, clipboardItemView);
        AnchorPane itemView = clipboardItemView.getView();

        itemView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> Platform.runLater(() -> {
            if(context.isOpen()) {
                deselectCurrentItem();
                AnchorPane anchorPane = (AnchorPane) event.getSource();
                selectIndex = Integer.parseInt(anchorPane.getId().trim());
                selectNextItem();
            }
        }));

        vbox.getChildren().add(0, itemView);
        vbox.applyCss();
        vbox.layout();
        translateTransition = clipboardItemView.animate();

        double height = vbox.getBoundsInParent().getHeight();

        if (height > bounds.getHeight()) {
            height = bounds.getHeight();
        }

        Context.getInstance().setStageHeight(height);
        stage.setHeight(height);
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
                StringSelection stringClip = new StringSelection(clipboardItem.getText());
                clipboard.setContents(stringClip, null);
                break;
            case IMAGE:
                String imgText = clipboardItem.getText();
                ImageTransferable imageTransferable;

                if (imgText == null) {
                    imageTransferable = new ImageTransferable(clipboardItem.getImage());
                } else {
                    imageTransferable = new ImageTransferable(clipboardItem.getImage(), clipboardItem.getText());
                }

                clipboard.setContents(imageTransferable, null);
                break;
            case HTML:
                HtmlTransferable htmlTransferable = new HtmlTransferable(clipboardItem.getHtml(), clipboardItem.getText());
                clipboard.setContents(htmlTransferable, null);
                break;
            default:
                break;
        }
    }

    /**
     * Remove the selected clipboard component
     */
    public void removeClipboardContent() {
        int index = selectIndex;
        Platform.runLater(() -> {
            vbox.getChildren().remove(vbox.lookup("#" + index));
            clipboardItemsViews.remove(index);
            clipboardItems.remove(index);
            changeSelectIndex(false);
            selectNextItem();
        });
    }

    /**
     * Select index
     */
    public void changeSelectIndex(boolean direction) {
        try {
            if (direction) {
                selectIndex = clipboardItemsViews.higherKey(selectIndex);
            } else {
                selectIndex = clipboardItemsViews.lowerKey(selectIndex);
            }
        } catch (NullPointerException e) {}
    }

    /**
     * Select the first item on the list
     */
    public TranslateTransition selectFirstItem() {
        selectIndex = clipboardItemsViews.lastKey();
        ClipboardItemView view = clipboardItemsViews.get(selectIndex);

        return view.setSelected();
    }

    /**
     * Select next item
     */
    public void selectNext(boolean direction) {
        deselectCurrentItem();
        changeSelectIndex(direction);
        selectNextItem();
    }

    /**
     * Reset item view back to start position
     */
    public void resetItemViews() {
        if (selectIndex != -1) {
            select();
            resetSelectedIndex();
        }
    }

    public void select() {
        try {
            if (selectIndex != clipboardItemsViews.lastKey()) {
                ClipboardItemView selectedView = clipboardItemsViews.get(selectIndex);
                setClipboardContent(selectedView.getId());
                removeClipboardContent();
            }
        } catch (NullPointerException e) {}
    }

    public void delete() {
        try {
            removeClipboardContent();
        } catch (NullPointerException e) {}
    }

    private void deselectCurrentItem() {
        try {
            ClipboardItemView view = clipboardItemsViews.get(selectIndex);
            view.deSelect();
        } catch (NullPointerException e) {}
    }

    private void selectNextItem() {
        try {
            ClipboardItemView nextView = clipboardItemsViews.get(selectIndex);
            TranslateTransition slide = nextView.setSelected();
            slide.play();
        } catch (NullPointerException e) {}
    }

    private void resetSelectedIndex() {
        selectIndex = -1;
    }

    private void initSizedTreeMapListener() {
        SizedTreeMapListener lis = (OverFlowEvent e) -> {
            sizedTreeMapOverFlow(e);
        };
        clipboardItems.setSizedTreeListener(lis);
    }

    @Override
    public void sizedTreeMapOverFlow(OverFlowEvent e) {
        selectIndex = (int) e.key;
        removeClipboardContent();
    }

    public void animateOpen() {
        if (clipboardItemsViews.size() != 0) {
            int key = clipboardItemsViews.lastKey();

            if (parallelTransition != null) {
                parallelTransition.stop();
            }

            if (translateTransition != null) {
                translateTransition.stop();
            }

            parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(new PauseTransition(Duration.millis(100)));
            for (Entry<Integer, ClipboardItemView> entry : clipboardItemsViews.entrySet()) {
                TranslateTransition slide = entry.getValue().transitionOpen(200);

                if (key == entry.getKey()) {
                    parallelTransition.getChildren().add(slide);
                } else {
                    TranslateTransition slide2 = selectFirstItem();
                    SequentialTransition seq = new SequentialTransition(slide, slide2);
                    parallelTransition.getChildren().add(seq);
                }
            }

            parallelTransition.play();
        }
    }

    public void animateClose() {
        if (clipboardItemsViews.size() != 0) {
            int key = clipboardItemsViews.firstKey();

            if (parallelTransition != null) {
                parallelTransition.stop();
            }

            if (translateTransition != null) {
                translateTransition.stop();
            }

            parallelTransition = new ParallelTransition();

            for (Entry<Integer, ClipboardItemView> entry : clipboardItemsViews.entrySet()) {
                TranslateTransition slide = entry.getValue().transitionClose();

                if (key == entry.getKey()) {
                    slide.setOnFinished(event -> {
                        stages.closeDirect();
                        resetItemViews();
                        scrollPane.setVvalue(0.0);
                    });
                }

                parallelTransition.getChildren().add(slide);
            }

            parallelTransition.play();
        } else {
            stages.closeDirect();
        }
    }
}
