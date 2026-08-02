package com.tungsten.fclcore.observable.value;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;

/**
 * Minimal reimplementation of {@code ObservableValue}.
 */
public interface ObservableValue<T> extends Observable {

    void addListener(ChangeListener<? super T> listener);

    void removeListener(ChangeListener<? super T> listener);

    @Override
    void addListener(InvalidationListener listener);

    @Override
    void removeListener(InvalidationListener listener);

    T getValue();
}
