package com.tungsten.fclcore.util.gson.fakefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableListCreator;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableMapCreator;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableSetCreator;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;

public class FxGsonBuilder {

    private final GsonBuilder builder;

    private boolean strictProperties = true;

    private boolean strictPrimitives = true;

    private boolean includeExtras = false;

    public FxGsonBuilder() {
        this(new GsonBuilder());
    }

    public FxGsonBuilder(GsonBuilder sourceBuilder) {
        this.builder = sourceBuilder;
    }

    public GsonBuilder builder() {
        // serialization of nulls is necessary to have properties with null values deserialized properly
        builder.serializeNulls()
               .registerTypeAdapter(ObservableList.class, new ObservableListCreator())
               .registerTypeAdapter(ObservableSet.class, new ObservableSetCreator())
               .registerTypeAdapter(ObservableMap.class, new ObservableMapCreator())
               .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(strictProperties, strictPrimitives));
        return builder;
    }

    public Gson create() {
        return builder().create();
    }

    public FxGsonBuilder acceptNullProperties() {
        strictProperties = false;
        return this;
    }

    public FxGsonBuilder acceptNullPrimitives() {
        strictPrimitives = false;
        return this;
    }

}