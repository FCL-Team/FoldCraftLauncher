package com.tungsten.fclcore.fakefx;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * An unmodifiable array-based List implementation. This is essentially like the
 * package private UnmodifiableRandomAccessList of the JDK, and helps us to
 * avoid having to do a lot of conversion work when we want to pass an array
 * into an unmodifiable list implementation (otherwise we would have to create
 * a temporary list that is then passed to Collections.unmodifiableList).
 */
public class UnmodifiableArrayList<T> extends AbstractList<T> implements RandomAccess {
    private T[] elements;
    private final int size;

    /**
     * The given elements are used directly (a defensive copy is not made),
     * and the given size is used as the size of this list. It is the callers
     * responsibility to make sure the size is accurate.
     *
     * @param elements    The elements to use.
     * @param size        The size must be <= the length of the elements array
     */
    public UnmodifiableArrayList(T[] elements, int size) {
        assert elements == null ? size == 0 : size <= elements.length;
        this.size = size;
        this.elements = elements;
    }

    @Override public T get(int index) {
        return elements[index];
    }

    @Override public int size() {
        return size;
    }
}
