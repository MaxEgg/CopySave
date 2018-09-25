package singleton;


import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author max
 */
public class Settings {
    protected static Settings instance;
    
    public static Settings getInstance() {
      if(instance == null) {
         instance = new Settings();
      }
      return instance;
   }
    
    
    public Stage stage;
    public int itemWidth = 200;
    public int itemHeight = 100;
    public int itemMaxHeight = 200;
    
    public double stageWidth;
    public double stageHeight;
}
