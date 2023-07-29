package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListWrapper;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class ButtonStyles {

    private ButtonStyles() {
    }

    private static final ObservableList<ControlButtonStyle> styles = observableArrayList(style -> new Observable[] { style });
    private static final ReadOnlyListWrapper<ControlButtonStyle> stylesWrapper = new ReadOnlyListWrapper<>(styles);

    public static void checkStyles() {
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
        styles.addListener(onInvalidating(ButtonStyles::updateStylesStorages));
        styles.addListener(onInvalidating(ButtonStyles::checkStyles));
    }

    public static void init() {
        if (initialized)
            throw new IllegalStateException("Already initialized");

        styles.addAll(getStylesFromDisk());
        checkStyles();

        initialized = true;
    }

    private static ArrayList<ControlButtonStyle> getStylesFromDisk() {
        try {
            String json = FileUtils.readText(new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(json, new TypeToken<ArrayList<ControlButtonStyle>>(){}.getType());
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to get button styles", e);
            return new ArrayList<>();
        }
    }

    public static ObservableList<ControlButtonStyle> getStyles() {
        return styles;
    }

    public static ReadOnlyListProperty<ControlButtonStyle> stylesProperty() {
        return stylesWrapper.getReadOnlyProperty();
    }

    public static void saveStyles() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new ArrayList<>(styles));
        try {
            FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json"), json);
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to save button styles", e);
        }
    }

    public static void addStyle(ControlButtonStyle style) {
        if (!initialized) return;
        styles.add(style);
    }

    public static void removeStyles(ControlButtonStyle style) {
        if (!initialized) return;
        styles.remove(style);
    }

    public static ControlButtonStyle findStyleByName(String name) {
        checkStyles();
        return styles.stream().filter(it -> it.getName().equals(name)).findFirst().orElse(styles.get(0));
    }

}
