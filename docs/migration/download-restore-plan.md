# 下载页面还原方案（download-restore-plan）

> 目的：系统性消除 Compose 版下载域与旧版（`pre-miuix-baseline`）之间的结构/交互/视觉差距。
> 基准读取方式：`git show pre-miuix-baseline:<path>`。现状代码：`FCL/src/main/java/com/tungsten/fcl/ui/download/compose/`（10 个文件）。
>
> **范围约定**：
> - 截图阅览已按维护者要求删除，**不加回**（旧 `page_download_addon_info.xml` 左栏截图区、`RemoteModScreenshotAdapter` 不再规划）。
> - 主题圆角（`FCLCornerRadius` 5dp/8dp）、滑杆、输入框（`FCLTextField` 下划线样式）已全局修复，本方案不重复。
> - 整合包安装向导（`ModpackSelectionPage`/`LocalModpackPage`/`RemoteModpackPage`/`ModpackInstaller`）仍为保留的原生 View 页面（见 cleanup-report §53），不在 Compose 还原范围，但与 Compose 页混排造成的风格断裂列为决策点 D6。
> - 差距分级：**P0 结构错位**（页面骨架/栏位/层级不同）、**P1 交互缺失**（旧版可做、新版不可做的行为）、**P2 视觉偏差**（同构但尺寸/间距/排布不同）。
> - 工作量单位：人日（d），按熟悉本代码库的执行者估算。

---

## 0. 容器与 Tab（ui_download.xml / DownloadUI）

### 旧版结构
`ui_download.xml`：`ConstraintLayout` → `FCLTabLayout`（6 Tab：游戏/整合包/Mod/资源包/存档/光影，`bg_container_white` 底、`follow_theme`、`tabGravity=fill`、margin 10/10/10）+ `FCLUILayout` 容器。`DownloadUI` 负责 Tab 切换 `DownloadPageManager.switchPage`、返回键先弹临时页、Profile/版本切换广播。

### 现状
`ui_download.xml` 与 `DownloadUI.java` **未改动**，`DownloadPageManager` 仅把页面实现换成 `ComposeDownloadPage`（ComposeDownloadShells.kt:42）。

### 差距清单
无。Tab 容器形态、返回链、版本广播均与旧版一致。**本页不需要还原。**

---

## 1. 远程资源搜索页（RemoteModSearchScreen ↔ page_download.xml + DownloadPage）

覆盖 Tab 1-5（整合包/Mod/资源包/存档/光影），现状文件：`RemoteModSearchScreen.kt`、`ComposeDownloadPages.kt`。

### 旧版结构（page_download.xml，精确到关键属性）

```
ConstraintLayout (paddingStart/Top/End=10dp)
├─ ScrollView#search_layout            宽 30%（layout_constraintWidth_percent=0.3）
│   bg_container_white，marginBottom=10dp
│   └─ LinearLayoutCompat (padding=10dp, vertical)
│       ├─ FCLTextView "名称"           单行，auto_text_tint
│       ├─ FCLEditText#name             marginTop=10dp，imeOptions=actionSearch，13sp
│       ├─ FCLTextView "下载源" + FCLSpinner#download_source   （仅多源时 VISIBLE）
│       ├─ FCLTextView "游戏版本" + FCLSpinner#game_version
│       ├─ FCLTextView "ModLoader" + FCLSpinner#modloader      （仅 Mod Tab VISIBLE）
│       ├─ FCLTextView "分类" + FCLSpinner#category            （递归缩进树，4 空格/级）
│       └─ FCLTextView "排序" + FCLSpinner#sort
│       （label→spinner 之间间距均 10dp；spinner item = item_spinner_auto_tint：14sp、padding 8dp、透明底、marquee）
├─ FCLButton#install_modpack   宽 30%，位于 search 上方（仅整合包 Tab VISIBLE）
├─ FCLButton#search            宽 30%，贴底，marginBottom=8dp
└─ CoordinatorLayout（右 70%，marginStart=10dp）
    ├─ FCLAppBarLayout (auto_tint=ltColor)
    │   └─ LinearLayoutCompat#list_layout (scrollFlags=scroll|enterAlways|snap → 列表上滑时分页栏折叠)
    │       ├─ FCLImageView#translate  40dp，ic_translation（仅 Mod/整合包 Tab + 中文环境 VISIBLE）
    │       ├─ FCLTextView#page        weight=1，bg_container_white + auto_text_background_tint（主色底），可点击→EditDialog 跳页
    │       └─ FCLButton#previous/next/first/last   主色实心，padding=10dp，marginStart=10dp
    └─ RecyclerView#list (marginTop=5dp，appbar_scrolling_view_behavior)
+ FCLProgressBar / FCLImageButton#retry（右 70% 区域居中，加载/失败态）
```

行为（DownloadPage.java）：搜索按钮/IME 搜索键触发搜索并重置页码（:280-286,:391）；加载中禁用全部输入控件（:125-140）；失败显示居中 retry；点页码弹 EditDialog 跳页、越界钳制（:414-428）；下载源切换重拉分类并重搜（:318,:445）；搜索 hint 按 `supportChinese` 切换中英文提示（:287）。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| S-P0-1 | P0 | **筛选控件形态**：旧版是"小字 label + 紧凑下拉行（FCLSpinner，14sp、透明底）"的纵向堆叠；现状用 `WindowSpinnerPreference`（设置项式整行：左标题、右当前值+箭头、行高约 56dp），5 个筛选撑满整个左栏且风格像设置页，与旧版面密度完全不同。这是"差距太大"的最大来源。 |
| S-P0-2 | P0 | **分页栏折叠行为缺失**：旧版分页栏在 `FCLAppBarLayout` 内随列表上滑折叠（scroll\|enterAlways\|snap）；现状分页栏为固定卡片常驻，列表可用高度变矮。 |
| S-P1-1 | P1 | **IME 搜索键缺失**：旧版 `imeOptions=actionSearch` + `OnEditorActionListener` 直接触发搜索；现状 `FCLTextField` 未传 `keyboardOptions/keyboardActions`，只能点搜索按钮。 |
| S-P1-2 | P1 | **加载中未禁用筛选控件**：旧版 `setLoading` 禁用 name/spinner×4/search；现状只禁用了搜索框与搜索按钮，`WindowSpinnerPreference` 未传 enabled。 |
| S-P1-3 | P1 | **搜索 hint 缺失**：旧版按 supportChinese 显示"支持中文搜索"/"仅英文搜索"hint；现状 label 即 placeholder（"名称"），无 hint 逻辑。 |
| S-P2-1 | P2 | **按钮顺序颠倒**：旧版整合包 Tab 底部为 [安装本地整合包] 在上、[搜索] 贴底；现状 [搜索] 在上、[安装整合包] 在下。 |
| S-P2-2 | P2 | **间距体系**：旧版面板 padding=10dp、label 间距 10dp、列表项 marginBottom=10dp、分页栏与列表间距 5dp；现状统一 8dp。 |
| S-P2-3 | P2 | 翻译按钮旧版 40dp 图标（`use_theme_color`）；现状 Miuix `IconButton`（默认 24dp+触摸区），视觉偏小但可接受——建议保留为 Miuix 风格化。 |

### 还原方案（RemoteModSearchScreen.kt）

1. **自研紧凑筛选控件 `FCLDropdownField`**（新 Composable，建议放 `ui/compose/FCLControls.kt`，全下载域复用）：
   结构 = `Column { Text(label, 13sp); Row(clickable→弹层) { Text(当前值, 14sp, marquee 效果可省); Icon(arrow_drop_down) } }`，弹层用 Miuix `SuperDropdown` 或 `ListPopup` 承载选项列表（分类树直接显示带 4 空格缩进的字符串，与旧版一致）。替换 5 处 `WindowSpinnerPreference`。**约 1d。**
2. **IME 搜索键**：名称 `FCLTextField` 传 `keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)` + `keyboardActions = KeyboardActions(onSearch = { holder.onSearchClick() })`，并补 `holder` 的 hint 状态（`supportChinese` → `R.string.search_hint_chinese/english`，经 `useLabelAsPlaceholder` 或新 hint 参数呈现）。**约 0.5d。**
3. **加载禁用**：`FCLDropdownField` 增加 `enabled` 参数，`!holder.loading` 透传。**随第 1 项一并完成。**
4. **分页栏折叠**（二选一，见决策点 D1）：
   - 方案 A（推荐）：用 `Modifier.nestedScroll` + 高度/偏移动画，列表上滑时分页栏向上收起、下滑回弹（对齐 enterAlways|snap 语义）。
   - 方案 B：保持常驻，仅把分页栏高度/间距压缩到旧版尺寸。**A 约 1d，B 约 0.2d。**
5. **按钮顺序与间距**：调换搜索/安装整合包按钮顺序；左栏 padding、label 间距、卡片间距回到 10dp 网格（`Spacer(8.dp)` → `10.dp`，`padding(bottom = 8.dp)` → `10.dp`）。**约 0.2d。**

---

## 2. 搜索结果项（RemoteModRow ↔ item_remote_mod.xml + RemoteModListAdapter）

### 旧版结构（item_remote_mod.xml）

```
FCLConstraintLayout#parent  marginBottom=10dp，bg_container_white_clickable（ltColor 染色），
                            padding 10/8，stateListAnimator=anim_scale（按压缩放），auto_tint
├─ FCLImageView#icon        30×30dp，左端垂直居中
├─ FCLTextView#title        14sp 单行，icon 右 10dp，第一行
├─ FCLTextView#tag          11sp，内联在 title 右侧（marginStart=10dp，与 title 基线对齐），marquee
├─ FCLImageView#icon_download 12×12dp，第二行起点（title 左缘对齐）
├─ FCLTextView#download_count 12sp 单行，icon_download 右侧
└─ FCLTextView#description  12sp，内联在 download_count 右侧（marginStart=5dp），marquee
```
即**两行布局**：第一行 = 标题+分类 tag，第二行 = 下载量+简介（同一行）。Adapter 另有：Glide 图标、每项入场 translationX 动画、"[已安装]"前缀（Mod Tab）。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| R-P0-1 | P0 | **行内结构不同**：现状为三行竖排（title / tag / description）+ 下载量图标挤到最右列；旧版是两行（title+tag 同行、下载量+description 同行）。信息密度与视觉动线完全不同，列表观感差异大。 |
| R-P2-1 | P2 | 图标 40dp vs 旧版 30dp。 |
| R-P2-2 | P2 | 字号：旧版 tag 11sp / 下载量与简介 12sp；现状下载量 11sp。 |
| R-P2-3 | P2 | 旧版 tag/description 为 marquee 滚动；现状 Ellipsis（决策点 D4）。 |
| R-P2-4 | P2 | 按压反馈：旧版 `anim_scale` 按压缩放；现状 `Row.clickable` 无任何反馈。 |
| R-P2-5 | P2 | 项间距 8dp vs 旧版 10dp（随 §1 第 5 项统一修）。 |

### 还原方案（RemoteModSearchScreen.kt `RemoteModRow`）

重写行内布局为两行：`Row(第一行){ Text(title, body1, weight 不占满) + Text(tag, 11sp, paddingStart=10dp, weight=1f, Ellipsis) }`；`Row(第二行){ Icon(download,12dp) + Text(count,12sp) + Text(description,12sp, paddingStart=5dp, weight=1f, Ellipsis) }`。图标改 30dp。卡片改用 `FCLCard(onClick=…, pressFeedbackType = PressFeedbackType.Sink)`（对齐 anim_scale，与 §3/§4 既有用法一致）。**约 0.5d。**

---

## 3. 远程资源详情页（RemoteModInfoScreen ↔ page_download_addon_info.xml + RemoteModInfoPage）

### 旧版结构

```
ConstraintLayout (padding 10/10/10)
├─ FCLProgressBar / FCLImageButton#retry（整页居中，加载/失败态）
└─ LinearLayout#layout (vertical)
    ├─ FCLLinearLayout 头部（bg_container_white + ltColor 染色，padding 10/8, horizontal）
    │   ├─ FCLImageView#icon        30×30dp
    │   ├─ Column (weight=1, marginStart=10dp)
    │   │   ├─ Row: FCLTextView#name 14sp + FCLTextView#tag 11sp（内联，marginStart=10dp）
    │   │   └─ FCLTextView#description 12sp **singleLine**
    │   ├─ FCLTextView#mcmod        纯文本可点（有译名才 VISIBLE），marginStart=10dp
    │   └─ FCLImageButton#website   ic_baseline_jump_24，no_padding
    └─ FCLLinearLayout (horizontal, marginTop=10dp)
        ├─ FCLConstraintLayout 左栏（weight=0.5，bg_container_white，截图区：FCLEditText#search 置顶 + 截图 RecyclerView）
        │   ※ 截图已删，但【版本搜索框原本在左栏】
        └─ ListView#version_list 右栏（weight=1，bg_container_white + ltColor 染色，divider 透明 10dp）
            条目 = ModGameVersionAdapter：纯文本行（"Minecraft x.x"/推荐项），无卡片
```
行为：搜索框实时过滤版本列表、推荐版本置顶（:145-159）；mcmod/website 开浏览器；后台已安装检测加前缀；失败整页 retry。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| I-P0-1 | P0 | **双栏 → 单栏**：截图删除后现状把头部、搜索框、版本列表全部堆成全宽单列；旧版版本列表是占 2/3 宽的右栏白容器。是否恢复分栏见决策点 D2（截图已无，左栏无内容可放，**建议保持单栏但把版本列表恢复为"白容器内纯文本行"形态**）。 |
| I-P1-1 | P1 | **版本列表项形态**：旧版是纯文本行（透明底、统一白容器、行距由 10dp divider 提供）；现状每项一张独立卡片（primaryContainer 底），视觉比旧版"重"很多。 |
| I-P2-1 | P2 | website 按钮图标：旧版 `ic_baseline_jump_24`；现状误用 `ic_baseline_earth_24`。 |
| I-P2-2 | P2 | 头部图标 60dp vs 旧版 30dp。 |
| I-P2-3 | P2 | description 旧版 **singleLine** 12sp；现状不限行 11sp，长简介把头卡撑得很高。 |
| I-P2-4 | P2 | name/tag 旧版同行内联；现状 tag 换行（tag 为空时倒是一致）。 |

### 还原方案（RemoteModInfoScreen.kt）

1. **头部 `InfoCard`**：图标 30dp；name+tag 恢复同行内联；description 恢复 `maxLines = 1 + Ellipsis`、12sp；website 图标换回 `ic_baseline_jump_24`。**约 0.3d。**
2. **版本列表去卡片化**：改为一张 `FCLCard`（primaryContainer）容器 + 内部 `Column` 纯文本行（"Minecraft x.x"，body2，padding 12dp，行间无分割线——对齐旧版透明 divider 纯间距），行点击 `Modifier.clickable` + Sink 反馈可省（旧版无按压动画之外的反馈）。搜索框保留在列表上方（旧版它在左栏，截图删除后置于列表上方是最小改动，决策点 D2）。**约 0.5d。**

---

## 4. 版本文件列表页（RemoteModVersionScreen ↔ page_download_addon_version.xml + item_mod_version.xml + ModVersionAdapter）

### 旧版结构

页面 = 裸 `ListView`（页面 padding 10dp，divider 透明 0dp）。条目（item_mod_version.xml）：
```
FCLLinearLayout#parent  marginBottom=10dp，bg_container_white_clickable + ltColor 染色，
                        padding 10/8，anim_scale 按压
└─ Column (weight=1)
    ├─ Row: FCLTextView#name 14sp + FCLTextView#tag 11sp（内联 marginStart=10dp）
    └─ FCLTextView#date 12sp 单行（第二行，name 左缘对齐）
```

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| V-P1-1 | P1 | **date 位置错位**：旧版 date 在 name/tag 之下第二行；现状 date 被放到条目最右端与 name 同行（`Row { Column(name,tag); Spacer; Text(date) }`）。 |
| V-P2-1 | P2 | 条目 padding：旧版 10/8dp；现状 12dp 全边。 |
| V-P2-2 | P2 | 按压反馈：现状已是 Sink（对齐 anim_scale），无差距。 |

（tag 内容、FULL 日期格式、入场动画、Mod Tab 进下载确认页的分发均已对齐。）

### 还原方案（RemoteModVersionScreen.kt）

条目改回旧版结构：`Column { Row { Text(name); Text(tag, 11sp, paddingStart=10dp) }; Text(date, 12sp) }`，padding 改 10/8dp。**约 0.2d。**

---

## 5. 下载确认页（RemoteModDownloadScreen ↔ page_download_addon.xml + RemoteModDownloadPage + DependencyAdapter）

### 旧版结构

```
ConstraintLayout (padding 10/10/10)
└─ FCLLinearLayout (vertical)
    ├─ 头部 FCLLinearLayout（bg_container_white + ltColor，padding 10/8）
    │   ├─ Row: FCLTextView#name 14sp + FCLTextView#tag 11sp（内联）
    │   └─ FCLTextView#date 12sp
    ├─ RelativeLayout (weight=1, marginTop=10dp)
    │   ├─ FCLProgressBar / retry（居中）
    │   └─ ScrollView#dependency_layout（bg_container_white + ltColor）
    │       └─ 动态构建：分组标题文本(padding 10, autoTint) + 1px 灰分割线 + 组内 ListView(1px divider)
    │          条目 = DependencyAdapter：纯文本行「依赖: 名称」，padding 10dp，bg_container_transparent_clickable
    └─ Row (marginTop=10dp)：FCLButton download/save_as/cancel/back **一行四按钮等宽**，间距 5dp
```
行为：下载/另存为/取消（一次返回）/返回（连弹三层 + 点击后禁用）——均已对齐；依赖加载失败 retry+Toast 已对齐。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| D-P1-1 | P1 | **底部按钮一行 → 两行**：旧版四按钮一行等宽；现状两行各两按钮，页面底部变高、依赖区变矮。 |
| D-P1-2 | P1 | **依赖区单一容器 → 多卡片**：旧版全部依赖组在同一个白容器内（组间 1px 灰线分隔）；现状每组一张独立卡片+8dp 间隔，视觉更"碎"。 |
| D-P2-1 | P2 | 头部 name+tag 旧版同行内联；现状 tag 换行。 |

### 还原方案（RemoteModDownloadScreen.kt）

1. 四按钮恢复一行 `Row(spacedBy(5.dp)) { Button × 4 (weight=1f) }`（按钮文字短，一行可放下）。**约 0.2d。**
2. 依赖区改为单张 `FCLCard` + 内部 `Column`：分组标题（padding 10dp）+ `HorizontalDivider`(1dp, 灰) + `DependencyRow` 列表，组间再加 divider（对齐旧版 preSplit 逻辑）。**约 0.5d。**
3. 头部 name+tag 改同行。**随第 2 项顺带。**

---

## 6. 游戏版本列表页（VersionInstallScreen ↔ page_install_version.xml + VersionInstallPage）

### 旧版结构

```
ConstraintLayout (padding 10/10/10)
└─ CoordinatorLayout
    ├─ FCLAppBarLayout (bg_container_white, auto_tint)
    │   └─ FCLLinearLayout#bar (horizontal, padding 10/2, scrollFlags=scroll|enterAlways|snap → 可折叠)
    │       ├─ FCLCheckBox release/snapshot/old/april_fools（marginStart=10dp）
    │       ├─ FCLEditText#search（weight=1，imeOptions=flagNoFullscreen，实时过滤）
    │       └─ FCLImageButton#refresh
    └─ RecyclerView#list (marginTop=5dp)
+ FCLProgressBar / FCLImageButton#failed_refresh（居中）
```
条目 = item_remote_version.xml（见 §7）。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| G-P1-1 | P1 | **顶栏单行 → 两行**：旧版 4 个 CheckBox + 搜索框 + 刷新按钮同一行（搜索框 weight=1）；现状 CheckBox 一行、搜索+刷新一行，顶栏高度约翻倍。 |
| G-P1-2 | P1 | **顶栏折叠行为缺失**（同 S-P0-2，scroll\|enterAlways\|snap）。 |
| G-P1-3 | P1 | **IME flagNoFullscreen 缺失**：旧版搜索框横屏不全屏输入；现状未设。 |
| G-P2-1 | P2 | 条目间距 8dp vs 10dp（统一修）。 |

（4 勾选过滤、空结果自动全勾、实时搜索、刷新/失败三态、版本项跳安装信息页均已对齐。）

### 还原方案（VersionInstallScreen.kt）

1. 顶栏恢复单行：`Row { Checkbox×4; FCLTextField(weight=1f, imeOptions=flagNoFullscreen 对应 KeyboardOptions); IconButton(refresh) }`，行内容不下时保留 `horizontalScroll` 兜底（对齐旧版 weight 压缩行为可接受差异，见决策点 D3）。**约 0.3d。**
2. 折叠行为与 §1 第 4 项同一方案统一实现（可抽成共用 `fclCollapsingBar` modifier）。**计入 §1。**
3. `FCLTextField` 传 `keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default, …)` 并确认横屏 `flagNoFullscreen` 等价行为（Compose 默认即不全屏，验证即可）。**约 0.1d。**

---

## 7. 版本行（RemoteVersionRow ↔ item_remote_version.xml + RemoteVersionListAdapter）

### 旧版结构

```
FCLConstraintLayout#parent  marginBottom=10dp，bg_container_white_clickable + ltColor，padding 10/8，anim_scale
├─ FCLImageView#icon     30×30dp
├─ FCLTextView#version   14sp，第一行
├─ FCLTextView#tag       11sp，内联 version 右侧，bg_container_white + 主色 tint（chip），marquee
├─ FCLTextView#date      12sp，第二行（无日期 GONE），marquee
├─ FCLImageButton#wiki   ic_baseline_earth_24（仅游戏 Release/Snapshot 显示）
└─ FCLImageButton#save   ic_baseline_jump_24（仅非游戏/非 API 库显示）→ 原生 AlertDialog 选镜像 URL
```

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| VR-P2-1 | P2 | **save 按钮位置**：`InstallerListScreen` 中 save 按钮被放在卡片**外部**右侧（Row: card(weight) + IconButton）；旧版 save 在条目内部右端，随卡片一起染色/按压。 |
| VR-P2-2 | P2 | date/tag 的 marquee → Ellipsis（同 D4）。 |
| VR-P2-3 | P2 | tag chip 圆角：旧版 bg_container_white（5dp）；现状 RoundedCornerShape(5.dp)，一致。无差距。 |

（图标映射、wiki 特例映射、tag 文案、入场动画均逐条对齐，质量高。）

### 还原方案（InstallerListScreen.kt / RemoteVersionUi.kt）

把 save `IconButton` 移入 `RemoteVersionRow` 卡片内部右端（对齐 item_remote_version.xml 约束），`RemoteVersionRow` 增加可选 `trailing` 槽或直接内建 save 逻辑。**约 0.2d。**

---

## 8. 安装信息页（VersionInstallInfoScreen ↔ page_installer.xml + view_installer_item.xml + VersionInstallInfoPage）

### 旧版结构

```
ConstraintLayout (padding=10dp)
├─ LinearLayoutCompat#name_bar（bg_container_white + ltColor，padding 10/8, horizontal）
│   ├─ FCLTextView "版本命名"（auto_text_tint）
│   ├─ FCLEditText#edit（weight=1，14sp，自动命名/手动修改标记）
│   └─ FCLImageButton#install（ic_baseline_download_24，auto_tint）
└─ ScrollView#scroll（marginTop=10dp）→ InstallerItemGroup 视图：每项 view_installer_item
    （icon 30dp + name 14sp/state 12sp + 弹性空白 + remove(close) + select(arrow)，白底 clickable，anim_scale）
```

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| VI-P2-1 | P2 | select 箭头：旧版是可点 `FCLImageButton`（点击=整项 action 的镜像）；现状为纯图标、整行 clickable——行为等价，视觉一致，**建议保留现状**。 |
| VI-P2-2 | P2 | 加载器列表：旧版普通 ScrollView 堆叠（每项自带白底）；现状 LazyColumn 卡片——形态等价，无实质差距。 |
| VI-P2-3 | P2 | name_bar 下方间距：旧版 ScrollView marginTop=10dp；现状 `Spacer(Modifier.width(10.dp))`——**用了 width 不是 height**，纵向间距实际是卡片默认 margin，疑似笔误。 |

（自动命名/手动修改标记、互斥联动、Fabric API 警告、三重校验 Toast、GameBuilder 任务链、成功/失败弹窗均已对齐。）

### 还原方案（VersionInstallInfoScreen.kt）

修正 `Spacer(Modifier.width(10.dp))` → `Spacer(Modifier.height(10.dp))`（VersionInstallInfoScreen.kt:334）。**约 0.1d。** 本页其余无需还原。

---

## 9. 加载器版本选择页（InstallerListScreen ↔ InstallerListPage + page_install_version.xml）

### 旧版结构
复用 page_install_version.xml：过滤栏（3 CheckBox，无类型分级的库整栏 GONE；愚人节与搜索框 INVISIBLE）+ 刷新 + 列表（item_remote_version）。空列表 Toast + failed_refresh；过滤为空自动全勾。

### 现状差距清单

| 编号 | 级别 | 差距 |
|---|---|---|
| IL-P2-1 | P2 | save 按钮在卡片外（同 VR-P2-1，一并修）。 |
| IL-P2-2 | P2 | 顶栏与列表间距 8dp vs 旧版 marginTop=5dp；统一按 10dp 网格后取 10dp（决策点 D5）。 |

（hasType 隐藏过滤栏、空列表 Toast、自动全勾、active 守卫、三态进度均已对齐。）

### 还原方案
随 §7 一并完成，无独立工作项。

---

## 10. 跨页面全局差距（建议优先于单页修补）

| 编号 | 级别 | 差距 | 方案 | 工作量 |
|---|---|---|---|---|
| X-1 | P0 | **Spinner 形态**：`WindowSpinnerPreference`（设置项式）vs 旧版 FCLSpinner（label+紧凑下拉行）。影响搜索页 5 处，是左栏观感的根因。 | 自研 `FCLDropdownField`（§1 第 1 项），Miuix `SuperDropdown`/`ListPopup` 做弹层。 | 1d |
| X-2 | P1 | **AppBar 折叠**：旧版两处 `scroll\|enterAlways\|snap`（搜索页分页栏、游戏版本页过滤栏）在 Compose 全部丢失。 | 抽共用 `fclCollapsingBar`（nestedScroll + 偏移动画），决策点 D1。 | 1d |
| X-3 | P1 | **IME 行为**：actionSearch（搜索页）、flagNoFullscreen（游戏版本页）未迁移。 | 各 `FCLTextField` 补 `keyboardOptions/keyboardActions`。 | 0.5d |
| X-4 | P2 | **10dp 网格 → 8dp**：页面 padding、卡片间距、项间距全面 8dp。 | 下载域内统一回 10dp（含 §1-§9 各处 Spacer/padding）。 | 0.3d |
| X-5 | P2 | **按压反馈**：旧版列表项统一 `anim_scale` 按压缩放；`RemoteModRow` 等用裸 `clickable` 无反馈。 | 可点条目统一走 `FCLCard(onClick=…, pressFeedbackType=Sink)`。 | 0.2d |
| X-6 | P2 | **marquee → Ellipsis**：旧版 tag/description/date 多处跑马灯。 | 决策点 D4，建议保留 Ellipsis。 | 0 |
| X-7 | P2 | **加载/失败态**：旧版 FCLProgressBar（环形）vs Miuix `InfiniteProgressIndicator`；retry 灰图标已对齐。 | 决策点 D4，建议保留 Miuix 指示器。 | 0 |

---

## 11. 总结

### 11.1 差距计数（按页）

| 页面 | P0 | P1 | P2 |
|---|---|---|---|
| 0 容器/Tab | 0 | 0 | 0 |
| 1 远程资源搜索页 | 2 | 3 | 3 |
| 2 搜索结果项 | 1 | 0 | 5 |
| 3 详情页 | 1 | 1 | 4 |
| 4 版本文件列表页 | 0 | 1 | 2 |
| 5 下载确认页 | 0 | 2 | 1 |
| 6 游戏版本列表页 | 0 | 3 | 1 |
| 7 版本行 | 0 | 0 | 2 |
| 8 安装信息页 | 0 | 0 | 3 |
| 9 加载器选择页 | 0 | 0 | 2 |
| 10 跨页面全局 | 1 | 2 | 4 |
| **合计** | **5** | **12** | **27** |

### 11.2 Top 5 最严重差距（维护者痛感排序）

1. **X-1 / S-P0-1 筛选控件形态**（搜索页左栏）：WindowSpinnerPreference 设置项式 vs 旧版紧凑 label+下拉——左栏观感完全不同的根因。
2. **R-P0-1 搜索结果项两行结构**：title+tag / 下载量+description 的两行紧凑排布被改成三行竖排+右侧孤立下载量。
3. **X-2 / S-P0-2 / G-P1-2 顶栏折叠丢失**：两页旧版 AppBar 随列表滑动折叠，新版常驻占高。
4. **I-P1-1 + I-P0-1 详情页**：版本列表从"白容器纯文本行"变成"每项一卡片"，且头部图标/description 行数放大，整页变"重"。
5. **D-P1-1 + D-P1-2 下载确认页**：四按钮拆两行、依赖区碎成多卡片。

### 11.3 分批执行计划

- **批 1（结构还原，约 2.5d）**：X-1 `FCLDropdownField` 并替换 5 处 spinner（S-P0-1/S-P1-2）；R-P0-1 结果项两行重写；I 头部+版本列表去卡片化（I-P0-1/I-P1-1/I-P2-*）；V-P1-1 date 归位。
  门禁：编译通过；搜索页 5 个 Tab 目视对照旧版截图逐页过一遍；分类树缩进、源切换重搜、加载禁用回归正常。
- **批 2（交互还原，约 2d）**：X-2 折叠顶栏（搜索页+游戏版本页）；X-3 IME（actionSearch、flagNoFullscreen、hint）；D-P1-1 四按钮一行；D-P1-2 依赖区单容器；G-P1-1 顶栏单行；VR-P2-1 save 按钮入卡。
  门禁：软键盘搜索键可触发；列表上滑顶栏折叠/下滑回弹；下载确认页四按钮与三层返回行为回归。
- **批 3（视觉对齐，约 1d）**：X-4 10dp 网格；X-5 按压反馈统一 Sink；S-P2-1 按钮顺序；I-P2-1 图标换回 jump_24；I-P2-3 description 单行；VI-P2-3 Spacer 笔误；各 P2 字号/图标尺寸（30dp 等）。
  门禁：逐页与旧版截图 diff；无功能回归（跑既有构建门禁 `./gradlew :FCL:assembleDebug` 或项目既定 CI 任务）。

### 11.4 风险点

- **FCLDropdownField 弹层**：Miuix `SuperDropdown` 在超长分类树（数百项缩进文本）下的滚动与定位需验证；备选 `ListPopup`。
- **折叠顶栏自研**：nestedScroll 与 LazyColumn 的快速fling 边界（半折叠态 snap）容易出抖动；若超 1d 未收敛，降级为方案 B（常驻压缩高度）。
- **两行结果项**：title 过长时 tag 被挤压——旧版用 ConstraintLayout 双向约束，Compose 需 `weight` + `Ellipsis` 策略，注意 tag 为空时布局回退。
- **加载禁用**：`FCLDropdownField` 的 enabled 要同时禁用弹层触发，避免加载中改条件导致结果与条件不一致（旧版语义）。
- **依赖区单容器**：`Column` + `verticalScroll` 非懒加载，依赖组极多时性能略差——与旧版 ScrollView+ListView 同构，可接受。

### 11.5 决策点（有意的 Miuix 风格化，建议保留，需维护者确认）

| 编号 | 事项 | 建议 |
|---|---|---|
| D1 | 顶栏折叠：完整还原（方案 A）还是常驻压缩（方案 B）？ | 推荐 A；若维护者认为折叠无感可取 B 省 0.8d。 |
| D2 | 详情页：截图删除后是否强行恢复双栏？ | **建议单栏**（左栏已无内容），仅恢复"白容器纯文本行"版本列表。 |
| D3 | 游戏版本页顶栏单行在空间不足时：旧版 weight 压缩 vs 现状 horizontalScroll？ | 建议保留 horizontalScroll 兜底（比旧版更不易挤坏）。 |
| D4 | marquee 跑马灯、`InfiniteProgressIndicator`、Sink 按压反馈：保留 Miuix 风格化？ | 建议全部保留（Ellipsis/Miuix 指示器/Sink），不回退。 |
| D5 | 间距严格回 10dp 网格，还是保留 8dp？ | 建议回 10dp（维护者痛感主要来自密度）。 |
| D6 | 整合包安装向导（ModpackSelectionPage 等）仍是原生旧式页面，与 Compose 页混排风格断裂，是否一并 Compose 化？ | 本方案不含；若维护者痛感强，另立任务（估 3-4d）。 |
| D7 | 加载/失败态图标与染色（primary 进度环、灰 retry）：保留 Miuix 样式？ | 建议保留。 |
