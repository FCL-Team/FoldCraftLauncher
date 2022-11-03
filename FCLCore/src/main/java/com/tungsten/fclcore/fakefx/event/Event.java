package com.tungsten.fclcore.fakefx.event;

import com.tungsten.fclcore.fakefx.beans.NamedArg;

import java.util.EventObject;

import java.io.IOException;

// PENDING_DOC_REVIEW
/**
 * Base class for FX events. Each FX event has associated an event source,
 * event target and an event type. The event source specifies for an event
 * handler the object on which that handler has been registered and which sent
 * the event to it. The event target defines the path through which the event
 * will travel when posted. The event type provides additional classification
 * to events of the same {@code Event} class.
 * @since JavaFX 2.0
 */
public class Event extends EventObject implements Cloneable {

    private static final long serialVersionUID = 20121107L;
    /**
     * The constant which represents an unknown event source / target.
     */
    public static final EventTarget NULL_SOURCE_TARGET = tail -> tail;

    /**
     * Common supertype for all event types.
     */
    public static final EventType<Event> ANY = EventType.ROOT;

    /**
     * Type of the event.
     */
    protected EventType<? extends Event> eventType;

    /**
     * Event target that defines the path through which the event
     * will travel when posted.
     */
    protected transient EventTarget target;

    /**
     * Whether this event has been consumed by any filter or handler.
     */
    protected boolean consumed;

    /**
     * Construct a new {@code Event} with the specified event type. The source
     * and target of the event is set to {@code NULL_SOURCE_TARGET}.
     *
     * @param eventType the event type
     */
    public Event(final @NamedArg("eventType") EventType<? extends Event> eventType) {
        this(null, null, eventType);
    }

    /**
     * Construct a new {@code Event} with the specified event source, target
     * and type. If the source or target is set to {@code null}, it is replaced
     * by the {@code NULL_SOURCE_TARGET} value.
     *
     * @param source the event source which sent the event
     * @param target the event target to associate with the event
     * @param eventType the event type
     */
    public Event(final @NamedArg("source") Object source,
                 final @NamedArg("target") EventTarget target,
                 final @NamedArg("eventType") EventType<? extends Event> eventType) {
        super((source != null) ? source : NULL_SOURCE_TARGET);
        this.target = (target != null) ? target : NULL_SOURCE_TARGET;
        this.eventType = eventType;
    }

    /**
     * Returns the event target of this event. The event target specifies
     * the path through which the event will travel when posted.
     *
     * @return the event target
     */
    public EventTarget getTarget() {
        return target;
    }

    /**
     * Gets the event type of this event. Objects of the same {@code Event}
     * class can have different event types. These event types further specify
     * what kind of event occurred.
     *
     * @return the event type
     */
    public EventType<? extends Event> getEventType() {
        return eventType;
    }

    /**
     * Creates and returns a copy of this event with the specified event source
     * and target. If the source or target is set to {@code null}, it is
     * replaced by the {@code NULL_SOURCE_TARGET} value.
     *
     * @param newSource the new source of the copied event
     * @param newTarget the new target of the copied event
     * @return the event copy with the new source and target
     */
    public Event copyFor(final Object newSource, final EventTarget newTarget) {
        final Event newEvent = (Event) clone();

        newEvent.source = (newSource != null) ? newSource : NULL_SOURCE_TARGET;
        newEvent.target = (newTarget != null) ? newTarget : NULL_SOURCE_TARGET;
        newEvent.consumed = false;

        return newEvent;
    }

    /**
     * Indicates whether this {@code Event} has been consumed by any filter or
     * handler.
     *
     * @return {@code true} if this {@code Event} has been consumed,
     *     {@code false} otherwise
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Marks this {@code Event} as consumed. This stops its further propagation.
     */
    public void consume() {
        consumed = true;
    }

    /**
     * Creates and returns a copy of this {@code Event}.
     * @return a new instance of {@code Event} with all values copied from
     * this {@code Event}.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            // we implement Cloneable, this shouldn't happen
            throw new RuntimeException("Can't clone Event");
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        source = NULL_SOURCE_TARGET;
        target = NULL_SOURCE_TARGET;
    }


    // PENDING_DOC_REVIEW
    /**
     * Fires the specified event. The given event target specifies the path
     * through which the event will travel.
     *
     * @param eventTarget the target for the event
     * @param event the event to fire
     * @throws NullPointerException if eventTarget or event is null
     */
    public static void fireEvent(EventTarget eventTarget, Event event) {
        if (eventTarget == null) {
            throw new NullPointerException("Event target must not be null!");
        }

        if (event == null) {
            throw new NullPointerException("Event must not be null!");
        }

        EventUtil.fireEvent(eventTarget, event);
    }
}
