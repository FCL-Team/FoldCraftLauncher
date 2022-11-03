package com.tungsten.fclcore.util.gson.fakefx.creators;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;

/**
 * An {@link InstanceCreator} for observable sets using {@link FXCollections}.
 */
public class ObservableSetCreator implements InstanceCreator<ObservableSet<?>> {

    public ObservableSet<?> createInstance(Type type) {
        // No need to use a parametrized set since the actual instance will have the raw type anyway.
        return FXCollections.observableSet();
    }
}