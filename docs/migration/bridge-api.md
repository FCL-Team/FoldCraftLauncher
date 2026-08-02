# 迁移期桥接层 API 文档（阶段二 · 小步骤 2.3）

> 生成时间：2026-08-01 ｜ 分支：`feature/miuix-migration`
> 输入：`docs/migration/interaction-map.md`（G1 双层导航 / G2 返回键 / G4 fakefx 绑定 / G10 静态单例反向调用）、`theme-mapping.md`（Miuix 0.9.3 组件结论）、`foundation-deps.md`（依赖基座）。
> 产出代码：`FCL/src/main/java/com/tungsten/fcl/ui/bridge/`（LegacyBridge.kt、FCLViewModel.kt、FakeFxStateFlow.kt）与 `ui/bridge/example/`（LauncherSettingsViewModel.kt、LauncherSettingsScreen.kt）。
> 定位：**迁移期临时设施**。全部页面迁移完成后，LegacyBridge 应被 Navigation 事件 + ViewModel 取代并整体删除；FCLViewModel/FakeFxStateFlow 中的 fakefx 承接随遗留数据层 fakefx 化改造逐步退场。

---

## 1. 桥接层解决什么问题

迁移期内新旧体系并存，interaction-map.md 的全局事实决定了桥接层的形态：

| 事实 | 桥接层对策 |
|---|---|
| G1：`UIManager.switchUI` + `PageManager` 页面栈/临时页栈，无 NavController | 不重建导航；Compose 侧经 `LegacyBridge` 安全调用既有单例 |
| G2：返回键被 `MainActivity.onKeyDown` 拦截走 `UIManager.onBackPressed()` 链 | Compose 页面/弹窗需要触发返回时调 `LegacyBridge.onBackPressed()`；嵌入 View 体系的 Compose 页面天然被该链覆盖（返回键在 Activity 层拦截，不进 Compose） |
| G4：fakefx 双向绑定渗透全部表单（VersionSettingPage 单页 17 组） | 数据层 fakefx Property 不动，边界处用 `toStateFlow`/`toMutableStateFlow` 转 Flow（§3） |
| G10：静态单例反向调用遍布 | Compose 侧禁止新增反向调用；遗留方向统一走 `LegacyBridge` 的两个通道 |
| G5/G7：TaskDialog 19 处复用、38 个 FCLDialog 子类 | 遗留 View 页面继续用 FCLDialog；前台是 Compose 页面时的弹窗走 `LegacyBridge.requestAlertDialog`（§2.3）。Miuix 版通用 TaskDialog 属阶段三组件工作，不在本步骤 |

两个支撑方向：
- **方向一（Compose → 遗留）**：Compose 页面内嵌在现有 Activity 体系（`createComposeView`），并能触发切 UI / 压临时页 / 关临时页 / 返回链。
- **方向二（遗留 → Compose）**：遗留 Java/Kotlin 代码在 Compose 页面前台时弹出 Miuix 弹窗（`requestAlertDialog` + `LegacyDialogHost`）。

---

## 2. LegacyBridge（`ui/bridge/LegacyBridge.kt`）

`object LegacyBridge`（Kotlin object，Java 侧经 `LegacyBridge.INSTANCE` 或 `@JvmStatic` 方法直调）。

### 2.1 通用存取

```kotlin
@JvmStatic fun uiManager(): UIManager?
```
安全获取 `UIManager.instance`（`@JvmStatic lateinit`，MainActivity 初始化完成前返回 null 而非抛异常）。

```kotlin
@JvmStatic fun currentActivity(): Activity?
```
经 `FCLApplication.getCurrentActivity()`（FCLauncher 模块的生命周期回调维护）取当前前台 Activity，可能为 null。供需要 Activity 的遗留 API 使用（`MainActivity.fileLauncher`、`UpdateChecker.checkManually(activity)` 等）。

### 2.2 方向一：Compose → 遗留导航

| 方法 | 语义 | 返回 |
|---|---|---|
| `switchUI(ui: FCLCommonUI): Boolean` | 切顶层 UI（等价左侧菜单点击）。目标实例取自 `uiManager()?.settingUI` 等 | UIManager 未就绪返回 false |
| `showTempPage(pageManager: PageManager, page: FCLTempPage)` | 压临时页（对应 `PageManager.showTempPage`） | — |
| `dismissCurrentTempPage(pageManager: PageManager): Boolean` | 关栈顶临时页 | 是否真有临时页被关（可作 BackHandler 是否消费的依据） |
| `onBackPressed(): Boolean` | 触发完整遗留返回链（当前 UI → PageManager 临时页栈 → MainActivity 默认返回事件），等价物理返回键 | UIManager 未就绪返回 false |

PageManager 实例取自已有的静态单例（interaction-map G10 的既有单例，不是新建）：`SettingPageManager.instance`、`ManagePageManager.instance`、`DownloadPageManager.instance`、`ControllerPageManager.instance`、`VersionPageManager.getInstance()`。

**内嵌 Compose 到现有 View 体系**：

```kotlin
@JvmStatic
fun createComposeView(context: Context, content: @Composable () -> Unit): ComposeView
```

自动做两件事：套 `FCLTheme`（Miuix 主题）；销毁策略 `DisposeOnViewTreeLifecycleDestroyed`（跟随宿主 ViewTree 生命周期释放组合，与 FCLCommonPage 的 View 生命周期对齐）。

```kotlin
// 用法：替换/嵌入某个遗留页面的内容区
val view = LegacyBridge.createComposeView(context) {
    LauncherSettingsScreen(onEvent = ::handleEvent)
    LegacyBridge.LegacyDialogHost()   // 每个 Compose 根安装一次
}
parent.addView(view)
```

宿主须是带 ViewTreeLifecycleOwner 的 Activity（FCLActivity 继承 AppCompatActivity，天然满足）。

### 2.3 方向二：遗留代码 → Compose 弹窗

```kotlin
class AlertDialogRequest(
    val title: String?, val message: String?,
    val positiveText: String?, val negativeText: String?,
    val onResult: Consumer<Boolean>?,   // true=确定，false=取消/关闭；主线程回调
)

@JvmStatic
fun requestAlertDialog(
    title: String?, message: String?,
    positiveText: String?, negativeText: String?,
    onResult: Consumer<Boolean>?,
): Boolean
```

- **单槽位**：已有未处理请求时返回 false，调用方应回退到遗留 `FCLAlertDialog`（避免静默覆盖）。对齐 FCLAlertDialog 同一时刻只弹一个的实际用法；确需排队时再扩展。
- 可任意线程调用（StateFlow `compareAndSet`）。
- **宿主未安装请求不会显示**：请求一直挂在槽位里直到某个 `LegacyDialogHost` 消费。纯 View 页面里的弹窗继续直接用 FCLAlertDialog；本通道只服务"弹窗触发时前台是 Compose 页面"。

Java 调用示例：

```java
boolean accepted = LegacyBridge.requestAlertDialog(
        "标题", "内容", "确定", "取消",
        result -> { if (result) { /* 确定 */ } });
if (!accepted) {
    // 回退：new FCLAlertDialog.Builder(context)...show();
}
```

Compose 宿主：

```kotlin
@Composable fun LegacyBridge.LegacyDialogHost()
```

订阅 `alertDialogRequest` 与 `taskDialogRequest`（3.2 新增）并以 **Miuix `window/WindowDialog`** 渲染（title/summary + 正/负 TextButton）。点遮罩/返回键关闭 = 取消（回调 false）。

### 2.3.1 任务进度弹窗通道（小步骤 3.2 新增）

```kotlin
class TaskDialogRequest(
    val title: String?,
    val executor: TaskExecutor,
    val cancelAction: Runnable?,   // null = 取消按钮置灰；executor.cancel()+关闭由宿主自动完成
    val autoClose: Boolean,        // 任务结束（onStop）自动关闭
)

@JvmStatic @JvmOverloads
fun requestTaskDialog(
    title: String?, executor: TaskExecutor,
    cancelAction: Runnable?, autoClose: Boolean = true,
): Boolean
```

- 单槽位语义与 `requestAlertDialog` 一致（占用返回 false 时调用方回退遗留 TaskDialog 或 `ui/compose/MiuixTaskDialog`）。
- 由 `LegacyDialogHost` 消费后以 `FCLDialog` + `FCLTaskDialogContent` 渲染：阶段行（pending/active/done/failed 图标 + n/total 计数）、任务进度行（LinearProgressIndicator，进度 <0 显示不确定态）、下载速度（FetchTask.speedEvent 弱订阅，dispose 时反注册）、取消按钮。
- 取消语义对齐遗留 TaskDialog：点击 = `executor.cancel()` + `cancelAction.run()` + 关闭。
- **View 页面的任务弹窗不走本通道**：`ui/compose/MiuixTaskDialog`（AppCompatDialog + ComposeView，自带平台 window）提供与遗留 TaskDialog 相同的命令式 API（setTitle/setExecutor/show），无需 Compose 宿主；3.2 已接入 3 个调用点（Versions.downloadModpack、Versions.updateGameAssets、WorldExportDialog 导出），开关 `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG`。
- 通用命令式弹窗：`ui/compose/FCLComposeDialog`（基座）+ `FCLDialogs.showAlert/showProgress`（覆盖 FCLAlertDialog 与 FCLLibrary ProgressDialog 用法）；Compose 页面内则用 `ui/compose/FCLDialog.kt` 的 `FCLDialog`/`FCLDialogCard` 组件。

选 WindowDialog 而非 overlay/OverlayDialog 的原因（0.9.3 源码核实）：OverlayDialog 经 `LocalRootDialogStates`/`LocalDialogStates` 渲染，**必须有 Miuix Scaffold 祖先**；WindowDialog 走平台 Dialog window，无 Scaffold 依赖，独立 window 形态与遗留 FCLDialog 语义一致。

### 2.4 明确不做的（防过度设计）

- 不封装 Compose Navigation / 自建页面栈：迁移期导航事实标准仍是 UIManager/PageManager。
- 不做弹窗队列、不做 TaskDialog 的 Miuix 版（阶段三组件工作，interaction-map G5 建议最先做）。
- 不提供 ThemeEngine 对接（theme-mapping.md §4 遗留项 4，阶段三处理）。

---

## 3. ViewModel 基类与 fakefx 承接（`ui/bridge/FCLViewModel.kt`、`FakeFxStateFlow.kt`）

### 3.1 范式

```kotlin
abstract class FCLViewModel<S : Any, E : Any>(initialState: S) : ViewModel() {
    val uiState: StateFlow<S>            // 页面唯一渲染数据源（不可变 data class）
    val events: SharedFlow<E>            // 一次性事件（导航/Toast/文件选择），buffer=16

    protected fun updateState(reducer: S.() -> S)          // updateState { copy(x = v) }
    protected fun sendEvent(event: E)                      // tryEmit，满则丢弃+警告
    protected fun <T> Flow<T>.observeIntoState(reducer: S.(T) -> S)  // Flow 投影进 UiState
    protected fun <T> ObservableValue<T>.asStateFlow(started): StateFlow<T>   // fakefx 单向
    protected fun <T> Property<T>.asMutableStateFlow(): MutableStateFlow<T>   // fakefx 双向
}
```

Compose 侧：`val state by vm.uiState.collectAsStateWithLifecycle()` + `LaunchedEffect(Unit) { vm.events.collect { ... } }`（`vm` 来自 `androidx.lifecycle.viewmodel.compose.viewModel()`，lifecycle-viewmodel-compose 2.10.0）。

规则：Composable 只读 `uiState`、只调 ViewModel 的语义化方法；业务规则（校验/联动/重置）一律收在 ViewModel 方法里——这是对旧代码"listener 散落在页面 create() 里"的收敛。

### 3.2 fakefx → Flow 适配器（顶层函数，可脱离基类单用）

```kotlin
fun <T> ObservableValue<T>.toStateFlow(scope, started = WhileSubscribed(5000)): StateFlow<T>
fun <T> Property<T>.toMutableStateFlow(scope): MutableStateFlow<T>
```

- `toStateFlow`：单向。callbackFlow 注册 `ChangeListener`，Flow 关闭自动 `removeListener`；`distinctUntilChanged` 去重。等价旧代码 View 属性的单向 `bind(observable)`。
- `toMutableStateFlow`：双向，等价 `bindBidirectional`。写 `flow.value` 回写 Property；Property 被遗留代码改动流入 Flow。回环防护靠 StateFlow 同值合并 + 写前 `value != newValue` 判断；scope 取消（ViewModel.onCleared）自动解注册。
- **注意数值类型的类型实参**：fakefx 沿袭 JavaFX 惯例，`IntegerProperty`/`LongProperty`/`DoubleProperty` 实现的是 `Property<Number>` 而非 `Property<Int>`——投影进 UiState 时用 `it.toInt()`/`toDouble()` 收窄，写入时 Int 字面量可直接赋（Int 是 Number 子类）。示例见 LauncherSettingsViewModel 的 downloadThreads 投影。
- Config 持久化零成本：`ConfigHolder` 监听 Config 全局变更自动落盘 config.json（ConfigHolder.java:64），写 fakefx Property 即持久化，**无需手动 save**。

### 3.3 旧绑定写法 → 新写法对照表（承接 G4，含 VersionSettingPage 17 组绑定的迁移模式）

| 遗留写法（FXUtils/属性绑定） | Compose/Miuix 写法 |
|---|---|
| `FXUtils.bindString(editText, stringProperty)` | VM 内 `stringProperty.asMutableStateFlow()` → `TextField(value = state.x, onValueChange = vm::setX)` |
| `FXUtils.bindBoolean(fclSwitch, booleanProperty)` | `SwitchPreference(checked = state.x, onCheckedChange = vm::setX)` |
| `seekBar.progressProperty().bindBidirectional(intProperty)` | `SliderPreference(value = state.x.toFloat(), onValueChange = { vm.setX(it.toInt()) })` |
| `FXUtils.bindSelection(spinner, property)` | `WindowSpinnerPreference`/`OverlaySpinnerPreference`（selected + onSelectedChange 回写 VM） |
| `view.visibilityProperty().bind(checkProperty[.not()])` | `if (state.x) { ... }` 或 `AnimatedVisibility(visible = state.x)` |
| `bar.percentProgressProperty().bind(task.progressProperty())` | VM 内 `task.progressProperty().asStateFlow()` 投影 → `LinearProgressIndicator(progress = { state.p })`（Miuix 进度组件） |
| Adapter 属性监听 → `notifyDataSetChanged()` 全量刷 | UiState 里的 `List<T>` 不可变替换 → LazyColumn 自动重组（key 定位） |
| 静态单例反向刷新页面（G10） | 数据源变更 → fakefx 属性/事件 → VM 投影 UiState，页面自动重组 |

### 3.4 设置页示例（`ui/bridge/example/`）

以阶段三首个目标"设置页"为示例，截取 LauncherSettingPage 的"下载"区域（LauncherSettingPage.java:199-209）：

- `LauncherSettingsViewModel`：零参构造（`viewModel()` 默认工厂直建）。两个 Config fakefx 属性（`autoDownloadThreadsProperty`/`downloadThreadsProperty`）经 `asMutableStateFlow()` 双向桥接，`observeIntoState` 投影进 `LauncherSettingsUiState`；"勾选自动时强制重置线程数为 `FetchTask.DEFAULT_CONCURRENCY`"的联动收在 `setAutoDownloadThreads()` 里。
- `LauncherSettingsScreen`：SwitchPreference + SliderPreference（范围 1f..128f，对齐原 SeekBar `android:min=1/max=128`）+ ArrowPreference（点击发一次性事件 `PickBackgroundImage`）+ GlideImage 用法示例。

需要构造参数的 ViewModel（如需 Context 读 SharedPreferences 的设置项）：

```kotlin
val vm: MyViewModel = viewModel(initializer = {
    MyViewModel(context.applicationContext as Application)
})
```

---

## 4. 图片加载决策：Glide Compose 1.0.0-alpha.6

### 4.1 核实过程

- 项目已有 `com.github.bumptech.glide:glide:4.16.0`（View 侧 40+ 调用点：Mod/截图/皮肤/头像/图标）。
- Glide 官方 Compose 集成为独立构件 `com.github.bumptech.glide:compose`，Maven Central 可查：最新 `1.0.0-beta10`（2026-07-11），全部版本仍处 alpha/beta（无 stable）。
- **版本与 Glide 核心的绑定关系**（逐版 POM 核实）：
  - `1.0.0-alpha.6` → compile 依赖 `glide:4.16.0`（另带 `ktx:1.0.0-alpha.5`、`recyclerview-integration:4.16.0`、compose ui/foundation 1.5.0）；
  - `1.0.0-beta01` ~ `beta10` → 依赖 `glide:5.0.0-rc01` ~ `5.0.9`。
- alpha.6 AAR：`minSdk=21`（项目 26，满足）；API 含 `GlideImage`（`loading`/`failure: Placeholder?`）、`placeholder(@DrawableRes)`、`GlideSubcomposition`、`ExperimentalGlideComposeApi` 注解（compose-alpha6 tag 源码核实）。

### 4.2 决策

**采用 `com.github.bumptech.glide:compose:1.0.0-alpha.6`**，理由：

1. **与现有 Glide 4.16.0 精确对齐**：alpha.6 是针对 4.16.0 API 编译的，零行为差异；beta 线会把全应用 Glide 抬升到 5.0.x 大版本——跨大版本升级影响 View 侧全部既有加载逻辑（触碰"不改现有页面逻辑"红线），应作为独立任务评估而非夹带。
2. 功能面足够：Compose 侧图片场景（远程 Mod 图标、截图列表、皮肤/头像）只需 `GlideImage` + 占位符，alpha.6 已全部具备。
3. 兼容性：Compose 运行时向后兼容（1.5 编译的构件可在 1.11.4 运行时工作）；Kotlin 2.3.20 可读旧版元数据；checkAarMetadata 已在 2.1 全局禁用，无新阻塞。
4. "alpha"成熟度风险可控：构件职责窄（加载图片进 Compose），且一旦暴露问题，回退方案零成本（§4.4）。

依赖变更（在 2.1 基础上追加）：
- `gradle/libs.versions.toml`：`glideCompose = "1.0.0-alpha.6"` + `glide-compose` 坐标（附选型注释）；
- `FCL/build.gradle.kts`：`implementation(libs.glide.compose)`。

### 4.3 用法

```kotlin
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModIcon(url: String?) {
    GlideImage(
        model = url,                       // String/Uri/File/@DrawableRes Int 均可（与 Glide 一致）
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        loading = placeholder(R.drawable.ic_baseline_application_24),
        failure = placeholder(R.drawable.ic_baseline_close),
    )
}
```

迁移对照：`Glide.with(imageView).load(x).into(imageView)` → `GlideImage(model = x)`；`.placeholder()/.error()` → `loading/failure = placeholder(resId)`；跑马灯/入场动画等 ItemView 行为随 LazyColumn 重写（interaction-map §5.4）。

### 4.4 回退方案（若 alpha.6 运行时暴露阻塞问题）

不新增库，用 `AndroidView` 包 `ImageView` + 既有 Glide 4.16 加载（十几行封装）；或届时评估 Glide 5.x 整体升级。决策记录在本节，届时直接改本节结论即可。

---

## 5. 后续页面迁移 Agent 使用指引

迁移任何一个页面时按以下顺序落地：

1. **建 ViewModel**：继承 `FCLViewModel<UiState, Event>`；把 interaction-map.md 中该页登记的 fakefx 绑定逐组按 §3.3 对照表转成 `asStateFlow/asMutableStateFlow` + `observeIntoState` 投影；业务规则（校验、联动、防重入标志）搬进 VM 方法。
2. **建 Screen**：Miuix 0.9.3 组件（设置行用 miuix-preference 的 *Preference 家族；弹窗默认 WindowDialog，页面已在 Miuix Scaffold 内时可用 OverlayDialog）；只读 `uiState`，事件回调调 VM 方法。
3. **接宿主**：
   - 整页替换（嵌入现有 UI 容器）：`LegacyBridge.createComposeView(context) { MyScreen(...) ; LegacyBridge.LegacyDialogHost() }`，addView 到原页面容器；
   - 独立 Activity：`setContent { FCLTheme { MyScreen(...) ; LegacyBridge.LegacyDialogHost() } }`（参考 activity/ThemeTestActivity.kt 的 setContent 模式）。
4. **导航/返回**：切 UI 用 `LegacyBridge.switchUI(uiManager()?.xxxUI)`；临时页用 `LegacyBridge.showTempPage/dismissCurrentTempPage`（pageManager 取既有静态单例）；Compose 内 `BackHandler { LegacyBridge.dismissCurrentTempPage(pm) || LegacyBridge.onBackPressed() }`。物理返回键已被 MainActivity 拦截走遗留链，嵌入 View 体系的 Compose 页面无需自建 BackHandler。
5. **遗留触发弹窗**：遗留代码在 Compose 前台时需要弹窗 → `LegacyBridge.requestAlertDialog(...)`，返回 false 时回退 FCLAlertDialog；**禁止**在 Composable 里直接 new FCLDialog（Context 窗口层级与 Compose 弹窗不一致）。
6. **图片**：一律 `GlideImage`（§4.3），不要再 `AndroidView` 包 ImageView（除非命中 §4.4 回退条件）。
7. **一次性副作用**（文件选择/权限/Toast）：VM 发 event → Screen 的 onEvent 回调 → 宿主转 `MainActivity.fileLauncher` / `rememberLauncherForActivityResult` / `android.widget.Toast`（Toast 迁移期继续可用，91 处既有用法不强行替换）。

## 6. 遗留问题（后续步骤处理）

1. ~~**Miuix 版通用 TaskDialog 未做**~~（3.2 已完成）：`ui/compose/FCLTaskDialog.kt`（状态+渲染）+ `MiuixTaskDialog`（命令式封装）+ `requestTaskDialog` 通道（§2.3.1）。已接入 3 处调用点（开关 `MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG`）；3.4 又替换 4 处（DownloadPage、RemoteModVersionPage、ui/manage InstallerListPage×2、ModpackFileSelectionPage）。其余 12 处触发点（interaction-map G5：LauncherHelper、ModpackInstaller、ModpackSelectionPage×2、VersionInstallInfoPage、ModListPage、ModUpdatesPage×2、ControllerRepoPage、ControllerDownloadPage、UpdateDialog）待批量替换——注意 LauncherHelper 使用了 `titleProperty()` fakefx 绑定与 setCancel 动态切换，UpdateDialog 嵌套在更新对话框内，需逐点评估。
2. **弹窗单槽位**：`requestAlertDialog` 不排队；若阶段三出现并发弹窗诉求（如自动更新检查 + 任务失败同时触发），扩展为 Channel/列表。
3. **glide-compose alpha 成熟度未真机验证**：本步骤只验证了依赖解析与编译（示例含 GlideImage 调用）；首次真机运行含图片的迁移页面时留意 §4.4 回退条件。
4. **ViewModel 与 PageManager 生命周期未打通**：`createComposeView` 的组合跟随 ViewTree 生命周期，但 `viewModel()` 的 ViewModelStore 归宿主 Activity——页面（FCLCommonPage）级销毁不会清 ViewModel。迁移期页面少、状态轻，可接受；阶段三如出现"版本切换后状态残留"，在页面 onStop/onDestroy 里手动清或换自定义 ViewModelStoreOwner。
5. **ThemeEngine/壁纸背景未接入**（theme-mapping.md §4）：Compose 页面的主题色当前取 FCLTheme 默认值，取色器/壁纸联动画在阶段三设置页迁移时处理。
