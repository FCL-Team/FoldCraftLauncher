package com.tungsten.fcl.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mio.customcontrol.MioCrossingKeyboard;
import com.mio.customcontrol.MioCustomButton;
import com.mio.customcontrol.MioCustomManager;
import com.mio.customcontrol.MioCustomUtils;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fclauncher.FCLKeycodes;
import com.tungsten.fclauncher.bridge.FCLBridge;

import java.io.File;

public class MioMainCustom implements MioCrossingKeyboard.MioListener, MioCustomManager.自定义按键回调 {
    private static MioMainCustom INSTANCE;
    private MioCustomManager mioCustomManager;
    private MioCrossingKeyboard mioCrossingKeyboard;
    private GameMenu menu;
    private FCLBridge fclBridge;
    private static boolean isShown = false;
    private Button openKeyboard;

    private MioMainCustom() {
    }

    public static MioMainCustom getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MioMainCustom();
        }
        return INSTANCE;
    }

    public static boolean isShown() {
        return isShown;
    }

    public void init(GameMenu menu, int width, int height) {
        this.menu = menu;
        this.fclBridge = menu.getBridge();
        Context context = menu.getBaseLayout().getContext();
        mioCrossingKeyboard = new MioCrossingKeyboard(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MioCustomUtils.dip2px(context, 170), MioCustomUtils.dip2px(context, 170));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(MioCustomUtils.dip2px(context, 50), 0, 0, MioCustomUtils.dip2px(context, 50));
        menu.getBaseLayout().addView(mioCrossingKeyboard, params);
        mioCrossingKeyboard.setListener(this);
        MioCustomManager.设置屏幕高宽(height, width);
        mioCustomManager = new MioCustomManager();
        String path = menu.getBridge().getGameDir();
        if (!new File(path, "Miokey.json").exists()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL";
        }
        mioCustomManager.初始化(context, menu.getBaseLayout(), path);
        mioCustomManager.设置自定义按键回调(this);
        Button mioCustomSwitch = new Button(context);
        mioCustomSwitch.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        params = new RelativeLayout.LayoutParams(MioCustomUtils.dip2px(context, 40), MioCustomUtils.dip2px(context, 40));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        menu.getBaseLayout().addView(mioCustomSwitch, params);

        openKeyboard = new Button(context);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#00FFFFFF"));
        drawable.setCornerRadius(25);
        drawable.setStroke(1, Color.WHITE);
        openKeyboard.setBackground(drawable);
        openKeyboard.setText("键盘");
        openKeyboard.setTextColor(Color.WHITE);
        openKeyboard.setOnClickListener(v->{
            menu.getTouchCharInput().switchKeyboardState();
        });
        params = new RelativeLayout.LayoutParams(MioCustomUtils.dip2px(context, 40), MioCustomUtils.dip2px(context, 40));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        menu.getBaseLayout().addView(openKeyboard, params);

        mioCustomSwitch.setOnClickListener(v -> {
            if (mioCrossingKeyboard.getVisibility() != View.VISIBLE) {
                return;
            }
            if (!mioCustomManager.获取开关状态()) {
                mioCustomManager.自定义开关();
                mioCrossingKeyboard.自定义();
            } else {
                if (mioCustomManager.获取按键选择模式()) {
                    mioCustomManager.设置按键选择模式(false);
                    if (mioCustomManager.获取按键修改模式()) {
                        mioCustomManager.自定义按键对话框(false);
                        if (mioCustomManager.获取当前按键().get按键类型().equals("显隐控制按键")) {
                            for (String 标识 : mioCustomManager.获取当前按键().get目标按键标识()) {
                                MioCustomButton b = mioCustomManager.获取按键(标识);
                                b.setTextColor(Color.parseColor(b.get文本颜色()));
                                b.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        mioCustomManager.自定义按键对话框(true);
                    }
                } else {
                    mioCustomManager.自定义按键对话框(true);
                }
            }
        });
        mioCustomSwitch.setOnLongClickListener(v -> {
            if (mioCustomManager.获取开关状态()) {
                mioCustomManager.自定义开关();
                mioCrossingKeyboard.自定义();
            } else {
                switchVisbility();
            }
            return true;
        });
        switchVisbility();
    }

    private void switchVisbility() {
        mioCustomManager.按键显示隐藏();
        if (mioCrossingKeyboard.getVisibility() == View.VISIBLE) {
            mioCrossingKeyboard.setVisibility(View.INVISIBLE);
            openKeyboard.setVisibility(View.INVISIBLE);
        } else {
            mioCrossingKeyboard.setVisibility(View.VISIBLE);
            openKeyboard.setVisibility(View.VISIBLE);
        }
        for (ControlViewGroup viewGroup : menu.getController().viewGroups()) {
            menu.getViewManager().switchViewGroupVisibility(viewGroup);
        }
    }

    //自定义按键
    @Override
    public void 命令接收事件(String 命令) {
        input(命令);
    }

    @Override
    public void 键值接收事件(int 键值, boolean 按下) {
        fclBridge.pushEventKey(键值, 0, 按下);
    }

    @Override
    public void 控制鼠标指针移动事件(int x, int y) {
        if (menu.getCursorMode() == FCLBridge.CursorDisabled) {
            x = menu.getPointerX() + x;
            y = menu.getPointerY() + y;
            if (menu.getMenuSetting().isEnableGyroscope()) {
                menu.setPointerX(x);
                menu.setPointerY(y);
            } else {
                menu.getInput().setPointer(x, y, "miocustom");
            }
        } else {
            x = menu.getCursorX() + x;
            y = menu.getCursorY() + y;
            menu.getInput().setPointer(x, y, "miocustom");
        }
    }

    @Override
    public void 鼠标回调(int 键值, boolean 按下) {
        fclBridge.pushEventMouseButton(键值, 按下);
    }

    @Override
    public void 按下() {

    }

    @Override
    public void 抬起() {

    }

    //
    private boolean upFromCenter = false;
    private boolean upToCenter = false;
    private boolean isShift = false;

    @Override
    public void onLeftUp() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
    }

    @Override
    public void onUp() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
    }

    @Override
    public void onRightUp() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, true);
    }

    @Override
    public void onLeft() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
    }

    @Override
    public void onCenter(boolean direct) {
        if (direct) {
            isShift = true;
            fclBridge.pushEventKey(FCLKeycodes.KEY_LEFTSHIFT, 0, true);
        } else {
            upFromCenter = true;
        }
    }

    @Override
    public void onRight() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, true);
    }

    @Override
    public void onLeftDown() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
    }

    @Override
    public void onDown() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
    }

    @Override
    public void onRightDown() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, true);
    }

    @Override
    public void onSlipLeftUp() {
    }

    @Override
    public void onSlipUp() {
    }

    @Override
    public void onSlipRightUp() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_E, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_E, 0, false);
    }

    @Override
    public void onSlipLeft() {
    }

    @Override
    public void onSlipRight() {
    }

    @Override
    public void onSlipLeftDown() {
//                showKeyboard();
    }

    @Override
    public void onSlipDown() {
    }

    @Override
    public void onSlipRightDown() {
    }

    @Override
    public void onUpToCenter() {
        if (upToCenter) {
            upToCenter = false;
        } else {
            upToCenter = true;
        }
        fclBridge.pushEventKey(FCLKeycodes.KEY_SPACE, 0, upToCenter);
    }

    @Override
    public void onFingerUp() {
        fclBridge.pushEventKey(FCLKeycodes.KEY_W, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_A, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_S, 0, false);
        fclBridge.pushEventKey(FCLKeycodes.KEY_D, 0, false);
        if (upToCenter) {
            fclBridge.pushEventKey(FCLKeycodes.KEY_SPACE, 0, false);
            upToCenter = false;
        }
        if (isShift) {
            isShift = false;
            fclBridge.pushEventKey(FCLKeycodes.KEY_LEFTSHIFT, 0, false);
        }
    }

    public void input(String str) {
        fclBridge.pushEventKey(FCLKeycodes.KEY_SLASH, 0, true);
        fclBridge.pushEventKey(FCLKeycodes.KEY_SLASH, 0, false);
        new Thread(() -> {
            try {
                Thread.sleep(300);
                for (char ch : str.replace("/", "").toCharArray()) {
                    fclBridge.pushEventKey(FCLKeycodes.KEY_RESERVED, ch, true);
                    fclBridge.pushEventKey(FCLKeycodes.KEY_RESERVED, ch, false);
                    Thread.sleep(50);
                }
                fclBridge.pushEventKey(FCLKeycodes.KEY_ENTER, '\n', true);
                fclBridge.pushEventKey(FCLKeycodes.KEY_ENTER, '\n', false);
            } catch (InterruptedException e) {

            }
        }).start();
    }
}
