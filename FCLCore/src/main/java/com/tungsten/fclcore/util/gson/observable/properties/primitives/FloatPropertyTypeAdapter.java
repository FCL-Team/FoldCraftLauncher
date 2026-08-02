package com.tungsten.fclcore.util.gson.observable.properties.primitives;

import com.google.gson.TypeAdapter;
import com.tungsten.fclcore.observable.property.FloatProperty;
import com.tungsten.fclcore.observable.property.SimpleFloatProperty;

/**
 * An implementation of {@link PrimitivePropertyTypeAdapter} for JavaFX {@link FloatProperty}. It serializes the float
 * value of the property instead of the property itself.
 */
public class FloatPropertyTypeAdapter extends PrimitivePropertyTypeAdapter<Float, FloatProperty> {

    public FloatPropertyTypeAdapter(TypeAdapter<Float> delegate, boolean throwOnNullProperty,
                                    boolean crashOnNullValue) {
        super(delegate, throwOnNullProperty, crashOnNullValue);
    }

    @Override
    protected Float extractPrimitiveValue(FloatProperty property) {
        return property.get();
    }

    @Override
    protected FloatProperty createDefaultProperty() {
        return new SimpleFloatProperty();
    }

    @Override
    protected FloatProperty wrapNonNullPrimitiveValue(Float deserializedValue) {
        return new SimpleFloatProperty(deserializedValue);
    }
}