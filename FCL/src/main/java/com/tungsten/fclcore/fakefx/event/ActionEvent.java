package com.tungsten.fclcore.fakefx.event;

public class ActionEvent extends Event {

    private static final long serialVersionUID = 20121107L;
    /**
     * The only valid EventType for the ActionEvent.
     */
    public static final EventType<ActionEvent> ACTION =
            new EventType<ActionEvent>(Event.ANY, "ACTION");

    /**
     * Common supertype for all action event types.
     * @since JavaFX 8.0
     */
    public static final EventType<ActionEvent> ANY = ACTION;

    /**
     * Creates a new {@code ActionEvent} with an event type of {@code ACTION}.
     * The source and target of the event is set to {@code NULL_SOURCE_TARGET}.
     */
    public ActionEvent() {
        super(ACTION);
    }

    /**
     * Construct a new {@code ActionEvent} with the specified event source and target.
     * If the source or target is set to {@code null}, it is replaced by the
     * {@code NULL_SOURCE_TARGET} value. All ActionEvents have their type set to
     * {@code ACTION}.
     *
     * @param source    the event source which sent the event
     * @param target    the event target to associate with the event
     */
    public ActionEvent(Object source, EventTarget target) {
        super(source, target, ACTION);
    }

    @Override
    public ActionEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ActionEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends ActionEvent> getEventType() {
        return (EventType<? extends ActionEvent>) super.getEventType();
    }



}
