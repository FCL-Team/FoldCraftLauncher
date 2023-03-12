package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

import android.graphics.Color;

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
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.Optional;

@JsonAdapter(ControlButtonStyle.Serializer.class)
public class ControlButtonStyle implements Cloneable, Observable {

    public static final ControlButtonStyle DEFAULT_BUTTON_STYLE = new ControlButtonStyle("Default");

    /**
     * Style name
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
     * Button display text color
     */
    private final IntegerProperty textColorProperty = new SimpleIntegerProperty(this, "textColor", Color.WHITE);

    public IntegerProperty textColorProperty() {
        return textColorProperty;
    }

    public void setTextColor(int color) {
        textColorProperty.set(color);
    }

    public int getTextColor() {
        return textColorProperty.get();
    }

    /**
     * Button display text size
     */
    private final IntegerProperty textSizeProperty = new SimpleIntegerProperty(this, "textSize", 12);

    public IntegerProperty textSizeProperty() {
        return textSizeProperty;
    }

    public void setTextSize(int size) {
        textSizeProperty.set(size);
    }

    public int getTextSize() {
        return textSizeProperty.get();
    }

    /**
     * Button stroke width
     * 10 times the actual value
     */
    private final IntegerProperty strokeWidthProperty = new SimpleIntegerProperty(this, "strokeWidth", 10);

    public IntegerProperty strokeWidthProperty() {
        return strokeWidthProperty;
    }

    public void setStrokeWidth(int strokeWidth) {
        strokeWidthProperty.set(strokeWidth);
    }

    public int getStrokeWidth() {
        return strokeWidthProperty.get();
    }

    /**
     * Button stroke color
     */
    private final IntegerProperty strokeColorProperty = new SimpleIntegerProperty(this, "strokeColor", Color.DKGRAY);

    public IntegerProperty strokeColorProperty() {
        return strokeColorProperty;
    }

    public void setStrokeColor(int strokeColor) {
        strokeColorProperty.set(strokeColor);
    }

    public int getStrokeColor() {
        return strokeColorProperty.get();
    }

    /**
     * Button corner radius
     * 10 times the actual value
     */
    private final IntegerProperty cornerRadiusProperty = new SimpleIntegerProperty(this, "cornerRadius", 100);

    public IntegerProperty cornerRadiusProperty() {
        return cornerRadiusProperty;
    }

    public void setCornerRadius(int cornerRadius) {
        cornerRadiusProperty.set(cornerRadius);
    }

    public int getCornerRadius() {
        return cornerRadiusProperty.get();
    }

    /**
     * Button fill color
     */
    private final IntegerProperty fillColorProperty = new SimpleIntegerProperty(this, "fillColor", Color.TRANSPARENT);

    public IntegerProperty fillColorProperty() {
        return fillColorProperty;
    }

    public void setFillColor(int fillColor) {
        fillColorProperty.set(fillColor);
    }

    public int getFillColor() {
        return fillColorProperty.get();
    }

    /**
     * Button display text color (pressed)
     */
    private final IntegerProperty textColorPressedProperty = new SimpleIntegerProperty(this, "textColorPressed", Color.WHITE);

    public IntegerProperty textColorPressedProperty() {
        return textColorPressedProperty;
    }

    public void setTextColorPressed(int colorPressed) {
        textColorPressedProperty.set(colorPressed);
    }

    public int getTextColorPressed() {
        return textColorPressedProperty.get();
    }

    /**
     * Button display text size (pressed)
     */
    private final IntegerProperty textSizePressedProperty = new SimpleIntegerProperty(this, "textSizePressed", 12);

    public IntegerProperty textSizePressedProperty() {
        return textSizePressedProperty;
    }

    public void setTextSizePressed(int sizePressed) {
        textSizePressedProperty.set(sizePressed);
    }

    public int getTextSizePressed() {
        return textSizePressedProperty.get();
    }

    /**
     * Button stroke width (pressed)
     * 10 times the actual value
     */
    private final IntegerProperty strokeWidthPressedProperty = new SimpleIntegerProperty(this, "strokeWidthPressed", 10);

    public IntegerProperty strokeWidthPressedProperty() {
        return strokeWidthPressedProperty;
    }

    public void setStrokeWidthPressed(int strokeWidthPressed) {
        strokeWidthPressedProperty.set(strokeWidthPressed);
    }

    public int getStrokeWidthPressed() {
        return strokeWidthPressedProperty.get();
    }

    /**
     * Button stroke color (pressed)
     */
    private final IntegerProperty strokeColorPressedProperty = new SimpleIntegerProperty(this, "strokeColorPressed", Color.DKGRAY);

    public IntegerProperty strokeColorPressedProperty() {
        return strokeColorPressedProperty;
    }

    public void setStrokeColorPressed(int strokeColorPressed) {
        strokeColorPressedProperty.set(strokeColorPressed);
    }

    public int getStrokeColorPressed() {
        return strokeColorPressedProperty.get();
    }

    /**
     * Button corner radius (pressed)
     * 10 times the actual value
     */
    private final IntegerProperty cornerRadiusPressedProperty = new SimpleIntegerProperty(this, "cornerRadiusPressed", 100);

    public IntegerProperty cornerRadiusPressedProperty() {
        return cornerRadiusPressedProperty;
    }

    public void setCornerRadiusPressed(int cornerRadiusPressed) {
        cornerRadiusPressedProperty.set(cornerRadiusPressed);
    }

    public int getCornerRadiusPressed() {
        return cornerRadiusPressedProperty.get();
    }

    /**
     * Button fill color (pressed)
     */
    private final IntegerProperty fillColorPressedProperty = new SimpleIntegerProperty(this, "fillColorPressed", Color.LTGRAY);

    public IntegerProperty fillColorPressedProperty() {
        return fillColorPressedProperty;
    }

    public void setFillColorPressed(int fillColorPressed) {
        fillColorPressedProperty.set(fillColorPressed);
    }

    public int getFillColorPressed() {
        return fillColorPressedProperty.get();
    }

    public ControlButtonStyle(String name) {
        this.nameProperty.set(name);

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        nameProperty.addListener(listener);
        textColorProperty.addListener(listener);
        textSizeProperty.addListener(listener);
        strokeWidthProperty.addListener(listener);
        strokeColorProperty.addListener(listener);
        cornerRadiusProperty.addListener(listener);
        fillColorProperty.addListener(listener);
        textColorPressedProperty.addListener(listener);
        textSizePressedProperty.addListener(listener);
        strokeWidthPressedProperty.addListener(listener);
        strokeColorPressedProperty.addListener(listener);
        cornerRadiusPressedProperty.addListener(listener);
        fillColorPressedProperty.addListener(listener);
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
    public ControlButtonStyle clone() {
        ControlButtonStyle style = new ControlButtonStyle(getName());
        style.setTextColor(getTextColor());
        style.setTextSize(getTextSize());
        style.setStrokeColor(getStrokeColor());
        style.setStrokeWidth(getStrokeWidth());
        style.setCornerRadius(getCornerRadius());
        style.setFillColor(getFillColor());
        style.setTextColorPressed(getTextColorPressed());
        style.setTextSizePressed(getTextSizePressed());
        style.setStrokeColorPressed(getStrokeColorPressed());
        style.setStrokeWidthPressed(getStrokeWidthPressed());
        style.setCornerRadiusPressed(getCornerRadiusPressed());
        style.setFillColorPressed(getFillColorPressed());
        return style;
    }

    public static class Serializer implements JsonSerializer<ControlButtonStyle>, JsonDeserializer<ControlButtonStyle> {
        @Override
        public JsonElement serialize(ControlButtonStyle src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            obj.addProperty("name", src.getName());
            obj.addProperty("textColor", src.getTextColor());
            obj.addProperty("textSize", src.getTextSize());
            obj.addProperty("strokeColor", src.getStrokeColor());
            obj.addProperty("strokeWidth", src.getStrokeWidth());
            obj.addProperty("cornerRadius", src.getCornerRadius());
            obj.addProperty("fillColor", src.getFillColor());
            obj.addProperty("textColorPressed", src.getTextColorPressed());
            obj.addProperty("textSizePressed", src.getTextSizePressed());
            obj.addProperty("strokeColorPressed", src.getStrokeColorPressed());
            obj.addProperty("strokeWidthPressed", src.getStrokeWidthPressed());
            obj.addProperty("cornerRadiusPressed", src.getCornerRadiusPressed());
            obj.addProperty("fillColorPressed", src.getFillColorPressed());

            return obj;
        }

        @Override
        public ControlButtonStyle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlButtonStyle style = new ControlButtonStyle(Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse(""));

            style.setTextColor(Optional.ofNullable(obj.get("textColor")).map(JsonElement::getAsInt).orElse(Color.WHITE));
            style.setTextSize(Optional.ofNullable(obj.get("textSize")).map(JsonElement::getAsInt).orElse(12));
            style.setStrokeColor(Optional.ofNullable(obj.get("strokeColor")).map(JsonElement::getAsInt).orElse(Color.DKGRAY));
            style.setStrokeWidth(Optional.ofNullable(obj.get("strokeWidth")).map(JsonElement::getAsInt).orElse(10));
            style.setCornerRadius(Optional.ofNullable(obj.get("cornerRadius")).map(JsonElement::getAsInt).orElse(100));
            style.setFillColor(Optional.ofNullable(obj.get("fillColor")).map(JsonElement::getAsInt).orElse(Color.TRANSPARENT));
            style.setTextColorPressed(Optional.ofNullable(obj.get("textColorPressed")).map(JsonElement::getAsInt).orElse(Color.WHITE));
            style.setTextSizePressed(Optional.ofNullable(obj.get("textSizePressed")).map(JsonElement::getAsInt).orElse(12));
            style.setStrokeColorPressed(Optional.ofNullable(obj.get("strokeColorPressed")).map(JsonElement::getAsInt).orElse(Color.DKGRAY));
            style.setStrokeWidthPressed(Optional.ofNullable(obj.get("strokeWidthPressed")).map(JsonElement::getAsInt).orElse(10));
            style.setCornerRadiusPressed(Optional.ofNullable(obj.get("cornerRadiusPressed")).map(JsonElement::getAsInt).orElse(100));
            style.setFillColorPressed(Optional.ofNullable(obj.get("fillColorPressed")).map(JsonElement::getAsInt).orElse(Color.LTGRAY));

            return style;
        }
    }

}