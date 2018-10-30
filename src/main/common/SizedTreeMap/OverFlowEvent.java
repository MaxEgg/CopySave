package common.SizedTreeMap;

public class OverFlowEvent<K,V>{
    public K key; 
    public V value;
    
    public OverFlowEvent(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
