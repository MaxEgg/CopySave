
package common.SizedTreeMap;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SizedTreeMap<K,V> extends TreeMap<K,V> {
    
    private Set<SizedTreeMapListener>listeners = new HashSet<SizedTreeMapListener>();
    
    private static final long serialVersionUID = 1;
    
    private int size;
    
    public SizedTreeMap(int size ){
        super();
        this.size = size;
    }
    
    public V put(K key, V value) {
        V old = super.put(key, value);
        if (size() > size) {
            fireOverFlowEvent(firstKey(), (V)firstEntry());
        }
        
        return old;
    }
    
    public void setSizedTreeListener(SizedTreeMapListener listener){
        listeners.add(listener);
    }
    
    public void fireOverFlowEvent(K key, V value){
        OverFlowEvent e = new OverFlowEvent<K,V>(key , value);
        for (SizedTreeMapListener listener : listeners) {
            listener.SizedTreeMapOverFlow(e);
        }
    }
}   
