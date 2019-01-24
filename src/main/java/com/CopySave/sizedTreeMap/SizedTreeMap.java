
package com.CopySave.sizedTreeMap;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class SizedTreeMap<K, V> extends TreeMap<K, V> {

    private Set<SizedTreeMapListener> listeners = new HashSet<>();

    private static final long serialVersionUID = 1;

    private int size;

    public SizedTreeMap(int size) {
        super();
        this.size = size;
    }

    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        V old = super.put(key, value);

        if (size() > size) {
            fireOverFlowEvent(firstKey());
        }

        return old;
    }

    public void setSizedTreeListener(SizedTreeMapListener listener) {
        listeners.add(listener);
    }

    public void fireOverFlowEvent(K key) {
        OverFlowEvent e = new OverFlowEvent<>(key);

        for (SizedTreeMapListener listener : listeners) {
            listener.sizedTreeMapOverFlow(e);
        }
    }
}   
