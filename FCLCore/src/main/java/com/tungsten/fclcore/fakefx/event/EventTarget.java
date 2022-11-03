package com.tungsten.fclcore.fakefx.event;

// PENDING_DOC_REVIEW
/**
 * Represents an event target.
 * @since JavaFX 2.0
 */
public interface EventTarget {
    /**
     * Construct an event dispatch chain for this target. The event dispatch
     * chain contains event dispatchers which might be interested in processing
     * of events targeted at this {@code EventTarget}. This event target is
     * not automatically added to the chain, so if it wants to process events,
     * it needs to add an {@code EventDispatcher} for itself to the chain.
     * <p>
     * In the case the event target is part of some hierarchy, the chain for it
     * is usually built from event dispatchers collected from the root of the
     * hierarchy to the event target.
     * <p>
     * The event dispatch chain is constructed by modifications to the provided
     * initial event dispatch chain. The returned chain should have the initial
     * chain at its end so the dispatchers should be prepended to the initial
     * chain.
     * <p>
     * The caller shouldn't assume that the initial chain remains unchanged nor
     * that the returned value will reference a different chain.
     *
     * @param tail the initial chain to build from
     * @return the resulting event dispatch chain for this target
     */
    EventDispatchChain buildEventDispatchChain(EventDispatchChain tail);
}
