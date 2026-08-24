package com.tungsten.fcl.setting;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

import android.app.Activity;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.tungsten.fcl.FCLApp;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcl.util.Constants;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonAdapter(Controller.Serializer.class)
public class Controller implements Cloneable, Observable {

    private final SimpleStringProperty id;

    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    private final SimpleStringProperty name;

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    private final SimpleStringProperty version;

    public StringProperty versionProperty() {
        return version;
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    private final SimpleIntegerProperty versionCode;

    public IntegerProperty versionCodeProperty() {
        return versionCode;
    }

    public int getVersionCode() {
        return versionCode.get();
    }

    public void setVersionCode(int versionCode) {
        this.versionCode.set(versionCode);
    }

    private final SimpleStringProperty author;

    public StringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    private final SimpleStringProperty description;

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    private final IntegerProperty controllerVersion = new SimpleIntegerProperty(this, "controllerVersion");

    public ReadOnlyIntegerProperty controllerVersionProperty() {
        return controllerVersion;
    }

    public int getControllerVersion() {
        return controllerVersion.get();
    }

    private final ObservableList<ControlViewGroup> viewGroups;

    public ObservableList<ControlViewGroup> viewGroups() {
        return viewGroups;
    }

    public void setViewGroups(ObservableList<ControlViewGroup> viewGroups) {
        this.viewGroups.addAll(viewGroups);
    }

    public Controller(String name) {
        this(generateRandomId(), name);
    }

    public Controller(String id, String name) {
        this(id, name, "");
    }

    public Controller(String id, String name, String version) {
        this(id, name, version, 1);
    }

    public Controller(String id, String name, String version, int versionCode) {
        this(id, name, version, versionCode, "");
    }

    public Controller(String id, String name, String version, int versionCode, String author) {
        this(id, name, version, versionCode, author, "");
    }

    public Controller(String id, String name, String version, int versionCode, String author, String description) {
        this(id, name, version, versionCode, author, description, Constants.CONTROLLER_VERSION);
    }

    public Controller(String id, String name, String version, int versionCode, String author, String description, int controllerVersion) {
        this(id, name, version, versionCode, author, description, controllerVersion, FXCollections.observableArrayList(new ArrayList<>()));
    }

    public Controller(String id, String name, String version, int versionCode, String author, String description, int controllerVersion, ObservableList<ControlViewGroup> viewGroups) {
        this.id = new SimpleStringProperty(this, "id", id);
        this.name = new SimpleStringProperty(this, "name", name);
        this.version = new SimpleStringProperty(this, "version", version);
        this.versionCode = new SimpleIntegerProperty(this, "versionCode", versionCode);
        this.author = new SimpleStringProperty(this, "author", author);
        this.description = new SimpleStringProperty(this, "description", description);
        this.viewGroups = viewGroups;

        this.controllerVersion.set(controllerVersion);

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    /**
     * 完整反序列化用（与 saveToDisk 相同的配置；Controller 各嵌套类自带 @JsonAdapter）。
     */
    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
            .setPrettyPrinting()
            .create();

    /**
     * 源文件引用（轻量加载用）：列表加载只解析元数据，布局按键数据通过
     * {@link #loadViewGroupData(ControlViewGroup)} 按需从该文件补全。transient 不参与序列化。
     */
    private transient File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 轻量解析：按 JsonReader 流式读取控制器元数据与各布局的元数据（id/name/visibility），
     * 并注册按钮/方向样式（按键数据解析依赖样式注册表）。布局的按键数据（viewData）留空，
     * 由 {@link #loadViewGroupData(ControlViewGroup)} 按需补全。相比完整反序列化不实例化
     * 任何按键对象，启动/列表加载零卡顿。
     */
    public static Controller parseLightweight(File file) throws IOException {
        try {
            return parseLightweight0(file);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            // 流式解析的非法状态（缺失字段/类型不符等）统一转为损坏文件异常
            throw new JsonParseException("Controller file may broken!\n" + e);
        }
    }

    private static Controller parseLightweight0(File file) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String id = null;
            String name = "";
            String version = "";
            String author = "";
            String description = "";
            int versionCode = 1;
            int controllerVersion = Constants.CONTROLLER_VERSION;
            List<ControlViewGroup> viewGroups = new ArrayList<>();

            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "id":
                        id = reader.nextString();
                        break;
                    case "name":
                        name = reader.nextString();
                        break;
                    case "version":
                        version = reader.nextString();
                        break;
                    case "author":
                        author = reader.nextString();
                        break;
                    case "description":
                        description = reader.nextString();
                        break;
                    case "versionCode":
                        versionCode = reader.nextInt();
                        break;
                    case "controllerVersion":
                        controllerVersion = reader.nextInt();
                        break;
                    case "buttonStyles":
                        registerStyles(reader, false);
                        break;
                    case "directionStyles":
                        registerStyles(reader, true);
                        break;
                    case "viewGroups":
                        reader.beginArray();
                        while (reader.hasNext()) {
                            String groupId = null;
                            String groupName = "";
                            ControlViewGroup.Visibility visibility = ControlViewGroup.Visibility.VISIBLE;
                            reader.beginObject();
                            while (reader.hasNext()) {
                                switch (reader.nextName()) {
                                    case "id":
                                        groupId = reader.nextString();
                                        break;
                                    case "name":
                                        groupName = reader.nextString();
                                        break;
                                    case "visibility":
                                        String vis = reader.nextString();
                                        try {
                                            visibility = ControlViewGroup.Visibility.valueOf(vis);
                                        } catch (IllegalArgumentException e) {
                                            // 未知取值回退为可见
                                        }
                                        break;
                                    default:
                                        reader.skipValue();
                                }
                            }
                            reader.endObject();
                            if (groupId != null) {
                                ControlViewGroup group = new ControlViewGroup(groupId);
                                group.setName(groupName);
                                group.setVisibility(visibility);
                                viewGroups.add(group);
                            }
                        }
                        reader.endArray();
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();

            if (id == null) {
                throw new JsonParseException("Controller id is missing!");
            }
            Controller controller = new Controller(id, name, version, versionCode, author, description, controllerVersion, FXCollections.observableArrayList(viewGroups));
            controller.setFile(file);
            return controller;
        }
    }

    /** 解析并注册按钮/方向样式（轻量加载时完成，按键数据按需解析时样式名可查） */
    private static void registerStyles(JsonReader reader, boolean direction) throws IOException {
        JsonElement element = JsonParser.parseReader(reader);
        ButtonStyles.init();
        DirectionStyles.init();
        if (direction) {
            List<ControlDirectionStyle> styles = GSON.fromJson(element, new TypeToken<ArrayList<ControlDirectionStyle>>() {
            }.getType());
            if (styles != null) styles.forEach(DirectionStyles::addStyle);
        } else {
            List<ControlButtonStyle> styles = GSON.fromJson(element, new TypeToken<ArrayList<ControlButtonStyle>>() {
            }.getType());
            if (styles != null) styles.forEach(ButtonStyles::addStyle);
        }
    }

    /**
     * 完整解析单个布局的按键数据：流式定位 viewGroups 数组中 id 匹配的布局并解析其
     * viewData，其余布局跳过（不实例化按键对象）。只做解析不触碰模型，可在任意线程调用。
     *
     * @return 完整 viewData，布局不存在或解析失败时返回 null
     */
    public ControlViewGroup.ViewData loadViewGroupData(ControlViewGroup viewGroup) {
        if (file == null) return null;
        try (JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            reader.beginObject();
            while (reader.hasNext()) {
                if ("viewGroups".equals(reader.nextName())) {
                    JsonElement element = findViewData(reader, viewGroup.getId());
                    if (element == null) return null;
                    return GSON.fromJson(element, ControlViewGroup.ViewData.class);
                }
                reader.skipValue();
            }
            return null;
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to load view group " + viewGroup.getId() + " of " + getFileName(), e);
            return null;
        }
    }

    /** 流式定位：仅解析目标布局的 viewData（依赖序列化顺序 id 先于 viewData，FCL 自身格式） */
    private static JsonElement findViewData(JsonReader reader, String groupId) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            String id = null;
            JsonElement viewData = null;
            reader.beginObject();
            while (reader.hasNext()) {
                String key = reader.nextName();
                if ("id".equals(key)) {
                    id = reader.nextString();
                } else if ("viewData".equals(key) && groupId.equals(id)) {
                    viewData = JsonParser.parseReader(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            if (groupId.equals(id) && viewData != null) {
                return viewData;
            }
        }
        reader.endArray();
        return null;
    }

    /**
     * 补全所有未加载布局的按键数据（保存前调用，防止轻量对象序列化时丢失按键）。
     * 解析在调用线程执行；模型填充回主线程（fakefx 列表监听非线程安全，后台线程
     * 改动会与主线程列表操作并发崩溃）。
     */
    public void ensureAllLoaded() {
        if (file == null) return;
        for (ControlViewGroup group : viewGroups) {
            if (group.isDataLoaded()) continue;
            ControlViewGroup.ViewData data = loadViewGroupData(group);
            if (data == null) continue;
            if (Looper.getMainLooper() == Looper.myLooper()) {
                group.setViewData(data);
                group.setDataLoaded(true);
            } else {
                CountDownLatch latch = new CountDownLatch(1);
                Schedulers.androidUIThread().execute(() -> {
                    group.setViewData(data);
                    group.setDataLoaded(true);
                    latch.countDown();
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void addViewGroup(ControlViewGroup viewGroup) {
        boolean exist = false;
        for (ControlViewGroup group : viewGroups()) {
            if (viewGroup.getId().equals(group.getId())) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            viewGroups.add(viewGroup);
        }
    }

    public void removeViewGroup(ControlViewGroup viewGroup) {
        for (ControlViewGroup group : viewGroups()) {
            if (viewGroup.getId().equals(group.getId())) {
                viewGroups.remove(group);
                break;
            }
        }
    }

    public void updateViewGroup(ControlViewGroup viewGroup) {
        for (ControlViewGroup group : viewGroups()) {
            if (viewGroup.getId().equals(group.getId())) {
                group.setName(viewGroup.getName());
                group.setVisibility(viewGroup.getVisibility());
                group.setViewData(viewGroup.getViewData());
                break;
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("name", getName())
                .append("version", getVersion())
                .append("versionCode", getVersionCode())
                .append("author", getAuthor())
                .append("description", getDescription())
                .append("controllerVersion", getControllerVersion())
                .toString();
    }

    private void addPropertyChangedListener(InvalidationListener listener) {
        id.addListener(listener);
        name.addListener(listener);
        version.addListener(listener);
        versionCode.addListener(listener);
        author.addListener(listener);
        description.addListener(listener);
        viewGroups.addListener(listener);
        viewGroups.forEach(it -> it.addListener(listener));
        viewGroups.addListener((InvalidationListener) observable -> {
            viewGroups.forEach(it -> it.removeListener(listener));
            viewGroups.forEach(it -> it.addListener(listener));
        });
        controllerVersion.addListener(listener);
    }

    private ObservableHelper observableHelper = new ObservableHelper(this);

    @Override
    public void addListener(InvalidationListener listener) {
        observableHelper.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        observableHelper.removeListener(listener);
    }

    private void invalidate() {
        observableHelper.invalidate();
    }

    @Override
    public Controller clone() {
        ObservableList<ControlViewGroup> viewGroups = FXCollections.observableArrayList(new ArrayList<>());
        viewGroups.addAll(viewGroups().stream().map(ControlViewGroup::clone).collect(Collectors.toList()));
        return new Controller(generateRandomId(), getName() + "_clone", getVersion(), getVersionCode(), getAuthor(), getDescription(), getControllerVersion(), viewGroups);
    }

    // function

    public String getFileName() {
        return getId() + ".json";
    }

    public synchronized void saveToDisk() {
        Schedulers.io().execute(() -> {
            // 轻量对象先补全未加载布局的按键数据，避免空 viewData 覆盖磁盘上的按钮
            ensureAllLoaded();
            String str = new GsonBuilder()
                    .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                    .setPrettyPrinting()
                    .create().toJson(this);
            try {
                FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR, getFileName()), str);
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to save controller!", e);
            }
        });
    }

    public void changeId(String newId) throws IOException {
        renameFile(getFileName(), newId + ".json");
        setId(newId);
        // 轻量对象同步更新源文件引用，避免按需加载读取已删除的旧文件
        if (file != null) {
            file = new File(FCLPath.CONTROLLER_DIR, newId + ".json");
        }
    }

    public void renameFile(String oldFileName, String newFileName) throws IOException {
        FileUtils.copyFile(new File(FCLPath.CONTROLLER_DIR, oldFileName), new File(FCLPath.CONTROLLER_DIR, newFileName));
        new File(FCLPath.CONTROLLER_DIR, oldFileName).delete();
    }

    public void upgrade() {
        this.controllerVersion.set(Constants.CONTROLLER_VERSION);
    }

    public static void showUpgradeDialog(String name, String id) {
        Schedulers.androidUIThread().execute(() -> {
            Activity activity = FCLApp.getActivity();
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder.setMessage(String.format(activity.getString(R.string.control_upgrade), name));
            builder.setPositiveButton(() -> Controllers.findControllerById(id).upgrade());
            builder.setNegativeButton(null);
            builder.create().show();
        });
    }

    public static void showIncompatibleDialog(String name) {
        Schedulers.androidUIThread().execute(() -> {
            Activity activity = FCLApp.getActivity();
            if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
                return;
            }
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(String.format(activity.getString(R.string.control_incompatible), name));
            builder.setNegativeButton(null);
            builder.create().show();
        });
    }

    public static final class Serializer implements JsonSerializer<Controller>, JsonDeserializer<Controller> {
        @Override
        public JsonElement serialize(Controller src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null)
                return JsonNull.INSTANCE;

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", src.getId());
            jsonObject.addProperty("name", src.getName());
            jsonObject.addProperty("version", src.getVersion());
            jsonObject.addProperty("versionCode", src.getVersionCode());
            jsonObject.addProperty("author", src.getAuthor());
            jsonObject.addProperty("description", src.getDescription());
            jsonObject.addProperty("controllerVersion", src.getControllerVersion());
            Stream<ControlButtonStyle> buttonStyleStream = src.viewGroups().stream().map(viewGroup -> viewGroup.getViewData().buttonList()).flatMap(buttonList -> buttonList.stream().map(data -> data.getStyle().getName()).distinct()).distinct().map(ButtonStyles::findStyleByName);
            Stream<ControlDirectionStyle> directionStyleStream = src.viewGroups().stream().map(viewGroup -> viewGroup.getViewData().directionList()).flatMap(directionList -> directionList.stream().map(data -> data.getStyle().getName()).distinct()).distinct().map(DirectionStyles::findStyleByName);
            jsonObject.add("buttonStyles", gson.toJsonTree(buttonStyleStream.collect(Collectors.toList()), new TypeToken<ArrayList<ControlButtonStyle>>() {
            }.getType()).getAsJsonArray());
            jsonObject.add("directionStyles", gson.toJsonTree(directionStyleStream.collect(Collectors.toList()), new TypeToken<ArrayList<ControlDirectionStyle>>() {
            }.getType()).getAsJsonArray());
            jsonObject.add("viewGroups", gson.toJsonTree(new ArrayList<>(src.viewGroups()), new TypeToken<ArrayList<ControlViewGroup>>() {
            }.getType()).getAsJsonArray());

            return jsonObject;
        }

        @Override
        public Controller deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject)) return null;
            JsonObject obj = (JsonObject) json;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try {
                String id = Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(generateRandomId());
                String name = Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse("Error");
                String version = Optional.ofNullable(obj.get("version")).map(JsonElement::getAsString).orElse("");
                int versionCode = Optional.ofNullable(obj.get("versionCode")).map(JsonElement::getAsInt).orElse(1);
                String author = Optional.ofNullable(obj.get("author")).map(JsonElement::getAsString).orElse("");
                String description = Optional.ofNullable(obj.get("description")).map(JsonElement::getAsString).orElse("");

                int controllerVersion = Optional.ofNullable(obj.get("controllerVersion")).map(JsonElement::getAsInt).orElse(Constants.CONTROLLER_VERSION);
                if (controllerVersion < Constants.MIN_CONTROLLER_VERSION || controllerVersion > Constants.CONTROLLER_VERSION) {
                    showIncompatibleDialog(name);
                    return new Controller("Incompatible Controller - " + name);
                }

                List<ControlButtonStyle> buttonStyles = gson.fromJson(Optional.ofNullable(obj.get("buttonStyles")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlButtonStyle>>() {
                }.getType());
                List<ControlDirectionStyle> directionStyles = gson.fromJson(Optional.ofNullable(obj.get("directionStyles")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlDirectionStyle>>() {
                }.getType());
                ButtonStyles.init();
                DirectionStyles.init();
                buttonStyles.forEach(ButtonStyles::addStyle);
                directionStyles.forEach(DirectionStyles::addStyle);
                ObservableList<ControlViewGroup> viewGroups = FXCollections.observableList(gson.fromJson(Optional.ofNullable(obj.get("viewGroups")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlViewGroup>>() {
                }.getType()));

                if (controllerVersion < Constants.CONTROLLER_VERSION) {
                    showUpgradeDialog(name, id);
                }
                // 完整反序列化的布局按键数据已就绪（轻量解析构造的布局默认未加载，按需补全）
                viewGroups.forEach(viewGroup -> viewGroup.setDataLoaded(true));
                return new Controller(id, name, version, versionCode, author, description, controllerVersion, viewGroups);
            } catch (Exception e) {
                throw new JsonParseException("Controller file may broken!\n" + e);
            }
        }

    }
}
