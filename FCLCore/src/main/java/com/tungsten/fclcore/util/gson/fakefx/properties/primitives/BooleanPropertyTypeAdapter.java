package com.tungsten.fclcore.util.gson.fakefx.properties.primitives;

import com.google.gson.TypeAdapter;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;

/**
 * An implementation of {@link PrimitivePropertyTypeAdapter} for JavaFX {@link BooleanProperty}. It serializes the
 * boolean value of the property instead of the property itself.
 */
public class BooleanPropertyTypeAdapter extends PrimitivePropertyTypeAdapter<Boolean, BooleanProperty> {

    public BooleanPropertyTypeAdapter(TypeAdapter<Boolean> delegate, boolean throwOnNullProperty,
                                      boolean crashOnNullValue) {
        super(delegate, throwOnNullProperty, crashOnNullValue);
    }

    @Override
    protected Boolean extractPrimitiveValue(BooleanProperty property) {
        return property.get();
    }

    @Override
    protected BooleanProperty createDefaultProperty() {
        return new SimpleBooleanProperty();
    }

    @Override
    protected BooleanProperty wrapNonNullPrimitiveValue(Boolean deserializedValue) {
        return new SimpleBooleanProperty(deserializedValue);
    }
}