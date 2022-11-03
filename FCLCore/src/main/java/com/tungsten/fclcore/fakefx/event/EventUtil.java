package com.tungsten.fclcore.fakefx.event;

import java.util.concurrent.atomic.AtomicBoolean;

public final class EventUtil {
    private static final EventDispatchChainImpl eventDispatchChain =
            new EventDispatchChainImpl();

    private static final AtomicBoolean eventDispatchChainInUse =
            new AtomicBoolean();

    public static Event fireEvent(EventTarget eventTarget, Event event) {
        if (event.getTarget() != eventTarget) {
            event = event.copyFor(event.getSource(), eventTarget);
        }

        if (eventDispatchChainInUse.getAndSet(true)) {
            // the member event dispatch chain is in use currently, we need to
            // create a new instance for this call
            return fireEventImpl(new EventDispatchChainImpl(),
                                 eventTarget, event);
        }

        try {
            return fireEventImpl(eventDispatchChain, eventTarget, event);
        } finally {
            // need to do reset after use to remove references to event
            // dispatchers from the chain
            eventDispatchChain.reset();
            eventDispatchChainInUse.set(false);
        }
    }

    public static Event fireEvent(Event event, EventTarget... eventTargets) {
        return fireEventImpl(new EventDispatchTreeImpl(),
                             new CompositeEventTargetImpl(eventTargets),
                             event);
    }

    private static Event fireEventImpl(EventDispatchChain eventDispatchChain,
                                       EventTarget eventTarget,
                                       Event event) {
        final EventDispatchChain targetDispatchChain =
                eventTarget.buildEventDispatchChain(eventDispatchChain);
        return targetDispatchChain.dispatchEvent(event);
    }
}
