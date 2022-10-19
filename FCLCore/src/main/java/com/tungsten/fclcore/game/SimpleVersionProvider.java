package com.tungsten.fclcore.game;

import java.util.HashMap;
import java.util.Map;

public class SimpleVersionProvider implements VersionProvider {

    protected final Map<String, Version> versionMap = new HashMap<>();

    @Override
    public boolean hasVersion(String id) {
        return versionMap.containsKey(id);
    }

    @Override
    public Version getVersion(String id) {
        if (!hasVersion(id))
            throw new VersionNotFoundException("Version id " + id + " not found");
        else
            return versionMap.get(id);
    }

    public void addVersion(Version version) {
        versionMap.put(version.getId(), version);
    }

    public Map<String, Version> getVersionMap() {
        return versionMap;
    }
}
