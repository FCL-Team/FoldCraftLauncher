package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.Observable;

import java.util.Set;

/**
 * Minimal reimplementation of {@code ObservableSet}.
 */
public interface ObservableSet<E> extends Set<E>, Observable {

    void addListener(SetChangeListener<? super E> listener);

    void removeListener(SetChangeListener<? super E> listener);
}
