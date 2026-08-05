# FCL 液态玻璃 UI 全面迁移设计

## 目标
将 Fold Craft Launcher（FCL）中所有剩余的传统 View/XML UI 迁移为基于 Jetpack Compose + Backdrop 的液态玻璃风格 UI，最终移除对传统 `MainActivity` 及旧页面框架的依赖，使整个应用保持单一入口 `GlassMainActivity`。

## 设计原则
1. **不改底层模型**：只替换 UI 层，FCLCore / FCLLibrary 的数据模型、任务、配置逻辑原样复用。
2. **统一组件库**：所有新页面复用 `/ui/glass/component/` 中已有的 GlassCard、GlassButton、GlassTopBar、GlassSearchBar 等组件；缺失的组件在本计划中补齐。
3. **Backdrop 一致性**：所有可滚动/列表容器使用 `layerBackdrop` / `drawBackdrop` 实现玻璃态背景，形状统一使用 `GlassTheme.glassCornerRadius` 与 `RoundedCornerShape(50)`。
4. **单 Activity + Compose Navigation**：新页面统一走 `FCLGlassRoute` / `GlassNavHost`；旧 `MainActivity`、`ManageUI`、`DownloadPageManager` 等页面管理器逐步退役。
5. **Dialog 统一**：将旧 `FCLDialog` / `AlertDialog` 统一为 Compose `GlassAlertDialog` / `GlassInputDialog` / `GlassSelectionDialog`，避免页面与旧 Dialog 混用。
6. **编译即验证**：每个阶段结束时必须能通过 `./gradlew :FCL:compileFordebugKotlin`。

## Backdrop 官方最佳实践
本迁移严格遵循 Backdrop 官方文档与示例 APP（[AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass)）的用法。

### Backdrop 类型选择
- 主背景使用 `rememberLayerBackdrop { drawRect(backgroundColor); drawContent() }`，并通过 `Modifier.layerBackdrop(backdrop)` 让导航宿主绘制内容到 backdrop。
- 需要合并多个背景源（如 Slider、Tabs）时使用 `rememberCombinedBackdrop(...)`。
- "glass on glass" 嵌套场景（底部玻璃弹窗内再放玻璃按钮）使用 `exportedBackdrop` 参数避免循环渲染崩溃。

### Effects 顺序
必须按 `color filter ⇒ blur ⇒ lens` 的顺序调用：
```kotlin
effects = {
    vibrancy()              // color filter
    blur(4f.dp.toPx())      // blur
    lens(16f.dp.toPx(), 32f.dp.toPx())  // lens (Android 13+)
}
```

### 形状
- 按钮、底部导航项等胶囊形元素优先使用官方示例中的 `Capsule()`（来自 [Shapes](https://github.com/Kyant0/Shapes) 库）。
- 卡片使用 `RoundedCornerShape` 或 Shapes 库提供的 G2 连续圆角。
- 注意：`lens()` 要求形状为 `CornerBasedShape`；`height` 必须在 `[0, shape.minCornerRadius]` 范围内。

### 可读性
必须在 `onDrawSurface` 中叠加半透明底色或主题色，不能只依赖 blur/lens：
```kotlin
onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
```

### 交互反馈
- 按钮/导航项的按压缩放、位移等变换必须放在 `drawBackdrop` 的 `layerBlock` 中，避免 backdrop 跟随变形。
- 参考官方示例实现 `InteractiveHighlight` 或直接使用 `Modifier.clickable` + 简单的 `Animatable` 缩放。

## 阶段划分

### 阶段 1：通用 Dialog 统一化
先把散落在各处的旧 Dialog 改成 Compose，避免后续页面重写时还要桥接旧弹窗。

| 旧文件 | 新文件 | 说明 |
| --- | --- | --- |
| `dialog/CreateAccountDialog.java` | `glass/component/dialog/GlassCreateAccountDialog.kt` | 创建离线/微软/AuthlibInjector 账户 |
| `dialog/ReLoginDialog.java` | `glass/component/dialog/GlassReLoginDialog.kt` | 账户过期重新登录 |
| `dialog/AddAuthlibInjectorServerDialog.java` | `glass/component/dialog/GlassAuthlibServerDialog.kt` | 添加/编辑认证服务器 |
| `dialog/RenameVersionDialog.java` | `glass/component/dialog/GlassRenameVersionDialog.kt` | 重命名版本 |
| `dialog/DuplicateVersionDialog.java` | `glass/component/dialog/GlassDuplicateVersionDialog.kt` | 复制版本 |
| `dialog/QuickInputDialog.java` | `glass/component/dialog/GlassInputDialog.kt` | 通用单行输入 |
| `dialog/AddProfileDialog.java` | `glass/component/dialog/GlassAddProfileDialog.kt` | 添加游戏配置文件 |

同时补齐通用组件：
- `GlassAlertDialog`：标题 + 消息 + 确认/取消
- `GlassInputDialog`：标题 + 输入框 + 确认/取消
- `GlassSelectionDialog`：标题 + 单选列表 + 确认/取消
- `GlassProgressDialog`：显示后台任务进度（替代 `ProgressDialog` / `TaskDialog` 的 UI 层）

### 阶段 2：版本深度管理
在已完成 `VersionsPage` 的基础上，补齐版本相关页面。

| 旧文件 | 新文件 | 说明 |
| --- | --- | --- |
| `dialog/VersionSettingsDialog.java` | `glass/page/version/VersionSettingsPage.kt` | 单个版本的内存、Java、渲染等设置 |
| `ui/download/version/VersionInstallPage.java` | `glass/page/download/VersionInstallPage.kt` | 安装 Minecraft / Forge / Fabric / OptiFine 向导 |
| `ui/manage/ModListPage.java` | `glass/page/version/VersionModListPage.kt` | 已安装 Mod 列表与管理 |
| `ui/manage/WorldListPage.java` | `glass/page/version/VersionWorldListPage.kt` | 已安装世界列表与管理 |
| 资源包/光影管理旧页 | `glass/page/version/VersionResourcePackPage.kt` / `VersionShaderPackPage.kt` | 资源包、光影包管理 |
| `dialog/ModInfoDialog.java` | `glass/page/version/ModInfoPage.kt` | Mod 详情 |
| `dialog/WorldExportDialog.java` | `glass/component/dialog/GlassWorldExportDialog.kt` | 导出世界 |

`VersionsPage` 中版本项的「设置」入口不再 Toast，改为导航到 `VersionSettingsPage`。

### 阶段 3：下载与整合包
已有通用 `RemoteModListPage` / `RemoteModInfoPage` / `RemoteModVersionPage`，补齐整合包与特殊下载流程。

| 旧文件 | 新文件 | 说明 |
| --- | --- | --- |
| `ui/download/modpack/ModpackDownloadPage.java` | `glass/page/download/ModpackDownloadPage.kt` | 整合包下载列表 |
| `dialog/DownloadModpackDialog.java` | `glass/page/download/ModpackInstallPage.kt` | 整合包下载与安装 |
| `dialog/ModpackSelectionDialog.java` | `glass/page/download/ModpackSelectionPage.kt` | 本地整合包选择安装 |
| `dialog/ModpackInfoDialog.java` | `glass/page/download/ModpackInfoPage.kt` | 整合包详情 |
| `dialog/ModpackUrlDialog.java` | `glass/component/dialog/GlassUrlInputDialog.kt` | URL 安装整合包 |
| `dialog/DownloadAddonDialog.java` | `glass/page/download/AddonDownloadPage.kt` | 通用 Addon 下载对话框改为页面 |

### 阶段 4：设置与 Java
将设置页从目前的「主题预设 + 主题模式 + 关于」扩展到完整的启动器设置。

| 旧文件 | 新文件 | 说明 |
| --- | --- | --- |
| `ui/setting/LauncherSettingPage.java` | `glass/page/settings/launcher/*` | 拆分多个设置分类页 |
| `dialog/ManageJavaDialog.java` | `glass/page/settings/JavaRuntimePage.kt` | Java 运行时管理 |
| `fragment/RuntimeFragment.kt` | 合并进 `JavaRuntimePage.kt` | Java 版本列表与配置 |
| `activity/JVMActivity.java` | `glass/page/settings/JvmArgsPage.kt` | JVM 参数配置 |

设置分类页建议：
- `LauncherGeneralSettingsPage`：语言、检查更新、下载源等
- `LauncherGameSettingsPage`：默认游戏目录、版本隔离等
- `JavaRuntimePage`：Java 运行时列表、添加、删除
- `JvmArgsPage`：全局 JVM 参数

### 阶段 5：控制器与杂项
| 旧文件 | 新文件 | 说明 |
| --- | --- | --- |
| `activity/ControllerActivity.java` | `glass/page/controller/ControllerPage.kt` / `ControllerDownloadPage.kt` | 控制器管理与下载 |
| `dialog/SelectControllerDialog.java` | `glass/component/dialog/GlassControllerSelectDialog.kt` | 选择控制器 |
| `dialog/DownloadControllerDialog.java` | `glass/page/controller/ControllerDownloadPage.kt` | 下载控制器配置 |
| `fragment/EulaFragment.java` | `glass/page/EulaPage.kt` | 首次启动用户协议 |
| `activity/ShellActivity.java` | `glass/page/ShellPage.kt` | Shell 命令输出 |
| `activity/WebActivity.java` | `glass/page/WebPage.kt` | WebView 页面 |

### 阶段 6：MainActivity 退役与收尾
- 将 `MainActivity` 中仍在被旧代码依赖的实用方法提取到独立工具类。
- 把所有外部入口（activity-alias、旧 Intent）重定向到 `GlassMainActivity`。
- 删除或标记废弃 `MainActivity`、`ManageUI`、`DownloadPageManager` 等旧页面框架。
- 最终 AndroidManifest 只保留 `GlassMainActivity` 作为入口，其他 Activity 按需保留（Web/Shell/JVM 等可改为 Composable 内部页面或独立 Activity 但用 Compose 重写）。

## 技术要点

### 导航
- 主导航继续使用 `FCLGlassRoute` + 底部 `GlassBottomBar`。
- 版本内子页面、设置分类页、下载子流程使用嵌套 NavHost：
  - `GlassNavHost` 负责顶层 5 个 Tab。
  - `GlassDownloadNavHost` 已存在，继续扩展整合包等路由。
  - 新增 `GlassSettingsNavHost`、`GlassVersionNavHost`。

### 状态管理
- 继续使用 `*StateHolder` 模式（如 `AccountListStateHolder`、`VersionListStateHolder`）。
- 每个新页面配套一个 StateHolder，负责监听 FCLCore 数据变化并暴露 Compose State。

### 后台任务与弹窗
- 旧 `TaskDialog` / `ProgressDialog` 改为在 Compose 中显示 `GlassProgressDialog`。
- 下载、安装、删除等耗时操作继续走 FCLCore `Task`，但结果回调在 UI 线程更新 Compose State。

### 旧代码桥接
- 在全部迁移完成前，部分旧 Activity 可能需要临时桥接。优先把 Dialog 改成 Compose，减少 Activity 间跳转。
- 如果某个旧页面业务逻辑过重（如 `MainActivity` 的页面切换），先提取为独立工具类，再重写 UI。

### 组件库升级
在迁移旧页面的同时，逐步对照官方示例升级已有玻璃组件：
- **GlassBottomBar**：增加 `layerBlock` 按压缩放反馈；可选引入 `Shapes` 库将 `RoundedCornerShape(50)` 替换为 `Capsule()`。
- **GlassButton / GlassIconButton**：增加 `layerBlock` 交互高光/缩放；支持 `surfaceColor` 参数。
- **GlassCard**：保持现有 `RoundedCornerShape` 或升级为 G2 连续圆角。
- **新增组件**：
  - `GlassBottomSheet`：使用 `exportedBackdrop` 实现玻璃底部弹窗。
  - `GlassSlider`：使用 `rememberCombinedBackdrop` 实现轨道 + 滑块双层玻璃效果。
  - `GlassProgressDialog`：统一后台任务进度显示。

### 依赖
当前已引入 `io.github.kyant0:backdrop:2.0.0`。如需使用官方示例中的 `Capsule()` 等形状，可额外引入 `com.kyant.shapes:shapes:<version>`，但这不是迁移阻塞项，可在视觉效果优化阶段再决定是否引入。

## 风险与回退
- **范围风险**：阶段 4/5 设置与控制器涉及配置项较多，可能一次做不完。可在设计文档审批后进一步拆分。
- **编译风险**：任何阶段都必须保证 `:FCL:compileFordebugKotlin` 通过；若旧文件被删除导致引用断裂，需同步修改引用点。
- **运行时风险**：新 UI 与旧 `TaskDialog` / `FCLDialog` 混用可能弹窗异常。阶段 1 优先统一 Dialog 就是为了降低此风险。
- **回退策略**：每个阶段独立提交；若某阶段问题太多，可回退到上一阶段编译通过的版本。

## 验收标准
- 每个阶段结束时 `./gradlew :FCL:compileFordebugKotlin` 编译通过。
- 用户能完成核心闭环：启动应用 → 浏览版本 → 安装版本 → 启动游戏 → 下载 Mod/整合包 → 管理已安装内容 → 修改设置。
- 所有可见 UI 均使用 Backdrop 玻璃效果，无原生 Android 旧风格残留。
