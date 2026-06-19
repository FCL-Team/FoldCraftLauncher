# FCL 液态玻璃 UI 迁移 - 阶段 1：通用 Dialog 统一化

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 FCL 中所有旧 `FCLDialog` / `AlertDialog` 替换为统一的液态玻璃风格 Compose Dialog，并通过一个集中式 `GlassDialogHost` 在同一 Compose 树中渲染，确保 Dialog 也能复用 Backdrop 玻璃效果。

**Architecture:** 采用 "Dialog Host" 模式。`GlassDialogManager` 维护一个可观察的 Dialog 状态栈；`FCLGlassApp` 在根层级放置 `GlassDialogHost`，根据状态栈渲染对应的玻璃 Dialog；业务代码从 `new XxxDialog(context).show()` 改为调用 `GlassDialogManager.showXxx(...)`。所有 Dialog UI 组件均使用 `drawBackdrop` 实现玻璃态背景。

**Tech Stack:** Kotlin, Jetpack Compose, Backdrop (`io.github.kyant0:backdrop:2.0.0`), Compose Navigation, FCLCore tasks.

---

## File Structure

| File | Responsibility |
| --- | --- |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt` | 单例状态持有者，管理当前显示的 Dialog 请求队列，提供 `showXxx` / `dismiss` API。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt` | 根层级 Composable，读取 `GlassDialogManager` 状态并渲染当前 Dialog。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAlertDialog.kt` | 通用确认/提示 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassInputDialog.kt` | 通用单行输入 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassSelectionDialog.kt` | 通用单选列表 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassProgressDialog.kt` | 通用后台任务进度 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassCreateAccountDialog.kt` | 创建账户 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassReLoginDialog.kt` | 账户重新登录 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAuthlibServerDialog.kt` | 添加/编辑 AuthlibInjector 服务器 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassRenameVersionDialog.kt` | 重命名版本 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDuplicateVersionDialog.kt` | 复制版本 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAddProfileDialog.kt` | 添加游戏配置文件 Dialog UI。 |
| `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt` | 根组件，注入 `GlassDialogHost`。 |

---

## Task 1: GlassDialogManager - Dialog 状态管理器

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt`

- [ ] **Step 1: Create the manager skeleton**

Create `GlassDialogManager.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tungsten.fclcore.auth.Account
import com.tungsten.fclcore.auth.AccountFactory
import com.tungsten.fcl.setting.Profile

sealed class DialogRequest(
    val id: String = java.util.UUID.randomUUID().toString()
) {
    data class Alert(
        val title: String,
        val message: String,
        val confirmText: String,
        val dismissText: String? = null,
        val onConfirm: () -> Unit = {},
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Input(
        val title: String,
        val initialValue: String = "",
        val hint: String = "",
        val confirmText: String,
        val dismissText: String = "",
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Selection<T>(
        val title: String,
        val items: List<T>,
        val selected: T? = null,
        val itemText: (T) -> String,
        val confirmText: String? = null,
        val dismissText: String = "",
        val onSelect: (T) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class Progress(
        val title: String,
        val message: String = "",
        val progress: Float? = null,
        val onCancel: (() -> Unit)? = null
    ) : DialogRequest()

    data class CreateAccount(
        val factory: AccountFactory<*>? = null,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class ReLogin(
        val account: Account,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class AuthlibServer(
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class RenameVersion(
        val profile: Profile,
        val version: String,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class DuplicateVersion(
        val profile: Profile,
        val version: String,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class AddProfile(
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()
}

object GlassDialogManager {
    private val _requests = mutableStateListOf<DialogRequest>()
    val requests: List<DialogRequest> get() = _requests

    var current by mutableStateOf<DialogRequest?>(null)
        private set

    fun show(request: DialogRequest) {
        _requests.add(request)
        if (current == null) current = request
    }

    fun dismiss(request: DialogRequest? = current) {
        request?.let { _requests.remove(it) }
        current = _requests.firstOrNull()
    }

    // Convenience methods
    fun showAlert(
        title: String,
        message: String,
        confirmText: String,
        dismissText: String? = null,
        onConfirm: () -> Unit = {},
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Alert(title, message, confirmText, dismissText, onConfirm, onDismiss))

    fun showInput(
        title: String,
        initialValue: String = "",
        hint: String = "",
        confirmText: String,
        dismissText: String = "",
        onConfirm: (String) -> Unit,
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Input(title, initialValue, hint, confirmText, dismissText, onConfirm, onDismiss))

    fun <T> showSelection(
        title: String,
        items: List<T>,
        selected: T? = null,
        itemText: (T) -> String = { it.toString() },
        confirmText: String? = null,
        dismissText: String = "",
        onSelect: (T) -> Unit,
        onDismiss: () -> Unit = {}
    ) = show(DialogRequest.Selection(title, items, selected, itemText, confirmText, dismissText, onSelect, onDismiss))

    fun showProgress(
        title: String,
        message: String = "",
        progress: Float? = null,
        onCancel: (() -> Unit)? = null
    ) = show(DialogRequest.Progress(title, message, progress, onCancel))

    fun showCreateAccount(factory: AccountFactory<*>? = null, onDismiss: () -> Unit = {}) =
        show(DialogRequest.CreateAccount(factory, onDismiss))

    fun showReLogin(account: Account, onDismiss: () -> Unit = {}) =
        show(DialogRequest.ReLogin(account, onDismiss))

    fun showAuthlibServer(onDismiss: () -> Unit = {}) =
        show(DialogRequest.AuthlibServer(onDismiss))

    fun showRenameVersion(profile: Profile, version: String, onDismiss: () -> Unit = {}) =
        show(DialogRequest.RenameVersion(profile, version, onDismiss))

    fun showDuplicateVersion(profile: Profile, version: String, onDismiss: () -> Unit = {}) =
        show(DialogRequest.DuplicateVersion(profile, version, onDismiss))

    fun showAddProfile(onDismiss: () -> Unit = {}) =
        show(DialogRequest.AddProfile(onDismiss))
}
```

- [ ] **Step 2: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: FAIL with unresolved symbols from Backdrop/Compose (normal; components not yet created).

- [ ] **Step 3: Commit skeleton**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt
git commit -m "feat(glass): add GlassDialogManager for centralized dialog state"
```

---

## Task 2: GlassDialogHost - 根层级 Dialog 渲染器

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt`

- [ ] **Step 1: Create GlassDialogHost**

Create `GlassDialogHost.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassDialogHost(
    backdrop: Backdrop,
    modifier: Modifier = Modifier
) {
    val request = GlassDialogManager.current ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    when (request) {
                        is DialogRequest.Alert -> {
                            request.onDismiss()
                            GlassDialogManager.dismiss()
                        }
                        is DialogRequest.Input -> {
                            request.onDismiss()
                            GlassDialogManager.dismiss()
                        }
                        is DialogRequest.Selection<*> -> {
                            request.onDismiss()
                            GlassDialogManager.dismiss()
                        }
                        else -> { /* non-dismissible by background tap */ }
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.padding(horizontal = 32.dp)) {
            when (request) {
                is DialogRequest.Alert -> GlassAlertDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.Input -> GlassInputDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.Selection<*> -> GlassSelectionDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.Progress -> GlassProgressDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.CreateAccount -> GlassCreateAccountDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.ReLogin -> GlassReLoginDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.AuthlibServer -> GlassAuthlibServerDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.RenameVersion -> GlassRenameVersionDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.DuplicateVersion -> GlassDuplicateVersionDialog(
                    backdrop = backdrop,
                    request = request
                )
                is DialogRequest.AddProfile -> GlassAddProfileDialog(
                    backdrop = backdrop,
                    request = request
                )
            }
        }
    }
}
```

- [ ] **Step 2: Inject GlassDialogHost into FCLGlassApp**

Modify `FCLGlassApp.kt` to place `GlassDialogHost` at the root, above `GlassNavHost`.

Example change:

```kotlin
// In FCLGlassApp.kt, inside the root Box/Scaffold
Box(modifier = Modifier.fillMaxSize()) {
    // ... background & nav host ...
    GlassDialogHost(backdrop = backdrop)
}
```

- [ ] **Step 3: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: FAIL with missing dialog component references (they are created in next tasks).

- [ ] **Step 4: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt FCL/src/main/java/com/tungsten/fcl/ui/glass/FCLGlassApp.kt
git commit -m "feat(glass): add GlassDialogHost and wire into FCLGlassApp"
```

---

## Task 3: Generic Dialog UI Components

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAlertDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassInputDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassSelectionDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassProgressDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogSurface.kt` (shared backdrop container)

- [ ] **Step 1: Create shared GlassDialogSurface**

Create `GlassDialogSurface.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.tungsten.fcl.ui.glass.theme.GlassTheme

@Composable
fun GlassDialogSurface(
    backdrop: Backdrop,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(GlassTheme.glassCornerRadius)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = {
                    vibrancy()
                    blur(12f.dp.toPx())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        lens(12f.dp.toPx(), 24f.dp.toPx())
                    }
                },
                onDrawSurface = { drawRect(GlassTheme.surfaceColor()) }
            )
            .padding(20.dp)
    ) {
        content()
    }
}
```

- [ ] **Step 2: Create GlassAlertDialog**

Create `GlassAlertDialog.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun GlassAlertDialog(
    backdrop: Backdrop,
    request: DialogRequest.Alert
) {
    val tint = Color(ThemeEngine.getInstance().getTheme().getColor())
    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = request.message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            request.dismissText?.let {
                GlassTextButton(
                    backdrop = backdrop,
                    text = it,
                    onClick = {
                        request.onDismiss()
                        GlassDialogManager.dismiss()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            GlassButton(
                backdrop = backdrop,
                onClick = {
                    request.onConfirm()
                    GlassDialogManager.dismiss()
                },
                tint = tint,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = request.confirmText, color = Color.White)
            }
        }
    }
}
```

- [ ] **Step 3: Create GlassInputDialog**

Create `GlassInputDialog.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcl.ui.glass.component.GlassTextField
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun GlassInputDialog(
    backdrop: Backdrop,
    request: DialogRequest.Input
) {
    val tint = Color(ThemeEngine.getInstance().getTheme().getColor())
    var value by remember { mutableStateOf(request.initialValue) }

    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassTextField(
            backdrop = backdrop,
            value = value,
            onValueChange = { value = it },
            hint = request.hint,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassTextButton(
                backdrop = backdrop,
                text = request.dismissText,
                onClick = {
                    request.onDismiss()
                    GlassDialogManager.dismiss()
                },
                modifier = Modifier.weight(1f)
            )
            GlassButton(
                backdrop = backdrop,
                onClick = {
                    request.onConfirm(value)
                    GlassDialogManager.dismiss()
                },
                tint = tint,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = request.confirmText, color = Color.White)
            }
        }
    }
}
```

- [ ] **Step 4: Create GlassSelectionDialog**

Create `GlassSelectionDialog.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassTextButton

@Composable
@Suppress("UNCHECKED_CAST")
fun <T> GlassSelectionDialog(
    backdrop: Backdrop,
    request: DialogRequest.Selection<T>
) {
    var selected by remember { mutableStateOf(request.selected) }

    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            request.items.forEach { item ->
                RowWithRadio(
                    text = request.itemText(item),
                    selected = selected == item,
                    onClick = { selected = item }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        RowWithSpacing {
            GlassTextButton(
                backdrop = backdrop,
                text = request.dismissText,
                onClick = {
                    request.onDismiss()
                    GlassDialogManager.dismiss()
                },
                modifier = Modifier.weight(1f)
            )
            request.confirmText?.let {
                GlassTextButton(
                    backdrop = backdrop,
                    text = it,
                    onClick = {
                        selected?.let { s -> request.onSelect(s as T) }
                        GlassDialogManager.dismiss()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun RowWithRadio(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
```

- [ ] **Step 5: Create GlassProgressDialog**

Create `GlassProgressDialog.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.glass.component.GlassTextButton
import com.tungsten.fcllibrary.component.theme.ThemeEngine

@Composable
fun GlassProgressDialog(
    backdrop: Backdrop,
    request: DialogRequest.Progress
) {
    GlassDialogSurface(backdrop = backdrop) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (request.message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = request.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (request.progress != null) {
            LinearProgressIndicator(
                progress = { request.progress },
                modifier = Modifier.fillMaxWidth(),
                color = Color(ThemeEngine.getInstance().getTheme().getColor())
            )
        } else {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color(ThemeEngine.getInstance().getTheme().getColor())
            )
        }
        request.onCancel?.let { onCancel ->
            Spacer(modifier = Modifier.height(16.dp))
            GlassTextButton(
                backdrop = backdrop,
                text = "Cancel",
                onClick = {
                    onCancel()
                    GlassDialogManager.dismiss()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
```

- [ ] **Step 6: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: PASS (only generic dialogs; specific account/version dialogs created later).

- [ ] **Step 7: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/
git commit -m "feat(glass): add generic glass dialog components"
```

---

## Task 4: Account Dialogs

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassCreateAccountDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassReLoginDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAuthlibServerDialog.kt`

- [ ] **Step 1: Study old CreateAccountDialog logic**

Read `FCL/src/main/java/com/tungsten/fcl/ui/account/CreateAccountDialog.java` and identify:
- Tab layout (Offline / Microsoft / AuthlibInjector)
- Form fields per tab
- Validation rules
- Account creation call (`Accounts.getAccounts().add(account)`)

- [ ] **Step 2: Implement GlassCreateAccountDialog**

Create `GlassCreateAccountDialog.kt` with tabbed form UI. Keep business logic by calling the same `Accounts` APIs used in the old dialog.

Skeleton:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fclcore.auth.AccountFactory

@Composable
fun GlassCreateAccountDialog(
    backdrop: Backdrop,
    request: DialogRequest.CreateAccount
) {
    var selectedType by remember {
        mutableStateOf(
            when (request.factory) {
                Accounts.FACTORY_OFFLINE -> AccountType.OFFLINE
                Accounts.FACTORY_MICROSOFT -> AccountType.MICROSOFT
                else -> AccountType.OFFLINE
            }
        )
    }

    GlassDialogSurface(backdrop = backdrop) {
        // Tabs + form per type
        // Offline: username input
        // Microsoft: login button
        // AuthlibInjector: server selection + username
        // Confirm/Cancel buttons
    }
}

private enum class AccountType { OFFLINE, MICROSOFT, AUTHLIB_INJECTOR }
```

- [ ] **Step 3: Implement GlassReLoginDialog**

Create `GlassReLoginDialog.kt`:

```kotlin
@Composable
fun GlassReLoginDialog(
    backdrop: Backdrop,
    request: DialogRequest.ReLogin
) {
    GlassDialogSurface(backdrop = backdrop) {
        // Show account info + password/refresh UI
        // Call account.logIn() on confirm
    }
}
```

- [ ] **Step 4: Implement GlassAuthlibServerDialog**

Create `GlassAuthlibServerDialog.kt`:

```kotlin
@Composable
fun GlassAuthlibServerDialog(
    backdrop: Backdrop,
    request: DialogRequest.AuthlibServer
) {
    GlassDialogSurface(backdrop = backdrop) {
        // Server URL input + validation
        // Save to AuthlibInjectorServers
    }
}
```

- [ ] **Step 5: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: PASS.

- [ ] **Step 6: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassCreateAccountDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassReLoginDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAuthlibServerDialog.kt
git commit -m "feat(glass): add account glass dialogs"
```

---

## Task 5: Version Dialogs

**Files:**
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassRenameVersionDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDuplicateVersionDialog.kt`
- Create: `FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAddProfileDialog.kt`

- [ ] **Step 1: Implement GlassRenameVersionDialog**

Create `GlassRenameVersionDialog.kt`:

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.runtime.Composable
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.ui.version.Versions

@Composable
fun GlassRenameVersionDialog(
    backdrop: Backdrop,
    request: DialogRequest.RenameVersion
) {
    GlassInputDialog(
        backdrop = backdrop,
        request = DialogRequest.Input(
            title = "Rename Version", // TODO use R.string.version_manage_rename
            initialValue = request.version,
            hint = "New name",
            confirmText = "OK",
            dismissText = "Cancel",
            onConfirm = { newName ->
                Versions.renameVersion(
                    /* context */ TODO(),
                    request.profile,
                    request.version
                )
            },
            onDismiss = request.onDismiss
        )
    )
}
```

Note: The context must be obtained from `LocalContext.current` inside the composable; wrap the call appropriately.

- [ ] **Step 2: Implement GlassDuplicateVersionDialog**

Create `GlassDuplicateVersionDialog.kt`:

```kotlin
@Composable
fun GlassDuplicateVersionDialog(
    backdrop: Backdrop,
    request: DialogRequest.DuplicateVersion
) {
    GlassDialogSurface(backdrop = backdrop) {
        // Two fields: new version name + copy saves checkbox
        // Call Versions.duplicateVersion on confirm
    }
}
```

- [ ] **Step 3: Implement GlassAddProfileDialog**

Create `GlassAddProfileDialog.kt`:

```kotlin
@Composable
fun GlassAddProfileDialog(
    backdrop: Backdrop,
    request: DialogRequest.AddProfile
) {
    GlassDialogSurface(backdrop = backdrop) {
        // Profile name + game directory
        // Call Profiles API on confirm
    }
}
```

- [ ] **Step 4: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassRenameVersionDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDuplicateVersionDialog.kt \
        FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassAddProfileDialog.kt
git commit -m "feat(glass): add version and profile glass dialogs"
```

---

## Task 6: Replace Old Dialog Calls

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/AccountPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/VersionsPage.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/versions/VersionListStateHolder.kt`
- Modify: other call sites as discovered

- [ ] **Step 1: Replace CreateAccountDialog in AccountPage**

In `AccountPage.kt`, change:

```kotlin
CreateAccountDialog(context, Accounts.FACTORY_OFFLINE).show()
```

to:

```kotlin
GlassDialogManager.showCreateAccount(Accounts.FACTORY_OFFLINE)
```

And similarly for `FACTORY_MICROSOFT`.

- [ ] **Step 2: Replace Rename/Duplicate Version Dialogs**

In `VersionListStateHolder.kt`, change `Versions.renameVersion` and `Versions.duplicateVersion` calls to show the new glass dialogs instead.

Or, if `Versions.renameVersion` itself shows a dialog, create a wrapper:

```kotlin
fun renameVersion(context: Context, item: VersionListItem) {
    GlassDialogManager.showRenameVersion(item.profile, item.version)
}
```

- [ ] **Step 3: Replace Version Settings Toast**

In `VersionsPage.kt`, change the "Not implemented" Toast for settings to navigate to a placeholder or use `GlassDialogManager.showAlert`.

- [ ] **Step 4: Compile check**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add FCL/src/main/java/com/tungsten/fcl/ui/glass/page/
git commit -m "refactor(glass): replace old dialog calls with GlassDialogManager"
```

---

## Task 7: Final Compilation and Cleanup

- [ ] **Step 1: Full compile**

Run: `JAVA_HOME=/root/.local/share/mise/installs/java/17.0.2 gradle --no-daemon :FCL:compileFordebugKotlin`

Expected: PASS.

- [ ] **Step 2: Remove unused old Dialog imports**

Search for `import com.tungsten.fcl.dialog.*` and `import com.tungsten.fcl.ui.account.CreateAccountDialog` in modified glass files. Remove unused ones.

Run compile again to confirm.

- [ ] **Step 3: Commit**

```bash
git commit -m "chore(glass): phase 1 dialog migration compile cleanup"
```

---

## Spec Coverage Check

- Dialog Host 模式：Task 1 + Task 2
- 通用 Dialog UI：Task 3
- 账户 Dialog：Task 4
- 版本/配置 Dialog：Task 5
- 旧调用替换：Task 6
- 编译验证：每个 Task 末尾

## Notes

- Some dialog logic (e.g., Microsoft OAuth flow, AuthlibInjector server validation) is complex. This plan creates the UI shell; exact business logic should mirror the old Java dialogs line-by-line during implementation.
- `GlassProgressDialog` will be used more heavily in later phases (download/install); in phase 1 it is created but mainly wired for existing progress dialogs.
- If `GlassTextField` does not exist yet, create it as part of Task 3 (same package as other glass components).
