# fcl：control 包（游戏内悬浮控件系统）

范围：`FCL/src/main/java/com/tungsten/fcl/control/`，47 个文件（含 data/view/keyboard/gamepad/download 五个子包），Java+Kotlin 混编约 12000 行。边界类 Controller/Controllers 在 setting 包、FCLBridge 在 fclauncher 包、TouchController 在 com.mio.touchcontroller。

## 一、包结构与模块划分

- 根包（~20 文件）：GameMenu（总控，1325 行）、FCLInput/AWTInput（输入汇聚）、Gyroscope（陀螺仪）、GestureMode/MouseMoveMode/MenuType/MenuCallback（枚举与接口）、左右菜单适配器（LeftMenuAdapter/RightMenuAdapter/MenuUi）、全部编辑与功能对话框、MultiplayerDialog（Terracotta 联机）、JarExecutorMenu（jar 执行器的简化菜单，走 AWT 事件通道）。
- `data/`：控件数据模型 + Gson 手写序列化器 + 样式/快捷文本注册表。
- `view/`：游戏内渲染层（ControlButton/ControlDirection/TouchPad/ViewManager/MenuView/GameItemBar/ControlEditBar/KeycodeView/LogWindow）。
- `keyboard/`：软键盘字符通道（TouchCharInput 是个隐藏 EditText，源自 PojavLauncher）。
- `gamepad/`：手柄映射（基于 fr.spse gamepad_remapper 库）。
- `download/`：控件布局在线下载页的数据类（分类/索引/版本）。

## 二、数据模型与 JSON 持久化

模型全部基于 fakefx 属性系统，实现 `Observable`，任何属性变更经 `ObservableHelper.invalidate()` 触发保存监听。序列化不用 Gson 反射注解，而是每类手写 `JsonSerializer/JsonDeserializer`（字段容错用 `Optional.ofNullable`，JsonElements.kt 统一把 JsonNull 归一为 null 防崩溃）。

层次结构：`Controller`（setting 包，含 id/name/version/versionCode/author/description/controllerVersion + `viewGroups` 列表）→ `ControlViewGroup`（id/name/visibility(VISIBLE|INVISIBLE)/viewData）→ `ViewData`（buttonList + directionList）→ 控件数据。磁盘位置 `FCLPath.CONTROLLER_DIR/<controllerId>.json`；样式单独存 `styles/button_styles.json`、`styles/direction_styles.json`；快捷输入存 `input/input_text.json`（格式 `备注&*&内容`）；菜单设置存 `FILES_DIR/menu_setting.json`（MenuSetting.kt，约 30 个字段：吸附、手势模式、鼠标灵敏度/尺寸/偏移、窗口缩放、itemBar 尺寸、menuPositionX/Y 悬浮球比例位置等，任意变更即整体回写）。

关键 JSON 字段：
- `ControlButtonData`：id、text、style（**存样式名字符串**；旧格式兼容——若 style 节点含 `"name"` 对象则内联解析并注册进样式表，否则按名查表找不到回退 Default）、baseInfo、event。
- `BaseInfoData`：visibilityType(ALWAYS/IN_GAME/MENU)、xPosition/yPosition（**千分比，实际值×10，即 0–1000**）、sizeType(PERCENTAGE/ABSOLUTE)、absoluteWidth/Height(dp)、percentageWidth/Height{reference(SCREEN_WIDTH/HEIGHT), size}。
- `ButtonEventData`：pointerFollow（点击拖动控制指针）、movable（运行时可拖动按钮本身）、swipable（滑动联动）、pressEvent/longPressEvent/clickEvent/doubleClickEvent 四组 `Event{autoKeep, autoClick, openMenu, switchTouchMode, switchMouseMode, input, quickInput, outputText, outputKeycodes[], bindViewGroup[]}`。注意序列化键 `"Movable"` 大写 M（历史遗留，读写一致所以兼容）。
- `DirectionEventData`：up/down/left/rightKeycode 四个**数组**（多键同发，默认 WASD；反序列化兼容旧的单 int 格式）、followOption(FIXED/CENTER_FOLLOW/FOLLOW)、sneak+snneakKeycode(双击中心潜行，默认 LEFT_SHIFT)、deadZone（摇杆死区 %）、canLock+lockThreshold（前进锁）。
- `ControlButtonStyle`：normal/pressed 两套（textColor/textSize/strokeWidth/strokeColor/cornerRadius/fillColor，尺寸均×10 存储）；`ControlDirectionStyle`：styleType(BUTTON/ROCKER) + buttonStyle{interval,...} + rockerStyle{rockerSize/bgCornerRadius/bgStroke*/rockerCornerRadius/rockerStroke*}。
- 迁移与容错：`controllerVersion` 常量 `Constants.CONTROLLER_VERSION=21`；ViewData 反序列化按 id 去重（注释：历史崩溃可能留下重复 id）；JSON 损坏文件直接删除重建。

**轻量加载（重要架构点）**：Controllers 列表启动时用 `Controller.parseLightweight()` 流式（JsonReader）只读元数据和每组 id/name/visibility，viewData 留空并标记 `dataLoaded=false`；ViewManager 首次渲染或编辑时经 `Controllers.loadViewGroup()`（IO 线程解析 + 主线程回调）补全，`requestLoadGroup` 带 loadingGroups 去重与批量进度框（最短显示 500ms）。保存前 `ensureAllLoaded()` 会阻塞式补全所有组防止丢数据；`saveToDisk()` 用 dirty 标志 + AtomicBoolean 合并多次保存为一次 IO。

## 三、视图层级与事件流

**宿主**：GameMenu 不是 View，而是实现 `MenuCallback`/`FCLBridgeCallback` 的控制器，`getLayout()` inflate `view_game_menu.xml`——根是 **DrawerLayout**（左右抽屉菜单），内含 base_layout(RelativeLayout)：TouchPad、LogWindow、fps/memory(DraggableTextView)、CursorView（com.mio 的指针图，支持 png/gif 自定义光标）、launch_progress、TouchCharInput(input_scanner)、TouchControllerInputView、GameItemBar，以及 include 的左右菜单。ControllerActivity 中 `addContentView(menu.getLayout())` 直接叠在游戏 SurfaceView 之上（同一 Activity 内，非独立 overlay 窗口；编辑布局预览走 ControllerActivity 传 null bridge，即 simulated 模式）。

**渲染**：ViewManager.setup 创建 MenuView（悬浮球，elevation 2000）和 ControlEditBar（elevation 1500），监听 controller/viewGroup/editMode 三个属性变化即 `initializeController()` 全量重建：按 `targets()`（游戏模式=所有 VISIBLE 组；编辑模式=当前组+手动开启的参考组）逐组 `renderGroup`，每组按键 `translationZ = 组序号×2`（参考组 -2 且半透明 ghost），按钮 elevation 113、方向键 112——保证跨组遮挡稳定（异步加载乱序也不出错）。ControlButton 继承 AppCompatButton，尺寸/位置由 BaseInfoData 千分比换算像素（post 后 setX/setY），样式刷成 GradientDrawable；可见性用 fakefx 绑定：ghost 恒显、编辑模式只显示当前组的控件、游戏模式=parentVisibility && (ALWAYS || (IN_GAME && CursorDisabled) || (MENU && CursorEnabled))。数据→视图通过 5 个 InvalidationListener（notify/dataChange/boundary/visibility/alpha，全部 Schedulers.androidUIThread 投递），removeListener 时置 null 防泄漏。

**输入注入链路（核心）**：`FCLInput.sendKeyEvent(keycode, press)` → 鼠标键映射（MOUSE_LEFT=1000→FCLBridge.Button1 等，滚轮走 `CallbackBridge.sendScroll`）→ 普通键 `bridge.pushEventKey()` → `org.lwjgl.glfw.CallbackBridge.sendKeycode()`（静态 JNI 桥，全局 modifiers 由 `CallbackBridge.setModifiers` 维护）→ 游戏 JVM。指针：`setPointer(x,y)` 同时移动 CursorView、更新 cursorX/Y 与 pointerX/Y，并 `pushEventPointer(x×scaleFactor)`（分辨率缩放）。`sendChar()` 有个关键兼容：lwjglx 的 LWJGL2 兼容层把 keydown 暂存到 charMods，所以字符要**成对补发 keydown/keyup**（用 `EfficientAndroidLWJGLKeycode` 反查键码），查不到才发纯 `pushEventChar`。`sendBoundKeyEvent` 经 `MinecraftKeyBindingMapper` 读 options.txt 把 MC 绑定名映射成键码（GameItemBar 快捷栏 1–9、双击换手、QuicInput 自动补 T 开聊天都走它）。物理键盘：`FCLInput.handleKeyEvent`（Activity 级分发）处理外接键/软键盘 ENTER/手柄分支。`AWTInput` 是 jar 执行器的另一条通道：`nativeSendData(EVENT_TYPE_*, ...)` 直传 JNI（EVENT_TYPE_KEY=1005/CHAR=1000/CURSOR_POS=1003/MOUSE_BUTTON=1006）。

**触摸手势（TouchPad，全屏底层）**：编辑模式下点空白清除选中。指针模式（CursorEnabled）：CLICK 模式点哪指哪（Choreographer 延迟 33ms 发左键防误触）；SLIDE 模式滑动移动光标+轻点左键。游戏模式（CursorDisabled）：滑动视角（灵敏度/缩放比修正）、400ms 长按触发抓取（BUILD 左键/FIGHT 右键）、轻点两分支互换、`isDisableLeftTouch` 屏蔽左半屏、双指交给 TouchController。陀螺仪（Gyroscope）启用时滑动只更新 pointerX/Y 本地值，由传感器事件直接 `pushEventPointer`（松手 disableSensor 时补偿提交一次）。`pointerId` 机制：多来源（按钮/TouchPad/外接鼠标"Gyro"/外部"External"）竞争指针控制权，首个 setPointerId 占位，null 才让位。

**虚拟摇杆（ControlDirection）**：BUTTON 型 = 九宫格 9 个 AppCompatButton（按触点坐标分区，斜向同时按两键，对角按钮动态显隐）；ROCKER 型 = area+rocker 两层，`getRockerPositionPoint` 限幅+角度 8 分区（22.5°/67.5°...阈值）切换方向键组，支持死区回中、CENTER_FOLLOW（按在杆上才拖动）、FOLLOW（哪里按哪里跳杆）、双击中心=sneak、前进锁（正北且超过 lockThreshold% 时松手锁前进，杆体描边变主题色提示，再次触摸解除）。

**滑动联动（swipable）**：手指滑出当前按钮边界→释放它→`findSwipableButtonAt` 从顶到底找命中且 isSwipable 的按钮按下（swipePressed/swipeEngaged 状态机），抬起时若出过界不触发单击/双击。

## 四、编辑器架构

编辑器完全在 GameMenu 内（非独立 Activity）：左抽屉 LeftMenuAdapter（编辑开关/边界显示/不透明度/一键隐藏/吸附设置+吸附距离/当前控制器切换，编辑态追加"添加按键/方向键/管理样式"）；右抽屉编辑态整体替换为控件组面板（每组行：点击切组、开关显示参考组、复制/编辑/删除，ItemTouchHelper 长按拖动排序——组顺序即 z 序）。选中控件后 ControlEditBar 悬浮操作栏（设置/复制/删除，定位在控件下方防出屏）。拖动：期望位置=手指 raw 坐标差，`ViewManager.snapPosition` 同边贴齐/邻接保间距（阈值内吸附并经 TouchPad.drawLine 画绿色参考线），吸附修正不进下一帧基准防抖动；缩放：选中后控件内角 12dp 手柄（画在 onDraw，命中区与视觉重合），按钮双角独立缩放、方向键等比，结束写回数据（百分比按参考轴换算回 permille）。EditViewDialog（info/event 两页）+ EditButtonDetails/EditDirectionDetails（基类 EditViewDetails 封装 spinner/seekbar 与 fakefx 属性双向绑定，编辑在 clone 上进行、确认后逐属性回写再 saveController）。样式编辑器 AddButtonStyleDialog/AddDirectionStyleDialog（TabLayout normal/pressed 两页实时预览，FCLColorPickerDialog 取色）。

## 五、值得注意的细节与坑

1. **手柄**：Gamepad 用 RemapperManager 回调 `handleGamepadInput(code,value)` → GamepadMap（可 toggle 的按键、左右摇杆 45° 八方向映射 WASD、右摇杆经 Choreographer 帧回调按帧时间积分模拟鼠标）。SDL 直通模式（MC 26.3+）与映射模式互斥处理，SDL 未就绪时吞事件防双重响应。映射存 FILES_DIR/gamepad.json。
2. TouchCharInput 优先走 SDL 输入框（SdlBridge.isSdlRenderActive 时切 SDLActivity.enableSDLEditKeyboard），否则本地 IME；静态 sActiveInput 供 SDL 侧统一关闭。
3. 一处明显笔误：GameMenu.initCursorView 中 Y 偏移用了 `setOffsetY(menuSetting.getMouseOffsetX())`（X 写到 Y）。
4. ButtonEventData 的 "Movable" 大写键名、ControlDirectionData.equals 参数类型错写成 ControlButtonData（无害但易踩）。
5. MultiplayerDialog 是 Terracotta/EasyTier 联机的状态机 UI（仅当 third_party 中 terracotta 开关打开时右菜单才显示入口）；download 包三类只服务于在线布局下载页。
6. LogWindow 有 200 条/200ms 熔断自动关闭防日志刷屏卡顿；fps/memory 各自独立线程每秒采样。
7. LeftMenuAdapter/RightMenuAdapter 的 SeekBar 行复用前必须摘旧监听（setMax/setMin 会触发旧回调把数值写错行）——代码注释明确记录了这个坑。
