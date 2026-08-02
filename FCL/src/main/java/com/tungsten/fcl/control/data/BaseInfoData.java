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

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 控件基础信息（阶段 4b）：属性已 StateFlow 化；任何字段变更递增
 * {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(BaseInfoData.Serializer.class)
public class BaseInfoData implements Cloneable {

    public enum SizeType {
        PERCENTAGE,
        ABSOLUTE
    }

    public enum VisibilityType {
        ALWAYS,
        IN_GAME,
        MENU
    }

    /**
     * Visibility type
     */
    private final MutableStateFlow<VisibilityType> visibilityType = StateFlowKt.MutableStateFlow(VisibilityType.ALWAYS);

    public StateFlow<VisibilityType> visibilityTypeFlow() {
        return visibilityType;
    }

    public void setVisibilityType(VisibilityType visibilityType) {
        this.visibilityType.setValue(visibilityType);
    }

    public VisibilityType getVisibilityType() {
        return visibilityType.getValue();
    }

    /**
     * Controller x percentage position
     * 10 times the actual value
     */
    private final MutableStateFlow<Integer> xPosition = StateFlowKt.MutableStateFlow(0);

    public StateFlow<Integer> xPositionFlow() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition.setValue(xPosition);
    }

    public int getXPosition() {
        return xPosition.getValue();
    }

    /**
     * Controller y percentage position
     * 10 times the actual value
     */
    private final MutableStateFlow<Integer> yPosition = StateFlowKt.MutableStateFlow(0);

    public StateFlow<Integer> yPositionFlow() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition.setValue(yPosition);
    }

    public int getYPosition() {
        return yPosition.getValue();
    }

    /**
     * Size type
     */
    private final MutableStateFlow<SizeType> sizeType = StateFlowKt.MutableStateFlow(SizeType.PERCENTAGE);

    public StateFlow<SizeType> sizeTypeFlow() {
        return sizeType;
    }

    public void setSizeType(SizeType sizeType) {
        this.sizeType.setValue(sizeType);
    }

    public SizeType getSizeType() {
        return sizeType.getValue();
    }

    /**
     * Absolute width
     * dp
     */
    private final MutableStateFlow<Integer> absoluteWidth = StateFlowKt.MutableStateFlow(50);

    public StateFlow<Integer> absoluteWidthFlow() {
        return absoluteWidth;
    }

    public void setAbsoluteWidth(int absoluteWidth) {
        this.absoluteWidth.setValue(absoluteWidth);
    }

    public int getAbsoluteWidth() {
        return absoluteWidth.getValue();
    }

    /**
     * Absolute height
     * dp
     */
    private final MutableStateFlow<Integer> absoluteHeight = StateFlowKt.MutableStateFlow(50);

    public StateFlow<Integer> absoluteHeightFlow() {
        return absoluteHeight;
    }

    public void setAbsoluteHeight(int absoluteHeight) {
        this.absoluteHeight.setValue(absoluteHeight);
    }

    public int getAbsoluteHeight() {
        return absoluteHeight.getValue();
    }

    /**
     * Percentage width
     */
    private final MutableStateFlow<PercentageSize> percentageWidth = StateFlowKt.MutableStateFlow(new PercentageSize());

    public StateFlow<PercentageSize> percentageWidthFlow() {
        return percentageWidth;
    }

    public void setPercentageWidth(PercentageSize percentageWidth) {
        this.percentageWidth.setValue(percentageWidth);
    }

    public PercentageSize getPercentageWidth() {
        return percentageWidth.getValue();
    }

    /**
     * Percentage height
     */
    private final MutableStateFlow<PercentageSize> percentageHeight = StateFlowKt.MutableStateFlow(new PercentageSize());

    public StateFlow<PercentageSize> percentageHeightFlow() {
        return percentageHeight;
    }

    public void setPercentageHeight(PercentageSize percentageHeight) {
        this.percentageHeight.setValue(percentageHeight);
    }

    public PercentageSize getPercentageHeight() {
        return percentageHeight.getValue();
    }

    public BaseInfoData() {
        FlowSubscriptions.subscribe(visibilityType, v -> invalidate());
        FlowSubscriptions.subscribe(xPosition, v -> invalidate());
        FlowSubscriptions.subscribe(yPosition, v -> invalidate());
        FlowSubscriptions.subscribe(sizeType, v -> invalidate());
        FlowSubscriptions.subscribe(absoluteWidth, v -> invalidate());
        FlowSubscriptions.subscribe(absoluteHeight, v -> invalidate());
        FlowSubscriptions.subscribe(percentageWidth, v -> invalidate());
        FlowSubscriptions.subscribe(percentageHeight, v -> invalidate());
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
    public BaseInfoData clone() {
        BaseInfoData data = new BaseInfoData();
        data.setVisibilityType(getVisibilityType());
        data.setXPosition(getXPosition());
        data.setYPosition(getYPosition());
        data.setSizeType(getSizeType());
        data.setAbsoluteWidth(getAbsoluteWidth());
        data.setAbsoluteHeight(getAbsoluteHeight());
        data.setPercentageWidth(getPercentageWidth().clone());
        data.setPercentageHeight(getPercentageHeight().clone());
        return data;
    }

    public static class Serializer implements JsonSerializer<BaseInfoData>, JsonDeserializer<BaseInfoData> {
        @Override
        public JsonElement serialize(BaseInfoData src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("visibilityType", src.getVisibilityType().toString());
            obj.addProperty("xPosition", src.getXPosition());
            obj.addProperty("yPosition", src.getYPosition());
            obj.addProperty("sizeType", src.getSizeType().toString());
            obj.addProperty("absoluteWidth", src.getAbsoluteWidth());
            obj.addProperty("absoluteHeight", src.getAbsoluteHeight());
            obj.add("percentageWidth", gson.toJsonTree(src.getPercentageWidth()).getAsJsonObject());
            obj.add("percentageHeight", gson.toJsonTree(src.getPercentageHeight()).getAsJsonObject());

            return obj;
        }

        @Override
        public BaseInfoData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            BaseInfoData data = new BaseInfoData();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            data.setVisibilityType(getVisibilityType(Optional.ofNullable(obj.get("visibilityType")).map(JsonElement::getAsString).orElse(VisibilityType.ALWAYS.toString())));
            data.setXPosition(Optional.ofNullable(obj.get("xPosition")).map(JsonElement::getAsInt).orElse(0));
            data.setYPosition(Optional.ofNullable(obj.get("yPosition")).map(JsonElement::getAsInt).orElse(0));
            data.setSizeType(Optional.ofNullable(obj.get("sizeType")).map(JsonElement::getAsString).orElse(SizeType.PERCENTAGE.toString()).equals(SizeType.ABSOLUTE.toString()) ? SizeType.ABSOLUTE : SizeType.PERCENTAGE);
            data.setAbsoluteWidth(Optional.ofNullable(obj.get("absoluteWidth")).map(JsonElement::getAsInt).orElse(50));
            data.setAbsoluteHeight(Optional.ofNullable(obj.get("absoluteHeight")).map(JsonElement::getAsInt).orElse(50));
            data.setPercentageWidth(gson.fromJson(Optional.ofNullable(obj.get("percentageWidth")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new PercentageSize()).getAsJsonObject()), new TypeToken<PercentageSize>(){}.getType()));
            data.setPercentageHeight(gson.fromJson(Optional.ofNullable(obj.get("percentageHeight")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new PercentageSize()).getAsJsonObject()), new TypeToken<PercentageSize>(){}.getType()));

            return data;
        }

        public VisibilityType getVisibilityType(String type) {
            if (type.equals(VisibilityType.IN_GAME.toString())) {
                return VisibilityType.IN_GAME;
            } else if (type.equals(VisibilityType.MENU.toString())) {
                return VisibilityType.MENU;
            } else {
                return VisibilityType.ALWAYS;
            }
        }
    }

    @JsonAdapter(PercentageSize.Serializer.class)
    public static class PercentageSize implements Cloneable {

        public enum Reference {
            SCREEN_WIDTH,
            SCREEN_HEIGHT
        }

        /**
         * Size reference
         * SCREEN_WIDTH: actual width = screen width * value
         * SCREEN_HEIGHT: actual height = screen height * value
         */
        private final MutableStateFlow<Reference> reference = StateFlowKt.MutableStateFlow(Reference.SCREEN_WIDTH);

        public StateFlow<Reference> referenceFlow() {
            return reference;
        }

        public void setReference(Reference reference) {
            this.reference.setValue(reference);
        }

        public Reference getReference() {
            return reference.getValue();
        }

        /**
         * Percentage size
         * 10 times the actual size
         */
        private final MutableStateFlow<Integer> size = StateFlowKt.MutableStateFlow(50);

        public StateFlow<Integer> sizeFlow() {
            return size;
        }

        public void setSize(int size) {
            this.size.setValue(size);
        }

        public int getSize() {
            return size.getValue();
        }

        public PercentageSize() {
            FlowSubscriptions.subscribe(reference, v -> invalidate());
            FlowSubscriptions.subscribe(size, v -> invalidate());
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
        public PercentageSize clone() {
            PercentageSize size = new PercentageSize();
            size.setReference(getReference());
            size.setSize(getSize());
            return size;
        }

        public static class Serializer implements JsonSerializer<PercentageSize>, JsonDeserializer<PercentageSize> {
            @Override
            public JsonElement serialize(PercentageSize src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();

                obj.addProperty("reference", src.getReference().toString());
                obj.addProperty("size", src.getSize());

                return obj;
            }

            @Override
            public PercentageSize deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                PercentageSize size = new PercentageSize();

                size.setReference(Optional.ofNullable(obj.get("reference")).map(JsonElement::getAsString).orElse(Reference.SCREEN_WIDTH.toString()).equals(Reference.SCREEN_HEIGHT.toString()) ? Reference.SCREEN_HEIGHT : Reference.SCREEN_WIDTH);
                size.setSize(Optional.ofNullable(obj.get("size")).map(JsonElement::getAsInt).orElse(50));

                return size;
            }
        }

    }

}
