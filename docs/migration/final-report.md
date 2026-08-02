# FCL → Miuix UI 迁移最终报告（阶段六 · 小步骤 6.2）

> 生成时间：2026-08-02 ｜ 分支：`feature/miuix-docs`（基于 `feature/miuix-migration`，含阶段一至四全部成果）
> 输入：`方案.md`、`docs/migration/` 全部阶段文档、`regression-report.md`（5.1，同日产出）。
> **边界声明：本工程全程无真机。5.2 性能基准、5.3 设备兼容性与真机功能验收均未执行，是发布前必须补齐的最高优先级事项（清单见 §5）。6.1 代码清理未执行，仅产出候选清单（§6）。**

---

## 1. 迁移总览

### 1.1 范围与成果

| 项 | 数据 |
|---|---|
| 迁移对象 | FCL 模块 142 个 layout XML、9 个 Activity、2 个 Fragment、13 个自定义 View 类（ui-inventory.md） |
| 已 Compose 化 | **83 个 layout**（58.5%），全部带独立回滚开关；「决策应迁移」口径覆盖率 **100%** |
| 保留原生 | 56 个 layout（红线 5、容器壳 6、决策保留活跃页面/项 45）；含 Splash/主 Activity 骨架、游戏内 UI、手柄域页面、管理域 Mod/存档/安装器页面、整合包安装向导、联机界面 |
| 疑似废弃 | 3 个 layout（`dialog_relogin_classic`、`dialog_modpack_selection`、`dialog_world_name`）+ 孤儿类 `ModpackSelectionDialog.java` |
| Compose 开关 | **49 个**（48 true / 1 false），逐点回滚 |
| 新增代码 | `ui/compose/`（基座 + 36 个 Miuix 弹窗类，覆盖 38 个已迁弹窗）、`ui/*/compose/`（6 个页面域）、`activity/compose/`（2 个 Activity）、`ui/bridge/`（桥接层）、`ui/theme/`（主题） |
| 依赖变更 | Compose BOM 2026.06.01、activity-compose 1.13.0、lifecycle-compose 2.10.0、Miuix 0.9.3（ui/icons/preference）、glide-compose 1.0.0-alpha.6，全部走 Version Catalog（foundation-deps.md） |
| APK 体积 | 基线 296.3 MiB → 2.1 后 307.1 MiB（+10.8 MiB，Compose+Miuix 引入）；5.2 体积复测待真机阶段一并进行 |

### 1.2 开关体系

统一模式：编译期 `const val USE_COMPOSE_*`，`true` = Miuix 实现，`false` = 整体回滚旧 View 实现（旧类与旧 XML 全部保留未删）。开关分四层：

- **对话框**（40 个）：`ui/compose/dialog/ComposeDialogs.kt`（39 个）+ `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG`；
- **页面**（7 个）：`ComposeVersionPages`×2、`SettingPageManager.USE_COMPOSE_SETTING_PAGES`、`ComposeDownloadPages`、`ComposeAccountUI`、`ComposeMainUI`×2 中的页面级开关；
- **Activity**（2 个）：`ComposeActivities`（Web/Shell，onCreate 内双路径）；
- 唯一 `false`：`USE_COMPOSE_MULTIPLAYER_MENU`（联机菜单 7 状态 View 体系，有意保留）。

全量开关表（含旧实现位置）见 `regression-report.md` §3。

### 1.3 回滚策略

- **逐点回滚**：任一开关改 false 重新构建即回滚对应页面/弹窗，新旧实现同仓共存、互不干扰；
- **整页回滚**：页面级开关（版本/设置/下载/账户/主页五大页面域 + Web/Shell）各自独立；
- **总回滚点**：tag `pre-miuix-baseline`（2.0 建立，commit `1b1ead1f`）；
- 回滚有效性未经真机逐点演练——真机验收时每个开关的 false 路径需至少抽查一次（§5 清单已含）。

### 1.4 红线遵守情况

| 红线 | 遵守情况 |
|---|---|
| 游戏启动链路（FCLauncher/FCLCore 接口 100% 兼容） | ✅ 未改两模块任何接口；LauncherHelper 仅弹窗调用点接开关 |
| 文件系统操作路径逻辑不可变 | ✅ 零改动 |
| JNI/NDK 链路与 ControllerActivity 结构不动 | ✅ ControllerActivity 仅 +5 行系统栏透明；JVMActivity 仅 +6 行；GameMenu/menu_left/menu_right/view_keyboard 结构零改动（view_keyboard 经 AndroidView 包装复用，触摸链未动） |
| 权限体系不可变 | ✅ 零改动 |
| FCLLibrary/FCLCore 等红线模块 | ✅ 仅在 FCL 侧桥接（ActivityResultLauncher、fakefx→Flow），未改红线模块源码 |
| 冲突热点串行化（libs.versions.toml / FCL/build.gradle.kts / Manifest / theme/） | ✅ git 历史显示热点文件均由对应阶段单独提交，无并行冲突 |

---

## 2. 架构说明

### 2.1 基座（阶段二）

- **Compose 环境**：Kotlin 2.x 官方 Compose Compiler 插件（版本跟随 Kotlin 2.3.20），`buildFeatures { compose = true }`，`viewBinding` 迁移期保留；
- **主题**：`ui/theme/FCLTheme.kt` 封装 MiuixTheme，主色经 fakefx 属性桥观察 ThemeEngine、themeMode 读 SP "launcher"（与 `FCLActivity.applySavedNightMode` 同源），4.3 完成 Light/Dark/FollowSystem 三模式；Design Token 见 `design-tokens.md`、`theme-mapping.md`；
- **组件基座**：`ui/compose/FCLDialog.kt`（Compose 内弹窗组件）、`FCLComposeDialog.kt` + `FCLDialogs.showAlert/showProgress`（命令式弹窗，覆盖 FCLAlertDialog/ProgressDialog 用法）、`MiuixTaskDialog`（AppCompatDialog+ComposeView，与旧 TaskDialog 同命令式 API）；
- **图片**：Glide Compose 1.0.0-alpha.6（与既有 Glide 4.16.0 精确对齐，决策与回退方案见 bridge-api.md §4）；
- **图片/媒体包装**：SkinViewer（GL 皮肤渲染）、WebView、VideoView 动态背景均以 AndroidView 包装保留原生。

### 2.2 桥接层（`ui/bridge/`，迁移期临时设施）

- `LegacyBridge`：Compose→遗留导航（switchUI/showTempPage/dismissCurrentTempPage/onBackPressed）+ 遗留→Compose 弹窗（`requestAlertDialog`/`requestTaskDialog` 单槽位通道，`LegacyDialogHost` 消费）；`createComposeView` 统一挂 FCLTheme + DisposeOnViewTreeLifecycleDestroyed；
- `FCLViewModel` + `FakeFxStateFlow`：UiState/Event 范式基类与 fakefx↔Flow 双向适配器（17 组版本设置绑定按 bridge-api §3.3 对照表承接）；
- 退场条件：全部页面迁移完成后 LegacyBridge 应由 Navigation+ViewModel 取代并删除。

### 2.3 页面形态

Compose 页面统一为 `FCLCommonPage(context, id, parent, R.layout.page_compose_container)` 壳（新增容器布局，复用既有 PageManager 生命周期），内容区 `LegacyBridge.createComposeView` 挂 Screen；因此新旧页面在同一 PageManager 临时页栈内可混跳（下载域、整合包向导均依赖此性质）。

### 2.4 主题与暗黑

50 个 Compose 文件硬编码色审计修正（4.3），全部颜色走 FCLTheme；Shell 终端刻意保留黑底白字（终端语义，activity-migration §5.3）。

---

## 3. 风险登记册核销（对照方案.md 5 项）

| # | 风险 | 状态 | 核销说明 |
|---|---|---|---|
| 1 | Miuix 版本迭代快、API 不稳定 | ✅ 已缓解（持续） | 锁定 0.9.3 并写入 Catalog；组件名全部经源码核实（component-mapping.md）；升级按方案要求单独立项 |
| 2 | Miuix 缺少某原生控件等价物 | ✅ 已核销 | 1.4 映射表提前暴露：三态勾选树（ModpackFileSelectionPage）、联机 7 状态视图、键盘/键码视图、SkinViewer、虚拟摇杆等全部按「保留原生 / AndroidView 包装」落地，无交互退化项硬迁 |
| 3 | Compose 与游戏渲染 surface 共存 | ✅ 已核销（静态层面） | ControllerActivity/JVMActivity 未重写，红线区结构零改动；Profiler 验证待真机（转入 §5 清单 F3） |
| 4 | 多 Agent 改同一热点文件 | ✅ 已核销 | 串行化约定全程遵守，git 历史无热点文件冲突合并 |
| 5 | XML/Compose 双栈长期并存 | ⚠️ 部分核销 | 迁移期双栈是刻意设计（49 个开关逐点回滚）；**硬性清理（6.1）尚未执行**，候选清单见 §6，待真机验收后按序删除，风险敞口存续至清理完成 |

新增登记（5.1 静态核查发现，详见 regression-report.md §6）：

| # | 风险/遗留 | 级别 | 处置建议 |
|---|---|---|---|
| 6 | ~~DriverSelectDialog 未迁移且无开关~~ **已修复**（USE_COMPOSE_DRIVER_SELECT 双分支） | 低 | 已走 FCLDialogs 基座补迁，待真机验证 |
| 7 | ~~Versions.deleteVersion / checkVersionForLaunching / downloadModpackImpl 失败提示、LauncherHelper 启动进度 TaskDialog 未接开关~~ **已修复**（USE_COMPOSE_VERSION_OP_ALERTS / USE_COMPOSE_TASK_DIALOG 双分支；titleProperty 预警经核实不存在，见 regression-report §4.2-#1 勘误） | 低 | 同上，待真机验证 |
| 8 | `check*AarMetadata` 检查被禁用（miuix 要求 minCompileSdk=37 > 项目 35） | 中 | 升级 AGP 9.x + compileSdk 37 后必须移除 FCL/build.gradle.kts 中的禁用块并恢复检查（foundation-deps.md §5） |
| 9 | glide-compose 1.0.0-alpha.6 为 alpha 构件，未真机验证 | 低 | 首次真机运行含图片页面时留意；零成本回退方案见 bridge-api §4.4 |
| 10 | 弹窗单槽位不排队（requestAlertDialog 占用返回 false） | 低 | 并发弹窗场景（自动更新+任务失败）真机验证 F9；确需时扩展 Channel |

---

## 4. 各阶段产物索引

| 阶段 | 产物 |
|---|---|
| 1.1 UI 资产盘点 | `ui-inventory.md` |
| 1.2 交互逻辑映射 | `interaction-map.md` |
| 1.3 设计令牌 | `design-tokens.md` |
| 1.4 组件映射表 | `component-mapping.md` |
| 2.0 基线快照 | `baseline.md`（tag `pre-miuix-baseline`，APK 296.3 MiB） |
| 2.1 依赖与 Compose 环境 | `foundation-deps.md` |
| 2.2 主题基座 | `theme-mapping.md` + `ui/theme/` |
| 2.3 桥接层 | `bridge-api.md` + `ui/bridge/` |
| 3.1–3.6 逐页迁移 | 代码 + 各 merge commit（构建通过，真机冒烟待补） |
| 3.7 其余 Activity 处置 | `activity-migration.md` |
| 4.1–4.3 打磨 | 代码 + commit（4.1 自定义 View / 4.2 动画 / 4.3 暗黑） |
| 5.1 静态回归核查 | `regression-report.md` |
| 6.2 文档与最终报告 | 本文件 + README Miuix 小节 |

---

## 5. 真机验证清单汇总（各阶段清单合并去重，发布前必须全量执行）

> 来源：activity-migration.md §4、bridge-api.md §6、各 merge commit「真机冒烟待补」登记、regression-report.md §5。去重后按域分组；每项标注涉及开关（改 false 重新构建即回滚复测）。**本清单与 regression-report.md §5 互为同一份内容的两视图：此处按验收域组织，彼处按方案 5.1 功能项组织。**

### 5.1 启动与主页（开关：USE_COMPOSE_MAIN_UI / USE_COMPOSE_VERSION_PAGES / USE_COMPOSE_CREATE_ACCOUNT / USE_COMPOSE_SKIP_LOGIN / USE_COMPOSE_TIP_RELOGIN）

- [ ] 冷启动 → Splash（保留原生）→ 主页 Compose 右侧栏/内容区渲染、皮肤 3D 展示、公告栏
- [ ] 启动游戏全链路（A1–A5，regression-report §5.1-A）；启动进度旧 TaskDialog 为已知遗留，记录不判失败
- [ ] 动态背景（VideoView）、FCLDynamicIsland、左侧菜单 7 项切换无回归

### 5.2 账户（USE_COMPOSE_ACCOUNT_UI / CREATE_ACCOUNT / CHARACTER_SELECTOR / OFFLINE_ACCOUNT_SKIN / RELOGIN_OAUTH / ADD_AUTHLIB_INJECTOR_SERVER）

- [ ] 微软登录 / 离线创建+角色 / 皮肤设置 / OAuth 重登 / 外置服务器（D1–D6）

### 5.3 下载与安装（USE_COMPOSE_DOWNLOAD_PAGES / USE_COMPOSE_TASK_DIALOG / USE_COMPOSE_DOWNLOAD_ADDON / USE_COMPOSE_MODPACK_URL / USE_COMPOSE_TRANSLATION）

- [ ] Forge/Fabric/NeoForge 安装（B1–B4）；Mod/整合包/资源包/光影/存档 5 远程 Tab 搜索→详情→版本→下载链
- [ ] 导入整合包/存档（C1–C5，向导保留原生属刻意设计）；外部 Intent 导入（ImportActivity alias）

### 5.4 版本与管理（USE_COMPOSE_VERSION_SETTING / RENAME_VERSION / DUPLICATE_VERSION / WORLD_EXPORT / MOD_INFO / ROLLBACK_MOD / JAVA_MANAGE / RENDERER_SELECT）

- [ ] 版本列表选择/重命名/复制/删除（L2 已修复：删除确认走 USE_COMPOSE_VERSION_OP_ALERTS 开关双分支）；全局/单独设置 17 组绑定双向同步
- [ ] Mod 管理、存档管理、数据包、自动安装器（页面保留原生，同体系弹窗一致）
- [ ] DriverSelectDialog（L1 已修复：USE_COMPOSE_DRIVER_SELECT 开关双分支，真机验证 Miuix 驱动列表选择与回写）；任务弹窗速度/取消/autoClose/不确定态（F8，含 L4 已修复的启动进度弹窗链路）

### 5.5 游戏内与控制（红线区 + 弹窗开关组）

- [ ] 虚拟鼠标/按键映射/手柄编辑/快捷输入/物品栏（E1–E6；view_keyboard AndroidView 触摸链重点）
- [ ] ControllerActivity/JVMActivity 系统栏透明与既有行为（F3）；GameMenu Drawer/音量键/返回键
- [ ] 联机菜单 7 状态（保留原生）+ 邀请码 Miuix 弹窗

### 5.6 其余 Activity 与通用面（USE_COMPOSE_WEB / USE_COMPOSE_SHELL）

- [ ] Web/Shell 正向 + 回滚（F1/F2）；JVMCrashActivity（F4）
- [ ] 主题联动与三模式切换（F5）；设置/帮助/关于（F6）；单槽位回退（F9）；动画（F10）
- [ ] 无障碍/字体缩放/minSdk 26/大屏/刘海（F11，含方案 5.3）
- [ ] 性能：冷启动（退化 ≤5%）、内存（≤10%）、列表帧率、APK 体积对照 baseline.md（方案 5.2）
- [ ] glide-compose alpha 图片场景首验（风险 #9）

---

## 6. 6.1 清理候选清单（**仅清单，待真机验收通过后执行，本步不删任何代码**）

### 6.1-A 可立即删除（废弃项，无引用，风险零）

1. `FCL/src/main/res/layout/dialog_relogin_classic.xml`（空布局，全仓无引用）
2. `FCL/src/main/res/layout/dialog_modpack_selection.xml`（空壳）
3. `FCL/src/main/res/layout/dialog_world_name.xml`（孤儿）
4. `FCL/src/main/java/com/tungsten/fcl/ui/version/ModpackSelectionDialog.java`（孤儿类，全仓无实例化）

### 6.1-B 开关固化后删除（前提：对应开关真机验收通过且团队决定固化 true）

删除顺序建议（先叶子后主干，每批删除后过构建门禁 + 冒烟）：

1. **批 1 · 回滚弹窗类**（39 个 ComposeDialogs 开关 + MiuixTaskDialog 已验收项）：删除旧 Dialog 类（TranslationDialog、UpdateDialog、OldVersionDialog、DuplicateVersionDialog、RenameVersionDialog、WorldExportDialog、AddProfileDialog、ModInfoDialog、ModRollbackDialog、JavaManageDialog、RendererSelectDialog、GamepadMapDialog、GameItemBarSettingDialog、OpenFolderDialog、SelectKeycodeDialog、CreateAccountDialog、AddAuthlibInjectorServerDialog、OAuthAccountLoginDialog、OfflineAccountSkinDialog、QuickInputDialog、AddInputTextDialog、AddButtonStyleDialog、AddDirectionStyleDialog、ButtonStyleDialog、DirectionStyleDialog、EditViewDialog、EditViewGroupDialog、ViewGroupDialog、SelectControllerDialog、ControllerInfoDialog、ControllerUploadDialog、TaskDialog 已替换调用点部分）+ 对应 38 个 dialog XML 与回滚分支代码、开关常量；
2. **批 2 · 回滚页面类**：VersionListPage、ManagePage、VersionSettingPage、LauncherSettingPage、HelpPage、AboutPage、VersionInstallPage、DownloadPage 5 子类、RemoteMod* 临时页 + 11 个 page XML + 对应 item XML（item_version/item_manage/item_profile/item_article/item_remote_mod/item_mod_version/item_account 等 18 项）+ 页面开关常量；
3. **批 3 · 一级 UI 与 Activity 回滚路径**：MainUI（ui_main.xml）、AccountUI（ui_account.xml）、WebActivity/ShellActivity 的 View 路径（activity_web/activity_shell.xml）+ 4 个开关常量；
4. **批 4 · 依赖与构建**：全部开关删除后，移除 `viewBinding`（需先清完 ViewBinding 引用）、评估移除 `material`/`appcompat`/`constraintlayout` 依赖（前提：保留原生页面不再使用——**当前仍大量使用，预计本批仅能做部分**）；恢复 `check*AarMetadata`（需先升级 AGP/compileSdk，风险 #8）。

### 6.1-C 保留不删（红线与决策保留）

- 游戏侧：menu_left/right、view_game_menu、view_keyboard、view_jar_executor_menu、activity_jvm、view_multiplayer_*、dialog_multiplyer_menu 及 GameMenu/ControlButton/ControlDirection/TouchPad 等全部游戏内 View；
- 保留原生页面：ui_version/ui_manage/ui_download/ui_controller/ui_setting/ui_multiplayer 容器、手柄域 4 页、管理域 Mod/存档/安装器链路、整合包向导 6 页、activity_splash/activity_jvm_crash、fragment_eula/fragment_runtime；
- `page_compose_container.xml`（Compose 页面壳，长期使用）；
- FCLLibrary `FCL*` 组件：保留原生页面与游戏侧仍在使用，随 6.1-B 批 4 再评估。

---

## 7. 构建门禁（本步）

- 命令：`GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug --console=plain`
- 结果：**BUILD SUCCESSFUL in 9s**（155 actionable tasks：8 executed / 147 up-to-date；本步无代码改动，全量增量命中）。
- 本步变更文件：`docs/migration/regression-report.md`（新）、`docs/migration/final-report.md`（新）、`README.md`（新增 Miuix 小节）；零代码/零资源/零 Gradle 配置改动，无需额外回归。

## 8. 遗留问题与后续行动（按优先级）

1. **真机验收**（最高优先）：§5 清单全量执行；通过前不得执行 6.1 删除、不得发布。
2. ~~小步骤补迁 L1–L4~~ **已完成**（DriverSelectDialog、Versions 三处、LauncherHelper TaskDialog 均已走既有 FCLDialogs/MiuixTaskDialog 基座补迁，见 regression-report §4.1）。
3. **SplashActivity + 2 个 Fragment 迁移**：单独立项（activity-migration §5.1）。
4. **6.1 清理**：按 §6 顺序执行，每批过门禁。
5. **构建栈升级**：AGP 9.x + compileSdk 37 后恢复 checkAarMetadata、评估 miuix-blur 引入（minSdk 33 冲突待产品决策）。
