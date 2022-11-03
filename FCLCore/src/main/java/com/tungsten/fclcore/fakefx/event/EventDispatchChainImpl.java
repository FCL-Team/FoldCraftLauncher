package com.tungsten.fclcore.fakefx.event;

public class EventDispatchChainImpl implements EventDispatchChain {
    /** Must be a power of two. */
    private static final int CAPACITY_GROWTH_FACTOR = 8;

    private EventDispatcher[] dispatchers;

    private int[] nextLinks;

    private int reservedCount;
    private int activeCount;
    private int headIndex;
    private int tailIndex;

    public EventDispatchChainImpl() {
    }

    public void reset() {
        // shrink?
        for (int i = 0; i < reservedCount; ++i) {
            dispatchers[i] = null;
        }

        reservedCount = 0;
        activeCount = 0;
        headIndex = 0;
        tailIndex = 0;
    }

    @Override
    public EventDispatchChain append(final EventDispatcher eventDispatcher) {
        ensureCapacity(reservedCount + 1);

        if (activeCount == 0) {
            insertFirst(eventDispatcher);
            return this;
        }

        dispatchers[reservedCount] = eventDispatcher;
        nextLinks[tailIndex] = reservedCount;
        tailIndex = reservedCount;

        ++activeCount;
        ++reservedCount;

        return this;
    }

    @Override
    public EventDispatchChain prepend(final EventDispatcher eventDispatcher) {
        ensureCapacity(reservedCount + 1);

        if (activeCount == 0) {
            insertFirst(eventDispatcher);
            return this;
        }

        dispatchers[reservedCount] = eventDispatcher;
        nextLinks[reservedCount] = headIndex;
        headIndex = reservedCount;

        ++activeCount;
        ++reservedCount;

        return this;
    }

    @Override
    public Event dispatchEvent(final Event event) {
        if (activeCount == 0) {
            return event;
        }

        // push current state
        final int savedHeadIndex = headIndex;
        final int savedTailIndex = tailIndex;
        final int savedActiveCount = activeCount;
        final int savedReservedCount = reservedCount;

        final EventDispatcher nextEventDispatcher = dispatchers[headIndex];
        headIndex = nextLinks[headIndex];
        --activeCount;
        final Event returnEvent =
                nextEventDispatcher.dispatchEvent(event, this);

        // pop saved state
        headIndex = savedHeadIndex;
        tailIndex = savedTailIndex;
        activeCount = savedActiveCount;
        reservedCount = savedReservedCount;

        return returnEvent;
    }

    private void insertFirst(final EventDispatcher eventDispatcher) {
        dispatchers[reservedCount] = eventDispatcher;
        headIndex = reservedCount;
        tailIndex = reservedCount;

        activeCount = 1;
        ++reservedCount;
    }

    private void ensureCapacity(final int size) {
        final int newCapacity = (size + CAPACITY_GROWTH_FACTOR - 1)
                                    & ~(CAPACITY_GROWTH_FACTOR - 1);
        if (newCapacity == 0) {
            return;
        }

        if ((dispatchers == null) || (dispatchers.length < newCapacity)) {
            final EventDispatcher[] newDispatchers =
                    new EventDispatcher[newCapacity];
            final int[] newLinks = new int[newCapacity];

            if (reservedCount > 0) {
                System.arraycopy(dispatchers, 0, newDispatchers, 0,
                                 reservedCount);
                System.arraycopy(nextLinks, 0, newLinks, 0, reservedCount);
            }

            dispatchers = newDispatchers;
            nextLinks = newLinks;
        }
    }
}
