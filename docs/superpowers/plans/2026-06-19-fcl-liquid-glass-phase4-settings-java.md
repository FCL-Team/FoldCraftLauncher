# FCL 液态玻璃 UI 迁移 - 阶段 4：设置与 Java

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将启动器设置、Java 运行时管理、JVM 参数管理等传统 View 页面迁移为液态玻璃 Compose UI，并接入设置导航。

**Architecture:** 在 `FCLGlassRoute` 中新增设置子路由；复用阶段 1 的 `GlassDialogManager` 处理确认/输入弹窗；复用阶段 2 的 `VersionSettingsPage` 中已有的设置项作为基础。业务逻辑继续复用 FCLCore 的 `ConfigHolder`、`JavaManager`、`GameJavaVersion` 等 API。

**Tech Stack:** Kotlin, Jetpack Compose, Compose Navigation, Backdrop, FCLCore.

---

## File Structure

| File | Responsibility |
| --- | --- |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt` | 新增设置路由：LauncherSettings、JavaRuntime、JvmArgs、QuickInput |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt` | 注册新路由并映射到对应 Page |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/launcher/LauncherSettingPage.kt` | 启动器全局设置页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/launcher/LauncherSettingStateHolder.kt` | 管理启动器设置状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JavaRuntimePage.kt` | Java 运行时列表与管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JavaRuntimeStateHolder.kt` | 管理 Java 运行时状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JvmArgsPage.kt` | JVM 参数管理页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassQuickInputDialog.kt` | 通用快速输入对话框（替代 `QuickInputDialog`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt` | 添加启动器设置、Java 管理、JVM 参数入口 |

---

## Task 1: Route Infrastructure

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Add routes to FCLGlassRoute**

In `FCLGlassRoute.kt`, add:

```kotlin
@Serializable
data object LauncherSettings : FCLGlassRoute()

@Serializable
data object JavaRuntime : FCLGlassRoute()

@Serializable
data object JvmArgs : FCLGlassRoute()

@Serializable
data class QuickInput(
    val title: String,
    val initialValue: String,
    val hint: String
) : FCLGlassRoute()
```

- [ ] **Step 2: Register routes in GlassNavHost**

Add `composable` blocks for each new route. For now use `GlassPlaceholderPage`.

- [ ] **Step 3: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add settings and java routes"
```

---

## Task 2: LauncherSettingPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/launcher/LauncherSettingPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/launcher/LauncherSettingStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt`

- [ ] **Step 1: Study old LauncherSettingPage**

Read `FCL/src/main/java/com/tungsten/fcl/ui/setting/LauncherSettingPage.java` and `SettingUI.java`. Identify settings categories:
- Language
- Theme / color
- Download source / concurrency
- Update channel
- Launcher behavior (kill game on exit, etc.)

- [ ] **Step 2: Create LauncherSettingStateHolder**

Create `LauncherSettingStateHolder.kt`:

```kotlin
class LauncherSettingStateHolder {
    val config = ConfigHolder.config()
    var language by mutableStateOf(config.localization)
    var theme by mutableStateOf(config.theme)
    var downloadSource by mutableStateOf(config.downloadType)
    var autoDownloadThreads by mutableStateOf(config.autoDownloadThreads)
    // ... other fields

    fun save() {
        config.localization = language
        config.theme = theme
        config.downloadType = downloadSource
        ConfigHolder.save()
    }
}
```

Use actual config fields from `Config` class.

- [ ] **Step 3: Create LauncherSettingPage UI**

Create `LauncherSettingPage.kt`:

```kotlin
@Composable
fun LauncherSettingPage(
    backdrop: Backdrop,
    onBack: () -> Unit
) {
    val stateHolder = remember { LauncherSettingStateHolder() }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "Launcher Settings", navigationIcon = { GlassBackButton(onBack) })
        LazyColumn(modifier = Modifier.weight(1f).padding(20.dp)) {
            item { LanguageSection(stateHolder) }
            item { ThemeSection(stateHolder) }
            item { DownloadSection(stateHolder) }
            item { BehaviorSection(stateHolder) }
        }
        GlassButton(
            backdrop = backdrop,
            onClick = { stateHolder.save(); onBack() },
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Text(text = "Save", color = Color.White)
        }
    }
}
```

- [ ] **Step 4: Update GlassNavHost and SettingsPage**

Replace `LauncherSettings` placeholder with real page.
Add a card in `SettingsPage` to navigate to `FCLGlassRoute.LauncherSettings`.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/launcher/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt
git commit -m "feat(glass): add LauncherSettingPage"
```

---

## Task 3: JavaRuntimePage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JavaRuntimePage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JavaRuntimeStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt`

- [ ] **Step 1: Study old ManageJavaDialog / RuntimeFragment**

Read `FCL/src/main/java/com/tungsten/fcl/dialog/ManageJavaDialog.java` and `FCL/src/main/java/com/tungsten/fcl/fragment/RuntimeFragment.kt`. Identify:
- Java runtime list
- Add Java runtime
- Remove Java runtime
- Auto download Java

- [ ] **Step 2: Create JavaRuntimeStateHolder**

Create `JavaRuntimeStateHolder.kt`:

```kotlin
class JavaRuntimeStateHolder {
    val javaRuntimes = mutableStateListOf<JavaRuntimeItem>()
    var isLoading by mutableStateOf(false)

    fun load() { /* read JavaManager */ }
    fun addJava(context: Context, path: String) { }
    fun removeJava(runtime: JavaRuntimeItem) { }
    fun downloadJava(context: Context, version: Int) { }
}
```

Define `JavaRuntimeItem` with name, version, path, architecture.

- [ ] **Step 3: Create JavaRuntimePage UI**

Create `JavaRuntimePage.kt`:

```kotlin
@Composable
fun JavaRuntimePage(
    backdrop: Backdrop,
    onBack: () -> Unit
) {
    val stateHolder = remember { JavaRuntimeStateHolder() }
    val context = LocalContext.current
    DisposableEffect(Unit) { stateHolder.load(); onDispose {} }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "Java Runtime", navigationIcon = { GlassBackButton(onBack) })
        LazyColumn(modifier = Modifier.weight(1f).padding(20.dp)) {
            items(stateHolder.javaRuntimes, key = { it.path }) { runtime ->
                GlassJavaRuntimeItem(
                    backdrop = backdrop,
                    runtime = runtime,
                    onRemove = { stateHolder.removeJava(runtime) }
                )
            }
        }
        Row(modifier = Modifier.padding(20.dp)) {
            GlassButton(onClick = { /* file picker or input dialog */ }) { Text("Add") }
            GlassButton(onClick = { /* show download options */ }) { Text("Download") }
        }
    }
}
```

- [ ] **Step 4: Update GlassNavHost and SettingsPage**

Replace `JavaRuntime` placeholder. Add a card in `SettingsPage`.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt
git commit -m "feat(glass): add JavaRuntimePage"
```

---

## Task 4: JvmArgsPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JvmArgsPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt`

- [ ] **Step 1: Create JvmArgsPage**

A simple page to edit global default JVM arguments. Store in `Config`.

```kotlin
@Composable
fun JvmArgsPage(backdrop: Backdrop, onBack: () -> Unit) {
    val config = remember { ConfigHolder.config() }
    var args by remember { mutableStateOf(config.jvmArgs ?: "") }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "JVM Arguments", navigationIcon = { GlassBackButton(onBack) })
        GlassTextField(
            value = args,
            onValueChange = { args = it },
            label = "JVM Args",
            modifier = Modifier.padding(20.dp).fillMaxWidth().weight(1f)
        )
        GlassButton(
            backdrop = backdrop,
            onClick = { config.jvmArgs = args; ConfigHolder.save(); onBack() },
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) { Text("Save", color = Color.White) }
    }
}
```

- [ ] **Step 2: Update GlassNavHost and SettingsPage**

Replace placeholder. Add SettingsPage entry.

- [ ] **Step 3: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/java/JvmArgsPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/SettingsPage.kt
git commit -m "feat(glass): add JvmArgsPage"
```

---

## Task 5: GlassQuickInputDialog

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassQuickInputDialog.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: Add DialogRequest.QuickInput**

In `GlassDialogManager.kt`:

```kotlin
data class QuickInput(
    val title: String,
    val initialValue: String,
    val hint: String,
    val onConfirm: (String) -> Unit,
    val onDismiss: () -> Unit = {}
) : DialogRequest()

fun showQuickInput(
    title: String,
    initialValue: String,
    hint: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit = {}
) = show(DialogRequest.QuickInput(title, initialValue, hint, onConfirm, onDismiss))
```

- [ ] **Step 2: Create GlassQuickInputDialog UI**

Create `GlassQuickInputDialog.kt`:

```kotlin
@Composable
fun GlassQuickInputDialog(backdrop: Backdrop, request: DialogRequest.QuickInput) {
    var text by remember { mutableStateOf(request.initialValue) }
    GlassDialogSurface(backdrop = backdrop) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(request.title, style = MaterialTheme.typography.titleLarge)
            GlassTextField(value = text, onValueChange = { text = it }, label = request.hint)
            Row {
                GlassTextButton(onClick = { request.onDismiss(); dismiss() }) { Text("Cancel") }
                GlassButton(onClick = { request.onConfirm(text); dismiss() }) { Text("OK") }
            }
        }
    }
}
```

- [ ] **Step 3: Wire in GlassDialogHost**

Add a branch for `DialogRequest.QuickInput`.

- [ ] **Step 4: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassQuickInputDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt
git commit -m "feat(glass): add GlassQuickInputDialog"
```

---

## Task 6: Final Compile and Cleanup

- [ ] **Step 1: Full compile**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

- [ ] **Step 2: Remove unused imports**

- [ ] **Step 3: Commit**

```bash
git commit -m "chore(glass): phase 4 settings and java compile cleanup"
```

---

## Spec Coverage Check

- Launcher settings: Task 2
- Java runtime: Task 3
- JVM args: Task 4
- Quick input dialog: Task 5
- Cleanup: Task 6
