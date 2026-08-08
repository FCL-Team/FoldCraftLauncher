package com.tungsten.fcl.control.data;

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
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 按钮控件数据（阶段 4b）：属性已 StateFlow 化；任何字段（替换式）变更递增
 * {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(ControlButtonData.Serializer.class)
public class ControlButtonData implements Cloneable, CustomControl {

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
    private final MutableStateFlow<String> text = StateFlowKt.MutableStateFlow("");

    public StateFlow<String> textFlow() {
        return text;
    }

    public void setText(String text) {
        this.text.setValue(text);
    }

    public String getText() {
        return text.getValue();
    }

    /**
     * Button style
     */
    private final MutableStateFlow<ControlButtonStyle> style = StateFlowKt.MutableStateFlow(ControlButtonStyle.DEFAULT_BUTTON_STYLE);

    public StateFlow<ControlButtonStyle> styleFlow() {
        return style;
    }

    public void setStyle(ControlButtonStyle style) {
        this.style.setValue(style);
    }

    public ControlButtonStyle getStyle() {
        return style.getValue();
    }

    /**
     * Base info data
     * Contains position and size
     */
    private final MutableStateFlow<BaseInfoData> baseInfo = StateFlowKt.MutableStateFlow(new BaseInfoData());

    public StateFlow<BaseInfoData> baseInfoFlow() {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfoData baseInfo) {
        this.baseInfo.setValue(baseInfo);
    }

    public BaseInfoData getBaseInfo() {
        return baseInfo.getValue();
    }

    /**
     * Button event data
     */
    private final MutableStateFlow<ButtonEventData> event = StateFlowKt.MutableStateFlow(new ButtonEventData());

    public StateFlow<ButtonEventData> eventFlow() {
        return event;
    }

    public void setEvent(ButtonEventData event) {
        this.event.setValue(event);
    }

    public ButtonEventData getEvent() {
        return event.getValue();
    }

    public ControlButtonData(String id) {
        this.id = id;

        FlowSubscriptions.subscribe(text, v -> invalidate());
        FlowSubscriptions.subscribe(style, v -> invalidate());
        FlowSubscriptions.subscribe(baseInfo, v -> invalidate());
        FlowSubscriptions.subscribe(event, v -> invalidate());
    }

    private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

    /** 任何字段（替换式）变更时递增（对齐原 Observable 失效语义）。 */
    public StateFlow<Long> revisionFlow() {
        return revision;
    }

    private void invalidate() {
        revision.setValue(revision.getValue() + 1);
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

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("id", src.getId());
            obj.addProperty("text", src.getText());
            obj.addProperty("style", src.getStyle().getName());
            obj.add("baseInfo", gson.toJsonTree(src.getBaseInfo()).getAsJsonObject());
            obj.add("event", gson.toJsonTree(src.getEvent()).getAsJsonObject());

            return obj;
        }

        @Override
        public ControlButtonData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlButtonData data = new ControlButtonData(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            data.setText(Optional.ofNullable(obj.get("text")).map(JsonElement::getAsString).orElse(""));
            if (!ButtonStyles.isInitialized()) {
                ButtonStyles.init();
            }
            if (obj.get("style").toString().contains("\"name\"")) {
                data.setStyle(gson.fromJson(Optional.ofNullable(obj.get("style")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(ControlButtonStyle.DEFAULT_BUTTON_STYLE).getAsJsonObject()), ControlButtonStyle.class));
                ButtonStyles.addStyle(data.getStyle());
            } else {
                data.setStyle(ButtonStyles.findStyleByName(obj.get("style").getAsString()));
            }
            data.setBaseInfo(gson.fromJson(Optional.ofNullable(obj.get("baseInfo")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new BaseInfoData()).getAsJsonObject()), BaseInfoData.class));
            data.setEvent(gson.fromJson(Optional.ofNullable(obj.get("event")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new ButtonEventData()).getAsJsonObject()), ButtonEventData.class));

            return data;
        }
    }

}
