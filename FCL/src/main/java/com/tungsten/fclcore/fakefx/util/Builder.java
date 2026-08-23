package com.tungsten.fclcore.fakefx.util;

/**
 * Interface representing a builder. Builders are objects that are used to
 * construct other objects.
 *
 * @since JavaFX 2.0
 */
@FunctionalInterface
public interface Builder<T> {
    /**
     * Builds and returns the object.
     *
     * @return the object
     */
    public T build();
}
