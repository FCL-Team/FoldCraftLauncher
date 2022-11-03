package com.tungsten.fclcore.fakefx.event;

import com.tungsten.fclcore.fakefx.beans.NamedArg;

import java.lang.ref.WeakReference;

/**
 * Used in event handler registration in place of its associated event handler.
 * Its sole purpose is to break the otherwise strong reference between an event
 * handler container and its associated event handler. While the container still
 * holds strong reference to the registered {@code WeakEventHandler} proxy, the
 * proxy itself references the original handler only weakly and so doesn't
 * prevent it from being garbage collected. Until this weak reference is broken,
 * any event notification received by the proxy is forwarded to the original
 * handler.
 *
 * @param <T> the event class this handler can handle
 * @since JavaFX 8.0
 */
public final class WeakEventHandler<T extends Event>
        implements EventHandler<T> {
    private final WeakReference<EventHandler<T>> weakRef;

    /**
     * Creates a new instance of {@code WeakEventHandler}.
     *
     * @param eventHandler the original event handler to which to forward event
     *      notifications
     */
    public WeakEventHandler(final @NamedArg("eventHandler") EventHandler<T> eventHandler) {
        weakRef = new WeakReference<EventHandler<T>>(eventHandler);
    }

    /**
     * Indicates whether the associated event handler has been garbage
     * collected. Used by containers to detect when the storage of corresponding
     * references to this {@code WeakEventHandler} is no longer necessary.
     *
     * @return {@code true} if the associated handler has been garbage
     *      collected, {@code false} otherwise
     */
    public boolean wasGarbageCollected() {
        return weakRef.get() == null;
    }

    /**
     * Forwards event notification to the associated event handler.
     *
     * @param event the event which occurred
     */
    @Override
    public void handle(final T event) {
        final EventHandler<T> eventHandler = weakRef.get();
        if (eventHandler != null) {
            eventHandler.handle(event);
        }
    }

    /* Used for testing. */
    void clear() {
        weakRef.clear();
    }
}
