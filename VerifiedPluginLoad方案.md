
## 1. 目标与边界

`VerifiedPluginLoad`（以下简称 `VPL`）应作为独立 Android library/module，为 FoldCraftLauncher 及其他启动器提供：

- APK 签名证书指纹提取与校验。
- 内置、可热更新的全局作者/组织信任列表。
- 本地用户信任列表：
  - 受信 `author UUID`。
  - 受信签名 `key hash`。
- 对插件 APK 返回统一状态：
  - `TRUSTED`：已受信
  - `PENDING_TRUST`：待受信
  - `UNTRUSTED`：不受信
  - `BANNED`：已封禁
- 返回可供上层 UI 展示的作者、证书和 APK 信息。
- 管理信任列表更新、损坏恢复及持久化。

VPL 只负责“插件来源是否可信”的决策，不直接决定插件如何加载。启动器在真正加载 renderer/driver 的 `.so` 前必须调用 VPL，并根据结果阻止或放行。

---

## 2. 模块划分

建议拆为以下包或 Gradle 子模块：

```text
verified-plugin-load/
  api/                 // 上游启动器使用的公开接口
  model/               // 状态、列表、结果、异常的数据模型
  signature/           // APK 签名证书读取与指纹计算
  truststore/          // 内置/远程全局信任列表
  userstore/           // 用户 author/key 信任列表
  update/              // 下载、签名验证、缓存与原子替换
  verification/        // 信任判定核心逻辑
  storage/             // 文件布局、损坏恢复、并发控制
```

对外仅暴露 `VerifiedPluginLoad`、配置对象和数据模型，不向启动器暴露 JSON 解析、文件名或下载细节。

---

## 3. APK 签名识别

### 3.1 Android API

使用 Android 的 `PackageManager` / `SigningInfo` 获取签名证书，避免解析 APK 内部签名块。

Android 28 及以上：

```kotlin
packageManager.getPackageInfo(
    packageName,
    PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
)
```

低版本使用：

```kotlin
PackageManager.GET_SIGNATURES
```

必须处理：

- 单签名 APK。
- 多签名 APK。
- APK Signing Certificate Rotation。
- 已安装应用。
- 外部 APK 文件路径。
- 包名与实际 APK 文件不一致。
- 无法解析、无签名、读取权限不足等异常。

对于已安装应用，使用 `packageName` 查询；对于用户选择的外部 APK，使用：

```kotlin
packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNING_CERTIFICATES)
```

并设置 `applicationInfo.sourceDir/publicSourceDir`，以便系统正确读取 archive 中的信息。

### 3.2 Hash 格式

需求中要求“APK 签名指纹的 SHA-256 和 SHA-1 拼接字符串为 hash”。建议将其定义为稳定且无歧义的规范格式：

```text
sha256:<UPPERCASE_HEX>|sha1:<UPPERCASE_HEX>
```

例：

```text
sha256:7B1C...A9F4|sha1:90D2...11EC
```

不要使用没有分隔符的直接拼接。否则无法区分字段边界，也会给后续迁移和调试造成问题。

建议将 SHA-256 视为主要安全标识，SHA-1 仅为兼容现有生态或人工识别用途。匹配时应至少完整匹配 SHA-256；不应只凭 SHA-1 认定可信。

对于多签名 APK，应生成多个 `KeyHash` 并逐一进行匹配。判定策略建议明确为：

- 任一当前有效签名命中 `banned`：`BANNED`。
- 任一当前有效签名命中全局受信作者的 `active` key：按 author 信任状态返回 `TRUSTED` 或 `PENDING_TRUST`。
- 任一签名命中用户受信 key：`TRUSTED`。
- 均未命中：`UNTRUSTED`。

如果签名轮换链可获取，建议同时识别历史证书，并将其标记为 `historical`，防止官方签名轮换后旧版插件全部失去识别能力。

---

## 4. 全局受信列表格式

建议文件名：

```text
trusted-authors.json
```

建议 schema 如下：

```json
{
  "format_version": 1,
  "list_version": 2025030801,
  "generated_at": "2025-03-08T00:00:00Z",
  "expires_at": "2025-06-08T00:00:00Z",
  "keys": [
    {
      "uuid": "8ecad48e-5486-4a23-a84c-3a56b63c4015",
      "name": "Example Graphics Project",
      "type": "org",
      "confidence": 2,
      "description": "Maintainer of Example Renderer and related graphics components.",
      "web": "https://example.org",
      "hashes": [
        {
          "value": "sha256:7B1C...A9F4|sha1:90D2...11EC",
          "state": "active",
          "description": "Primary release signing certificate"
        },
        {
          "value": "sha256:ABC1...3321|sha1:1234...88EE",
          "state": "banned",
          "description": "Signing key revoked after suspected private-key compromise",
          "banned_at": "2025-01-12T00:00:00Z"
        }
      ]
    }
  ]
}
```

字段建议：

| 字段 | 用途 |
|---|---|
| `format_version` | JSON schema 版本，不能识别时视为不可用 |
| `list_version` | 单调递增的列表版本，用于拒绝降级更新 |
| `generated_at` | 列表生成时间 |
| `expires_at` | 列表建议失效时间，可用于提示上层 |
| `uuid` | 作者或组织稳定身份标识 |
| `name` | UI 展示名称 |
| `type` | `person` / `org` |
| `confidence` | `0`、`1`、`2` |
| `description` | 作者/组织说明 |
| `web` | 官方主页或项目地址 |
| `hashes` | 该作者名下的证书列表 |
| `state` | `active` / `banned` |

`confidence` 的语义应写死在 VPL 文档中：

| 值 | 语义 | UI 基调 |
|---|---|---|
| `0` | 此作者/组织不受信任 | 明确警告，不应鼓励信任 |
| `1` | 基本可信，但存在较低程度的维护、来源或审计不确定性 | 提醒用户确认来源 |
| `2` | 广泛可信，具有较稳定的维护、发布或社区信誉 | 正常确认提示 |

注意：`confidence` 仅适用于 `active` key。一个 `banned` key 无论所属 author 的置信度为何，均必须阻止加载。

---

## 5. 信任列表更新安全

“联网下载 JSON 后覆盖本地文件”本身不安全。HTTPS 不能完全解决 CDN、DNS、代理、服务器被入侵或错误发布的问题。全局信任列表是整个方案的信任根，必须增加独立的内容签名。

建议远程发布两个文件：

```text
trusted-authors.json
trusted-authors.json.sig
```

或者使用带签名封装的 JSON：

```json
{
  "payload": { "...": "..." },
  "signature": "base64..."
}
```

VPL 内置一个不可热更新的 Ed25519 公钥：

```text
VPL_TRUST_LIST_ROOT_PUBLIC_KEY
```

更新流程：

1. 从所有镜像前缀并行下载候选 JSON 与签名；每对文件必须来自同一个镜像。
2. 验证 Ed25519 签名。
3. 严格解析 JSON。
4. 校验 `format_version == 1`。
5. 校验 UUID、hash 格式、`confidence`、`state` 和字段长度。
6. 校验 `list_version` 不低于当前有效列表版本，防止回滚攻击。
7. 可选校验 `expires_at` 与 `generated_at` 合理性。
8. 写入临时文件。
9. 对临时文件重新读取、解析、验签。
10. 原子替换当前文件。
11. 将本次成功更新时间、ETag、版本写入元数据文件。

绝不能在“下载完成”后立刻覆盖当前有效列表。只有候选文件完整通过验证后，才能原子替换。

---

## 6. 配置文件目录与文件布局

上层启动器提供基础目录：

```kotlin
VerifiedPluginLoadConfig(
    storageDirectory = File(context.filesDir, "verified-plugin-load"),
    trustListUrlPrefixes = listOf(
        "https://mirror-a.example/vpl",
        "https://mirror-b.example/vpl"
    ),
    trustListJsonSuffix = "trusted-authors.json",
    trustListSignatureSuffix = "trusted-authors.json.sig"
)
```

VPL 会并行拉取所有镜像，使用第一个下载完整且通过签名、格式和回滚校验的文件对，并取消其余请求。镜像前缀必须服务同一份已签名内容；任何损坏或不同步的镜像不会阻止其他镜像完成更新。

推荐使用应用私有目录：

```text
<context.filesDir>/verified-plugin-load/
```

不建议使用公共外部存储，因为用户、其他应用或文件管理器可篡改该目录内容。

目录结构：

```text
verified-plugin-load/
  trusted-authors.json
  trusted-authors.json.sig
  trusted-authors.meta.json
  trusted-authors.json.tmp
  user-trust.json
```

首次启动流程：

1. 检查本地 `trusted-authors.json`。
2. 若文件不存在，写入 VPL 内置列表。
3. 读取并校验本地列表。
4. 若网络可用，尝试拉取远程更新。
5. 远程更新通过签名、schema 和版本验证后原子替换。
6. 使用最终有效列表继续启动。

本地列表损坏流程：

1. JSON 解析失败、`format_version` 不匹配、签名无效、字段校验失败，均视为损坏。
2. 若网络可用，立即尝试下载并验证最新列表。
3. 更新成功后使用新列表。
4. 网络不可用或更新失败时，回退到内置列表。
5. 内置列表也应经过同一套 schema 校验。
6. 回退事件应写入日志，供启动器诊断。

建议保留一份最近一次有效的备份：

```text
trusted-authors.previous.json
trusted-authors.previous.json.sig
```

这样可以优先从“当前有效文件 -> 上一有效文件 -> 内置文件”恢复。

---

## 7. 用户信任列表

建议文件名：

```text
user-trust.json
```

格式：

```json
{
  "format_version": 1,
  "trusted_author_uuids": [
    "8ecad48e-5486-4a23-a84c-3a56b63c4015"
  ],
  "trusted_key_hashes": [
    "sha256:7B1C...A9F4|sha1:90D2...11EC"
  ],
  "updated_at": "2025-03-08T00:00:00Z"
}
```

语义应严格区分：

- 信任 `author UUID`：
  - 信任该 author 当前和未来出现在全局列表中的 `active` key。
  - 优点是作者正常换证书后，用户不必再次确认。
  - 风险是用户信任的是“全局列表中该 UUID 的维护者身份”，因此远程列表必须有签名和防回滚保护。

- 信任 `key hash`：
  - 只信任这一张具体签名证书。
  - 适用于不在全局列表内的插件开发者。
  - 作者换证书后需要再次确认。
  - 不应因远程列表后来添加了同 hash 的 author 而改变其原本的用户信任结果。

用户信任列表应只由上层显式用户操作写入，不能因检测到某 APK 而自动添加。

---

## 8. 核心状态判定逻辑

公开入口建议：

```kotlin
fun inspectInstalledPackage(packageName: String): PluginVerificationResult

fun inspectApkFile(apkFile: File): PluginVerificationResult
```

核心伪代码：

```kotlin
for (keyHash in apkSignatureHashes) {
    val globalMatch = globalTrustList.findByHash(keyHash)

    if (globalMatch?.state == BANNED) {
        return BANNED(globalMatch.author, keyHash)
    }
}

for (keyHash in apkSignatureHashes) {
    val globalMatch = globalTrustList.findActiveByHash(keyHash)

    if (globalMatch != null) {
        return if (userTrustStore.containsAuthor(globalMatch.author.uuid)) {
            TRUSTED(globalMatch.author, keyHash, TRUST_SOURCE_AUTHOR)
        } else {
            PENDING_TRUST(globalMatch.author, keyHash)
        }
    }
}

for (keyHash in apkSignatureHashes) {
    if (userTrustStore.containsKeyHash(keyHash)) {
        return TRUSTED(author = null, keyHash, TRUST_SOURCE_KEY)
    }
}

return UNTRUSTED(apkInfo, apkSignatureHashes)
```

结果对象应包含完整上下文，而不是只返回枚举：

```kotlin
data class PluginVerificationResult(
    val status: PluginTrustStatus,
    val packageInfo: PluginPackageInfo,
    val matchedSignature: KeyHash?,
    val allSignatures: List<KeyHash>,
    val author: TrustedAuthorInfo?,
    val keyState: KeyState?,
    val trustSource: TrustSource?,
    val trustListSource: TrustListSource,
    val diagnostic: VerificationDiagnostic?
)
```

其中 `PluginPackageInfo` 至少包含：

```kotlin
data class PluginPackageInfo(
    val packageName: String?,
    val applicationLabel: String?,
    val versionName: String?,
    val versionCode: Long?,
    val apkPath: String?,
    val installerPackageName: String?
)
```

`installerPackageName` 仅作为展示和诊断信息，绝不能作为信任依据。

---

## 9. VPL 对外 API

建议最小公开 API：

```kotlin
interface VerifiedPluginLoad {
    suspend fun initialize(): InitializationResult

    suspend fun refreshTrustList(): TrustListRefreshResult

    fun inspectInstalledPackage(packageName: String): PluginVerificationResult

    fun inspectApkFile(apkFile: File): PluginVerificationResult

    fun trustAuthor(authorUuid: String): TrustActionResult

    fun revokeAuthorTrust(authorUuid: String): TrustActionResult

    fun trustKeyHash(keyHash: String): TrustActionResult

    fun revokeKeyHashTrust(keyHash: String): TrustActionResult

    fun getTrustedAuthors(): List<TrustedAuthorInfo>

    fun getUserTrustSnapshot(): UserTrustSnapshot
}
```

初始化建议由上层在启动器进程启动后尽早调用：

```kotlin
val vpl = VerifiedPluginLoadFactory.create(
    context = applicationContext,
    config = VerifiedPluginLoadConfig(
        storageDirectory = File(filesDir, "verified-plugin-load"),
        trustListUrlPrefixes = TRUST_LIST_URL_PREFIXES,
        trustListJsonSuffix = TRUST_LIST_JSON_SUFFIX,
        trustListSignatureSuffix = TRUST_LIST_SIGNATURE_SUFFIX
    )
)

val initialization = vpl.initialize()
```

`initialize()` 不应因为网络超时无限阻塞启动器。建议：

- 本地有效列表存在时：快速完成初始化，远程更新可在后台执行。
- 本地列表损坏时：给联网更新一个明确的短超时，例如 5 至 10 秒。
- 更新失败后回退内置列表并返回诊断状态。

---

## 10. FoldCraftLauncher 集成

### 10.1 设置项

增加持久化设置：

```text
允许启动不受信任插件
allow_untrusted_plugins
默认值：false
```

必须默认关闭。

这个选项只允许 `UNTRUSTED` 插件进入额外确认流程，不能绕过 `BANNED`。

### 10.2 检查时机

启动游戏前，在最终确定插件 APK、且真正 `System.load()` / `dlopen()` 前执行检查。

检查目标：

- 自定义 renderer：
  - 若 `renderer == custom renderer`，其 APK 视为 renderer plugin。
- Vulkan driver plugin：
  - 若启用外部 Vulkan driver APK，则其 APK 视为 driver plugin。
- 未来其他可执行 native plugin：
  - 统一走同一 VPL 检查管线。

建议建立启动前校验阶段：

```text
Resolve renderer plugin
  -> Verify renderer plugin
  -> Resolve Vulkan driver plugin
  -> Verify Vulkan driver plugin
  -> Build launch configuration
  -> Load plugin native libraries
  -> Launch game
```

任一插件未通过均不得进入 `.so` 加载步骤。

### 10.3 多插件的 UI 处理

建议按插件逐个校验和确认。确认一个后继续下一个，避免一个对话框混合多个来源和多个风险等级。

对于每次确认，弹窗必须绑定当前具体 APK 的：

- 应用名称。
- 包名。
- 版本号。
- 签名 SHA-256。
- 插件类型：Renderer / Vulkan Driver。
- 作者信息或未知来源信息。

用户点击确认后，在真正加载前应再次读取该 APK 的签名，降低“校验后 APK 被替换”的 TOCTOU 风险。

---

## 11. 启动器状态处理与文案

### `TRUSTED`

行为：

```text
直接通过，继续检查下一插件或启动游戏。
```

可记录审计日志：

```text
Renderer plugin trusted by author: <uuid>
Driver plugin trusted by key hash: <hash>
```

不需要打扰用户。

### `PENDING_TRUST`

弹出信息确认窗口，只展示全局信任列表中的作者信息：

```text
标题：
发现已登记的插件开发者

内容：
插件：<应用名>
包名：<package name>
类型：<Renderer / Vulkan Driver>

开发者/组织：<author name>
类型：<person / org>
可信度：<confidence 文本>
说明：<description>
网站：<web>
签名：<SHA-256>
```

按钮：

```text
信任此开发者/组织
取消启动
```

确认后：

```kotlin
vpl.trustAuthor(authorUuid)
```

随后重新执行验证并继续启动。不能仅在内存里假定已经受信。

不同 `confidence` 文案：

| 置信度 | 建议文案 |
|---|---|
| `0` | “该开发者/组织已被登记，但当前不被信任。请仅在你能够独立确认插件来源和安全性的情况下继续。” |
| `1` | “该开发者/组织具有基本可信度。建议确认插件来源、版本和适用设备后再继续。” |
| `2` | “该开发者/组织具有较高可信度，签名与受信列表中的登记信息一致。” |

建议对 `confidence = 0` 的 `PENDING_TRUST` 增加更强的二次确认，或者不提供“信任 author”，仅允许用户退回。否则“全局列表明确不受信任”与“允许一键 author 级信任”在产品语义上冲突。

### `UNTRUSTED` 且设置关闭

显示错误窗口：

```text
标题：
不受信任的插件

内容：
插件：<应用名>
包名：<package name>
版本：<version>
类型：<Renderer / Vulkan Driver>
签名：<SHA-256>

此插件的签名不在受信开发者列表中，且尚未被你信任。
为了保护设备安全，启动器不会加载该插件。
```

按钮：

```text
关闭
```

不提供继续、信任或直接跳转开启设置的入口，以符合需求。

### `UNTRUSTED` 且设置开启

显示警告窗口：

```text
标题：
未知来源插件

内容：
插件：<应用名>
包名：<package name>
版本：<version>
类型：<Renderer / Vulkan Driver>
签名：<SHA-256>

该插件不在受信开发者列表中。加载其原生代码可能导致数据泄露、应用崩溃或设备安全风险。
启动器及其维护者无法对该插件的安全性提供保障。
```

按钮：

```text
取消启动
信任此签名并启动
```

“信任此签名并启动”必须经历 6 秒冷静期：

- 初始禁用。
- 显示剩余秒数，例如 `请阅读风险说明（6）`。
- 计时结束后改为 `信任此签名并启动` 并启用。
- 用户离开弹窗、切换插件、屏幕旋转或 Activity 重建时，应重新计时。
- 点击后调用：

```kotlin
vpl.trustKeyHash(currentSignatureHash)
```

然后重新检验，再启动。

不要在此处信任 author，因为未知插件不具备可验证的 author UUID。

### `BANNED`

显示不可绕过的错误窗口：

```text
标题：
已禁止的插件签名

内容：
插件：<应用名>
包名：<package name>
版本：<version>
类型：<Renderer / Vulkan Driver>

开发者/组织：<author name>
签名：<SHA-256>
原因：<banned key description>
```

按作者置信度补充：

| 置信度 | 建议文案 |
|---|---|
| `0` | “该开发者/组织不受信任，此签名已被禁止。无法继续启动。” |
| `1` / `2` | “该签名证书可能已泄漏、被滥用或已被撤销。请从开发者官方渠道获取最新版本。” |

按钮：

```text
关闭
```

禁止：

- 启动游戏。
- 添加到用户 key 信任。
- 添加到用户 author 信任。
- 使用“允许不受信任插件”绕过。
- 通过已有用户 key 信任绕过。

全局 `banned` 必须始终具有最高优先级。

---

## 12. 原生库加载安全要求

VPL 解决的是来源信任，不会自动让 native plugin 安全。启动器还应做到：

1. 仅在 VPL 校验通过后加载插件 `.so`。
2. 校验所使用的 APK 路径与被检查的 APK 路径一致。
3. 校验和加载之间尽量缩短时间。
4. 加载前再次读取 APK 包信息与签名。
5. 对已安装 APK，优先从 PackageManager 返回的 `ApplicationInfo.sourceDir` 取得路径。
6. 不接受任意字符串 `.so` 路径作为插件来源。
7. 明确限制 ABI：
   - `arm64-v8a`
   - `armeabi-v7a`
   - `x86_64`
8. 检查目标 `.so` 是否位于 APK 的合法 native library 路径或受控解压目录。
9. 避免从共享外部存储目录直接加载 `.so`。
10. 在日志中记录插件包名、版本、SHA-256、决策状态和列表版本，但不要记录用户隐私信息。

特别需要明确：已经受信任的插件仍然是本地 native code，能在启动器进程权限范围内执行。VPL 防止“未知 APK 的 native code 被直接加载”，不等价于 sandbox。

如果安全等级要求更高，应让 renderer/driver plugin 在独立进程运行，并采用 Binder/AIDL 传递有限能力，而不是与启动器主进程共享完整权限。

---

## 13. 错误与诊断模型

建议向上层区分“信任状态”与“校验执行错误”。

例如：

```kotlin
sealed interface VerificationDiagnostic {
    data object None : VerificationDiagnostic
    data object PackageNotFound : VerificationDiagnostic
    data object ApkUnreadable : VerificationDiagnostic
    data object ApkUnsigned : VerificationDiagnostic
    data object SignatureExtractionFailed : VerificationDiagnostic
    data object TrustListFallbackToBuiltin : VerificationDiagnostic
    data object TrustListUpdateFailed : VerificationDiagnostic
    data object TrustListExpired : VerificationDiagnostic
}
```

对于无法取得签名、APK 损坏或解析失败的情况，安全默认应是：

```text
不允许加载
```

这些情况不要伪装成普通 `UNTRUSTED`，否则 UI 会允许用户通过“信任 key hash”绕过无法验证的 APK。

---

## 14. 测试范围

### 单元测试

覆盖：

- SHA-256 / SHA-1 规范化。
- 多签名 APK 判定。
- `banned` 优先于用户 key 信任。
- `banned` 优先于用户 author 信任。
- author 信任命中 active key 时返回 `TRUSTED`。
- active key 命中但 author 未信任时返回 `PENDING_TRUST`。
- 未登记但用户 key 已信任时返回 `TRUSTED`。
- 完全无匹配时返回 `UNTRUSTED`。
- `format_version` 不支持、JSON 截断、字段非法、重复 UUID、重复 hash。
- 远程列表签名错误。
- 远程列表版本降级。
- 网络失败时的当前列表、备份列表、内置列表回退顺序。
- 用户信任文件损坏时的恢复策略。

### Instrumentation 测试

覆盖：

- 读取已安装 APK 签名。
- 从 archive APK 文件读取签名。
- Android 低版本与 Android 28+ 签名 API 分支。
- 签名轮换 APK。
- 外部 APK 文件权限失败。
- 配置目录不可写。
- 并发执行初始化、更新和校验。
- Activity 重建时 6 秒冷静期重新开始。
- 多插件启动链中第一插件确认后继续验证第二插件。

### 集成测试

准备测试 APK：

```text
trusted-author.apk
pending-author.apk
unknown-key.apk
banned-key.apk
multi-signed.apk
unsigned-or-corrupt.apk
```

验证 FoldCraftLauncher 从“选定 renderer/driver”到“最终加载 native library”的完整阻断行为。

---

## 15. 实施阶段

1. 定义 JSON schema、hash 规范、Ed25519 根公钥与内置初始信任列表。
2. 实现 APK 签名读取与 `KeyHash` 标准化。
3. 实现全局信任列表解析、schema 校验、内置回退和原子存储。
4. 实现远程列表下载、签名验签、ETag、版本防回滚与更新元数据。
5. 实现用户 author/key 信任存储。
6. 实现 `PluginVerificationResult` 与四态判定逻辑。
7. 为 VPL 补齐单元测试和 instrumentation 测试。
8. 在 FoldCraftLauncher 增加设置项、启动前插件检查和四类弹窗。
9. 将所有 renderer/driver `.so` 加载入口收敛到受检验的统一路径。
10. 做测试 APK 的端到端回归，并审查所有可绕过加载的旧入口。

---

## 16. 必须固定的安全规则

以下规则建议作为 VPL 的不可配置行为：

- `banned` 永远优先于任何用户信任记录。
- 不可读取或验证 APK 签名时，禁止加载。
- 远程信任列表必须经内置公钥验签后才可使用。
- 更新失败不能删除现有有效列表。
- 本地文件损坏时必须回退到最近有效列表或内置列表。
- 用户仅能显式信任 author 或具体签名，不能自动信任。
- “允许不受信任插件”默认关闭，且永远不能绕过 `banned`。
- 对未知插件只能信任具体 `key hash`，不能凭 APK 声明的作者名称建立 author 信任。
- 每次真正加载 native plugin 前重新验证目标 APK 的签名。
- 外部存储中的可写 `.so` 不应成为直接加载源。
