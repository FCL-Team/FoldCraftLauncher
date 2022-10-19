package com.tungsten.fclcore.event;

import com.tungsten.fclcore.util.ToStringBuilder;

/**
 * This event gets fired when a minecraft version is being removed.
 * <br>
 * This event is fired on the {@link com.tungsten.fclcore.event.EventBus#EVENT_BUS}
 */
public class RemoveVersionEvent extends Event {

    private final String version;

    /**
     *
     * @param source {@link com.tungsten.fclcore.game.GameRepository}
     * @param version the version id.
     */
    public RemoveVersionEvent(Object source, String version) {
        super(source);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("source", source)
                .append("version", version)
                .toString();
    }
}
