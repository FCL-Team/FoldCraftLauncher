package com.tungsten.fclcore.fakefx.event;

import java.util.EventListener;

// PENDING_DOC_REVIEW
/**
 * Handler for events of a specific class / type.
 *
 * @param <T> the event class this handler can handle
 * @since JavaFX 2.0
 */
@FunctionalInterface
public interface EventHandler<T extends Event> extends EventListener {
    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     *
     * @param event the event which occurred
     */
    void handle(T event);
}
