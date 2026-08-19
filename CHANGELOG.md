# Changelog

## [1.3.2.7] - 2026-08-19

### 中文

#### ✨ 新功能

1. **接入 Weblate 本地化翻译**：接入 Weblate 平台，翻译协同更新更高效

#### ⚡ 优化

1. **FCLLibrary 模块整体并入 FCL 模块**：源码/资源/Manifest 合并，减少模块依赖与构建复杂度
2. **布局加载改为同步**：移除 AsyncLayoutInflater，布局统一同步加载，避免异步加载带来的布局问题
3. **移除 UIListener 回调接口与 UIManager.init 回调参数**：简化 UI 生命周期管理代码

#### 🐛 修复

1. 修复损坏的压缩文件抛 ZipError 导致的崩溃，避免整合包安装失败
2. 修复删除账户弹窗内容文字异常（#1752）
3. 修复 GLFW 上下文版本探测问题：仅当驱动版本更高时才覆盖上下文版本，放宽 minor 下界并统一上界
4. 更新 LWJGL 3.3.3/3.4.1 合并产物，修复使用部分模组后启动游戏画面无法显示的问题
5. 修复世界列表条目上下间距过大的问题

### English

#### ✨ New Features

1. **Integrated Weblate localization**: Connected the project to Weblate for more efficient collaborative translation updates

#### ⚡ Improvements

1. **Merged the FCLLibrary module into FCL**: Sources, resources and manifest are unified to reduce module dependencies and build complexity
2. **Synchronous layout inflation**: Removed AsyncLayoutInflater so layouts are always inflated synchronously, avoiding layout issues caused by async inflation
3. **Removed the UIListener callback interface and UIManager.init callback parameter**: Simplified UI lifecycle management code

#### 🐛 Bug Fixes

1. Fixed a crash caused by corrupted archives throwing ZipError, preventing modpack installation failures
2. Fixed abnormal text in the account deletion dialog (#1752)
3. Fixed GLFW context version detection: only override the context version when the driver version is higher, relaxed the minor lower bound and unified the upper bound
4. Updated LWJGL 3.3.3/3.4.1 merged artifacts, fixed the game screen not displaying after using certain mods
5. Fixed excessive vertical spacing between world list items

## [1.3.2.6] - 2026-08-14

### 中文

#### ✨ 新功能

1. **损坏的模组文件支持确认删除**：模组文件损坏时弹窗提示，支持确认后删除，避免手动去文件管理器清理

#### ⚡ 优化

1. **模组列表增量加载优化**：扫描过程中边扫描边分批显示，筛选时不再重新扫描，模组多时列表响应更快
2. **账号皮肤上传改为协程实现**：不再使用 Task 异步系统，逻辑更简洁清晰
3. **移除 NG-GL4ES 子模块**：渲染器改用独立项目（NG-GL4ES）的预构建产物，不再以子模块形式依赖源码

#### 🐛 修复

1. 修复部分设备 F3 页面 CPU 显示为 Unknown 的问题
2. 修复部分渲染错误（#1747）
3. 修复 GLFW 窗口上下文创建失败时导致的崩溃
4. 修复 LWJGL freetype 库从打包 natives 目录加载的问题
5. 损坏的模组文件跳过并提示，避免整个模组列表加载失败

### English

#### ✨ New Features

1. **Confirmation dialog for corrupted mod files**: Corrupted mods now show a prompt with the option to delete them, so users no longer need to clean up files manually

#### ⚡ Improvements

1. **Incremental mod list loading**: Mods are now scanned and displayed in batches, and filtering no longer rescans the whole list, making large mod lists much more responsive
2. **Account skin upload reimplemented with coroutines**: Replaced the Task async system with coroutines for cleaner logic
3. **Removed the NG-GL4ES submodule**: The renderer now uses prebuilt artifacts from the standalone NG-GL4ES project instead of vendoring its source

#### 🐛 Bug Fixes

1. Fixed F3 page CPU showing "Unknown" on some devices
2. Fixed some rendering errors (#1747)
3. Fixed a crash when GLFW window context creation fails
4. Fixed LWJGL freetype library loading from the packaged natives directory
5. Corrupted mod files are now skipped with a prompt instead of failing the entire mod list
## [1.3.2.5] - 2026-08-12

### 中文

#### ✨ 新功能

1. **支持 LWJGL 3.3.3 / 3.4.1 双版本**：彻底移除旧 LWJGL-Pojav 模块，改为基于官方 LWJGL 合并 Android 源码的构建方式，两个版本按需切换；新增 3.4.1 Vulkan 模块支持
2. **GLFW 桥延迟初始化**：GLFW 初始化推迟到真正需要时进行，并补充了 3.4.1 版本缺失的 API
3. **删除仿基岩触控相关功能**：移除历史遗留的仿基岩触控支持，简化代码与界面

#### ⚡ 优化

1. **模组列表远程信息加载优化**：远程信息缓存复用 + 可见条目优先加载，浏览模组列表更流畅
2. **FCLConfig 迁移 Kotlin**：配置类迁移至 Kotlin 并接入 LWJGL natives 路径管理
3. **mod 元数据读取迁移**：从 ZipFileTree 迁移到 zipfs，统一走混合编码兼容的压缩文件系统
4. **jre_launcher 日志改进**：改用 FCL_LOG 统一日志输出

#### 🐛 修复

1. 修复游戏设置页面中游戏参数与 Java 虚拟机参数无法通过长按设置为空的问题（#1728）
2. 修复混合编码 zip 解压时条目名解码错误导致的 NoSuchFileException
3. 修复 LWJGL 构建流程问题、IDE 中 LWJGL 模块类意外报错的问题
4. 同步 GLCapabilities 与 Amethyst 实现，同步 JNI 与新版 LWJGL 接口
5. LWJGL natives 按架构过滤支持命令行 `-Darch` 覆盖
6. 完善 LWJGL 双版本类路径与库过滤逻辑
7. ProcessService 对空 command 增加兜底处理
8. 同步 Amethyst JNI 修复并重构窗口尺寸事件上报
9. 修复 freetype 库名引用，改为相对名
10. 更新 Java 25 网盘下载链接

#### 🔧 其他

1. 原生代码布局向 Amethyst-Android 对齐：JVM 钩子提取到 `jvm_hooks/` 目录、`awt_bridge.c` 移至 jni 根目录、linkerhook 重写为 C 并合并驱动加载到 egl_bridge
2. 更新 MioLibPatcher、Java 25、vulkan 3.4.1 模块等运行时组件
3. 更新多语言字符串资源

### English

#### ✨ New Features

1. **Dual LWJGL support (3.3.3 / 3.4.1)**: Removed the old LWJGL-Pojav module entirely and switched to a build based on official LWJGL merged with Android sources; both versions can be used on demand, with new Vulkan module support for 3.4.1
2. **Delayed GLFW bridge initialization**: GLFW is now initialized lazily when actually needed, with missing 3.4.1 APIs added
3. **Removed bedrock-style touch controls**: Dropped the legacy bedrock-style touch control feature to simplify the codebase and UI

#### ⚡ Improvements

1. **Mod list remote info loading optimized**: Remote info is now cached and reused, with visible items loaded first for smoother browsing
2. **FCLConfig migrated to Kotlin**: Configuration class migrated to Kotlin and integrated with LWJGL natives path management
3. **Mod metadata reading migrated**: Moved from ZipFileTree to zipfs, unified on a mixed-encoding-compatible zip filesystem
4. **jre_launcher logging improved**: Switched to unified FCL_LOG output

#### 🐛 Bug Fixes

1. Fixed game arguments and Java VM arguments being unable to be cleared via long-press on the game settings page (#1728)
2. Fixed NoSuchFileException caused by entry name decoding errors when extracting mixed-encoding zips
3. Fixed LWJGL build pipeline issues and unexpected IDE errors from LWJGL module classes
4. Synced GLCapabilities with the Amethyst implementation and synced JNI with the new LWJGL interfaces
5. LWJGL natives per-architecture filtering now honors the `-Darch` command-line override
6. Improved dual-version LWJGL classpath and library filtering
7. Added fallback handling for empty commands in ProcessService
8. Synced Amethyst JNI fixes and reworked window size event reporting
9. Fixed freetype library name reference to use a relative name
10. Updated Java 25 netdisk download links

#### 🔧 Other

1. Native code layout aligned with Amethyst-Android: JVM hooks moved to `jvm_hooks/`, `awt_bridge.c` moved to the jni root, linkerhook rewritten in C with driver loading merged into egl_bridge
2. Updated runtime components: MioLibPatcher, Java 25, Vulkan 3.4.1 module
3. Updated multilingual string resources

