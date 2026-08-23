package com.tungsten.fclcore.fakefx.event;

// PENDING_DOC_REVIEW
/**
 * Represents a chain of {@code EventDispatcher} objects, which can dispatch
 * an {@code Event}. The event is dispatched by passing it from one
 * {@code EventDispatcher} to the next in the chain until the end of chain is
 * reached. Each {@code EventDispatcher} in the chain can influence the event
 * path and the event itself. The chain is usually formed by following some
 * parent - child hierarchy from the root to the event target and appending
 * all {@code EventDispatcher} objects encountered to the chain.
 * @since JavaFX 2.0
 */
public interface EventDispatchChain {
    /**
     * Appends the specified {@code EventDispatcher} to this chain. Returns a
     * reference to the chain with the appended element.
     * <p>
     * The caller shouldn't assume that this {@code EventDispatchChain} remains
     * unchanged nor that the returned value will reference a different chain
     * after the call. All this depends on the {@code EventDispatchChain}
     * implementation.
     * <p>
     * So the call should be always done in the following form:
     * {@code chain = chain.append(eventDispatcher);}
     *
     * @param eventDispatcher the {@code EventDispatcher} to append to the
     *      chain
     * @return the chain with the appended event dispatcher
     */
    EventDispatchChain append(EventDispatcher eventDispatcher);

    /**
     * Prepends the specified {@code EventDispatcher} to this chain. Returns a
     * reference to the chain with the prepended element.
     * <p>
     * The caller shouldn't assume that this {@code EventDispatchChain} remains
     * unchanged nor that the returned value will reference a different chain
     * after the call. All this depends on the {@code EventDispatchChain}
     * implementation.
     * <p>
     * So the call should be always done in the following form:
     * {@code chain = chain.prepend(eventDispatcher);}
     *
     * @param eventDispatcher the {@code EventDispatcher} to prepend to the
     *      chain
     * @return the chain with the prepended event dispatcher
     */
    EventDispatchChain prepend(EventDispatcher eventDispatcher);

    /**
     * Dispatches the specified event through this {@code EventDispatchChain}.
     * The return value represents the event after processing done by the chain.
     * If further processing is to be done after the call the event referenced
     * by the return value should be used instead of the original event. In the
     * case the event is fully handled / consumed in the chain the returned
     * value is {@code null} and no further processing should be done with that
     * event.
     *
     * @param event the event to dispatch
     * @return the processed event or {@code null} if the event had been fully
     *      handled / consumed
     */
    Event dispatchEvent(Event event);
}
