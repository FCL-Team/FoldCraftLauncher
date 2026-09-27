# FCL 模块源码通读报告

对 `FCL` 模块全部 Java/Kotlin 源码（889 个 Java + 196 个 Kotlin，约 17.6 万行）的通读记录，按包分片成 12 篇详细报告。报告基于 2026-09-27 的 main 分支源码。

## 目录

| 文档 | 范围 |
|---|---|
| [01-fclcore-task-event-launch.md](01-fclcore-task-event-launch.md) | fclcore 的 task / event / launch 三个包（任务系统、事件总线、启动器核心） |
| [02-fclcore-fakefx.md](02-fclcore-fakefx.md) | fclcore/fakefx（OpenJFX 数据层移植，结构级略读） |
| [03-fclcore-util.md](03-fclcore-util.md) | fclcore/util 及全部子包（网络/zip/版本号/gson/png/skin 等） |
| [04-fclcore-auth-download.md](04-fclcore-auth-download.md) | fclcore 的 auth 与 download 包（账户认证、下载体系） |
| [05-fclcore-game-mod.md](05-fclcore-game-mod.md) | fclcore 的 game 与 mod 包（版本模型、模组与整合包） |
| [06-fcl-ui.md](06-fcl-ui.md) | com/tungsten/fcl/ui（8 个主页面全部页面树与交互流程） |
| [07-fcl-control.md](07-fcl-control.md) | com/tungsten/fcl/control（游戏内悬浮控件系统） |
| [08-fcl-activity-game-setting.md](08-fcl-activity-game-setting.md) | fcl 根目录与 activity / game / setting / fragment / upgrade / terracotta / scoped / util |
| [09-com-mio.md](09-com-mio.md) | com/mio 全包（新代码层） |
| [10-fclauncher-sdl.md](10-fclauncher-sdl.md) | fclauncher + org/libsdl/app + org/lwjgl/glfw（JVM 启动后端与 SDL） |
| [11-fcllibrary.md](11-fcllibrary.md) | fcllibrary 组件库（基类体系、主题引擎、文件浏览器、崩溃处理） |
| [12-build-manifest-jni-res.md](12-build-manifest-jni-res.md) | 构建脚本、Manifest、JNI、资源与 assets 概览 |

## 总体分层

```
FCLApp（轻量 Application，仅跟踪 currentActivity）
 ├─ SplashActivity → EulaFragment/RuntimeFragment（EULA/权限/运行时解压）
 │   └─ enterLauncher：RendererManager → JavaManager → Controllers → ConfigHolder.init
 ├─ MainActivity（ViewPager2 纵向承载 8 个 UI，UIManager 管理，页面随回收销毁不保留状态）
 ├─ JVMActivity（游戏/Jar 执行器共用，TextureView 驱动，静态 fclBridge 单游戏）
 ├─ JVMCrashActivity（独立 :crash 进程）＋ ProcessService（独立 :jvm 进程，安装器宿主）
 └─ 底层：fclcore（HMCL 移植核心）+ fclauncher（Pojav 移植启动后端）
          + fcllibrary（组件库）+ com.mio（新代码层）+ org.libsdl（SDL3 嵌入式改造）
```

重初始化不在 Application：强杀进程可能不经过 Splash 直接落 MainActivity，故 MainActivity.onCreate 有 `ConfigHolder.isInit()` 兜底双保险。

## 点击启动 → 游戏画面全链路

`Versions.launch` → LauncherHelper（TaskDialog 六阶段：MaintainTask 版本维护 → 全量 mod 扫描+lwjgl3ify 补丁 → Java 版本校验 → 完整性检查+释放 MioLibPatcher/MioLaunchWrapper → 登录（进度回调实时写弹窗）→ 构建 LaunchOptions+FCLGameLauncher+五个人工确认点）→ `DefaultLauncher.generateCommandLine` 组装完整 java 命令行 → `FCLauncher.launchMinecraft` → JVMActivity `onSurfaceTextureAvailable` 才真正 `fclBridge.execute(surface, callback)` → native 侧 setenv/dlopen/VMLauncher 在同进程内拉起 OpenJDK（非 OS 进程）→ `mio.Wrapper` 包装主类 → 游戏跑 GL4ES/Zink/SDL 渲染路径，输入从 control 包经 JNI 管道进游戏。

## 与 AGENTS.md 的出入及遗留问题

1. **AGENTS.md 已过时一处**：`FCLCommonPage` / `FCLPageManager` / `FCLTempPage` / `FCLUILifecycleCallbacks` 四个类在仓库中已不存在（grep 零命中），页面机制实际是 `FCLMultiPageUI`（内层 ViewPager2 + overlay 临时页栈）+ `FCLPage`。
2. DownloadUI 与 FCLMultiPageUI 并存两套临时页栈（自维护 overlay 栈 vs 统一栈），跨 UI 弹栈时需注意调用侧。
3. 已知遗留/坑（代码注释自证）：`OAuth.IS_CANCELED` 为 public static 可变字段（并发登录互相取消）；`GameMenu.initCursorView` 的 Y 偏移误用 `getMouseOffsetX()`（笔误）；favorite Room 库 v2 破坏式迁移待补；fakefx 约 1/5 死代码可清理；ClassicAccountLoginDialog 空壳、SettingUI 的 DocIndex 三类未挂载；`LwjglKeycodeMap` KEY_3 重复添加。
4. 其他非显然行为：`GameVersionNumber` 依赖类加载时解析 versions.txt 建静态表（首次触达成本高）；`HttpRequest` 的 GET 不校验状态码与 POST 不对称；`CacheRepository.getStorage` 读锁包 computeIfAbsent 存在理论竞态；`OSRestriction.allow()` 恒 false 使所有带 OS 规则的库一律不适用（Android 特化）。
