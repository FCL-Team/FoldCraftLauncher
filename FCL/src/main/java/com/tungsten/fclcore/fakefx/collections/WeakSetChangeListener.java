package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.NamedArg;
import com.tungsten.fclcore.fakefx.beans.WeakListener;

import java.lang.ref.WeakReference;

public final class WeakSetChangeListener<E> implements SetChangeListener<E>, WeakListener {

    private final WeakReference<SetChangeListener<E>> ref;

    /**
     * The constructor of {@code WeakSetChangeListener}.
     *
     * @param listener
     *            The original listener that should be notified
     */
    public WeakSetChangeListener(@NamedArg("listener") SetChangeListener<E> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<SetChangeListener<E>>(listener);
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
    public void onChanged(Change<? extends E> change) {
        final SetChangeListener<E> listener = ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            // The weakly reference listener has been garbage collected,
            // so this WeakListener will now unhook itself from the
            // source bean
            change.getSet().removeListener(this);
        }
    }
}
