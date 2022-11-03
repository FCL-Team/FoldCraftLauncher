package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

public class ImmutableObservableList<E> extends AbstractList<E> implements ObservableList<E> {

    private final E[] elements;

    public ImmutableObservableList(E... elements) {
        this.elements = ((elements == null) || (elements.length == 0))?
                null : Arrays.copyOf(elements, elements.length);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        // no-op
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        // no-op
    }

    @Override
    public void addListener(ListChangeListener<? super E> listener) {
        // no-op
    }

    @Override
    public void removeListener(ListChangeListener<? super E> listener) {
        // no-op
    }

    @Override
    public boolean addAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setAll(Collection<? extends E> col) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(E... elements) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int index) {
        if ((index < 0) || (index >= size())) {
            throw new IndexOutOfBoundsException();
        }
        return elements[index];
    }

    @Override
    public int size() {
        return (elements == null)? 0 : elements.length;
    }

}
