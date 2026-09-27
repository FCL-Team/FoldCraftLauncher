# com.mio 包（新代码层）

范围：`FCL/src/main/java/com/mio/` 全部 91 个文件、约 13600 行，Kotlin 为主。子包：cache、controlconverter、data（favorite）、datastore、dialog、download、flite、manager、minecraft、plugin、promo、skin、touchcontroller、ui（adapter/dialog/popup/view/widget）、util。

整体风格：Kotlin object 单例 + 懒初始化 + `StateFlow` 状态流 + DataStore/Room 持久化；对外给 Java 侧用 `@JvmStatic`/`@JvmField` 暴露静态入口。被 `com.tungsten` 侧约 110 个文件引用，是事实上的"新基础设施层"。

## 1. datastore — DataStore 持久化体系（5 文件）

全部走 **Jetpack DataStore + kotlinx.serialization JSON**（非 Proto），统一模式：`val Context.xxxDataStore by dataStore(fileName, serializer)`，serializer 手写 `readFrom/writeTo`（IO 线程写、解析失败回落 defaultValue）。五个存储：

| 文件 | 数据 | 持久化文件 |
|---|---|---|
| `PluginSetting.kt` | `PluginPreference`：disabledPlugins（禁用插件包名）、rendererEnvPrefs（v2 渲染器插件环境变量，双层 Map<包名,<键,值>>）、MioLibPatcher 四个开关 | `plugin_settings.json` |
| `SkinAnimationSetting.kt` | 动画 id（注册表默认 idle）、solidLayerEnabled、upperBodySeparated | `skin_animation_settings.json` |
| `GameItemBarSetting.kt` | 物品栏滑选/双击换手 | `game_item_bar_settings.json` |
| `GuideSetting.kt` | 已展示引导 tag 列表 | `guide_settings.json` |
| `LaunchCountSetting.kt` | 总启动数、保底计数、pendingPopup（夸克推广） | `launch_count_settings.json` |

注意：主设置（Profile/版本设置）仍走老的 fcl setting 体系，DataStore 只管新增功能。

## 2. plugin — 插件体系（8 文件）

- **`PluginManager`**：统一扫描注册中心。`scan()` 用 `queryIntentActivities(GET_META_DATA)` 枚举非系统应用，按 meta-data 布尔 `fclPlugin`、`fclPlugin_V2`、`FCLNativePlugin` 识别插件（一个应用可多类型），FFmpeg 插件固定包名 `net.kdt.pojavlaunch.ffmpeg` 单独探测。产出 `PluginApp(packageName,label,version,icon,types,appInfo,lastUpdateTime)`。禁用集合放 `MutableStateFlow<Set<String>>` + DataStore 落盘；`ensureDisabledLoaded` 用 double-check + `runBlocking` 首次同步加载（未加载完视为启用）；`invalidate()` 处理进程感知不到的卸载/安装；`refreshAll` 联动刷 Renderer/Driver/NativeLib 三个插件表。
- **`AbstractPlugin<T>`**：模板基类，`items` 属性 getter 懒初始化（首次访问扫描 `enabledApps` 并逐个 `parse(app)`），`refresh()` 清表重扫。
- **`RendererPlugin`**（最复杂，318 行）：v1 解析 meta-data 字符串 `renderer/des/boatEnv/pojavEnv`（冒号分隔）；v2 读取插件 APK 内 `fclPlugin_V2` 指向的 string 资源（JSON），按 `RendererConfigV2`（sealed interface：Normal/Selectable/Customizable/Toggleable Env，与 Zalith RendererPlugin-v2 DSL 对齐，`ignoreUnknownKeys`）解析，标题经 meta-data 里的资源 id 做插件内本地化。v2 转成 v1 语义 `Renderer`：`**|` 前缀是"插件 nativeLibraryDir 下库文件"占位符（`LIB_MESA_NAME/MESA_LIBRARY/DLOPEN` 三键运行时拼目录，其余直接替换绝对路径）；用户配置存 DataStore，`updateEnvConfigs` 更新内存 + 异步落盘 + 重建 Renderer 并调 `RendererManager.replaceRenderer`。`buildPojavEnv` 把 Normal/Selectable/Customizable/Toggleable 按用户选择展开为 `KEY=VALUE` 列表，DLOPEN 单独聚合去重。
- **`DriverPlugin`**：驱动列表（内置 Turnip 兜底 + 插件 `driver` meta-data），`selected` 当前驱动。
- **`NativeLibPlugin`**：原生库插件，meta-data `environment`（空格分隔 `K=V`），支持 `{nativeLibraryDir}` 占位符且带**路径穿越防护**（normalize 后必须 startsWith 基目录）；`getJVMEnv()` 合并所有插件 JVM 环境。
- **`FFmpegPlugin.java`**：固定包名探测 + `Os.setenv("FFMPEG_PATH", ...)`。
- **`MioLibPatcherManager`**：java-agent 注入开关，`getJvmOptions()` 生成 `-Dmiolibpatcher.*`（Sable Rapier 默认开、关闭才写属性 false，其余默认关、开启才写 true）。与 PluginManager 同款 `ensureLoaded` + runBlocking 模式。
- **`_Bundle.kt`**：`Bundle.safeGetString` 扩展（容错 int/float 型 meta-data）。
- **`RendererConfigV2.kt`**：纯数据模型（@SerialName 对齐官方 JSON）。

## 3. manager / data / favorite

- **`RendererManager`**：六个内置渲染器常量（GL4ES、VirGL、VGPU、Zink、Freedreno、NGGL4ES/Krypton）+ 懒初始化时 `RendererPlugin.init` 并追加插件渲染器；`getRenderer(id)` 找不到回落 NGGL4ES；`replaceRenderer` 供 v2 配置变化原位替换。**`Renderer`**（data 包）：含 UUID 常量 id、min/max MC 版本、`pojavRendererId`（仅 v2）、`source`（来源插件名）、`displayMaxMCver`（展示与判断分离）。
- **`FavoriteManager`**：下载资源收藏（CurseForge/Modrinth）。**Room + 内存镜像**双轨：Room Flow 收集驱动 StateFlow，但 toggle 写库后立即同步内存（不等 Flow 回调，防快速连点读到过期态）。分组功能：`favorite_groups` 表，条目 `groups` 字段多对多引用；新建/重命名有 `Mutex` 防双击重复创建；删组只摘标记不取消收藏。`idOf = "$source:$modId"`。
- **favorite/**：Room v2 库 `download_favorites.db`，`fallbackToDestructiveMigration()`（注释明确"分组 v2 测试期破坏式重建，合并前需补迁移"——**这是坑点**）；`FavoriteConverters` 用 kotlinx JSON 存 `List<String>` 列。

## 4. download — 非阻塞下载队列

- **`DownloadManager`**：注册中心。`submit(title, task, executor, installAction?, cleanupAction?)` 把 fclcore `Task` 包成 `DownloadTaskInfo` 放进 `StateFlow<List>`；首个任务出现时启动前台服务 + 请求 Android 13 通知权限（每次运行至多一次）+ 引用计数 WakeLock/WifiLock（`util` 内实现）。任务链 `TaskListener.onStop`（切 UI 线程）移除条目；**待安装条目**（整合包）成功后置 `ready=true` 保留在面板，用户点条目执行 `installAction`、点 ✕ 执行 `cleanupAction`。聚合进度 = 有效任务进度平均（-1 表不确定）。注释记录了一个真实的坑：**不要在 onStop 里 removeTaskListener，AsyncTaskExecutor 正并发遍历非线程安全 listener 列表会 CME**。Toast 连续提交时只显示最新。
- **`DownloadService`**：纯保活壳（只 startForeground 承载通知与进程优先级），任务空时立即自停，START_NOT_STICKY。
- UI 侧配套：`ui/view/DownloadSlidePanel`（右菜单列内容，注册 `FetchTask.speedEvent` weak 监听显示全局速度）+ `DownloadListAdapter`（ListAdapter，bind/unbind 严格配对进度监听防回收视图泄漏；待安装态点击安装/丢弃）。

## 5. skin — 3D 皮肤渲染（9 文件，作者重写）

架构：`SkinViewer`（TextureView + EGL14 + HandlerThread 渲染线程）→ `SkinRenderer`（GLES2 渲染器）→ `GltfPlayerModel`（classic/slim 双实例）→ `GltfModel`（GLTF 解析/层级/动画）+ `SolidSkinLayer`（体素第二层）。

- **SkinViewer**：替代旧 GLSurfaceView，`setOpaque(false)` 参与视图透明合成；Choreographer vsync 驱动帧循环，**60fps 上限**（帧间隔 16ms，高刷屏跳帧）；`onSurfaceTextureDestroyed` 时 UI 线程 `CountDownLatch` 同步等渲染线程释放 EGL 并在线程内 `quitSafely`（避免死线程 Choreographer 崩溃）。手势：拖动旋转、双指捏合缩放 0.7~2.0、双击回调。
- **SkinRenderer**：相机 fov=50、距离公式 `4.5 + 16.5/tan(fov/2)/0.9/scale`（clamp 10~256）；皮肤纹理经 `NormalizedSkin` 归一化后在 UI 线程准备、`@Volatile pending*` 字段交渲染线程下一帧消费；片元 `alpha<0.1 discard`、预乘 alpha 混合；体素层用环境光 0.70+漫反射 0.42 简易光照（shader 内 `uLightMix`）。
- **GltfModel**：资产来自 Modrinth App（GPL-3.0），仅支持内嵌 base64 buffer、FLOAT accessor；刚体节点层级（无蒙皮），单位 1/16 MC 像素→×16；烘焙 clip LINEAR 插值（平移+四元数 slerp），无通道节点保持 rest；`UPPER_BODY_LIFT=0.75px` 上身抬高防腰部共面接缝闪烁（注释解释了为何不能取小：待机动画躯干下沉 0.64px 会重新贴面）；`centerModel()` 按世界包围盒重居中；绘制两遍：体素立方体（写深度）→ 零厚度面片（关深度写、GL_LESS，呈现半透明像素）。
- **GltfPlayerModel**：classic/slim 各约 131KB 同时加载切换零成本；待机 8~15 秒随机插播 idle_sub_* 变体一轮（用 `elapsed >= duration` 检测循环回绕）；开关状态两实例同步。
- **SolidSkinLayer**：复刻 3D Skin Layers / Axolotl 的 SolidPixelWrapper——每个 alpha=255 像素生成六面贴图立方体、相邻面剔除、epsilon 重叠消缝、UV 内缩 1/4096 防渗色、头 1.05/四肢 1.02 缩放校正；半透明像素不体素化（避免重叠立方体二次混合成条纹）。
- **SkinTextureLoader**：注释记录了重写动机——旧 fakefx ObjectBinding 链跨三线程回调存在覆盖竞态导致概率性停在默认皮肤；现用代际号（generation）+ 主线程回调 + `account.textures` 失效监听。
- **SkinAnimations / AnimationDialog**：动画注册表（idle + 3 变体）、`restoreSkinSettings/saveSkinSettings` 扩展函数接 DataStore；弹窗含 3D 层开关与分离开关。
- **Quat**：单精度四元数（w,x,y,z），GLTF (x,y,z,w) 顺序转换注意点写在注释里。

## 6. flite — TTS 旁述

`FliteTts` 是"游戏复述"的安卓 TTS 后端（替代缺失的 flite 引擎；`libflite` shim 在 native 侧经 JNI 调到本类，`GameMenu`/游戏进程侧调用，`JVMActivity` 在退出时 `shutdown()`）。设计要点：HandlerThread 承载 TextToSpeech（游戏线程无 Looper）；init 采用**乐观返回**（2s 内无响应视为引擎冷启动中返回 true，把等待留给 speak 阶段 20s）；speak 用 utteranceId + CountDownLatch **阻塞至播完**（与 flite 同步语义一致，上限 30s 防僵死）；引擎失败自动回退尝试设备上其他 TTS 引擎（triedEngines 去重）；过期回调忽略、构建期间被 shutdown 立即释放防泄漏。

## 7. touchcontroller — 外部控件 mod 桥接

- **`TouchController`**：对接 `top.fifthlight.touchcontroller` mod 的 proxy client（Unix socket `FoldCraftLauncher` + `TOUCH_CONTROLLER_PROXY_SOCKET` 环境变量），把 `MotionEvent` 归一化成 0..1 坐标转发（pointerId 重映射），vibrate 消息映射到系统 Vibrator；`moveView` 提供视角滑动通道。
- **`TouchControllerInputView`**（653 行）：隐形文本编辑 View，游戏内聊天框文本状态（`TextInputState`）与 Android IME 双向同步——`onCreateInputConnection` 返回完整 `InputConnectionImpl`：commitText/setComposingText/deleteSurrounding（含 codePoint 版）/批量编辑延迟提交/ExtractedText token 管理/CursorAnchorInfo 光标定位（协程 combine cursorRect+inputAreaRect）；换行不进文本而是转 `FCLInput` 的 KEY_ENTER 按键；方向键/退格映射文本操作、其他按键透传游戏。

## 8. controlconverter — FCL↔ZL2 控件布局转换（12 文件）

语义基准是 Python 参考实现 `cc.py`（替代原 Go `libcc.so` JNI，**去 ABI 依赖**）。层次：
- `CcConverter` 入口（detectFormat/convertFclToZl/convertZlToFcl/convertAuto）；
- `CcFclToZl`/`CcZlToFcl` 主流程（lossless 元数据往返：`_control_byIQge报错别找我` 键内嵌 original，回转时恢复原对象+覆写共享字段）；
- `CcButtons/CcDirection/CcEvents/CcStyles/CcGeometry` 各域转换（overlay 显示/事件分离按钮几何评分配对、方向控件↔ZL 摇杆、图层 show/hide/switch 与 FCL bindViewGroup 的 toggle 语义模拟——偶数次抵消只输出奇数次、互为伴随图层推断、内置菜单词表"聊天/社交"启发式）；
- `CcUtils` 数值层：**Python 语义复刻**——银行家舍入 pyRound、`repr(float)` 最短往返格式（pyFloatFormat，含 1e-07 科学计数）、`scalePositionToFcl` 的浮点除+银行家舍入（4999→500 不是整除 499）、CJK 与 alnum 分 run 的文本归一化；
- `CcJson`：保序树 + Python None 语义访问扩展 + `true==1` 深比较 + JSON 注释剥离；
- `CcConstants`：GLFW↔FCL 键码全表、ZL 键名别名表、fallback 表（F25→F24、侧键→滚轮、MOD_*→对应修饰键等，每个 fallback 带告警文案）。
- 对外门面 `util/LayoutConverter`：文件到文件、成功返 null 失败返错误串（与旧 JNI 调用约定一致）。

## 9. ui — 自研控件与弹窗

- **根 `DialogCard.kt`**：`dialogCardBackground/selectedCardBackground/applySelectableItemStyle`——全项目选择类列表"选中主题色半透明圆角底+勾选"的统一样式出口。
- **adapter/**：`ViewHolder`（通用复用）、`SpacingItemDecoration`（行间距装饰器，支持按行回调+缝隙内绘制分割线）、`PluginManageAdapter`（MioLibPatcher 置顶条目+插件条目，ListAdapter；**自定义 areContentsTheSame** 因重扫后 icon/appInfo 是新实例会整表误判重绑）、`RendererSelectItemAdapter`、`ManageJavaItemAdapter`、`GamepadMapItemAdapter`。
- **dialog/**：`RendererSelectDialog`（选渲染器写 Profile 设置，打开时 scrollToPosition 定位当前项，刷新按钮直接复用同一 list 实例全量刷新）、`RendererEnvDialog`（v2 插件环境变量编辑：Spinner/输入框/开关三形态，确定全量回传；FCLSpinner 关 autoTint 用固定深色文字的注释）、`JavaManageDialog`（Java 列表/删除/导入 .tar.xz 解压+patchJava+ELF 安卓性校验/Jar 执行入口含长按自定义参数）、`MioLibPatcherDialog`、`GamepadMapDialog`。
- **popup/**：`VersionSwitchPopup`——长按版本卡片弹出的快速切换弹窗，与右菜单对齐定位（顶到屏顶、悬于卡片上方），数据读 `VersionCache` 共享快照，`preload()` 后台预热（等仓库加载 30s+刷新 60s 超时）。
- **view/**：`GuideOverlayView`（功能引导全屏遮罩：软件层 CLEAR 挖圆角洞+呼吸描边+气泡卡片，洞位置动画扩散/滑动/收缩）、`WaveProgressView`（M3 风格连续波浪进度线，振幅沿 x 连续过渡，负进度为不确定态；下载面板开关）、`SwipeMenuLayout` + `RecyclerView.closeSwipeMenuOnOutsideTouch` 扩展（左滑菜单：菜单层垫底、速度吸附、打开时点内容只关菜单、水平拖动 requestDisallowIntercept；这是"右菜单/favorite 列表滑动菜单"的核心容器）、`DownloadSlidePanel/DownloadListAdapter`、`DraggableTextView`（可拖动保存位置的悬浮文本，SharedPreferences 按 saveKey 存储）、`CursorView`（游戏内鼠标光标，offsetX/Y 做 dp 偏移修正）。
- **widget/**：`FCLAppBarLayout`（autoTint 主题跟随）。注：FCLSpinner/FCLMenuView 实际位于 `fcllibrary`（组件库层），com.mio 只调用不定义；FCLTabLayout 亦在 fcllibrary。

## 10. cache / minecraft / promo / dialog / JavaManager

- **`cache/VersionCache`**：版本列表会话级快照（组件摘要、整合包 tag、图标 ConstantState、Mod 数、真实版本号），`parallel()` 流计算，按 Profile 实例缓存；版本列表页与 VersionSwitchPopup 共享；图标每次 `newDrawable()` 防 bounds 污染；Mod 数用 `Files.list().use()` 防 fd 泄漏。
- **minecraft/**：`ModChecker` 按 modId 黑名单/条件检查（physicsmod 查 zip 内 ELF 架构、mcef/valkyrienskies/replaymod(需 FFmpeg 插件)/axiom(需 NativeLib 插件)/sodium+GL4ES≥1.17 拒绝等）；`ResourcePack`（解析 pack.mcmeta/图标/格式范围，读写 options.txt 时**同步维护 incompatibleResourcePacks** 等价游戏内"仍然使用"；GameOption.set(List) 不带引号的坑有注释）；`ShaderPack`（shaders/ 目录有效性）。
- **`promo/QuarkPromo`**：启动前推广拦截，仅中国大陆中文用户；保底概率 0.6% 起、80 次起每次 +10% 封顶 90%、90 次必弹；链接未就绪时保留触发（pendingPopup）下次兑现；`updateNetdiskUrl` 由 UpdateChecker 回填。
- **`dialog/ItemSelectionDialog`**：单项选择对话框，宽度按最长条目实测自适应（320~560dp）、高度用"空白根测量+行高累加"精确 WRAP_CONTENT 不裁按钮。
- **`JavaManager`**（包根）：Java 运行时管理，扫描 `JAVA_PATH` 下含 `release` 文件的目录读 JAVA_VERSION；"Auto"兜底；`getSuitableJavaVersion` 找精确或下一个更高 major。

## 11. util（15 文件）

`AndroidUtil`（378 行最大杂烩：ELF 架构读取——so 文件/zip 内条目两种、checkElfIsAndroid 检查动态链接是否安卓风格、内存信息、剪贴板、openLink 降级复制、getLocalizedText 按 getIdentifier 反查、hasStringId 带 ConcurrentHashMap 缓存、isAdrenoGPU 经 EGL 探测、ViewPager2 禁滚轮、下载引用计数 WakeLock/WifiLock）；`AnimUtil`（属性动画工厂）；`DialogUtil`（showErrorDialog/showItemSelectionDialog 等快捷）；`DisplayUtil`（真实屏幕尺寸+刘海计算，非全屏时扣 notch）；`GuideUtil`（GuideTag sealed interface 类名即持久化 id，DataStore 去重已展示）；`ImageUtil`（Glide 背景加载）；`LauncherUtil`（自定义启动器名模板 `${launcher_version}` 替换）；`LayoutConverter`（见上）；`LoginProgress`（微软登录阶段文案+主线程回调绑定）；`MathUtil`（中文万/亿 vs 英文 K/M/B 数字缩写）；`ParseUtil.java`（Oracle JDK 源码节选，URL 编码路径校验）；`PerfUtil`（Looper setMessageLogging 主线程卡顿检测，>300ms 打印+栈采样）；`PixelIcon`（≤128px 像素图最近邻插值保持锐利，ConstantState 缓存+每次新实例）；`SourceBadgeStyle`（CurseForge/Modrinth 徽标主题色配色）；`SystemDns`（读当前网络真实 DNS，供 Terracotta 联机用）。

## 12. 与 fcl / fclcore / fclauncher 的交互点

被 tungsten 侧约 110 个文件引用，重点链路：
- **启动链**：`LauncherHelper` 用 JavaManager/ModChecker/NativeLibPlugin/RendererManager/ParseUtil；`DefaultLauncher` 在 JVM 参数装配时若 `MioLibPatcherManager.isEnabled()` 加 `-javaagent` + `getJvmOptions()`，并追加 `mio.Wrapper`；`FCLauncher`/`FCLConfig`/`LaunchOptions` 携带 `Renderer` 与 DriverPlugin/FFmpegPlugin/NativeLibPlugin 输出；`FCLGameLauncher` 设置渲染器环境变量（v1 约定 `/` 开头拼插件 nativeLibraryDir）。
- **UI 链**：`MainActivity/MainUI/UIManager`（下载面板、VersionSwitchPopup、右菜单）、`AccountUI`（SkinViewer/SkinTextureLoader/AnimationDialog）、download 包 favorite 页、manage 页（ResourcePack/ShaderPack 适配器）、`PluginManagePage`、`VersionSettingPage`。
- **依赖方向**：com.mio 大量 import `fclcore.task`（Task/Schedulers）、`fclcore.fakefx`（属性绑定，DownloadManager/SkinTextureLoader 的 ChangeListener）、`fcllibrary`（FCLDialog/ThemeEngine/FCLSpinner 等）。

## 13. 值得注意的实现细节与坑

1. **同步 DataStore 读取模式**：PluginManager/MioLibPatcherManager 用 `runBlocking { data.first() }` 首次同步加载（小文件可接受），但若在主线程首次触碰会卡顿；PluginManageAdapter 特意自定义 Diff 比较规避重扫整表重绑。
2. **DownloadManager.onStop 不移除 listener** 的 CME 注释是实战经验：onStop 是最终回调，移除动作须在遍历结束后（dismiss 时可移、回调内禁移）。
3. **Room 破坏式迁移**：favorite 库 v2 尚无正式迁移，升级会清空收藏。
4. **skin 双实例设计**与 pending 字段跨线程协议（`@Volatile` 单字段、分离开关转交渲染线程消费）避免了锁；`SkinTextureLoader` 注释记录了旧实现的竞态根因。
5. **controlconverter 的 Python 语义复刻**非常谨慎（银行家舍入、repr 格式、dict 保序、true==1 比较，注释带编号），存在跨实现对拍（deterministic 模式）。
6. **硬编码中文**：CcGeometry 内置菜单词表（"聊天""社交"）、CcEvents 的"聊天"分组特判（按 T 键抑制聊天层切换）依赖中文布局名。
7. **GltfModel 上身抬高 0.75px** 与 ZL 死区/锁定阈值单位换算（ZL 0–1 vs FCL ×100）这类魔法数字都有详细因果注释。
8. **QuarkPromo** 是商业化推广逻辑与启动流程的耦合点（interceptLaunch 挂在启动前），非大陆用户直接放行。
9. `PerfUtil.install()` 一旦安装会全局打日志（属调试工具，是否常开需查调用方）。
10. `LaunchCountSetting` 注释默认值与 `SkinAnimations.DEFAULT_ID`（"idle"）不一致——实际以注册表 validId 回退兜底，无功能问题但易混淆。
