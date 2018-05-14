
import clipboard.ClipboardContent;
import clipboard.ClipboardEvent;
import clipboard.ClipboardListener;

public class App {
           
    public static void main(String[] args) throws InterruptedException {
        //start clipboard
        
        ClipboardListener lis = (ClipboardEvent e) -> {
            System.out.println("HI");
            System.out.println(e.getImage());
            System.out.println(e.getText());

        };
        
        ClipboardContent cc = new ClipboardContent();
        cc.addListener(lis);
        Thread t1 = new Thread(cc);
        t1.start();
    }
    
    

 
}

