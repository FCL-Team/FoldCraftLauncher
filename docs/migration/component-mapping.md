# 原生控件 → Miuix 组件映射表（阶段一 · 小步骤 1.4）

> 生成时间：2026-08-01 ｜ 分支：`feature/miuix-migration`
> 输入：`docs/migration/ui-inventory.md`（142 布局/13 自定义 View）、`docs/migration/interaction-map.md`（547 条交互）、`docs/migration/design-tokens.md`（设计令牌）。
> 本文档是后续 Compose/Miuix 迁移时选择目标组件的唯一事实来源。

---

## 0. Miuix 0.8.8 核实结论（所有组件名的来源）

### 0.1 核实方法

1. **源码树**：GitHub 仓库 `compose-miuix-ui/miuix`（原 `miuix-kotlin-multiplatform/miuix`，已迁移），按 tag **`v0.8.8`** 拉取完整 git tree（838 个路径，`truncated: false`），逐一列出 `miuix/src/commonMain/kotlin/top/yukonga/miuix/kmp/` 下全部组件文件。
   - 树地址：https://github.com/compose-miuix-ui/miuix/tree/v0.8.8
2. **Maven Central**：逐个 HTTP 校验 `https://repo1.maven.org/maven2/top/yukonga/miuix/kmp/<artifact>/0.8.8/<artifact>-0.8.8.module` 的存在性（200/404）。
3. **API 签名**：抓取 v0.8.8 raw 源码文件，读取公开 `@Composable fun` 签名确认准确命名与参数（下文「来源」列均为 tag v0.8.8 下的相对路径）。
4. **组件文档**：仓库内 `docs/components/*.md`（含 `docs/zh_CN/components/*.md` 中文版），与源码清单一一对应。

### 0.2 Maven 子模块（0.8.8 实际存在性，Maven Central 逐一校验）

| 构件（`top.yukonga.miuix.kmp:*`） | 0.8.8 是否存在 | 说明 |
|---|---|---|
| `miuix` | ✅ 存在（含 `miuix-android:0.8.8`） | **核心 UI 库**，全部组件在此 |
| `miuix-icons` | ✅ 存在 | 扩展图标库（`MiuixIcons.*`，Light/Regular/Heavy 三字重） |
| `miuix-navigation3-ui` | ✅ 存在 | androidx.navigation3 的 fork（`NavDisplay`），可选导航方案 |
| `miuix-preference` | ❌ **0.8.8 不存在**（0.9.0 才首现） | 0.8.8 无 Preference 组件，设置页用 `BasicComponent`/`Super*` 系列自建 |
| `miuix-blur` | ❌ **0.8.8 不存在**（0.9.1 才首现） | 0.8.8 无官方模糊库 |
| `miuix-squircle` | ❌ **0.8.8 不存在**（0.9.2 才首现） | 0.8.8 平滑圆角逻辑在 `miuix` 模块内部 `theme/SmoothRounding.kt` |
| `miuix-ui` | ❌ **0.8.8 不存在**（0.9.x 起的改名） | 0.8.8 核心模块名就是 `miuix`，注意与新版文档区分 |

> **关键结论**：本工程锁定 0.8.8，则依赖坐标为
> `top.yukonga.miuix.kmp:miuix:0.8.8`（必需）+ `top.yukonga.miuix.kmp:miuix-icons:0.8.8`（建议）+ `top.yukonga.miuix.kmp:miuix-navigation3-ui:0.8.8`（可选）。
> 任务前置假设中的 `miuix-preference`/`miuix-blur` 在 0.8.8 均不可用，相关映射（设置页、模糊）已按下表改为 `miuix` 模块内组件或自研。

### 0.3 Miuix 0.8.8 全部组件清单（源码核实，按包分组）

包前缀均为 `top.yukonga.miuix.kmp.`，模块均为 `miuix`（除非另行标注）。

- **`basic/`**（路径 `miuix/src/commonMain/kotlin/top/yukonga/miuix/kmp/basic/`）：
  `BasicComponent`（Component.kt）、`Button` / `TextButton`（Button.kt）、`Card`（Card.kt，含可点击重载 `pressFeedbackType`）、`Checkbox`（Checkbox.kt）、`ColorPalette`（ColorPalette.kt）、`ColorPicker` / `HsvColorPicker` / `OkHsvColorPicker` / `OkLabColorPicker` / `OkLchColorPicker` 及配套 Slider（ColorPicker.kt）、`HorizontalDivider` / `VerticalDivider`（Divider.kt）、`FloatingActionButton`（FloatingActionButton.kt）、`FloatingToolbar`（FloatingToolbar.kt）、`Icon`（Icon.kt）、`IconButton`（IconButton.kt）、`ListPopupColumn` / `ListPopupContent`（ListPopup.kt）、`NavigationBar` / `NavigationBarItem` / `FloatingNavigationBar` / `FloatingNavigationBarItem`（NavigationBar.kt）、`NavigationRail` / `NavigationRailItem`（NavigationRail.kt）、`NumberPicker`（NumberPicker.kt）、`LinearProgressIndicator` / `CircularProgressIndicator` / `InfiniteProgressIndicator`（ProgressIndicator.kt，三者 `progress: Float? = null` 即不定态）、`PullToRefresh` / `rememberPullToRefreshState`（PullToRefresh.kt）、`RadioButton`（RadioButton.kt）、`Scaffold`（Scaffold.kt，内置 `popupHost = MiuixPopupHost()`）、`VerticalScrollBar` / `HorizontalScrollBar`（ScrollBar.kt）、`SearchBar` / `InputField`（SearchBar.kt）、`Slider` / `VerticalSlider` / `RangeSlider`（Slider.kt，支持 `steps`/`keyPoints`/`magnetThreshold`/触觉反馈）、`SmallTitle`（SmallTitle.kt）、`Snackbar` / `SnackbarHost` / `SnackbarHostState`（Snackbar.kt）、`Surface`（Surface.kt）、`Switch`（Switch.kt）、`TabRow` / `TabRowWithContour`（TabRow.kt，数据驱动 `tabs: List<String>`）、`Text`（Text.kt）、`TextField`（TextField.kt，`state: TextFieldState` + `label`）、`TopAppBar` / `SmallTopAppBar` / `MiuixScrollBehavior`（TopAppBar.kt）。
- **`extra/`**（同前缀 `extra/`）：`SuperArrow`（SuperArrow.kt）、`SuperSwitch`（SuperSwitch.kt）、`SuperCheckbox`（SuperCheckbox.kt）、`SuperRadioButton`（SuperRadioButton.kt）、`SuperDialog`（SuperDialog.kt，`show: Boolean/MutableState<Boolean>`，渲染于 Scaffold 内，`renderInRootScaffold`）、`SuperSpinner`（SuperSpinner.kt，对话框式下拉）、`SuperDropdown`（SuperDropdown.kt）、`SuperListPopup`（SuperListPopup.kt）、`SuperBottomSheet`（SuperBottomSheet.kt）、`WindowDialog` / `WindowSpinner` / `WindowDropdown` / `WindowListPopup` / `WindowBottomSheet`（Window*.kt，渲染于平台 Dialog window）。
  - 下拉数据类 `SpinnerEntry(icon, title, summary)` 定义于 `basic/Dropdown.kt:316`；`DropdownImpl`/`SpinnerItemImpl` 为内部实现，公开入口是上述 `Super*`/`Window*` 系列。
- **`theme/`**：`MiuixTheme`（MiuixTheme.kt）、`lightColorScheme()` / `darkColorScheme()`（Colors.kt）、`ThemeController` + `ColorSchemeMode`（含 Monet 动态取色，ThemeController.kt）、`TextStyles`（TextStyles.kt）、`SmoothRounding`（平滑圆角，theme/SmoothRounding.kt）。
- **`utils/`**：`MiuixPopupUtils`（弹窗管理）、`overScrollVertical()` / `overScrollHorizontal()`（Overscroll.kt，越界回弹）、`PressFeedback`（按压反馈）、`platform()` / `getRoundedCorner()` / `platformDialogProperties()`（Utils.kt）。
- **`anim/`**：`MiuixEasing` 等缓动曲线。
- **图标**：`MiuixIcons.Basic.*`（仅 5 个：ArrowRight、ArrowUpDown、Check、Search、SearchCleanup，在 `miuix` 模块 `icon/basic/`）；扩展图标 150+（`miuix-icons` 模块 `icon/extended/`，如 `MiuixIcons.Settings`、`MiuixIcons.Refresh`）。

### 0.4 0.8.8 明确**没有**的 API（防止迁移时误判）

| 前置假设/常见需求 | 0.8.8 实际情况 | 替代方案 |
|---|---|---|
| `miuix-preference`（Preference 组件） | 不存在 | 用 `Card` + `BasicComponent`/`SuperSwitch`/`SuperArrow`/`SuperSpinner` 组合（即 Miuix 官方设置页写法） |
| `miuix-blur` | 不存在 | 半透明底色（`Color.copy(alpha)`）或自研 `Modifier.blur`（API 31+） |
| WindowSizeClass 处理 API | **全仓库无任何 WindowSizeClass 代码**（源码树 grep 核实） | Compose `BoxWithConstraints` 或 androidx `material3-window-size-class` 自研断点；弹窗大屏自适应已由 `DialogLayout(enableAutoLargeScreen=true)` 内置 |
| `LazyColumn` 等列表封装 | 不提供（Miuix 只给滚动条/回弹修饰符） | 直接用 compose-foundation `LazyColumn` + `Modifier.overScrollVertical()` |
| Toast | 不提供 | 保留 `android.widget.Toast`，或改 `Snackbar`（`miuix`） |
| 下拉刷新 SwipeRefreshLayout 对照 | 有 `PullToRefresh`（basic/PullToRefresh.kt），但**项目现状无下拉刷新**（interaction-map G9 核实），仅作可选增强 | — |

---

## 1. 映射总表

> 「使用次数」为 `FCL/src/main/res/layout/` 全量 grep 实测（XML 标签出现次数/涉及文件数）；对话框类使用次数来自 interaction-map.md。
> 「迁移方式」三选一：**Compose 重写（Miuix 替换）** / **保留原生 + AndroidView 包装** / **删除（死代码）**。
> maven 模块列中 `—` 表示不使用 Miuix 组件。

### 1.1 原生 / AndroidX / Material 控件

| 原生控件 | 使用次数/代表文件 | Miuix 目标组件 | 所在 maven 模块 | 迁移方式 | 备注 |
|---|---|---|---|---|---|
| `TextView` | 5 次/4 文件（如 `page_setting_about.xml`） | `Text`（basic/Text.kt） | `miuix` | Compose 重写 | 跑马灯需求（`ProfileListAdapter` 路径文本）需自研 `Modifier` 或 `basicMarquee`（foundation） |
| `EditText` | 0（全部走 FCLEditText，见 1.2） | `TextField` | `miuix` | Compose 重写 | — |
| `Button` | 0（全部走 FCLButton） | `Button`/`TextButton` | `miuix` | Compose 重写 | — |
| `ImageView` | 1 次/`page_setting_about.xml` | `Icon` / `Image`（compose-foundation） | `miuix` | Compose 重写 | 网络图用 Coil `AsyncImage`（替换 Glide 的 Compose 方案） |
| `ImageButton` | 0（全部走 FCLImageButton） | `IconButton` | `miuix` | Compose 重写 | — |
| `CheckBox` | 0（全部走 FCLCheckBox） | `Checkbox`/`SuperCheckbox` | `miuix` | Compose 重写 | — |
| `RadioButton` + `RadioGroup` | RadioGroup 1 次/`page_version_list.xml`（内为 FCLRadioButton） | `RadioButton`/`SuperRadioButton` | `miuix` | Compose 重写 | 分类过滤条改 `Row` + `RadioButton` 或 `TabRow` |
| `Switch`（Material） | 0（全部走 FCLSwitch） | `Switch`/`SuperSwitch` | `miuix` | Compose 重写 | — |
| `SeekBar` | 0（走 FCLNumberSeekBar/FCLPreciseSeekBar） | `Slider` | `miuix` | Compose 重写 | — |
| `ProgressBar` | 1 次/`activity_web.xml` | `LinearProgressIndicator` / `CircularProgressIndicator` / `InfiniteProgressIndicator` | `miuix` | Compose 重写 | `progress: Float? = null` 即不定态，覆盖现有两种 bg_progress drawable |
| `Spinner` | 0（全部走 FCLSpinner） | `SuperSpinner`/`WindowSpinner` | `miuix` | Compose 重写 | — |
| `RecyclerView` | 13 次/12 文件（`page_version_list.xml`、`ui_account.xml` 等） | `LazyColumn` / `LazyRow`（compose-foundation）+ `Modifier.overScrollVertical()` | —（非 Miuix） | Compose 重写 | Adapter/ViewHolder 全部废弃；入场动画改 `Modifier.animateItem()`；`VerticalScrollBar` 可选 |
| `ListView`（含 FCLAdapter 体系） | 23 次/22 文件（`dialog_task.xml`、`page_mod_update.xml` 等） | `LazyColumn` | —（非 Miuix） | Compose 重写 | `TaskListPane` 等 BaseAdapter 手动刷新全部改 state 驱动 |
| `ScrollView` | 37 次/35 文件 | `Column` + `Modifier.verticalScroll()` | —（非 Miuix） | Compose 重写 | 嵌套滚动（RemoteModDownloadPage 手动测高）直接由 Compose 布局消解 |
| `HorizontalScrollView` | 1 次/`page_version_list.xml` | `Row` + `Modifier.horizontalScroll()` | —（非 Miuix） | Compose 重写 | — |
| `androidx.drawerlayout.widget.DrawerLayout` | 1 次/`view_game_menu.xml` | — | — | **保留原生** | 游戏内左右抽屉（GameMenu），红线区，见 §3 |
| `WebView` | 1 次/`activity_web.xml` | — | — | 保留原生 + AndroidView | `WebActivity` 页面壳可 Compose 化，WebView 本体 AndroidView 包装 |
| `VideoView` | 1 次/`activity_main.xml`（动态背景） | — | — | 保留原生 + AndroidView | 循环播放+暂停续播+音量控制与生命周期耦合，Compose 化收益低 |
| `TextureView` | 1 次/`activity_jvm.xml` | — | — | **保留原生 + AndroidView（红线）** | SurfaceTexture 时序不可变（interaction-map 1.7） |
| `com.google.android.material`（Material 组件） | 0 次直接引用（仅 `FCLAppBarLayout` 继承 AppBarLayout） | `TopAppBar` 系列 | `miuix` | Compose 重写 | 工程实际几乎不直接用 Material 控件，主题仅挂 MaterialComponents 父主题 |
| 原生 `AlertDialog`（appcompat） | 1 处（`RemoteVersionListAdapter.kt:79-108` 镜像 URL 列表） | `SuperDialog` + `ListPopupColumn` 或 `WindowSpinner` | `miuix` | Compose 重写 | 列表选择场景 |
| 原生 `ProgressDialog` | 7 处/4 文件（`Versions.java`×3、`ManagePage.kt`×2、`ControllerUploadPage`、`ControllerManagePage`） | 自研 `FCLProgressDialog` = `SuperDialog` + `CircularProgressIndicator` | `miuix` | Compose 重写 | 见 §4 自研清单 |
| `Toast` | 91 处/42 文件 | 保留 `android.widget.Toast`（轻提示）/ 需要用户注意时改 `Snackbar` | `miuix`（Snackbar） | 保留原生 | Toast 非 UI 组件，不阻碍 Compose 化；统一封装 `ToastUtil` 即可 |
| `SwipeRefreshLayout` / `Snackbar`（Material） | 0（interaction-map G9 核实不存在） | `PullToRefresh` / `Snackbar` | `miuix` | 无需迁移 | 仅作为可选增强登记 |
| `BottomSheetDialog`（Material） | 0（项目未使用） | `SuperBottomSheet` / `WindowBottomSheet` | `miuix` | 无需迁移 | 登记备查：联机菜单等底部面板场景可选用 |
| `AppBarLayout` 滚动联动（`page_version_list.xml` scroll flags） | 4 文件经 `FCLAppBarLayout` | `TopAppBar` + `MiuixScrollBehavior`（basic/TopAppBar.kt） | `miuix` | Compose 重写 | `scrollBehavior.nestedScrollConnection` 接 `LazyColumn` |

### 1.2 FCLLibrary `FCL*` 基础组件（红线模块，仅替换 FCL 侧使用点）

| FCLLibrary 组件 | 使用次数/代表文件 | Miuix 目标组件 | 所在 maven 模块 | 迁移方式 | 备注 |
|---|---|---|---|---|---|
| `FCLTextView` | 498 次/110 文件 | `Text` | `miuix` | Compose 重写 | autoTint 着色改 `MiuixTheme.colorScheme` |
| `FCLButton` | 205 次/72 文件 | `Button`（主按钮）/ `TextButton`（次要） | `miuix` | Compose 重写 | 圆角 8dp 对齐 `ButtonDefaults.CornerRadius`；按压缩放改 `pressFeedback`（utils/PressFeedback.kt） |
| `FCLLinearLayout` | 202 次/48 文件 | `Column` / `Row` | —（非 Miuix） | Compose 重写 | — |
| `FCLImageButton` | 63 次/32 文件 | `IconButton` | `miuix` | Compose 重写 | — |
| `FCLImageView` | 59 次/29 文件 | `Icon` / `Image` / Coil `AsyncImage` | `miuix` | Compose 重写 | 主题着色改 `ColorFilter.tint` |
| `FCLEditText` | 58 次/34 文件 | `TextField`（`state` + `label`） | `miuix` | Compose 重写 | TextWatcher → `snapshotFlow(state.text)`；长按全屏编辑场景见 `FullEditDialog` 自研 |
| `FCLSwitch` | 50 次/11 文件（设置页、GameMenu 抽屉） | 设置项：`SuperSwitch`；行内：`Switch` | `miuix` | Compose 重写 | fakefx `bindBoolean` → `MutableState`；对话框反向 setChecked 语义注意重组循环（interaction-map §9） |
| `FCLProgressBar` | 38 次/29 文件 | `LinearProgressIndicator` / `CircularProgressIndicator` / `InfiniteProgressIndicator` | `miuix` | Compose 重写 | `percentProgressProperty().bind()` → 采集任务 `StateFlow` |
| `FCLSpinner` | 27 次/12 文件（设置、下载筛选、WorldInfo 等） | `SuperSpinner`（对话框式，设置页默认）/ `WindowSpinner`（窗口下拉式） | `miuix` | Compose 重写 | `item_spinner*` 7 个布局全部废弃，改 `SpinnerEntry(title, summary, icon)` 数据驱动；`FXUtils.bindSelection` → `selectedIndex` + `onSelectedIndexChange` |
| `FCLCheckBox` | 19 次/12 文件 | `SuperCheckbox`（带标题）/ `Checkbox`（行内） | `miuix` | Compose 重写 | 复用解绑防误触发问题在 Compose 中天然消解 |
| `FCLRadioButton` | 15 次/7 文件 | `SuperRadioButton` / `RadioButton` | `miuix` | Compose 重写 | — |
| `FCLNumberSeekBar` | 18 次/5 文件（GameMenu 参数条等） | `Slider` + `Text`（值显示） | `miuix` | Compose 重写 | `steps`、`hapticEffect` 对齐刻度手感 |
| `FCLPreciseSeekBar` | 16 次（GameMenu 灵敏度等） | `Slider`（`keyPoints` + `magnetThreshold`） | `miuix` | Compose 重写 | 精细吸附用磁吸参数复刻 |
| `FCLSeekBar` | 0（基类，未直接入布局） | `Slider` | `miuix` | Compose 重写 | — |
| `FCLConstraintLayout` | 9 次/8 文件（`item_remote_mod.xml` 等） | `ConstraintLayout`（compose）或 `Box`/`Column` | —（非 Miuix） | Compose 重写 | 主题 tint 改 colorScheme |
| `FCLCheckedTextView` | 2 次（`item_spinner_dropdown.xml`） | `SpinnerEntry` + SuperSpinner 内置选中态 | `miuix` | Compose 重写 | 随 Spinner 体系消亡 |
| `FCLView` | 3 次 | `Box` / `Surface` | `miuix` | Compose 重写 | — |
| `FCLTabLayout` | 6 次/6 文件（ManageUI/DownloadUI/SettingUI/CreateAccountDialog 等） | `TabRow`（`tabs: List<String>` 数据驱动） | `miuix` | Compose 重写 | `tabLayout.selectTab()` 跨 UI 联动改导航参数传递（interaction-map G11） |
| `FCLMenuView` | 7 次/1 文件（`activity_main.xml` 左侧导航） | `NavigationRail` + `NavigationRailItem`，或自研 NavItem（`IconButton`+动画） | `miuix` | Compose 重写 | 选中动画（旋转 360°+缩放 Overshoot）无现成对等，需 `animateFloatAsState` 自研；建议自研 NavItem 保留原版手感 |
| `FCLUILayout` | 6 次/6 文件（各 `ui_*.xml` 容器） | `Scaffold` / `Box` + 导航容器 | `miuix` | Compose 重写 | UIManager 双层导航栈整体重建，见 1.4 |
| `FCLDynamicIsland` | 1 次/`activity_main.xml` | 无对等组件 → 自研（`Card`/`Surface` + 动画） | `miuix` | Compose 重写（自研） | 灵动岛通知，见 §4 自研清单 |
| `HorizontalListView` | 布局 0 次；代码用于 `ControllerDownloadPage` 横向截图 | `LazyRow` | —（非 Miuix） | Compose 重写 | — |
| `SkinViewer`（`fcllibrary.skin`，GL 3D 皮肤渲染） | 2 次（`ui_main.xml`、`item_account.xml`）+ `OfflineAccountSkinDialog` 内嵌 SkinRenderer | — | — | 保留原生 + AndroidView | OpenGL 渲染器，Compose 化无收益；AndroidView 托管 + 生命周期手动同步（interaction-map 2.6） |
| `FCLCheckBoxTreeAdapter`（三态文件树） | `page_modpack_file.xml`（ModpackFileSelectionPage） | 无对等组件 → 自研 Compose 树 | — | Compose 重写（自研） | indeterminate 半选传播逻辑保留，见 §4 |
| `ThemeEngine`/`Theme`（运行时换肤引擎） | 20+ 调用点 | `MiuixTheme(colors = ...)` + 自定义 `Colors`；动态取色评估 `ThemeController(ColorSchemeMode.MonetSystem, keyColor)` | `miuix` | Compose 重写 | FCL 是三主题色体系（color/color2/color2Dark + HSV 派生），需派生算法移植（design-tokens §2），不能只套 lightColorScheme |
| `FileBrowserActivity` / `FileBrowserLauncher` | 全工程文件选择统一入口 | 保留原生 Activity + `rememberLauncherForActivityResult` | — | 保留原生 | interaction-map G8；SAF 双路径逻辑不动 |
| `FCLActivity` 基类（主题/权限封装） | 全部 Activity | Activity 保留，UI 层 `setContent { MiuixTheme { ... } }` | — | 保留原生 | 渐进迁移的桥 |

### 1.3 FCLLibrary 对话框体系（38 个 FCLDialog 子类 + helper）

| 原组件 | 使用次数/代表文件 | Miuix 目标组件 | 所在 maven 模块 | 迁移方式 | 备注 |
|---|---|---|---|---|---|
| `FCLDialog`（自定义 Dialog 基类） | 38 个子类（附录 A，interaction-map G7） | 自研 `FCLDialog` 基座，基于 **`SuperDialog`**（extra/SuperDialog.kt）封装 | `miuix` | Compose 重写（自研基座） | 选 SuperDialog 理由：`show: MutableState<Boolean>` 驱动、Scaffold 内渲染（`renderInRootScaffold`）、`onDismissRequest=null` 即 `setCancelable(false)`、内置进出场动画与大屏自适应；`WindowDialog` 作备用（需在 Dialog window 中渲染时） |
| `FCLAlertDialog`（确认/错误/警告） | 全工程最高频对话框（含 `DialogUtil.showErrorDialog/showWarningDialog/showItemSelectionDialog`） | `SuperDialog(title, summary)` + 按钮行 | `miuix` | Compose 重写 | 阻塞式 `CountDownLatch` 等待结果处（AccountListItem ×2、CreateAccountDialog 角色选择）必须改挂起函数 |
| `EditDialog`（单行输入） | 10+ 处（跳页码、改 UUID、jar 参数、世界命名） | 自研 `EditDialog` = `SuperDialog` + `TextField` | `miuix` | Compose 重写（自研） | 输入校验 Toast 逻辑保留 |
| `FullEditDialog`（全屏多行编辑） | 2 处（JVM/游戏参数、环境变量） | 自研：`WindowDialog` 全屏变体 或独立页面 + `TextField` | `miuix` | Compose 重写（自研） | 全屏表单建议直接做成临时页而非 dialog |
| `FCLColorPickerDialog`（取色器） | 3 处主题色（LauncherSettingPage） | `SuperDialog` + `ColorPalette`（basic/ColorPalette.kt）或 `ColorPicker` 家族 | `miuix` | Compose 重写 | **0.8.8 自带取色器**，拖动实时预览/确定保存/取消还原三回调用 state 快照实现 |
| `FullImageDialog`（全屏看图） | 1 处（截图长按，RemoteModScreenshotAdapter） | 无现成 → 自研 `SuperDialog` + 缩放 Image | `miuix` | Compose 重写（自研） | 需 pinch 缩放 gesture |
| `TaskDialog`（统一任务进度，19 处触发） | `dialog_task.xml` + `TaskListPane` | 自研 `TaskDialog` = `SuperDialog(onDismissRequest=null)` + `LazyColumn` + `LinearProgressIndicator` | `miuix` | Compose 重写（自研，**最先做**） | interaction-map G5 明确建议优先实现；速度事件总线改 `DisposableEffect` 订阅 |
| `FCLProgressDialog`（原生 ProgressDialog 7 处） | 见 1.1 | `SuperDialog` + `CircularProgressIndicator` | `miuix` | Compose 重写（自研） | — |
| `dialog_*` 42 个布局 | 见 ui-inventory §1.2 | 全部基于上述基座重建 | `miuix` | Compose 重写 | 其中 3 个死文件直接删除（`dialog_relogin_classic`、`dialog_modpack_selection`、`dialog_world_name`） |

### 1.4 框架级（导航/容器/动画/主题）

| 原机制 | 代表位置 | Miuix/Compose 目标 | 所在 maven 模块 | 迁移方式 | 备注 |
|---|---|---|---|---|---|
| `UIManager`（8 个 `ui_*` 一级界面切换） | `ui/UIManager.kt` | `NavigationRail`（左侧 7 项导航）+ 内容区；导航实现二选一：`miuix-navigation3-ui` 的 `NavDisplay` 或自研状态驱动切换 | `miuix` / `miuix-navigation3-ui` | Compose 重写 | 双层导航是最大改造工程（interaction-map G1） |
| `PageManager` + TempPage 栈 | `ui/PageManager.java` | NavDisplay backStack / 自研栈；「切回恢复 temp page」语义必须复刻 | `miuix-navigation3-ui` | Compose 重写 | 返回键改 `BackHandler`（G2） |
| 主界面左右菜单 + 启动栏（`activity_main.xml`） | `MainActivity.kt` | `Scaffold` + 自研侧栏 | `miuix` | Compose 重写 | 入场动画组改 staggered `Animatable` |
| UI/页面切换动画（`ui_show/ui_hide/page_show/page_hide`） | FCLLibrary DisplayAnimUtils | `AnimatedContent` transition / NavDisplay 转场 | —（非 Miuix） | Compose 重写 | 时长 = `animationSpeed × 100`ms 令牌保留 |
| `AnimUtil`（13 个 ObjectAnimator 工厂） | `com/mio/util/AnimUtil.kt` | `animate*AsState` / `Animatable` / `keyframes` | —（非 Miuix） | Compose 重写 | 抖动动画、菜单旋转缩放逐一复刻 |
| `anim_scale` StateListAnimator（按压缩放） | 下载页 item | `pressFeedback`（utils/PressFeedback.kt）/ Card `pressFeedbackType` | `miuix` | Compose 重写 | — |
| 主题 `Theme.FoldCraftLauncher` + ThemeEngine | design-tokens 全文 | `MiuixTheme(colors)` 自定义 ColorScheme（`#7797CF` 主色）+ 深色补全 | `miuix` | Compose 重写 | 暗色空洞（ui_bg_color 等无 dark 变体）迁移时补齐 |
| 背景系统（昼夜双图 + 视频背景 + Palette 取色） | `MainActivity.kt:582-658` | `Box` 底层 + `Image`/`AndroidView(VideoView)` + 状态切换 | — | Compose 重写（视频背景保留原生 View） | 用户核心自定义能力，功能必须等价 |
| 新手引导浮层 `GuideUtil` | `MainActivity.kt:268-274` | 无对等组件 | — | 自研或舍弃 | 建议首版舍弃，记入无法映射清单 |
| SplashScreen API + Eula/Runtime Fragment | `SplashActivity.kt` | 保留系统 SplashScreen API；Fragment 容器改 Compose 页面 | `miuix` | Compose 重写 | 权限/EULA 流程逻辑不变 |

### 1.5 FCL 自研自定义 View（13 个类/接口）

| 自定义 View | 代表文件 | Miuix 目标组件 | 所在 maven 模块 | 迁移方式 | 备注 |
|---|---|---|---|---|---|
| `ControlButton`（775 行，手柄虚拟按钮） | `control/view/ControlButton.java` | — | — | **保留原生（红线）** | 手写全套手势识别+自绘，游戏内控制界面默认保留原生 |
| `ControlDirection`（946 行，虚拟摇杆/方向键） | `control/view/ControlDirection.java` | — | — | **保留原生（红线）** | 陀螺仪联动、8 向/摇杆双模式 |
| `TouchPad`（触控板） | `control/view/TouchPad.java` | — | — | **保留原生（红线）** | 指针捕获/物理鼠标 HOVER |
| `GameItemBar`（物品栏悬浮条） | `control/view/GameItemBar.kt` | — | — | **保留原生（红线）** | 自绘+触摸选取 |
| `MenuView`（游戏内悬浮球） | `control/view/MenuView.java` | — | — | **保留原生（红线）** | — |
| `LogWindow`（游戏内日志浮窗） | `control/view/LogWindow.java`（2 次/2 文件） | — | — | **保留原生（红线）** | ShellActivity 的日志窗是另一个普通 EditText 场景，可 Compose 化为 `Text` + `verticalScroll` |
| `KeycodeView`（键码按钮） | 109 次/2 文件（`view_keyboard.xml`、`dialog_select_keycode.xml`） | — | — | **保留原生（红线）** | 全键盘布局（1502 行 XML）整体保留；仅在游戏外被复用时才考虑 Compose 重写 |
| `TouchCharInput`（软键盘输入桥） | `control/keyboard/TouchCharInput.java` | — | — | **保留原生（红线）** | IME 逐字符桥接，AndroidView 承载 |
| `TouchControllerInputView`（触控模组代理） | `mio/touchcontroller/TouchControllerInputView.kt` | — | — | **保留原生（红线）** | 多点触控→协议转换 |
| `DraggableTextView`（FPS/内存悬浮字） | 2 次/1 文件（`view_game_menu.xml`） | — | — | **保留原生（红线）** | 远期可 `pointerInput`+`detectDragGestures` 重写，非本期目标 |
| `CursorView`（虚拟光标） | 2 次/2 文件 | — | — | **保留原生（红线）** | 坐标系 hack 与覆盖层耦合 |
| `FCLAppBarLayout` | 4 次/4 文件 | `TopAppBar` + `MiuixScrollBehavior` | `miuix` | Compose 重写 | 主题 tint 改 colorScheme |
| `ViewManager`/`CustomView`/`ViewListener` | `control/view/` | — | — | **保留原生（红线）** | 控件视图调度框架，随游戏内区域整体保留 |

---

## 2. 无法映射清单（Miuix 0.8.8 无对等组件，共 12 项）

| # | 项 | 处置 | 理由 |
|---|---|---|---|
| 1 | 游戏内控制全套：`ControlButton`/`ControlDirection`/`TouchPad`/`GameItemBar`/`MenuView`/`CursorView`/`DraggableTextView`/`TouchCharInput`/`TouchControllerInputView`/`LogWindow`/`KeycodeView` + `view_game_menu`/`menu_left`/`menu_right`/`view_keyboard` + DrawerLayout GameMenu | **保留原生**（红线） | 手写手势分发、自绘、输入桥（FCLInput/FCLBridge）、指针捕获、陀螺仪深度耦合；Compose 化收益低风险最高（interaction-map §11） |
| 2 | `TextureView` 游戏画面（JVMActivity） | 保留原生 + AndroidView | SurfaceTexture 生命周期时序不可变 |
| 3 | `SkinViewer`/SkinRenderer GL 3D 皮肤预览（`ui_main`、`item_account`、OfflineAccountSkinDialog） | 保留原生 + AndroidView | OpenGL 渲染器，无 Compose 对等 |
| 4 | `VideoView` 主界面动态背景 | 保留原生 + AndroidView | 生命周期续播/音量控制已稳定，重写无收益 |
| 5 | `WebView`（WebActivity） | 保留原生 + AndroidView | 系统组件，Compose 无对等 |
| 6 | `FileBrowserActivity`（FCLLibrary 自研文件选择器） | 保留原生 Activity | `ActivityResultLauncher` 桥接即可，SAF 双路径逻辑复杂 |
| 7 | `FCLDynamicIsland` 灵动岛 | **自研 Compose**（`Card`+动画） | Miuix 无通知岛组件 |
| 8 | `GuideUtil` 新手引导浮层 | 自研或**首版舍弃** | Miuix 无引导组件；建议舍弃并在 changelog 说明 |
| 9 | `FCLCheckBoxTreeAdapter` 三态勾选文件树（整合包导出） | **自研 Compose 树组件** | Miuix/Compose 均无树组件；indeterminate 半选传播为业务核心 |
| 10 | WindowSizeClass 大屏适配 | 自研断点（`BoxWithConstraints`） | 0.8.8 无任何 WindowSizeClass API（源码树 grep 核实） |
| 11 | `FullImageDialog` 缩放看图 | 自研（`SuperDialog` + pinch gesture） | Miuix 无图片查看器 |
| 12 | 列表项跑马灯文本（ProfileListAdapter 等） | foundation `basicMarquee` 或自研 | Miuix `Text` 无跑马灯参数 |

## 3. 需自研 Compose 组件清单（按优先级，共 10 项）

| # | 自研组件 | 封装基础（核实来源） | 服务的原组件 |
|---|---|---|---|
| 1 | `FCLDialog` 基座 + `showConfirm/showError/showWarning/showItemSelection` helper | **`SuperDialog`**（`extra/SuperDialog.kt`，`show: MutableState<Boolean>`、`title/summary`、`onDismissRequest`）；需平台 Dialog window 时用 `WindowDialog`（extra/WindowDialog.kt） | 38 个 FCLDialog 子类、FCLAlertDialog、DialogUtil |
| 2 | `FCLProgressDialog` | `SuperDialog` + `CircularProgressIndicator`（basic/ProgressIndicator.kt） | 原生 ProgressDialog 7 处 |
| 3 | `TaskDialog`（任务进度+列表+取消+速度） | `SuperDialog(onDismissRequest=null)` + `LazyColumn` + `LinearProgressIndicator` | TaskDialog 19 处触发点（G5，**最优先**） |
| 4 | `EditDialog` / `FullEditDialog` | `SuperDialog` + `TextField`（basic/TextField.kt） | EditDialog 10+ 处、FullEditDialog 2 处 |
| 5 | `FCLColorPickerDialog` | `SuperDialog` + `ColorPalette`（basic/ColorPalette.kt）/ `ColorPicker`（basic/ColorPicker.kt） | 主题色选择 3 处 |
| 6 | 左侧导航 `NavItem`（图标+旋转缩放选中动画） | `NavigationRail`/`NavigationRailItem`（basic/NavigationRail.kt）或 `IconButton` + `animateFloatAsState` | FCLMenuView 7 项 |
| 7 | `FCLDynamicIsland` | `Card`（basic/Card.kt）+ 动画 | 主界面灵动岛 |
| 8 | 三态勾选文件树 `CheckBoxTree` | `LazyColumn` + `Checkbox`（basic/Checkbox.kt）+ 自研缩进/半选状态机 | FCLCheckBoxTreeAdapter |
| 9 | 搜索栏 `FCLSearchBar`（输入框+搜索按钮+清空） | `SearchBar`/`InputField`（basic/SearchBar.kt）或 `TextField` + `IconButton` | 各下载页/Mod 页/版本页搜索框（IME_ACTION_SEARCH 交互保留） |
| 10 | `FullImageDialog` | `SuperDialog` + 缩放 gesture | 截图长按查看 |

> 注：`FCLSpinner` 不需要自研——`SuperSpinner`（对话框式）与 `WindowSpinner`（窗口下拉式）已覆盖全部形态，`item_spinner*` 7 个 XML 由 `SpinnerEntry` 数据驱动取代。设置页整体不需要 preference 库——`Card` + `BasicComponent`/`SuperSwitch`/`SuperArrow`/`SuperSpinner` 组合即 Miuix 官方设置页范式。

---

*统计：映射条目 85 条（§1.1 原生 26 + §1.2 FCLLibrary 组件 27 + §1.3 对话框 9 + §1.4 框架 10 + §1.5 自研 View 13）；无法映射 12 项；需自研 Compose 组件 10 项。所有 Miuix 组件名均核实自 `compose-miuix-ui/miuix` 仓库 tag `v0.8.8` 源码文件与 Maven Central 构件目录。*
