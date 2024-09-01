package com.tungsten.fcl.setting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import com.tungsten.fcl.control.GestureMode;
import com.tungsten.fcl.control.MouseMoveMode;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.DoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleDoubleProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;

import java.lang.reflect.Type;
import java.util.Optional;

@JsonAdapter(MenuSetting.Serializer.class)
public class MenuSetting {

    private final BooleanProperty autoFitProperty = new SimpleBooleanProperty(this, "autoFit", true);

    public BooleanProperty autoFitProperty() {
        return autoFitProperty;
    }

    public boolean isAutoFit() {
        return autoFitProperty.get();
    }

    public void setAutoFit(boolean autoFit) {
        this.autoFitProperty.set(autoFit);
    }

    private final IntegerProperty autoFitDistProperty = new SimpleIntegerProperty(this, "autoFitDist", 0);

    public IntegerProperty autoFitDistProperty() {
        return autoFitDistProperty;
    }

    public int getAutoFitDist() {
        return autoFitDistProperty.get();
    }

    public void setAutoFitDist(int autoFitDist) {
        this.autoFitDistProperty.set(autoFitDist);
    }

    private final BooleanProperty lockMenuViewProperty = new SimpleBooleanProperty(this, "lockMenuView", false);

    public BooleanProperty lockMenuViewProperty() {
        return lockMenuViewProperty;
    }

    public boolean isLockMenuView() {
        return lockMenuViewProperty.get();
    }

    public void setLockMenuView(boolean lockMenuView) {
        this.lockMenuViewProperty.set(lockMenuView);
    }

    private final BooleanProperty disableSoftKeyAdjustProperty = new SimpleBooleanProperty(this, "disableSoftKeyAdjust", false);

    public BooleanProperty disableSoftKeyAdjustProperty() {
        return disableSoftKeyAdjustProperty;
    }

    public boolean isDisableSoftKeyAdjust() {
        return disableSoftKeyAdjustProperty.get();
    }

    public void setDisableSoftKeyAdjust(boolean disableSoftKeyAdjust) {
        this.disableSoftKeyAdjustProperty.set(disableSoftKeyAdjust);
    }

    private final BooleanProperty showLogProperty = new SimpleBooleanProperty(this, "showLog", false);

    public BooleanProperty showLogProperty() {
        return showLogProperty;
    }

    public boolean isshowLog() {
        return showLogProperty.get();
    }

    public void setShowLog(boolean showLog) {
        this.showLogProperty.set(showLog);
    }

    private final DoubleProperty menuPositionXProperty = new SimpleDoubleProperty(this, "menuPositionX", 0.5d);

    public DoubleProperty menuPositionXProperty() {
        return menuPositionXProperty;
    }

    public double getMenuPositionX() {
        return menuPositionXProperty.get();
    }

    public void setMenuPositionX(double menuPositionX) {
        this.menuPositionXProperty.set(menuPositionX);
    }

    private final DoubleProperty menuPositionYProperty = new SimpleDoubleProperty(this, "menuPositionY", 0.5d);

    public DoubleProperty menuPositionYProperty() {
        return menuPositionYProperty;
    }

    public double getMenuPositionY() {
        return menuPositionYProperty.get();
    }

    public void setMenuPositionY(double menuPositionY) {
        this.menuPositionYProperty.set(menuPositionY);
    }

    private final BooleanProperty disableGestureProperty = new SimpleBooleanProperty(this, "disableGesture", false);

    public BooleanProperty disableGestureProperty() {
        return disableGestureProperty;
    }

    public boolean isDisableGesture() {
        return disableGestureProperty.get();
    }

    public void setDisableGesture(boolean disableGesture) {
        this.disableGestureProperty.set(disableGesture);
    }

    private final BooleanProperty disableBEGestureProperty = new SimpleBooleanProperty(this, "disableBEGesture", false);

    public BooleanProperty disableBEGestureProperty() {
        return disableBEGestureProperty;
    }

    public boolean isDisableBEGesture() {
        return disableBEGestureProperty.get();
    }

    public void setDisableBEGesture(boolean disableBEGesture) {
        this.disableBEGestureProperty.set(disableBEGesture);
    }

    private final ObjectProperty<GestureMode> gestureModeProperty = new SimpleObjectProperty<>(this, "gestureMode", GestureMode.BUILD);

    public ObjectProperty<GestureMode> gestureModeProperty() {
        return gestureModeProperty;
    }

    public GestureMode getGestureMode() {
        return gestureModeProperty.get();
    }

    public void setGestureMode(GestureMode gestureMode) {
        gestureModeProperty.set(gestureMode);
    }

    private final BooleanProperty enableGyroscopeProperty = new SimpleBooleanProperty(this, "enableGyroscope", false);

    public BooleanProperty enableGyroscopeProperty() {
        return enableGyroscopeProperty;
    }

    public boolean isEnableGyroscope() {
        return enableGyroscopeProperty.get();
    }

    public void setEnableGyroscope(boolean enableGyroscope) {
        this.enableGyroscopeProperty.set(enableGyroscope);
    }

    private final IntegerProperty gyroscopeSensitivityProperty = new SimpleIntegerProperty(this, "gyroscopeSensitivity", 10);

    public IntegerProperty gyroscopeSensitivityProperty() {
        return gyroscopeSensitivityProperty;
    }

    public int getGyroscopeSensitivity() {
        return gyroscopeSensitivityProperty.get();
    }

    public void setGyroscopeSensitivity(int gyroscopeSensitivity) {
        this.gyroscopeSensitivityProperty.set(gyroscopeSensitivity);
    }

    private final ObjectProperty<MouseMoveMode> mouseMoveModeProperty = new SimpleObjectProperty<>(this, "mouseMoveMode", MouseMoveMode.CLICK);

    public ObjectProperty<MouseMoveMode> mouseMoveModeProperty() {
        return mouseMoveModeProperty;
    }

    public MouseMoveMode getMouseMoveMode() {
        return mouseMoveModeProperty.get();
    }

    public void setMouseMoveMode(MouseMoveMode mouseMoveMode) {
        mouseMoveModeProperty.set(mouseMoveMode);
    }

    private final IntegerProperty itemBarScaleProperty = new SimpleIntegerProperty(this, "itemBarScale", 0);

    public IntegerProperty itemBarScaleProperty() {
        return itemBarScaleProperty;
    }

    public int getItemBarScale() {
        return itemBarScaleProperty.get();
    }

    public void setItemBarScale(int itemBarScale) {
        this.itemBarScaleProperty.set(itemBarScale);
    }

    private final DoubleProperty windowScaleProperty = new SimpleDoubleProperty(this, "windowScale", 1d);

    public DoubleProperty windowScaleProperty() {
        return windowScaleProperty;
    }

    public double getWindowScale() {
        return windowScaleProperty.get();
    }

    public void setWindowScale(double windowScale) {
        this.windowScaleProperty.set(windowScale);
    }

    private final DoubleProperty cursorOffsetProperty = new SimpleDoubleProperty(this, "cursorOffset", 0);

    public DoubleProperty cursorOffsetProperty() {
        return cursorOffsetProperty;
    }

    public double getCursorOffset() {
        return cursorOffsetProperty.get();
    }

    public void setCursorOffset(double cursorOffset) {
        this.cursorOffsetProperty.set(cursorOffset);
    }

    private final DoubleProperty mouseSensitivityProperty = new SimpleDoubleProperty(this, "mouseSensitivity", 1d);

    public DoubleProperty mouseSensitivityProperty() {
        return mouseSensitivityProperty;
    }

    public double getMouseSensitivity() {
        return mouseSensitivityProperty.get();
    }

    public void setMouseSensitivity(double mouseSensitivity) {
        this.mouseSensitivityProperty.set(mouseSensitivity);
    }

    private final IntegerProperty mouseSizeProperty = new SimpleIntegerProperty(this, "mouseSize", 15);

    public IntegerProperty mouseSizeProperty() {
        return mouseSizeProperty;
    }

    public int getMouseSize() {
        return mouseSizeProperty.get();
    }

    public void setMouseSize(int mouseSize) {
        this.mouseSizeProperty.set(mouseSize);
    }

    private final DoubleProperty gamepadDeadzoneProperty = new SimpleDoubleProperty(this, "gamepadDeadzone", 1d);

    public DoubleProperty gamepadDeadzoneProperty() {
        return gamepadDeadzoneProperty;
    }

    public double getGamepadDeadzone() {
        return gamepadDeadzoneProperty.get();
    }

    public void setGamepadDeadzone(double gamepadDeadzone) {
        this.gamepadDeadzoneProperty.set(gamepadDeadzone);
    }

    private final DoubleProperty gamepadAimAssistZoneProperty = new SimpleDoubleProperty(this, "gamepadAimAssistZone", 0.95d);

    public DoubleProperty gamepadAimAssistZoneProperty() {
        return gamepadAimAssistZoneProperty;
    }

    public double getGamepadAimAssistZone() {
        return gamepadAimAssistZoneProperty.get();
    }

    public void setGamepadAimAssistZone(double gamepadAimAssistZone) {
        this.gamepadAimAssistZoneProperty.set(gamepadAimAssistZone);
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        autoFitProperty.addListener(listener);
        autoFitDistProperty.addListener(listener);
        lockMenuViewProperty.addListener(listener);
        disableSoftKeyAdjustProperty.addListener(listener);
        showLogProperty.addListener(listener);
        menuPositionXProperty.addListener(listener);
        menuPositionYProperty.addListener(listener);
        disableGestureProperty.addListener(listener);
        disableBEGestureProperty.addListener(listener);
        gestureModeProperty.addListener(listener);
        enableGyroscopeProperty.addListener(listener);
        gyroscopeSensitivityProperty.addListener(listener);
        mouseMoveModeProperty.addListener(listener);
        mouseSensitivityProperty.addListener(listener);
        mouseSizeProperty.addListener(listener);
        itemBarScaleProperty.addListener(listener);
        windowScaleProperty.addListener(listener);
        cursorOffsetProperty.addListener(listener);
        gamepadDeadzoneProperty.addListener(listener);
        gamepadAimAssistZoneProperty.addListener(listener);
    }

    public static class Serializer implements JsonSerializer<MenuSetting>, JsonDeserializer<MenuSetting> {
        @Override
        public JsonElement serialize(MenuSetting src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.addProperty("autoFit", src.isAutoFit());
            obj.addProperty("autoFitDist", src.getAutoFitDist());
            obj.addProperty("lockMenuView", src.isLockMenuView());
            obj.addProperty("disableSoftKeyAdjust", src.isDisableSoftKeyAdjust());
            obj.addProperty("showLog", src.isshowLog());
            obj.addProperty("menuPositionX", src.getMenuPositionX());
            obj.addProperty("menuPositionY", src.getMenuPositionY());
            obj.addProperty("disableGesture", src.isDisableGesture());
            obj.addProperty("disableBEGesture", src.isDisableBEGesture());
            obj.addProperty("gestureMode", src.getGestureMode().getId());
            obj.addProperty("enableGyroscope", src.isEnableGyroscope());
            obj.addProperty("gyroscopeSensitivity", src.getGyroscopeSensitivity());
            obj.addProperty("mouseMoveMode", src.getMouseMoveMode().getId());
            obj.addProperty("mouseSensitivity", src.getMouseSensitivity());
            obj.addProperty("mouseSize", src.getMouseSize());
            obj.addProperty("itemBarScale", src.getItemBarScale());
            obj.addProperty("windowScale", src.getWindowScale());
            obj.addProperty("cursorOffset", src.getCursorOffset());
            obj.addProperty("gamepadDeadzone", src.getGamepadDeadzone());
            obj.addProperty("gamepadAimAssistZone", src.getGamepadAimAssistZone());
            return obj;
        }

        @Override
        public MenuSetting deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            MenuSetting ms = new MenuSetting();

            ms.setAutoFit(Optional.ofNullable(obj.get("autoFit")).map(JsonElement::getAsBoolean).orElse(true));
            ms.setAutoFitDist(Optional.ofNullable(obj.get("autoFitDist")).map(JsonElement::getAsInt).orElse(0));
            ms.setLockMenuView(Optional.ofNullable(obj.get("lockMenuView")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setDisableSoftKeyAdjust(Optional.ofNullable(obj.get("disableSoftKeyAdjust")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setShowLog(Optional.ofNullable(obj.get("showLog")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setMenuPositionX(Optional.ofNullable(obj.get("menuPositionX")).map(JsonElement::getAsDouble).orElse(0.5d));
            ms.setMenuPositionY(Optional.ofNullable(obj.get("menuPositionY")).map(JsonElement::getAsDouble).orElse(0.5d));
            ms.setDisableGesture(Optional.ofNullable(obj.get("disableGesture")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setDisableBEGesture(Optional.ofNullable(obj.get("disableBEGesture")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setGestureMode(GestureMode.getById(Optional.ofNullable(obj.get("gestureMode")).map(JsonElement::getAsInt).orElse(0)));
            ms.setEnableGyroscope(Optional.ofNullable(obj.get("enableGyroscope")).map(JsonElement::getAsBoolean).orElse(false));
            ms.setGyroscopeSensitivity(Optional.ofNullable(obj.get("gyroscopeSensitivity")).map(JsonElement::getAsInt).orElse(10));
            ms.setMouseMoveMode(MouseMoveMode.getById(Optional.ofNullable(obj.get("mouseMoveMode")).map(JsonElement::getAsInt).orElse(0)));
            ms.setMouseSensitivity(Optional.ofNullable(obj.get("mouseSensitivity")).map(JsonElement::getAsDouble).orElse(1d));
            ms.setMouseSize(Optional.ofNullable(obj.get("mouseSize")).map(JsonElement::getAsInt).orElse(15));
            ms.setItemBarScale(Optional.ofNullable(obj.get("itemBarScale")).map(JsonElement::getAsInt).orElse(0));
            ms.setWindowScale(Optional.ofNullable(obj.get("windowScale")).map(JsonElement::getAsDouble).orElse(1d));
            ms.setCursorOffset(Optional.ofNullable(obj.get("cursorOffset")).map(JsonElement::getAsInt).orElse(0));
            ms.setGamepadDeadzone(Optional.ofNullable(obj.get("gamepadDeadzone")).map(JsonElement::getAsDouble).orElse(0.2d));
            ms.setGamepadAimAssistZone(Optional.ofNullable(obj.get("gamepadAimAssistZone")).map(JsonElement::getAsDouble).orElse(0.98d));
            return ms;
        }
    }

}
