package com.tungsten.fcl.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclauncher.keycodes.MinecraftKeyBindingMapper;
import com.tungsten.fclcore.task.Schedulers;

public class GameItemBar extends RelativeLayout {

    private static final String BINDING_HOTBAR_1 = "key_key.hotbar.1";
    private static final String BINDING_HOTBAR_2 = "key_key.hotbar.2";
    private static final String BINDING_HOTBAR_3 = "key_key.hotbar.3";
    private static final String BINDING_HOTBAR_4 = "key_key.hotbar.4";
    private static final String BINDING_HOTBAR_5 = "key_key.hotbar.5";
    private static final String BINDING_HOTBAR_6 = "key_key.hotbar.6";
    private static final String BINDING_HOTBAR_7 = "key_key.hotbar.7";
    private static final String BINDING_HOTBAR_8 = "key_key.hotbar.8";
    private static final String BINDING_HOTBAR_9 = "key_key.hotbar.9";

    public GameItemBar(Context context) {
        super(context);
        init();
    }

    public GameItemBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameItemBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
    }

    public void notifySize(int size) {
        Schedulers.androidUIThread().execute(() -> {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = size * 9;
            params.height = size;
            setLayoutParams(params);
        });
    }

    private GameMenu gameMenu;
    private GameOption gameOption;
    private GameOption.GameOptionListener optionListener;
    private int position = 0;

    public void setup(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
        assert gameMenu.getBridge() != null;
        gameOption = new GameOption(gameMenu.getBridge().getGameDir());
        int width = (int) (gameMenu.getTouchPad().getWidth() * gameMenu.getBridge().getScaleFactor());
        int height = (int) (gameMenu.getTouchPad().getHeight() * gameMenu.getBridge().getScaleFactor());
        notifySize(gameOption.getGuiScale(width, height, gameMenu.getMenuSetting().getItemBarScale()) * 20);
        optionListener = () -> notifySize(gameOption.getGuiScale(width, height, gameMenu.getMenuSetting().getItemBarScale()) * 20);
        gameOption.addGameOptionListener(optionListener);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameMenu != null) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getX() >= 0 && event.getX() < getHeight()) {
                        position = 1;
                        sendKeycode(BINDING_HOTBAR_1, FCLKeycodes.KEY_1, true);
                    } else if (event.getX() >= getHeight() && event.getX() < 2 * getHeight()) {
                        position = 2;
                        sendKeycode(BINDING_HOTBAR_2, FCLKeycodes.KEY_2, true);
                    } else if (event.getX() >= 2 * getHeight() && event.getX() < 3 * getHeight()) {
                        position = 3;
                        sendKeycode(BINDING_HOTBAR_3, FCLKeycodes.KEY_3, true);
                    } else if (event.getX() >= 3 * getHeight() && event.getX() < 4 * getHeight()) {
                        position = 4;
                        sendKeycode(BINDING_HOTBAR_4, FCLKeycodes.KEY_4, true);
                    } else if (event.getX() >= 4 * getHeight() && event.getX() < 5 * getHeight()) {
                        position = 5;
                        sendKeycode(BINDING_HOTBAR_5, FCLKeycodes.KEY_5, true);
                    } else if (event.getX() >= 5 * getHeight() && event.getX() < 6 * getHeight()) {
                        position = 6;
                        sendKeycode(BINDING_HOTBAR_6, FCLKeycodes.KEY_6, true);
                    } else if (event.getX() >= 6 * getHeight() && event.getX() < 7 * getHeight()) {
                        position = 7;
                        sendKeycode(BINDING_HOTBAR_7, FCLKeycodes.KEY_7, true);
                    } else if (event.getX() >= 7 * getHeight() && event.getX() < 8 * getHeight()) {
                        position = 8;
                        sendKeycode(BINDING_HOTBAR_8, FCLKeycodes.KEY_8, true);
                    } else if (event.getX() >= 8 * getHeight() && event.getX() <= 9 * getHeight()) {
                        position = 9;
                        sendKeycode(BINDING_HOTBAR_9, FCLKeycodes.KEY_9, true);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    switch (position) {
                        case 1:
                            sendKeycode(BINDING_HOTBAR_1, FCLKeycodes.KEY_1, false);
                            break;
                        case 2:
                            sendKeycode(BINDING_HOTBAR_2, FCLKeycodes.KEY_2, false);
                            break;
                        case 3:
                            sendKeycode(BINDING_HOTBAR_3, FCLKeycodes.KEY_3, false);
                            break;
                        case 4:
                            sendKeycode(BINDING_HOTBAR_4, FCLKeycodes.KEY_4, false);
                            break;
                        case 5:
                            sendKeycode(BINDING_HOTBAR_5, FCLKeycodes.KEY_5, false);
                            break;
                        case 6:
                            sendKeycode(BINDING_HOTBAR_6, FCLKeycodes.KEY_6, false);
                            break;
                        case 7:
                            sendKeycode(BINDING_HOTBAR_7, FCLKeycodes.KEY_7, false);
                            break;
                        case 8:
                            sendKeycode(BINDING_HOTBAR_8, FCLKeycodes.KEY_8, false);
                            break;
                        case 9:
                            sendKeycode(BINDING_HOTBAR_9, FCLKeycodes.KEY_9, false);
                            break;
                        default:
                            break;
                    }
                    position = 0;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void sendKeycode(String key, int defaultKeycode, boolean press) {
        int keycode = mapBindingToKeycode(key, defaultKeycode);
        gameMenu.getInput().sendKeyEvent(keycode, press);
    }

    private int mapBindingToKeycode(String key, int defaultKeycode) {
        if (key == null) return defaultKeycode;

        String binding = gameOption.get(key);
        if (binding == null) return defaultKeycode;

        if (binding.startsWith("key.")) {
            //新版MC键绑定映射
            Short glfwKeycode = MinecraftKeyBindingMapper.getGlfwKeycode(binding);
            if (glfwKeycode == null) return defaultKeycode;
            return (int) glfwKeycode;
        } else {
            int lwjgl2Keycode;
            try {
                //MC旧版本直接存了LWJGL2的键值
                lwjgl2Keycode = Integer.parseInt(binding);
            } catch (NumberFormatException e) {
                return defaultKeycode;
            }
            return lwjgl2Keycode;
        }
    }

    public GameOption.GameOptionListener getOptionListener() {
        return optionListener;
    }
}
