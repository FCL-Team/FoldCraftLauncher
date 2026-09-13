/*
 * Zalith Launcher 2
 * Copyright (C) 2025 MovTery <movtery228@qq.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */

package org.libsdl.app;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_NORMAL;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.tungsten.fcl.game.sdl.SdlBridge;
import com.tungsten.fcl.control.keyboard.TouchCharInput;

/**
 * SDL 侧软键盘的显式控制器
 */
final class SdlImeController {
    enum Source { GAME, LAUNCHER, BACK }

    private static final String TAG = "SDLImeController";
    private static final int HEIGHT_PADDING = 15;

    private static SDLDummyEdit mEdit;
    private static boolean mTextInputActive;
    private static boolean mKeyboardShown;
    // 游戏侧通道关闭时由启动器显式唤起输入法代开的 native 文本输入通道
    private static boolean mForcedByLauncher;

    private SdlImeController() {
    }

    static boolean isTextInputActive() {
        return mTextInputActive;
    }

    /** 文本输入通道是否可接收输入（含启动器代开的通道） */
    static boolean isInputAccepted() {
        return mTextInputActive || mForcedByLauncher;
    }

    static boolean isEditAvailable() {
        return mEdit != null;
    }

    static boolean isKeyboardShown() {
        return mKeyboardShown;
    }

    static void reset() {
        if (mEdit != null) {
            ViewParent parent = mEdit.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mEdit);
            }
            mEdit = null;
        }
        mTextInputActive = false;
        mKeyboardShown = false;
        mForcedByLauncher = false;
    }

    static void requestShow(Source source) {
        requestShow(source, TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_NORMAL, -1, -1, -1, -1);
    }

    static boolean requestShow(Source source, int inputType, int x, int y, int w, int h) {
        Log.i(TAG, "IME: show requested by " + source);
        if (source == Source.GAME) {
            mTextInputActive = true;
        }
        return post(() -> doShow(source, inputType, x, y, w, h));
    }

    static void requestHide(Source source) {
        Log.i(TAG, "IME: hide requested by " + source);
        if (source == Source.GAME) {
            mTextInputActive = false;
        }
        post(() -> doHide(source));
    }

    /**
     * 系统 insets 汇报的 IME 可见性
     * @param visible IME 可见性
     */
    static void notifyVisibilityChanged(boolean visible) {
        if (!SdlBridge.getSdlEnabled()) return;
        if (visible && isUnwantedImeVisible()) {
            // IME 在通道关闭后自行弹出时强制按回
            Log.w(TAG, "IME: unwanted visibility while text input channel is closed, forcing hide");
            forceHideIme();
            return;
        }
        if (mKeyboardShown == visible) {
            return;
        }
        mKeyboardShown = visible;
        Log.i(TAG, "IME: visibility changed to " + (visible ? "shown" : "hidden"));
        if (visible) {
            SDLActivity.onNativeScreenKeyboardShown();
        } else {
            SDLActivity.onNativeScreenKeyboardHidden();
        }
    }

    private static boolean post(Runnable task) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            task.run();
            return true;
        }
        return SDLActivity.commandHandler.post(task);
    }

    private static void doShow(Source source, int inputType, int x, int y, int w, int h) {
        if (SDLActivity.mLayout == null) {
            Log.w(TAG, "IME: no layout available, show by " + source + " ignored");
            return;
        }

        if (source == Source.GAME) {
            TouchCharInput.disableActiveInput();
        } else if (!mTextInputActive && !mForcedByLauncher) {
            // 游戏侧文本输入通道关闭（如模组自绘输入界面会主动关闭通道）时，
            // 启动器显式唤起输入法需代为激活 native 通道，否则输入文本无法送达游戏
            if (!SdlBridge.setNativeTextInputActive(true)) {
                Log.w(TAG, "IME: show by " + source + " rejected, native text input unavailable");
                return;
            }
            mForcedByLauncher = true;
            Log.i(TAG, "IME: native text input force-activated by " + source);
        }

        // 自动弹出被关闭时延迟落编辑视图，但焦点落在隐藏编辑器上会让后续实体键盘输入触发软键盘
        boolean autoShow = source != Source.GAME || SdlBridge.getSdlImeAutoShowEnabled();
        if (!autoShow && mEdit == null) {
            Log.i(TAG, "IME: auto show suppressed by launcher setting, editor deferred");
            return;
        }

        if (mEdit == null) {
            mEdit = new SDLDummyEdit(SDLActivity.getContext());
            SDLActivity.mLayout.addView(mEdit, makeParams(x, y, w, h));
        } else if (x >= 0 && w > 0) {
            // 仅显式提供区域时更新位置，避免启动器请求覆盖游戏设置的输入框。
            // setLayoutParams 不做类型转换：复用 addView 时已转换的父容器 params
            // （FCL 游戏布局根为 RelativeLayout，硬编码 FrameLayout.LayoutParams 会强转崩溃）
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mEdit.getLayoutParams();
            params.width = w;
            params.height = h + HEIGHT_PADDING;
            params.leftMargin = x;
            params.topMargin = y;
            mEdit.setLayoutParams(params);
        }
        mEdit.setInputType(inputType);
        mEdit.setFocusable(true);
        mEdit.setFocusableInTouchMode(true);

        mEdit.setVisibility(View.VISIBLE);
        if (!mEdit.hasFocus()) {
            mEdit.requestFocus();
        }

        if (mKeyboardShown) {
            Log.i(TAG, "IME: already visible, show by " + source + " ignored");
            return;
        }
        if (!autoShow) {
            Log.i(TAG, "IME: auto show suppressed by launcher setting");
            return;
        }

        InputMethodManager imm = (InputMethodManager) SDLActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEdit, 0);
        if (imm.isAcceptingText()) {
            mKeyboardShown = true;
            Log.i(TAG, "IME: shown by " + source);
            SDLActivity.onNativeScreenKeyboardShown();
        }
    }

    private static FrameLayout.LayoutParams makeParams(int x, int y, int w, int h) {
        if (x < 0 || w <= 0) {
            x = 0;
            y = 0;
            w = 1;
            h = 1;
        }
        if (h + HEIGHT_PADDING <= 0) {
            h = 1 - HEIGHT_PADDING;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h + HEIGHT_PADDING);
        params.leftMargin = x;
        params.topMargin = y;
        return params;
    }

    private static void doHide(Source source) {
        if (mForcedByLauncher && source != Source.GAME) {
            // 还原启动器代开的 native 通道（游戏自行关闭时已无需重复操作）
            SdlBridge.setNativeTextInputActive(false);
        }
        mForcedByLauncher = false;

        if (mEdit == null) {
            Log.i(TAG, "IME: no text edit available, hide ignored");
            return;
        }
        forceHideIme();
        if (mKeyboardShown) {
            mKeyboardShown = false;
            Log.i(TAG, "IME: hidden");
            SDLActivity.onNativeScreenKeyboardHidden();
        }

        if (!isInputAccepted()) {
            // 部分 IME 会在隐藏后延迟回弹，通道关闭时追加一次压制
            SDLActivity.commandHandler.postDelayed(SdlImeController::recheckHidden, 300);
        }
    }

    private static void forceHideIme() {
        if (mEdit != null) {
            InputMethodManager imm = (InputMethodManager) SDLActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mEdit.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(mEdit.getWindowToken(), 0);
            }
            ViewParent parent = mEdit.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mEdit);
            }
            mEdit = null;
            Log.i(TAG, "IME: text edit removed from view tree");
        }
        ViewGroup layout = SDLActivity.mLayout;
        if (layout != null && layout.getWindowToken() != null) {
            //窗口级兜底：顽固 IME 无视移除强行回弹时按回
            InputMethodManager imm = (InputMethodManager) SDLActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        }
    }

    private static boolean isUnwantedImeVisible() {
        if (!SdlBridge.getSdlEnabled() || isInputAccepted()) {
            return false;
        }
        return mEdit != null && mEdit.hasFocus();
    }

    private static void recheckHidden() {
        if (mTextInputActive || mKeyboardShown) {
            return;
        }
        if (!isUnwantedImeVisible()) {
            return;
        }
        Log.i(TAG, "IME: re-hide to suppress stubborn IME");
        forceHideIme();
    }
}