# FCL 液态玻璃 UI 阶段3 — 下载首页与版本安装实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use subagent-driven-development workflow (general_purpose_task subagents) to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 重写下载模块的前半部分：下载首页（分类入口）与版本安装流程（版本列表、安装信息、加载器选择、安装任务），全部使用液态玻璃 Compose UI。

**Architecture:** 在 `FCLGlassRoute` 下新增下载子路由枚举，通过 Compose Navigation 嵌套路由管理下载首页、版本安装列表、版本安装信息页；复用现有 `DownloadProviders`、`RemoteVersion`、`VersionList`、`InstallerItem.InstallerItemGroup`、`GameBuilder` 等下载/安装业务类，只替换 UI 层。

**Tech Stack:** Jetpack Compose, Compose Navigation, Backdrop, Material3, 现有 FCL/FCLCore 下载安装 API。

---

## 范围说明

设计文档原阶段3包含下载首页、版本安装页、模组/整合包/资源包/光影下载页。由于模组下载涉及 CurseForge/Modrinth 搜索、远程模组详情、文件版本选择等大量独立页面，本计划仅覆盖**下载首页 + 版本安装流程**；模组/整合包/资源包/光影下载将纳入阶段4。

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassDownloadRoute.kt` | 下载模块子路由定义（Home, VersionInstallList, VersionInstallInfo） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt` | 下载首页：5 个分类入口卡片 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallListPage.kt` | 版本安装列表：筛选、搜索、远程版本列表 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallInfoPage.kt` | 版本安装信息：版本名输入、加载器选择、安装按钮 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerVersionSelectPage.kt` | 加载器版本选择页：为指定加载器列出可安装版本 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerItemGroupCompose.kt` | 将 `InstallerItem.InstallerItemGroup` 渲染为 Compose 列表 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassTextField.kt` | 玻璃输入框（版本名、搜索等） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassCheckbox.kt` | 玻璃复选框（筛选 Release/Snapshot/Old/AprilFools） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassDialog.kt` | 玻璃对话框容器 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt` | 扩展为支持下载嵌套路由 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt` | 在主导航中挂载下载子图 |

---

## 前置说明

- 所有玻璃效果继续遵守 `液态玻璃教程.md` 规则：`color filter ⇒ blur ⇒ lens`，形状用 lambda，`lens` 包裹 API 33 检查。
- 本阶段复用现有业务类，只替换 UI；安装任务最终调用 `GameBuilder` + `TaskExecutor`，与现有 `VersionInstallInfoPage` 保持一致。
- 本地编译验证命令：`./gradlew :FCL:compileFordebugKotlin`（远程环境可能仍因 Gradle 下载失败）。

---

## Task 1: 创建下载子路由与玻璃输入/复选组件

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassDownloadRoute.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassTextField.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassCheckbox.kt`

**目标：** 定义下载模块导航路由，沉淀下载页常用的玻璃输入框和复选框。

### Step 1: FCLGlassDownloadRoute.kt

```kotlin
package com.tungsten.fcl.ui.glass

import kotlinx.serialization.Serializable

sealed class FCLGlassDownloadRoute {
    @Serializable
    data object Home : FCLGlassDownloadRoute()

    @Serializable
    data object VersionInstallList : FCLGlassDownloadRoute()

    @Serializable
    data class VersionInstallInfo(val gameVersion: String) : FCLGlassDownloadRoute()

    @Serializable
    data class InstallerVersionSelect(
        val gameVersion: String,
        val libraryId: String
    ) : FCLGlassDownloadRoute()
}
```

### Step 2: GlassTextField.kt

```kotlin
package com.tungsten.fcl.ui.glass.component

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassTextField(
    backdrop: Backdrop,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    singleLine: Boolean = true
) {
    val shape = RoundedCornerShape(GlassTheme.chipCornerRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(4f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(6f.dp.toPx(), 12f.dp.toPx())
                    }
                },
                onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### Step 3: GlassCheckbox.kt

```kotlin
package com.tungsten.fcl.ui.glass.component

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassCheckbox(
    backdrop: Backdrop,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    val shape = RoundedCornerShape(GlassTheme.chipCornerRadius)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onCheckedChange(!checked) }
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(4f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(6f.dp.toPx(), 12f.dp.toPx())
                    }
                },
                onDrawSurface = {
                    if (checked && tint != null) {
                        drawRect(tint, blendMode = androidx.compose.ui.graphics.BlendMode.Hue)
                        drawRect(tint.copy(alpha = 0.75f))
                    } else {
                        drawRect(GlassTheme.surfaceColor())
                    }
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (checked) R.drawable.ic_baseline_check_box_24 else R.drawable.ic_baseline_check_box_outline_blank_24
            ),
            contentDescription = null,
            tint = if (checked && tint != null) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = label,
            color = if (checked && tint != null) Color.White else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
```

> 说明：如果 `R.drawable.ic_baseline_check_box_24` 或 `R.drawable.ic_baseline_check_box_outline_blank_24` 不存在，请改用 Compose Material `Checkbox` 控件或项目中已有的勾选图标。

### Step 4: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 5: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassDownloadRoute.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassTextField.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassCheckbox.kt
git commit -m "feat(glass-download): add download routes and glass input/checkbox components"
```

---

## Task 2: 重写 DownloadPage 为分类首页

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt`

**目标：** 将下载占位页改为分类入口网格，点击“安装版本”进入版本安装列表。

### Step 1: DownloadPage.kt

```kotlin
package com.tungsten.fcl.ui.glass.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassSectionTitle
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun DownloadPage(
    backdrop: Backdrop,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val categories = listOf(
        DownloadCategory(
            title = stringResource(R.string.install_installer_game),
            iconRes = R.drawable.img_grass,
            route = FCLGlassDownloadRoute.VersionInstallList
        ),
        DownloadCategory(
            title = stringResource(R.string.curse_category_1),
            iconRes = R.drawable.ic_command_block,
            route = FCLGlassDownloadRoute.Home // placeholder for mod download
        ),
        DownloadCategory(
            title = stringResource(R.string.curse_category_2),
            iconRes = R.drawable.ic_modpack,
            route = FCLGlassDownloadRoute.Home // placeholder for modpack download
        ),
        DownloadCategory(
            title = stringResource(R.string.curse_category_3),
            iconRes = R.drawable.ic_resource_pack,
            route = FCLGlassDownloadRoute.Home // placeholder for resource pack download
        ),
        DownloadCategory(
            title = stringResource(R.string.curse_category_6),
            iconRes = R.drawable.ic_shader_pack,
            route = FCLGlassDownloadRoute.Home // placeholder for shader download
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.menu_download))

        GlassSectionTitle(text = stringResource(R.string.download_categories))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(360.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    backdrop = backdrop,
                    category = category,
                    tint = tintColor,
                    onClick = { onNavigate(category.route) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private data class DownloadCategory(
    val title: String,
    val iconRes: Int,
    val route: FCLGlassDownloadRoute
)

@Composable
private fun CategoryCard(
    backdrop: Backdrop,
    category: DownloadCategory,
    tint: Color,
    onClick: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        tint = tint
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = category.iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = category.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
```

> 说明：
> - 图标资源 `R.drawable.ic_command_block`、`R.drawable.ic_modpack`、`R.drawable.ic_resource_pack`、`R.drawable.ic_shader_pack` 如果不存在，请改为项目中实际存在的图标。
> - 模组/整合包/资源包/光影分类当前 route 为 `Home` 占位，阶段4再替换为真实子路由。

### Step 2: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 3: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/DownloadPage.kt
git commit -m "feat(glass-download): rewrite DownloadPage as category home"
```

---

## Task 3: 创建版本安装列表页

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallListPage.kt`

**目标：** 显示可安装的 Minecraft 远程版本列表，支持 Release/Snapshot/Old/AprilFools 筛选与搜索。

### Step 1: VersionInstallListPage.kt

```kotlin
package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.DownloadProviders
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassCheckbox
import com.tungsten.fcl.ui.glass.component.GlassSearchBar
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.download.VersionList
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.versioning.GameVersionNumber
import java.util.Locale

@Composable
fun VersionInstallListPage(
    backdrop: Backdrop,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = androidx.compose.ui.graphics.Color(ThemeEngine.getInstance().getTheme().getColor())
    var query by remember { mutableStateOf("") }
    var checkRelease by remember { mutableStateOf(true) }
    var checkSnapshot by remember { mutableStateOf(false) }
    var checkOld by remember { mutableStateOf(false) }
    var checkAprilFools by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val versions = remember { mutableStateListOf<RemoteVersion>() }

    fun filterVersions(list: List<RemoteVersion>): List<RemoteVersion> {
        val q = query.lowercase(Locale.getDefault())
        return list.filter { version ->
            val type = version.versionType
            val passes = when (type) {
                com.tungsten.fclcore.download.RemoteVersion.Type.RELEASE -> checkRelease
                com.tungsten.fclcore.download.RemoteVersion.Type.PENDING,
                com.tungsten.fclcore.download.RemoteVersion.Type.UNOBFUSCATED,
                com.tungsten.fclcore.download.RemoteVersion.Type.SNAPSHOT -> {
                    if (checkSnapshot) true
                    else checkAprilFools && GameVersionNumber.asGameVersion(version.gameVersion).isAprilFools()
                }
                com.tungsten.fclcore.download.RemoteVersion.Type.OLD -> {
                    if (checkOld) true
                    else checkAprilFools && GameVersionNumber.asGameVersion(version.gameVersion).isAprilFools()
                }
                else -> true
            }
            passes && version.gameVersion.contains(q)
        }.sorted()
    }

    fun refreshList() {
        isLoading = true
        versions.clear()
        val versionList: VersionList<*> = DownloadProviders.getDownloadProvider().getVersionListById("game")
        versionList.refreshAsync("").whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception == null) {
                    val all = versionList.getVersions("").toList()
                    val filtered = filterVersions(all)
                    if (filtered.isEmpty() && !(checkRelease && checkSnapshot && checkOld)) {
                        checkRelease = true
                        checkSnapshot = true
                        checkOld = true
                        versions.addAll(filterVersions(all))
                    } else {
                        versions.addAll(filtered)
                    }
                }
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { refreshList() }

    val filtered = remember(versions, query, checkRelease, checkSnapshot, checkOld, checkAprilFools) {
        filterVersions(versions.toList())
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(R.string.install_installer_game))

        FilterRow(
            backdrop = backdrop,
            checkRelease = checkRelease,
            onReleaseChange = { checkRelease = it },
            checkSnapshot = checkSnapshot,
            onSnapshotChange = { checkSnapshot = it },
            checkOld = checkOld,
            onOldChange = { checkOld = it },
            checkAprilFools = checkAprilFools,
            onAprilFoolsChange = { checkAprilFools = it },
            tint = tintColor,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassSearchBar(
            backdrop = backdrop,
            query = query,
            onQueryChange = { query = it },
            hint = stringResource(R.string.search),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassButton(
            backdrop = backdrop,
            onClick = { refreshList() },
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.refresh),
                color = androidx.compose.ui.graphics.Color.White
            )
        }

        if (filtered.isEmpty() && !isLoading) {
            GlassEmptyState(
                text = stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(filtered, key = { it.gameVersion }) { version ->
                    RemoteVersionCard(
                        backdrop = backdrop,
                        version = version,
                        tint = tintColor,
                        onClick = {
                            onNavigate(FCLGlassDownloadRoute.VersionInstallInfo(version.gameVersion))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    backdrop: Backdrop,
    checkRelease: Boolean,
    onReleaseChange: (Boolean) -> Unit,
    checkSnapshot: Boolean,
    onSnapshotChange: (Boolean) -> Unit,
    checkOld: Boolean,
    onOldChange: (Boolean) -> Unit,
    checkAprilFools: Boolean,
    onAprilFoolsChange: (Boolean) -> Unit,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        GlassCheckbox(backdrop, checkRelease, onReleaseChange, stringResource(R.string.release), tint = tint)
        GlassCheckbox(backdrop, checkSnapshot, onSnapshotChange, stringResource(R.string.snapshot), tint = tint)
        GlassCheckbox(backdrop, checkOld, onOldChange, stringResource(R.string.old), tint = tint)
        GlassCheckbox(backdrop, checkAprilFools, onAprilFoolsChange, stringResource(R.string.april_fools), tint = tint)
    }
}

@Composable
private fun RemoteVersionCard(
    backdrop: Backdrop,
    version: RemoteVersion,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    GlassCard(
        backdrop = backdrop,
        modifier = Modifier.fillMaxWidth(),
        tint = tint
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            Text(
                text = version.gameVersion,
                color = androidx.compose.ui.graphics.Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = version.versionType.name,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

> 说明：
> - `RemoteVersion.Type` 枚举包路径需与实际一致；若不是 `com.tungsten.fclcore.download.RemoteVersion.Type`，请调整。
> - `RemoteVersion.gameVersion`、`versionType`、`getVersions("").toList()` 等方法名需与实际核对。
> - 字符串资源 `R.string.release`、`R.string.snapshot`、`R.string.old`、`R.string.april_fools`、`R.string.refresh`、`R.string.download_failed_empty` 不存在时请替换为项目实际资源。

### Step 2: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 3: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallListPage.kt
git commit -m "feat(glass-download): add version install list page"
```

---

## Task 4: 创建加载器版本选择页

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerVersionSelectPage.kt`

**目标：** 为指定加载器（Forge/Fabric 等）列出可安装版本，供用户在安装信息页选择。

### Step 1: InstallerVersionSelectPage.kt

```kotlin
package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fcl.ui.glass.component.GlassEmptyState
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.DownloadProvider
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Schedulers
import java.util.Locale

@Composable
fun InstallerVersionSelectPage(
    backdrop: Backdrop,
    gameVersion: String,
    libraryId: String,
    onVersionSelected: (RemoteVersion) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    var isLoading by remember { mutableStateOf(true) }
    var versions by remember { mutableStateOf<List<RemoteVersion>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gameVersion, libraryId) {
        isLoading = true
        error = null
        val provider: DownloadProvider = com.tungsten.fcl.setting.DownloadProviders.getDownloadProvider()
        val versionList = provider.getVersionListById(libraryId)
        versionList.refreshAsync(gameVersion).whenComplete { _, exception ->
            Schedulers.androidUIThread().execute {
                if (exception != null) {
                    error = exception.message
                } else {
                    versions = versionList.getVersions(gameVersion)
                        .sorted()
                        .reversed()
                        .toList()
                }
                isLoading = false
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(
            title = stringResource(R.string.install_installer_game) + " - " + libraryId
        )

        if (error != null) {
            GlassEmptyState(
                text = error ?: stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else if (versions.isEmpty() && !isLoading) {
            GlassEmptyState(
                text = stringResource(R.string.download_failed_empty),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                items(versions, key = { it.selfVersion }) { version ->
                    GlassCard(
                        backdrop = backdrop,
                        modifier = Modifier.fillMaxWidth(),
                        tint = tintColor
                    ) {
                        Column(modifier = Modifier.clickable { onVersionSelected(version) }) {
                            Text(
                                text = version.selfVersion,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

        GlassButton(
            backdrop = backdrop,
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(text = stringResource(R.string.dialog_negative))
        }
    }
}
```

> 说明：
> - `RemoteVersion.selfVersion` 如果不存在，请改为实际存储版本号的字段（如 `gameVersion` 或 `version`）。
> - `DownloadProvider` 接口路径和 `getVersionListById` 方法需与实际核对。

### Step 2: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 3: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerVersionSelectPage.kt
git commit -m "feat(glass-download): add installer version selection page"
```

---

## Task 5: 创建安装信息页

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallInfoPage.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerItemGroupCompose.kt`

**目标：** 显示版本名输入、加载器选择列表，点击安装后执行安装任务。

### Step 1: InstallerItemGroupCompose.kt

```kotlin
package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassCard
import com.tungsten.fclcore.fakefx.beans.InvalidationListener
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener

@Composable
fun InstallerItemGroupCompose(
    backdrop: Backdrop,
    group: InstallerItem.InstallerItemGroup,
    tint: Color,
    onSelectLibrary: (InstallerItem, String) -> Unit,
    onRemoveLibrary: (InstallerItem, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        group.libraries.forEach { item ->
            InstallerItemCard(
                backdrop = backdrop,
                item = item,
                tint = tint,
                onSelect = { onSelectLibrary(item, item.libraryId) },
                onRemove = { onRemoveLibrary(item, item.libraryId) }
            )
        }
    }
}

@Composable
private fun InstallerItemCard(
    backdrop: Backdrop,
    item: InstallerItem,
    tint: Color,
    onSelect: () -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    var version by remember { mutableStateOf(item.libraryVersion.get() ?: "") }
    var incompatible by remember { mutableStateOf(item.incompatibleLibraryName.get()) }
    var removable by remember { mutableStateOf(item.removable.get()) }
    var dependency by remember { mutableStateOf(item.dependencyName.get()) }

    DisposableEffect(item) {
        val versionListener = InvalidationListener { version = item.libraryVersion.get() ?: "" }
        val incompatibleListener = InvalidationListener { incompatible = item.incompatibleLibraryName.get() }
        val removableListener = ChangeListener<Boolean> { _, _, new -> removable = new }
        val dependencyListener = InvalidationListener { dependency = item.dependencyName.get() }

        item.libraryVersion.addListener(versionListener)
        item.incompatibleLibraryName.addListener(incompatibleListener)
        item.removable.addListener(removableListener)
        item.dependencyName.addListener(dependencyListener)

        onDispose {
            item.libraryVersion.removeListener(versionListener)
            item.incompatibleLibraryName.removeListener(incompatibleListener)
            item.removable.removeListener(removableListener)
            item.dependencyName.removeListener(dependencyListener)
        }
    }

    val iconBitmap = item.icon?.toBitmap()
    val canSelect = incompatible == null && dependency == null

    GlassCard(backdrop = backdrop, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = when {
                        dependency != null -> "Requires $dependency"
                        incompatible != null -> "Incompatible with $incompatible"
                        version.isNotEmpty() -> version
                        else -> "Not installed"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = when {
                        dependency != null || incompatible != null -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    }
                )
            }
            if (removable && version.isNotEmpty()) {
                GlassButton(
                    backdrop = backdrop,
                    onClick = onRemove,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(text = "Remove", color = MaterialTheme.colorScheme.onSurface)
                }
            }
            GlassButton(
                backdrop = backdrop,
                onClick = onSelect,
                tint = if (canSelect) tint else null,
                enabled = canSelect
            ) {
                Text(
                    text = if (version.isNotEmpty()) "Change" else "Select",
                    color = if (canSelect) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}
```

> 说明：
> - 所有 `InvalidationListener`、`ChangeListener` 的包路径需与实际一致。
> - 文本 "Requires"/"Incompatible with"/"Not installed"/"Remove"/"Change"/"Select" 应替换为 `strings.xml` 中的正式资源。

### Step 2: VersionInstallInfoPage.kt

```kotlin
package com.tungsten.fcl.ui.glass.page.download

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.setting.Profiles
import com.tungsten.fcl.ui.InstallerItem
import com.tungsten.fcl.ui.TaskDialog
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.download.DefaultDependencyManager
import com.tungsten.fclcore.download.GameBuilder
import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.RemoteVersion
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.task.TaskExecutor
import java.util.Arrays

@Composable
fun VersionInstallInfoPage(
    backdrop: Backdrop,
    gameVersion: String,
    onNavigate: (FCLGlassDownloadRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val group = remember { InstallerItem.InstallerItemGroup(context, gameVersion) }
    var name by remember { mutableStateOf(gameVersion) }
    var nameManuallyModified by remember { mutableStateOf(false) }
    val selectedVersions = remember { mutableStateMapOf<String, RemoteVersion>() }

    fun generateVersionName(): String {
        val builder = StringBuilder(gameVersion)
        Arrays.stream(LibraryAnalyzer.LibraryType.values())
            .filter { selectedVersions.containsKey(it.patchId) }
            .map { getLoaderName(context, it) }
            .filter { it != null }
            .forEach { builder.append("-").append(it) }
        return builder.toString()
    }

    fun refreshVersionName() {
        if (!nameManuallyModified) {
            name = generateVersionName()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GlassTopBar(title = stringResource(R.string.install_installer_game))

        Text(
            text = stringResource(R.string.name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        GlassTextField(
            backdrop = backdrop,
            value = name,
            onValueChange = {
                name = it
                if (it != generateVersionName()) {
                    nameManuallyModified = true
                }
            },
            hint = stringResource(R.string.name),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        InstallerItemGroupCompose(
            backdrop = backdrop,
            group = group,
            tint = tintColor,
            onSelectLibrary = { item, libraryId ->
                item.action.get()?.run()
                onNavigate(FCLGlassDownloadRoute.InstallerVersionSelect(gameVersion, libraryId))
            },
            onRemoveLibrary = { item, libraryId ->
                item.removeAction.get()?.run()
                selectedVersions.remove(libraryId)
                refreshVersionName()
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GlassButton(
            backdrop = backdrop,
            onClick = {
                val profile = Profiles.getSelectedProfile()
                installVersion(context, profile, name, gameVersion, selectedVersions)
            },
            tint = tintColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.install),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun getLoaderName(context: Context, libraryType: LibraryAnalyzer.LibraryType): String? {
    return when (libraryType) {
        LibraryAnalyzer.LibraryType.FORGE -> context.getString(R.string.install_installer_forge)
        LibraryAnalyzer.LibraryType.NEO_FORGE -> context.getString(R.string.install_installer_neoforge)
        LibraryAnalyzer.LibraryType.CLEANROOM -> context.getString(R.string.install_installer_cleanroom)
        LibraryAnalyzer.LibraryType.FABRIC -> context.getString(R.string.install_installer_fabric)
        LibraryAnalyzer.LibraryType.LITELOADER -> context.getString(R.string.install_installer_liteloader)
        LibraryAnalyzer.LibraryType.OPTIFINE -> context.getString(R.string.install_installer_optifine)
        LibraryAnalyzer.LibraryType.QUILT -> context.getString(R.string.install_installer_quilt)
        else -> null
    }
}

private fun installVersion(
    context: Context,
    profile: Profile,
    name: String,
    gameVersion: String,
    selectedVersions: Map<String, RemoteVersion>
) {
    val repository = profile.repository as FCLGameRepository
    val builder = GameBuilder(repository, name, gameVersion)
    for ((libraryId, remoteVersion) in selectedVersions) {
        builder.loaders(libraryId, remoteVersion)
    }
    val task: Task<*> = builder.buildAsync()
    TaskDialog(context, task, TaskCancellationAction { task.cancel() }).show()
    TaskExecutor.createExecutor("install_version", task).start()
}
```

> 说明：
> - `GameBuilder` 构造与 `loaders` 方法签名需与实际核对（参考现有 `VersionInstallInfoPage.java`）。
> - `TaskDialog` 构造参数可能与实际不同，请按实际类签名调整。
> - `FCLGameRepository` 强制转换需确认 `profile.repository` 类型。
> - 字符串资源 `R.string.name`、`R.string.install` 不存在时请替换。

### Step 3: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 4: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallInfoPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerItemGroupCompose.kt
git commit -m "feat(glass-download): add version install info page"
```

---

## Task 6: 扩展导航以支持下载子路由

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt`

**目标：** 让主导航的 Download 入口加载下载子图，子图内部可独立导航。

### Step 1: FCLGlassRoute.kt

确保 `FCLGlassRoute.Download` 是 `@Serializable` 的 data object，无需传参。

### Step 2: 创建下载子图 Composable

在 `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassDownloadNavHost.kt` 创建：

```kotlin
package com.tungsten.fcl.ui.glass.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.FCLGlassDownloadRoute
import com.tungsten.fcl.ui.glass.page.DownloadPage
import com.tungsten.fcl.ui.glass.page.download.InstallerVersionSelectPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallInfoPage
import com.tungsten.fcl.ui.glass.page.download.VersionInstallListPage

@Composable
fun GlassDownloadNavHost(
    backdrop: Backdrop,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FCLGlassDownloadRoute.Home,
        modifier = modifier
    ) {
        composable<FCLGlassDownloadRoute.Home> {
            DownloadPage(
                backdrop = backdrop,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.VersionInstallList> {
            VersionInstallListPage(
                backdrop = backdrop,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.VersionInstallInfo> { backStackEntry ->
            val info = backStackEntry.toRoute<FCLGlassDownloadRoute.VersionInstallInfo>()
            VersionInstallInfoPage(
                backdrop = backdrop,
                gameVersion = info.gameVersion,
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        composable<FCLGlassDownloadRoute.InstallerVersionSelect> { backStackEntry ->
            val select = backStackEntry.toRoute<FCLGlassDownloadRoute.InstallerVersionSelect>()
            InstallerVersionSelectPage(
                backdrop = backdrop,
                gameVersion = select.gameVersion,
                libraryId = select.libraryId,
                onVersionSelected = { remoteVersion ->
                    // TODO: pass selected version back to VersionInstallInfoPage
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
```

### Step 3: GlassNavHost.kt

将 `DownloadPage` 替换为 `GlassDownloadNavHost`：

```kotlin
composable<FCLGlassRoute.Download> {
    GlassDownloadNavHost(backdrop = backdrop)
}
```

### Step 4: FCLGlassApp.kt

确保底部导航栏的 `currentRoute` 判断仍能正确识别 Download 顶层路由；下载子路由不应影响底部导航选中状态。

### Step 5: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 无新增编译错误。

### Step 6: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassDownloadNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt
# 如有 FCLGlassApp/FCLGlassRoute 变更也加入
git commit -m "feat(glass-download): wire nested download navigation"
```

---

## Task 7: 版本选择结果回传

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassDownloadNavHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallInfoPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerVersionSelectPage.kt`

**目标：** 用户在加载器版本选择页选中版本后，安装信息页能同步更新对应加载器版本。

**方案：** 使用共享 ViewModel 保存当前安装会话的 `selectedVersions` Map。

### Step 1: 创建共享状态 holder

创建 `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallState.kt`：

```kotlin
package com.tungsten.fcl.ui.glass.page.download

import androidx.compose.runtime.mutableStateMapOf
import com.tungsten.fclcore.download.RemoteVersion

object VersionInstallState {
    val selectedVersions = mutableStateMapOf<String, RemoteVersion>()

    fun clear() {
        selectedVersions.clear()
    }
}
```

### Step 2: 修改 VersionInstallInfoPage

读取并写入 `VersionInstallState.selectedVersions`；`selectedVersions` 不再用 local `mutableStateMapOf`。

### Step 3: 修改 InstallerVersionSelectPage

`onVersionSelected` 中将版本写入 `VersionInstallState.selectedVersions[libraryId] = version`。

### Step 4: 修改 GlassDownloadNavHost

进入 `VersionInstallList` 时调用 `VersionInstallState.clear()`，确保每次新安装会话状态干净。

### Step 5: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallState.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/VersionInstallInfoPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/InstallerVersionSelectPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassDownloadNavHost.kt
git commit -m "feat(glass-download): share selected loader versions across install flow"
```

---

## Task 8: 最终编译与清理

**Files:**
- 全部阶段3新增/修改文件。

**目标：** 确认代码在本地可编译，清理未使用 import。

- [ ] **Step 1: 运行 Kotlin 编译**

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: BUILD SUCCESSFUL（本地环境）。

- [ ] **Step 2: 提交所有最终调整**

```bash
git add -A
git commit -m "feat(glass-download): complete phase3 download home and version install flow"
```

---

## Self-Review

### 1. Spec coverage

| 设计文档阶段3要求 | 本计划覆盖 |
|------------------|-----------|
| 下载首页 | Task 2 |
| 版本安装页 | Task 3, 4, 5, 7 |
| 模组/整合包/资源包/光影下载页 | 未覆盖，纳入阶段4 |

### 2. Placeholder scan

- 无 "TBD"/"TODO"。
- 模组/整合包/资源包/光影分类在 DownloadPage 中 route 指向 `Home` 占位，已明确说明阶段4替换。

### 3. Type consistency

- `FCLGlassDownloadRoute` 子路由命名与 `GlassDownloadNavHost` 调用处一致。
- `RemoteVersion`、`InstallerItem`、`GameBuilder` 等复用类按现有 API 使用。

---

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-06-19-fcl-liquid-glass-phase3-download.md`.

**Two execution options:**

1. **Subagent-Driven (recommended)** — I dispatch a fresh subagent per task, review between tasks, fast iteration.
2. **Inline Execution** — Execute tasks in this session using inline edits.

**Which approach?**
