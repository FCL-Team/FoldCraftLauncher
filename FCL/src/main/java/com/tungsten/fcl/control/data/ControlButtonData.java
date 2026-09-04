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
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

@JsonAdapter(ControlButtonData.Serializer.class)
public class ControlButtonData implements Cloneable, Observable, CustomControl {

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
     * Button display text
     */
    private final StringProperty textProperty = new SimpleStringProperty(this, "text", "");

    public StringProperty textProperty() {
        return textProperty;
    }

    public void setText(String text) {
        textProperty.set(text);
    }

    public String getText() {
        return textProperty.get();
    }

    /**
     * Button style
     */
    private final ObjectProperty<ControlButtonStyle> styleProperty = new SimpleObjectProperty<>(this, "style", ControlButtonStyle.DEFAULT_BUTTON_STYLE);

    public ObjectProperty<ControlButtonStyle> styleProperty() {
        return styleProperty;
    }

    public void setStyle(ControlButtonStyle style) {
        styleProperty.set(style);
    }

    public ControlButtonStyle getStyle() {
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
     * Button event data
     */
    public final ObjectProperty<ButtonEventData> eventProperty = new SimpleObjectProperty<>(this, "event", new ButtonEventData());

    public ObjectProperty<ButtonEventData> eventProperty() {
        return eventProperty;
    }

    public void setEvent(ButtonEventData event) {
        eventProperty.set(event);
    }

    public ButtonEventData getEvent() {
        return eventProperty.get();
    }

    public ControlButtonData(String id) {
        this.id = id;

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        textProperty.addListener(listener);
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
    public ControlButtonData clone() {
        ControlButtonData data = new ControlButtonData(UUID.randomUUID().toString());
        data.setText(getText());
        data.setStyle(getStyle().clone());
        data.setBaseInfo(getBaseInfo().clone());
        data.setEvent(getEvent().clone());
        return data;
    }

    @Override
    public ViewType getType() {
        return ViewType.CONTROL_BUTTON;
    }

    @Override
    public String getViewId() {
        return getId();
    }

    @Override
    public CustomControl cloneView() {
        ControlButtonData clone = clone();
        clone.getBaseInfo().setXPosition(0);
        clone.getBaseInfo().setYPosition(0);
        return clone;
    }

    public static class Serializer implements JsonSerializer<ControlButtonData>, JsonDeserializer<ControlButtonData> {
        @Override
        public JsonElement serialize(ControlButtonData src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.addProperty("id", src.getId());
            obj.addProperty("text", src.getText());
            obj.addProperty("style", src.getStyle().getName());
            obj.add("baseInfo", new BaseInfoData.Serializer().serialize(src.getBaseInfo(), null, null));
            obj.add("event", new ButtonEventData.Serializer().serialize(src.getEvent(), null, null));

            return obj;
        }

        @Override
        public ControlButtonData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlButtonData data = new ControlButtonData(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));

            data.setText(Optional.ofNullable(obj.get("text")).map(JsonElement::getAsString).orElse(""));
            if (!ButtonStyles.isInitialized()) {
                ButtonStyles.init();
            }
            if (obj.get("style").toString().contains("\"name\"")) {
                data.setStyle(new ControlButtonStyle.Serializer().deserialize(obj.get("style"), null, null));
                ButtonStyles.addStyle(data.getStyle());
            } else {
                data.setStyle(ButtonStyles.findStyleByName(obj.get("style").getAsString()));
            }
            data.setBaseInfo(Optional.ofNullable(obj.get("baseInfo")).map(JsonElement::getAsJsonObject).map(baseInfo -> new BaseInfoData.Serializer().deserialize(baseInfo, null, null)).orElseGet(BaseInfoData::new));
            data.setEvent(Optional.ofNullable(obj.get("event")).map(JsonElement::getAsJsonObject).map(event -> new ButtonEventData.Serializer().deserialize(event, null, null)).orElseGet(ButtonEventData::new));

            return data;
        }
    }

}
