# fakefx（JavaFX 属性体系）彻底移除执行方案

> 状态：**阶段 3（FCLCore 公开 API 全面 StateFlow 化）已完成**——9 个 FCLCore 类的公开 API 全部切换到 kotlinx-coroutines StateFlow（D2），FCL 12 个调用方级联改完，`:FCL:assembleDebug` 构建通过、冒烟 68/68、Config JSON 回环实测一致。记录见 §八；2a/2b 记录见 §六/§七。
> 分支：`feature/fclcore-stateflow`（阶段 3；基于含阶段 2b 的 `feature/miuix-migration` 系）。
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

## 六、阶段 2a 执行记录：自研极简 observable 库（2026-08-02 完成）

对应本文「阶段 1」（用户侧阶段编号 2a）。决策已拍板：**D1 新建包**（不沿用 fakefx 包名）、**D4 完整实现 extractor 冒泡存盘语义**。本步不改任何调用点，新库与 fakefx 并存。

### 6.1 实证修正（相对 §一 旧数据）

- 6.1 清理后外部引用面收敛：**FCL+FCLCore 共 82 个文件**（旧数据 107）。
- **新发现：FCLLibrary 有 24 个文件引用 fakefx**（§1.4 红线分析漏扫该模块）。用量集中在 `IntegerPropertyBase/BooleanPropertyBase/DoublePropertyBase/StringPropertyBase/ObjectPropertyBase` 匿名子类（控件属性）+ `SimpleXxx` + 1 处 `Bindings.createObjectBinding` + `StringConverter`。新库已全覆盖，阶段 2b 的 import 替换范围须加上 FCLLibrary。
- 方法级实证：`FXCollections` 仅 observableArrayList(×4 签名)/observableList/observableMap 被用；`Bindings` 仅 createStringBinding/createBooleanBinding/createObjectBinding/concat/bindContent/unbindContent；Change 的方法级消费几乎为零（监听器多为 `{ reload() }` 形态），`.sort()`/`unmodifiableObservableList`/`bindContentBidirectional` 等均无外部调用，**未实现**。

### 6.2 实现清单（`FCLCore/src/main/java/com/tungsten/fclcore/observable/`，89 文件 / 6,926 行）

| 子包 | 类 | 对齐的 fakefx 类 |
|---|---|---|
| 根 | Observable / InvalidationListener / WeakListener / WeakInvalidationListener / NamedArg | beans 同名类 |
| value | ObservableValue / ChangeListener / WritableValue / WeakChangeListener / ObservableBooleanValue / ObservableNumberValue / Observable{Integer,Long,Float,Double}Value | beans.value 同名类 |
| binding | ExpressionHelper / BindingHelperObserver / ObjectBinding / BooleanBinding / StringBinding / Bindings（仅 8 个被用方法）/ BidirectionalBinding / ContentBinding | binding.* 与 beans.binding.*（合并两个包） |
| property | Property / ReadOnlyProperty；Object、String、Boolean、Integer、Long、Float、Double 七系各含 ReadOnly*Property、*Property、*PropertyBase、Simple*Property；ReadOnly{Object,String,Double}Wrapper、ReadOnly{Object,String,Boolean,Integer,Double}PropertyBase；List 系：ReadOnlyListProperty / ListProperty / ListPropertyBase / SimpleListProperty / ReadOnlyListPropertyBase / ReadOnlyListWrapper / ListPropertyHelper（包私有）；Map 系：ReadOnlyMapProperty / MapProperty / MapPropertyBase / SimpleMapProperty / MapPropertyHelper（包私有） | beans.property 同名类 + binding/ListExpressionHelper、MapExpressionHelper |
| collections | ObservableList / ListChangeListener / WeakListChangeListener / ObservableListBase（合并 ModifiableObservableListBase）/ ObservableListWrapper / ElementObserver / SourceAdapterChange / FXCollections / ObservableMap / MapChangeListener / ObservableMapWrapper / ObservableSet / SetChangeListener / ObservableSetWrapper | collections 同名类（省掉 ObservableSequentialListWrapper、SortableList、NonIterableChange、transformation 等无调用部分） |
| util | Callback / StringConverter | util 同名类 |

### 6.3 语义对齐关键点（逐条读 fakefx 源码确认）

- **触发时机**：`*PropertyBase.set` 在 bound 状态抛 `"A bound value cannot be set."`；ObjectPropertyBase 用**引用比较**（同引用不触发），StringPropertyBase 用 **equals**，原生类型用值比较。`markInvalid` 只在 valid→invalid 边沿触发一次——**连续两次 set 且中间无 get() 只发一次事件**（与 fakefx 一致，冒烟已验证）；若挂了 ChangeListener，fire 内部 `getValue()` 会使属性重新生效。
- **ChangeListener vs InvalidationListener**：invalidation 总是触发且先触发；change 仅在 getValue() 与缓存值 **equals 不等**时触发（oldValue/newValue 取自触发前后缓存）。监听内异常转交 `uncaughtExceptionHandler`（同 fakefx）。
- **bind 单向同步**：bind 立即失效并向 source 注册弱引用 Listener（source 失效 → markInvalid → get() 时取 source 值）；unbind 固化当前值并摘除。
- **bindBidirectional 防回环**：`updating` 标志 + 双方弱引用（一侧 GC 自动互摘）；传播失败回滚 oldValue；equals/hashCode 与顺序无关，`unbindBidirectional` 靠 equals 匹配移除（冒烟覆盖 a→b、b→a、解绑后互不影响、无死循环）。
- **extractor 冒泡（D4）**：`observableArrayList(extractor)` 经 ElementObserver（IdentityHashMap + 引用计数）给元素挂失效监听；元素失效 → `beginChange/nextUpdate(i)/endChange` → 列表发 wasUpdated 的 Change + invalidation——**Accounts/Controllers/控件样式的"改元素即存盘"链路语义完整保留**（冒烟覆盖：冒泡、元素移出后摘除）。
- **ListChangeListener.Change**：wasAdded/wasRemoved/wasReplaced/wasPermutated/wasUpdated、getFrom/getTo/getRemoved/getAddedSubList 语义与 fakefx 逐条对齐；begin/end 块内多次 mutation 合并为一次分发（子变更序列等价，仅不做相邻合并优化）；同一 Change 分发给多个 listener 前逐个 reset。
- **List/Map 属性**：内容变更冒泡（invalidated + change 收 (current,current) + list/map-change 收原始 Change）；整体替换时 list-change 收 from=0/to=新 size/removed=旧内容的 wasReplaced 变更；Change.getList() 经 SourceAdapterChange 指向属性本身。
- **弱监听**：WeakInvalidationListener/WeakChangeListener/WeakListChangeListener 目标 GC 后下次触发时自动摘除；helper 添加监听时顺带清理已死的弱监听（对应 fakefx trim）。冒烟含 System.gc 实测。
- **ObjectBinding/BooleanBinding/StringBinding**：惰性求值 + 缓存，依赖失效 → invalidate → 向自身监听者冒泡；`Bindings.create*` 的 computeValue 异常兜底（false/null/""）与 fakefx 一致；concat 的 null→"null" 语义一致。

### 6.4 验证

- 新包独立 `javac` 编译通过（零外部依赖）。
- 冒烟测试 `SmokeTest.java`（68 断言）在纯 JVM 运行 **68/68 通过**，覆盖：同值/同引用不触发、valid 边沿单次触发、change vs invalidation 区分与顺序、bind/unbind、bound set 抛异常、双向绑定与防回环、StringBinding 惰性、concat、列表 add/remove/set/clear/setAll 事件形态、extractor 冒泡与摘除、ReadOnlyListWrapper 只读视图事件同步与 Change.getList() 指向、弱监听 GC、ListProperty 内容/整体替换、bindContent/unbindContent 重放、Map/Set 事件、MapProperty 冒泡、多 listener reset。
- 冒烟脚本存放于 `docs/migration/observable-smoke/SmokeTest.java`（工程无测试源集与 junit 依赖，按最小侵入不新增 gradle 配置；复跑方式见该目录 README）。
- `:FCL:assembleDebug` 全量编译通过（新代码随 FCLCore 编译）。

### 6.5 遗留问题（交阶段 2b）

1. **FCLLibrary 24 文件**须纳入 import 替换范围（见 6.1）。
2. `util/fakefx/`（ObservableHelper、BindingMapping 等）与 `util/gson/fakefx/`（JavaFxPropertyTypeAdapterFactory 及 creators）在 2b 需同步移植到新包类型，Config 磁盘 JSON 格式不变的前提是其只依赖 Property 的 getValue/setValue 语义——新库已满足，但需实测回环。
3. ObservableListBase 未实现 `SubObservableList`（subList 视图事件传播）与 sort 事件——外部无调用；若 2b 中出现再补。
4. `FXCollections.observableList` 不区分 RandomAccess（统一 ObservableListWrapper）——外部全部传 ArrayList/Arrays.asList，无行为差异。
5. 阶段 2b 替换后须复核 Kotlin 调用点（SAM 重载消解、`Property.value` 合成属性、`{ arrayOf<Observable>(it) }` extractor lambda）——新库签名与 fakefx 逐方法一致，预期零改动，但需以编译为准。

---

## 七、阶段 2b 执行记录：全仓 import 替换 + 删除 fakefx 包（2026-08-02 完成）

对应本文「阶段 2」（用户侧阶段编号 2b）。import 批量替换、util 辅助包移植、fakefx 整包删除。

### 7.1 替换统计（机械 sed，包结构映射 beans→根、beans.property→property、beans.binding+binding→binding、collections/util 同名平移）

| 模块 | 改动文件数 | 说明 |
|---|---|---|
| FCL | 82 | 73 import 替换 + 9 个 Kotlin 文件注释字样清理 |
| FCLCore | 102 | 9 调用点替换 + observable/util 库内 javadoc 字样清理 |
| FCLLibrary | 24 | 2a 遗留红线漏扫模块，本步已纳入 |
| 合计 | 208 | import 引用点 1,133 处（beans.property 399 / beans 330 / collections 225 / beans.value 169 / binding 82 / beans.binding 67 / util 51，按替换前 grep 前缀计） |

- `fakefx.event` 外部引用确认为零（与 2a 核实一致），`beans.property.adapter`/`collections.transformation`/`reflect`/`util.converter` 等子包均为 fakefx 包内自引用，随整包删除。
- **删除统计**：`FCLCore/src/main/java/com/tungsten/fclcore/fakefx/` 整包 **292 文件**（git rm）。删除前全仓 grep 确认无 import/FQCN 残留。

### 7.2 util 辅助包移植（30 文件，git mv 保历史）

- `util/fakefx/`（8 文件：ObservableHelper、BindingMapping、MappedObservableList、ObservableCache、ObservableOptionalCache、PropertyUtils、ReferenceHolder、SafeStringConverter）→ `util/observable/`。
- `util/gson/fakefx/`（21 文件：FxGson、FxGsonBuilder、JavaFxPropertyTypeAdapterFactory 及 creators/properties 适配器）→ `util/gson/observable/`。类名保持不动（JavaFxPropertyTypeAdapterFactory 等仅为历史命名），仅改包名与内部引用。
- 新发现并已处置：`util/png/fakefx/PNGFakeFXUtils`（不依赖 fakefx 包，仅包名含字样）→ 移至 `util/png/`，唯一调用点 YggdrasilServer 同步改 import。

### 7.3 observable 库补漏（+5 文件，94 文件 / 7,582 行）

util 移植暴露 2a 的两个缺口（6.5 遗留 3 预判范围内）：

- **Set 属性族**：`ReadOnlySetProperty` / `SetProperty` / `SetPropertyBase` / `SetPropertyHelper`（包私有）/ `SimpleSetProperty`——逐方法镜像 Map 系语义（内容变更冒泡、整体替换逐元素 removed→added），gson `SetPropertyTypeAdapter` 依赖。
- **`FXCollections.unmodifiableObservableList`**：移植 fakefx `UnmodifiableObservableListImpl`（弱监听转发 + SourceAdapterChange），`MappedObservableList` 依赖。

### 7.4 Config JSON 回环实测（临时冒烟，测后已删）

- 方法：从 git HEAD 还原旧 `util/gson/fakefx`（21 文件）与磁盘未动的 fakefx 包编译为「旧世界」，新 observable + `util/gson/observable` 为「新世界」；同一属性图（String/Boolean×2/Integer/Long/Float/Double/Object/Object\<List\>/List/List\<POJO\>/Map/Set 共 15 字段，含 unicode、转义、空串、负数）经 `JavaFxPropertyTypeAdapterFactory(true, true)`（与 Config.java:63 相同构造参数）序列化→反序列化→再序列化。
- 结果：**两侧 stdout 逐字节一致**（diff 零差异），且各自 `json1 == json2`（双重回环稳定），反序列化取值抽检一致。临时文件（/tmp/jsonrt）已删除，可复现脚本要点记录于此。

### 7.5 门禁验证

- 冒烟 68/68 复跑通过（补漏后 observable 库全量重编译）。
- `:FCL:assembleDebug` 全量构建：**BUILD SUCCESSFUL（2m 58s，零编译错误）**——Kotlin 调用点零语法适配（SAM 重载/合成属性/extractor lambda 与 fakefx 签名逐方法一致，6.5 遗留 5 的"预期零改动"成立）；仅有与本次改动无关的既有 deprecation 警告。

### 7.6 遗留问题

1. 类名 `FakeFxCompose` / `FakeFxStateFlow`（FCL/ui/bridge）与 `PNGFakeFXUtils`（FCLCore/util/png）保留历史命名——仅名称含 "FakeFx" 字样，不引用已删包；改名属 API 变更，未纳入本步范围。
2. `docs/` 下迁移文档（含本文 §一~§六）保留 fakefx 历史记述，不清理。
3. ObservableListBase 仍未实现 subList 视图事件与 sort 事件（外部无调用，2b 全量编译证实无新增需求）。

---

## 八、阶段 3 执行记录：FCLCore 公开 API 全面 StateFlow 化（2026-08-02 完成）

对应本文「阶段 2（旧编号）」的 9 文件改造，按用户拍板的 **D2 = 全面 StateFlow** 执行。分支 `feature/fclcore-stateflow`。

### 8.1 依赖与互操作基座（本步唯一的 gradle 改动）

- `gradle/libs.versions.toml` 新增 `kotlinxCoroutines = "1.9.0"`（与全仓 Compose/miuix 传递解析到的版本一致，gradle 缓存内已存在）与 `kotlinx-coroutines-core` catalog 条目；`FCLCore/build.gradle.kts` 以 **`api`** 引入（StateFlow 出现在公开 API 签名中，必须向 FCL 编译类路径传递）。
- **Java 互操作方案**（新文件）：
  - `FCLCore/util/flow/FlowSubscriptions.kt`：`subscribe(flow, Consumer)`（跳过当前值，对齐 `addListener` 语义）与 `subscribeWithCurrent(flow, Consumer)`（先同步当前值，对齐 `bind` 语义），返回可取消的 `Subscription`。共享作用域用 **`Dispatchers.Unconfined`——回调在发射线程同步执行**，与 fakefx「不做隐式线程切换」语义逐条对齐（红线要求）；StateFlow 合并语义下消费方均幂等。
  - `FCLCore/util/observable/FlowBridge.java`：observable→StateFlow 镜像（`asStateFlow(ObservableValue)`），仅供 FCLCore 内部把惰性 ObjectBinding（账户纹理链）包装成 Flow 暴露。
  - `FCL/util/FlowObservables.java`：反向桥（StateFlow→`Observable`/`ObjectProperty`），仅供 FCL 内部仍走 observable 的存量链路（extractor 冒泡、`Bindings.create*Binding` 依赖、`BindingMapping` 消费）对接新 API。

### 8.2 逐类 API 变更对照（旧 → 新）

| 类 | 旧签名 | 新签名 |
|---|---|---|
| `task/Task.java` | `ReadOnlyDoubleProperty progressProperty()`；`ReadOnlyStringProperty messageProperty()`；内部 ReadOnlyWrapper + `doSubTask` 的 `bind/unbind` | `StateFlow<Double> progressFlow()` + `double getProgress()`；`StateFlow<String> messageFlow()` + `String getMessage()`；`doSubTask` 改为 `subscribeWithCurrent` 转发 + finally `cancel()`（等价 bind/unbind：先同步、转发、摘除后保留末值） |
| `auth/Account.java` | `implements Observable`（add/removeListener + ObservableHelper）；`BooleanProperty portableProperty()`；`ObjectBinding<Optional<Map<TextureType,Texture>>> getTextures()` | 去接口；`StateFlow<Long> revisionFlow()`（每次 `invalidate()` 递增，对齐失效语义）；`StateFlow<Boolean> portableFlow()`（`isPortable()/setPortable()` 保留）；`StateFlow<Optional<Map<TextureType,Texture>>> texturesFlow()`（基类恒空常量）；`hashCode` 改取 `isPortable()` |
| `auth/microsoft/MicrosoftAccount.java` | 覆写 `getTextures()`（BindingMapping.of(profileRepository.binding(uuid)).map(...)） | 覆写 `texturesFlow()`：同一绑定链经 `FlowBridge.asStateFlow` 镜像，**懒加载 + 缓存**（原每次调用新建绑定，现每账户一条） |
| `auth/yggdrasil/YggdrasilAccount.java` | 同上 | 同上（私有 `profilePropertiesBinding` 仍是内部 ObjectBinding，机制保留、不进公开 API） |
| `auth/offline/OfflineAccount.java` | 覆写 `getTextures()`（createObjectBinding 常量） | 覆写 `texturesFlow()`：构造期常量 StateFlow |
| `auth/authlibinjector/AuthlibInjectorServer.java` | `implements Observable` + ObservableHelper，元数据刷新时 `helper.invalidate()` | 去接口；`StateFlow<Long> revisionFlow()`，元数据刷新时递增（try/catch 保留）；序列化字段（url/metadataResponse/metadataTimestamp，helper 本就 transient）**零变化** |
| `mod/LocalModFile.java` | `BooleanProperty activeProperty()`（匿名类 invalidated() 内做启/停用改名） | `StateFlow<Boolean> activeFlow()`；`isActive()/setActive(boolean)` 保留，改名逻辑内联进 `setActive`（同值 set 为 no-op，与原 BooleanPropertyBase 一致） |
| `mod/Datapack.java` | `ObservableList<Pack> getInfo()`；`Pack.activeProperty(): BooleanProperty` | `StateFlow<List<Pack>> infoFlow()`（不可变快照整体替换）+ `List<Pack> getInfo()` 只读快照；`Pack.activeFlow(): StateFlow<Boolean>`，改名逻辑内联 `setActive` |
| `util/Holder.java` | `implements InvalidationListener`（no-op） | 去接口（仅 InvocationDispatcher 作值容器使用，无监听语义消费方） |

### 8.3 调用方级联修改（FCL 12 文件；FCLLibrary 零调用点）

| 文件 | 改动 |
|---|---|
| `setting/Accounts.java` | accounts extractor 由观察 `account`（Observable）改为观察**镜像信号** `accountChangeMirror(account)`（IdentityHashMap 缓存的 SimpleBooleanProperty，随 revisionFlow 翻转）；列表移除账户时取消镜像订阅。**「账户失效冒泡 → wasUpdated → updateAccountStorages 存盘 + UI 刷新」链路语义不变** |
| `setting/Config.java` | authlibInjectorServers extractor 同理镜像 `serverChangeMirror(server)`（静态 Map + transient 清理标记）；「元数据刷新冒泡 → Config 存盘」语义不变；Gson 序列化字段零变化（新增均为 static/transient） |
| `ui/compose/FCLTaskDialog.kt` | `bindTask/unbindTask` 由 ChangeListener 挂摘改为 `FlowSubscriptions.subscribeWithCurrent` + `cancel()`（初值同步与负进度=不确定进度语义不变） |
| `ui/TaskListPane.java` | `ProgressListNode` 的 `percentProgressProperty().bind/stringProperty().bind` 改为 `subscribeWithCurrent` 写入属性，`unbind()` 改 `cancel()`（遗留类，保持可编译） |
| `game/TexturesLoader.java` | `skinBinding/textureBinding` 的 `BindingMapping.of(account.getTextures())` 改为 `of(FlowObservables.toProperty(account.texturesFlow()))`；返回类型 ObjectBinding 不变（FCL 内部机制） |
| `ui/account/AccountListItem.java` | `Bindings.createStringBinding(account::getCharacter, account)` 与 `(server::getName, server)` 的 Observable 依赖改为 `FlowObservables.toObservable(revisionFlow())` |
| `activity/MainActivity.kt` | `BindingMapping.of(account){...}` / `of(account.server){...}` 改为 `Bindings.createObjectBinding({...}, FlowObservables.toObservable(revisionFlow()))` |
| `ui/main/compose/MainRightMenu.kt` | `AccountSubtitle` 的 `BindingMapping.of(account.server){it.name}` 直接改为 Flow 原生：`revisionFlow().map{name}.stateIn(scope, Eagerly, name)` + `collectAsState()`（不再经过 observable 桥） |
| `ui/manage/ModListPage.java` | `ModInfoObject.active` 字段类型 BooleanProperty → `StateFlow<Boolean>`（取 `localModFile.activeFlow()`） |
| `ui/manage/LocalModListAdapter.kt` | `active.get()/.set(checked)` → `active.value` / `modInfo.setActive(checked)` |
| `ui/manage/DatapackListPage.java` | `DatapackInfoObject` 去掉 active 代理字段；`refresh()` 首次改为本地 ObservableList 镜像（`subscribeWithCurrent(infoFlow)` → 切 UI 线程 `setAll`）供 MappedObservableList 消费 |
| `ui/manage/DatapackListAdapter.java` | `checkProperty().bindBidirectional(pack.activeProperty())` 改为手动双向：初值 `set(pack.isActive())` + `subscribe(activeFlow)`（pack→checkbox）+ checkProperty ChangeListener（checkbox→pack）；两侧同值写入均 no-op，天然防回环；视图回收时 cancel + removeListener |

### 8.4 门禁验证

- `:FCL:assembleDebug` 全量构建 **BUILD SUCCESSFUL**（四次迭代修平：Datapack.installTo 迭代遗漏、MainRightMenu 遗漏调用点——勘察 grep 未覆盖该 `BindingMapping.of` 形态、两处镜像 lambda 非 final 变量）。
- observable 冒烟 **68/68 复跑通过**（本步未动 observable 库实现，确认无连带破坏）。
- Config JSON 回环实测：临时 `ServerJsonRoundTrip`（测后已删）对 AuthlibInjectorServer 做 反序列化→序列化→反序列化→序列化 双重回环，**两次输出逐字节一致**，且产物仅含 url/metadataResponse/metadataTimestamp（与改动前字段集一致）；同时断言 `subscribe` 不立即回调（addListener 语义）。Config/Account 的 Gson 字段 diff 审查确认零序列化面变化。
- 红线：FCLauncher/Terracotta/LWJGL-Pojav/ZipFileSystem/NG-GL4ES 零改动；启动链路不经本次改动面（Task 的 TaskListener/executor 契约未动）。

### 8.5 遗留问题

1. **桥接订阅生命周期**：`FlowObservables`/`FlowBridge` 的镜像订阅不可取消（等价于原被 bind 长期持有的绑定对象），账户/服务器移除场景已显式清理（Accounts/Config 的 discard 方法）；MainActivity/AccountListItem 的 `toObservable` 镜像随账户对象同生死，切换选中账户会各留一个极轻量镜像，阶段 4/5 数据层全面 Flow 化后桥即删除。
2. `TaskListPane.java` 为已被 Compose 版（FCLTaskDialog）替代的遗留类，本次仅保持可编译，不验证其运行时行为。
3. StateFlow 合并语义：高频 `invalidate()`/进度更新可能合并为一次回调（fakefx 下同值/同引用亦不重复触发，存盘与 UI 刷新均幂等）；任务进度本身有 `getProgressInterval()` 节流，无实际差异。
4. `YggdrasilAccount`/`MicrosoftAccount` 的纹理链内部仍使用 ObjectBinding + FlowBridge（私有实现，不进公开 API）；阶段 4 之后如需去 observable，可将 ObservableOptionalCache 一并 Flow 化。

---

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
