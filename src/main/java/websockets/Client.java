package websockets;

import websockets.Websocket;
import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.util.component.LifeCycle;

public class Client {
    
     public Client()
    {
        URI uri = URI.create("ws://localhost:8081/");
        WebSocketClient client = new WebSocketClient();
        
//        try
//        {
//            try
//            {
//                client.start();
//                // The socket that receives events
//                Websocket socket = new Websocket();
//                // Attempt Connect
//                Future<Session> fut = client.connect(socket,uri);
//                // Wait for Connect
//                System.out.println("sendstring2"+ fut.get());
//
//                Session session = fut.get();
//                // Send a message
//                System.out.println("sendstring");
//
//                session.getRemote().sendString("Hello");
//                // Close session
//                System.out.println("asdas");
//                session.close();
//            }catch(Exception t){
//                t.printStackTrace(System.err);
//            }
//            finally
//            {
//                client.stop();
//            }
//        }
//        catch (Throwable t)
//        {
//            t.printStackTrace(System.err);
//        }
    }
}
