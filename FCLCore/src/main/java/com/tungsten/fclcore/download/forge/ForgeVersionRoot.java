package com.tungsten.fclcore.download.forge;

import com.google.gson.JsonParseException;
import com.tungsten.fclcore.util.gson.Validation;

import java.util.Map;

public final class ForgeVersionRoot implements Validation {

    private final String artifact;
    private final String webpath;
    private final String adfly;
    private final String homepage;
    private final String name;
    private final Map<String, int[]> branches;
    private final Map<String, int[]> mcversion;
    private final Map<String, Integer> promos;
    private final Map<Integer, ForgeVersion> number;

    /**
     * No-arg constructor for Gson.
     */
    @SuppressWarnings("unused")
    public ForgeVersionRoot() {
        this(null, null, null, null, null, null, null, null, null);
    }

    public ForgeVersionRoot(String artifact, String webpath, String adfly, String homepage, String name, Map<String, int[]> branches, Map<String, int[]> mcversion, Map<String, Integer> promos, Map<Integer, ForgeVersion> number) {
        this.artifact = artifact;
        this.webpath = webpath;
        this.adfly = adfly;
        this.homepage = homepage;
        this.name = name;
        this.branches = branches;
        this.mcversion = mcversion;
        this.promos = promos;
        this.number = number;
    }

    public String getArtifact() {
        return artifact;
    }

    public String getWebPath() {
        return webpath;
    }

    public String getAdfly() {
        return adfly;
    }

    public String getHomePage() {
        return homepage;
    }

    public String getName() {
        return name;
    }

    public Map<String, int[]> getBranches() {
        return branches;
    }

    public Map<String, int[]> getGameVersions() {
        return mcversion;
    }

    public Map<String, Integer> getPromos() {
        return promos;
    }

    public Map<Integer, ForgeVersion> getNumber() {
        return number;
    }

    @Override
    public void validate() throws JsonParseException {
        if (number == null)
            throw new JsonParseException("ForgeVersionRoot number cannot be null");
        if (mcversion == null)
            throw new JsonParseException("ForgeVersionRoot mcversion cannot be null");
    }

}
