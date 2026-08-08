# 2.0 基线快照（pre-miuix-baseline）

## 构建信息

| 项目 | 值 |
| --- | --- |
| 执行时间 | 2026-08-01 19:59 ~ 20:25 (UTC+8) |
| commit | `1b1ead1ffd932d00e3824facbd007661cf568d40` |
| 分支 / tag | `feature/miuix-migration` / `pre-miuix-baseline` |
| 构建命令 | `GRADLE_USER_HOME=E:/gradle-home ./gradlew :FCL:assembleDebug --console=plain` |
| 构建结果 | **BUILD SUCCESSFUL in 25m 49s**（156 actionable tasks，全部执行；首次冷缓存全量构建） |
| 构建栈 | AGP 8.13.2 / Kotlin 2.3.20 / compileSdk 35 / minSdk 26 / targetSdk 34 / Java 17 |

## 产物

| 项目 | 值 |
| --- | --- |
| APK 路径 | `FCL/build/outputs/apk/debug/FCL-debug-1.3.2.1-all.apk` |
| APK 体积 | 310,678,173 字节（约 296.3 MiB，全 ABI + 全 JRE 资产） |
| versionCode / versionName | 1321 / 1.3.2.1 |

## 环境备注

- **GRADLE_USER_HOME 指向 E 盘**：C 盘仅剩约 297M 可用，Gradle 默认缓存（`~/.gradle`）位于 C 盘写不下新增依赖，故本次及后续迁移阶段构建一律使用 `GRADLE_USER_HOME=E:/gradle-home`。首次执行重新下载了 Gradle 8.13 发行版与全部依赖（缓存约 1.3G），属预期，后续增量构建会显著加快。C 盘文件未做任何改动。
- 构建过程中 SDK 管理器自动安装了 CMake 3.22.1（NDK 构建 FCLauncher/FCLLibrary 原生代码所需），位于 `E:\AndroidSDK`。
- 构建仅有告警，无错误：
  - `values-vi/strings.xml` 多处 `Multiple substitutions specified in non-positional format`（既有问题）；
  - FCLauncher JNI C 代码 `-Wformat` 告警 8 条（既有问题）；
  - Java/Kotlin deprecation 与 unchecked 告警（既有问题）。

## 运行指标

- 冷启动时间、内存占用等运行指标需真机环境，**待真机补测**。
