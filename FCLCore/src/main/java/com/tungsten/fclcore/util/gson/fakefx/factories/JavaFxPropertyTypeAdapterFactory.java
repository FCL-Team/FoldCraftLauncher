package com.tungsten.fclcore.util.gson.fakefx.factories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.FloatProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.LongProperty;
import com.tungsten.fclcore.fakefx.beans.property.MapProperty;
import com.tungsten.fclcore.fakefx.beans.property.Property;
import com.tungsten.fclcore.fakefx.beans.property.SetProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.util.gson.fakefx.properties.ListPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.MapPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.ObjectPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.SetPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.StringPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.primitives.BooleanPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.primitives.DoublePropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.primitives.FloatPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.primitives.IntegerPropertyTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.properties.primitives.LongPropertyTypeAdapter;

public class JavaFxPropertyTypeAdapterFactory implements TypeAdapterFactory {

    private final boolean strictProperties;

    private final boolean strictPrimitives;

    /**
     * Creates a new JavaFxPropertyTypeAdapterFactory. This default factory forbids null properties and null values for
     * primitive properties.
     *
     * @see #JavaFxPropertyTypeAdapterFactory(boolean, boolean)
     */
    public JavaFxPropertyTypeAdapterFactory() {
        this(true, true);
    }

    public JavaFxPropertyTypeAdapterFactory(boolean throwOnNullProperties, boolean throwOnNullPrimitives) {
        this.strictProperties = throwOnNullProperties;
        this.strictPrimitives = throwOnNullPrimitives;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> clazz = type.getRawType();

        // this factory only handles JavaFX property types
        if (!Property.class.isAssignableFrom(clazz)) {
            return null;
        }

        // simple property types

        if (BooleanProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new BooleanPropertyTypeAdapter(gson.getAdapter(boolean.class), strictProperties,
                    strictPrimitives);
        }
        if (IntegerProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new IntegerPropertyTypeAdapter(gson.getAdapter(int.class), strictProperties,
                    strictPrimitives);
        }
        if (LongProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new LongPropertyTypeAdapter(gson.getAdapter(long.class), strictProperties,
                    strictPrimitives);
        }
        if (FloatProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new FloatPropertyTypeAdapter(gson.getAdapter(float.class), strictProperties,
                    strictPrimitives);
        }
        if (DoubleProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new DoublePropertyTypeAdapter(gson.getAdapter(double.class), strictProperties,
                    strictPrimitives);
        }
        if (StringProperty.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new StringPropertyTypeAdapter(gson.getAdapter(String.class), strictProperties);
        }

        // collection property types

        if (ListProperty.class.isAssignableFrom(clazz)) {
            TypeAdapter<?> delegate = gson.getAdapter(TypeHelper.withRawType(type, ObservableList.class));
            return new ListPropertyTypeAdapter(delegate, strictProperties);
        }
        if (SetProperty.class.isAssignableFrom(clazz)) {
            TypeAdapter<?> delegate = gson.getAdapter(TypeHelper.withRawType(type, ObservableSet.class));
            return new SetPropertyTypeAdapter(delegate, strictProperties);
        }
        if (MapProperty.class.isAssignableFrom(clazz)) {
            TypeAdapter<?> delegate = gson.getAdapter(TypeHelper.withRawType(type, ObservableMap.class));
            return new MapPropertyTypeAdapter(delegate, strictProperties);
        }

        // generic Property<?> type

        Type[] typeParams = ((ParameterizedType) type.getType()).getActualTypeArguments();
        Type param = typeParams[0];
        // null factory skipPast because the nested type argument might also be a Property
        TypeAdapter<?> delegate = gson.getAdapter(TypeToken.get(param));
        return (TypeAdapter<T>) new ObjectPropertyTypeAdapter<>(delegate, strictProperties);
    }
}