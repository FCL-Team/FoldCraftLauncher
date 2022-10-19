package com.tungsten.fclcore.event;

/**
 * This event gets fired when all the versions in .minecraft folder are loaded.
 * <br>
 * This event is fired on the {@link com.tungsten.fclcore.event.EventBus#EVENT_BUS}
 */
public final class RefreshedVersionsEvent extends Event {

    /**
     * Constructor.
     *
     * @param source {@link com.tungsten.fclcore.game.GameRepository}
     */
    public RefreshedVersionsEvent(Object source) {
        super(source);
    }

}
