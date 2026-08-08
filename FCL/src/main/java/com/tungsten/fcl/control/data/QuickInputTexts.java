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
import java.util.List;
import java.util.logging.Level;

import kotlinx.coroutines.flow.StateFlow;

/**
 * 快捷输入文本库（阶段 4b）：ObservableList 已替换为 FlowList，
 * 列表成员变更即落盘（对齐原 onInvalidating 监听语义）。
 */
public class QuickInputTexts {

    private QuickInputTexts() {
    }

    private static final FlowList<String> inputTexts = new FlowList<>();

    /**
     * True if {@link #init()} hasn't been called.
     */
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    private static void updateInputTextsStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized)
            return;
        // update storage
        saveInputTexts();
    }

    static {
        FlowSubscriptions.subscribe(inputTexts.flow(), v -> updateInputTextsStorages());
    }

    public static void init() {
        if (initialized)
            throw new IllegalStateException("Already initialized");

        inputTexts.addAll(getInputTextsFromDisk());

        initialized = true;
    }

    private static ArrayList<String> getInputTextsFromDisk() {
        try {
            File file = new File(FCLPath.CONTROLLER_DIR + "/input/input_text.json");
            if (file.exists()) {
                String json = FileUtils.readText(file);
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                return gson.fromJson(json, TypeToken.getParameterized(ArrayList.class, String.class).getType());
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to get quick input text", e);
        } catch (JsonSyntaxException e) {
            new File(FCLPath.CONTROLLER_DIR + "/input/input_text.json").delete();
        }
        return new ArrayList<>();
    }

    public static List<String> getInputTexts() {
        return inputTexts.get();
    }

    public static StateFlow<List<String>> inputTextsFlow() {
        return inputTexts.flow();
    }

    public static void saveInputTexts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(new ArrayList<>(inputTexts.get()));
        try {
            FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR + "/input/input_text.json"), json);
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to save quick input text", e);
        }
    }

    public static void addInputText(String inputText) {
        if (!initialized) return;
        inputTexts.add(inputText);
    }

    public static void removeInputText(String inputText) {
        if (!initialized) return;
        inputTexts.remove(inputText);
    }

}
