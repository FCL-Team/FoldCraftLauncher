# FCL 液态玻璃 UI 迁移 - 阶段 2：版本深度管理

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将版本相关的传统 View 页面（版本设置、版本安装、已安装 Mod/World/资源包/光影管理）迁移为液态玻璃 Compose UI，并接入主导航。

**Architecture:** 在 `FCLGlassRoute` 中新增版本子路由，使用 `GlassNavHost` 内部导航或独立 Composable 页面承载版本设置/安装/管理流程。复用阶段 1 的 `GlassDialogManager` 处理确认弹窗。业务逻辑继续复用 `Versions`、`Profile`、`GameDirectory`、`ModManager` 等 FCLCore API。

**Tech Stack:** Kotlin, Jetpack Compose, Compose Navigation, Backdrop, FCLCore.

---

## File Structure

| File | Responsibility |
| --- | --- |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt` | 新增版本子路由：VersionSettings、VersionInstall、VersionMods、VersionWorlds、VersionResourcePacks、VersionShaderPacks、ModInfo |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt` | 注册新路由并映射到对应 Page |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionSettingsPage.kt` | 单个版本的设置页（替代 `VersionSettingsDialog`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionSettingsStateHolder.kt` | 管理版本设置状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallPage.kt` | 安装 Minecraft / 加载器向导（替代 `VersionInstallPage.java`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallStateHolder.kt` | 管理安装向导状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionModListPage.kt` | 已安装 Mod 列表与管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionModListStateHolder.kt` | 管理 Mod 列表状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionWorldListPage.kt` | 已安装世界列表与管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionWorldListStateHolder.kt` | 管理世界列表状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionResourcePackPage.kt` | 资源包管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionShaderPackPage.kt` | 光影包管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ModInfoPage.kt` | Mod 详情页（替代 `ModInfoDialog`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassWorldExportDialog.kt` | 导出世界对话框（替代 `WorldExportDialog`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/VersionsPage.kt` | 将版本项的 Settings 入口改为导航到 VersionSettingsPage |

---

## Task 1: Route Infrastructure

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Add routes to FCLGlassRoute**

In `FCLGlassRoute.kt`, add these serializable data classes/routes inside the sealed class:

```kotlin
@Serializable
data class VersionSettings(
    val profileName: String,
    val version: String
) : FCLGlassRoute()

@Serializable
data class VersionInstall(
    val profileName: String,
    val version: String? = null
) : FCLGlassRoute()

@Serializable
data class VersionMods(
    val profileName: String,
    val version: String
) : FCLGlassRoute()

@Serializable
data class VersionWorlds(
    val profileName: String,
    val version: String
) : FCLGlassRoute()

@Serializable
data class VersionResourcePacks(
    val profileName: String,
    val version: String
) : FCLGlassRoute()

@Serializable
data class VersionShaderPacks(
    val profileName: String,
    val version: String
) : FCLGlassRoute()

@Serializable
data class ModInfo(
    val modFileName: String,
    val profileName: String,
    val version: String
) : FCLGlassRoute()
```

- [ ] **Step 2: Register routes in GlassNavHost**

In `GlassNavHost.kt`, add `composable` blocks for each new route. For now use placeholder lambdas that render a `GlassPlaceholderPage` with the route name; the real pages are built in later tasks.

```kotlin
composable<FCLGlassRoute.VersionSettings> { backStackEntry ->
    val route = backStackEntry.toRoute<FCLGlassRoute.VersionSettings>()
    GlassPlaceholderPage(backdrop, "Version Settings: ${route.version}")
}
// ... similar placeholders for VersionInstall, VersionMods, VersionWorlds, VersionResourcePacks, VersionShaderPacks, ModInfo
```

Create `GlassPlaceholderPage` as a simple Composable in `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassPlaceholderPage.kt`:

```kotlin
@Composable
fun GlassPlaceholderPage(backdrop: Backdrop, title: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = title)
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
```

- [ ] **Step 3: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`
Expected: PASS if dependencies available; otherwise report hang/offline failure.

- [ ] **Step 4: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassPlaceholderPage.kt
git commit -m "feat(glass): add version management routes"
```

---

## Task 2: VersionSettingsPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionSettingsPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionSettingsStateHolder.kt`

- [ ] **Step 1: Study old VersionSettingsDialog**

Read `FCL/src/main/java/com/tungsten/fcl/dialog/VersionSettingsDialog.java`. Identify settings categories:
- Memory (min/max RAM)
- Java runtime selection
- JVM arguments
- Renderer / game directory / version isolation
- Server address
- Window size
- Etc.

- [ ] **Step 2: Create VersionSettingsStateHolder**

Create `VersionSettingsStateHolder.kt`:

```kotlin
class VersionSettingsStateHolder(
    val profile: Profile,
    val version: String
) {
    private val versionSetting: VersionSetting = Versions.getVersionSetting(profile, version)
    // expose states for each setting field
    val javaVersion by mutableStateOf(versionSetting.javaVersion)
    val maxMemory by mutableStateOf(versionSetting.maxMemory)
    val minMemory by mutableStateOf(versionSetting.minMemory)
    val jvmArgs by mutableStateOf(versionSetting.jvmArgs ?: "")
    val server by mutableStateOf(versionSetting.server ?: "")
    val width by mutableStateOf(versionSetting.width)
    val height by mutableStateOf(versionSetting.height)
    // ... other fields as needed

    fun save() {
        versionSetting.javaVersion = javaVersion
        versionSetting.maxMemory = maxMemory
        versionSetting.minMemory = minMemory
        versionSetting.jvmArgs = jvmArgs
        versionSetting.server = server
        versionSetting.width = width
        versionSetting.height = height
        // persist
        Versions.saveVersionSetting(profile, version)
    }
}
```

Use actual types from `VersionSetting` class in FCLCore.

- [ ] **Step 3: Create VersionSettingsPage UI**

Create `VersionSettingsPage.kt`:

```kotlin
@Composable
fun VersionSettingsPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    onBack: () -> Unit
) {
    val stateHolder = remember { VersionSettingsStateHolder(profile, version) }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.version_settings),
            navigationIcon = { GlassBackButton(onBack) }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { MemorySection(stateHolder) }
            item { JavaSection(stateHolder) }
            item { JvmArgsSection(stateHolder) }
            item { DisplaySection(stateHolder) }
            item { ServerSection(stateHolder) }
        }
        GlassButton(
            backdrop = backdrop,
            onClick = { stateHolder.save(); onBack() },
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save), color = Color.White)
        }
    }
}
```

Create section composables (`MemorySection`, `JavaSection`, etc.) using `GlassCard` or `GlassDialogSurface` as card containers, `GlassTextField` for inputs, and dropdowns/sliders where appropriate.

- [ ] **Step 4: Wire VersionsPage settings entry**

In `VersionsPage.kt`, change the `onSettings` callback from showing an alert to navigating:

```kotlin
onSettings = {
    navController.navigate(FCLGlassRoute.VersionSettings(item.profile.name, item.version))
}
```

Ensure `navController` is available in `VersionsPage`.

- [ ] **Step 5: Compile check**

Run compile check.

- [ ] **Step 6: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/VersionsPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add VersionSettingsPage"
```

---

## Task 3: VersionInstallPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallStateHolder.kt`

- [ ] **Step 1: Study old VersionInstallPage**

Read `FCL/src/main/java/com/tungsten/fcl/ui/download/version/VersionInstallPage.java`. Identify:
- Game version selection
- Loader selection (Forge, Fabric, OptiFine, Quilt, etc.)
- Loader version selection
- Install task execution

- [ ] **Step 2: Create VersionInstallStateHolder**

Create `VersionInstallStateHolder.kt`:

```kotlin
class VersionInstallStateHolder(
    val profile: Profile,
    val parentVersion: String? = null
) {
    val gameVersions = mutableStateListOf<String>()
    var selectedGameVersion by mutableStateOf<String?>(null)
    val loaders = mutableStateListOf<LoaderType>()
    var selectedLoader by mutableStateOf<LoaderType?>(null)
    val loaderVersions = mutableStateListOf<String>()
    var selectedLoaderVersion by mutableStateOf<String?>(null)
    var isInstalling by mutableStateOf(false)

    fun loadGameVersions() { /* fetch remote */ }
    fun loadLoaderVersions() { /* fetch remote */ }
    fun install(context: Context, onComplete: () -> Unit) { /* run FCLCore task */ }
}
```

Use actual FCLCore types for game versions and loaders.

- [ ] **Step 3: Create VersionInstallPage UI**

Create `VersionInstallPage.kt` as a wizard-style page:

```kotlin
@Composable
fun VersionInstallPage(
    backdrop: Backdrop,
    profile: Profile,
    parentVersion: String? = null,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val stateHolder = remember { VersionInstallStateHolder(profile, parentVersion) }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "Install Version", navigationIcon = { GlassBackButton(onBack) })
        LazyColumn(modifier = Modifier.weight(1f).padding(20.dp)) {
            item { GameVersionSection(stateHolder) }
            item { LoaderSection(stateHolder) }
            item { LoaderVersionSection(stateHolder) }
        }
        GlassButton(
            backdrop = backdrop,
            onClick = { stateHolder.install(context, onComplete) },
            enabled = stateHolder.selectedGameVersion != null,
            modifier = Modifier.padding(20.dp).fillMaxWidth()
        ) {
            Text(text = if (stateHolder.isInstalling) "Installing..." else "Install", color = Color.White)
        }
    }
}
```

- [ ] **Step 4: Add navigation entry point**

Add a button in `VersionsPage` (e.g., floating action button or top bar action) to navigate to `FCLGlassRoute.VersionInstall(profile.name)`.

- [ ] **Step 5: Compile check and commit**

Run compile check.

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/
git commit -m "feat(glass): add VersionInstallPage"
```

---

## Task 4: VersionModListPage and ModInfoPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionModListPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionModListStateHolder.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ModInfoPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Study old ModListPage**

Read `FCL/src/main/java/com/tungsten/fcl/ui/manage/ModListPage.java` and `dialog/ModInfoDialog.java`. Identify:
- Mod list loading
- Enable/disable mod
- Delete mod
- Show mod info

- [ ] **Step 2: Create VersionModListStateHolder**

Create `VersionModListStateHolder.kt`:

```kotlin
class VersionModListStateHolder(
    val profile: Profile,
    val version: String
) {
    val mods = mutableStateListOf<ModListItem>()
    var isLoading by mutableStateOf(false)

    fun load() { /* read mods directory */ }
    fun toggleMod(mod: ModListItem) { /* rename .jar / .disabled */ }
    fun deleteMod(mod: ModListItem) { /* delete file */ }
}
```

Use actual types from FCLCore (e.g., `ModManager`).

- [ ] **Step 3: Create VersionModListPage UI**

Create `VersionModListPage.kt`:

```kotlin
@Composable
fun VersionModListPage(
    backdrop: Backdrop,
    profile: Profile,
    version: String,
    navController: NavController,
    onBack: () -> Unit
) {
    val stateHolder = remember { VersionModListStateHolder(profile, version) }
    DisposableEffect(Unit) { stateHolder.load(); onDispose {} }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "Mods", navigationIcon = { GlassBackButton(onBack) })
        LazyColumn(modifier = Modifier.weight(1f).padding(20.dp)) {
            items(stateHolder.mods, key = { it.fileName }) { mod ->
                GlassModItem(
                    backdrop = backdrop,
                    mod = mod,
                    onToggle = { stateHolder.toggleMod(mod) },
                    onInfo = { navController.navigate(FCLGlassRoute.ModInfo(mod.fileName, profile.name, version)) },
                    onDelete = { stateHolder.deleteMod(mod) }
                )
            }
        }
    }
}
```

- [ ] **Step 4: Create ModInfoPage**

Create `ModInfoPage.kt` to display mod name, version, description, authors, etc. Use actual `ModInfo` parsing from FCLCore.

- [ ] **Step 5: Update GlassNavHost**

Replace placeholders for `VersionMods` and `ModInfo` with real pages.

- [ ] **Step 6: Add entry from VersionSettingsPage**

Add a navigation row in `VersionSettingsPage` to "Manage Mods".

- [ ] **Step 7: Compile check and commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add VersionModListPage and ModInfoPage"
```

---

## Task 5: VersionWorldListPage and GlassWorldExportDialog

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionWorldListPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionWorldListStateHolder.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassWorldExportDialog.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Study old WorldListPage and WorldExportDialog**

Read `FCL/src/main/java/com/tungsten/fcl/ui/manage/WorldListPage.java` and `dialog/WorldExportDialog.java`.

- [ ] **Step 2: Create VersionWorldListStateHolder**

```kotlin
class VersionWorldListStateHolder(
    val profile: Profile,
    val version: String
) {
    val worlds = mutableStateListOf<WorldListItem>()
    fun load() { /* read saves directory */ }
    fun deleteWorld(world: WorldListItem) { }
    fun exportWorld(world: WorldListItem) { }
}
```

- [ ] **Step 3: Create VersionWorldListPage UI**

List worlds with name, game mode, icon, and actions (delete, export).

- [ ] **Step 4: Create GlassWorldExportDialog**

Create `GlassWorldExportDialog.kt` as a full-screen host dialog using `GlassDialogSurface`. It takes a `WorldListItem` and export path, then calls the old export logic.

- [ ] **Step 5: Update GlassNavHost and VersionSettingsPage**

Replace `VersionWorlds` placeholder and add "Manage Worlds" entry in settings.

- [ ] **Step 6: Compile check and commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassWorldExportDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add VersionWorldListPage and world export dialog"
```

---

## Task 6: ResourcePack and ShaderPack Pages

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionResourcePackPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionResourcePackStateHolder.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionShaderPackPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionShaderPackStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/VersionSettingsPage.kt`

- [ ] **Step 1: Create pages and state holders**

Follow the same pattern as `VersionModListPage`. Use FCLCore resource pack / shader pack managers.

- [ ] **Step 2: Register routes and add settings entries**

Replace `VersionResourcePacks` and `VersionShaderPacks` placeholders. Add navigation rows in `VersionSettingsPage`.

- [ ] **Step 3: Compile check and commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/version/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add resource pack and shader pack pages"
```

---

## Task 7: Final Compile and Cleanup

- [ ] **Step 1: Full compile**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

- [ ] **Step 2: Remove unused imports**

Search for unused imports in modified files and remove.

- [ ] **Step 3: Commit**

```bash
git commit -m "chore(glass): phase 2 version management compile cleanup"
```

---

## Spec Coverage Check

- Version settings: Task 2
- Version install: Task 3
- Mod list + info: Task 4
- World list + export: Task 5
- Resource/Shader packs: Task 6
- Route infrastructure: Task 1
- Compile/cleanup: Task 7
