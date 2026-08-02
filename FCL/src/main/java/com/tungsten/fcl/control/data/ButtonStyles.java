package com.tungsten.fcl.control.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import kotlinx.coroutines.flow.StateFlow;

/**
 * 按钮样式库（阶段 4b）：ObservableList(extractor) 已替换为 FlowList +
 * 逐元素 revisionFlow 订阅（对齐 extractor 冒泡语义：成员增删与元素内部
 * 属性变更均触发落盘与 checkStyles）。
 */
public class ButtonStyles {

    private ButtonStyles() {
    }

    private static final FlowList<ControlButtonStyle> styles = new FlowList<>();
    private static final Map<ControlButtonStyle, FlowSubscriptions.Subscription> styleSubscriptions = new IdentityHashMap<>();

    public static void checkStyles() {
        if (!initialized)
            return;
        if (styles.isEmpty()) {
            styles.add(ControlButtonStyle.DEFAULT_BUTTON_STYLE);
            saveStyles();
        }
    }

    /**
     * True if {@link #init()} hasn't been called.
     */
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    private static void updateStylesStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized)
            return;
        // update storage
        saveStyles();
    }

    static {
        FlowSubscriptions.subscribe(styles.flow(), v -> {
            updateStylesStorages();
            checkStyles();
            rewireStyleSubscriptions();
        });
        rewireStyleSubscriptions();
    }

    /** 元素冒泡（对齐 extractor）：元素内部属性变更 → 落盘 + checkStyles。 */
    private static void rewireStyleSubscriptions() {
        styleSubscriptions.values().forEach(FlowSubscriptions.Subscription::cancel);
        styleSubscriptions.clear();
        for (ControlButtonStyle style : styles.get()) {
            styleSubscriptions.put(style, FlowSubscriptions.subscribe(style.revisionFlow(), v -> {
                updateStylesStorages();
                checkStyles();
            }));
        }
    }

    public static void init() {
        if (initialized)
            return;

        styles.addAll(getStylesFromDisk());
        checkStyles();

        initialized = true;
    }

    private static ArrayList<ControlButtonStyle> getStylesFromDisk() {
        ArrayList<ControlButtonStyle> list = new ArrayList<>();
        try {
            String json = FileUtils.readText(new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ArrayList<ControlButtonStyle> styles = gson.fromJson(json, new TypeToken<ArrayList<ControlButtonStyle>>() {
            }.getType());
            if (Objects.isNull(styles)) {
                new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json").delete();
            } else {
                list.addAll(styles);
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to get button styles", e);
        } catch (JsonSyntaxException e) {
            new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json").delete();
        }
        return list;
    }

    public static List<ControlButtonStyle> getStyles() {
        return styles.get();
    }

    public static StateFlow<List<ControlButtonStyle>> stylesFlow() {
        return styles.flow();
    }

    public static void saveStyles() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new ArrayList<>(styles.get()));
        try {
            FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json"), json);
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to save button styles", e);
        }
    }

    public static void addStyle(ControlButtonStyle style) {
        if (!initialized) return;
        boolean add = true;
        for (ControlButtonStyle buttonStyle : getStyles())
            if (buttonStyle.getName().equals(style.getName()))
                add = false;
        if (add)
            styles.add(style);
    }

    public static void addStyle(ControlButtonStyle style, int index) {
        if (!initialized) return;
        boolean add = true;
        for (ControlButtonStyle buttonStyle : getStyles())
            if (buttonStyle.getName().equals(style.getName()))
                add = false;
        if (add)
            styles.add(index, style);
    }

    public static void removeStyles(ControlButtonStyle style) {
        if (!initialized) return;
        styles.remove(style);
    }

    public static ControlButtonStyle findStyleByName(String name) {
        checkStyles();
        return styles.get().stream().filter(it -> it.getName().equals(name)).findFirst().orElse(styles.get().get(0));
    }

    public static int findStyleIndexByName(String name) {
        checkStyles();
        return styles.get().indexOf(findStyleByName(name));
    }

}
