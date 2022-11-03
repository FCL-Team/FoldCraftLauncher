package com.tungsten.fcl.setting;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.game.GameDirectoryType;
import com.tungsten.fclcore.game.JavaVersion;
import com.tungsten.fclcore.game.ProcessPriority;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.platform.MemoryUtils;
import com.tungsten.fclcore.util.versioning.VersionNumber;

import java.lang.reflect.Type;
import java.util.Optional;

@JsonAdapter(VersionSetting.Serializer.class)
public final class VersionSetting implements Cloneable {

    private boolean global = false;

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    private final BooleanProperty usesGlobalProperty = new SimpleBooleanProperty(this, "usesGlobal", true);

    public BooleanProperty usesGlobalProperty() {
        return usesGlobalProperty;
    }

    /**
     * HMCL Version Settings have been divided into 2 parts.
     * 1. Global settings.
     * 2. Version settings.
     * If a version claims that it uses global settings, its version setting will be disabled.
     * <p>
     * Defaults false because if one version uses global first, custom version file will not be generated.
     */
    public boolean isUsesGlobal() {
        return usesGlobalProperty.get();
    }

    public void setUsesGlobal(boolean usesGlobal) {
        usesGlobalProperty.set(usesGlobal);
    }

    // java

    private final ObjectProperty<Integer> javaProperty = new SimpleObjectProperty<>(this, "java", 0);

    public ObjectProperty<Integer> javaProperty() {
        return javaProperty;
    }

    /**
     * Java version or "Custom" if user customizes java directory, "Default" if the jvm that this app relies on.
     */
    public int getJava() {
        return javaProperty.get();
    }

    public void setJava(int java) {
        javaProperty.set(java);
    }

    private final StringProperty permSizeProperty = new SimpleStringProperty(this, "permSize", "");

    public StringProperty permSizeProperty() {
        return permSizeProperty;
    }

    /**
     * The permanent generation size of JVM garbage collection.
     */
    public String getPermSize() {
        return permSizeProperty.get();
    }

    public void setPermSize(String permSize) {
        permSizeProperty.set(permSize);
    }

    private final IntegerProperty maxMemoryProperty = new SimpleIntegerProperty(this, "maxMemory", MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT));

    public IntegerProperty maxMemoryProperty() {
        return maxMemoryProperty;
    }

    /**
     * The maximum memory/MB that JVM can allocate for heap.
     */
    public int getMaxMemory() {
        return maxMemoryProperty.get();
    }

    public void setMaxMemory(int maxMemory) {
        maxMemoryProperty.set(maxMemory);
    }

    /**
     * The minimum memory that JVM can allocate for heap.
     */
    private final ObjectProperty<Integer> minMemoryProperty = new SimpleObjectProperty<>(this, "minMemory", null);

    public ObjectProperty<Integer> minMemoryProperty() {
        return minMemoryProperty;
    }

    public Integer getMinMemory() {
        return minMemoryProperty.get();
    }

    public void setMinMemory(Integer minMemory) {
        minMemoryProperty.set(minMemory);
    }

    private final BooleanProperty autoMemory = new SimpleBooleanProperty(this, "autoMemory", true);

    public boolean isAutoMemory() {
        return autoMemory.get();
    }

    public BooleanProperty autoMemoryProperty() {
        return autoMemory;
    }

    public void setAutoMemory(boolean autoMemory) {
        this.autoMemory.set(autoMemory);
    }

    // options

    private final StringProperty javaArgsProperty = new SimpleStringProperty(this, "javaArgs", "");

    public StringProperty javaArgsProperty() {
        return javaArgsProperty;
    }

    /**
     * The user customized arguments passed to JVM.
     */
    public String getJavaArgs() {
        return javaArgsProperty.get();
    }

    public void setJavaArgs(String javaArgs) {
        javaArgsProperty.set(javaArgs);
    }

    private final StringProperty minecraftArgsProperty = new SimpleStringProperty(this, "minecraftArgs", "");

    public StringProperty minecraftArgsProperty() {
        return minecraftArgsProperty;
    }

    /**
     * The user customized arguments passed to Minecraft.
     */
    public String getMinecraftArgs() {
        return minecraftArgsProperty.get();
    }

    public void setMinecraftArgs(String minecraftArgs) {
        minecraftArgsProperty.set(minecraftArgs);
    }

    private final BooleanProperty notCheckJVMProperty = new SimpleBooleanProperty(this, "notCheckJVM", false);

    public BooleanProperty notCheckJVMProperty() {
        return notCheckJVMProperty;
    }

    /**
     * True if HMCL does not check JVM validity.
     */
    public boolean isNotCheckJVM() {
        return notCheckJVMProperty.get();
    }

    public void setNotCheckJVM(boolean notCheckJVM) {
        notCheckJVMProperty.set(notCheckJVM);
    }

    private final BooleanProperty notCheckGameProperty = new SimpleBooleanProperty(this, "notCheckGame", false);

    public BooleanProperty notCheckGameProperty() {
        return notCheckGameProperty;
    }

    /**
     * True if HMCL does not check game's completeness.
     */
    public boolean isNotCheckGame() {
        return notCheckGameProperty.get();
    }

    public void setNotCheckGame(boolean notCheckGame) {
        notCheckGameProperty.set(notCheckGame);
    }

    // Minecraft settings.

    private final StringProperty serverIpProperty = new SimpleStringProperty(this, "serverIp", "");

    public StringProperty serverIpProperty() {
        return serverIpProperty;
    }

    /**
     * The server ip that will be entered after Minecraft successfully loaded ly.
     * <p>
     * Format: ip:port or without port.
     */
    public String getServerIp() {
        return serverIpProperty.get();
    }

    public void setServerIp(String serverIp) {
        serverIpProperty.set(serverIp);
    }


    private final BooleanProperty fullscreenProperty = new SimpleBooleanProperty(this, "fullscreen", false);

    public BooleanProperty fullscreenProperty() {
        return fullscreenProperty;
    }

    /**
     * True if Minecraft started in fullscreen mode.
     */
    public boolean isFullscreen() {
        return fullscreenProperty.get();
    }

    public void setFullscreen(boolean fullscreen) {
        fullscreenProperty.set(fullscreen);
    }

    private final IntegerProperty widthProperty = new SimpleIntegerProperty(this, "width", 854);

    public IntegerProperty widthProperty() {
        return widthProperty;
    }

    /**
     * The width of Minecraft window, defaults 800.
     * <p>
     * The field saves int value.
     * String type prevents unexpected value from JsonParseException.
     * We can only reset this field instead of recreating the whole setting file.
     */
    public int getWidth() {
        return widthProperty.get();
    }

    public void setWidth(int width) {
        widthProperty.set(width);
    }


    private final IntegerProperty heightProperty = new SimpleIntegerProperty(this, "height", 480);

    public IntegerProperty heightProperty() {
        return heightProperty;
    }

    /**
     * The height of Minecraft window, defaults 480.
     * <p>
     * The field saves int value.
     * String type prevents unexpected value from JsonParseException.
     * We can only reset this field instead of recreating the whole setting file.
     */
    public int getHeight() {
        return heightProperty.get();
    }

    public void setHeight(int height) {
        heightProperty.set(height);
    }

    /**
     * 0 - .minecraft<br/>
     * 1 - .minecraft/versions/&lt;version&gt;/<br/>
     */
    private final ObjectProperty<GameDirectoryType> gameDirTypeProperty = new SimpleObjectProperty<>(this, "gameDirType", GameDirectoryType.ROOT_FOLDER);

    public ObjectProperty<GameDirectoryType> gameDirTypeProperty() {
        return gameDirTypeProperty;
    }

    public GameDirectoryType getGameDirType() {
        return gameDirTypeProperty.get();
    }

    public void setGameDirType(GameDirectoryType gameDirType) {
        gameDirTypeProperty.set(gameDirType);
    }

    private final ObjectProperty<ProcessPriority> processPriorityProperty = new SimpleObjectProperty<>(this, "processPriority", ProcessPriority.NORMAL);

    public ObjectProperty<ProcessPriority> processPriorityProperty() {
        return processPriorityProperty;
    }

    public ProcessPriority getProcessPriority() {
        return processPriorityProperty.get();
    }

    public void setProcessPriority(ProcessPriority processPriority) {
        processPriorityProperty.set(processPriority);
    }

    // launcher settings

    public Task<JavaVersion> getJavaVersion(Version version) {
        return Task.runAsync(Schedulers.androidUIThread(), () -> {
            if (getJava() != 0 && getJava() != 1 && getJava() != 2) {
                setJava(0);
            }
        }).thenSupplyAsync(() -> {
            if (getJava() == 0) {
                return JavaVersion.getSuitableJavaVersion(version);
            }
            else {
                return JavaVersion.getJavaFromId(getJava());
            }
        });
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        usesGlobalProperty.addListener(listener);
        javaProperty.addListener(listener);
        permSizeProperty.addListener(listener);
        maxMemoryProperty.addListener(listener);
        minMemoryProperty.addListener(listener);
        autoMemory.addListener(listener);
        javaArgsProperty.addListener(listener);
        minecraftArgsProperty.addListener(listener);
        notCheckGameProperty.addListener(listener);
        notCheckJVMProperty.addListener(listener);
        serverIpProperty.addListener(listener);
        fullscreenProperty.addListener(listener);
        widthProperty.addListener(listener);
        heightProperty.addListener(listener);
        gameDirTypeProperty.addListener(listener);
        processPriorityProperty.addListener(listener);
    }

    @Override
    public VersionSetting clone() {
        VersionSetting versionSetting = new VersionSetting();
        versionSetting.setUsesGlobal(isUsesGlobal());
        versionSetting.setJava(getJava());
        versionSetting.setPermSize(getPermSize());
        versionSetting.setMaxMemory(getMaxMemory());
        versionSetting.setMinMemory(getMinMemory());
        versionSetting.setAutoMemory(isAutoMemory());
        versionSetting.setJavaArgs(getJavaArgs());
        versionSetting.setMinecraftArgs(getMinecraftArgs());
        versionSetting.setNotCheckGame(isNotCheckGame());
        versionSetting.setNotCheckJVM(isNotCheckJVM());
        versionSetting.setServerIp(getServerIp());
        versionSetting.setFullscreen(isFullscreen());
        versionSetting.setWidth(getWidth());
        versionSetting.setHeight(getHeight());
        versionSetting.setGameDirType(getGameDirType());
        versionSetting.setProcessPriority(getProcessPriority());
        return versionSetting;
    }

    public static class Serializer implements JsonSerializer<VersionSetting>, JsonDeserializer<VersionSetting> {
        @Override
        public JsonElement serialize(VersionSetting src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.addProperty("usesGlobal", src.isUsesGlobal());
            obj.addProperty("javaArgs", src.getJavaArgs());
            obj.addProperty("minecraftArgs", src.getMinecraftArgs());
            obj.addProperty("maxMemory", src.getMaxMemory() <= 0 ? MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT) : src.getMaxMemory());
            obj.addProperty("minMemory", src.getMinMemory());
            obj.addProperty("autoMemory", src.isAutoMemory());
            obj.addProperty("permSize", src.getPermSize());
            obj.addProperty("width", src.getWidth());
            obj.addProperty("height", src.getHeight());
            obj.addProperty("serverIp", src.getServerIp());
            obj.addProperty("java", src.getJava());
            obj.addProperty("fullscreen", src.isFullscreen());
            obj.addProperty("notCheckGame", src.isNotCheckGame());
            obj.addProperty("notCheckJVM", src.isNotCheckJVM());
            obj.addProperty("processPriority", src.getProcessPriority().ordinal());
            obj.addProperty("gameDirType", src.getGameDirType().ordinal());

            return obj;
        }

        @Override
        public VersionSetting deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            int maxMemoryN = parseJsonPrimitive(Optional.ofNullable(obj.get("maxMemory")).map(JsonElement::getAsJsonPrimitive).orElse(null), MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT));
            if (maxMemoryN <= 0) maxMemoryN = MemoryUtils.findBestRAMAllocation(FCLPath.CONTEXT);

            VersionSetting vs = new VersionSetting();

            vs.setUsesGlobal(Optional.ofNullable(obj.get("usesGlobal")).map(JsonElement::getAsBoolean).orElse(false));
            vs.setJavaArgs(Optional.ofNullable(obj.get("javaArgs")).map(JsonElement::getAsString).orElse(""));
            vs.setMinecraftArgs(Optional.ofNullable(obj.get("minecraftArgs")).map(JsonElement::getAsString).orElse(""));
            vs.setMaxMemory(maxMemoryN);
            vs.setMinMemory(Optional.ofNullable(obj.get("minMemory")).map(JsonElement::getAsInt).orElse(null));
            vs.setAutoMemory(Optional.ofNullable(obj.get("autoMemory")).map(JsonElement::getAsBoolean).orElse(true));
            vs.setPermSize(Optional.ofNullable(obj.get("permSize")).map(JsonElement::getAsString).orElse(""));
            vs.setWidth(Optional.ofNullable(obj.get("width")).map(JsonElement::getAsJsonPrimitive).map(this::parseJsonPrimitive).orElse(0));
            vs.setHeight(Optional.ofNullable(obj.get("height")).map(JsonElement::getAsJsonPrimitive).map(this::parseJsonPrimitive).orElse(0));
            vs.setServerIp(Optional.ofNullable(obj.get("serverIp")).map(JsonElement::getAsString).orElse(""));
            vs.setJava(Optional.ofNullable(obj.get("java")).map(JsonElement::getAsInt).orElse(0));
            vs.setFullscreen(Optional.ofNullable(obj.get("fullscreen")).map(JsonElement::getAsBoolean).orElse(false));
            vs.setNotCheckGame(Optional.ofNullable(obj.get("notCheckGame")).map(JsonElement::getAsBoolean).orElse(false));
            vs.setNotCheckJVM(Optional.ofNullable(obj.get("notCheckJVM")).map(JsonElement::getAsBoolean).orElse(false));
            vs.setProcessPriority(ProcessPriority.values()[Optional.ofNullable(obj.get("processPriority")).map(JsonElement::getAsInt).orElse(ProcessPriority.NORMAL.ordinal())]);
            vs.setGameDirType(GameDirectoryType.values()[Optional.ofNullable(obj.get("gameDirType")).map(JsonElement::getAsInt).orElse(GameDirectoryType.ROOT_FOLDER.ordinal())]);

            return vs;
        }

        private int parseJsonPrimitive(JsonPrimitive primitive) {
            return parseJsonPrimitive(primitive, 0);
        }

        private int parseJsonPrimitive(JsonPrimitive primitive, int defaultValue) {
            if (primitive == null)
                return defaultValue;
            else if (primitive.isNumber())
                return primitive.getAsInt();
            else
                return Lang.parseInt(primitive.getAsString(), defaultValue);
        }
    }
}
