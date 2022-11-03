package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.Observable;

import java.util.Map;

public interface ObservableMap<K, V> extends Map<K, V>, Observable {
    /**
     * Add a listener to this observable map.
     * @param listener the listener for listening to the list changes
     */
    public void addListener(MapChangeListener<? super K, ? super V> listener);
    /**
     * Tries to removed a listener from this observable map. If the listener is not
     * attached to this map, nothing happens.
     * @param listener a listener to remove
     */
    public void removeListener(MapChangeListener<? super K, ? super V> listener);
}
