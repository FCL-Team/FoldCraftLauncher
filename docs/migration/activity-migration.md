# 其余 Activity 处置报告（阶段三 · 小步骤 3.7）

> 生成时间：2026-08-02 ｜ 分支：`feature/miuix-activities`（基于 `feature/miuix-migration`，含 3.1–3.6）
> 范围：`AndroidManifest.xml` 声明的 9 个 Activity 中除 MainActivity（3.6 已完成）外的 8 个 + activity-alias。
> 输入：方案.md §3.7、ui-inventory.md §4、interaction-map.md §1.2–1.7/G12、component-mapping.md §1.1/§2。
> **验证边界：无真机，仅 `:FCL:assembleDebug` 构建门禁；真机验证清单见 §4。**

---

## 1. 决策总表

| Activity | 决策 | 理由（评估依据） | 变更文件 |
|---|---|---|---|
| `SplashActivity`（LAUNCHER） | **保留原生** | 启动链路第一屏：SplashScreen API + EULA/存储权限门 + 运行时检查 + Fragment 容器（Eula/RuntimeFragment 未迁移）+ modpack 导入 Intent 处理，且是 `ImportActivity` alias 的 target。无真机验证下重写首启路径风险/收益比不成立；component-mapping §1.4 的「Fragment 容器改 Compose 页面」属后续专项（需先迁两个 Fragment） | 无 |
| `ImportActivity`（activity-alias） | **保留（不可删）** | 无对应类的 alias，target=SplashActivity，zip/mrpack/7z VIEW 导入入口（interaction-map G12「最易误删」）。Manifest 一行未动 | 无 |
| `WebActivity` | **迁移 Compose+Miuix（带开关）** | P0：55 行、WebView + 居中进度条，无 native/渲染耦合；WebView 本体按 component-mapping §1.1 保留原生 AndroidView 包装 | `activity/WebActivity.kt`（新，Java→Kotlin 双路径）、`activity/compose/WebScreen.kt`（新）、`activity/compose/ComposeActivities.kt`（新）、删 `activity/WebActivity.java` |
| `ControllerActivity` | **保留原生 + 仅主题色对齐（红线）** | 红线：GameMenu DrawerLayout 覆盖层 + 音量键唤出双 Drawer（interaction-map §1.5 难点「高」）。未动结构，仅系统栏底色透明对齐 | `activity/ControllerActivity.java`（+5 行） |
| `ShellActivity` | **迁移 Compose+Miuix（带开关）** | P1 但结构简单：日志流 + 单行输入 + ShellUtil 回调，无 native 耦合（component-mapping §1.5 明确「ShellActivity 日志窗可 Compose 化」）；manifest `adjustResize` 不动 | `activity/ShellActivity.kt`（新，Java→Kotlin 双路径）、`activity/compose/ShellScreen.kt`（新）、`ComposeActivities.kt`、删 `activity/ShellActivity.java` |
| `JVMActivity` | **保留原生 + 仅主题色对齐（红线邻近）** | 最高风险（interaction-map §1.7）：静态 `fclBridge`/`menuType`/`isRunning` 桥接状态、TextureView surface 四回调时序、返回键映射 ESC、音量键 Drawer 防抖、软键盘顶起画面。未动任何结构与输入分发，仅系统栏底色透明 | `activity/JVMActivity.java`（+6 行） |
| `JVMCrashActivity` | **保留原生** | 独立 `:crash` 进程的**崩溃兜底页**：游戏已崩溃时拉起，此路径必须最大健壮；Compose 运行时初始化增加失败面，而该页曝光率极低、视觉一致性收益趋零。无真机验证下判定「有风险保留原生」 | 无 |
| `FileBrowserActivity`（FCLLibrary） | **保留原生** | 红线模块，`ActivityResultLauncher` 桥接即可（component-mapping §1.2） | 无 |
| `CrashReportActivity`（FCLLibrary） | **保留原生** | 红线模块，Java 层崩溃兜底 | 无 |

---

## 2. 迁移实现说明

### 2.1 开关回滚（对齐 ComposeDialogs / ComposeMainUI 模式）

新增 `FCL/src/main/java/com/tungsten/fcl/activity/compose/ComposeActivities.kt`：

```kotlin
object ComposeActivities {
    const val USE_COMPOSE_WEB = true   // false → 回滚 activity_web.xml 路径
    const val USE_COMPOSE_SHELL = true // false → 回滚 activity_shell.xml 路径
}
```

Activity 类名、Manifest、Intent 契约全部不变：开关在 Activity `onCreate` 内二选一
（`setContent { FCLTheme(this) { ... } }` vs 旧 `setContentView(R.layout...)`），
旧布局 XML 与旧逻辑完整保留在 Kotlin 双路径中，改常量即整体回滚。

### 2.2 WebActivity → `WebScreen.kt`

- WebView 本体 `AndroidView` 包装，JS 开启 / `LOAD_NO_CACHE` / `onPageStarted` 显示、
  `onPageFinished` 隐藏进度（居中 `CircularProgressIndicator` 替代原 ProgressBar）逐一对齐；
  `onRelease` 调 `webView.destroy()`；
- `onDestroy` 清 WebView 缓存（`AndroidUtils.clearWebViewCache`）两条路径共用；
- extra `url` 必传契约保留（`intent.extras!!.getString("url")!!`，与原版同样的空指针语义）。

### 2.3 ShellActivity → `ShellScreen.kt`

- 黑底白字终端观感保留；日志整串 state 追加（对齐 `EditText.append` 流式语义），
  `SelectionContainer` 保留原文本可选中；新输出 `LaunchedEffect` 自动滚到底部；
- 提交语义逐条对齐遗留 `afterTextChanged`：回车 → 回显 `"->cmd"` → 含 `"clear"` 清屏且
  不发给 shell → 否则 `shellUtil.append(cmd)`（cmd 含结尾 `\n`，形态一致）；
- 回车双通道兼容：`KeyboardActions(onSend)`（singleLine 下 IME Send）+ `onValueChange`
  检测结尾 `\n`（会把回车插成换行的键盘）；
- 点击日志区 → 输入框 `FocusRequester` 获焦弹键盘；`ShellUtil` 生命周期
  （onCreate start / onDestroy interrupt）与欢迎语两条路径共用。

### 2.4 ControllerActivity / JVMActivity 主题色对齐（仅 +5/+6 行）

```java
getWindow().setStatusBarColor(Color.TRANSPARENT);
getWindow().setNavigationBarColor(Color.TRANSPARENT);
```

现状：`themes.xml` 静态写死 `android:statusBarColor = default_theme_color`（#7797CF），
与 ThemeEngine 运行时用户主题色可能不一致；FCLActivity 又对所有 Activity 施加
IMMERSIVE_STICKY 全屏，系统栏仅在边缘滑动短暂呼出时可见。改透明后短暂呼出时露出的是
窗口内容（Controller 的 ThemeEngine 背景 / JVM 的游戏画面），即与主题一致。
未触碰：GameMenu、TextureView surface 时序、按键/手势分发、FORCE_RESOLUTION、
静态桥接字段——评估中未发现需要动结构的点，红线保持完整。

### 2.5 主题桥

Compose 路径统一走既有 `FCLTheme(context)`（ui/theme/FCLTheme.kt，3.2 抽取的环境自解析版）：
themeMode 读 SP "launcher"（与 `FCLActivity.applySavedNightMode` 同源）、主色经 fakefx
属性桥观察 ThemeEngine，与 3.1–3.6 全部页面一致。

---

## 3. 构建门禁

- 命令：`GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug`
- 结果：**BUILD SUCCESSFUL in 1m 12s**（2026-08-02，`feature/miuix-activities`）。
  编译警告仅 7 条且全部位于既有文件（DriverSelectDialog/RendererSelectDialog/FCLTaskDialog），
  本步新增/改动文件零警告零错误。

## 4. 真机验证清单（无真机，以下全部未验）

迁移项（开关 true 路径）：

- [ ] WebActivity：从「帮助」等入口打开网页，加载中居中进度显隐正确；页面可交互、
      JS 正常；退出后再次进入无缓存残留（clearWebViewCache 生效）；横屏 configChanges 不重建
- [ ] WebActivity 回滚：`USE_COMPOSE_WEB = false` 后行为与迁移前完全一致
- [ ] ShellActivity：主界面 back 长按隐藏入口进入；欢迎语两行；输入命令回车执行、
      输出追加并自动滚底；`clear` 清屏；点日志区弹键盘；软键盘弹出时 adjustResize 不遮挡输入框；
      退出 Activity 后 shell 进程被 interrupt（`ps` 无残留 sh）
- [ ] ShellActivity 回滚：`USE_COMPOSE_SHELL = false` 后行为与迁移前完全一致
- [ ] 主题联动：设置页改主题色/深浅色模式后，Web/Shell 两页 Miuix 颜色即时跟随

主题对齐项：

- [ ] ControllerActivity：手柄编辑器内边缘滑动短暂呼出系统栏，栏底色透明（不再闪
      静态 #7797CF）；音量键唤出双 Drawer、返回即 finish 行为不变
- [ ] JVMActivity：游戏画面全屏无系统栏残色；TextureView 渲染、返回键=ESC、
      音量键 Drawer、软键盘顶起画面全部无回归（重点回归项，红线邻近）

保留项抽查：

- [ ] JVMCrashActivity：构造一次游戏崩溃，崩溃页日志预览/重启/关闭/上传/分享正常，
      `:crash` 进程杀进程语义不变
- [ ] ImportActivity alias：外部文件管理器打开 .mrpack/.zip 拉起启动器并进入导入流程
      （验证 alias 未被本步影响）

## 5. 遗留问题

1. **SplashActivity 未迁移**：依赖 EulaFragment/RuntimeFragment 两个 Fragment 先迁移
   （component-mapping §1.4 目标为「保留 SplashScreen API，Fragment 容器改 Compose 页面」），
   建议单独立项，不在 3.7 范围内硬做。
2. **JVMCrashActivity 视觉陈旧**：保留原生意味着崩溃页仍是旧 FCL 风格；若后续要统一，
   需先在 `:crash` 进程做 Compose 冒烟验证再迁移。
3. **Shell 终端观感**：黑底白字 + Miuix TextField 的混搭是刻意保留（终端语义），
   若设计要求全 Miuix 配色可在阶段四打磨。
4. `activity_web.xml` / `activity_shell.xml` 为回滚路径保留，待迁移稳定后方可随
   开关常量一起删除（对齐 3.2 对话框「旧 XML 保留未删」策略）。
