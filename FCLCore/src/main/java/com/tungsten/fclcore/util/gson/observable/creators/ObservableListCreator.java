package com.tungsten.fclcore.util.gson.observable.creators;

import com.google.gson.InstanceCreator;
import com.tungsten.fclcore.observable.collections.FXCollections;
import com.tungsten.fclcore.observable.collections.ObservableList;

import java.lang.reflect.Type;

public class ObservableListCreator implements InstanceCreator<ObservableList<?>> {

    public ObservableList<?> createInstance(Type type) {
        // No need to use a parametrized list since the actual instance will have the raw type anyway.
        return FXCollections.observableArrayList();
    }
}