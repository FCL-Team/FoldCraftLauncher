package com.tungsten.fcl.control;

import static android.content.Context.DISPLAY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.Surface;

import com.tungsten.fclauncher.bridge.FCLBridge;

import org.lwjgl.glfw.CallbackBridge;

import java.util.Arrays;

/**
 * 陀螺仪转视角：仅在游戏捕获指针（转视角）时生效，把角速度转为相对增量下发。
 * 数据经滑动窗口平均抑制手抖，低于死区的微小移动视为传感器噪声忽略；
 * 轴映射随屏幕旋转自适应，X/Y 轴可独立反转。
 */
public class Gyroscope implements SensorEventListener {

    // 角速度积分的时间系数：沿用历史版本标定值（并非真实纳秒到秒换算，含既有手感放大倍率）
    private static final float TIME_SCALE = 1.0f / 40000000.0f;
    // 滑动平均窗口长度，越大越平滑但响应越迟
    private static final int SMOOTHING_WINDOW = 4;
    // 死区阈值（rad/s），低于该值的角速度不驱动视角
    private static final float DEAD_ZONE = 0.02f;

    private final GameMenu gameMenu;
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final DisplayManager displayManager;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // 屏幕旋转缓存：由 DisplayListener 推送刷新，避免在每个传感器事件里查询显示服务
    private final DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {
        }

        @Override
        public void onDisplayChanged(int displayId) {
            if (displayId == Display.DEFAULT_DISPLAY) {
                refreshRotation();
            }
        }

        @Override
        public void onDisplayRemoved(int displayId) {
        }
    };
    private int rotation = Surface.ROTATION_0;

    // 平滑环形缓冲与窗口内累计和
    private final float[][] buffer = new float[SMOOTHING_WINDOW][2];
    private float xTotal;
    private float yTotal;
    private int historyIndex;
    private long timestamp;
    // 轴映射：swapXY 表示水平/垂直视角交换传感器轴，factor 为各轴方向（含屏幕旋转与反转设置）
    private boolean swapXY;
    private float xFactor;
    private float yFactor;

    public Gyroscope(GameMenu gameMenu) {
        this.gameMenu = gameMenu;

        sensorManager = (SensorManager) gameMenu.getActivity().getSystemService(SENSOR_SERVICE);
        displayManager = (DisplayManager) gameMenu.getActivity().getSystemService(DISPLAY_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (isAvailable() && gameMenu.getMenuSetting().isEnableGyroscope()) {
            enableSensor();
        }
    }

    /** 设备是否具备陀螺仪传感器 */
    public boolean isAvailable() {
        return sensor != null;
    }

    public void enableSensor() {
        if (!isAvailable()) {
            return;
        }
        refreshRotation();
        displayManager.registerDisplayListener(displayListener, mainHandler);
        reset();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void disableSensor() {
        // 捕获态是纯增量流，关闭时不再补发绝对坐标，
        // 否则会把视角拽回开启时的位置（幅度=期间累计转动）
        sensorManager.unregisterListener(this);
        displayManager.unregisterDisplayListener(displayListener);
        reset();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 非捕获状态不累计时间戳，重新进入时从当前帧重新起步，避免大步长突跳
        if (gameMenu.getCursorMode() != FCLBridge.CursorDisabled) {
            timestamp = 0;
            return;
        }
        if (timestamp == 0) {
            timestamp = event.timestamp;
            return;
        }
        final float dT = (event.timestamp - timestamp) * TIME_SCALE;
        timestamp = event.timestamp;
        if (dT <= 0) {
            return;
        }

        updateFactors();

        float x = event.values[0];
        float y = event.values[1];
        // 屏幕旋转后水平/垂直视角对应的传感器轴与方向随之变化
        float vx = (swapXY ? y : x) * xFactor;
        float vy = (swapXY ? x : y) * yFactor;

        // 滑动平均：新值入环形缓冲，以窗口均值替代瞬时值
        historyIndex = (historyIndex + 1) % SMOOTHING_WINDOW;
        xTotal -= buffer[historyIndex][0];
        yTotal -= buffer[historyIndex][1];
        buffer[historyIndex][0] = vx;
        buffer[historyIndex][1] = vy;
        xTotal += vx;
        yTotal += vy;
        vx = xTotal / SMOOTHING_WINDOW;
        vy = yTotal / SMOOTHING_WINDOW;

        // 死区过滤
        if (Math.abs(vx) < DEAD_ZONE) {
            vx = 0;
        }
        if (Math.abs(vy) < DEAD_ZONE) {
            vy = 0;
        }
        if (vx == 0 && vy == 0) {
            return;
        }

        float sensitivityX = gameMenu.getMenuSetting().getGyroscopeSensitivityX();
        float sensitivityY = gameMenu.getMenuSetting().getGyroscopeSensitivityY();
        CallbackBridge.sendCursorDelta(vx * dT * sensitivityX, vy * dT * sensitivityY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }

    /** 按当前屏幕旋转计算轴映射：水平视角取自竖直轴的角速度，垂直视角取自水平轴的角速度 */
    private void updateFactors() {
        switch (rotation) {
            case Surface.ROTATION_0: // 竖屏：左右转头绕 Y 轴，上下点头绕 X 轴
                swapXY = true;
                xFactor = -1;
                yFactor = 1;
                break;
            case Surface.ROTATION_180: // 倒置竖屏：两轴相对竖屏反向
                swapXY = true;
                xFactor = 1;
                yFactor = -1;
                break;
            case Surface.ROTATION_270: // 右横屏：两轴相对左横屏反向
                swapXY = false;
                xFactor = 1;
                yFactor = -1;
                break;
            default: // ROTATION_90 左横屏，历史标定方向
                swapXY = false;
                xFactor = -1;
                yFactor = 1;
                break;
        }
        if (gameMenu.getMenuSetting().isInvertGyroscopeX()) {
            xFactor *= -1;
        }
        if (gameMenu.getMenuSetting().isInvertGyroscopeY()) {
            yFactor *= -1;
        }
    }

    /** 读取默认显示屏的当前旋转角度，在启用传感器与旋转变化时调用 */
    private void refreshRotation() {
        Display display = displayManager.getDisplay(Display.DEFAULT_DISPLAY);
        if (display != null) {
            rotation = display.getRotation();
        }
    }

    /** 清空平滑缓冲与时间戳，在启停、进出捕获状态时调用，避免旧状态造成视角跳变 */
    private void reset() {
        timestamp = 0;
        historyIndex = 0;
        xTotal = 0;
        yTotal = 0;
        for (float[] axis : buffer) {
            Arrays.fill(axis, 0);
        }
    }

}
