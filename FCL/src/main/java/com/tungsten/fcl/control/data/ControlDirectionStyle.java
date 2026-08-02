package com.tungsten.fcl.control.data;

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
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import java.util.Optional;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 方向键样式（阶段 4b）：属性已 StateFlow 化；name/styleType 变更与
 * buttonStyle/rockerStyle 内部变更均递增 {@link #revisionFlow()}
 * （对齐原 Observable 失效语义，DirectionStyles 据此冒泡落盘）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(ControlDirectionStyle.Serializer.class)
public class ControlDirectionStyle implements Cloneable {

    public static final ControlDirectionStyle DEFAULT_DIRECTION_STYLE = new ControlDirectionStyle("Default");

    public enum Type {
        BUTTON,
        ROCKER
    }

    /**
     * Style name
     */
    private final MutableStateFlow<String> name = StateFlowKt.MutableStateFlow("");

    public StateFlow<String> nameFlow() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getName() {
        return name.getValue();
    }

    /**
     * Style type
     */
    private final MutableStateFlow<Type> styleType = StateFlowKt.MutableStateFlow(Type.BUTTON);

    public StateFlow<Type> styleTypeFlow() {
        return styleType;
    }

    public void setStyleType(Type type) {
        styleType.setValue(type);
    }

    public Type getStyleType() {
        return styleType.getValue();
    }

    /**
     * Button style
     */
    private final MutableStateFlow<ButtonStyle> buttonStyle = StateFlowKt.MutableStateFlow(new ButtonStyle());

    public StateFlow<ButtonStyle> buttonStyleFlow() {
        return buttonStyle;
    }

    private FlowSubscriptions.Subscription buttonStyleSubscription;

    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle.setValue(buttonStyle);
        if (buttonStyleSubscription != null)
            buttonStyleSubscription.cancel();
        buttonStyleSubscription = FlowSubscriptions.subscribe(buttonStyle.revisionFlow(), v -> invalidate());
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle.getValue();
    }

    /**
     * Rocker style
     */
    private final MutableStateFlow<RockerStyle> rockerStyle = StateFlowKt.MutableStateFlow(new RockerStyle());

    public StateFlow<RockerStyle> rockerStyleFlow() {
        return rockerStyle;
    }

    private FlowSubscriptions.Subscription rockerStyleSubscription;

    public void setRockerStyle(RockerStyle rockerStyle) {
        this.rockerStyle.setValue(rockerStyle);
        if (rockerStyleSubscription != null)
            rockerStyleSubscription.cancel();
        rockerStyleSubscription = FlowSubscriptions.subscribe(rockerStyle.revisionFlow(), v -> invalidate());
    }

    public RockerStyle getRockerStyle() {
        return rockerStyle.getValue();
    }

    public ControlDirectionStyle(String name) {
        setName(name);

        FlowSubscriptions.subscribe(this.name, v -> invalidate());
        FlowSubscriptions.subscribe(styleType, v -> invalidate());
        // 内嵌样式内部变更冒泡（对齐原 addPropertyChangedListener 对元素挂监听）
        buttonStyleSubscription = FlowSubscriptions.subscribe(getButtonStyle().revisionFlow(), v -> invalidate());
        rockerStyleSubscription = FlowSubscriptions.subscribe(getRockerStyle().revisionFlow(), v -> invalidate());
    }

    private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

    /** name/styleType 或内嵌样式内部变更时递增（对齐原 Observable 失效语义）。 */
    public StateFlow<Long> revisionFlow() {
        return revision;
    }

    private void invalidate() {
        revision.setValue(revision.getValue() + 1);
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
    public static class ButtonStyle implements Cloneable {

        /**
         * Button interval
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> interval = StateFlowKt.MutableStateFlow(50);

        public StateFlow<Integer> intervalFlow() {
            return interval;
        }

        public void setInterval(int interval) {
            this.interval.setValue(interval);
        }

        public int getInterval() {
            return interval.getValue();
        }

        /**
         * Button display text color
         */
        private final MutableStateFlow<Integer> textColor = StateFlowKt.MutableStateFlow(Color.WHITE);

        public StateFlow<Integer> textColorFlow() {
            return textColor;
        }

        public void setTextColor(int color) {
            textColor.setValue(color);
        }

        public int getTextColor() {
            return textColor.getValue();
        }

        /**
         * Button display text size
         */
        private final MutableStateFlow<Integer> textSize = StateFlowKt.MutableStateFlow(12);

        public StateFlow<Integer> textSizeFlow() {
            return textSize;
        }

        public void setTextSize(int size) {
            textSize.setValue(size);
        }

        public int getTextSize() {
            return textSize.getValue();
        }

        /**
         * Button stroke width
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> strokeWidth = StateFlowKt.MutableStateFlow(10);

        public StateFlow<Integer> strokeWidthFlow() {
            return strokeWidth;
        }

        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth.setValue(strokeWidth);
        }

        public int getStrokeWidth() {
            return strokeWidth.getValue();
        }

        /**
         * Button stroke color
         */
        private final MutableStateFlow<Integer> strokeColor = StateFlowKt.MutableStateFlow(Color.DKGRAY);

        public StateFlow<Integer> strokeColorFlow() {
            return strokeColor;
        }

        public void setStrokeColor(int strokeColor) {
            this.strokeColor.setValue(strokeColor);
        }

        public int getStrokeColor() {
            return strokeColor.getValue();
        }

        /**
         * Button corner radius
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> cornerRadius = StateFlowKt.MutableStateFlow(100);

        public StateFlow<Integer> cornerRadiusFlow() {
            return cornerRadius;
        }

        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius.setValue(cornerRadius);
        }

        public int getCornerRadius() {
            return cornerRadius.getValue();
        }

        /**
         * Button fill color
         */
        private final MutableStateFlow<Integer> fillColor = StateFlowKt.MutableStateFlow(Color.TRANSPARENT);

        public StateFlow<Integer> fillColorFlow() {
            return fillColor;
        }

        public void setFillColor(int fillColor) {
            this.fillColor.setValue(fillColor);
        }

        public int getFillColor() {
            return fillColor.getValue();
        }

        /**
         * Button display text color (pressed)
         */
        private final MutableStateFlow<Integer> textColorPressed = StateFlowKt.MutableStateFlow(Color.WHITE);

        public StateFlow<Integer> textColorPressedFlow() {
            return textColorPressed;
        }

        public void setTextColorPressed(int colorPressed) {
            textColorPressed.setValue(colorPressed);
        }

        public int getTextColorPressed() {
            return textColorPressed.getValue();
        }

        /**
         * Button display text size (pressed)
         */
        private final MutableStateFlow<Integer> textSizePressed = StateFlowKt.MutableStateFlow(12);

        public StateFlow<Integer> textSizePressedFlow() {
            return textSizePressed;
        }

        public void setTextSizePressed(int sizePressed) {
            textSizePressed.setValue(sizePressed);
        }

        public int getTextSizePressed() {
            return textSizePressed.getValue();
        }

        /**
         * Button stroke width (pressed)
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> strokeWidthPressed = StateFlowKt.MutableStateFlow(10);

        public StateFlow<Integer> strokeWidthPressedFlow() {
            return strokeWidthPressed;
        }

        public void setStrokeWidthPressed(int strokeWidthPressed) {
            this.strokeWidthPressed.setValue(strokeWidthPressed);
        }

        public int getStrokeWidthPressed() {
            return strokeWidthPressed.getValue();
        }

        /**
         * Button stroke color (pressed)
         */
        private final MutableStateFlow<Integer> strokeColorPressed = StateFlowKt.MutableStateFlow(Color.DKGRAY);

        public StateFlow<Integer> strokeColorPressedFlow() {
            return strokeColorPressed;
        }

        public void setStrokeColorPressed(int strokeColorPressed) {
            this.strokeColorPressed.setValue(strokeColorPressed);
        }

        public int getStrokeColorPressed() {
            return strokeColorPressed.getValue();
        }

        /**
         * Button corner radius (pressed)
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> cornerRadiusPressed = StateFlowKt.MutableStateFlow(100);

        public StateFlow<Integer> cornerRadiusPressedFlow() {
            return cornerRadiusPressed;
        }

        public void setCornerRadiusPressed(int cornerRadiusPressed) {
            this.cornerRadiusPressed.setValue(cornerRadiusPressed);
        }

        public int getCornerRadiusPressed() {
            return cornerRadiusPressed.getValue();
        }

        /**
         * Button fill color (pressed)
         */
        private final MutableStateFlow<Integer> fillColorPressed = StateFlowKt.MutableStateFlow(Color.LTGRAY);

        public StateFlow<Integer> fillColorPressedFlow() {
            return fillColorPressed;
        }

        public void setFillColorPressed(int fillColorPressed) {
            this.fillColorPressed.setValue(fillColorPressed);
        }

        public int getFillColorPressed() {
            return fillColorPressed.getValue();
        }

        public ButtonStyle() {
            FlowSubscriptions.subscribe(interval, v -> invalidate());
            FlowSubscriptions.subscribe(textColor, v -> invalidate());
            FlowSubscriptions.subscribe(textSize, v -> invalidate());
            FlowSubscriptions.subscribe(strokeWidth, v -> invalidate());
            FlowSubscriptions.subscribe(strokeColor, v -> invalidate());
            FlowSubscriptions.subscribe(cornerRadius, v -> invalidate());
            FlowSubscriptions.subscribe(fillColor, v -> invalidate());
            FlowSubscriptions.subscribe(textColorPressed, v -> invalidate());
            FlowSubscriptions.subscribe(textSizePressed, v -> invalidate());
            FlowSubscriptions.subscribe(strokeWidthPressed, v -> invalidate());
            FlowSubscriptions.subscribe(strokeColorPressed, v -> invalidate());
            FlowSubscriptions.subscribe(cornerRadiusPressed, v -> invalidate());
            FlowSubscriptions.subscribe(fillColorPressed, v -> invalidate());
        }

        private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

        /** 任何字段变更时递增（对齐原 Observable 失效语义）。 */
        public StateFlow<Long> revisionFlow() {
            return revision;
        }

        private void invalidate() {
            revision.setValue(revision.getValue() + 1);
        }

        @Override
        public ButtonStyle clone() {
            ButtonStyle style = new ButtonStyle();
            style.setInterval(getInterval());
            style.setTextColor(getTextColor());
            style.setTextSize(getTextSize());
            style.setStrokeWidth(getStrokeWidth());
            style.setStrokeColor(getStrokeColor());
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
    public static class RockerStyle implements Cloneable {

        /**
         * Percentage rocker size, max is 90%, min is 10%
         * 10 times the actual value (100 - 900)
         */
        private final MutableStateFlow<Integer> rockerSize = StateFlowKt.MutableStateFlow(400);

        public StateFlow<Integer> rockerSizeFlow() {
            return rockerSize;
        }

        public void setRockerSize(int rockerSize) {
            this.rockerSize.setValue(rockerSize);
        }

        public int getRockerSize() {
            return rockerSize.getValue();
        }

        /**
         * Percentage rocker background corner radius, max is 50%, min is 0%
         * 10 times the actual value (0 - 500)
         */
        private final MutableStateFlow<Integer> bgCornerRadius = StateFlowKt.MutableStateFlow(500);

        public StateFlow<Integer> bgCornerRadiusFlow() {
            return bgCornerRadius;
        }

        public void setBgCornerRadius(int bgCornerRadius) {
            this.bgCornerRadius.setValue(bgCornerRadius);
        }

        public int getBgCornerRadius() {
            return bgCornerRadius.getValue();
        }

        /**
         * Rocker background stroke color
         */
        private final MutableStateFlow<Integer> bgStrokeColor = StateFlowKt.MutableStateFlow(Color.DKGRAY);

        public StateFlow<Integer> bgStrokeColorFlow() {
            return bgStrokeColor;
        }

        public void setBgStrokeColor(int bgStrokeColor) {
            this.bgStrokeColor.setValue(bgStrokeColor);
        }

        public int getBgStrokeColor() {
            return bgStrokeColor.getValue();
        }

        /**
         * Rocker background stroke width
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> bgStrokeWidth = StateFlowKt.MutableStateFlow(20);

        public StateFlow<Integer> bgStrokeWidthFlow() {
            return bgStrokeWidth;
        }

        public void setBgStrokeWidth(int bgStrokeWidth) {
            this.bgStrokeWidth.setValue(bgStrokeWidth);
        }

        public int getBgStrokeWidth() {
            return bgStrokeWidth.getValue();
        }

        /**
         * Rocker background fill color
         */
        private final MutableStateFlow<Integer> bgFillColor = StateFlowKt.MutableStateFlow(Color.TRANSPARENT);

        public StateFlow<Integer> bgFillColorFlow() {
            return bgFillColor;
        }

        public void setBgFillColor(int bgFillColor) {
            this.bgFillColor.setValue(bgFillColor);
        }

        public int getBgFillColor() {
            return bgFillColor.getValue();
        }

        /**
         * Percentage rocker corner radius, max is 50%, min is 0%
         * 10 times the actual value (0 - 500)
         */
        private final MutableStateFlow<Integer> rockerCornerRadius = StateFlowKt.MutableStateFlow(500);

        public StateFlow<Integer> rockerCornerRadiusFlow() {
            return rockerCornerRadius;
        }

        public void setRockerCornerRadius(int rockerCornerRadius) {
            this.rockerCornerRadius.setValue(rockerCornerRadius);
        }

        public int getRockerCornerRadius() {
            return rockerCornerRadius.getValue();
        }

        /**
         * Rocker stroke color
         */
        private final MutableStateFlow<Integer> rockerStrokeColor = StateFlowKt.MutableStateFlow(Color.DKGRAY);

        public StateFlow<Integer> rockerStrokeColorFlow() {
            return rockerStrokeColor;
        }

        public void setRockerStrokeColor(int rockerStrokeColor) {
            this.rockerStrokeColor.setValue(rockerStrokeColor);
        }

        public int getRockerStrokeColor() {
            return rockerStrokeColor.getValue();
        }

        /**
         * Rocker stroke width
         * 10 times the actual value
         */
        private final MutableStateFlow<Integer> rockerStrokeWidth = StateFlowKt.MutableStateFlow(10);

        public StateFlow<Integer> rockerStrokeWidthFlow() {
            return rockerStrokeWidth;
        }

        public void setRockerStrokeWidth(int rockerStrokeWidth) {
            this.rockerStrokeWidth.setValue(rockerStrokeWidth);
        }

        public int getRockerStrokeWidth() {
            return rockerStrokeWidth.getValue();
        }

        /**
         * Rocker fill color
         */
        private final MutableStateFlow<Integer> rockerFillColor = StateFlowKt.MutableStateFlow(Color.GRAY);

        public StateFlow<Integer> rockerFillColorFlow() {
            return rockerFillColor;
        }

        public void setRockerFillColor(int rockerFillColor) {
            this.rockerFillColor.setValue(rockerFillColor);
        }

        public int getRockerFillColor() {
            return rockerFillColor.getValue();
        }

        public RockerStyle() {
            FlowSubscriptions.subscribe(rockerSize, v -> invalidate());
            FlowSubscriptions.subscribe(bgCornerRadius, v -> invalidate());
            FlowSubscriptions.subscribe(bgStrokeWidth, v -> invalidate());
            FlowSubscriptions.subscribe(bgStrokeColor, v -> invalidate());
            FlowSubscriptions.subscribe(bgFillColor, v -> invalidate());
            FlowSubscriptions.subscribe(rockerCornerRadius, v -> invalidate());
            FlowSubscriptions.subscribe(rockerStrokeWidth, v -> invalidate());
            FlowSubscriptions.subscribe(rockerStrokeColor, v -> invalidate());
            FlowSubscriptions.subscribe(rockerFillColor, v -> invalidate());
        }

        private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

        /** 任何字段变更时递增（对齐原 Observable 失效语义）。 */
        public StateFlow<Long> revisionFlow() {
            return revision;
        }

        private void invalidate() {
            revision.setValue(revision.getValue() + 1);
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
