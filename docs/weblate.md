# Weblate 本地化接入指南

本项目通过 [hosted.weblate.org](https://hosted.weblate.org) 管理翻译（开源项目免费）。本文件面向仓库维护者，说明接入步骤与维护事项；译者请直接访问 [翻译页面](https://hosted.weblate.org/projects/foldcraftlauncher/)。

## 接入步骤

1. **注册账号**：在 hosted.weblate.org 注册（建议用 GitHub 账号登录）。

2. **安装 Hosted Weblate GitHub App**：
   - 在「Connect GitHub account」流程中安装到 FCL-Team 组织并授予本仓库访问权
   - App 使用 GitHub installation token 完成克隆、推送、创建 PR，无需手动配置 webhook，也无需邀请用户

3. **创建 Project**：
   - Name：`Fold Craft Launcher`
   - **Slug 必须为 `foldcraftlauncher`**（README 徽章 URL 依赖此 slug）

4. **创建 Component**（创建向导中选「From version control」并从仓库自动检测参数，创建后按下表核对）：

| 配置项 | 值 |
|---|---|
| Name / Slug | `fcl` / `fcl` |
| File mask | `FCL/src/main/res/values-*/strings.xml` |
| Monolingual base language file | `FCL/src/main/res/values/strings.xml` |
| File format | Android String Resource |
| Source language | English |
| Language code style | Android style（新语言目录按 `pt-rBR` 风格命名） |
| Version control system | GitHub (via Weblate GitHub app) |
| Push branch | `weblate` |
| Push on commit | 开启（默认） |

   FCLLibrary 模块已并入 FCL，全部字符串统一由这一个组件管理，无需再创建其他组件。

5. **启用推荐 add-ons**（Component → Manage → Add-ons）：
   - **Cleanup translation files**：源文件删除条目时自动清理各语言文件中的失效条目
   - **Add missing languages**：自动为缺失的语言创建翻译目录
   - 可选：Automatic translation（机器翻译预填充）、Pseudolocale generation（伪本地化质量检查）

## 翻译流程

- 译者通过 <https://hosted.weblate.org/projects/foldcraftlauncher/> 在网页上直接翻译，无需安装任何工具
- 每次提交翻译后，Weblate 自动推送到 `weblate` 分支并创建/更新 PR
- PR 由维护者人工审核后合并

## 人工审核与合并

- 翻译 PR 与普通 PR 一样走 review 流程，CI（Build）通过后由维护者手动合并
- 合并时必须使用 **Merge（merge commit）而非 squash**：Weblate 官方明确警告，squash 合并会使 Git 无法识别后续变更，导致 Weblate 之后的提交冲突或失效
- 建议给 `main` 分支开启分支保护（要求 PR review），强制所有 PR 经审核后才能合并

## 徽章与链接

README 中的翻译状态徽章 URL：

```
https://hosted.weblate.org/widgets/foldcraftlauncher/fcl/en/status-badge.svg
```

- `foldcraftlauncher` / `fcl` 为 project / component slug，`en` 为源语言代码
- 项目级徽章可省略组件：`https://hosted.weblate.org/widgets/foldcraftlauncher/en/status-badge.svg`

## 注意事项

- Android 格式内置 Java format 占位符检查（`%s`、`%1$s` 等），无需额外配置
- `translatable="false"` 的字符串在 Weblate 中按只读处理，不参与翻译
- 不要手动编辑语言文件并直接推送到 `main`——会与 Weblate 的推送产生冲突；修改文案请改源文件 `values/strings.xml`，由 Weblate 同步
