package com.tungsten.fclcore.fakefx.event;

/**
 * Event dispatcher which introduces event dispatch phase specific methods -
 * {@code dispatchCapturingEvent} and {@code dispatchBubblingEvent}. These
 * are used in the {@code BasicEventDispatcher.dispatchEvent} implementation,
 * but because they are public they can be called directly as well. Their
 * default implementation does nothing and is expected to be overridden in
 * subclasses. The {@code BasicEventDispatcher} also adds possibility to chain
 * event dispatchers. This is used together with the direct access to the phase
 * specific dispatch methods to implement {@code CompositeEventDispatcher}.
 * <p>
 * An event dispatcher derived from {@code BasicEventDispatcher} can act as
 * a standalone event dispatcher or can be used to form a dispatch chain in
 * {@code CompositeEventDispatcher}.
 */
public abstract class BasicEventDispatcher implements EventDispatcher {
    private BasicEventDispatcher previousDispatcher;
    private BasicEventDispatcher nextDispatcher;

    @Override
    public Event dispatchEvent(Event event, final EventDispatchChain tail) {
        event = dispatchCapturingEvent(event);
        if (event.isConsumed()) {
            return null;
        }
        event = tail.dispatchEvent(event);
        if (event != null) {
            event = dispatchBubblingEvent(event);
            if (event.isConsumed()) {
                return null;
            }
        }

        return event;
    }

    public Event dispatchCapturingEvent(Event event) {
        return event;
    }

    public Event dispatchBubblingEvent(Event event) {
        return event;
    }

    public final BasicEventDispatcher getPreviousDispatcher() {
        return previousDispatcher;
    }

    public final BasicEventDispatcher getNextDispatcher() {
        return nextDispatcher;
    }

    public final void insertNextDispatcher(
            final BasicEventDispatcher newDispatcher) {
        if (nextDispatcher != null) {
            nextDispatcher.previousDispatcher = newDispatcher;
        }
        newDispatcher.nextDispatcher = nextDispatcher;
        newDispatcher.previousDispatcher = this;
        nextDispatcher = newDispatcher;
    }
}
