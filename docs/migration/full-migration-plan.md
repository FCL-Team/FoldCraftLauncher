# 全量迁移计划（剩余原生 UI → Miuix/Compose）

> 生成时间：2026-08-04 ｜ 分支：`feature/miuix-migration`（HEAD `ccbdbc77`）
> 指令：除控件系统外，所有剩余原生 UI 全部迁移到 Miuix，并确保 100% 还原。
> 本文档为**纯规划**：盘点以当前 HEAD 代码为准（已对 regression-report.md 过时项逐条复核），不含任何业务代码改动。
> 工作量单位：人日（d），按熟悉本代码库的执行者估算。

---

## 0. 范围与红线

### 0.1 明确排除（控件系统红线，不迁移）

| 项 | 路径 |
|---|---|
| 游戏内控件视图与数据 | `control/view/`、`control/data/` |
| 游戏内覆盖菜单 | `control/GameMenu.java`、`JarExecutorMenu.java`（`view_game_menu.xml`/`view_jar_executor_menu.xml`/`menu_left.xml`/`menu_right.xml`） |
| 游戏渲染链路 | `activity/ControllerActivity.java`、`activity/JVMActivity.java`（`activity_jvm.xml`） |
| 原生键盘 | `view_keyboard.xml`（1502 行，经 AndroidView 包装进 MiuixSelectKeycodeDialog，触摸链零改动原则） |
| FCLLibrary 红线资产 | SkinViewer/SkinRenderer（GL）、crash 族（CrashReporter）、FileBrowser 族、ThemeEngine |

### 0.2 HEAD 与旧报告的关键差异（复核结论）

1. **fakefx 已全仓删除**（`f1b2e011`），绑定机制为 `MutableStateFlow` + `FlowSubscriptions`——旧报告中「fakefx 绑定承接」的描述全部过时，剩余原生页的绑定量已大幅低于旧报告口径。
2. **整合包安装向导已 Compose 化**（`79f2332a`）：`Versions.importModpack:57`/`downloadModpackImpl:83`/`updateVersion:174`、`MainActivity.kt:912` 全部 new `ComposeModpackSelectionPage`/`ComposeLocalModpackPage`；旧 `ModpackSelectionPage`/`LocalModpackPage`/`RemoteModpackPage`/`ModpackPage`/原生 `ModpackFileSelectionPage` 已无实例化点（死代码）。regression-report §2.6 的 6 个 🟡 已过时。
3. **开关固化清理已发生**（`d50aa4a8`，-24,308 行）：`MainUI`/`AccountUI` 回滚分支与 `USE_COMPOSE_MAIN_UI`/`USE_COMPOSE_ACCOUNT_UI` 已删，`ui_main.xml` 已删。layout 目录由 142 个降至 **57 个**。
4. **导出向导仍是混合栈**：`ComposeManagePage` → `Versions.exportVersion:145` → **原生** ModpackTypeSelectionPage → **原生** ModpackInfoPage → **Compose** ModpackFileSelectionPage，回滚/返回栈跨两套实现。

---

## 1. 剩余原生 UI 全量盘点（以 HEAD 为准）

图例：迁移方式 = **C** Compose 重写 / **W** AndroidView 包装过渡 / **K** 建议保留（需用户决策）。

### 1.1 首启链路（3 屏，最高危域）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| SplashActivity | `activity/SplashActivity.kt`(268) + `activity_splash.xml`(17) | 中 | C（壳保留，内容 Compose 化） | **高危**：LAUNCHER 入口 + `ImportActivity` activity-alias（zip/mrpack/7z VIEW，Manifest:62-93，interaction-map G12「最易误删」）；`modpack_cache_path` extra 契约（MainActivity.kt:899-922 消费 → ComposeLocalModpackPage）；JVMCrashActivity.kt:84 崩溃重启入口；初始化顺序 `FCLPath.loadPaths→Logging.start→initState→RendererManager/JavaManager/ConfigHolder.init` 必须保持（MainActivity.kt:158-166 有绕过 Splash 的补初始化容错）；SplashScreen API（`installSplashScreen()` + `Theme.Splash`）与 Compose 兼容，成本低 |
| EulaFragment | `fragment/EulaFragment.java`(77) + `fragment_eula.xml`(72) | 低 | C | 读 assets/eula.txt + 滚动文本 + 单按钮；裸 Thread 回填改 LaunchedEffect 即可 |
| RuntimeFragment | `fragment/RuntimeFragment.kt`(311) + `fragment_runtime.xml`(396) | 中 | C | 8 项运行时（lwjgl/cacio/cacio17/java8/17/21/25/jna）状态机：当前靠 Activity public var 共享 + 8 协程并行安装（本地 assets 解包，RuntimeUtils 可复用）+ 完成汇聚 → enterLauncher；迁 Compose 应收敛为单 ViewModel/StateFlow；`installing` 防重入与失败复位语义需保留 |
| 协议/权限门 | SplashActivity 内联（FCLAlertDialog×2 + 存储权限分支） | 中 | C | MANAGE_ALL_FILES（API≥30）/ 运行时权限双分支 + rationale + 设置页跳转；`isAgree`/`isFirstLaunch` SP 契约不变 |

附：Fragment 切换动画 `frag_start_anim`/`frag_stop_anim`（500ms 淡入+右滑入）→ Compose `AnimatedContent` 复刻。

### 1.2 控制布局管理域（启动器内页面，5 页）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| ControllerUI 壳 | `ui/controller/ControllerUI.java`(101) + `ui_controller.xml`(13) | 低 | C | `UIManager.kt:38` 装配；`onBackPressed` 三段分发（先弹临时页、Repo 回 Manager）语义需复刻 |
| ControllerPageManager | `ControllerPageManager.kt`(64) | 低 | C | 常页×2（Manager=15040 立即创建 / Repo=15041 lazy）+ 临时页栈；可迁 Compose 导航或状态栈 |
| ControllerManagePage | `ControllerManagePage.java`(326) + `page_controller_manager.xml`(342) + `item_controller_editable.xml`(59) + `EditableControllerListAdapter.java`(90) | 低-中 | C | 左列表+右详情双栏；删除确认 FCLAlertDialog；导入 .json（MainActivity fileLauncher）；分享（ZL2 转换 `LayoutConverter.convertFclToZl2` 异步 + FileProvider）；编辑布局 → ControllerActivity Intent（extra `"controller"`，红线入口，契约不动）；已用 MiuixControllerInfoDialog |
| ControllerRepoPage | `ControllerRepoPage.java`(391) + `page_controller_repo.xml`(173) + `item_remote_version.xml`(92) + `ControllerListAdapter.kt`(78) | 中 | C | 4 Spinner 搜索面板（含 1 路 StateFlow 双向绑定 + `regex:` 前缀搜索）；`checkUpdate` 逐个比对 versionCode；`downloadFile`（FileDownloadTask + **原生 TaskDialog** + 失败 cache 回滚）；硬调 ManagePage.addController/removeController（7 处跨页调用，迁移时改走 `Controllers` 数据层顺带解耦）；Glide 图标 + 逐条 translationX 入场动画；ThemeEngine 搜索面板 tint |
| ControllerDownloadPage | `ControllerDownloadPage.java`(271) + `page_controller_download.xml`(290) + `ControllerScreenshotAdapter.java`(49) | 中 | C | 临时页；横向截图列表 → LazyRow；历史版本 MiuixOldVersionDialog（已 Miuix）；downloadFile 与 RepoPage 近乎复制（先抽公共逻辑再迁） |
| ControllerUploadPage | `ControllerUploadPage.java`(144) + `page_controller_upload.xml`(108) | 低 | C | 临时页；QQ 群 scheme Intent；MiuixControllerUploadDialog（已 Miuix，内部 AndroidView 嵌 AppCompatSpinner 保留）+ 异步打包 zip + FileProvider 分享 |

**唯一反向桥接点**：`VersionSettingHost.kt:94-101`（Compose 版本设置页）经 `runAfterInit` 驱动原生 `pageManager.switchPage(PAGE_ID_CONTROLLER_REPO)`——迁移时必须同步改造。数据模型 `control/download/Controller*.java`（3 个纯 Gson 类）与三个 Miuix 弹窗零改动复用。

### 1.3 联机域（2 个 UI 单元）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| MultiplayerUI | `ui/multiplayer/MultiplayerUI.java`(180) + `ui_multiplayer.xml`(317) | 低 | C | 主界面「联机」页（MainActivity.kt:401 左菜单拉起）；三 ScrollView 教程视图 setVisibility 切换 + FCLSwitch 总开关 + 用户须知版本门控（SP "third_party"）+ 通知权限请求 + FileProvider 导出日志；零绑定零异步，**最佳试水页** |
| MultiplayerDialog（游戏内联机菜单） | `control/MultiplayerDialog.java`(599) + `MultiPlayerProfileAdapter.java`(65) + `dialog_multiplyer_menu.xml`(62) + `view_multiplayer_*.xml`×5(617) + `item_terracotta_profile.xml`(47) | 中 | C（FCLComposeDialog 壳模式） | **运行在游戏 GL surface 之上**——但 MiuixInviteCodeInputDialog（MultiplayerDialog.java:212 已在用）证明该模式可行；7 状态 StateBindingUI 切换（removeAllViews/addView，无动画）→ Compose when 分支；Terracotta StateFlow 状态机（8 状态 + `isForkOf` 局部刷新语义依赖 previousState，需精确保留）；15 处 marquee 跑马灯；70%×90% 窗口 + cancelable=false；拉起方 GameMenu.java:1016-1022 属红线但仅需改 new 调用一行；`TERRACOTTA_PLAYER` extra 契约（LauncherHelper.java:245）；开关 `USE_COMPOSE_MULTIPLAYER_MENU` 已存在（当前 false，纯标记无读取点，迁移时启用） |

Terracotta 后端层（`terracotta/` 6 类 + Rust JNI）不在 UI 迁移范围，保持不动。

### 1.4 管理域残留原生页（9 页，混合栈最重的域）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| ManageUI 壳 + ManagePageManager | `ManageUI.java`(227) + `ManagePageManager.kt`(120) + `ui_manage.xml`(54) | 中 | C | 5 Tab：Tab0/1 已是 Compose 壳页，Tab2/3/4 原生；**VersionLoadable 契约被 LauncherHelper.java:447（直摸 tabLayout 切 Tab2）、SettingUI、Versions、Compose 页共用**——壳迁移依赖批 0 收口 |
| ModListPage（Tab3） | `ModListPage.java`(609) + `LocalModListAdapter.kt`(276) + `page_manage_mod.xml`(203) + `item_local_mod.xml`(120) | **高** | C | 普通/多选双工具栏状态机（switchLayout）；regex 搜索 + enabled/disabled 过滤；10 按钮；`ModCheckUpdatesTask`（Task 树，每 Mod×2 源并行）；Adapter per-position 协程 Job 联网反查图标并与 ViewHolder 回收联动；跳转已是 `ComposeDownloadPage.jumpToModPage`；删除 FCLAlertDialog；**原生 TaskDialog**（:405）；fileLauncher 多选 |
| ModUpdatesPage（临时页） | `ModUpdatesPage.java`(349) + `ModUpdateListAdapter.java`(70) + `page_mod_update.xml`(69) + `item_update_mod.xml`(72) | 中 | C | `ModUpdateTask` 内部 Task 依赖树（FileDownloadTask + 失败回滚 setOld/disable）；CSV 导出外部存储；原生 TaskDialog×2（:118,:150） |
| WorldListPage（Tab4） | `WorldListPage.kt`(267) + `WorldListAdapter.kt`(76) + `WorldListItem.java`(102) + `page_manage_world.xml`(80) + `item_world.xml`(70) | 中 | C | 协程加载 + showAll 过滤双向绑定；`fixPrivate`（unix:mode 属性）；导入 zip → EditDialog（suspendCancellableCoroutine 包装）；WorldListItem 动作载体：export 已用 MiuixWorldExportDialog、manageDatapacks/showInfo 拉起临时页、delete 后强转 WorldListPage 刷新（耦合 ManagePageManager 单例，迁移时解耦） |
| WorldInfoPage（临时页） | `WorldInfoPage.java`(469) + `page_manage_world_info.xml`(571) | 中 | C | **NBT 即改即写**：难度/模式 FCLSpinner 双向绑定 + 血量/饱食/经验 EditText 订阅即 `writeLevelDat`；构造期同步 `readLevelDat()` 抛异常路径；571 行布局 |
| DatapackListPage（临时页） | `DatapackListPage.java`(228) + `DatapackListAdapter.java`(121) + `page_datapack_list.xml`(87) + `item_datapack.xml`(52) | 低-中 | C | `datapack::loadFromDir` Task + infoFlow 订阅；多选删除/启用/禁用；裸线程添加 |
| InstallerListPage（manage，Tab2） | `manage/InstallerListPage.java`(266) + `page_manage_auto_install.xml`(26) + `view_installer_item.xml`(75) + `ui/InstallerItem.java`(347) | 中 | C | ScrollView 动态 addView（InstallerItem.createView）→ LazyColumn；**InstallerItemGroup 双栈共享**（Compose 侧 VersionInstallInfoScreen.kt:98 同用，互斥图联动时机敏感）；离线安装 jar fileLauncher；已用 MiuixTaskDialog |
| InstallerListPage（download/version，临时页） | `ui/download/version/InstallerListPage` + `page_install_version.xml`(127) | 中 | C | manage InstallerListPage.java:110 拉起的库版本选择页；**Compose 侧已有对等实现 `InstallerListScreen.kt`**（下载域还原批已精修），迁移 = 改调用点指向 Compose 版 + 删除原生类，工作量小但需核对两版语义差 |
| ModpackTypeSelectionPage（导出向导①） | `ModpackTypeSelectionPage.java`(77) + `page_modpack_type.xml`(175) | 低 | C | 3 按钮；`MODPACK_TYPE_*` 常量已被 Compose 侧引用（ModpackFileSelectionScreen.kt:40），类删前常量需挪窝 |
| ModpackInfoPage（导出向导②） | `ModpackInfoPage.java`(276) + `page_modpack_info.xml`(609) | **高** | C | 609 行布局、~15 路 StateFlow 双向绑定、按 `ModpackExportInfo.Options` 动态显隐 8 区块、FCLNumberSeekBar/FCLSpinner/FCLSwitch 混用；出口已是 ComposeModpackFileSelectionPage（:271）——迁完此页导出向导全 Compose 化，混合栈消除 |

已 Compose 化（旧类死代码，随批 0 清理）：ModpackSelectionPage/LocalModpackPage/RemoteModpackPage/ModpackPage/原生 ModpackFileSelectionPage；`page_modpack.xml`/`page_modpack_selection.xml`/`page_modpack_file.xml`。

### 1.5 一级界面容器壳（3 个）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| VersionUI 壳 | `ui/version/VersionUI.java` + `ui_version.xml`(10) | 低 | C 或 K（决策点 D1） | FCLMultiPageUI；内容页已全 Compose |
| DownloadUI 壳 | `ui/download/DownloadUI.java` + `ui_download.xml`(59) | 低-中 | C 或 K（D1） | FCLTabLayout 6 Tab + 返回键先弹临时页 + Profile/版本切换广播 |
| SettingUI 壳 | `ui/setting/SettingUI.java` + `ui_setting.xml`(49) | 低 | C 或 K（D1） | FCLTabLayout + SettingPageManager（内容已全 Compose） |

注意：FCLCommonPage/FCLTempPage/FCLUILayout/PageManager/UIManager 是 **Compose 壳页的运行时骨架**（ComposeDownloadPage 等全部继承 FCLCommonPage、容器为 FCLUILayout）——迁移这些基座等于动全身，建议保留（决策点 D1）。

### 1.6 MainActivity 壳层（1 壳 4 组件）

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| 左侧导航 FCLMenuView×7 | `FCLLibrary/.../FCLMenuView.java`(122) + `activity_main.xml` | **高** | C | **全仓 ≥8 处反向写选中态**（`binding.download/home/manage/controller.setSelected(true)` + `refreshMenuView(null)`：Versions.java:218、ModListPage.java:468、ManageUI.java:73/172/190、LocalModListAdapter.kt:209、**Compose 页也在写**：VersionListScreen.kt:480、VersionSettingHost.kt:95）；选中动画（旋转 360°+缩放 Overshoot，MainActivity.kt:360-368）+ 入场 Bounce 错峰动画（:758-802）；单击/长按语义（home 长按分享日志、back 长按进 ShellActivity） |
| FCLDynamicIsland 标题岛 | `FCLDynamicIsland.java`(149) + `DynamicIslandAnim.java`(141) | 中 | C | 自绘胶囊描边 + 9 个 ObjectAnimator 链 + mark 防重入 + Handler/裸 Thread 定时隐藏；Miuix 无对等件需自研，但调用点集中（10 处全在 MainActivity） |
| VideoView 动态背景 + 壁纸背景 | `activity_main.xml` + MainActivity.kt:124/:11 | 中 | W（AndroidView 包 VideoView 过渡）→ C | 位置保存/恢复、音量 SP；反向调用：LauncherSettingHost.kt:110-155（直触 binding.background/setupLiveBackground/setLiveBackgroundVolume）、LauncherHelper.java:249（shouldPlayVideo） |
| FCLUILayout 内容容器 + 右侧栏僵尸双写 | `activity_main.xml`(311) | 低 | 清理 | 右侧 account/start/version/jar View 常置 GONE，但 loadVersion/updateColor/setupAccountDisplay 仍双写旧 View + MainRightMenuBridge——顺手删除 GONE View 与双写代码 |

前置硬依赖：**批 0 导航事件收口**（把 `MainActivity.getInstance().binding.*` 写选中态改为导航事件/StateFlow 总线），否则左侧菜单无法 Compose 化。`MainActivity.getInstance().fileLauncher` 有 20+ 使用点（含 Miuix 弹窗），Activity 本体不退役、仅壳层视图迁移。

### 1.7 遗漏项

| 项 | 文件 / 布局 | 复杂度 | 方式 | 依赖与风险 |
|---|---|---|---|---|
| TaskDialog + TaskListPane | `ui/TaskDialog.java`(123) + `ui/TaskListPane.java`(260) + `dialog_task.xml`(55) + `item_task_progress/stage.xml` | 中 | C（统一接 MiuixTaskDialog） | **6 处活跃调用点**：ModpackInstaller.java:35（被 Compose ModpackInfoScreens.kt:230/332 调用！跨体系）、ControllerRepoPage.java:267、ControllerDownloadPage.java:197、ModUpdatesPage.java:118/150、ModListPage.java:405；`TaskCancellationAction.Consumer<TaskDialog>` 签名耦合需松动；FileDownloadTask.speedEvent 弱引用总线；MiuixTaskDialog 已有承接先例（LauncherHelper 启动进度） |
| JVMCrashActivity | `activity/JVMCrashActivity.kt`(176) + `activity_jvm_crash.xml`(72) | 低-中 | K 或 C（决策点 D3） | 独立 `:crash` 进程崩溃兜底页：游戏已崩溃时拉起，必须最大健壮，Compose 运行时初始化增加失败面；UI 极简（日志预览 + 4 按钮）；Intent 契约 `isGame/exitCode/logPath` + `startCrashActivity()` 静态入口；拉起方 GameMenu.java:948/JarExecutorMenu.java:188 在红线内——若迁移需先在 `:crash` 进程做 Compose 冒烟验证 |
| 死组件/死代码 | FCLSeekBar、HorizontalListView（零引用）、FCLRadioButton（仅注释）、FCLCheckBoxTreeAdapter/Item、原生 modpack 旧类×5 + `page_modpack*.xml`×3 | — | 删除 | 批 0 顺手清理 |

---

## 2. 总量统计

- **迁移对象单元：26 个**（页面/壳/弹窗/Activity）：首启 3 + 控制布局域 5 + 联机 2 + 管理域 9 + 容器壳 3 + MainActivity 壳 1 + TaskDialog 1 + JVMCrashActivity 1 + download/version InstallerListPage 1（已并入管理域计数，见 §1.4，共 26 含全部）。
- **涉及源码文件约 45 个**（不含红线与后端层），**活跃原生布局约 44 个**（57 个 layout 中扣除红线 6 个、`page_compose_container` 1 个、死代码 3 个、Compose 复用的 spinner 项若干）。
- **复杂度分布**：高 4（ModListPage、ModpackInfoPage、MainActivity 左导航、首启 Intent 契约）；中 14；低 8。

---

## 3. 分批执行计划（风险升序，每批独立可交付、可回滚）

排序原则：低风险域先迁以沉淀模式与证据表流程，高危域（首启链路、壳层）在模式成熟、前置收口完成后最后做。每批沿用既有约定：**新增 `USE_COMPOSE_*` 编译期开关 + 旧实现保留作回滚**，稳定后再随清理批固化删除（对齐 `d50aa4a8` 先例）。

### 批 0：前置收口 + 死码清理（约 1.5d）——无条件最先做

- **内容**：
  1. 导航事件收口：新增导航 StateFlow/事件总线（如 `MainNavEvents`），改造 ≥8 处 `binding.*.setSelected(true)`/`refreshMenuView` 反向写入点（Versions、ModListPage、ManageUI、LocalModListAdapter、VersionListScreen、VersionSettingHost）为事件投递；MainActivity 侧订阅驱动菜单选中。纯行为等价重构。
  2. TaskDialog 收口前置：松动 `TaskCancellationAction.Consumer<TaskDialog>` 签名为接口/函数类型，6 处活跃调用点逐一对照 MiuixTaskDialog 能力表核销（对齐 regression-report §4.2 的触发点清单模式）。
  3. 死码删除：原生 modpack 旧类×5 + `page_modpack.xml`/`page_modpack_selection.xml`/`page_modpack_file.xml` + FCLSeekBar/HorizontalListView/FCLRadioButton/FCLCheckBoxTreeAdapter/Item + MainActivity 右侧栏 GONE View 与僵尸双写。
- **门禁**：`./gradlew :FCL:assembleDebug` 通过；全仓 grep 无残留引用；真机冒烟：主页切 Tab、删除版本、启动游戏进度弹窗三项行为同前。
- **回滚**：纯重构 + 删除，git revert 单提交即可。

### 批 1：联机 MultiplayerUI（约 1d）——试水批

- **内容**：MultiplayerUI → Compose 页（三教程视图切换、Terracotta 总开关、用户须知、通知权限、导出日志），开关 `USE_COMPOSE_MULTIPLAYER_UI`。产出本计划第一张完整证据表（流程验证）。
- **门禁**：构建通过；证据表 100% 核销；真机：开关切 Terracotta 状态机、三视图切换、日志分享。
- **回滚**：开关置 false。

### 批 2：控制布局管理域 5 页（约 3.5d）

- **内容**：ControllerUI 壳 + PageManager（临时页栈语义复刻）→ ControllerManagePage → ControllerUploadPage（低）→ ControllerRepoPage + ControllerDownloadPage（中，先抽公共 downloadFile）。改造 `VersionSettingHost.kt:94-101` 桥接点。原生 TaskDialog 两处随批 0 收口换 MiuixTaskDialog。开关 `USE_COMPOSE_CONTROLLER_PAGES`。
- **门禁**：构建通过；证据表核销；真机：导入 .json、新建/删除布局、仓库搜索/下载（含 cache 回滚）、上传打包分享、版本设置页跳转仓库页桥接。
- **回滚**：开关置 false，旧类全保留。
- **风险**：7 处跨页硬调用改走 `Controllers` 数据层时的行为等价性；列表入场动画近似度。

### 批 3：管理域列表页组（约 5d）

- **内容**：WorldListPage + WorldListItem + WorldInfoPage + DatapackListPage（中）→ InstallerListPage(manage) + download/version InstallerListPage 切换 Compose 版（中）→ ModListPage + ModUpdatesPage（高，多选状态机最后攻）。开关 `USE_COMPOSE_MANAGE_PAGES`。
- **门禁**：构建通过；证据表核销；真机：Mod 增删/多选/检查更新（Task 树并行）、世界导入导出/NBT 即改即写/数据包启停、安装器离线 jar 安装。
- **回滚**：开关置 false。
- **风险**：ModListPage 双工具栏多选状态机 + per-item 协程与回收联动；InstallerItemGroup 双栈镜像在原生侧删除后单栈化（需确认 Compose 侧 VersionInstallInfoScreen 行为不变）；WorldInfoPage 即改即写的 IO 时机。

### 批 4：导出整合包向导收口（约 2.5d）

- **内容**：ModpackTypeSelectionPage + ModpackInfoPage → Compose（609 行布局、~15 路双向绑定、Options 动态显隐）；`MODPACK_TYPE_*` 常量挪至中立位置。完成后导出链全 Compose，混合临时页栈消除。开关 `USE_COMPOSE_MODPACK_EXPORT`。
- **门禁**：构建通过；证据表核销；真机：三种类型导出全流程（类型→信息→文件选择→导出），返回栈逐层行为。
- **回滚**：开关置 false。

### 批 5：联机 MultiplayerDialog 7 状态（约 2.5d）

- **内容**：7 个 StateBindingUI 子视图 → Compose when 分支，整体装入 FCLComposeDialog 壳（沿用 MiuixInviteCodeInputDialog 已验证模式）；`isForkOf` 局部刷新语义精确镜像；15 处 marquee 处理（`Modifier.basicMarquee()`，决策点 D5）；启用既有 `USE_COMPOSE_MULTIPLAYER_MENU` 开关。GameMenu.java:1016-1022 拉起点仅改 new 一行（红线内最小触碰）。
- **门禁**：构建通过；证据表核销；真机：7 状态全流转（Waiting/Scanning/Starting/HostOk/GuestOk/Exception）、邀请码复制、玩家列表、VPN 权限流——**在游戏内实机验证**。
- **回滚**：开关置 false（当前即 false，天然回滚态）。
- **风险**：GL surface 之上 Compose 弹窗的输入法/焦点/帧率表现；isForkOf 语义偏差导致的状态不同步。

### 批 6：容器壳与 MainActivity 壳（约 4d，依赖批 0/2/3/4 全部完成）

- **内容**：
  1. VersionUI/DownloadUI/SettingUI/ManageUI 壳的 FCLTabLayout + 壳视图 Compose 化（决策点 D1 若选「迁」）；FCLCommonPage/FCLUILayout/PageManager/UIManager 基座按决策保留或收口。
  2. MainActivity 壳：左侧 7 键导航 Compose 化（选中动画/入场错峰/单击长按语义）、FCLDynamicIsland 自研 Compose 对等件（9 动画链 + 定时隐藏）、VideoView 动态背景 AndroidView 过渡（LauncherSettingHost/LauncherHelper 反向调用改桥接）、activity_main.xml 重构为 Compose 根。开关 `USE_COMPOSE_MAIN_SHELL`。
- **门禁**：构建通过；证据表核销；真机：七键导航+选中态外部驱动（如下载页内跳版本列表后菜单高亮）、灵动岛 10 个调用点逐条、动态背景播放/音量/切页暂停、返回键 exitProcess 语义。
- **回滚**：开关置 false。
- **风险**：灵动岛动画链还原度；动态背景生命周期（Compose 重组 vs VideoView 状态保存）。

### 批 7：首启链路（约 3d，最高危，放最后）

- **内容**：Activity 壳与 Manifest **零改动**（LAUNCHER、ImportActivity alias、Theme.Splash、Intent 契约全部不动）；EulaFragment/RuntimeFragment 内容 Compose 化（ComposeView 承载或直接 setContent 状态机 + AnimatedContent 复刻切换动画）；8 项运行时状态机收敛为单 ViewModel；协议/权限门 Compose 化；`RuntimeUtils` 与初始化顺序原样复用。开关 `USE_COMPOSE_SPLASH`。
- **门禁**：构建通过；证据表核销；真机（**全量首启矩阵**）：全新安装首启（EULA→权限→运行时安装→进主页）、非首启直达、存储权限拒绝/设置页回跳、arch 不支持弹窗、**外部文件管理器打开 .mrpack/.zip/.7z（ImportActivity alias）进导入向导**、崩溃后重启进 Splash、杀进程绕过 Splash 的 MainActivity 补初始化。
- **回滚**：开关置 false；因 Activity 壳未动，回滚面最小。
- **风险**：alias Intent 契约破损（外部「用 FCL 打开」失效，最高危点）；初始化顺序/线程约束偏差；运行时并行安装汇聚判定竞态。

### 批 8：JVMCrashActivity（约 1d，**需先过决策点 D3**）

- **内容**：若决策迁移——先在 `:crash` 进程做 Compose 初始化冒烟（空 ComposeActivity 拉起验证），通过后再重写崩溃页（日志预览 + 4 按钮 + FileProvider 分享），Intent 契约与 `startCrashActivity()` 静态入口不动。开关 `USE_COMPOSE_JVM_CRASH`。
- **门禁**：`:crash` 进程冒烟通过；真机构造真实游戏崩溃验证四按钮 + 重启链路（→ SplashActivity）。
- **回滚**：开关置 false；若冒烟不过，维持保留原生（决策有据）。

### 批 9：固化清理批（约 1d，全部稳定后）

- 各开关稳定运行后，按 `d50aa4a8` 先例固化删除回滚分支与旧 XML/旧类；更新 regression-report 状态表与本计划核销附录。

**总工作量估算：约 24.5 人日**（不含真机验证执行时间）。

---

## 4. 「100% 还原」验收方法（复用下载域精修模式）

每批每个页面执行以下流程（完整范例见 `download-restore-plan.md` 及其附录 A 核销表）：

1. **旧版基准拆解**：以 `git show pre-miuix-baseline:<path>` 读取旧 XML/旧类，产出「旧版结构」节——精确到属性（尺寸 dp、字号 sp、padding/margin、约束关系、动画参数、可见性条件）。
2. **差距清单**：对照 Compose 实现逐元素列差距，三级分类：**P0 结构错位**（骨架/栏位/层级不同）、**P1 交互缺失**（旧版可做新版不可做）、**P2 视觉偏差**（同构但尺寸/间距/排布不同）。
3. **逐元素核销证据表**：每页一张表，列 = 编号/级别/差距描述/修复方式/状态；状态 = ✅ 已核销 / 🔵 决策保留（有意 Miuix 风格化，须入决策点清单经用户确认）/ ➖ 无差距（核对后确认）。**100% 还原的判定 = 表内无未核销项，且 🔵 项全部经用户签字**。
4. **静态对照点清单**（每页必查项，源自下载域批 3 新增核销的教训——方案表外的偏差靠逐页 diff 才能抓到）：
   - 字号映射（旧 14sp/12sp/11sp vs Compose body1/body2）、10dp 网格（页面 padding、项间距、label 间距）、容器 padding（10/8 等实测值）、图标资源与尺寸（30dp 图标、jump_24 vs earth_24 类误用）、按钮顺序与等宽排布、按压反馈（anim_scale ↔ Sink）、marquee 处理、IME 行为（actionSearch/flagNoFullscreen/hint）、加载/失败/内容三态、列表入场动画、Spinner 显隐条件与双向绑定、Toast/弹窗文案逐字一致。
5. **构建门禁**：每批 `GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug` 通过。
6. **真机对照**：每批产出真机清单（对齐 regression-report §5 格式），开关 true/false 双路径 A/B 对照；高风险批（5/6/7/8）必须真机全项通过后方可进入下一批。
7. **跨页契约回归**：临时页栈返回链、Profile/版本切换广播、fileLauncher 回调、外部 Intent 导入——按 interaction-map.md 的交互图逐链核销。

---

## 5. 需用户决策点

| 编号 | 事项 | 建议 | 影响 |
|---|---|---|---|
| D1 | 一级界面容器壳（VersionUI/DownloadUI/SettingUI 的 FCLTabLayout 壳 + FCLCommonPage/FCLUILayout/PageManager/UIManager 基座）是否迁移？ | **壳可迁（批 6），基座建议保留**：基座是全部 Compose 壳页的运行时骨架，动它等于动全身且收益仅是「XML 清零」 | 批 6 范围 ±2d |
| D2 | MainActivity 壳是否全量 Compose 化（左菜单/灵动岛/视频背景）？ | 建议迁（指令要求「全部」），但接受批 0 前置收口 + 批 6 独立开关回滚 | 无则剩 1 个混合壳 |
| D3 | JVMCrashActivity 是否迁移？ | **建议保留原生**（`:crash` 进程崩溃兜底，Compose 初始化增加失败面，曝光率极低）；若坚持「除控件系统外全部」，走批 8 冒烟先行流程 | 批 8 ±1d |
| D4 | MultiplayerDialog 游戏内弹窗是否迁移？ | 用户指令已明确纳入（「control/MultiplayerDialog 此前保留」列入盘点）；建议迁，FCLComposeDialog 壳模式风险已被邀请码弹窗先例化解 | 批 5 |
| D5 | marquee 跑马灯、`InfiniteProgressIndicator`、Sink 按压反馈等 Miuix 风格化项 | 沿用下载域 D4 结论：保留 Miuix 风格化（basicMarquee/Ellipsis 细节逐页入证据表 🔵 项） | 各批证据表 |
| D6 | 开关策略：新增批次的开关是长期保留还是稳定后固化删除？ | 沿用现状节奏：迁移期带开关，稳定后随固化清理批删除（`d50aa4a8` 先例） | 批 9 |
| D7 | 死代码（原生 modpack 旧类、FCLSeekBar 等）删除时点 | 批 0 顺手删（均无实例化点，grep 已核销） | 批 0 |
| D8 | 灵动岛（FCLDynamicIsland）无 Miuix 对等件：自研 Compose 复刻 vs 改用 Miuix 风格横幅？ | 建议自研复刻（100% 还原要求），动画链封闭、调用点集中，工作量可控 | 批 6 |

---

## 6. 风险 Top 5 汇总

1. **首启链路 Intent 契约**（批 7）：ImportActivity alias、modpack_cache_path、崩溃重启、初始化顺序——任何偏差即「打不开/导不进/起不来」级事故；缓解 = Activity 壳零改动 + 全量首启真机矩阵 + 开关回滚。
2. **MainActivity 壳反向调用网络**（批 0/6）：≥8 处跨域写菜单选中态（含 Compose 页）+ 20+ 处 `getInstance()` 使用点；不先收口则左菜单迁不动，收口不当则导航高亮错乱；缓解 = 批 0 独立成批、纯等价重构、事件总线单一出口。
3. **ModListPage + ModpackInfoPage 两个高复杂度单点**（批 3/4）：多选状态机/per-item 协程联动、609 行布局 15 路双向绑定——100% 还原的证据表工作量集中于此；缓解 = 同域低复杂度页先迁、逐元素核销表兜底。
4. **MultiplayerDialog 游戏内环境**（批 5）：GL surface 之上的 Compose 弹窗（输入法/焦点/帧率）；缓解 = 邀请码弹窗先例 + 开关当前即 false（天然回滚态）+ 游戏内实机验证门禁。
5. **导出向导混合栈**（现状即存在，批 4 消除）：原生①②+Compose③ 跨栈返回链是当前 HEAD 最脆弱的交互面（双 PageManager 分支，interaction-map §559 标「高」）；批 4 完成后该风险归零。

---

## 附录：与既有文档的关系

- 本计划取代 regression-report.md §2 状态表中已过时的 🟡 项口径（差异见 §0.2）；状态表本身待各批执行后更新。
- 每批的差距清单/证据表模板直接复制 `download-restore-plan.md` 的正文结构与附录 A 格式。
- 真机清单格式沿用 `regression-report.md` §5；交互链核销对照 `interaction-map.md`。
