package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.NamedArg;
import com.tungsten.fclcore.fakefx.beans.WeakListener;

import java.lang.ref.WeakReference;

public final class WeakListChangeListener<E> implements ListChangeListener<E>, WeakListener {

    private final WeakReference<ListChangeListener<E>> ref;

    /**
     * The constructor of {@code WeakListChangeListener}.
     *
     * @param listener
     *            The original listener that should be notified
     */
    public WeakListChangeListener(@NamedArg("listener") ListChangeListener<E> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<ListChangeListener<E>>(listener);
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
        final ListChangeListener<E> listener = ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            // The weakly reference listener has been garbage collected,
            // so this WeakListener will now unhook itself from the
            // source bean
            change.getList().removeListener(this);
        }
    }
}
