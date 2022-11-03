package com.tungsten.fclcore.util.gson.fakefx.properties;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tungsten.fclcore.fakefx.beans.property.Property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A base implementation of {@link TypeAdapter} for JavaFX properties. It serializes the value inside the property
 * instead of the property itself.
 *
 * @param <I>
 *         the inner value type of the property
 * @param <P>
 *         the property type that this adapter can serialize/deserialize, containing a value of type I
 */
public abstract class PropertyTypeAdapter<I, P extends Property<? extends I>> extends TypeAdapter<P> {

    private final TypeAdapter<I> delegate;

    private final boolean throwOnNullProperty;

    /**
     * Creates a new PropertyTypeAdapter.
     *
     * @param innerValueTypeAdapter
     *         a delegate adapter to use for the inner value of the property
     * @param throwOnNullProperty
     *         if true, this adapter will throw {@link NullPropertyException} when given a null {@link Property} to
     *         serialize
     */
    PropertyTypeAdapter(TypeAdapter<I> innerValueTypeAdapter, boolean throwOnNullProperty) {
        this.delegate = innerValueTypeAdapter;
        this.throwOnNullProperty = throwOnNullProperty;
    }

    @Override
    public void write(JsonWriter out, P property) throws IOException {
        if (property == null) {
            if (throwOnNullProperty) {
                throw new NullPropertyException();
            }
            out.nullValue();
            return;
        }
        delegate.write(out, property.getValue());
    }

    @Override
    public P read(JsonReader in) throws IOException {
        return createProperty(delegate.read(in));
    }

    /**
     * Creates a new property object with the given initial value.
     *
     * @param deserializedValue
     *         the deserialized value for the property to create. May be null.
     *
     * @return a new property object containing the given value
     */
    @NotNull
    protected abstract P createProperty(@Nullable I deserializedValue);
}
