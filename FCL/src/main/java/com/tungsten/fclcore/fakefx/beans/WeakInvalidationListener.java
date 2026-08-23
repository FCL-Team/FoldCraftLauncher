package com.tungsten.fclcore.fakefx.beans;

import java.lang.ref.WeakReference;

/**
 * A {@code WeakInvalidationListener} can be used if an {@link Observable}
 * should only maintain a weak reference to the listener. This helps to avoid
 * memory leaks that can occur if observers are not unregistered from observed
 * objects after use.
 * <p>
 * A {@code WeakInvalidationListener} is created by passing in the original
 * {@link InvalidationListener}. The {@code WeakInvalidationListener} should
 * then be registered to listen for changes of the observed object.
 * <p>
 * Note: You have to keep a reference to the {@code InvalidationListener} that
 * was passed in as long as it is in use, otherwise it can be garbage collected
 * too soon.
 *
 * @see InvalidationListener
 * @see Observable
 *
 *
 * @since JavaFX 2.0
 */
public final class WeakInvalidationListener implements InvalidationListener, WeakListener {

    private final WeakReference<InvalidationListener> ref;

    /**
     * The constructor of {@code WeakInvalidationListener}.
     *
     * @param listener
     *            The original listener that should be notified
     */
    public WeakInvalidationListener(@NamedArg("listener") InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<InvalidationListener>(listener);
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
    public void invalidated(Observable observable) {
        InvalidationListener listener = ref.get();
        if (listener != null) {
            listener.invalidated(observable);
        } else {
            // The weakly reference listener has been garbage collected,
            // so this WeakListener will now unhook itself from the
            // source bean
            observable.removeListener(this);
        }
    }
}
