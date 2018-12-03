package copysave.front.components;

import copysave.clipboard.ClipboardItem;
import copysave.clipboard.ClipboardItem.DataType;
import copysave.common.OnFinish.OnFinish;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import copysave.singleton.Settings;
import copysave.singleton.Stages;

public class ClipboardItemView extends OnFinish {
    
    private AnchorPane holder;
    private AnchorPane anchorPane;
    private ClipboardItem item;
    private Stage stage;
    private Settings settings = Settings.getInstance();
    private Stages stages = Stages.getInstance();
    private Rectangle rect;
    
    private double height = settings.itemHeight,
                   width = settings.itemWidth;
    
    public ClipboardItemView(int index, ClipboardItem item){
        Settings settings =  Settings.getInstance();
        stage = settings.stage;
        holder = new AnchorPane();
        
        Pane pane = new Pane();
        String backgroundColor = "ffffff";
        DataType dataType = item.getDataType();
        
        switch(dataType){
            case TEXT:
                Text text = new Text();
                text.setY(15);
                text.setX(15);
                text.setText(item.getText());
                pane.getChildren().add(text);
                height = text.minHeight(-1) + 5;
                
                break;
            case IMAGE:
                ImageView imageView = new ImageView();
                imageView.setFitWidth(width);
                
                Image img = item.getImageFX();
                imageView.setImage(img);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                pane.getChildren().add(imageView);
                
                int imageWidth =(int) img.getWidth();
                int imageHeight = (int) img.getHeight();
                double ratio = (imageWidth/width);
                height = (imageHeight / ratio)  ;
                
                break;
            case HTML:
                WebView webview = new WebView();
                webview.setDisable(true);
                WebEngine engine =  webview.getEngine();
                
                engine.loadContent(item.getHtml());
                String webText = item.getText();
                
                Text textHeight = new Text();
                textHeight.setWrappingWidth(800);
                textHeight.setText(item.getText());
                
                if(webText != null){
                    Pattern p = Pattern.compile("font-size: ([0-9]+)px");
                    Matcher m = p.matcher(item.getHtml());
                    List<Integer> pixels = new ArrayList<Integer>();
                    
                    while (m.find()) {
                        Integer pixel = Integer.parseInt(m.group(1));
                        pixels.add(pixel);
                    }
                    
                    if(pixels.size() > 0){
                        Collections.sort(pixels);
                        int fontsize = pixels.get(pixels.size() -1) > 12 ? pixels.get(pixels.size() -1) :  12 ;
                        textHeight.setFont(Font.font(null, FontWeight.NORMAL, fontsize));
                    }
                    com.sun.javafx.webkit.Accessor.getPageFor(engine).setBackgroundColor(0);
                    height = textHeight.minHeight(-1) + 15;
                    textHeight = null;
                }
                
                Pattern p2 = Pattern.compile("background-color:(.+?);");
                Matcher m2 = p2.matcher(item.getHtml());
                
                while (m2.find()) {
                    backgroundColor = m2.group(1);
                    break;
                }
                pane.getChildren().add(webview);
                
                break;
        }
        
        if(height > settings.itemMaxHeight){
            height = settings.itemMaxHeight;
        }
        
        holder.setStyle("-fx-background-color: transparent;");
        holder.setPrefSize(width * 2  , height);
        holder.setId(""+index);
        
        pane.setPrefSize(width, height);
        rect = new Rectangle(width, height);
        pane.setClip(rect);
        pane.setStyle("-fx-background-color: "+backgroundColor+"; -fx-border-radius: 3px;  -fx-background-radius: 3px;");

        this.anchorPane = new AnchorPane();
        anchorPane.setPrefSize(width, height);
        anchorPane.getChildren().add(pane);
        anchorPane.setStyle("-fx-border-radius: 3px; -fx-background-radius: 3px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 4, 0, 0, 0);");
        
        holder.setLeftAnchor(anchorPane, (double)settings.shadowSpacing);
        holder.getChildren().add(anchorPane);
    }
    
    public AnchorPane getView() {
        return holder;
    }
    
    public void animate(){
        stages.openDirect();

        TranslateTransition tt = new TranslateTransition(new Duration(1000), anchorPane);
        tt.setOnFinished(e -> {
            TranslateTransition slide = transitionClose();
            slide.setOnFinished(event -> {
                stages.closeDirect();
            });
            slide.play();
        });
        tt.play();
    }
    
    /**
     * Set selected item
     */
    public TranslateTransition setSelected(){
        TranslateTransition slide = new TranslateTransition(new Duration(50), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(settings.shadowSpacing);
        return slide;
    }
    
    /**
     * DeselectItemView
     */
    public void deSelect(){
        TranslateTransition slide = new TranslateTransition(new Duration(50), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(settings.selectedWidth + settings.shadowSpacing);
        slide.play();
    }
    
    public int getId(){
        return Integer.parseInt(holder.getId());
    }
    
    public TranslateTransition transitionClose(){        
        TranslateTransition slide = new TranslateTransition(new Duration(settings.closeSpeed), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(width + settings.selectedWidth + settings.shadowSpacing);
        return slide;
    }
    
    public TranslateTransition transitionOpen(int duration){        
        TranslateTransition slide = new TranslateTransition(new Duration(duration), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(settings.selectedWidth + settings.shadowSpacing);
        return slide;
    }
}
