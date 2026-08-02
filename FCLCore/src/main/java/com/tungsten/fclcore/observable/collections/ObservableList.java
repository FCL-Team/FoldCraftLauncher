package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.Observable;

import java.util.Collection;
import java.util.List;

/**
 * Minimal reimplementation of {@code fakefx.collections.ObservableList}.
 */
public interface ObservableList<E> extends List<E>, Observable {

    void addListener(ListChangeListener<? super E> listener);

    void removeListener(ListChangeListener<? super E> listener);

    boolean addAll(E... elements);

    boolean setAll(E... elements);

    boolean setAll(Collection<? extends E> col);

    boolean removeAll(E... elements);

    boolean retainAll(E... elements);

    void remove(int from, int to);
}
