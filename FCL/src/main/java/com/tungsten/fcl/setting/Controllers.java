package com.tungsten.fcl.setting;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.gson.observable.factories.JavaFxPropertyTypeAdapterFactory;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 控制器仓库（阶段 4a）：列表已 StateFlow 化（成员增删 → 新快照发射）。
 * 元素冒泡（extractor 语义：控制器内部任何变更 → 全部落盘）由对每个控制器
 * revisionFlow 的直接订阅承接，控制器移出列表时取消订阅。
 * 任何变化（成员或元素）都会递增 {@link #controllersSignal}（供 UI 刷新）。
 */
public class Controllers {

    private Controllers() {
    }

    private static final MutableStateFlow<List<Controller>> controllers = StateFlowKt.MutableStateFlow(new ArrayList<>());

    /** 列表任何变化（成员增删或元素内部变更）时递增的信号流（供 UI 刷新）。 */
    private static final MutableStateFlow<Long> controllersSignal = StateFlowKt.MutableStateFlow(0L);

    private static final Map<Controller, FlowSubscriptions.Subscription> controllerSubscriptions = new IdentityHashMap<>();

    public static Controller DEFAULT_CONTROLLER;

    private static final List<Runnable> CALLBACKS = new ArrayList<>();

    private static void bumpControllersSignal() {
        controllersSignal.setValue(controllersSignal.getValue() + 1);
    }

    private static void attachControllerSubscription(Controller controller) {
        if (controller == null || controllerSubscriptions.containsKey(controller))
            return;
        controllerSubscriptions.put(controller,
                FlowSubscriptions.subscribe(controller.revisionFlow(), revision -> bumpControllersSignal()));
    }

    private static void detachControllerSubscription(Controller controller) {
        FlowSubscriptions.Subscription subscription = controllerSubscriptions.remove(controller);
        if (subscription != null)
            subscription.cancel();
    }

    private static void setControllersInternal(List<Controller> newList) {
        for (Controller controller : controllers.getValue()) {
            if (!newList.contains(controller))
                detachControllerSubscription(controller);
        }
        newList.forEach(Controllers::attachControllerSubscription);
        controllers.setValue(newList);
        bumpControllersSignal();
    }

    /** 对齐原静态块：列表任何变化（成员增删或元素冒泡）→ 落盘 + 自检。 */
    static {
        FlowSubscriptions.subscribe(controllersSignal, signal -> onControllersChanged());
    }

    private static void onControllersChanged() {
        updateControllerStorages();
        checkControllers();
    }

    public static void checkControllers() {
        List<Controller> list = controllers.getValue();
        if (list.contains(null)) {
            List<Controller> newList = new ArrayList<>(list);
            newList.remove(null);
            setControllersInternal(newList);
        }
        if (controllers.getValue().isEmpty()) {
            try {
                if (DEFAULT_CONTROLLER == null) {
                    String str = IOUtils.readFullyAsString(Controllers.class.getResourceAsStream("/assets/controllers/00000000.json"));
                    DEFAULT_CONTROLLER = new GsonBuilder()
                            .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                            .setPrettyPrinting()
                            .create().fromJson(str, Controller.class);
                }
                DEFAULT_CONTROLLER.saveToDisk();
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to generate default controller!", e.getMessage());
            }
            setControllersInternal(getControllersFromDisk());
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
            ArrayList<String> fileNames = (ArrayList<String>) controllers.getValue().stream().map(Controller::getFileName).collect(Collectors.toList());
            for (File file : files) {
                if (((file.isDirectory() && !file.getName().equals("styles") && !file.getName().equals("input")) || !fileNames.contains(file.getName())) && !file.getName().endsWith(".bak")) {
                    file.delete();
                }
            }
        }
        for (Controller controller : controllers.getValue()) {
            controller.saveToDisk();
        }
    }

    public static void init() {
        if (initialized)
            return;

        setControllersInternal(getControllersFromDisk());
        checkControllers();

        initialized = true;
        CALLBACKS.forEach(callback -> Schedulers.androidUIThread().execute(callback));
        CALLBACKS.clear();
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
                    if (controller == null) {
                        throw new JsonParseException("Controller is null!");
                    }
                    if (!json.getName().equals(controller.getFileName())) {
                        controller.renameFile(json.getName(), controller.getFileName());
                    }
                    list.add(controller);
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Can't read file: " + json.getAbsolutePath(), e.getMessage());
                } catch (JsonParseException e) {
                    Logging.LOG.log(Level.WARNING, "File: " + json.getAbsolutePath(), e.getMessage() + " is broken!");
                    json.renameTo(new File(FCLPath.CONTROLLER_DIR, json.getName() + ".bak"));
                }
            }
        }
        return list;
    }

    /** 控制器列表快照（只读）；任何变化经 {@link #controllersSignalFlow()} 通知。 */
    public static List<Controller> getControllers() {
        List<Controller> list = controllers.getValue();
        if (list.contains(null)) {
            List<Controller> newList = new ArrayList<>(list);
            newList.remove(null);
            setControllersInternal(newList);
        }
        if (controllers.getValue().isEmpty()) {
            List<Controller> newList = new ArrayList<>(controllers.getValue());
            newList.add(DEFAULT_CONTROLLER);
            setControllersInternal(newList);
        }
        return Collections.unmodifiableList(controllers.getValue());
    }

    /** 控制器列表变化信号（成员增删与元素内部变更都会递增）。 */
    public static StateFlow<Long> controllersSignalFlow() {
        return controllersSignal;
    }

    public static void addController(Controller controller) {
        if (!initialized) return;
        List<Controller> newList = new ArrayList<>(controllers.getValue());
        newList.add(controller);
        setControllersInternal(newList);
    }

    public static void removeControllers(Controller controller) {
        if (!initialized) return;
        List<Controller> newList = new ArrayList<>(controllers.getValue());
        if (newList.remove(controller)) {
            setControllersInternal(newList);
        }
    }

    public static Controller findControllerById(String id) {
        checkControllers();
        List<Controller> list = controllers.getValue();
        return list.stream().filter(it -> it.getId().equals(id)).findFirst().orElse(list.get(0));
    }

    public static void addCallback(Runnable callback) {
        if (initialized) {
            callback.run();
            return;
        }
        CALLBACKS.add(callback);
    }

}
