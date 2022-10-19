package com.tungsten.fclcore.game.tlauncher;

import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.game.Artifact;
import com.tungsten.fclcore.game.CompatibilityRule;
import com.tungsten.fclcore.game.ExtractRules;
import com.tungsten.fclcore.game.LibrariesDownloadInfo;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.LibraryDownloadInfo;
import com.tungsten.fclcore.util.platform.OperatingSystem;

import java.util.List;
import java.util.Map;

public class TLauncherLibrary {

    @SerializedName("name")
    private final Artifact name;
    private final String url;
    private final LibraryDownloadInfo artifact;

    @SerializedName("classifies") // stupid typo made by TLauncher
    private final Map<String, LibraryDownloadInfo> classifiers;
    private final ExtractRules extract;
    private final Map<OperatingSystem, String> natives;
    private final List<CompatibilityRule> rules;
    private final List<String> checksums;

    public TLauncherLibrary(Artifact name, String url, LibraryDownloadInfo artifact, Map<String, LibraryDownloadInfo> classifiers, ExtractRules extract, Map<OperatingSystem, String> natives, List<CompatibilityRule> rules, List<String> checksums) {
        this.name = name;
        this.url = url;
        this.artifact = artifact;
        this.classifiers = classifiers;
        this.extract = extract;
        this.natives = natives;
        this.rules = rules;
        this.checksums = checksums;
    }

    public Library toLibrary() {
        return new Library(
                name,
                url,
                new LibrariesDownloadInfo(artifact, classifiers),
                checksums,
                extract,
                natives,
                rules,
                null,
                null
        );
    }
}
