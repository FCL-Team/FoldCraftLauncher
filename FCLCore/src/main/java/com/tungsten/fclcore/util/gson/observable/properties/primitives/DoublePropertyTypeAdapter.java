package com.tungsten.fclcore.util.gson.observable.properties.primitives;

import com.google.gson.TypeAdapter;
import com.tungsten.fclcore.observable.property.DoubleProperty;
import com.tungsten.fclcore.observable.property.SimpleDoubleProperty;

/**
 * An implementation of {@link PrimitivePropertyTypeAdapter} for JavaFX {@link DoubleProperty}. It serializes the double
 * value of the property instead of the property itself.
 */
public class DoublePropertyTypeAdapter extends PrimitivePropertyTypeAdapter<Double, DoubleProperty> {

    public DoublePropertyTypeAdapter(TypeAdapter<Double> delegate, boolean throwOnNullProperty,
                                     boolean crashOnNullValue) {
        super(delegate, throwOnNullProperty, crashOnNullValue);
    }

    @Override
    protected Double extractPrimitiveValue(DoubleProperty property) {
        return property.get();
    }

    @Override
    protected DoubleProperty createDefaultProperty() {
        return new SimpleDoubleProperty();
    }

    @Override
    protected DoubleProperty wrapNonNullPrimitiveValue(Double deserializedValue) {
        return new SimpleDoubleProperty(deserializedValue);
    }
}