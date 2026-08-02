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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer;
import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.observable.ObservableHelper;
import com.tungsten.fclcore.util.gson.FileTypeAdapter;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 启动器全局配置（阶段 4a）：字段已由 observable Property/ObservableList 切换为
 * StateFlow（标量）与 StateFlow&lt;List/Map&gt; 快照（集合）。
 *
 * <p><b>持久化语义承接</b>（红线）：原实现靠 {@code PropertyUtils.attachListener}
 * 把 helper 挂到每个 Property/集合上，任何失效 → Config invalidated →
 * ConfigHolder 存盘。现改为构造函数内对每个 Flow {@code subscribe}（跳过当前值，
 * 对齐 addListener 语义；Dispatchers.Unconfined 保证回调在发射线程同步执行，
 * 时机与原 property 失效一致）：</p>
 * <ul>
 *   <li>标量字段：值变化（equals 不等）→ invalidate，同值 set 不触发（与 Property 一致）；</li>
 *   <li>accountStorages：整体替换且内容变化 → invalidate（原 guarded setAll 等价）；</li>
 *   <li>authlibInjectorServers：成员增删 → invalidate；元素（服务器元数据刷新）
 *       冒泡由 Config 内部对每个服务器 revisionFlow 的订阅承接（替代阶段 3 的
 *       extractor 镜像），服务器移出列表时取消订阅；</li>
 *   <li>configurations：唯一写入方是 Profiles.updateProfileStorages（整体替换），
 *       原 MapProperty.set 同内容也必失效存盘，StateFlow 同值不发射无法承接，
 *       故由 Profiles 在替换后显式调用 {@link #invalidate()} 触发存盘，触发点不变。</li>
 * </ul>
 *
 * <p>序列化：字段类型变为 Flow 后 JavaFxPropertyTypeAdapterFactory 不再适用，
 * 改用 {@link Serializer} 手写适配，JSON 字段集/字段顺序/值级渲染与原工厂产物
 * 逐字节一致（实测回环见 docs/migration/fakefx-removal-plan.md §九）。</p>
 */
@JsonAdapter(Config.Serializer.class)
public final class Config implements Cloneable, Observable {

    public static final int CURRENT_UI_VERSION = 0;

    public static final Gson CONFIG_GSON = new GsonBuilder()
            .registerTypeAdapter(File.class, FileTypeAdapter.INSTANCE)
            .setPrettyPrinting()
            .create();

    @Nullable
    public static Config fromJson(String json) throws JsonParseException {
        return CONFIG_GSON.fromJson(json, Config.class);
    }

    private final MutableStateFlow<String> selectedProfile = StateFlowKt.MutableStateFlow("");

    private final MutableStateFlow<String> commonDirectory = StateFlowKt.MutableStateFlow(FCLPath.SHARED_COMMON_DIR);

    private final MutableStateFlow<Boolean> autoDownloadThreads = StateFlowKt.MutableStateFlow(true);

    private final MutableStateFlow<Integer> downloadThreads = StateFlowKt.MutableStateFlow(64);

    private final MutableStateFlow<String> downloadType = StateFlowKt.MutableStateFlow(DownloadProviders.DEFAULT_RAW_PROVIDER_ID);

    private final MutableStateFlow<Boolean> autoChooseDownloadType = StateFlowKt.MutableStateFlow(true);

    private final MutableStateFlow<String> versionListSource = StateFlowKt.MutableStateFlow("balanced");

    private final MutableStateFlow<Map<String, Profile>> configurations = StateFlowKt.MutableStateFlow(new TreeMap<>());

    private final MutableStateFlow<String> selectedAccount = StateFlowKt.MutableStateFlow(null);

    private final MutableStateFlow<List<Map<Object, Object>>> accountStorages = StateFlowKt.MutableStateFlow(new ArrayList<>());

    private final MutableStateFlow<List<AuthlibInjectorServer>> authlibInjectorServers = StateFlowKt.MutableStateFlow(new ArrayList<>());

    private final MutableStateFlow<String> promptedVersion = StateFlowKt.MutableStateFlow(null);

    private final MutableStateFlow<Integer> configVersion = StateFlowKt.MutableStateFlow(0);

    private final MutableStateFlow<Integer> uiVersion = StateFlowKt.MutableStateFlow(0);

    private final MutableStateFlow<String> preferredLoginType = StateFlowKt.MutableStateFlow(null);

    private transient ObservableHelper helper = new ObservableHelper(this);

    public Config() {
        FlowSubscriptions.subscribe(selectedProfile, v -> helper.invalidate());
        FlowSubscriptions.subscribe(commonDirectory, v -> helper.invalidate());
        FlowSubscriptions.subscribe(autoDownloadThreads, v -> helper.invalidate());
        FlowSubscriptions.subscribe(downloadThreads, v -> helper.invalidate());
        FlowSubscriptions.subscribe(downloadType, v -> helper.invalidate());
        FlowSubscriptions.subscribe(autoChooseDownloadType, v -> helper.invalidate());
        FlowSubscriptions.subscribe(versionListSource, v -> helper.invalidate());
        FlowSubscriptions.subscribe(selectedAccount, v -> helper.invalidate());
        FlowSubscriptions.subscribe(accountStorages, v -> helper.invalidate());
        FlowSubscriptions.subscribe(authlibInjectorServers, v -> helper.invalidate());
        FlowSubscriptions.subscribe(promptedVersion, v -> helper.invalidate());
        FlowSubscriptions.subscribe(configVersion, v -> helper.invalidate());
        FlowSubscriptions.subscribe(uiVersion, v -> helper.invalidate());
        FlowSubscriptions.subscribe(preferredLoginType, v -> helper.invalidate());
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper.removeListener(listener);
    }

    /**
     * 显式触发一次 Config 失效（→ ConfigHolder 存盘）。
     * 供 Profiles 在整体替换 configurations 后调用（StateFlow 同值不发射，
     * 需保留原 MapProperty.set 必失效的存盘语义）。
     */
    public void invalidate() {
        helper.invalidate();
    }

    public String toJson() {
        return CONFIG_GSON.toJson(this);
    }

    @Override
    public Config clone() {
        return fromJson(this.toJson());
    }

    // Getters & Setters & Flows
    public String getSelectedProfile() {
        return selectedProfile.getValue();
    }

    public void setSelectedProfile(String selectedProfile) {
        this.selectedProfile.setValue(selectedProfile);
    }

    public StateFlow<String> selectedProfileFlow() {
        return selectedProfile;
    }

    public String getCommonDirectory() {
        return commonDirectory.getValue();
    }

    public void setCommonDirectory(String commonDirectory) {
        this.commonDirectory.setValue(commonDirectory);
    }

    public StateFlow<String> commonDirectoryFlow() {
        return commonDirectory;
    }

    public boolean getAutoDownloadThreads() {
        return autoDownloadThreads.getValue();
    }

    public StateFlow<Boolean> autoDownloadThreadsFlow() {
        return autoDownloadThreads;
    }

    public void setAutoDownloadThreads(boolean autoDownloadThreads) {
        this.autoDownloadThreads.setValue(autoDownloadThreads);
    }

    public int getDownloadThreads() {
        return downloadThreads.getValue();
    }

    public StateFlow<Integer> downloadThreadsFlow() {
        return downloadThreads;
    }

    public void setDownloadThreads(int downloadThreads) {
        this.downloadThreads.setValue(downloadThreads);
    }

    public String getDownloadType() {
        return downloadType.getValue();
    }

    public void setDownloadType(String downloadType) {
        this.downloadType.setValue(downloadType);
    }

    public StateFlow<String> downloadTypeFlow() {
        return downloadType;
    }

    public boolean isAutoChooseDownloadType() {
        return autoChooseDownloadType.getValue();
    }

    public StateFlow<Boolean> autoChooseDownloadTypeFlow() {
        return autoChooseDownloadType;
    }

    public void setAutoChooseDownloadType(boolean autoChooseDownloadType) {
        this.autoChooseDownloadType.setValue(autoChooseDownloadType);
    }

    public String getVersionListSource() {
        return versionListSource.getValue();
    }

    public void setVersionListSource(String versionListSource) {
        this.versionListSource.setValue(versionListSource);
    }

    public StateFlow<String> versionListSourceFlow() {
        return versionListSource;
    }

    /**
     * 当前全部游戏目录（Profile）映射（name → Profile）。只读视图；
     * 唯一写入方是 {@link Profiles#updateProfileStorages} 与反序列化。
     */
    public Map<String, Profile> getConfigurations() {
        return configurations.getValue();
    }

    public void setConfigurations(Map<String, Profile> configurations) {
        this.configurations.setValue(configurations);
    }

    public String getSelectedAccount() {
        return selectedAccount.getValue();
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount.setValue(selectedAccount);
    }

    public StateFlow<String> selectedAccountFlow() {
        return selectedAccount;
    }

    /** 便携账户存储快照（只读）；写入方仅 {@link Accounts#updateAccountStorages}。 */
    public List<Map<Object, Object>> getAccountStorages() {
        return Collections.unmodifiableList(accountStorages.getValue());
    }

    public void setAccountStorages(List<Map<Object, Object>> storages) {
        accountStorages.setValue(new ArrayList<>(storages));
    }

    /** 外置登录服务器列表快照（只读）；增删走 add/remove 方法以管理元素订阅。 */
    public List<AuthlibInjectorServer> getAuthlibInjectorServers() {
        return Collections.unmodifiableList(authlibInjectorServers.getValue());
    }

    public StateFlow<List<AuthlibInjectorServer>> authlibInjectorServersFlow() {
        return authlibInjectorServers;
    }

    // 阶段 4a：extractor 冒泡的 Flow 承接——对列表内每个服务器的 revisionFlow 订阅，
    // 元数据刷新（revision 递增）→ Config 失效 → 存盘；服务器移出列表时取消订阅。
    private final transient Map<AuthlibInjectorServer, FlowSubscriptions.Subscription> serverSubscriptions = new IdentityHashMap<>();

    private void attachServerSubscription(AuthlibInjectorServer server) {
        if (serverSubscriptions.containsKey(server))
            return;
        serverSubscriptions.put(server,
                FlowSubscriptions.subscribe(server.revisionFlow(), revision -> helper.invalidate()));
    }

    private void detachServerSubscription(AuthlibInjectorServer server) {
        FlowSubscriptions.Subscription subscription = serverSubscriptions.remove(server);
        if (subscription != null)
            subscription.cancel();
    }

    public void addAuthlibInjectorServer(AuthlibInjectorServer server) {
        List<AuthlibInjectorServer> newList = new ArrayList<>(authlibInjectorServers.getValue());
        newList.add(server);
        attachServerSubscription(server);
        authlibInjectorServers.setValue(newList);
    }

    public void removeAuthlibInjectorServer(AuthlibInjectorServer server) {
        List<AuthlibInjectorServer> newList = new ArrayList<>(authlibInjectorServers.getValue());
        if (newList.remove(server)) {
            detachServerSubscription(server);
            authlibInjectorServers.setValue(newList);
        }
    }

    /** 反序列化整体载入（附着元素订阅；此时监听器尚未注册，不触发存盘）。 */
    private void setAuthlibInjectorServers(List<AuthlibInjectorServer> servers) {
        servers.forEach(this::attachServerSubscription);
        authlibInjectorServers.setValue(new ArrayList<>(servers));
    }

    public int getConfigVersion() {
        return configVersion.getValue();
    }

    public StateFlow<Integer> configVersionFlow() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion.setValue(configVersion);
    }

    public int getUiVersion() {
        return uiVersion.getValue();
    }

    public StateFlow<Integer> uiVersionFlow() {
        return uiVersion;
    }

    public void setUiVersion(int uiVersion) {
        this.uiVersion.setValue(uiVersion);
    }

    public String getPreferredLoginType() {
        return preferredLoginType.getValue();
    }

    public void setPreferredLoginType(String preferredLoginType) {
        this.preferredLoginType.setValue(preferredLoginType);
    }

    public StateFlow<String> preferredLoginTypeFlow() {
        return preferredLoginType;
    }

    public String getPromptedVersion() {
        return promptedVersion.getValue();
    }

    public StateFlow<String> promptedVersionFlow() {
        return promptedVersion;
    }

    public void setPromptedVersion(String promptedVersion) {
        this.promptedVersion.setValue(promptedVersion);
    }

    /**
     * Config 的值级 JSON 适配器：字段集/字段顺序（按原声明序）/值渲染与
     * JavaFxPropertyTypeAdapterFactory + creators 的产物逐字节一致。
     * 缺失字段保留构造默认值（与原工厂一致）；JSON null 字符串映射为 null。
     */
    public static final class Serializer implements JsonSerializer<Config>, JsonDeserializer<Config> {

        private static final Type CONFIGURATIONS_TYPE = new TypeToken<TreeMap<String, Profile>>() {
        }.getType();
        private static final Type ACCOUNT_STORAGES_TYPE = new TypeToken<List<Map<Object, Object>>>() {
        }.getType();
        private static final Type SERVER_LIST_TYPE = new TypeToken<List<AuthlibInjectorServer>>() {
        }.getType();

        @Override
        public JsonElement serialize(Config src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null)
                return JsonNull.INSTANCE;

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("last", src.getSelectedProfile());
            jsonObject.addProperty("commonpath", src.getCommonDirectory());
            jsonObject.addProperty("autoDownloadThreads", src.getAutoDownloadThreads());
            jsonObject.addProperty("downloadThreads", src.getDownloadThreads());
            jsonObject.addProperty("downloadType", src.getDownloadType());
            jsonObject.addProperty("autoChooseDownloadType", src.isAutoChooseDownloadType());
            jsonObject.addProperty("versionListSource", src.getVersionListSource());
            jsonObject.add("configurations", context.serialize(src.getConfigurations(), CONFIGURATIONS_TYPE));
            jsonObject.addProperty("selectedAccount", src.getSelectedAccount());
            jsonObject.add("accounts", context.serialize(src.getAccountStorages(), ACCOUNT_STORAGES_TYPE));
            jsonObject.add("authlibInjectorServers", context.serialize(src.getAuthlibInjectorServers(), SERVER_LIST_TYPE));
            jsonObject.addProperty("promptedVersion", src.getPromptedVersion());
            jsonObject.addProperty("_version", src.getConfigVersion());
            jsonObject.addProperty("uiVersion", src.getUiVersion());
            jsonObject.addProperty("preferredLoginType", src.getPreferredLoginType());
            return jsonObject;
        }

        @Override
        public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            Config config = new Config();
            config.setSelectedProfile(getString(obj, "last", ""));
            config.setCommonDirectory(getString(obj, "commonpath", FCLPath.SHARED_COMMON_DIR));
            config.setAutoDownloadThreads(getBoolean(obj, "autoDownloadThreads", true));
            config.setDownloadThreads(getInt(obj, "downloadThreads", 64));
            config.setDownloadType(getString(obj, "downloadType", DownloadProviders.DEFAULT_RAW_PROVIDER_ID));
            config.setAutoChooseDownloadType(getBoolean(obj, "autoChooseDownloadType", true));
            config.setVersionListSource(getString(obj, "versionListSource", "balanced"));
            if (obj.has("configurations") && obj.get("configurations").isJsonObject()) {
                config.setConfigurations(context.deserialize(obj.get("configurations"), CONFIGURATIONS_TYPE));
            }
            config.setSelectedAccount(getString(obj, "selectedAccount", null));
            if (obj.has("accounts") && obj.get("accounts").isJsonArray()) {
                config.setAccountStorages(context.deserialize(obj.get("accounts"), ACCOUNT_STORAGES_TYPE));
            }
            if (obj.has("authlibInjectorServers") && obj.get("authlibInjectorServers").isJsonArray()) {
                config.setAuthlibInjectorServers(context.deserialize(obj.get("authlibInjectorServers"), SERVER_LIST_TYPE));
            }
            config.setPromptedVersion(getString(obj, "promptedVersion", null));
            config.setConfigVersion(getInt(obj, "_version", 0));
            config.setUiVersion(getInt(obj, "uiVersion", 0));
            config.setPreferredLoginType(getString(obj, "preferredLoginType", null));
            return config;
        }

        private static String getString(JsonObject obj, String key, String defaultValue) {
            if (!obj.has(key) || obj.get(key).isJsonNull())
                return defaultValue;
            return obj.get(key).getAsString();
        }

        private static boolean getBoolean(JsonObject obj, String key, boolean defaultValue) {
            if (!obj.has(key) || obj.get(key).isJsonNull())
                return defaultValue;
            return obj.get(key).getAsBoolean();
        }

        private static int getInt(JsonObject obj, String key, int defaultValue) {
            if (!obj.has(key) || obj.get(key).isJsonNull())
                return defaultValue;
            return obj.get(key).getAsInt();
        }
    }
}
