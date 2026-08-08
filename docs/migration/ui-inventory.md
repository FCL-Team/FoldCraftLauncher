# FCL UI 资产盘点（阶段一 · 步骤 1.1）

> 生成时间：2026-08-01 ｜ 分支：`feature/miuix-migration`
> 范围：仅限 `FCL/` 模块（`FCL/src/main/`）。FCLCore、FCLauncher、FCLLibrary 等模块为红线，仅在必要时交叉引用其符号以确认归属。
> 所有条目均经实际文件核实（布局行数、根元素、`R.layout.*` / ViewBinding 引用关系逐一 grep 验证）。
>
> **复杂度评级标准**
> - **P0**：简单静态 / 纯展示（静态表单、少量文本+按钮，无复杂交互）
> - **P1**：中等交互（列表+弹窗、表单校验、分页、进度展示等）
> - **P2**：复杂自定义交互（虚拟摇杆、手势绘制、游戏内悬浮菜单、键盘、实时渲染等）

---

## 0. 总览

| 资产类别 | 数量 | 位置 |
|---|---|---|
| 布局 XML | **142** | `FCL/src/main/res/layout/` |
| ├ activity_ | 6 | 同上 |
| ├ dialog_ | 42 | 同上 |
| ├ item_ | 34 | 同上 |
| ├ fragment_ / menu_ / page_ / ui_ / view_ | 2 / 2 / 27 / 8 / 21 | 同上 |
| drawable 资源 | 77 | `FCL/src/main/res/drawable/` |
| values 资源 | colors(7色) / themes(4样式) / attrs(3组) / strings(≈1182条)；**无 dimens.xml** | `FCL/src/main/res/values*/` |
| anim / xml | 4 / 4 | `res/anim/`、`res/xml/` |
| Activity（Manifest 声明） | 9（7 个在 FCL，2 个在 FCLLibrary） | `FCL/src/main/AndroidManifest.xml` |
| Fragment | 2 | `FCL/src/main/java/com/tungsten/fcl/fragment/` |
| 自定义 View（FCL 模块内） | 13 个类（含 2 接口、1 管理器、1 客户端封装） | `control/view/`、`com/mio/ui/`、`com/mio/touchcontroller/` |

**UI 架构关键事实（迁移 Agent 必读）**

1. **主界面不是多 Activity 架构**：除 Splash/Main/游戏侧 Activity 外，启动器全部业务页面运行在 `MainActivity` 内部的自研 UI 栈上——`FCLUILayout`（FCLLibrary 组件）作为容器，`UIManager`（`FCL/src/main/java/com/tungsten/fcl/ui/UIManager.kt`）管理 8 个 `ui_*.xml` 一级界面（各对应一个 `FCLCommonUI` 子类），每个界面再用 `PageManager`（`FCL/src/main/java/com/tungsten/fcl/ui/PageManager.java`）切换 `page_*.xml` 二级页面。迁移时这套"UI 栈"是核心改造对象。
2. **组件高度依赖 FCLLibrary 自研控件**：布局中大量使用 `com.tungsten.fcllibrary.component.view.FCL*`（FCLButton/FCLTextView/FCLConstraintLayout/FCLSpinner/FCLProgressBar/FCLDynamicIsland 等）与 `FCLAlertDialog`、`ThemeEngine`。FCLLibrary 是红线模块，Miuix 迁移需先在 FCL 侧建立替换策略（包裹/桥接或整体替换）。
3. **主题不走 Android 主题体系**：`themes.xml` 仅 4 个样式，实际换肤由 `ThemeEngine`（FCLLibrary）运行时驱动，`colors.xml` 仅 7 个颜色，**没有 dimens.xml**（尺寸全部硬编码在各布局中），暗色支持仅 `values-night/colors.xml` 覆盖 1 个颜色。Compose 化时设计令牌可近乎从零定义。
4. **游戏内 UI（ControllerActivity/JVMActivity + GameMenu）是最高风险区**：`view_game_menu.xml` include 了 `menu_left/menu_right`（合计 1100+ 行），叠加 ControlButton/ControlDirection/TouchPad/GameItemBar 等自绘 View 与手柄/陀螺仪输入，属于"游戏内悬浮控制层"，建议最晚迁移或保留 View 体系。
5. **发现 2 个疑似废弃空布局**（见 §1.2 备注）：`dialog_relogin_classic.xml`、`dialog_modpack_selection.xml` 均为无子 View 的空 ConstraintLayout（5 行）；`dialog_world_name.xml` 全仓库无引用（孤儿文件）。

---

## 1. 布局清单（142 个，`FCL/src/main/res/layout/`）

### 1.1 activity_（6 个）

| 文件 | 行数 | 根元素 | 用途（使用方） | 复杂度 |
|---|---|---|---|---|
| `activity_splash.xml` | 16 | ConstraintLayout | 启动页背景容器（`SplashActivity`，splashscreen API + Eula/Runtime Fragment 宿主） | P0 |
| `activity_main.xml` | 298 | ConstraintLayout | 启动器主界面骨架：VideoView 动态背景、左侧 7 个 `FCLMenuView` 导航、右侧账户/启动栏、`FCLUILayout` 容器、`FCLDynamicIsland`（`MainActivity`，ViewBinding） | P1 |
| `activity_web.xml` | 22 | ConstraintLayout | 内置 WebView + 进度条（`WebActivity`，加载外部 URL） | P0 |
| `activity_shell.xml` | 34 | ConstraintLayout | Shell 终端页：日志窗口 + 命令输入（`ShellActivity`） | P1 |
| `activity_jvm.xml` | 11 | RelativeLayout | 游戏/JAR 运行画面，仅一个 TextureView 画布（`JVMActivity`，SurfaceTexture 渲染） | P2 |
| `activity_jvm_crash.xml` | 72 | ConstraintLayout | JVM/游戏崩溃报告页：日志预览 + 重启/关闭/上传/分享（`JVMCrashActivity`，独立 `:crash` 进程，ViewBinding） | P1 |

### 1.2 dialog_（42 个）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `dialog_add_authlib_injector_server.xml` | 153 | 添加 authlib-injector 外置登录服务器（`ui/account/AddAuthlibInjectorServerDialog.java`） | P1 |
| `dialog_add_button_style.xml` | 106 | 新增手柄按钮样式（`control/AddButtonStyleDialog.java`） | P1 |
| `dialog_add_direction_style.xml` | 113 | 新增方向键样式（`control/AddDirectionStyleDialog.java`） | P1 |
| `dialog_add_input_text.xml` | 88 | 新增快捷输入文本（`control/AddInputTextDialog.java`） | P0 |
| `dialog_add_profile.xml` | 94 | 新建游戏版本/安装整合包入口（`ui/version/AddProfileDialog.java`） | P1 |
| `dialog_character_selector.xml` | 36 | 离线账户皮肤角色选择（`ui/account/CreateAccountDialog.java:478`） | P0 |
| `dialog_controller_info.xml` | 197 | 手柄布局详情展示（`ui/controller/ControllerInfoDialog.java`） | P1 |
| `dialog_controller_upload.xml` | 311 | 上传手柄布局到仓库（`ui/controller/ControllerUploadDialog.java`，全工程最大的 dialog） | P1 |
| `dialog_create_account.xml` | 69 | 创建账户类型选择（`ui/account/CreateAccountDialog.java`） | P1 |
| `dialog_download_addon.xml` | 46 | 下载附加内容确认（下载页弹出） | P0 |
| `dialog_download_controllor.xml` | 35 | 下载手柄布局确认（`ui/controller/`，文件名拼写沿用现状） | P0 |
| `dialog_duplicate_version.xml` | 89 | 版本重名处理（`ui/version/DuplicateVersionDialog.java`） | P0 |
| `dialog_edit_view.xml` | 117 | 编辑手柄控件属性（`control/EditViewDialog.java`） | P1 |
| `dialog_edit_view_group.xml` | 98 | 编辑控件分组（`control/EditViewGroupDialog.java`） | P1 |
| `dialog_gamepad_map.xml` | 33 | 手柄按键映射（`com/mio/ui/dialog/GamepadMapDialog.kt`，ViewBinding） | P1 |
| `dialog_input_invite_code.xml` | 65 | 联机输入邀请码（`control/MultiplayerDialog.java:283`） | P0 |
| `dialog_itembar_setting.xml` | 31 | 游戏物品栏设置（`control/GameItemBarSettingDialog.kt`，ViewBinding） | P0 |
| `dialog_manage_button_style.xml` | 54 | 管理按钮样式列表（`control/ButtonStyleDialog.java`） | P1 |
| `dialog_manage_direction_style.xml` | 55 | 管理方向键样式列表（`control/DirectionStyleDialog.java`） | P1 |
| `dialog_manage_java.xml` | 61 | Java 运行时管理（`com/mio/ui/dialog/JavaManageDialog.kt`，ViewBinding） | P1 |
| `dialog_manage_view_groups.xml` | 45 | 管理控件分组（`control/ViewGroupDialog.java`） | P1 |
| `dialog_mod_info.xml` | 92 | Mod 详情（`ui/manage/ModInfoDialog.java`） | P1 |
| `dialog_modpack_selection.xml` | 5 | **空布局（无子 View）**，被 `ui/version/ModpackSelectionDialog.java` setContentView 引用后程序化填充，疑似废弃占位 | P0 |
| `dialog_modpack_url.xml` | 46 | 输入整合包链接（下载模块） | P0 |
| `dialog_multiplyer_menu.xml` | 62 | 联机功能菜单（`control/MultiplayerDialog.java:71`，文件名拼写沿用现状） | P1 |
| `dialog_offline_account_skin.xml` | 200 | 离线账户皮肤设置（`ui/account/OfflineAccountSkinDialog.kt`，ViewBinding） | P1 |
| `dialog_open_folder.xml` | 98 | 游戏内打开文件夹选择（`control/OpenFolderDialog.kt`，ViewBinding） | P1 |
| `dialog_quick_input.xml` | 44 | 游戏内快捷输入面板（`control/QuickInputDialog.kt`，ViewBinding） | P1 |
| `dialog_relogin_classic.xml` | 5 | **空布局（无子 View）**，全仓库无代码引用，疑似废弃 | P0 |
| `dialog_relogin_oauth.xml` | 57 | OAuth 重新登录（`ui/account/OAuthAccountLoginDialog.java:48`） | P1 |
| `dialog_rename_version.xml` | 62 | 重命名版本（`ui/version/RenameVersionDialog.java`） | P0 |
| `dialog_rollback_mod.xml` | 36 | Mod 版本回滚（`ui/manage/ModRollbackDialog.java`） | P1 |
| `dialog_select_controller.xml` | 37 | 选择手柄布局（`control/SelectControllerDialog.java`） | P1 |
| `dialog_select_keycode.xml` | 150 | 选择键码，内嵌 `<include layout="@layout/view_keyboard"/>`（`control/SelectKeycodeDialog.kt`） | P2 |
| `dialog_select_renderer.xml` | 42 | 选择渲染器（`com/mio/ui/dialog/RendererSelectDialog.kt`，ViewBinding） | P1 |
| `dialog_skip_login.xml` | 52 | 跳过登录确认（`game/LauncherHelper.java:622`） | P0 |
| `dialog_task.xml` | 55 | 任务进度弹窗（`ui/TaskDialog.java`，任务列表+进度） | P1 |
| `dialog_tip_relogin.xml` | 44 | 提示重新登录（`game/LauncherHelper.java:664`） | P0 |
| `dialog_translation.xml` | 42 | 翻译贡献名单（`ui/download/TranslationDialog.kt`，ViewBinding） | P0 |
| `dialog_update.xml` | 111 | 启动器更新提示（`upgrade/UpdateDialog.java`，LinearLayout 根） | P1 |
| `dialog_world_export.xml` | 89 | 导出存档（`ui/manage/WorldExportDialog.java`） | P0 |
| `dialog_world_name.xml` | 46 | **孤儿文件**：全仓库（java/kt/xml）无引用，疑似废弃 | P0 |

> 备注：dialog 均基于 FCLLibrary `FCLDialog`/`FCLAlertDialog` 体系（自定义 Dialog 基类），迁移时需统一替换为 Miuix Dialog。

### 1.3 fragment_（2 个）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `fragment_eula.xml` | 71 | 最终用户协议页（`fragment/EulaFragment.java`，由 `SplashActivity` 首次启动时展示） | P0 |
| `fragment_runtime.xml` | 395 | 运行时环境下载/安装页：LWJGL、Cacio、Java 8/17/21/25、JNA 多组件安装进度（`fragment/RuntimeFragment.kt`，ViewBinding） | P1 |

### 1.4 item_（34 个，列表项）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `item_account.xml` | 141 | 账户列表项（`ui/account/AccountListAdapter.kt`，含皮肤 3D 渲染容器） | P1 |
| `item_article.xml` | 54 | 帮助文档文章项（`ui/setting/ArticleAdapter.java`） | P0 |
| `item_authlib_injector_server.xml` | 74 | 外置登录服务器项（`ui/account/ServerListAdapter.java`） | P0 |
| `item_button_style.xml` | 51 | 按钮样式项（`control/ButtonStyleAdapter.java`） | P0 |
| `item_character.xml` | 38 | 角色选择项（`CreateAccountDialog` 角色网格） | P0 |
| `item_controller_editable.xml` | 59 | 可编辑手柄布局项（`ui/controller/EditableControllerListAdapter.java`） | P0 |
| `item_controller_selectable.xml` | 72 | 可选手柄布局项（`control/SelectableControllerListAdapter.java` 等） | P0 |
| `item_datapack.xml` | 52 | 数据包列表项（`ui/manage/DatapackListAdapter.java`） | P0 |
| `item_direction_style.xml` | 52 | 方向键样式项（`control/DirectionStyleAdapter.java`） | P0 |
| `item_gamepad_map.xml` | 28 | 手柄映射项（`com/mio/ui/adapter/GamepadMapItemAdapter.kt`，ViewBinding） | P0 |
| `item_input_text.xml` | 36 | 快捷输入文本项（`control/InputTextAdapter.java`） | P0 |
| `item_local_mod.xml` | 120 | 本地 Mod 项（`ui/manage/LocalModListAdapter.kt`，ViewBinding） | P1 |
| `item_manage.xml` | 26 | 管理页入口项（`ui/manage/adapter/`，ViewBinding） | P0 |
| `item_manage_java.xml` | 43 | Java 运行时项（`com/mio/ui/adapter/ManageJavaItemAdapter.kt`，ViewBinding） | P0 |
| `item_mod_version.xml` | 68 | Mod 版本项（`ui/manage/ModOldVersionListAdapter.java`） | P0 |
| `item_profile.xml` | 65 | 游戏配置档案项（`ui/version/ProfileListAdapter.java`） | P0 |
| `item_remote_mod.xml` | 97 | 远程 Mod 项（下载页，ViewBinding，`FCLConstraintLayout` 根） | P1 |
| `item_remote_version.xml` | 92 | 远程版本项（下载页，ViewBinding，被引用 5 处） | P0 |
| `item_renderer.xml` | 10 | 渲染器选项（单行 `FCLTextView`，`RendererSelectDialog`/`DriverSelectDialog`） | P0 |
| `item_screenshot_path.xml` | 23 | 截图路径项（手柄截图适配） | P0 |
| `item_spinner.xml` | 13 | Spinner 默认项（FCLSpinner 使用，FCL 代码引用 10 处） | P0 |
| `item_spinner_auto_tint.xml` | 14 | Spinner 自动着色项（引用 17 处） | P0 |
| `item_spinner_dropdown.xml` | 14 | Spinner 下拉项（`FCLCheckedTextView`，引用 28 处，全工程引用最多的 item） | P0 |
| `item_spinner_dropdown_small.xml` | 14 | Spinner 小号下拉项（引用 4 处） | P0 |
| `item_spinner_small.xml` | 13 | Spinner 小号项（引用 4 处） | P0 |
| `item_spinner_theme_color.xml` | 14 | 主题色选择 Spinner 项（设置页） | P0 |
| `item_task_progress.xml` | 33 | 任务进度项（`ui/TaskListPane.java`） | P0 |
| `item_task_stage.xml` | 22 | 任务阶段项（`ui/TaskListPane.java`） | P0 |
| `item_terracotta_profile.xml` | 47 | 陶瓦联机节点项（`control/MultiPlayerProfileAdapter.java`） | P0 |
| `item_translation.xml` | 18 | 翻译者名单项（`TranslationDialog`，ViewBinding） | P0 |
| `item_update_mod.xml` | 72 | Mod 更新项（`ui/manage/ModUpdateListAdapter.java`） | P0 |
| `item_version.xml` | 112 | 游戏版本项（`ui/version/VersionListAdapter.kt`，ViewBinding） | P1 |
| `item_view_group.xml` | 67 | 控件分组项（`control/ViewGroupAdapter.java`） | P0 |
| `item_world.xml` | 70 | 存档列表项（`ui/manage/WorldListAdapter.kt`，ViewBinding） | P0 |

### 1.5 menu_（2 个，游戏内菜单，P2 高危区）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `menu_left.xml` | 404 | 游戏内左侧悬浮菜单（被 `view_game_menu.xml` include，含菜单按钮组/摇杆开关/键盘开关等） | P2 |
| `menu_right.xml` | 732 | 游戏内右侧悬浮菜单（被 `view_game_menu.xml` include，含快捷输入、物品栏设置、截图、日志等大量功能入口） | P2 |

### 1.6 page_（27 个，主界面二级页面）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `page_controller_download.xml` | 290 | 手柄布局下载页（`ui/controller/ControllerDownloadPage.java`） | P1 |
| `page_controller_manager.xml` | 342 | 手柄布局管理页（`ui/controller/ControllerManagePage.java`） | P1 |
| `page_controller_repo.xml` | 173 | 手柄仓库页（`ui/controller/ControllerRepoPage.java`） | P1 |
| `page_controller_upload.xml` | 108 | 手柄上传页（`ui/controller/ControllerUploadPage.java`） | P1 |
| `page_datapack_list.xml` | 87 | 数据包列表页（`ui/manage/DatapackListPage.java`） | P1 |
| `page_download.xml` | 264 | 下载中心主页（`ui/download/DownloadUI.java` + `DownloadPageManager.kt`） | P1 |
| `page_download_addon.xml` | 149 | 附加资源下载列表（Mod/整合包/资源包/光影，`ui/download/`） | P1 |
| `page_download_addon_info.xml` | 208 | 附加资源详情（`ui/download/common/`） | P1 |
| `page_download_addon_version.xml` | 15 | 附加资源版本选择（仅列表容器） | P0 |
| `page_install_version.xml` | 127 | 安装新游戏版本（`ui/download/version/`，引用 3 处） | P1 |
| `page_installer.xml` | 57 | 自动安装器页（`ui/manage/InstallerListPage.java`） | P1 |
| `page_manage_auto_install.xml` | 26 | 自动安装整合包页 | P1 |
| `page_manage_mod.xml` | 203 | Mod 管理页（`ui/manage/ModListPage.java`） | P1 |
| `page_manage_version.xml` | 30 | 版本管理容器页（`ui/manage/ManagePage.kt` 等，ViewBinding） | P1 |
| `page_manage_world.xml` | 80 | 存档管理页（`ui/manage/WorldListPage.kt`，ViewBinding） | P1 |
| `page_manage_world_info.xml` | 571 | 存档详情页（`ui/manage/WorldInfoPage.java`，管理类最大页面） | P1 |
| `page_mod_update.xml` | 69 | Mod 批量更新页（`ui/manage/ModUpdatesPage.java`） | P1 |
| `page_modpack.xml` | 202 | 整合包下载列表页（`ui/download/modpack/`，引用 5 处） | P1 |
| `page_modpack_file.xml` | 37 | 整合包文件选择页（`ui/manage/ModpackFileSelectionPage.java`） | P1 |
| `page_modpack_info.xml` | 609 | 整合包详情页（`ui/manage/ModpackInfoPage.java`） | P1 |
| `page_modpack_selection.xml` | 72 | 整合包类型选择页（`ui/manage/ModpackTypeSelectionPage.java`） | P0 |
| `page_modpack_type.xml` | 175 | 整合包来源类型页（下载模块） | P1 |
| `page_setting_about.xml` | 223 | 关于页（`ui/setting/AboutPage.java`） | P0 |
| `page_setting_help.xml` | 99 | 帮助页（`ui/setting/HelpPage.java`，文档列表） | P1 |
| `page_setting_launcher.xml` | 1073 | **启动器设置页（`ui/setting/LauncherSettingPage.java`，全工程最大布局，ViewBinding）** | P1 |
| `page_version_list.xml` | 146 | 游戏版本列表页（`ui/version/VersionListPage.kt`，ViewBinding） | P1 |
| `page_version_setting.xml` | 763 | 版本全局/单独设置页（`ui/manage/VersionSettingPage.kt`，ViewBinding，第二大布局） | P1 |

### 1.7 ui_（8 个，主界面一级页面，各对应一个 `FCLCommonUI` 子类）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `ui_main.xml` | 96 | 首页：公告栏 + 账户皮肤 3D 展示（`ui/main/MainUI.java`，含 `SkinViewer` GL 渲染） | P2 |
| `ui_account.xml` | 141 | 账户管理界面（`ui/account/AccountUI.java`） | P1 |
| `ui_version.xml` | 10 | 版本管理容器（`ui/version/VersionUI.java`，页面由 PageManager 填充） | P1 |
| `ui_manage.xml` | 54 | 管理容器（`ui/manage/ManageUI.java` + `ManagePageManager.kt`） | P1 |
| `ui_download.xml` | 59 | 下载容器（`ui/download/DownloadUI.java` + `DownloadPageManager.kt`） | P1 |
| `ui_controller.xml` | 13 | 手柄容器（`ui/controller/ControllerUI.java` + `ControllerPageManager.kt`） | P1 |
| `ui_multiplayer.xml` | 317 | 联机界面（`ui/multiplayer/MultiplayerUI.java`，Terracotta/EasyTier 状态机视图切换） | P2 |
| `ui_setting.xml` | 49 | 设置容器（`ui/setting/SettingUI.java` + `SettingPageManager.kt`） | P1 |

### 1.8 view_（21 个，复合视图/嵌入式组件）

| 文件 | 行数 | 用途（使用方） | 复杂度 |
|---|---|---|---|
| `view_button_style.xml` | 254 | 按钮样式预览/编辑视图（`control/` 样式编辑，`FCLLinearLayout` 根） | P2 |
| `view_create_account_external.xml` | 101 | 外置登录表单（`CreateAccountDialog.java:396`，ScrollView 根） | P1 |
| `view_create_account_microsoft.xml` | 20 | 微软登录视图（`CreateAccountDialog.java:331`） | P0 |
| `view_create_account_offline.xml` | 36 | 离线账户创建视图（`CreateAccountDialog.java:292`） | P1 |
| `view_direction_style_button.xml` | 84 | 按钮式方向键样式编辑（`control/DirectionStyle*`） | P2 |
| `view_direction_style_rocker.xml` | 370 | 摇杆式方向键样式编辑（`control/DirectionStyle*`，方向样式中最大） | P2 |
| `view_edit_button_event.xml` | 109 | 按钮事件编辑（`control/EditViewDialog` 内嵌） | P1 |
| `view_edit_button_event_child.xml` | 279 | 按钮事件子项编辑（引用 4 处） | P1 |
| `view_edit_button_info.xml` | 337 | 按钮属性编辑（手柄编辑器核心表单） | P2 |
| `view_edit_direction_event.xml` | 228 | 方向键事件编辑 | P1 |
| `view_edit_direction_info.xml` | 248 | 方向键属性编辑 | P2 |
| `view_game_menu.xml` | 115 | 游戏内菜单骨架：DrawerLayout + include `menu_left`/`menu_right`（`control/GameMenu.java:787`，1060 行逻辑类） | P2 |
| `view_installer_item.xml` | 75 | 安装器条目视图（`ui/InstallerItem.java:250`，Forge/Fabric 等安装选项） | P1 |
| `view_jar_executor_menu.xml` | 150 | JAR 执行器菜单（`control/JarExecutorMenu.java:109`） | P2 |
| `view_keyboard.xml` | 1502 | **全键盘视图（全工程最大布局，被 `dialog_select_keycode.xml` include，键码选择键盘）** | P2 |
| `view_mod_screenshot.xml` | 39 | Mod 截图浏览视图（`ui/download/common/RemoteModScreenshotAdapter.kt`，ViewBinding） | P0 |
| `view_multiplayer_exception.xml` | 124 | 联机异常状态视图（`ui/multiplayer/MultiplayerUI.java`） | P1 |
| `view_multiplayer_host_guest_ok.xml` | 177 | 联机主机/客机就绪视图（引用 2 处） | P1 |
| `view_multiplayer_scanning.xml` | 84 | 联机扫描中视图 | P1 |
| `view_multiplayer_starting.xml` | 126 | 联机启动中视图（引用 2 处） | P1 |
| `view_multiplayer_waiting.xml` | 106 | 联机等待视图 | P1 |

---

## 2. drawable 资源（77 个，`FCL/src/main/res/drawable/`）

| 分组 | 数量 | 文件 | 说明 |
|---|---|---|---|
| 启动器/品牌 PNG | 14 | `april_fools.png`、`img_app.png`、`img_chicken.png`、`img_cleanroom.png`、`img_command.png`、`img_craft_table.png`、`img_cursor.png`、`img_enchantnet.png`、`img_fabric.png`、`img_forge.png`、`img_grass.png`、`img_neoforge.png`、`img_optifine.png`、`img_quilt.png` | 多为 Mod 加载器/安装器图标与装饰图；Compose 中直接以 painterResource 复用 |
| 矢量图标（Material 风格） | 42 | `ic_baseline_*.xml`（account_circle、application、arrow_back/forward/upward/downward、build、close、cloud_download/upload、content_copy、delete、done、download、earth、easytier、edit、feedback、guest、hanger、home、host、input、jump、keyboard、list、microsoft、more_horiz、output、person_add、refresh、restore、save、screenshot、script、server、settings、texture、tune、update、videogame_asset、wifi） | 标准 vector drawable；Miuix 迁移时优先替换为 Miuix/Material Icons |
| 矢量图标（描边/杂项） | 7 | `ic_outline_account_tree_24.xml`、`ic_outline_extension_24.xml`、`ic_outline_info_24.xml`、`ic_boat.xml`、`ic_cube.xml`、`ic_pojav.xml`、`ic_translation.xml` | 同上 |
| 背景/状态选择器 | 11 | `bg_container_transparent_clickable.xml`、`bg_container_transparent_selected.xml`、`bg_container_white.xml`、`bg_container_white_clickable.xml`、`bg_game_menu.xml`、`bg_item.xml`、`bg_item_clickable.xml`、`bg_progress.xml`、`bg_progress_indeterminate.xml`、`bg_right_menu.xml`、`bg_right_menu_button.xml` | 圆角/波纹/进度样式；Compose 化后大多可由 Modifier.background + 主题色取代 |
| 键码按钮状态 | 2 | `keycode_view_normal.xml`、`keycode_view_selected.xml` | 配合 `KeycodeView` 自定义 View |
| 其他 | 1 | `java.xml` | Java 图标 vector |

另有 `mipmap-*/`（6 目录）存放 `ic_launcher*` 启动图标，不参与页面 UI 迁移。

## 3. values 资源（`FCL/src/main/res/values*/`）

| 文件 | 规模 | 内容摘要 |
|---|---|---|
| `values/colors.xml` | 7 色 | `default_theme_color #7797CF`、`black/white`、`ui_bg_color #40F4F4F4`、`right_menu_color #80F4F4F4`、`icon_background_color`、`primary_text #0E0E0E` |
| `values-night/colors.xml` | 1 色 | 仅覆盖 `primary_text #FFFFFF`（暗色支持极弱） |
| `values-v31/colors.xml` | 1 色 | `icon_background_color` 用 `system_accent1_0`（Material You 动态取色） |
| `values/themes.xml` | 4 样式 | `Theme.FoldCraftLauncher`（MaterialComponents DayNight NoActionBar，全屏+透明导航栏）、`Theme.Splash`（SplashScreen API）、`TabTextAppearance`、`NavIndicator` |
| `values-night/themes.xml` | 2 样式 | 同基主题 + `TabTextAppearance` |
| `values-v31/themes.xml` | 2 样式 | 主题 + `Theme.Splash`（cutout shortEdges） |
| `values/attrs.xml` | 3 组 declare-styleable | `KeycodeView(keycode)`、`LogWindow(auto_log_tint)`、`DraggableTextView(save_key)`——即 FCL 模块自定义 View 的全部自定义属性 |
| `values/strings.xml` | ≈1182 条 name | 全量文案基线；另有 8 个语言目录翻译（de、fa、pt-rBR、ru、uk、vi、zh、zh-rHK） |
| **dimens.xml** | **不存在** | 所有尺寸硬编码于布局 XML，迁移时需重新建立间距/尺寸令牌 |

其他：`res/anim/` 4 个（`frag_start_anim`、`frag_stop_anim`、`progress_indeterminate_rect1/2`）；`res/xml/` 4 个（`backup_rules`、`data_extraction_rules`、`network_security_config`、`provider_paths`）。布局中引用的 `@xml/anim_scale`、`@xml/anim_scale_large`（stateListAnimator）位于 FCLLibrary 模块，不在本盘点范围。

---

## 4. Activity 清单（Manifest 声明 9 个）

来源：`FCL/src/main/AndroidManifest.xml`。全部声明 `configChanges`（横竖屏/键盘等不重建），多为 `sensorLandscape` 强制横屏。

| Activity | 代码位置（行数） | 布局 | UI 职责 | 生命周期依赖 |
|---|---|---|---|---|
| `SplashActivity` (LAUNCHER) | `activity/SplashActivity.kt` (268) | `activity_splash.xml` | 启动入口：SplashScreen API、权限检查、EULA 同意流程、运行时环境检查，内嵌 `EulaFragment`/`RuntimeFragment` | `SharedPreferences("launcher")` 读 `isAgree`；完成后跳 `MainActivity`；处理 modpack 文件导入 Intent（activity-alias `ImportActivity` 也指向它） |
| `MainActivity` | `activity/MainActivity.kt` (830) | `activity_main.xml` (ViewBinding) | 启动器主界面：左右菜单、`UIManager` 托管 8 个一级 UI、账户/版本展示、主题与动态背景（VideoView/MediaPlayer）、日志分享、通知权限 | 单例弱引用 `getInstance()`；`onPause/onResume` 控制视频背景；`onSaveInstanceState` 保存 modpackHandled；依赖 `ConfigHolder.init()`（若 Splash 被跳过则自行初始化）；账户/版本变化走 fakefx `ObjectProperty` 监听 |
| `WebActivity` | `activity/WebActivity.java` (≈60) | `activity_web.xml` | 通用内置浏览器（JS 开启、进度条） | Intent extra `url` 必传 |
| `ControllerActivity` | `activity/ControllerActivity.java` (80) | 无独立布局：代码构建 `FCLImageView` 背景 + `GameMenu.getLayout()` | 手柄布局编辑/预览沙盒（游戏外） | 持有 `GameMenu` 实例；音量键强行打开左右 Drawer；返回即 finish |
| `ShellActivity` | `activity/ShellActivity.java` (≈150) | `activity_shell.xml` | 内置 Shell 终端（竖屏） | `ShellUtil` 生命周期随 onCreate 启动；`adjustResize` 软键盘 |
| `JVMActivity` | `activity/JVMActivity.java` (279) | `activity_jvm.xml` | 游戏/JAR 运行画面：TextureView Surface 渲染桥接（`FCLBridge`）、游戏内菜单（GameMenu/JarExecutorMenu）、音量键/返回键拦截、Terracotta 联机 | **静态** `fclBridge`/`menuType`/`isRunning` 状态（`setFCLBridge()` 必须先于启动调用）；`TextureView.SurfaceTextureListener` 全生命周期回调；进程存活依赖 `:jvm` 进程 ProcessService |
| `JVMCrashActivity` | `activity/JVMCrashActivity.kt` (176) | `activity_jvm_crash.xml` (ViewBinding) | 崩溃报告页（独立 `:crash` 进程） | Intent extras：`isGame`/`exitCode`/`logPath`；`FLAG_SECURE` 依 `allowScreenshots` 偏好；分享走 FileProvider |
| `FileBrowserActivity` | **FCLLibrary 模块**（红线）：`com.tungsten.fcllibrary.browser` | FCLLibrary 资源 | 文件选择器 | 以 `startActivityForResult`/`ActivityResultLauncher` 方式被 FCL 各页调用 |
| `CrashReportActivity` | **FCLLibrary 模块**（红线）：`com.tungsten.fcllibrary.crash` | FCLLibrary 资源 | Java 层崩溃兜底报告 | 由 FCLLibrary 全局异常处理拉起 |

## 5. Fragment 清单（2 个）

| Fragment | 代码位置（行数） | 布局 | UI 职责 | 生命周期依赖 |
|---|---|---|---|---|
| `EulaFragment` | `fragment/EulaFragment.java` (77) | `fragment_eula.xml` | 首次启动展示用户协议，同意后才进入初始化 | 宿主为 `SplashActivity`；同意后写 `SharedPreferences isAgree=true`；从 assets 读取协议文本 |
| `RuntimeFragment` | `fragment/RuntimeFragment.kt` (311) | `fragment_runtime.xml` (ViewBinding) | 下载/安装运行环境（LWJGL、Cacio、Java 8/17/21/25、JNA），多组件勾选+进度 | 宿主为 `SplashActivity`；`lifecycleScope` 协程 + `RuntimeUtils`；安装完成回调 Activity 继续启动流程 |

---

## 6. 自定义 View 清单（FCL 模块内，13 个类/接口）

### 6.1 `control/view/`（手柄/游戏内控制视图，10 个）

| 类 | 行数 | 继承 | UI 职责 | 生命周期依赖 | 复杂度 |
|---|---|---|---|---|---|
| `ControlButton` | 775 | AppCompatButton | 手柄虚拟按钮：自绘样式、按下/滑动事件、键码映射、属性编辑回调 | 依赖 `GameMenu` 回调与 `CustomControl.ViewType` 数据；注册输入监听，随 ViewManager 移除 | P2 |
| `ControlDirection` | 946 | RelativeLayout | 虚拟方向键/摇杆：手势绘制、8 向/摇杆两种模式、陀螺仪联动 | 同上；持有事件分发与绘制状态 | P2 |
| `TouchPad` | 303 | View | 触控板：手势滑动/点击模拟鼠标 | 依赖 `FCLInput`/`GestureMode`/`MouseMoveMode` | P2 |
| `GameItemBar.kt` | 223 | View | 游戏物品栏悬浮条：自绘格子、触摸选取快捷栏 | 依赖 `GameMenu`/`GameOption`；协程刷新 | P2 |
| `MenuView` | 191 | View | 游戏内菜单展开按钮（悬浮球） | 被 `ControllerActivity`/`JVMActivity` 引用 | P1 |
| `LogWindow` | 97 | ScrollView | 游戏内日志浮窗（自定义 attr `auto_log_tint`） | 由 GameMenu 管理显隐 | P1 |
| `KeycodeView` | 104 | AppCompatButton | 键码展示/选择按钮（自定义 attr `keycode`） | 配合 `view_keyboard`/SelectKeycodeDialog | P1 |
| `ViewManager` | 144 | （管理器，非 View） | 手柄控件视图的创建/回收调度 | 持有 `ViewListener` 回调集合 | P2 |
| `CustomView` | 10 | 接口 | 控件视图统一协议（getType/getViewId/可见性/移除监听） | — | — |
| `ViewListener` | 5 | 接口 | 控件就绪回调 | — | — |

### 6.2 `com/mio/`（4 个）

| 类 | 行数 | 继承 | UI 职责 | 复杂度 |
|---|---|---|---|---|
| `mio/touchcontroller/TouchControllerInputView.kt` | 653 | View | TouchController（触控模组代理）输入视图：多点触控→模组协议转换 | P2 |
| `mio/touchcontroller/TouchController.kt` | 159 | （封装类，非 View） | 触控模组 UnixSocket 客户端封装（震动消息等） | P1 |
| `mio/ui/view/DraggableTextView.kt` | 106 | AppCompatTextView | 可拖动文本（自定义 attr `save_key`，位置持久化到 SharedPreferences） | P1 |
| `mio/ui/view/CursorView.kt` | 38 | AppCompatImageView | 虚拟鼠标光标（偏移量可调） | P1 |
| `mio/ui/widget/FCLAppBarLayout.kt` | 55 | AppBarLayout | AppBar 容器小定制（读取 FCLLibrary 属性） | P0 |

> 说明：`control/` 下另有 GameMenu(1060 行)、JarExecutorMenu(327 行) 等"菜单控制器"类（非 View 子类但承载视图逻辑），`ui/` 各目录为 `FCLCommonUI`/Page/Dialog/Adapter 类，均已在 §1 表格中与布局逐一对应。FCLLibrary 的 `FCL*` 基础组件（FCLButton、FCLSpinner、FCLUILayout、FCLDynamicIsland、SkinViewer 等）被全量使用但属红线模块，不在本清单展开。

---

## 7. 分类汇总

| 类别 | 总数 | P0 | P1 | P2 |
|---|---|---|---|---|
| activity_ 布局 | 6 | 3 | 2 | 1 |
| dialog_ 布局 | 42 | 16 | 25 | 1 |
| fragment_ 布局 | 2 | 1 | 1 | 0 |
| item_ 布局 | 34 | 29 | 5 | 0 |
| menu_ 布局 | 2 | 0 | 0 | 2 |
| page_ 布局 | 27 | 2 | 25 | 0 |
| ui_ 布局 | 8 | 0 | 6 | 2 |
| view_ 布局 | 21 | 2 | 9 | 10 |
| **布局合计** | **142** | **53** | **73** | **16** |
| drawable | 77 | — | — | — |
| Activity | 9（FCL 7 + FCLLibrary 2） | 2 | 4 | 3 |
| Fragment | 2 | 1 | 1 | 0 |
| 自定义 View（FCL） | 13（类/接口） | 1 | 5 | 5（另 2 接口） |

---

## 8. 建议迁移顺序（风险从低到高）

1. **第一批（低风险 · P0 纯展示）**：`page_setting_about`、`page_setting_help`、`dialog_translation`、`dialog_update`、`dialog_task`、各 `item_spinner_*`、其余 16 个 P0 dialog。结构简单、无业务耦合，适合验证 Miuix 主题/组件基座。
2. **第二批（中低风险 · 标准列表+表单页）**：版本管理链路（`page_version_list`→`page_manage_version`→`page_install_version`）、账户链路（`ui_account` + `dialog_create_account`/`dialog_relogin_oauth`/`dialog_offline_account_skin`）、设置主页 `page_setting_launcher`（最大但全是标准控件）、帮助/关于。风险点仅在工作量大。
3. **第三批（中风险 · 下载/管理业务页）**：下载中心全套（`ui_download` + `page_download*` + `page_modpack*`）、管理全套（`ui_manage` + `page_manage_*` + `page_mod_update`）。涉及任务进度、远程仓库数据流、批量操作，需要 Compose 状态管理设计。
4. **第四批（中高风险 · 主框架）**：`activity_main` + 8 个 `ui_*` 一级界面 + `UIManager`/`PageManager` 自研导航栈整体替换；`MainActivity` 的视频背景/`FCLDynamicIsland`/fakefx 属性监听需要 Compose 侧对等实现。`ui_multiplayer`（Terracotta 状态机多视图）与 `ui_main`（SkinViewer GL 皮肤渲染）建议本批最后处理。
5. **第五批（高风险 · 游戏内 UI，建议保留 View 体系或最后攻坚）**：`JVMActivity`/`ControllerActivity` 承载的 GameMenu 全套（`view_game_menu`+`menu_left/right`+`view_keyboard`+`view_edit_*`+`view_*style*`）以及 ControlButton/ControlDirection/TouchPad/GameItemBar/TouchControllerInputView 自绘控件。这些与输入桥接（FCLInput/FCLBridge）、手柄/陀螺仪、悬浮窗权限深度耦合，Compose 化收益低、回归风险最高。
6. **可顺手清理项**：`dialog_relogin_classic.xml`（空且无引用）、`dialog_modpack_selection.xml`（空壳）、`dialog_world_name.xml`（孤儿）——建议与维护者确认后直接删除，不参与迁移。
