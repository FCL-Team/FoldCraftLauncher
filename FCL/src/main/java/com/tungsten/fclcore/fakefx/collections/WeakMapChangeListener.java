package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.NamedArg;
import com.tungsten.fclcore.fakefx.beans.WeakListener;

import java.lang.ref.WeakReference;

public final class WeakMapChangeListener<K, V> implements MapChangeListener<K, V>, WeakListener {

    private final WeakReference<MapChangeListener<K, V>> ref;

    /**
     * The constructor of {@code WeakMapChangeListener}.
     *
     * @param listener
     *            The original listener that should be notified
     */
    public WeakMapChangeListener(@NamedArg("listener") MapChangeListener<K, V> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<MapChangeListener<K, V>>(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasGarbageCollected() {
        return (ref.get() == null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChanged(Change<? extends K,? extends V> change) {
        final MapChangeListener<K, V> listener = ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            // The weakly reference listener has been garbage collected,
            // so this WeakListener will now unhook itself from the
            // source bean
            change.getMap().removeListener(this);
        }
    }
}
