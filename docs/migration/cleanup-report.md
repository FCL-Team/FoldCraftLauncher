# 6.1 遗留代码清理报告

> 生成时间：2026-08-02 ｜ 分支：`feature/miuix-cleanup`（基于 `feature/miuix-migration` 全部成果）
> 前提：真机基准测试已通过（用户确认），方案授权删除已被替代的旧 UI 实现。
> 输入：`final-report.md` §6（清理候选清单与顺序）、`regression-report.md` §1/§3、`fakefx-removal-plan.md` 阶段 0。
> **边界声明：本步骤执行环境无真机。全部验证为静态核查（逐文件全仓 grep 零引用确认）+ 每批 `:FCL:assembleDebug` 构建门禁。清理后的运行时行为未做真机回归，发布前仍需按 `final-report.md` §5 清单抽查。**

---

## 1. 总览

| 项 | 数据 |
|---|---|
| 删除文件 | **166**（代码 78：52 Java + 26 Kotlin；布局 XML 85；drawable 3） |
| 修改文件 | 76（调用点收敛 + import 清理 + 注释同步 + 资源条目删除） |
| 新增文件 | 1（`ui/download/version/InstallFailureAlert.java`，见 §4.2） |
| 行数变化 | **+1,373 / −24,308**（git diff --stat，242 文件） |
| 开关收敛 | 51 个开关删除 50 个（48 true + L1/L2 补迁 2 个），保留 1 个（`USE_COMPOSE_MULTIPLAYER_MENU = false`） |
| 构建门禁 | 批 0+1 / 批 2 / 批 3 / 资源批共 **4 次 `assembleDebug` 全部 BUILD SUCCESSFUL** |
| fakefx 引用面 | FCL 侧引用 fakefx 的文件 **98 → 73**（−25，−26%） |
| 依赖 | `viewBinding`/`material`/`appcompat`/`constraintlayout` 均未动（保留原生页面仍在用）；`gradle/libs.versions.toml` 与 `FCL/build.gradle.kts` 零改动 |

## 2. 批次执行记录与验证方式

每批的通用验证：删除每个类前全仓 grep 类名（范围 FCL/FCLCore/FCLLibrary/FCLauncher/Terracotta 全部 src，含 .java/.kt/.xml/AndroidManifest.xml）；删除每个布局前双向 grep `R.layout.<名>` 与 `@layout/<名>`（含 FCLLibrary res），并附带核查对应 ViewBinding 类名零残留引用。批后构建命令统一为 `GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug --console=plain`。

### 批 0 · 废弃项（6.1-A + 示例/测试入口）

删除 7 个文件，全部删除前 grep 确认零引用：

- `res/layout/dialog_relogin_classic.xml`、`dialog_modpack_selection.xml`、`dialog_world_name.xml`（空布局/孤儿，全仓零引用）
- `ui/version/ModpackSelectionDialog.java`（孤儿类，全仓无实例化）
- `ui/bridge/example/LauncherSettingsScreen.kt`、`LauncherSettingsViewModel.kt`（桥接层示例，无 import 方，已被正式迁移替代）
- `activity/ThemeTestActivity.kt`（2.2 临时主题测试入口，判断删除）+ `AndroidManifest.xml` 中对应 `<activity>` 声明与注释（Manifest 本次唯一改动，最小化）

### 批 1 · 旧弹窗类 + 弹窗开关收敛（6.1-B 批 1，含 L1–L4 旧分支）

收敛方式：全部 `if (ComposeDialogs.USE_COMPOSE_X) { Miuix } else { 旧实现 }` 双分支删除 else 侧，只走 Compose 路径；同步删除失效 import 与仅服务旧分支的私有成员。

删除代码文件 43 个（旧弹窗类 30 + 配套专用 adapter 9 + 开关宿主/杂项 4）：

- **通用/版本域**：`ui/version/AddProfileDialog.java`、`DuplicateVersionDialog.java`、`RenameVersionDialog.java`；`ui/manage/WorldExportDialog.java`、`ModInfoDialog.java`、`ModRollbackDialog.java`（+`ModOldVersionListAdapter.java`）；`com/mio/ui/dialog/JavaManageDialog.kt`（+`ManageJavaItemAdapter.kt`）、`RendererSelectDialog.kt`、`DriverSelectDialog.kt`；`control/OpenFolderDialog.kt`
- **账户域**：`ui/account/CreateAccountDialog.java`（含内嵌 DialogCharacterSelector）、`AddAuthlibInjectorServerDialog.java`、`OAuthAccountLoginDialog.java`、`OfflineAccountSkinDialog.kt`
- **控件域（control/，旧弹窗类，非游戏内 View 体系）**：`AddButtonStyleDialog.java`、`AddDirectionStyleDialog.java`、`ButtonStyleDialog.java`（+`ButtonStyleAdapter.java`）、`DirectionStyleDialog.java`（+`DirectionStyleAdapter.java`）、`ViewGroupDialog.java`（+`ViewGroupAdapter.java`）、`QuickInputDialog.kt`（+`InputTextAdapter.java`）、`SelectKeycodeDialog.kt`、`GameItemBarSettingDialog.kt`、`com/mio/ui/dialog/GamepadMapDialog.kt`（+`GamepadMapItemAdapter.kt`）
- **手柄/下载/杂项域**：`ui/controller/OldVersionDialog.java`（+`HistoricalListAdapter.java`）、`ControllerInfoDialog.java`、`ControllerUploadDialog.java`；`control/SelectControllerDialog.java`（+`SelectableControllerListAdapter.java`）；`ui/download/TranslationDialog.kt`、`ui/download/modpack/ModpackUrlDialog.java`、`ui/upgrade/UpdateDialog.java`
- **死代码清除**：`LauncherHelper` 内嵌旧 `SkipLoginDialog`/`TipReLoginLoginDialog` 两个内部类；`MultiplayerDialog` 内嵌旧 `InviteCodeInputDialog` 内部类

删除布局 47 个：`dialog_translation`、`item_translation`、`dialog_update`、`dialog_download_controllor`、`dialog_modpack_url`、`dialog_add_profile`、`dialog_duplicate_version`、`dialog_rename_version`、`dialog_world_export`、`dialog_manage_java`、`item_manage_java`、`dialog_mod_info`、`dialog_rollback_mod`、`dialog_select_renderer`、`item_renderer`、`dialog_open_folder`、`dialog_create_account`、`dialog_character_selector`、`dialog_add_authlib_injector_server`、`dialog_relogin_oauth`、`dialog_offline_account_skin`、`item_character`、`view_create_account_external`、`view_create_account_microsoft`、`view_create_account_offline`、`dialog_add_button_style`、`dialog_add_direction_style`、`dialog_add_input_text`、`dialog_edit_view`、`dialog_edit_view_group`、`dialog_gamepad_map`、`dialog_itembar_setting`、`dialog_manage_button_style`、`dialog_manage_direction_style`、`dialog_manage_view_groups`、`dialog_quick_input`、`item_button_style`、`item_direction_style`、`item_gamepad_map`、`item_input_text`、`item_view_group`、`view_button_style`、`view_direction_style_button`、`view_direction_style_rocker`、`view_edit_button_event`、`view_edit_button_event_child`、`view_edit_button_info`、`view_edit_direction_event`、`view_edit_direction_info`、`dialog_controller_info`、`dialog_controller_upload`、`item_screenshot_path`、`dialog_select_controller`、`item_controller_selectable`、`dialog_skip_login`、`dialog_tip_relogin`、`dialog_input_invite_code`（全部位于 `FCL/src/main/res/layout/`，`.xml` 后缀略）。

收敛调用点涉及 40+ 文件，关键保留文件的改动边界：

- **红线/红线邻近文件**（`GameMenu.java`、`ControlButton.java`、`ControlDirection.java`、`GameItemBar.kt`、`JVMActivity.java`、`LauncherHelper.java`、`MultiplayerDialog.java`）：仅删除 else 回滚分支、死内部类与失效 import，其余逻辑零改动。
- **保留原生页面**（`ControllerManagePage/ControllerUploadPage/ControllerDownloadPage`、`InstallerListPage`、`ModpackFileSelectionPage`、`ModpackSelectionPage`、`Versions.java`、`UpdateChecker.java`、`LocalModListAdapter.kt`、`WorldListItem.java` 等）：同上，只收敛开关分支。
- **Callback 类型收编**（必要联动）：旧类删除后其嵌套 `Callback` 类型仍被 Miuix 侧/保留页使用，逐一迁往新家，签名逐行不变——`MiuixSelectControllerDialog.Callback`、`MiuixOldVersionDialog.Callback`、`MiuixModpackUrlDialog.Callback`、`MiuixControllerInfoDialog.Callback`、`MiuixControllerUploadDialog.Callback`、`MiuixModRollbackDialog.Callback`（均改为 `fun interface`，Java 方法引用与 Kotlin SAM 调用点无需改动）。

**批 1 门禁：第一次构建暴露 3 处 Kotlin 可空性问题**（`MiuixSelectControllerDialog.Callback` 签名为 `Controller?`，两处调用点按旧非空签名写）→ 修复（`VersionSettingPage.kt`、`VersionSettingHost.kt` 加 null 守卫）→ **BUILD SUCCESSFUL（2m29s）**。

### 批 2 · 旧页面类 + 页面开关收敛（6.1-B 批 2）

删除代码文件 25 个：

- **版本/设置域**：`ui/version/VersionListPage.kt`（+`ProfileListAdapter.java`、`VersionListAdapter.kt`、`VersionListItem.java`）；`ui/manage/ManagePage.kt`（+`adapter/ManageItemAdapter.kt`、`item/ManageItem.kt`）、`VersionSettingPage.kt`；`ui/setting/LauncherSettingPage.java`、`HelpPage.java`、`AboutPage.java`（+`ArticleAdapter.java`、`DocCategoryAdapter.java`）；`ui/version/compose/ComposeVersionPages.kt`（开关 object，常量删除后无其他成员）
- **下载域**：`ui/download/common/`（`DownloadPage.java` 及临时页链 `RemoteModDownloadPage/RemoteModInfoPage/RemoteModVersionPage.java`、内嵌 `DownloadAddonDialog.java`、配套 `RemoteModListAdapter.kt`、`RemoteModScreenshotAdapter.kt`、`ModVersionAdapter.java`、`ModGameVersionAdapter.java`、`DependencyAdapter.java`，目录已空并移除）；`ui/download/ModDownloadPage.java`、`ResourcePackDownloadPage.java`、`ShaderPackDownloadPage.java`、`modpack/ModpackDownloadPage.java`；`ui/download/version/VersionInstallPage.java`、`VersionInstallInfoPage.java`

删除布局 21 个：`page_version_list`、`page_manage_version`、`page_version_setting`、`page_setting_launcher`、`page_setting_help`、`page_setting_about`、`item_version`、`item_manage`、`item_profile`、`item_article`、`page_download`、`page_download_addon`、`page_download_addon_info`、`page_download_addon_version`、`page_installer`、`item_remote_mod`、`item_mod_version`、`view_mod_screenshot`、`dialog_download_addon`（DownloadAddonDialog 配套，删类后成孤儿）、`item_authlib_injector_server`（批 3 连带，见 §2 批 3）、`item_account`（同批 3）。

装配点收敛：`VersionPageManager.java`、`ManagePageManager.kt`、`SettingPageManager.kt`（含删除其 private 常量 `USE_COMPOSE_SETTING_PAGES`）、`DownloadPageManager.kt`；开关常量声明随 `ComposeVersionPages.kt`、`ComposeDownloadPages.kt` 开关 object 一并删除。

**必要联动修复（抽取存活成员后再删类，内容逐行不变）**：

1. `RemoteModVersionPage.DownloadCallback` → `RemoteModActions.kt` 顶层 `fun interface DownloadCallback`；
2. `DownloadAddonDialog.Callback` → `MiuixDownloadAddonDialog` 嵌套 `fun interface Callback`；
3. `ModVersionAdapter.FORMATTER` → `RemoteModVersionScreen.kt` 顶层 `REMOTE_MOD_DATE_FORMATTER`；
4. `RemoteModDownloadPage.STRING_ID_KEY` → `RemoteModDownloadScreen.kt` 私有 `DEPENDENCY_STRING_ID_KEY`；
5. `VersionInstallInfoPage.alertFailureMessage` → **新增 `ui/download/version/InstallFailureAlert.java`**（保留原生链路 `ModpackInstaller.java`、`manage/InstallerListPage.java` 与 Compose 侧 `VersionInstallInfoScreen.kt` 三方共用，调用点仅改 import/限定名）；
6. `LocalModListAdapter.kt`（保留原生）对 `ModDownloadPage` 的 cast + `jumpToModPage` → `ComposeDownloadPage` 新增等价 `jumpToModPage(RemoteMod)`（按仓库类型打开 Compose 详情页）；`MiuixAddProfileDialog.kt` 删除对已删 `VersionListPage` 的刷新调用（Compose 页自带 Profiles 监听，原注释已注明无需手动刷新）。

**批 2 门禁：BUILD SUCCESSFUL（1m59s）**。

### 批 3 · 一级 UI 与 Activity 回滚路径（6.1-B 批 3）

删除代码文件 6 个：`ui/main/MainUI.java`、`ui/account/AccountUI.java`（+`AccountListAdapter.kt`、`ServerListAdapter.java`）、`activity/compose/ComposeActivities.kt`（开关 object）、`activity/ThemeTestActivity.kt` 计入批 0。
删除布局 6 个：`ui_main`、`ui_account`、`activity_web`、`activity_shell`、`item_account`、`item_authlib_injector_server`。

收敛：`UIManager.kt`（只实例化 ComposeMainUI/ComposeAccountUI）、`MainActivity.kt`（移除 MAIN_UI 判断）、`WebActivity.kt`/`ShellActivity.kt`（删除 else 旧 View 路径及仅服务旧路径的字段/内部类；ShellUtil 生命周期契约不变）；`ComposeMainUI.kt`/`ComposeAccountUI.kt` 删除开关常量。

**必要联动修复**：`MainScreen.kt` 收编 `MainUI.ANNOUNCEMENT_URL/ANNOUNCEMENT_URL_CN` 为文件级私有常量、`alex.png` 类加载锚点改为 `ComposeMainUI::class.java`；`AccountListItem.java` 删除 `instanceof MainUI` 分发分支。

**批 3 门禁：BUILD SUCCESSFUL（1m29s）**。

### 批 4 · 专属资源核查（保守）

从 git HEAD 提取全部已删布局的 `@drawable/@string/@color/@dimen` 引用候选，逐一全仓 grep（含 FCLLibrary、values 内交叉引用）确认零引用后删除：

- drawable 3 个：`bg_item.xml`、`bg_item_clickable.xml`、`ic_baseline_server_24.xml`
- string 7 条 × 9 个 locale（values + 8 个 values-\*）：`button_set`、`input_hint_optional`、`settings_advanced_minecraft_arguments_prompt`、`settings_advanced_server_ip_prompt`、`settings_memory_allocate_auto`、`settings_memory_used_per_total`、`world_name_enter`
- color 1 条 × 2（values + values-night）：`primary_text`

**批 4 门禁：BUILD SUCCESSFUL（1m16s）**。

## 3. 开关收敛清单

51 个开关 → 删除 50 个，保留 1 个：

- **删除 42 个 `ComposeDialogs.USE_COMPOSE_*`**：TRANSLATION、UPDATE、DOWNLOAD_ADDON、CONTROLLER_OLD_VERSION、MODPACK_URL、DUPLICATE_VERSION、RENAME_VERSION、WORLD_EXPORT、SKIP_LOGIN、TIP_RELOGIN、ADD_PROFILE、MOD_INFO、ROLLBACK_MOD、JAVA_MANAGE、RENDERER_SELECT、DRIVER_SELECT（L1）、VERSION_OP_ALERTS（L2/L3）、GAMEPAD_MAP、ITEMBAR_SETTING、OPEN_FOLDER、INVITE_CODE、SELECT_KEYCODE、CREATE_ACCOUNT、CHARACTER_SELECTOR、ADD_AUTHLIB_INJECTOR_SERVER、RELOGIN_OAUTH、OFFLINE_ACCOUNT_SKIN、QUICK_INPUT、ADD_INPUT_TEXT、ADD_BUTTON_STYLE、ADD_DIRECTION_STYLE、BUTTON_STYLE、DIRECTION_STYLE、EDIT_VIEW、EDIT_VIEW_GROUP、VIEW_GROUP、SELECT_CONTROLLER、CONTROLLER_INFO、CONTROLLER_UPLOAD。`ComposeDialogs.kt` 重写为仅含 MULTIPLAYER_MENU。
- **删除 `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG`**（L4）：全部已接开关调用点只走 MiuixTaskDialog；未接开关的 8 处保留原生页面调用点本就走旧 TaskDialog，不受影响。
- **删除页面开关 4 个**：`ComposeVersionPages.USE_COMPOSE_VERSION_PAGES`/`USE_COMPOSE_VERSION_SETTING`（文件随删）、`SettingPageManager.USE_COMPOSE_SETTING_PAGES`、`ComposeDownloadPages.USE_COMPOSE_DOWNLOAD_PAGES`。
- **删除一级开关 4 个**：`ComposeMainUI.USE_COMPOSE_MAIN_UI`、`ComposeAccountUI.USE_COMPOSE_ACCOUNT_UI`、`ComposeActivities.USE_COMPOSE_WEB`/`USE_COMPOSE_SHELL`（ComposeActivities.kt 随删）。
- **保留 1 个**：`ComposeDialogs.USE_COMPOSE_MULTIPLAYER_MENU = false`——联机菜单 7 状态 View 体系有意保留原生，`MultiplayerDialog.java` + `dialog_multiplyer_menu.xml` + 6 个 `view_multiplayer_*` + `item_terracotta_profile` 完整保留。

## 4. 保留项清单与理由

### 4.1 方案既定保留（红线与决策保留，未触碰）

- **游戏内/红线**：`control/` 游戏内控件 View 体系（ControlButton/ControlDirection/TouchPad/GameMenu 等类本身）、`ControllerActivity`/`JVMActivity`/`SplashActivity`/`JVMCrashActivity`、`menu_left/right`、`view_game_menu`、`view_keyboard`（MiuixSelectKeycodeDialog 经 AndroidView 包装使用）、`view_jar_executor_menu`、`activity_jvm`、`activity_splash`、`activity_jvm_crash`、`fragment_eula/runtime`；
- **联机**：见 §3 保留开关项（`ui_multiplayer.xml` 及 MultiplayerUI 同保留）；
- **容器壳**：`ui_version/ui_manage/ui_download/ui_controller/ui_setting` + 对应 UI 容器类、`activity_main.xml` 原生骨架（MainActivity 仍直接用其 versionName/versionProgress 显示启动进度）、`page_compose_container.xml`（Compose 页面壳，长期使用）；
- **保留原生页面**：手柄域 4 页（page_controller_\*）、管理域 Mod/存档/数据包/安装器链路（ModListPage/WorldListPage/WorldInfoPage/DatapackListPage/ModUpdatesPage/两个 InstallerListPage + page_install_version/view_installer_item 等）、整合包向导全部（ModpackSelectionPage/LocalModpackPage/RemoteModpackPage/ModpackInfoPage/ModpackFileSelectionPage/ModpackTypeSelectionPage/ModpackInstaller + page_modpack\*）；
- **AndroidView 包装组件**：SkinViewer、WebView、VideoView 动态背景；
- **FCLLibrary `FCL*` 组件**：保留原生页面与游戏侧仍大量使用。

### 4.2 清理中新发现的保留项（任务点名删除但验证后有存活引用，保守保留）

| 项 | 理由 |
|---|---|
| `dialog_select_keycode.xml` | `MiuixSelectKeycodeDialog.kt:93` 经 AndroidView inflate 此布局（内含红线 view_keyboard），删除即编译失败。**此布局实为 Compose 链路的现役资产**，非回滚残留 |
| `control/EditViewDialog.java`、`EditViewGroupDialog.java`、`AddInputTextDialog.java` | 旧实现已删，仅保留嵌套 `Callback` 接口：被 GameMenu/ControlButton/ControlDirection（红线）与 Miuix 弹窗复用 |
| `ui/account/AccountListItem.java` | Compose 账户页（AccountViewModel/AccountScreen/MiuixOfflineAccountSkinDialog）现役登录/换肤链路载体 |
| `ui/account/ClassicAccountLoginDialog.java` | 被 AccountListItem 实例化（离线账户凭据过期重登录），未在迁移清单内 |
| `ui/main/Announcement.java` | Compose MainScreen 公告数据模型现役引用 |
| `ui/setting/DocIndex.java` | Compose HelpViewModel/HelpScreen 现役引用 |
| `ui/download/version/RemoteVersionListAdapter.kt` | 保留原生 download/version/InstallerListPage 现役引用 |
| `ui/TaskDialog.java` + `dialog_task.xml` + `item_task_progress/stage` | 保留原生页面 8 处活跃调用点（ModListPage/ModUpdatesPage/ControllerRepoPage/ControllerDownloadPage/ModpackInstaller/ModpackSelectionPage） |
| `util/FXUtils.java`、`util/WeakListenerHolder.java` | 保留原生页面、control/data、setting 乃至部分 Miuix 弹窗仍在用；随 fakefx 后续阶段处理 |

### 4.3 依赖收敛结论

`viewBinding`/`material`/`appcompat`/`constraintlayout` 均有保留原生页面与 FCLLibrary 现役使用，**依赖全部未动**；`libs.versions.toml`、`FCL/build.gradle.kts` 零改动（`check*AarMetadata` 禁用块维持现状，待 AGP 9.x + compileSdk 37 升级时处理，风险 #8 不变）。

## 5. fakefx 协同效果（fakefx-removal-plan 阶段 0 核销）

FCL 侧 `import com.tungsten.fclcore.fakefx` 的文件数：**98 → 73**（−25 文件，−26%）。低于方案预估的 ~40%（98→55–60），原因：方案预估假定 `FXUtils` 39 个引用方全部随 6.1 消失，实际保留原生页面/控件数据层/setting 业务状态（均属"必须重写"类）也是 FXUtils 引用方，不随 6.1 消失。已消失的均为旧 View 绑定类引用，与方案阶段 0 目标一致；剩余 73 文件全部落在方案 1.5 表的「业务状态/控件/桥接/Compose 接缝」四类，无新增类别。

## 6. 遗留问题

1. **无真机验证**：本步全部验证为静态 grep + 构建门禁。删除的 else 分支均为编译期死代码（开关为 const true，Kotlin/Java 编译器本就不执行 else），理论行为不变，但发布前仍建议按 `final-report.md` §5 抽查主要链路（启动、账户、下载、版本管理、手柄弹窗）。
2. **注释级历史引用保留**：Compose 侧各 Screen/弹窗中「对齐遗留 XxxPage/XxxDialog」的 KDoc/注释有意保留（迁移档案价值），不构成代码引用。
3. **3 个 Callback 持有类**（§4.2 第 3 行）仍是 fakefx 时代的命名，fakefx 阶段 4（control/ 就地重写）时可一并收编。
4. **Compose 版 jumpToModPage 与遗留语义差异**：不再切换搜索页下载源 spinner，改为直接给详情页传正确仓库（语义等价，批 2 执行者已标注）。
5. **activity_main.xml 旧右侧栏 View 层**（account/start/jar 视图）在 Compose 模式下不可见但仍被 MainActivity 原生骨架部分使用（versionName/versionProgress），未进一步拆解——属 MainActivity 骨架迁移（Splash/主 Activity 单独立项）的范围。
6. **回滚方式变更**：自此清理后，逐点开关回滚不再可用；回归兜底为 git 历史（本步未提交，建议尽快 commit 并打 tag，如 `post-miuix-cleanup`）。
