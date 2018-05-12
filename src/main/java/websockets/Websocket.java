/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import clipboard.ClipboardListener;
import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class Websocket extends WebSocketAdapter{
       
    @Override 
    public void onWebSocketConnect(Session sess){
        super.onWebSocketConnect(sess);
        Connections.getInstance().join(sess);
        System.out.println("Socket Connected: "+ sess);
    }
    
    @Override
    public void onWebSocketText(String message){
        super.onWebSocketText(message);
        System.out.println("Received text" + message);
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason){
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket closed" + statusCode +   " | " + reason );  
    }
    
    @Override
    public void onWebSocketError(Throwable cause){
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
    
    public void sendMessage(Session session, String message) throws IOException{
        if(session.isOpen()){
            session.getRemote().sendString(message);
        }else{
            System.out.println("Session has no connection");
        }
    }
    
}
