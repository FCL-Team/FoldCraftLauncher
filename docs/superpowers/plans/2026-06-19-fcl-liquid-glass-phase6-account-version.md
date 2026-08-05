# FCL 液态玻璃 UI 阶段6 — 账号页与版本管理页重写

> **For agentic workers:** REQUIRED SUB-SKILL: Use subagent-driven-development workflow (general_purpose_task subagents) to implement this plan task-by-task.

**Goal:** 把底部导航栏的 **Manage** 页拆分为账号页与版本管理页（或保留 Manage 作为二级入口），并将这两个核心页面重写成液态玻璃 Compose 风格。

**Architecture Decision:** 当前底部导航是 Home/Versions/Download/Manage/Settings。阶段6把：
- **Manage** 改为 **Account**（账号页），因为版本管理已有独立的 **Versions** 入口
- **Versions** 页已部分实现，本阶段继续完善其操作菜单（重命名/删除/复制/设置/启动）

如果用户希望 Manage 同时包含账号+版本，可在 Account 页加版本管理快捷入口，但本计划优先把 Account 作为独立一级导航。

---

## 范围说明

- **账号页**：列出所有账户、显示头像/角色名/类型、选择账户、添加离线/微软/外置账户、删除/刷新账户
- **版本管理页完善**：在已有 `VersionsPage` 基础上增加长按/点击菜单：启动、重命名、删除、复制、设置、设为当前

暂不实现在线登录弹窗、皮肤上传、版本设置页详细表单（这些作为阶段7）。

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/AccountPage.kt` | 账号列表页 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/account/AccountListStateHolder.kt` | 账号页状态管理 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassAccountItem.kt` | 账号列表项 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/versions/VersionActions.kt` | 版本操作封装 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassVersionItem.kt` | 版本列表项（如已存在则扩展） |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt` | 把 Manage 路由改为 Account |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassBottomBar.kt` | 更新图标/标签 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt` | 绑定 Account 页 |

---

## Task 1: 探索并确认账号/版本 API

**Files to read:**
- `com.tungsten.fcl.setting.Accounts`
- `com.tungsten.fclcore.auth.Account`
- `com.tungsten.fcl.ui.account.AccountListItem`
- `com.tungsten.fcl.game.TexturesLoader`
- `com.tungsten.fcl.ui.version.VersionListItem`
- `com.tungsten.fcl.ui.version.Versions`
- `com.tungsten.fcl.ui.glass.page.VersionsPage`
- `com.tungsten.fcl.ui.glass.page.versions.VersionListStateHolder`

**目标：** 确认方法签名、状态监听方式、可用图标/头像加载方式。

### Step 1: Account API

Confirm:
- `Accounts.getAccounts()` returns `ObservableList<Account>`
- `Accounts.getSelectedAccount()` / `setSelectedAccount()`
- `Account.getUsername()`, `getCharacter()`, `getUUID()`, `getTextures()`
- `Accounts.getLocalizedLoginTypeName(context, factory)` exists
- `TexturesLoader.avatarBinding(account, size)` or equivalent returns avatar bitmap
- `CreateAccountDialog(context, factory).show()` for adding accounts
- `AccountListItem.logIn(account)` or `account.clearCache()` + `account.logIn()` for refresh

### Step 2: Version API

Confirm:
- `Profiles.getSelectedProfile()` / `Profiles.getSelectedVersion()`
- `profile.repository.getDisplayVersions()`
- `Versions.launch(context, profile, version)`
- `Versions.renameVersion(context, profile, version)`
- `Versions.deleteVersion(context, profile, version)`
- `Versions.duplicateVersion(context, profile, version)`
- `VersionListItem` constructor or factory

### Step 3: Commit

If only reading, no commit. Move to Task 2.

---

## Task 2: 创建账号页状态与列表项

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/account/AccountListStateHolder.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassAccountItem.kt`

### Step 1: AccountListStateHolder.kt

```kotlin
package com.tungsten.fcl.ui.glass.page.account

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.fakefx.collections.ListChangeListener

class AccountListStateHolder {
    val accounts = mutableStateListOf<Account>()
    var selectedAccount by mutableStateOf<Account?>(Accounts.getSelectedAccount())
    private var listener: ListChangeListener<Account>? = null
    private var selectedListener: javafx.beans.value.ChangeListener<Account>? = null

    fun startObserving() {
        accounts.clear()
        accounts.addAll(Accounts.getAccounts())
        selectedAccount = Accounts.getSelectedAccount()

        listener = ListChangeListener { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    accounts.addAll(change.addedSubList)
                }
                if (change.wasRemoved()) {
                    accounts.removeAll(change.removed.toSet())
                }
            }
        }
        selectedListener = javafx.beans.value.ChangeListener { _, _, new ->
            selectedAccount = new
        }

        Accounts.getAccounts().addListener(listener)
        Accounts.selectedAccountProperty().addListener(selectedListener)
    }

    fun stopObserving() {
        listener?.let { Accounts.getAccounts().removeListener(it) }
        selectedListener?.let { Accounts.selectedAccountProperty().removeListener(it) }
    }

    fun select(account: Account) {
        Accounts.setSelectedAccount(account)
    }

    fun remove(account: Account) {
        Accounts.getAccounts().remove(account)
    }
}
```

> 注意：fakefx import 路径可能是 `com.tungsten.fclcore.fakefx.collections.ListChangeListener` 和 `com.tungsten.fclcore.fakefx.beans.value.ChangeListener`。请按实际路径调整。

### Step 2: GlassAccountItem.kt

```kotlin
package com.tungsten.fcl.ui.glass.component

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.game.TexturesLoader
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.glass.theme.GlassTheme
import com.tungsten.fclcore.auth.Account

@Composable
fun GlassAccountItem(
    backdrop: Backdrop,
    account: Account,
    selected: Boolean,
    onSelect: () -> Unit,
    onRefresh: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(GlassTheme.glassCornerRadius)
    var avatar by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    DisposableEffect(account) {
        val binding = TexturesLoader.avatarBinding(account, 64)
        val listener = javafx.beans.value.ChangeListener<Drawable> { _, _, new ->
            avatar = (new as? android.graphics.drawable.BitmapDrawable)?.bitmap
        }
        binding.addListener(listener)
        binding.get()?.let { listener.changed(binding, null, it) }

        onDispose {
            binding.removeListener(listener)
        }
    }

    val typeName = remember(account) {
        Accounts.getLocalizedLoginTypeName(context, Accounts.getAccountFactory(account))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(8f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(12f.dp.toPx(), 24f.dp.toPx())
                    }
                },
                onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
            )
            .padding(16.dp)
    ) {
        RadioButton(selected = selected, onClick = onSelect)

        avatar?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = account.character,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = typeName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        GlassIconButton(backdrop, R.drawable.ic_baseline_refresh_24, onRefresh)
        GlassIconButton(backdrop, R.drawable.ic_baseline_delete_24, onDelete)
    }
}
```

> 说明：
> - `GlassIconButton` 如果不存在，用 `GlassButton` 或普通 Icon + clickable 替代。
> - `TexturesLoader.avatarBinding` 返回类型可能是 `ObjectBinding<Drawable>` 或类似；请按实际调整。
> - `Accounts.getAccountFactory(account)` 需要确认存在。
> - `account.character` 可能是方法 `getCharacter()`，Kotlin 中作为属性访问。

### Step 3: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`

### Step 4: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/account/AccountListStateHolder.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassAccountItem.kt
git commit -m "feat(glass-account): add account state holder and list item"
```

---

## Task 3: 创建账号页

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/AccountPage.kt`

### Step 1: AccountPage.kt

```kotlin
package com.tungsten.fcl.ui.glass.page

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.account.CreateAccountDialog
import com.tungsten.fcl.ui.glass.component.GlassAccountItem
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTopBar
import com.tungsten.fcl.ui.glass.page.account.AccountListStateHolder
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import com.tungsten.fclcore.task.Schedulers

@Composable
fun AccountPage(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tintColor = Color(ThemeEngine.getInstance().getTheme().getColor())
    val stateHolder = remember { AccountListStateHolder() }

    DisposableEffect(Unit) {
        stateHolder.startObserving()
        onDispose { stateHolder.stopObserving() }
    }

    Column(modifier = modifier.fillMaxSize()) {
        GlassTopBar(title = stringResource(R.string.account))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            items(stateHolder.accounts, key = { it.identifier }) { account ->
                GlassAccountItem(
                    backdrop = backdrop,
                    account = account,
                    selected = stateHolder.selectedAccount == account,
                    onSelect = { stateHolder.select(account) },
                    onRefresh = {
                        refreshAccount(context, account)
                    },
                    onDelete = {
                        stateHolder.remove(account)
                    }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            GlassButton(
                backdrop = backdrop,
                onClick = { CreateAccountDialog(context, Accounts.FACTORY_OFFLINE).show() },
                tint = tintColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.account_create_offline), color = Color.White)
            }
            GlassButton(
                backdrop = backdrop,
                onClick = { CreateAccountDialog(context, Accounts.FACTORY_MICROSOFT).show() },
                tint = tintColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.account_create_microsoft), color = Color.White)
            }
        }
    }
}

private fun refreshAccount(context: android.content.Context, account: com.tungsten.fclcore.auth.Account) {
    com.tungsten.fclcore.task.Task.runAsync {
        account.clearCache()
        account.logIn()
    }.whenComplete(Schedulers.androidUIThread()) { _, exception ->
        if (exception != null) {
            // If credential expired, show login dialog
            if (exception is com.tungsten.fclcore.auth.CredentialExpiredException) {
                com.tungsten.fcl.ui.account.AccountListItem.logIn(account)
            } else {
                android.widget.Toast.makeText(context, exception.message, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }.start()
}
```

> 说明：
> - `Accounts.FACTORY_OFFLINE` / `FACTORY_MICROSOFT` 需要确认存在。
> - `AccountListItem.logIn(account)` 如果是实例方法，需要创建实例或找到静态方式。
> - 外置登录按钮可后续添加。
> - `account.identifier` 需要确认存在或用 `account.uuid.toString()` 作为 key。

### Step 2: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`

### Step 3: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/AccountPage.kt
git commit -m "feat(glass-account): add account page"
```

---

## Task 4: 把 Manage 导航改为 Account

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassBottomBar.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt`

### Step 1: FCLGlassRoute.kt

Rename `Manage` route to `Account`:

```kotlin
    @Serializable
    data object Account : FCLGlassRoute()
```

### Step 2: GlassBottomBar.kt

Update the bottom bar items. Find where items are defined and replace Manage with Account:

```kotlin
        GlassBottomBarItem(
            iconRes = R.drawable.ic_baseline_account_circle_24,
            contentDescription = stringResource(R.string.account),
            route = FCLGlassRoute.Account
        )
```

Confirm icon resource exists.

### Step 3: GlassNavHost.kt

Update the composable mapping:

```kotlin
composable<FCLGlassRoute.Account> {
    AccountPage(backdrop = backdrop)
}
```

Remove or keep Manage mapping as needed.

### Step 4: FCLGlassApp.kt

Update the `hasRoute` check for bottom bar selection:

```kotlin
selected = currentRoute?.hasRoute<FCLGlassRoute.Account>() == true,
```

(If it already uses hasRoute, just make sure it references Account.)

### Step 5: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`

### Step 6: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassRoute.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassBottomBar.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassNavHost.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt
git commit -m "feat(glass-nav): replace Manage tab with Account"
```

---

## Task 5: 完善版本管理页操作

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/VersionsPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/versions/VersionListStateHolder.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassVersionItem.kt` (if exists)

**目标：** 给版本列表项添加操作：启动、重命名、删除、复制、设置。

### Step 1: Add actions to state holder

Add helper methods to `VersionListStateHolder`:

```kotlin
    fun launchVersion(context: Context, item: VersionListItem) {
        Versions.launch(context, item.profile, item.version)
    }

    fun renameVersion(context: Context, item: VersionListItem) {
        Versions.renameVersion(context, item.profile, item.version)
    }

    fun deleteVersion(context: Context, item: VersionListItem) {
        Versions.deleteVersion(context, item.profile, item.version)
    }

    fun duplicateVersion(context: Context, item: VersionListItem) {
        Versions.duplicateVersion(context, item.profile, item.version)
    }
```

### Step 2: Add menu/actions to version item

If `GlassVersionItem` exists, add a trailing menu button that shows options:
- 启动
- 重命名
- 复制
- 删除
- 设置

Use a simple `DropdownMenu` or custom glass action sheet.

If the file doesn't exist, create it based on existing `VersionsPage` item layout.

### Step 3: 编译检查

Run: `./gradlew :FCL:compileFordebugKotlin`

### Step 4: Commit

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/VersionsPage.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/page/versions/VersionListStateHolder.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/GlassVersionItem.kt
git commit -m "feat(glass-versions): add version actions menu"
```

---

## Task 6: 最终编译与清理

**Files:** All phase6 files.

- [ ] Run: `./gradlew :FCL:compileFordebugKotlin`
- [ ] Fix any remaining errors
- [ ] Commit final adjustments

---

## Self-Review

### Spec coverage

| 要求 | 覆盖 |
|------|------|
| 账号页 | Task 2-3 |
| 版本管理页完善 | Task 5 |
| 导航切换 Manage->Account | Task 4 |

### Placeholder scan

- 外置登录按钮未实现（阶段7）
- 版本设置页详细表单未实现（阶段7）
- 皮肤上传未实现（阶段7）

---

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-06-19-fcl-liquid-glass-phase6-account-version.md`.

**Two execution options:**

1. **Subagent-Driven (recommended)** — dispatch a fresh subagent per task.
2. **Inline Execution** — execute in this session.

**Which approach?**
