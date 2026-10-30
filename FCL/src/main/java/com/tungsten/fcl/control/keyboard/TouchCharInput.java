package com.tungsten.fcl.control.keyboard;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.game.sdl.SdlBridge;

import org.libsdl.app.SDLActivity;

/**
 * From PojavLauncher
 * This class is intended for sending characters used in chat via the virtual keyboard
 */
public class TouchCharInput extends androidx.appcompat.widget.AppCompatEditText {

    public static final String TEXT_FILLER = "                              ";

    public TouchCharInput(@NonNull Context context) {
        this(context, null);
    }

    public TouchCharInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    public TouchCharInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private GameMenu menu;
    private boolean isDoingInternalChanges = false;
    private CharacterSenderStrategy characterSender;

    /**
     * 键盘是否被系统真实回报为显示：呼出请求是异步生效的（软键盘动画期间布局回调仍会读到
     * ime 不可见），只有真实出现过之后的“隐藏”才认定是输入法自行收起，避免误伤呼出中的请求
     */
    private boolean imeSeenVisible = false;
    private final ViewTreeObserver.OnGlobalLayoutListener imeLayoutListener = this::syncImeVisibility;

    /**
     * We take the new chars, and send them to the game.
     * If less chars are present, remove some.
     * The text is always cleaned up.
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (isDoingInternalChanges)
            return;
        if (characterSender != null) {
            for (int i = 0; i < lengthBefore; ++i) {
                characterSender.sendBackspace();
            }

            for (int i = start, count = 0; count < lengthAfter; ++i) {
                characterSender.sendChar(text.charAt(i));
                ++count;
            }
        }

        // Reset the keyboard state
        if (text.length() < 1)
            clear();
    }

    /**
     * When we change from app to app, the keyboard gets disabled.
     * So, we disable the object
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        disable();
    }

    /**
     * Intercepts the back key to disable focus
     * Does not affect the rest of the activity.
     */
    @Override
    public boolean onKeyPreIme(final int keyCode, final KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            disable();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(imeLayoutListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(imeLayoutListener);
        super.onDetachedFromWindow();
    }

    /**
     * 随系统同步键盘显隐：通过输入法内的按键自行收起时没有任何按键事件下发到本视图，
     * 焦点也不消失，仅靠 hasFocus() 判断开关会让状态与键盘脱节（表现为再次呼出需点两次），
     * 这里在布局回调中查询键盘真实可见性并同步清理
     */
    private void syncImeVisibility() {
        if (isImeVisible()) {
            imeSeenVisible = true;
        } else if (!hasFocus()) {
            imeSeenVisible = false;
        } else if (imeSeenVisible) {
            Log.i("TouchCharInput", "IME hidden by itself, syncing keyboard state");
            imeSeenVisible = false;
            onKeyboardHidden();
        }
    }

    /**
     * 键盘真实可见性：API 30+ 查询系统 insets 真值；旧版本没有专用通道，
     * 用可视区域底部与屏幕高度之比估算（与 JVMActivity 平推同源同阈值，
     * 窗口为 adjustResize，键盘弹出时可视区域必然收缩）
     */
    @SuppressWarnings("deprecation")
    private boolean isImeVisible() {
        if (Build.VERSION.SDK_INT >= 30) {
            WindowInsets insets = getRootWindowInsets();
            return insets != null && insets.isVisible(WindowInsets.Type.ime());
        }
        View root = getRootView();
        Rect frame = new Rect();
        root.getWindowVisibleDisplayFrame(frame);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y > 0 && frame.bottom < size.y * 2 / 3;
    }

    /**
     * 键盘关闭后的统一清理：清空文本、隐藏控件，并把焦点与指针捕获还给游戏视图
     */
    private void onKeyboardHidden() {
        clear();
        disable();
        if (menu != null && menu.getInput().getFocusableView() != null) {
            if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                menu.getInput().getFocusableView().requestFocus();
                menu.getInput().getFocusableView().requestPointerCapture();
            }
        }
    }

    /**
     * Toggle on and off the soft keyboard, depending of the state
     */
    public void switchKeyboardState() {
        // 仅当游戏运行在 SDL 渲染路径（MC 26.3+）时由 SDL 输入框接管；
        // 仅手柄子系统初始化 SDL 时（如 MC 26.2 挂 Controlify）游戏输入仍走 GLFW 桥，
        // 委托给 SDL 通道只会被拒绝，须回落到启动器侧输入
        if (SdlBridge.getSdlEnabled() && SdlBridge.isSdlRenderActive()) {
            if (SDLActivity.isSDLEditKeyboardShown()) {
                SDLActivity.disableSDLEditKeyboard();
            } else {
                SDLActivity.enableSDLEditKeyboard();
            }
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (hasFocus() && isImeVisible()) {
            imeSeenVisible = false;
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            onKeyboardHidden();
        } else {
            if (menu != null && menu.getInput().getFocusableView() != null) {
                if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                    menu.getInput().getFocusableView().releasePointerCapture();
                    menu.getInput().getFocusableView().clearFocus();
                }
            }
            enable();
            // 呼出异步生效，等 insets 确认键盘出现后再武装自动收起
            imeSeenVisible = false;
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    /**
     * Clear the EditText from any leftover inputs
     * It does not affect the in-game input
     */
    @SuppressLint("SetTextI18n")
    public void clear() {
        isDoingInternalChanges = true;
        setText(TEXT_FILLER);
        setSelection(TEXT_FILLER.length());
        isDoingInternalChanges = false;
    }

    /**
     * Regain ability to exist, take focus and have some text being input
     */
    public void enable() {
        setEnabled(true);
        setFocusable(true);
        setVisibility(VISIBLE);
        requestFocus();
        sActiveInput = this;
    }

    /**
     * Lose ability to exist, take focus and have some text being input
     */
    public void disable() {
        clear();
        setVisibility(GONE);
        clearFocus();
        setEnabled(false);
        if (sActiveInput == this) {
            sActiveInput = null;
        }
    }

    // 当前激活的字符输入控件，供 SDL 输入法接管时统一关闭（见 SDLActivity）
    private static TouchCharInput sActiveInput;

    public static void disableActiveInput() {
        if (sActiveInput != null) {
            sActiveInput.disable();
        }
    }

    /**
     * Send the enter key.
     */
    private void sendEnter() {
        characterSender.sendEnter();
        clear();
    }

    /**
     * Just sets the char sender that should be used.
     */
    public void setCharacterSender(GameMenu gameMenu, CharacterSenderStrategy characterSender) {
        this.menu = gameMenu;
        this.characterSender = characterSender;
    }

    /**
     * This function deals with anything that has to be executed when the constructor is called
     */
    private void setup() {
        setOnEditorActionListener((textView, i, keyEvent) -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            sendEnter();
            clear();
            disable();
            if (menu != null && menu.getInput().getFocusableView() != null) {
                if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                    menu.getInput().getFocusableView().requestFocus();
                    menu.getInput().getFocusableView().requestPointerCapture();
                }
            }
            return false;
        });
        clear();
        disable();
    }

    public void hide() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
        sendEnter();
        clear();
        disable();
    }

}