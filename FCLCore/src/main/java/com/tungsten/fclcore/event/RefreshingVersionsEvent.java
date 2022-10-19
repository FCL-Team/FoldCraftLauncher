package com.tungsten.fclcore.event;

/**
 * This event gets fired when loading versions in a .minecraft folder.
 * <br>
 * This event is fired on the {@link com.tungsten.fclcore.event.EventBus#EVENT_BUS}
 */
public final class RefreshingVersionsEvent extends Event {

    /**
     * Constructor.
     *
     * @param source {@link com.tungsten.fclcore.game.GameRepository}
     */
    public RefreshingVersionsEvent(Object source) {
        super(source);
    }

    @Override
    public boolean hasResult() {
        return true;
    }
}
