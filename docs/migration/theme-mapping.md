# 主题 Token → Miuix ColorScheme 映射对照（阶段二 · 小步骤 2.2）

> 生成时间：2026-08-01 ｜ 分支：`feature/miuix-migration`
> 输入：`docs/migration/design-tokens.md`（token 唯一事实来源）、Miuix **0.9.3** 本地依赖缓存实测 + 官方 v0.9.3 源码。
> 产出代码：`FCL/src/main/java/com/tungsten/fcl/ui/theme/FCLColorScheme.kt`、`FCLTheme.kt`；测试入口 `activity/ThemeTestActivity.kt`。

---

## 1. Miuix 0.9.3 主题 API 核实结论（以本地缓存 + v0.9.3 源码为准）

核实方式：`E:/gradle-home/caches/modules-2/files-2.1/top.yukonga.miuix.kmp/` 下 `miuix-ui.aar` / `miuix-preference.aar` 解包 `classes.jar` 做 javap，并对照 GitHub `compose-miuix-ui/miuix` tag `v0.9.3` 源码（theme/Colors.kt、MiuixTheme.kt、ThemeController.kt、basic/Button.kt、basic/Card.kt、preference/SwitchPreference.kt）。

### 1.1 主题体系

- 颜色方案类名为 **`top.yukonga.miuix.kmp.theme.Colors`**（非 `ColorScheme`；访问器仍是 `MiuixTheme.colorScheme`，返回 `Colors`）。
- `MiuixTheme` 有两个重载：
  - `MiuixTheme(controller: ThemeController, textStyles: TextStyles = ..., content)` —— 支持 Monet 动态取色；
  - `MiuixTheme(colors: Colors = MiuixTheme.colorScheme, textStyles: TextStyles = ..., content)` —— 静态配色，**本步骤采用此重载**。
- `Colors` 共 **53 个色槽**，全部 `var` + `mutableStateOf`（`internal set`），MiuixTheme 内部 `copy()` + `updateColorsFrom()` 就地更新，传入新实例可安全触发重组。
- 工厂函数 `lightColorScheme(...)` / `darkColorScheme(...)`：53 个参数全有默认值，可只覆盖关注的槽位。
- `ThemeController(colorSchemeMode, lightColors, darkColors, keyColor, colorSpec, paletteStyle, isDark)` 全默认参数；`ColorSchemeMode` 六值：`System / Light / Dark / MonetSystem / MonetLight / MonetDark`。Monet 由 materialkolor 实现（`ThemeColorSpec.Spec2021/2025`、`ThemePaletteStyle.TonalSpot/...`），可作 ThemeEngine 壁纸取色的远期候选。
- `TextStyles` 14 档：`main / paragraph / body1 / body2 / button / footnote1 / footnote2 / headline1 / headline2 / subtitle / title1 / title2 / title3 / title4`。本步骤沿用默认，FCL 9 档字号（design-tokens §3）的映射属后续步骤。
- **圆角无全局 Shape token**：各组件自带 `cornerRadius` 参数（Button/Card 默认 **16.dp**，squircle 平滑圆角由 `miuix-squircle` 传递依赖提供）。FCL 5dp/8dp 两档体系（design-tokens §4）已经 `FCLCornerRadius` + `FCLCard` 包装对齐（见 FCLControls.kt）。

### 1.2 与 component-mapping.md（按 0.8.8 核实）的重大出入

| 0.8.8（映射文档） | 0.9.3（实际） | 影响 |
|---|---|---|
| `extra/` 包：`SuperSwitch` / `SuperArrow` / `SuperCheckbox` / `SuperRadioButton` / `SuperSpinner` / `SuperDropdown` | **整体移除**，等价物迁入 `miuix-preference` 模块并改名：`SwitchPreference` / `ArrowPreference` / `CheckboxPreference` / `RadioButtonPreference` / `OverlaySpinnerPreference` / `WindowSpinnerPreference` / `OverlayDropdownPreference` / `WindowDropdownPreference`（另新增 `SliderPreference`） | §1.2/§3 自研清单中所有 Super* 引用需改名 |
| `SuperDialog`（`show: MutableState<Boolean>`，Scaffold 内渲染） | 拆为双轨：**`overlay/OverlayDialog`**（组合内渲染，承接 renderInRootScaffold 语义）与 **`window/WindowDialog`**（平台 Dialog window）；`SuperBottomSheet` → `OverlayBottomSheet` / `WindowBottomSheet`；`ListPopup` → `OverlayListPopup` / `WindowListPopup`（另有 Cascading 级联变体） | §1.3 对话框体系与 §3-1 自研 FCLDialog 基座的封装目标改为 OverlayDialog 优先 |
| `Button` 文字直接入参 | 内容槽为 `@Composable RowScope.() -> Unit`，文字需内嵌 `Text`；另有 `TextButton(text, onClick, ...)`；**默认配色是 secondaryVariant 灰系**，主色按钮须显式 `colors = ButtonDefaults.buttonColorsPrimary()` | 迁移 FCLButton 时注意默认色差异 |
| `Card` | 默认色 `surfaceContainer / onSurfaceContainer`，`insideMargin` 默认 `0.dp`，圆角默认 16dp，有可点击重载（`onClick/onLongPress/pressFeedbackType`） | 卡片内边距需自行给 |
| `Switch` | `Switch(checked, onCheckedChange, modifier, colors, enabled)` | 行内开关 |
| `Text` | 增加 `autoSize: TextAutoSize?` 参数（Compose 1.11 新能力） | 无破坏性 |
| 图标 `MiuixIcons.Basic.*`（在 `miuix` 模块） | 0.9.3 基本图标在 **miuix-ui** 的 `icon/basic/`（ArrowRight、ArrowUpDown 等），扩展图标仍在 `miuix-icons` | 一致，模块名随核心构件改名 |
| `LocalContentColor` | 迁至 `top.yukonga.miuix.kmp.theme` 包 | 自定义组件取内容色时注意 import |

> 结论：component-mapping.md 的**组件存在性判断在 0.9.3 下大部分成立**，但 Super* 家族与弹窗体系的命名/归属全部需要按上表修订；「设置页不需要 preference 库」的结论在 0.9.3 下反转——**设置页应直接用 miuix-preference 的 *Preference 组件**（已在依赖中）。

---

## 2. Token → Miuix `Colors` 字段映射总表（53 槽全覆盖）

映射类型：**token** = design-tokens 有明确来源；**派生** = 按文档记录的算法/规则生成；**默认** = 无 token，沿用 Miuix 默认（后续按页面实测微调）。

### 2.1 品牌主色族（运行时三件套 `color` + HSV 派生，design-tokens §2）

| Colors 字段 | Light | Dark | 类型 | 来源/算法 |
|---|---|---|---|---|
| `primary` | `#7797CF` | 同左（不分昼夜） | token | `default_theme_color`（colors.xml:3）= ThemeEngine `color` 默认值 |
| `onPrimary` | `#FFFFFFFF` | 同左 | 派生 | `autoTint(color)`：亮度 0.306 < 0.5 → 白（Theme.java:56 算法复刻） |
| `primaryVariant` | `dkColor(#7797CF)` | 同左 | 派生 | HSV：S+=(1−S)×0.3，V−=(1−V)×0.3（Theme.java:44-47） |
| `onPrimaryVariant` | `autoTint(dkColor)` | 同左 | 派生 | 同上亮度二值算法 |
| `primaryContainer` | `ltColor(#7797CF)` | 同左 | 派生 | HSV：S−=(1−S)×0.3，V+=(1−V)×0.3（Theme.java:40-43） |
| `onPrimaryContainer` | `autoTint(ltColor)` | 同左 | 派生 | 同上 |
| `disabledPrimary` / `disabledOnPrimary` / `disabledPrimaryButton` / `disabledOnPrimaryButton` / `disabledPrimarySlider` | Miuix 默认 | Miuix 默认 | 默认 | 无对应 token；默认为 Miuix 蓝调，后续按品牌色微调 |

### 2.2 背景 / 表面 / 内容色（`color2` / `color2Dark` 运行时二件套 + 静态表面 token）

| Colors 字段 | Light | Dark | 类型 | 来源 |
|---|---|---|---|---|
| `background` | `#FFFFFF` | `#232323` | token | light：白色容器 `bg_container_white`（页面真实底为用户壁纸，design-tokens §6）；dark：`dialog_background` night 值（FCLLibrary values-night/colors.xml:3，**全项目唯一实测 dark 表面色**） |
| `onBackground` | `color2`（默认 `#000000`） | `color2Dark`（默认 `#FFFFFF`） | token | 运行时主题色 2（Theme.java:209-210）；静态资源等价物 `primary_text` #0E0E0E/#FFFFFF |
| `onBackgroundVariant` | Miuix 默认 `#8C93B0` | Miuix 默认 `#787E96` | 默认 | 无 token |
| `surface` | `#F4F4F4` | `#232323` | token | `ui_bg_color`/`dialog_background` 的纯色部分（colors.xml:6、FCLLibrary colors.xml:3）；dark 同 `background` |
| `onSurface` | `color2` | `color2Dark` | token | 同 `onBackground` |
| `surfaceVariant` | Miuix 默认 `#FFFFFF` | Miuix 默认 `#242424` | 默认 | 无 token（dark 默认值与 #232323 近似，视觉差异可接受） |
| `onSurfaceSecondary` | `color2` × 80% | `color2Dark` × 80% | 派生 | 对齐 Miuix 自身的 alpha 档（#CC000000 / #CCFFFFFF） |
| `onSurfaceVariantSummary` | `color2` × 60% | `color2Dark` × 60% | 派生 | 与 `autoHintTint` 二值（#99000000/#99FFFFFF，Theme.java:90）同构 |
| `onSurfaceVariantActions` | `color2` × 40% | `color2Dark` × 40% | 派生 | 对齐 Miuix alpha 档（#66000000 / #66FFFFFF） |
| `disabledOnSurface` | Miuix 默认 | Miuix 默认 | 默认 | 无 token |
| `surfaceContainer` 系（`surfaceContainer` / `onSurfaceContainer` / `onSurfaceContainerVariant` / `surfaceContainerHigh` / `onSurfaceContainerHigh` / `surfaceContainerHighest` / `onSurfaceContainerHighest`） | Miuix 默认 | Miuix 默认 | 默认 | Card 默认底色所在族；无直接 token，dark 默认 #242424/#2D2D2D 与 #232323 同族 |

### 2.3 描边 / 分割线 / 其余

| Colors 字段 | Light | Dark | 类型 | 来源 |
|---|---|---|---|---|
| `outline` | `#AAAAAA` | 同左 | token | `@android:color/darker_gray`（bg_item.xml:13、keycode_view_normal.xml:14 等，无 night 变体，昼夜同色） |
| `dividerLine` | Miuix 默认 `#E0E0E0` | Miuix 默认 `#393939` | 默认 | 无 token（FCL 分隔线即 1dp 描边体系，如需统一可改指 `outline`） |
| `error` / `onError` / `errorContainer` / `onErrorContainer` | Miuix 默认 | Miuix 默认 | 默认 | FCL 无错误色 token |
| `secondary` 族 ×12（`secondary` / `onSecondary` / `secondaryVariant` / `onSecondaryVariant` / `disabled*` / `secondaryContainer*`） | Miuix 默认 | Miuix 默认 | 默认 | Miuix Button 默认配色所在族（灰系）；FCLButton 主按钮语义走 `buttonColorsPrimary()`，灰系默认值可直接用 |
| `tertiaryContainer` 族 ×3 | Miuix 默认 | Miuix 默认 | 默认 | 无 token |
| `windowDimming` | Miuix 默认（黑 30%） | Miuix 默认（黑 60%） | 默认 | 对话框遮罩，无 token |
| `sliderKeyPoint` / `sliderKeyPointForeground` / `sliderBackground` | Miuix 默认 | Miuix 默认 | 默认 | 无 token |

### 2.4 Miuix `Colors` 无槽位的 FCL 扩展 token（挂在 `FCLThemeTokens`，不进 ColorScheme）

| FCL token | Light | Dark（补齐） | 用途 |
|---|---|---|---|
| `UiBackgroundLight/Dark` | `#40F4F4F4` | `#40232323` | `ui_bg_color` 半透明浮层；dark 补齐规则 = 同 alpha × #232323（design-tokens §5 空洞，按唯一 light→dark 表面对派生） |
| `RightMenuLight/Dark` | `#80F4F4F4` | `#80232323` | `right_menu_color` 右侧面板；补齐规则同上 |
| `StrokeGray` | `#AAAAAA` | 同左 | 描边常量（同时映射进 `outline`） |
| 背景图系统（`lt.png` / `dk.png` / 视频背景 / Palette 取色） | — | — | **不进 ColorScheme**：迁移时在页面层以 `Box` 底置 `Image`/`AndroidView(VideoView)` 实现（component-mapping §1.4），FCLTheme 留 TODO |

---

## 3. 代码落点

| 文件 | 内容 |
|---|---|
| `FCL/src/main/java/com/tungsten/fcl/ui/theme/FCLColorScheme.kt` | `FCLThemeTokens` 常量、`deriveLtColor` / `deriveDkColor` / `autoTintOn` 算法复刻、`fclLightColorScheme()` / `fclDarkColorScheme()`（参数化 `primary` / `color2` / `color2Dark`） |
| `FCL/src/main/java/com/tungsten/fcl/ui/theme/FCLTheme.kt` | `FCLThemeMode`（Light/Dark/FollowSystem）+ `FCLTheme()` 入口（封装 `MiuixTheme(colors)`）；预留自定义主色/内容色参数，ThemeEngine 对接留 TODO |
| `FCL/src/main/java/com/tungsten/fcl/activity/ThemeTestActivity.kt` | 临时测试页：Text/Button/Card/Switch/SwitchPreference 展示 + Light/Dark/FollowSystem 切换 + 令牌色板直读 `MiuixTheme.colorScheme` 验证映射 |

## 4. 遗留问题（后续步骤处理）

1. **Typography 未映射**：FCL 9 档字号（8–20sp，design-tokens §3）→ Miuix `TextStyles` 14 档的对照未定，本步骤沿用 Miuix 默认。
2. ~~**圆角体系未对齐**~~（已解决，PR #1714 review）：`FCLCornerRadius` 两档 token（卡片 5dp / 弹窗 8dp，对齐 design-tokens §4.1）+ `FCLCard` 包装统一接入，见 FCLControls.kt。已知边界：Miuix `WindowDialog` 圆角由设备圆角推导（≥32dp，0.9.3 无公开参数），`FCLDialog`（WindowDialog 路径）暂未对齐 8dp。
3. **disabled\*/secondary\*/tertiary\*/slider\* 等默认沿用槽位**：其中 disabledPrimary 系为 Miuix 蓝调（#C2D9FF 等），与品牌色 #7797CF 存在色相偏差，待真实页面出现后按截图微调。
4. **ThemeEngine 对接未做**：`FCLTheme` 的自定义参数当前由调用方传入；读取 SharedPreferences "theme"（theme_color/theme_color2/theme_color2_dark）、监听取色器变更、与 `AppCompatDelegate.setDefaultNightMode` 状态同源，均为 TODO（见 FCLTheme.kt 注释）。另注意 design-tokens §2 记录的已知不一致：`LauncherSettingPage` 重置 color2Dark 写死 #000000 与出厂默认 #FFFFFF 不同，接入时需决策。
5. **miuix-preference 成为设置页标准组件**（0.8.8 时代「不需要 preference 库」的结论已反转），component-mapping.md §1.2/§3 相关条目待批量修订。
6. **androidx.core 1.18.0 已移除 `ColorUtils.colorToHSV` / `HSVToColor`**（本地缓存 javap 核实，仅余 `calculateLuminance` 等）：HSV 派生算法必须改用 framework 的 `android.graphics.Color.colorToHSV/HSVToColor`（与 Theme.java 同源），FCLColorScheme.kt 已按此实现。后续任何移植 Theme.java 派生逻辑的代码都会踩到同一坑。
