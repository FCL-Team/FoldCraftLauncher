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

    private static final float NS2S = 1.0f / 40000000.0f;

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
        timestamp = 0;
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void disableSensor() {
        sensorManager.unregisterListener(this);
        if (gameMenu.getCursorMode() == FCLBridge.CursorDisabled) {
            gameMenu.getInput().setPointer(gameMenu.getPointerX(), gameMenu.getPointerY(), "Gyro");
        }
    }

    private long timestamp;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (gameMenu.getCursorMode() == FCLBridge.CursorDisabled) {
            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                angle[0] += event.values[0] * dT * gameMenu.getMenuSetting().getGyroscopeSensitivity();
                angle[1] += event.values[1] * dT * gameMenu.getMenuSetting().getGyroscopeSensitivity();
                if (gameMenu.getBridge() != null) {
                    gameMenu.getBridge().pushEventPointer((gameMenu.getPointerX() - angle[0]), (gameMenu.getPointerY() + angle[1]));
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
