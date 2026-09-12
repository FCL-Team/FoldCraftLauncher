/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.mod.multimc;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclcore.game.Argument;
import com.tungsten.fclcore.game.Arguments;
import com.tungsten.fclcore.game.AssetIndexInfo;
import com.tungsten.fclcore.game.CompatibilityRule;
import com.tungsten.fclcore.game.DownloadInfo;
import com.tungsten.fclcore.game.DownloadType;
import com.tungsten.fclcore.game.GameJavaVersion;
import com.tungsten.fclcore.game.Library;
import com.tungsten.fclcore.game.LibraryDownloadInfo;
import com.tungsten.fclcore.game.OSRestriction;
import com.tungsten.fclcore.game.RuledArgument;
import com.tungsten.fclcore.game.StringArgument;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * MultiMC 实例的 Json-Patch(来自包内 patches/*.json 或 meta.multimc.org),
 * 并负责把 Json-Patch 列表无损(部分有损)地合成官方版本格式的补丁。
 */
public final class MultiMCInstancePatch {

    private final int formatVersion;

    @SerializedName("uid")
    private final String id;

    @SerializedName("version")
    private final String version;

    @SerializedName("assetIndex")
    private final AssetIndexInfo assetIndex;

    @SerializedName("minecraftArguments")
    private final String minecraftArguments;

    @SerializedName("+jvmArgs")
    private final List<String> jvmArgs;

    @SerializedName("mainClass")
    private final String mainClass;

    @SerializedName("compatibleJavaMajors")
    private final int[] javaMajors;

    @SerializedName("mainJar")
    private final Library mainJar;

    @SerializedName("+traits")
    private final List<String> traits;

    @SerializedName("+tweakers")
    private final List<String> tweakers;

    @SerializedName("+libraries")
    private final List<Library> libraries0;

    @SerializedName("libraries")
    private final List<Library> libraries1;

    @SerializedName("mavenFiles")
    private final List<Library> mavenFiles;

    @SerializedName("jarMods")
    private final List<Library> jarMods;

    @SerializedName("requires")
    private final List<MultiMCManifest.MultiMCManifestCachedRequires> requires;

    public MultiMCInstancePatch(int formatVersion, String id, String version, AssetIndexInfo assetIndex, String minecraftArguments, List<String> jvmArgs, String mainClass, int[] javaMajors, Library mainJar, List<String> traits, List<String> tweakers, List<Library> libraries0, List<Library> libraries1, List<Library> mavenFiles, List<Library> jarMods, List<MultiMCManifest.MultiMCManifestCachedRequires> requires) {
        this.formatVersion = formatVersion;
        this.id = id;
        this.version = version;
        this.assetIndex = assetIndex;
        this.minecraftArguments = minecraftArguments;
        this.jvmArgs = jvmArgs;
        this.mainClass = mainClass;
        this.javaMajors = javaMajors;
        this.mainJar = mainJar;
        this.traits = traits;
        this.tweakers = tweakers;
        this.libraries0 = libraries0;
        this.libraries1 = libraries1;
        this.mavenFiles = mavenFiles;
        this.jarMods = jarMods;
        this.requires = requires;
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public String getID() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public AssetIndexInfo getAssetIndex() {
        return assetIndex;
    }

    public String getMinecraftArguments() {
        return minecraftArguments;
    }

    public List<String> getJvmArgs() {
        return nonNullOrEmpty(jvmArgs);
    }

    public String getMainClass() {
        return mainClass;
    }

    public int[] getJavaMajors() {
        return javaMajors;
    }

    public Library getMainJar() {
        return mainJar;
    }

    public List<String> getTraits() {
        return nonNullOrEmpty(traits);
    }

    public List<String> getTweakers() {
        return nonNullOrEmpty(tweakers);
    }

    public List<Library> getLibraries() {
        List<Library> list = new ArrayList<>();
        if (libraries0 != null) {
            list.addAll(libraries0);
        }
        if (libraries1 != null) {
            list.addAll(libraries1);
        }
        return nonNullOrEmpty(list);
    }

    public List<Library> getMavenOnlyFiles() {
        return nonNullOrEmpty(mavenFiles);
    }

    public List<Library> getJarMods() {
        return nonNullOrEmpty(jarMods);
    }

    public List<String> getJarModFileNames() {
        List<String> fileNames = new ArrayList<>();
        for (Library library : getJarMods()) {
            fileNames.add(library.getFileName());
        }
        return nonNullOrEmpty(fileNames);
    }

    public List<MultiMCManifest.MultiMCManifestCachedRequires> getRequires() {
        return nonNullOrEmpty(requires);
    }

    public static MultiMCInstancePatch read(String componentID, String text) {
        try {
            return JsonUtils.fromNonNullJson(text, MultiMCInstancePatch.class);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Illegal Json-Patch: " + componentID, e);
        }
    }

    private static <T> List<T> nonNullOrEmpty(List<T> value) {
        return value != null && !value.isEmpty() ? value : Collections.emptyList();
    }

    private static <T> List<T> dropDuplicate(List<T> original) {
        Set<T> values = new HashSet<>();
        List<T> result = new ArrayList<>();
        for (T item : original) {
            if (values.add(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * FCL 可识别的 Java 版本。官方版本 json 只支持单个预定义的 Java 版本,
     * 不支持数组,所以只取一个最合适的 major。
     */
    private static GameJavaVersion getGameJavaVersion(int major) {
        switch (major) {
            case 8:
                return GameJavaVersion.JAVA_8;
            case 16:
                return GameJavaVersion.JAVA_16;
            case 17:
                return GameJavaVersion.JAVA_17;
            default:
                return null;
        }
    }

    /**
     * 把 Json-Patch 列表按 MultiMC 语义合并成一个可直接 addPatch 的官方格式补丁。
     *
     * @param patches    组件解析顺序下的全部 Json-Patch
     * @param instanceId 目标实例 id
     */
    public static ResolvedInstance resolveArtifact(List<MultiMCInstancePatch> patches, String instanceId) {
        if (patches.isEmpty()) {
            throw new IllegalArgumentException("Empty components.");
        }
        for (MultiMCInstancePatch patch : patches) {
            Objects.requireNonNull(patch, "patch");
            if (patch.getFormatVersion() != 1) {
                throw new UnsupportedOperationException(
                        String.format("Unsupported Json-Patch [%s] format version: %d", patch.getID(), patch.getFormatVersion()));
            }
        }

        StringBuilder message = new StringBuilder();

        List<String> minecraftArguments;
        ArrayList<Argument> jvmArguments = new ArrayList<>(Arguments.DEFAULT_JVM_ARGUMENTS);
        String mainClass;
        AssetIndexInfo assetIndex;
        int[] javaMajors;
        Library mainJar;
        List<String> traits;
        List<String> tweakers;
        List<Library> libraries;
        List<Library> mavenOnlyFiles;
        List<String> jarModFileNames;

        {
            MultiMCInstancePatch last = patches.get(patches.size() - 1);
            minecraftArguments = last.getMinecraftArguments() == null ? null : StringUtils.tokenize(last.getMinecraftArguments());
            mainClass = last.getMainClass();
            assetIndex = last.getAssetIndex();
            javaMajors = last.getJavaMajors();
            mainJar = last.getMainJar();
            traits = last.getTraits();
            tweakers = last.getTweakers();
            libraries = last.getLibraries();
            mavenOnlyFiles = last.getMavenOnlyFiles();
            jarModFileNames = last.getJarModFileNames();
        }

        for (int i = patches.size() - 2; i >= 0; i--) {
            MultiMCInstancePatch patch = patches.get(i);
            if (minecraftArguments == null && patch.getMinecraftArguments() != null) {
                minecraftArguments = StringUtils.tokenize(patch.getMinecraftArguments());
            }
            for (String jvmArg : patch.getJvmArgs()) {
                jvmArguments.add(new StringArgument(jvmArg));
            }
            mainClass = Lang.requireNonNullElse(mainClass, patch.getMainClass());
            assetIndex = Lang.requireNonNullElse(patch.getAssetIndex(), assetIndex);
            javaMajors = Lang.requireNonNullElse(patch.getJavaMajors(), javaMajors);
            mainJar = Lang.requireNonNullElse(patch.getMainJar(), mainJar);
            traits = Lang.merge(patch.getTraits(), traits);
            tweakers = Lang.merge(patch.getTweakers(), tweakers);
            libraries = Lang.merge(patch.getLibraries(), libraries);
            mavenOnlyFiles = Lang.merge(patch.getMavenOnlyFiles(), mavenOnlyFiles);
            jarModFileNames = Lang.merge(patch.getJarModFileNames(), jarModFileNames);
        }

        mainClass = Objects.requireNonNullElse(mainClass, "net.minecraft.client.Minecraft");

        if (minecraftArguments == null) {
            minecraftArguments = new ArrayList<>();
        }

        // "--tweakClass" 不会是最后一个参数
        for (int i = minecraftArguments.size() - 2; i >= 0; i--) {
            if ("--tweakClass".equals(minecraftArguments.get(i))) {
                tweakers.add(minecraftArguments.get(i + 1));

                minecraftArguments.remove(i);
                minecraftArguments.remove(i);
            }
        }

        // MultiMC 允许同名 artifact 的库共存(纯 jar 与带 natives classifier 的变体),库列表不能按名字去重
        traits = dropDuplicate(traits);
        tweakers = dropDuplicate(tweakers);
        jarModFileNames = dropDuplicate(jarModFileNames);

        for (String tweaker : tweakers) {
            minecraftArguments.add("--tweakClass");
            minecraftArguments.add(tweaker);
        }

        for (String trait : traits) {
            switch (trait) {
                case "FirstThreadOnMacOS":
                    jvmArguments.add(new RuledArgument(
                            Collections.singletonList(new CompatibilityRule(CompatibilityRule.Action.ALLOW, new OSRestriction(OperatingSystem.OSX))),
                            Collections.singletonList("-XstartOnFirstThread")));
                    break;
                case "XR:Initial":
                case "texturepacks":
                case "no-texturepacks":
                    break;
                default:
                    message.append(" - Trait: ").append(trait).append('\n');
                    break;
            }
        }

        for (Library library : libraries) {
            if ("io.github.zekerzhayard".equals(library.getGroupId()) && "ForgeWrapper".equals(library.getArtifactId())) {
                jvmArguments.add(new StringArgument("-Dforgewrapper.librariesDir=${library_directory}"));
                jvmArguments.add(new StringArgument("-Dforgewrapper.minecraft=${primary_jar}"));

                for (Library lib : libraries) {
                    if ("net.minecraftforge".equals(lib.getGroupId()) && "forge".equals(lib.getArtifactId()) && "installer".equals(lib.getClassifier())
                            || "net.neoforged".equals(lib.getGroupId()) && "neoforge".equals(lib.getArtifactId()) && "installer".equals(lib.getClassifier())) {
                        jvmArguments.add(new StringArgument("-Dforgewrapper.installer=${library_directory}/" + lib.getPath()));
                    }
                }
            }
        }

        // 合成参数以老格式 minecraftArguments 字符串承载,补丁合并时覆盖基础版本的官方参数,
        // 避免与新格式 game args 被启动器重复拼接
        Version patch = new Version("multimc", null, 1,
                new Arguments().withJvm(jvmArguments), mainClass, libraries);
        if (!minecraftArguments.isEmpty()) {
            patch = patch.setMinecraftArguments(String.join(" ", minecraftArguments));
        }
        if (mainJar != null) {
            LibraryDownloadInfo download = mainJar.getDownload();
            patch = patch.setDownloads(Collections.singletonMap(DownloadType.CLIENT, new DownloadInfo(download.getUrl(), download.getSha1())));
        }
        if (assetIndex != null) {
            patch = patch.setAssetIndex(assetIndex);
        }

        javaMajors:
        if (javaMajors != null) {
            javaMajors = javaMajors.clone();
            Arrays.sort(javaMajors);

            for (int i = javaMajors.length - 1; i >= 0; i--) {
                GameJavaVersion jv = getGameJavaVersion(javaMajors[i]);
                if (jv != null) {
                    patch = patch.setJavaVersion(jv);
                    break javaMajors;
                }
            }

            message.append(" - Java Version Range: ").append(Arrays.toString(javaMajors)).append('\n');
        }

        String gameVersion = null;
        for (MultiMCInstancePatch item : patches) {
            if (MultiMCComponents.GAME.equals(item.getID())) {
                gameVersion = item.getVersion();
                break;
            }
        }

        if (message.length() != 0) {
            if (message.charAt(message.length() - 1) == '\n') {
                message.setLength(message.length() - 1);
            }
            Logging.LOG.warning("Cannot fully parse MultiMC modpack with following unsupported features: \n" + message);
        }

        return new ResolvedInstance(patch, gameVersion, jarModFileNames, mavenOnlyFiles);
    }

    public static final class ResolvedInstance {
        private final Version patch;
        private final String gameVersion;
        private final List<String> jarModFileNames;
        private final List<Library> mavenOnlyFiles;

        public ResolvedInstance(Version patch, String gameVersion, List<String> jarModFileNames, List<Library> mavenOnlyFiles) {
            this.patch = patch;
            this.gameVersion = gameVersion;
            this.jarModFileNames = jarModFileNames;
            this.mavenOnlyFiles = mavenOnlyFiles;
        }

        public Version getPatch() {
            return patch;
        }

        public String getGameVersion() {
            return gameVersion;
        }

        public List<String> getJarModFileNames() {
            return jarModFileNames;
        }

        public List<Library> getMavenOnlyFiles() {
            return mavenOnlyFiles;
        }
    }
}
