package com.tungsten.fcl.setting;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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
import com.tungsten.fcl.control.data.ControlViewGroup;
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
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonAdapter(Controller.Serializer.class)
public class Controller implements Cloneable, Observable {

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
        this(name, "");
    }

    public Controller(String name, String version) {
        this(name, version, "");
    }

    public Controller(String name, String version, String author) {
        this(name, version, author, "");
    }

    public Controller(String name, String version, String author, String description) {
        this(name, version, author, description, Constants.CONTROLLER_VERSION);
    }

    public Controller(String name, String version, String author, String description, int controllerVersion) {
        this(name, version, author, description, controllerVersion, FXCollections.observableArrayList(new ArrayList<>()));
    }

    public Controller(String name, String version, String author, String description, int controllerVersion, ObservableList<ControlViewGroup> viewGroups) {
        this.name = new SimpleStringProperty(this, "name", name);
        this.version = new SimpleStringProperty(this, "version", version);
        this.author = new SimpleStringProperty(this, "author", author);
        this.description = new SimpleStringProperty(this, "description", description);
        this.viewGroups = viewGroups;

        this.controllerVersion.set(controllerVersion);

        addPropertyChangedListener(onInvalidating(this::invalidate));
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
                .append("name", getName())
                .append("version", getVersion())
                .append("author", getAuthor())
                .append("description", getDescription())
                .append("controllerVersion", getControllerVersion())
                .toString();
    }

    private void addPropertyChangedListener(InvalidationListener listener) {
        name.addListener(listener);
        version.addListener(listener);
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
        return new Controller(getName() + "_clone", getVersion(), getAuthor(), getDescription(), getControllerVersion(), viewGroups);
    }

    // function

    public String getFileName() {
        return getName() + ".json";
    }

    public void saveToDisk() throws IOException {
        String str = new GsonBuilder()
                .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                .setPrettyPrinting()
                .create().toJson(this);
        FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR, getFileName()), str);
    }

    public void rename(String newName) throws IOException {
        FileUtils.copyFile(new File(FCLPath.CONTROLLER_DIR, getFileName()), new File(FCLPath.CONTROLLER_DIR, newName + ".json"));
        new File(FCLPath.CONTROLLER_DIR, getFileName()).delete();
        setName(newName);
    }

    public static final class Serializer implements JsonSerializer<Controller>, JsonDeserializer<Controller> {
        @Override
        public JsonElement serialize(Controller src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null)
                return JsonNull.INSTANCE;

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", src.getName());
            jsonObject.addProperty("version", src.getVersion());
            jsonObject.addProperty("author", src.getAuthor());
            jsonObject.addProperty("description", src.getDescription());
            jsonObject.addProperty("controllerVersion", src.getControllerVersion());
            jsonObject.add("viewGroups", gson.toJsonTree(new ArrayList<>(src.viewGroups()), new TypeToken<ArrayList<ControlViewGroup>>(){}.getType()).getAsJsonArray());

            return jsonObject;
        }

        @Override
        public Controller deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject)) return null;
            JsonObject obj = (JsonObject) json;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String name = Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse("Error");
            String version = Optional.ofNullable(obj.get("version")).map(JsonElement::getAsString).orElse("");
            String author = Optional.ofNullable(obj.get("author")).map(JsonElement::getAsString).orElse("");
            String description = Optional.ofNullable(obj.get("description")).map(JsonElement::getAsString).orElse("");
            int controllerVersion = Optional.ofNullable(obj.get("controllerVersion")).map(JsonElement::getAsInt).orElse(Constants.CONTROLLER_VERSION);
            ObservableList<ControlViewGroup> viewGroups = FXCollections.observableList(gson.fromJson(Optional.ofNullable(obj.get("viewGroups")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlViewGroup>>(){}.getType()));

            return new Controller(name, version, author, description, controllerVersion, viewGroups);
        }

    }
}
