# Fold Craft Launcher 项目指南

## 项目概览

Fold Craft Launcher（FCL）是运行在 Android 上的 Minecraft: Java Edition 启动器。项目基于 HMCL 的核心功能，并使用 PojavLauncher 风格的后端在 Android 环境中启动 Java 虚拟机、LWJGL 和 Minecraft。

主要能力包括 Minecraft 全版本管理、Java 运行时管理、账户认证、游戏资源和模组下载、整合包管理、虚拟鼠标与按键映射、渲染器选择以及联机功能。仓库采用 GPL-3.0 许可证。

项目文档以中文为主，根目录同时维护 `README_EN.md` 和 `README_RU.md`。涉及 Compose/Miuix 迁移时，应优先阅读 `docs/migration/` 下的相关文档，尤其是 `final-report.md`、`regression-report.md`、`bridge-api.md`、`foundation-deps.md` 和 `activity-migration.md`。

## 工程结构

这是一个使用 Kotlin DSL 的 Gradle 多模块 Android 工程。模块由 `settings.gradle.kts` 声明，当前参与构建的模块如下：

- `FCL`：最终 Android application。包含 Activity、页面和导航、主要 UI、账户/版本/设置/下载/模组/存档功能、启动编排、Java 运行时和渲染器选择。
- `FCLCore`：Android library。提供 Minecraft 版本模型与解析、认证、下载、模组和整合包管理、启动器核心、任务系统、事件和通用 IO/版本工具。
- `FCLauncher`：Android library。提供嵌入式 Java 虚拟机启动、LWJGL/GLFW 桥接、Android Surface 处理、渲染器环境、JNI 和 NDK 原生库。
- `FCLLibrary`：Android library。提供共享 Android 组件、旧 View UI、自定义控件、文件浏览器、崩溃报告、通用 Dialog、主题和工具类。
- `LWJGL-Pojav`：纯 Java 模块。整理 LWJGL 及相关运行时 JAR，并通过 `buildLwjgl` 输出到 `FCL/src/main/assets/app_runtime/lwjgl`。
- `Terracotta`：Android library。提供联机/VPN 相关能力及 `libterracotta.so`。
- `ZipFileSystem`：Android library。提供 ZIP `FileSystemProvider`，服务注册文件位于 `ZipFileSystem/src/main/resources/META-INF/services/java.nio.file.spi.FileSystemProvider`。

`NG-GL4ES` 目录是已检出的 Git submodule，但 `settings.gradle.kts` 中的 `include` 仍被注释，因此当前不参与 Gradle 构建。

主要依赖方向为：

```text
FCL
├── FCLCore
├── FCLLibrary
├── FCLauncher
└── Terracotta

FCLCore
├── FCLauncher
└── ZipFileSystem

FCLLibrary
├── FCLauncher
└── FCLCore

FCLauncher
└── ByteHook
```

源码常见布局为 `src/main/java`、`src/main/res`、`src/main/assets`、`src/main/jni`、`src/main/jniLibs`；`ZipFileSystem` 还使用 `src/main/resources`。

## 技术栈与配置

关键配置文件：

- `settings.gradle.kts`：插件仓库、依赖仓库、根工程名和模块列表；依赖解析使用 `RepositoriesMode.FAIL_ON_PROJECT_REPOS`。
- `build.gradle.kts`：根级 Android application/library 和 Kotlin 插件声明。
- `gradle/libs.versions.toml`：Version Catalog，集中管理版本、依赖坐标和插件。
- 各模块的 `build.gradle.kts`：模块类型、Android 配置、依赖、构建变体、资源处理和任务。
- `FCL/proguard-rules.pro`：R8/ProGuard 规则。
- `gradle/wrapper/gradle-wrapper.properties`、`gradlew`、`gradlew.bat`：Gradle Wrapper。
- `FCL/src/main/AndroidManifest.xml`：应用组件、进程、权限、Provider、Service 和 Activity 声明。
- `FCLauncher/src/main/jni/CMakeLists.txt`：FCLauncher 原生库构建配置。
- `.github/workflows/build.yml`、`.github/workflows/release.yml`：CI 构建和 Tag 发布流程。

当前构建栈和 Android 参数为：

- Gradle `8.13`
- Android Gradle Plugin `8.13.2`
- Kotlin `2.3.20`
- Java/Kotlin JVM target `17`
- `compileSdk 35`
- `minSdk 26`
- `targetSdk 34`
- FCLauncher NDK `27.0.12077973`
- `LWJGL-Pojav` 使用 Java 8 toolchain 构建其运行时 JAR
- Compose 使用 `org.jetbrains.kotlin.plugin.compose` 插件，不配置旧式 `kotlinCompilerExtensionVersion`
- Miuix `0.9.3`
- Compose BOM `2026.06.01`
- `activity-compose 1.13.0`、`lifecycle-compose 2.10.0`

项目同时使用 Kotlin 和 Java。AndroidX、AppCompat、Material、ConstraintLayout、SplashScreen、Lifecycle、Compose、Miuix、Glide、Coroutines、DataStore、Gson、OpenNBT、Apache Commons、NanoHTTPD、JSoup、Tomlj、Junrar、ByteHook 等依赖均应通过 `gradle/libs.versions.toml` 管理。新增依赖时不要在模块构建脚本中硬编码版本或坐标。

## 运行时架构

应用入口和关键组件在 `FCL/src/main/AndroidManifest.xml` 中声明：

- `.FCLApplication`
- `.activity.SplashActivity`
- `.activity.MainActivity`
- `.activity.JVMActivity`
- `.activity.JVMCrashActivity`
- Web、Shell、Controller、文件浏览和导入相关 Activity/alias
- `ProcessService`，运行在 `:jvm` 进程
- `TerracottaVPNService`

启动流程大致如下：

1. `SplashActivity` 检查用户协议、权限、FCL 路径和运行时资源。
2. 初始化 Java、LWJGL、Caciocavallo、JNA 和渲染器资源；缺少或版本不匹配时进入相应引导页面。
3. 初始化 `RendererManager`、`JavaManager` 和 `ConfigHolder`，然后进入 `MainActivity`。
4. `UIManager` 管理顶层 UI，`PageManager` 管理页面和临时页面栈。
5. 用户启动版本时，`Versions.launch()` 创建 `LauncherHelper`，执行 Java 选择、依赖/资源/库检查、登录、模组和渲染器检查。
6. `FCLBridge` 连接 Android 层和游戏启动层，`JVMActivity` 创建 `TextureView` Surface 并调用 `FCLBridge.execute()`。
7. `FCLauncher` 构造 JVM、JRE、JLI 和 native library 路径，设置 `JAVA_HOME`、`HOME`、`LD_LIBRARY_PATH`、`POJAV_RENDERER` 等环境，预加载相关 `.so`，再调用 `VMLauncher.launchJVM(args)`。
8. Minecraft 在内嵌 Java 运行时中执行，输入、剪贴板、日志、退出码和 Surface 事件通过 JNI/桥接层返回 Android。

FCLauncher 的原生库和代码主要位于：

```text
FCLauncher/src/main/jni/fcl
FCLauncher/src/main/jni/ctxbridges
FCLauncher/src/main/jni/environ
FCLauncher/src/main/jni/virgl
FCLauncher/src/main/jni/driver_helper
FCLauncher/src/main/jni/linkerhook
FCLauncher/src/main/jniLibs
```

支持的 native ABI 包括 `arm64-v8a`、`armeabi-v7a`、`x86` 和 `x86_64`，预置库覆盖 ANGLE、GL4ES、OSMesa、VirGL、Vulkan driver 和 OpenAL 等运行时组件。

## Compose/Miuix 迁移约定

项目目前处于旧 View/XML 与 Compose/Miuix 双栈并存的迁移阶段。迁移代码主要位于：

```text
FCL/src/main/java/com/tungsten/fcl/ui/compose
FCL/src/main/java/com/tungsten/fcl/ui/theme
FCL/src/main/java/com/tungsten/fcl/ui/bridge
FCL/src/main/java/com/tungsten/fcl/ui/main/compose
FCL/src/main/java/com/tungsten/fcl/ui/account/compose
FCL/src/main/java/com/tungsten/fcl/ui/setting/compose
FCL/src/main/java/com/tungsten/fcl/ui/version/compose
FCL/src/main/java/com/tungsten/fcl/ui/manage/compose
FCL/src/main/java/com/tungsten/fcl/ui/download/compose
```

`UIManager` 和 `PageManager` 仍是顶层导航和页面栈的基础；Compose 页面通常通过 `ComposeView` 嵌入旧页面壳，`LegacyBridge` 负责导航、弹窗和任务桥接，`FCLTheme.kt` 统一封装 Miuix 主题和 FCL 主题色。

已迁移页面和弹窗通过 `USE_COMPOSE_*` 开关选择 Compose 或旧 View/XML 路径，默认通常为 `true`。旧实现暂时不能随意删除，涉及 UI 的改动应验证两条路径。联机菜单、游戏内菜单、控制器以及部分复杂页面仍保留原生 View 实现。游戏启动链路、JNI/NDK、`FCLCore`、文件系统路径、权限体系和游戏内控制 UI 属于高风险边界，迁移 UI 时不要改变其行为或生命周期假设。

Compose 弹窗自适应的两个实测坑（2026-08 键码弹窗修复记录）：

- `wrapContentWidth` 卡片内任何 `fillMaxWidth` 子组件（如 `FCLDialogButtonsRow`）会把卡片撑到最大约束宽度，内容不足时右侧留白；按钮行应 wrap 宽度 + `align(Alignment.End)`。
- 不要把"等比缩放原生 View"当自适应首选：`graphicsLayer` 作用在 `AndroidView` 上 `transformOrigin` 不可靠；`FCLLinearLayout` 等主题 View 在 UNSPECIFIED 测量下固有尺寸会失真，运行时测量再缩放风险高。优先对齐旧版 wrap_content 语义（弹窗迁就内容自然尺寸），竖向溢出交给布局内 ScrollView。

## 构建命令

优先使用项目自带 Gradle Wrapper。Windows 使用 `gradlew.bat`，Linux/macOS 或 Git Bash 使用 `./gradlew`。

常用命令：

```bash
# 查看全部任务
./gradlew tasks --all --no-daemon

# 主应用 Debug 构建
./gradlew :FCL:assembleDebug --no-daemon

# 迁移文档记录的构建门禁
./gradlew :FCL:assembleDebug --console=plain

# fordebug 构建，可按 ABI 构建
./gradlew :FCL:assembleFordebug --no-daemon -Darch=all

# Release 构建
./gradlew :FCL:assembleRelease --no-daemon -Darch=all

# 构建 LWJGL 运行时资源
./gradlew :LWJGL-Pojav:buildLwjgl

# 静态检查和签名信息
./gradlew :FCL:lint
./gradlew :FCL:check
./gradlew :FCL:signingReport
```

`-Darch` 支持：

- `all`
- `arm`：`armeabi-v7a`
- `arm64`：`arm64-v8a`
- `x86`
- `x86_64`

FCL 的主要构建变体为 `debug`、`fordebug` 和 `release`。`fordebug` 使用 debug 签名、增加 `.debug` applicationId，并配置了 R8；`release` 使用正式签名并启用 R8。APK 名称由 `FCL/build.gradle.kts` 统一生成，格式为 `FCL-{buildType}-{versionName}-{abi}.apk`。合并 assets 后，构建逻辑会根据 ABI 清理不需要的 Java runtime 资源。

`release` 和 `fordebug` 的 R8 配置见 `FCL/proguard-rules.pro`。当前配置使用 `-dontobfuscate` 和 `-dontoptimize`，并保留自有包、JNI、Gson 注解模型和 AndroidX Startup 等反射或 native 需要的符号；由于项目通过 `getIdentifier` 动态查找资源，未启用 `shrinkResources`。

Gson/反射兼容的硬性约定（经 R8 官方 FAQ GSON 小节与 Gson Troubleshooting.md 核实）：

- 匿名 `TypeToken` 子类（`new TypeToken<...>() {}`）的泛型签名，必须三条规则同时配置才能在 R8 下存活：`-keepattributes Signature` + `-keep class com.google.gson.reflect.TypeToken { *; }` + `-keep class * extends com.google.gson.reflect.TypeToken`，full/compat 模式皆然（`proguard-rules.pro` 已配置）。
- 即使如此，源码中**优先使用 `TypeToken.getParameterized(...)`**（gson 官方认可的匿名子类替代方案，不依赖 Signature 属性，天然免疫签名擦除）：泛型容器用它构造，非泛型直接传 `X.class`；注意 `getParameterized` 的实参个数必须等于 rawType 自身声明的类型变量数（嵌套泛型需分层构造），通配符类型（`<?>`）没有公开 API，只能按上界显式给出类型实参。
- `-keepattributes` 只写 R8/ProGuard 合法属性名（Signature/Exceptions/InnerClasses/EnclosingMethod/RuntimeVisibleAnnotations 等），非法名不报错、静默失效；多条 `-keepattributes` 为叠加关系。
- full mode 下 `-keepclassmembers` 不再隐式保留宿主类的可实例化性，仅靠反射创建的对象（反序列化目标、adapter）需要类级 `-keep`。
- 项目 gson 版本 2.10.1 早于 2.11.0，不含 jar 内置 `META-INF/proguard/gson.pro` 自动规则，相关 keep 需手工维护；升级 gson 后应重评本节规则。
- 改动 R8 规则或升级 AGP/gson 后，必须构建 fordebug 并真机验证（R8 官方与 gson 官方均要求 minify 后实测）。

FCL 中的 `*AarMetadata` 任务目前被临时禁用，因为 Miuix 0.9.3 和部分 Compose 依赖声明的 `minCompileSdk` 高于当前 `compileSdk 35`。相关说明在 `FCL/build.gradle.kts`。升级构建栈和 compile SDK 后，应重新评估并恢复该检查。

## 测试与验证

仓库当前没有发现实际的 `src/test`、`src/androidTest` 或 `src/testFixtures` 测试源，也没有现成的 JUnit、Robolectric 或 Espresso 测试用例。Android Gradle Plugin 会生成测试任务，例如：

```bash
./gradlew :FCL:testDebugUnitTest
./gradlew :FCL:testFordebugUnitTest
./gradlew :FCL:testReleaseUnitTest
./gradlew :FCL:connectedDebugAndroidTest
```

这些任务的存在不代表仓库已有测试覆盖。当前验证主要依赖：

- Gradle 编译，至少运行 `:FCL:assembleDebug`
- 必要时运行 `:FCL:lint` 或 `:FCL:check`
- 静态源码核查和全仓搜索
- Compose 迁移页面的 Compose/旧 View 双分支检查
- Android 真机或模拟器上的功能冒烟和回归验证

迁移文档明确记录目前尚未完成完整真机验收，性能、设备兼容性和完整功能验收不能由静态构建通过替代。涉及 UI、游戏启动、输入、渲染器、权限、文件访问或 JNI 的改动，应在可用设备上补充验证，并在 PR 中说明结果。CI 当前只执行 APK 构建，不自动执行 unit test、instrumentation test、lint 或 `check`。

## CI、发布与部署

`.github/workflows/build.yml` 在 Pull Request、普通 push 和手动 `workflow_dispatch` 时运行，忽略工作流文件、Markdown 和 `version_map.json` 的变更。CI 使用 Ubuntu、Temurin JDK 17、Gradle cache，并递归检出 submodule。

构建矩阵为 `all`、`arm`、`arm64`、`x86`、`x86_64`：

- `FCL-Team` 仓库的非 PR、非 Tag push 构建 `assemblerelease -Darch=<arch>`。
- Pull Request 或其他仓库构建 `assemblefordebug -Darch=<arch>`。
- 构建产物分别从 `FCL/build/outputs/apk/release/*` 或 `FCL/build/outputs/apk/fordebug/*` 上传为 GitHub Actions artifact。

`.github/workflows/release.yml` 在任意 Tag push 时对五种 ABI 构建 Release APK，并使用 `softprops/action-gh-release@v3` 将 `FCL/build/outputs/apk/release/*.apk` 发布到 GitHub Release。发布依赖 GitHub Secrets 中的签名密码、API key 和 release token。

## 本地密钥与安全注意事项

本地构建需要的 API key 可在未提交的 `local.properties` 中配置：

```properties
oauth.api.key=<Azure 应用 Client ID>
curse.api.key=<CurseForge API Key>
```

实际键名为 `oauth.api.key` 和 `curse.api.key`；也可以使用环境变量 `OAUTH_API_KEY` 和 `CURSE_API_KEY`。CI 通过 GitHub Secrets 注入。不要把 API key、密码或 `local.properties` 内容提交到仓库。

正式签名配置在 `FCL/build.gradle.kts`：正式 keystore 路径为 `../key-store.jks`，alias 为 `FCL-Key`，密码来自 `FCL_KEYSTORE_PASSWORD` 或 `local.properties` 中的 `pwd`。`fordebug` 使用 `../debug-key.jks` 和 debug 签名配置。

仓库当前包含 `key-store.jks`、`debug-key.jks` 和 `private_key.pepk` 等敏感签名材料；处理发布密钥、签名配置或 CI 时必须避免泄露内容。正式发布前应由维护者确认这些材料是否已公开、是否需要轮换，并优先使用受保护的 CI secret 或签名服务。

Manifest 还声明了网络、存储、安装 APK、前台服务、通知、录音、VPN、Provider 和导入 alias 等高权限或 exported 组件，并设置了 `usesCleartextTraffic="true"`。修改这些配置时必须结合真实功能、Android 目标版本和发布渠道政策逐项复核，不能仅为通过构建而放宽权限或导出设置。

## 代码与文档约定

- Kotlin 和 Java 混合维护；不要因为迁移 Compose 就假设现有模块可以全部改为 Kotlin。
- 包名主要为 `com.tungsten.fcl`、`com.tungsten.fclcore`、`com.tungsten.fcllibrary` 和 `com.tungsten.fclauncher`。
- 类名使用 PascalCase，方法和变量使用 camelCase，常量通常使用全大写下划线形式。
- Compose 页面通常放在对应的 `ui/**/compose/` 目录，常见命名为 `*Screen`、`*ViewModel`、`*Host`；旧 View 页面常见 `*Page`、`*UI`、`*Dialog`、`*Adapter`。
- 自研 Android 控件常带 `FCL` 前缀，Miuix 迁移实现常带 `Miuix` 前缀；资源使用 Android 标准下划线命名。
- Kotlin 和 Java 现有代码通常使用 4 空格缩进。局部修改应匹配邻近代码，不要顺带进行大范围格式化或无关重构。
- 新增项目内部注释优先使用简短中文；上游 HMCL、JDK、ZipFS 等代码的版权头、英文 API 文档和原有注释应保留。
- 修改行为后同步检查附近注释、迁移文档和回滚开关名称，避免文档继续描述旧行为。
- 根 `.gitignore` 已忽略 Gradle/Android 构建产物、`local.properties`、IDE 文件、日志和部分本地产物；不要提交生成的 APK、密钥、API key 或本地配置。
