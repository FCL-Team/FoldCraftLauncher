package com.tungsten.fcl.control.data;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
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
import java.util.Objects;
import java.util.logging.Level;

public class DirectionStyles {

    private DirectionStyles() {
    }

    private static final ObservableList<ControlDirectionStyle> styles = observableArrayList(style -> new Observable[] { style });
    private static final ReadOnlyListWrapper<ControlDirectionStyle> stylesWrapper = new ReadOnlyListWrapper<>(styles);

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

        getStylesFromDisk().forEach(DirectionStyles::addStyle);
        checkStyles();

        initialized = true;
    }

    private static ArrayList<ControlDirectionStyle> getStylesFromDisk() {
        ArrayList<ControlDirectionStyle> list = new ArrayList<>();
        try {
            String json = FileUtils.readText(new File(FCLPath.CONTROLLER_DIR + "/styles/direction_styles.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ArrayList<ControlDirectionStyle> styles =  gson.fromJson(json, new TypeToken<ArrayList<ControlDirectionStyle>>(){}.getType());
            if (Objects.isNull(styles)) {
                new File(FCLPath.CONTROLLER_DIR + "/styles/button_styles.json").delete();
            } else {
                list.addAll(styles);
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(new ArrayList<>(styles));
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

    public static void removeStyles(ControlDirectionStyle style) {
        if (!initialized) return;
        styles.remove(style);
    }

    public static ControlDirectionStyle findStyleByName(String name) {
        checkStyles();
        return styles.stream().filter(it -> it.getName().equals(name)).findFirst().orElse(styles.get(0));
    }

}
