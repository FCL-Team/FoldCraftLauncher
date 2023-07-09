package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

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
import com.tungsten.fclcore.util.fakefx.ObservableHelper;

import java.lang.reflect.Type;
import java.util.Optional;

@JsonAdapter(BaseInfoData.Serializer.class)
public class BaseInfoData implements Cloneable, Observable {

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
    private final ObjectProperty<VisibilityType> visibilityTypeProperty = new SimpleObjectProperty<>(this, "visibilityType", VisibilityType.ALWAYS);

    public ObjectProperty<VisibilityType> visibilityTypeProperty() {
        return visibilityTypeProperty;
    }

    public void setVisibilityType(VisibilityType visibilityType) {
        visibilityTypeProperty.set(visibilityType);
    }

    public VisibilityType getVisibilityType() {
        return visibilityTypeProperty.get();
    }

    /**
     * Controller x percentage position
     * 10 times the actual value
     */
    private final IntegerProperty xPositionProperty = new SimpleIntegerProperty(this, "xPosition", 0);

    public IntegerProperty xPositionProperty() {
        return xPositionProperty;
    }

    public void setXPosition(int xPosition) {
        xPositionProperty.set(xPosition);
    }

    public int getXPosition() {
        return xPositionProperty.get();
    }

    /**
     * Controller y percentage position
     * 10 times the actual value
     */
    private final IntegerProperty yPositionProperty = new SimpleIntegerProperty(this, "yPosition", 0);

    public IntegerProperty yPositionProperty() {
        return yPositionProperty;
    }

    public void setYPosition(int yPosition) {
        yPositionProperty.set(yPosition);
    }

    public int getYPosition() {
        return yPositionProperty.get();
    }

    /**
     * Size type
     */
    private final ObjectProperty<SizeType> sizeTypeProperty = new SimpleObjectProperty<>(this, "sizeType", SizeType.PERCENTAGE);

    public ObjectProperty<SizeType> sizeTypeProperty() {
        return sizeTypeProperty;
    }

    public void setSizeType(SizeType sizeType) {
        sizeTypeProperty.set(sizeType);
    }

    public SizeType getSizeType() {
        return sizeTypeProperty.get();
    }

    /**
     * Absolute width
     * dp
     */
    private final IntegerProperty absoluteWidthProperty = new SimpleIntegerProperty(this, "absoluteWidth", 50);

    public IntegerProperty absoluteWidthProperty() {
        return absoluteWidthProperty;
    }

    public void setAbsoluteWidth(int absoluteWidth) {
        absoluteWidthProperty.set(absoluteWidth);
    }

    public int getAbsoluteWidth() {
        return absoluteWidthProperty.get();
    }

    /**
     * Absolute height
     * dp
     */
    private final IntegerProperty absoluteHeightProperty = new SimpleIntegerProperty(this, "absoluteHeight", 50);

    public IntegerProperty absoluteHeightProperty() {
        return absoluteHeightProperty;
    }

    public void setAbsoluteHeight(int absoluteHeight) {
        absoluteHeightProperty.set(absoluteHeight);
    }

    public int getAbsoluteHeight() {
        return absoluteHeightProperty.get();
    }

    /**
     * Percentage width
     */
    private final ObjectProperty<PercentageSize> percentageWidthProperty = new SimpleObjectProperty<>(this, "percentageWidth", new PercentageSize());

    public ObjectProperty<PercentageSize> percentageWidthProperty() {
        return percentageWidthProperty;
    }

    public void setPercentageWidth(PercentageSize percentageWidth) {
        percentageWidthProperty.set(percentageWidth);
    }

    public PercentageSize getPercentageWidth() {
        return percentageWidthProperty.get();
    }

    /**
     * Percentage height
     */
    private final ObjectProperty<PercentageSize> percentageHeightProperty = new SimpleObjectProperty<>(this, "percentageHeight", new PercentageSize());

    public ObjectProperty<PercentageSize> percentageHeightProperty() {
        return percentageHeightProperty;
    }

    public void setPercentageHeight(PercentageSize percentageHeight) {
        percentageHeightProperty.set(percentageHeight);
    }

    public PercentageSize getPercentageHeight() {
        return percentageHeightProperty.get();
    }

    public BaseInfoData() {
        addPropertyChangedListener(onInvalidating(this::invalidate));
    }

    public void addPropertyChangedListener(InvalidationListener listener) {
        visibilityTypeProperty.addListener(listener);
        xPositionProperty.addListener(listener);
        yPositionProperty.addListener(listener);
        sizeTypeProperty.addListener(listener);
        absoluteWidthProperty.addListener(listener);
        absoluteHeightProperty.addListener(listener);
        percentageWidthProperty.addListener(listener);
        percentageHeightProperty.addListener(listener);
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
    public static class PercentageSize implements Cloneable, Observable {
        
        public enum Reference {
            SCREEN_WIDTH,
            SCREEN_HEIGHT
        }

        /**
         * Size reference
         * SCREEN_WIDTH: actual width = screen width * value
         * SCREEN_HEIGHT: actual height = screen height * value
         */
        private final ObjectProperty<Reference> referenceProperty = new SimpleObjectProperty<>(this, "reference", Reference.SCREEN_WIDTH);

        public ObjectProperty<Reference> referenceProperty() {
            return referenceProperty;
        }

        public void setReference(Reference reference) {
            referenceProperty.set(reference);
        }

        public Reference getReference() {
            return referenceProperty.get();
        }

        /**
         * Percentage size
         * 10 times the actual size
         */
        private final IntegerProperty sizeProperty = new SimpleIntegerProperty(this, "size", 50);

        public IntegerProperty sizeProperty() {
            return sizeProperty;
        }

        public void setSize(int size) {
            sizeProperty.set(size);
        }

        public int getSize() {
            return sizeProperty.get();
        }
        
        public PercentageSize() {
            addPropertyChangedListener(onInvalidating(this::invalidate));
        }

        public void addPropertyChangedListener(InvalidationListener listener) {
            referenceProperty.addListener(listener);
            sizeProperty.addListener(listener);
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
