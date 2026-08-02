package com.tungsten.fcl.control.data;

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
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.gson.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 方向键事件数据（阶段 4b）：属性已 StateFlow 化；任何字段（含键码列表成员）变更递增
 * {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(DirectionEventData.Serializer.class)
public class DirectionEventData implements Cloneable {

    public enum FollowOption {
        FIXED,
        CENTER_FOLLOW,
        FOLLOW
    }

    /**
     * Up keycode
     * Default is W
     */
    private final FlowList<Integer> upKeycodes = new FlowList<>(Collections.singletonList(FCLKeycodes.KEY_W));

    public StateFlow<List<Integer>> upKeycodesFlow() {
        return upKeycodes.flow();
    }

    public List<Integer> getUpKeycodes() {
        return upKeycodes.get();
    }

    public void setUpKeycode(List<Integer> keycode) {
        upKeycodes.setAll(keycode);
    }

    /**
     * Down keycode
     * Default is S
     */
    private final FlowList<Integer> downKeycodes = new FlowList<>(Collections.singletonList(FCLKeycodes.KEY_S));

    public StateFlow<List<Integer>> downKeycodesFlow() {
        return downKeycodes.flow();
    }

    public List<Integer> getDownKeycodes() {
        return downKeycodes.get();
    }

    public void setDownKeycode(List<Integer> keycode) {
        downKeycodes.setAll(keycode);
    }

    /**
     * Left keycode
     * Default is A
     */
    private final FlowList<Integer> leftKeycodes = new FlowList<>(Collections.singletonList(FCLKeycodes.KEY_A));

    public StateFlow<List<Integer>> leftKeycodesFlow() {
        return leftKeycodes.flow();
    }

    public List<Integer> getLeftKeycodes() {
        return leftKeycodes.get();
    }

    public void setLeftKeycode(List<Integer> keycode) {
        leftKeycodes.setAll(keycode);
    }

    /**
     * Right keycode
     * Default is D
     */
    private final FlowList<Integer> rightKeycodes = new FlowList<>(Collections.singletonList(FCLKeycodes.KEY_D));

    public StateFlow<List<Integer>> rightKeycodesFlow() {
        return rightKeycodes.flow();
    }

    public List<Integer> getRightKeycodes() {
        return rightKeycodes.get();
    }

    public void setRightKeycode(List<Integer> keycode) {
        rightKeycodes.setAll(keycode);
    }

    /**
     * Follow option (only rocker style)
     */
    private final MutableStateFlow<FollowOption> followOption = StateFlowKt.MutableStateFlow(FollowOption.CENTER_FOLLOW);

    public StateFlow<FollowOption> followOptionFlow() {
        return followOption;
    }

    public void setFollowOption(FollowOption followOption) {
        this.followOption.setValue(followOption);
    }

    public FollowOption getFollowOption() {
        return followOption.getValue();
    }

    /**
     * Double click center to enable sneak
     */
    private final MutableStateFlow<Boolean> sneak = StateFlowKt.MutableStateFlow(true);

    public StateFlow<Boolean> sneakFlow() {
        return sneak;
    }

    public void setSneak(boolean sneak) {
        this.sneak.setValue(sneak);
    }

    public boolean isSneak() {
        return sneak.getValue();
    }

    /**
     * Sneak keycode
     */
    private final MutableStateFlow<Integer> sneakKeycode = StateFlowKt.MutableStateFlow(FCLKeycodes.KEY_LEFTSHIFT);

    public StateFlow<Integer> sneakKeycodeFlow() {
        return sneakKeycode;
    }

    public void setSneakKeycode(int keycode) {
        sneakKeycode.setValue(keycode);
    }

    public int getSneakKeycode() {
        return sneakKeycode.getValue();
    }

    public DirectionEventData() {
        FlowSubscriptions.subscribe(upKeycodes.flow(), v -> invalidate());
        FlowSubscriptions.subscribe(downKeycodes.flow(), v -> invalidate());
        FlowSubscriptions.subscribe(leftKeycodes.flow(), v -> invalidate());
        FlowSubscriptions.subscribe(rightKeycodes.flow(), v -> invalidate());
        FlowSubscriptions.subscribe(followOption, v -> invalidate());
        FlowSubscriptions.subscribe(sneak, v -> invalidate());
        FlowSubscriptions.subscribe(sneakKeycode, v -> invalidate());
    }

    private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

    /** 任何字段（含键码列表成员）变更时递增（对齐原 Observable 失效语义）。 */
    public StateFlow<Long> revisionFlow() {
        return revision;
    }

    private void invalidate() {
        revision.setValue(revision.getValue() + 1);
    }

    @Override
    public DirectionEventData clone() {
        DirectionEventData data = new DirectionEventData();
        data.setUpKeycode(getUpKeycodes());
        data.setDownKeycode(getDownKeycodes());
        data.setLeftKeycode(getLeftKeycodes());
        data.setRightKeycode(getRightKeycodes());
        data.setFollowOption(getFollowOption());
        data.setSneak(isSneak());
        data.setSneakKeycode(getSneakKeycode());
        return data;
    }

    public static class Serializer implements JsonSerializer<DirectionEventData>, JsonDeserializer<DirectionEventData> {
        @Override
        public JsonElement serialize(DirectionEventData src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.add("upKeycode", JsonUtils.GSON_SIMPLE.toJsonTree(new ArrayList<>(src.getUpKeycodes()), new TypeToken<ArrayList<Integer>>() {
            }.getType()).getAsJsonArray());
            obj.add("downKeycode", JsonUtils.GSON_SIMPLE.toJsonTree(new ArrayList<>(src.getDownKeycodes()), new TypeToken<ArrayList<Integer>>() {
            }.getType()).getAsJsonArray());
            obj.add("leftKeycode", JsonUtils.GSON_SIMPLE.toJsonTree(new ArrayList<>(src.getLeftKeycodes()), new TypeToken<ArrayList<Integer>>() {
            }.getType()).getAsJsonArray());
            obj.add("rightKeycode", JsonUtils.GSON_SIMPLE.toJsonTree(new ArrayList<>(src.getRightKeycodes()), new TypeToken<ArrayList<Integer>>() {
            }.getType()).getAsJsonArray());
            obj.addProperty("followOption", src.getFollowOption().toString());
            obj.addProperty("sneak", src.isSneak());
            obj.addProperty("sneakKeycode", src.getSneakKeycode());

            return obj;
        }

        @Override
        public DirectionEventData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;
            DirectionEventData data = new DirectionEventData();

            deserializeKeycodeList(obj, "upKeycode", data::setUpKeycode, FCLKeycodes.KEY_W);
            deserializeKeycodeList(obj, "downKeycode", data::setDownKeycode, FCLKeycodes.KEY_S);
            deserializeKeycodeList(obj, "leftKeycode", data::setLeftKeycode, FCLKeycodes.KEY_A);
            deserializeKeycodeList(obj, "rightKeycode", data::setRightKeycode, FCLKeycodes.KEY_D);

            data.setFollowOption(getFollowOption(Optional.ofNullable(obj.get("followOption")).map(JsonElement::getAsString).orElse(FollowOption.CENTER_FOLLOW.toString())));
            data.setSneak(Optional.ofNullable(obj.get("sneak")).map(JsonElement::getAsBoolean).orElse(true));
            data.setSneakKeycode(Optional.ofNullable(obj.get("sneakKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_LEFTSHIFT));

            return data;
        }

        /**
         * 通用的方向键反序列化方法
         */
        private void deserializeKeycodeList(JsonObject obj, String keyName, java.util.function.Consumer<List<Integer>> setter, int defaultKeycode) {
            if (obj.get(keyName).isJsonArray()) {
                setter.accept(JsonUtils.GSON_SIMPLE.fromJson(
                        Optional.ofNullable(obj.get(keyName)).map(JsonElement::getAsJsonArray).orElse(new JsonArray()),
                        new TypeToken<ArrayList<Integer>>() {
                        }.getType()
                ));
            } else {
                setter.accept(Collections.singletonList(
                        Optional.ofNullable(obj.get(keyName)).map(JsonElement::getAsInt).orElse(defaultKeycode)
                ));
            }
        }

        public FollowOption getFollowOption(String option) {
            if (option.equals(FollowOption.FIXED.toString())) {
                return FollowOption.FIXED;
            } else if (option.equals(FollowOption.FOLLOW.toString())) {
                return FollowOption.FOLLOW;
            } else {
                return FollowOption.CENTER_FOLLOW;
            }
        }
    }

}
