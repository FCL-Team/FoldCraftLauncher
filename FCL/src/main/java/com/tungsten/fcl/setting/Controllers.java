package com.tungsten.fcl.setting;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.google.gson.GsonBuilder;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListWrapper;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Controllers {

    private Controllers() {
    }

    private static final ObservableList<Controller> controllers = observableArrayList(controller -> new Observable[] { controller });
    private static final ReadOnlyListWrapper<Controller> controllersWrapper = new ReadOnlyListWrapper<>(controllers);

    public static void checkControllers() {
        if (controllers.isEmpty()) {
            try {
                String str = IOUtils.readFullyAsString(Controllers.class.getResourceAsStream("/assets/controllers/Default.json"));
                Controller controller = new GsonBuilder()
                        .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                        .setPrettyPrinting()
                        .create().fromJson(str, Controller.class);
                controller.saveToDisk();
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to generate default controller!", e.getMessage());
            }
            controllers.addAll(getControllersFromDisk());
        }
    }

    /**
     * True if {@link #init()} hasn't been called.
     */
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    private static void updateControllerStorages() {
        // don't update the underlying storage before data loading is completed
        // otherwise it might cause data loss
        if (!initialized)
            return;
        // update storage
        File[] files = new File(FCLPath.CONTROLLER_DIR).listFiles();
        if (files != null) {
            ArrayList<String> fileNames = (ArrayList<String>) controllers.stream().map(Controller::getFileName).collect(Collectors.toList());
            for (File file : files) {
                if (file.isDirectory() || !fileNames.contains(file.getName())) {
                    file.delete();
                }
            }
        }
        for (Controller controller : controllers) {
            try {
                controller.saveToDisk();
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to save controller!", e);
            }
        }
    }

    static {
        controllers.addListener(onInvalidating(Controllers::updateControllerStorages));
        controllers.addListener(onInvalidating(Controllers::checkControllers));
    }

    public static void init() {
        if (initialized)
            throw new IllegalStateException("Already initialized");

        controllers.addAll(getControllersFromDisk());
        checkControllers();

        initialized = true;
    }

    private static ArrayList<Controller> getControllersFromDisk() {
        ArrayList<Controller> list = new ArrayList<>();
        List<File> jsons = FileUtils.listFilesByExtension(new File(FCLPath.CONTROLLER_DIR), "json");
        for (File json : jsons) {
            if (json.isFile()) {
                try {
                    String str = FileUtils.readText(json);
                    Controller controller = new GsonBuilder()
                            .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                            .setPrettyPrinting()
                            .create().fromJson(str, Controller.class);
                    list.add(controller);
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Can't read file: " + json.getAbsolutePath(), e.getMessage());
                }
            }
        }
        return list;
    }

    public static ObservableList<Controller> getControllers() {
        return controllers;
    }

    public static ReadOnlyListProperty<Controller> controllersProperty() {
        return controllersWrapper.getReadOnlyProperty();
    }

    public static void addController(Controller controller) {
        if (!initialized) return;
        controllers.add(controller);
    }

    public static void removeControllers(Controller controller) {
        if (!initialized) return;
        controllers.remove(controller);
    }

    public static Controller findControllerByName(String name) {
        checkControllers();
        return controllers.stream().filter(it -> it.getName().equals(name)).findFirst().orElse(controllers.get(0));
    }

}
