package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.Observable;

import java.util.Set;

public interface ObservableSet<E> extends Set<E>, Observable {
    /**
     * Add a listener to this observable set.
     * @param listener the listener for listening to the set changes
     */
    public void addListener(SetChangeListener<? super E> listener);
    /**
     * Tries to removed a listener from this observable set. If the listener is not
     * attached to this list, nothing happens.
     * @param listener a listener to remove
     */
    public void removeListener(SetChangeListener<? super E> listener);
}
