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
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
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
    private final IntegerProperty upKeycodeProperty = new SimpleIntegerProperty(this, "upKeycode", FCLKeycodes.KEY_W);
    
    public IntegerProperty upKeycodeProperty() {
        return upKeycodeProperty;
    }
    
    public void setUpKeycode(int keycode) {
        upKeycodeProperty.set(keycode);
    }
    
    public int getUpKeycode() {
        return upKeycodeProperty.get();
    }

    /**
     * Down keycode
     * Default is S
     */
    private final IntegerProperty downKeycodeProperty = new SimpleIntegerProperty(this, "downKeycode", FCLKeycodes.KEY_S);

    public IntegerProperty downKeycodeProperty() {
        return downKeycodeProperty;
    }

    public void setDownKeycode(int keycode) {
        downKeycodeProperty.set(keycode);
    }

    public int getDownKeycode() {
        return downKeycodeProperty.get();
    }

    /**
     * Left keycode
     * Default is A
     */
    private final IntegerProperty leftKeycodeProperty = new SimpleIntegerProperty(this, "leftKeycode", FCLKeycodes.KEY_A);

    public IntegerProperty leftKeycodeProperty() {
        return leftKeycodeProperty;
    }

    public void setLeftKeycode(int keycode) {
        leftKeycodeProperty.set(keycode);
    }

    public int getLeftKeycode() {
        return leftKeycodeProperty.get();
    }

    /**
     * Right keycode
     * Default is D
     */
    private final IntegerProperty rightKeycodeProperty = new SimpleIntegerProperty(this, "rightKeycode", FCLKeycodes.KEY_D);

    public IntegerProperty rightKeycodeProperty() {
        return rightKeycodeProperty;
    }

    public void setRightKeycode(int keycode) {
        rightKeycodeProperty.set(keycode);
    }

    public int getRightKeycode() {
        return rightKeycodeProperty.get();
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
        upKeycodeProperty.addListener(listener);
        downKeycodeProperty.addListener(listener);
        leftKeycodeProperty.addListener(listener);
        rightKeycodeProperty.addListener(listener);
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
        data.setUpKeycode(getUpKeycode());
        data.setDownKeycode(getDownKeycode());
        data.setLeftKeycode(getLeftKeycode());
        data.setRightKeycode(getRightKeycode());
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

            obj.addProperty("upKeycode", src.getUpKeycode());
            obj.addProperty("downKeycode", src.getDownKeycode());
            obj.addProperty("leftKeycode", src.getLeftKeycode());
            obj.addProperty("rightKeycode", src.getRightKeycode());
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

            data.setUpKeycode(Optional.ofNullable(obj.get("upKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_W));
            data.setDownKeycode(Optional.ofNullable(obj.get("downKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_S));
            data.setLeftKeycode(Optional.ofNullable(obj.get("leftKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_A));
            data.setRightKeycode(Optional.ofNullable(obj.get("rightKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_D));
            data.setFollowOption(getFollowOption(Optional.ofNullable(obj.get("followOption")).map(JsonElement::getAsString).orElse(FollowOption.CENTER_FOLLOW.toString())));
            data.setSneak(Optional.ofNullable(obj.get("sneak")).map(JsonElement::getAsBoolean).orElse(true));
            data.setSneakKeycode(Optional.ofNullable(obj.get("sneakKeycode")).map(JsonElement::getAsInt).orElse(FCLKeycodes.KEY_LEFTSHIFT));

            return data;
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
