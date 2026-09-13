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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.MapProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleMapProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;
import com.tungsten.fclcore.util.fakefx.PropertyUtils;
import com.tungsten.fclcore.util.gson.FileTypeAdapter;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableListCreator;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableMapCreator;
import com.tungsten.fclcore.util.gson.fakefx.creators.ObservableSetCreator;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public final class Config implements Cloneable, Observable {

    public static final int CURRENT_UI_VERSION = 0;

    public static final Gson CONFIG_GSON = new GsonBuilder()
            .registerTypeAdapter(File.class, FileTypeAdapter.INSTANCE)
            .registerTypeAdapter(ObservableList.class, new ObservableListCreator())
            .registerTypeAdapter(ObservableSet.class, new ObservableSetCreator())
            .registerTypeAdapter(ObservableMap.class, new ObservableMapCreator())
            .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
            .setPrettyPrinting()
            .create();

    @Nullable
    public static Config fromJson(String json) throws JsonParseException {
        Config loaded = CONFIG_GSON.fromJson(json, Config.class);
        if (loaded == null) {
            return null;
        }
        Config instance = new Config();
        PropertyUtils.copyProperties(loaded, instance);
        migrateDownloadSource(json, instance);
        return instance;
    }

    /**
     * 旧版配置迁移：downloadType("mojang"/"bmclapi")、autoChooseDownloadType、versionListSource
     * ("official"/"balanced"/"mirror") → versionListSource/fileDownloadSource(DEFAULT/OFFICIAL/MIRROR)。
     * 新格式字段已是枚举名时保持不动。
     */
    private static void migrateDownloadSource(String json, Config config) {
        JsonElement root;
        try {
            root = JsonParser.parseString(json);
        } catch (JsonParseException | IllegalStateException e) {
            return;
        }
        if (!root.isJsonObject()) {
            return;
        }
        JsonObject oldConfig = root.getAsJsonObject();

        String versionListSource = oldConfig.has("versionListSource") && oldConfig.get("versionListSource").isJsonPrimitive()
                ? oldConfig.get("versionListSource").getAsString()
                : null;
        if (versionListSource != null && !isSourceName(versionListSource)) {
            config.setVersionListSource(normalizeLegacySource(versionListSource));
        }

        // 新格式 fileDownloadSource 已存在时不迁移
        if (oldConfig.has("fileDownloadSource")) {
            return;
        }

        String fileDownloadSource;
        if (oldConfig.has("autoChooseDownloadType") && oldConfig.get("autoChooseDownloadType").isJsonPrimitive()
                && !oldConfig.get("autoChooseDownloadType").getAsBoolean()) {
            // 旧版手动选择文件下载源
            String downloadType = oldConfig.has("downloadType") && oldConfig.get("downloadType").isJsonPrimitive()
                    ? oldConfig.get("downloadType").getAsString()
                    : null;
            fileDownloadSource = "bmclapi".equals(downloadType)
                    ? DownloadSource.MIRROR.name()
                    : DownloadSource.OFFICIAL.name();
        } else if (versionListSource != null && !isSourceName(versionListSource)) {
            // 旧版自动模式跟随版本列表源
            fileDownloadSource = normalizeLegacySource(versionListSource);
        } else {
            fileDownloadSource = DownloadSource.DEFAULT.name();
        }
        config.setFileDownloadSource(fileDownloadSource);
    }

    private static boolean isSourceName(String value) {
        return DownloadSource.DEFAULT.name().equals(value)
                || DownloadSource.OFFICIAL.name().equals(value)
                || DownloadSource.MIRROR.name().equals(value);
    }

    private static String normalizeLegacySource(String legacy) {
        if ("official".equals(legacy)) {
            return DownloadSource.OFFICIAL.name();
        } else if ("mirror".equals(legacy)) {
            return DownloadSource.MIRROR.name();
        }
        return DownloadSource.DEFAULT.name();
    }

    @SerializedName("last")
    private StringProperty selectedProfile = new SimpleStringProperty("");

    @SerializedName("commonpath")
    private StringProperty commonDirectory = new SimpleStringProperty(FCLPath.SHARED_COMMON_DIR);

    @SerializedName("autoDownloadThreads")
    private BooleanProperty autoDownloadThreads = new SimpleBooleanProperty(true);

    @SerializedName("downloadThreads")
    private IntegerProperty downloadThreads = new SimpleIntegerProperty(64);

    @SerializedName("fileDownloadSource")
    private StringProperty fileDownloadSource = new SimpleStringProperty(DownloadSource.DEFAULT.name());

    @SerializedName("versionListSource")
    private StringProperty versionListSource = new SimpleStringProperty(DownloadSource.DEFAULT.name());

    @SerializedName("configurations")
    private SimpleMapProperty<String, Profile> configurations = new SimpleMapProperty<>(FXCollections.observableMap(new TreeMap<>()));

    @SerializedName("selectedAccount")
    private StringProperty selectedAccount = new SimpleStringProperty();

    @SerializedName("accounts")
    private ObservableList<Map<Object, Object>> accountStorages = FXCollections.observableArrayList();

    @SerializedName("authlibInjectorServers")
    private ObservableList<AuthlibInjectorServer> authlibInjectorServers = FXCollections.observableArrayList(server -> new Observable[] { server });

    @SerializedName("promptedVersion")
    private StringProperty promptedVersion = new SimpleStringProperty();

    @SerializedName("_version")
    private IntegerProperty configVersion = new SimpleIntegerProperty(0);

    /**
     * The version of UI that the user have last used.
     * If there is a major change in UI, {@link Config#CURRENT_UI_VERSION} should be increased.
     * When {@link #CURRENT_UI_VERSION} is higher than the property, the user guide should be shown,
     * then this property is set to the same value as {@link #CURRENT_UI_VERSION}.
     * In particular, the property is default to 0, so that whoever open the application for the first time will see the guide.
     */
    @SerializedName("uiVersion")
    private IntegerProperty uiVersion = new SimpleIntegerProperty(0);

    /**
     * The preferred login type to use when the user wants to add an account.
     */
    @SerializedName("preferredLoginType")
    private StringProperty preferredLoginType = new SimpleStringProperty();

    private transient ObservableHelper helper = new ObservableHelper(this);

    public Config() {
        PropertyUtils.attachListener(this, helper);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper.removeListener(listener);
    }

    public String toJson() {
        return CONFIG_GSON.toJson(this);
    }

    @Override
    public Config clone() {
        return fromJson(this.toJson());
    }

    // Getters & Setters & Properties
    public String getSelectedProfile() {
        return selectedProfile.get();
    }

    public void setSelectedProfile(String selectedProfile) {
        this.selectedProfile.set(selectedProfile);
    }

    public StringProperty selectedProfileProperty() {
        return selectedProfile;
    }

    public String getCommonDirectory() {
        return commonDirectory.get();
    }

    public void setCommonDirectory(String commonDirectory) {
        this.commonDirectory.set(commonDirectory);
    }

    public StringProperty commonDirectoryProperty() {
        return commonDirectory;
    }

    public boolean getAutoDownloadThreads() {
        return autoDownloadThreads.get();
    }

    public BooleanProperty autoDownloadThreadsProperty() {
        return autoDownloadThreads;
    }

    public void setAutoDownloadThreads(boolean autoDownloadThreads) {
        this.autoDownloadThreads.set(autoDownloadThreads);
    }

    public int getDownloadThreads() {
        return downloadThreads.get();
    }

    public IntegerProperty downloadThreadsProperty() {
        return downloadThreads;
    }

    public void setDownloadThreads(int downloadThreads) {
        this.downloadThreads.set(downloadThreads);
    }

    public String getFileDownloadSource() {
        return fileDownloadSource.get();
    }

    public void setFileDownloadSource(String fileDownloadSource) {
        this.fileDownloadSource.set(fileDownloadSource);
    }

    public StringProperty fileDownloadSourceProperty() {
        return fileDownloadSource;
    }

    public String getVersionListSource() {
        return versionListSource.get();
    }

    public void setVersionListSource(String versionListSource) {
        this.versionListSource.set(versionListSource);
    }

    public StringProperty versionListSourceProperty() {
        return versionListSource;
    }

    public MapProperty<String, Profile> getConfigurations() {
        return configurations;
    }

    public String getSelectedAccount() {
        return selectedAccount.get();
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount.set(selectedAccount);
    }

    public StringProperty selectedAccountProperty() {
        return selectedAccount;
    }

    public ObservableList<Map<Object, Object>> getAccountStorages() {
        return accountStorages;
    }

    public ObservableList<AuthlibInjectorServer> getAuthlibInjectorServers() {
        return authlibInjectorServers;
    }

    public int getConfigVersion() {
        return configVersion.get();
    }

    public IntegerProperty configVersionProperty() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion.set(configVersion);
    }

    public int getUiVersion() {
        return uiVersion.get();
    }

    public IntegerProperty uiVersionProperty() {
        return uiVersion;
    }

    public void setUiVersion(int uiVersion) {
        this.uiVersion.set(uiVersion);
    }

    public String getPreferredLoginType() {
        return preferredLoginType.get();
    }

    public void setPreferredLoginType(String preferredLoginType) {
        this.preferredLoginType.set(preferredLoginType);
    }

    public StringProperty preferredLoginTypeProperty() {
        return preferredLoginType;
    }

    public String getPromptedVersion() {
        return promptedVersion.get();
    }

    public StringProperty promptedVersionProperty() {
        return promptedVersion;
    }

    public void setPromptedVersion(String promptedVersion) {
        this.promptedVersion.set(promptedVersion);
    }
}
