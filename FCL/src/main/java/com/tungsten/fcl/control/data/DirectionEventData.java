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
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonAdapter(DirectionEventData.Serializer.class)
public class DirectionEventData implements Cloneable, Observable {

    public enum FollowOption {
        FIXED,
        CENTER_FOLLOW,
        FOLLOW
    }

    /**
     * Up keycode
     * Default is W
     */
    private final ObservableList<Integer> upKeycodeList = FXCollections.observableArrayList(FCLKeycodes.KEY_W);

    public ObservableList<Integer> upKeycodeList() {
        return upKeycodeList;
    }

    public void setUpKeycode(ObservableList<Integer> keycode) {
        upKeycodeList.setAll(keycode);
    }

    /**
     * Down keycode
     * Default is S
     */
    private final ObservableList<Integer> downKeycodeList = FXCollections.observableArrayList(FCLKeycodes.KEY_S);

    public ObservableList<Integer> downKeycodeList() {
        return downKeycodeList;
    }

    public void setDownKeycode(ObservableList<Integer> keycode) {
        downKeycodeList.setAll(keycode);
    }

    /**
     * Left keycode
     * Default is A
     */
    private final ObservableList<Integer> leftKeycodeList = FXCollections.observableArrayList(FCLKeycodes.KEY_A);

    public ObservableList<Integer> leftKeycodeList() {
        return leftKeycodeList;
    }

    public void setLeftKeycode(ObservableList<Integer> keycode) {
        leftKeycodeList.setAll(keycode);
    }

    /**
     * Right keycode
     * Default is D
     */
    private final ObservableList<Integer> rightKeycodeList = FXCollections.observableArrayList(FCLKeycodes.KEY_D);

    public ObservableList<Integer> rightKeycodeList() {
        return rightKeycodeList;
    }

    public void setRightKeycode(ObservableList<Integer> keycode) {
        rightKeycodeList.setAll(keycode);
    }

    /**
     * Follow option (only rocker style)
     */
    private final ObjectProperty<FollowOption> followOptionProperty = new SimpleObjectProperty<>(this, "followOption", FollowOption.CENTER_FOLLOW);

    public ObjectProperty<FollowOption> followOptionProperty() {
        return followOptionProperty;
    }

    public void setFollowOption(FollowOption followOption) {
        followOptionProperty.set(followOption);
    }

    public FollowOption getFollowOption() {
        return followOptionProperty.get();
    }

    /**
     * Double click center to enable sneak
     */
    private final BooleanProperty sneakProperty = new SimpleBooleanProperty(this, "sneak", true);

    public BooleanProperty sneakProperty() {
        return sneakProperty;
    }

    public void setSneak(boolean sneak) {
        sneakProperty.set(sneak);
    }

    public boolean isSneak() {
        return sneakProperty.get();
    }

    /**
     * Sneak keycode
     */
    private final IntegerProperty sneakKeycodeProperty = new SimpleIntegerProperty(this, "sneakKeycode", FCLKeycodes.KEY_LEFTSHIFT);

    public IntegerProperty sneakKeycodeProperty() {
        return sneakKeycodeProperty;
    }

    public void setSneakKeycode(int keycode) {
        sneakKeycodeProperty.set(keycode);
    }

    public int getSneakKeycode() {
        return sneakKeycodeProperty.get();
    }

    public DirectionEventData() {
        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        upKeycodeList.addListener(listener);
        downKeycodeList.addListener(listener);
        leftKeycodeList.addListener(listener);
        rightKeycodeList.addListener(listener);
        followOptionProperty.addListener(listener);
        sneakProperty.addListener(listener);
        sneakKeycodeProperty.addListener(listener);
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
    public DirectionEventData clone() {
        DirectionEventData data = new DirectionEventData();
        data.setUpKeycode(upKeycodeList());
        data.setDownKeycode(downKeycodeList());
        data.setLeftKeycode(leftKeycodeList());
        data.setRightKeycode(rightKeycodeList());
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

            obj.add("upKeycode", toJsonKeycodeList(src.upKeycodeList()));
            obj.add("downKeycode", toJsonKeycodeList(src.downKeycodeList()));
            obj.add("leftKeycode", toJsonKeycodeList(src.leftKeycodeList()));
            obj.add("rightKeycode", toJsonKeycodeList(src.rightKeycodeList()));
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
        private void deserializeKeycodeList(JsonObject obj, String keyName, java.util.function.Consumer<ObservableList<Integer>> setter, int defaultKeycode) {
            JsonElement element = obj.get(keyName);
            if (element != null && element.isJsonArray()) {
                ArrayList<Integer> keycodes = new ArrayList<>();
                for (JsonElement item : element.getAsJsonArray()) {
                    keycodes.add(item.getAsInt());
                }
                setter.accept(FXCollections.observableList(keycodes));
            } else {
                setter.accept(FXCollections.observableArrayList(
                        element != null && element.isJsonPrimitive() ? element.getAsInt() : defaultKeycode
                ));
            }
        }

        private static JsonArray toJsonKeycodeList(List<Integer> keycodes) {
            JsonArray array = new JsonArray();
            for (Integer keycode : keycodes) {
                array.add(keycode);
            }
            return array;
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
