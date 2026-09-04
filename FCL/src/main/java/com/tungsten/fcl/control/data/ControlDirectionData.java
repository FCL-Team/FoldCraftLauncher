package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

@JsonAdapter(ControlDirectionData.Serializer.class)
public class ControlDirectionData implements Cloneable, Observable, CustomControl {

    /**
     * Unique id
     */
    private final String id;

    public String getId() {
        return id;
    }

    public boolean equals(ControlButtonData data) {
        return data.getId().equals(id);
    }

    /**
     * Control direction style
     */
    private final ObjectProperty<ControlDirectionStyle> styleProperty = new SimpleObjectProperty<>(this, "style", ControlDirectionStyle.DEFAULT_DIRECTION_STYLE);

    public ObjectProperty<ControlDirectionStyle> styleProperty() {
        return styleProperty;
    }

    public void setStyle(ControlDirectionStyle style) {
        styleProperty.set(style);
    }

    public ControlDirectionStyle getStyle() {
        return styleProperty.get();
    }

    /**
     * Base info data
     * Contains position and size
     */
    public final ObjectProperty<BaseInfoData> baseInfoProperty = new SimpleObjectProperty<>(this, "baseInfo", new BaseInfoData());

    public ObjectProperty<BaseInfoData> baseInfoProperty() {
        return baseInfoProperty;
    }

    public void setBaseInfo(BaseInfoData baseInfo) {
        baseInfoProperty.set(baseInfo);
    }

    public BaseInfoData getBaseInfo() {
        return baseInfoProperty.get();
    }

    /**
     * Event data
     */
    public final ObjectProperty<DirectionEventData> eventProperty = new SimpleObjectProperty<>(this, "event", new DirectionEventData());

    public ObjectProperty<DirectionEventData> eventProperty() {
        return eventProperty;
    }

    public void setEvent(DirectionEventData event) {
        eventProperty.set(event);
    }

    public DirectionEventData getEvent() {
        return eventProperty.get();
    }

    public ControlDirectionData(String id) {
        this.id = id;

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        styleProperty.addListener(listener);
        baseInfoProperty.addListener(listener);
        eventProperty.addListener(listener);
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
        try {
            observableHelper.invalidate();
        } catch (NullPointerException ignore) {
        }
    }

    @Override
    public ControlDirectionData clone() {
        ControlDirectionData data = new ControlDirectionData(UUID.randomUUID().toString());
        data.setStyle(getStyle().clone());
        data.setBaseInfo(getBaseInfo().clone());
        data.setEvent(getEvent().clone());
        return data;
    }

    @Override
    public ViewType getType() {
        return ViewType.CONTROL_DIRECTION;
    }

    @Override
    public String getViewId() {
        return getId();
    }

    @Override
    public CustomControl cloneView() {
        ControlDirectionData clone = clone();
        clone.getBaseInfo().setXPosition(0);
        clone.getBaseInfo().setYPosition(0);
        return clone;
    }

    public static class Serializer implements JsonSerializer<ControlDirectionData>, JsonDeserializer<ControlDirectionData> {
        @Override
        public JsonElement serialize(ControlDirectionData src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.addProperty("id", src.getId());
            obj.addProperty("style", src.getStyle().getName());
            obj.add("baseInfo", new BaseInfoData.Serializer().serialize(src.getBaseInfo(), null, null));
            obj.add("event", new DirectionEventData.Serializer().serialize(src.getEvent(), null, null));

            return obj;
        }

        @Override
        public ControlDirectionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlDirectionData data = new ControlDirectionData(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));

            if (!DirectionStyles.isInitialized()) {
                DirectionStyles.init();
            }
            if (obj.get("style").toString().contains("\"name\"")) {
                data.setStyle(new ControlDirectionStyle.Serializer().deserialize(obj.get("style"), null, null));
                DirectionStyles.addStyle(data.getStyle());
            } else {
                data.setStyle(DirectionStyles.findStyleByName(obj.get("style").getAsString()));
            }
            data.setBaseInfo(Optional.ofNullable(obj.get("baseInfo")).map(JsonElement::getAsJsonObject).map(baseInfo -> new BaseInfoData.Serializer().deserialize(baseInfo, null, null)).orElseGet(BaseInfoData::new));
            data.setEvent(Optional.ofNullable(obj.get("event")).map(JsonElement::getAsJsonObject).map(event -> new DirectionEventData.Serializer().deserialize(event, null, null)).orElseGet(DirectionEventData::new));

            return data;
        }
    }

}
