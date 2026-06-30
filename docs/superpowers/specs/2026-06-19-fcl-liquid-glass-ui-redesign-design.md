# FCL 液态玻璃 UI 全面重构设计文档

## 1. 背景与目标

### 1.1 背景
FCL（Fold Craft Launcher）当前 UI 基于传统 Android View 系统（XML + Java/Kotlin）构建，包含约 80+ 个页面与组件。用户希望将整个应用 UI 改为基于 Backdrop 库的液态玻璃（Liquid Glass）风格。

### 1.2 目标
- 将 FCL 主界面全面迁移至 Jetpack Compose + Backdrop
- 采用单 Activity + Compose Navigation 架构
- 统一的玻璃容器（Glass Container）与底部玻璃导航栏（Glass Bottom Bar）
- 所有一级页面都使用液态玻璃视觉语言
- 主题预设颜色作为液态玻璃的 tint 来源

## 2. 关键决策

| 决策项 | 选择 | 原因 |
|--------|------|------|
| 推进方式 | A. 全面重写 | 用户明确要求"全部都是这个 UI" |
| 架构方案 | 2. 重新设计主架构 | 单 Activity + Compose Navigation 能获得最统一的玻璃效果 |
| 最低 SDK | A. 提升至 API 31 | Backdrop 效果依赖 Android 12+ |
| 主导航 | A. 底部玻璃导航栏 | 与 Backdrop 官方教程对齐，移动场景最自然 |

## 3. 架构概览

```
MainActivity
 └── FCLGlassApp (Compose root)
      ├── GlassBackground (主题背景层)
      ├── GlassNavHost (页面内容，被 layerBackdrop 捕获为 backdrop)
      │    └── 各页面 Composable (Home, Versions, Download, Settings, ...)
      └── GlassBottomBar (底部玻璃导航栏，使用 drawBackdrop 折射背景)
```

### 3.1 核心组件

#### `FCLGlassApp`
- 应用根 Composable
- 提供主题、导航控制器、全局状态
- 管理 System UI（状态栏、导航栏）透明/沉浸

#### `GlassBackground`
- 根据当前主题模式（亮/暗）渲染背景
- 支持主题预设色作为背景渐变/装饰色
- 为 GlassNavHost 提供视觉底层

#### `GlassNavHost`
- 使用 Compose Navigation 管理页面路由
- 被 `Modifier.layerBackdrop(backdrop)` 包裹，将其内容捕获到 backdrop
- 页面切换使用 Compose 默认或自定义过渡动画

#### `GlassBottomBar`
- 底部常驻导航栏
- 使用 `rememberLayerBackdrop` + `drawBackdrop` 实现官方教程中的玻璃效果
- 每个导航项都是玻璃胶囊按钮
- 当前选中项使用主题色 tint（`BlendMode.Hue` + `alpha = 0.75f`）

### 3.2 导航结构

底部导航固定 5 个入口：

| 入口 | 标识 | 主要内容 |
|------|------|---------|
| 主页 | home | 公告、快速启动、当前账号 |
| 版本 | versions | 版本列表、安装、管理 |
| 下载 | download | 游戏、模组、整合包、资源包、光影 |
| 管理 | manage | 模组、存档、数据包、世界 |
| 设置 | settings | 启动器设置、主题、账号、关于 |

## 4. 技术栈

- **UI 框架**：Jetpack Compose
- **玻璃效果库**：Backdrop 2.0.0
- **导航**：Compose Navigation
- **依赖注入/状态**：Compose State、ViewModel（可选）
- **主题**：现有 `ThemeEngine` + `ThemePreset` 桥接到 Compose

## 5. 主题系统

### 5.1 颜色映射
- 主题预设主色（`preset.getColor()`）作为 glass tint
- 亮色模式背景：`Color(0xFFF0F4F8)` 或根据预设微调
- 暗色模式背景：`Color(0xFF1A1A2E)` 或根据预设微调
- 玻璃表面层：`Color.White.copy(alpha = 0.5f)`

### 5.2 暗色/亮色切换
- 继续复用现有 `themeMode` 设置
- Compose 中通过 `isSystemInDarkTheme()` 或保存的设置决定

## 6. 分阶段实施计划

由于涉及页面众多，必须分阶段推进：

### 阶段 1：Compose 基础框架
- 创建 `MainActivity` 作为唯一入口
- 配置 Compose 主题与 Backdrop
- 实现 `FCLGlassApp`、`GlassBackground`、`GlassNavHost`、`GlassBottomBar`
- 设置底部导航路由框架
- 调整 `minSdk` 为 31

### 阶段 2：核心页面重写
- 主页（Home）
- 版本列表页（Versions）
- 设置页（Settings）

### 阶段 3：下载模块重写
- 下载首页
- 版本安装页
- 模组/整合包/资源包/光影下载页

### 阶段 4：管理与账号模块重写
- 管理首页
- 模组管理、存档管理、世界管理
- 账号管理

### 阶段 5：细节打磨
- 页面切换动画
- 玻璃卡片、玻璃对话框统一组件
- 性能优化
- 无障碍与 RTL 适配

## 7. 保留与废弃

### 7.1 保留
- `FCLCore`：业务逻辑、下载、启动、模组管理核心
- `FCLauncher`：JNI、渲染、JRE 启动
- `JVMActivity`、`ControllerActivity` 等游戏相关 Activity
- 现有主题预设系统

### 7.2 废弃/替换
- `FCL/src/main/res/layout/` 中大部分 XML 布局（逐步替换）
- `FCL/src/main/java/com/tungsten/fcl/ui/` 中大部分传统 View 页面（逐步替换）
- `FCLLibrary` 中的自定义 View 组件（用 Compose 等价物替换）

## 8. 风险与约束

1. **工程量巨大**：80+ 页面/组件需要重写，可能需要数周时间
2. **构建验证困难**：当前远程环境无法下载 Backdrop/Compose 依赖，需在本地验证
3. **兼容性**：提升 minSdk 到 31 会放弃 Android 12 以下设备
4. **性能**：Backdrop 效果依赖 RenderEffect，低端设备可能掉帧
5. **维护**：需要同时维护新旧两套 UI 直到迁移完成

## 9. 验收标准

1. 应用启动后显示 Compose 主界面
2. 底部玻璃导航栏可见且可点击切换
3. 所有一级页面使用液态玻璃风格
4. 主题预设切换时，玻璃 tint 颜色同步变化
5. 亮/暗模式切换时，背景与玻璃效果正确适配
6. 原有游戏启动功能不受影响

## 10. 下一步

本设计文档批准后，将使用 writing-plans skill 制定阶段 1 的详细实现计划。
