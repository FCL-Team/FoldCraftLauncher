package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.Observable;

/**
 * {@code ObservableArray} is an array that allows listeners to track changes
 * when they occur. In order to track changes, the internal array
 * is encapsulated and there is no direct access available from the outside.
 * Bulk operations are supported but they always do a copy of the data range.
 * You can find them in subclasses as they deal with primitive arrays directly.
 *
 * <p>Implementations have both {@code capacity}, which is internal array length,
 * and {@code size}. If size needs to be increased beyond capacity, the capacity
 * increases to match that new size. Use {@link #trimToSize()} method
 * to shrink it.
 *
 * @see ArrayChangeListener
 * @param <T> actual array instance type
 * @since JavaFX 8.0
 */
public interface ObservableArray<T extends ObservableArray<T>> extends Observable {

    /**
     * Add a listener to this observable array.
     * @param listener the listener for listening to the array changes
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    public void addListener(ArrayChangeListener<T> listener);

    /**
     * Tries to remove a listener from this observable array. If the listener is not
     * attached to this array, nothing happens.
     * @param listener a listener to remove
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    public void removeListener(ArrayChangeListener<T> listener);

    /**
     * Sets new length of data in this array. This method grows capacity
     * if necessary but never shrinks it. Resulting array will contain existing
     * data for indexes that are less than the current size and zeroes for
     * indexes that are greater than the current size.
     * @param size new length of data in this array
     * @throws NegativeArraySizeException if size is negative
     */
    public void resize(int size);

    /**
     * Grows the capacity of this array if the current capacity is less than
     * given {@code capacity}, does nothing if it already exceeds
     * the {@code capacity}.
     * @param capacity the capacity of this array
     */
    public void ensureCapacity(int capacity);

    /**
     * Shrinks the capacity to the current size of data in the array.
     */
    public void trimToSize();

    /**
     * Empties the array by resizing it to 0. Capacity is not changed.
     * @see #trimToSize()
     */
    public void clear();

    /**
     * Retrieves length of data in this array.
     * @return length of data in this array
     */
    public int size();
}
