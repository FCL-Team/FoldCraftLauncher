package com.tungsten.fclcore.fakefx.beans.value;

import com.tungsten.fclcore.fakefx.collections.ObservableMap;

public interface WritableMapValue<K, V> extends WritableObjectValue<ObservableMap<K,V>>, ObservableMap<K, V> {
}
