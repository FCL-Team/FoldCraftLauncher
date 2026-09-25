# Changelog

## [1.3.3.5] - 2026-09-25

### 中文

#### ✨ 新功能

1. **兼容 lwjgl3ify，开箱即玩 GT New Horizons**：自动检测 mods 目录中的 lwjgl3ify 并合并内嵌 relauncher 配置（RetroFuturaBootstrap 主类、Java 版本要求、全套 --add-opens 参数与依赖闭包），合并幂等、lwjgl3ify 升级后自动重新合并，首次合并前自动备份原版本 JSON；内嵌库按来源重写 Maven 下载地址，forgePatches 构件从 jar 内离线还原；自动关闭桌面快捷方式生成，避免在 Android 上静默退出；版本设置强制 jre8 时提示切换 Auto 并建议 ≥4GB 内存
2. **Legacy Fabric 加载器安装**：同步上游 HMCL，安装页新增 Legacy Fabric / Legacy Fabric API 条目，与 Fabric 按是否存在 net.legacyfabric 库互斥区分，兼容矩阵、依赖绑定与版本区间对齐上游；启动链路将 legacyfabric 归入 fabric 体系
3. **世界存档能力同步上游**：世界列表条目新增世界图标与复制世界入口；支持 26.1 新存档格式（世界生成设置与玩家数据外置文件、难度设置等新字段）；新增 session.lock 锁检测，世界被占用时给出提示；NBT 读取支持 GZip / LZ4 Block 压缩自动检测
4. **光影管理 tab**：管理页新增光影管理，扫描版本 shaderpacks 目录的 zip 与文件夹形式光影包，支持导入、搜索过滤、多选删除、长按重命名与详情弹窗，可跳转下载页光影 tab；启停由游戏内光影模组（Iris/OptiFine 等）完成
5. **版本图标识别同步上游**：快照与预发布版显示命令方块图标

#### ⚡ 优化

1. **主题色透明度百分比显示**：透明度设置改以百分比呈现，更直观；FCLMenuView 选中色不再受主题透明度影响
2. 精简资源包页属性初始化与世界列表过滤判空

#### 🐛 修复

1. 修复 jre25 下 AWT 回退 headless：cacio17 更新至 jdk25 修复版构建，依赖 java.awt.Font 的模组不再启动即崩
2. 修复外接物理键盘输入重复：物理键盘字符随按键一次性下发
3. 修复定制 ROM 上控制器仓库搜索框回车崩溃：补 imeOptions 搜索动作

### English

#### ✨ New Features

1. **lwjgl3ify compatibility — GT New Horizons out of the box**: lwjgl3ify jars in the mods folder are detected automatically and their embedded relauncher config (RetroFuturaBootstrap main class, Java version requirement, the full --add-opens set and dependency closure) is merged into the instance version idempotently — re-merged after lwjgl3ify upgrades, with the original version JSON backed up before the first merge; embedded libraries get rewritten Maven sources, and the forgePatches artifact is restored offline from the jar; desktop-entry creation is disabled automatically to prevent silent exits on Android; a hint to switch to Auto (with ≥4GB RAM recommended) is shown when jre8 is forced
2. **Legacy Fabric loader installation**: Synced from upstream HMCL — the install page gained Legacy Fabric / Legacy Fabric API entries, mutually exclusive with Fabric by the presence of net.legacyfabric libraries, with the compatibility matrix, dependency bindings and version ranges aligned upstream; the launch chain routes legacyfabric through the fabric system
3. **World save capabilities synced from upstream**: World list entries now show world icons and a copy-world action; the new 26.1 save format is supported (external world-gen settings and player data files, new fields such as difficulty settings); session.lock detection warns when a world is in use; NBT reading auto-detects GZip / LZ4 Block compression
4. **Shader pack management tab**: The manage page gained a shader tab that scans the version's shaderpacks folder (zip files and folders), supporting import, search filtering, multi-select deletion, long-press rename and a detail dialog, with a shortcut to the download page's shader tab; enabling/disabling is left to in-game shader mods (Iris/OptiFine, etc.)
5. **Version icon recognition synced from upstream**: Snapshots and pre-release versions now show a command block icon

#### ⚡ Improvements

1. **Theme color transparency shown as a percentage**: More intuitive than raw values; the FCLMenuView selection color is no longer affected by theme transparency
2. Simplified resource pack page property initialization and world list filtering null checks

#### 🐛 Bug Fixes

1. Fixed AWT falling back to headless on jre25: cacio17 updated to a JDK 25 fixed build, so mods depending on java.awt.Font no longer crash on launch
2. Fixed duplicated input with external physical keyboards: physical keyboard characters are now sent together with key events in one pass
3. Fixed a crash when pressing enter in the controller repository search box on custom ROMs: an imeOptions search action was added

## [1.3.3.4] - 2026-09-23

### 中文

#### ✨ 新功能

1. **下载页资源收藏与分组**：下载列表左滑即可收藏资源，支持自定义分组（管理入口为分组区扳手图标）、分组筛选、多选与批量一键下载
2. **资源包管理页**：管理页新增资源包 tab，支持启用/禁用（写 options.txt）、导入、重命名、多选删除与搜索
3. **版本卡片快速切换**：长按主界面版本卡片弹出版本快速切换菜单（右侧启动按钮可直接切换并启动），数据与版本列表页共享会话快照；另新增首次使用的点击卡片引导提示（仅显示一次）
4. **皮肤模型开关**：新增 3D 皮肤层开关，禁用时第二层回落原版面片，状态随动画一起持久化；新增身体与腿部分离开关，关闭后上身贴合腿部

#### ⚡ 优化

1. **管理页与设置页 tab 图标化**：tab 标题旁显示对应图标，视觉更直观
2. **FCLTabLayout 滑动指示箭头**：tab 栏可左右滑动时在对应边缘显示指示箭头，提示还有未展示的内容
3. **控件转换器字段同步**：滑动联动与摇杆死区/前进锁字段支持双向映射

#### 🐛 修复

1. 修复 Cleanroom 字母键与聊天输入失效：补齐按键与字符事件成对发送
2. 修复导入含 null 的控制器 JSON 保存时崩溃：控件数据反序列化全链空安全加固
3. 修复新建控件组后因数据未标记加载完成而无法添加按键
4. 修复下载与收藏列表标题过长时来源徽标被挤出可视区：标题行改用 ConstraintLayout 保证徽标固定行尾

### English

#### ✨ New Features

1. **Download page favorites and groups**: Swipe left on download list items to favorite resources, with custom groups (manage via the wrench icon in the group area), group filtering, multi-select and one-tap batch download
2. **Resource pack management page**: A new resource pack tab in the manage page supports enabling/disabling (writes options.txt), importing, renaming, multi-select deletion and search
3. **Quick version switch on the version card**: Long-press the version card on the main screen to open a quick version switch menu (the button on the right switches and launches directly); the data shares a session snapshot with the version list page. A one-time guide hint for tapping the card was also added
4. **Skin model toggles**: A new 3D skin layer toggle — disabling it falls the second layer back to vanilla faces, with the state persisted alongside animations; a new body/leg separation toggle — when off, the upper body fits onto the legs

#### ⚡ Improvements

1. **Icons on manage and setting page tabs**: Tabs now show an icon next to the title for quicker recognition
2. **FCLTabLayout scroll indicator arrows**: When the tab bar can scroll left/right, indicator arrows appear at the corresponding edge to hint at hidden content
3. **Control converter field sync**: Swipe-chain linkage and joystick dead zone / forward-lock fields now support two-way mapping

#### 🐛 Bug Fixes

1. Fixed Cleanroom letter keys and chat input not working: key and character events are now sent in pairs
2. Fixed crashes when saving imported controller JSON containing nulls: null-safety hardening across the whole control-data deserialization chain
3. Fixed being unable to add keys after creating a new control group (the data was not marked as loaded)
4. Fixed the source badge being squeezed out of view on long download/favorite list titles: the title row now uses ConstraintLayout to keep the badge pinned to the end of the line

## [1.3.3.3] - 2026-09-18

### 中文

#### ✨ 新功能

1. **下载页聚合搜索**：CurseForge 与 Modrinth 双源并行搜索，结果交错合并并按标识/标题归一化去重，单源失败时自动降级只显示可用来源
2. **MultiMC 整合包导入**：同步上游 HMCL 实现，支持 natives 字符串键、本地库文件名推导、minecraftArguments 兼容等适配，可导入 lwj3ify 等特殊实例
3. **FCL 弹窗彩蛋**：点击启动游戏按保底概率触发

#### ⚡ 优化

1. **下载源体系跟进上游重构**：DownloadProvider 全链路对齐上游 HMCL，URL/URI 与组件类型模型精简
2. **版本列表会话级快照缓存**：命中快照即时显示、后台重算覆盖，切换页面不再等待；排序改用 GameVersionNumber 真实游戏版本号降序，手动刷新强制失效缓存
3. **主界面右菜单布局调整**：执行 Jar 功能移入 Java 管理对话框，右菜单更聚焦版本与账户
4. **渲染器选择体验**：对话框打开时自动定位到当前选中的渲染器
5. **FCLNumberSeekBar 轨道自绘**：数值文本两侧断开并圆角收口，视觉更完整
6. **主题派生色透明度独立设置**：主色全透明时背景不再不可见
7. **版本图标像素感知加载**：按图标尺寸切换缩放插值（小图最近邻、大图双线性），禁用密度预缩放解码，安装器、下载页等全部加载点统一，像素风图标放大不再发糊；更换 NeoForge 版本图标

#### 🐛 修复

1. 修复 SDL 复用窗口被提前销毁导致游戏崩溃
2. 修复 Controlify 环境无法呼出软键盘：SDL 输入法按渲染路径分流，仅 SDL 渲染激活时走 SDL 通道，其余回落原路径
3. 微软登录体验修复：登录结束或设备码轮询成功后自动关闭内嵌登录页，返回键直接退出不回退网页；修复 OAuth 事件监听残留导致堆叠多个登录页
4. 修复多任务返回后导航栏常驻遮挡底部按钮（对话框重获焦点时恢复沉浸标志）；编辑控件对话框背景补 10dp inset 恢复屏幕边缘间隔
5. 修复皮肤半透明像素体素化重叠产生的大量条纹，半透明回落零厚度面片呈现
6. 修复 SeekBar 行复用时 max/min 钳制回调误写数据
7. 修复下载源空缓存在后台线程早读导致的崩溃（DownloadProviders 改惰性查表）
8. 修复 goSetting 菜单已选中时不触发切页；usesGlobal 反序列化缺省值改为 true

### English

#### ✨ New Features

1. **Aggregated search on the download page**: CurseForge and Modrinth are searched in parallel, results are interleaved and deduplicated by normalized identifier/title, and a failing source degrades gracefully to the other
2. **MultiMC modpack import**: Synced from upstream HMCL, with adaptations for string-keyed natives, local library filename derivation, and minecraftArguments compatibility — imports special instances such as lwj3ify
3. **FCL easter egg dialog**: Triggered with a pity probability when launching the game

#### ⚡ Improvements

1. **Download provider system follows the upstream refactor**: The full DownloadProvider chain is aligned with upstream HMCL, with a leaner URL/URI and component-type model
2. **Session-level version list snapshot cache**: A cache hit shows instantly while recomputation runs in the background, so switching pages no longer waits; sorting now uses real game version numbers (GameVersionNumber) in descending order, and manual refresh invalidates the cache
3. **Main screen right menu layout reworked**: "Execute Jar" moved into the Java management dialog, keeping the right menu focused on versions and accounts
4. **Renderer selection UX**: The dialog now auto-scrolls to the currently selected renderer when opened
5. **Self-drawn FCLNumberSeekBar track**: The track breaks and rounds around the value text for a cleaner look
6. **Separate theme derived-color alpha setting**: The background no longer disappears when the theme color is fully transparent
7. **Pixel-aware version icon loading**: Scaling interpolation switches by icon size (nearest-neighbor for small icons, bilinear for large ones), density pre-scaling decoding is disabled, and all loading points (installer, download pages, etc.) are unified — pixel-art icons stay sharp when scaled up; the NeoForge version icon was replaced

#### 🐛 Bug Fixes

1. Fixed game crashes caused by SDL reused windows being destroyed early
2. Fixed the soft keyboard not opening with Controlify: SDL IME now routes by render path — the SDL channel is used only when SDL rendering is active, otherwise it falls back to the original path
3. Microsoft login fixes: the embedded login page now closes automatically when login finishes or device-code polling succeeds, and Back exits directly instead of navigating back through web pages; fixed stacked login pages caused by leaked OAuth event listeners
4. Fixed the navigation bar staying on top and covering bottom buttons after returning from multitasking (immersive flags are restored when dialogs regain focus); the control edit dialog background gains a 10dp inset to restore screen-edge spacing
5. Fixed heavy striping from overlapping semi-transparent voxels in skins — translucency now falls back to zero-thickness faces
6. Fixed SeekBar row reuse writing wrong data through max/min clamping callbacks
7. Fixed background crashes from an early read of the empty download-provider cache (DownloadProviders now uses lazy lookup)
8. Fixed goSetting not switching pages when already selected; the usesGlobal deserialization default is now true

## [1.3.3.2] - 2026-09-13

### 中文

#### ✨ 新功能

1. **游戏内复述功能恢复（TTS）**：libflite 桥接安卓系统 TTS，游戏复述改走系统语音引擎
2. **原生 JSound**：原生 libjsound.so 桥接 OpenAL，一套源码服务 jre8/17/21/25，修复 Forge 加载器下 Java Sound 失效
3. **控制布局编辑与运行时增强**：控件双角手柄缩放、悬浮操作栏、控件组管理面板与控件组复制、滑动链、摇杆死区与前进锁、全局控件不透明度
4. **控件编辑器重构为 Kotlin**：全面对齐游戏菜单视觉，修复吸附与编辑条问题，新增控件组复制
5. **游戏内右菜单重构**：顶部图标 Tab 直接显示分类内容；物品栏缩放移入手势页、控件不透明度移入左菜单、锁定/隐藏控件移入调试页
6. **渲染器版本上限调整**：判断/展示双轨版本号与 26.3 版本目录，修正各渲染器版本适配情况

#### ⚡ 优化

1. **解除帧率锁定**：游戏帧率不再锁定屏幕刷新率，启动时向系统投票设备最高刷新率；关闭垂直同步时交换间隔强制置 0 并切入 BufferQueue 异步模式
2. **JNA natives 分架构打包**：去 zip 化，改为分架构 natives 目录并随 APK 架构裁剪打包
3. **FCLNumberSeekBar 重构为 Kotlin**：轨道加粗为胶囊条、数值文本去除底衬，修复两端点击热区与显示位置不一致
4. **皮肤模型细节**：缩小腰部接缝间隙
5. **下载管理面板样式调整**

#### 🐛 修复

1. 修复游戏内无法解析 SRV 记录的问题
2. 修复使用 zink 启动 26.3+ 时无法自动回退 Vulkan
3. 修复 lwjgl-sdl 并入合并产物后与 Forge 模块解析的 split package 冲突
4. 修复插件卸载缺少 REQUEST_DELETE_PACKAGES 权限导致系统卸载器立即退出
5. 修复模组更新保留旧版本时切换后崩溃
6. 修复熄屏状态下打开启动器可能崩溃
7. 修复主题色带透明度时 ltColor/dkColor 透明度不跟随
8. 修复 ViewPager2 布局时误清输入框焦点导致的输入异常（FCLEditText 增加焦点恢复守卫）
9. 修复下载模组列表已安装标记不实时刷新：扫描完成后通知列表、下载成功回调触发重检、payload 局部绑定避免动画重播

#### 🔧 其他

1. 更新多语言翻译

### English

#### ✨ New Features

1. **In-game narration restored (TTS)**: libflite now bridges Android system TTS, so game narration uses the system speech engine
2. **Native JSound**: Native libjsound.so bridging OpenAL, one set of sources serving jre8/17/21/25, fixing Java Sound failures under the Forge loader
3. **Control layout editing and runtime enhancements**: Dual-corner handle scaling, floating action bar, control group management panel with group duplication, sliding chains, joystick dead zone and forward lock, global control opacity
4. **Control editor rewritten in Kotlin**: Fully aligned with the in-game menu visuals, fixed snapping and editing bar issues, added control group duplication
5. **In-game right menu rework**: Top icon tabs now show category content directly; hotbar scale moved to the gestures page, control opacity to the left menu, and lock/hide controls to the debug page
6. **Renderer version limits adjusted**: Dual-track version numbers for checking/display plus a 26.3 version catalog, with renderer compatibility corrected

#### ⚡ Improvements

1. **Frame rate unlocked**: The game frame rate is no longer locked to the screen refresh rate — the launcher votes for the device's maximum refresh rate at startup; with vsync off the swap interval is forced to 0 and the BufferQueue switches to async mode
2. **Per-ABI JNA natives packaging**: De-zipped, switched to per-ABI natives directories pruned together with the APK architecture
3. **FCLNumberSeekBar rewritten in Kotlin**: Track thickened into a capsule bar, value text background removed, and mismatched edge click hotspots/display positions fixed
4. **Skin model detail**: Narrowed the waist seam gap
5. **Download manager panel style refresh**

#### 🐛 Bug Fixes

1. Fixed SRV record resolution failing in game
2. Fixed failing to fall back to Vulkan when launching 26.3+ with zink
3. Fixed the split package conflict between the merged lwjgl-sdl artifact and Forge module resolution
4. Fixed plugin uninstall exiting immediately due to the missing REQUEST_DELETE_PACKAGES permission
5. Fixed a crash when switching mods kept as old versions after an update
6. Fixed a possible crash when opening the launcher while the screen is off
7. Fixed ltColor/dkColor alpha not following when the theme color has transparency
8. Fixed input anomalies caused by ViewPager2 layout clearing EditText focus (FCLEditText now guards focus restoration)
9. Fixed installed badges in the mod download list not refreshing in real time: the list is notified after scanning, download success callbacks trigger a re-check, and payload-based partial binding avoids animation replays

#### 🔧 Other

1. Updated translations

## [1.3.3.1] - 2026-09-08

### 中文

#### ✨ 新功能

1. **SDL3 集成**：集成 SDL3 输入/渲染，支持启动 Minecraft 26.3+；SDL 直通输入不再落入按键映射层
2. **插件管理功能**：设置页新增「插件管理」，统一扫描管理渲染器/驱动/native 库插件，支持禁用持久化；新增 RendererPlugin V2 支持与插件环境变量配置；插件管理页置顶 MioLibPatcher 管理项，支持启用/禁用与功能开关（配置对话框改 v2 风格，Sable Rapier 默认开启）
3. **皮肤 3D 预览重构**：改用 GLTF 模型 + 烘焙动画 + 体素化第二层
4. **手柄输入对齐**：首次手柄输入弹窗选择模式；「禁用手柄映射」改为「手柄控制」总开关（持久化，关闭时禁用模式选择）
5. **JSound 集成**：javax.sound → OpenAL 桥接集成到 lwjgl 共享源码，提升游戏音频兼容性
6. **控件转换器**：改用纯 Kotlin 实现并支持导入 ZL2 布局
7. **待机动画随机插播变体**：基础待机 8~15 秒后插播一个变体（避开上一次，播完回待机）
8. **游戏内菜单卡片化**：左右菜单条目卡片化，抽公共 MenuUi 卡片背景与间距
9. **下载页分类 tab 图标**：分类 tab 添加图标，并调整下载管理面板样式
10. **FCLSpinner 重写为自定义 View**：修复滑动页面指示器错乱与 listener 伪回调；文字随主题色着色（浅底对话框场景可关闭）；展开时箭头旋转 180° 过渡，选项弹窗增加下落淡入/上滑淡出动画
11. **语言切换新增**：日语、土耳其语、繁体中文（台湾）

#### ⚡ 优化

1. **Fabric 下载列表加载速度**：优化列表构建性能，缩短加载等待
2. **控制数据序列化改手写 JSON**：全面消除 Gson 递归解析；控制器保存任务合并，避免序列化任务风暴
3. **版本列表分类指示器**：改为 TabLayout 并自适应铺满
4. **选择类弹窗统一**：统一为 ItemSelectionDialog 卡片式风格；渲染器选择对话框改用卡片 item，统一行高并新增来源显示（内置/插件名），无版本范围时显示未知
5. **统一游戏日志分享逻辑**：附带 hs_err 崩溃日志并脱敏 accessToken
6. **IO 线程池扩容**：Schedulers.io() 线程数 4 调整为 8
7. **Turnip 驱动加载重写**：改为命名空间方案，提升驱动加载兼容性

#### 🐛 修复

1. 修复 SDL 函数经 dlsym 解析绕过 hook 导致的窗口复用不生效（解析出口改用代理）
2. 修复 SDL_EGL_LIBRARY 对系统 EGL 错误拼接启动器 native 路径
3. 修复导出 MCBBS 整合包因文件过多导致的 Out Of Memory
4. 修复皮肤预览渲染线程退出时 Choreographer 死线程崩溃
5. 修复联机对话框节点列表阻塞点击响应（改独立线程获取）
6. 修复插件 v2 环境变量配置不持久的问题
7. 修复崩溃页背景不随亮暗模式切换，暗色模式下背景与文字同色无法看清
8. 修复 RendererEnvDialog 的 FCLSpinner 深色模式文字与对话框背景同色
9. 修复检查更新时间戳比较精度不一致导致的首次误报（统一秒级精度）

#### 🔧 其他

1. 新增控制数据序列化回归测试
2. 接入 checkstyle 静态检查与 PR 自动评论机器人（规则适配现状）
3. 启用 core library desugaring，兼容 Java 9+ stdlib API
4. 删除「强制渲染器在大核上运行」功能
5. 更新 MioLibPatcher
6. 更新多语言翻译

### English

#### ✨ New Features

1. **SDL3 integration**: Integrated SDL3 input/rendering to support launching Minecraft 26.3+; SDL direct input no longer falls into the key-mapping layer
2. **Plugin manager**: New "Plugin management" in settings, scanning and managing renderer/driver/native plugins in one place with persisted enable/disable state; RendererPlugin V2 and per-plugin environment variable support; MioLibPatcher pinned to the top of the plugin page with enable/disable and feature toggles (config dialog restyled to v2, Sable Rapier enabled by default)
3. **Skin 3D preview rebuilt**: GLTF model + baked animations + voxelized overlay layer
4. **Gamepad input alignment**: Mode-selection prompt on first gamepad input; "disable gamepad mapping" replaced by a "gamepad control" master switch (persisted, disables mode selection when off)
5. **JSound integration**: javax.sound → OpenAL bridge integrated into the lwjgl shared sources for better in-game audio compatibility
6. **Control converter**: Rewritten in pure Kotlin with support for importing ZL2 layouts
7. **Idle animation variants**: A random variant is inserted after 8–15 seconds of base idle (avoiding the previous one, returning to idle when finished)
8. **In-game menu cards**: Left/right menu items are now card-styled, with a shared MenuUi card background and spacing
9. **Download page category tab icons**: Category tabs now show icons, and the download manager panel style is refreshed
10. **FCLSpinner rewritten as a custom view**: Fixed sliding indicator glitches and fake listener callbacks; text now tints with the theme (can be disabled on light-background dialogs); 180° arrow rotation when expanded, plus drop/fade window transition animations for the option popup
11. **New languages**: Japanese, Turkish and Traditional Chinese (Taiwan)

#### ⚡ Improvements

1. **Fabric download list speed**: Optimized list building for shorter waits
2. **Hand-written JSON for control data serialization**: Gson recursive parsing fully removed; controller save tasks merged to avoid serialization task storms
3. **Version list category indicator**: Migrated to TabLayout, filling the width adaptively
4. **Unified selection dialogs**: ItemSelectionDialog card-based style; the renderer selection dialog now uses card items with unified row height and a source column (built-in/plugin name), showing "unknown" when no version range exists
5. **Unified game log sharing**: hs_err crash logs are attached and access tokens are sanitized
6. **IO thread pool expanded**: Schedulers.io() thread count raised from 4 to 8
7. **Turnip driver loading rewritten**: Namespace-based scheme for better driver loading compatibility

#### 🐛 Bug Fixes

1. Fixed SDL window reuse not taking effect because dlsym-resolved functions bypassed hooks (proxy at the resolution exit)
2. Fixed SDL_EGL_LIBRARY wrongly appending the launcher native path to the system EGL
3. Fixed Out Of Memory when exporting MCBBS modpacks with too many files
4. Fixed a Choreographer dead-thread crash when the skin preview render thread exits
5. Fixed the multiplayer dialog node list blocking click response (now fetched on a dedicated thread)
6. Fixed plugin v2 environment variable configuration not persisting
7. Fixed the crash page background not following light/dark mode, leaving text unreadable in dark mode
8. Fixed FCLSpinner text in RendererEnvDialog matching the dialog background in dark mode
9. Fixed false update prompts caused by inconsistent timestamp precision (now compared at second precision)

#### 🔧 Other

1. Added regression tests for control data serialization
2. Introduced checkstyle static checks with a PR auto-comment bot (rules adapted to the current codebase)
3. Enabled core library desugaring for Java 9+ stdlib API compatibility
4. Removed the "force renderer onto big cores" feature
5. Updated MioLibPatcher
6. Updated translations

## [1.3.3.0] - 2026-09-02

### 中文

#### ✨ 新功能

1. **全局下载管理与前置依赖一键下载**：下载面板统一管理下载任务，标题行实时显示下载速度；下载模组时自动识别前置依赖并支持一键下载
2. **模组远程查询磁盘缓存**：指纹/详情/版本列表/分类结果落盘缓存，存储改用 SQLite，消除全量加载与写放大，大幅减少重复网络请求
3. **整合包安装实时进度**：解压与解析阶段上报实时进度，安装过程不再是无进展的黑盒
4. **任务等待过程可见**：版本列表刷新任务显示名称，安装加载器期间的联网等待不再表现为静默卡住
5. **下载面板实时速度**：标题行显示当前下载速度

#### ⚡ 优化

1. **下载稳定性**：重试间增加指数退避等待，连接超时从 8 秒调整为 10 秒
2. **懒加载控制布局**：控制器布局按键按需异步加载，优化加载速度
3. **MenuView 重写为 Kotlin**：优化触摸打开抽屉的判定
4. **模组查询细节优化**：CurseForge 大文件指纹改为流式计算避免 OOM（HMCL#6631）；Modrinth 指纹未命中（404）写入负缓存，消除重复白查与错误日志

#### 🐛 修复

1. 修复离线皮肤对话框取消时覆盖旧皮肤文件，现在点确认时才落位 SKIN_DIR
2. 修复取色拖动时部分设备卡死（主题刷新消息合并，避免高频重绘风暴）
3. 修复 NeoForge 版本列表对缺失 versions 字段未判空导致的崩溃
4. 修复控制器编辑模式视图组初始化时序问题（setup 阶段自动初始化，不再依赖菜单列表绑定兜底）
5. 修复任务对话框日志区在写日志的任务结束后不收起
6. 修复 Kotlin 2.4.10 产物 dex 时 metadata 解析崩溃（buildscript classpath 覆盖 R8 9.4.17）
7. 修复前置解析的 Stream.toList 在低版本设备的兼容性问题

#### 🔧 其他

1. 模组数据类 record 化并迁移 kotlinx.serialization，根治缺失字段 NPE
2. 文件选择回调迁移 SelectedFile，统一 content 与本地路径处理
3. 更新 MioLibPatcher
4. 工具链升级：Kotlin Gradle 插件 2.4.10、Gradle 8.14.4
5. 更新多语言翻译（日语、繁中 HK、土耳其语等，来自 Weblate）

### English

#### ✨ New Features

1. **Global download manager & one-tap prerequisite download**: The download panel now manages download tasks centrally with real-time speed in the title row; mod prerequisite dependencies are detected automatically and can be downloaded with one tap
2. **Disk cache for remote mod queries**: Fingerprints/details/version lists/categories are cached on disk, now backed by SQLite, eliminating full in-memory loads and write amplification and greatly reducing repeated network requests
3. **Real-time modpack install progress**: The unzip and parse stages now report live progress instead of an unresponsive wait
4. **Visible task waits**: Version list refresh tasks display their names, so the network wait while installing loaders no longer looks like a silent hang
5. **Live download speed**: The download panel title row shows the current download speed

#### ⚡ Improvements

1. **Download stability**: Exponential backoff waits between retries; connection timeout raised from 8 to 10 seconds
2. **Lazy controller layout loading**: Controller layout keys load asynchronously on demand, speeding up loading
3. **MenuView rewritten in Kotlin**: Improved touch-open drawer detection
4. **Mod query refinements**: CurseForge fingerprints of large files are now computed in a streaming fashion to avoid OOM (HMCL#6631); Modrinth fingerprint misses (404) are negatively cached, eliminating repeated futile lookups and error logs

#### 🐛 Bug Fixes

1. Fixed the offline skin dialog overwriting the old skin file on cancel; the skin file is now only written on confirm
2. Fixed freezing on some devices while dragging the theme color picker (theme refresh messages are merged to avoid high-frequency redraw storms)
3. Fixed a crash from NeoForge version lists missing the versions field
4. Fixed controller edit-mode view group initialization timing (auto-initialized during setup instead of relying on the menu-list binding fallback)
5. Fixed the task dialog log area not collapsing after the logging task finishes
6. Fixed a dex metadata parsing crash for Kotlin 2.4.10 output (buildscript classpath now pins R8 9.4.17)
7. Fixed a compatibility issue with Stream.toList in prerequisite parsing on older devices

#### 🔧 Other

1. Mod data classes record-ized and migrated to kotlinx.serialization, eliminating missing-field NPEs at the root
2. File-selection callbacks migrated to SelectedFile, unifying content URI and local path handling
3. Updated MioLibPatcher
4. Toolchain upgrades: Kotlin Gradle plugin 2.4.10, Gradle 8.14.4
5. Updated translations (Japanese, Traditional Chinese HK, Turkish, etc., via Weblate)

## [1.3.2.9] - 2026-08-29

### 中文

#### ✨ 新功能

1. **微软登录实时显示认证进度**：登录过程中实时显示 Xbox 认证、档案获取等各阶段进度，不再只有转圈
2. **语言切换立即生效**：切换语言后重建 Activity 直接应用新语言，移除"重启后生效"提示
3. **设置项分组显示**：启动器设置页的主题/背景/游戏内等设置、版本设置页的渲染相关项分别连成组并带次要主题色分隔线，关于页链接行同样合并分组
4. **深色模式主要主题色**：新增深色模式下的主色调，设置页操作按钮改为图标按钮
5. **运行时安装详细进度**：运行时安装页逐条目显示"正在复制 xxx"等文件级进度
6. **未选账户启动引导**：启动游戏未选中账户时改为提示并跳转账户管理页

#### ⚡ 优化

1. **游戏内菜单重构**：重构为 RecyclerView 多级菜单
2. **安装器进程通信改文件轮询**：通过退出码文件与日志增量读取通信，日志实时显示；修复多次创建安装进程时的卡死与误杀，进程超时后自动重启 :jvm 重跑同一命令
3. **内置浏览器缓存策略**：改为默认缓存策略，退出时不再清除缓存

#### 🐛 修复

1. 修复切换游戏目录后模组下载位置与推荐版本未跟随新目录
2. 修复反复安装后 View 树泄漏（TaskDialog 任务监听引用链与临时页动画结束回调不执行），消除累积卡顿
3. 修复版本设置页输入框失焦（列表误重建），FCLEditText 迁移 Kotlin 并防御部分定制 ROM 焦点前进崩溃
4. 修复布局编辑关闭后按键位置偏移（千分比换算向下截断导致相邻按键贴住）
5. 修复设置页 Spinner 绑定伪回调误触发选中逻辑导致主题模式切换卡顿
6. 修复整合包导出文件选择页滚动时监听器与双向绑定累积导致的 OOM
7. 修复外部应用调用启动器安装整合包时崩溃
8. 修复游标抓取状态回调的竞态与空指针隐患
9. 修复游戏启动时未应用 FPS/内存显示与持续性能模式开关
10. 修复版本设置页控制器未初始化时显示默认名称、FCLNumberSeekBar 设置进度未同步保存
11. 调整下载页搜索框输入法全屏行为

#### 🔧 其他

1. 移除自定义 UUID 功能，游戏启动始终使用账户 UUID
2. 移除"清除缓存"设置项及相关字符串资源，创建账户对话框移除 TabLayout 切换
3. 禁用主界面 ViewPager2 鼠标滚轮翻页
4. 下载页游戏目录/版本与已安装标记改为动态获取，移除已无消费的快照链
5. 显式声明代码直接使用的传递依赖（core-ktx/lifecycle/recyclerview/coroutines），补全 README 相关项目与依赖清单
6. 清理模块合并后的死代码与重复启动逻辑

### English

#### ✨ New Features

1. **Real-time Microsoft login progress**: Each authentication stage (Xbox auth, profile fetch, etc.) is now shown live during login instead of a bare spinner
2. **Instant language switching**: Activities are recreated to apply the new language immediately; the "restart to apply" hint is removed
3. **Grouped settings items**: Related settings are grouped with secondary-theme-color dividers — theme/background/in-game groups on the launcher settings page, renderer-related items on the version settings page, and merged link groups on the about page
4. **Dark-mode primary theme color**: Added a primary color for dark mode; settings page action buttons are now icon buttons
5. **Detailed runtime installation progress**: The runtime installation page shows per-file progress such as which file is being copied
6. **No-account launch guidance**: Starting a game without a selected account now shows a prompt and jumps to the account management page

#### ⚡ Improvements

1. **In-game menu refactored**: Rebuilt as a RecyclerView multi-level menu
2. **Installer process communication via file polling**: Communicates through an exit-code file with incremental log reading for real-time log display; fixed hangs and wrong-process kills when creating installer processes repeatedly, with automatic :jvm restart and command retry on timeout
3. **Built-in browser caching**: Now uses the default caching policy and no longer clears the cache on exit

#### 🐛 Bug Fixes

1. Fixed mod download location and recommended version not following the newly selected game directory
2. Fixed view tree leaks after repeated installations (TaskDialog task-listener chain and temp-page animation end callbacks never running), eliminating accumulated jank
3. Fixed version settings page input focus loss (spurious list rebuild); FCLEditText migrated to Kotlin with a guard against the focus-advance crash on some custom ROMs
4. Fixed key position offset after closing the layout editor (truncating the permille conversion made adjacent keys stick together)
5. Fixed theme mode switching jank caused by settings-page spinner pseudo-callbacks falsely triggering selection logic
6. Fixed an OOM in the modpack export file picker caused by listeners and bidirectional bindings accumulating while scrolling
7. Fixed a crash when external apps invoke the launcher to install a modpack
8. Fixed a race condition and NPE risk in cursor grab state callbacks
9. Fixed FPS/memory display and sustained performance mode toggles not being applied at game launch
10. Fixed default controller name display when not initialized on the version settings page, and FCLNumberSeekBar not syncing/saving its progress
11. Adjusted the download page search box IME fullscreen behavior

#### 🔧 Other

1. Removed the custom UUID feature; game launch always uses the account UUID
2. Removed the "clear cache" setting and its string resources; removed the TabLayout switch from the create-account dialog
3. Disabled ViewPager2 mouse-wheel page switching on the main UI
4. Download page game directory/version and installed markers are now fetched dynamically; the unused snapshot chain was removed
5. Explicitly declared directly-used transitive dependencies (core-ktx/lifecycle/recyclerview/coroutines); completed the README project and dependency list
6. Cleaned up dead code and duplicate launch logic left over from the module merge

## [1.3.2.8] - 2026-08-24

### 中文

#### ✨ 新功能

1. **启动页显示加载信息与进度条**：SplashActivity 改用 viewBinding，加载期间展示加载状态与进度反馈
2. **设置页 tab 随内容滚动收起/展开**：AppBarLayout 效果，滚动页面时 tab 自动收起、上滑恢复
3. **设置项行下方新增作用描述**：为设置项补充说明文字，方便了解设置作用
4. **下载页 tab 切换添加过渡动画**：tab 与模式页内容更新均淡入过渡
5. **右侧菜单手势交互**：支持双指滑动切换与手势显示/隐藏，隐藏时皮肤预览位置固定
6. **LWJGL GLFW 补充 glfwGetWindowPos stub**：窗口位置恒为 0,0

#### ⚡ 优化

1. **主界面 UI 架构重构（ViewPager2）**：UIManager 改用 ViewPager2 承载 8 个 UI 页面，页面随回收销毁不保留状态；页面切换统一为淡入上滑过渡动画；主界面禁用滑动手势仅通过菜单切换，避免与页面内纵向滚动冲突
2. **下载页全面优化**：5 个下载模式共享单个布局实例，tab 切换经 ViewModel 恢复状态；列表布局轻量化（线性排列、去 marquee）；图标限制 90x90 解码尺寸、快速滑动时暂停加载，消除全尺寸解码 GC 停顿；Mod 翻译数据后台预热，避免首次 bind 主线程解析
3. **设置页/版本设置页重构为 RecyclerView 行级复用**
4. **Profile/VersionSetting/Theme 完成 fakefx 迁移**：Profile 重构为 Kotlin 移除 fakefx，Profiles.selectedProfile 迁移 Repository 单例 + StateFlow，selectedVersion 迁移 StateFlow；VersionSetting 改用普通类型字段；Theme/ThemeEngine 重构为 Kotlin + StateFlow，主题存储从 SharedPreferences 迁移 DataStore
5. **模块合并与代码清理**：FCLauncher 与 FCLCore 模块整体并入 FCL；移除 FCLPath.CONTEXT，全局 Context 改用 FCLApp.getAppContext()/getActivity()；AndroidUtils 迁移至 com.mio.util 顶层函数；getLocalizedText 静态 key 迁移至 R.string；删除 DisplayAnimUtils 等死代码
6. **版本列表加载提速**：解析与资源缓存、切换 profile 保留缓存；Mod 数统计移入加载流程，避免滑动时主线程目录 IO
7. **下载页搜索框优化**：imeOptions 组合 flagNoFullscreen，消除 AutofillManager 日志刷屏
8. **界面细节优化**：圆角统一 8dp、页面 tab 背景改为顶部圆角、左侧菜单点击播放选中动画、临时页返回时下层内容上滑进入过渡
9. **更新 libcc.so**：方向控件统一转 ZL2 摇杆并修正 sizeType
10. **更新 LWJGL 3.3.3/3.4.1 合并产物**：含 glfwGetWindowPos stub；移除运行时 jsr305.jar，解决与 java.annotation 模块的包冲突

#### 🐛 修复

1. 修复切换版本后退出启动器版本回退（selectedVersion 变化未触发配置保存）
2. 修复版本列表并发崩溃、配置保存刷屏与空版本设置 NPE
3. 修复模组管理页往返切换时全量重扫模组导致的主线程 ANR
4. 修复亮暗模式切换不生效，主题切换不再依赖 Activity 重建
5. 修复主界面销毁后主题刷新回调加载崩溃，主题回调改弱引用修复页面回收后视图泄漏
6. 修复快速滑动/切换页面时控件首帧闪白（registerEvent 改为同步执行）
7. 修复离线账户披风加载 NPE
8. 修复主界面版本图标放大显示不完全（共享 Drawable 实例 bounds 互相污染）
9. 修复下载页重建后首次切换模式列表空白（复用缓存 adapter 时补设 LayoutManager）
10. 修复下载列表图标全尺寸解码 GC 卡顿、图片加载完成触发布局重排、切换模式时滑入动画重播
11. 修复下载页切换 tab 时临时页覆盖层遮挡新页面，多层临时页透明背景透出
12. 修复页面切换/ViewPager 切换闪烁（重复 dispatch 当前页、平滑滑动后淡入闪烁）
13. 修复监听回调内增删监听导致的并发修改崩溃（遍历前复制列表）
14. 修复 Profile/版本监听累积泄漏（新增 Profiles.unregisterVersionsListener），切换 profile 时取消旧加载
15. 修复控制器列表项 inflate 时提前 attachToRoot 的问题，控制器截图按原始分辨率加载避免预览模糊
16. 修复 SmoothFont 选择字体后重启字体缩放错误
17. 修复 VersionInstallInfoPage 选择安装器版本后条目未刷新
18. 修复 Files 目录流未关闭导致的资源泄漏，LWJGL 版本路径补空值防护

#### 🔧 其他

1. 更新多语言翻译（德、波斯、葡、俄、乌、越、繁中等）
2. 新增重构内容的 androidTest 仪器测试

### English

#### ✨ New Features

1. **Loading info and progress bar on the Splash screen**: SplashActivity now uses viewBinding and shows loading status with progress feedback
2. **Settings tab collapses/expands with content scroll**: AppBarLayout effect — the tab hides while scrolling and reappears when scrolling up
3. **Usage descriptions below setting items**: Explanatory text added under settings for easier understanding
4. **Transition animation for download page tab switching**: Tab and mode page content updates fade in
5. **Right-side menu gesture interaction**: Two-finger swipe switching and gesture-based show/hide, with the skin preview position fixed when hidden
6. **LWJGL GLFW glfwGetWindowPos stub added**: Window position is always 0,0

#### ⚡ Improvements

1. **Main UI architecture refactored (ViewPager2)**: UIManager now hosts the 8 UI pages with ViewPager2; pages are destroyed on recycle without keeping state; page transitions unified to fade-in & slide-up animations; swipe gesture disabled on the main UI (switching only via the menu) to avoid conflicts with in-page vertical scrolling
2. **Download page fully optimized**: The 5 download modes share a single layout instance with tab switching state restored via ViewModel; lighter list layouts (linear arrangement, no marquee); icons decoded at a 90x90 limit and image loading paused while scrolling fast, eliminating GC hitches from full-size decoding; Mod translation data pre-warmed in the background to avoid main-thread parsing on first bind
3. **Settings/version settings pages refactored to RecyclerView row-level reuse**
4. **Profile/VersionSetting/Theme completed the fakefx migration**: Profile refactored to Kotlin without fakefx, Profiles.selectedProfile migrated to a Repository singleton + StateFlow, selectedVersion migrated to StateFlow; VersionSetting uses plain fields; Theme/ThemeEngine refactored to Kotlin + StateFlow with theme storage migrated from SharedPreferences to DataStore
5. **Module merge and code cleanup**: FCLauncher and FCLCore modules merged into FCL; FCLPath.CONTEXT removed, global Context now uses FCLApp.getAppContext()/getActivity(); AndroidUtils moved to com.mio.util top-level functions; getLocalizedText static keys migrated to R.string; dead code like DisplayAnimUtils removed
6. **Version list loading speedup**: Parsing and resource caching, cache kept when switching profiles; mod count statistics moved into the loading flow to avoid main-thread directory IO while scrolling
7. **Download page search box optimization**: imeOptions combined with flagNoFullscreen to eliminate AutofillManager log spam
8. **UI detail polish**: Unified 8dp corner radius, top-rounded page tab backgrounds, left menu click selection animation, lower content slides up when returning from a temp page
9. **libcc.so updated**: Direction controls unified to the ZL2 joystick with sizeType fixed
10. **LWJGL 3.3.3/3.4.1 merged artifacts updated**: Including the glfwGetWindowPos stub; removed the runtime jsr305.jar to resolve the package conflict with java.annotation

#### 🐛 Bug Fixes

1. Fixed the version reverting after exiting the launcher (selectedVersion change not triggering config save)
2. Fixed concurrent crashes in the version list, config save spam and an NPE on empty version settings
3. Fixed a main-thread ANR caused by a full mod rescan when navigating back and forth in the mod management page
4. Fixed light/dark mode switching not taking effect; theme switching no longer depends on Activity recreation
5. Fixed a crash when theme refresh callbacks load after the main UI is destroyed; theme callbacks now use weak references to fix view leaks after page recycle
6. Fixed first-frame white flash on controls when swiping/switching pages quickly (registerEvent now runs synchronously)
7. Fixed an offline account cape loading NPE
8. Fixed version icons on the main UI being enlarged (shared Drawable instances polluting each other's bounds)
9. Fixed the blank mode list on the first switch after the download page rebuilds (LayoutManager re-set when reusing the cached adapter)
10. Fixed GC hitches from full-size icon decoding in the download list, layout reflow triggered by image load completion and slide-in animation replaying on mode switch
11. Fixed temp pages covering the new page when switching tabs in the download page and transparent backgrounds showing through with nested temp pages
12. Fixed page/viewpager transition flicker (duplicate dispatch of the current page, fade-in flicker after smooth sliding)
13. Fixed a ConcurrentModificationException from adding/removing listeners inside callbacks (list copied before iteration)
14. Fixed accumulating Profile/version listener leaks (Profiles.unregisterVersionsListener added) and cancelled stale loads when switching profiles
15. Fixed controller list items being attachToRoot too early during inflation; controller screenshots now load at original resolution to avoid blurry previews
16. Fixed SmoothFont font scaling errors after restarting when a font is selected
17. Fixed VersionInstallInfoPage items not refreshing after selecting an installer version
18. Fixed resource leaks from unclosed Files directory streams; added null protection for LWJGL version paths

#### 🔧 Other

1. Updated multilingual translations (German, Persian, Portuguese, Russian, Ukrainian, Vietnamese, Traditional Chinese, etc.)
2. Added androidTest instrumentation tests for the refactoring

## [1.3.2.7] - 2026-08-19

### 中文

#### ✨ 新功能

1. **接入 Weblate 本地化翻译**：接入 Weblate 平台，翻译协同更新更高效

#### ⚡ 优化

1. **FCLLibrary 模块整体并入 FCL 模块**：源码/资源/Manifest 合并，减少模块依赖与构建复杂度
2. **布局加载改为同步**：移除 AsyncLayoutInflater，布局统一同步加载，避免异步加载带来的布局问题
3. **移除 UIListener 回调接口与 UIManager.init 回调参数**：简化 UI 生命周期管理代码

#### 🐛 修复

1. 修复损坏的压缩文件抛 ZipError 导致的崩溃，避免整合包安装失败
2. 修复删除账户弹窗内容文字异常（#1752）
3. 修复 GLFW 上下文版本探测问题：仅当驱动版本更高时才覆盖上下文版本，放宽 minor 下界并统一上界
4. 更新 LWJGL 3.3.3/3.4.1 合并产物，修复使用部分模组后启动游戏画面无法显示的问题
5. 修复世界列表条目上下间距过大的问题

### English

#### ✨ New Features

1. **Integrated Weblate localization**: Connected the project to Weblate for more efficient collaborative translation updates

#### ⚡ Improvements

1. **Merged the FCLLibrary module into FCL**: Sources, resources and manifest are unified to reduce module dependencies and build complexity
2. **Synchronous layout inflation**: Removed AsyncLayoutInflater so layouts are always inflated synchronously, avoiding layout issues caused by async inflation
3. **Removed the UIListener callback interface and UIManager.init callback parameter**: Simplified UI lifecycle management code

#### 🐛 Bug Fixes

1. Fixed a crash caused by corrupted archives throwing ZipError, preventing modpack installation failures
2. Fixed abnormal text in the account deletion dialog (#1752)
3. Fixed GLFW context version detection: only override the context version when the driver version is higher, relaxed the minor lower bound and unified the upper bound
4. Updated LWJGL 3.3.3/3.4.1 merged artifacts, fixed the game screen not displaying after using certain mods
5. Fixed excessive vertical spacing between world list items

## [1.3.2.6] - 2026-08-14

### 中文

#### ✨ 新功能

1. **损坏的模组文件支持确认删除**：模组文件损坏时弹窗提示，支持确认后删除，避免手动去文件管理器清理

#### ⚡ 优化

1. **模组列表增量加载优化**：扫描过程中边扫描边分批显示，筛选时不再重新扫描，模组多时列表响应更快
2. **账号皮肤上传改为协程实现**：不再使用 Task 异步系统，逻辑更简洁清晰
3. **移除 NG-GL4ES 子模块**：渲染器改用独立项目（NG-GL4ES）的预构建产物，不再以子模块形式依赖源码

#### 🐛 修复

1. 修复部分设备 F3 页面 CPU 显示为 Unknown 的问题
2. 修复部分渲染错误（#1747）
3. 修复 GLFW 窗口上下文创建失败时导致的崩溃
4. 修复 LWJGL freetype 库从打包 natives 目录加载的问题
5. 损坏的模组文件跳过并提示，避免整个模组列表加载失败

### English

#### ✨ New Features

1. **Confirmation dialog for corrupted mod files**: Corrupted mods now show a prompt with the option to delete them, so users no longer need to clean up files manually

#### ⚡ Improvements

1. **Incremental mod list loading**: Mods are now scanned and displayed in batches, and filtering no longer rescans the whole list, making large mod lists much more responsive
2. **Account skin upload reimplemented with coroutines**: Replaced the Task async system with coroutines for cleaner logic
3. **Removed the NG-GL4ES submodule**: The renderer now uses prebuilt artifacts from the standalone NG-GL4ES project instead of vendoring its source

#### 🐛 Bug Fixes

1. Fixed F3 page CPU showing "Unknown" on some devices
2. Fixed some rendering errors (#1747)
3. Fixed a crash when GLFW window context creation fails
4. Fixed LWJGL freetype library loading from the packaged natives directory
5. Corrupted mod files are now skipped with a prompt instead of failing the entire mod list
## [1.3.2.5] - 2026-08-12

### 中文

#### ✨ 新功能

1. **支持 LWJGL 3.3.3 / 3.4.1 双版本**：彻底移除旧 LWJGL-Pojav 模块，改为基于官方 LWJGL 合并 Android 源码的构建方式，两个版本按需切换；新增 3.4.1 Vulkan 模块支持
2. **GLFW 桥延迟初始化**：GLFW 初始化推迟到真正需要时进行，并补充了 3.4.1 版本缺失的 API
3. **删除仿基岩触控相关功能**：移除历史遗留的仿基岩触控支持，简化代码与界面

#### ⚡ 优化

1. **模组列表远程信息加载优化**：远程信息缓存复用 + 可见条目优先加载，浏览模组列表更流畅
2. **FCLConfig 迁移 Kotlin**：配置类迁移至 Kotlin 并接入 LWJGL natives 路径管理
3. **mod 元数据读取迁移**：从 ZipFileTree 迁移到 zipfs，统一走混合编码兼容的压缩文件系统
4. **jre_launcher 日志改进**：改用 FCL_LOG 统一日志输出

#### 🐛 修复

1. 修复游戏设置页面中游戏参数与 Java 虚拟机参数无法通过长按设置为空的问题（#1728）
2. 修复混合编码 zip 解压时条目名解码错误导致的 NoSuchFileException
3. 修复 LWJGL 构建流程问题、IDE 中 LWJGL 模块类意外报错的问题
4. 同步 GLCapabilities 与 Amethyst 实现，同步 JNI 与新版 LWJGL 接口
5. LWJGL natives 按架构过滤支持命令行 `-Darch` 覆盖
6. 完善 LWJGL 双版本类路径与库过滤逻辑
7. ProcessService 对空 command 增加兜底处理
8. 同步 Amethyst JNI 修复并重构窗口尺寸事件上报
9. 修复 freetype 库名引用，改为相对名
10. 更新 Java 25 网盘下载链接

#### 🔧 其他

1. 原生代码布局向 Amethyst-Android 对齐：JVM 钩子提取到 `jvm_hooks/` 目录、`awt_bridge.c` 移至 jni 根目录、linkerhook 重写为 C 并合并驱动加载到 egl_bridge
2. 更新 MioLibPatcher、Java 25、vulkan 3.4.1 模块等运行时组件
3. 更新多语言字符串资源

### English

#### ✨ New Features

1. **Dual LWJGL support (3.3.3 / 3.4.1)**: Removed the old LWJGL-Pojav module entirely and switched to a build based on official LWJGL merged with Android sources; both versions can be used on demand, with new Vulkan module support for 3.4.1
2. **Delayed GLFW bridge initialization**: GLFW is now initialized lazily when actually needed, with missing 3.4.1 APIs added
3. **Removed bedrock-style touch controls**: Dropped the legacy bedrock-style touch control feature to simplify the codebase and UI

#### ⚡ Improvements

1. **Mod list remote info loading optimized**: Remote info is now cached and reused, with visible items loaded first for smoother browsing
2. **FCLConfig migrated to Kotlin**: Configuration class migrated to Kotlin and integrated with LWJGL natives path management
3. **Mod metadata reading migrated**: Moved from ZipFileTree to zipfs, unified on a mixed-encoding-compatible zip filesystem
4. **jre_launcher logging improved**: Switched to unified FCL_LOG output

#### 🐛 Bug Fixes

1. Fixed game arguments and Java VM arguments being unable to be cleared via long-press on the game settings page (#1728)
2. Fixed NoSuchFileException caused by entry name decoding errors when extracting mixed-encoding zips
3. Fixed LWJGL build pipeline issues and unexpected IDE errors from LWJGL module classes
4. Synced GLCapabilities with the Amethyst implementation and synced JNI with the new LWJGL interfaces
5. LWJGL natives per-architecture filtering now honors the `-Darch` command-line override
6. Improved dual-version LWJGL classpath and library filtering
7. Added fallback handling for empty commands in ProcessService
8. Synced Amethyst JNI fixes and reworked window size event reporting
9. Fixed freetype library name reference to use a relative name
10. Updated Java 25 netdisk download links

#### 🔧 Other

1. Native code layout aligned with Amethyst-Android: JVM hooks moved to `jvm_hooks/`, `awt_bridge.c` moved to the jni root, linkerhook rewritten in C with driver loading merged into egl_bridge
2. Updated runtime components: MioLibPatcher, Java 25, Vulkan 3.4.1 module
3. Updated multilingual string resources

