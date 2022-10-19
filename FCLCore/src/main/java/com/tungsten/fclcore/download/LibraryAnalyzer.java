package com.tungsten.fclcore.download;

import static com.tungsten.fclcore.util.Pair.pair;

import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.game.VersionProvider;
import com.tungsten.fclcore.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Todo : fix
public final class LibraryAnalyzer implements Iterable<LibraryAnalyzer.LibraryMark> {
    private Version version;
    private final Map<String, Pair<Library, String>> libraries;

    private LibraryAnalyzer(Version version, Map<String, Pair<Library, String>> libraries) {
        this.version = version;
        this.libraries = libraries;
    }

    public String getVersion(LibraryType type) {
        return getVersion(type.getPatchId());
    }

    public String getVersion(String type) {
        return Objects.requireNonNull(libraries.get(type)).getValue();
    }

    public Library getLibrary(LibraryType type) {
        return Objects.requireNonNull(libraries.get(type.getPatchId())).getKey();
    }

    @NotNull
    @Override
    public Iterator<LibraryMark> iterator() {
        return new Iterator<LibraryMark>() {
            Iterator<Map.Entry<String, Pair<Library, String>>> impl = libraries.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return impl.hasNext();
            }

            @Override
            public LibraryMark next() {
                Map.Entry<String, Pair<Library, String>> entry = impl.next();
                return new LibraryMark(entry.getKey(), entry.getValue().getValue());
            }
        };
    }

    public boolean has(LibraryType type) {
        return has(type.getPatchId());
    }

    public boolean has(String type) {
        return libraries.containsKey(type);
    }

    public boolean hasModLoader() {
        for (String s : libraries.keySet()) {
            if (Objects.requireNonNull(LibraryType.fromPatchId(s)).isModLoader()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasModLauncher() {
        final String modLauncher = "cpw.mods.modlauncher.Launcher";
        boolean has = false;
        for (Version patch : version.getPatches()) {
            if (version.getMainClass().equals(modLauncher)) {
                has = true;
                break;
            }
        }
        return modLauncher.equals(version.getMainClass()) || has;
    }

    public boolean hasBootstrapLauncher() {
        final String bootstrapLauncher = "cpw.mods.bootstraplauncher.BootstrapLauncher";
        boolean has = false;
        for (Version patch : version.getPatches()) {
            if (version.getMainClass().equals(bootstrapLauncher)) {
                has = true;
                break;
            }
        }
        return bootstrapLauncher.equals(version.getMainClass()) || has;
    }

    private Version removingMatchedLibrary(Version version, String libraryId) {
        LibraryType type = LibraryType.fromPatchId(libraryId);
        if (type == null) return version;

        List<Library> libraries = new ArrayList<>();
        for (Library library : version.getLibraries()) {
            if (type.matchLibrary(library)) {
                // skip
            } else {
                libraries.add(library);
            }
        }
        return version.setLibraries(libraries);
    }

    /**
     * Remove library by library id
     * @param libraryId patch id or "forge"/"optifine"/"liteloader"/"fabric"
     * @return this
     */
    public LibraryAnalyzer removeLibrary(String libraryId) {
        if (!has(libraryId)) return this;
        List<Version> newPatches = new ArrayList<>();
        for (Version p : version.getPatches()) {
            removingMatchedLibrary(p, libraryId);
            newPatches.add(p);
        }
        version = removingMatchedLibrary(version, libraryId).setPatches(newPatches);
        return this;
    }

    public Version build() {
        return version;
    }

    public static LibraryAnalyzer analyze(Version version) {
        if (version.getInheritsFrom() != null)
            throw new IllegalArgumentException("LibraryAnalyzer can only analyze independent game version");

        Map<String, Pair<Library, String>> libraries = new HashMap<>();

        for (Library library : version.resolve(null).getLibraries()) {
            for (LibraryType type : LibraryType.values()) {
                if (type.matchLibrary(library)) {
                    libraries.put(type.getPatchId(), pair(library, type.patchVersion(library.getVersion())));
                    break;
                }
            }
        }

        for (Version patch : version.getPatches()) {
            if (patch.isHidden()) continue;
            libraries.put(patch.getId(), pair(null, patch.getVersion()));
        }

        return new LibraryAnalyzer(version, libraries);
    }

    public static boolean isModded(VersionProvider provider, Version version) {
        Version resolvedVersion = version.resolve(provider);
        String mainClass = resolvedVersion.getMainClass();
        return mainClass != null && (LAUNCH_WRAPPER_MAIN.equals(mainClass) || mainClass.startsWith("net.fabricmc") || mainClass.startsWith("cpw.mods"));
    }

    public enum LibraryType {
        MINECRAFT(true, "game", Pattern.compile("^$"), Pattern.compile("^$")),
        FABRIC(true, "fabric", Pattern.compile("net\\.fabricmc"), Pattern.compile("fabric-loader")),
        FABRIC_API(true, "fabric-api", Pattern.compile("net\\.fabricmc"), Pattern.compile("fabric-api")),
        FORGE(true, "forge", Pattern.compile("net\\.minecraftforge"), Pattern.compile("(forge|fmlloader)")) {
            private final Pattern FORGE_VERSION_MATCHER = Pattern.compile("^([0-9.]+)-(?<forge>[0-9.]+)(-([0-9.]+))?$");

            @Override
            public String patchVersion(String libraryVersion) {
                Matcher matcher = FORGE_VERSION_MATCHER.matcher(libraryVersion);
                if (matcher.find()) {
                    return matcher.group("forge");
                }
                return super.patchVersion(libraryVersion);
            }
        },
        LITELOADER(true, "liteloader", Pattern.compile("com\\.mumfrey"), Pattern.compile("liteloader")),
        OPTIFINE(false, "optifine", Pattern.compile("(net\\.)?optifine"), Pattern.compile("^(?!.*launchwrapper).*$")),
        QUILT(true, "quilt", Pattern.compile("org\\.quiltmc"), Pattern.compile("quilt-loader")),
        QUILT_API(true, "quilt-api", Pattern.compile("org\\.quiltmc"), Pattern.compile("quilt-api")),
        BOOTSTRAP_LAUNCHER(false, "", Pattern.compile("cpw\\.mods"), Pattern.compile("bootstraplauncher"));

        private final boolean modLoader;
        private final String patchId;
        private final Pattern group, artifact;

        LibraryType(boolean modLoader, String patchId, Pattern group, Pattern artifact) {
            this.modLoader = modLoader;
            this.patchId = patchId;
            this.group = group;
            this.artifact = artifact;
        }

        public boolean isModLoader() {
            return modLoader;
        }

        public String getPatchId() {
            return patchId;
        }

        public static LibraryType fromPatchId(String patchId) {
            for (LibraryType type : values())
                if (type.getPatchId().equals(patchId))
                    return type;
            return null;
        }

        public boolean matchLibrary(Library library) {
            return group.matcher(library.getGroupId()).matches() && artifact.matcher(library.getArtifactId()).matches();
        }

        public String patchVersion(String libraryVersion) {
            return libraryVersion;
        }
    }

    public static class LibraryMark {
        private final String libraryId;
        private final String libraryVersion;

        public LibraryMark(@NotNull String libraryId, @Nullable String libraryVersion) {
            this.libraryId = libraryId;
            this.libraryVersion = libraryVersion;
        }

        @NotNull
        public String getLibraryId() {
            return libraryId;
        }

        @Nullable
        public String getLibraryVersion() {
            return libraryVersion;
        }
    }

    public static final String VANILLA_MAIN = "net.minecraft.client.main.Main";
    public static final String LAUNCH_WRAPPER_MAIN = "net.minecraft.launchwrapper.Launch";
    public static final String MOD_LAUNCHER_MAIN = "cpw.mods.modlauncher.Launcher";
    public static final String BOOTSTRAP_LAUNCHER_MAIN = "cpw.mods.bootstraplauncher.BootstrapLauncher";

    public static final String[] FORGE_TWEAKERS = new String[] {
        "net.minecraftforge.legacy._1_5_2.LibraryFixerTweaker", // 1.5.2
        "cpw.mods.fml.common.launcher.FMLTweaker", // 1.6.1 ~ 1.7.10
        "net.minecraftforge.fml.common.launcher.FMLTweaker" // 1.8 ~ 1.12.2
    };
    public static final String[] OPTIFINE_TWEAKERS = new String[] {
        "optifine.OptiFineTweaker",
        "optifine.OptiFineForgeTweaker"
    };
    public static final String LITELOADER_TWEAKER = "com.mumfrey.liteloader.launch.LiteLoaderTweaker";
}
