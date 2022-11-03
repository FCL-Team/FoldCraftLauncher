package com.tungsten.fcl.game;

import com.tungsten.fclcore.download.DefaultCacheRepository;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;

import java.nio.file.Paths;

public class FCLCacheRepository extends DefaultCacheRepository {

    private final StringProperty directory = new SimpleStringProperty();

    public FCLCacheRepository() {
        directory.addListener((a, b, t) -> changeDirectory(Paths.get(t)));
    }

    public String getDirectory() {
        return directory.get();
    }

    public StringProperty directoryProperty() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory.set(directory);
    }

    public static final FCLCacheRepository REPOSITORY = new FCLCacheRepository();
}
