package com.tungsten.fclcore.event;

import com.tungsten.fclcore.util.ToStringBuilder;

import java.io.File;

/**
 * This event gets fired when json of a game version is malformed. You can do something here.
 * auto making up for the missing json, don't forget to set result to {@link Event.Result#ALLOW}.
 * and even asking for removing the redundant version folder.
 *
 * The result ALLOW means you have corrected the json.
 */
public final class GameJsonParseFailedEvent extends Event {
    private final String version;
    private final File jsonFile;

    /**
     *
     * @param source {@link com.tungsten.fclcore.game.DefaultGameRepository}
     * @param jsonFile the minecraft.json file.
     * @param version the version name
     */
    public GameJsonParseFailedEvent(Object source, File jsonFile, String version) {
        super(source);
        this.version = version;
        this.jsonFile = jsonFile;
    }

    public File getJsonFile() {
        return jsonFile;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("source", source)
                .append("jsonFile", jsonFile)
                .append("version", version)
                .toString();
    }
}
