/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mouse;

import clipboard.ClipboardContent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 *
 * @author max
 */
public class MouseFollower implements Runnable {

    private  int time = 100;
    private List<Point> record = new ArrayList<Point>();
    private boolean recording = false;
    private Point lastPoint;


    public MouseFollower() {}

    
    private Point getMousePoint(){
        PointerInfo inf = MouseInfo.getPointerInfo();
        Point p = inf.getLocation();
        return p;
    }
    
    public void mouseLocation() {
        Point p = getMousePoint();
        int Speed = 0;

        if (lastPoint != null) {

            int xSpeed = p.x - lastPoint.x;
            int ySpeed = p.y - lastPoint.x;
            //Euclidean distance formula 
            double speed = Math.round(Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(xSpeed, 2)));
            
            if(speed > 2){
                recording = true;
                record.add(p);
            }else if(recording){
                analyseRecord();
                record = new ArrayList<Point>();
            }
        }
        lastPoint = p;
    }
    
    /**
     * Detect monitor. This functions return the Screen where the mouse is present.
     */
    public Screen detectMonitor(){
        Point p = getMousePoint();
        Point2D p2d = new Point2D(p.getX(), p.getY());

        Iterable<Screen> monitors = Screen.getScreens();
        for(Screen monitor : monitors){
            Rectangle2D rect = monitor.getBounds();
                
           if(rect.contains(p2d)){
               return monitor;
           }
        }
        
        return Screen.getPrimary();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                mouseLocation();
                Thread.sleep(time);
            } catch (InterruptedException | NullPointerException ex) {

            }
        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    
    public void analyseRecord(){
        int size = record.size(),
            x;
        Point p;
        
        for(x = 0; x < size; ++x){
//            record()
        }
    }
}
