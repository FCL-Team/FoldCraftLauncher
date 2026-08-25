package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@JsonAdapter(ControlViewGroup.Serializer.class)
public class ControlViewGroup implements Cloneable, Observable {

    public enum Visibility {
        VISIBLE,
        INVISIBLE
    }

    /**
     * Unique id
     */
    private final String id;

    public String getId() {
        return id;
    }

    public boolean equals(ControlViewGroup viewGroup) {
        return viewGroup.getId().equals(id);
    }

    /**
     * Name
     */
    private final StringProperty nameProperty = new SimpleStringProperty(this, "name", "");

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public void setName(String name) {
        nameProperty.set(name);
    }

    public String getName() {
        return nameProperty.get();
    }

    /**
     * Initial visibility
     */
    private final ObjectProperty<Visibility> visibilityProperty = new SimpleObjectProperty<>(this, "visibility", Visibility.VISIBLE);

    public ObjectProperty<Visibility> visibilityProperty() {
        return visibilityProperty;
    }

    public void setVisibility(Visibility visibility) {
        visibilityProperty.set(visibility);
    }

    public Visibility getVisibility() {
        return visibilityProperty.get();
    }

    /**
     * View data
     */
    private final ObjectProperty<ViewData> viewDataProperty = new SimpleObjectProperty<>(this, "viewData", new ViewData());

    public ObjectProperty<ViewData> viewDataProperty() {
        return viewDataProperty;
    }

    public void setViewData(ViewData viewData) {
        viewDataProperty.set(viewData);
    }

    public ViewData getViewData() {
        return viewDataProperty.get();
    }

    public ControlViewGroup(String id) {
        this.id = id;

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        nameProperty.addListener(listener);
        visibilityProperty.addListener(listener);
        viewDataProperty.addListener(listener);
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
    public ControlViewGroup clone() {
        ControlViewGroup viewGroup = new ControlViewGroup(UUID.randomUUID().toString());
        viewGroup.setName(getName());
        viewGroup.setVisibility(getVisibility());
        viewGroup.setViewData(getViewData());
        return viewGroup;
    }

    public static class Serializer implements JsonSerializer<ControlViewGroup>, JsonDeserializer<ControlViewGroup> {
        @Override
        public JsonElement serialize(ControlViewGroup src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("id", src.getId());
            obj.addProperty("name", src.getName());
            obj.addProperty("visibility", src.getVisibility().toString());
            obj.add("viewData", gson.toJsonTree(src.getViewData()).getAsJsonObject());

            return obj;
        }

        @Override
        public ControlViewGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlViewGroup viewGroup = new ControlViewGroup(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            viewGroup.setName(Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse(""));
            viewGroup.setVisibility(Optional.ofNullable(obj.get("visibility")).map(JsonElement::getAsString).orElse(Visibility.VISIBLE.toString()).equals(Visibility.INVISIBLE.toString()) ? Visibility.INVISIBLE : Visibility.VISIBLE);
            viewGroup.setViewData(gson.fromJson(Optional.ofNullable(obj.get("viewData")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new ViewData()).getAsJsonObject()), new TypeToken<ViewData>() {
            }.getType()));

            return viewGroup;
        }
    }

    @JsonAdapter(ViewData.Serializer.class)
    public static class ViewData implements Cloneable, Observable {

        /**
         * Button data list
         */
        private final ObservableList<ControlButtonData> buttonList = FXCollections.observableArrayList(new ArrayList<>());

        public ObservableList<ControlButtonData> buttonList() {
            return buttonList;
        }

        public void setButtonList(ObservableList<ControlButtonData> list) {
            buttonList.setAll(list);
        }

        /**
         * Direction data list
         */
        private final ObservableList<ControlDirectionData> directionList = FXCollections.observableArrayList(new ArrayList<>());

        public ObservableList<ControlDirectionData> directionList() {
            return directionList;
        }

        public void setDirectionList(ObservableList<ControlDirectionData> list) {
            directionList.setAll(list);
        }

        public void addButton(ControlButtonData data) {
            boolean exist = false;
            for (ControlButtonData buttonData : buttonList()) {
                if (buttonData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                buttonList.add(data);
            }
        }

        public void removeButton(ControlButtonData data) {
            for (ControlButtonData buttonData : buttonList()) {
                if (buttonData.equals(data)) {
                    buttonList.remove(buttonData);
                    break;
                }
            }
        }

        public void addDirection(ControlDirectionData data) {
            boolean exist = false;
            for (ControlDirectionData directionData : directionList()) {
                if (directionData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                directionList.add(data);
            }
        }

        public void removeDirection(ControlDirectionData data) {
            for (ControlDirectionData directionData : directionList()) {
                if (directionData.equals(data)) {
                    directionList.remove(directionData);
                    break;
                }
            }
        }

        public ViewData() {
            addPropertyChangedListener(onInvalidating(this::invalidate));
        }

        public void addPropertyChangedListener(InvalidationListener listener) {
            buttonList.addListener(listener);
            buttonList.forEach(it -> it.addListener(listener));
            buttonList.addListener((InvalidationListener) observable -> {
                buttonList.forEach(it -> it.removeListener(listener));
                buttonList.forEach(it -> it.addListener(listener));
            });
            directionList.addListener(listener);
            directionList.forEach(it -> it.addListener(listener));
            directionList.addListener((InvalidationListener) observable -> {
                directionList.forEach(it -> it.removeListener(listener));
                directionList.forEach(it -> it.addListener(listener));
            });
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
        public ViewData clone() {
            ViewData data = new ViewData();
            data.setButtonList(buttonList());
            data.setDirectionList(directionList());
            return data;
        }

        public static class Serializer implements JsonSerializer<ViewData>, JsonDeserializer<ViewData> {
            @Override
            public JsonElement serialize(ViewData src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                obj.add("buttonList", gson.toJsonTree(new ArrayList<>(src.buttonList()), new TypeToken<ArrayList<ControlButtonData>>() {
                }.getType()).getAsJsonArray());
                obj.add("directionList", gson.toJsonTree(new ArrayList<>(src.directionList()), new TypeToken<ArrayList<ControlDirectionData>>() {
                }.getType()).getAsJsonArray());

                return obj;
            }

            @Override
            public ViewData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                ViewData data = new ViewData();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ObservableList<ControlButtonData> buttonList = FXCollections.observableList(Optional.ofNullable(obj.get("buttonList"))
                        .map(JsonElement::getAsJsonArray)
                        .orElse(new JsonArray())
                        .asList()
                        .stream()
                        .parallel()
                        .map(button -> {
                            if (button != null) {
                                return gson.fromJson(button, ControlButtonData.class);
                            }
                            throw new JsonParseException("ControlButtonData broken!");
                        }).collect(Collectors.toList()));
                data.setButtonList(buttonList);
                data.setDirectionList(FXCollections.observableList(gson.fromJson(Optional.ofNullable(obj.get("directionList")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlDirectionData>>() {
                }.getType())));

                return data;
            }
        }
    }

}
