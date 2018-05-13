/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

/**
 *
 * @author max
 */
public class ClipboardEvent {
    
    private ClipboardListener source;
    
    public ClipboardEvent(ClipboardListener source){
        this.source = source;
    }
    
    public ClipboardListener getSource(){
        return source;
    }
}
