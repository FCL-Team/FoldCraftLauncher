package com.tungsten.fclcore.util.gson.fakefx.creators;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

/**
 * An {@link InstanceCreator} for observable maps using {@link FXCollections}.
 */
public class ObservableMapCreator implements InstanceCreator<ObservableMap<?, ?>> {

    public ObservableMap<?, ?> createInstance(Type type) {
        // No need to use a parametrized map since the actual instance will have the raw type anyway.
        return FXCollections.observableHashMap();
    }
}