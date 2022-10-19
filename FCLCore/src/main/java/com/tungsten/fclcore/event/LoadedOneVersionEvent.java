package com.tungsten.fclcore.event;

import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.ToStringBuilder;

/**
 * This event gets fired when a minecraft version has been loaded.
 * <br>
 * This event is fired on the {@link com.tungsten.fclcore.event.EventBus#EVENT_BUS}
 */
public final class LoadedOneVersionEvent extends Event {

    private final Version version;

    /**
     *
     * @param source {@link com.tungsten.fclcore.game.GameRepository}
     * @param version the version id.
     */
    public LoadedOneVersionEvent(Object source, Version version) {
        super(source);
        this.version = version;
    }

    public Version getVersion() {
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
