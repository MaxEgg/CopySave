/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package front.components;

import clipboard.ClipboardItem;
import clipboard.ClipboardItem.DataType;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
//import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author max
 */
public class ClipboardItemView {
    
    private AnchorPane holder;
    private AnchorPane anchorPane;
    private ClipboardItem item;
    
    private int height = 100,
                width = 200;
    
    
    public ClipboardItemView(int index, ClipboardItem item){
       
        this.holder = new AnchorPane();
        holder.setStyle("-fx-background-color: transparent; ");
        holder.setPrefSize(width * 2 , height);
        holder.setId(""+index);
        Pane pane = new Pane();
        pane.setPrefSize(width, height);
        Rectangle rect = new Rectangle(width, height);
        pane.setClip(rect);
        pane.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #ececec);");
        
        DataType dataType = item.getDataType();
        
        switch(dataType){
            case TEXT: 
                    Text text = new Text();
                    text.setY(15);
                    text.setX(15);
                    text.setText(item.getText());
                    pane.getChildren().add(text);
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
                break;
            case HTML:                    
                    WebView webview = new WebView();
                    webview.setDisable(true);
                    WebEngine engine =  webview.getEngine();
                    engine.loadContent(item.getHtml());            
                    pane.getChildren().add(webview);
                    
                break;
        }
 
        this.anchorPane = new AnchorPane();
        anchorPane.setPrefSize(width, height);
        anchorPane.getChildren().add(pane);
        holder.setRightAnchor(anchorPane, 0d);
        holder.getChildren().add(anchorPane);
    }
    
    public AnchorPane getView(){
        TranslateTransition slide = new TranslateTransition(new Duration(600), anchorPane);
        slide.setInterpolator(Interpolator.EASE_OUT);
        slide.setToX(width * -1);
        slide.play();
        return holder;
    }
}
