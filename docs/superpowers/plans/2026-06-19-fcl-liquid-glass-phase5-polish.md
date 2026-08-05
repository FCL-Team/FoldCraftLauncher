# FCL 液态玻璃 UI 阶段5 — 下载体验完善与问题修复

**Goal:** 修复阶段4遗留问题，提升模组/整合包/资源包/光影下载流程的可用性。

**Scope:**
1. 修复 MODPACK 安装回调中 `FCLUILayout` parent 参数问题
2. 为搜索、版本加载、下载失败添加用户可感知的错误提示
3. 为下载/安装过程添加 `TaskDialog` 进度展示
4. 在文件版本页展示依赖项列表
5. 清理新增 Kotlin 文件中的未使用 import 与 lint 警告

---

## Task 1: 修复 MODPACK 安装回调 parent 参数

**Files:**
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/RemoteContentType.kt`
- Modify: `FCL/src/main/java/com/tungsten/fcl/ui/glass/page/download/RemoteModVersionPage.kt`

**目标：** 让整合包安装能够拿到当前 Activity/Layout 作为 parent，与原 `Versions.downloadModpackImpl` 调用一致。

### Step 1: 确认 `Versions.downloadModpackImpl` 签名

Read `com.tungsten.fcl.ui.version.Versions.java` to confirm signature. Expected:
```java
public static void downloadModpackImpl(Context context, FCLUILayout parent, Profile profile, RemoteMod.Version version)
```

### Step 2: 修改 RemoteContentType.kt

Change `installCallback` signature to accept a nullable `FCLUILayout?` parent:

```kotlin
fun installCallback(context: Context, parent: FCLUILayout?): (Profile, String, RemoteMod.Version) -> Unit {
    return { profile, version, remoteVersion ->
        when (this) {
            MOD -> { /* ... */ }
            MODPACK -> com.tungsten.fcl.ui.version.Versions.downloadModpackImpl(context, parent, profile, remoteVersion)
            // ...
        }
    }
}
```

### Step 3: 修改 RemoteModVersionPage.kt

Pass the parent from current Activity:

```kotlin
val parent = context as? FCLUILayout
installCallback(context, type, parent, profile, selectedVersion, version)
```

Or pass parent through `RemoteContentType.installCallback`.

### Step 4: Commit

```bash
git commit -m "fix(glass-mod): pass FCLUILayout parent to modpack install"
```

---

## Task 2: 添加下载错误提示

**Files:**
- Modify: `RemoteModListPage.kt`
- Modify: `RemoteModInfoPage.kt`
- Modify: `RemoteModVersionPage.kt`

**目标：** 搜索失败、版本加载失败、下载失败时给用户 Toast 提示。

### Implementation

Use `android.widget.Toast.makeText(context, message, Toast.LENGTH_SHORT).show()` in the failure branches.

Add helper:

```kotlin
private fun showToast(context: Context, message: String?) {
    if (!message.isNullOrBlank()) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
```

Replace error state-only handling with toast + empty state.

### Commit

```bash
git commit -m "feat(glass-mod): add error toasts for mod download flow"
```

---

## Task 3: 添加下载进度 TaskDialog

**Files:**
- Modify: `RemoteModVersionPage.kt`

**目标：** 下载和安装整合包/模组时显示进度对话框。

### Implementation

Wrap download task in `TaskDialog` similar to existing code:

```kotlin
val task = Task.supplyAsync { version.download(repository, downloadProvider, selectedVersion) }
val dialog = TaskDialog(context, task, TaskCancellationAction { task.cancel() })
dialog.show()
TaskExecutor.createExecutor("download_mod", task).start()
```

Adjust based on actual `TaskDialog` and `TaskExecutor` APIs.

### Commit

```bash
git commit -m "feat(glass-mod): show TaskDialog during mod download"
```

---

## Task 4: 添加依赖项展示弹窗

**Files:**
- Modify: `RemoteModVersionPage.kt`

**目标：** 在下载前展示版本依赖列表。

### Implementation

When a version is selected, load dependencies via `version.dependencies` (confirm API), then show a simple glass-style alert dialog listing dependencies.

If API not available, skip this task and document it.

### Commit

```bash
git commit -m "feat(glass-mod): show dependency list before download"
```

---

## Task 5: 清理未使用 import 与 lint 警告

**Files:**
- All new Kotlin files in `ui/glass/page/download/` and `ui/glass/component/`

**目标：** 删除未使用的 import，减少 lint 噪音。

Run lint or manually inspect each file. Common unused imports:
- `java.util.Collections`
- `androidx.compose.foundation.layout.fillMaxWidth` (where not used)
- `com.tungsten.fcl.game.FCLGameRepository` (in RemoteContentType if refactored)

### Commit

```bash
git commit -m "chore(glass): clean unused imports"
```

---

## Task 6: 最终验证

Run: `./gradlew :FCL:compileFordebugKotlin`
Expected: 本地环境编译成功。

---

## Execution Options

1. **Subagent-Driven (recommended)** — dispatch one subagent per task
2. **Inline Execution** — execute in this session

**Which approach?**
