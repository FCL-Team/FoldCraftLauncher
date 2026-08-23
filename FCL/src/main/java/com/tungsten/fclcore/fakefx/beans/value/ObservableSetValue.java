package com.tungsten.fclcore.fakefx.beans.value;

import com.tungsten.fclcore.fakefx.collections.ObservableSet;

/**
 * An observable reference to an {@link ObservableSet}.
 *
 * @see ObservableSet
 * @see ObservableObjectValue
 * @see ObservableValue
 *
 * @param <E> the type of the {@code Set} elements
 * @since JavaFX 2.1
 */
public interface ObservableSetValue<E> extends ObservableObjectValue<ObservableSet<E>>, ObservableSet<E> {
}
