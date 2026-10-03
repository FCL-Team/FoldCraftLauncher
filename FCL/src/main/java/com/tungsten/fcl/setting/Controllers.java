package com.tungsten.fcl.setting;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;
import static com.tungsten.fclcore.fakefx.collections.FXCollections.observableArrayList;

import com.google.gson.JsonParseException;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.util.Constants;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyListWrapper;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Controllers {

    private Controllers() {
    }

    private static final ObservableList<Controller> controllers = observableArrayList(controller -> new Observable[]{controller});
    private static final ReadOnlyListWrapper<Controller> controllersWrapper = new ReadOnlyListWrapper<>(controllers);
    public static Controller DEFAULT_CONTROLLER;

    /**
     * 列表结构操作与初始化互斥：init 在后台线程执行（启动预初始化），
     * 主线程读路径与 init 并发时通过该锁串行化，避免双重扫描与列表结构竞争。
     */
    private static final Object LOCK = new Object();

    private static final List<Runnable> CALLBACKS = new CopyOnWriteArrayList<>();

    public static void checkControllers() {
        synchronized (LOCK) {
            if (controllers.contains(null)) {
                controllers.remove(null);
            }
            if (controllers.isEmpty()) {
                try {
                    if (DEFAULT_CONTROLLER == null) {
                        String str = IOUtils.readFullyAsString(Controllers.class.getResourceAsStream("/assets/controllers/00000000.json"));
                        // assets 默认控制器保持完整解析（列表为空场景罕见，一次可接受）
                        DEFAULT_CONTROLLER = Controller.GSON.fromJson(str, Controller.class);
                    }
                    // 必须同步写盘：异步保存未落盘前磁盘上没有布局文件，紧随的扫描会拿到空结果，
                    // 后续兜底逻辑会反复触发保存与清理
                    DEFAULT_CONTROLLER.saveToDiskSync();
                } catch (IOException e) {
                    Logging.LOG.log(Level.SEVERE, "Failed to generate default controller!", e.getMessage());
                }
                addControllersFromDisk();
            }
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
            List<String> fileNames = controllers.stream().map(Controller::getFileName).collect(Collectors.toList());
            for (File file : files) {
                // 仅对不在内存列表的布局 json 做孤儿清理：列表加载不完整时（初始化竞态/解析失败），
                // 直接删除会永久丢失用户布局，故改名为 .bak 保留数据；
                // 其余文件（.tmp 在途临时文件、styles/input 等目录）一律不动
                if (file.isFile() && file.getName().endsWith(".json") && !fileNames.contains(file.getName())) {
                    file.renameTo(new File(file.getParentFile(), file.getName() + ".bak"));
                }
            }
        }
        for (Controller controller : controllers) {
            controller.saveToDisk();
        }
    }

    static {
        controllers.addListener(onInvalidating(Controllers::updateControllerStorages));
        controllers.addListener(onInvalidating(Controllers::checkControllers));
    }

    public static void init() {
        synchronized (LOCK) {
            if (initialized)
                return;

            addControllersFromDisk();
            checkControllers();

            initialized = true;
        }
        CALLBACKS.forEach(callback -> Schedulers.androidUIThread().execute(callback));
        CALLBACKS.clear();
    }

    /**
     * 扫描磁盘布局加入列表，按 id 去重：init 与 checkControllers 可能先后触发扫描，
     * 重复加入会让同一布局文件出现两个实例并发写盘（内容互相覆盖或 .tmp 竞争失败）。
     */
    private static void addControllersFromDisk() {
        for (Controller controller : getControllersFromDisk()) {
            boolean exist = controllers.stream().anyMatch(it -> it.getId().equals(controller.getId()));
            if (!exist) {
                controllers.add(controller);
            }
        }
    }

    private static ArrayList<Controller> getControllersFromDisk() {
        ArrayList<Controller> list = new ArrayList<>();
        List<File> jsons = FileUtils.listFilesByExtension(new File(FCLPath.CONTROLLER_DIR), "json");
        for (File json : jsons) {
            if (json.isFile()) {
                try {
                    // 轻量解析：只读元数据与布局信息，按键数据在使用时按需加载
                    Controller controller = Controller.parseLightweight(json);
                    if (controller == null) {
                        throw new JsonParseException("Controller is null!");
                    }
                    if (controller.getControllerVersion() < Constants.MIN_CONTROLLER_VERSION || controller.getControllerVersion() > Constants.CONTROLLER_VERSION) {
                        // 版本不兼容：跳过该文件（完整加载路径才弹不兼容提示）
                        Logging.LOG.log(Level.WARNING, "File: " + json.getAbsolutePath() + " is incompatible! (controllerVersion " + controller.getControllerVersion() + ")");
                        continue;
                    }
                    if (!json.getName().equals(controller.getFileName())) {
                        controller.renameFile(json.getName(), controller.getFileName());
                        // 文件被重命名，源文件引用同步更新（否则按需加载会读取已删除的旧文件）
                        controller.setFile(new File(FCLPath.CONTROLLER_DIR, controller.getFileName()));
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

    public static ObservableList<Controller> getControllers() {
        synchronized (LOCK) {
            if (controllers.contains(null)) {
                controllers.remove(null);
            }
            if (controllers.isEmpty()) {
                // 初始化未完成时先补扫磁盘（内部含默认控制器同步落盘），
                // 仅当磁盘确实无布局时才回退到 assets 默认控制器，避免 assets 数据
                // 进入列表后覆盖磁盘上用户编辑过的同名布局
                checkControllers();
                if (controllers.isEmpty()) controllers.add(DEFAULT_CONTROLLER);
            }
        }
        return controllers;
    }

    public static ReadOnlyListProperty<Controller> controllersProperty() {
        return controllersWrapper.getReadOnlyProperty();
    }

    public static void addController(Controller controller) {
        if (!initialized) return;
        synchronized (LOCK) {
            controllers.add(controller);
        }
    }

    public static void removeControllers(Controller controller) {
        if (!initialized) return;
        synchronized (LOCK) {
            controllers.remove(controller);
        }
    }

    public static Controller findControllerById(String id) {
        synchronized (LOCK) {
            checkControllers();
            return controllers.stream().filter(it -> it.getId().equals(id)).findFirst().orElseGet(() -> {
                Logging.LOG.log(Level.WARNING, "Controller " + id + " not found, fallback to the first one");
                return controllers.get(0);
            });
        }
    }

    public static void addCallback(Runnable callback) {
        if (initialized) {
            callback.run();
            return;
        }
        CALLBACKS.add(callback);
    }

    /**
     * 布局按键数据加载回调（均在主线程回调）。
     */
    public interface ViewGroupLoadCallback {
        void onLoaded(ControlViewGroup viewGroup);

        void onFailed(Throwable e);
    }

    /**
     * 异步申请加载控制器某个布局的完整按键数据：后台线程只解析不触碰模型，
     * 模型填充（setViewData）与监听触发统一回主线程（fakefx 列表监听非线程安全）。
     * 同布局重复申请幂等（已加载直接回调）。
     */
    public static void loadViewGroup(Controller controller, ControlViewGroup viewGroup, ViewGroupLoadCallback callback) {
        if (viewGroup.isDataLoaded()) {
            Schedulers.androidUIThread().execute(() -> callback.onLoaded(viewGroup));
            return;
        }
        Schedulers.io().execute(() -> {
            try {
                ControlViewGroup.ViewData data = controller.loadViewGroupData(viewGroup);
                if (data == null) {
                    throw new IOException("View group not found: " + viewGroup.getId());
                }
                Schedulers.androidUIThread().execute(() -> {
                    viewGroup.setViewData(data);
                    viewGroup.setDataLoaded(true);
                    callback.onLoaded(viewGroup);
                });
            } catch (Exception e) {
                Schedulers.androidUIThread().execute(() -> callback.onFailed(e));
            }
        });
    }

}