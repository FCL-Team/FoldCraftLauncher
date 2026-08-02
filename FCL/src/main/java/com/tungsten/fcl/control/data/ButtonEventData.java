package com.tungsten.fcl.control.data;

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
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 按钮事件数据（阶段 4b）：属性已 StateFlow 化；任何字段（含列表成员）变更递增
 * {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(ButtonEventData.Serializer.class)
public class ButtonEventData implements Cloneable {

    /**
     * Control mouse pointer
     */
    private final MutableStateFlow<Boolean> pointerFollow = StateFlowKt.MutableStateFlow(false);

    public StateFlow<Boolean> pointerFollowFlow() {
        return pointerFollow;
    }

    public void setPointerFollow(boolean pointerFollow) {
        this.pointerFollow.setValue(pointerFollow);
    }

    public boolean isPointerFollow() {
        return pointerFollow.getValue();
    }

    /**
     * Movable
     */
    private final MutableStateFlow<Boolean> movable = StateFlowKt.MutableStateFlow(false);

    public StateFlow<Boolean> movableFlow() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable.setValue(movable);
    }

    public boolean isMovable() {
        return movable.getValue();
    }

    /**
     * Press event
     */
    private final MutableStateFlow<Event> pressEvent = StateFlowKt.MutableStateFlow(new Event());

    public StateFlow<Event> pressEventFlow() {
        return pressEvent;
    }

    public void setPressEvent(Event event) {
        pressEvent.setValue(event);
    }

    public Event getPressEvent() {
        return pressEvent.getValue();
    }

    /**
     * Long press event
     */
    private final MutableStateFlow<Event> longPressEvent = StateFlowKt.MutableStateFlow(new Event());

    public StateFlow<Event> longPressEventFlow() {
        return longPressEvent;
    }

    public void setLongPressEvent(Event event) {
        longPressEvent.setValue(event);
    }

    public Event getLongPressEvent() {
        return longPressEvent.getValue();
    }

    /**
     * Click event
     */
    private final MutableStateFlow<Event> clickEvent = StateFlowKt.MutableStateFlow(new Event());

    public StateFlow<Event> clickEventFlow() {
        return clickEvent;
    }

    public void setClickEvent(Event event) {
        clickEvent.setValue(event);
    }

    public Event getClickEvent() {
        return clickEvent.getValue();
    }

    /**
     * Click event
     */
    private final MutableStateFlow<Event> doubleClickEvent = StateFlowKt.MutableStateFlow(new Event());

    public StateFlow<Event> doubleClickEventFlow() {
        return doubleClickEvent;
    }

    public void setDoubleClickEvent(Event event) {
        doubleClickEvent.setValue(event);
    }

    public Event getDoubleClickEvent() {
        return doubleClickEvent.getValue();
    }

    public ButtonEventData() {
        FlowSubscriptions.subscribe(pointerFollow, v -> invalidate());
        FlowSubscriptions.subscribe(movable, v -> invalidate());
        FlowSubscriptions.subscribe(pressEvent, v -> invalidate());
        FlowSubscriptions.subscribe(longPressEvent, v -> invalidate());
        FlowSubscriptions.subscribe(clickEvent, v -> invalidate());
        FlowSubscriptions.subscribe(doubleClickEvent, v -> invalidate());
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
    public ButtonEventData clone() {
        ButtonEventData data = new ButtonEventData();
        data.setPointerFollow(isPointerFollow());
        data.setMovable(isMovable());
        data.setPressEvent(getPressEvent().clone());
        data.setLongPressEvent(getLongPressEvent().clone());
        data.setClickEvent(getClickEvent().clone());
        data.setDoubleClickEvent(getDoubleClickEvent().clone());
        return data;
    }

    public static class Serializer implements JsonSerializer<ButtonEventData>, JsonDeserializer<ButtonEventData> {
        @Override
        public JsonElement serialize(ButtonEventData src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("pointerFollow", src.isPointerFollow());
            obj.addProperty("Movable", src.isMovable());
            obj.add("pressEvent", gson.toJsonTree(src.getPressEvent()).getAsJsonObject());
            obj.add("longPressEvent", gson.toJsonTree(src.getLongPressEvent()).getAsJsonObject());
            obj.add("clickEvent", gson.toJsonTree(src.getClickEvent()).getAsJsonObject());
            obj.add("doubleClickEvent", gson.toJsonTree(src.getDoubleClickEvent()).getAsJsonObject());

            return obj;
        }

        @Override
        public ButtonEventData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ButtonEventData data = new ButtonEventData();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            data.setPointerFollow(Optional.ofNullable(obj.get("pointerFollow")).map(JsonElement::getAsBoolean).orElse(false));
            data.setMovable(Optional.ofNullable(obj.get("Movable")).map(JsonElement::getAsBoolean).orElse(false));
            data.setPressEvent(gson.fromJson(Optional.ofNullable(obj.get("pressEvent")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new Event()).getAsJsonObject()), new TypeToken<Event>(){}.getType()));
            data.setLongPressEvent(gson.fromJson(Optional.ofNullable(obj.get("longPressEvent")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new Event()).getAsJsonObject()), new TypeToken<Event>(){}.getType()));
            data.setClickEvent(gson.fromJson(Optional.ofNullable(obj.get("clickEvent")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new Event()).getAsJsonObject()), new TypeToken<Event>(){}.getType()));
            data.setDoubleClickEvent(gson.fromJson(Optional.ofNullable(obj.get("doubleClickEvent")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new Event()).getAsJsonObject()), new TypeToken<Event>(){}.getType()));

            return data;
        }
    }

    @JsonAdapter(Event.Serializer.class)
    public static class Event implements Cloneable {

        /**
         * Keep pressing
         */
        private final MutableStateFlow<Boolean> autoKeep = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> autoKeepFlow() {
            return autoKeep;
        }

        public void setAutoKeep(boolean autoKeep) {
            this.autoKeep.setValue(autoKeep);
        }

        public boolean isAutoKeep() {
            return autoKeep.getValue();
        }

        /**
         * Keep clicking
         */
        private final MutableStateFlow<Boolean> autoClick = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> autoClickFlow() {
            return autoClick;
        }

        public void setAutoClick(boolean autoClick) {
            this.autoClick.setValue(autoClick);
        }

        public boolean isAutoClick() {
            return autoClick.getValue();
        }

        /**
         * Open menu
         */
        private final MutableStateFlow<Boolean> openMenu = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> openMenuFlow() {
            return openMenu;
        }

        public void setOpenMenu(boolean openMenu) {
            this.openMenu.setValue(openMenu);
        }

        public boolean isOpenMenu() {
            return openMenu.getValue();
        }

        /**
         * Switch touch mode
         */
        private final MutableStateFlow<Boolean> switchTouchMode = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> switchTouchModeFlow() {
            return switchTouchMode;
        }

        public void setSwitchTouchMode(boolean switchTouchMode) {
            this.switchTouchMode.setValue(switchTouchMode);
        }

        public boolean isSwitchTouchMode() {
            return switchTouchMode.getValue();
        }

        /**
         * Switch mouse mode
         */
        private final MutableStateFlow<Boolean> switchMouseMode = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> switchMouseModeFlow() {
            return switchMouseMode;
        }

        public void setSwitchMouseMode(boolean switchMouseMode) {
            this.switchMouseMode.setValue(switchMouseMode);
        }

        public boolean isSwitchMouseMode() {
            return switchMouseMode.getValue();
        }

        /**
         * Input words
         */
        private final MutableStateFlow<Boolean> input = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> inputFlow() {
            return input;
        }

        public void setInput(boolean input) {
            this.input.setValue(input);
        }

        public boolean isInput() {
            return input.getValue();
        }

        /**
         * Open quick input dialog
         */
        private final MutableStateFlow<Boolean> quickInput = StateFlowKt.MutableStateFlow(false);

        public StateFlow<Boolean> quickInputFlow() {
            return quickInput;
        }

        public void setQuickInput(boolean quickInput) {
            this.quickInput.setValue(quickInput);
        }

        public boolean isQuickInput() {
            return quickInput.getValue();
        }

        /**
         * Output text
         */
        private final MutableStateFlow<String> outputText = StateFlowKt.MutableStateFlow("");

        public StateFlow<String> outputTextFlow() {
            return outputText;
        }

        public void setOutputText(String outputText) {
            this.outputText.setValue(outputText);
        }

        public String getOutputText() {
            return outputText.getValue();
        }

        /**
         * Output keycodes
         */
        private final FlowList<Integer> outputKeycodes = new FlowList<>();

        public StateFlow<List<Integer>> outputKeycodesFlow() {
            return outputKeycodes.flow();
        }

        public List<Integer> getOutputKeycodes() {
            return outputKeycodes.get();
        }

        public void setOutputKeycodes(List<Integer> keycodes) {
            outputKeycodes.setAll(keycodes);
        }

        /**
         * Switch view group visibility
         */
        private final FlowList<String> bindViewGroups = new FlowList<>();

        public StateFlow<List<String>> bindViewGroupsFlow() {
            return bindViewGroups.flow();
        }

        public List<String> getBindViewGroups() {
            return bindViewGroups.get();
        }

        public void setBindViewGroup(List<String> bindViewGroup) {
            bindViewGroups.setAll(bindViewGroup);
        }

        public Event() {
            FlowSubscriptions.subscribe(autoKeep, v -> invalidate());
            FlowSubscriptions.subscribe(autoClick, v -> invalidate());
            FlowSubscriptions.subscribe(openMenu, v -> invalidate());
            FlowSubscriptions.subscribe(switchTouchMode, v -> invalidate());
            FlowSubscriptions.subscribe(switchMouseMode, v -> invalidate());
            FlowSubscriptions.subscribe(input, v -> invalidate());
            FlowSubscriptions.subscribe(quickInput, v -> invalidate());
            FlowSubscriptions.subscribe(outputText, v -> invalidate());
            FlowSubscriptions.subscribe(outputKeycodes.flow(), v -> invalidate());
            FlowSubscriptions.subscribe(bindViewGroups.flow(), v -> invalidate());
        }

        private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

        /** 任何字段（含列表成员）变更时递增（对齐原 Observable 失效语义）。 */
        public StateFlow<Long> revisionFlow() {
            return revision;
        }

        private void invalidate() {
            revision.setValue(revision.getValue() + 1);
        }

        @Override
        public Event clone() {
            Event event = new Event();
            event.setAutoKeep(isAutoKeep());
            event.setAutoClick(isAutoClick());
            event.setOpenMenu(isOpenMenu());
            event.setSwitchTouchMode(isSwitchTouchMode());
            event.setSwitchMouseMode(isSwitchMouseMode());
            event.setInput(isInput());
            event.setQuickInput(isQuickInput());
            event.setOutputText(getOutputText());
            event.setOutputKeycodes(getOutputKeycodes());
            event.setBindViewGroup(getBindViewGroups());
            return event;
        }

        public static class Serializer implements JsonSerializer<Event>, JsonDeserializer<Event> {
            @Override
            public JsonElement serialize(Event src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                obj.addProperty("autoKeep", src.isAutoKeep());
                obj.addProperty("autoClick", src.isAutoClick());
                obj.addProperty("openMenu", src.isOpenMenu());
                obj.addProperty("switchTouchMode", src.isSwitchTouchMode());
                obj.addProperty("switchMouseMode", src.isSwitchMouseMode());
                obj.addProperty("input", src.isInput());
                obj.addProperty("quickInput", src.isQuickInput());
                obj.addProperty("outputText", src.getOutputText());
                obj.add("outputKeycodes", gson.toJsonTree(new ArrayList<>(src.getOutputKeycodes()), new TypeToken<ArrayList<Integer>>(){}.getType()).getAsJsonArray());
                obj.add("bindViewGroup", gson.toJsonTree(new ArrayList<>(src.getBindViewGroups()), new TypeToken<ArrayList<String>>(){}.getType()).getAsJsonArray());

                return obj;
            }

            @Override
            public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                Event event = new Event();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                event.setAutoKeep(Optional.ofNullable(obj.get("autoKeep")).map(JsonElement::getAsBoolean).orElse(false));
                event.setAutoClick(Optional.ofNullable(obj.get("autoClick")).map(JsonElement::getAsBoolean).orElse(false));
                event.setOpenMenu(Optional.ofNullable(obj.get("openMenu")).map(JsonElement::getAsBoolean).orElse(false));
                event.setSwitchTouchMode(Optional.ofNullable(obj.get("switchTouchMode")).map(JsonElement::getAsBoolean).orElse(false));
                event.setSwitchMouseMode(Optional.ofNullable(obj.get("switchMouseMode")).map(JsonElement::getAsBoolean).orElse(false));
                event.setInput(Optional.ofNullable(obj.get("input")).map(JsonElement::getAsBoolean).orElse(false));
                event.setQuickInput(Optional.ofNullable(obj.get("quickInput")).map(JsonElement::getAsBoolean).orElse(false));
                event.setOutputText(Optional.ofNullable(obj.get("outputText")).map(JsonElement::getAsString).orElse(""));
                event.setOutputKeycodes(gson.fromJson(Optional.ofNullable(obj.get("outputKeycodes")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<Integer>>(){}.getType()));
                event.setBindViewGroup(gson.fromJson(Optional.ofNullable(obj.get("bindViewGroup")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<String>>(){}.getType()));

                return event;
            }
        }

    }

}
