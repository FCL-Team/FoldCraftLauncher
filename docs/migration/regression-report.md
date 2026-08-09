# 静态回归核查报告（阶段五 · 小步骤 5.1）

> 生成时间：2026-08-02 ｜ 分支：`feature/miuix-docs`（基于 `feature/miuix-migration`，含阶段一至四全部成果）
> 范围：对照 `docs/migration/ui-inventory.md` 的 142 个 layout 全量清单逐项核查迁移覆盖率；全仓 grep 核查旧 UI 类活跃调用点。
> **验证边界：本环境无真机，本报告全部为静态核查（源码阅读 + grep 核销 + 构建门禁）。5.2 性能基准、5.3 设备兼容性与一切真机功能测试均未执行，真机执行清单见 §5。**

---

## 1. 覆盖率总览

| 状态 | 含义 | activity_ | dialog_ | fragment_ | item_ | menu_ | page_ | ui_ | view_ | 合计 |
|---|---|---|---|---|---|---|---|---|---|---|
| ✅ 已 Compose 化（有开关，旧 XML 保留作回滚） | 调用点经 `USE_COMPOSE_*` 开关二选一 | 2 | 38 | 0 | 19 | 0 | 11 | 2 | 12 | **84** |
| 🟡 保留原生（决策保留，代码路径活跃） | 红线、容器壳、或评估后保留 | 4 | 1 | 2 | 15 | 2 | 16 | 6 | 9 | **55** |
| 🗑 疑似废弃（孤儿/空壳，待维护者确认删除） | 全仓无活跃引用 | 0 | 3 | 0 | 0 | 0 | 0 | 0 | 0 | **3** |
| **合计** | | 6 | 42 | 2 | 34 | 2 | 27 | 8 | 21 | **142** |

迁移覆盖率（已 Compose 化 / 总数）：**84 / 142 ≈ 59.2%**；若扣除红线与疑似废弃项，分母为「决策上应迁移」的 84 项时，覆盖率为 **84 / 84 = 100%**——即凡决策迁移的项均已带开关落地，无「应迁未迁」缺口（5.1 遗留 L1–L4 已修复后口径）。

补充事实：

- 迁移期新增了 1 个不在 142 清单内的布局 `page_compose_container.xml`（Compose 页面的 View 壳容器，被全部 Compose 页面/临时页壳类共用），属新增资产而非迁移对象。
- 「已 Compose 化」的旧 XML 与旧类**全部保留未删**（回滚路径），6.1 清理候选清单见 `final-report.md` §6。
- `activity_main.xml` 计入「保留原生」：MainActivity 外壳（VideoView 动态背景、左侧 FCLMenuView 导航、FCLUILayout 容器）保留原生，右侧账户/启动栏与一级界面内容区经 `USE_COMPOSE_MAIN_UI` 开关 Compose 化嵌入，属「原生骨架 + Compose 嵌入」的混合形态。

---

## 2. 142 个 layout 逐项状态表

状态图例：✅ = 已 Compose 化（开关名见「开关/依据」列，当前值除特别标注外均为 `true`）；🟡 = 保留原生（活跃）；⛔ = 保留原生（红线/红线邻近）；🗑 = 疑似废弃。

### 2.1 activity_（6）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `activity_splash.xml` | 🟡 | SplashActivity 保留原生（activity-migration.md §1：启动链路 + EULA/RuntimeFragment 宿主，未迁） |
| `activity_main.xml` | 🟡 | MainActivity 原生骨架 + Compose 嵌入；内容区开关 `ComposeMainUI.USE_COMPOSE_MAIN_UI` |
| `activity_web.xml` | ✅ | `ComposeActivities.USE_COMPOSE_WEB`（回滚路径在 WebActivity.kt onCreate 内） |
| `activity_shell.xml` | ✅ | `ComposeActivities.USE_COMPOSE_SHELL`（同上） |
| `activity_jvm.xml` | ⛔ | JVMActivity 红线邻近，仅系统栏主题色对齐（activity-migration.md §1） |
| `activity_jvm_crash.xml` | 🟡 | JVMCrashActivity 独立 `:crash` 进程崩溃兜底页，决策保留 |

### 2.2 dialog_（42）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `dialog_add_authlib_injector_server.xml` | ✅ | `USE_COMPOSE_ADD_AUTHLIB_INJECTOR_SERVER` → MiuixAddAuthlibInjectorServerDialog |
| `dialog_add_button_style.xml` | ✅ | `USE_COMPOSE_ADD_BUTTON_STYLE` → MiuixAddButtonStyleDialog |
| `dialog_add_direction_style.xml` | ✅ | `USE_COMPOSE_ADD_DIRECTION_STYLE` → MiuixAddDirectionStyleDialog |
| `dialog_add_input_text.xml` | ✅ | `USE_COMPOSE_ADD_INPUT_TEXT` → MiuixAddInputTextDialog |
| `dialog_add_profile.xml` | ✅ | `USE_COMPOSE_ADD_PROFILE` → MiuixAddProfileDialog |
| `dialog_character_selector.xml` | ✅ | `USE_COMPOSE_CHARACTER_SELECTOR` → MiuixCharacterSelectorDialog |
| `dialog_controller_info.xml` | ✅ | `USE_COMPOSE_CONTROLLER_INFO` → MiuixControllerInfoDialog |
| `dialog_controller_upload.xml` | ✅ | `USE_COMPOSE_CONTROLLER_UPLOAD` → MiuixControllerUploadDialog |
| `dialog_create_account.xml` | ✅ | `USE_COMPOSE_CREATE_ACCOUNT` → MiuixCreateAccountDialog |
| `dialog_download_addon.xml` | ✅ | `USE_COMPOSE_DOWNLOAD_ADDON` → MiuixDownloadAddonDialog |
| `dialog_download_controllor.xml` | ✅ | `USE_COMPOSE_CONTROLLER_OLD_VERSION` → MiuixOldVersionDialog（OldVersionDialog 的布局） |
| `dialog_duplicate_version.xml` | ✅ | `USE_COMPOSE_DUPLICATE_VERSION` → MiuixDuplicateVersionDialog |
| `dialog_edit_view.xml` | ✅ | `USE_COMPOSE_EDIT_VIEW` → MiuixEditViewDialog |
| `dialog_edit_view_group.xml` | ✅ | `USE_COMPOSE_EDIT_VIEW_GROUP` → MiuixEditViewGroupDialog |
| `dialog_gamepad_map.xml` | ✅ | `USE_COMPOSE_GAMEPAD_MAP` → MiuixGamepadMapDialog |
| `dialog_input_invite_code.xml` | ✅ | `USE_COMPOSE_INVITE_CODE` → MiuixInviteCodeInputDialog |
| `dialog_itembar_setting.xml` | ✅ | `USE_COMPOSE_ITEMBAR_SETTING` → MiuixGameItemBarSettingDialog |
| `dialog_manage_button_style.xml` | ✅ | `USE_COMPOSE_BUTTON_STYLE` → MiuixButtonStyleDialog |
| `dialog_manage_direction_style.xml` | ✅ | `USE_COMPOSE_DIRECTION_STYLE` → MiuixDirectionStyleDialog |
| `dialog_manage_java.xml` | ✅ | `USE_COMPOSE_JAVA_MANAGE` → MiuixJavaManageDialog |
| `dialog_manage_view_groups.xml` | ✅ | `USE_COMPOSE_VIEW_GROUP` → MiuixViewGroupDialog |
| `dialog_mod_info.xml` | ✅ | `USE_COMPOSE_MOD_INFO` → MiuixModInfoDialog |
| `dialog_modpack_selection.xml` | 🗑 | 空壳布局；唯一引用方 `ui/version/ModpackSelectionDialog.java` 全仓无实例化（孤儿类） |
| `dialog_modpack_url.xml` | ✅ | `USE_COMPOSE_MODPACK_URL` → MiuixModpackUrlDialog |
| `dialog_multiplyer_menu.xml` | 🟡 | **唯一 false 开关** `USE_COMPOSE_MULTIPLAYER_MENU = false`：联机菜单 7 状态 View 体系保留（ComposeDialogs.kt:40-46 注明理由） |
| `dialog_offline_account_skin.xml` | ✅ | `USE_COMPOSE_OFFLINE_ACCOUNT_SKIN` → MiuixOfflineAccountSkinDialog |
| `dialog_open_folder.xml` | ✅ | `USE_COMPOSE_OPEN_FOLDER` → MiuixOpenFolderDialog |
| `dialog_quick_input.xml` | ✅ | `USE_COMPOSE_QUICK_INPUT` → MiuixQuickInputDialog |
| `dialog_relogin_classic.xml` | 🗑 | 空布局，全仓无引用（ui-inventory §1.2 已标记） |
| `dialog_relogin_oauth.xml` | ✅ | `USE_COMPOSE_RELOGIN_OAUTH` → MiuixOAuthAccountLoginDialog |
| `dialog_rename_version.xml` | ✅ | `USE_COMPOSE_RENAME_VERSION` → MiuixRenameVersionDialog |
| `dialog_rollback_mod.xml` | ✅ | `USE_COMPOSE_ROLLBACK_MOD` → MiuixModRollbackDialog |
| `dialog_select_controller.xml` | ✅ | `USE_COMPOSE_SELECT_CONTROLLER` → MiuixSelectControllerDialog |
| `dialog_select_keycode.xml` | ✅ | `USE_COMPOSE_SELECT_KEYCODE` → MiuixSelectKeycodeDialog（Miuix 外壳 + AndroidView 包装原生键盘） |
| `dialog_select_renderer.xml` | ✅ | `USE_COMPOSE_RENDERER_SELECT` → MiuixRendererSelectDialog |
| `dialog_skip_login.xml` | ✅ | `USE_COMPOSE_SKIP_LOGIN` → MiuixLoginPromptDialogs |
| `dialog_task.xml` | ✅ | `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG` → MiuixTaskDialog/FCLTaskDialog（LauncherHelper 启动进度点已随 L4 接入，其余触发点核销见 §4.2） |
| `dialog_tip_relogin.xml` | ✅ | `USE_COMPOSE_TIP_RELOGIN` → MiuixLoginPromptDialogs |
| `dialog_translation.xml` | ✅ | `USE_COMPOSE_TRANSLATION` → MiuixTranslationDialog |
| `dialog_update.xml` | ✅ | `USE_COMPOSE_UPDATE` → MiuixUpdateDialog |
| `dialog_world_export.xml` | ✅ | `USE_COMPOSE_WORLD_EXPORT` → MiuixWorldExportDialog |
| `dialog_world_name.xml` | 🗑 | 孤儿文件，全仓（java/kt/xml）无引用（ui-inventory §1.2 已标记） |

### 2.3 fragment_（2）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `fragment_eula.xml` | 🟡 | EulaFragment 随 SplashActivity 保留原生（activity-migration.md §5 遗留 1：需单独立项） |
| `fragment_runtime.xml` | 🟡 | RuntimeFragment 同上 |

### 2.4 item_（34）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `item_account.xml` | ✅ | 随 AccountUI 迁移（`USE_COMPOSE_ACCOUNT_UI`；AccountListAdapter 仅回滚可达） |
| `item_article.xml` | ✅ | 随 HelpPage 迁移（`USE_COMPOSE_SETTING_PAGES`） |
| `item_authlib_injector_server.xml` | ✅ | 随 ServerListAdapter 所属弹窗迁移（`USE_COMPOSE_CREATE_ACCOUNT` 等） |
| `item_button_style.xml` | ✅ | 随 ButtonStyleDialog 迁移（`USE_COMPOSE_BUTTON_STYLE`） |
| `item_character.xml` | ✅ | 随 CreateAccountDialog 迁移（`USE_COMPOSE_CREATE_ACCOUNT`） |
| `item_controller_editable.xml` | 🟡 | ControllerManagePage 保留原生（手柄域页面未迁，决策见 §2.6 说明） |
| `item_controller_selectable.xml` | ✅ | 随 SelectControllerDialog 迁移（`USE_COMPOSE_SELECT_CONTROLLER`） |
| `item_datapack.xml` | 🟡 | DatapackListPage 保留原生（存档详情链路未迁） |
| `item_direction_style.xml` | ✅ | 随 DirectionStyleDialog 迁移（`USE_COMPOSE_DIRECTION_STYLE`） |
| `item_gamepad_map.xml` | ✅ | 随 GamepadMapDialog 迁移（`USE_COMPOSE_GAMEPAD_MAP`） |
| `item_input_text.xml` | ✅ | 随 QuickInputDialog/AddInputTextDialog 迁移（`USE_COMPOSE_QUICK_INPUT` 等） |
| `item_local_mod.xml` | 🟡 | ModListPage 保留原生（Mod 管理页未迁） |
| `item_manage.xml` | ✅ | 随 ManagePage 迁移（`USE_COMPOSE_VERSION_PAGES` → ManageScreen） |
| `item_manage_java.xml` | ✅ | 随 JavaManageDialog 迁移（`USE_COMPOSE_JAVA_MANAGE`） |
| `item_mod_version.xml` | ✅ | 随 RemoteModVersionPage 迁移（`USE_COMPOSE_DOWNLOAD_PAGES`） |
| `item_profile.xml` | ✅ | 随 VersionListPage 迁移（`USE_COMPOSE_VERSION_PAGES`） |
| `item_remote_mod.xml` | ✅ | 随 DownloadPage 迁移（`USE_COMPOSE_DOWNLOAD_PAGES`） |
| `item_remote_version.xml` | 🟡 | ControllerListAdapter（ControllerRepoPage）保留原生活跃 |
| `item_renderer.xml` | ✅ | 随 DriverSelectDialog 迁移（5.1 遗留 L1 已修复：`USE_COMPOSE_DRIVER_SELECT` → MiuixDriverSelectDialog；旧实现与 `USE_COMPOSE_RENDERER_SELECT` 共用此布局） |
| `item_screenshot_path.xml` | ✅ | 随 ControllerUploadDialog 迁移（`USE_COMPOSE_CONTROLLER_UPLOAD`） |
| `item_spinner.xml` | 🟡 | FCLSpinner 项，保留原生页面/游戏侧活跃（GameMenu、EditViewDialog 回滚等） |
| `item_spinner_auto_tint.xml` | 🟡 | 同上（WorldInfoPage、ControllerRepoPage、ModpackInfoPage 等活跃） |
| `item_spinner_dropdown.xml` | 🟡 | 同上，且 MiuixControllerUploadDialog（Compose 侧）仍复用 |
| `item_spinner_dropdown_small.xml` | 🟡 | GameMenu（游戏侧红线）活跃 |
| `item_spinner_small.xml` | 🟡 | GameMenu（游戏侧红线）活跃 |
| `item_spinner_theme_color.xml` | 🟡 | MiuixControllerUploadDialog（Compose 侧）仍复用 |
| `item_task_progress.xml` | 🟡 | TaskListPane：9 处活跃遗留 TaskDialog 调用点仍在用（§4.2） |
| `item_task_stage.xml` | 🟡 | 同上 |
| `item_terracotta_profile.xml` | 🟡 | MultiplayerDialog 保留原生（`USE_COMPOSE_MULTIPLAYER_MENU = false`） |
| `item_translation.xml` | ✅ | 随 TranslationDialog 迁移（`USE_COMPOSE_TRANSLATION`） |
| `item_update_mod.xml` | 🟡 | ModUpdatesPage 保留原生（Mod 管理链路未迁） |
| `item_version.xml` | ✅ | 随 VersionListPage 迁移（`USE_COMPOSE_VERSION_PAGES`） |
| `item_view_group.xml` | ✅ | 随 ViewGroupDialog 迁移（`USE_COMPOSE_VIEW_GROUP`） |
| `item_world.xml` | 🟡 | WorldListPage 保留原生（存档管理页未迁） |

### 2.5 menu_（2）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `menu_left.xml` | ⛔ | 游戏内悬浮菜单，红线（ui-inventory §1.5，不迁移决策） |
| `menu_right.xml` | ⛔ | 同上 |

### 2.6 page_（27）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `page_controller_download.xml` | 🟡 | 手柄域页面保留原生（仅其弹窗已迁）；ControllerRepoPage → ControllerDownloadPage 活跃 |
| `page_controller_manager.xml` | 🟡 | 同上（ControllerPageManager 装配，无开关） |
| `page_controller_repo.xml` | 🟡 | 同上 |
| `page_controller_upload.xml` | 🟡 | 同上（ControllerManagePage → ControllerUploadPage 活跃） |
| `page_datapack_list.xml` | 🟡 | 存档详情链路保留原生（WorldListItem → DatapackListPage 活跃） |
| `page_download.xml` | ✅ | `USE_COMPOSE_DOWNLOAD_PAGES`（DownloadPage 5 子类 → ComposeDownloadPage） |
| `page_download_addon.xml` | ✅ | 同上（RemoteModDownloadPage 回滚可达） |
| `page_download_addon_info.xml` | ✅ | 同上（RemoteModInfoPage 回滚可达） |
| `page_download_addon_version.xml` | ✅ | 同上（RemoteModVersionPage 回滚可达） |
| `page_install_version.xml` | 🟡 | download/version/InstallerListPage 保留原生活跃（manage InstallerListPage.java:113 拉起）；VersionInstallPage 侧为回滚路径 |
| `page_installer.xml` | ✅ | 随 VersionInstallPage 迁移（`USE_COMPOSE_DOWNLOAD_PAGES`，回滚可达） |
| `page_manage_auto_install.xml` | 🟡 | manage/InstallerListPage 保留原生（ManagePageManager 装配，无开关） |
| `page_manage_mod.xml` | 🟡 | ModListPage 保留原生（同上） |
| `page_manage_version.xml` | ✅ | `USE_COMPOSE_VERSION_PAGES`（ManagePage → ComposeManagePage） |
| `page_manage_world.xml` | 🟡 | WorldListPage 保留原生（ManagePageManager 装配，无开关） |
| `page_manage_world_info.xml` | 🟡 | WorldInfoPage 保留原生（WorldListItem 拉起，活跃） |
| `page_mod_update.xml` | 🟡 | ModUpdatesPage 保留原生（ModListPage 拉起，活跃） |
| `page_modpack.xml` | 🟡 | 整合包向导保留原生（Versions.importModpack/downloadModpackImpl、MainActivity:945 导入 Intent 拉起，活跃，Compose 流亦走此链路） |
| `page_modpack_file.xml` | 🟡 | 同上（ModpackInfoPage → ModpackFileSelectionPage） |
| `page_modpack_info.xml` | 🟡 | 同上（ModpackTypeSelectionPage → ModpackInfoPage） |
| `page_modpack_selection.xml` | 🟡 | 同上（向导第 1 步，双 PageManager 分支为脆弱点，interaction-map §559 标记「高」） |
| `page_modpack_type.xml` | 🟡 | 同上（Versions.exportVersion 拉起） |
| `page_setting_about.xml` | ✅ | `USE_COMPOSE_SETTING_PAGES`（AboutPage → AboutScreen） |
| `page_setting_help.xml` | ✅ | `USE_COMPOSE_SETTING_PAGES`（HelpPage → HelpScreen） |
| `page_setting_launcher.xml` | ✅ | `USE_COMPOSE_SETTING_PAGES`（LauncherSettingPage → LauncherSettingScreen） |
| `page_version_list.xml` | ✅ | `USE_COMPOSE_VERSION_PAGES`（VersionListPage → ComposeVersionListPage） |
| `page_version_setting.xml` | ✅ | `USE_COMPOSE_VERSION_SETTING`（VersionSettingPage → ComposeVersionSettingPage，17 组 fakefx 绑定承接） |

> 说明：手柄域 4 页、管理域 Mod/存档/安装器 3 页、整合包安装向导 6 页（含数据包/存档详情/Mod 更新 3 个临时页）为**评估后保留原生**的活跃页面——它们全部可从 Compose 页面（ComposeManagePage、ComposeDownloadPage）经遗留 PageManager 临时页栈到达，新旧页面混跳是刻意设计（同 PageManager 栈内跳转，见 ComposeDownloadPages.kt 注释），不是遗漏。

### 2.7 ui_（8）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `ui_main.xml` | ✅ | `USE_COMPOSE_MAIN_UI`（MainUI → ComposeMainUI） |
| `ui_account.xml` | ✅ | `USE_COMPOSE_ACCOUNT_UI`（AccountUI → ComposeAccountUI） |
| `ui_version.xml` | 🟡 | VersionUI 容器壳保留原生（其页面内容已由 `USE_COMPOSE_VERSION_PAGES` Compose 化） |
| `ui_manage.xml` | 🟡 | ManageUI 容器壳保留原生（同上） |
| `ui_download.xml` | 🟡 | DownloadUI 容器壳保留原生（同上） |
| `ui_controller.xml` | 🟡 | ControllerUI 容器壳保留原生（手柄域页面未迁） |
| `ui_multiplayer.xml` | 🟡 | MultiplayerUI 保留原生（P2 Terracotta 状态机多视图，ui-inventory 建议最后/保留） |
| `ui_setting.xml` | 🟡 | SettingUI 容器壳保留原生（其页面内容已 Compose 化） |

### 2.8 view_（21）

| 文件 | 状态 | 开关/依据 |
|---|---|---|
| `view_button_style.xml` | ✅ | 随 AddButtonStyleDialog 迁移（`USE_COMPOSE_ADD_BUTTON_STYLE`） |
| `view_create_account_external.xml` | ✅ | 随 CreateAccountDialog 迁移（`USE_COMPOSE_CREATE_ACCOUNT`） |
| `view_create_account_microsoft.xml` | ✅ | 同上 |
| `view_create_account_offline.xml` | ✅ | 同上 |
| `view_direction_style_button.xml` | ✅ | 随 AddDirectionStyleDialog 迁移（`USE_COMPOSE_ADD_DIRECTION_STYLE`） |
| `view_direction_style_rocker.xml` | ✅ | 同上 |
| `view_edit_button_event.xml` | ✅ | 随 EditViewDialog 迁移（`USE_COMPOSE_EDIT_VIEW`） |
| `view_edit_button_event_child.xml` | ✅ | 同上 |
| `view_edit_button_info.xml` | ✅ | 同上 |
| `view_edit_direction_event.xml` | ✅ | 同上 |
| `view_edit_direction_info.xml` | ✅ | 同上 |
| `view_game_menu.xml` | ⛔ | GameMenu 游戏内菜单骨架，红线保留 |
| `view_installer_item.xml` | 🟡 | InstallerItem：manage/InstallerListPage 保留原生活跃（VersionInstallInfoPage 侧为回滚） |
| `view_jar_executor_menu.xml` | ⛔ | JarExecutorMenu 游戏侧 JAR 执行器菜单，红线保留 |
| `view_keyboard.xml` | ⛔ | 全键盘视图红线保留，经 AndroidView 包装进 MiuixSelectKeycodeDialog（触摸链零改动） |
| `view_mod_screenshot.xml` | ✅ | 随 RemoteModInfoPage 迁移（`USE_COMPOSE_DOWNLOAD_PAGES`） |
| `view_multiplayer_exception.xml` | 🟡 | MultiplayerDialog 7 状态视图保留原生（`USE_COMPOSE_MULTIPLAYER_MENU = false`） |
| `view_multiplayer_host_guest_ok.xml` | 🟡 | 同上 |
| `view_multiplayer_scanning.xml` | 🟡 | 同上 |
| `view_multiplayer_starting.xml` | 🟡 | 同上 |
| `view_multiplayer_waiting.xml` | 🟡 | 同上 |

---

## 3. Compose 开关全量清单（49 个，48 true / 1 false）

| # | 开关 | 当前值 | 位置 | 旧实现（回滚路径） |
|---|---|---|---|---|
| 1 | `ComposeDialogs.USE_COMPOSE_TRANSLATION` | true | `ui/compose/dialog/ComposeDialogs.kt:11` | TranslationDialog + dialog_translation/item_translation |
| 2 | `USE_COMPOSE_UPDATE` | true | ComposeDialogs.kt:12 | UpdateDialog + dialog_update |
| 3 | `USE_COMPOSE_DOWNLOAD_ADDON` | true | ComposeDialogs.kt:13 | DownloadAddonDialog（DownloadPage 内嵌）+ dialog_download_addon |
| 4 | `USE_COMPOSE_CONTROLLER_OLD_VERSION` | true | ComposeDialogs.kt:14 | OldVersionDialog + dialog_download_controllor |
| 5 | `USE_COMPOSE_MODPACK_URL` | true | ComposeDialogs.kt:15 | ModpackUrlDialog + dialog_modpack_url |
| 6 | `USE_COMPOSE_DUPLICATE_VERSION` | true | ComposeDialogs.kt:16 | DuplicateVersionDialog + dialog_duplicate_version |
| 7 | `USE_COMPOSE_RENAME_VERSION` | true | ComposeDialogs.kt:17 | RenameVersionDialog + dialog_rename_version |
| 8 | `USE_COMPOSE_WORLD_EXPORT` | true | ComposeDialogs.kt:18 | WorldExportDialog + dialog_world_export |
| 9 | `USE_COMPOSE_SKIP_LOGIN` | true | ComposeDialogs.kt:19 | LauncherHelper:622 内联 FCLDialog + dialog_skip_login |
| 10 | `USE_COMPOSE_TIP_RELOGIN` | true | ComposeDialogs.kt:20 | LauncherHelper:664 内联 FCLDialog + dialog_tip_relogin |
| 11 | `USE_COMPOSE_ADD_PROFILE` | true | ComposeDialogs.kt:23 | AddProfileDialog + dialog_add_profile |
| 12 | `USE_COMPOSE_MOD_INFO` | true | ComposeDialogs.kt:24 | ModInfoDialog + dialog_mod_info |
| 13 | `USE_COMPOSE_ROLLBACK_MOD` | true | ComposeDialogs.kt:25 | ModRollbackDialog + dialog_rollback_mod |
| 14 | `USE_COMPOSE_JAVA_MANAGE` | true | ComposeDialogs.kt:26 | JavaManageDialog + dialog_manage_java/item_manage_java |
| 15 | `USE_COMPOSE_RENDERER_SELECT` | true | ComposeDialogs.kt:27 | RendererSelectDialog + dialog_select_renderer |
| 16 | `USE_COMPOSE_GAMEPAD_MAP` | true | ComposeDialogs.kt:28 | GamepadMapDialog + dialog_gamepad_map/item_gamepad_map |
| 17 | `USE_COMPOSE_ITEMBAR_SETTING` | true | ComposeDialogs.kt:29 | GameItemBarSettingDialog + dialog_itembar_setting |
| 18 | `USE_COMPOSE_OPEN_FOLDER` | true | ComposeDialogs.kt:30 | OpenFolderDialog + dialog_open_folder |
| 19 | `USE_COMPOSE_INVITE_CODE` | true | ComposeDialogs.kt:31 | MultiplayerDialog:283 内联 + dialog_input_invite_code |
| 20 | `USE_COMPOSE_SELECT_KEYCODE` | true | ComposeDialogs.kt:38 | SelectKeycodeDialog + dialog_select_keycode（view_keyboard 红线保留） |
| 21 | `USE_COMPOSE_MULTIPLAYER_MENU` | **false** | ComposeDialogs.kt:46 | MultiplayerDialog + dialog_multiplyer_menu + 6 个 view_multiplayer_*（**当前即旧实现**，有意保留） |
| 22 | `USE_COMPOSE_CREATE_ACCOUNT` | true | ComposeDialogs.kt:49 | CreateAccountDialog + dialog_create_account + 3 个 view_create_account_* |
| 23 | `USE_COMPOSE_CHARACTER_SELECTOR` | true | ComposeDialogs.kt:56 | CreateAccountDialog.CharacterSelector + dialog_character_selector/item_character |
| 24 | `USE_COMPOSE_ADD_AUTHLIB_INJECTOR_SERVER` | true | ComposeDialogs.kt:57 | AddAuthlibInjectorServerDialog + dialog_add_authlib_injector_server |
| 25 | `USE_COMPOSE_RELOGIN_OAUTH` | true | ComposeDialogs.kt:58 | OAuthAccountLoginDialog + dialog_relogin_oauth |
| 26 | `USE_COMPOSE_OFFLINE_ACCOUNT_SKIN` | true | ComposeDialogs.kt:59 | OfflineAccountSkinDialog + dialog_offline_account_skin |
| 27 | `USE_COMPOSE_QUICK_INPUT` | true | ComposeDialogs.kt:60 | QuickInputDialog + dialog_quick_input/item_input_text |
| 28 | `USE_COMPOSE_ADD_INPUT_TEXT` | true | ComposeDialogs.kt:61 | AddInputTextDialog + dialog_add_input_text |
| 29 | `USE_COMPOSE_ADD_BUTTON_STYLE` | true | ComposeDialogs.kt:64 | AddButtonStyleDialog + dialog_add_button_style/view_button_style |
| 30 | `USE_COMPOSE_ADD_DIRECTION_STYLE` | true | ComposeDialogs.kt:65 | AddDirectionStyleDialog + dialog_add_direction_style + 2 个 view_direction_style_* |
| 31 | `USE_COMPOSE_BUTTON_STYLE` | true | ComposeDialogs.kt:66 | ButtonStyleDialog + dialog_manage_button_style/item_button_style |
| 32 | `USE_COMPOSE_DIRECTION_STYLE` | true | ComposeDialogs.kt:67 | DirectionStyleDialog + dialog_manage_direction_style/item_direction_style |
| 33 | `USE_COMPOSE_EDIT_VIEW` | true | ComposeDialogs.kt:68 | EditViewDialog + dialog_edit_view + 6 个 view_edit_* |
| 34 | `USE_COMPOSE_EDIT_VIEW_GROUP` | true | ComposeDialogs.kt:69 | EditViewGroupDialog + dialog_edit_view_group |
| 35 | `USE_COMPOSE_VIEW_GROUP` | true | ComposeDialogs.kt:70 | ViewGroupDialog + dialog_manage_view_groups/item_view_group |
| 36 | `USE_COMPOSE_SELECT_CONTROLLER` | true | ComposeDialogs.kt:71 | SelectControllerDialog + dialog_select_controller/item_controller_selectable |
| 37 | `USE_COMPOSE_CONTROLLER_INFO` | true | ComposeDialogs.kt:72 | ControllerInfoDialog + dialog_controller_info |
| 38 | `USE_COMPOSE_CONTROLLER_UPLOAD` | true | ComposeDialogs.kt:73 | ControllerUploadDialog + dialog_controller_upload/item_screenshot_path |
| 39 | `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG` | true | `ui/compose/MiuixTaskDialog.kt:44` | TaskDialog + dialog_task（9 处活跃遗留调用点未接开关，见 §4.2） |
| 40 | `ComposeVersionPages.USE_COMPOSE_VERSION_PAGES` | true | `ui/version/compose/ComposeVersionPages.kt:13` | VersionListPage + ManagePage + page_version_list/page_manage_version |
| 41 | `ComposeVersionPages.USE_COMPOSE_VERSION_SETTING` | true | ComposeVersionPages.kt:23 | VersionSettingPage + page_version_setting |
| 42 | `SettingPageManager.USE_COMPOSE_SETTING_PAGES` | true | `ui/setting/SettingPageManager.kt:36`（private） | LauncherSettingPage/HelpPage/AboutPage + 3 个 page_setting_* |
| 43 | `ComposeDownloadPages.USE_COMPOSE_DOWNLOAD_PAGES` | true | `ui/download/compose/ComposeDownloadPages.kt:24` | VersionInstallPage + DownloadPage 5 子类 + RemoteMod* 临时页 + 6 个 page_download*/page_install* |
| 44 | `ComposeAccountUI.USE_COMPOSE_ACCOUNT_UI` | true | `ui/account/compose/ComposeAccountUI.kt:44` | AccountUI + ui_account/item_account |
| 45 | `ComposeMainUI.USE_COMPOSE_MAIN_UI` | true | `ui/main/compose/ComposeMainUI.kt:48` | MainUI + ui_main |
| 46 | `ComposeActivities.USE_COMPOSE_WEB` | true | `activity/compose/ComposeActivities.kt:16` | WebActivity View 路径 + activity_web |
| 47 | `ComposeActivities.USE_COMPOSE_SHELL` | true | ComposeActivities.kt:17 | ShellActivity View 路径 + activity_shell |
| 48 | `ComposeDialogs.USE_COMPOSE_DRIVER_SELECT` | true | ComposeDialogs.kt:28（5.1 遗留 L1 修复新增） | DriverSelectDialog + item_renderer |
| 49 | `ComposeDialogs.USE_COMPOSE_VERSION_OP_ALERTS` | true | ComposeDialogs.kt:36（5.1 遗留 L2/L3 修复新增） | Versions.java 内 3 处 FCLAlertDialog + deleteVersion 的 ProgressDialog |

> 合计：ComposeDialogs 42 + MiuixTaskDialog 1 + ComposeVersionPages 2 + SettingPageManager 1 + ComposeDownloadPages 1 + ComposeAccountUI 1 + ComposeMainUI 1 + ComposeActivities 2 = **51 个开关，50 个 true，1 个 false（#21 联机菜单，有意保留原生）**。

---

## 4. 旧 UI 类活跃调用点核查（全仓 grep）

### 4.1 FCLAlertDialog 剩余调用点统计与归类

全仓 `FCLAlertDialog.Builder` 共 **167 处，分布 52 个文件**。逐文件按「调用点在当前开关值（全 true）下是否活跃」归类：

| 类别 | 文件数 | 处数（约） | 性质 |
|---|---|---|---|
| A. 回滚分支 / 回滚专用类（开关 false 才可达，预期保留） | 25 | ≈70 | 双分支 else 侧（Versions、WorldExportDialog、AccountListItem、JavaManageDialog、OAuthAccountLoginDialog、CreateAccountDialog、ServerListAdapter、OpenFolderDialog、VersionSettingPage、LauncherSettingPage、DownloadPage、UpdateDialog、ButtonStyleAdapter、DirectionStyleAdapter、ViewGroupAdapter 等） |
| B. 保留原生页面/游戏侧（决策保留，活跃，正常） | 17 | ≈75 | ModListPage(10)、ModUpdatesPage(6)、InstallerListPage(6)、LocalModpackPage(6)、ModpackInstaller(14)、ModpackFileSelectionPage(4)、DatapackListPage(4)、ControllerRepoPage(4)、ControllerDownloadPage(4)、RemoteModpackPage(4)、WorldListPage/WorldListItem(5)、ManagePage(2)、MultiplayerUI(2)、GameMenu(2)、JarExecutorMenu(2)、Controller(setting,4)、EditableControllerListAdapter(2) |
| C. Compose 侧桥接回退（单槽位占用时的刻意回退，bridge-api §2.3 设计） | 6 | ≈10 | LauncherSettingHost(1)、RemoteModActions(1)、AccountScreen(1)、FCLComposeDialog(1)、MiuixJavaManageDialog(5)、MiuixCreateAccountDialog(2) 等 Miuix 弹窗内部失败提示 |
| D. ~~迁移流中的未迁移遗留（无开关，Compose 前台仍弹旧窗）~~ **已清零（L1–L4 修复后）** | 0 | 0 | 原 4 项遗留已全部接入开关，见下方清单 |
| E. 启动链路（Splash/运行时，保留原生域，活跃正常） | 3 | ≈5 | SplashActivity(2)、RuntimeFragment(1)、MainActivity(1)、LauncherHelper(10)（LauncherHelper 属启动链路保留原生） |

**原 D 类清单（L1–L4，现已全部修复，均带独立开关、旧实现保留可回滚）：**

1. ~~`Versions.java:119-131` — deleteVersion 删除版本二次确认弹窗~~ → **L2 已修复**：确认弹窗与删除进度弹窗接 `ComposeDialogs.USE_COMPOSE_VERSION_OP_ALERTS` 双分支（Miuix 侧走 `FCLDialogs.showAlert` / `FCLDialogs.showProgress`）；删除动作（removeVersionFromDisk + UI 线程 dismiss 进度）原样保留，调用方刷新契约不变。
2. ~~`Versions.java:257-266` — checkVersionForLaunching「未选择版本」提示~~ → **L3 已修复**：同开关双分支；Miuix 侧单按钮（文案同遗留 `dialog_positive`），点击后 refreshMenuView + 跳转下载页动作逐行对齐。
3. ~~`Versions.java:73-79 / 91-97` — downloadModpackImpl 下载失败弹窗~~ → **L3 已修复**：两处失败提示提取为 `showModpackDownloadFailed`，同开关双分支；取消 Toast 与成功进 LocalModpackPage 链路未动。
4. ~~`com/mio/ui/dialog/DriverSelectDialog.kt` — 驱动选择弹窗（对应 item_renderer）~~ → **L1 已修复**：新增 `ui/compose/dialog/MiuixDriverSelectDialog.kt`，开关 `ComposeDialogs.USE_COMPOSE_DRIVER_SELECT`；两个调用点（Compose 侧 VersionSettingHost.kt:60、回滚侧 VersionSettingPage.kt:541）均已接双分支。语义对齐：选中项写 `VersionSetting.driver` + `DriverPlugin.selected`，dismiss 后以驱动名回调；取消仅 dismiss。

### 4.2 TaskDialog 剩余触发点核销（对照 bridge-api.md §6.1 / interaction-map G5）

G5 登记 19 处触发点。经逐点 grep 核实：3.2/3.4 已接开关替换 **8 处**（Versions×2、WorldExportDialog、DownloadPage、RemoteModVersionPage、InstallerListPage×2、ModpackFileSelectionPage）。
> 计数勘误：bridge-api §6.1 记为「替换 7 处、其余 12 处」，但其「3.4 替换 4 处」将 InstallerListPage 两处合并计数；按 G5 触发点逐一枚举，实际已替换 8 处、**剩余 11 处**（bridge-api 自己列举的"其余 12 处"清单实际也只列出 11 个触发点）。本报告以实际枚举的 11 处为准：

| # | 触发点 | 当前状态 | 核销结论 |
|---|---|---|---|
| 1 | `LauncherHelper.java:133`（启动游戏进度） | ~~活跃遗留，无开关~~ **已接开关（L4 修复）** | 已接 `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG` 双分支。勘误：bridge-api §6.1 预警的 `titleProperty()` 绑定 + setCancel 动态切换在当前源码中并不存在——该调用点实际仅用 `setTitle`（静态）+ `TaskCancellationAction.NORMAL`（= no-op，与 `MiuixTaskDialog(context)` 默认取消动作一致）+ `setExecutor(executor, false)` + onStop 手动 dismiss，MiuixTaskDialog 既有 API 全部承接，无需新增 titleProperty 能力 |
| 2 | `ModpackInstaller.java:35` | 活跃（保留原生向导内） | 页面本身保留原生，旧 TaskDialog 与页面同体系，一致，无需处理 |
| 3 | `ModpackSelectionPage.java:98` | 活跃（同上） | 同上 |
| 4 | `ModpackSelectionPage.java:125` | 活跃（同上） | 同上 |
| 5 | `VersionInstallInfoPage.java:226` | 仅回滚可达（`USE_COMPOSE_DOWNLOAD_PAGES=true` 时由 VersionInstallInfoScreen 替代，其内部 :235/:240 双分支已接开关） | 已核销（回滚路径） |
| 6 | `ModListPage.java:398`（检查 Mod 更新） | **活跃遗留，无开关** | ModListPage 保留原生，弹窗与页面同体系，一致；如未来迁 Mod 管理页需同步接开关 |
| 7 | `ModUpdatesPage.java:120` | 活跃（保留原生页内） | 同 #6 |
| 8 | `ModUpdatesPage.java:152` | 活跃（保留原生页内） | 同 #6 |
| 9 | `ControllerRepoPage.java:261` | 活跃（保留原生页内） | 手柄域页面保留原生，一致 |
| 10 | `ControllerDownloadPage.java:199` | 活跃（保留原生页内） | 同上 |
| 11 | `UpdateDialog.java:121` | 仅回滚可达（`USE_COMPOSE_UPDATE=true` 时 UpdateChecker:93 走 MiuixUpdateDialog，其内部 :139/:144 双分支已接开关） | 已核销（回滚路径） |

结论：剩余 11 处中 **3 处已核销**（#5/#11 仅回滚路径可达，#1 LauncherHelper 已接 `USE_COMPOSE_TASK_DIALOG` 双分支，L4 修复），**8 处活跃但与保留原生页面同体系、新旧一致无需处理**；跨体系遗留已清零。

### 4.3 其他旧 UI 类调用点抽查

- `FCLDialog` 子类旧实现（38 个）全部仅回滚可达或保留原生域内使用，无跨体系遗漏（§2.2 逐一对照）。
- ~~`DriverSelectDialog` 为唯一发现的「应迁未迁」弹窗~~ → L1 已修复（§4.1-D4），「应迁未迁」弹窗清零。
- `ProgressDialog`（FCLLibrary）：Versions.deleteVersion/renameVersion/duplicateVersion 内部仍在用；rename/duplicate 已被 Miuix 弹窗包裹（进度在 Miuix 弹窗语义内），deleteVersion 已随 L2 接双分支（Miuix 侧走 `FCLDialogs.showProgress`）。
- 未发现任何已迁移页面在开关 true 路径下直接 new 旧 FCLDialog 子类（grep `new XxxDialog(` 对照开关分支逐一确认）。

---

## 5. 真机功能对照测试清单（方案 5.1，待真机执行）

> 执行前提：构建产物 `FCL-debug-1.3.2.1-all.apk`；每项先测开关 true（默认）路径，发现异常时将对应开关改 false 复测以隔离是否为迁移回归。全部开关均为编译期常量，改值需重新构建。

### 5.1-A 启动游戏

- [ ] A1 版本列表选择版本 → 主页点启动 → 游戏进入画面（涉及：`USE_COMPOSE_MAIN_UI`、`USE_COMPOSE_VERSION_PAGES`；启动进度弹窗已接 `USE_COMPOSE_TASK_DIALOG`，L4 修复后需在真机验证 Miuix 分支进度/取消/失败提示）
- [ ] A2 未选版本时点启动 → 「未选择版本」提示弹出并可跳转下载页（已接 `USE_COMPOSE_VERSION_OP_ALERTS`，L3 修复；回滚：改 false 复测）
- [ ] A3 未登录时点启动 → 弹出 Miuix 创建账户弹窗，创建后继续启动（`USE_COMPOSE_CREATE_ACCOUNT`；回滚：改 false 复测）
- [ ] A4 跳过登录确认、重新登录提示两弹窗文案/按钮行为一致（`USE_COMPOSE_SKIP_LOGIN` / `USE_COMPOSE_TIP_RELOGIN`）
- [ ] A5 回滚验证：`USE_COMPOSE_MAIN_UI = false` 后主页与启动链路同迁移前

### 5.1-B 下载安装 Forge / Fabric / NeoForge

- [ ] B1 下载页 → 游戏 Tab → 选版本 → 选 Forge/NeoForge/Fabric(+API) 安装 → TaskDialog 进度 → 成功（`USE_COMPOSE_DOWNLOAD_PAGES`、`USE_COMPOSE_TASK_DIALOG`）
- [ ] B2 安装器列表（manage 域 InstallerListPage 保留原生）离线安装 .jar 与更换版本安装（无开关，同体系）
- [ ] B3 名称三重校验 Toast、自动命名被手动修改后停止（VersionInstallInfoScreen 对齐 VersionInstallInfoPage 语义）
- [ ] B4 回滚验证：`USE_COMPOSE_DOWNLOAD_PAGES = false` 后安装向导同迁移前

### 5.1-C 导入整合包 / 存档

- [ ] C1 下载页整合包 Tab →「安装本地整合包」→ 选 .zip/.mrpack → 向导（ModpackSelectionPage 保留原生）→ 安装成功（Compose 入口 ComposeDownloadShells.kt:90 → 遗留向导，属刻意保留）
- [ ] C2 外部文件管理器打开 .mrpack/.zip 拉起启动器进入导入流程（ImportActivity alias，activity-migration §4）
- [ ] C3 版本管理 → 导出整合包 → 类型选择 → 信息页 → 文件选择（三态勾选树）→ 导出（ModpackType/Info/FileSelection 保留原生）
- [ ] C4 存档管理：导入/导出存档（WorldListPage 保留原生；导出确认弹窗 `USE_COMPOSE_WORLD_EXPORT`）
- [ ] C5 整合包版本「更新」入口（ComposeManagePage → Versions.updateVersion → 保留原生向导）

### 5.1-D 微软 / 离线登录

- [ ] D1 账户页 → 添加微软账户 → OAuth 流程 → 账户入库（`USE_COMPOSE_ACCOUNT_UI`、`USE_COMPOSE_CREATE_ACCOUNT`）
- [ ] D2 离线账户创建 + 角色选择（`USE_COMPOSE_CHARACTER_SELECTOR`，可与 D1 开关交叉搭配复测）
- [ ] D3 离线账户皮肤设置（`USE_COMPOSE_OFFLINE_ACCOUNT_SKIN`）
- [ ] D4 微软账户过期 → 重新登录（`USE_COMPOSE_RELOGIN_OAUTH`）
- [ ] D5 authlib-injector 外置登录服务器添加/登录（`USE_COMPOSE_ADD_AUTHLIB_INJECTOR_SERVER`）
- [ ] D6 回滚验证：`USE_COMPOSE_ACCOUNT_UI = false` 后账户页同迁移前

### 5.1-E 虚拟鼠标与按键映射

- [ ] E1 游戏内虚拟鼠标移动/点击/手势模式切换（游戏侧红线，零改动；回归基线对照即可）
- [ ] E2 游戏内菜单 → 按键映射（`USE_COMPOSE_GAMEPAD_MAP`）；编辑控件 → 选择键码 → 键盘弹窗（`USE_COMPOSE_SELECT_KEYCODE`，AndroidView 包装原生键盘，重点验证触摸链）
- [ ] E3 手柄控件编辑全套：编辑按钮/方向键属性与事件（`USE_COMPOSE_EDIT_VIEW`）、控件分组（`USE_COMPOSE_VIEW_GROUP`/`USE_COMPOSE_EDIT_VIEW_GROUP`）、按钮/方向键样式（`USE_COMPOSE_BUTTON_STYLE`/`USE_COMPOSE_DIRECTION_STYLE`/`USE_COMPOSE_ADD_BUTTON_STYLE`/`USE_COMPOSE_ADD_DIRECTION_STYLE`）
- [ ] E4 快捷输入面板与新增输入文本（`USE_COMPOSE_QUICK_INPUT`/`USE_COMPOSE_ADD_INPUT_TEXT`）
- [ ] E5 手柄布局管理/仓库/下载/上传（页面保留原生；弹窗 `USE_COMPOSE_CONTROLLER_INFO`/`USE_COMPOSE_CONTROLLER_UPLOAD`/`USE_COMPOSE_CONTROLLER_OLD_VERSION`/`USE_COMPOSE_SELECT_CONTROLLER`）
- [ ] E6 联机菜单（`USE_COMPOSE_MULTIPLAYER_MENU = false` 保留原生）7 状态流转 + 邀请码弹窗（`USE_COMPOSE_INVITE_CODE`）

### 5.1-F 通用回归面（各阶段报告清单合并）

- [ ] F1 WebActivity：帮助入口打开网页、进度显隐、缓存清理、横屏不重建；回滚 `USE_COMPOSE_WEB = false`（activity-migration §4）
- [ ] F2 ShellActivity：命令执行/清屏/弹键盘/adjustResize/进程回收；回滚 `USE_COMPOSE_SHELL = false`（同上）
- [ ] F3 ControllerActivity/JVMActivity 系统栏短暂呼出透明、Drawer/返回键/软键盘行为不变（红线邻近重点回归）
- [ ] F4 JVMCrashActivity 崩溃页四按钮 + `:crash` 进程语义
- [ ] F5 主题联动：改主题色/深浅色后全部 Compose 页面与 Web/Shell 即时跟随；三模式（Light/Dark/FollowSystem）切换
- [ ] F6 设置页全项（LauncherSettingScreen 1073 行旧页面对照）、游戏设置 17 组绑定双向同步、关于/帮助页
- [ ] F7 版本列表：选择/重命名/复制/删除（删除确认弹窗已接 `USE_COMPOSE_VERSION_OP_ALERTS`，L2 修复，真机验证 Miuix 分支确认→进度→列表刷新）/全局设置入口
- [ ] F8 任务弹窗：下载速度显示、取消语义（executor.cancel + 关闭）、autoClose、进度 <0 不确定态
- [ ] F9 弹窗单槽位回退：Compose 前台触发并发弹窗时回退 FCLAlertDialog 不丢提示（bridge-api §6.2）
- [ ] F10 动画：列表入场（animationSpeed 桥接）、按压反馈、页面切换壳层动画无缺口（4.2）
- [ ] F11 无障碍与显示：TalkBack、字体缩放、minSdk 26 设备、大屏/平板 WindowSizeClass、刘海安全区（5.3 项，随真机一并执行）

---

## 6. 结论与遗留问题

1. **覆盖率**：142 个 layout 中 84 已 Compose 化（全部带独立回滚开关）、55 保留原生（红线/容器/决策保留且活跃）、3 疑似废弃（`dialog_relogin_classic`、`dialog_modpack_selection`、`dialog_world_name`，另附孤儿类 `ModpackSelectionDialog.java`）；「应迁尽迁」口径下覆盖率 100%。
2. ~~**发现的遗漏**~~ **L1–L4 已全部修复（均不阻塞构建与功能，现已接入开关）**：
   - L1 `DriverSelectDialog` ~~未迁移且无开关~~ → 已修复：新增 `MiuixDriverSelectDialog` + `USE_COMPOSE_DRIVER_SELECT`，两个调用点双分支（§4.1-D4）；
   - L2 `Versions.deleteVersion` 删除确认弹窗 ~~未接开关~~ → 已修复：接 `USE_COMPOSE_VERSION_OP_ALERTS`，Miuix 侧走 `FCLDialogs.showAlert`/`showProgress`（§4.1-D1）；
   - L3 `Versions.checkVersionForLaunching` / `downloadModpackImpl` 失败提示 ~~未接开关~~ → 已修复：同开关双分支（§4.1-D2/D3）；
   - L4 `LauncherHelper` 启动进度 TaskDialog ~~未接开关~~ → 已修复：接 `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG`；勘误——预警中的 titleProperty fakefx 绑定在当前源码并不存在，实际仅静态 setTitle + NORMAL 取消 + 手动 dismiss，既有 MiuixTaskDialog API 全部承接（§4.2-#1）。
3. **遗留**：3 个废弃文件与孤儿类随 6.1 清理删除（见 final-report.md §6）。本步修复未做真机验证（环境无真机），L1–L4 的真机对照项已更新至 §5 清单。
4. **构建门禁**：L1–L4 修复后 `GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug` → **BUILD SUCCESSFUL**（155 actionable tasks，编译警告仅过时 API 提示，无新增错误）。
