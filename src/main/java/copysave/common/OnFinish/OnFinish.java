
package copysave.common.OnFinish;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class OnFinish {
    
    private ObjectProperty<EventHandler<ActionEvent>> onFinished;
    private static final EventHandler<ActionEvent> DEFAULT_ON_FINISHED = null;
    
    public void then(EventHandler<ActionEvent> value){
        onFinishedProperty().set(value);
    }
    
    public EventHandler<ActionEvent> getOnFinished(){
        return (onFinished == null)? DEFAULT_ON_FINISHED : onFinished.get();
    }
    
    public final ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        if (onFinished == null) {
            onFinished = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onFinished", DEFAULT_ON_FINISHED);
        }
        return onFinished;
    }
    
    public void handleOnFinish(){
        final EventHandler<ActionEvent> handler = getOnFinished();
        if (handler != null) {
            handler.handle(new ActionEvent(this, null));
        }
    }
    
    public void handleOnFinish(String action){
        final EventHandler<ActionEvent> handler = getOnFinished();
        if (handler != null) {
            handler.handle(new ActionEvent(this, null));
        }
    }
}
