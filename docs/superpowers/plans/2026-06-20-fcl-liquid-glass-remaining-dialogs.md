# FCL 液态玻璃旧弹窗/页面迁移计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 FCL 中仍在使用的旧 Android View 弹窗与独立 Activity 迁移为液态玻璃（Backdrop + Jetpack Compose）风格，使核心流程不再跳出旧 UI。

**Architecture：** 复用现有玻璃对话框系统 `GlassDialogManager` / `GlassDialogHost`，新增专用 `DialogRequest` 子类型；独立页面类弹窗改为 Compose Dialog 或全屏页面；独立 Activity 尽量保留 Activity 但将其内容替换为 Compose。

**Tech Stack：** Kotlin, Jetpack Compose, Material3, Backdrop (kyant), FCLCore, FCLLibrary

---

## 文件结构

- **新增/修改的玻璃基础设施**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt` — 注册更多 `DialogRequest` 类型
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt` — 渲染新增弹窗类型
- **账号相关**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/account/CreateAccountDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/account/AccountDialogs.kt` — 离线、微软、外置登录表单
- **版本管理相关**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/RenameVersionDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/DuplicateVersionDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/AddProfileDialog.kt`
- **整合包相关**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/modpack/ModpackSelectionDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/modpack/ModpackUrlDialog.kt`
- **任务/更新**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/task/TaskProgressDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/upgrade/UpdateDialog.kt`
- **世界/Mod**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/world/WorldNameDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/world/WorldExportDialog.kt`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/mod/ModRollbackDialog.kt`
- **独立 Activity 内容替换**
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/JVMActivity.java`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/WebActivity.java`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/ShellActivity.java`
  - `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/ControllerActivity.java`

---

## Task 1: 扩展 GlassDialogManager 与 GlassDialogHost 的弹窗类型

**Files:**
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogManager.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 在 GlassDialogManager 中新增专用 DialogRequest 类型**

在 `sealed class DialogRequest` 的末尾追加以下子类（保留已有类型）：

```kotlin
@Serializable
    data class CreateAccount(
        val factory: AccountFactory<*>? = null,
        val authServer: AuthlibInjectorServer? = null,
        val onCreated: () -> Unit
    ) : DialogRequest()

    data class RenameVersion(
        val oldName: String,
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class DuplicateVersion(
        val oldName: String,
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class AddProfile(
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class ModpackUrl(
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class WorldName(
        val oldName: String = "",
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class WorldExport(
        val worldName: String,
        val onConfirm: (String) -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class ModRollback(
        val modName: String,
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()

    data class TaskProgress(
        val title: String = "",
        val executor: TaskExecutor? = null,
        val onCancel: () -> Unit = {}
    ) : DialogRequest()

    data class AppUpdate(
        val version: String,
        val changelog: String,
        val downloadUrl: String,
        val onDownload: () -> Unit,
        val onDismiss: () -> Unit = {}
    ) : DialogRequest()
```

并新增对应的 `show*` 方法：

```kotlin
fun showCreateAccount(factory: AccountFactory<*>? = null, authServer: AuthlibInjectorServer? = null, onCreated: () -> Unit = {}) {
    add(CreateAccount(factory, authServer, onCreated))
}

fun showRenameVersion(oldName: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(RenameVersion(oldName, onConfirm, onDismiss))
}

fun showDuplicateVersion(oldName: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(DuplicateVersion(oldName, onConfirm, onDismiss))
}

fun showAddProfile(onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(AddProfile(onConfirm, onDismiss))
}

fun showModpackUrl(onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(ModpackUrl(onConfirm, onDismiss))
}

fun showWorldName(oldName: String = "", onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(WorldName(oldName, onConfirm, onDismiss))
}

fun showWorldExport(worldName: String, onConfirm: (String) -> Unit, onDismiss: () -> Unit = {}) {
    add(WorldExport(worldName, onConfirm, onDismiss))
}

fun showModRollback(modName: String, onConfirm: () -> Unit, onDismiss: () -> Unit = {}) {
    add(ModRollback(modName, onConfirm, onDismiss))
}

fun showTaskProgress(title: String = "", executor: TaskExecutor? = null, onCancel: () -> Unit = {}) {
    add(TaskProgress(title, executor, onCancel))
}

fun showAppUpdate(version: String, changelog: String, downloadUrl: String, onDownload: () -> Unit, onDismiss: () -> Unit = {}) {
    add(AppUpdate(version, changelog, downloadUrl, onDownload, onDismiss))
}
```

- [ ] **Step 2: 在 GlassDialogHost 中处理新类型**

在 `GlassDialogHost.kt` 的 `when (request)` 分支中，为每个新增类型渲染占位 Alert：

```kotlin
is DialogRequest.CreateAccount -> AlertDialogSurface(
    request = request,
    title = stringResource(R.string.account_create),
    message = "CreateAccount dialog is under migration.",
    confirmText = "OK",
    onConfirm = { dismiss(request) }
)
// 其余新增类型同理，标题使用对应 string 资源或硬编码
```

> 说明：此处先渲染占位，后续 Task 逐个替换为真实 UI。

- [ ] **Step 3: 编译无关静态检查**

确认 `GlassDialogManager.kt` 导入 `com.tungsten.fclcore.auth.AccountFactory`、`com.tungsten.fclcore.auth.authlibinjector.AuthlibInjectorServer`、`com.tungsten.fclcore.task.TaskExecutor`。

---

## Task 2: 创建通用输入弹窗组件 GlassInputDialog

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassInputDialog.kt`

- [ ] **Step 1: 创建通用输入弹窗**

```kotlin
package com.tungsten.fcl.ui.glass.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R

@Composable
fun GlassInputDialog(
    backdrop: Backdrop,
    title: String,
    initialValue: String = "",
    hint: String = "",
    confirmText: String = stringResource(R.string.dialog_positive),
    dismissText: String = stringResource(R.string.dialog_negative),
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }

    GlassDialogSurface(backdrop = backdrop) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            GlassTextField(
                backdrop = backdrop,
                value = value,
                onValueChange = { value = it },
                hint = hint,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = dismissText, color = MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = { onConfirm(value) }) {
                    Text(text = confirmText, color = Color.White)
                }
            }
        }
    }
}
```

- [ ] **Step 2: 确认 GlassTextField 导包路径**

`GlassTextField` 位于 `com.tungsten.fcl.ui.glass.component.GlassTextField`。

---

## Task 3: 版本重命名弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/RenameVersionDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`
- Modify: 调用旧 `RenameVersionDialog` 的位置

- [ ] **Step 1: 创建玻璃版本重命名弹窗逻辑**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.version

import android.content.Context
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.FutureCallback
import java.util.concurrent.CompletableFuture

fun showRenameVersionDialog(
    context: Context,
    oldName: String,
    callback: FutureCallback<String>
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    GlassDialogManager.showRenameVersion(
        oldName = oldName,
        onConfirm = { newName ->
            callback.call(newName, {
                future.complete(newName)
            }, { msg ->
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            })
        },
        onDismiss = { future.cancel(true) }
    )
    return future
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 RenameVersion 占位**

将 `DialogRequest.RenameVersion` 分支改为：

```kotlin
is DialogRequest.RenameVersion -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.version_rename),
    initialValue = request.oldName,
    hint = stringResource(R.string.version_rename_hint),
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)
```

> 若 `R.string.version_rename_hint` 不存在，使用硬编码 `"New version name"`。

- [ ] **Step 3: 查找并替换旧调用点**

搜索 `new RenameVersionDialog` 的调用处，将其替换为 `showRenameVersionDialog(context, oldName, callback)`。

---

## Task 4: 版本复制弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/DuplicateVersionDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建玻璃版本复制弹窗逻辑**

与 Task 3 结构一致，使用 `DialogRequest.DuplicateVersion`。

```kotlin
fun showDuplicateVersionDialog(
    context: Context,
    oldName: String,
    callback: FutureCallback<String>
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    GlassDialogManager.showDuplicateVersion(
        oldName = oldName,
        onConfirm = { newName ->
            callback.call(newName, { future.complete(newName) }, { msg ->
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            })
        },
        onDismiss = { future.cancel(true) }
    )
    return future
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 DuplicateVersion 占位**

```kotlin
is DialogRequest.DuplicateVersion -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.version_duplicate),
    initialValue = request.oldName,
    hint = stringResource(R.string.version_duplicate_hint),
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)
```

> 若 hint 资源不存在，硬编码 `"Duplicate version name"`。

- [ ] **Step 3: 替换旧调用点**

搜索 `new DuplicateVersionDialog` 并替换为 `showDuplicateVersionDialog(context, oldName, callback)`。

---

## Task 5: 新建 Profile 弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/version/AddProfileDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建玻璃新建 Profile 弹窗逻辑**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.version

import android.content.Context
import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.util.FutureCallback
import java.util.concurrent.CompletableFuture

fun showAddProfileDialog(
    context: Context,
    callback: FutureCallback<String>
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    GlassDialogManager.showAddProfile(
        onConfirm = { name ->
            callback.call(name, { future.complete(name) }, { msg ->
                Schedulers.androidUIThread().execute {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            })
        },
        onDismiss = { future.cancel(true) }
    )
    return future
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 AddProfile 占位**

```kotlin
is DialogRequest.AddProfile -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.profile_add),
    hint = stringResource(R.string.profile_name_hint),
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)
```

> 若资源不存在，硬编码对应文本。

- [ ] **Step 3: 替换旧调用点**

搜索 `new AddProfileDialog` 并替换。

---

## Task 6: 世界重命名/导出弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/world/WorldNameDialog.kt`
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/world/WorldExportDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建世界重命名弹窗入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.world

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.util.FutureCallback
import java.util.concurrent.CompletableFuture

fun showWorldNameDialog(
    oldName: String,
    callback: FutureCallback<String>
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    GlassDialogManager.showWorldName(
        oldName = oldName,
        onConfirm = { name ->
            callback.call(name, { future.complete(name) }, {})
        },
        onDismiss = { future.cancel(true) }
    )
    return future
}
```

- [ ] **Step 2: 创建世界导出弹窗入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.world

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.util.FutureCallback
import java.util.concurrent.CompletableFuture

fun showWorldExportDialog(
    worldName: String,
    callback: FutureCallback<String>
): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    GlassDialogManager.showWorldExport(
        worldName = worldName,
        onConfirm = { fileName ->
            callback.call(fileName, { future.complete(fileName) }, {})
        },
        onDismiss = { future.cancel(true) }
    )
    return future
}
```

- [ ] **Step 3: 在 GlassDialogHost 中替换占位**

```kotlin
is DialogRequest.WorldName -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.world_rename),
    initialValue = request.oldName,
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)

is DialogRequest.WorldExport -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.world_export),
    initialValue = request.worldName,
    hint = stringResource(R.string.world_export_hint),
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)
```

- [ ] **Step 4: 替换旧调用点**

搜索 `new WorldNameDialog` 和 `new WorldExportDialog` 并替换。

---

## Task 7: Mod 回滚弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/mod/ModRollbackDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建 ModRollback 入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.mod

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager

fun showModRollbackDialog(modName: String, onConfirm: () -> Unit) {
    GlassDialogManager.showModRollback(
        modName = modName,
        onConfirm = onConfirm,
        onDismiss = {}
    )
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换占位为 Alert**

```kotlin
is DialogRequest.ModRollback -> AlertDialogSurface(
    request = request,
    title = stringResource(R.string.mod_rollback_title),
    message = stringResource(R.string.mod_rollback_message, request.modName),
    confirmText = stringResource(R.string.dialog_positive),
    dismissText = stringResource(R.string.dialog_negative),
    onConfirm = {
        request.onConfirm()
        dismiss(request)
    },
    onDismiss = {
        request.onDismiss()
        dismiss(request)
    }
)
```

> 若对应 string 资源不存在，使用硬编码。

- [ ] **Step 3: 替换旧调用点**

搜索 `new ModRollbackDialog` 并替换。

---

## Task 8: 整合包 URL 导入弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/modpack/ModpackUrlDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建 ModpackUrl 入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.modpack

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager

fun showModpackUrlDialog(onConfirm: (String) -> Unit) {
    GlassDialogManager.showModpackUrl(
        onConfirm = onConfirm,
        onDismiss = {}
    )
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换占位**

```kotlin
is DialogRequest.ModpackUrl -> GlassInputDialog(
    backdrop = backdrop,
    title = stringResource(R.string.modpack_url),
    hint = "https://",
    onConfirm = { request.onConfirm(it); dismiss(request) },
    onDismiss = { request.onDismiss(); dismiss(request) }
)
```

- [ ] **Step 3: 替换旧调用点**

搜索 `new ModpackUrlDialog` 并替换。

---

## Task 9: 整合包安装选择弹窗（保留旧版跳转或最小化改造）

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/modpack/ModpackSelectionDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建最小占位**

`ModpackSelectionDialog.java` 当前只有空布局。迁移时先用一个玻璃 Alert 告知用户选择逻辑：

```kotlin
package com.tungsten.fcl.ui.glass.dialog.modpack

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager

fun showModpackSelectionDialog(onConfirm: () -> Unit = {}) {
    GlassDialogManager.showAlert(
        title = "Modpack Selection",
        message = "This flow is under glass migration. Continuing will use the existing installer.",
        confirmText = "Continue",
        onConfirm = onConfirm
    ) {}
}
```

- [ ] **Step 2: 替换旧调用点**

搜索 `new ModpackSelectionDialog` 并替换为 `showModpackSelectionDialog { ... }`。

> 后续如果需要完整的选择 Profile/版本 UI，再扩展为专用 DialogRequest。

---

## Task 10: 账号创建弹窗（第一阶段：离线账号）

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/account/CreateAccountDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建离线账号创建弹窗入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.account

import android.widget.Toast
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Accounts
import com.tungsten.fcl.ui.UIManager
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.auth.CharacterSelector
import com.tungsten.fclcore.auth.offline.OfflineAccountFactory
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fclcore.task.Task

fun showCreateOfflineAccountDialog(username: String, onCreated: () -> Unit) {
    if (username.isBlank()) {
        Schedulers.androidUIThread().execute {
            Toast.makeText(UIManager.getInstance().activity, R.string.account_login_empty, Toast.LENGTH_SHORT).show()
        }
        return
    }
    Task.supplyAsync {
        Accounts.FACTORY_OFFLINE.create(
            CharacterSelector { _, _ -> throw NoSelectedCharacterException() },
            username, "", null, null
        )
    }.whenComplete(Schedulers.androidUIThread()) { account ->
        Accounts.addAccount(account)
        Accounts.setSelectedAccount(account)
        onCreated()
    }.fail { e ->
        Schedulers.androidUIThread().execute {
            Toast.makeText(UIManager.getInstance().activity, e.message, Toast.LENGTH_SHORT).show()
        }
    }.start()
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 CreateAccount 占位为离线输入弹窗**

```kotlin
is DialogRequest.CreateAccount -> {
    var name by remember { mutableStateOf("") }
    GlassDialogSurface(backdrop = backdrop) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.account_create_offline),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            GlassTextField(
                backdrop = backdrop,
                value = name,
                onValueChange = { name = it },
                hint = stringResource(R.string.account_username),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { dismiss(request); request.onCreated() }) {
                    Text(text = stringResource(R.string.dialog_negative), color = MaterialTheme.colorScheme.onSurface)
                }
                TextButton(onClick = {
                    showCreateOfflineAccountDialog(name, request.onCreated)
                    dismiss(request)
                }) {
                    Text(text = stringResource(R.string.dialog_positive), color = Color.White)
                }
            }
        }
    }
}
```

- [ ] **Step 3: 替换旧 CreateAccountDialog 调用点**

搜索 `new CreateAccountDialog` 并替换为 `GlassDialogManager.showCreateAccount(factory = ..., onCreated = { ... })`。

> 微软/外置登录在后续 Task 中扩展。

---

## Task 11: 任务进度弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/task/TaskProgressDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建任务进度入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.task

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager
import com.tungsten.fclcore.task.TaskExecutor

fun showTaskProgressDialog(title: String = "", executor: TaskExecutor? = null, onCancel: () -> Unit = {}) {
    GlassDialogManager.showTaskProgress(title, executor, onCancel)
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 TaskProgress 占位**

实现一个显示标题、取消按钮、简单任务列表的弹窗。使用 `request.executor?.tasks` 或监听事件。若实现复杂，可先简化为只显示标题和取消按钮：

```kotlin
is DialogRequest.TaskProgress -> GlassDialogSurface(backdrop = backdrop) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = request.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Running...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        GlassButton(
            backdrop = backdrop,
            onClick = {
                request.executor?.cancel()
                request.onCancel()
                dismiss(request)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.button_cancel), color = Color.White)
        }
    }
}
```

- [ ] **Step 3: 替换旧 TaskDialog 调用点**

搜索 `new TaskDialog` 并替换。

---

## Task 12: 更新弹窗

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/dialog/upgrade/UpdateDialog.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/component/dialog/GlassDialogHost.kt`

- [ ] **Step 1: 创建更新弹窗入口**

```kotlin
package com.tungsten.fcl.ui.glass.dialog.upgrade

import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.dialog.GlassDialogManager

fun showUpdateDialog(version: String, changelog: String, downloadUrl: String, onDownload: () -> Unit) {
    GlassDialogManager.showAppUpdate(version, changelog, downloadUrl, onDownload)
}
```

- [ ] **Step 2: 在 GlassDialogHost 中替换 AppUpdate 占位**

```kotlin
is DialogRequest.AppUpdate -> GlassDialogSurface(backdrop = backdrop) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Update ${request.version}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = request.changelog,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = { dismiss(request); request.onDismiss() }) {
                Text(text = stringResource(R.string.dialog_negative), color = MaterialTheme.colorScheme.onSurface)
            }
            TextButton(onClick = {
                request.onDownload()
                dismiss(request)
            }) {
                Text(text = stringResource(R.string.dialog_positive), color = Color.White)
            }
        }
    }
}
```

- [ ] **Step 3: 替换旧 UpdateDialog 调用点**

搜索 `new UpdateDialog` 并替换。

---

## Task 13: JVMActivity 内容玻璃化

**Files:**
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/JVMActivity.java`

- [ ] **Step 1: 将 JVMActivity 的 setContentView 替换为 Compose**

保留 Activity 入口和 intent 处理逻辑，移除 XML 布局 inflate，改为在 ContentView 中设置 Compose：

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 保留原有 intent 解析和权限/初始化代码
    setContentView(
        new ComposeView(this) {{
            setContent(() -> {
                // 这里调用一个待创建的 @Composable JVMPage()
                return JVMPageKt.JVMPage(this);
            });
        }}
    );
}
```

> 更简单的方式：让 Activity 继承 `ComponentActivity` 并使用 `setContent { ... }`。若改动过大，则只改布局为 ComposeView。

- [ ] **Step 2: 创建 JVMPage.kt**

```kotlin
package com.tungsten.fcl.activity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun JVMPage() {
    Text(text = "JVM Console (glass migration placeholder)")
}
```

- [ ] **Step 3: 复用旧 Activity 逻辑**

将原 `JVMActivity` 中处理命令执行、日志输出的逻辑提取到 ViewModel 或 StateHolder，页面仅展示输出。具体逻辑参考原 Java 文件。

---

## Task 14: WebActivity / ShellActivity / ControllerActivity 内容玻璃化

**Files:**
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/WebActivity.java`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/ShellActivity.java`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/activity/ControllerActivity.java`

- [ ] **Step 1: 为每个 Activity 创建 Compose 包装页面**

```kotlin
package com.tungsten.fcl.activity

import androidx.compose.runtime.Composable

@Composable
fun WebPage(url: String) {
    AndroidView(
        factory = { context -> WebView(context).apply { loadUrl(url) } },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ShellPage() {
    Text(text = "Shell (glass migration placeholder)")
}

@Composable
fun ControllerPage() {
    Text(text = "Controller (glass migration placeholder)")
}
```

- [ ] **Step 2: 替换各 Activity 的 setContentView**

保留 Activity 生命周期和 intent 解析，将 XML 布局替换为 ComposeView 或 `setContent { ... }`。

---

## Task 15: EulaFragment 玻璃化

**Files:**
- Create: `/workspace/FCL/src/main/java/com/tungsten/fcl/ui/glass/page/EulaPage.kt`
- Modify: `/workspace/FCL/src/main/java/com/tungsten/fcl/fragment/EulaFragment.java`

- [ ] **Step 1: 创建 EulaPage**

```kotlin
package com.tungsten.fcl.ui.glass.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.glass.component.GlassButton
import com.tungsten.fcl.ui.glass.component.GlassTopBar

@Composable
fun EulaPage(
    backdrop: Backdrop,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlassTopBar(title = "EULA", onBack = onDecline)
        Text(
            text = stringResource(R.string.eula_content),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        )
        GlassButton(
            backdrop = backdrop,
            onClick = onAccept,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = stringResource(R.string.eula_accept), color = Color.White)
        }
    }
}
```

> 若对应 string 资源不存在，使用硬编码。

- [ ] **Step 2: 在 EulaFragment 中替换视图**

将 `EulaFragment` 的 `onCreateView` 改为返回 `ComposeView`，并渲染 `EulaPage`。

---

## Task 16: 最终静态检查与调用点收尾

**Files:**
- All files above

- [ ] **Step 1: 扫描旧弹窗调用点**

搜索以下类名，确保全部替换为新的玻璃入口：
- `RenameVersionDialog`
- `DuplicateVersionDialog`
- `AddProfileDialog`
- `WorldNameDialog`
- `WorldExportDialog`
- `ModRollbackDialog`
- `ModpackUrlDialog`
- `ModpackSelectionDialog`
- `CreateAccountDialog`
- `TaskDialog`
- `UpdateDialog`

- [ ] **Step 2: 确认导入和字符串资源**

- 每个新增文件导入 `GlassDialogManager` 的路径正确。
- 缺少的字符串资源已用硬编码兜底，记录到本计划末尾。

- [ ] **Step 3: 提交 / 交付**

用户将本地编译，因此不运行远程 Gradle。完成所有 Task 后向用户报告剩余未迁移项（控制器/联机相关旧弹窗若不在本计划范围内）。

---

## 已知未覆盖的剩余项

以下旧 UI 不在本计划范围内，可视需求继续迁移：

- `ClassicAccountLoginDialog.java`
- `OAuthAccountLoginDialog.java`
- `AddAuthlibInjectorServerDialog.java`
- `DownloadAddonDialog.java`
- 控制器编辑相关弹窗：`ControllerUploadDialog`, `OldVersionDialog`, `ControllerInfoDialog`, `ViewGroupDialog`, `SelectControllerDialog`, `DirectionStyleDialog`, `EditViewGroupDialog`, `EditViewDialog`, `AddDirectionStyleDialog`, `ButtonStyleDialog`, `AddButtonStyleDialog`, `AddInputTextDialog`, `MultiplayerDialog`
- `LauncherHelper.java` 中可能弹出的旧 Alert

---

## 执行方式

Plan complete and saved to `docs/superpowers/plans/2026-06-20-fcl-liquid-glass-remaining-dialogs.md`.

Two execution options:

1. **Subagent-Driven (recommended)** — 每个 Task 分派新子智能体，任务间审查，快速迭代。
2. **Inline Execution** — 在当前会话中使用 executing-plans 批量执行，关键节点人工检查。

Which approach do you want?
