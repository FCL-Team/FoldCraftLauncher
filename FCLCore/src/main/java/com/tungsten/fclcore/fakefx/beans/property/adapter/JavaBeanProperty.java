package com.tungsten.fclcore.fakefx.beans.property.adapter;

import com.tungsten.fclcore.fakefx.beans.property.Property;

/**
 * {@code JavaBeanProperty} is the super interface of all adapters between
 * writable Java Bean properties and JavaFX properties.
 *
 * @param <T> The type of the wrapped property
 * @since JavaFX 2.1
 */
public interface JavaBeanProperty<T> extends ReadOnlyJavaBeanProperty<T>, Property<T> {
}
