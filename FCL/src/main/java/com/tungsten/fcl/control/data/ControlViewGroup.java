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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 控件视图分组（阶段 4b）：属性已 StateFlow 化；name/visibility/viewData（替换式）
 * 变更递增 {@link #revisionFlow()}（对齐原 Observable 失效语义）。
 *
 * <p>磁盘 JSON 由手写 {@link Serializer} 产出，与属性类型无关，格式不变。</p>
 */
@JsonAdapter(ControlViewGroup.Serializer.class)
public class ControlViewGroup implements Cloneable {

    public enum Visibility {
        VISIBLE,
        INVISIBLE
    }

    /**
     * Unique id
     */
    private final String id;

    public String getId() {
        return id;
    }

    public boolean equals(ControlViewGroup viewGroup) {
        return viewGroup.getId().equals(id);
    }

    /**
     * Name
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
     * Initial visibility
     */
    private final MutableStateFlow<Visibility> visibility = StateFlowKt.MutableStateFlow(Visibility.VISIBLE);

    public StateFlow<Visibility> visibilityFlow() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility.setValue(visibility);
    }

    public Visibility getVisibility() {
        return visibility.getValue();
    }

    /**
     * View data
     */
    private final MutableStateFlow<ViewData> viewData = StateFlowKt.MutableStateFlow(new ViewData());

    public StateFlow<ViewData> viewDataFlow() {
        return viewData;
    }

    public void setViewData(ViewData viewData) {
        this.viewData.setValue(viewData);
    }

    public ViewData getViewData() {
        return viewData.getValue();
    }

    public ControlViewGroup(String id) {
        this.id = id;

        FlowSubscriptions.subscribe(name, v -> invalidate());
        FlowSubscriptions.subscribe(visibility, v -> invalidate());
        FlowSubscriptions.subscribe(viewData, v -> invalidate());
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
    public ControlViewGroup clone() {
        ControlViewGroup viewGroup = new ControlViewGroup(UUID.randomUUID().toString());
        viewGroup.setName(getName());
        viewGroup.setVisibility(getVisibility());
        viewGroup.setViewData(getViewData());
        return viewGroup;
    }

    public static class Serializer implements JsonSerializer<ControlViewGroup>, JsonDeserializer<ControlViewGroup> {
        @Override
        public JsonElement serialize(ControlViewGroup src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) return JsonNull.INSTANCE;
            JsonObject obj = new JsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            obj.addProperty("id", src.getId());
            obj.addProperty("name", src.getName());
            obj.addProperty("visibility", src.getVisibility().toString());
            obj.add("viewData", gson.toJsonTree(src.getViewData()).getAsJsonObject());

            return obj;
        }

        @Override
        public ControlViewGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                return null;
            JsonObject obj = (JsonObject) json;

            ControlViewGroup viewGroup = new ControlViewGroup(Optional.ofNullable(obj.get("id")).map(JsonElement::getAsString).orElse(UUID.randomUUID().toString()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            viewGroup.setName(Optional.ofNullable(obj.get("name")).map(JsonElement::getAsString).orElse(""));
            viewGroup.setVisibility(Optional.ofNullable(obj.get("visibility")).map(JsonElement::getAsString).orElse(Visibility.VISIBLE.toString()).equals(Visibility.INVISIBLE.toString()) ? Visibility.INVISIBLE : Visibility.VISIBLE);
            viewGroup.setViewData(gson.fromJson(Optional.ofNullable(obj.get("viewData")).map(JsonElement::getAsJsonObject).orElse(gson.toJsonTree(new ViewData()).getAsJsonObject()), new TypeToken<ViewData>() {
            }.getType()));

            return viewGroup;
        }
    }

    @JsonAdapter(ViewData.Serializer.class)
    public static class ViewData implements Cloneable {

        /**
         * Button data list
         */
        private final FlowList<ControlButtonData> buttonList = new FlowList<>();

        public StateFlow<List<ControlButtonData>> buttonListFlow() {
            return buttonList.flow();
        }

        public List<ControlButtonData> getButtonList() {
            return buttonList.get();
        }

        public void setButtonList(List<ControlButtonData> list) {
            buttonList.setAll(list);
        }

        /**
         * Direction data list
         */
        private final FlowList<ControlDirectionData> directionList = new FlowList<>();

        public StateFlow<List<ControlDirectionData>> directionListFlow() {
            return directionList.flow();
        }

        public List<ControlDirectionData> getDirectionList() {
            return directionList.get();
        }

        public void setDirectionList(List<ControlDirectionData> list) {
            directionList.setAll(list);
        }

        public void addButton(ControlButtonData data) {
            boolean exist = false;
            for (ControlButtonData buttonData : getButtonList()) {
                if (buttonData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                buttonList.add(data);
            }
        }

        public void removeButton(ControlButtonData data) {
            for (ControlButtonData buttonData : getButtonList()) {
                if (buttonData.equals(data)) {
                    buttonList.remove(buttonData);
                    break;
                }
            }
        }

        public void addDirection(ControlDirectionData data) {
            boolean exist = false;
            for (ControlDirectionData directionData : getDirectionList()) {
                if (directionData.equals(data)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                directionList.add(data);
            }
        }

        public void removeDirection(ControlDirectionData data) {
            for (ControlDirectionData directionData : getDirectionList()) {
                if (directionData.equals(data)) {
                    directionList.remove(directionData);
                    break;
                }
            }
        }

        // 元素冒泡：列表成员变更或元素自身 revision 变更均使本 ViewData 失效
        //（对齐原 addPropertyChangedListener 对列表与每个元素挂监听 + 列表变更时重挂）。
        private final Map<ControlButtonData, FlowSubscriptions.Subscription> buttonSubscriptions = new IdentityHashMap<>();
        private final Map<ControlDirectionData, FlowSubscriptions.Subscription> directionSubscriptions = new IdentityHashMap<>();

        public ViewData() {
            FlowSubscriptions.subscribe(buttonList.flow(), v -> {
                invalidate();
                rewireButtonSubscriptions();
            });
            FlowSubscriptions.subscribe(directionList.flow(), v -> {
                invalidate();
                rewireDirectionSubscriptions();
            });
            rewireButtonSubscriptions();
            rewireDirectionSubscriptions();
        }

        private void rewireButtonSubscriptions() {
            buttonSubscriptions.values().forEach(FlowSubscriptions.Subscription::cancel);
            buttonSubscriptions.clear();
            for (ControlButtonData data : getButtonList()) {
                buttonSubscriptions.put(data, FlowSubscriptions.subscribe(data.revisionFlow(), v -> invalidate()));
            }
        }

        private void rewireDirectionSubscriptions() {
            directionSubscriptions.values().forEach(FlowSubscriptions.Subscription::cancel);
            directionSubscriptions.clear();
            for (ControlDirectionData data : getDirectionList()) {
                directionSubscriptions.put(data, FlowSubscriptions.subscribe(data.revisionFlow(), v -> invalidate()));
            }
        }

        private final MutableStateFlow<Long> revision = StateFlowKt.MutableStateFlow(0L);

        /** 列表成员或元素内部变更时递增（对齐原 Observable 失效语义）。 */
        public StateFlow<Long> revisionFlow() {
            return revision;
        }

        private void invalidate() {
            revision.setValue(revision.getValue() + 1);
        }

        @Override
        public ViewData clone() {
            ViewData data = new ViewData();
            data.setButtonList(getButtonList());
            data.setDirectionList(getDirectionList());
            return data;
        }

        public static class Serializer implements JsonSerializer<ViewData>, JsonDeserializer<ViewData> {
            @Override
            public JsonElement serialize(ViewData src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) return JsonNull.INSTANCE;
                JsonObject obj = new JsonObject();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                obj.add("buttonList", gson.toJsonTree(new ArrayList<>(src.getButtonList()), new TypeToken<ArrayList<ControlButtonData>>() {
                }.getType()).getAsJsonArray());
                obj.add("directionList", gson.toJsonTree(new ArrayList<>(src.getDirectionList()), new TypeToken<ArrayList<ControlDirectionData>>() {
                }.getType()).getAsJsonArray());

                return obj;
            }

            @Override
            public ViewData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json == JsonNull.INSTANCE || !(json instanceof JsonObject))
                    return null;
                JsonObject obj = (JsonObject) json;

                ViewData data = new ViewData();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                List<ControlButtonData> buttonList = Optional.ofNullable(obj.get("buttonList"))
                        .map(JsonElement::getAsJsonArray)
                        .orElse(new JsonArray())
                        .asList()
                        .stream()
                        .parallel()
                        .map(button -> {
                            if (button != null) {
                                return gson.fromJson(button, ControlButtonData.class);
                            }
                            throw new JsonParseException("ControlButtonData broken!");
                        }).collect(Collectors.toList());
                data.setButtonList(buttonList);
                data.setDirectionList(gson.fromJson(Optional.ofNullable(obj.get("directionList")).map(JsonElement::getAsJsonArray).orElse(new JsonArray()), new TypeToken<ArrayList<ControlDirectionData>>() {
                }.getType()));

                return data;
            }
        }
    }

}
