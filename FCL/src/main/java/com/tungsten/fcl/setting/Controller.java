package com.tungsten.fcl.setting;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

import android.content.Context;

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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
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
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public synchronized void saveToDisk(){
        Schedulers.io().execute(()->{
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
    }

    public void renameFile(String oldFileName, String newFileName) throws IOException {
        FileUtils.copyFile(new File(FCLPath.CONTROLLER_DIR, oldFileName), new File(FCLPath.CONTROLLER_DIR, newFileName));
        new File(FCLPath.CONTROLLER_DIR, oldFileName).delete();
    }

    public void upgrade() {
        this.controllerVersion.set(Constants.CONTROLLER_VERSION);
    }

    public static void showUpgradeDialog(Context context, String name, String id) {
        Schedulers.androidUIThread().execute(() -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder.setMessage(String.format(context.getString(R.string.control_upgrade), name));
            builder.setPositiveButton(() -> Controllers.findControllerById(id).upgrade());
            builder.setNegativeButton(null);
            builder.create().show();
        });
    }

    public static void showIncompatibleDialog(Context context, String name) {
        Schedulers.androidUIThread().execute(() -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(String.format(context.getString(R.string.control_incompatible), name));
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
            jsonObject.add("buttonStyles", gson.toJsonTree(buttonStyleStream.collect(Collectors.toList()), new TypeToken<ArrayList<ControlButtonStyle>>(){}.getType()).getAsJsonArray());
            jsonObject.add("directionStyles", gson.toJsonTree(directionStyleStream.collect(Collectors.toList()), new TypeToken<ArrayList<ControlDirectionStyle>>(){}.getType()).getAsJsonArray());
            jsonObject.add("viewGroups", gson.toJsonTree(new ArrayList<>(src.viewGroups()), new TypeToken<ArrayList<ControlViewGroup>>(){}.getType()).getAsJsonArray());

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
                    showIncompatibleDialog(FCLPath.CONTEXT, name);
                    return new Controller("Incompatible Controller - " + name);
                }

                List<ControlButtonStyle> buttonStyles = gson.fromJson(Optional.ofNullable(obj.get("buttonStyles")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlButtonStyle>>() {}.getType());
                List<ControlDirectionStyle> directionStyles = gson.fromJson(Optional.ofNullable(obj.get("directionStyles")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlDirectionStyle>>() {}.getType());
                ButtonStyles.init();
                DirectionStyles.init();
                buttonStyles.forEach(ButtonStyles::addStyle);
                directionStyles.forEach(DirectionStyles::addStyle);
                ObservableList<ControlViewGroup> viewGroups = FXCollections.observableList(gson.fromJson(Optional.ofNullable(obj.get("viewGroups")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlViewGroup>>(){}.getType()));

                if (controllerVersion < Constants.CONTROLLER_VERSION) {
                    showUpgradeDialog(FCLPath.CONTEXT, name, id);
                }
                return new Controller(id, name, version, versionCode, author, description, controllerVersion, viewGroups);
            } catch (Exception e) {
                throw new JsonParseException("Controller file may broken!");
            }
        }

    }
}
