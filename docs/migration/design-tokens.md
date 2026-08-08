# 设计令牌（Design Tokens）——主题与品牌资产提取

> 阶段一·小步骤 1.3 产出。本文档是后续 Miuix（Compose）迁移时映射 `ColorScheme` / `Typography` / `Shape` 的唯一事实来源。
> 所有条目均核实自实际文件，路径列为可直接跳转的来源。
>
> 范围说明：UI 资源集中在 `FCL/src/main/res/`；动态取色引擎 `Theme`/`ThemeEngine` 位于 FCLLibrary 模块（红线模块，本文仅作只读引用，不修改）。

---

## 1. 静态颜色 Token（res/values*）

### 1.1 FCL 模块 `colors.xml`

| Token | Light 值 | Dark 值 | 来源文件 |
|---|---|---|---|
| `default_theme_color`（品牌主色） | `#7797CF` | 同左（无 night 变体） | `FCL/src/main/res/values/colors.xml:3` |
| `black` | `#FF000000` | 同左 | `FCL/src/main/res/values/colors.xml:4` |
| `white` | `#FFFFFFFF` | 同左 | `FCL/src/main/res/values/colors.xml:5` |
| `ui_bg_color`（界面浮层底色，半透明） | `#40F4F4F4` | 同左（无 night 变体） | `FCL/src/main/res/values/colors.xml:6` |
| `right_menu_color` | `#80F4F4F4` | 同左 | `FCL/src/main/res/values/colors.xml:7` |
| `icon_background_color`（自适应图标底色） | `#FFFFFF`；API 31+ 为 `@android:color/system_accent1_0` | 同左 | `FCL/src/main/res/values/colors.xml:8`、`FCL/src/main/res/values-v31/colors.xml:3` |
| `primary_text` | `#0E0E0E` | `#FFFFFF` | `FCL/src/main/res/values/colors.xml:9`、`FCL/src/main/res/values-night/colors.xml:3` |

### 1.2 主题属性映射（themes.xml）

`Theme.FoldCraftLauncher` 继承 `Theme.MaterialComponents.DayNight.NoActionBar`。light / night / v31 三个变体中以下映射完全一致（所有 brand 属性都指向同一个 `default_theme_color`）：

| 主题属性 | 值 | 来源文件 |
|---|---|---|
| `colorPrimary` | `@color/default_theme_color`（`#7797CF`） | `FCL/src/main/res/values/themes.xml:5`、`values-night/themes.xml:6`、`values-v31/themes.xml:7` |
| `colorPrimaryVariant` | 同上 | `values/themes.xml:6` |
| `colorOnPrimary` | 同上（注意：On 色与主色相同，非常规） | `values/themes.xml:7` |
| `colorSecondary` | 同上 | `values/themes.xml:9` |
| `colorSecondaryVariant` | 同上 | `values/themes.xml:10` |
| `colorOnSecondary` | 同上 | `values/themes.xml:11` |
| `android:statusBarColor` | 同上 | `values/themes.xml:13` |
| `android:windowFullscreen` / `windowTranslucentNavigation` | `true`（全屏沉浸式） | `values/themes.xml:17-18` |
| `Theme.Splash` → `windowSplashScreenBackground` | `@color/white` | `values/themes.xml:25` |
| `NavIndicator`（底部导航指示器色） | `#FFF` | `values/themes.xml:34-36` |
| `windowSplashScreenAnimationDuration` | `200` ms | `values/themes.xml:24` |

### 1.3 FCLLibrary 组件库颜色（只读参考）

| Token | Light 值 | Dark 值 | 来源文件 |
|---|---|---|---|
| `dialog_background`（对话框底色） | `#F4F4F4` | `#232323` | `FCLLibrary/src/main/res/values/colors.xml:3`、`FCLLibrary/src/main/res/values-night/colors.xml:3` |

### 1.4 硬编码颜色（未抽成资源，迁移时需人工归并）

| 值 | 用途 | 来源文件 |
|---|---|---|
| `#80AAAAAA` | 透明容器按压态填充 | `FCL/src/main/res/drawable/bg_container_transparent_clickable.xml:6` |
| `@android:color/darker_gray` | 列表项/键位描边与按压态（多处 drawable） | `FCL/src/main/res/drawable/bg_item.xml:13`、`bg_item_clickable.xml`、`keycode_view_normal.xml`、`keycode_view_selected.xml`、`bg_container_transparent_selected.xml:7` |
| `#77FF00`（亮绿） | 主界面 `textColorLink` 链接色 | `FCL/src/main/res/layout/ui_main.xml:55` |
| `#000000` / `#FFFFFF` | 主题引擎 autoTint 二值（见 §2） | `FCLLibrary/.../theme/Theme.java:56` |
| `#99000000` / `#99FFFFFF` | 主题引擎 autoHintTint 二值（见 §2） | `FCLLibrary/.../theme/Theme.java:90` |

---

## 2. 动态主题引擎 Token（ThemeEngine / Theme）

引擎代码：`FCLLibrary/src/main/java/com/tungsten/fcllibrary/component/theme/Theme.java`、`ThemeEngine.java`（只读引用）。
持久化：SharedPreferences `"theme"`（`Theme.java:205-233`）；启动时在 `FCLActivity`/`FCLService` 基类初始化（`FCLLibrary/src/main/java/com/tungsten/fcllibrary/component/FCLActivity.java:42`）。

| Token | 默认值（Light） | 默认值（Dark） | SharedPreferences Key | 说明 | 来源 |
|---|---|---|---|---|---|
| `color`（主题色 1，主强调色） | `#7797CF` | 同左（不分昼夜） | `theme_color` | 用户可用取色器自定义；重置回 `R.color.default_theme_color` | `Theme.java:208`；`LauncherSettingPage.java:440` |
| `color2`（主题色 2，浅色模式用） | `#000000` | 不直接用 | `theme_color2` | 文字/图标等着色；重置回 `#000000` | `Theme.java:209`；`LauncherSettingPage.java:443` |
| `color2Dark`（主题色 2，深色模式用） | 不直接用 | `#FFFFFF` | `theme_color2_dark` | `getColor2()` 按当前 uiMode 二选一 | `Theme.java:210,66-68` |
| `ltColor`（派生浅色调） | 由 `color` 派生 | 同左 | 不持久化 | HSV：S −= (1−S)×0.3，V += (1−V)×0.3 | `Theme.java:40-43,163-166` |
| `dkColor`（派生深色调） | 由 `color` 派生 | 同左 | 不持久化 | HSV：S += (1−S)×0.3，V −= (1−V)×0.3 | `Theme.java:44-47,167-170` |
| `autoTint`（主色上的前景色） | 亮度 ≥ 0.5 → `#FF000000`，否则 `#FFFFFFFF` | 同算法 | 不持久化 | 基于 `ColorUtils.calculateLuminance(color)` | `Theme.java:56,173` |
| `autoHintTint`（提示文字色） | 亮度 ≥ 0.5 → `#99000000`，否则 `#99FFFFFF` | 同算法 | 不持久化 | 同上 | `Theme.java:89-91` |
| 壁纸取色 `getWallpaperColor` | 回退 `#7797CF` | 同左 | 不持久化 | API 27+ 读系统壁纸主色 | `ThemeEngine.java:177-186` |

派生色调用点示例：`MainActivity.kt:175-177`（左菜单背景着 `color`）、`MainActivity.kt:590-595`（版本信息文字着 `color2`）；全工程共 20+ 处 `ThemeEngine.getInstance().getTheme().getColor*()` 调用（ui/、control/ 包内各页面与适配器）。

**已知不一致（迁移时需决策）**：`LauncherSettingPage.java:446` 的「重置主题色 2（深色）」写死 `#000000`，而 `Theme.java:210` 的初始默认是 `#FFFFFF`——重置后与出厂默认不同，疑似 bug。

---

## 3. 字号层级（Typography）

**项目没有 `dimens.xml`**，所有字号以 `android:textSize` 内联在 `FCL/src/main/res/layout/*.xml`。按出现频次统计（grep 全量核实）：

| 字号 | 出现次数 | 典型用途 | 来源示例 |
|---|---|---|---|
| `8sp` | 109 | 辅助标签、徽标、次要说明 | `FCL/src/main/res/layout/*.xml`（全量统计） |
| `12sp` | 93 | 正文/列表次要行 | 同上 |
| `14sp` | 56 | 正文、Tab 文字 | `values/themes.xml:29`（`TabTextAppearance` 明确 14sp） |
| `11sp` | 40 | 次要说明 | 同上 |
| `16sp` | 18 | 标题/强调 | 同上 |
| `13sp` | 10 | 过渡档 | 同上 |
| `18sp` | 3 | 大标题 | 同上 |
| `20sp` | 1 | 最大标题 | 同上 |
| `10sp` | 1 | 极小标签 | 同上 |

代码内动态字号：手柄/控制器按钮样式字号通过 `SharedPreferences` 存储，默认 `12sp`（`FCL/src/main/java/com/tungsten/fcl/control/` 下 ButtonStyle 数据类，`textSize`/`textSizePressed` 属性，用户可在编辑界面用 SeekBar 调整并实时显示 `"N sp"`）。

---

## 4. 圆角 / 描边 / 阴影（Shape & Elevation）

### 4.1 圆角

| 圆角值 | 用途 | 来源文件 |
|---|---|---|
| `5dp`（写作 `5.0dip`/`5dp`） | 列表项、白色容器、键位视图、组件库按钮 | `FCL/src/main/res/drawable/bg_item.xml:5-8`、`bg_container_white.xml:4-7`、`bg_container_white_clickable.xml`、`bg_item_clickable.xml`、`keycode_view_normal.xml`、`keycode_view_selected.xml`；`FCLLibrary/src/main/res/drawable/fcl_button.xml:6-9` |
| `8dp` | 游戏菜单、右侧面板、透明容器、对话框、组件库 FCLButton | `FCL/src/main/res/drawable/bg_game_menu.xml:5`、`bg_right_menu.xml:4-5`（仅左上+左下两角）、`bg_container_transparent_clickable.xml`、`bg_container_transparent_selected.xml`、`bg_right_menu_button.xml`；`FCLLibrary/src/main/res/drawable/dialog_background.xml:5`；`FCLLibrary/src/main/java/com/tungsten/fcllibrary/component/view/FCLButton.java:90,93`（代码 `setCornerRadius(dip2px(8))`） |
| 对话框 inset | 对话框背景向内缩进 `10dp` | `FCLLibrary/src/main/res/drawable/dialog_background.xml:3` |

### 4.2 描边

| 宽度 | 颜色 | 来源文件 |
|---|---|---|
| `1.5dp` | `darker_gray`，列表项 | `FCL/src/main/res/drawable/bg_item.xml:12-13`、`bg_item_clickable.xml` |
| `1dp` | `darker_gray`，键位视图/右侧菜单按钮 | `FCL/src/main/res/drawable/keycode_view_normal.xml:14`、`bg_right_menu_button.xml` |

### 4.3 阴影 / Elevation

| 值 | 用途 | 来源文件 |
|---|---|---|
| `100dp` | 主界面左、右菜单浮层（极端高值，仅为压过背景层） | `FCL/src/main/res/layout/activity_main.xml:23,110` |
| `50dp` | 头像 | `FCL/src/main/res/layout/activity_main.xml:132` |

无其它 `elevation`/`cardElevation` 使用；**全项目没有系统化的阴影 token**（Material 默认阴影之外仅上述 3 处）。

### 4.4 间距（Spacing）

无 dimens 资源，布局内联。按 `FCL/src/main/res/layout/` 全量统计，主导间距刻度为：

| 间距 | 出现次数 | 定位 |
|---|---|---|
| `10dp` | 785 | 基准网格（外边距/内边距主力值） |
| `8dp` | 207 | 次基准 |
| `5dp` | 150 | 紧凑间距（半格） |
| `1dp` | 101 | 分隔线/描边 |
| `12dp` | 67 | — |
| `20dp` / `15dp` | 41 / 41 | 大间距 |
| `30dp` | 39 | 区块分隔 |
| `48dp` / `50dp` | 29 / 28 | 触控目标/头像尺寸 |

迁移建议（供后续 Agent 参考）：可归纳为 4 的倍数网格 `4/8/12/16/20`，外加遗留 `5/10/15/30` 半格体系，映射 Miuix 时需逐页确认。

---

## 5. 暗黑模式实现方案

机制为**三条通道并存**：

1. **AppCompat DayNight 主题**：`Theme.FoldCraftLauncher` parent 为 `Theme.MaterialComponents.DayNight.NoActionBar`（`FCL/src/main/res/values/themes.xml:3`），`values-night/` 提供覆盖资源。
2. **模式切换**：`LauncherSettingPage.java:132-139`（spinner：跟随系统/浅色/深色）→ `:540-544` 调 `AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM / MODE_NIGHT_NO / MODE_NIGHT_YES)`。spinner 选中项存 SharedPreferences `"setting"` 的 `themeMode`（仅恢复 UI 状态；夜间模式本身由 AppCompat 自行持久化，启动时无需手动 apply——全工程除该页面外无 `setDefaultNightMode` 调用，已 grep 核实）。
3. **运行时手动判断**：`ThemeEngine.isNightMode()`（`ThemeEngine.java:67-69`）与 `Theme.getColor2()`/`getBackground(Context)`（`Theme.java:66-68,157-160`）直接读 `uiMode`，用于动态着色与背景图切换，**不依赖资源系统的 night 限定符**。

拥有 dark 变体的资源（全量，仅 2 项）：

| Token | Light | Dark | 来源 |
|---|---|---|---|
| `primary_text` | `#0E0E0E` | `#FFFFFF` | `FCL/src/main/res/values{,-night}/colors.xml` |
| `dialog_background`（FCLLibrary） | `#F4F4F4` | `#232323` | `FCLLibrary/src/main/res/values{,-night}/colors.xml` |

注意：`ui_bg_color`、`right_menu_color`、`default_theme_color` 等均**无 dark 变体**——暗色模式下浮层仍是半透明白 `#40F4F4F4`，靠用户自设深色背景图兜底。这是迁移到 Miuix `ColorScheme` 时必须补全的空洞。

---

## 6. 用户自定义背景 / 配色能力

| 能力 | 实现 | 来源 |
|---|---|---|
| 自定义主题色 ×3 | `FCLColorPickerDialog`（取色器对话框）分别编辑 `color` / `color2` / `color2Dark`；「重置」回默认值 | `FCL/src/main/java/com/tungsten/fcl/ui/setting/LauncherSettingPage.java:303-358,439-447`；`FCLLibrary/src/main/java/com/tungsten/fcllibrary/component/dialog/FCLColorPickerDialog.java:15` |
| 从背景图提取配色 | Palette API：`getDominantColor` → `color`；`getVibrantColor` → `color2` / `color2Dark` | `LauncherSettingPage.java:455-476` |
| 静态背景图（分昼夜） | 用户选图复制到 `filesDir/background/lt.png`、`dk.png`（`FCLPath.LT_BACKGROUND_PATH`/`DK_BACKGROUND_PATH`）；缺省回退 FCLLibrary 内置 `background_light.jpg` / `background_dark.jpg` | `ThemeEngine.java:125-144`；`Theme.java:214-217`；`FCLLibrary/src/main/res/drawable/` |
| 动态视频背景 | 存在 `FCLPath.LIVE_BACKGROUND_PATH` 视频时，主界面 `VideoView` 循环播放，音量可调（`videoBackgroundVolume`，默认 100） | `FCL/src/main/java/com/tungsten/fcl/activity/MainActivity.kt:765-799`；`FCL/src/main/res/layout/activity_main.xml:11-14`；`LauncherSettingPage.java:374-388` |
| 背景加载入口 | `Theme.getBackground(context)` 按 uiMode 返回昼夜 `BitmapDrawable`，主界面根布局 `binding.background` 承载 | `Theme.java:157-160`；`MainActivity.kt:130` |
| 忽略刘海（fullscreen） | `fullscreen` 布尔，`LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES` + 沉浸式 flag | `Theme.java:211`；`ThemeEngine.java:102-123` |
| 动画速度 | `animation_speed` int，默认 `8` | `Theme.java:213`；`MainActivity.kt:324` |

---

## 7. 迁移要点速查

- 品牌主色唯一来源：`#7797CF`，全部 brand 属性同色；Miuix `ColorScheme` 的 `primary` 默认值即此。
- 真正的「内容色」是运行时三件套 `color / color2(light) / color2Dark(dark)` + 派生 `ltColor / dkColor / autoTint / autoHintTint`，Compose 侧建议做成可观察的 `State<Color>` 并复刻 HSV 派生算法（±(1−x)×0.3，见 §2）。
- 暗黑模式资源覆盖极少（仅 2 个颜色），大部分暗色表现依赖动态引擎；`ui_bg_color` 等半透明浮层色无 dark 变体，需在 Miuix 侧新增。
- 圆角体系仅两档：`5dp`（列表项/按钮）与 `8dp`（面板/对话框）；描边 `1dp`/`1.5dp` 灰；无阴影体系。
- 字号 9 档（8–20sp），间距以 `10dp` 为基准网格；均无 dimens 资源，全部内联，迁移时需逐页核对。
- 背景系统（昼夜双图 + 视频背景 + Palette 取色）是用户核心自定义能力，迁移时功能必须等价保留。

*统计：颜色类 token 32 条（§1 静态 24 + §2 动态 8）、字号 9 档、圆角/描边/阴影 7 条、间距刻度 8 档、dark 变体 2 项、自定义能力 7 项，合计 65 个条目。*
