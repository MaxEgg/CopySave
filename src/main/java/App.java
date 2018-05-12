
import clipboard.ClipboardListener;
import clipboard.ClipboardListener2;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.swing.SwingUtilities;
import model.ClipboardItem;
import websockets.Client;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import websockets.Servlet;

public class App  {
    
    ArrayList<ClipboardItem> clipboardItems = new ArrayList<ClipboardItem>();
    
    Stage window; 
   
    public static void main(String[] args) throws InterruptedException {
        //start clipboard
    	ClipboardListener2 cl = new ClipboardListener2();
        Thread t1 = new Thread(cl);
        t1.start();
            
//        launch(args);
    }  

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        window = primaryStage;
//        window.setTitle("Copybox");
//        
//    }
    
}







        //start websocket server
//        Server server = new Server(55588);
//        ServletContextHandler serverContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        serverContextHandler.setContextPath("/");
//        server.setHandler(serverContextHandler);
//        
//        ServletHolder holderEvents = new ServletHolder("ws-events", Servlet.class);
//        serverContextHandler.addServlet(holderEvents, "/*");
//        
//        try {
//            server.start();
//            server.join();
//        } catch (Exception e) {
//            System.err.println("Server error:\n" + e);
//            e.printStackTrace(System.err);