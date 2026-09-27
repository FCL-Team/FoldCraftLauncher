# fcl：ui 包（主界面 8 页面）

范围：`FCL/src/main/java/com/tungsten/fcl/ui/`，107 文件约 20900 行。背景：UIManager（`FCL/ui/UIManager.kt`）用 ViewPager2 管理页面；每个 UI 继承 FCLCommonUI（fcllibrary 包），FCLMultiPageUI 提供分页与临时页栈；页面实例随 ViewPager 创建/销毁、不保留状态。

## 一、总体架构

**UIManager** 是主界面骨架：一个垂直方向的 ViewPager2（`ORIENTATION_VERTICAL`、`isUserInputEnabled=false` 禁滑、`isSaveEnabled=false` 不恢复位置、`offscreenPageLimit=DEFAULT` 不预加载），内部 `UIAdapter` 是 RecyclerView.Adapter，8 个位置对应 8 个主 UI 工厂（MainUI→VersionUI，布局 ui_main…ui_version）。核心机制：

- `uiRegistry[8]` 缓存 UI 实例；`getUI(position)` 惰性创建并 `onCreate()`；`onViewRecycled` 时 `destroyUI(position)` 置空 —— **UI 实例随 ViewPager 创建/销毁、不保留状态**。
- `onBindViewHolder` 里先把 contentView 从旧 parent remove（防御 GapWorker 预取导致 "child already has a parent"）。
- 页面切换动画统一在 `onPageSelected`：淡入 + 上移 30dp、250ms；用 `lastSelectedPosition` 过滤 ViewPager2 因软键盘等布局变化重复 dispatch 当前页的情况；`setCurrentItem(position,false)` 瞬时跳转避免途经中间页逐个创建重 UI。
- `switchUI()` 供跨页跳转；`currentUI` 由 MainActivity 回调同步菜单高亮；`mainUI/manageUI/…` 等 8 个属性便捷访问器（内部 getUI 触发惰性创建）。
- 伴生对象 `UIManager.instance` 是全局静态入口，几乎所有页面/对话框通过它互相访问。

**FCLCommonUI**（fcllibrary）承载内容容器；**FCLMultiPageUI** 提供 `setupPages(container, tabLayout)`、`getPage(position)`、`showPage(pos)`、`showTempPage/dismissCurrentTempPage/dismissAllTempPages` 临时页栈与 `canReturn()`。各 UI 覆写 `createPage(position)` 与 `getPageCount()`。页面 id 常量统一为 150xx 段。ui/ 根下还有通用组件：`TaskDialog`（进度+速度+安装日志+任务列表，dismiss 时解除全部监听防泄漏）、`TaskListPane`（StageNode/ProgressListNode 两级列表，按 stage key 本地化安装阶段）、`InstallerItem`（安装器条目模型+皮肤，`InstallerItemGroup` 内建互斥不兼容矩阵与按游戏版本筛选列表）。

## 二、各 UI 页面树

### 1. MainUI（无分页，FCLCommonUI 直挂）
- 布局：公告卡（announcement_container/layout/title/date/hide）+ 皮肤预览 `SkinViewer`+`SkinRenderer`（5f 缩放）。**皮肤预览**：`SkinTextureLoader.load(Accounts.getSelectedAccount())`；选中账户变化经 `selectedAccountProperty` InvalidationListener 重载；用 `addOnAttachStateChangeListener` 替代 onStart/onStop（attach 时恢复渲染+重注册监听，detach 时暂停+release）；双击模型弹 `AnimationDialog`（动画/实心层/上半身分离），设置经 `saveSkinSettings` 持久化；主题 `isCloseSkinModel` 为 true 时隐藏。
- **启动入口不在 MainUI**，在 `Versions.launch()`。MainUI 只负责公告（`Announcement.java` v2：id/significant/outdated/min-max version/specificLang 多语言，`shouldDisplay` 用 SharedPreferences "launcher"/"ignore_announcement" 比较 id 决定显示，hide 写回 id；中文环境走 Gitee 镜像 URL）与 `refreshSkin(account)` 公开方法（AccountListItem 刷新皮肤后回调）。

### 2. VersionUI（FCLMultiPageUI，1 页）
- `PAGE_ID_VERSION_LIST=15020` → **VersionListPage**：搜索框（本地过滤）、TabLayout 分类（全部/Fabric/Forge/NeoForge/其他，按 libraries 字符串过滤）、Profile 侧栏（ProfileListAdapter：切换 profile 需 `!MainActivity.isVersionLoading()`，否则抖动动画；删除有保底）、刷新按钮（`VersionCache.invalidate` + `refreshVersionsAsync`）、新建 Profile（AddProfileDialog，路径用 fileLauncher 选目录）。
- 数据流：`registerVersionsListener`（attach 恢复/detach 注销）+ `Profiles.selectedProfile` StateFlow collect（挂在 activity.lifecycleScope）；`loadVersions` 先取 `VersionCache` 会话级快照立即渲染、后台 `VersionCache.refresh` 后 `sameEntries` 比对去重重绘；排序按 `GameVersionNumber` 降序；loadJob 可取消防过期结果；选中版本高亮走 `profile.addSelectedVersionListener`。条目 `VersionListItem`（selected BooleanProperty）由 **VersionListAdapter** 渲染：radio 选中即 `profile.selectedVersion=…`；非 usesGlobal 版本显示 setting 按钮 → 跳 ManageUI tab 0；delete → `Versions.deleteVersion`。
- **Versions.java** 是版本操作的静态门面：importModpack（跳 DownloadUI 临时页）、downloadModpackImpl、delete/rename/duplicate/export/update/updateGameAssets/clean、**launch()**。

### 3. ManageUI（FCLMultiPageUI + TabLayout，7 页）
- 页 id：15000~15006。position 0=**VersionSettingPage**(SETTING)、1=**ManagePage**(MANAGE)、2=**InstallerListPage**(INSTALL)、3=**ModListPage**(MOD)、4=**WorldListPage**(WORLD)、5=**ResourcePackListPage**、6=**ShaderPackListPage**。核心接口 `ManageUI.VersionLoadable { loadVersion(profile, version) }`。
- 版本分发：`setVersion/loadVersion` 遍历 `forEachCreatedPage` 分发；`RefreshedVersionsEvent`（EventBus，HIGHEST 优先级）回调 `checkSelectedVersion`（版本没了则回退 preferredVersionName 或跳主页）；Profile 切换监听（Profiles 自带 Runnable listener 列表 + attach/detach 注册注销）；`onRunDirectoryChange`（隔离目录开关变化）只刷新 3~6 页。
- **VersionSettingPage**（全局/特定版本复用，`globalSetting` 标志）：设置行由 **VersionSettingAdapter** 用 sealed Row（Switch/Value/Edit/Memory/Icon）+ `VersionSettingTag` 枚举渲染，`SettingGroup` 分组 + SpacingItemDecoration 做圆角连块；`loadVersion` 切换 `settingsChangeListener`（isolateGameDir 变化触发 onRunDirectoryChange；usesGlobal 变化重刷列表）、校验 GPU 驱动（非法回退 Turnip）；按钮分发到 JavaManageDialog/SelectControllerDialog/RendererSelectDialog/驱动与环境变量编辑（env/force_resolution 存 SharedPreferences "launcher"）；内存行手写 `FCLGameRepository.getAllocatedMemory` 换算。**坑**：usesGlobal 每次输入触发全量重建导致输入框失焦——已用条件跳过修复。
- **ManagePage**：左右两列 `ManageItemAdapter`（数据类 ManageItem）：上传日志/浏览 log/game/mods/config/resourcepacks/shaderpacks/screenshots/saves 目录（FileBrowser）；右侧版本操作（更新=updateVersion→ModpackSelectionPage、重命名、复制、导出、重下 assets 索引、删 libraries、清 logs），全部转调 Versions。
- **InstallerListPage(manage)**：`LibraryAnalyzer.analyze` 后用 InstallerItemGroup 逐组件显示已装版本/可升级/可卸载；action 打开 `download.version.InstallerListPage` 临时页选版本 → `finish()` 组装 installLibraryAsync 任务（先 remove 后 install，失败不破坏现版本）；`installOffline` 支持本地 jar 安装。
- **ModListPage**：`modded` 属性（无 modloader 显示警告）；增量加载（每 16 个一批 notify，`BATCH_SIZE`）、同版本重复 loadVersion 直接跳过（防 ANR）；多选模式（selectAll/invert/delete/checkUpdates）；搜索支持 `regex:` 前缀；损坏模组弹窗删除；`checkUpdates` → **ModCheckUpdatesTask**（每模组 × 每平台并行任务，取日期最新 candidate）→ **ModUpdatesPage**（临时页，ModUpdateListAdapter 勾选、CSV 导出、ModUpdateTask 批量下载含失败回滚 setOld/disable）→ 完成回 `modListPage.refresh()`。`download()` 跳 DownloadUI 的 MOD 页。**LocalModListAdapter** 的亮点：远程信息查询挂在 **onViewAttachedToWindow**（而非 onBindViewHolder，绕过 RecyclerView view cache 不重绑导致防抖后永久丢查询），delay 200ms 防抖、按 fileName 记 Job、detach 取消；列表变更监听区分 wasReplaced（全量重置）与 wasAdded/Removed（增量通知）；查到 RemoteMod 后 Glide 加载图标、标题加 [已安装]/中文译名、显示 jump 按钮跳详情。
- **WorldListPage / WorldListAdapter / WorldListItem / WorldInfoPage / WorldExportDialog**：世界扫描按游戏版本过滤（showAll 开关双向绑定）；fixPrivate 修复私有目录权限（unix:mode=1535）；条目操作（信息/数据包/导出/复制/删除）在 WorldListItem；WorldInfoPage 直接改 NBT（openNBT）并 `writeWorldData()`，处理 1.16/25w07a/26.1 等多代 tag 兼容；DatapackListPage/Adapter 管理世界数据包（临时页）。
- **ResourcePack/ShaderPackListPage(+Adapter)**：结构相同（IO 层来自 com.mio.minecraft 扩展函数），启用/禁用写 options.txt（resourcepacks 独有，失败回滚 checkbox）、重命名（长按）、信息、多选删除、下载按钮跳 DownloadUI 对应 tab、导入重名自动追加序号。
- **整合包导出三连**：ModpackTypeSelectionPage（mcbbs/multimc/server/curseforge/modrinth）→ ModpackInfoPage（按各类型 Options 显隐字段、双向绑定、校验）→ ModpackFileSelectionPage（文件树勾选白名单，后台建树、ModAdviser 建议）→ 对应 ExportTask + TaskDialog。

### 4. DownloadUI（FCLCommonUI 但自建三层内容，非分页复用）
- 页 id 15010~15016。**结构独特**：contentContainer 里常驻三个视图——`VersionInstallPage`(GAME)、共享的 **DownloadPage**（5 个下载模式共用一个实例）、`FavoritePage`；临时页用自维护 overlay + `tempPageStack`（不同于 FCLMultiPageUI 栈），带 200ms 淡入淡出/下滑返回过渡；TabLayout 7 个 tab（游戏/整合包/模组/资源包/世界/光影/收藏），切换时 `dismissAllTempPages()` + 可见性切换 + `downloadPage.switchType(pageId)`。attach 监听：重新可见时刷新打开中的 RemoteModInfoPage 推荐版本（目录/版本可能在别处被切换）。还有 TabLayout 宽度变化自愈（scrollSelectedTabIntoView）。
- **DownloadPage（聚合搜索核心）**：`switchType` 按 pageId 建仓库（世界固定 CurseForge，其余 LocalizedRepository 双源）+ 下载回调（动态取当前 profile/版本，避免切目录后下到旧目录）+ 恢复 `DownloadSearchViewModel.State`（挂 Activity ViewModelStore，按 pageId 保存搜索词/版本/分类/排序/页码/源/结果/adapter——切回不重搜、不重载图）。**聚合搜索** `searchAggregated`：两源 CompletableFuture 并行 + 交错合并（CF 在前）+ 跨平台去重（归一化 slug/title 一致且更新时间差 ≤7 天才隐藏）+ 单源失败降级提示。分类用统一静态表 **DownloadCategories**（"cfId/mrSlug" 标记，源自 PCL xaml）；世界模式动态拉 CF 分类树。分页/跳页/重试、翻译搜索（TranslationDialog，中文名→英文名回填搜索）、一键下载 `downloadWithDependencies`（ModDependenciesResolver 解析 REQUIRED 前置闭包、已安装去重）、批量 `downloadModsBatch`（解析任务本身进下载面板带进度）。
- **临时页链**：RemoteModListAdapter 条目 → **RemoteModInfoPage**（版本按游戏版本分组 SimpleMultimap、推荐版本按 LibraryAnalyzer 的 mcv+loader、已安装检测、收藏星形、mcmod 链接、截图 RecyclerView）→ **RemoteModVersionPage**（版本列表；整合包模式直接下载，模组模式进 **RemoteModDownloadPage**）→ RemoteModDownloadPage（依赖分组列表、单独下载/saveAs/一键下载/返回按钮一次 onBackPressed×3）。
- **收藏体系**（com.mio.data.FavoriteManager，Room+StateFlow）：**FavoritePage** 左侧类别×分组双维筛选（自绘 FilterOptionRow 胶囊行+计数徽标）、多选批量改分组/取消收藏、一键批量下载（解析匹配当前 mcv/loader → FavoriteBatchDialog 勾选 → downloadModsBatch）；**FavoriteAdapter** DiffUtil 差量、左滑菜单（SwipeMenuLayout 互斥）；**FavoriteActions.kt** `bindFavoriteIcon/handleFavoriteClick`（未收藏先弹 GroupSelectionDialog 选分组）；GroupManageDialog 增删改分组。注意 FavoritePage 注释的坑：**FCLPage 超类构造期间回调 onCreate，属性初始化器会在 onCreate 赋值后再执行覆盖，因此字段必须 lateinit**。
- **version 安装**：**VersionInstallPage**（release/snapshot/old/aprilFools 勾选过滤+搜索+刷新）→ **VersionInstallInfoPage**（InstallerItemGroup 组件勾选；版本名自动生成"1.20.1-Fabric"等，手动改后停止自动；选 Fabric/Quilt 自动带最新 API；GameBuilder.buildAsync 安装，成功后 mkdir mods；`alertFailureMessage` 静态方法集中翻译各类安装异常，被 manage 包复用）→ **InstallerListPage(download)**（某组件版本列表，release/snapshot/old 过滤、wiki 链接、保存 URL）。
- **modpack 安装**：`Versions.importModpack` → **ModpackSelectionPage**（本地文件/远程 URL）→ **LocalModpackPage**（探测编码→读 manifest；手工包特殊警告+installAsVersion=false 走 ModpackInstaller.getModpackInstallTask(profile,file,name,charset)；describe 显 HTML 描述）或 **RemoteModpackPage**（server-manifest.json）；**ModpackInstaller.installModpack** 统一 TaskDialog + ModpackCompletionException/FileNotFound 特判；updateVersion != null 时走 ModpackHelper.getUpdateTask 并显示在 ManageUI 临时页（版本更新流程）。

### 5. AccountUI（FCLCommonUI 无分页）
- 三入口：离线/微软（CreateAccountDialog 传工厂）、外置登录服务器（AddAuthlibInjectorServerDialog 两步向导：locateServer 联网校验 → 加入 config().authlibInjectorServers）。ServerListAdapter 展示服务器列表（点击即以其工厂开登录框）。
- **CreateAccountDialog**：Details 策略接口（Offline/Microsoft/External 三实现，getter 抛 IllegalStateException 统一提示）；微软登录长按改外部浏览器；设备码事件经 `Accounts.OAUTH_CALLBACK`（onGrantDeviceCode 复制 userCode / onOpenBrowser 打开内置 WebView 或外链 / onLoginFinished 通知内嵌登录页关闭）——强引用 register + dismiss/release 注销；登录 Task 后台跑 `factory.create(selector,…)`，**多角色经 DialogCharacterSelector（CountDownLatch 阻塞后台线程选角色）**；成功 `Accounts.addAccount+setSelectedAccount+accountUI.refresh()`。
- **AccountListAdapter/AccountListItem**：头像/标题/副标题全 fakefx 绑定（TexturesLoader.avatarBinding）；刷新按钮（微软注入 LoginStageTextBinder 实时显示登录阶段，CredentialExpiredException 回退密码登录）；皮肤上传（离线 → OfflineAccountSkinDialog 本地皮肤/披风+模型选择+3D 预览，确认才落位 SKIN_DIR；微软 → MicrosoftAccountSkinDialog 上传/重置/披风列表激活隐藏+预览；Yggdrasil 直接 uploadSkin）；离线账户可改 UUID（EditDialog）；离线删除连带清理本地皮肤文件；本地皮肤长按选择。`AccountListItem.logIn` companion：Classic/OAuth 账户在后台线程用 CountDownLatch 阻塞等对话框结果（启动前凭证过期重登流程）。ClassicAccountLoginDialog 目前是**空壳**（只 super(context)）。

### 6. SettingUI（FCLMultiPageUI + TabLayout，4 页）
- 页 id 15030~15034。0=VersionSettingPage(global=true)、1=**LauncherSettingPage**、2=**PluginManagePage**、3=**AboutPage**。onPageCreated 对 VersionSettingPage 注入 `loadVersion(profile,null)`；onResume 联动 PluginManagePage.onHostResume（系统卸载返回后重扫插件）。
- **LauncherSettingPage/LauncherSettingAdapter**：与版本设置同款 Row+Tag 机制（LauncherSettingTag：CHECK_UPDATE/SHOW_CHANGELOG/EXPORT_LOG/权限/四组主题色 set/fetch/reset、亮暗背景、视频背景+音量、光标/菜单图标、语言/主题模式 Spinner、下载源/线程、动画速度/色彩透明度/振动等）；语言切换 `getActivity().recreate()`；亮暗切换因 configChanges 含 uiMode 需手动 `ThemeEngine.refreshTheme()`；从背景取色用 Palette（muted/vibrant）；SeekBar 行复用时**先摘监听再 setMax/progress**（钳制回调会把旧行 tag 的值写进错误设置项）。
- DocIndex/DocCategoryAdapter/ArticleAdapter 是帮助文档选择器组件（ArticleAdapter 打开 fcl-team.github.io 文档页），当前 SettingUI 未挂载第 5 页，属遗留代码。

### 7. ControllerUI（FCLMultiPageUI 无 tab，2 页）
- 0=**ControllerManagePage**(15040)：本地布局列表（EditableControllerListAdapter）+右侧信息；导入（.json，**自动识别 ZL2 布局并经 LayoutConverter 转换**，全程后台+ProgressDialog）；新建/编辑信息（ControllerInfoDialog，作者变化则重新生成随机 id）；编辑（跳 ControllerActivity 传 controller id）；分享（原格式或转 ZL2，FileProvider）；上传（ControllerUploadPage：打包 index.json+version.json+截图+图标+布局 zip 后系统分享，另有加 QQ 群按钮）。
- 1=**ControllerRepoPage**(15041)：布局仓库（GitHub/国内镜像双源 index.json+category.json，语言/设备/分类筛选，regex: 搜索）；checkUpdate 对比 versionCode；ControllerDownloadPage 详情（Glide 截图、历史版本 OldVersionDialog）；下载逻辑两处相同：**下载前先备份旧文件到 cache、移除旧 Controller，失败回滚**。onBackPressed：临时页→仓库页→退出。

### 8. MultiplayerUI（FCLCommonUI 无分页，Terracotta）
- 纯说明/开关页：三段教程切换（主/房主/客人）、EasyTier 链接、反馈按钮隐藏（TODO 注释）。`enable_multiplayer` 开关写 SharedPreferences "third_party"/"terracotta"，首次开启需确认 `terracotta_user_notice` 版本（Terracotta.getUserNoticeVersion），并申请通知权限；导出 terracotta.log。

## 三、关键交互流程

**启动流程**：任意入口调 `Versions.launch(context,profile[,id])` → checkVersionForLaunching（无版本弹窗引导跳下载页）→ ensureSelectedAccount（无账户 Toast 后 `switchUI(getAccountUI())` 中止）→ new LauncherHelper(context,profile,account,id).launch()（LauncherHelper 内部触发账户登录，若凭证过期会走 AccountListItem.logIn 的阻塞对话框路径）。

**版本安装流程**：下载页 GAME tab → VersionInstallPage 选版本 → VersionInstallInfoPage（可叠加 loader/API/OptiFine，自动版本名+自动 API）→ install → GameBuilder.buildAsync → TaskDialog 展示 TaskListPane 阶段进度（stage key "fcl.install.xxx:version"）→ 成功后 `profile.setSelectedVersion(name)` + refreshVersions（RefreshedVersionsEvent 触发 VersionListPage 重载与 ManageUI 校验）。

**账户添加流程**：AccountUI 入口 → CreateAccountDialog(factory) → factory.create（微软经 OAuthServer 事件流；外置登录经 DialogCharacterSelector）→ addAccount+setSelectedAccount → accountUI.refresh()；同时 MainUI 的 accountListener 自动换皮肤。

**下载流程**：DownloadUI tab → DownloadPage.switchType（仓库/回调/状态恢复）→ search（聚合或单源）→ RemoteModListAdapter → RemoteModInfoPage → RemoteModVersionPage →（模组）RemoteModDownloadPage → download callback → DownloadAddonDialog 命名 → FileDownloadTask + **DownloadManager.submit**（统一下载面板）。

## 四、跨页面通信方式盘点

1. **单例直达**：`UIManager.instance.xxxUI` / `MainActivity.getInstance()`（binding、fileLauncher、lifecycleScope、uiManager）——最主要的耦合方式，页面被回收重建不影响调用（getUI 会重建）。
2. **fakefx 属性/绑定**：Accounts.selectedAccountProperty、InstallerItem 的 StringProperty/BooleanProperty + Bindings、VersionListItem.selectedProperty、Task 的 progressProperty/messageProperty（TaskListPane/TaskDialog 绑定）。
3. **EventBus**（fclcore.event）：仅见 `RefreshedVersionsEvent`（ManageUI 弱引用注册 + WeakListenerHolder）与 ModpackInfoPage 等的 onVersionIconChanged.fireEvent。
4. **StateFlow**（Kotlin）：`Profiles.selectedProfile`（VersionListPage/VersionSettingPage collect，ManageUI 用自己的 Runnable listener 列表），`FavoriteManager.favorites/groups`（收藏页/下载列表）。
5. **静态回调列表**：`Profiles.addSelectedProfileListener`、`Controllers.addCallback`、`Profiles.registerVersionsListener`、`Accounts.OAUTH_CALLBACK` 事件通道。
6. **SharedPreferences**："launcher"（公告 id、force_resolution、env、autoExitLauncher、themeMode、vibration…）、"third_party"（terracotta）。
7. **显式注册/注销纪律**：所有挂在单例上的监听一律 attach 注册/detach 注销（ManageUI、VersionListPage、MainUI、DownloadUI 均如此），多处注释明确说明防页面销毁后仍被回调。

## 五、值得注意的实现细节与坑

- **TaskDialog/TaskListPane 泄漏防护**：executor 生命周期长于对话框，dismiss 时统一 removeTaskListener、解绑 messageProperty、taskListPane.release() 解绑进度条与 View 树。
- **DownloadPage 恢复 adapter 时 RecyclerView 是新视图**（DownloadUI 被 ViewPager 回收重建），必须补 LayoutManager 否则列表不渲染；restore 后要 refreshInstalledState。
- **RemoteModListAdapter 占位图**用固定 90×90 位图（与 override 尺寸一致）避免 Glide 加载完成触发 requestLayout 重排、重置其他条目的跑马灯；收藏/已安装用 payload 局部刷新防闪烁。
- **LocalModListAdapter 远程查询挂 attach 而非 bind**（RecyclerView 缓存视图重显不重绑会永久丢查询机会），是很有代表性的坑位注释。
- **FavoritePage 的 Kotlin 构造顺序坑**：FCLPage 超类构造期间回调 onCreate，字段初始化器在 onCreate 之后执行会覆盖赋值 → 必须 lateinit。
- **VersionSettingPage/LauncherSettingPage 的行复用监听陷阱**：setMax 钳制触发 onProgressChanged、TextWatcher 累积、ThemeEngine.registerEvent 同 view 覆盖旧回调——代码中均有针对性处理（先置 null 监听、Holder 缓存 watcher、先 unregister）。
- **ManageUI.onPageCreated 未 setVersion 时跳过分发**，靠 RefreshedVersionsEvent 兜底校验；isShowing() 用于区分"当前页"与"后台页"的行为差异（如 ModUpdatesPage 回调）。
- **ModListPage.loadVersion 同版本短路**防上百模组重复 zip 解析 ANR；InstallerItem 互斥矩阵保证组件冲突即时反映到 UI。
- **AccountUI 的 ClassicAccountLoginDialog 是空实现**、MultiplayerUI 反馈按钮带 TODO 隐藏、SettingUI 的 Doc 相关三个类未被引用——疑似遗留代码，后续维护可关注。
- DownloadUI 与统一页栈并存两套临时页栈（自身 overlay 栈 vs FCLMultiPageUI 栈），`Versions.importModpack` 等跨 UI 跳转时依赖 `dismissCurrentTempPage` 在对应 UI 上正确弹栈，调用点均按 updateVersion==null 区分 Download/Manage 两侧。
