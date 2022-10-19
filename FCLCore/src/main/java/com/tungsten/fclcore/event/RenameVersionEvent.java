package com.tungsten.fclcore.event;

import com.tungsten.fclcore.util.ToStringBuilder;

/**
 * This event gets fired when a minecraft version is being removed.
 * <br>
 * This event is fired on the {@link com.tungsten.fclcore.event.EventBus#EVENT_BUS}
 */
public class RenameVersionEvent extends Event {

    private final String from, to;

    /**
     *
     * @param source {@link com.tungsten.fclcore.game.GameRepository}
     * @param from the version id.
     */
    public RenameVersionEvent(Object source, String from, String to) {
        super(source);
        this.from = from;
        this.to = to;
    }

    public String getFromVersion() {
        return from;
    }

    public String getToVersion() {
        return to;
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("source", source)
                .append("from", from)
                .append("to", to)
                .toString();
    }
}
