# fclcore：game 与 mod 包

范围：`FCL/src/main/java/com/tungsten/fclcore/` 下的 `game/`（39 文件约 4400 行，含 tlauncher/ 子包）与 `mod/`（70 文件约 10500 行，含 curse/modrinth/mcbbs/modinfo/multimc/server 子包）。

## 一、game 包（版本与启动配置模型）

### 1. Version —— 版本 JSON 模型（核心）

`Version.java`（513 行）是不可变对象，所有 setter 均为"复制并返回新实例"。字段完整覆盖官方版本 JSON：`id / version / priority / minecraftArguments / arguments / mainClass / inheritsFrom / jar / assetIndex / assets / complianceLevel / javaVersion / libraries / compatibilityRules / downloads(JsonMap<DownloadType,DownloadInfo>) / logging / type / time / releaseTime / minimumLauncherVersion / root / hidden / patches`，外加 transient 的 `resolved` 标记。

- **三种存在形态**：① 完整版本 JSON（inheritsFrom 或 patches 为准）；② patch（双参构造器，有 version/priority，无 root）；③ root 形态（`root=true`，本地 versions/<id>/<id>.json 里的"壳"，真实内容在 patches 里）。FCL 保存整合包/加载器版本时正是用 root+patches 结构。
- **resolve 流程**（`resolve(provider, resolvedSoFar)`）：无 inheritsFrom 时，root 版本退化为 `new Version(id)` 并保留 patches，且 jar 缺省设为自身 id；有 inheritsFrom 时递归解析父版本（用 `resolvedSoFar` Set 防循环依赖，成环时只告警不抛异常），再 `merge(parent, false)`。patches 按 `priority` 升序逐个 `patch.setJar(null).merge(thisVersion, true)`，即 **patch 优先级越高越晚合并、越能覆盖**。
- **merge 语义**：子非空覆盖父（minecraftArguments/mainClass/jar/assetIndex/assets/javaVersion/downloads/logging/type 等）；`arguments` 用 `Arguments.merge` 拼接（父子列表串联）；**libraries 是"子在前、父在后"拼接**（不按名字去重）；compatibilityRules 相反是父在前；minimumLauncherVersion 取 max。合并结果 `inheritsFrom=null`、`resolved=true`；若 isPatch，父的 patches 保留，否则把自身 `toPatch()`（改 id 为 `"resolved."+id`、hidden、清 patches）并入。
- **resolvePreservingPatches**：不真正合并，只把自身和 inheritsFrom 链上所有版本转成 patch 挂到 root 上（`addPatch(toPatch())`，按 id 去重），供编辑器/维持 patch 结构的保存（`MaintainTask.maintainPreservingPatches`）使用。
- **字段回退**：`getAssetIndex()` 在 JSON 无 assetIndex 时按 `assets` id 查一张硬编码 SHA-1 表（1.8/1.7.4/1.7.10/legacy 等 8 项），URL 指向 `Constants.DEFAULT_INDEX_URL`；`getDownloadInfo()` 无 downloads.client 时按 `Constants.DEFAULT_VERSION_DOWNLOAD_URL/<jar>/<jar>.jar` 拼 URL。
- `Validation.validate()` 要求 id 非空。`equals/hashCode` 仅看 id。`_setId` 是绕过不可变性的内部手段（refreshVersions 发现目录名与 json 内 id 不一致时强改）。

### 2. GameRepository / DefaultGameRepository

接口 `GameRepository extends VersionProvider`，约定目录：`getVersionRoot(id) = <base>/versions/<id>`、json/jar 均为 `<root>/<id>.{json,jar}`；`getRunDirectory` 由 `getGameDirectoryType` 决定（`ROOT_FOLDER`=base 或 `VERSION_FOLDER`=版本目录）。**DefaultGameRepository 固定 ROOT_FOLDER；FCL 层的 `FCLGameRepository` 覆写：整合包版本或 VersionSetting 开了隔离时返回 VERSION_FOLDER——这就是版本隔离实现点**。

关键行为：
- `refreshVersionsImpl`：并行扫描 versions/ 目录；**容错纠正**——若 `<id>.json` 不存在但目录里恰好只有一个 json，自动改名（连 jar 一起）；解析失败的版本直接跳过；json 内 id 与目录名不符时以目录名为准（`_setId`）； Classic 检测（`bin/` 下有 lwjgl/jinput/lwjgl_util 三 jar 即插入 `ClassicVersion`，对应 1.5.x 以前，mainClass 为 `net.minecraft.client.Minecraft`，老式 minecraftArguments）；最后对每个版本 `resolve()` 并按 `appliesToCurrentEnvironment()` 过滤后才入表；解析失败的 inheritsFrom 版本被忽略。`gameVersions` 缓存随之清空。
- `readVersionJson` **先尝试 TLauncherVersion 再尝试官方 Version**——TLauncher 的格式以 `tlauncherVersion` 字段区分，其 library 有 `"classifies"` 拼写错误（`TLauncherLibrary` 用 `@SerializedName("classifies")` 兼容）。
- `getVersionJar`：resolve 后取 `jar` 字段（缺省 id），定位 `versions/<jar>/<jar>.jar`。
- `getLibraryFile`：`hint=="local"` 时放 `versions/<version>/libraries/<fileName 或 artifact 路径>`（MultiMC 内嵌库约定），否则放共享 `libraries/` 下 maven 路径。
- `getGameVersion`：用 `gameVersions: ConcurrentHashMap<File,Optional<String>>` 缓存，底层 `GameVersion.minecraftVersion` 依次尝试 jar 内 `version.json` 的 id（含 " / " 截断）、扫描 `net/minecraft/client/Minecraft.class` 常量池找 `"Minecraft Minecraft "` 前缀串（Beta 前缀转 b1.x）、再扫描 `MinecraftServer.class` 用 `"Can't keep up!"` 字符串往前找含数字的串——纯启发式。
- `reconstructAssets`：虚拟资源（index 的 `virtual` 或 `map_to_resources`）把 `objects/<hash前2>/<hash>` 复制到 `assets/virtual/<assetId>/`，map_to_resources 时再复制到运行目录 `resources/`；**覆盖率低于 10% 判定资源缺失，回退原始目录**。
- `renameVersion`：先移目录、再移 json/jar、失败回滚；更新 json 内 id/jar 字段，并遍历修复其它版本对旧 id 的 `inheritsFrom` 引用。
- `removeVersionFromDisk`：先把目录改名为 `<id>_removed`（探测文件占用），**先删 json 再删目录**，保证中途失败也不会被识别为有效版本；最后异步 refreshVersions。
- 整合包接入点：`getModpackConfiguration(version) = versions/<id>/modpack.json`，`readModpackConfiguration`/`isModpack`，`getModManager(version)` 直接 new `ModManager(this, version)`。
- `saveAsync`：清除 jar 版本缓存；已解析保留 patch 的版本走 `MaintainTask.maintainPreservingPatches` 再 `VersionJsonSaveTask`。
- **`getClasspath`（接口 default 方法）被 FCL 改动：硬编码跳过所有 `org.lwjgl` 库**（注释"过滤 LWJGL 官方库"），因为 Android 端由宿主提供 LWJGL。

### 3. Argument 体系（两段式启动参数）

- `Argument` 接口 + 自定义 Deserializer：JSON 基本类型 → `StringArgument`，对象 → `RuledArgument`。
- `StringArgument.toString(keys, features)`：正则 `\$\{(.*?)\}` 逐个替换，**key 未命中时保留 `${...}` 原文**。
- `RuledArgument`：`rules + value/values`（两种字段名都支持），只有规则通过才输出。
- `Arguments`：`game` 与 `jvm` 两个列表（新版两段式）。静态默认值：`DEFAULT_JVM_ARGUMENTS`（-Djava.library.path / brand / version / -cp ${classpath}）、`DEFAULT_GAME_ARGUMENTS`（仅 `has_custom_resolution` feature 允许时输出 --width/--height）。老版本 `minecraftArguments` 与新 `arguments` 并存于模型中。
- `CompatibilityRule.appliesToCurrentEnvironment`：**规则列表中最后一条可应用的规则生效**（无规则=允许；全部不可应用=默认 DISALLOW）。feature 用 `Objects.equals` 精确匹配。
- **重大移植 quirks**：`OSRestriction.allow()` **恒返回 false**（Android 宿主不是 linux/windows/osx）。后果：所有带 OS 规则的 library（含 natives 变体）一律不适用；`MultiMCInstancePatch` 里为 `FirstThreadOnMacOS` 合成的 OSX RuledArgument 实际永远不会生效。`arch` 字段被解析但无用。
- `VersionLibraryBuilder`：统一维护 mcArgs（老格式）或 game/jvm（新格式）；老格式下 build 时把新格式 game 参数摊平追加进 minecraftArguments 并清掉 arguments（模仿官方"有 minecraftArguments 就忽略 arguments"）；提供 `--tweakClass` 的替换/去重/前插逻辑，供 LiteLoader/OptiFine/ForgeWrapper 安装器使用。

### 4. Library / Artifact / 下载信息

`Artifact` 解析 `group:name:version[:classifier][@ext]`，生成 maven 相对路径。`Library` 的 `hint/filename` 接受 MMC-hint/MMC-filename 别名；`getDownload()` 优先 `downloads.artifact`（natives 时取 classifiers[classifier]），URL 缺失时用 `url` 字段或 `DEFAULT_LIBRARY_URL + path`；`getSha1()` 把字面量 "invalid" 视为无校验。`equals` 按 name+isNative。`withoutCommunityFields()` 供 MultiMC 安装时生成标准布局副本。`DownloadType` 含 CLIENT/SERVER/WINDOWS_SERVER/映射表。

### 5. World（存档）

- 支持目录世界与 zip 世界（zip 根直含 level.dat，或仅一个子目录）；`special_level.dat` 兼容愚人节版 20w14infinite。
- NBT 读取按文件头自动识别 **GZip / LZ4 Block / 未压缩**（`openNBTInputStream`），用 steveice10 openNBT。
- 图标 icon.png 用 **Android BitmapFactory** 解码（inScaled=false）——平台特化。
- 元数据访问区分版本：种子 `WorldGenSettings.seed`（1.16+）或 `RandomSeed`；largeBiomes 三代格式分别处理；playerData 支持 `Player` 标签或 `singleplayer_uuid`(IntArray)→`players/data/<uuid>.dat`。
- `session.lock` 文件锁：`lock()` 写入 "☃" 后 tryLock；`isLocked` 对 AccessDenied/Overlapping 视为锁定；delete/copy 前检查并抛 `WorldLockedException`。
- `install()`：zip 世界解包后 `new World(worldDir).rename(name)` 同步改 LevelName 与目录名；`copy` 排除 session.lock。`getWorlds(savesDir)` 静态枚举。

### 6. 其它

- `GameComponentType`：patch id 枚举（game/forge/fabric/quilt/neoforge/optifine/liteloader/cleanroom + legacyfabric/fabric-api 等 FCL 扩展），是 mod 包与 download 包的公共词汇。
- `JavaVersion.kt` 是 FCL 简化实现：仅 isAuto/versionName/name，Auto 走 `JavaManager.getSuitableJavaVersion`，路径为 `FCLPath.JAVA_PATH/<name>`。
- `LaunchOptions` 被 FCL 特化：`Renderer`（com.mio.data.Renderer）、`vulkanDriverSystem`、`debugLog`。
- HMCL 的 GameDump/TxtProcessing 在本仓库不存在（全仓 grep 无命中，未被移植）。

## 二、mod 包（模组与整合包）

### 1. ModManager（增删禁用恢复）

- 扫描 `refreshMods(onScanned)`：先 `LibraryAnalyzer.analyze(resolvedPreservingPatchesVersion)` 拿当前 loader 集合；mods 目录下**名为纯版本号的子目录也递归扫描**（Forge 老版本约定）；按扩展名（jar/zip/litemod）选 READERS 表，先尝试当前环境支持的 loader 解析器，全失败再试不支持的，仍失败则构造 **UNKNOWN loader、以文件名为 modid 的降级 LocalModFile**；zip 损坏（ZipError）记入 `brokenFiles` 而不炸整个列表（FCL 加固）。每个成功项回调 `onScanned` 供 UI 增量刷新。
- 后缀约定：`.disabled` / `.old`。`disableMod/enableMod/backupMod/restoreMod` 全是 `Files.move` 改名；`LocalModFile.activeProperty` 的 `invalidated()` 副作用就是调这两个方法——**UI 开关即文件改名**。`setOld` 切换文件与 localModFiles 集合；`rollback` 有大量前置校验（同名直接 return）。
- Android 特化：`addMod(Activity, Uri, name)` 从 ContentResolver 拷入；`isFileNameMod(Uri)`。
- `hasSimpleMod` 同时检查启用名与 `.disabled` 名——整合包补全（Curse/Modrinth/Server）据此跳过已存在文件。

### 2. 本地模组索引/缓存的现状

HMCL 原版基于文件的 `LocalModRepository` 索引在 FCL 中**被 Room 数据库 `RemoteModCache` 取代**（`mod_repository_cache.db`，表名仍叫 `cache`）：key-value 缓存远程 API 原始 JSON，TTL 分级（指纹/单文件永久、详情 24h、版本列表 10 分钟、分类 7 天）；`json=NULL` 表示**负缓存**（如指纹未命中、Modrinth 404），防止反复白查；命中刷新 time 实现 LRU；上限 300 条/10MB/单条 2MB；首次建库时**直接 `deleteRecursively` 旧版 files/cache 目录**（可重建故不迁移）。过期即视为未命中并由 fetcher 重新拉取。

### 3. modinfo 子包（元数据解析）

统一用 kotlinx.serialization（`MOD_METADATA_JSON`：ignoreUnknownKeys + coerceInputValues），每个解析器 `fromFile(manager, path, fs)` 返回 LocalModFile：
- `ForgeOldModMetadata`：`mcmod.info`，兼容顶层数组与 `{modList:[...]}` 两种形态；作者回退链 author→authors→authorList→credits。
- `ForgeNewModMetadata`：`META-INF/mods.toml`（NeoForge 先试 `neoforge.mods.toml`），用 tomlj 解析后转 JSON 再反序列化；`${file.jarVersion}` 从 MANIFEST.MF Implementation-Version 替换；无 toml 时解析**嵌入模组**（`Embedded-Dependencies-Mod` 或 jar-in-jar `META-INF/jarjar/metadata.json`，解压到临时文件递归解析）；`analyzeLoader` 从 `dependencies.<modId>` 表的 modId=forge/neoforge 修正 loader 类型，并容忍 `[[dependencies]]` 怪写法（HMCL#5068）。
- `FabricModMetadata`：authors 元素兼容字符串/`{name}` 对象。`QuiltModMetadata`：要求 schema_version==1，contributors 为 map。`LiteModMetadata`：litemod.json。`PackMcMeta`：资源/数据包，pack_format + min/max_format（可数组），description 递归解析 MC 文本组件颜色。
- 注意 ModLoaderType 枚举拼写是 **NEO_FORGED**（不是 NEO_FORGE），且 FCL 加了 CLEANROOM。没有独立 ForgeLike 类——NeoForge 复用 ForgeNew 解析器仅换 loader 参数。

### 4. 四种整合包格式对比

统一骨架：`ModpackProvider`（getName/readManifest/createCompletionTask/createUpdateTask）→ `Modpack`（匿名子类实现 getInstallTask）→ 安装 Task（dependents: GameBuilder+ModpackInstallTask+MinecraftInstanceTask；dependencies: CompletionTask）→ `ModpackConfiguration<T>`（写入 `versions/<id>/modpack.json`，含 overrides 文件+SHA1 清单）→ `ModpackUpdateTask`（先备份版本目录到 `backup/<id>-<随机数>`，失败时恢复）。`ModpackInstallTask` 的覆盖策略是精髓：新有旧无→写入；双方都有但用户已删→保持缺失；双方都有且内容 hash 等于旧清单→覆盖（未改动）；**用户改过（hash 不等于旧清单）→保留用户版**；旧有新无→删除。

- **Curse（curse/）**：`manifest.json`（manifestType=minecraftModpack）+ `overrides/` + `modlist.html`；files 只有 projectID/fileID，无 URL 时拼 `edge.forgecdn.net/files/<id/1000>/<id%1000>/<fileName>`。`CurseCompletionTask` 用 API 反查文件名并回写 manifest.json；按项目 classId 分流（12→resourcepacks、6552→shaderpacks、其余 mods）；文件不存在/反查失败→`ModpackCompletionException`（但继续其它下载）。`CurseForgeRemoteModRepository`：API key 取自 Android 资源、`X-API-KEY` 头；**search 结果本地重排**（归一化全等 300 万分 > 前缀 200 万 > Levenshtein 距离+词命中减分）；指纹=剔除 0x09/0A/0D/20 后 MurmurHash2，两遍流式扫描防 OOM；两个硬编码指纹黑名单（HMCL#4597 误匹配 workaround）。
- **Modrinth（modrinth/）**：`modrinth.index.json` + `client-overrides/`（兼容 `overrides/`）；files 带 path/hashes/env/downloads。InstallTask **读旧配置时 TypeToken 用的是 `ModpackConfiguration<CurseManifest>`**（从 Curse 复制粘贴的痕迹，因 Gson 运行时擦除恰好无害）；CompletionTask 有 `Unsecure path` 路径穿越校验；env.client=unsupported 跳过。仓库层用 SHA-1 反查（404 归负缓存，含"HttpGetRequest 不检查状态码"的兼容注释）。
- **MCBBS（mcbbs/）**：`mcbbs.packmeta`（兼容 manifest.json），信息最全：Addon 列表（game/forge/fabric...版本）、AddonFile（path+sha1+force）与 CurseFile 多态（@JsonType）、LaunchInfo（launchArgument/javaArgument/minMemory/supportJava，通过 `Provider.injectLaunchOptions` 注入启动）、**fileApi 远程源**——CompletionTask 会拉 `<fileApi>/manifest.json` 做增量更新（force 字段决定是否覆盖用户修改），并保留已 prefetch 的 Curse 文件名（mergeFile）。LocalInstallTask 给版本加名为 `"mcbbs"` 的 patch 携带 manifest.libraries。导出任务**同时产出 mcbbs.packmeta 与 Curse manifest.json 双格式**。
- **MultiMC（multimc/）**：`instance.cfg`（ini，Properties 解析 + HMCL#2991 末尾冒号 workaround）、`mmc-pack.json` components、`patches/<uid>.json` 本地补丁否则拉 meta.multimc.org；requires 递归补齐缺失组件。最复杂的是 `MultiMCInstancePatch.resolveArtifact`：把 Json-Patch 列表**有损合成**为一个 `"multimc"` patch——末位 patch 优先、jvmArgs 累加、从 minecraftArguments 提取 `--tweakClass` 对转 tweakers、traits 转换（FirstThreadOnMacOS→OSX 规则的 -XstartOnFirstThread，**但 OSRestriction.allow 恒 false 使其失效**）、ForgeWrapper 注入 JVM 属性、compatibleJavaMajors 取可识别最大值；合成参数用老格式 minecraftArguments 承载以避免与新格式重复拼接；不支持的特性汇总告警。overrides 目录探测 4 种布局；hint=local 库双布局各拷一份；jarmods 解压合并进主 jar。
- **Server（server/）**：HMCL 自有格式 `server-manifest.json` + fileApi；files 直接复用 `ModpackConfiguration.FileInformation`；更新时 addons 变化会重建版本；下载判定考虑 mods 下 `.disabled/.old` 文件不重复下载；同样有 Unsecure path 校验。

### 5. 其它值得注意的实现

- `ModDependenciesResolver`：BFS 解析 REQUIRED 依赖（上限 50 防病态图），兼容性以 root 的 gameVersions/loaders 交集为准（loader 精确匹配优先）；去重两级——文件名零网络比对 + 项目 ID 惰性远程反查。
- `RemoteMod.File.getIntegrityCheck` 优先级 md5>sha1>sha256>sha512；`RemoteMod` 有全局 EMPTY 注册点供 BROKEN 依赖占位。
- 导出任务（curse/modrinth 的 .kt）：反查命中进 files、未命中进 overrides；**禁用态文件的语义保留**是细节——Curse 直接排除 `.disabled/.old`，Modrinth 则以 `client: optional` 写入索引并去掉后缀。
- `ModManager.refreshMods` 依赖 `LibraryAnalyzer.analyze`（download 包），`GameBuilder`/`DefaultDependencyManager` 在 download 包——整合包安装与下载体系强耦合，本包只负责"格式与编排"。
- `ModpackUpdateTask` 的备份目录名用 `Math.random()*10000000` 探测，无清理机制（失败恢复后备份目录仍留在 `backup/`）。
