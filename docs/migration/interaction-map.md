# FCL 交互逻辑映射（小步骤 1.2）

> 阶段一：侦察与基线建立 · 交互逻辑映射
> 分支：`feature/miuix-migration` ｜ 侦察范围：`FCL/src/main/java`（UI 全部集中在 FCL 模块）
> 本文档是后续 Compose/Miuix 迁移 Agent 的唯一事实来源。所有条目均基于对实际源码的逐文件核实，禁止在未核对本文档引用位置前臆测行为。

## 阅读说明

- **难点评级**：低 = 纯点击/弹窗/Toast/简单开关；中 = 列表多选/表单联动/fakefx 绑定/简单动画/跨页传参；高 = 手写手势分发/拖拽/游戏内覆盖层/阻塞式对话框/复杂页面栈/GL 与 Surface 托管。
- **路径**均为相对项目根的路径；行号为侦察时实际读取值。
- **术语**：fakefx = 工程内自带的 JavaFX 风格属性绑定体系（`BooleanProperty`/`StringProperty`/`bindBidirectional` 等），Compose 化需统一转为 State/Flow；FCLMenuView/FCLDialog/FCLAlertDialog/EditDialog/FCLColorPickerDialog/FileBrowser/FCLCheckBoxTreeAdapter 等为 FCLLibrary 模块（红线）组件，本文档只登记其在 FCL 侧的使用点。
- **"临时页（TempPage）"**：`PageManager.showTempPage()` 压入的栈式覆盖层页面，类似 dialog 栈，是整个 App 页面导航的核心机制。

## 0. 全局结论（跨区域的架构性事实）

| # | 全局事实 | 关键位置 | 难点 |
|---|---|---|---|
| G1 | **双层自研导航体系**：UI 级 `UIManager.switchUI`（8 个子 UI 全部 addView 到同一容器，靠 View 可见性 + 补间动画切换）+ 页级 `PageManager.switchPage/showTempPage/dismissCurrentTempPage`（页面栈 + 临时页栈），无 Fragment/NavController | `FCL/src/main/java/com/tungsten/fcl/ui/UIManager.kt:26-91`；`FCL/src/main/java/com/tungsten/fcl/ui/PageManager.java:69-149` | 高 |
| G2 | **返回键不走 OnBackPressedDispatcher**：`MainActivity.onKeyDown(KEYCODE_BACK)` → `UIManager.onBackPressed()` → 当前 UI → PageManager 临时页栈 → 默认事件（主页 = HOME + `exitProcess(0)`；非主页 = 切回 home） | `FCL/src/main/java/com/tungsten/fcl/activity/MainActivity.kt:215-225, 283-289` | 高 |
| G3 | **UI/页面切换动画**：UI 级用 FCLLibrary 资源 `ui_show.xml/ui_hide.xml`（横向滑入滑出+淡入淡出），页级/TempPage 用 `page_show.xml/page_hide.xml`（纵向），时长 = 主题 `animationSpeed × 100`ms；TempPage `dismiss()` 后 `Handler.postDelayed(onDestroy, 800)` 延迟销毁与动画时长耦合。现状缺陷：`hideViewWithAnim` 先 GONE 再播动画，滑出动画实际不可见，重建时不必还原 | FCLLibrary `DisplayAnimUtils`/`FCLCommonUI`/`FCLTempPage`（红线，仅登记）；调用点遍布各 UI/Page 基类 | 中 |
| G4 | **fakefx 属性绑定渗透全部表单与列表**：双向绑定、可见性绑定、Spinner 选择绑定（`FXUtils.bindSelection`/`bindBoolean`）、任务进度绑定（`percentProgressProperty().bind()`）。VersionSettingPage 单页 17 组绑定 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/VersionSettingPage.kt:286-350`；`FCL/src/main/java/com/tungsten/fcl/util/FXUtils.java` | 高 |
| G5 | **TaskDialog 是全应用统一任务进度弹窗，19 处触发**：启动游戏 `game/LauncherHelper.java:130`；版本 `ui/version/Versions.java:76,184`；整合包 `ui/download/modpack/ModpackInstaller.java:35`、`ModpackSelectionPage.java:96,123`；下载 `ui/download/common/DownloadPage.java:347`、`RemoteModVersionPage.java:74`、`ui/download/version/VersionInstallInfoPage.java:226`；管理 `ui/manage/InstallerListPage.java:181,228`、`ModListPage.java:398`、`ModUpdatesPage.java:120,152`、`ModpackFileSelectionPage.java:113`、`WorldExportDialog.java:70`；控制器 `ui/controller/ControllerRepoPage.java:261`、`ControllerDownloadPage.java:197`；更新 `upgrade/UpdateDialog.java:121`。含速度事件总线弱引用订阅、autoClose 自 dismiss、`setCancelable(false)`。**建议最先做出 Miuix 版通用 TaskDialog** | `FCL/src/main/java/com/tungsten/fcl/ui/TaskDialog.java:40-121` | 中 |
| G6 | **动画全部走 `AnimUtil` 的 ObjectAnimator 工厂**（13 个方法，`FCL/src/main/java/com/mio/util/AnimUtil.kt:11-168`）；全工程 grep **无** `ValueAnimator`/`AnimatorSet`/`View.animate()`；另有 `anim_scale` StateListAnimator 按压缩放（`ui/download/common/ModGameVersionAdapter.java:62`、`ModVersionAdapter.java:63`）；转场仅 SplashActivity 两处（Fragment 事务动画 `R.anim.frag_start_anim/frag_stop_anim`、`ActivityOptionsCompat.makeCustomAnimation(0,0)`，`activity/SplashActivity.kt:116-144`） | 见左 | 低 |
| G7 | **对话框体系**：FCL 侧共 38 个自定义 Dialog 类（extends FCLDialog，清单见附录 A）；全局 helper `showErrorDialog/showWarningDialog/showItemSelectionDialog` 定义于 FCLLibrary `FCLLibrary/src/main/java/com/mio/util/DialogUtil.kt:9-35`（红线，登记）；`ProgressDialog` 7 处/4 文件（`ui/version/Versions.java`×3、`ui/manage/ManagePage.kt`×2、`ui/controller/ControllerUploadPage.java`、`ControllerManagePage.java`）；`Toast.makeText` 共 91 处/42 文件 | grep 全量核实 | 低 |
| G8 | **文件选择统一走 `MainActivity.fileLauncher`**（FileBrowserLauncher，Activity Result API，自研 FileBrowserActivity 或 SAF 双路径）：`launchSingleSelection`（文件/目录模式）与 `launchMultiSelection`，回调内普遍有 `AndroidUtils.isDocUri` 的 SAF 拷贝分支。Compose 化需统一封装为 `rememberLauncherForActivityResult` + 协程 | 例：`ui/version/AddProfileDialog.java:46-50`、`ui/manage/ModListPage.java:322-382` | 中 |
| G9 | **全工程未发现**（grep 核实，迁移时无需重建）：`SwipeRefreshLayout`（无任何下拉刷新）、`ItemTouchHelper`（无拖拽排序/滑动删除）、`overridePendingTransition`、`GestureDetector`/`ScaleGestureDetector`、`onInterceptTouchEvent`、Snackbar | grep 全量核实 | — |
| G10 | **静态单例反向调用遍布**：`MainActivity.getInstance()`、`ManagePageManager.instance`、`DownloadPageManager.instance`、`VersionPageManager.getInstance()`、`SettingPageManager.instance`、`ControllerPageManager.instance` 被 Adapter/对话框/数据载体直接调用来刷页面、切 Tab、压临时页；Compose 需改为导航事件/CompositionLocal/ViewModel | 各 PageManager companion object | 高 |
| G11 | **跨 UI 硬编码跳转**：多处直接操作 `MainActivity.binding.home/download/controller.setSelected(true)` + 其他 UI 的 `pageManager/tabLayout.selectTab(...)` | `MainActivity.kt:426-441`、`ui/manage/ManageUI.java:66`、`ui/manage/ModListPage.java:460-464`、`ui/manage/LocalModListAdapter.kt:212-224`、`ui/manage/VersionSettingPage.kt:463-470`、`ui/version/Versions.java:226-228` | 高 |
| G12 | **manifest 关键声明**：`.ImportActivity`（`AndroidManifest.xml:62-93`）是 **activity-alias（无需对应类）**，target = SplashActivity，带 zip/mrpack/7z 的 VIEW intent-filter，是外部整合包导入入口，**迁移时最易被误删**；`ShellActivity` 是全 manifest 唯一 `windowSoftInputMode=adjustResize`（:110）；`JVMActivity` 显式 `launchMode=standard` + `alwaysRetainTaskState`（:112-117）；`JVMCrashActivity` 独立 `:crash` 进程（:121）；全部 Activity 声明了 configChanges 与横屏（Shell 为竖屏） | `FCL/src/main/AndroidManifest.xml:49-130` | 中 |

---

## 1. Activity 与 Fragment 层

### 1.1 MainActivity（831 行，核心）

继承 `FCLActivity`（红线模块），实现 `FCLMenuView.OnSelectListener`、`View.OnClickListener`。文件：`FCL/src/main/java/com/tungsten/fcl/activity/MainActivity.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 左侧菜单 6 项（home/manage/download/controller/multiplayer/setting）选中切换 UI | `onCreate` 内 `uiManager.init{}` + `onSelect(FCLMenuView)` | MainActivity.kt:227-233, 322-372 | 中 | FCLMenuView 侧栏 + `uiManager.switchUI(...)` 整页切换 |
| 菜单选中动画：旋转 360° + X/Y 缩放 1→2→1，OvershootInterpolator，时长随主题 animationSpeed | `onSelect` 内 `AnimUtil.playRotation/playScaleX/playScaleY` | MainActivity.kt:324-330 | 中 | Compose 用 `animateFloatAsState`/keyframes 重建 |
| 返回键拦截 → `uiManager.onBackPressed()` | `onKeyDown(KEYCODE_BACK)` | MainActivity.kt:283-289 | 中 | 非 OnBackPressedDispatcher；Compose 改 `BackHandler` |
| 默认返回逻辑：主 UI 时 HOME + `exitProcess(0)`；否则选中 home 回主页 | `uiManager.registerDefaultBackEvent{}` | MainActivity.kt:215-225 | 中 | "回主页再退出杀进程"两级返回语义必须保留 |
| `back` 菜单项点击 → `uiManager.onBackPressed()` | `onClick` | MainActivity.kt:238, 394-396 | 低 | |
| `back` 长按 → 跳 ShellActivity（隐藏入口） | `back.setOnLongClickListener` | MainActivity.kt:239-242 | 低 | |
| `home` 长按 → 分享日志 | `home.setOnLongClickListener` → `shareLog()` | MainActivity.kt:234-237, 706-729 | 低 | FileProvider + ACTION_SEND chooser |
| `account` 点击 → 切 accountUI | `onClick` | MainActivity.kt:185, 384-388 | 低 | |
| `version` 点击 → 切 versionUI | `onClick` | MainActivity.kt:186, 389-393 | 低 | |
| `start`（启动游戏）点击：控制器未初始化时抖动动画；否则选驱动、刷新分辨率、`Versions.launch()` | `onClick` (view === start) | MainActivity.kt:188, 409-425 | 中 | 抖动 `AnimUtil.playTranslationX(start,700,0,50,-50,50,-50,0)` + Overshoot（412-414） |
| `start` 长按 → `RendererSelectDialog` 选渲染器后再启动 | `start.setOnLongClickListener` | MainActivity.kt:189-194 | 低 | mio 对话框 |
| `jar` 点击：首次弹 `showWarningDialog`（SP 记忆不再提示），否则 `JarExecutorHelper.start()` | `onClick` (view === jar) | MainActivity.kt:195, 397-408 | 低 | |
| `jar` 长按 → `EditDialog` 输入自定义参数执行 jar | `jar.setOnLongClickListener` | MainActivity.kt:196-211 | 低 | |
| `goSetting` 点击：全局设置→settingUI tab0；版本独立设置→manageUI tab0 | `onClick` (view === goSetting) | MainActivity.kt:187, 426-441 | 中 | 跨 UI TabLayout 联动（`runAfterInit` + `selectTab`），Compose 需跨页传初始 tab 参数 |
| 通知权限：首次 `FCLAlertDialog` 询问 → 跳系统通知设置或 `permissionResultLauncher.launch(POST_NOTIFICATIONS)` | `checkNotificationPermission`/`requestNotificationPermission` | MainActivity.kt:244-260, 731-763 | 中 | SP 只问一次；`registerForActivityResult` 在 277-279 |
| 动态背景视频：VideoView 循环播放，onPause 记录进度暂停、onResume 续播；音量读 SP | `setupLiveBackground`/`setLiveBackgroundVolume` | MainActivity.kt:296-320, 765-799 | 中 | Compose 需 AndroidView 包 VideoView 或 ExoPlayer |
| 外部整合包 intent：Toast + `downloadUI.pageManager.showTempPage(LocalModpackPage)`；`onSaveInstanceState` 存 `modpack_handled` | `handleModpack` | MainActivity.kt:261-263, 291-294, 801-825 | 中 | 跨 UI 临时页注入 |
| 入场动画组：leftMenu/rightMenu 平移 + start/version/jar 与 7 个菜单项 Y/X 双向平移，BounceInterpolator，逐项延迟 (index+1)×100ms | `playAnim` | MainActivity.kt:660-704 | 中 | 时长随 `theme.animationSpeed`；Compose 用 staggered `Animatable` |
| 主题联动：ThemeEngine.registerEvent 改 leftMenu 圆角背景；theme/theme2/theme2Dark 三个 IntegerProperty 绑定 → `updateColor()` | `initBackground`/`updateColor`/`createBackground` | MainActivity.kt:175-183, 582-658 | 中 | fakefx 绑定 → Miuix 主题 state |
| 账号区数据绑定：currentAccount invalidated → 头像/名称刷新（TexturesLoader） | `setupAccountDisplay`/`refreshAvatar` | MainActivity.kt:445-497 | 低 | |
| 版本区数据刷新：选中版本变化 → 协程解析 → 更新图标/名称/进度条 | `setupVersionDisplay`/`loadVersion` | MainActivity.kt:499-567 | 低 | |
| 新手引导浮层：延迟 1500ms，`GuideUtil.show` 指向 setting 和 home | `uiLayout.postDelayed` | MainActivity.kt:268-274 | 中 | Miuix 无对等组件，需自建或舍弃 |
| `refreshMenuView`：互斥清除其他菜单项选中态 | `refreshMenuView` | MainActivity.kt:374-380 | 低 | |
| 启动前刷新屏幕尺寸缓存 | `refreshScreenSize` | MainActivity.kt:827-830 | 低 | |

### 1.2 SplashActivity（268 行）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/SplashActivity.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 系统 SplashScreen API | `installSplashScreen()` | SplashActivity.kt:61 | 低 | |
| 用户协议对话框：`isAgree=false` 时 FCLAlertDialog（不可取消），同意→checkPermission，拒绝→finish | `onCreate` | SplashActivity.kt:69-83 | 低 | |
| 存储权限说明对话框：同意→requestPermission，拒绝→finish | `checkPermission` | SplashActivity.kt:86-99 | 低 | |
| 权限请求三分支：R+ 跳 `ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION`；R 以下 `requestPermissions`；已拒绝则 Toast + 跳应用详情页 | `requestPermission`/`hasPermission` | SplashActivity.kt:171-226 | 中 | `startActivityForResult{}` 为 FCLActivity 封装；Compose 换 `rememberLauncherForActivityResult` |
| Fragment 事务：首启→EulaFragment，否则→RuntimeFragment，带自定义进出动画 `R.anim.frag_start_anim/frag_stop_anim` | `start()` | SplashActivity.kt:116-126 | 中 | Compose 化后 Fragment 容器消失 |
| 进入主界面：`startActivity(MainActivity)` + `makeCustomAnimation(0,0)`（无转场）+ finish；`handleModpack` 把 VIEW intent 的 zip/mrpack/7z 拷入 cacheDir 并塞 extra | `enterLauncher`/`handleModpack` | SplashActivity.kt:129-144, 146-169 | 中 | 外部文件导入链路起点（配合 manifest activity-alias） |

### 1.3 JVMCrashActivity（176 行，独立 `:crash` 进程）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/JVMCrashActivity.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 重启按钮：CLEAR_TASK 回 SplashActivity → finish → killProcess → `exitProcess(10)` | `onClick`(restart) | JVMCrashActivity.kt:42, 83-94 | 中 | 杀进程语义须原样保留 |
| 关闭按钮：finish + 杀进程 | `onClick`(close) | JVMCrashActivity.kt:43, 95-99 | 低 | |
| 上传日志按钮：失败 `showErrorDialog` | `onClick`(upload) | JVMCrashActivity.kt:44, 100-110 | 低 | 拼接 hs_err 致命日志 |
| 分享按钮：拷临时 .log → FileProvider → ACTION_SEND | `onClick`(share) | JVMCrashActivity.kt:45, 111-138 | 低 | |
| FLAG_SECURE 防截屏（按 SP `allowScreenshots` 反向设置） | `onCreate` | JVMCrashActivity.kt:38-40 | 低 | |
| 检测到 Fabric 不兼容 mod → 自动 `showErrorDialog` | `init` | JVMCrashActivity.kt:66-72 | 低 | 正则提取 149-160 |
| 静态入口 `startCrashActivity(...)`：CLEAR_TASK+NEW_TASK | companion | JVMCrashActivity.kt:162-175 | 低 | 由游戏进程外部触发 |

### 1.4 WebActivity（55 行）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/WebActivity.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| WebView 加载 intent extra "url"，JS 开启、LOAD_NO_CACHE | `onCreate` | WebActivity.java:30-34 | 中 | Compose 需 AndroidView 嵌 WebView |
| WebViewClient：onPageStarted 显示 / onPageFinished 隐藏 ProgressBar | `WebViewTrackClient` | WebActivity.java:37-48 | 低 | |
| onDestroy 清 WebView 缓存 | `onDestroy` | WebActivity.java:50-54 | 低 | |

### 1.5 ControllerActivity（50 行，手柄/控制映射界面）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/ControllerActivity.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 无 XML 布局：代码 new FCLImageView 设主题背景，再 `addContentView(menu.getLayout())` 叠加 GameMenu（DrawerLayout） | `onCreate` | ControllerActivity.java:26-33 | 高 | 游戏内覆盖层菜单，与 control 包强耦合 |
| 返回键 = 直接 finish（屏蔽菜单回退） | `onBackPressed` | ControllerActivity.java:36-39 | 低 | |
| 音量键拦截：VOLUME_UP/DOWN 同时打开左右两个 Drawer | `dispatchKeyEvent` | ControllerActivity.java:41-49 | 高 | 音量键改作菜单唤出，Compose 需按键拦截层 |

### 1.6 ShellActivity（105 行，竖屏 Shell 终端）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/ShellActivity.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 输入框 TextWatcher：检测 `\n` 结尾即执行命令并清空；含 "clear" 清空日志窗 | `editText.addTextChangedListener` | ShellActivity.java:40-64 | 中 | 回车即提交，Compose 用 `onValueChange`/IME Action |
| 日志窗点击 → 输入框 requestFocus 弹键盘 | `logWindow.setOnClickListener` | ShellActivity.java:65-67 | 低 | |
| 日志窗设全吞 KeyListener 禁编辑 | `logWindow.setKeyListener` | ShellActivity.java:68-93 | 低 | |
| Shell 输出经 ShellUtil 回调 runOnUiThread 追加；onDestroy interrupt | `appendLog` | ShellActivity.java:38-39, 96-104 | 低 | manifest 声明 `adjustResize` |

### 1.7 JVMActivity（279 行，游戏/Jar 运行容器，最高风险）

文件：`FCL/src/main/java/com/tungsten/fcl/activity/JVMActivity.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| TextureView SurfaceTextureListener 全生命周期：available→计算宽高/写 GameOption/`fclBridge.execute`；sizeChanged→重推窗口；destroyed→`setSurfaceDestroyed`；updated 前 2 帧回调 `menu.onGraphicOutput` | 四个 surface 回调 | JVMActivity.java:72, 110-169 | 高 | Compose 必须 AndroidView 保留 TextureView，surface 时序不能变 |
| 软键盘顶起画面：OnGlobalLayoutListener 检测可视区域 < 2/3 屏 → textureView.translationY 上移；可被菜单设置禁用 | `addOnGlobalLayoutListener` | JVMActivity.java:86-100 | 高 | 自定义键盘避让（manifest 无 adjustResize） |
| 按键分发：菜单 input 先吃；返回键 → 发 ESC 给游戏（TouchCharInput 启用时除外）；音量键 → Drawer 开合，800ms 双击防抖 | `dispatchKeyEvent` | JVMActivity.java:204-240 | 高 | 返回键被映射成游戏内 ESC，手势/按键冲突核心 |
| 手柄/鼠标通用事件分发 | `dispatchGenericMotionEvent` | JVMActivity.java:243-250 | 高 | |
| FORCE_RESOLUTION：按比例缩放 TextureView 尺寸并 setX 居中 | `onCreate` | JVMActivity.java:73-81 | 高 | |
| `onBrowse` 回调 → `OpenFolderDialog`（游戏内打开目录浏览） | `onBrowse` | JVMActivity.java:103-107 | 低 | FCLBridge 静态回调注册（61） |
| 生命周期 → native 窗口属性：onPause/Resume/Start/Stop/onWindowFocusChanged 同步 GLFW 状态 | 各生命周期方法 | JVMActivity.java:171-201, 274-278 | 中 | |
| KEEP_SCREEN_ON；onPostResume/onConfigurationChanged 重推 surface 尺寸；onDestroy 通知 Terracotta | 各方法 | JVMActivity.java:85, 252-272 | 中 | |

### 1.8 EulaFragment（77 行）

文件：`FCL/src/main/java/com/tungsten/fcl/fragment/EulaFragment.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| next 按钮：SP 写 `isFirstLaunch=false`，回调 `SplashActivity.start()` 进 RuntimeFragment | `onClick` | EulaFragment.java:38-39, 65-76 | 低 | 与宿主 SplashActivity 强耦合（强转调用） |
| 子线程读 assets/eula.txt → runOnUiThread 隐藏进度条、填文本 | `loadEula` | EulaFragment.java:46-63 | 低 | |

### 1.9 RuntimeFragment（311 行）

文件：`FCL/src/main/java/com/tungsten/fcl/fragment/RuntimeFragment.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| install 按钮：先 `isJavaArchSupported` 校验 ABI，不支持则 `showErrorDialog`；支持则 `install()` | `onClick` | RuntimeFragment.kt:42, 260-298 | 低 | `installing` 标志防重入（94, 97） |
| 8 个运行时组件（lwjgl/cacio/cacio17/java8/17/21/25/jna）顺序安装：state 图标 ↔ progress 条 visibility 互换，协程装完刷新 | `install` | RuntimeFragment.kt:96-258 | 中 | 8 段高度重复模式，Compose 可用列表 state 收敛 |
| 安装失败 → FCLAlertDialog 错误弹窗并重置 installing | `showErrorDialog` | RuntimeFragment.kt:169, 189, 209, 229, 300-310 | 低 | java 系四项有失败弹窗，其余静默 runCatching |
| 全部就绪自动 `SplashActivity.enterLauncher()` | `check` | RuntimeFragment.kt:88-92 | 低 | 状态从 SplashActivity 字段直读，强耦合 |

---

## 2. 框架层（UIManager / PageManager / 任务 / 更新 / 主页）

### 2.1 UIManager.kt（顶层 UI 容器）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/UIManager.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 8 个子 UI 懒加载注册（account/version/manage/download/controller/multiplayer/setting + main） | 属性委托 `by lazy` | UIManager.kt:26-33 | 高 | 每个 UI 持有一个 XML 布局，全部 addView 到同一 FCLUILayout 容器 |
| 异步初始化 + 加载回调 | `init(listener)` → `mainUI.addLoadingCallback` | UIManager.kt:38-49 | 中 | 底层是 AsyncLayoutInflater（FCLBaseUI），首帧时序依赖回调 |
| UI 切换：旧 UI `onStop()`（播隐藏动画）→ 新 UI `onStart()`（播进入动画） | `switchUI(ui)` | UIManager.kt:51-71 | 高 | 全靠 View 可见性 + 动画；Compose 需 Navigation 重建 |
| 全局默认返回事件注册（静态） | `registerDefaultBackEvent` | UIManager.kt:73-75 | 中 | 实际逻辑在 MainActivity.kt:215-225 |
| 返回键分发到当前 UI | `onBackPressed()` | UIManager.kt:77-79 | 中 | |
| 生命周期广播 | `onPause`/`onResume` | UIManager.kt:81-91 | 低 | |

### 2.2 PageManager.java（UI 内页面栈抽象基类）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/PageManager.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 构造即初始化页面列表并切到默认页 | 构造器 | PageManager.java:24-34 | 高 | 子类：version/manage/download/setting/controller 五个 PageManager |
| 页面切换：停旧页（含其 temp page）→ 起新页（若挂着 temp page 则 `restart()` 而非 `onStart()`） | `switchPage(id)` | PageManager.java:69-96 | 高 | "切回时恢复 temp page"语义（86-88）是 Compose Navigation 重建时最易丢的行为 |
| 临时页入栈：停当前页/栈顶 temp page，新 temp page `onStart()` | `showTempPage(fclTempPage)` | PageManager.java:98-112 | 高 | 栈式覆盖层 |
| 返回判断 | `canReturn()` | PageManager.java:114-116 | 低 | |
| 栈顶 temp page 出栈并恢复下一层 | `dismissCurrentTempPage()` | PageManager.java:118-131 | 高 | 20+ 处调用点 |
| 按页批量清除 temp page | `dismissAllTempPagesCreatedByPage`/`dismissAllTempPages` | PageManager.java:133-149 | 中 | 整合包流程跨 UI 清栈 |
| 生命周期广播（含 temp page） | `onPause`/`onResume` | PageManager.java:151-167 | 低 | |

### 2.3 UIListener.java

纯接口 `onLoad()` 回调（`FCL/src/main/java/com/tungsten/fcl/ui/UIListener.java:3-5`），无交互。难点：低。

### 2.4 TaskListPane.java（任务进度列表）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/TaskListPane.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 继承 FCLAdapter（ListView 用）；`getView` 返回缓存 View 列表 | `getCount/getItem/getView` | TaskListPane.java:59-85 | 中 | 无 ViewHolder 复用、无 DiffUtil |
| 任务执行器事件监听 → 动态增删列表项 + `notifyDataSetChanged()` | `setExecutor` 内 TaskListener `onStart` | TaskListPane.java:87-100 | 中 | 每次事件 `Schedulers.androidUIThread()` 切线程 |
| 按任务类型改名（20+ 个 `instanceof` 分支）后按 stage 索引插入节点 | `TaskListener.onRunning` | TaskListPane.java:110-161 | 中 | Compose 用有序 state list |
| 任务完成/失败移除节点、更新 stage 图标 | `onFinished`/`onFailed` | TaskListPane.java:163-188 | 低 | |
| 阶段计数器更新（`n/total` 文案） | `onPropertiesUpdate` | TaskListPane.java:190-210, 284-299 | 低 | |
| Stage 节点：inflate、图标着色、begin/fail/succeed 换图标 | `StageNode` | TaskListPane.java:215-304 | 低 | 状态机：more_horiz→arrow_forward→done/close |
| 进度节点：fakefx 属性绑定进度条与消息 | `ProgressListNode.bind/unbind` | TaskListPane.java:306-341 | 中 | `bar.percentProgressProperty().bind(task.progressProperty())`（322）→ Compose 改 State 采集 |

### 2.5 TaskDialog.java（任务进度对话框，全应用复用）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/TaskDialog.java`（19 处触发点见 G5）

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 创建：布局 `dialog_task`、`setCancelable(false)` | 构造器 | TaskDialog.java:40-48 | 低 | |
| 取消按钮 → `executor.cancel()` + 取消回调 + dismiss | `onClick` | TaskDialog.java:52, 114-121 | 低 | `setCancel(null)` 时按钮置灰（108-112） |
| 下载速度事件订阅（全局事件总线 `FileDownloadTask.speedEvent`）→ 更新速度文本 | `speedEventHandler` + `registerWeak` | TaskDialog.java:54-71 | 中 | 弱引用注册；Compose 需在 DisposableEffect 里解注册 |
| 绑定执行器：autoClose 模式任务结束自动 dismiss | `setExecutor(executor, autoClose)` | TaskDialog.java:74-94 | 中 | |
| 标题属性暴露（fakefx StringProperty） | `titleProperty/setTitle` | TaskDialog.java:96-106 | 低 | |

### 2.6 MainUI.java（主页：公告 + 3D 皮肤预览）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/main/MainUI.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 控件绑定：公告容器/标题/内容/日期/隐藏按钮 | `findViewById` | MainUI.java:54-62 | 低 | |
| 公告背景随主题色着色 | `ThemeEngine.registerEvent` | MainUI.java:63 | 中 | |
| "隐藏公告"点击：重要公告先弹 FCLAlertDialog（ALERT、不可取消）确认后隐藏 | `onClick` | MainUI.java:64, 173-188 | 低 | |
| 公告网络拉取与展示（中文走 gitee，否则 GitHub raw） | `checkAnnouncement` | MainUI.java:122-138 | 低 | |
| 隐藏公告并写 SP 忽略标记 | `hideAnnouncement` | MainUI.java:140-145 | 低 | |
| 皮肤 3D 预览：SkinViewer（GL 渲染）创建与生命周期手动跟随 UI 可见性 | `onCreate/onStart/onPause/onResume/onStop` | MainUI.java:66-68, 73-113 | 高 | OpenGL 皮肤渲染器，Compose 需 AndroidView 托管；受 `isCloseSkinModel` 开关控制 |
| 当前账号皮肤纹理绑定（fakefx + TexturesLoader） | `setupSkinDisplay`/`refreshSkin` | MainUI.java:147-171 | 中 | 账号切换/皮肤修改触发纹理重载（AccountListItem.java:230 调 `refreshSkin`） |
| 启动按钮、左侧菜单不在此文件（在 MainActivity 绑定，见 1.1） | — | MainActivity.kt:322-425 | — | |

### 2.7 Announcement.java

纯数据模型 + 展示判定（`shouldDisplay` 版本区间/语言/忽略标记过滤、`hide` 写 `ignore_announcement`），`FCL/src/main/java/com/tungsten/fcl/ui/main/Announcement.java:101-128`。无交互。难点：低。

### 2.8 UpdateChecker.java（更新检查）

文件：`FCL/src/main/java/com/tungsten/fcl/upgrade/UpdateChecker.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 手动检查入口（带 beta + Toast） | `checkManually` | UpdateChecker.java:44-46 | 低 | 触发点：LauncherSettingPage.java:244-245（`isChecking()` 防重入） |
| 自动检查入口（无 Toast、忽略 beta 与被忽略版本） | `checkAuto` | UpdateChecker.java:48-50 | 低 | 触发时机：MainActivity UI 加载完成回调（MainActivity.kt:243） |
| 检查中/已最新 Toast | `check` 内 `Toast.makeText` | UpdateChecker.java:56, 72 | 低 | |
| 发现新版本 → UI 线程弹 UpdateDialog | `showUpdateDialog` | UpdateChecker.java:89-94 | 低 | 网络源按语言选 GitHub/gitee |
| "忽略此版本"持久化 | `isIgnore`/`setIgnore` | UpdateChecker.java:96-106 | 低 | SP `ignore_update` |

### 2.9 UpdateDialog.java（更新弹窗）

文件：`FCL/src/main/java/com/tungsten/fcl/upgrade/UpdateDialog.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 创建：`dialog_update`、`setCancelable(false)`；由 `UpdateChecker.showUpdateDialog` 弹出 | 构造器 | UpdateDialog.java:56-63 | 低 | |
| 四按钮点击（ignore/positive/negative/netdisk） | `init` | UpdateDialog.java:80-87 | 低 | |
| "立即更新"**长按** → 浏览器打开 GitHub releases | `positive.setOnLongClickListener` | UpdateDialog.java:89-92 | 低 | |
| 窗口高度自适应（双层 post 测量） | `checkHeight` | UpdateDialog.java:97-112 | 中 | Compose 用 `heightIn(max=)` 天然解决 |
| "忽略" → 写忽略标记 + dismiss | `onClick`(ignore) | UpdateDialog.java:116-119 | 低 | |
| "更新" → TaskDialog 下载 APK（按 ABI 选包）→ FileProvider + ACTION_VIEW 发起系统安装；失败 FCLAlertDialog（含网盘回退） | `onClick`(positive)/`getTargetArchUrl` | UpdateDialog.java:120-153, 163-175 | 中 | |
| "取消" → dismiss；"网盘" → `AndroidUtils.openLink` + dismiss | `onClick` | UpdateDialog.java:154-160 | 低 | |

### 2.10 RemoteVersion.java

纯数据模型（`isBeta`/`getDisplayType`/`getDisplayDescription`），`FCL/src/main/java/com/tungsten/fcl/upgrade/RemoteVersion.java:58-76`。无交互。难点：低。

---

## 3. 版本管理 UI（ui/version/）

### 3.1 VersionUI.java / VersionPageManager.java（容器与页面管理器）

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 返回键拦截：优先关 PageManager 临时页，否则冒泡父级 | `VersionUI.onBackPressed()` | `FCL/src/main/java/com/tungsten/fcl/ui/version/VersionUI.java:35` | 中 | Compose 用 BackHandler + 页面栈状态 |
| 生命周期透传 onPause/onResume 到 PageManager | `onPause()/onResume()` | VersionUI.java:44,52 | 低 | |
| 延迟初始化页面（container.post） | `onCreate()` | VersionUI.java:31 | 低 | |
| 页面注册/静态单例持有 | `VersionPageManager` 构造器/`getInstance()` | `FCL/src/main/java/com/tungsten/fcl/ui/version/VersionPageManager.java:28,21` | 中 | 单例被 ProfileListAdapter、AddProfileDialog 反向调用刷新，Compose 应改状态提升 |
| 页面初始化（仅 VersionListPage 一页） | `init()` | VersionPageManager.java:34 | 低 | |

### 3.2 VersionListPage.kt（版本列表主页，核心）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/version/VersionListPage.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 刷新按钮 → 重新扫描版本仓库（加载中按钮禁用） | `onClick` | VersionListPage.kt:45,235 | 低 | |
| 新建游戏目录按钮 → AddProfileDialog | `onClick` | VersionListPage.kt:46,238 | 低 | |
| 搜索框 TextWatcher 实时过滤（大小写不敏感） | `textWatcher` | VersionListPage.kt:49-64 | 中 | 加载时先 remove 再清空避免误触发（:127-128），恢复在 :217 |
| 分类 RadioGroup（全部/Fabric/Forge/NeoForge/其他）切换过滤 | `category.setOnCheckedChangeListener` | VersionListPage.kt:65-112 | 中 | 加载时强制 check(R.id.all)（:126） |
| RadioGroup 在 HorizontalScrollView + FCLAppBarLayout 中，AppBar scroll flags 与列表滚动联动收起 | 布局 `FCL/src/main/res/layout/page_version_list.xml:74-77, 128-133` | 同左 | 中 | Compose 用 TopAppBar scrollBehavior |
| 版本仓库变更监听自动重载列表 | `registerVersionsListener` | VersionListPage.kt:47 | 中 | fakefx 监听器 → Flow/State |
| 加载进度显隐、空列表隐藏 | `loadVersions` | VersionListPage.kt:130-131,213-216 | 低 | |
| 加载完成后自动滚动到当前选中版本 | `versionList.scrollToPosition` | VersionListPage.kt:218-221 | 低 | LazyListState.scrollToItem |
| 选中态双向绑定（fakefx BooleanProperty bind 到 profile.selectedVersionProperty） | `loadVersions` 尾部 | VersionListPage.kt:223-229 | 中 | |

### 3.3 VersionListAdapter.kt（版本列表 Adapter）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/version/VersionListAdapter.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 单选按钮点击 → 设为选中版本 | `onBindViewHolder`（radioButton.setOnClickListener） | VersionListAdapter.kt:54 | 低 | checkProperty 与 selectedProperty 双向绑定（:45-46） |
| 整行点击 → 设为选中版本 | `root.setOnClickListener` | VersionListAdapter.kt:64 | 低 | |
| 删除按钮 → Versions.deleteVersion 确认弹窗 | `delete.setOnClickListener` | VersionListAdapter.kt:57 | 低 | |
| 设置按钮（仅版本独立设置可见）→ 选中该版本并跳 Manage UI 第一个 Tab | `setting.setOnClickListener` | VersionListAdapter.kt:67-77 | 中 | 跨 UI 跳转 + Tab 联动 |
| 每项入场平移动画 playTranslationX（-100f→0f，时长=animationSpeed×30） | `onBindViewHolder` 尾部 | VersionListAdapter.kt:94-99 | 中 | onBind 即触发，复用时反复播放；Compose 用 LazyColumn item 动画 |
| 数据刷新：notifyDataSetChanged 全量 | `updateVersionList` | VersionListAdapter.kt:106-111 | 低 | |
| 副标题拼接 Mod 数量（同步 Files.list IO） | `onBindViewHolder` | VersionListAdapter.kt:81-93 | 低 | onBind 中磁盘 IO，重建时应移出 |

### 3.4 ProfileListAdapter.java（游戏目录 ListView 适配器）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/version/ProfileListAdapter.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 点击 Profile 项 → 切换选中目录；加载中禁止切换并播抖动动画 | `getView`（parent.setOnClickListener） | ProfileListAdapter.java:69-76 | 中 | 依赖 MainActivity.isVersionLoading() 全局状态 |
| 删除按钮：仅剩 1 个时抖动拒绝，否则直接删除并强刷 VersionListPage | `delete.setOnClickListener` | ProfileListAdapter.java:77-84 | 中 | **无确认弹窗直接删除**；经 PageManager 单例反向刷新 |
| 错误反馈抖动动画 playTranslationX(0→50→-50→50→-50→0, Overshoot, 700ms) | `playAnim` | ProfileListAdapter.java:88-92 | 中 | |
| 选中项背景切换、路径 TextView 跑马灯 | `getView` | ProfileListAdapter.java:65,68 | 低 | notifyDataSetChanged（:72） |

### 3.5 Versions.java（静态业务入口：启动/删除/重命名/复制/导入导出）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/version/Versions.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 启动游戏 launch()：版本校验失败弹 FCLAlertDialog，确认后跳下载页 | `launch`/`checkVersionForLaunching` | Versions.java:199-234 | 中 | 弹窗回调里操作 MainActivity 菜单 selected 态（:226-228） |
| 无账号时先弹 CreateAccountDialog，dismiss 回调决定是否继续启动 | `ensureSelectedAccount` | Versions.java:236-252 | 中 | 对话框 dismiss 串联异步流程，Compose 改挂起函数 |
| 删除版本：FCLAlertDialog 二次确认（区分隔离目录文案）+ ProgressDialog | `deleteVersion` | Versions.java:100-117 | 低 | |
| 重命名：RenameVersionDialog + 校验 + ProgressDialog + 完成后刷新选中新名 | `renameVersion` | Versions.java:119-145 | 中 | FutureCallback resolve/reject 三段式 |
| 复制版本：DuplicateVersionDialog + 校验 + 失败回滚删除残留 | `duplicateVersion` | Versions.java:152-174 | 中 | |
| 导入整合包：跳 ModpackSelectionPage 临时页（DownloadPageManager） | `importModpack` | Versions.java:51-57 | 中 | 跨模块临时页导航 |
| 下载整合包：TaskDialog + 失败 FCLAlertDialog + 取消 Toast | `downloadModpackImpl` | Versions.java:59-98 | 中 | |
| 导出版本/更新整合包：跳 ManagePageManager 临时页 | `exportVersion`/`updateVersion` | Versions.java:147-150,176-179 | 中 | |
| 更新游戏资源文件：TaskDialog + 可取消任务 | `updateGameAssets` | Versions.java:181-189 | 低 | |

### 3.6 版本区对话框

| 对话框 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| AddProfileDialog | 选择路径按钮 → fileLauncher 目录选择；确定校验（空名/空路径/重名 Toast）→ 新增 Profile 强刷页面；setCancelable(false) | `FCL/src/main/java/com/tungsten/fcl/ui/version/AddProfileDialog.java:46-64, 33` | 中 | 触发点：VersionListPage 新建目录按钮 |
| DuplicateVersionDialog | 确定三重校验（非空/不冲突/合法字符，失败 Toast）；提交时禁用按钮防重；"复制存档"CheckBox | `FCL/src/main/java/com/tungsten/fcl/ui/version/DuplicateVersionDialog.java:53-80, 39` | 中 | setCancelable(false) |
| RenameVersionDialog | 确定：禁用按钮 → FutureCallback 异步（resolve dismiss / reject Toast 恢复按钮）；预填旧名 | `FCL/src/main/java/com/tungsten/fcl/ui/version/RenameVersionDialog.java:41-57, 29` | 中 | 暴露 CompletableFuture 供串联（:60） |
| ModpackSelectionDialog | 仅 setContentView，**无交互、全工程无引用，死代码** | `FCL/src/main/java/com/tungsten/fcl/ui/version/ModpackSelectionDialog.java:12-16` | 低 | 实际走 ModpackSelectionPage；迁移可删 |
| VersionListItem | 纯数据类，无交互 | `FCL/src/main/java/com/tungsten/fcl/ui/version/VersionListItem.java:9` | — | 持 fakefx 选中态，改普通数据类 + 外部状态 |

---

## 4. 管理 UI（ui/manage/，版本内管理）

### 4.1 ManageUI.java（容器：Tab + 页面栈）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/manage/ManageUI.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| TabLayout 切换（5 个 PageId） | `onTabSelected` | ManageUI.java:125（绑定 :51） | 中 | Miuix TabRow 重建 |
| 返回键拦截（页面栈回退） | `onBackPressed()` | ManageUI.java:77 | 中 | |
| 版本被删除时自动跳回主页 | `onStart()` loadingCallback | ManageUI.java:59-73 | 中 | 回调里直接操作 `MainActivity.binding.home.setSelected(true)`，跨 UI 耦合 |
| EventBus 版本刷新事件 | `checkSelectedVersion()` | ManageUI.java:53,157 | 中 | `RefreshedVersionsEvent` weak listener |

### 4.2 ManagePage.kt（版本管理页：16 项菜单）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/manage/ManagePage.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 双列菜单项点击（上传日志/浏览目录×9/更新/重命名/复制/导出等 16 项） | `create()` 中 ManageItem lambda | ManagePage.kt:74-147 | 低 | 点击回调在 ManageItemAdapter |
| 版本更新不可用时抖动动画 | `AnimUtil.playTranslationX` + Overshoot | ManagePage.kt:111-112 | 中 | |
| 打开内置文件浏览器（FileBrowser Activity） | `onBrowse(String)` | ManagePage.kt:151-166 | 中 | FCLLibrary 自研 Activity，迁移需保留或替换 |
| 删除 libraries 确认对话框 + ProgressDialog | `clearLibraries()` | ManagePage.kt:172-195 | 低 | |
| 清理 logs 确认对话框 + ProgressDialog | `clearJunkFiles()` | ManagePage.kt:197-218 | 低 | |
| 上传日志错误提示（不存在/>5MB/读取失败三种） | `uploadLatestLog()` → `showErrorDialog` | ManagePage.kt:239-255 | 低 | |
| 重命名/复制/导出/更新版本委托 `Versions.*` | `rename()/duplicate()/export()/updateGame()` | ManagePage.kt:220-237 | 低 | 重命名后回写 preferredVersionName(:231) |

### 4.3 ManagePageManager.kt

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 页面切换延迟到版本加载完成（versionLoaded 前缓存 runnable） | `switchPage(id)` | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ManagePageManager.kt:84-90` | 高 | Compose 导航需复刻"等数据后跳默认页"语义 |
| 页面懒创建 + 注入版本数据 | `createPageById(id)` | ManagePageManager.kt:92-105 | 中 | |
| 全局单例 | `instance` | ManagePageManager.kt:19-20,63-65 | 中 | |
| 游戏目录隔离开关联动刷新 Mod/World 页 | `onRunDirectoryChange()` | ManagePageManager.kt:119-122 | 中 | 由 VersionSettingPage 属性监听触发 |

### 4.4 VersionSettingPage.kt（版本设置页，交互密度最高之一）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/manage/VersionSettingPage.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 12 个按钮统一点击分发（Java/图标/控制器/图形后端/渲染器/驱动/环境变量） | `onClick(View)`（绑定 :101-112） | VersionSettingPage.kt:438-569 | 中 | 单 OnClickListener if 链 |
| SeekBar 内存分配双向绑定 + 文本联动（3 个 StringBinding） | `barMemory.addProgressListener()` + `bindBidirectional` | VersionSettingPage.kt:131-169 | 中 | Compose Slider + derivedStateOf |
| 专用设置开关（全局↔实例）联动：specialize/globalize + 整页重绑 | `enableSpecificSettings` ChangeListener | VersionSettingPage.kt:179-187 | 中 | |
| 系统 Vulkan 驱动开关：Adreno GPU 弹 INFO 对话框；切 driverContainer 可见性 | `switchVulkanDriverSystem.setOnClickListener` | VersionSettingPage.kt:188-205 | 中 | |
| **长按** JVM/游戏参数 EditText → FullEditDialog 全屏编辑 | `OnLongClickListener` | VersionSettingPage.kt:206-215 | 中 | |
| 强制分辨率开关点击/长按都弹输入框（EditDialog 解析 "WxH"，onCancel 回滚开关） | `switchForceResolution` | VersionSettingPage.kt:216-248 | 中 | |
| 图标选择（SAF 单选 .png，doc Uri 拷贝中转） | `onExploreIcon()` | VersionSettingPage.kt:390-417 | 中 | |
| 删除图标 + 事件通知 | `onDeleteIcon()` | VersionSettingPage.kt:419-426 | 低 | |
| 选择控制器对话框（未初始化 Toast） | `SelectControllerDialog.show()` | VersionSettingPage.kt:445-461 | 低 | |
| 跳转控制器 UI 页 | `controller.setSelected(true)` + switchPage | VersionSettingPage.kt:463-470 | 中 | 跨 UI 跳转 |
| Java 管理对话框 | `JavaManageDialog(...).show()` | VersionSettingPage.kt:471-480 | 低 | |
| 安装 Java/渲染器/驱动：showItemSelectionDialog 选来源（Github/网盘）→ openLink | `showItemSelectionDialog` ×3 | VersionSettingPage.kt:482-495,528-557 | 低 | |
| 图形后端选择对话框（default/opengl/vulkan） | `showItemSelectionDialog` | VersionSettingPage.kt:496-506 | 低 | |
| 渲染器/驱动选择对话框 + 全局警告弹窗 | `RendererSelectDialog`/`DriverSelectDialog` + FCLAlertDialog | VersionSettingPage.kt:507-527 | 低 | |
| 环境变量全屏编辑对话框（FullEditDialog 存 SP） | `FullEditDialog(context, true)` | VersionSettingPage.kt:558-568 | 低 | |
| 属性解绑/重绑 17 组（loadVersion 切换版本） | `FXUtils.unbind*/bind*` | VersionSettingPage.kt:286-350 | 高 | fakefx 双向绑定贯穿整页，Compose 整体转 StateFlow |

### 4.5 ModListPage.java（Mod 列表：搜索 + 多选批量操作）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/manage/ModListPage.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 搜索框实时过滤（TextWatcher，支持 `regex:` 前缀） | `searchBar.addTextChangedListener` → `search()` | ModListPage.java:160-175,483-516 | 中 | 搜索时解绑 adapter 数据（Bindings.unbindContent :487），清空选中 |
| 8 按钮统一点击（add/checkUpdateAll/checkUpdate/refresh/delete/selectAll/selectInvert/cancel） | `onClick(View)` | ModListPage.java:146-153,178-212 | 中 | |
| 启用/禁用过滤 CheckBox 变更 → refresh() | `OnCheckedChangeListener` | ModListPage.java:154-158 | 低 | |
| 多选状态切换工具栏（normalGroup ↔ selectedGroup） | `switchLayout(boolean)` + adapter.selectedItemsProperty 监听 | ModListPage.java:119,279-287 | 中 | |
| 批量删除确认对话框 | deleteButton → FCLAlertDialog → `removeSelected` | ModListPage.java:194-202,384-394 | 低 | |
| 全选/反选/取消选择 | `adapter.selectAll()/selectInvert()/clear()` | ModListPage.java:203-211 | 低 | |
| 添加 Mod（SAF 多选 .jar/.zip/.litemod）+ 成功/失败汇总 FCLAlertDialog | `add()` → `launchMultiSelection` | ModListPage.java:322-382 | 中 | doc Uri 与 File 双路径 |
| 检查更新（TaskDialog + 取消）；整合包版本先弹 ALERT 警告 | `checkUpdates(boolean)` | ModListPage.java:396-458 | 中 | |
| 跳 ModUpdatesPage 临时页 | `showTempPage(page)` | ModListPage.java:436-437 | 中 | |
| 跳转下载 UI | `download()` 操作 MainActivity.binding + DownloadPageManager | ModListPage.java:460-464 | 中 | 跨 UI |
| 回滚 Mod + 失败 Toast | `rollback(from,to)` | ModListPage.java:466-473 | 低 | 由 adapter 中 ModRollbackDialog 回调触发 |
| 加载态切换（禁用全部按钮 + ProgressBar） | `setLoading(boolean)` | ModListPage.java:247-277 | 低 | |

### 4.6 LocalModListAdapter.kt（Mod 多选核心）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/manage/LocalModListAdapter.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 点击切换选中态（背景 tint 主题色↔浅色，`fromSelf` 防递归；直接改 tint 不 notifyItemChanged） | `parent.setOnClickListener` | LocalModListAdapter.kt:158-178 | 中 | |
| 启用/禁用开关（CheckBox 先清 listener 再绑定，防回收误触发） | `check.setOnCheckedChangeListener(null)` → `active.set(checked)` | LocalModListAdapter.kt:180-185 | 中 | |
| 全选/反选 | `selectAll()/selectInvert()` | LocalModListAdapter.kt:64-78 | 低 | |
| 数据刷新：listProperty/selectedItemsProperty 监听 → notifyDataSetChanged 全量 | — | LocalModListAdapter.kt:82-94 | 中 | 无 DiffUtil |
| 回滚按钮（有旧版本才显示）→ ModRollbackDialog | `restore.setOnClickListener` | LocalModListAdapter.kt:195-207 | 低 | |
| 信息按钮 → ModInfoDialog | `info.setOnClickListener` | LocalModListAdapter.kt:208-211 | 低 | |
| 跳转按钮（远程识别后显示）→ 切下载 UI ModDownloadPage | `jump.setOnClickListener` | LocalModListAdapter.kt:212-224 | 中 | 跨 UI + tabLayout.selectTab(2) |
| 异步识别远程 Mod（每 item 一个网络协程，jobs map + icon.tag 防复用串位） | `lifecycleScope.launch` + `jobs[position]` | LocalModListAdapter.kt:228-275 | 高 | Compose 需重写为按 key 的 produceState |

### 4.7 Mod 相关对话框与更新页

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ModInfoDialog | 网站按钮开外链 / 确定关闭；异步从 jar 读 logo | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModInfoDialog.java:89-96, 46, 60-78` | 低 | setCancelable(false) |
| ModUpdatesPage | 4 按钮（export/update/updateWithout/cancel）；批量更新（TaskDialog + 汇总对话框）；导出 CSV | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModUpdatesPage.java:74-111,113-189` | 中 | cancel → dismissCurrentTempPage(:109) |
| ModUpdateListAdapter | CheckBox 勾选是否更新（bindBidirectional + 复用解绑） | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModUpdateListAdapter.java:59-63` | 中 | ListView + FCLAdapter |
| ModOldVersionListAdapter | 点击旧版本项 → 关对话框并回调 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModOldVersionListAdapter.java:65-68` | 低 | 代码动态构建视图 |
| ModRollbackDialog | 取消关闭；setCancelable(false) | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModRollbackDialog.java:36-40, 24` | 低 | 由 LocalModListAdapter.kt:199 创建 |
| ModCheckUpdatesTask | 纯后台任务，无交互 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModCheckUpdatesTask.java` | — | |

### 4.8 世界管理（WorldListPage/Adapter/Item/InfoPage/ExportDialog）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| WorldListPage.kt | 3 按钮（add/refresh/fixPrivate 改 unix 权限后 Toast）；"显示全部版本"开关过滤；添加世界（SAF 选 .zip → INFO 对话框 → EditDialog 命名（协程挂起 :260-272）→ 安装）；协程加载 + 加载态 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldListPage.kt:73-128,130-258` | 中 | 链式文件导入交互 |
| WorldListAdapter.kt | 点击项 → 世界信息页；数据包/导出/删除按钮委托 WorldListItem；notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldListAdapter.kt:24-54` | 低 | |
| WorldListItem.java | 导出（SAF 选目录 → WorldExportDialog）；管理数据包（<1.13 弹提示，否则 showTempPage DatapackListPage）；showInfo() → showTempPage WorldInfoPage；删除确认对话框 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldListItem.java:57-103` | 中 | 数据载体承载导航逻辑（需解耦） |
| WorldInfoPage.java | 作弊/生成建筑 Switch 变更即保存 NBT；难度/游戏模式 Spinner（FXUtils.bindSelection + 监听保存）；生命/饱食/经验 EditText 文本监听即保存 + 非法输入 Toast | `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldInfoPage.java:131-287` | 中 | 即改即写盘；注意 239-287 校验条件为 `&&` 的疑似 bug（空且非数字才提示） |
| WorldExportDialog | 确定按钮可用性实时绑定（文件名空/非法/已存在）；确定导出（TaskDialog + 成功后 dismissAllTempPagesCreatedByPage :83） | `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldExportDialog.java:59-107` | 中 | |

### 4.9 数据包（DatapackListPage/Adapter）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| DatapackListPage | 5 按钮（delete/enable/disable/add/refresh）；批量删除确认；批量启用/禁用；添加（SAF 多选 .zip + INFO 对话框，裸 new Thread 安装 :170）；加载态 | `FCL/src/main/java/com/tungsten/fcl/ui/manage/DatapackListPage.java:74-211` | 中 | |
| DatapackListAdapter | 点击切换选中态（tint + fromSelf 防递归）；启用/禁用 CheckBox（bindBidirectional + 复用解绑）；双 ListProperty → post notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/ui/manage/DatapackListAdapter.java:42-107` | 中 | ListView + FCLAdapter |

### 4.10 InstallerListPage.java（加载器/库管理页，ui/manage）

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 离线安装按钮 → SAF 选 .jar → TaskDialog 安装（成功/失败 FCLAlertDialog） | `installOffline()/doInstallOffline()` | `FCL/src/main/java/com/tungsten/fcl/ui/manage/InstallerListPage.java:64,139-186` | 中 | |
| 各库项点击 → 弹版本选择临时页（已装版本先弹确认对话框） | `loadVersion` 中 `installerItem.action.set` | InstallerListPage.java:111-127 | 中 | showTempPage download.version.InstallerListPage |
| 库项删除动作（removeAction） | `removeAction.apply(libraryId)` | InstallerListPage.java:84-91,130-133 | 中 | 点击本体在 `FCL/src/main/java/com/tungsten/fcl/ui/InstallerItem.java`（通用组件） |
| 更换版本安装（TaskDialog + 成功 dismiss 临时页） | `finish(profile, remoteVersion)` | InstallerListPage.java:218-261 | 中 | |
| 动态构建视图列表（ScrollView + LinearLayoutCompat 手动 addView，非 RecyclerView） | `clear()/addView()` | InstallerListPage.java:188-216 | 中 | Compose 改 LazyColumn |

### 4.11 整合包导出向导（ModpackInfoPage/FileSelectionPage/TypeSelectionPage）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ModpackInfoPage | 10 组 EditText 双向绑定 + 按导出类型显隐字段；内存 SeekBar 双向绑定；authlib 服务器 Spinner；强制更新 Switch；选择导出路径（SAF 目录）；下一步多级校验 Toast ×5 → 跳 ModpackFileSelectionPage | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModpackInfoPage.java:129-268` | 中 | Options 驱动表单结构 |
| ModpackFileSelectionPage | 文件树构建（后台递归扫描 + 顾问建议勾选/隐藏 + **indeterminate 半选传播**，FCLCheckBoxTreeAdapter）；下一步收集白名单 → 按类型导出（TaskDialog + 结果对话框） | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModpackFileSelectionPage.java:86-208,336-340` | 高 | 三态勾选树组件，Compose/Miuix 无等价物，需自研 |
| ModpackTypeSelectionPage | 三类型卡点击（mcbbs/multimc/server）→ showTempPage ModpackInfoPage | `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModpackTypeSelectionPage.java:39-72` | 低 | |

### 4.12 adapter/item

- `adapter/ManageItemAdapter.kt:39-41`：菜单项点击回调（携带 item View 供动画用）。难点低。
- `item/ManageItem.kt:5`：纯数据类，无交互。
- 本区域**未发现**：SwipeRefreshLayout、ItemTouchHelper/拖拽排序、onTouchEvent、startActivityForResult（文件选择统一走 fileLauncher）。

---

## 5. 下载 UI（ui/download/）

> **重要事实**：本区域"分页加载"**不是滚动到底加载更多**，而是首/上/下/末四按钮 + 点击页码弹 EditDialog 跳页的按钮式分页。区域内**没有** SwipeRefreshLayout、ItemTouchHelper、拖拽排序、滑动删除、多选模式；唯一长按是截图查看大图。

### 5.1 DownloadUI.java / DownloadPageManager.kt（容器与页面管理器）

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| Tab 点击切换 6 个下载页（游戏/整合包/Mod/资源包/存档/光影） | `DownloadUI.onTabSelected` | `FCL/src/main/java/com/tungsten/fcl/ui/download/DownloadUI.java:106`（绑定 :39） | 中 | |
| 返回键拦截：有临时页先 dismiss | `DownloadUI.onBackPressed` | DownloadUI.java:49 | 中 | |
| Profile/版本切换监听，联动刷新全部下载页 | `DownloadUI.loadVersions` | DownloadUI.java:98 | 中 | `Profiles.registerVersionsListener` + selectedVersionProperty 监听 |
| 按 id 懒创建页面并压入页面栈（5 个下载页 `by lazy`） | `DownloadPageManager.createPageById` | `FCL/src/main/java/com/tungsten/fcl/ui/download/DownloadPageManager.kt:88, 36-66` | 高 | 创建时回调 `VersionLoadable.loadVersion` |
| 全局静态单例 `instance`（所有 TempPage 直接调 showTempPage/dismiss） | companion object | DownloadPageManager.kt:23-24 | 高 | 本区域迁移最大耦合点 |
| 版本切换向所有已建页面广播 | `loadVersion` | DownloadPageManager.kt:104 | 低 | |

### 5.2 common/DownloadPage.java（远程资源搜索页基类，核心）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/download/common/DownloadPage.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 搜索按钮点击 → 重置页码并搜索 | `onClick`(v==search) | DownloadPage.java:391（绑定 :255） | 低 | |
| 软键盘 IME_ACTION_SEARCH 触发搜索 | `nameEditText.setOnEditorActionListener` | DownloadPage.java:280 | 低 | |
| 分页四按钮（first/previous/next/last，带边界判断） | `onClick` | DownloadPage.java:395-410（绑定 :273-276） | 中 | **非滚动加载** |
| 点击页码文本 → EditDialog 输入页码跳页（解析失败静默） | `onClick`(v==page) | DownloadPage.java:414（绑定 :278） | 低 | |
| 搜索失败重试按钮 | `onClick`(v==retry) | DownloadPage.java:411（绑定 :277） | 低 | |
| 下载源 Spinner 双向绑定 + 切换刷新分类并重搜 | `create`/`refreshCategory` | DownloadPage.java:292-298,318,445 | 中 | FXUtils.bindSelection + downloadSource.addListener |
| 游戏版本/分类/排序 3 个 Spinner 绑定；分类是递归缩进树，异步拉取后重建 adapter 并重绑（先 unbind 再 bind，458-461） | `create`/`resolveCategory` | DownloadPage.java:300-332,438,458-461 | 中 | 时序敏感 |
| 页码属性监听联动页码文本 | pageOffset/pageCount addListener | DownloadPage.java:333-334 | 低 | |
| 搜索结果项点击 → 打开 RemoteModInfoPage 临时页 | RemoteModListAdapter 回调 | DownloadPage.java:183-186 | 中 | 搜索为异步 Task，先 cancel 旧 executor（162-164）；Mod 页有 modLoader 并行流过滤（168-176） |
| 下载确认：DownloadAddonDialog 改名 → TaskDialog 进度下载；成功/取消 Toast、失败 FCLAlertDialog | `download` | DownloadPage.java:339-377 | 中 | |
| 中文翻译搜索对话框入口 | `showTranslationDialog` | DownloadPage.java:466 | 低 | 回调回填搜索框并立即搜索 |
| 外部跳转某 Mod 页（自动切下载源） | `jumpToModPage` | DownloadPage.java:478 | 低 | 供其他 UI 调用 |

### 5.3 Mod/资源包/光影下载页与翻译对话框

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ModDownloadPage | 翻译按钮 → TranslationDialog（仅中文可见）；ModLoader Spinner（全部/Forge/NeoForge/Fabric/Quilt）→ 本地二次过滤搜索结果 | `FCL/src/main/java/com/tungsten/fcl/ui/download/ModDownloadPage.java:47, 67` | 中 | 原生 AdapterView.OnItemSelectedListener |
| ResourcePackDownloadPage / ShaderPackDownloadPage | 无独立交互，纯仓库配置（继承 DownloadPage 全部交互） | `FCL/src/main/java/com/tungsten/fcl/ui/download/ResourcePackDownloadPage.java:14-67`；`ShaderPackDownloadPage.java:14-67` | 低 | supportChinese=false 无翻译按钮 |
| TranslationDialog.kt | 输入框 TextWatcher → 协程后台搜索译名刷新列表；列表项点击 → 回调 + dismiss；取消按钮；notifyDataSetChanged 整表 | `FCL/src/main/java/com/tungsten/fcl/ui/download/TranslationDialog.kt:47-108` | 中 | 对话框宽固定 500dp（:35） |

### 5.4 搜索结果与详情/版本/下载页（common/）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| RemoteModListAdapter.kt | 项点击 → 回调开详情页；每项 bind 播 translationX 入场动画（-100f→0f，animationSpeed×30，复用重复播放）；init 块异步扫描本地已装 Mod（哈希匹配）打"[已安装]"前缀（后台写 bind 读，**有竞态**）；Glide 图标 + 跑马灯 | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/RemoteModListAdapter.kt:37-61, 87-128` | 高 | Compose 改为状态驱动 |
| RemoteModInfoPage.java | 版本搜索框实时过滤游戏版本列表（推荐版本置顶）；版本项点击 → RemoteModVersionPage 临时页（二级堆叠）；重试按钮；mcmod/官网按钮开浏览器；截图重试图标（**create() 漏绑 screenshotRetry 监听，疑似现存 bug**，:114-116 仅绑 retry/mcmod/website）；后台检测已安装加"[已安装]" | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/RemoteModInfoPage.java:114-157, 198, 312-325` | 中 | |
| ModGameVersionAdapter / ModVersionAdapter | 版本项点击回调；按压缩放 StateListAnimator（anim_scale）；每项 bind 播 translationX 入场动画 | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/ModGameVersionAdapter.java:62-69`；`ModVersionAdapter.java:63-73` | 中 | ListView + FCLAdapter |
| RemoteModVersionPage | 文件项点击：Mod 页 → RemoteModDownloadPage 临时页，其他页 → 直接下载；"另存为"：fileLauncher 目录选择 + TaskDialog；下载回调分发 | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/RemoteModVersionPage.java:50-74` | 中 | |
| RemoteModDownloadPage | 下载/另存为/取消按钮（cancel 调 UIManager.onBackPressed()）；**"返回"按钮连续调 3 次 onBackPressed 弹三层临时页**（:222-227）；依赖加载失败重试 + Toast；依赖项点击 → 再开一层 RemoteModInfoPage；ListView 手动 measure 算高度嵌在 ScrollView（getListViewHeight :141） | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/RemoteModDownloadPage.java:104, 132-141, 213-227` | 高 | 硬编码页面栈深度，Compose 需 popUpTo 语义；嵌套滚动需重新设计 |
| DependencyAdapter | 依赖项点击回调（纯代码构建视图） | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/DependencyAdapter.java:70` | 低 | |
| DownloadAddonDialog | 确定：文件名校验失败 Toast，通过回调 + dismiss；取消 | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/DownloadAddonDialog.java:34-48` | 低 | setCancelable(false)；触发点 DownloadPage.java:344 |
| RemoteModScreenshotAdapter.kt | 截图**长按** → FullImageDialog 全屏看大图（区域内唯一长按）；点击 → 重新加载（失败重试语义，error 占位为刷新图标） | `FCL/src/main/java/com/tungsten/fcl/ui/download/common/RemoteModScreenshotAdapter.kt:45-72` | 中 | |

### 5.5 整合包下载/安装向导（modpack/）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ModpackDownloadPage | "安装本地整合包" → Versions.importModpack；翻译按钮 → TranslationDialog；其余继承 DownloadPage | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackDownloadPage.java:87-97` | 低 | |
| ModpackSelectionPage（向导第 1 步） | "本地" → fileLauncher（.zip/.mrpack/.7z/.rar）→ SAF 拷贝缓存 → LocalModpackPage；"在线" → ModpackUrlDialog → 按 URL 是否 server-manifest.json 结尾走 GetTask/FileDownloadTask 两条 TaskDialog 路径；**完成后按 updateVersion 是否为 null 在 DownloadPageManager/ManagePageManager 两套管理器间 dismiss+show（双 PageManager 分支）**；失败 Toast ×4 | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackSelectionPage.java:58-145` | 高 | 双 PageManager 分支是向导流转最脆弱处 |
| ModpackPage（抽象基类） | 安装按钮 → 先弹 FCLAlertDialog 警告确认后 onInstall；简介按钮 → onDescribe | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackPage.java:54-84` | 低 | setCanceledOnTouchOutside(false) |
| LocalModpackPage（向导第 2 步） | onStart 异步解析 manifest：手工整合包 → 警告对话框（可退出页面）；解析失败 → 错误对话框并退出（回调内双 PageManager dismiss）；安装：名称三重校验 Toast ×3（区分"装为新版本"/"外部游戏"）；简介 → FCLAlertDialog 显示 HTML | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/LocalModpackPage.java:73-190` | 中 | |
| RemoteModpackPage | onStart manifest 转换失败 → 错误对话框并退出；安装：名称三重校验 Toast + 构建安装任务；简介 → HTML 对话框 | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/RemoteModpackPage.java:42-115` | 中 | 校验逻辑与 LocalModpackPage 重复 |
| ModpackInstaller | TaskDialog 进度 + onStop 结果分发：成功/各类异常分别弹 FCLAlertDialog（按钮回调均带双 PageManager dismiss 分支 ×4）；特殊：ModpackCompletionException 非 FileNotFound 时失败也提示"安装成功"（74-85） | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackInstaller.java:34-99, 130-168` | 中 | 无视图，负责对话框编排 |
| ModpackUrlDialog | 确定（非空才回调）/取消 | `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackUrlDialog.java:32-47` | 低 | 触发点 ModpackSelectionPage.java:91 |

### 5.6 游戏版本安装向导（version/）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| VersionInstallPage（向导第 1 步） | 4 个 CheckBox（Release/快照/旧版/愚人节）过滤（每次变更重建整个 adapter :107）；搜索框实时过滤（结果为空自动全勾 :125-128）；刷新/失败重刷 + 进度条三态；版本项点击 → VersionInstallInfoPage 临时页 | `FCL/src/main/java/com/tungsten/fcl/ui/download/version/VersionInstallPage.java:66-167` | 中 | |
| RemoteVersionListAdapter.kt | 版本项点击回调；wiki 图标 → 打开 Minecraft Wiki（大量版本号特例映射 :195-251）；save 图标 → 原生 AlertDialog 列表选镜像 URL 开浏览器；每项 bind 播 translationX 入场动画 | `FCL/src/main/java/com/tungsten/fcl/ui/download/version/RemoteVersionListAdapter.kt:54, 79-108, 195-251` | 中 | 两处复用（VersionInstallPage/InstallerListPage） |
| VersionInstallInfoPage（向导第 2 步） | 名称输入 TextWatcher：与自动生成名不一致即标记"手动修改"停止自动命名（与 refreshVersionName :152 联动）；各加载器 InstallerItem 点击 → InstallerListPage 临时页选版本（Fabric API 先弹警告）；已选加载器移除按钮；安装：名称三重校验 Toast → GameBuilder 异步 + TaskDialog → 成功/失败对话框；静态 `alertFailureMessage` 按异常类型映射弹窗（取消异常不弹），被 ModpackInstaller 跨类复用 | `FCL/src/main/java/com/tungsten/fcl/ui/download/version/VersionInstallInfoPage.java:84, 103-138, 193-255, 275-349` | 中 | Compose 需做成公共错误处理器 |
| InstallerListPage（向导第 3 步） | 3 个 CheckBox 过滤（无类型的库隐藏过滤栏；结果为空自动全勾）；刷新/失败重刷 + 三态进度（空列表 Toast :118；愚人节与搜索框 INVISIBLE :76-77）；版本项点击 → 回调并 dismiss 本页 | `FCL/src/main/java/com/tungsten/fcl/ui/download/version/InstallerListPage.java:56, 68-175` | 中 | 复用 RemoteVersionListAdapter |

---

## 6. 设置 UI（ui/setting/）

### 6.1 SettingUI.java / SettingPageManager.kt

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| Tab 点击切换设置子页（游戏/启动器/帮助/关于）；切回 tab0 额外调 `VersionSettingPage.loadVersion()` 刷新 | `onCreate` 注册 + `onTabSelected` | `FCL/src/main/java/com/tungsten/fcl/ui/setting/SettingUI.java:38, 96-114` | 中 | |
| 返回键拦截：先关临时页再退出 | `onBackPressed` | SettingUI.java:51-57 | 中 | |
| 每次 onStart 重新加载当前版本设置 | `onStart` | SettingUI.java:43-48 | 低 | 复用 manage 区 VersionSettingPage |
| 延迟初始化页面 | `container.post(this::initPages)` | SettingUI.java:39, 76-78 | 低 | |
| 按 ID 懒创建/切换子页面（launcher/help/about 三页 lazy） | `SettingPageManager.createPageById` | `FCL/src/main/java/com/tungsten/fcl/ui/setting/SettingPageManager.kt:32-45, 66-74` | 中 | 页面切换无转场动画，仅 onStop/onStart |
| 初始化即加载选中版本配置 | `init` | SettingPageManager.kt:48-58 | 低 | |
| 全局单例持有 | companion `instance` | SettingPageManager.kt:18-29 | 低 | |

### 6.2 LauncherSettingPage.java（启动器设置页，交互密度最高）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/setting/LauncherSettingPage.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 21 个按钮统一注册 OnClickListener | `onCreate` | LauncherSettingPage.java:85-105 | 低 | 单一 onClick 分发 |
| "检查更新" → 异步检查，失败 FCLAlertDialog | `onClick`(checkUpdate) | LauncherSettingPage.java:244-255 | 低 | isChecking() 防重入 |
| "清除缓存" → 直接删目录（**无任何反馈**） | `onClick`(clearCache) | LauncherSettingPage.java:256-258 | 低 | |
| "导出日志" → 后台写文件，成功/失败各弹 FCLAlertDialog | `onClick`(exportLog) | LauncherSettingPage.java:259-286 | 低 | |
| "申请录音权限" → 运行时权限请求或跳系统应用详情页 | `onClick`(requestAudioRecord) | LauncherSettingPage.java:287-302 | 中 | 依赖 MainActivity.permissionResultLauncher |
| 主题色 1/2/2Dark 选择：FCLColorPickerDialog，**拖动实时预览 + 确定保存 + 取消还原**三回调 | `onClick`(theme/theme2/theme2Dark) | LauncherSettingPage.java:303-359 | 中 | Miuix 无现成取色器，需自建 |
| 浅/深背景图选择：fileLauncher 单选（png/jpg/jpeg），SAF 拷贝后应用 | `onClick`(backgroundLt/backgroundDk) | LauncherSettingPage.java:360-373 | 中 | |
| 动态（视频）背景选择：单选 mp4，拷贝后调 `MainActivity.setupLiveBackground()` 即时生效 | `onClick`(backgroundLive) | LauncherSettingPage.java:374-390 | 中 | |
| 鼠标指针图选择（png/gif，先删旧再拷贝改名） | `onClick`(cursor) | LauncherSettingPage.java:391-414 | 中 | |
| 菜单图标选择（png/gif） | `onClick`(menuIcon) | LauncherSettingPage.java:415-438 | 中 | |
| 重置主题色 ×3 | `onClick`(resetTheme/resetTheme2/resetTheme2Dark) | LauncherSettingPage.java:439-447 | 低 | **resetTheme2Dark 未注册监听（97-105 行漏绑），死代码** |
| 重置动态背景：删文件 + 重设 | `onClick`(resetBackgroundLive) | LauncherSettingPage.java:448-454 | 低 | |
| "从背景取色"：Palette 提取主色应用主题 | `onClick`(fetchBackgroundColor/2/2Dark) | LauncherSettingPage.java:455-478 | 中 | **fetchBackgroundColor2Dark 未注册监听，死代码** |
| 重置浅/深背景图：子线程删文件，失败 Toast（全区域仅两处 Toast） | `onClick`(resetBackgroundLt/resetBackgroundDk) | LauncherSettingPage.java:479-494 | 低 | Toast 在 :482, :490 |
| 重置指针/菜单图标：删文件 | `onClick` + `deleteCursorFile/deleteMenuIconFile` | LauncherSettingPage.java:495-519 | 低 | |
| 语言 Spinner（9 种）：切换后弹"重启生效"对话框；`isFirst` 标志抑制首次回调 | `onCreate`(116-130) + `onItemSelected` | LauncherSettingPage.java:116-130, 522-537 | 中 | LocaleUtils.changeLanguage 直接改配置 |
| 主题模式 Spinner（跟随系统/浅色/深色）：`AppCompatDelegate.setDefaultNightMode` 触发 recreate | `onCreate`(132-140) + `onItemSelected` | LauncherSettingPage.java:132-140, 538-545 | 中 | Compose 需自建深色切换与持久化 |
| 普通开关 ×3：autoExitLauncher / disableFullscreenInput / allowScreenshots | `onCheckedChanged` | LauncherSettingPage.java:142-143,162-163,172-173,554-568 | 低 | 纯 SP 读写 |
| ignoreNotch 开关：改 Window flags（FLAG_LAYOUT_IN_SCREEN）即时生效 | `onCheckedChanged` | LauncherSettingPage.java:145-146, 556-558 | 中 | Compose 需经 Activity window |
| closeSkinModel 开关 | `onCheckedChanged` | LauncherSettingPage.java:148-149, 559-561 | 低 | |
| 视频背景音量 SeekBar：实时写 SP + 调 `MainActivity.setLiveBackgroundVolume()` | `onProgressChanged` | LauncherSettingPage.java:151-152, 572-577 | 低 | |
| 动画速度 SeekBar：fakefx 双向绑定 theme.animationSpeedProperty + 自动 saveTheme | `onCreate` | LauncherSettingPage.java:154-157 | 中 | |
| 震动时长 SeekBar：fakefx 属性监听写 SP | `onCreate` | LauncherSettingPage.java:159-161 | 低 | addProgressListener 为 FCLSeekBar 自定义 |
| 启动器名称 EditText：TextWatcher 实时保存 | `onCreate` | LauncherSettingPage.java:165-170 | 低 | |
| 下载源表单联动：checkAutoSource 开关 ↔ sourceAuto/source Spinner 可见性绑定 + 双向绑定 config | `onCreate` | LauncherSettingPage.java:175-198 | 中 | visibilityProperty().bind(checkProperty[.not()]) |
| 下载线程联动：checkAutoThreads 开关（勾选自动时强制重置线程数）+ threads SeekBar 双向绑定 | `onCreate` | LauncherSettingPage.java:199-209 | 中 | |
| 打开页面时静默清理超 3 天缓存 | `onCreate` | LauncherSettingPage.java:211-214 | 低 | 无 UI 反馈副作用 |

### 6.3 AboutPage / HelpPage / 文档 Adapter

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| AboutPage | 6 条目点击（官网/GitHub/Discord/QQ 群(mqqopensdkapi scheme)/爱发电/源码库）→ openLink 或隐式 Intent | `FCL/src/main/java/com/tungsten/fcl/ui/setting/AboutPage.java:37-86` | 低 | joinQQGroup try/catch 静默 |
| HelpPage | retry/refresh/website 三按钮；异步拉取文档索引 + 三态加载态；分类选中属性监听 → 按语言过滤重建 ArticleAdapter（每次 new Adapter 非差量） | `FCL/src/main/java/com/tungsten/fcl/ui/setting/HelpPage.java:48-117` | 中 | |
| ArticleAdapter | 列表项点击打开文档网页（URL 拼 `?path=`） | `FCL/src/main/java/com/tungsten/fcl/ui/setting/ArticleAdapter.java:57-59` | 低 | FCLAdapter(BaseAdapter) |
| DocCategoryAdapter | 分类单选：点击写 selectedIndexProperty，属性监听触发 notifyDataSetChanged 全量刷新重绘背景；默认选中第 0 项 | `FCL/src/main/java/com/tungsten/fcl/ui/setting/DocCategoryAdapter.java:36-40, 78-79` | 中 | |
| DocIndex | 纯数据/无交互 | `FCL/src/main/java/com/tungsten/fcl/ui/setting/DocIndex.java:1-110` | 低 | |

---

## 7. 控制器 UI（ui/controller/）

### 7.1 ControllerUI / ControllerPageManager

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 返回键分发：先关临时页 → Repo 页退回管理页 → 否则默认（三级返回栈） | `ControllerUI.onBackPressed()` | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerUI.java:41-49` | 中 | |
| 生命周期转发 PageManager；延迟初始化 + runAfterInit 回调队列 | `onPause/onResume/onCreate/runAfterInit` | ControllerUI.java:29-33,52-73,95-100 | 低 | |
| 页面切换/临时页栈（继承 PageManager）；Repo 页 lazy + createPageById 动态加入 | `ControllerPageManager` | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerPageManager.kt:10-15, 54-63` | 中 | 页面 ID 15040/15041 |
| 全局单例 | companion object | ControllerPageManager.kt:20-22 | 低 | |

### 7.2 ControllerManagePage.java（控制器管理页）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerManagePage.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 导入控制器（fileLauncher 选 .json → 解析/Toast/showErrorDialog） | `onClick` importController | ControllerManagePage.java:201-223 | 中 | SAF Uri copyFileToDir（:207-209） |
| 新建控制器 → ControllerInfoDialog | `onClick` createController | ControllerManagePage.java:224-227 | 低 | |
| 跳转仓库页（switchPage） | `onClick` downloadController | ControllerManagePage.java:228-230 | 低 | |
| 上传 → showTempPage ControllerUploadPage | `onClick` upload | ControllerManagePage.java:231-234 | 低 | |
| 分享 → showItemSelectionDialog 选直分享/ZL2 → ProgressDialog + 异步转换 → 系统分享面板（ZL2 不支持 Toast :278） | `onClick` share + `shareDirect/shareAsZl2/shareFile` | ControllerManagePage.java:235-251, 268-318 | 中 | FileProvider + ACTION_SEND |
| 编辑信息（ControllerInfoDialog 编辑模式） | `onClick` editInfo | ControllerManagePage.java:252-255 | 低 | |
| 进入布局编辑器（Intent → ControllerActivity，携带 controller id） | `onClick` editController | ControllerManagePage.java:256-262 | 低 | |
| 选中控制器属性绑定（infoLayout 可见性 + 4 TextView fakefx 绑定） | `init` | ControllerManagePage.java:125-135 | 中 | |
| 列表刷新（重建 EditableControllerListAdapter 整表替换） | `refreshList` | ControllerManagePage.java:152-155 | 低 | |

### 7.3 ControllerRepoPage.java（控制器仓库页）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerRepoPage.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 4 个 Spinner 筛选（下载源/语言/分类/设备）；source Spinner 切换触发 refreshCategory；category 双向绑定 | `onCreate/onItemSelected` | ControllerRepoPage.java:317-346, 378-380, 189-197 | 中 | 初始化自动 search()（:353） |
| 搜索/重试按钮 → 异步网络搜索（支持 `regex:` 前缀 :166）→ 加载/失败态 UI 切换 | `onClick` + `search(...)` | ControllerRepoPage.java:368-375, 116-158, 91-114 | 中 | |
| 检查更新按钮 → 逐个 FCLAlertDialog 提示可更新项 → 确认后下载（Toast ×3） | `checkUpdate` | ControllerRepoPage.java:202-253 | 中 | |
| 下载文件（TaskDialog 进度+取消、失败回滚旧控制器、Toast/AlertDialog） | `downloadFile` | ControllerRepoPage.java:255-303 | 中 | |
| 主题引擎监听（searchLayout 背景 tint） | `onCreate` | ControllerRepoPage.java:308-309 | 低 | |
| 进入页面即自动静默检查更新 | `onCreate` | ControllerRepoPage.java:354 | 低 | |

### 7.4 ControllerDownloadPage / UploadPage / UploadDialog / InfoDialog

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ControllerDownloadPage | 重试按钮；历史版本 → OldVersionDialog（空历史 Toast）；下载最新版（已存在时 FCLAlertDialog 确认覆盖）；下载任务（TaskDialog/取消/失败回滚）；加载/失败态 + Glide 图标 + 横向截图 ListView（listView.post 内 setAdapter :127-130） | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerDownloadPage.java:79-271` | 中 | |
| ControllerUploadPage | 加入 QQ 群（mqqopensdkapi scheme）；分享 → ControllerUploadDialog 收集元数据；打包 zip 异步 + ProgressDialog + 系统分享 | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerUploadPage.java:79-142` | 中 | |
| ControllerUploadDialog | 语言 Spinner（switch 只覆盖 case 5，de/uk 缺失，疑似 bug 保持原样）；3 设备 CheckBox 维护 devices 列表；图标单选 .png；截图多选（≤16 张动态 addView，超限 Toast）；截图条目内删除按钮；分享表单校验 Toast；setCancelable(false) | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerUploadDialog.java:58-256` | 中 | |
| ControllerInfoDialog | "更多信息" CheckBox → 动态改窗口高度 + 展开 moreInfoLayout；versionCode 数字输入过滤；确认：名称校验 Toast（作者变更重新生成随机 id :99-101）；setCancelable(false)；固定宽 400dp | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerInfoDialog.java:46-115` | 中 | window.setLayout 动态尺寸 |

### 7.5 控制器列表 Adapter 与版本对话框

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ControllerListAdapter.kt | item 点击回调进详情页；onBind 播 translationX 入场动画（-100f→0f，animationSpeed） | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerListAdapter.kt:44-68` | 中 | 无 DiffUtil |
| EditableControllerListAdapter.java | item 点击切换选中态（换背景 + notifyDataSetChanged，选中态反查 ManagePage :62）；删除按钮 → FCLAlertDialog 确认 → removeController；文本 fakefx 绑定 | `FCL/src/main/java/com/tungsten/fcl/ui/controller/EditableControllerListAdapter.java:62-77` | 中 | ListView + FCLAdapter |
| HistoricalListAdapter | item 点击回调选择版本（代码动态构建视图） | `FCL/src/main/java/com/tungsten/fcl/ui/controller/HistoricalListAdapter.java:50-65` | 低 | |
| ControllerScreenshotAdapter | 纯图片列表（Glide），无交互 | `FCL/src/main/java/com/tungsten/fcl/ui/controller/ControllerScreenshotAdapter.java:34-47` | 低 | |
| OldVersionDialog | ListView 选历史版本 → 回调下载 + dismiss；取消；setCancelable(false) | `FCL/src/main/java/com/tungsten/fcl/ui/controller/OldVersionDialog.java:27-41` | 低 | |
| SelectControllerDialog（control 包） | 单选控制器列表 + 确定回调；控制器列表失效自动回退选中项 | `FCL/src/main/java/com/tungsten/fcl/control/SelectControllerDialog.java:26-46, 84-90` | 中 | 触发点 VersionSettingPage.kt:447 |
| SelectableControllerListAdapter | RadioButton 单选 + 三属性绑定 + notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/control/SelectableControllerListAdapter.java:44-67` | 低 | |

---

## 8. 账号 UI（ui/account/）

### 8.1 AccountUI.java（账号主页）

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 添加离线账号（CreateAccountDialog FACTORY_OFFLINE） | `onClick` | `FCL/src/main/java/com/tungsten/fcl/ui/account/AccountUI.java:77-80` | 低 | |
| 添加微软账号（FACTORY_MICROSOFT） | `onClick` | AccountUI.java:81-84 | 低 | |
| 添加外置登录服务器（AddAuthlibInjectorServerDialog） | `onClick` | AccountUI.java:85-88 | 低 | |
| onStart 加载回调 → 异步 refresh 账号列表（RecyclerView LinearLayoutManager） | `onStart/refresh` | AccountUI.java:52-73 | 低 | |

### 8.2 AccountListAdapter.kt（账号列表）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/account/AccountListAdapter.kt`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 单选 radio 点击切换当前账号 → 全表 refresh | `radio.setOnClickListener` | AccountListAdapter.kt:63-66 | 中 | 选中态 = Accounts.getSelectedAccount()（:48） |
| 刷新凭据按钮 → 进度圈切换 → refreshAsync → 失败 FCLAlertDialog | `refresh.setOnClickListener` | AccountListAdapter.kt:67-87 | 中 | |
| 皮肤按钮点击：按账号类型三分支（AuthlibInjector 后台线程+进度；Offline 弹 OfflineAccountSkinDialog；其他 FileBrowser 选图上传），含阻塞 `.get()`（:95, :122） | `skin.setOnClickListener` | AccountListAdapter.kt:88-142 | 高 | Compose 需改协程 |
| 皮肤按钮**长按**：仅离线账号选本地 .png 皮肤（点击/长按共存同一按钮） | `skin.setOnLongClickListener` | AccountListAdapter.kt:184-198 | 中 | |
| 复制 UUID → 剪贴板 + Toast | `copyUuid.setOnClickListener` | AccountListAdapter.kt:143-146 | 低 | |
| 编辑按钮（仅离线）→ EditDialog 改 UUID → 校验 Toast → 替换账号 | `edit.setOnClickListener` | AccountListAdapter.kt:147-167 | 中 | |
| 删除按钮 → FCLAlertDialog 确认 → remove + refresh | `delete.setOnClickListener` | AccountListAdapter.kt:168-183 | 低 | |
| 数据刷新：clear+addAll+notifyDataSetChanged | `refresh` | AccountListAdapter.kt:205-210 | 低 | |
| 头像/名称/类型 fakefx 属性绑定（unbind 再 bind） | `onBindViewHolder` | AccountListAdapter.kt:49-56 | 中 | |

### 8.3 AccountListItem.java（含阻塞式登录/换肤）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/account/AccountListItem.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 上传皮肤三分支；Yggdrasil 走 FileBrowser 文件选择（**CountDownLatch 阻塞等待** :173, 215-219）；失败 FCLAlertDialog | `uploadSkin` | AccountListItem.java:147-222 | 高 | 后台线程 latch.await 等 UI，Compose 必须改挂起函数 |
| 重新登录：凭据过期弹 Classic/OAuthAccountLoginDialog（**CountDownLatch 同步等待对话框结果**） | `logIn` | AccountListItem.java:233-260 | 高 | |
| 刷新头像/皮肤绑定并通知 MainActivity/MainUI | `refreshSkinBinding` | AccountListItem.java:224-231 | 中 | 跨 UI 联动刷新 |

### 8.4 CreateAccountDialog.java（创建账号，579 行，最复杂对话框之一）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/account/CreateAccountDialog.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| TabLayout 切换登录方式（离线/微软/外置），onTabSelected 动态替换 details 视图（removeAllViews+addView） | `onTabSelected/initDetails` | CreateAccountDialog.java:249-260, 145-160 | 中 | |
| 登录按钮 → 表单校验 Toast → 异步创建 → 成功刷新 dismiss / 失败 FCLAlertDialog；离线用户名非法先弹确认对话框（:213-224） | `onClick/login` | CreateAccountDialog.java:239-242, 162-228 | 中 | |
| 登录按钮**长按** → 改用外部浏览器走 OAuth | `login.setOnLongClickListener` | CreateAccountDialog.java:98-104 | 中 | 长按改变行为 |
| 取消按钮 → 取消登录任务（OAuth.IS_CANCELED）+ dismiss | `onCancel` | CreateAccountDialog.java:230-236, 243-245 | 低 | |
| 微软设备码流程：onGrantDeviceCode 自动复制用户码、onOpenBrowser 打开内置 WebView/外部浏览器（**OAUTH_CALLBACK 事件总线 + WeakListenerHolder**） | `MicrosoftDetails` 构造器 | CreateAccountDialog.java:329-347 | 高 | Compose 需事件流订阅 |
| 外置服务器 home/register 按钮打开链接（按 links 动态显隐） | `ExternalDetails.refreshAuthenticateServer` | CreateAccountDialog.java:408-433 | 低 | |
| 角色选择子对话框 DialogCharacterSelector：ListView 点选角色（**CountDownLatch 阻塞返回**）、取消按钮 | `DialogCharacterSelector.select/onClick` | CreateAccountDialog.java:466-577 | 高 | 对话框内嵌套对话框 |
| setCancelable(false)；静态 instance 单例 | 构造器 | CreateAccountDialog.java:86-90 | 低 | |

### 8.5 其他账号对话框

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ClassicAccountLoginDialog | 空壳构造器，无交互 | `FCL/src/main/java/com/tungsten/fcl/ui/account/ClassicAccountLoginDialog.java:13-17` | 低 | |
| OAuthAccountLoginDialog | 登录点击 → 异步 logInWhenCredentialsExpired → 成功回调/失败 AlertDialog；**长按登录 = 外部浏览器**；取消 = failed 回调；设备码自动复制 + 开浏览器 | `FCL/src/main/java/com/tungsten/fcl/ui/account/OAuthAccountLoginDialog.java:51-105` | 高 | 同款 OAuth 事件流 |
| AddAuthlibInjectorServerDialog | 两步向导（输入 URL → next 异步解析 → 第二步确认添加）；back 返回第一步；双取消按钮；失败 Toast | `FCL/src/main/java/com/tungsten/fcl/ui/account/AddAuthlibInjectorServerDialog.java:63-124` | 中 | firstLayout/secondLayout 显隐切换 |
| ServerListAdapter | item 点击 → CreateAccountDialog（绑定该服务器）；删除按钮 → FCLAlertDialog 确认；ObservableList 变更自动 notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/ui/account/ServerListAdapter.java:33, 72-83` | 中 | |
| OfflineAccountSkinDialog.kt | 5 皮肤类型 Radio 切换 + 布局显隐；皮肤/披风路径按钮 → fileLauncher；**3D 皮肤预览（SkinRenderer GL 视图，show/dismiss 联动 onResume/onPause）**；确认保存；属性变更监听自动 refreshSkin（isFirst 去抖 :90-93） | `FCL/src/main/java/com/tungsten/fcl/ui/account/OfflineAccountSkinDialog.kt:44-53, 88-122, 160-226` | 高 | GL 预览嵌入对话框，Compose 需 AndroidView 包裹 |

---

## 9. 联机 UI（ui/multiplayer/）

文件：`FCL/src/main/java/com/tungsten/fcl/ui/multiplayer/MultiplayerUI.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 3 个 Tab 式按钮切换 mainLayout/hostLayout/guestLayout 三个 ScrollView 显隐 | `onClick` btnMain/btnHost/btnGuest | MultiplayerUI.java:97-112 | 中 | 手动 Tab 显隐 |
| 反馈按钮（已 GONE :70，Todo 待删）：带版本参数打开链接 | `onClick` btnFeedback | MultiplayerUI.java:113-130 | 低 | |
| EasyTier 按钮打开外部链接 | `onClick` btnEasytier | MultiplayerUI.java:131-133 | 低 | |
| 导出日志 → FileProvider 系统分享；无日志 Toast | `onClick` shareLog | MultiplayerUI.java:135-148 | 低 | |
| 总开关 Switch：写 SP、请求通知权限、显隐 extraLayout；未读须知版本时先弹 FCLAlertDialog 确认再**反向 setChecked 置回开关** | `onCheckedChanged` | MultiplayerUI.java:152-179 | 中 | 对话框回调反向改开关（:172, :176），注意 Compose 重组循环 |

---

## 10. mio 包 UI（com/mio/ui/ + com/mio/util/）

### 10.1 Adapter

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| GamepadMapItemAdapter | 每行 remap 按钮点击 → SelectKeycodeDialog 改键映射 | `FCL/src/main/java/com/mio/ui/adapter/GamepadMapItemAdapter.kt:69-72` | 低 | 固定 16 个手柄键（refresh :22-52） |
| ManageJavaItemAdapter | delete 按钮（内置 Java 隐藏）→ action(data,true)；root 点击 → action(data,false) 选中 | `FCL/src/main/java/com/mio/ui/adapter/ManageJavaItemAdapter.kt:39-50` | 低 | |
| ViewHolder | 通用包装，无交互 | `FCL/src/main/java/com/mio/ui/adapter/ViewHolder.kt:5` | 低 | |

### 10.2 Dialog

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| DriverSelectDialog | ListView item 点击选驱动 → 写 versionSetting + Plugin.selected → 回调 dismiss；cancel；按屏幕比例定窗口高 | `FCL/src/main/java/com/mio/ui/dialog/DriverSelectDialog.kt:24-56` | 低 | |
| GamepadMapDialog | RecyclerView + confirm 保存映射 dismiss / cancel | `FCL/src/main/java/com/mio/ui/dialog/GamepadMapDialog.kt:13-26` | 低 | |
| JavaManageDialog | 删除 Java（FCLAlertDialog 确认）；item 点击选中；cancel（isLoading 门控 :64-71）；autoSelect 按钮；importJava 文件选择器选 .tar.xz → 校验/覆盖确认 → 异步解压 + ELF 校验 → 结果对话框 | `FCL/src/main/java/com/mio/ui/dialog/JavaManageDialog.kt:42-202` | 中 | 多级确认对话框链 |
| RendererSelectDialog | item 点击选渲染器 → 写配置 → 回调 dismiss；refresh 按钮重扫列表换 adapter；cancel | `FCL/src/main/java/com/mio/ui/dialog/RendererSelectDialog.kt:21-64` | 低 | |

### 10.3 View / Widget

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| DraggableTextView | **onTouchEvent 自处理 DOWN/MOVE/UP 拖拽**（rawX/rawY 增量、父布局边界 clamp），MOVE 中实时写 SP，UP/CANCEL 触发 performClick；位置持久化 + initPosition/resetPosition；setText 时非拖拽中重新归位 | `FCL/src/main/java/com/mio/ui/view/DraggableTextView.kt:34-92` | 高 | 游戏内悬浮字（FPS/内存，res/layout/view_game_menu.xml:27,38），消费全部触摸；Compose 需 pointerInput + detectDragGestures 重写 |
| CursorView | 重写 setX/setY/getX/getY 叠加 dp 偏移的光标 ImageView，无触摸处理 | `FCL/src/main/java/com/mio/ui/view/CursorView.kt:24-38` | 中 | 游戏内虚拟光标，坐标系 hack |
| FCLAppBarLayout | 主题色属性绑定自动 tint 背景（fakefx IntegerProperty invalidated）；无触摸交互 | `FCL/src/main/java/com/mio/ui/widget/FCLAppBarLayout.kt:21-53` | 低 | |

### 10.4 AnimUtil.kt（动画工具，逐方法盘点）

文件：`FCL/src/main/java/com/mio/util/AnimUtil.kt` —— 全部为 ObjectAnimator 工厂，无 AnimatorSet/共享元素/转场。

| 方法 | 位置 | 用途 |
|---|---|---|
| `playTranslationY(view/list, duration, vararg values)` | AnimUtil.kt:11-32 | 单/批量 translationY |
| `playTranslationX(view/list, ...)` | AnimUtil.kt:35-56 | 单/批量 translationX（列表入场动画、抖动动画主用） |
| `playTranslationZ(view/list, ...)` | AnimUtil.kt:59-80 | translationZ |
| `playRotation(view, ...)` | AnimUtil.kt:83-91 | rotation（菜单选中动画） |
| `playScaleX/Y(view/list, ...)` | AnimUtil.kt:94-139 | scaleX/scaleY |
| `playAlpha(view, ...)` | AnimUtil.kt:142-150 | alpha |
| `ObjectAnimator.delay/interpolator/startAfter` 扩展 | AnimUtil.kt:153-168 | startDelay/插值器/延迟启动 |

Compose 替代：`Animatable`/`animate*AsState`/LazyColumn item 动画。难点低。
（另：`GuideUtil.kt` 为新手引导浮层工具，Miuix 无对等组件；`LauncherUtil.kt` 为启动辅助，无直接 UI 交互。）

---

## 11. 游戏内控制覆盖层（control/ + mio/touchcontroller/）—— 迁移难度最高区域

> 本区域全部用裸 `onTouchEvent` + 时间/位移阈值（100ms/10px/400ms）手写手势识别，与指针捕获、陀螺仪、TouchController 代理互相联动；以 DrawerLayout + 绝对坐标（setX/setY 千分比定位）叠加在游戏画面上。**建议最后迁移，且输入桥部分（TouchControllerInputView/TouchCharInput/FCLInput/TextureView）必须保留 View 体系（AndroidView 互操作）。**

### 11.1 GameMenu.java（游戏内菜单总控，1060 行，DrawerLayout 左右双抽屉）

文件：`FCL/src/main/java/com/tungsten/fcl/control/GameMenu.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 左抽屉 4 个 FCLSwitch 双向绑定（编辑模式/显示边界/隐藏全部/自动对齐） | `initLeftMenu` FXUtils.bindBoolean | GameMenu.java:315-336 | 中 | |
| 自动对齐距离 SeekBar 双向绑定 | `initLeftMenu` autoFitDist | GameMenu.java:338-339 | 低 | |
| 当前控制器 Spinner（联动刷新 ViewGroup Spinner） | `initLeftMenu` + controllerProperty.addListener | GameMenu.java:341-353 | 中 | |
| ViewGroup Spinner（onItemSelected 内重建样式缓存） | `refreshViewGroupList` | GameMenu.java:370-406 | 中 | |
| 隐藏全部控件开关 → Toast | hideAllViewsProperty.addListener | GameMenu.java:355-359 | 低 | |
| 编辑布局可见性绑定 editModeProperty | `editLayout.visibilityProperty().bind` | GameMenu.java:361 | 低 | |
| 左抽屉 5 按钮（管理分组/添加按钮/添加方向键/管理按钮样式/管理方向键样式）→ 各弹对话框；无分组时 Toast ×2 | `initLeftMenu` → `onClick` | GameMenu.java:363-367, 917-964 | 低 | ViewGroupDialog/EditViewDialog×2/ButtonStyleDialog/DirectionStyleDialog |
| 右抽屉约 14 个 FCLSwitch 绑定（锁定/隐藏菜单、软键盘、手势、BE手势、左半屏触摸、陀螺仪、反转、物理鼠标、日志等） | `initRightMenu` FXUtils.bindBoolean | GameMenu.java:410-468 | 中 | 需逐个映射 Miuix Switch |
| 禁用手柄映射开关 → gamepadDisabled | `disableGamepadMapping.setOnCheckedChangeListener` | GameMenu.java:454-456 | 低 | |
| 性能模式开关 → setSustainedPerformanceMode | `performanceModeSwitch` | GameMenu.java:470-474 | 低 | |
| 隐藏菜单开关 → MenuView 隐藏 + Toast | menuSetting.getHideMenuViewViewProperty().addListener | GameMenu.java:476-481 | 低 | |
| FPS 显示开关（启停每秒刷新线程）+ **长按重置悬浮文字位置** | `showFps.*` | GameMenu.java:483-513 | 中 | 后台线程写 DraggableTextView |
| 内存显示开关（同上模式）+ 长按重置位置 | `showMemory.*` | GameMenu.java:515-552 | 中 | |
| 日志窗口显隐双属性联动 | showLog/autoShowLog addListener | GameMenu.java:554-562 | 中 | |
| 手势模式（建造/战斗）/鼠标模式（点击/滑动）Spinner | `initRightMenu` + FXUtils.bindSelection | GameMenu.java:564-585 | 低 | |
| 11 个参数 SeekBar（物品栏宽高、窗口缩放、光标偏移、灵敏度×2、指针大小/偏移XY、手柄死区、陀螺仪灵敏度），拖动实时回写 MenuSetting | `initSeekbar` | GameMenu.java:587-642 | 中 | |
| 右抽屉 6 按钮（联机/快捷输入/发送键码/重置手柄映射/手柄按键绑定/强制退出） | `initRightMenu` → `onClick` | GameMenu.java:631-636, 965-1008 | 低 | MultiplayerDialog/QuickInputDialog/SelectKeycodeDialog/Remapper.wipePreferences/GamepadMapDialog/FCLAlertDialog 强制退出 |
| 外接物理鼠标 onGenericMotion（HOVER_MOVE）+ 指针捕获 requestPointerCapture（863-866） | `touchPad.setOnGenericMotionListener` | GameMenu.java:760-769 | 高 | |
| 光标模式切换回调（显示/隐藏 CursorView 与 GameItemBar、指针居中） | `onCursorModeChange` | GameMenu.java:844-869 | 高 | 覆盖层全局状态机 |
| DrawerLayout 锁定 LOCK_MODE_LOCKED_CLOSED（只能程序化打开） | `getLayout` | GameMenu.java:786-790 | 高 | |
| 游戏画面输出后移除启动进度、隐藏自动日志 | `onGraphicOutput` | GameMenu.java:835-841 | 低 | |
| 进程退出非 0 → 跳 JVMCrashActivity 并 killProcess | `onExit` | GameMenu.java:897-903 | 低 | |
| 游戏日志追加 LogWindow + 写文件 | `onLog` | GameMenu.java:873-894 | 低 | |
| 光标自定义图片/GIF 加载（Glide） | `setup` | GameMenu.java:728-748 | 低 | |

### 11.2 JarExecutorMenu.java（JAR 执行器菜单，327 行）

文件：`FCL/src/main/java/com/tungsten/fcl/control/JarExecutorMenu.java`

| 交互行为 | 所在类与方法 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| 13 按钮点击（强制退出 FCLAlertDialog/日志/鼠标模式 Toast/输入法/复制/粘贴/鼠标左右键/窗口移动四方向 ±10px） | `setup` → `onClick` | JarExecutorMenu.java:80-104, 195-243 | 低 | |
| 触摸板点击模式：DOWN/UP 直发鼠标位置+按下/抬起 | `onTouch` clickMode 分支 | JarExecutorMenu.java:249-261 | 中 | |
| 触摸板滑动模式：相对拖动光标 + 短按（≤100ms/≤10px）判定点击 | `onTouch` 滑动分支 | JarExecutorMenu.java:262-289 | 中 | 手写手势判定 |
| 日志/退出回调、JVM 崩溃跳转 | `onLog/onExit` | JarExecutorMenu.java:165-192 | 低 | |

### 11.3 菜单框架、输入桥与陀螺仪

| 文件 | 结论 | 位置 | 难点 |
|---|---|---|---|
| MenuCallback.java | 纯接口（setup/getLayout/onLog/onExit 契约） | `FCL/src/main/java/com/tungsten/fcl/control/MenuCallback.java:14-42` | — |
| MenuType.java / GestureMode.java / MouseMoveMode.java | 纯枚举 | 同目录 | — |
| FCLInput.java | 手柄/键盘/滚轮入口：`handleKeyEvent`（:221-271，BACK 键→鼠标右键 230-234、软键盘 Enter 237-256、手柄转发 259-262）、`handleGenericMotionEvent`（273-295）、`handleLeftJoyStick` 8 方向死区（297-314）、`handleRightJoyStick` 视角（349-361）+ Choreographer 帧回调 doTick（363-371）。属"保留逻辑"，Compose 层需保留 KeyEvent/MotionEvent 拦截钩子 | `FCL/src/main/java/com/tungsten/fcl/control/FCLInput.java` | 高 |
| AWTInput.java | 输入事件分发核心（非 UI） | 同目录 | — |
| Gyroscope.java | SensorEventListener：仅光标禁用时积分角速度推视角，可反转轴、灵敏度乘法 | `FCL/src/main/java/com/tungsten/fcl/control/Gyroscope.java:79-95` | 中 |
| keyboard/TouchCharInput.java | 软键盘输入桥（AppCompatEditText，onTextChanged 逐字符发送 :47-48、onKeyPreIme :82、switchKeyboardState :92），Compose 化需 AndroidView 承载 | `FCL/src/main/java/com/tungsten/fcl/control/keyboard/TouchCharInput.java` | 高 |

### 11.4 覆盖层自定义 View（control/view/）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| ControlButton（775 行） | 编辑模式：拖拽移动（setX/setY + 边界钳制）；自动对齐吸附 + 绘制对齐线（autoFitPosition/showLine 遍历同级控件）；短按（≤100ms/≤10px）弹 EditViewDialog；落点换算千分比保存。游戏模式：**按下/长按（400ms Handler）/点击/双击（≤400ms）四事件体系**；autoKeep/autoClick（20ms 循环连发 Handler）；按钮可触发开抽屉/切手势/输入法/快捷输入/输出文本/切 ViewGroup 可见性；pointerFollow 拖动视角（与陀螺仪联动 527-533）、movable 游戏中拖按钮本身；可见性 fakefx 绑定（三种 VisibilityType + 父组）；边界红框 onDraw；TouchController 联动转发 moveView | `FCL/src/main/java/com/tungsten/fcl/control/view/ControlButton.java:202-213, 247-258, 287-407, 428-499, 517-544, 546-574, 601-719, 346-348` | 高 | 手写全套手势识别，回归风险最高之一 |
| ControlDirection（946 行） | 编辑拖拽 + 短按弹 EditViewDialog（注意 :443 编辑拖拽乘了鼠标灵敏度，疑似 bug 但需保留行为）；BUTTON 模式：九宫格命中 8 方向 + 中心双击潜行（自绘九宫格，斜向按钮按下才可见 806-841）；ROCKER 模式：摇杆跟随手指（FOLLOW/CENTER_FOLLOW 整个控件随按下点移动）、圆周钳制；摇杆角度→8 方向状态机（22.5° 分段）；四组 keycode 输出；抬起全量复位；展示模式第二构造器（样式预览用） | `FCL/src/main/java/com/tungsten/fcl/control/view/ControlDirection.java:170-201, 433-487, 490-585, 679-737, 753-890` | 高 | |
| TouchPad（303 行） | 转发 TouchController 多点触控；光标模式·外接鼠标 HOVER 直接设指针并拦截；CLICK 模式：触摸点即指针，DOWN/UP 经 **Choreographer 延迟 33 帧**发左键；SLIDE 模式：相对拖动 + 短按点击；游戏模式：长按 400ms 发建造（左键）/战斗（右键），BE 手势按 hitResultType 自动选模式；多指追踪重置、scaleFactor 缩放视角拖动、陀螺仪联动、短按发键、左半屏禁用触摸 | `FCL/src/main/java/com/tungsten/fcl/control/view/TouchPad.java:72-102, 114-300` | 高 | 本区域最复杂手势分支 |
| MenuView（悬浮菜单球，191 行） | 拖动悬浮球（锁定时禁止）+ 位置持久化为屏幕比例；短按（≤10px/≤400ms）**同时打开左右抽屉**；按下态圆形底色自绘 + 自定义图标/GIF | `FCL/src/main/java/com/tungsten/fcl/control/view/MenuView.java:100-124, 136-150, 167-187` | 中 | |
| GameItemBar.kt（物品栏，223 行） | 单指按下按 x 坐标映射 1-9 槽位发热键；同槽位 200ms 内双击 → 交换双手（受设置开关）；单指滑动切换选中槽位；**双指上滑 200px → 弹 GameItemBarSettingDialog**；设置变更背景短暂变色 1500ms；DataStore 收集设置流 | `FCL/src/main/java/com/tungsten/fcl/control/view/GameItemBar.kt:55-71, 74-78, 93-135, 153-207` | 高 | 双指手势 |
| KeycodeView | 手写短按（≤10px/≤200ms）切换选中态，回调 onKeycodeAdd/Remove；setSelectedWithoutCallback/checkSelection | `FCL/src/main/java/com/tungsten/fcl/control/view/KeycodeView.java:46-73` | 中 | |
| LogWindow | appendLog 自动滚到底、>100 行清空、200ms 内 >200 条自动隐藏（防刷屏）；无用户手势 | `FCL/src/main/java/com/tungsten/fcl/control/view/LogWindow.java:68-91` | 低 | |
| ViewManager | 控件增删与分组可见性切换（无直接手势）；含 Toast edit_view_no_group | `FCL/src/main/java/com/tungsten/fcl/control/view/ViewManager.java:44-58, 130-142` | 中 | 重建 View 树 |
| CustomView / ViewListener | 纯接口 | 同目录 | — | |

### 11.5 控制相关对话框

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| EditViewDialog（685 行，最高复杂度表单） | 顶部 info/event 两 ImageButton 切换子布局 + 确定/取消/克隆/删除四按钮；按钮信息页：文本绑定、可见性/尺寸类型/宽高参照 4 Spinner 联动、位置与宽高 FCLPreciseSeekBar 双向绑定、点数值文本弹 EditDialog 数字输入、**尺寸类型切换时重绑 SeekBar 区间**、style 按钮弹 ButtonStyleDialog；按钮事件页：pointerFollow/movable 两 Switch + 4 套事件子页（各含 7 Switch + 输出文本 + SelectKeycodeDialog + ViewGroupDialog 选择模式）+ FCLTabLayout 切换；方向键信息/事件页同构 | `FCL/src/main/java/com/tungsten/fcl/control/EditViewDialog.java:90-137, 160-411, 470-659` | 高 | 由 ControlButton/ControlDirection 短按及 GameMenu"添加"按钮触发 |
| ViewGroupDialog | 添加分组按钮（弹 EditViewGroupDialog）、确定回调选择结果 | `FCL/src/main/java/com/tungsten/fcl/control/ViewGroupDialog.java:64-87` | 中 | 两处触发（GameMenu、EditViewDialog） |
| ViewGroupAdapter | 选择模式 CheckBox 多选；管理模式**上下移排序（Collections.swap）**、编辑、删除（FCLAlertDialog 确认）；条目入场 TranslationX 动画（:148） | `FCL/src/main/java/com/tungsten/fcl/control/ViewGroupAdapter.java:75-149` | 中 | 全工程唯一"排序"交互（按钮式，非拖拽） |
| EditViewGroupDialog | 名称 EditText + 可见性 Spinner + 确定校验（重名/空白 Toast） | `FCL/src/main/java/com/tungsten/fcl/control/EditViewGroupDialog.java:47-81` | 低 | |
| ButtonStyleDialog | 添加/编辑样式（弹 AddButtonStyleDialog）、确定回调；ListView 定位初始样式 | `FCL/src/main/java/com/tungsten/fcl/control/ButtonStyleDialog.java:63-108` | 中 | |
| ButtonStyleAdapter | 单选 RadioButton 绑定 selectedStyle、删除确认、**预览按钮 OnTouchListener 按 DOWN/UP 切换按压样式** | `FCL/src/main/java/com/tungsten/fcl/control/ButtonStyleAdapter.java:112-147` | 中 | |
| AddButtonStyleDialog | TabLayout 切换普通/按压两套样式页；各 3 FCLPreciseSeekBar + 点文本弹 EditDialog + 3 组颜色按钮弹 FCLColorPickerDialog；名称重复/空白 Toast；预览按钮触摸换样式 | `FCL/src/main/java/com/tungsten/fcl/control/AddButtonStyleDialog.java:73-96, 139-195, 207-309, 332-344, 352-376` | 中 | 表单体量大但模式重复 |
| DirectionStyleDialog / DirectionStyleAdapter | 同构于按钮样式（单选、删除确认、预览用 ControlDirection 展示模式） | `FCL/src/main/java/com/tungsten/fcl/control/DirectionStyleDialog.java:71-110`；`DirectionStyleAdapter.java:79-122` | 中 | |
| AddDirectionStyleDialog | 类型 Spinner 切换按钮/摇杆样式页（container 换布局）；按钮页内嵌 ButtonStyleDialog；摇杆页 5 SeekBar + 4 颜色选择；**取消时还原 beforeStyle** | `FCL/src/main/java/com/tungsten/fcl/control/AddDirectionStyleDialog.java:77-96, 142-161, 318-324, 340-367` | 中 | |
| SelectKeycodeDialog | 递归遍历布局所有 KeycodeView 注册多选/单选监听（单选禁止取消选中）；确定回调 | `FCL/src/main/java/com/tungsten/fcl/control/SelectKeycodeDialog.kt:35-55, 67-114` | 中 | |
| QuickInputDialog | 快捷文本列表点击 → 菜单模式逐字符输入/游戏模式开聊天栏输入+回车；添加按钮弹 AddInputTextDialog | `FCL/src/main/java/com/tungsten/fcl/control/QuickInputDialog.kt:33-81` | 中 | 由 GameMenu 按钮与 ControlButton 事件触发 |
| InputTextAdapter | 条目点击回调输入、删除按钮移除 + notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/control/InputTextAdapter.java:55-76` | 低 | |
| AddInputTextDialog | 文本+备注输入，空白/重复 Toast 校验 | `FCL/src/main/java/com/tungsten/fcl/control/AddInputTextDialog.java:47-63` | 低 | |
| GameItemBarSettingDialog | 两个 Switch（滑动选择/双击交换手）变更即写 DataStore + 关闭按钮 | `FCL/src/main/java/com/tungsten/fcl/control/GameItemBarSettingDialog.kt:17-32` | 低 | 由 GameItemBar 双指上滑触发 |
| OpenFolderDialog | FileBrowserAdapter 目录浏览、返回上级、导入按钮起 SAF 多选（launchMultiSelection）异步复制、取消复制任务、失败 FCLAlertDialog | `FCL/src/main/java/com/tungsten/fcl/control/OpenFolderDialog.kt:53-175` | 中 | 触发点 JVMActivity.java:105 |
| MultiplayerDialog（635 行） | **7 个状态子 UI 按 Terracotta 状态机切换（switchUI）**；等待页房主/来客按钮（异步 fetch 节点）；邀请码输入子对话框（TextWatcher 实时校验房型并变色）；各返回/复制邀请码/复制地址（剪贴板+Toast）；导出日志；玩家列表刷新 | `FCL/src/main/java/com/tungsten/fcl/control/MultiplayerDialog.java:110-149, 202-258, 287-344, 414-470, 536-633` | 高 | 状态机 + 异步任务链 + 表单校验 |
| MultiPlayerProfileAdapter | 纯展示列表，refreshList + notifyDataSetChanged | `FCL/src/main/java/com/tungsten/fcl/control/MultiPlayerProfileAdapter.java:25-64` | 低 | |

### 11.6 TouchController（com/mio/touchcontroller/）

| 组件 | 交互行为 | 位置 | 难点 | 备注 |
|---|---|---|---|---|
| TouchController.kt | 多点触控指针 ID 映射并转发代理客户端（DOWN/POINTER_DOWN/MOVE/UP/POINTER_UP 全分支）；按钮拖动视角逐指增量 moveView；震动反馈 Vibrator | `FCL/src/main/java/com/mio/touchcontroller/TouchController.kt:52-159` | 高 | 由 TouchPad 与 ControlButton 转发 |
| TouchControllerInputView.kt | **自定义 InputConnection 全实现**（commitText/setComposingText/删除/选区/剪贴板/方向键/回车转发），IME 显隐、CursorAnchorInfo 光标区域上报（含 TIRAMISU EditorBoundsInfo） | `FCL/src/main/java/com/mio/touchcontroller/TouchControllerInputView.kt:78-143, 196-233, 285-652` | 高 | 游戏内文本输入桥，Compose 重建仍需 AndroidView 承载 |

### 11.7 手柄输入入口（control/gamepad/，简要）

| 文件 | 结论 | 位置 | 难点 |
|---|---|---|---|
| Gamepad.kt | `isGamepadEvent`（KeyEvent :54-61 / MotionEvent :48-51）；`handleMotionEventInput/handleKeyEvent` 委托 RemapperManager（:87-93）；`handleGamepadInput` 全按键/摇杆/扳机映射分发（:96-204）；RemapperManager 图形化重映射（:17-32, 206-223） | `FCL/src/main/java/com/tungsten/fcl/control/gamepad/Gamepad.kt` | 高 |
| GamepadMap.kt | 纯数据：按键→键码映射表 | 同目录 :7-131 | — |
| GamepadButton.kt / GamepadEmulatedButton.kt | 可切换按键状态机 / 按下状态去抖基类，无 UI 交互 | 同目录 | — |

---

## 12. 附录

### 附录 A：自定义对话框清单（FCL 侧 38 个，均 extends FCLDialog）

- **框架/更新**（2）：`ui/TaskDialog.java`、`upgrade/UpdateDialog.java`
- **版本**（4）：`ui/version/AddProfileDialog.java`、`DuplicateVersionDialog.java`、`RenameVersionDialog.java`、`ModpackSelectionDialog.java`（死代码）
- **管理**（3）：`ui/manage/ModInfoDialog.java`、`ModRollbackDialog.java`、`WorldExportDialog.java`
- **下载**（3）：`ui/download/TranslationDialog.kt`、`ui/download/common/DownloadAddonDialog.java`、`ui/download/modpack/ModpackUrlDialog.java`
- **控制器**（3）：`ui/controller/ControllerInfoDialog.java`、`ControllerUploadDialog.java`、`OldVersionDialog.java`
- **账号**（5）：`ui/account/CreateAccountDialog.java`、`ClassicAccountLoginDialog.java`（空壳）、`OAuthAccountLoginDialog.java`、`AddAuthlibInjectorServerDialog.java`、`OfflineAccountSkinDialog.kt`
- **控制**（14）：`control/EditViewDialog.java`、`EditViewGroupDialog.java`、`ViewGroupDialog.java`、`ButtonStyleDialog.java`、`AddButtonStyleDialog.java`、`DirectionStyleDialog.java`、`AddDirectionStyleDialog.java`、`AddInputTextDialog.java`、`SelectControllerDialog.java`、`MultiplayerDialog.java`、`GameItemBarSettingDialog.kt`、`OpenFolderDialog.kt`、`QuickInputDialog.kt`、`SelectKeycodeDialog.kt`
- **mio**（4）：`mio/ui/dialog/DriverSelectDialog.kt`、`GamepadMapDialog.kt`、`JavaManageDialog.kt`、`RendererSelectDialog.kt`

另有红线模块 FCLLibrary 通用对话框被广泛使用：FCLAlertDialog（确认/警告/三按钮）、EditDialog（文本输入）、FullEditDialog（全屏编辑）、FullImageDialog（大图查看）、FCLColorPickerDialog（取色，3 处使用 + AddButtonStyleDialog/AddDirectionStyleDialog 内 7 处）、ProgressDialog（7 处/4 文件）、以及 helper `DialogUtil.kt` 的 `showErrorDialog/showWarningDialog/showItemSelectionDialog`（`FCLLibrary/src/main/java/com/mio/util/DialogUtil.kt:9-35`）。

### 附录 B：Toast 使用分布（共 91 处 / 42 文件，grep 核实）

高频文件：`control/MultiplayerDialog.java`×6、`control/GameMenu.java`×5、`ui/controller/ControllerRepoPage.java`×5、`ui/download/modpack/LocalModpackPage.java`×6、`ui/manage/ModpackInfoPage.java`×5、`ui/version/DuplicateVersionDialog.java`×4。封装入口 `util/AndroidUtils.java`×2。用途集中于：表单校验失败、异步操作成功/失败反馈、功能入口提示。Compose 侧统一映射 Miuix `toast()` 即可，难点低。

### 附录 C：难点评级汇总（高难条目清单，迁移排期依据）

| 高难条目 | 位置 |
|---|---|
| 双层自研导航体系（UIManager.switchUI + PageManager 页面/临时页栈，含 switchPage 恢复 temp page 语义） | `ui/UIManager.kt:51-71`、`ui/PageManager.java:69-149` |
| 返回键链（onKeyDown → UI → 页面栈 → 主页 exitProcess） | `activity/MainActivity.kt:215-225, 283-289` |
| 静态单例反向调用 + 跨 UI 硬编码跳转 | 各 PageManager companion；`ui/manage/ModListPage.java:460-464` 等 |
| JVMActivity 按键/触摸分发链 + TextureView surface 时序 + 自定义键盘避让 | `activity/JVMActivity.java:86-100, 204-250` |
| ControllerActivity 音量键唤出双 Drawer | `activity/ControllerActivity.java:41-49` |
| MainUI 3D 皮肤 SkinViewer（GL）生命周期手动托管 | `ui/main/MainUI.java:66-113` |
| LocalModListAdapter per-item 网络协程防串位 | `ui/manage/LocalModListAdapter.kt:228-275` |
| ModpackFileSelectionPage 三态勾选文件树（FCLCheckBoxTreeAdapter） | `ui/manage/ModpackFileSelectionPage.java:86-208` |
| VersionSettingPage 17 组 fakefx 双向绑定 | `ui/manage/VersionSettingPage.kt:286-350` |
| ManagePageManager 延迟切页（等版本加载） | `ui/manage/ManagePageManager.kt:84-90` |
| RemoteModDownloadPage 硬编码连弹 3 层临时页 | `ui/download/common/RemoteModDownloadPage.java:222-227` |
| 整合包向导双 PageManager 分支（Download↔Manage） | `ui/download/modpack/ModpackSelectionPage.java:79-135`、`LocalModpackPage.java:90-115`、`ModpackInstaller.java:34-99` |
| DownloadPageManager 静态单例 + 4 层以上临时页堆叠 | `ui/download/DownloadPageManager.kt:23-24` |
| RemoteModListAdapter 已安装检测竞态 | `ui/download/common/RemoteModListAdapter.kt:37-61, 117-128` |
| 账号皮肤三分支异步 + CountDownLatch 阻塞对话框（logIn/uploadSkin/DialogCharacterSelector） | `ui/account/AccountListItem.java:147-260`、`ui/account/CreateAccountDialog.java:466-577` |
| OAuth 设备码事件总线（OAUTH_CALLBACK + WeakListenerHolder） | `ui/account/CreateAccountDialog.java:329-347`、`OAuthAccountLoginDialog.java:51-105` |
| OfflineAccountSkinDialog 内嵌 GL 3D 预览 | `ui/account/OfflineAccountSkinDialog.kt:104-122, 160-168` |
| 游戏内覆盖层全部手写手势（ControlButton/ControlDirection/TouchPad/GameItemBar/DraggableTextView） | `control/view/`、`mio/ui/view/DraggableTextView.kt:34-92` |
| GameMenu 抽屉锁定 + 指针捕获 + 光标模式状态机 | `control/GameMenu.java:760-790, 844-869` |
| EditViewDialog 双层 Tab + 4 套事件子页表单 | `control/EditViewDialog.java:90-411` |
| MultiplayerDialog 7 状态子 UI 状态机 | `control/MultiplayerDialog.java:110-149` |
| TouchControllerInputView 自定义 InputConnection / TouchCharInput / FCLInput 输入桥 | `mio/touchcontroller/TouchControllerInputView.kt:285-652` 等 |

### 附录 D：现存缺陷登记（迁移时需决策，勿无意识还原）

1. `hideViewWithAnim` 先 GONE 再播动画，滑出动画实际不可见（FCLLibrary DisplayAnimUtils）。
2. `RemoteModInfoPage` 的 `screenshotRetry` 未绑定点击监听（`ui/download/common/RemoteModInfoPage.java:114-116` 仅绑 retry/mcmod/website）。
3. `LauncherSettingPage` 的 `resetTheme2Dark` 与 `fetchBackgroundColor2Dark` 未注册点击监听（`ui/setting/LauncherSettingPage.java:97-105, 114`），onClick 分支为死代码。
4. `ControllerUploadDialog` 语言 switch 缺 de/uk 分支（`ui/controller/ControllerUploadDialog.java:169-190`）。
5. `ui/version/ModpackSelectionDialog.java` 全工程无引用，死代码。
6. `ControlDirection` 编辑拖拽乘了鼠标灵敏度（`control/view/ControlDirection.java:443`），疑似 bug 但需保留行为。
7. `WorldInfoPage` 数值校验条件为 `&&`（`ui/manage/WorldInfoPage.java:239-287`），空且非数字才提示，疑似 bug。
8. `RemoteModListAdapter` init 后台写 `modIdList`、bind 读，无刷新联动，存在竞态（`ui/download/common/RemoteModListAdapter.kt:37-61`）。
9. `VersionListAdapter` onBind 中做磁盘 IO 统计 Mod 数且每次复用重播入场动画（`ui/version/VersionListAdapter.kt:81-99`）。
10. 语言 Spinner 靠 `isFirst` 标志抑制初始化回调（`ui/setting/LauncherSettingPage.java:526-537`），Compose 重组下时序语义不同需重设计。
11. `ModpackInstaller` 对 ModpackCompletionException（非 FileNotFound）失败也提示"安装成功"（`ui/download/modpack/ModpackInstaller.java:74-85`），属有意行为需保留。
12. `GameItemBarSettingDialog` 每次开关回调都基于构造时 `setting` 的单字段 copy，同一次会话先动开关 A 再动开关 B 会丢失 A 的改动（lost-update，`control/GameItemBarSettingDialog.kt:25-30`）。3.2 批 2 迁移决策：Miuix 版（`MiuixGameItemBarSettingDialog`）以两个开关实时状态构造回调值，不还原该缺陷；回滚分支保留原行为。
13. 3.2 批 4（手柄/控件样式域 10 弹窗）有意偏差登记，均写在各 Miuix 文件头：① `MiuixViewGroupDialog` 选择模式内持选择快照、确定时回调（遗留 CheckBox 实时改写传入列表；两个真实调用点均不依赖实时改写，对外等价）；② `MiuixSelectControllerDialog` 条目读快照值（遗留 fakefx 属性绑定实时刷新）；③ `MiuixControllerInfoDialog` 动态窗口高度改为 Compose 条件渲染自适应；④ 各弹窗按钮按 Miuix 惯例确定居右（遗留部分弹窗左确定右取消）；⑤ `MiuixAddButtonStyleDialog`/`MiuixAddDirectionStyleDialog` 设置区滚动高度 120dp→260dp；⑥ `ControllerUploadDialog` 语言 switch 缺 de/uk（条目 4）在 Miuix 版中原样保留、未修复。回滚分支均保留原行为。

### 附录 E：条目统计

| 区域 | 交互条目数（约） | 文件覆盖 |
|---|---|---|
| 1. Activity 与 Fragment | 64 | 7 Activity + 2 Fragment + Manifest 全覆盖 |
| 2. 框架层（UIManager/PageManager/Task/更新/主页） | 41 | 10 文件全覆盖 |
| 3. 版本管理 ui/version | 40 | 11 文件全覆盖 |
| 4. 管理 ui/manage | 78 | 24 文件全覆盖 |
| 5. 下载 ui/download | 78 | 27 文件全覆盖 |
| 6. 设置 ui/setting | 42 | 8 文件全覆盖 |
| 7. 控制器 ui/controller | 49 | 15 文件全覆盖（含 control 包 2 个相关组件） |
| 8. 账号 ui/account | 30 | 9 文件全覆盖 |
| 9. 联机 ui/multiplayer | 5 | 1 文件全覆盖 |
| 10. mio 包 UI + AnimUtil | 25 | 11 文件 + AnimUtil 13 方法全覆盖 |
| 11. 游戏内控制覆盖层 control/ | 95 | 41 文件全覆盖（纯数据/接口 8 个已标注） |
| **合计** | **约 547 条**（含全局结论 12 条、附录清单） | FCL/src/main/java 下全部 UI 相关文件 |

> 侦察方法：8 个并行子代理逐文件通读（含分页读完超限文件）+ 主 Agent 全局 grep 交叉核实（SwipeRefreshLayout/ItemTouchHelper/overridePendingTransition/GestureDetector/ValueAnimator/AnimatorSet 均为零命中；Toast 91 处/42 文件；ProgressDialog 7 处/4 文件；FCLDialog 子类 38 个）。`.ImportActivity` 已确认为 activity-alias 而非缺失类（manifest :62-93）。
