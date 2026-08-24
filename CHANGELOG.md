# Changelog

## [1.3.2.8] - 2026-08-24

### 中文

#### ✨ 新功能

1. **启动页显示加载信息与进度条**：SplashActivity 改用 viewBinding，加载期间展示加载状态与进度反馈
2. **设置页 tab 随内容滚动收起/展开**：AppBarLayout 效果，滚动页面时 tab 自动收起、上滑恢复
3. **设置项行下方新增作用描述**：为设置项补充说明文字，方便了解设置作用
4. **下载页 tab 切换添加过渡动画**：tab 与模式页内容更新均淡入过渡
5. **右侧菜单手势交互**：支持双指滑动切换与手势显示/隐藏，隐藏时皮肤预览位置固定
6. **LWJGL GLFW 补充 glfwGetWindowPos stub**：窗口位置恒为 0,0

#### ⚡ 优化

1. **主界面 UI 架构重构（ViewPager2）**：UIManager 改用 ViewPager2 承载 8 个 UI 页面，页面随回收销毁不保留状态；页面切换统一为淡入上滑过渡动画；主界面禁用滑动手势仅通过菜单切换，避免与页面内纵向滚动冲突
2. **下载页全面优化**：5 个下载模式共享单个布局实例，tab 切换经 ViewModel 恢复状态；列表布局轻量化（线性排列、去 marquee）；图标限制 90x90 解码尺寸、快速滑动时暂停加载，消除全尺寸解码 GC 停顿；Mod 翻译数据后台预热，避免首次 bind 主线程解析
3. **设置页/版本设置页重构为 RecyclerView 行级复用**
4. **Profile/VersionSetting/Theme 完成 fakefx 迁移**：Profile 重构为 Kotlin 移除 fakefx，Profiles.selectedProfile 迁移 Repository 单例 + StateFlow，selectedVersion 迁移 StateFlow；VersionSetting 改用普通类型字段；Theme/ThemeEngine 重构为 Kotlin + StateFlow，主题存储从 SharedPreferences 迁移 DataStore
5. **模块合并与代码清理**：FCLauncher 与 FCLCore 模块整体并入 FCL；移除 FCLPath.CONTEXT，全局 Context 改用 FCLApp.getAppContext()/getActivity()；AndroidUtils 迁移至 com.mio.util 顶层函数；getLocalizedText 静态 key 迁移至 R.string；删除 DisplayAnimUtils 等死代码
6. **版本列表加载提速**：解析与资源缓存、切换 profile 保留缓存；Mod 数统计移入加载流程，避免滑动时主线程目录 IO
7. **下载页搜索框优化**：imeOptions 组合 flagNoFullscreen，消除 AutofillManager 日志刷屏
8. **界面细节优化**：圆角统一 8dp、页面 tab 背景改为顶部圆角、左侧菜单点击播放选中动画、临时页返回时下层内容上滑进入过渡
9. **更新 libcc.so**：方向控件统一转 ZL2 摇杆并修正 sizeType
10. **更新 LWJGL 3.3.3/3.4.1 合并产物**：含 glfwGetWindowPos stub；移除运行时 jsr305.jar，解决与 java.annotation 模块的包冲突

#### 🐛 修复

1. 修复切换版本后退出启动器版本回退（selectedVersion 变化未触发配置保存）
2. 修复版本列表并发崩溃、配置保存刷屏与空版本设置 NPE
3. 修复模组管理页往返切换时全量重扫模组导致的主线程 ANR
4. 修复亮暗模式切换不生效，主题切换不再依赖 Activity 重建
5. 修复主界面销毁后主题刷新回调加载崩溃，主题回调改弱引用修复页面回收后视图泄漏
6. 修复快速滑动/切换页面时控件首帧闪白（registerEvent 改为同步执行）
7. 修复离线账户披风加载 NPE
8. 修复主界面版本图标放大显示不完全（共享 Drawable 实例 bounds 互相污染）
9. 修复下载页重建后首次切换模式列表空白（复用缓存 adapter 时补设 LayoutManager）
10. 修复下载列表图标全尺寸解码 GC 卡顿、图片加载完成触发布局重排、切换模式时滑入动画重播
11. 修复下载页切换 tab 时临时页覆盖层遮挡新页面，多层临时页透明背景透出
12. 修复页面切换/ViewPager 切换闪烁（重复 dispatch 当前页、平滑滑动后淡入闪烁）
13. 修复监听回调内增删监听导致的并发修改崩溃（遍历前复制列表）
14. 修复 Profile/版本监听累积泄漏（新增 Profiles.unregisterVersionsListener），切换 profile 时取消旧加载
15. 修复控制器列表项 inflate 时提前 attachToRoot 的问题，控制器截图按原始分辨率加载避免预览模糊
16. 修复 SmoothFont 选择字体后重启字体缩放错误
17. 修复 VersionInstallInfoPage 选择安装器版本后条目未刷新
18. 修复 Files 目录流未关闭导致的资源泄漏，LWJGL 版本路径补空值防护

#### 🔧 其他

1. 更新多语言翻译（德、波斯、葡、俄、乌、越、繁中等）
2. 新增重构内容的 androidTest 仪器测试

### English

#### ✨ New Features

1. **Loading info and progress bar on the Splash screen**: SplashActivity now uses viewBinding and shows loading status with progress feedback
2. **Settings tab collapses/expands with content scroll**: AppBarLayout effect — the tab hides while scrolling and reappears when scrolling up
3. **Usage descriptions below setting items**: Explanatory text added under settings for easier understanding
4. **Transition animation for download page tab switching**: Tab and mode page content updates fade in
5. **Right-side menu gesture interaction**: Two-finger swipe switching and gesture-based show/hide, with the skin preview position fixed when hidden
6. **LWJGL GLFW glfwGetWindowPos stub added**: Window position is always 0,0

#### ⚡ Improvements

1. **Main UI architecture refactored (ViewPager2)**: UIManager now hosts the 8 UI pages with ViewPager2; pages are destroyed on recycle without keeping state; page transitions unified to fade-in & slide-up animations; swipe gesture disabled on the main UI (switching only via the menu) to avoid conflicts with in-page vertical scrolling
2. **Download page fully optimized**: The 5 download modes share a single layout instance with tab switching state restored via ViewModel; lighter list layouts (linear arrangement, no marquee); icons decoded at a 90x90 limit and image loading paused while scrolling fast, eliminating GC hitches from full-size decoding; Mod translation data pre-warmed in the background to avoid main-thread parsing on first bind
3. **Settings/version settings pages refactored to RecyclerView row-level reuse**
4. **Profile/VersionSetting/Theme completed the fakefx migration**: Profile refactored to Kotlin without fakefx, Profiles.selectedProfile migrated to a Repository singleton + StateFlow, selectedVersion migrated to StateFlow; VersionSetting uses plain fields; Theme/ThemeEngine refactored to Kotlin + StateFlow with theme storage migrated from SharedPreferences to DataStore
5. **Module merge and code cleanup**: FCLauncher and FCLCore modules merged into FCL; FCLPath.CONTEXT removed, global Context now uses FCLApp.getAppContext()/getActivity(); AndroidUtils moved to com.mio.util top-level functions; getLocalizedText static keys migrated to R.string; dead code like DisplayAnimUtils removed
6. **Version list loading speedup**: Parsing and resource caching, cache kept when switching profiles; mod count statistics moved into the loading flow to avoid main-thread directory IO while scrolling
7. **Download page search box optimization**: imeOptions combined with flagNoFullscreen to eliminate AutofillManager log spam
8. **UI detail polish**: Unified 8dp corner radius, top-rounded page tab backgrounds, left menu click selection animation, lower content slides up when returning from a temp page
9. **libcc.so updated**: Direction controls unified to the ZL2 joystick with sizeType fixed
10. **LWJGL 3.3.3/3.4.1 merged artifacts updated**: Including the glfwGetWindowPos stub; removed the runtime jsr305.jar to resolve the package conflict with java.annotation

#### 🐛 Bug Fixes

1. Fixed the version reverting after exiting the launcher (selectedVersion change not triggering config save)
2. Fixed concurrent crashes in the version list, config save spam and an NPE on empty version settings
3. Fixed a main-thread ANR caused by a full mod rescan when navigating back and forth in the mod management page
4. Fixed light/dark mode switching not taking effect; theme switching no longer depends on Activity recreation
5. Fixed a crash when theme refresh callbacks load after the main UI is destroyed; theme callbacks now use weak references to fix view leaks after page recycle
6. Fixed first-frame white flash on controls when swiping/switching pages quickly (registerEvent now runs synchronously)
7. Fixed an offline account cape loading NPE
8. Fixed version icons on the main UI being enlarged (shared Drawable instances polluting each other's bounds)
9. Fixed the blank mode list on the first switch after the download page rebuilds (LayoutManager re-set when reusing the cached adapter)
10. Fixed GC hitches from full-size icon decoding in the download list, layout reflow triggered by image load completion and slide-in animation replaying on mode switch
11. Fixed temp pages covering the new page when switching tabs in the download page and transparent backgrounds showing through with nested temp pages
12. Fixed page/viewpager transition flicker (duplicate dispatch of the current page, fade-in flicker after smooth sliding)
13. Fixed a ConcurrentModificationException from adding/removing listeners inside callbacks (list copied before iteration)
14. Fixed accumulating Profile/version listener leaks (Profiles.unregisterVersionsListener added) and cancelled stale loads when switching profiles
15. Fixed controller list items being attachToRoot too early during inflation; controller screenshots now load at original resolution to avoid blurry previews
16. Fixed SmoothFont font scaling errors after restarting when a font is selected
17. Fixed VersionInstallInfoPage items not refreshing after selecting an installer version
18. Fixed resource leaks from unclosed Files directory streams; added null protection for LWJGL version paths

#### 🔧 Other

1. Updated multilingual translations (German, Persian, Portuguese, Russian, Ukrainian, Vietnamese, Traditional Chinese, etc.)
2. Added androidTest instrumentation tests for the refactoring

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

