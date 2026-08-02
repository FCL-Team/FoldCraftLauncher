package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.Observable;

import java.util.Map;

/**
 * Minimal reimplementation of {@code fakefx.collections.ObservableMap}.
 */
public interface ObservableMap<K, V> extends Map<K, V>, Observable {

    void addListener(MapChangeListener<? super K, ? super V> listener);

    void removeListener(MapChangeListener<? super K, ? super V> listener);
}
