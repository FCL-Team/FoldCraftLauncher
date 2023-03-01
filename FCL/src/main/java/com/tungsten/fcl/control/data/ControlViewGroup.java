package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

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
            obj.addProperty("viewData", gson.toJson(src.getViewData()));

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
            viewGroup.setViewData(gson.fromJson(Optional.ofNullable(obj.get("viewData")).map(JsonElement::getAsString).orElse(gson.toJson(new ViewData())), ViewData.class));

            return viewGroup;
        }
    }

    @JsonAdapter(ViewData.Serializer.class)
    public static class ViewData implements Cloneable {

        /**
         * Button data list
         */
        private final ObjectProperty<ArrayList<ControlButtonData>> buttonListProperty = new SimpleObjectProperty<>(this, "buttonList", new ArrayList<>());
        
        public ObjectProperty<ArrayList<ControlButtonData>> buttonListProperty() {
            return buttonListProperty;
        }
        
        public void setButtonList(ArrayList<ControlButtonData> buttonList) {
            buttonListProperty.set(buttonList);
        }
        
        public ArrayList<ControlButtonData> getButtonList() {
            return buttonListProperty.get();
        }

        /**
         * Direction data list
         */
        private final ObjectProperty<ArrayList<ControlDirectionData>> directionListProperty = new SimpleObjectProperty<>(this, "directionList", new ArrayList<>());

        public ObjectProperty<ArrayList<ControlDirectionData>> directionListProperty() {
            return directionListProperty;
        }

        public void setDirectionList(ArrayList<ControlDirectionData> directionList) {
            directionListProperty.set(directionList);
        }

        public ArrayList<ControlDirectionData> getDirectionList() {
            return directionListProperty.get();
        }

        public void addButton(ControlButtonData data) {
            ArrayList<ControlButtonData> list = getButtonList();
            boolean exist = false;
            for (ControlButtonData buttonData : list) {
                if (buttonData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                list.add(data);
                setButtonList(list);
            }
        }

        public void removeButton(ControlButtonData data) {
            ArrayList<ControlButtonData> list = getButtonList();
            for (ControlButtonData buttonData : list) {
                if (buttonData.equals(data)) {
                    list.remove(buttonData);
                    setButtonList(list);
                    break;
                }
            }
        }

        public void addDirection(ControlDirectionData data) {
            ArrayList<ControlDirectionData> list = getDirectionList();
            boolean exist = false;
            for (ControlDirectionData directionData : list) {
                if (directionData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                list.add(data);
                setDirectionList(list);
            }
        }

        public void removeDirection(ControlDirectionData data) {
            ArrayList<ControlDirectionData> list = getDirectionList();
            for (ControlDirectionData directionData : list) {
                if (directionData.equals(data)) {
                    list.remove(directionData);
                    setDirectionList(list);
                    break;
                }
            }
        }
        
        public ViewData() {
            
        }

        public void addPropertyChangedListener(InvalidationListener listener) {
            buttonListProperty.addListener(listener);
            directionListProperty.addListener(listener);
        }

        @Override
        public ViewData clone() {
            ViewData data = new ViewData();
            data.setButtonList(getButtonList());
            data.setDirectionList(getDirectionList());
            return data;
        }

        public static class Serializer implements JsonSerializer<ViewData>, JsonDeserializer<ViewData> {
            @Override
            public JsonElement serialize(ViewData src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                obj.addProperty("buttonList", gson.toJson(src.getButtonList()));
                obj.addProperty("directionList", gson.toJson(src.getDirectionList()));

                return obj;
            }

            @Override
            public ViewData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                ViewData data = new ViewData();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                data.setButtonList(gson.fromJson(Optional.ofNullable(obj.get("buttonList")).map(JsonElement::getAsString).orElse(gson.toJson(new ArrayList<>())), new TypeToken<ControlButtonData>(){}.getType()));
                data.setDirectionList(gson.fromJson(Optional.ofNullable(obj.get("directionList")).map(JsonElement::getAsString).orElse(gson.toJson(new ArrayList<>())), new TypeToken<ControlDirectionData>(){}.getType()));

                return data;
            }
        }
    }

}
