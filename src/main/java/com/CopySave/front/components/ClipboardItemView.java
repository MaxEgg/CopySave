package com.CopySave.front.components;

import com.CopySave.clipboard.ClipboardItem;
import com.CopySave.clipboard.ClipboardItem.DataType;
import com.CopySave.common.OnFinish;
import com.CopySave.singleton.Preferences;
import com.CopySave.singleton.Stages;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClipboardItemView extends OnFinish {

    private AnchorPane holder;
    private AnchorPane anchorPane;
    private Stages stages = Stages.getInstance();
    private Preferences preferences = Preferences.getInstance();

    private Rectangle rect;

    private double height = preferences.getItemMinimumHeight(),
            width = preferences.getItemWidth();

    public ClipboardItemView(int index, ClipboardItem item) {
        this.holder = new AnchorPane();
        Pane pane = new Pane();
        String backgroundColor = "ffffff";
        DataType dataType = item.getDataType();

        switch (dataType) {
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

                int imageWidth = (int) img.getWidth();
                int imageHeight = (int) img.getHeight();
                double ratio = (imageWidth / width);
                height = (imageHeight / ratio);

                break;
            case HTML:
                WebView webView = new WebView();
                webView.setContextMenuEnabled(false);
                webView.setDisable(true);

                WebEngine webEngine = webView.getEngine();
                webEngine.setJavaScriptEnabled(false);

                String webText = item.getText();

                Text textHeight = new Text();
                textHeight.setWrappingWidth(800);
                textHeight.setText(item.getText());

                if (webText != null) {
                    Pattern p = Pattern.compile("font-size: ([0-9]+)px");
                    Matcher m = p.matcher(item.getHtml());
                    List<Integer> pixels = new ArrayList<Integer>();

                    while (m.find()) {
                        Integer pixel = Integer.parseInt(m.group(1));
                        pixels.add(pixel);
                    }

                    if (pixels.size() > 0) {
                        Collections.sort(pixels);
                        int fontSize = pixels.get(pixels.size() - 1) > 12 ? pixels.get(pixels.size() - 1) : 12;
                        textHeight.setFont(Font.font(null, FontWeight.NORMAL, fontSize));
                    }

                    height = textHeight.minHeight(-1) + 15;
                }

                Pattern p2 = Pattern.compile("background-color:(.+?);");
                Matcher m2 = p2.matcher(item.getHtml());

                while (m2.find()) {
                    backgroundColor = m2.group(1);
                    break;
                }

                webEngine.loadContent(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<html style=\"background-color:" + backgroundColor + ";\">" +
                                "<head>" +
                                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
                                "</head>" +
                                "<body style=\"background-color:" + backgroundColor + ";\">" +
                                item.getHtml() +
                                "</body>" +
                                "</html>"
                );

                webView.getStyleClass().add("clipboard-item-webview");
                pane.getChildren().add(webView);

                break;
            default:
                break;
        }

        if (height > preferences.getItemMaximumHeight()) {
            height = preferences.getItemMaximumHeight();
        }

        holder.getStyleClass().add("clipboard-item-holder");
        holder.setPrefSize(width * 2, height);
        holder.setId("" + index);

        pane.setPrefSize(width, height);
        rect = new Rectangle(width, height);
        pane.setClip(rect);
        pane.setStyle("-fx-background-color: " + backgroundColor + ";");
        pane.getStyleClass().add("clipboard-item");
        pane.setCache(true);
        pane.setCacheShape(true);
        pane.setCacheHint(CacheHint.SPEED);

        this.anchorPane = new AnchorPane();
        anchorPane.setPrefSize(width, height);
        anchorPane.getChildren().add(pane);
        anchorPane.setCache(true);
        anchorPane.setCacheShape(true);
        anchorPane.setCacheHint(CacheHint.SPEED);
        anchorPane.getStyleClass().add("clipboard-item-anchor");

        holder.setLeftAnchor(anchorPane, (double) preferences.getShadowSpacing());
        holder.getChildren().add(anchorPane);
    }

    public AnchorPane getView() {
        return holder;
    }

    public TranslateTransition animate() {
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

        return tt;
    }

    /**
     * Set selected item
     */
    public TranslateTransition setSelected() {
        TranslateTransition slide = new TranslateTransition(new Duration(50), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(preferences.getShadowSpacing());
        return slide;
    }

    /**
     * DeselectItemView
     */
    public void deSelect() {
        TranslateTransition slide = new TranslateTransition(new Duration(50), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(preferences.getSelectedWidth() + preferences.getShadowSpacing());
        slide.play();
    }

    public int getId() {
        return Integer.parseInt(holder.getId());
    }

    public TranslateTransition transitionClose() {
        TranslateTransition slide = new TranslateTransition(new Duration(preferences.getCloseSpeed()), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(width + preferences.getSelectedWidth() + preferences.getShadowSpacing());
        return slide;
    }

    public TranslateTransition transitionOpen(int duration) {
        TranslateTransition slide = new TranslateTransition(new Duration(duration), anchorPane);
        slide.setInterpolator(Interpolator.EASE_IN);
        slide.setToX(preferences.getSelectedWidth() + preferences.getShadowSpacing());
        return slide;
    }
}
