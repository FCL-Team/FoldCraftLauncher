# fclcore：auth 与 download 包

范围：`FCL/src/main/java/com/tungsten/fclcore/` 下的 `auth/`（47 文件约 5000 行）与 `download/`（85 文件约 10000 行）。curse/modrinth 的仓库实现位于 mod 包，本文仅做交叉引用。

## 一、auth 包

### 1. 类清单与继承体系

根抽象类 `auth/Account.java`（实现 fakefx `Observable`）：抽象方法 `getUsername/getCharacter/getUUID/logIn/playOffline/toStorage/getIdentifier`，带 `portable` BooleanProperty、`invalidate()` 失效通知（可在任意线程调用）、`clearCache()` 空默认、`getTextures()` 默认返回空。注意基类 `equals` 只比较 `isPortable()`，各子类自行覆写（均为 characterUUID/username + portable）。

两个中间抽象类：
- `ClassicAccount`：增加 `logInWithPassword(password)`（凭据过期后用密码重登）。
- `OAuthAccount`：增加 `logInWhenCredentialsExpired()`（打开浏览器重新授权）+ 内部 `WrongAccountException(expected, actual)`。

三个具体分支：
- `yggdrasil/YggdrasilAccount`（继承 ClassicAccount）→ `authlibinjector/AuthlibInjectorAccount`
- `microsoft/MicrosoftAccount`（继承 OAuthAccount）
- `offline/OfflineAccount`（直接继承 Account）

工厂侧：`AccountFactory<T>` 定义 `AccountLoginType`（NONE/USERNAME/USERNAME_PASSWORD）与 `create(selector, username, password, progressCallback, additionalData)` / `fromStorage(Map)`。**FCL 增改**：新增 `ProgressCallback` 接口（`onProgressChanged(stageName)` + `NO_OP` 空实现），HMCL 原版签名无此参数；`CharacterSelector` 接口只有 `select(yggdrasilService, List<GameProfile>)`，取消时抛 `NoSelectedCharacterException`。异常族全部继承 `AuthenticationException`：`CredentialExpiredException`、`NotLoggedInException`、`NoCharacterException`、`CharacterDeletedException`、`ServerDisconnectException`、`ServerResponseMalformedException`、`RemoteAuthenticationException(error, errorMessage, cause)` 等。`AuthInfo`（username/uuid/accessToken/userType/userProperties）可被覆写 `getLaunchArguments()` 注入 JVM 参数——这是外置登录与离线皮肤注入的钩子。userType 常量 msa/mojang/legacy。

### 2. 微软登录全流程

`microsoft/MicrosoftService.java`（506 行，核心）。阶段常量 `STAGE_XBOX/XSTS/MINECRAFT/OWNERSHIP/PROFILE` 通过 ProgressCallback 上报。`authenticate()` 走 `OAuth.GrantFlow.DEVICE`，scope 为 `XboxLive.signin offline_access`，然后 `authenticateViaLiveAccessToken` 串五段：
1. XBL：POST `https://user.auth.xboxlive.com/user/authenticate`（RpsTicket=`d=<msToken>`，RelyingParty `http://auth.xboxlive.com`），从 `DisplayClaims.xui[0].uhs` 取用户哈希；XErr 错误码常量 BANNED=2148916227 / MISSING_XBOX_ACCOUNT / COUNTRY_UNAVAILABLE / ADD_FAMILY，封装为 `XboxAuthorizationException`；400 抛 `XBox400Exception`。
2. XSTS：POST `https://xsts.auth.xboxlive.com/xsts/authorize`（SandboxId RETAIL，RelyingParty `rp://api.minecraftservices.com/`），并校验两段 uhs 一致（不一致抛 "uhs mismatched"）。
3. MC：POST `https://api.minecraftservices.com/authentication/login_with_xbox`（identityToken=`XBL3.0 x=<uhs>;<xstsToken>`），记录 `notAfter = expiresIn*1000 + now`。
4. 所有权：GET `/entitlements/mcstore` 必须 200（注释引 HMCL#2979）。
5. 档案：GET `/minecraft/profile`；**404 时再查 `/entitlements/license`** 区分 `MinecraftJavaEditionLicenseNotFoundException`（无 Java 版许可）与 `...ProfileNotFoundException`（有许可没建档案）——这是很细的 UX 区分。

`refresh(oldSession)`：先 OAuth refresh_token 刷新 MS 令牌（`OAuth.refresh`：公共客户端不带 client_secret），再**完整重跑 XBL→XSTS→MC 链**。`validate(notAfter, ...)`：未过期且 GET profile 200 即有效。profile 缓存用 `ObservableOptionalCache`（专用 2~10s keepalive 线程池），档案本体取 `sessionserver.mojang.com/session/minecraft/profile/<uuid>`。所有请求统一 `.retry(5)`。`MicrosoftAccount.logIn()`：`!authenticated || now>notAfter` 时 validate→否则 refresh，refresh 后校验 profile UUID 未变否则抛异常；皮肤/披风管理走 Kotlin `MinecraftSkinService.kt`（POST `/minecraft/profile/skins` multipart、DELETE `skins/active`、PUT/DELETE `capes/active`）。`progressCallback` 为 volatile（UI 线程注入、后台线程读取），且**必须在构造器发起认证前注入**（构造器即登录）。

`auth/OAuth.java`：封装端点对 `https://login.live.com/oauth20_authorize.srf` / `oauth20_token.srf` / `login.microsoftonline.com/consumers/oauth2/v2.0/{devicecode,token}`。支持 AUTHORIZATION_CODE 与 DEVICE 两种流。DEVICE 流：取 device_code → `grantDeviceCode(userCode, uri)` 回调 → `openBrowser` → 循环 sleep(interval) 轮询 token 端点，处理 `authorization_pending`/`expired_token`（抛 NoSelectedCharacterException）/`slow_down`（interval+=5s）；总上限 `min(expiresIn, 900)` 秒；`UnknownHostException` 直接 continue（弱网不断线）。**坑**：取消标志 `IS_CANCELED` 是 public static 可变字段；轮询阻塞调用线程。`invalid_grant` + `AADSTS70000` 映射为 `CredentialExpiredException`。

回调事件机制：`Session`/`Callback` 接口由 `com/tungsten/fcl/game/OAuthServer.java` 实现——基于 NanoHTTPD 的 localhost 回调服务器（redirect_uri `http://localhost:<port>/auth-response`，用 CompletableFuture 传 code，返回打包的 microsoft_auth.html 关闭页，1 秒后 stop），服务于授权码流；当前 FCL 默认设备码流，该类仍保留。

### 3. 外置登录（authlib-injector）

`AuthlibInjectorServer`：`locateServer(url)` 请求目标站，读响应头 `x-authlib-injector-api-location` 重定向到真正 API root（忽略尾斜杠比较），再拉元数据缓存（`meta.serverName/links/feature.non_email_login`）；缓存带 `metadataRefreshed` 脏标记，`invalidateMetadataCache()` 置脏后 `fetchMetadataResponse()` 懒刷新；自定义 Gson Deserializer 支持把元数据缓存一起持久化。它内部持有 `YggdrasilService`，端点由 `AuthlibInjectorProvider` 按 `<apiRoot>authserver/{authenticate,refresh,validate,invalidate}`、`api/user/profile/<uuid>/skin`、`sessionserver/session/minecraft/profile/<uuid>` 拼出。

`AuthlibInjectorAccount.logIn()` 的 `inject()` 用**两个并行 CompletableFuture** 预取服务器元数据与 authlib-injector jar，与 yggdrasil 登录并发执行，最后包装成 `AuthlibInjectorAuthInfo`，其 `getLaunchArguments` 注入 `-javaagent:<jar>=<serverUrl>`、`-Dauthlibinjector.side=client`、`-Dauthlibinjector.yggdrasil.prefetched=<Base64(元数据)>`。`playOffline()` 需 artifact 与 prefetched 元数据齐备，否则抛 `NotLoggedInException`。jar 下载由 `AuthlibInjectorDownloader` 负责：从 `authlib-injector.yushi.moe/artifact/latest.json`（经下载源镜像注入）取 build_number 比较升级，SHA-256 校验，`AtomicBoolean` 保证 checkUpdate 只跑一次，`synchronized` 防并发下载；本地 jar 靠读 manifest（`Implementation-Title=authlib-injector`、Build-Number）识别。两个工厂：`AuthlibInjectorAccountFactory`（serverLookup 函数按 URL 找 server）与 `BoundAuthlibInjectorAccountFactory`（绑定单服务器，供服务器列表 UI）。fromStorage 时会把持久化的 `profileProperties` 回填进 profileRepository 再立刻 invalidate（预热线程）。

### 4. Yggdrasil 通用层与离线账户

`YggdrasilService`：authenticate 请求体含 `agent{name:Minecraft,version:1}`、随机 clientToken（UUID）、`requestUser:true`；响应校验 clientToken 未被换；refresh 可带 selectedProfile 并校验；validate 遇 `ForbiddenOperationException` 返回 false（否则向上抛）；`getTextures` 解码 base64 `textures` property。`YggdrasilAccount.logIn()` 为 synchronized：validate 通过则复用；否则 refresh（Forbidden→CredentialExpiredException），并校验 selectedProfile 未变。构造时把 `profileRepository.binding(uuid, true)` 的 **binding 存为字段防 GC**（注释自认 tricky），档案到达后 `invalidate()` 刷新 UI。`toStorage()` 会把已缓存的 `profileProperties` 一并写盘。

`OfflineAccount`：UUID 用官方算法 `nameUUIDFromBytes("OfflinePlayer:"+name)`；logIn 生成随机 accessToken，user type 写 `msa`（注释与常量略有出入）。若皮肤类型非 DEFAULT（`loadAuthlibInjector`），登录时并行下载 authlib-injector jar；`OfflineAuthInfo.getLaunchArguments` 在**启动时**新起本地 `YggdrasilServer`（端口 0），注入 `-javaagent:...=http://localhost:<port>`。`offline/YggdrasilServer` 是完整本地 yggdrasil 实现（HttpServer）：路由 `/`、`/status`、`/api/profiles/minecraft`、`hasJoined`、`join`（直接 204）、`profile/<uuid>`、`/textures/<hash>`；用随机 RSA 密钥做 SHA1withRSA 签名 textures property，skinDomains 限定 127.0.0.1/localhost；`offline/Texture` 按官方算法（宽高+逐像素 ARGB，透明置零）计算 SHA-256 并静态缓存。皮肤模型 `Skin`（DEFAULT/ALEX/STEVE/LOCAL_FILE/YGGDRASIL_API，支持注册默认皮肤加载器）。

### 5. 存储格式（Map 序列化，键值扁平）
- Microsoft：`uuid/displayName/tokenType/accessToken/refreshToken/notAfter/userid`
- Yggdrasil：`clientToken/accessToken/uuid/displayName/userProperties` + 账户层 `username/profileProperties` + 注入层 `serverBaseURL`
- Offline：`uuid/username/skin{type/textureModel/localSkinPath/localSkinCape}`

## 二、download 包

### 1. DownloadProvider 镜像机制

接口 `DownloadProvider`：`getVersionListURLs / getAssetObjectCandidates / injectURL / injectURLWithCandidates（默认单候选）/ injectURLsWithCandidates / getVersionList(GameComponentType) / getConcurrency`。核心思想是"URL 注入"：Mojang/Forge JSON 里写死的 URL 统一过 `injectURL` 改写，并产生**候选 URL 链**供下载层逐个尝试。

- `MojangDownloadProvider`：纯官方源；但 OptiFine 列表仍只能用 BMCLAPI（`OptiFineBMCLVersionList(apiRoot)`），Forge 用 `hmcl.glavo.site` 元数据。FCL 增强：`injectURLWithCandidates` 对 `libraries.minecraft.net` 未命中时追加 BMCLAPI `/maven/` 与 Maven Central 候选（1.7.10 Forge 的 Scala/Akka、lwjgl3ify forgePatches 等官方库不托管）。并发 6。
- `BMCLAPIDownloadProvider`：主替换表覆盖 piston-meta/piston-data/launcher.mojang→root、libraries.minecraft.net→root/libraries、forge/neoforge/liteloader/fabric maven→root/maven、authlib-injector→root/mirrors/...、Maven Central→腾讯云、hmcl.glavo.site 元数据→alist.8mi.tech；另有 `fallbackReplacement`（Modrinth/CurseForge API→`mod.mcimirror.top`），**只在主表未命中时**启用且候选含原 URL。并发 `max(cpu*2, 6)`。
- `AutoDownloadProvider`（FCL 的 multiple source 实现）：版本列表与文件下载用两条独立 provider 候选链；`getVersionList` 返回 `MultipleSourceVersionList`，其 `refreshAsync(gameVersion)` 动态构建递归 fallback 任务链（execute 里若依赖失败且还有后端则现场生成 nextTask），成功则把后端结果合并进自身 multimap，全部失败才抛（并把任务 significance 降为 MINOR）。
- `DownloadProviderWrapper`：volatile provider 热替换（设置变更即时生效），**关键设计**：同一组件类型复用同一代理 `ComponentVersionList` 实例（ConcurrentMap 缓存），refresh 完成后把真实 provider 的结果 `putAll` 进代理——否则"刷新合并与读取会落在不同实例上"。全局 `refreshAsync()` 抛 UnsupportedOperationException。
- FCL 侧 `fcl/setting/DownloadProviders.java`：`versionListSource` 与 `fileDownloadSource` 两个独立设置项；DEFAULT 时按 `LocaleUtils.IS_CHINA_MAINLAND` 决定 BMCLAPI/Mojang 先后；通过 `FXUtils.observeWeak` 监听设置变化调 `PROVIDER_WRAPPER.setProvider(...)`；并联动 `FetchTask.setDownloadExecutorConcurrency`（task 包）设置下载线程。

### 2. VersionList 体系

**没有名为 `VersionList` 的基类**——HMCL 的 `VersionList/RemoteVersion` 在 FCL 中被重命名为 `ComponentVersionList` / `ComponentRemoteVersion`（包内直接抽象，无独立文件）。`ComponentVersionList<V>`：`SimpleMultimap<gameVersion, TreeSet<V>>` + ReentrantReadWriteLock；`refreshAsync()`/`refreshAsync(gameVersion)`；`loadAsync` 读锁内判懒加载；`getVersion` 先按 `selfVersion` 再按 `fullVersion` 匹配。`ComponentRemoteVersion`：componentType/gameVersion/selfVersion/releaseDate/urls/type(RELEASE/SNAPSHOT/OLD/PENDING/UNOBFUSCATED)，抽象 `getInstallTask(ddm, baseVersion)` 由子类多态分派到各 InstallTask——**这就是依赖解析的入口；代码库中无独立的 DependencySequence 类**。FCL 性能增强：compareTo 里用静态 `ConcurrentHashMap` 缓存 `VersionNumber` 解析（Fabric/Quilt 笛卡尔积可达数万条）。排序约定：compareTo 反向（新版本在前）以便 TreeSet 自然序。

各实现要点：
- `game/GameVersionList`：拉 version_manifest + 合并内置 `/assets/game/unlisted-versions.json`（恢复下架版本，BMCL 镜像表也含 unlisted 镜像）；`getVersionsImpl` 忽略 gameVersion 键返回全部。`GameRemoteVersion` 排序按 releaseDate→GameVersionNumber。
- `forge/ForgeVersionList`（官方源用）：数据源是 `https://hmcl.glavo.site/metadata/forge/` 元数据（gameVersion→int[] build→ForgeVersion 明细，拼 installer URL）；1.7.10-pre4 与 `1.7.10_pre4` 双向转换。`ForgeBMCLVersionList`：不支持全量刷新，按游戏版本查 `/forge/minecraft/<v>`，每个版本给 maven 双命名 + BMCLAPI download 三个候选 URL；`getVersion` 会截掉 "1.18.2-" 前缀。
- `fabric/FabricVersionList`：meta.fabricmc.net v2 loader×game 笛卡尔积；`FabricInstallTask` 拉 loader/<g>/<l> 元数据，解析 launcherMeta（mainClass 可为对象取 client、可选 launchwrapper tweakers），库只取 common+server 两段，patch priority 30000。`FabricAPIVersionList` **直接以 Modrinth 为后端**（项目 ID `P7dR8mSH`），`FabricAPIInstallTask` 把 mod jar 直接下到 `runs/<id>/mods/`（不产 patch）。
- `quilt`（meta.quiltmc.org v3，同 Fabric 结构）、`legacyfabric`（带 normalizeVersion；与 Fabric 共用 `net.fabricmc:fabric-loader` 坐标，靠 `net.legacyfabric` 库区分归属）。
- `neoforge/NeoForgeOfficialVersionList`：并行拉 `maven.neoforged.net` 的旧 `net/neoforged/forge`（1.20.1）与新 `net/neoforged/neoforge` 两个 API，从版本串反解 MC 版本（0.x snapshot、major>=26 新纪元含 `+build` 后缀），对 `versions:null` 容错（FCL 增强）；1.20.1 需 `normalize`。`NeoForgeInstallTask.install` 识别两种 installer：profile=forge 且含 `META-INF/NEOFORGE.RSA`/文本 neoforge → 复用 `ForgeNewInstallTask` 再改 patch id；profile=neoforge/NeoForge → `NeoForgeOldInstallTask`（整份复制的 processor 管线）。
- `optifine`：仅 BMCLAPI 列表（`/optifine/versionlist`，去重 mirror）；`OptiFineInstallTask`：把 installer 存为 `optifine:OptiFine:<mc>_<ver>:installer` 库并删除其中 `META-INF/mods.toml`；若含 `optifine/Patcher.class` 则运行 optifine.Patcher 生成 patched jar（Java 降级链 8→17→11→21），提取 launchwrapper-2.0 / launchwrapper-of-<ver>；`buildof.txt` 早于 20210924-190833 且 Forge 1.17 → 抛不兼容。本地 installer 识别用 `ConstantPoolScanner` 扫 Config.class 常量池的 MC_VERSION/OF_EDITION/OF_RELEASE（不运行安装器即可判定版本）。
- `liteloader`：官方 versions.json（含字段拼写 `repoitory`），snapshot 走 repo.mumfrey.com 的 metadata.xml 并用 **Jsoup** 解析（HMCL#3147 workaround）；`LiteLoaderInstallTask` patch priority 60000，且 `setLogging(emptyMap)` 防畸形日志 XML 崩溃。BMCL 版按游戏版本查。
- `cleanroom`：hmcl.glavo.site 元数据（固定 1.12.2），安装直接复用 `ForgeNewInstallTask` 后改 patch id。

### 3. 依赖解析与安装流程

`DependencyManager` 接口 → `AbstractDependencyManager`（getVersionList 委托 DownloadProvider）→ `DefaultDependencyManager`（无状态，持有 DefaultGameRepository + DownloadProvider + DefaultCacheRepository）。串链逻辑在 `installLibraryAsync(gameVersion, baseVersion, libraryId, libraryVersion)`：
1. 校验 baseVersion 未 resolved；
2. `GameComponentType.fromPatchId` → `versionList.loadAsync(gameVersion)`（FCL 给该任务命名以在 UI 可见）；
3. `getVersion(gameVersion, libraryVersion)` → `installLibraryAsync(baseVersion, remoteVersion)`；
4. 先 `removeLibraryAsync`（`LibraryAnalyzer.analyze + removeLibrary` 清旧库与旧 patch）→ `remoteVersion.getInstallTask(this, version)` 多态分派 → `addPatch`。
失败回滚之外的兜底：`installLibraryAsync(oldVersion, Path installer)` 依次尝试 Cleanroom→NeoForge→Forge→OptiFine 的本地 installer 识别，全部失败抛 `UnsupportedLibraryInstallerException`。

`GameBuilder`（流式 API：name/gameVersion/version(id,v)/version(remoteVersion)）→ `DefaultGameBuilder.buildAsync()`：空 Version → 依次 thenCompose game patch → toolVersions 各 patch → remoteVersions 各 patch → `repository.saveAsync`；**FCL 增强**：失败时 `removeVersionFromDisk(name)` 回滚。全程带 `fcl.install.<id>:<ver>` stages hint。

`LibraryAnalyzer`：13 个 `LibraryType`（game/fabric/fabric-api/legacyfabric/.../forge/cleanroom/neoforge/liteloader/optifine/quilt/quilt-api/bootstraplauncher），group+artifact 正则匹配库坐标；forge/neoforge 从版本串提取纯版本，neoforge 兜底扫描 `--fml.neoForgeVersion` 参数；`isModded` 按 mainClass 前缀（含 Cleanroom 的 top.outlands 与 RetroFuturaBootstrap）；常量 `FORGE_OPTIFINE_BROKEN_RANGE`（48.0.0~49.0.50）与三组 FORGE_TWEAKERS/OPTIFINE_TWEAKERS 供 MaintainTask 使用。`MaintainTask.maintain`：按 mainClass 分三条路径（LaunchWrapper 调 tweakClass 顺序；ModLauncher+OptiFine 注入打包在 assets 里的 `HMCLTransformerDiscoveryService-1.0.jar` + `-Dhmcl.transformer.candidates`；BootstrapLauncher 修 `-DignoreList`，区分 bootstraplauncher 0.1.17 前后语义）；`unique()` 对同 group:artifact 库去重（rules 相同取新版本，同版本取 JSON 更长/信息更全者，text2speech 这类 lib+native 共坐标场景放行）。

游戏完整性检查：`checkGameCompletionAsync` = jar 缺失补下 + patch 检查 + `GameAssetDownloadTask` + `GameLibrariesTask`。资产任务先经 `GameAssetIndexDownloadTask`（URL 含 sha1 才做哈希校验，否则仅 JSON 解析验证——注释解释 Mojang 会随时改 index），再逐 object 生成 FileDownloadTask（candidate 指向共享缓存 `assets/objects/<前缀>`，`withCounter("fcl.install.assets")` 汇总进度）。库任务 `GameLibrariesTask`：先 `LibFilter` 过滤（Android 裁剪），特判 **1.20.4 + Forge 在 broken range 时直接删 OptiFine jar 内的 META-INF/mods.toml** 修复崩溃；`shouldDownloadLibrary` 做存在性+SHA1+旧式 checksums+ZIP 完整性四层校验。`LibraryDownloadTask`：forge 主库强制 universal classifier；preExecute 先查缓存（`DefaultCacheRepository.getLibrary`：SHA1 内容寻址文件 → index.json（forge 类型需 checksums 验证）→ 旧版 libraries 目录迁移），下载失败时在 execute 里把 FileDownloadTask 的 DownloadException 解包为 `LibraryDownloadException`。`DefaultCacheRepository` 基于 `FCLPath.CACHE_DIR`，索引 `cache/index.json {libraries:[{name,hash,type}]}`，读写锁保护。

Forge 安装（最复杂）：`ForgeInstallTask` 包装下载 installer（ZIP 完整性校验）→ 1.13+ 校验原 mainClass 防 Forge/Fabric 共存 → `detectForgeInstallerType`（install_profile.json 有 `spec` = new，有 `install`+`versionInfo` = old）。`ForgeNewInstallTask`：解出 profile（ForgeNewInstallProfile：spec/minecraft/json/version/path/libraries/processors/data，processors 过滤 client side，data 取 client 值），预拷贝内嵌 maven/ 库；变量解析支持 `{VAR}`/`'literal'`/`[artifact]`/转义文本（`replaceTokens` 手写状态机）；文件型 data 解包到临时目录；processors 用 `Task.runSequentially` 逐个跑 `ProcessorTask`（拼 `java -cp <classpath+jar> <mainClass> <args>`，输出文件 SHA-1 校验、已存在且匹配则跳过）；**特殊 patch**：识别 `--task DOWNLOAD_MOJMAPS --side client` 的 processor，改为直接 `VersionJsonDownloadTask` + client_mappings 下载（绕开原 processor）；JVM 失败按 8→17→11→21 降级重跑；安装日志环形 200 行经 `updateMessage` 推 UI。Android 上进程由 `InstallerProcessRunner.kt` 统一运行：`:jvm` 前台服务（ProcessService）+ **文件轮询通信**（exit code 文件、started 标记、增量读日志，半行不消费防截断 UTF-8），5 分钟超时、进程级重试 1 次、服务启动重试 5 次，并刻意不依赖 `getRunningAppProcesses` 判活（部分 ROM 不返回 :jvm 进程）。`ForgeOldInstallTask` 仅解包 universal jar + versionInfo patch。

### 4. CurseForge / Modrinth（位于 mod 包，交叉引用）

`mod/curse/CurseForgeRemoteModRepository.java`：PREFIX `https://api.curseforge.com`，实现 `RemoteModRepository`（search/addon 详情/files/fingerprints），模型 `CurseAddon`（含 LatestFile）+ record `Pagination/Response<T>/FingerprintMatchesResult/FingerprintMatch`；含 HMCL#4597 workaround。`mod/modrinth/ModrinthRemoteModRepository.java`：PREFIX `https://api.modrinth.com` `/v2/search`、project versions、`get_version_from_hashes` 批量反查；record 模型 `Project/ProjectVersion/ProjectVersionFile/Category/Dependency/Screenshot`。两者请求都经 `downloadProvider.injectURLWithCandidates`，因此在 BMCL 源下自动获得 `mod.mcimirror.top` 镜像候选——这是 download 包 `fallbackReplacement` 与 mod 体系的耦合点；`FabricAPIVersionList` 直接调 `ModrinthRemoteModRepository` 取 fabric-api（P7dR8mSH）版本则是反向耦合。

### 5. 与 task 包（FetchTask）的引用关系

download 包只消费 task 包的 `Task/GetTask/FileDownloadTask`：构造形如 `new FileDownloadTask(List<URL> candidates, File, IntegrityCheck)`，再 `setCandidate(缓存路径)/setCaching(true)/setCacheRepository/addIntegrityCheckHandler(ZIP_INTEGRITY_CHECK_HANDLER)`；候选链失败重试逻辑全部封装在 FileDownloadTask 内部。并发线程数由 FCL 侧 `FetchTask.setDownloadExecutorConcurrency` 全局设置（DownloadProviders.init 里随配置联动）。

## 三、值得注意的细节与坑

1. **`OAuth.IS_CANCELED` 是 public static 可变布尔**：并发两次微软登录会互相取消；设备码轮询 sleep 在调用线程，不能在主线程调。
2. **`MojangDownloadProvider` 并非纯官方**：OptiFine/Forge/Cleanroom 的版本列表源无论选什么源都走第三方元数据（BMCLAPI、hmcl.glavo.site），官方源只是"文件下载走官方"。
3. **候选链语义不统一**：BMCL 主表命中→单候选（不做 fallback）；未命中且匹配 fallback 表→原 URL+镜像双候选；Mojang 源则对 libraries.minecraft.net 无条件追加两个候选。
4. `GameInstallTask` 把 assets/libraries 下载失败**静默吞掉**（`.withRunAsync` 忽略），允许先落盘一个不完整版本，后续靠 checkGameCompletion 补齐。
5. `ComponentRemoteVersion.equals/hashCode` 只按 selfVersion——TreeSet 合并多源版本号相同的条目属预期去重，但意味着同版本号不同 URL 的条目会被丢弃（依赖候选链兜底）。
6. `DownloadProviderWrapper.getVersionList` 的代理列表 `refreshAsync()`（无参全局刷新）直接抛 UnsupportedOperationException——UI 必须走 per-gameVersion 刷新。
7. `YggdrasilAccount` 用字段强持有 profile binding 防 GC（作者注释自认 tricky）；`logIn` 为 synchronized，而 `MicrosoftAccount.logIn` 不是（靠 authenticated 标志 + validate）。
8. `OfflineAuthInfo` 里本地 YggdrasilServer 起在随机端口后**从不 stop**（依赖进程回收）。
9. `AuthlibInjectorDownloader.getArtifactInfo` 本地无 jar 时会 `updateChecked.set(true)` 再 update，导致之后的 `checkUpdate()` 变成空操作——升级检查只此一次。
10. NeoForge 官方 API 对 `versions:null` 容错、BMCL Forge 列表的双命名 URL、LiteLoader snapshot 走 mumfrey 快照仓库——三处都是针对线上服务不稳定性的防御补丁（多带 HMCL issue 编号）。
11. `DefaultCacheRepository` 在构造器里调用可被覆写的 `changeDirectory`（虚函数调用构造器的反模式，但因子类无状态而安全）；index.json 损坏时静默重建。
12. 大量 FCL 专属中文注释标记的增强点：ProgressCallback 注入、任务命名（`version_list_refreshing`）、安装日志 200 行环形、VersionNumber 缓存、失败回滚 removeVersionFromDisk、installer 进程文件通信——这些是 FCL 相对 HMCL 上游的主要改动面。
