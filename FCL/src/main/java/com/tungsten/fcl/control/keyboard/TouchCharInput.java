package com.tungsten.fcl.control.keyboard;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.control.GameMenu;

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

    /**
     * Toggle on and off the soft keyboard, depending of the state
     */
    public void switchKeyboardState() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (hasFocus()) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            clear();
            disable();
            if (menu != null && menu.getInput().getFocusableView() != null) {
                if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                    menu.getInput().getFocusableView().requestFocus();
                    menu.getInput().getFocusableView().requestPointerCapture();
                }
            }
        } else {
            if (menu != null && menu.getInput().getFocusableView() != null) {
                if (!menu.getMenuSetting().isPhysicalMouseMode()) {
                    menu.getInput().getFocusableView().releasePointerCapture();
                    menu.getInput().getFocusableView().clearFocus();
                }
            }
            enable();
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
    }

    /**
     * Lose ability to exist, take focus and have some text being input
     */
    public void disable() {
        clear();
        setVisibility(GONE);
        clearFocus();
        setEnabled(false);
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