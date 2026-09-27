# fclcore：util 包

范围：`FCL/src/main/java/com/tungsten/fclcore/util/` 全部 102 个文件、约 11750 行，含子包：fakefx、function、gson（含 gson/fakefx）、io、platform、png（含 image/fakefx）、skin、tree、versioning。

## 一、总体结构

util 是整个 fclcore 的地基，依赖方向基本单向（被 auth/download/game/mod/task 反向引用，自身仅少量引用 FCL Android 层和 fakefx）。

**根包（19 类）**：`Constants`（3 个常量：Mojang 库/版本源、bmclapi 备源 URL）；`Logging`；`Lang`（工具函数大杂烩）；`StringUtils`（621 行）；`CacheRepository`（ETag HTTP 缓存仓库）；`DigestUtils`/`Hex`（SHA 系列摘要 + 小写 hex 编解码）；`MurmurHash2.kt`；`SimpleMultimap`（Map+Collection 组合的简易多值映射，非线程安全）；`InfiniteSizeList`（可稀疏 set 的 ArrayList，`size()` 返回最后一个非 null 元素+1）；`Holder`（可变引用容器，兼作 InvalidationListener）；`Pair`（record）；`InvocationDispatcher`；`FutureCallback`（带 resolve/reject 的回调接口，供下载重试）；`KeyUtils`（RSA 4096 密钥生成 + PEM 公钥导出，用于离线皮肤签名类场景）；`LibFilter`（FCL 特有：过滤/替换版本 JSON 中的库）；`Pack200Utils`（调 Android 侧 `libunpack200.so` 解 .pack，FCL 特有移植）；`ReflectionHelper.getCaller`；`ToStringBuilder`。

**function（7 接口）**：`Exceptional*` 系列，给 Runnable/Supplier/Function/Consumer/BiFunction/BiConsumer/Predicate 加受检异常泛型 `E`，配套 `Lang.wrap`/`Lang.rethrow` 把异常解包 ExecutionException/CompletionException 后重抛。

**io（13 类）**：见下文网络与 zip 部分；另有 `FileUtils`、`IOUtils`、`CSVTable`、`JarUtils`、`HttpServer`、`HttpMultipartRequest`、`ResponseCodeException`、`ChecksumMismatchException`。

**versioning（3 类）**：`VersionNumber`/`GameVersionNumber`/`VersionRange`，见第四节。

**gson（14 类 + fakefx 子包 16 类）**：见第五节。

**png（8 类）**、**skin（2 类）**、**tree（3 类）**、**platform（3 类）**、**fakefx（8 类）**：见第六、七节。

## 二、网络层

网络有三层：

1. **`NetworkUtils`**：底层 `URLConnection` 工厂。`createConnection` 统一设置 UA（`FCL/<版本号>`，取自 `FCLApp.getAppContext()`，即 util 包硬耦合了 Android 应用上下文）、10 秒连接/读取超时、`setUseCaches(false)`、Accept-Language；对 `*.baidupcs.com` 特判伪装 `pan.baidu.com` UA。**`resolveConnection` 是重点**：手动跟随 3xx 重定向（`setInstanceFollowRedirects(false)`，逐跳复制请求头与方法，Location 用 `encodeLocation` 修复未编码的响应，仿 curl 实现），上限 20 跳。`readData` 优先读输入流，出错时回落读 error stream（便于拿到错误响应体），自动识别 gzip。`doGet(List<URL>)` 依序尝试候选镜像，全部失败时聚合 suppressed 异常。另有 query 编解码、`detectFileName`（Content-Disposition 优先）。

2. **`HttpRequest`**：链式 GET/POST 封装。带 `retry(n)` 重试（`getStringWithRetry` 捕获 Throwable 循环重试，最终统一包装为 IOException；**默认 retry=1 即不重试**）、`ignoreHttpCode`/`ignoreHttpErrorCode(code)` 容忍码、自定义 `filter` 校验器。POST 对非 2xx 抛 `ResponseCodeException` 并携带响应体数据。异步方法走 `Schedulers.io()` 的 CompletableFuture。**注意 GET 与 POST 行为不一致：GET 不检查状态码**（4xx/5xx 直接读流或抛普通 IO 异常），状态码检查只有 POST 有；GET 仅对 gzip 解包。

3. **`CacheRepository`**（单例，可 `setInstance` 替换）：基于 ETag 的磁盘缓存。文件按 `cache/SHA-1/前两位/完整hash` 内容寻址存储；`etag.json` 索引 URL→(eTag, sha1, 本地 mtime, 远端 Last-Modified)。`injectConnection` 注入 `If-None-Match`；`getCachedRemoteFile` 命中时还会按 mtime 快速校验完整性（不等则重算 SHA1）；`cacheData` 写入时若同 URL 有更旧条目（按 RFC1123 时间比较）则删除旧内容文件。索引保存用 `FileChannel.lock` 跨进程文件锁 + 先读后合并，`saveETagIndex` 允许多进程并发。`Storage` 内部类是通用 KV 持久化（`cache/<name>.json`）。读写锁保护内存 index，但 `getStorage` 用读锁包 `computeIfAbsent`（对 HashMap 并发写是潜在竞态，实际因 key 少而侥幸）。

## 三、zip 处理与混合编码

`CompressingUtils` 核心是**混合编码 zip 的自动检测**：zip 规范中非 UTF-8 条目名以原始字节存储（`general purpose bit` 未标记 UTF-8 时 `getRawName()` 给原始字节）。`testEncoding` 用 `CodingErrorAction.REPORT` 的严格 CharsetDecoder 逐一尝试解码所有非 UTF-8 标记条目的 rawName，任何一个 malformed/mappable 失败即判定不是该编码。`findSuitableEncoding` 依序尝试 UTF-8 → 本机字符集 → 21 个候选（GB18030、Big5、Shift_JIS、EUC 系列、KOI8-R、windows-125x、UTF-16/32 等），全部失败抛 IOException。`openZipFileWithPossibleEncoding` 据此自动选编码重开 commons-compress 的 `ZipFile`。

`createZipFileSystem` 走 JDK zipfs（FCL 用 `com.sun.nio.zipfs.ZipFileSystemProvider`，这是 JDK 内部 API 的 Android 移植）。**关键行为：捕获 `ZipError`（Error 而非 Exception，中央目录损坏时 zipfs 抛出）并 `initCause` 包装为 `ZipException("Corrupted zip file")`**，防止 Error 穿透崩溃进程（ZipException 是 IOException 子类）。writable 模式强制 `useTempFile`。

周边：`Unzipper`（流式解压到目录，支持子目录剥离、过滤器、已存在策略、进度回调；进度回调总数只统计文件条目且刻意不让带副作用的过滤器参与计数）、`Zipper`（打包，自动跳 .DS_Store，重复条目自动加 `.1`~`.9` 后缀，非线程安全）、`CompressingUtils.extract` 按扩展名分发 zip/jar/mrpack/7z/rar（junrar 解 RAR 时把文件名中的 `\` 替换为 `/`）。`tree` 包的 `ArchiveFileTree` 把 zip/tar(.gz) 组织成内存目录树（`Dir` + subDirs/files 两个 HashMap），构建时拒绝 `..`、空段和文件目录同名冲突，`ZipFileTree` 还能恢复 Unix 权限位和符号链接，`TarFileTree` 对 .tar.gz 先解压到临时文件并挂 shutdown hook 兜底删除。

## 四、版本号体系

- **`VersionNumber`**：移植自 Maven `ComparableVersion`（Apache 2.0）。把版本串解析成 `Item` 树（`LongItem`≤18位 / `BigIntegerItem` / `StringItem` 限定符 / `ListItem` 子列表），比较时按 Maven 序。FCL 改动：`StringItem` 标记 `alpha/beta/pre/rc/experimental` 前缀为 pre，使其**小于**无限定符版本（`1-beta < 1 < 1-xxx`）——比原版 Maven 更贴合 MC 语义。`isIntVersionNumber` 判断纯点分整数。用途：解析任意版本串的通用比较。
- **`GameVersionNumber`**（Java 17 sealed 类，936 行，最大文件）：**MC 专用**。五个子类：`Old`（rd-/in-/inf-/a/b/c 前缀的远古版本，内部用 VersionNumber 比较）、`Release`（1.x 和 26+ 年度版本，正则解析 major.minor.patch + `-pre`/`-rc`/`-snapshot` 后缀，区分 legacy（1.x 用 `-pre`）与新式（26+ 用 `-pre-`）中缀并生成 normalized 串）、`LegacySnapshot`（`25w14a` 格式，year/week/suffix/unobfuscated 打包成一个 int 比较）、`Special`（愚人节等特殊版本，链到前一个正常版本定位排序）。比较先按 `Type` 枚举序（PRE_CLASSIC→NEW），Release 与 LegacySnapshot 之间的比较靠类加载时读 `/assets/game/versions.txt` 建的快照 int 有序数组 + `version-alias.csv` 别名表做二分。`isAtLeast(release, snapshot)` 是性能优化 API：避免查表直接与"引入特性的首个快照"比较。`asGameVersion` 解析失败兜底返回 `Special`（unknown）。
- **`VersionRange<T>`**：闭区间 [min,max]，含 empty/all 单例、重叠判断、交集。

**用法区别**：任意字符串比较/排序用 `VersionNumber`；判断"某 MC 版本是否包含某特性/是否≥某版本"必须用 `GameVersionNumber`（VersionNumber 会把 `1.16.5` 和 `21w03a` 排出荒谬结果）。

## 五、gson 包

`JsonUtils` 提供三个 Gson 实例：`GSON`（默认，pretty printing + 各适配器）、`GSON_SIMPLE`、`UGLY_GSON`（不带 Instant/UUID/File 适配器的紧凑版）；以及 null 校验的 `fromNonNullJson`（null 抛 JsonParseException）、容错的 `fromMaybeMalformedJson`（语法错返回 null）。

适配器覆盖：
- `InstantTypeAdapter`：序列化为系统时区 ISO_OFFSET 日期时间；反序列化四级回落（美式 Medium 格式 → 带 offset ISO → 无时区 ISO 按本机时区 → 修补个位数小时 offset `+9:00`→`+09:00` 再试），兼容 authlib 返回的多种脏格式。
- `UUIDTypeAdapter`：无横线 32 位 hex（MC 标准格式），读取时用正则重新插横线。
- `FileTypeAdapter`：File ↔ 路径字符串。
- `LowerCaseEnumTypeAdapterFactory`：所有枚举统一序列化为全小写、读取时忽略大小写匹配（未匹配**静默返回 null**）。
- `Validation` + `ValidationTypeAdapterFactory`：对象实现 `validate()`，反序列化后自动校验，抛 `TolerableValidationException` 时**整个对象替换为 null**（序列化时同样处理）；其他 JsonParseException 则正常传播。
- `JsonType`/`JsonSubtype` + `JsonTypeAdapterFactory`：声明式多态（类似 Jackson 的 @JsonTypeInfo），按指定 property 字段值选子类适配器，写序列化时注入标签字段（若已有同名字段则抛异常）。
- `JsonMap`：put 时忽略 null key 的 HashMap，规避 Gson 内部 "duplicate key: null" 崩溃。
- `EnumOrdinalDeserializer`：按 ordinal 数字字符串反序列化（兼容数字编码的枚举），支持 @SerializedName 别名。
- `GsonSerializerHelper`：add 辅助（null 跳过）。

**gson/fakefx** 是 FxGson 库的内嵌移植：`FxGson`/`FxGsonBuilder` 注册 `ObservableList/Map/Set` 的 `InstanceCreator`（反序列化直接产出 fakefx 可观察集合）和 `JavaFxPropertyTypeAdapterFactory`——对 `Property` 类型序列化**属性内值而非属性本身**，反序列化新建 SimpleXxxProperty 包装；String/List/Set/Map/Object 属性由 `PropertyTypeAdapter` 基类处理，Boolean/Int/Long/Float/Double 原始属性由 `PrimitivePropertyTypeAdapter` 处理（null 值可配置为抛 `NullPrimitiveException` 或回退默认属性）；`strictProperties/strictPrimitives` 默认严格。还强制 `serializeNulls()`。

## 六、png 与 skin 包的能力边界

**png 包只有编码器，没有解码器**——这是重要的能力边界。`PNGWriter` 手写 PNG 二进制格式：8 字节文件头 + IHDR + 可选文本块 + IDAT + IEND，逐块计算 CRC32，`DeflaterOutputStream` 压缩，仅支持 RGB/RGBA（`PNGType` 枚举虽列了 5 种，writer 明确 `UnsupportedOperationException`）。文本块智能选择 tEXt（ASCII 短文本）/zTXt（ASCII≥20 字节且压得动）/iTXt（非 ASCII）。`PNGMetadata` 提供标准文本关键字（Title/Author/Software/Creation Time 等）。`png/fakefx/PNGFakeFXUtils` 是 **Android 桥**：把 `android.graphics.Bitmap` 逐像素包装成 `ArgbImage` 写 PNG（性能低，适合截图等低频场景）。`PNGFilterType` 枚举定义了 5 种行过滤器但 writer 实际**全部用 filter=0（NONE）**，属于未使用的预留。

**skin 包只有两个类**：`NormalizedSkin` 把 64x32 旧版皮肤**就地升级**为 64x64 新版（12 段复制翻转腿/臂到下层），支持任意整数倍缩放（w/64）；`isSlim` 用启发式判断 Alex 模型（手臂区域有透明像素，或四块区域全黑）。它不做渲染——渲染在 FCL UI 层（`com.mio.skin.SkinRenderer.kt`）和 account 相关类中使用它。皮肤下载/解码不在本包。

## 七、fakefx 工具、platform 与其他值得注意的细节

**fakefx 包**是配合 fakefx（纯 Java 移植的 JavaFX）的辅助：`BindingMapping`（map/flatMap/asyncMap 链式绑定，asyncMap 带计算去重与线程安全的状态机）；`MappedObservableList`（增量同步映射列表，用 IdentityHashMap 缓存被删元素减少 mapper 调用，引用图精心设计避免泄漏：target 强持有 listener、origin 弱持有）；`ObservableCache`/`ObservableOptionalCache`（异步 KV 缓存 + ObjectBinding，重复请求合并到同一 future，失败回退 fallback 值）；`PropertyUtils`（反射枚举 `xxxProperty()` 与 `getXxx()` 集合方法，实现任意对象间属性拷贝/批量监听）；`InvocationDispatcher`（合并高频事件：仅当无 pending 时才向 executor 投递，执行时取最新值）。

**platform 包**：`OperatingSystem` 枚举只有 5 个常量，**没有 HMCL 原版的 `CURRENT_OS` 字段**——因为 Android 上 `os.name` 恒为 Linux，跨平台规则（OSRestriction）统一回退 UNKNOWN；静态块还把 GBK/GB2312 归一到 GB18030、ASCII 归一到 UTF-8。`isNameValid` 按 FAT32 规则限制文件名字符。`CommandBuilder` 处理 JVM 参数去重：`-D`/`-XX:`/`-Xmx` 等"默认参数"被用户显式参数压制（addDefault 前缀匹配即跳过并记日志），`toString` 做类 shell 转义。`MemoryUtils` 完全是 Android API（ActivityManager），`findBestRAMAllocation` 按 32/64 位设备分档推荐内存。

**值得注意的坑/非显然行为**：
1. `FileUtils.writeBytes` 先写 `.tmp` 再 `ATOMIC_MOVE` 替换——所有配置落盘都是原子的；`saveSafely` 同理且保留 Windows 隐藏属性。
2. `HttpRequest.HttpGetRequest` 不校验状态码，与 POST 不对称；GET 也无 UA 以外的特殊处理。
3. `NetworkUtils` UA 构造依赖 Android Context，util 在非 Android 测试环境构造连接会 NPE。
4. `LibFilter` 用 JSON 字符串常量经 `GSON.fromJson` 静态构造替换库（asm→5.0.4、jna→5.13.0、oshi→6.3.0），移除 `org.lwjgl` 与 jinput/twitch-platform——这是 FCL 在 Android 跑 MC 的核心适配点之一。
5. `Pack200Utils` 依赖工作目录下的 `libunpack200.so` 可执行文件（把 so 当命令跑）。
6. `InstantTypeAdapter.deserializeToInstant` 最后一个 catch 块引用了外层变量 `e`（第一个 DateTimeParseException），可读性差但能编译。
7. `LowerCaseEnumTypeAdapterFactory` 读到未知枚举名静默返回 null 而不是报错，配合 Validation 可能掩盖数据问题。
8. `Hex.decodeHex` 抛 IOException 而非 IllegalArgumentException，语义别扭。
9. `StringUtils.tokenize` 实现了完整的 shell 风格词法（单引号、双引号内反引号转义与 `$VAR` 展开），JVM 参数解析多处复用；`LevCalculator`/`LongestCommonSubsequence` 复用 DP 数组用于模糊搜索（版本搜索/模组搜索）。
10. `CacheRepository.Storage.putEntry` 每次写都全量读改写文件，高频写有性能隐患；`joinEntries` 读盘合并意味着删除 key 的操作无法持久化。
11. `GameVersionNumber.Versions` 静态块在类加载时解析约 1000 行资源文件并构建三个静态表，首次触达成本高但之后 O(1)。
12. `HttpServer`（NanoHTTPD 封装）自带 traceId 日志路由，JsonParseException 映射 400、其余异常 500，供本地回环服务（如登录回调）使用。
