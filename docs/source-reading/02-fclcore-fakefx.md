# fclcore：fakefx 包（OpenJFX 数据层移植）

范围：`FCL/src/main/java/com/tungsten/fclcore/fakefx/`，共 292 个 Java 文件、44324 行。是 OpenJFX 中 beans / collections / event / util 子系统的机械重命名移植（`javafx.*` → `com.tungsten.fclcore.fakefx.*`），供 HMCL 移植代码替代原版 JavaFX beans API，底层实现完全一致。

## 一、子包职责一览

| 子包 | 文件/行数 | 职责 |
|---|---|---|
| `beans` | 7/218 | 根接口：`Observable`、`InvalidationListener`、`WeakListener`、`WeakInvalidationListener`、`NamedArg` |
| `beans/value` | 27/777 | `ObservableValue`/`WritableValue`/`ChangeListener` 及 boolean/int/long/float/double/String/List/Map/Set 全套特化接口、`WeakChangeListener` |
| `beans/property` | 62/6946 | 属性体系主体：`Property`/`ReadOnlyProperty` 接口、`*PropertyBase` 抽象实现、`Simple*Property`、`ReadOnly*Wrapper`，覆盖数值/String/List/Map/Set |
| `beans/property/adapter` | 31/3983 | 与 java.beans 的双向适配（`JavaBean*Property` + Builder 全家） |
| `beans/binding` | 26/9588 | `Bindings` 静态工厂 + 各类型 `Binding`/`Expression` 抽象类、`When` 三元绑定 |
| `binding`（内部） | 22/5221 | 监听器管理核心 `ExpressionHelper`、`List/Set/MapExpressionHelper`、`BindingHelperObserver`、`Bidirectional(Content)Binding`、`StringFormatter`、`Mapped/FlatMapped/OrElse/Select/LazyBinding`、`Subscription` |
| `collections` | 45/10577 | `FXCollections`、`ObservableList(Base/Wrapper)`、`ModifiableObservableListBase`、`ListChangeBuilder`、`List/Map/SetListenerHelper`、`NonIterableChange`、ObservableArray(Float/Int) 族 |
| `collections/transformation` | 3/831 | `TransformationList` 抽象基 + `FilteredList`（int[] 索引 + refilter）+ `SortedList`（SortHelper） |
| `event` | 22/2131 | 完整事件分发框架：`Event`/`EventType` 层级、`EventTarget`/`EventDispatcher`/`EventDispatchChain`、`EventHandlerManager`、捕获-冒泡两阶段 |
| `property` + `property/adapter` + `reflect` | 16/1422 | JavaBean 内省支持链：`PropertyReference`、`PropertyDescriptor`、`Disposer`、`MethodHelper`、`MethodUtil`/`ReflectUtil`（Trampoline 反射技巧） |
| `util`(+converter) | 28/2273 | `Callback`、`Pair`、`StringConverter`、`Duration`、`Builder`；converter 为 21 个类型转换器 |
| 根部 3 文件 | 357 | `PlatformUtil`、`FXPermissions`、`UnmodifiableArrayList` |

## 二、核心机制

- **属性存储与失效通知**（`beans/property/ObjectPropertyBase.java`，Boolean/Integer/String 同构）：字段为 `value + observable(绑定源) + valid + ExpressionHelper helper`。`set()` 先拒绝已绑定属性，值不等才 `markInvalid()`：`valid=false → invalidated() 钩子 → fireValueChangedEvent()`，即**惰性失效模型**；`get()` 才置 `valid=true` 并求值。
- **绑定**：`bind()` 挂内部静态类 `Listener`（持 `WeakReference<PropertyBase>`，实现 `WeakListener.wasGarbageCollected()` 自清理）；`unbind()` 把源当前值固化进 `value`。非 Observable 类型化源（如 `ObservableValue<Boolean>` 绑到 BooleanProperty）用内部 `ValueWrapper` 适配。
- **监听器管理**（`binding/ExpressionHelper.java`）：0/1/多监听器三态——null → `SingleInvalidation`/`SingleChange` → `Generic` 数组版（1.5 倍扩容、`locked` 防并发修改、add 容量不足时先 `trim()` 清除已 GC 弱监听）。`fireValueChangedEvent` 先发 Invalidation，再取新值与缓存 `currentValue` 比较，**仅真正变化才回调 ChangeListener**；监听器异常交给 `Thread.currentThread().getUncaughtExceptionHandler()`（无 FX 线程）。包内不存在 `ListeningHelper`，也没有 `WeakReferenceProcessor`；弱引用清理就是 `WeakListener.wasGarbageCollected()` + `ExpressionHelperBase.trim()`。
- **集合变更事件模型**（`collections/ObservableListBase.java` + `ListChangeBuilder`）：`beginChange()/nextAdd/nextRemove/nextSet/nextPermutation/nextUpdate/endChange()` 块式记录，`endChange` 合并为单个 `ListChangeListener.Change` 分发；`ModifiableObservableListBase` 只需实现 `get/size/doAdd/doSet/doRemove` 即自动生成变更事件。`ReadOnly*Wrapper` 是"一写一读两个互相同步的属性"模式（见 `ReadOnlyObjectWrapper`）。

## 三、与原版 JavaFX 的差异

- **保留**：beans/value/property/binding、collections（含 transformation）、event、util(+converter)、reflect —— 即纯数据层。移植基线较新（含 JavaFX 19 的 `ObjectBinding.allowValidation()/isObserved()`）。
- **砍掉**：graphics/scene/CSS/animation/application/`Platform`/FX Application Thread 全部不存在；`event` 包只有分发骨架，没有 UI 脉络。`FilteredList/SortedList` 仅通过 `ObservableList.filtered()/sorted()` default 方法可达。
- **com.sun.javafx 残留**：仅 3 处字符串级引用（`PlatformUtil` 读 `com.sun.javafx.isEmbedded`、`property/JavaBeanAccessHelper` 反射 `JavaBeanQuickAccessor`、`reflect/MethodUtil` 的 MISC_PKG），运行时均不可达。

## 四、实际使用 API 面

- **fcl UI 模块是主力**（68 个文件，Java+Kotlin 混合）：`ObservableList`/`FXCollections`、`SimpleObjectProperty/SimpleBooleanProperty/SimpleStringProperty/SimpleIntegerProperty`（约各 10–20 处）、`ObjectProperty/BooleanProperty/StringProperty/IntegerProperty` 声明类型、`Bindings`（`bindBidirectional/bindContent` 等 43 处调用）、`InvalidationListener/ChangeListener`、`ReadOnlyListWrapper/ListProperty`。另有 `util.StringConverter` 用于 `fcllibrary/component/FCLCheckBoxTreeItem` 与 `fcl/util/FXUtils`。
- **fclcore（fakefx 之外）仅 13 个文件使用**：`auth/Account` 体系（ObjectProperty/SimpleBooleanProperty/Bindings/ReadOnly*Wrapper）、`task/Task`、`mod/LocalModFile`、`Datapack`。`setting/VersionSetting|MenuSetting|Profile` 已明确注释"不再依赖 fakefx property"，改用普通字段 + 自研 `addOnChangeListener`。

## 五、疑似死代码清单（项目内零外部引用，约 9500+ 行，占包体 ~22%）

1. **`event` 整包 22 文件 2131 行** —— 最大的死岛，无任何外部 import。
2. **JavaBean 适配链 ~4960 行**：`beans/property/adapter` 全包 31 文件 + `property`（3）+ `property/adapter`（9）+ `reflect`（4），只在 fakefx 内部互引成环；`JavaBeanQuickAccessor` 反射初始化失败时直接抛 UnsupportedOperationException。
3. **`util/converter` 整包 21 文件 1636 行**（仅包内互引）。
4. **根部**：`PlatformUtil`、`FXPermissions`、`UnmodifiableArrayList`；**util**：`Pair`、`Duration`、`Builder`、`BuilderFactory`（`Callback`/`StringConverter` 在用）。
5. **binding**：`LazyObjectBinding`、`Subscription`、`SelectBinding/MappedBinding/FlatMappedBinding/OrElseBinding`（仅 Bindings 内部可达，应用未触发）；`beans.binding` 的 `ListBinding/MapBinding/SetBinding/NumberBinding/When`。
6. **collections 死 API**：`ObservableArray/FloatArray/IntegerArray` 族（应用 0 调用）、`SortableList`、`VetoableListDecorator`、`TrackableObservableList`、`UnmodifiableListSet`、`WeakMap/SetChangeListener`；`transformation`（`FilteredList/SortedList`）应用无调用。注意 `ListChangeBuilder`、`ChangeHelper`、`ElementObserver/MappingChange`（extractor 路径）、各 `ListenerHelper` 属于"无外部 import 但被 FXCollections/Wrapper 内部依赖"的**活基础设施**，不可删。

**结论**：fakefx 是 OpenJFX 数据层（beans+collections+binding）的高保真移植，通知/弱引用/变更事件语义与原版一致；实际被 FCL 使用的只有 `Simple*Property`、`ObservableList/FXCollections`、`Bindings`、各类 Listener 和 `StringConverter` 这一条窄 API 面，event、JavaBean 适配、converter、数组集合等约 1/5 代码是可安全移除的死代码。
