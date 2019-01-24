package com.CopySave.sizedTreeMap;

public class OverFlowEvent<K>{
    public K key; 
//    public V value;
    
    public OverFlowEvent(K key) {
        this.key = key;
//        this.value = value;
    }
}
