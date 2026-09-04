package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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

    /**
     * 完整按键数据（viewData）是否已加载：轻量加载只解析布局元数据，
     * 布局首次显示/编辑前通过 controllers 异步补全。
     */
    private transient boolean dataLoaded;

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded = dataLoaded;
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

            obj.addProperty("id", src.getId());
            obj.addProperty("name", src.getName());
            obj.addProperty("visibility", src.getVisibility().toString());
            obj.add("viewData", new ViewData.Serializer().serialize(src.getViewData(), null, null));

            return obj;
        }

        @Override
        public ControlViewGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlViewGroup viewGroup = new ControlViewGroup(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));

            viewGroup.setName(Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse(""));
            viewGroup.setVisibility(Optional.ofNullable(obj.get("visibility")).map(JsonElement::getAsString).orElse(Visibility.VISIBLE.toString()).equals(Visibility.INVISIBLE.toString()) ? Visibility.INVISIBLE : Visibility.VISIBLE);
            viewGroup.setViewData(Optional.ofNullable(obj.get("viewData")).map(JsonElement::getAsJsonObject).map(viewData -> new ViewData.Serializer().deserialize(viewData, null, null)).orElseGet(ViewData::new));

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

                obj.add("buttonList", toJsonButtonList(src.buttonList()));
                obj.add("directionList", toJsonDirectionList(src.directionList()));

                return obj;
            }

            private static JsonArray toJsonButtonList(ObservableList<ControlButtonData> buttons) {
                JsonArray array = new JsonArray();
                for (ControlButtonData button : buttons) {
                    array.add(new ControlButtonData.Serializer().serialize(button, null, null));
                }
                return array;
            }

            private static JsonArray toJsonDirectionList(ObservableList<ControlDirectionData> directions) {
                JsonArray array = new JsonArray();
                for (ControlDirectionData direction : directions) {
                    array.add(new ControlDirectionData.Serializer().serialize(direction, null, null));
                }
                return array;
            }

            @Override
            public ViewData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                ViewData data = new ViewData();
                ArrayList<ControlButtonData> buttonList = new ArrayList<>();
                JsonArray buttonArray = Optional.ofNullable(obj.get("buttonList")).map(JsonElement::getAsJsonArray).orElseGet(JsonArray::new);
                for (JsonElement button : buttonArray) {
                    if (button != null) {
                        buttonList.add(new ControlButtonData.Serializer().deserialize(button, null, null));
                    } else {
                        throw new JsonParseException("ControlButtonData broken!");
                    }
                }
                data.setButtonList(FXCollections.observableList(buttonList));
                ArrayList<ControlDirectionData> directionList = new ArrayList<>();
                for (JsonElement direction : Optional.ofNullable(obj.get("directionList")).map(JsonElement::getAsJsonArray).orElseGet(JsonArray::new)) {
                    directionList.add(new ControlDirectionData.Serializer().deserialize(direction, null, null));
                }
                data.setDirectionList(FXCollections.observableList(directionList));

                return data;
            }
        }
    }

}
