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

    private SdlImeController() {
    }

    static boolean isTextInputActive() {
        return mTextInputActive;
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
        post(SdlImeController::doHide);
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

        // SDL 文本输入通道关闭后，任何启动器侧请求都不允许唤起对游戏打字的软键盘
        if (source != Source.GAME && !mTextInputActive) {
            Log.w(TAG, "IME: show by " + source + " rejected, SDL text input channel is closed");
            forceHideIme();
            return;
        }
        if (source == Source.GAME) {
            TouchCharInput.disableActiveInput();
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

    private static void doHide() {
        if (mEdit == null) {
            Log.i(TAG, "IME: no text edit available, hide ignored");
            SdlBridge.requestComposeFocus();
            return;
        }
        forceHideIme();
        if (mKeyboardShown) {
            mKeyboardShown = false;
            Log.i(TAG, "IME: hidden");
            SDLActivity.onNativeScreenKeyboardHidden();
        }

        if (!mTextInputActive) {
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
        SdlBridge.requestComposeFocus();
    }

    private static boolean isUnwantedImeVisible() {
        if (!SdlBridge.getSdlEnabled() || mTextInputActive) {
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