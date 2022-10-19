package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.game.DefaultGameRepository;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;

/**
 * This task is to save the version json.
 */
public final class VersionJsonSaveTask extends Task<Version> {

    private final DefaultGameRepository repository;
    private final Version version;

    /**
     * Constructor.
     *
     * @param repository the game repository
     * @param version the game version
     */
    public VersionJsonSaveTask(DefaultGameRepository repository, Version version) {
        this.repository = repository;
        this.version = version;

        setSignificance(TaskSignificance.MODERATE);
        setResult(version);
    }

    @Override
    public void execute() throws Exception {
        File json = repository.getVersionJson(version.getId()).getAbsoluteFile();
        FileUtils.writeText(json, JsonUtils.GSON.toJson(version));
    }
}
