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
package com.tungsten.fcl.setting;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.tungsten.fcl.game.FCLCacheRepository;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.util.WeakListenerHolder;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.event.EventBus;
import com.tungsten.fclcore.event.EventPriority;
import com.tungsten.fclcore.event.RefreshedVersionsEvent;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Optional;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 游戏目录（阶段 4a）：属性已 StateFlow 化；name/gameDir/selectedVersion 任一变更
 * 或全局 VersionSetting 变更都会递增 {@link #revisionFlow()}（对齐原 Observable
 * 失效语义，Profiles 据此冒泡触发 configurations 存盘）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(Profile.Serializer.class)
public final class Profile {
    private final WeakListenerHolder listenerHolder = new WeakListenerHolder();
    private final FCLGameRepository repository;

    private final MutableStateFlow<String> selectedVersion = StateFlowKt.MutableStateFlow(null);

    public StateFlow<String> selectedVersionFlow() {
        return selectedVersion;
    }

    public String getSelectedVersion() {
        return selectedVersion.getValue();
    }

    public void setSelectedVersion(String selectedVersion) {
        this.selectedVersion.setValue(selectedVersion);
    }

    private final MutableStateFlow<File> gameDir;

    public StateFlow<File> gameDirFlow() {
        return gameDir;
    }

    public File getGameDir() {
        return gameDir.getValue();
    }

    public void setGameDir(File gameDir) {
        this.gameDir.setValue(gameDir);
    }

    private VersionSetting global;

    public VersionSetting getGlobal() {
        return global;
    }

    private final MutableStateFlow<String> name;

    public StateFlow<String> nameFlow() {
        return name;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public Profile(String name) {
        this(name, new File(FCLPath.SHARED_COMMON_DIR));
    }

    public Profile(String name, File initialGameDir) {
        this(name, initialGameDir, new VersionSetting());
    }

    public Profile(String name, File initialGameDir, VersionSetting global) {
        this(name, initialGameDir, global, null);
    }

    public Profile(String name, File initialGameDir, VersionSetting global, String selectedVersion) {
        this.name = StateFlowKt.MutableStateFlow(name);
        gameDir = StateFlowKt.MutableStateFlow(initialGameDir);
        repository = new FCLGameRepository(this, initialGameDir);
        this.global = global == null ? new VersionSetting() : global;
        this.selectedVersion.setValue(selectedVersion);

        FlowSubscriptions.subscribe(gameDir, newValue -> repository.changeDirectory(newValue));
        FlowSubscriptions.subscribe(this.selectedVersion, o -> checkSelectedVersion());
        listenerHolder.add(EventBus.EVENT_BUS.channel(RefreshedVersionsEvent.class).registerWeak(event -> checkSelectedVersion(), EventPriority.HIGHEST));

        addPropertyChangedListener();
    }

    private void checkSelectedVersion() {
        if (!repository.isLoaded()) return;
        String newValue = selectedVersion.getValue();
        if (!repository.hasVersion(newValue)) {
            Optional<String> version = repository.getVersions().stream().findFirst().map(Version::getId);
            if (version.isPresent())
                selectedVersion.setValue(version.get());
            else if (newValue != null)
                selectedVersion.setValue(null);
        }
    }

    public FCLGameRepository getRepository() {
        return repository;
    }

    public DefaultDependencyManager getDependency() {
        return getDependency(DownloadProviders.getDownloadProvider());
    }

    public DefaultDependencyManager getDependency(DownloadProvider downloadProvider) {
        return new DefaultDependencyManager(repository, downloadProvider, FCLCacheRepository.REPOSITORY);
    }

    public VersionSetting getVersionSetting(String id) {
        return repository.getVersionSetting(id);
    }

    public VersionSetting getVersionSetting() {
        return repository.getVersionSetting(getSelectedVersion());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("gameDir", getGameDir())
                .append("name", getName())
                .toString();
    }

    private void addPropertyChangedListener() {
        FlowSubscriptions.subscribe(name, v -> invalidate());
        FlowSubscriptions.subscribe(gameDir, v -> invalidate());
        FlowSubscriptions.subscribe(selectedVersion, v -> invalidate());
        global.addPropertyChangedListener(this::invalidate);
    }

    private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

    /** 任何属性变更（含全局 VersionSetting 变更）时递增（对齐原 Observable 失效语义）。 */
    public StateFlow<Long> revisionFlow() {
        return revision;
    }

    private void invalidate() {
        revision.setValue(revision.getValue() + 1);
    }

    public static class ProfileVersion {
        private final Profile profile;
        private final String version;

        public ProfileVersion(Profile profile, String version) {
            this.profile = profile;
            this.version = version;
        }

        public Profile getProfile() {
            return profile;
        }

        public String getVersion() {
            return version;
        }
    }

    public static final class Serializer implements JsonSerializer<Profile>, JsonDeserializer<Profile> {
        @Override
        public JsonElement serialize(Profile src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null)
                return JsonNull.INSTANCE;

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("global", context.serialize(src.getGlobal()));
            jsonObject.addProperty("gameDir", src.getGameDir().getPath());
            jsonObject.addProperty("selectedMinecraftVersion", src.getSelectedVersion());

            return jsonObject;
        }

        @Override
        public Profile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject)) return null;
            JsonObject obj = (JsonObject) json;
            String gameDir = Optional.ofNullable(obj.get("gameDir")).map(JsonElement::getAsString).orElse("");

            return new Profile("Default",
                    new File(gameDir),
                    context.deserialize(obj.get("global"), VersionSetting.class),
                    Optional.ofNullable(obj.get("selectedMinecraftVersion")).map(JsonElement::getAsString).orElse(""));
        }

    }
}
