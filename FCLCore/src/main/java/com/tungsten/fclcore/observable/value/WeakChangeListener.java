package com.tungsten.fclcore.observable.value;

import com.tungsten.fclcore.observable.NamedArg;
import com.tungsten.fclcore.observable.WeakListener;

import java.lang.ref.WeakReference;

/**
 * Minimal reimplementation of {@code WeakChangeListener}.
 */
public final class WeakChangeListener<T> implements ChangeListener<T>, WeakListener {

    private final WeakReference<ChangeListener<T>> ref;

    public WeakChangeListener(@NamedArg("listener") ChangeListener<T> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override
    public boolean wasGarbageCollected() {
        return (ref.get() == null);
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        ChangeListener<T> listener = ref.get();
        if (listener != null) {
            listener.changed(observable, oldValue, newValue);
        } else {
            observable.removeListener(this);
        }
    }
}
