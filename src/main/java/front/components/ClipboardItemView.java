/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package front.components;

import clipboard.ClipboardItem;
import clipboard.ClipboardItem.DataType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
//import org.fxmisc.richtext.StyleClassedTextArea;
import singleton.Settings;

/**
 *
 * @author max
 */
public class ClipboardItemView {
    
    private AnchorPane holder;
    private AnchorPane anchorPane;
    private ClipboardItem item;
    private Stage stage;
    private Settings settings = Settings.getInstance();
    
    private double height = settings.itemHeight,
                    width = settings.itemWidth;
    
    
    public ClipboardItemView(int index, ClipboardItem item){
        Settings settings =  Settings.getInstance();
        this.stage = settings.stage;
        this.holder = new AnchorPane();
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
                    imageView.setCache(true);
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
        
        holder.setStyle("-fx-background-color: transparent; ");
        holder.setPrefSize(width * 2  , height);
        holder.setId(""+index);
     
        pane.setPrefSize(width, height);
        Rectangle rect = new Rectangle(width, height);
        pane.setClip(rect);
        pane.setStyle("-fx-background-color: "+backgroundColor+";");
        
        this.anchorPane = new AnchorPane();
        anchorPane.setPrefSize(width, height);
        anchorPane.getChildren().add(pane);
        
        holder.setLeftAnchor(anchorPane, 0d);
        //holder.setRightAnchor(anchorPane, 0d);
        holder.getChildren().add(anchorPane);
    }
    
    public AnchorPane getView() {
        this.stage.setWidth(width);
        this.stage.setHeight(height);

        TranslateTransition tt = new TranslateTransition(new Duration(1000), anchorPane);
        tt.setOnFinished(e ->{
            TranslateTransition slide = new TranslateTransition(new Duration(800), anchorPane);
            slide.setInterpolator(Interpolator.EASE_IN);
            slide.setToX(width);
            slide.setOnFinished(e2 -> {
                this.stage.setWidth(width);
                this.stage.setHeight(height);
            });
            slide.play();
        });
        tt.play();
        return holder;
    }
}
