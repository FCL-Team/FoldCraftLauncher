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
 * 方向键控件数据（阶段 4b）：属性已 StateFlow 化；任何字段（替换式）变更递增
 * {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(ControlDirectionData.Serializer.class)
public class ControlDirectionData implements Cloneable, CustomControl {

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
    private final MutableStateFlow<ControlDirectionStyle> style = StateFlowKt.MutableStateFlow(ControlDirectionStyle.DEFAULT_DIRECTION_STYLE);

    public StateFlow<ControlDirectionStyle> styleFlow() {
        return style;
    }

    public void setStyle(ControlDirectionStyle style) {
        this.style.setValue(style);
    }

    public ControlDirectionStyle getStyle() {
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
     * Event data
     */
    private final MutableStateFlow<DirectionEventData> event = StateFlowKt.MutableStateFlow(new DirectionEventData());

    public StateFlow<DirectionEventData> eventFlow() {
        return event;
    }

    public void setEvent(DirectionEventData event) {
        this.event.setValue(event);
    }

    public DirectionEventData getEvent() {
        return event.getValue();
    }

    public ControlDirectionData(String id) {
        this.id = id;

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

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("id", src.getId());
            obj.addProperty("style", src.getStyle().getName());
            obj.add("baseInfo", gson.toJsonTree(src.getBaseInfo()).getAsJsonObject());
            obj.add("event", gson.toJsonTree(src.getEvent()).getAsJsonObject());

            return obj;
        }

        @Override
        public ControlDirectionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlDirectionData data = new ControlDirectionData(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if (!DirectionStyles.isInitialized()) {
                DirectionStyles.init();
            }
            if (obj.get("style").toString().contains("\"name\"")) {
                data.setStyle(gson.fromJson(Optional.ofNullable(obj.get("style")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(ControlDirectionStyle.DEFAULT_DIRECTION_STYLE).getAsJsonObject()), ControlDirectionStyle.class));
                DirectionStyles.addStyle(data.getStyle());
            } else {
                data.setStyle(DirectionStyles.findStyleByName(obj.get("style").getAsString()));
            }
            data.setBaseInfo(gson.fromJson(Optional.ofNullable(obj.get("baseInfo")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new BaseInfoData()).getAsJsonObject()), BaseInfoData.class));
            data.setEvent(gson.fromJson(Optional.ofNullable(obj.get("event")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new DirectionEventData()).getAsJsonObject()), DirectionEventData.class));

            return data;
        }
    }

}
