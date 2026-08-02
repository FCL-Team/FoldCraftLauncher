package com.tungsten.fclcore.util.gson.observable.properties;

import com.google.gson.TypeAdapter;
import com.tungsten.fclcore.observable.property.MapProperty;
import com.tungsten.fclcore.observable.property.SimpleMapProperty;
import com.tungsten.fclcore.observable.collections.ObservableMap;

import org.jetbrains.annotations.NotNull;

/**
 * A basic {@link TypeAdapter} for JavaFX {@link MapProperty}. It serializes the map inside the property instead of the
 * property itself.
 */
public class MapPropertyTypeAdapter<K, V> extends PropertyTypeAdapter<ObservableMap<K, V>, MapProperty<K, V>> {

    public MapPropertyTypeAdapter(TypeAdapter<ObservableMap<K, V>> delegate, boolean throwOnNullProperty) {
        super(delegate, throwOnNullProperty);
    }

    @NotNull
    @Override
    protected MapProperty<K, V> createProperty(ObservableMap<K, V> deserializedValue) {
        return new SimpleMapProperty<>(deserializedValue);
    }
}