# fakefx（JavaFX 属性体系）彻底移除执行方案

> 状态：调研完成，待用户拍板决策点后执行。
> 分支：`feature/miuix-theme-fix`。
> 调研日期：2026-08-02。所有数据均用 Grep/Read 实证，命令可复现。
> 边界声明：本方案只规划移除 fakefx；控件系统（`control/`）与 WebView 保留；6.1 旧代码清理（见 `final-report.md` §6）按本文 §四 的合并点协同执行。

---

## 一、现状盘点（实证数据）

### 1.1 fakefx 包规模与结构

位置：`FCLCore/src/main/java/com/tungsten/fclcore/fakefx/`，**292 个 Java 文件，44,324 行，2.1MB**。

| 子包 | 文件数 | 外部引用（FCL+FCLCore 非 fakefx 的 import 数） | 结论 |
|---|---|---|---|
| `beans/property` | 62 | 全部被使用类的主要来源（见 1.2） | 保留语义，需替代 |
| `collections`（含 transformation） | 45 | 高（ObservableList 34、FXCollections 21…） | 保留语义，需替代 |
| `beans/value` | 27 | 低（ChangeListener/ObservableValue/WeakChangeListener 等 8） | 保留语义，需替代 |
| `beans/binding` | 26 | 仅 Bindings 19、ObjectBinding 9、StringBinding 1 | 门面被用，内部实现可弃 |
| `beans`（根） | 7 | Observable 24、InvalidationListener 32、WeakInvalidationListener 5 | 保留语义，需替代 |
| `binding`（顶层） | 22 | **0** | 纯内部实现 |
| `event` | 22 | **0** | 纯内部实现 |
| `reflect` | 4 | **0** | 纯内部实现 |
| `property` + `property/adapter` | 3+9 | **0** | JavaBean 反射适配，纯内部 |
| `beans/property/adapter` | 31 | **0** | JavaBean 反射适配，纯内部 |
| `util`（含 converter） | 7 | StringConverter 2 | 仅 StringConverter 被用 |

另有两个 fakefx 耦合的辅助包（在 fakefx 目录之外，同属移除范围）：

- `FCLCore/src/main/java/com/tungsten/fclcore/util/fakefx/`：8 文件 / 52K（ObservableHelper、BindingMapping、MappedObservableList、PropertyUtils、ObservableCache 等）。
- `FCLCore/src/main/java/com/tungsten/fclcore/util/gson/fakefx/`：21 文件 / 81K（`JavaFxPropertyTypeAdapterFactory`、ObservableList/Map/SetCreator 等 Gson 适配）。

### 1.2 被外部使用的 API 表（import 计数，外部 = FCL 98 文件 + FCLCore 9 文件，共 107 文件）

| 类 | 外部 import 数 | 总引用点数（词级） | 主要使用方 |
|---|---|---|---|
| `collections.ObservableList` | 34 | 158 | FCL setting/control/ui，FCLCore Datapack |
| `beans.InvalidationListener` | 32 | 141 | 全部业务状态类 |
| `beans.property.SimpleObjectProperty` | 27 | 90 | Config、control/data、账户 |
| `beans.property.ObjectProperty` | 26 | 115 | 同上（API 签名） |
| `beans.Observable` | 24 | 72 | Account、Config、control/data |
| `beans.property.BooleanProperty` | 23 | 131 | Config、VersionSetting、LocalModFile |
| `collections.FXCollections` | 21 | 99 | 静态工厂调用点 |
| `beans.binding.Bindings` | 19 | 89 | 旧 View 绑定、Account 派生值 |
| `beans.property.StringProperty` / `SimpleStringProperty` | 18 / 17 | 92 / 83 | Config、control/data |
| `beans.property.SimpleBooleanProperty` | 18 | 86 | 同 BooleanProperty |
| `beans.property.IntegerProperty` / `SimpleIntegerProperty` | 14 / 11 | 150 / 83 | Config、control/data |
| `beans.binding.ObjectBinding` | 9 | 26 | Account、旧 UI |
| `collections.ObservableSet` / `ObservableMap` | 6 / 6 | 17 / 17 | Config |
| `beans.property.Property` | 6 | 49 | 桥接层 `toMutableStateFlow` |
| `beans.property.ListProperty` / `SimpleListProperty` | 6 / 5 | 27 / 18 | Profiles、Controllers |
| `beans.property.ReadOnlyListWrapper` / `ReadOnlyListProperty` | 4 / 4 | 8+ | Controllers |
| `util.StringConverter` | 2 | 8 | 旧 UI |
| `beans.value.ChangeListener` / `ObservableValue` | 2 / 2 | 27 / 30 | 桥接层 |
| 其余 ~30 个类（Long/Float/Double 系、ReadOnly 系、Weak 系、SetProperty/MapProperty 等） | 各 1–2 | 少量 | 散点 |

合计：**约 50 个类被外部使用，约 1,700 个引用点**；其中前 20 个类覆盖绝大部分。引用形态高度集中：`SimpleXxxProperty` 字段 + `xxxProperty()` 访问器 + `InvalidationListener` 监听 + `FXCollections.observableArrayList` + 少量 `Bindings.*` 派生。

### 1.3 FCLCore 公开 API 暴露点（9 个文件）

| 文件 | 暴露的 fakefx 类型 | 消费方 |
|---|---|---|
| `task/Task.java` | `ReadOnlyDoubleProperty progressProperty()`、`ReadOnlyStringProperty messageProperty()` | FCL 13 个文件（任务进度 UI） |
| `auth/Account.java` | `implements Observable`、`BooleanProperty portableProperty()`、`ObjectBinding`（Bindings 派生） | FCL ui/account |
| `auth/authlibinjector/AuthlibInjectorServer.java` | `implements Observable` + add/removeListener | FCL ui/account、setting |
| `auth/{microsoft,offline,yggdrasil}/*Account.java` | 覆写上述绑定 | 同上 |
| `mod/LocalModFile.java` | `BooleanProperty activeProperty()` | FCL ui/manage（Mod 列表） |
| `mod/Datapack.java` | `ObservableList<Pack> getInfo()`、`Pack.activeProperty()` | FCL ui/manage |
| `util/Holder.java` | `implements InvalidationListener` | FCLCore 内部工具 |

注：FCLCore 其余公开 API（DownloadProvider、版本/下载/启动链路）**不暴露** fakefx 类型——红线的游戏启动链路不经 fakefx 传递状态。

### 1.4 红线边界确认

`grep -rl "com.tungsten.fclcore.fakefx"` 结果：**FCLauncher / Terracotta / LWJGL-Pojav / ZipFileSystem / NG-GL4ES 均为 0**。
且模块依赖方向为 `FCLCore → FCLauncher / ZipFileSystem`（FCLCore/build.gradle.kts:43-44），上游模块结构上不可能引用 fakefx。**红线干净，移除工作不需要触碰任何启动链路模块。**

### 1.5 FCL 模块使用分类（98 文件）

| 分类 | 文件数 | 明细 | 处置 |
|---|---|---|---|
| 旧 View 绑定（随 6.1 删除自然消失） | ~39 | `FXUtils.*` 的 39 个引用方 + `util/FXUtils.java`、`util/WeakListenerHolder.java`、旧 Page/Dialog 中的 `bind*/Bindings` | **不迁移**，6.1 删除时一并消失 |
| Compose 桥接（迁移期接缝） | 13 | `ui/bridge/*`（FakeFxStateFlow/FakeFxCompose/LegacyBridge）、`ui/compose/*` 10 个 dialog/page | 随数据层改造逐点改写为 StateFlow，最后删桥 |
| 业务状态（必须重写） | 8+ | `setting/`（Config、Accounts、Controllers、Controller、Profiles、Profile、VersionSetting、MenuSetting、DownloadProviders）、`game/` 2、`activity/MainActivity.kt`、`terracotta/Terracotta.java` | 核心工作量 |
| 控件系统 control/（保留红线，就地重写） | ~20 | `control/data` 11（属性化数据类）、`control/view` 3、对话框若干、`GameMenu`、`Gyroscope` | 就地换属性实现，行为不变 |
| FCLCore 联动 | 9 | 见 1.3 | 改签名 + 同步 FCL 消费方 |

### 1.6 持久化耦合（关键风险实证）

- **控件布局 JSON 已解耦**：`control/data` 各类（如 `BaseInfoData.java:227`）均带手写 `@JsonAdapter` Serializer/Deserializer，磁盘格式是值级 JSON（实证：`FCL/src/main/assets/controllers/00000000.json` 中 `baseInfo` 为纯值）。替换属性实现**不改变**控制柄布局格式。
- **Config/Controller 体系依赖 `JavaFxPropertyTypeAdapterFactory`**：使用方为 `Config.java`、`Controller.java`、`Controllers.java`、`ControllerDownloadPage.java`、`ControllerRepoPage.java`（5 处）。该工厂把 Property 字段序列化为其当前值，**JSON 格式与属性类型无关**——只要替代实现配一个等价 TypeAdapterFactory，磁盘配置格式可保持不变。
- `Accounts.java:157`、`Controllers.java:31` 使用 `FXCollections.observableArrayList(extractor)` 的 **extractor 模式**（元素自身失效时冒泡列表事件），替代实现必须覆盖此语义（账户/控制器列表的"改元素即存盘"依赖它）。

### 1.7 现有桥接层（迁移接缝）

`FCL/src/main/java/com/tungsten/fcl/ui/bridge/FakeFxStateFlow.kt`：`ObservableValue→StateFlow`、`Property→MutableStateFlow`（双向，带回环防护）。`FakeFxCompose.kt` 提供 Compose State 适配。迁移策略上这是**已就位的转换边界**：数据层换成 StateFlow 后这些适配器自然失去存在意义。

---

## 二、替代技术选型评估

| 方案 | 说明 | 优点 | 缺点 | 结论 |
|---|---|---|---|---|
| a) Kotlin StateFlow/SharedFlow | 数据层直接改为 Flow，Compose 原生消费 | 与 Compose 终态一致；无自研代码；桥接层可删 | FCLCore 是 Java 为主（仅 6 个 kt 文件），公开 API 暴露 Flow 对 Java 调用方不友好；FCLCore 未声明 coroutines 依赖（FCL 经 Compose 栈间接获得）；`Task.progressProperty()` 这类 Java 消费方（13 文件）需全部 Kotlin 化或加 `kotlinx-coroutines` + 包装 | **终态方向，但不适合作为 FCLCore 内部 API 的第一步** |
| b) 自研极简 observable（API 兼容子集） | 新建 `fclcore/observable/`（或就地精简 fakefx 包），实现 1.2 表中 ~20 个核心类：Observable/InvalidationListener、ObservableValue/ChangeListener、Property 及 boolean/int/String/Object 变体、ObservableList/Set/Map（含 extractor 与 ListChangeListener）、FXCollections 工厂、StringConverter | 调用点几乎零改动（改 import 即可）；可分文件渐进替换；extractor/Weak listener 语义可控；体量可从 44k 行压到约 2–3k 行 | 自研代码需要测试兜底；语义偏差风险（如 listener 触发时机、Weak 引用行为） | **推荐作为第一步（换引擎不换车身）** |
| c) 直接重写调用点 | 1,700 个引用点逐处改为 StateFlow/普通字段+回调 | 一步到位无中间态 | 单 PR 改动面巨大、不可评审；Config 存盘语义（invalidated→save）散在 100+ 文件，回归风险最高 | 不推荐单独采用；**作为 b 之后逐域收敛的终态** |

**推荐路线：b →（逐域）a/c 收敛 → 删除。** 即先做"API 兼容的极简 observable"替换掉 44k 行 fakefx（调用点只改 import），再按域把业务状态收敛到 StateFlow/显式回调，最终连极简 observable 也一并删除。这样每一步都可独立编译、独立验收、独立回滚。

---

## 三、分阶段移除方案

排序原则：依赖最少、可独立验证先行；每阶段门禁 = 全量编译通过 + 该域既有功能冒烟（无真机项沿用 `final-report.md` §5 的声明，标记为"待真机"）。

### 阶段 0：6.1 旧代码清理（前置合并点）

- **范围**：`final-report.md` §6 清单（6.1-A 立即删除批 + 6.1-B 开关固化批）。
- **与 fakefx 的关系**：39 个 `FXUtils` 引用方、旧 Page/Dialog 中的 Bindings/bind 调用、`FXUtils.java`、`WeakListenerHolder.java`、`ui/TaskDialog.java`（旧）等 ~40 个文件的 fakefx 引用**随之自然消失**，约占 FCL 侧 fakefx 引用面的 40%。
- **门禁**：同 6.1 既定门禁（开关固化需真机验收）。
- **工作量**：沿用 6.1 估算，本方案不重复计。
- **产出**：FCL 侧 fakefx 引用从 98 文件收敛到约 55–60 文件，剩余全部是"必须重写"的业务状态/控件/桥接。

### 阶段 1：自研极简 observable（换引擎）

- **范围**：新建 `FCLCore/src/main/java/com/tungsten/fclcore/observable/`，实现 1.2 表的核心语义子集：
  - `Observable` / `InvalidationListener` / `WeakInvalidationListener`
  - `ObservableValue<T>` / `ChangeListener<T>` / `WritableValue<T>`
  - `Property<T>` + `SimpleObjectProperty`、`SimpleString/Boolean/Integer/Long/Float/DoubleProperty`（含 bean/name 两参构造与 `ReadOnlyXxxWrapper` 对）
  - `ObservableList/Set/Map` + `ListChangeListener`（`Change` 的 wasAdded/wasRemoved/addedSubList 子集）+ `FXCollections.observableArrayList()`（含 extractor 变体）、`observableSet/observableMap`、`unmodifiableObservableList`
  - `StringConverter`（如阶段 0 后仍有消费方）
- **包名决策**：若保持 `com.tungsten.fclcore.fakefx.*` 包名不动、就地替换文件内容，则 107 个调用文件零改动，但包名名不副实；若新建 `observable` 包，则调用点需批量改 import（机械操作，107 文件）。**建议新包名 + 批量改 import**（一次 sed 级操作， diff 干净，且彻底切断与"javafx"字样的关联——这正是用户的最终目标）。此为一个决策点（见 §五 D1）。
- **改动模式**（旧→新对照）：

  ```java
  // 旧
  import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
  // 新（仅包名变化，API 逐方法对齐）
  import com.tungsten.fclcore.observable.property.SimpleBooleanProperty;
  ```
- **门禁**：FCLCore/FCL 全量编译；Config 读写回环冒烟（改设置→杀进程→重启→值保持）；账户列表增删改冒烟；**自研 observable 需配单元测试**（property set/get/listener、list extractor 冒泡、weak listener 不泄漏，~15 个用例）。
- **风险**：listener 触发语义偏差（如同值 set 是否触发、bind 单向同步时机）；Weak listener 被 GC 提前回收导致的"存盘失灵"。对策：语义对齐测试先行，再批量替换。
- **预估**：核心实现 + 测试 3–4 天；批量替换 + 排雷 2–3 天。
- **本阶段结束状态**：`fakefx/` 44k 行、`util/fakefx/`、`util/gson/fakefx/` 中不再被引用的部分**可整目录删除**（注意：`JavaFxPropertyTypeAdapterFactory` 需先移植为面向新属性类型的等价工厂，Config/Controller 5 处消费方同步切换，磁盘 JSON 格式不变）。

### 阶段 2：FCLCore 公开 API 去 fakefx 化（9 文件）

- **范围**：1.3 表全部。改造模式：

  | 旧写法 | 新写法 |
  |---|---|
  | `Task.progressProperty(): ReadOnlyDoubleProperty` | `Task` 增加 `addProgressListener(DoubleConsumer)` / 或直接让 FCL 侧经回调轮询；`messageProperty()` 同理（`Consumer<String>`） |
  | `Account implements Observable` + `addListener(InvalidationListener)` | 自定义 `interface AccountListener { void onChanged(); }` 或保留极简 Observable（若阶段 1 产物仍在用） |
  | `LocalModFile.activeProperty(): BooleanProperty` | `isActive()/setActive(boolean)` + `addActiveChangeListener(Consumer<Boolean>)` |
  | `Datapack.getInfo(): ObservableList<Pack>` | `List<Pack>` + 显式 `addInfoChangeListener` |
  | `Holder implements InvalidationListener` | 改为 `Runnable`/`Consumer` 语义 |

- **门禁**：FCLCore 编译；FCL 13 个 Task 进度消费方（`ui/download`、`MiuixTaskDialog/FCLTaskDialog` 等）同步改完并编译；任务进度 UI 冒烟（下载/安装任务进度条与文案刷新正常，**启动游戏链路 A1–A5 回归**——Task 是启动链路边上的状态通道，见 §四）。
- **风险**：`TaskExecutor`/`TaskListener` 与 property 的耦合点在 executor 内部（进度上报走 wrapper.set），改造时需保留"后台线程 set → UI 线程收到"的既有线程模型（fakefx 不做线程切换，现状消费方自行切线程，新实现也不应隐式切换）。
- **预估**：2–3 天。
- **与 6.1 的合并**：若 6.1-B 批删除了旧 TaskDialog 的全部调用点，本阶段 FCL 侧消费方数量会减少，宜排在 6.1 之后。

### 阶段 3：FCL 业务状态层（setting/game/activity/terracotta，~13 文件）

- **范围**：`Config.java`（字段最多：String/Boolean/Integer/Map/Set/List 属性全套 + `ObservableHelper` 存盘联动）、`Accounts.java`、`Controllers.java`、`Controller.java`、`Profiles.java/kt`、`Profile.java`、`VersionSetting.kt`、`MenuSetting.kt`、`DownloadProviders.java`、`TexturesLoader.java`、`FCLCacheRepository.java`、`MainActivity.kt`、`Terracotta.java`。
- **改动模式**（以 Config 为例）：

  ```java
  // 旧：属性字段 + invalidated→save 联动
  private BooleanProperty autoDownloadThreads = new SimpleBooleanProperty(true);
  // 新（两种取向，逐域择一）：
  // 3a. 保留极简 observable 形态（阶段 1 产物），仅换 import —— 零行为变化；
  // 3b. 终态：普通字段 + 显式 onChanged 回调 / StateFlow，
  //     Compose 侧 collectAsStateWithLifecycle，Java 侧注册回调。
  ```
- **门禁**：配置持久化回环（含 Map/Set 类型字段）；账户选择/新增/删除全流程；控制器仓库页列表刷新；Gson 序列化产物与旧格式逐字段 diff 一致（用 `JavaFxPropertyTypeAdapterFactory` 的等价新工厂，已有用户配置不能丢）。
- **风险**：**这是全方案回归风险最高的一阶段**——`observableArrayList(extractor)` 的"元素失效冒泡→存盘"语义一旦丢失，表现为"改了设置不保存"。必须有针对 Accounts/Controllers 的存盘冒烟。
- **预估**：4–6 天（Config 单文件约 1–1.5 天）。
- **拆分建议**：3a（换 import 保行为）与 3b（收敛 StateFlow）分两个 PR；3b 可按域再拆（setting 域 / account 域 / controller 域）。

### 阶段 4：控件系统 control/ 就地重写（~20 文件）

- **范围**：`control/data` 11 个属性化数据类、`control/view` 3、对话框与 `GameMenu`、`Gyroscope`。
- **约束**：control/ 是保留红线，**行为与磁盘布局格式均不可变**。`@JsonAdapter` 手写序列化器已把磁盘格式与属性类型解耦（1.6 实证），本阶段只换内存属性实现。
- **改动模式**：同阶段 3 的 3a→3b；`ControlViewGroup`/`GameMenu` 中对 data 类的 `addListener(onInvalidating(...))`（视图刷新联动）替换为等价回调。
- **门禁**：控制柄编辑（拖动/改属性即时刷新）、布局保存/加载回环、内置 `00000000.json` 加载无损；游戏内控制柄显示冒烟（待真机）。
- **预估**：3–4 天。
- **与 6.1 的合并**：6.1-B 会删除一批旧控制柄对话框（AddButtonStyleDialog 等旧 View 版），可减少本阶段文件数，宜排在 6.1 之后。

### 阶段 5：桥接层与终态收敛（删桥）

- **范围**：`ui/bridge/FakeFxStateFlow.kt`、`FakeFxCompose.kt`、Compose 侧 10 个仍 import fakefx 的 dialog/page。
- **内容**：数据层完成 3b/4 后，Compose 侧直接消费 StateFlow/回调；逐点删除 `toStateFlow()/toMutableStateFlow()` 调用；最后删除桥接文件与 `ui/bridge/example/`。
- **门禁**：Compose 各表单（下载线程数滑杆等双向绑定场景）读写回环。
- **预估**：1–2 天。

### 阶段 6：终态删除与收尾

- 删除：`fclcore/fakefx/`（若阶段 1 未删净）、`util/fakefx/`、`util/gson/fakefx/`、极简 observable 中已无消费方的类（若全面收敛到 StateFlow，可整个删）。
- 全仓 grep 断言：`grep -rn "fakefx\|javafx" --include="*.java" --include="*.kt" --include="*.kts" FCL FCLCore` 结果为空。
- 更新 `docs/migration/`（baseline、final-report）与 AGENTS.md（如有时）。
- **预估**：0.5–1 天。

### 阶段总览

| 阶段 | 范围 | 依赖 | 预估 | 可与 6.1 合并 |
|---|---|---|---|---|
| 0 | 6.1 旧代码清理 | 真机验收 | 沿用 6.1 | 即 6.1 本身 |
| 1 | 极简 observable + 批量换 import | 无（可最先做） | 5–7 天 | 否 |
| 2 | FCLCore 9 文件 API 改造 | 阶段 1（或与其并行设计） | 2–3 天 | 部分（TaskDialog 调用点） |
| 3 | FCL 业务状态层 | 阶段 1、2 | 4–6 天 | 否 |
| 4 | control/ 就地重写 | 阶段 1 | 3–4 天 | 部分（旧控制柄对话框） |
| 5 | 删桥接层 | 阶段 3、4 | 1–2 天 | 否 |
| 6 | 终态删除 + 断言 | 全部 | 0.5–1 天 | 否 |

合计约 16–24 人天（不含 6.1 与真机验收）。阶段 1+2 可与阶段 0 并行启动（不依赖真机），阶段 3/4 建议在 6.1 之后以缩小改动面。

---

## 四、红线分析：对游戏启动链路的影响

1. **模块边界**：fakefx 仅在 FCLCore 与 FCL 内；FCLauncher（原生启动/JNI）、Terracotta、LWJGL-Pojav、ZipFileSystem、NG-GL4ES 零引用，且依赖方向倒置（FCLCore 依赖 FCLauncher），**启动链路的 native/渲染/文件系统层不受影响**（1.4 实证）。
2. **FCLCore 启动相关 API 的暴露面**：9 个暴露 fakefx 的文件中，与启动链路相邻的只有 `Task`（启动游戏时的进度/文案通道，`MainActivity`/启动流程经 `Task.executor` 消费 progress/message）。改造 `Task` 签名时：
   - **线程模型必须保持不变**：现状为后台线程 `wrapper.set()`、消费方自行切 UI 线程；新回调机制同样不做隐式线程切换，避免引入"回调在 UI 线程外触发 Compose 状态写"的新崩溃面。
   - `TaskListener`/`TaskExecutor` 的契约（onStart/onFinish/onFailed/onInterrupted）不动，只动 progress/message 两个 property 通道。
3. **配置兼容**：`config.json`、控制器仓库配置、账户存储的磁盘格式经 Gson 值级序列化，替换属性实现时配以等价 TypeAdapterFactory 即可保持字节级兼容；**不做格式迁移、不需要用户数据升级脚本**。门禁中加入"旧配置文件加载→保存→与原文 diff"用例。
4. **回滚策略**：每阶段独立 PR；阶段 1 的回滚 = 还原 import 批量替换 + 恢复 fakefx 目录（git revert 即可，无数据迁移，回滚零成本）。这是"b 先行"路线相对"c 直接重写"的最大优势。
5. **遗留风险（诚实声明）**：本工程全程无真机（`final-report.md` §边界声明），控制柄游戏内显示、启动全链路 A1–A5 只能编译期 + 代码审查兜底，真机项必须在发布前补齐。

---

## 五、决策点清单（需用户拍板）

- **D1 包名策略**：阶段 1 新建 `fclcore.observable` 包并批量改 107 文件 import（推荐：彻底去除 fakefx/javafx 字样），还是保留 `fakefx` 包名就地替换文件内容（调用点零改动但字样残留）？
- **D2 终态形态**：业务状态层（Config/Accounts/Controllers/control-data）最终收敛到 **StateFlow**（Compose 原生、需给 FCLCore 显式引入 kotlinx-coroutines 依赖、Java 消费方用 `StateFlow.getValue()` 略别扭），还是**普通字段 + 显式监听器回调**（Java/Kotlin 两侧都自然，Compose 侧需一层薄包装）？推荐：FCLCore 内部用"普通字段+回调"，FCL 的 Compose 边界用 StateFlow。
- **D3 阶段 0 前置性**：是否坚持"6.1 清理完成后再动 fakefx"？推荐：阶段 1（自研 observable）与 6.1 并行，阶段 2–4 等 6.1 落地，以缩小改动面。
- **D4 extractor 语义取舍**：`Accounts`/`Controllers` 依赖 `observableArrayList(extractor)` 的冒泡存盘。是自研 observable 完整实现 extractor（推荐，行为不变），还是借机改为显式 `save()` 调用（语义更显式，但要人工找全所有"改元素"路径，漏一个就是不存盘）？
- **D5 Bindings 派生表达式的处理**：89 个 `Bindings.*` 引用点多数在旧 View（阶段 0 消失），剩余少数（Account 的 `ObjectBinding` 派生等）是手写派生逻辑替换（推荐）还是在极简 observable 中实现 `Bindings` 门面子集？
- **D6 工作量与排期**：16–24 人天的估算是否接受；是否允许阶段 1 先行合并（它独立就有 44k→~3k 行的删减收益，即使后续阶段缓行也不亏）。

---

## 附：复现命令

```bash
# 各模块 fakefx 引用
for m in FCL FCLCore FCLauncher Terracotta LWJGL-Pojav ZipFileSystem NG-GL4ES; do
  echo "$m: $(grep -rl 'com.tungsten.fclcore.fakefx' $m/src 2>/dev/null | grep -v '/fakefx/' | wc -l)"
done
# 逐类外部引用计数（1.2 表）
# 见会话脚本：对 fakefx 每个类 grep "import com.tungsten.fclcore.fakefx.<cls>;" 计数并减去包内自引用
# fakefx 规模
find FCLCore/src/main/java/com/tungsten/fclcore/fakefx -name "*.java" | xargs wc -l
```
