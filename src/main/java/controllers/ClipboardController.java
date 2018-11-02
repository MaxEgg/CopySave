package controllers;

import clipboard.ClipboardContent;
import clipboard.ClipboardEvent;
import clipboard.ClipboardItem;
import clipboard.ClipboardItem.DataType;
import clipboard.ClipboardListener;
import clipboard.HtmlTransferable;
import clipboard.ImageTransferable;
import common.SizedTreeMap.OverFlowEvent;
import common.SizedTreeMap.SizedTreeMap;
import front.components.ClipboardItemView;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mouse.MouseFollower;
import singleton.Settings;
import common.SizedTreeMap.SizedTreeMapListener;
import java.util.Map.Entry;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import singleton.Stages;

public class ClipboardController implements SizedTreeMapListener {
    
    private ScrollPane scrollPane;
    private VBox vbox;
    private Clipboard clipboard;
    private Stage stage;
    private VBox root;
    Rectangle2D bounds;
    private MouseFollower mouseFollower;
    private Settings settings = Settings.getInstance();
    private Stages stages = Stages.getInstance();
    
    private int selectIndex;
    private int itemsAdded;
    
    private SizedTreeMap<Integer, ClipboardItem> clipboardItems = new SizedTreeMap<Integer, ClipboardItem>(settings.maxItems);
    private SizedTreeMap<Integer, ClipboardItemView> clipboardItemsViews = new SizedTreeMap<Integer, ClipboardItemView>(settings.maxItems);
    
    /**
     * Controller for the clipboard object;
     */
    public ClipboardController(VBox root) {
        System.out.println("Initiating clipboard");
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        initView();
        initListener();
        this.stage = settings.stage;
        this.root = root;
        bounds = Screen.getPrimary().getVisualBounds();
        itemsAdded = 0;
        resetSelectedIndex();
        initSizedTreeMapListener();
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
                            clipboardItems.put(itemsAdded,clipboardItem);
                            addItem(clipboardItem);
                        }
                    } else {
                        clipboardItems.put(itemsAdded, e.getClipboardItem());
                        addItem(e.getClipboardItem());
                    }
                    ++itemsAdded;
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
        ClipboardItemView clipboardItemView = new ClipboardItemView(itemsAdded, item);
        clipboardItemsViews.put(itemsAdded, clipboardItemView);
        AnchorPane itemView = clipboardItemView.getView();
        
        itemView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() % 1 == 0){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            deselectCurrentItem();
                            AnchorPane anchorPane = (AnchorPane) event.getSource();
                            selectIndex = Integer.parseInt(anchorPane.getId().trim());
                            selectNextItem();
                        }
                    });
                }
            }
        });
        
        vbox.getChildren().add(0, itemView);
        vbox.applyCss();
        vbox.layout();
        clipboardItemView.animate();
        
        double height = vbox.getBoundsInParent().getHeight();
        
        if (height > bounds.getHeight()) {
            height = bounds.getHeight();
        }
        
        Settings.getInstance().stageHeight = height;
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
     * Remove the selected clipboard component
     */
    public void removeClipboardContent(){
        int index = selectIndex;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().remove(vbox.lookup("#"+index));
                clipboardItemsViews.remove(index);
                clipboardItems.remove(index);
                changeSelectIndex(false);
                selectNextItem();
            }
        });
    }
    
    /**
     * Select index
     */
    public void changeSelectIndex(boolean direction){
        try{
            if(direction){
                selectIndex = clipboardItemsViews.higherKey(selectIndex);
            }else{
                selectIndex = clipboardItemsViews.lowerKey(selectIndex);
            }
        }catch(NullPointerException e){
            System.out.println("hit the roof or the bottom");
        }
    }
    
    /**
     * Select the first item on the list
     */
    public TranslateTransition selectFirstItem(){
        selectIndex = clipboardItemsViews.lastKey();
        ClipboardItemView view = clipboardItemsViews.get(selectIndex);
        return view.setSelected();
    }
    
    /**
     * Select next item
     */
    public void selectNext(boolean direction){
        deselectCurrentItem();
        changeSelectIndex(direction);
        selectNextItem();
    }
    
    /**
     * Reset item view back to start position
     */
    public void resetItemViews(){
        if(selectIndex != -1){
            select();
            resetSelectedIndex();
        }
    }
    
    public void select() {
        try{
            ClipboardItemView selectedView = clipboardItemsViews.get(selectIndex);
            setClipboardContent(selectedView.getId());
            if(selectIndex != clipboardItemsViews.lastKey()){
                removeClipboardContent();
            }
        }catch(NullPointerException e){
            System.out.println("hit the roof or the bottom");
        }
    }
    
    public void delete(){
        try{
            removeClipboardContent();
        }catch(NullPointerException e){
            System.out.println("Nullpointer on removing item");
        }
    }
    
    private void deselectCurrentItem(){
        try{
            ClipboardItemView view = clipboardItemsViews.get(selectIndex);
            view.deSelect();
        }catch(NullPointerException e){
            System.out.println("Nullpointer on removing item");
        }
    }
    
    private void selectNextItem(){
        try{
            ClipboardItemView nextView = clipboardItemsViews.get(selectIndex);
            TranslateTransition slide = nextView.setSelected();
            slide.play();
        }catch(NullPointerException e){
            System.out.println("hit the roof or the bottom");
        }
    }
    
    private void resetSelectedIndex(){
        selectIndex = -1;
    }
    
    private void initSizedTreeMapListener(){
        SizedTreeMapListener lis = (OverFlowEvent e) -> {
            SizedTreeMapOverFlow(e);
        };
        clipboardItems.setSizedTreeListener(lis);
    }
    
    @Override
    public void SizedTreeMapOverFlow(OverFlowEvent e) {
        selectIndex = (int)e.key;
        removeClipboardContent();
    }
    
    public void animateOpen(){
        int key = clipboardItemsViews.lastKey();
        
        for(Entry<Integer, ClipboardItemView> entry : clipboardItemsViews.entrySet()){
            TranslateTransition slide =  entry.getValue().transitionOpen(200);
            
            if(key == entry.getKey()){
                slide.play();
            }else{
                TranslateTransition slide2 =  selectFirstItem();
                SequentialTransition seq = new SequentialTransition(slide,slide2);
                seq.play();
            }
        }
    }
    
    void animateClose() {
        int key = clipboardItemsViews.firstKey();
        
        for(Entry<Integer, ClipboardItemView> entry : clipboardItemsViews.entrySet()){
            TranslateTransition slide = entry.getValue().transitionClose();
            
            if(key == entry.getKey()){
                slide.setOnFinished(event -> {
                    stages.closeDirect();
                    resetItemViews();
                    scrollPane.setVvalue(0.0);
                });
            }
            
            slide.play();
        }
    }
}
