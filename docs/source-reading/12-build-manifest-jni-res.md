# 构建脚本、Manifest、JNI、资源与 assets 概览

范围：FCL 模块的非 Java 源码部分——`FCL/build.gradle.kts`、`src/main/AndroidManifest.xml`、`src/main/jni/`、`src/main/res/`、`src/main/assets/`、根目录构建配置与 `FCL/libs/`。

## 一、构建脚本要点（FCL/build.gradle.kts）

**变体矩阵**：`applicationId com.tungsten.fcl`，versionCode 1335 / versionName 1.3.3.5，compileSdk 35 / minSdk 26 / targetSdk 34，Java 17 + core library desugaring，NDK 27.0.12077973。共 3 个 buildType：`release`/`debug` 均用正式证书 FCLKey（key-store.jks，密码取 `FCL_KEYSTORE_PASSWORD` 环境变量或 local.properties）签名且不混淆；`fordebug`（testBuildType）继承 debug，id 加 `.debug` 后缀，用 debug-key.jks，并生成 resValue `file_browser_provider`。每个变体均注入 resValue：`app_version`/`curse_api_key`/`oauth_api_key`。

**arch 处理**：通过 `-Darch`（或 local.properties 的 `arch=arm64`，命令行优先）驱动两条链路：① `splits.abi` 仅在非 all 时启用并 include 对应 ABI；② `filterJreAssets`（自定义 Sync 任务）从 `src/main/jreAssets` 过滤 JRE 包，非 all 构建只保留 `version`、`universal.tar.xz` 和当前架构的 `bin-<arch>.tar.xz`，显式声明 arch 输入防止切换架构后任务不重跑。产物重命名为 `FCL-{buildType}-{version}-{abi}.apk`。

**mergeAssets doLast**：LWJGL/JNA natives 以 assets 形式打包在 aar 中（不走 abiFilters），构建后在 mergeAssets 尾部按 arch 递归删除 `app_runtime/lwjgl/<版本>/natives/<其他abi>` 与 `app_runtime/jna/<版本>/natives/<其他abi>`；LWJGL 版本列表从 `libs/lwjgl-*-natives-release.aar` 文件名正则推导。

**其他**：externalNativeBuild 指向 `src/main/jni/CMakeLists.txt`（STL=c++_shared，prefab 引 bytehook）；手动注册 `checkstyle` 任务（toolVersion 10.12.5，maxErrors/maxWarnings=0，只查 `src/main/java` 的 Java）；`updateMap` 任务将版本号/日期回写根目录 `version_map.json`；ksp 导出 Room schema 到 `schemas`；packaging 开启 legacy jniLibs 压缩并对 `libbytehook.so` pickFirst。

## 二、Manifest 组件清单

**权限（17 项）**：存储三件套（READ/WRITE/MANAGE_EXTERNAL_STORAGE）、INTERNET、网络状态两项、WAKE_LOCK、VIBRATE、REQUEST_INSTALL/DELETE_PACKAGES、FOREGROUND_SERVICE（含 dataSync、connectedDevice 子类型）、POST_NOTIFICATIONS、HIGH_SAMPLING_RATE_SENSORS（输入采样）、RECORD_AUDIO。

**Activity（9 个）**：SplashActivity（LAUNCHER 入口，Theme.Splash，横屏）；MainActivity、WebActivity、ControllerActivity（虚拟手柄编辑）、ShellActivity（竖屏终端）、JVMActivity（游戏运行界面，alwaysRetainTaskState）；**JVMCrashActivity 运行于独立进程 `:crash`**；另有 fcllibrary 的 FileBrowserActivity、CrashReportActivity。activity-alias `ImportActivity` 承接 zip/7z/mrpack 的 VIEW 打开实现整合包导入。

**Service（3 个）**：`ProcessService`（下载进程，**独立进程 `:jvm`**，dataSync 前台）、`DownloadService`（com.mio.download，dataSync）、`TerracottaVPNService`（BIND_VPN_SERVICE，connectedDevice|dataSync，声明 `android.net.VpnService` action）。

**Provider（3 个）**：`FolderProvider`（自定义 DocumentsProvider，authority `${applicationId}.document.provider`，需 MANAGE_DOCUMENTS）、FileProvider（`${applicationId}.provider`）、CrashReporterInitProvider。**queries**：`net.kdt.pojavlaunch.ffmpeg` 包、MAIN intent、TTS_SERVICE。Application 特性：largeHeap、isGame、allowNativeHeapPointerTagging=false、requestLegacyExternalStorage、cleartext 放行。

## 三、JNI 库清单（12 个 so 目标，src/main/jni/）

| 库 | 职责 |
|---|---|
| `pojavexec` | 核心库：egl_bridge.c（EGL/GL 桥）、input_bridge_v3.c（V3 输入桥，含 glfwSetCursorPos 抓鼠标）、jre_launcher.c（Oracle 源码的 JVM 启动器）、environ（pojav_environ 全局结构）、ctxbridges（gl_bridge/osm_bridge/egl_loader/osmesa_loader 等 GL 上下文桥）、virgl.c（virgl 渲染服务）、jvm_hooks（ProcessImpl.forkAndExec 劫持、EMUI dl_iterate_phdr 卡死规避、LWJGL dlopen 钩子）、native_hooks（exit/chmod/SDL/SDL_dlopen 钩子，ByteHook 实现）；仅 arm64 链接 EGL/GLESv2 |
| `pojavexec_awt` | awt_bridge.c：Dalvik 与 OpenJDK 双 VM 间的 AWT 事件桥 |
| `fcl` | fcl_loader.c：基于 ByteHook 的加载器（exit trap 等） |
| `linkerhook` | 拦截 libvulkan 加载器的 android_dlopen_ext，懒创建 Turnip 驱动命名空间（`-z global`） |
| `androidnsbypass` | Android linker namespace 绕过工具（vendor 自 Amethyst，MIT） |
| `glxshim` | 为 LWJGL<3.3.5 提供 `glXGetProcAddress` 符号 |
| `jsound` | OpenJDK libjsound 核心（vendor jdk17u）+ jsound_openal.c 的 OpenAL 后端 + jdk8_compat.c，服务 jre8/17/21/25 四代 |
| `flite` / `fliteWrapper` / `flite_cmu_us_kal16` | 游戏 TTS：flite 接口桥接安卓系统 TTS（经 DALVIK_JAVAVM 环境变量 attach 回 Dalvik 调 com.mio.flite.FliteTts）；Wrapper 转发旧版接口；kal16 提供占位 voice |
| `awt_headless` / `awt_xawt` | 占位空库 / java.awt.* initIDs 假实现 |

## 四、资源体系概览（res/）

**布局 187 个**：item_ 58、dialog_ 58、page_ 27、view_ 23、ui_ 8、activity_ 8、menu_ 2、fragment_ 2、popup_ 1——大量 UI 走 item+dialog 组合而非传统 activity 布局。**values**：strings.xml 1445 条、colors.xml 9 色、themes.xml、attrs.xml、fcl_styles.xml；drawable 113 个（ic_ 68、bg_ 18、img_ 16）；anim 6 个；xml 6 个。**多语言 11 种**：de/fa/ja/pt-rBR/ru/tr/uk/vi/zh/zh-rHK/zh-rTW，另有 values-night（深色主题）与 values-v31（Android 12+）。

**主题 attrs 体系**（原 FCLLibrary 控件族并入）：顶层 attr `use_theme_color`、`auto_tint`、`text_use_theme_color` + 一整套 `auto_*_tint` 开关（FCLTextView 的 auto_text_tint/auto_text_background_tint、FCLImageView 的 auto_src_tint、FCLEditText/FCLCheckBox/FCLTabLayout/FCLPreciseSeekBar 等各自 tint 属性），配合 FCLButton(ripple/shape)、FCLTitleView(title)、FCLNumberSeekBar(suffix/min/max)、KeycodeView(keycode)、LogWindow、DraggableTextView、ColorPickerView(cpv_*)。主题 `Theme.FoldCraftLauncher` 基于 MaterialComponents DayNight NoActionBar，全屏+透明导航，colorPrimary 全指向 `default_theme_color`（支持运行时换色）；Theme.Splash 用 core-splashscreen。

## 五、assets 运行时组件

- `app_runtime/lwjgl/{3.3.3,3.4.1}/`：各 12-13 个 jar（lwjgl.jar、merged-modules、lwjglx、vulkan、openal、nanovg、stb、tinyfd、vma、spvc、shaderc、freetype；3.4.1 多 spng）+ version 文件；natives 目录由对应 aar 注入并按架构裁剪。
- `app_runtime/jna/{5.13.0~5.18.0}` 六个版本：`natives/<四abi>/libjnidispatch.so` + version。
- `app_runtime/caciocavallo`（1.10，AWT 桥，含 ResConfHack.jar）与 `caciocavallo17`（1.19.1，含 cacio-agent/tta）。
- `src/main/jreAssets/app_runtime/java/`（构建期拷入）：jre8/17/21/25 四套 JRE，各含 `universal.tar.xz` + `bin-{arm,arm64,x86,x86_64}.tar.xz`（jre25 无 x86）+ version。
- `game/`：MioLaunchWrapper.jar、MioLibPatcher.jar、HMCLTransformerDiscoveryService-1.0.jar、4 个 log4j2 配置、versions.txt/version-alias.csv/unlisted-versions.json。
- 其他：controllers/00000000.json（默认手柄布局）、img/（steve/alex 皮肤+skin_model）、microsoft_auth.html、mod_data.txt、modpack_data.txt、options.txt、eula.txt。

## 六、根目录与 libs 依赖

**settings.gradle.kts**：模块 `:FCL`、`:Terracotta`、`:ZipFileSystem`、`:LWJGL` 及 `:LWJGL:lwjgl-3.3.3/3.4.1`（目录名为纯数字，故 projectDir 重定向）；仓库 google/mavenCentral/jitpack。**根 build.gradle.kts**：AGP 8.13.2 + Kotlin 2.4.10，buildscript 强制覆盖 R8 为 9.4.17（捆绑 D8/R8 不支持 Kotlin 2.4）。**gradle.properties**：-Xmx2048m、useAndroidX、nonTransitiveRClass。**checkstyle.xml**：以 HMCL 上游为骨架，"规则适配现状"裁剪（不查行长/javadoc/缩进），排除 org/lwjgl、org/libsdl、fakefx 第三方移植，单文件上限 2000 行，K&R 花括号。

**FCL/libs/（7 个 aar，fileTree 引入）**：`lwjgl-3.3.3-natives-release.aar`、`lwjgl-3.4.1-natives-release.aar`（LWJGL native）、`NG-GL4ES-release.aar`（GL4ES 转译层）、`kopper-zink-release.aar`（Zink Vulkan 上跑 GL）、`spirv-cross-natives.aar`（shader 转译）、`openal-soft-release.aar`（音频，jsound 后端依赖）、`SDL-release.aar`（SDL3 游戏手柄/窗口支持）。Maven 侧另有 bytehook、jelf、nanohttpd、xz/lz4/commons-compress、gson、tomlj、jsoup、Room、DataStore、Glide、touchcontroller、gamepad-remapper 等，项目依赖 Terracotta（VPN 联机）与 ZipFileSystem。
