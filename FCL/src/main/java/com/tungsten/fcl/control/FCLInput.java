package com.tungsten.fcl.control;

import static com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper.mapBindingToKeycode;

import android.os.Handler;
import android.os.Looper;
import android.view.Choreographer;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.control.gamepad.Gamepad;
import com.tungsten.fcl.game.sdl.GamepadInputMode;
import com.tungsten.fcl.game.sdl.GamepadModePromptDialog;
import com.tungsten.fcl.game.sdl.SdlBridge;
import com.tungsten.fcl.game.sdl.SdlSettings;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fcl.setting.MenuSetting;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.AndroidKeycodeMap;
import com.tungsten.fclauncher.keycodes.EfficientAndroidLWJGLKeycode;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.keycodes.LwjglKeycodeMap;

import org.libsdl.app.SDLActivity;
import org.lwjgl.glfw.CallbackBridge;

import java.util.HashMap;
import java.util.Map;

public class FCLInput implements View.OnCapturedPointerListener {

    public static final int MOUSE_LEFT = 1000;
    public static final int MOUSE_MIDDLE = 1001;
    public static final int MOUSE_RIGHT = 1002;
    public static final int MOUSE_SCROLL_UP = 1003;
    public static final int MOUSE_SCROLL_DOWN = 1004;

    public static final String EXTERNAL_MOUSE_ID = "External";

    private final int screenWidth;
    private final int screenHeight;

    private Gamepad gamepad;
    private int currentDirection = -1;
    private long lastFrameTime;
    private Choreographer choreographer;
    private float lastAxisZ;
    private float lastAxisRZ;

    public static final HashMap<Integer, Integer> MOUSE_MAP = new HashMap<Integer, Integer>() {
        {
            put(MOUSE_LEFT, FCLBridge.Button1);
            put(MOUSE_MIDDLE, FCLBridge.Button3);
            put(MOUSE_RIGHT, FCLBridge.Button2);
            put(MOUSE_SCROLL_UP, FCLBridge.Button4);
            put(MOUSE_SCROLL_DOWN, FCLBridge.Button5);
        }
    };

    @NonNull
    private final GameMenu menu;

    // 鼠标键按下状态（MOUSE_MAP 的键）。系统把右键/中键转换成 BACK/HOME 的 KeyEvent
    // 与原始按键的 MotionEvent 可能在部分设备上先后到达，按状态机去重，同一物理动作只投递一次
    private final Map<Integer, Boolean> mouseButtonState = new HashMap<>();
    private int lastExternalMouseButtons;

    // 指针捕获看门狗：requestPointerCapture 无返回值，失败是静默的；IME 弹出、对话框、
    // 模拟器注入时序等都会让捕获静默丢失且无人恢复，只能周期检查 hasPointerCapture 重试
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final long CAPTURE_RETRY_FAST_MS = 500;
    private static final long CAPTURE_RETRY_SLOW_MS = 2000;
    private static final int CAPTURE_MAX_FAST_RETRIES = 3;
    private boolean captureWatchdogRunning;
    private int captureRetryCount;

    /** 手动捕获接管：快捷键切换后的意图，覆盖自动策略，游戏光标模式变化时回归 AUTO */
    private static final int MANUAL_CAPTURE_AUTO = 0;
    private static final int MANUAL_CAPTURE_REQUEST = 1;
    private static final int MANUAL_CAPTURE_RELEASE = 2;
    private int manualCaptureIntent = MANUAL_CAPTURE_AUTO;
    /**
     * 持续请求捕获仍失败（部分模拟器/老设备不支持 pointer capture）时启用：
     * 以悬停事件的绝对坐标差值近似相对位移驱动视角，光标到屏幕边缘差值归零会卡住，属降级体验
     */
    private boolean hoverFallbackEnabled;
    private float lastHoverRawX;
    private float lastHoverRawY;
    private boolean lastHoverValid;

    public GameMenu getMenu() {
        return menu;
    }

    private String pointerId;

    public void setPointerId(String pointerId) {
        if (pointerId == null) {
            this.pointerId = null;
        } else if (this.pointerId == null) {
            this.pointerId = pointerId;
        }
    }

    public String getPointerId() {
        return pointerId;
    }

    public FCLInput(@NonNull GameMenu menu) {
        this.menu = menu;

        this.screenWidth = AndroidUtilKt.getScreenWidth();
        this.screenHeight = AndroidUtilKt.getScreenHeight();
    }

    public void setPointer(float x, float y, String id) {
        if (id.equals(pointerId) || id.equals("Gyro")) {
            setPointer(x, y);
        }
    }

    public void setPointer(float x, float y) {
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            menu.getCursor().setX(x);
            menu.getCursor().setY(y);
        }
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            menu.setCursorX(x);
            menu.setCursorY(y);
        }
        menu.setPointerX(x);
        menu.setPointerY(y);
        if (menu.getBridge() != null) {
            menu.getBridge().pushEventPointer(x, y);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void sendKeyEvent(int keycode, boolean press) {
        sendKeyEvent(keycode, 0, press);
    }

    public void sendKeyEvent(int keycode, int keyChar, boolean press) {
        if (menu.getBridge() != null) {
            if (MOUSE_MAP.containsKey(keycode) && MOUSE_MAP.get(keycode) != null) {
                if (isDuplicateMouseButtonEvent(keycode, press)) {
                    return;
                }
                menu.getBridge().pushEventMouseButton(MOUSE_MAP.get(keycode), press);
            } else {
                int code = LwjglKeycodeMap.convertKeycode(keycode);
                if (code >= 0) {
                    CallbackBridge.setModifiers(code, press);
                }
                menu.getBridge().pushEventKey(keycode, keyChar, press);
            }
        }
    }

    private boolean isDuplicateMouseButtonEvent(int keycode, boolean press) {
        // 仅按键参与去重：滚轮是瞬时事件只有 press，进状态机会把后续滚动滤掉
        if (keycode != MOUSE_LEFT && keycode != MOUSE_MIDDLE && keycode != MOUSE_RIGHT) {
            return false;
        }
        Boolean pressed = mouseButtonState.get(keycode);
        if (pressed != null && pressed == press) {
            return true;
        }
        mouseButtonState.put(keycode, press);
        return false;
    }

    /**
     * 光标模式切换或切后台时复位：丢弃悬停触摸的按键快照，并把仍视为按下的鼠标键补发释放，
     * 防止事件来源在切换中丢失（捕获抢走触摸、窗口失焦吞掉 UP）造成卡键
     */
    public void resetExternalMouseState() {
        if (menu.getBridge() != null) {
            for (Integer keycode : MOUSE_MAP.keySet()) {
                if (Boolean.TRUE.equals(mouseButtonState.get(keycode))) {
                    menu.getBridge().pushEventMouseButton(MOUSE_MAP.get(keycode), false);
                }
            }
        }
        mouseButtonState.clear();
        lastExternalMouseButtons = 0;
    }

    public void sendBoundKeyEvent(GameOption option, String binding, int defaultKeycode, boolean press) {
        String key = binding == null ? null : option.get(binding);
        int keycode = mapBindingToKeycode(key, defaultKeycode);
        sendKeyEvent(keycode, press);
    }

    public void sendChar(char keyChar) {
        if (menu.getBridge() != null) {
            // Cleanroom 的 lwjglx 系兼容层将字母等 keydown 暂存，待 charMods 事件合并后才投给游戏，
            // 只发字符无法驱动按键绑定，需按字符反查键码补发一对 keydown/keyup；其余实例保持纯字符通道
            if (CallbackBridge.isCleanroomActive()) {
                int androidKeycode = EfficientAndroidLWJGLKeycode.getAndroidKeycode(keyChar);
                int keycode = AndroidKeycodeMap.convertKeycode(androidKeycode);
                if (keycode != FCLKeycodes.KEY_UNKNOWN) {
                    menu.getBridge().pushEventKey(keycode, keyChar, true);
                    menu.getBridge().pushEventKey(keycode, keyChar, false);
                    return;
                }
            }
            menu.getBridge().pushEventChar(keyChar);
        }
    }

    private View focusableView;

    public View getFocusableView() {
        return focusableView;
    }

    public void initExternalController(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnCapturedPointerListener(this);
        view.getViewTreeObserver().addOnWindowFocusChangeListener(hasFocus -> {
            if (hasFocus && !menu.getMenuSetting().isPhysicalMouseMode()) {
                tryCapturePointer(view);
            }
        });
        view.requestFocus();

        this.focusableView = view;
        startCaptureWatchdog();
    }

    private void tryCapturePointer(View view) {
        // 窗口焦点刚切回或 view 未挂载时请求会被 InputDispatcher 静默拒绝，
        // 包一层防御；失败后由看门狗周期重试兜底
        // 手动释放接管时不自动请求
        if (manualCaptureIntent == MANUAL_CAPTURE_RELEASE) {
            return;
        }
        if (view.isAttachedToWindow() && view.hasWindowFocus() && !view.hasPointerCapture()) {
            try {
                view.requestPointerCapture();
            } catch (Throwable ignored) {
            }
        }
    }

    public void ensurePointerCapture() {
        View view = focusableView;
        if (view == null || menu.getMenuSetting().isPhysicalMouseMode()) {
            return;
        }
        tryCapturePointer(view);
    }

    public void startCaptureWatchdog() {
        if (captureWatchdogRunning) {
            return;
        }
        captureWatchdogRunning = true;
        mainHandler.postDelayed(captureWatchdog, CAPTURE_RETRY_FAST_MS);
    }

    public void stopCaptureWatchdog() {
        captureWatchdogRunning = false;
        mainHandler.removeCallbacks(captureWatchdog);
    }

    private final Runnable captureWatchdog = new Runnable() {
        @Override
        public void run() {
            View view = focusableView;
            if (view == null || !view.isAttachedToWindow()
                    || menu.getActivity().isDestroyed() || menu.getActivity().isFinishing()) {
                captureWatchdogRunning = false;
                return;
            }
            // 手动接管优先；否则实体鼠标模式仅游戏捕获视角期间（CursorDisabled）需要捕获，
            // 菜单态用系统指针；默认模式始终捕获。漏掉 cursorMode 判断会把游戏内刚建立的捕获又释放掉
            boolean captureWanted;
            if (manualCaptureIntent == MANUAL_CAPTURE_REQUEST) {
                captureWanted = true;
            } else if (manualCaptureIntent == MANUAL_CAPTURE_RELEASE) {
                captureWanted = false;
            } else {
                captureWanted = !menu.getMenuSetting().isPhysicalMouseMode()
                        || menu.getCursorMode() == FCLBridge.CursorDisabled;
            }
            long nextDelay;
            if (!captureWanted) {
                hoverFallbackEnabled = false;
                captureRetryCount = 0;
                if (view.hasPointerCapture()) {
                    view.releasePointerCapture();
                }
                nextDelay = CAPTURE_RETRY_SLOW_MS;
            } else if (view.hasPointerCapture()) {
                captureRetryCount = 0;
                hoverFallbackEnabled = false;
                lastHoverValid = false;
                nextDelay = CAPTURE_RETRY_SLOW_MS;
            } else {
                if (view.hasWindowFocus()) {
                    try {
                        view.requestPointerCapture();
                    } catch (Throwable ignored) {
                    }
                }
                captureRetryCount++;
                if (captureRetryCount > CAPTURE_MAX_FAST_RETRIES) {
                    hoverFallbackEnabled = true;
                    nextDelay = CAPTURE_RETRY_SLOW_MS;
                } else {
                    nextDelay = CAPTURE_RETRY_FAST_MS;
                }
            }
            mainHandler.postDelayed(this, nextDelay);
        }
    };

    public boolean handleExternalMouseEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS || event.getActionMasked() == MotionEvent.ACTION_BUTTON_RELEASE) {
            boolean press = event.getActionMasked() == MotionEvent.ACTION_BUTTON_PRESS;
            if (event.getActionButton() == MotionEvent.BUTTON_PRIMARY) {
                sendKeyEvent(MOUSE_LEFT, press);
            } else if (event.getActionButton() == MotionEvent.BUTTON_SECONDARY) {
                sendKeyEvent(MOUSE_RIGHT, press);
            } else if (event.getActionButton() == MotionEvent.BUTTON_TERTIARY) {
                sendKeyEvent(MOUSE_MIDDLE, press);
            }
        } else if (event.getActionMasked() == MotionEvent.ACTION_SCROLL) {
            for (int i = 0; i < Math.abs((int) event.getAxisValue(MotionEvent.AXIS_VSCROLL)); i++) {
                sendKeyEvent(event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0 ? MOUSE_SCROLL_UP : MOUSE_SCROLL_DOWN, true);
            }
        }
        return true;
    }

    @Override
    public boolean onCapturedPointer(View view, MotionEvent event) {
        return handleMouse(event, 0);
    }

    private boolean handleMouse(MotionEvent event, float deltaTimeScale) {
        if (event == null || event.getAction() == MotionEvent.ACTION_MOVE) {
            double deltaX;
            double deltaY;
            if (event != null) {
                double tX = event.getX();
                double tY = event.getY();
                final int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    tX += event.getHistoricalX(i);
                    tY += event.getHistoricalY(i);
                }
                deltaX = tX * menu.getMenuSetting().getMouseSensitivity();
                deltaY = tY * menu.getMenuSetting().getMouseSensitivity();
            } else {
                deltaX = lastAxisZ * deltaTimeScale * 10 * menu.getMenuSetting().getMouseSensitivity();
                deltaY = lastAxisRZ * deltaTimeScale * 10 * menu.getMenuSetting().getMouseSensitivity();
            }
            if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
                float targetX = (float) Math.max(0, Math.min(screenWidth, menu.getCursorX() + deltaX * menu.getMenuSetting().getMouseSensitivityCursor()));
                float targetY = (float) Math.max(0, Math.min(screenHeight, menu.getCursorY() + deltaY * menu.getMenuSetting().getMouseSensitivityCursor()));
                setPointerId(EXTERNAL_MOUSE_ID);
                setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
                setPointerId(null);
            } else {
                // 捕获态统一走相对增量流，与陀螺仪等来源的增量叠加互不干扰
                sendLookDelta((float) deltaX, (float) deltaY);
            }
        }
        if (event != null) {
            return handleExternalMouseEvent(event);
        }
        return false;
    }

    /** 捕获态视角增量统一出口：屏幕坐标系差值换算到游戏坐标系后走相对增量流 */
    private void sendLookDelta(float deltaX, float deltaY) {
        if (menu.getBridge() == null) {
            return;
        }
        // 强制分辨率下游戏窗口与 windowScale 无关，外置鼠标增量按 1:1 下发，由 pushEventLookDelta 统一除拉伸系数
        float scaleFactor = FCLBridge.FORCE_RESOLUTION ? 1f : (float) menu.getBridge().getScaleFactor();
        FCLBridge.pushEventLookDelta(deltaX * scaleFactor, deltaY * scaleFactor);
    }

    /** 悬停降级路径的增量应用：菜单态移动光标，捕获态转发视角增量 */
    private void applyPointerDelta(float deltaX, float deltaY) {
        if (menu.getCursorMode() == FCLBridge.CursorEnabled) {
            float targetX = (float) Math.max(0, Math.min(screenWidth, menu.getCursorX() + deltaX * menu.getMenuSetting().getMouseSensitivityCursor()));
            float targetY = (float) Math.max(0, Math.min(screenHeight, menu.getCursorY() + deltaY * menu.getMenuSetting().getMouseSensitivityCursor()));
            setPointerId(EXTERNAL_MOUSE_ID);
            setPointer(targetX, targetY, EXTERNAL_MOUSE_ID);
            setPointerId(null);
        } else {
            sendLookDelta(deltaX, deltaY);
        }
    }


    public boolean handleKeyEvent(KeyEvent event) {
        // 快捷键绑定监听中：吞掉所有按键交给 GameMenu 记录，BACK 取消
        if (menu.isKeyBindListening()) {
            menu.handleKeyBindCaptured(event);
            return true;
        }
        int fclKeycode = AndroidKeycodeMap.convertKeycode(event.getKeyCode());
        if (event.getKeyCode() == KeyEvent.KEYCODE_UNKNOWN || event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
            return false;
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getFlags() & KeyEvent.FLAG_CANCELED) != 0)
            return true;
        //mouse button right：部分 ROM/模拟器在 InputReader 层把鼠标右键转换成 BACK，
        //来源标记也可能被改写成纯键盘，回查设备能力兜底
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && isMouseOriginatedEvent(event)) {
            sendKeyEvent(MOUSE_RIGHT, event.getAction() == KeyEvent.ACTION_DOWN);
            return true;
        }
        //soft keyboard enter
        if ((event.getFlags() & KeyEvent.FLAG_SOFT_KEYBOARD) == KeyEvent.FLAG_SOFT_KEYBOARD) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                return true;
            menu.getTouchCharInput().dispatchKeyEvent(event);
            return true;
        }
        // 自定义快捷键（右菜单鼠标页可配）：指针捕获切换 / 输入法呼出，在转发游戏前拦截。
        // 匹配后按下与抬起须成对吞掉：只拦抬起而把按下转发给游戏，游戏收不到对应释放而卡键
        boolean captureHotkey = matchesHotkey(event, menu.getMenuSetting().getCapturePointerKey(),
                menu.getMenuSetting().getCapturePointerModifier());
        boolean imeHotkey = matchesHotkey(event, menu.getMenuSetting().getImeToggleKey(),
                menu.getMenuSetting().getImeToggleModifier());
        if (captureHotkey || imeHotkey) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (captureHotkey) {
                    togglePointerCapture();
                } else {
                    menu.getTouchCharInput().switchKeyboardState();
                    // shift 修饰呼出软键盘后复位游戏内的 shift 按住状态，避免潜行残留
                    if (menu.getMenuSetting().getImeToggleModifier() == MenuSetting.HOTKEY_MOD_SHIFT) {
                        sendKeyEvent(FCLKeycodes.KEY_RIGHTSHIFT, false);
                    }
                }
            }
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                menu.getTouchCharInput().hide();
            }
            return true;
        }

        //gamepad
        if (menu.isGamepadControl() && Gamepad.isGamepadEvent(event)) {
            // 首次手柄输入时弹窗选择输入模式，确认前吞掉手柄输入
            if (GamepadModePromptDialog.checkAndShow(menu.getActivity())) {
                return true;
            }
            checkGamepad();
            // SDL 直通模式：原始事件交给 SDL 手柄子系统；SDL 未就绪时也吞掉，
            // 不落入映射层，避免直通与映射同时响应造成双重操作
            if (SdlSettings.getGamepadInputMode().getValue() == GamepadInputMode.SDL_DIRECT) {
                if (SdlBridge.getSdlEnabled()) {
                    return SDLActivity.handleKeyEvent(null, event.getKeyCode(), event, null);
                }
                return true;
            }
            return gamepad.handleKeyEvent(event);
        }
        //keyboard
        if (fclKeycode == FCLKeycodes.KEY_UNKNOWN)
            return (event.getFlags() & KeyEvent.FLAG_FALLBACK) == KeyEvent.FLAG_FALLBACK;
        boolean press = event.getAction() == KeyEvent.ACTION_DOWN;
        // 物理键盘字符取自设备布局，随 keydown 一次性成对下发；不能再调 sendChar——
        // 那是软键盘纯字符通道，会反查键码补发一对 keydown/keyup，与本按键叠加成双重输入
        int keyChar = press && menu.getCursorMode() == FCLBridge.CursorEnabled ? event.getUnicodeChar() : 0;
        sendKeyEvent(fclKeycode, keyChar, press);
        return true;
    }

    /**
     * 快捷键匹配：事件键码经 AndroidKeycodeMap 换算后与配置一致，
     * 修饰键按 metaState 精确匹配（不允许携带其它修饰键）
     */
    private boolean matchesHotkey(KeyEvent event, int fclKeycode, int modifier) {
        if (fclKeycode == 0 || fclKeycode == FCLKeycodes.KEY_UNKNOWN) {
            return false;
        }
        if (AndroidKeycodeMap.convertKeycode(event.getKeyCode()) != fclKeycode) {
            return false;
        }
        int meta;
        switch (modifier) {
            case MenuSetting.HOTKEY_MOD_SHIFT:
                meta = KeyEvent.META_SHIFT_ON;
                break;
            case MenuSetting.HOTKEY_MOD_CTRL:
                meta = KeyEvent.META_CTRL_ON;
                break;
            case MenuSetting.HOTKEY_MOD_ALT:
                meta = KeyEvent.META_ALT_ON;
                break;
            default:
                meta = 0;
                break;
        }
        return KeyEvent.metaStateHasModifiers(event.getMetaState(), meta);
    }

    /**
     * 主动切换 Android 指针捕获（不同步游戏的 grab 状态）：已捕获则释放，未捕获则请求。
     * 切换后进入手动接管，看门狗与自动捕获路径维持该意图，游戏光标模式变化时回归自动
     */
    public void togglePointerCapture() {
        View view = focusableView;
        if (view == null) {
            return;
        }
        if (view.hasPointerCapture()) {
            manualCaptureIntent = MANUAL_CAPTURE_RELEASE;
            view.releasePointerCapture();
        } else {
            manualCaptureIntent = MANUAL_CAPTURE_REQUEST;
            try {
                view.requestPointerCapture();
            } catch (Throwable ignored) {
            }
        }
    }

    /** 游戏光标模式变化（进入/退出视角捕获）后回归自动捕获管理 */
    public void resetManualCaptureControl() {
        manualCaptureIntent = MANUAL_CAPTURE_AUTO;
    }

    private boolean isMouseOriginatedEvent(KeyEvent event) {
        int source = event.getSource();
        if ((source & InputDevice.SOURCE_MOUSE_RELATIVE) != 0
                || (source & InputDevice.SOURCE_MOUSE) != 0) {
            return true;
        }
        // 仅对 BACK 键回查：设备带鼠标能力且不带键盘能力时（键鼠套装的键盘仍按键盘处理），
        // 该 BACK 大概率是系统由右键转换而来
        InputDevice device = event.getDevice();
        if (device == null) {
            return false;
        }
        int sources = device.getSources();
        return (sources & InputDevice.SOURCE_MOUSE) != 0
                && (sources & InputDevice.SOURCE_KEYBOARD) == 0;
    }

    public boolean handleGenericMotionEvent(MotionEvent event) {
        if (menu.isGamepadControl() && Gamepad.isGamepadEvent(event)) {
            // 首次手柄输入时弹窗选择输入模式，确认前吞掉手柄输入
            if (GamepadModePromptDialog.checkAndShow(menu.getActivity())) {
                return true;
            }
            checkGamepad();
            // SDL 直通模式：原始摇杆事件交给 SDL；SDL 未就绪或转发失败时也吞掉，
            // 不落入映射层，避免直通与映射同时响应造成双重操作
            if (SdlSettings.getGamepadInputMode().getValue() == GamepadInputMode.SDL_DIRECT) {
                if (SdlBridge.getSdlEnabled()) {
                    try {
                        return SDLActivity.forwardGenericMotionToSDL(null, event);
                    } catch (Throwable ignored) {
                    }
                }
                return true;
            }
            if (choreographer == null) {
                choreographer = Choreographer.getInstance();
                Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
                    @Override
                    public void doFrame(long frameTimeNanos) {
                        doTick();
                        choreographer.postFrameCallback(this);
                    }
                };
                choreographer.postFrameCallback(frameCallback);
            }
            return gamepad.handleMotionEventInput(event);
        } else if (event.isFromSource(InputDevice.SOURCE_MOUSE)) {
            int action = event.getActionMasked();
            // 鼠标按键统一在入口层处理，不依赖事件命中的控件；捕获路径已投递的
            // 部分由 isDuplicateMouseButtonEvent 去重
            if (action == MotionEvent.ACTION_BUTTON_PRESS || action == MotionEvent.ACTION_BUTTON_RELEASE) {
                return handleExternalMouseEvent(event);
            }
            if (action == MotionEvent.ACTION_SCROLL) {
                for (int i = 0; i < Math.abs((int) event.getAxisValue(MotionEvent.AXIS_VSCROLL)); i++) {
                    sendKeyEvent(event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0 ? MOUSE_SCROLL_UP : MOUSE_SCROLL_DOWN, true);
                }
                return true;
            }
            if (action == MotionEvent.ACTION_HOVER_MOVE) {
                if (menu.getMenuSetting().isPhysicalMouseMode() && menu.getCursorMode() == FCLBridge.CursorEnabled) {
                    // 实体鼠标模式：系统指针的绝对坐标直接驱动光标
                    setPointer(event.getRawX(), event.getRawY());
                    return true;
                }
                if (hoverFallbackEnabled) {
                    applyHoverFallbackDelta(event);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指针捕获持续失败的设备上以悬停坐标差值近似相对位移；
     * 捕获一旦恢复，悬停事件不再产生，无需额外切换
     */
    private void applyHoverFallbackDelta(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        if (!lastHoverValid) {
            lastHoverRawX = rawX;
            lastHoverRawY = rawY;
            lastHoverValid = true;
            return;
        }
        // 增量全程 float 直发，不做逐事件取整：慢移的亚像素位移不丢失
        float deltaX = (float) ((rawX - lastHoverRawX) * menu.getMenuSetting().getMouseSensitivity());
        float deltaY = (float) ((rawY - lastHoverRawY) * menu.getMenuSetting().getMouseSensitivity());
        lastHoverRawX = rawX;
        lastHoverRawY = rawY;
        if (deltaX != 0 || deltaY != 0) {
            applyPointerDelta(deltaX, deltaY);
        }
    }

    /**
     * 未捕获的外接鼠标点击以普通 touch 事件到达（没有 BUTTON_PRESS），按 buttonState
     * 的变化补齐按键投递；与系统转换出的 KeyEvent 共用 {@link #isDuplicateMouseButtonEvent} 去重，
     * 双路同时到达的设备只生效一次
     */
    public void handleExternalTouchButtons(MotionEvent event) {
        int action = event.getActionMasked();
        if (action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP
                && action != MotionEvent.ACTION_CANCEL && action != MotionEvent.ACTION_POINTER_DOWN
                && action != MotionEvent.ACTION_POINTER_UP) {
            return;
        }
        int buttonState = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                ? 0 : event.getButtonState();
        int changed = lastExternalMouseButtons ^ buttonState;
        lastExternalMouseButtons = buttonState;
        if ((changed & MotionEvent.BUTTON_PRIMARY) != 0) {
            sendKeyEvent(MOUSE_LEFT, (buttonState & MotionEvent.BUTTON_PRIMARY) != 0);
        }
        if ((changed & MotionEvent.BUTTON_SECONDARY) != 0) {
            sendKeyEvent(MOUSE_RIGHT, (buttonState & MotionEvent.BUTTON_SECONDARY) != 0);
        }
        if ((changed & MotionEvent.BUTTON_TERTIARY) != 0) {
            sendKeyEvent(MOUSE_MIDDLE, (buttonState & MotionEvent.BUTTON_TERTIARY) != 0);
        }
    }

    public void handleLeftJoyStick(float xAxis, float yAxis) {
        double dist = Math.hypot(Math.abs(xAxis), Math.abs(yAxis));
        if (dist >= menu.getMenuSetting().getGamepadDeadzone()) {
            double degrees = Math.toDegrees(-Math.atan2(yAxis, xAxis));
            if (degrees < 0) {
                degrees += 360;
            }
            int lastDirection = currentDirection;
            currentDirection = ((int) ((degrees + 22.5) / 45)) % 8;
            sendDirection(lastDirection, false);
            sendDirection(currentDirection, true);
        } else {
            sendDirection(0, false);
            sendDirection(2, false);
            sendDirection(4, false);
            sendDirection(6, false);
        }
    }

    private void sendDirection(int direction, boolean press) {
        switch (direction) {
            case 0:
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                break;
            case 1:
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                break;
            case 2:
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                break;
            case 3:
                gamepad.getCurrentMap().DIRECTION_FORWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 4:
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 5:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_LEFT.update(press);
                break;
            case 6:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                break;
            case 7:
                gamepad.getCurrentMap().DIRECTION_BACKWARD.update(press);
                gamepad.getCurrentMap().DIRECTION_RIGHT.update(press);
                break;
        }
    }

    public void handleRightJoyStick(float axisZ, float axisRZ) {
        double dist = Math.hypot(Math.abs(axisZ), Math.abs(axisRZ));
        if (dist < menu.getMenuSetting().getGamepadDeadzone()) {
            lastAxisZ = 0;
            lastAxisRZ = 0;
            return;
        }
        if (lastAxisZ != axisZ || lastAxisRZ != axisRZ) {
            lastAxisZ = axisZ;
            lastAxisRZ = axisRZ;
            doTick();
        }
    }

    private void doTick() {
        long newFrameTime = System.nanoTime();
        if (lastAxisZ != 0 || lastAxisRZ != 0) {
            newFrameTime = System.nanoTime();
            float deltaTimeScale = ((newFrameTime - lastFrameTime) / 16666666f);
            handleMouse(null, deltaTimeScale);
        }
        lastFrameTime = newFrameTime;
    }

    public void resetMapper() {
        if (gamepad != null)
            gamepad.resetMapper();
    }

    public void checkGamepad() {
        if (gamepad == null) {
            gamepad = new Gamepad(menu.getActivity(), this);
        }
    }

    public Gamepad getGamepad() {
        return gamepad;
    }
}
