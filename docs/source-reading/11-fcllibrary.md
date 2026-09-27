# fcllibrary 组件库

范围：`FCL/src/main/java/com/tungsten/fcllibrary/`，46 个文件、约 7364 行，Java/Kotlin 混合（Kotlin 多为新近重构文件）。子包：anim（1）、browser（含 adapter/options 共 8）、component（根 6 + dialog 6 + theme 3 + ui 4 + view 21 + view/color 2）、crash（4）、ui（1）、util（3）。

原 `FCLCommonPage`/`FCLPageManager`/`FCLTempPage`/`FCLUILifecycleCallbacks` 四个类**已不存在于整个仓库**（grep 全源码无命中）。页面机制被重构进了 `FCLMultiPageUI` + `FCLPage`，代码注释中明确写有"替代原 PageManager 机制"。

## 一、基类体系（Activity / UI / Page 三层）

**Activity 层 `FCLActivity`**（component/FCLActivity.java，121 行）：继承 AppCompatActivity，是全部页面的宿主。onCreate 首行 `ThemeEngine.setupThemeEngine(this)` 初始化主题 → `applyFullscreen`（刘海屏 SHORT_EDGES + 沉浸式）→ `applySavedNightMode`（SharedPreferences("launcher") 的 themeMode 0 跟随系统/1 亮/2 暗）→ `DisplayUtil.updateWindowSize`（com.mio 工具）→ 有存储权限时 `FCLPath.loadPaths`。预置三个机制：`fileLauncher`（FileBrowserLauncher 实例，每个 Activity 自带一个）；`requestPermissions`/`startActivityForResult`（Activity Result API 封装，**回调字段一次性**，用完置 null）；`attachBaseContext` 走 `LocaleUtils.setLanguage`。onConfigurationChanged 里注释点明关键约定：configChanges 含 uiMode，亮暗切换**不重建 Activity**，需手动 `ThemeEngine.refreshTheme()`。

**UI 层**（component/ui）：`FCLBaseUI`（抽象，持有 context/activity/contentView，静态 `defaultBackEvent` 返回键兜底，抽象 `isShowing`/`refresh`）→ `FCLCommonUI`（构造时 setContentView(id)，refresh 返回 `Task<?>`，沿用 fclcore 的任务系统）→ `FCLMultiPageUI`（318 行，最核心）。后者用**内层 ViewPager2 + 覆盖层 FrameLayout** 双容器：ViewPager2 承载普通页（`setUserInputEnabled(false)` 禁滑动、`disableMouseWheelScroll` 禁滚轮翻页，仅 tab/showPage 切换），overlay 承载临时页导航栈（`showTempPage` 压栈时隐藏下层、`dismissCurrentTempPage` 弹栈恢复，返回键优先弹临时页）。内部 `PageAdapter` 是 RecyclerView.Adapter，页面**随回收销毁不保留状态**（onViewRecycled 置空注册表，下次全新创建），并在 onBind 里防御 GapWorker 预取导致的"child already has a parent"（先解除旧 parent）。onPageSelected 做三件事：清空临时页、同步 tab 高亮、对新页做淡入+上滑 250ms 过渡（用 `lastSelectedPosition` 过滤软键盘等布局变化引起的重复 dispatch，防闪烁）。

**Page 层 `FCLPage`**：轻量基类，只持 contentView，**构造器即完成 setContentView + onCreate 全部初始化**（无任何生命周期方法，注释明言"页面无生命周期"）。唯一常量 `PAGE_ID_TEMP = -10000`。**页 id 常量机制不在本库**，而在 com.tungsten.fcl 各 UI 类：如 `DownloadUI.PAGE_ID_DOWNLOAD_GAME = 15010`、`ControllerUI.PAGE_ID_CONTROLLER_MANAGER = 15040` 等，按"150XX"段位编码，页面以 `(context, PAGE_ID_XXX)` 构造。

另：`FCLUILayout` 只是 R.id.container 容器的 RelativeLayout 空壳（无逻辑，仅占位类型）；`FCLFragment` 仅一个 findViewById 辅助；`FCLAdapter` 是 BaseAdapter 空基类；`ResultListener` 是静态单监听的旧式 onActivityResult 回调（已被标记 deprecated 路线）。

## 二、主题引擎真身

**真身就在本库：`component/theme/ThemeEngine.kt`（Kotlin object）**，全仓库唯一，com.tungsten.fcl 与 com.mio 下无同名类。机制三件套：
- `ThemeEngine`：持 `MutableStateFlow<ThemeData?>`；`registerEvent(view, runnable)` 是核心——**WeakHashMap 弱键**存刷新回调，注册后**同步执行一次**（注释解释：异步 post 会让新 inflate 控件首帧露白）；`refreshTheme()` 全量刷新**合并为单任务**（`handler.removeCallbacks+post`），注释点出坑：拖动取色每帧多次 applyColor，不合并则高刷屏上每秒数万条刷新任务淹死主线程导致 ANR。
- `ThemeData`：不可变 data class，存亮/暗主色 color/colorDark、次色 color2/color2Dark、fullscreen、closeSkinModel、animationSpeed、colorAlpha（派生色透明度）、亮暗背景 BitmapDrawable。**亮暗切换不改数据**，`getColor()`/`getColor2()` 运行时经 `FCLApp.getActivity()` + `isNightMode` 动态取；ltColor/dkColor 用 HSV 提亮/压暗 30% 派生，autoTint 按亮度取黑/白。getter 用 `@JvmName("_getColor")` 等解决 Java 互操作命名冲突。
- `ThemePreference`：DataStore（theme.json，kotlinx.serialization），**含旧 SharedPreferences("theme") 一次性迁移逻辑**；saveTheme 异步写不阻塞 UI 线程。

## 三、组件清单要点

**dialog**：`FCLDialog`（AppCompatDialog 基类，应用沉浸全屏并在 onWindowFocusChanged 恢复——注释说明对话框窗口失焦后系统会清沉浸标志，Activity 侧恢复不会触发）；`FCLAlertDialog`（Builder + AlertLevel{ALERT,INFO} + positive/negative/neutral/**extra** 四按钮 + Linkify 自动链接 + checkHeight 高度自适应）；`EditDialog`/`FullEditDialog`（输入弹窗，ViewBinding）；`FullImageDialog`；`FCLColorPickerDialog`（取色器 + Hex 输入双向同步）。

**view**（所有控件统一模式：构造时 `ThemeEngine.registerEvent(this, this::refreshTheme)`，多数额外暴露 fakefx 风格 `visibilityProperty`/`disableProperty`/`checkProperty` 等属性绑定，用 `fromUserOrSystem` 标志防回环）：FCLButton（自绘 GradientDrawable 普通/按压态 + 可选 ripple + scale 动画 + compound drawable tint）、FCLImageButton/FCLImageView/FCLTextView（auto_tint/use_theme_color 系列 styleable）、FCLCheckBox（含 indeterminate 三态）、FCLRadioButton/FCLSwitch/FCLSeekBar/FCLProgressBar（percent/first/second progress 属性）、FCLPreciseSeekBar（±按钮组合布局，post 手动定位，覆写 requestLayout 强制二次测量）、**FCLNumberSeekBar**（Kotlin，轨道在数值文本两侧断开自绘、点数值弹 EditDialog 精确输入、透明胶囊 thumb 定位）、**FCLSpinner**（Kotlin，AppCompatTextView + ListPopupWindow 重写，注释：程序性 setSelection 永不触发监听、箭头 RotateDrawable level 旋转动画）、FCLEditText（Kotlin，含**焦点恢复守卫**：ViewPager2 每次布局会清页面焦点，被误清特征是全窗口无焦点持有者，延迟 150ms 检测并 requestFocus+拉起键盘）、FCLTabLayout（自绘左右滑动指示箭头）、FCLDynamicIsland（自绘胶囊 + DynamicIslandAnim 灵动岛动画）、FCLLinearLayout/FCLConstraintLayout/FCLView/FCLCheckedTextView/FCLMenuView（侧栏菜单单选钮）。**FCLCheckBoxTreeAdapter/Item**：嵌套 ListView 树，双向绑定 + 复用前 unbindItem 防监听器累积（注释自述防 OOM），嵌套高度后台线程计算。

**color**：ColorPickerView（950 行，经典 HSV 面板 + 可选 alpha 滑条，sat/val 面板 BitmapCache 按 hue 缓存，onMeasure 复杂自适应，支持状态保存）；AlphaPatternDrawable（透明度棋盘格）。

## 四、browser 文件浏览器

`FileBrowser`（Serializable 配置 + Builder：externalSelection、LibMode{FILE_BROWSER/FILE_CHOOSER/FOLDER_CHOOSER}、SelectionMode{SINGLE/MULTIPLE}、initDir、suffix 过滤、title、callback）；旧路径 `browse(activity,code,listener)` 走 ResultListener 已 @Deprecated，新路径 `FileBrowserLauncher`（每个 FCLActivity 自带实例，Activity Result API）。`FileBrowserActivity` 是完整浏览器页：目录列表 + 路径栏（点按 EditDialog 跳转）+ 公共/私有目录快捷键 + 系统选择器兜底（ACTION_GET_CONTENT 多选，结果转 Uri）+ 确认回传路径 Uri 列表；code 100/150/200/500/600/700/750 硬编码禁用外部选择。`FileOperator` 用 commons-io 双重排序（目录优先 + 名字不敏感）+ 后缀过滤。`FileBrowserAdapter` 行项：图标按主题 tint、选中灰底、长按 FileProvider 分享。`SelectedFile`（Kotlin）统一本地路径与 content:// 结果，`toFile` 会把 content 拷到 cacheDir。

## 五、crash 崩溃处理

这是 **CustomActivityOnCrash 库的改包版**。`CrashReporterInitProvider`（ContentProvider，manifest 注册为 `${applicationId}.crashreporterinitprovider`）在应用初始化最早时机 `CrashReporter.install`：替换默认 UncaughtExceptionHandler + 注册 ActivityLifecycleCallbacks（记录最近 Activity 弱引用、前台/后台计数、可选 50 条操作日志）。捕获后：3 秒内重复崩溃防循环；读 `/proc/self/cmdline` 判进程名，**栈含 handleBindApplication 或已在 `:error_activity` 进程则不启动自定义页**；堆栈截 128KB 防 TransactionTooLargeException；以 `FLAG_ACTIVITY_NEW_TASK|CLEAR_TASK` 启动错误页。**AndroidManifest 确认 CrashReportActivity 声明在独立进程 `android:process=":crash"`**（崩溃页与主进程隔离，logErrorOnRestart 会重新打日志便于 logcat 查看）。`CrashReportActivity` 继承 FCLActivity，提供重启/关闭/**上传日志**（uploadLog，国内走 logshare.cn、国外 mclo.gs）/**分享**（临时文件 + FileProvider）四按钮，背景随主题亮暗刷新，allowScreenshots 未开时 FLAG_SECURE。`CrashReporterConfig` 完整 Builder（backgroundMode 三态、restartActivity 猜测 intent-filter RESTART action 或 launcher）。

## 六、anim / ui / util

`DynamicIslandAnim`：8 个缩放 ObjectAnimator + 2 秒后淡出，mark 计数 + 线程中断防旧动画串扰，速度取自 theme.animationSpeed。`ui/ProgressDialog`：极简不可取消 FCLDialog 内嵌 FCLProgressBar（透明背景浮于全屏模糊上）。`util`：ConvertUtils（dp/px、角度弧度、Base64 位图）；LocaleUtils（11 种语言枚举、createConfigurationContext 换语言、IS_CHINA_MAINLAND 按时区+国家判断、world_time 格式化）；LogSharingUtils（日志上传/分享，**自动附 hs_err JVM 崩溃报告并脱敏 --accessToken**，8MB 上限）。

## 七、值得注意的细节与坑

1. **fakefx 属性系统渗透全库**：控件属性（visibility/disable/check/progress/string/image）全部是 fclcore 的 fakefx Property，invalidated 统一 post 到 androidUIThread——是 JavaFX 版 HMCL 迁移到 Android 的过渡痕迹；新 Kotlin 代码（FCLSpinner、FCLNumberSeekBar）已部分放弃该风格。
2. 大量注释记录了真实踩坑：临时页动画 withEndAction 取消不回调致 View 树残留泄漏、WeakHashMap 防页面泄漏、合并刷新防 ANR、ViewPager2 清焦点致键盘滞留、GapWorker 预取 addView 崩溃、对话框窗口沉浸标志丢失。
3. `FCLActivity.startActivityForResult` 回调字段一次性且不排队，并发发起两次会丢第一个回调；`ResultListener` 静态监听同样只能单次。
4. ThemeData.getColor 依赖 `FCLApp.getActivity()`，Activity 未注册时回退亮色值。
5. FileBrowserActivity 的 FOLDER_CHOOSER 逻辑选中目录即整体替换 selectedFiles，且 confirm 时路径字符串直接 `Uri.parse`，与外部选择器的真实 content uri 混在同一通道，依赖 SelectedFile 的双态兼容。
