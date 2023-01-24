package com.tungsten.fcl.control;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;

public class Gyroscope implements SensorEventListener {

    private static final float NS2S = 1.0f / 1000000000.0f;

    private BooleanProperty enableProperty;

    private final GameMenu gameMenu;
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private final float[] angle = new float[3];

    public Gyroscope(GameMenu gameMenu) {
        this.gameMenu = gameMenu;

        sensorManager = (SensorManager) gameMenu.getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gameMenu.getMenuSetting().isEnableGyroscope()) {
            enableSensor();
        } else {
            disableSensor();
        }
    }

    public BooleanProperty enableProperty() {
        if (enableProperty == null) {
            enableProperty = new BooleanPropertyBase() {
                @Override
                protected void invalidated() {
                    super.invalidated();
                    if (get()) {
                        enableSensor();
                    } else {
                        disableSensor();
                    }
                }

                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "enable";
                }
            };
        }
        return enableProperty;
    }

    public void enableSensor() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void disableSensor() {
        sensorManager.unregisterListener(this);
        if (gameMenu.getCursorMode() == FCLBridge.CursorDisabled) {
            gameMenu.getInput().setPointer(gameMenu.getPointerX(), gameMenu.getPointerY());
        }
    }

    private float timestamp;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (gameMenu.getCursorMode() == FCLBridge.CursorDisabled) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                angle[0] += event.values[0] * dT;
                angle[1] += event.values[1] * dT;
                float angleX = (float) Math.toDegrees(angle[0]);
                float angleY = (float) Math.toDegrees(angle[1]);
                if (gameMenu.getBridge() != null) {
                    gameMenu.getBridge().pushEventPointer((int) (gameMenu.getPointerX() - (angleX * gameMenu.getMenuSetting().getGyroscopeSensitivity())), (int) (gameMenu.getPointerY() + (angleY * gameMenu.getMenuSetting().getGyroscopeSensitivity())));
                }
            }
            timestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }
}
