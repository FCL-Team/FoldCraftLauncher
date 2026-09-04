package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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

public class DirectionStyles {

    private DirectionStyles() {
    }

    private static final ObservableList<ControlDirectionStyle> styles = observableArrayList(style -> new Observable[]{style});
    private static final ReadOnlyListWrapper<ControlDirectionStyle> stylesWrapper = new ReadOnlyListWrapper<>(styles);
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void checkStyles() {
        if (!initialized)
            return;
        if (styles.isEmpty()) {
            styles.add(ControlDirectionStyle.DEFAULT_DIRECTION_STYLE);
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
        styles.addListener(onInvalidating(DirectionStyles::updateStylesStorages));
        styles.addListener(onInvalidating(DirectionStyles::checkStyles));
    }

    public static void init() {
        if (initialized)
            return;

        styles.addAll(getStylesFromDisk());
        checkStyles();

        initialized = true;
    }

    private static ArrayList<ControlDirectionStyle> getStylesFromDisk() {
        ArrayList<ControlDirectionStyle> list = new ArrayList<>();
        try {
            String json = FileUtils.readText(new File(FCLPath.CONTROLLER_DIR + "/styles/direction_styles.json"));
            JsonElement element = JsonParser.parseString(json);
            if (element.isJsonArray()) {
                for (JsonElement item : element.getAsJsonArray()) {
                    list.add(new ControlDirectionStyle.Serializer().deserialize(item, null, null));
                }
            } else {
                new File(FCLPath.CONTROLLER_DIR + "/styles/direction_styles.json").delete();
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to get direction styles", e);
        } catch (JsonSyntaxException e) {
            new File(FCLPath.CONTROLLER_DIR + "/styles/direction_styles.json").delete();
        }
        return list;
    }

    public static ObservableList<ControlDirectionStyle> getStyles() {
        return styles;
    }

    public static ReadOnlyListProperty<ControlDirectionStyle> stylesProperty() {
        return stylesWrapper.getReadOnlyProperty();
    }

    public static void saveStyles() {
        JsonArray array = new JsonArray();
        for (ControlDirectionStyle style : styles) {
            array.add(new ControlDirectionStyle.Serializer().serialize(style, null, null));
        }
        String json = PRETTY_GSON.toJson(array);
        try {
            FileUtils.writeText(new File(FCLPath.CONTROLLER_DIR + "/styles/direction_styles.json"), json);
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, "Failed to save direction styles", e);
        }
    }

    public static void addStyle(ControlDirectionStyle style) {
        if (!initialized) return;
        boolean add = true;
        for (ControlDirectionStyle directionStyle : getStyles())
            if (directionStyle.getName().equals(style.getName()))
                add = false;
        if (add)
            styles.add(style);
    }

    public static void addStyle(ControlDirectionStyle style, int index) {
        if (!initialized) return;
        boolean add = true;
        for (ControlDirectionStyle directionStyle : getStyles())
            if (directionStyle.getName().equals(style.getName()))
                add = false;
        if (add)
            styles.add(index, style);
    }

    public static void removeStyles(ControlDirectionStyle style) {
        if (!initialized) return;
        styles.remove(style);
    }

    public static ControlDirectionStyle findStyleByName(String name) {
        checkStyles();
        return styles.stream().filter(it -> it.getName().equals(name)).findFirst().orElse(styles.get(0));
    }

    public static int findStyleIndexByName(String name) {
        checkStyles();
        return styles.indexOf(findStyleByName(name));
    }
}
