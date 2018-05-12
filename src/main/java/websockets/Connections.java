 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websockets;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author max
 */
public class Connections {
    private static final Connections INSTANCE = new Connections();
    private List<Session> sessions = new ArrayList<Session>();
    
    public static Connections getInstance(){ 
        return INSTANCE;
    }
    
    public void join(Session session){
        sessions.add(session);
    }
    
    public void leave(Session session){
        sessions.remove(session);
    }
    
    public void broadcast(String message){
        System.out.println("Broadcasting: " + message);
        for(Session session : sessions){
            if(session.isOpen()){
                try {
                    session.getRemote().sendString(message);
                } catch (IOException ex) {
                    System.out.println("Session error: "+ ex.getMessage());
                }
            }else{
                System.out.println("Session is closed.");
            }
        }
    }
}
