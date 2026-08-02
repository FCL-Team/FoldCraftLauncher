package com.tungsten.fclcore.observable;

import java.lang.ref.WeakReference;

/**
 * Minimal reimplementation of {@code WeakInvalidationListener}.
 * 被 GC 后在下一次触发时自动从 source 摘除（与 JavaFX 一致）。
 */
public final class WeakInvalidationListener implements InvalidationListener, WeakListener {

    private final WeakReference<InvalidationListener> ref;

    public WeakInvalidationListener(@NamedArg("listener") InvalidationListener listener) {
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
    public void invalidated(Observable observable) {
        InvalidationListener listener = ref.get();
        if (listener != null) {
            listener.invalidated(observable);
        } else {
            observable.removeListener(this);
        }
    }
}
