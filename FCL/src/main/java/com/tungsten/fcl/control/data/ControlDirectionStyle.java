package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

import android.graphics.Color;

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
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.util.Optional;

@JsonAdapter(ControlDirectionStyle.Serializer.class)
public class ControlDirectionStyle implements Cloneable, Observable {

    public static final ControlDirectionStyle DEFAULT_DIRECTION_STYLE = new ControlDirectionStyle("Default");

    public enum Type {
        BUTTON,
        ROCKER
    }

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
     * Style type
     */
    private final ObjectProperty<Type> styleTypeProperty = new SimpleObjectProperty<>(this, "styleType", Type.BUTTON);

    public ObjectProperty<Type> styleTypeProperty() {
        return styleTypeProperty;
    }

    public void setStyleType(Type type) {
        styleTypeProperty.set(type);
    }

    public Type getStyleType() {
        return styleTypeProperty.get();
    }

    /**
     * Button style
     */
    private final ObjectProperty<ButtonStyle> buttonStyleProperty = new SimpleObjectProperty<>(this, "buttonStyle", new ButtonStyle());

    public ObjectProperty<ButtonStyle> buttonStyleProperty() {
        return buttonStyleProperty;
    }

    public void setButtonStyle(ButtonStyle buttonStyle) {
        buttonStyleProperty.set(buttonStyle);
        buttonStyle.addListener(onInvalidating(this::invalidate));
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyleProperty.get();
    }

    /**
     * Rocker style
     */
    private final ObjectProperty<RockerStyle> rockerStyleProperty = new SimpleObjectProperty<>(this, "rockerStyle", new RockerStyle());

    public ObjectProperty<RockerStyle> rockerStyleProperty() {
        return rockerStyleProperty;
    }

    public void setRockerStyle(RockerStyle rockerStyle) {
        rockerStyleProperty.set(rockerStyle);
        rockerStyle.addListener(onInvalidating(this::invalidate));
    }

    public RockerStyle getRockerStyle() {
        return rockerStyleProperty.get();
    }

    public ControlDirectionStyle(String name) {
        this.nameProperty.set(name);

        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        nameProperty.addListener(listener);
        styleTypeProperty.addListener(listener);
        buttonStyleProperty.addListener(listener);
        rockerStyleProperty.addListener(listener);
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
    public ControlDirectionStyle clone() {
        ControlDirectionStyle style = new ControlDirectionStyle(getName());
        style.setStyleType(getStyleType());
        style.setButtonStyle(getButtonStyle().clone());
        style.setRockerStyle(getRockerStyle().clone());
        return style;
    }

    public static class Serializer implements JsonSerializer<ControlDirectionStyle>, JsonDeserializer<ControlDirectionStyle> {
        @Override
        public JsonElement serialize(ControlDirectionStyle src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("name", src.getName());
            obj.addProperty("styleType", src.getStyleType().toString());
            obj.add("buttonStyle", gson.toJsonTree(src.getButtonStyle()).getAsJsonObject());
            obj.add("rockerStyle", gson.toJsonTree(src.getRockerStyle()).getAsJsonObject());

            return obj;
        }

        @Override
        public ControlDirectionStyle deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlDirectionStyle style = new ControlDirectionStyle(Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse(""));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            style.setStyleType(Optional.ofNullable(obj.get("styleType")).map(JsonElement::getAsString).orElse(Type.BUTTON.toString()).equals(Type.ROCKER.toString()) ? Type.ROCKER : Type.BUTTON);
            style.setButtonStyle(gson.fromJson(Optional.ofNullable(obj.get("buttonStyle")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new ButtonStyle()).getAsJsonObject()), new TypeToken<ButtonStyle>(){}.getType()));
            style.setRockerStyle(gson.fromJson(Optional.ofNullable(obj.get("rockerStyle")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new RockerStyle()).getAsJsonObject()), new TypeToken<RockerStyle>(){}.getType()));

            return style;
        }
    }

    @JsonAdapter(ButtonStyle.Serializer.class)
    public static class ButtonStyle implements Cloneable, Observable {

        /**
         * Button interval
         * 10 times the actual value
         */
        private final IntegerProperty intervalProperty = new SimpleIntegerProperty(this, "interval", 50);

        public IntegerProperty intervalProperty() {
            return intervalProperty;
        }

        public void setInterval(int interval) {
            intervalProperty.set(interval);
        }

        public int getInterval() {
            return intervalProperty.get();
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

        public ButtonStyle() {
            addPropertyChangedListener(onInvalidating(this::invalidate));
        }

        public void addPropertyChangedListener(InvalidationListener listener) {
            intervalProperty.addListener(listener);
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
        public ButtonStyle clone() {
            ButtonStyle style = new ButtonStyle();
            style.setInterval(getInterval());
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

        public static class Serializer implements JsonSerializer<ButtonStyle>, JsonDeserializer<ButtonStyle> {
            @Override
            public JsonElement serialize(ButtonStyle src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();

                obj.addProperty("interval", src.getInterval());
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
            public ButtonStyle deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                ButtonStyle style = new ButtonStyle();

                style.setInterval(Optional.ofNullable(obj.get("interval")).map(JsonElement::getAsInt).orElse(50));
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

    @JsonAdapter(RockerStyle.Serializer.class)
    public static class RockerStyle implements Cloneable, Observable {

        /**
         * Percentage rocker size, max is 90%, min is 10%
         * 10 times the actual value (100 - 900)
         */
        private final IntegerProperty rockerSizeProperty = new SimpleIntegerProperty(this, "rockerSize", 400);

        public IntegerProperty rockerSizeProperty() {
            return rockerSizeProperty;
        }

        public void setRockerSize(int rockerSize) {
            rockerSizeProperty.set(rockerSize);
        }

        public int getRockerSize() {
            return rockerSizeProperty.get();
        }

        /**
         * Percentage rocker background corner radius, max is 50%, min is 0%
         * 10 times the actual value (0 - 500)
         */
        private final IntegerProperty bgCornerRadiusProperty = new SimpleIntegerProperty(this, "bgCornerRadius", 500);

        public IntegerProperty bgCornerRadiusProperty() {
            return bgCornerRadiusProperty;
        }

        public void setBgCornerRadius(int bgCornerRadius) {
            bgCornerRadiusProperty.set(bgCornerRadius);
        }

        public int getBgCornerRadius() {
            return bgCornerRadiusProperty.get();
        }

        /**
         * Rocker background stroke color
         */
        private final IntegerProperty bgStrokeColorProperty = new SimpleIntegerProperty(this, "bgStrokeColor", Color.DKGRAY);

        public IntegerProperty bgStrokeColorProperty() {
            return bgStrokeColorProperty;
        }

        public void setBgStrokeColor(int bgStrokeColor) {
            bgStrokeColorProperty.set(bgStrokeColor);
        }

        public int getBgStrokeColor() {
            return bgStrokeColorProperty.get();
        }

        /**
         * Rocker background stroke width
         * 10 times the actual value
         */
        private final IntegerProperty bgStrokeWidthProperty = new SimpleIntegerProperty(this, "bgStrokeWidth", 20);

        public IntegerProperty bgStrokeWidthProperty() {
            return bgStrokeWidthProperty;
        }

        public void setBgStrokeWidth(int bgStrokeWidth) {
            bgStrokeWidthProperty.set(bgStrokeWidth);
        }

        public int getBgStrokeWidth() {
            return bgStrokeWidthProperty.get();
        }

        /**
         * Rocker background fill color
         */
        private final IntegerProperty bgFillColorProperty = new SimpleIntegerProperty(this, "bgFillColor", Color.TRANSPARENT);

        public IntegerProperty bgFillColorProperty() {
            return bgFillColorProperty;
        }

        public void setBgFillColor(int bgFillColor) {
            bgFillColorProperty.set(bgFillColor);
        }

        public int getBgFillColor() {
            return bgFillColorProperty.get();
        }

        /**
         * Percentage rocker corner radius, max is 50%, min is 0%
         * 10 times the actual value (0 - 500)
         */
        private final IntegerProperty rockerCornerRadiusProperty = new SimpleIntegerProperty(this, "rockerCornerRadius", 500);

        public IntegerProperty rockerCornerRadiusProperty() {
            return rockerCornerRadiusProperty;
        }

        public void setRockerCornerRadius(int rockerCornerRadius) {
            rockerCornerRadiusProperty.set(rockerCornerRadius);
        }

        public int getRockerCornerRadius() {
            return rockerCornerRadiusProperty.get();
        }

        /**
         * Rocker stroke color
         */
        private final IntegerProperty rockerStrokeColorProperty = new SimpleIntegerProperty(this, "rockerStrokeColor", Color.DKGRAY);

        public IntegerProperty rockerStrokeColorProperty() {
            return rockerStrokeColorProperty;
        }

        public void setRockerStrokeColor(int rockerStrokeColor) {
            rockerStrokeColorProperty.set(rockerStrokeColor);
        }

        public int getRockerStrokeColor() {
            return rockerStrokeColorProperty.get();
        }

        /**
         * Rocker stroke width
         * 10 times the actual value
         */
        private final IntegerProperty rockerStrokeWidthProperty = new SimpleIntegerProperty(this, "rockerStrokeWidth", 10);

        public IntegerProperty rockerStrokeWidthProperty() {
            return rockerStrokeWidthProperty;
        }

        public void setRockerStrokeWidth(int rockerStrokeWidth) {
            rockerStrokeWidthProperty.set(rockerStrokeWidth);
        }

        public int getRockerStrokeWidth() {
            return rockerStrokeWidthProperty.get();
        }

        /**
         * Rocker fill color
         */
        private final IntegerProperty rockerFillColorProperty = new SimpleIntegerProperty(this, "rockerFillColor", Color.GRAY);

        public IntegerProperty rockerFillColorProperty() {
            return rockerFillColorProperty;
        }

        public void setRockerFillColor(int rockerFillColor) {
            rockerFillColorProperty.set(rockerFillColor);
        }

        public int getRockerFillColor() {
            return rockerFillColorProperty.get();
        }

        public RockerStyle() {
            addPropertyChangedListener(onInvalidating(this::invalidate));
        }

        public void addPropertyChangedListener(InvalidationListener listener) {
            rockerSizeProperty.addListener(listener);
            bgCornerRadiusProperty.addListener(listener);
            bgStrokeWidthProperty.addListener(listener);
            bgStrokeColorProperty.addListener(listener);
            bgFillColorProperty.addListener(listener);
            rockerCornerRadiusProperty.addListener(listener);
            rockerStrokeWidthProperty.addListener(listener);
            rockerStrokeColorProperty.addListener(listener);
            rockerFillColorProperty.addListener(listener);
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
        public RockerStyle clone() {
            RockerStyle style = new RockerStyle();
            style.setRockerSize(getRockerSize());
            style.setBgCornerRadius(getBgCornerRadius());
            style.setBgStrokeWidth(getBgStrokeWidth());
            style.setBgStrokeColor(getBgStrokeColor());
            style.setBgFillColor(getBgFillColor());
            style.setRockerCornerRadius(getRockerCornerRadius());
            style.setRockerStrokeWidth(getRockerStrokeWidth());
            style.setRockerStrokeColor(getRockerStrokeColor());
            style.setRockerFillColor(getRockerFillColor());
            return style;
        }

        public static class Serializer implements JsonSerializer<RockerStyle>, JsonDeserializer<RockerStyle> {
            @Override
            public JsonElement serialize(RockerStyle src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();

                obj.addProperty("rockerSize", src.getRockerSize());
                obj.addProperty("bgCornerRadius", src.getBgCornerRadius());
                obj.addProperty("bgStrokeWidth", src.getBgStrokeWidth());
                obj.addProperty("bgStrokeColor", src.getBgStrokeColor());
                obj.addProperty("bgFillColor", src.getBgFillColor());
                obj.addProperty("rockerCornerRadius", src.getRockerCornerRadius());
                obj.addProperty("rockerStrokeWidth", src.getRockerStrokeWidth());
                obj.addProperty("rockerStrokeColor", src.getRockerStrokeColor());
                obj.addProperty("rockerFillColor", src.getRockerFillColor());

                return obj;
            }

            @Override
            public RockerStyle deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                RockerStyle style = new RockerStyle();

                style.setRockerSize(Optional.ofNullable(obj.get("rockerSize")).map(JsonElement::getAsInt).orElse(400));
                style.setBgCornerRadius(Optional.ofNullable(obj.get("bgCornerRadius")).map(JsonElement::getAsInt).orElse(500));
                style.setBgStrokeWidth(Optional.ofNullable(obj.get("bgStrokeWidth")).map(JsonElement::getAsInt).orElse(20));
                style.setBgStrokeColor(Optional.ofNullable(obj.get("bgStrokeColor")).map(JsonElement::getAsInt).orElse(Color.DKGRAY));
                style.setBgFillColor(Optional.ofNullable(obj.get("bgFillColor")).map(JsonElement::getAsInt).orElse(Color.TRANSPARENT));
                style.setRockerCornerRadius(Optional.ofNullable(obj.get("rockerCornerRadius")).map(JsonElement::getAsInt).orElse(500));
                style.setRockerStrokeWidth(Optional.ofNullable(obj.get("rockerStrokeWidth")).map(JsonElement::getAsInt).orElse(10));
                style.setRockerStrokeColor(Optional.ofNullable(obj.get("rockerStrokeColor")).map(JsonElement::getAsInt).orElse(Color.DKGRAY));
                style.setRockerFillColor(Optional.ofNullable(obj.get("rockerFillColor")).map(JsonElement::getAsInt).orElse(Color.GRAY));

                return style;
            }
        }

    }

}
