# fclauncher + SDL 绑定（JVM 启动/渲染后端）

范围：`FCL/src/main/java/com/tungsten/fclauncher/`（15 文件约 3300 行，含 bridge/、keycodes/、utils/）、`org/libsdl/app/`（13 文件约 6400 行，SDL3 Java 绑定）、`org/lwjgl/glfw/`（1 文件 495 行）。

## 一、类清单与职责

**com.tungsten.fclauncher 根（4 文件）**
- `FCLauncher.java`（595 行）：JVM 启动总控。公开入口 `launchMinecraft`/`launchJarExecutor`/`launchAPIInstaller`（分别由 `fclcore/launch/DefaultLauncher.java:463`、`game/JarExecutorLauncher.java` 调用），全部汇入私有 `launchProcess(config, logName, task, render, logModList, highPriority)`。
- `FCLConfig.kt`：启动配置（context、logDir、javaPath、workingDir、renderer、args，可选 useVKDriverSystem、已装 mod loader 集合、lwjglVersion 默认 "3.3.3"）。
- `CriticalNativeTest.java`：11 行的 `@CriticalNative` native 测试桩，验证设备 CriticalNative 通道可用。
- 注意：本版 FCL 已把 Pojav 的多文件启动器精简为单类 FCLauncher。

**utils（2 文件）**
- `FCLPath.java`：全局静态路径体系。`RUNTIME_DIR = context.getDir("runtime")`，其下 `java/jre8|17|21|25`、`jna`、`lwjgl`、`caciocavallo(17)`（AWT 字体桥）；`MOD_RUNTIME_DIR = dir("runtime_mod")`（native mod）；`SHARED_COMMON_DIR = /sdcard/FCL/.minecraft`；插件目录下的 authlib-injector/MioLibPatcher/MioLaunchWrapper 三个 jar 路径；`loadPaths` 统一 mkdirs。
- `Architecture.java`：按 `Build.SUPPORTED_*_ABIS` 判 arm/arm64/x86/x86_64；`archAsStringAndroid` 产出 `arm64-v8a` 等，供拼 LWJGL natives 路径。

**bridge（2 文件）**
- `FCLBridge.java`（426 行）：JNI 总桥，静态块 `System.loadLibrary("fcl")` + `"pojavexec_awt"`。native 侧能力：`redirectStdio/chdir/setenv/dlopen/setLdLibraryPath/setupExitTrap/initializeHooks/getJavaVMPointer/jObjectToString/renderAWTScreenFrame/nativeSendData/nativeMoveWindow/nativeClipboardReceived`。保留了 Pojav 的 X11 风格事件类型常量（KeyPress=2…ConfigureNotify=22、FCLMessage=37，供 AWT 事件管道），鼠标按钮常量直接引用 GLFW 鼠标码。`execute(surface, callback)` 是启动触发点。
- `FCLBridgeCallback.java`：`onCursorModeChange/onLog/onExit` 三回调。

**keycodes（8 文件）——多套键码体系**
- `FCLKeycodes.java`：**Linux evdev 键码**（KEY_ESC=1、KEY_A=30…），作为触屏控件层的"中间键码"。
- `LwjglGlfwKeycode.java`：GLFW 3.4 键码 + `GLFW_MOD_*` + `GLFW_MOUSE_BUTTON_*`，游戏侧通用语言。
- `EfficientAndroidLWJGLKeycode.java`：Android→GLFW，112 项并行数组 + 二分查找（Pojav 原版移植），`execKey` 直接把 KeyEvent 转成 `CallbackBridge.sendKeyPress`；`getSdlAndroidKeycode` 处理 GLFW→Android 的差异键（KP_ENTER、SUPER 等）。
- `AndroidKeycodeMap.java`（Android→FCL）、`LwjglKeycodeMap.java`（FCL→GLFW，HashMap，`CallbackBridge.sendKeycode` 里用它）：存在「控制层发 FCL 键码 → 转 GLFW → native 管道」的链路。
- `GamepadKeycodeMap.java`：手柄→FCL 键码，含伪鼠标码 1000-1004 与摇杆伪键码 2000+。
- `MinecraftKeyBindingMapper.java`：MC options.txt 的 `key_key.*` 绑定名→FCL 键码（新版字符串键名；旧版直接 parse LWJGL2 数字）。
- `AWTInputEvent.java`：Oracle JDK `java.awt.event` 常量原样拷贝（913 行），供 pojavexec_awt 的 AWT 输入对照。

**org.lwjgl.glfw（1 文件）**
- `CallbackBridge.java`（495 行）：Pojav 的 GLFW 输入桥 Java 端（对应 libpojavexec）。静态加载 `pojavexec`，创建 direct gamepad 共享 `ByteBuffer/FloatBuffer`（小端）；一组 `@CriticalNative` 的 `nativeSendKey/Char/CharMods/CursorPos/MouseButton/Scroll/ScreenSize/ResetInputState` 把事件写入 native 输入队列；另有 JVM→Java 反向回调入口（见下）。

## 二、JVM 启动链路（逐步）

1. `DefaultLauncher` 组好完整 java 命令行数组塞进 `FCLConfig.args` → `FCLauncher.launchMinecraft` → `launchProcess`：`new FCLBridge()`（此时触发加载 libfcl/pojavexec_awt）、`setLogPath(LATEST_GAME_LOG)`，创建 `MAX_PRIORITY` 启动线程但**不 start**。
2. UI 侧 `JVMActivity.onSurfaceTextureAvailable`：`SurfaceTexture.setDefaultBufferSize`（可被 FORCE_RESOLUTION 强制 1920x1080）、写 `CallbackBridge.windowWidth/Height`、`SdlBridge.prepareSurface`，然后 `fclBridge.execute(surface, menu.getCallbackBridge())`。
3. `execute()`：`redirectStdio(日志路径)`（重定向 stdout/stderr）→ `handleWindow()`：**有 gameDir（游戏进程）走 `CallbackBridge.setupBridgeWindow(surface)` 把 ANativeWindow 交给 native egl_bridge；无 gameDir（jar 执行器等）则起 "AndroidAWTRenderer" 线程，lockCanvas + `renderAWTScreenFrame()` 软绘制 1280x720 位图** → `thread.start()`。
4. 启动线程内依次：`logStartInfo` → `setEnv`（`addCommonEnv`/`addModLoaderEnv`/`addRendererEnv`/`addCustomEnv`，逐条 `bridge.setenv` 写入进程 environ）→ `setUpJavaRuntime`：读 JRE 的 `release` 文件取 `OS_ARCH` 定位 `lib/<arch>`（JDK8 多一层 `/jre`；x86 归一为 i386/i486/i586），**dlopen libjli→libjvm→libfreetype/verify/java/net/nio/awt/awt_headless/fontmanager，再递归 `locateLibs` 把 JRE 目录里所有 .so 全部 dlopen** → `setupGraphicAndSoundEngine`：dlopen libopenal；插件渲染器按 pojavEnv 的 `DLOPEN` 逐个加载，再 dlopen GL 库，Android <R 时把句柄写入 `RENDERER_HANDLE` env（供 egl_bridge dlsym 复用句柄）→ `bridge.chdir(workingDir)`。
5. `launch()`：拼 `LD_LIBRARY_PATH`（JRE lib、jli、jvm 的 server/client 目录、jna、插件渲染器路径、NativeLibPlugin.getPaths、MOD_RUNTIME_DIR、/system|vendor lib64、`LWJGL_DIR/<版本>/natives/<abi>`、APK nativeLibraryDir）；`rebaseArgs` 把 args 里的 `${natives_directory}` 强制替换为该路径、`${gl_lib_name}/${egl_lib_name}` 替换为渲染器库；`bridge.setLdLibraryPath`、`setupExitTrap`、`FCLBridge.initializeHooks()`；最后 **`com.oracle.dalvik.VMLauncher.launchJVM(args)`**——同仓库的纯 native 声明类（`FCL/src/main/java/com/oracle/dalvik/VMLauncher.java`），实现在 FCL 原生层，在**当前 Android 进程内**拉起 OpenJDK（args[0] 的 `bin/java` 只是占位 argv[0]）；退出码经 `bridge.onExit` 回调 UI。
6. env 亮点：`DALVIK_JAVAVM`/`DALVIK_APPLICATION` 把 ART 的 JavaVM 指针与 Application 全局引用（十六进制，经 `jObjectToString`）传给游戏 JVM 侧原生代码（libflite TTS 桥 attach 回 ART 用）；`ALSOFT_DRIVERS=opensl`；`MOD_ANDROID_RUNTIME`；仅 lwjgl3ify 实例注入 `XDG_DATA_HOME` 兜底；用户自定义 env 从 "launcher" SharedPreferences 的 `env` 键（每行 KEY=VALUE）覆盖。

## 三、渲染/输入/音频三条桥

**渲染**：Java 侧没有 RendererKind 枚举；`com.mio.data.Renderer` 是数据类，内置 6 个 ID（GL4ES、VGPU、VIRGL、ZINK、FREEDRENO、NGGL4ES）+ 插件渲染器（`path` 非空）。`addRendererEnvInner` 按 ID 设置 `POJAV_RENDERER`（opengles2 / opengles3_desktopgl_zink_kopper / gallium_virgl / gallium_freedrozen 等）、`POJAVEXEC_EGL`、gl4es 的 `LIBGL_*` 调优、MESA 的 4.6/460 版本覆写与 `MESA_GLSL_CACHE_DIR`；插件渲染器透传其 pojavEnv（LIB_MESA_NAME/MESA_LIBRARY 自动加插件路径前缀）。`addRendererEnv` 再为 SDL 路径算出 `SDL_EGL_LIBRARY`/`SDL_OPENGL_LIBRARY` 绝对路径（仅当库真实存在于渲染器目录）。Surface 生命周期由 JVMActivity/TextureView 驱动（available/sizeChanged/destroyed → setupBridgeWindow / `FCLBridge.setSurfaceDestroyed`），Java 侧不参与游戏帧循环，FPS 经 `FCLBridge.getFps()` 轮询 native。

**输入**：上行 Android→游戏 为 `FCLBridge.pushEvent*` → `CallbackBridge.sendXxx` → `@CriticalNative` native 写输入管道（native 侧 stack queue，`nativeSetUseInputStackQueue` 开关）——queueEvent 的真正位置在 native。下行游戏→Android 有四类入口：`notifyLauncher/nativeNotifyLauncher`（JRE 侧 **sdl_hook.c** 调用，ACTION_INIT_LAUNCHER_INTEGRATION 时 `System.loadLibrary("SDL3")/("SDL2")` + `SdlBridge.setupJNI` + 绑定 SDLSurface）；`onGrabStateChanged`（鼠标捕获状态，Choreographer 延迟 16ms 防抖后回调 `setCursorMode`）；`accessAndroidClipboard`；`onDirectInputEnable`（GLFW direct gamepad 模式）。`sendKeycode` 里有个精细补丁：keydown 无字符时用 KeyCharacterMap 按键位+修饰键反查补发 char/charMods（对齐 LWJGLXX 的 Display.keyCallback 语义）。

**音频**：Java 侧不建桥——只 dlopen libopenal + `ALSOFT_DRIVERS=opensl`；`SDLAudioManager` 仅服务 SDL 路径的设备热插拔上报。

## 四、SDL 绑定的结构与定制点

13 个文件全部是 **SDL3**（版本 3.4.12）android-project 模板（文件头标注 "SDL3 android-project java code"，无 SDL2 绑定；运行时按需 loadLibrary SDL3 或 SDL2，共享同一套 Java）。**最大改造：SDLActivity 不再是 Activity**——`onCreate` 直接抛 IllegalStateException，`main()/SDLMain` 全部注释，改由 `SdlBridge.externalInitialize`（fcl/game/sdl，移植自 ZalithLauncher2 feat/sdl3）注入：mSingleton=启动器 Activity、mLayout=TextureView 父容器、`SDLSurface.setNativeSurface` 注入外部 Surface（校验 isValid 后手动 surfaceCreated）。SDL 被当库嵌入，为 MC 26.3+ 的 SDL 渲染后端服务。

定制 diff 特征：① 几乎所有 JNI 触点都加 `SdlBridge.getSdlEnabled()` 守卫；② 中文注释遍布；③ 新增 `SdlImeController`：GAME/LAUNCHER/BACK 三来源统一 IME 管理，支持"游戏通道关闭时启动器代开 native 通道"（mForcedByLauncher）、300ms 延迟压制顽固 IME、`HEIGHT_PADDING=15`；④ `messageboxShowMessageBox` 静态桥（host 非 SDLActivity 时用 MaterialAlertDialog + CountDownLatch 阻塞）；⑤ `SDLSurface.onTouch` 整段注释——触摸改由 control 包的自定义 View 处理；⑥ COMMAND_CHANGE_WINDOW_STYLE 全屏逻辑注释掉；⑦ `onResolvePointerIcon` NPE 兜底（SDL issue #13306）。HID 四件套（USB/BLE Steam 手柄）基本是未改动的上游代码。

## 五、值得注意的细节与坑

- `rebaseArgs` 里有一行孤立的 `config.getRenderer();` 死代码。
- `launch()` 靠扫描 `mio.Wrapper`（MioLaunchWrapper 主类）区分 Java 参数与程序参数打印日志，mainClass 计数允许两层主类。
- `AndroidKeycodeMap`/`EfficientAndroidLWJGLKeycode` 依赖数组**有序**才能 binarySearch，注释强调添加映射必须保序。
- `LwjglKeycodeMap.add(键位重复)`（KEY_3 加了两次）无害但易踩；`add(lwjgl, fcl)` 参数顺序与 put 方向相反，读代码时需小心。
- `onGrabStateChanged` 的延迟回调不可取消，靠 `isGrabbing != grabbing` 判过期 + activity 销毁保护。
- SDL 鼠标 Button4/5 在 SDL 侧**故意**与 back/forward 翻转（源码注释 "don't ask"）。
- `SDLDummyEdit` 布局参数注释指出：FCL 游戏布局根是 RelativeLayout，硬编码 FrameLayout.LayoutParams 会强转崩溃，故复用 `getLayoutParams()`。
- `mSingleton`/`getContext()` 在嵌入模式下是启动器 Activity，SDL 的 JNI 会按运行时类解析方法，这就是 messagebox 静态桥存在的理由。
- `HIDDeviceManager` 用 SharedPreferences 持久化 next_device_id，Chromebook 有专门的蓝牙轮询兜底（当前被注释停用）。

关键文件路径（均在 `/Users/mio/Documents/GitHub/FoldCraftLauncher/FCL/src/main/java/` 下）：`com/tungsten/fclauncher/FCLauncher.java`、`com/tungsten/fclauncher/bridge/FCLBridge.java`、`org/lwjgl/glfw/CallbackBridge.java`、`org/libsdl/app/SDLActivity.java`、`org/libsdl/app/SdlImeController.java`、`org/libsdl/app/SDLSurface.java`、`com/oracle/dalvik/VMLauncher.java`。
