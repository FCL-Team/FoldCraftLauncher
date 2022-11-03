package com.tungsten.fclcore.fakefx.beans.value;

import com.tungsten.fclcore.fakefx.collections.ObservableMap;

/**
 * An observable reference to an {@link ObservableMap}.
 *
 * @see ObservableMap
 * @see ObservableObjectValue
 * @see ObservableValue
 *
 * @param <K> the type of the key elements of the {@code Map}
 * @param <V> the type of the value elements of the {@code Map}
 * @since JavaFX 2.1
 */
public interface ObservableMapValue<K, V> extends ObservableObjectValue<ObservableMap<K,V>>, ObservableMap<K, V> {
}
