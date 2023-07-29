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
import com.tungsten.fclcore.task.Schedulers;

public class GameItemBar extends RelativeLayout {

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
        notifySize(gameOption.getGuiScale(width, height) * 20);
        optionListener = () -> notifySize(gameOption.getGuiScale(width, height) * 20);
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
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_1, true);
                    } else if (event.getX() >= getHeight() && event.getX() < 2 * getHeight()) {
                        position = 2;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_2, true);
                    } else if (event.getX() >= 2 * getHeight() && event.getX() < 3 * getHeight()) {
                        position = 3;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_3, true);
                    } else if (event.getX() >= 3 * getHeight() && event.getX() < 4 * getHeight()) {
                        position = 4;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_4, true);
                    } else if (event.getX() >= 4 * getHeight() && event.getX() < 5 * getHeight()) {
                        position = 5;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_5, true);
                    } else if (event.getX() >= 5 * getHeight() && event.getX() < 6 * getHeight()) {
                        position = 6;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_6, true);
                    } else if (event.getX() >= 6 * getHeight() && event.getX() < 7 * getHeight()) {
                        position = 7;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_7, true);
                    } else if (event.getX() >= 7 * getHeight() && event.getX() < 8 * getHeight()) {
                        position = 8;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_8, true);
                    } else if (event.getX() >= 8 * getHeight() && event.getX() <= 9 * getHeight()) {
                        position = 9;
                        gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_9, true);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    switch (position) {
                        case 1:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_1, false);
                            break;
                        case 2:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_2, false);
                            break;
                        case 3:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_3, false);
                            break;
                        case 4:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_4, false);
                            break;
                        case 5:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_5, false);
                            break;
                        case 6:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_6, false);
                            break;
                        case 7:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_7, false);
                            break;
                        case 8:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_8, false);
                            break;
                        case 9:
                            gameMenu.getInput().sendKeyEvent(FCLKeycodes.KEY_9, false);
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
}
