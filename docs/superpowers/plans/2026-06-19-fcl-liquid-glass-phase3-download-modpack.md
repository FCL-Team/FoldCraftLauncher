# FCL 液态玻璃 UI 迁移 - 阶段 3：下载与整合包

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将整合包下载/安装流程和 Addon 下载对话框迁移为液态玻璃 Compose UI，并接入下载导航。

**Architecture:** 在 `FCLGlassRoute` 中新增整合包相关路由；复用阶段 2 的 `VersionInstallPage` 完成整合包安装；使用 `GlassDialogManager` 处理选择/URL 输入弹窗。业务逻辑继续复用 FCLCore 的 `ModpackHelper`、`CurseForge`、`Modrinth`、`RemoteMod` 等 API。

**Tech Stack:** Kotlin, Jetpack Compose, Compose Navigation, Backdrop, FCLCore.

---

## File Structure

| File | Responsibility |
| --- | --- |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt` | 新增整合包路由：ModpackDownload、ModpackInstall、ModpackSelection、ModpackInfo、AddonDownload |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt` | 注册新路由并映射到对应 Page |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackDownloadPage.kt` | 整合包搜索/浏览列表页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackDownloadStateHolder.kt` | 管理整合包搜索列表状态 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackInfoPage.kt` | 整合包详情页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackSelectionPage.kt` | 整合包文件选择页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackInstallPage.kt` | 整合包安装页（复用版本安装结构） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/AddonDownloadPage.kt` | Addon 通用下载详情页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassModpackUrlDialog.kt` | 通过 URL 安装整合包的输入对话框 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt` | 新增「整合包」入口卡片 |

---

## Task 1: Route Infrastructure

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Add routes to FCLGlassRoute**

In `FCLGlassRoute.kt`, add these routes inside the sealed class:

```kotlin
@Serializable
data class ModpackDownload(
    val type: String // "curseforge" or "modrinth"
) : FCLGlassRoute()

@Serializable
data class ModpackInfo(
    val type: String,
    val projectId: String,
    val projectName: String
) : FCLGlassRoute()

@Serializable
data class ModpackSelection(
    val type: String,
    val projectId: String,
    val versionId: String,
    val gameVersion: String
) : FCLGlassRoute()

@Serializable
data class ModpackInstall(
    val profileName: String,
    val version: String,
    val modpackFileName: String,
    val modpackFilePath: String
) : FCLGlassRoute()

@Serializable
data class AddonDownload(
    val addonId: String,
    val addonType: String,
    val projectName: String
) : FCLGlassRoute()
```

- [ ] **Step 2: Register routes in GlassNavHost**

Add `composable` blocks for each new route. For now use `GlassPlaceholderPage` with the route name.

- [ ] **Step 3: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add modpack download routes"
```

---

## Task 2: ModpackDownloadPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackDownloadPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackDownloadStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt`

- [ ] **Step 1: Study old ModpackDownloadPage**

Read `FCL/src/main/java/com/tungsten/fcl/ui/download/modpack/ModpackDownloadPage.java`. Identify:
- Source switching (CurseForge / Modrinth)
- Search query handling
- Pagination
- Item click → ModpackInfoPage

- [ ] **Step 2: Create ModpackDownloadStateHolder**

Create `ModpackDownloadStateHolder.kt`:

```kotlin
class ModpackDownloadStateHolder(val type: String) {
    var query by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    val items = mutableStateListOf<ModpackListItem>()
    var page by mutableStateOf(0)
    var hasMore by mutableStateOf(true)

    fun search() { /* fetch from CurseForge or Modrinth */ }
    fun loadMore() { /* fetch next page */ }
}
```

Define `ModpackListItem` with: project id, name, summary, icon url, author, download count.

- [ ] **Step 3: Create ModpackDownloadPage UI**

Create `ModpackDownloadPage.kt`:

```kotlin
@Composable
fun ModpackDownloadPage(
    backdrop: Backdrop,
    type: String,
    navController: NavController,
    onBack: () -> Unit
) {
    val stateHolder = remember { ModpackDownloadStateHolder(type) }
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "Modpacks", navigationIcon = { GlassBackButton(onBack) })
        GlassSearchBar(
            value = stateHolder.query,
            onValueChange = { stateHolder.query = it },
            onSearch = { stateHolder.search() }
        )
        LazyColumn(modifier = Modifier.weight(1f).padding(20.dp)) {
            items(stateHolder.items, key = { it.projectId }) { item ->
                GlassModpackItem(
                    backdrop = backdrop,
                    item = item,
                    onClick = {
                        navController.navigate(FCLGlassRoute.ModpackInfo(type, item.projectId, item.name))
                    }
                )
            }
        }
    }
}
```

- [ ] **Step 4: Add entry from DownloadPage**

In `DownloadPage.kt`, add a card or button that navigates to `FCLGlassRoute.ModpackDownload("curseforge")` or `"modrinth"`.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add ModpackDownloadPage"
```

---

## Task 3: ModpackInfoPage and ModpackSelectionPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackInfoPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackSelectionPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Study old dialogs/pages**

Read `FCL/src/main/java/com/tungsten/fcl/dialog/ModpackInfoDialog.java` and `FCL/src/main/java/com/tungsten/fcl/dialog/ModpackSelectionDialog.java`.

- [ ] **Step 2: Create ModpackInfoPage**

Show modpack icon, name, summary, description, categories, download count. List available versions. On version click, navigate to `ModpackSelection`.

- [ ] **Step 3: Create ModpackSelectionPage**

Show files for the selected modpack version. On file click, download the file then navigate to `ModpackInstall`.

- [ ] **Step 4: Update GlassNavHost**

Replace `ModpackInfo` and `ModpackSelection` placeholders with real pages.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add ModpackInfoPage and ModpackSelectionPage"
```

---

## Task 4: ModpackInstallPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackInstallPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ModpackInstallStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Implement ModpackInstallPage**

Reuse the install flow from `VersionInstallPage`. After downloading a modpack file, this page should:
- Allow choosing a name for the new version
- Optionally allow selecting game version / loader
- Run the modpack install task via FCLCore
- Show progress via `GlassDialogManager.showProgress`

- [ ] **Step 2: Update GlassNavHost**

Replace `ModpackInstall` placeholder with real page.

- [ ] **Step 3: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/modpack/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add ModpackInstallPage"
```

---

## Task 5: GlassModpackUrlDialog and AddonDownloadPage

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassModpackUrlDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/AddonDownloadPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

- [ ] **Step 1: Add DialogRequest.ModpackUrl**

In `GlassDialogManager.kt`, add:

```kotlin
data class ModpackUrl(
    val onConfirm: (String) -> Unit,
    val onDismiss: () -> Unit = {}
) : DialogRequest()

fun showModpackUrl(onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) =
    show(DialogRequest.ModpackUrl(onConfirm, onDismiss))
```

- [ ] **Step 2: Create GlassModpackUrlDialog UI**

Input field for URL, confirm and dismiss buttons. On confirm, validate URL and call `onConfirm(url)`.

- [ ] **Step 3: Wire DialogRequest.ModpackUrl in GlassDialogHost**

Add a branch in the `when` expression.

- [ ] **Step 4: Create AddonDownloadPage**

Read old `DownloadAddonDialog.java`. Create a simple page to show addon details and download options.

- [ ] **Step 5: Update GlassNavHost**

Replace `AddonDownload` placeholder with real page.

- [ ] **Step 6: Add URL entry point**

In `DownloadPage`, add a button/card to install modpack via URL:

```kotlin
GlassDialogManager.showModpackUrl { url ->
    navController.navigate(FCLGlassRoute.ModpackInstall(...))
}
```

- [ ] **Step 7: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/ \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/AddonDownloadPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
git commit -m "feat(glass): add modpack URL dialog and addon download page"
```

---

## Task 6: Final Compile and Cleanup

- [ ] **Step 1: Full compile**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

- [ ] **Step 2: Remove unused imports**

- [ ] **Step 3: Commit**

```bash
git commit -m "chore(glass): phase 3 modpack download compile cleanup"
```

---

## Spec Coverage Check

- Modpack search: Task 2
- Modpack info/selection: Task 3
- Modpack install: Task 4
- URL dialog: Task 5
- Addon download: Task 5
- Cleanup: Task 6
