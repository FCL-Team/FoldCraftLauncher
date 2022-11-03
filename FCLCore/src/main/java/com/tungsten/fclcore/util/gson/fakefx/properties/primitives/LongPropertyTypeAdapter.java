package com.tungsten.fclcore.util.gson.fakefx.properties.primitives;

import com.google.gson.TypeAdapter;
import com.tungsten.fclcore.fakefx.beans.property.LongProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleLongProperty;

/**
 * An implementation of {@link PrimitivePropertyTypeAdapter} for JavaFX {@link LongProperty}. It serializes the long
 * value of the property instead of the property itself.
 */
public class LongPropertyTypeAdapter extends PrimitivePropertyTypeAdapter<Long, LongProperty> {

    public LongPropertyTypeAdapter(TypeAdapter<Long> delegate, boolean throwOnNullProperty, boolean crashOnNullValue) {
        super(delegate, throwOnNullProperty, crashOnNullValue);
    }

    @Override
    protected Long extractPrimitiveValue(LongProperty property) {
        return property.get();
    }

    @Override
    protected LongProperty createDefaultProperty() {
        return new SimpleLongProperty();
    }

    @Override
    protected LongProperty wrapNonNullPrimitiveValue(Long deserializedValue) {
        return new SimpleLongProperty(deserializedValue);
    }
}