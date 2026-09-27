# fclcore：task / event / launch 包

范围：`FCL/src/main/java/com/tungsten/fclcore/` 下的 `task/`（任务系统）、`event/`（事件总线）、`launch/`（启动器核心）。

## 一、task 包：异步任务系统（移植自 HMCL，Android 化改造）

### 1.1 职责定位与设计模式

该包实现了一个**基于"依赖/被依赖"关系的 DAG 任务编排框架**，本质是 HMCL Task 体系的移植。核心设计模式：

- **模板方法**：`Task` 定义 `preExecute() → dependents → execute() → dependencies → postExecute() → onDone` 的骨架，子类只实现 `execute()`；
- **组合器模式**（仿 `CompletableFuture` API）：`thenApplyAsync / thenComposeAsync / allOf / whenComplete` 等全部通过内部类 `UniApply`、`UniCompose`、匿名 `Task` 把链式调用编译为 dependents/dependencies 树；
- **指挥者**：`TaskExecutor`（抽象）+ `AsyncTaskExecutor`（唯一实现）负责真正调度，把任务树翻译成 `CompletableFuture` 链。

### 1.2 核心类清单

**Task.java**（39KB，抽象泛型类 `Task<T>`）：
- 状态字段：`state`（READY/RUNNING/EXECUTED/SUCCEEDED/FAILED）、`exception`、`dependentsSucceeded/dependenciesSucceeded`（由执行器回填）、`relyingOnDependents/relyingOnDependencies`（默认 true，控制失败是否阻断自身执行）。
- `executor` 字段默认 `Schedulers.defaultScheduler()`（ForkJoinPool.commonPool），可 `setExecutor` 换成专用线程池。
- `stage/inheritedStage`：阶段分组标签，UI（TaskListPane）据此渲染阶段节点；`withStage()` 包一层 `StageTask`、`withStagesHint()` 包 `StagesHintTask`（TaskExecutor 构造时读取其 stages 列表）、`withCounter()` 包 `CountTask`（doPostExecute 时触发 `notifyPropertiesChanged` → 监听器 `onPropertiesUpdate`）。
- `significance`：MAJOR/MODERATE/MINOR，控制日志与 UI 展示；`supplyAsync` 等工厂默认降为 MODERATE。
- 进度/消息：`progressProperty/messageProperty`（fakefx 只读属性，初值 -1），更新经 `InvocationDispatcher.runOn(Schedulers.androidUIThread(), ...)` **合并转发到 Android 主线程**（`AtomicReference` 去抖：已有 pending 时丢弃新值，取最后一个）；`updateProgress` 另有 1000ms 节流。
- 取消：`isCancelled()` = 线程中断标志 **或** 执行器注入的 `cancelled` Supplier（包私有 `setCancelled`，由 AsyncTaskExecutor 注入 `this::isCancelled`）——纯协作式取消。
- 关键签名：`public abstract void execute() throws Exception`；`public final T run()`（同步直跑路径：dependents→execute→dependencies→onDone，`doSubTask` 里用 fakefx `message.bind/progress.bind` 把子任务进度绑定到自身）；`public final TaskExecutor executor()/executor(boolean)/executor(TaskListener)`；`start()`/`test()`（test 阻塞等待结果）。
- 组合器行为：
    - `thenApplyAsync(fn)`：`UniApply`，this 是它的 dependent，execute 里 `setResult(fn.apply(this.getResult()))`；
    - `thenComposeAsync(fn/other)`：`UniCompose(relyingOnDependents=true)`，execute 中惰性求值出 `succ`，将其放入 `getDependencies()`（**dependencies 在 execute 之后跑**）；
    - `withComposeAsync/withRunAsync`：`UniCompose(relyingOnDependents=false)`，**前驱失败也继续执行**；
    - `whenComplete(executor, action)`：匿名 Task，dependents={this}，`isRelyingOnDependents()=false`，execute 里先断言不变式 `isDependentsSucceeded() != (exception==null)` 则 AssertionError，然后回调 `action.execute(exception)`；若上游失败则**重新抛出原异常**让失败继续传播（多个重载支持 success/failure 分离回调、携带 result）；
    - `allOf(tasks)`：dependents=全部任务，execute 只是聚合 `getResult()` 列表，significance=MINOR；
    - `runSequentially`：用 `thenComposeAsync` 折叠成链；
    - `composeAsync(fn)`：execute 里调 `fn.get()` 生成 `then`，放进 dependencies；
    - `supplyAsync/runAsync`：内部 `SimpleTask` 包装 Callable。
- 私有 `getCaller()`：用 `ReflectionHelper.getCaller`（过滤掉 task 包栈帧）以**调用点位置自动命名任务**，便于日志与 UI 显示。

**TaskExecutor.java**（抽象）：持 `firstTask`、`taskListeners`、`totTask`（AtomicInteger 统计任务总数）、`cancelled`（AtomicBoolean）、`exception`（执行器级别"最后一个异常"，供 UI 报错）；`stages` 仅当 firstTask 是 `StagesHintTask` 时提取。抽象方法 `start()/test()/cancel()`。

**AsyncTaskExecutor.java**（引擎，346 行）：
- `start()`：回调 `onStart` → `executeTasks(null, {firstTask})` → `thenApplyAsync` 中记日志、对 RuntimeException（排除 CancellationException/JsonParseException/RejectedExecutionException）调用静态 `uncaughtExceptionHandler`、回调 `onStop(success, this)`；最外层 `exceptionally` 交给 `Lang.handleUncaughtException`。`test()` 即 `start()+future.get()`。`cancel()` 未 start 会抛 IllegalStateException。
- `executeTasks` vs `executeTasksExceptionally`：前者把异常**捕获后作为返回值**（供父任务判断 relyingOn 语义），后者向上抛。空集合直接完成。
- `executeNormalTask` 的 CF 流水线（按序）：
    1. `checkCancellation` → 注入 cancelled → state=READY → stage 继承（自身 stage 否则父的 inheritedStage）→ 注入 propertiesChanged 回调 → `onReady` → 可选 `preExecute`（跑在 **task.getExecutor()** 上）；
    2. `executeTasks(task, getDependents())` 得 `dependentsException`：成功则 `setDependentsSucceeded()`，失败则 setException 且 `relyingOnDependents` 时 rethrow（**跳过 execute 与 dependencies**）；
    3. `runAsync(..., task.getExecutor())` 执行：state=RUNNING → `onRunning` → `execute()`，`whenComplete` 置 EXECUTED 并 rethrow；
    4. `executeTasks(task, getDependencies())`；
    5. 可选 `postExecute`（同样在 task.getExecutor() 上）；
    6. `thenApplyAsync`（无参 executor → commonPool）：dependencies 失败则记 severe 日志、setException、`relyingOnDependencies` 时 rethrow；否则 `checkCancellation` → `onDone.fireEvent(TaskEvent(failed=false))` → `onFinished` → state=SUCCEEDED → 返回 result；
    7. `exceptionally`：`resolveException` 解包 Completion/ExecutionException，`InterruptedException` 统一转 `CancellationException`；setException + 记录到执行器 `exception` 字段 → `onDone.fireEvent(failed=true)` → `onFailed` → state=FAILED → 再抛 CompletionException 向上传播（取消也走 onFailed，但日志是 "Task aborted"）。
- `executeCompletableFutureTask`：为 `CompletableFutureTask.getFuture(TaskCompletableFuture)` 传入匿名桥接（`one()`→executeTask、`all()`→executeTasksExceptionally），让任务能在 CF 世界里编排子任务；onReady 与 onRunning **连发**；完成后 setResult/onDone/onFinished/SUCCEEDED；异常分支中取消/中断时 `setException(null)`（取消不算错误）。
- 注意：**`Task.run()`（同步直跑）与 AsyncTaskExecutor 路径互斥**——执行器直接调 `execute()`，`onDone`/listener 由执行器触发；`Task.run()` 只用于手工递归执行场景。

**Schedulers.java**：`io()` 懒加载单例 8 线程 "IO" 池（10s 存活回收）；`androidUIThread()` 返回 `Handler(Looper.getMainLooper())::post`；`defaultScheduler()` 返回 `ForkJoinPool.commonPool()`；`shutdown()` 用 `shutdownNow` 中断全部线程（退出应用时避免进程驻留）。

**FetchTask.java**（抽象网络抓取基类）：
- 构造时 `setExecutor(download())` —— 所有下载跑在静态专用 "Download" 池（默认 `min(CPU*4, 64)` 线程，`setDownloadExecutorConcurrency` 可动态调整，注意先 setCore 后 setMax/反之的顺序是为了规避 TPE 的 IllegalArgumentException）。
- `execute()`：按 URL 顺序 × retry（默认 3）双重循环；ETag 三态（`shouldCheckETag()` 返回 CHECK_E_TAG/NOT_CHECK_E_TAG/CACHED，**CACHED 时 execute 直接 return 静默成功**）；`NetworkUtils.resolveConnection` 处理重定向，304 → 读缓存 `useCachedResult`，缓存损坏则 `removeRemoteEntry` 且 `retryTime--` 强制重连；4xx 直接放弃该 URL（FileNotFoundException 不重试），其余非 2xx 抛 ResponseCodeException；读流循环中轮询 `isCancelled()`，完成后校验 `downloaded != contentLength` 抛 IOException（源码注释解释了取消与大小校验的竞态窗口）；失败重试带**退避序列 500ms/2s/5s**（`awaitRetryDelay` 分片 sleep 以便快速响应取消，取消优先于重试）；最终异常包装为带 URL 的 `DownloadException`。
- 静态 `speedEvent`（独立 EventBus）+ 常驻 `Timer`：每秒把全局原子累加器以 `SpeedEvent` 派发（即使无下载也发 0），TaskDialog 用 `registerWeak` 订阅显示网速。
- 内部类：`Context`（Closeable，`withResult(true)` 标记成功，close 时才落盘）、`DownloadState`、空的 `DownloadMission` 占位。

**GetTask.java**：文本抓取，恒 CHECK_E_TAG，结果按 charset 读入内存；`thenGetJsonAsync` 在 JsonParseException 时**清掉对应 ETag 缓存**（注释：防止被截断正文连同 ETag 一起写坏缓存）。

**FileDownloadTask.java**：下载到临时文件（RandomAccessFile + 可选 MessageDigest 边写边算）；Context.close() 中：失败删临时文件 → 依次跑 `IntegrityCheckHandler`（内置 `ZIP_INTEGRITY_CHECK_HANDLER` 用只读 zip 文件系统试探完整性）→ 删旧文件、建目录、move 到位 → `performCheck` 校验和（不符抛 ChecksumMismatchException）→ 按 caching/ETag 写入 CacheRepository。`shouldCheckETag`：caching+有校验和时先查本地缓存命中（直接 copy 并返回 CACHED），否则 NOT_CHECK_E_TAG，否则 CHECK_E_TAG。

**TaskListener.java**：回调族 `onStart/onReady/onRunning/onFinished/onFailed(默认转 onFinished)/onStop(success, executor)/onPropertiesUpdate`。**TaskEvent**：携带 Task + failed 标志，发在每任务自己的 `onDone` EventManager 上，而非全局总线。

### 1.3 生命周期与线程模型总结

- **组合路径**：`Task.runAsync(...)` 创建 SimpleTask → `.withStage("x")` 包 StageTask → `.thenComposeAsync(...)`/`thenApplyAsync` 逐级构建 dependents/dependencies 图 → `.start()` 创建 AsyncTaskExecutor 异步执行（或 `.test()` 阻塞）。
- **线程**：流水线胶水（thenCompose/thenApply 无参版本）跑在 **ForkJoinPool.commonPool**；`execute()/preExecute()/postExecute()` 跑在 **task.getExecutor()**（普通任务默认也是 commonPool；FetchTask 是 Download 池）；**所有监听器回调与 onDone 事件都在完成线程上同步执行，绝不自动切 UI 线程**——UI 侧（如 TaskListPane、TaskDialog）自行用 `Schedulers.androidUIThread().execute(...)` 包装；只有 progress/message 属性更新被 `InvocationDispatcher` 自动合并投递到主线程。
- **回调时序**：`onReady`（READY，preExecute 与 dependents 之前）→ `onRunning`（RUNNING，execute 前）→ `onFinished`（SUCCEEDED，dependencies+postExecute 之后）或 `onFailed`；`onStart/onStop` 是整链级；`whenComplete` 的回调在上游任务**作为 dependent 成功后**、由包装任务的 execute 触发，异常透传。

## 二、event 包：轻量事件总线

- **Event**：source + 可取消标志（`setCanceled` 前检查 `isCancelable()`，默认 false，否则抛 UnsupportedOperationException）+ `Result`（DENY/DEFAULT/ALLOW，需 `hasResult()` 覆写为 true 才能设置）。
- **EventBus**：`ConcurrentHashMap<Class, EventManager>`，`channel(Class)` computeIfAbsent 懒建；静态单例 `EVENT_BUS`；`fireEvent(Event)` 按**具体类**找 channel（不做父类层级匹配），且每条事件打 INFO 日志（量大时很吵）。典型发布者：`DefaultGameRepository` 发 `RefreshedVersionsEvent`。
- **EventManager<T>**：`SimpleMultimap<EventPriority, Consumer<T>, CopyOnWriteArraySet>`（EnumMap 按优先级键）。`register` 按 (priority, consumer) 去重；`fireEvent` 为 **synchronized**，按 `EventPriority` 枚举序 **HIGHEST→HIGH→NORMAL→LOW→LOWEST**（与 Bukkit 相反：HIGHEST 先跑）依次同步调用，同优先级按插入序；有 Result 语义时返回结果。所有 handler 都在**调用者线程**同步执行，无任何线程切换保证。
- **弱引用**：`registerWeak` 包一层内部类 `WeakListener`（持 `WeakReference<Consumer>`），派发时发现引用被回收则**惰性自摘**（`unregister(this)`）。**坑**：WeakListener 与原 consumer 不是同一实例，`unregister(consumer)` 因 equals 不匹配**摘不掉 weak 注册**——WebActivity 有中文注释明确指出此问题，UI 层（ManageUI）用 `listenerHolder` 列表保强引用来防误回收。
- 具体事件：`FailedEvent<T>`（failedTime+newResult，供重试场景改写结果）、`GameJsonParseFailedEvent`（版本 JSON 损坏，允许监听者修复后置 ALLOW）、`RemoveVersionEvent`/`RenameVersionEvent`（hasResult=true，可 DENY 阻止操作）、`RefreshedVersionsEvent`。整体是"同步、按类分发、优先级有序、可选取消/裁决"的极简事件总线。

## 三、launch 包：启动器核心（Android 特化）

### 3.1 类清单

- **DefaultLauncher.java**：持 `Context`（Android）、`GameRepository`、`Version`、`AuthInfo`、`LaunchOptions`；`forbiddens` 映射（"-Xincgc" 在 Java≥9 时禁用，命令行收尾 `removeIf`）。核心是私有 `generateCommandLine()`（约 200 行）与公开 `launch()`。
- **CacioJavaArgs.java**：静态工具，为 Caciocavallo（Android 上的 AWT 模拟层）组装 JVM 参数，游戏与 Jar 执行器共用：Java 8 走 `net.java.openjdk.cacio.ctc` + `-Xbootclasspath/p`，Java 17 走 `com.github.caciocavallosilano.cacio.ctc` + `-Xbootclasspath/a` + 一大串 `--add-exports/--add-opens` + 可选 CTCPreloadClassLoader + `-javaagent:cacio-agent.jar`；cacio jar 从 `FCLPath.CACIOCAVALLO_8/17_DIR` 目录扫描拼接。
- **InjectorMap.java**：纯数据载体，`MapInfo(id, Argument)`，`Argument` 按 `LibraryAnalyzer` 探测的加载器类型（Forge/NeoForge/Fabric|LegacyFabric|Quilt/Vanilla）四选一返回注入参数。

### 3.2 启动流程骨架

1. **变量收集**（由 `fcl/game/LauncherHelper` 编排，launch 包只负责命令行与执行）：LauncherHelper 用 Task 链完成版本维护（MaintainTask）→ mod 全量扫描 → 状态检查 → 依赖补全 → 登录，最终从库列表提取 jna/lwjgl 版本（LWJGL 新旧声明并存时取最高）后 `new FCLGameLauncher(...)`（DefaultLauncher 的子类，补充 `${launcher_name/version}` 占位符、生成 options.txt/splash.properties、针对 GL4ES 渲染器改写 Sodium/Rubidium 等模组配置关 shader）并调 `launch()`。
2. **命令行构建** `generateCommandLine()`：
    - 最前插入 Cacio 参数；用户 JVM 参数（过滤字面量 "noXmx"）；`-Xmx/-Xms` 用 `addDefault`（不覆盖用户显式值）；file.encoding 及 JDK19 前后不同的 stdout/stderr encoding 属性；**log4j2 RCE 三连缓解**（useCodebaseOnly、两个 trustURLCodebase=false、formatMsgNoLookups=true，≥1.7 且允许时挂自定义 configurationFile）；`-Dminecraft.client.jar`；32 位设备强制 `-Xss 1m`（注释：1.13 栈溢出）；`ActiveProcessorCount`。
    - FCL 专属大段 `-D`：`-Dos.name=Linux`、`-Dos.version=Android-xxx`、LWJGL 各 so 路径（`gl/egl` 用 `${gl_lib_name}` 占位符留给 native 侧替换，openal 取 apk nativeLibraryDir，freetype/lwjgl.librarypath 指向 `LWJGL_DIR/<版本>/natives/<abi>`）、`user.home=游戏目录`、locale/timezone、`Process.launchMechanism=FORK`、`cpu.name=SoC 名`、NativeLibPlugin 环境批量注入、jna.boot.library.path（按 jna 版本目录优先）；1.7.2 Forge 加 `-Dsort.patch=true`；MioLibPatcher 启用时挂 `-javaagent`。
    - 类路径：`repository.getClasspath()` + `addLWJGLClassPath()`（LWJGL jar 置顶，<3.0 追加 lwjgl-lwjglx.jar 兼容旧接口）+ MioLaunchWrapper + 版本 jar（缺失时回退 `inheritsFrom` 的 jar）。
    - 参数模板替换：`getConfigurations()` 生成 `${auth_player_name}` 等官方占位符；`${natives_directory}` 先**自映射**（put 自身为值）让解析器保留 token，随后对 `io.netty.native.workdir`/`jna.tmpdir`/`SharedLibraryExtractPath` 三项后处理替换为 CACHE_DIR——非显然技巧。JVM 参数来自版本 JSON（或 DEFAULT_JVM_ARGUMENTS）与 AuthInfo；非 Java8 加 `--add-exports 主类包=ALL-UNNAMED`；主类前**插入 `mio.Wrapper` 包装器**再接真实 mainClass；老版 `minecraftArguments` 分词替换，新版 game args 带 features（仅 `has_custom_resolution`）；服务器直连按 1.20 分界用 `--server/--port` 或 `--quickPlayMultiplayer`。
3. **native 库处理与执行** `launch()`：命令行逐 token 校验非空白；`extractLog4jConfigurationFile()`（1.7/1.12 × debug 四套资源文件按版本分发到版本根目录）；`LibraryAnalyzer.analyze` 探测七个加载器装配进 `FCLConfig`；渲染器与 VK 驱动选项写入 config；最终 **`FCLauncher.launchMinecraft(config)` 返回 `FCLBridge`** —— 不经 OS 进程，命令行交给 FCL native 桥执行（这也是命令行里大量 `${...}` 占位符、自映射 trick 存在的原因：真正的值由 native/JNI 层二次解析）。`setLwjglVersion` 会钳制：<3.0 置 useLwjglX、≥3.4.1 钉在 3.4.1、否则默认 3.3.3。

### 3.3 值得注意的细节与坑

- `thenApplyAsync(Executor, fn)` 等便捷重载会**强制把 significance 设为 MODERATE**，链式调用 `setSignificance` 需放在其后。
- 取消语义：中断被视作取消（`convertInterruptedException`）；取消时 `exception` 置 null/不算失败，但 `onDone(failed=true)` 与 `onFailed` 仍会触发，UI 需按异常类型区分。
- `AsyncTaskExecutor` 的 `exception` 字段只保存**最后一个**任务异常；`cancel()` 前必须 start。
- `EventManager.fireEvent` synchronized：同一 channel 内慢 handler 会阻塞该 channel 所有事件；但同 channel 内重入派发安全（可重入锁）。
- `EventBus.fireEvent` 每条事件打 INFO 级日志，事件频繁时日志噪音明显。
- FetchTask 的静态 Timer 与下载线程池是进程级单例，永不关闭；`DownloadMission` 是空类占位。
- FileDownloadTask 的校验和/zip 完整性检查发生在 `Context.close()` 内（try-with-resources 触发），因此损坏文件会走统一的 IOException 重试路径，最终包成 DownloadException——重试次数因此涵盖"下完了但校验失败"的情形。
