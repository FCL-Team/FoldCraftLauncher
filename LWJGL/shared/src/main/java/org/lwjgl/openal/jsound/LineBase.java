package org.lwjgl.openal.jsound;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Shared plumbing for the three line implementations: listener dispatch,
 * the standard volume control set, and open state.
 */
abstract class LineBase implements Line {

    final JSoundMixer mixer;
    final LineControls controls;
    final CopyOnWriteArrayList<LineListener> listeners = new CopyOnWriteArrayList<>();

    volatile boolean open;

    LineBase(JSoundMixer mixer) {
        this.mixer = mixer;
        this.controls = new LineControls(OpenALEngine.get(), this::applyGain);
    }

    protected abstract void applyGain(float gain);

    protected abstract Line.Info specificLineInfo();

    /** Position reported in line events; data lines override with their real position. */
    protected long eventPosition() {
        return 0L;
    }

    void fire(LineEvent.Type type) {
        LineEvent event = new LineEvent(this, type, eventPosition());
        for (LineListener listener : listeners) {
            try {
                listener.update(event);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public Line.Info getLineInfo() {
        return specificLineInfo();
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void open() {
        // Base Line.open(): data lines are opened with an explicit format instead.
    }

    @Override
    public void addLineListener(LineListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeLineListener(LineListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean isControlSupported(Control.Type type) {
        return type == FloatControl.Type.MASTER_GAIN
                || type == FloatControl.Type.VOLUME
                || type == BooleanControl.Type.MUTE;
    }

    @Override
    public Control getControl(Control.Type type) {
        if (type == FloatControl.Type.MASTER_GAIN) {
            return controls.masterGain;
        }
        if (type == FloatControl.Type.VOLUME) {
            return controls.volume;
        }
        if (type == BooleanControl.Type.MUTE) {
            return controls.mute;
        }
        throw new IllegalArgumentException("Unsupported control: " + type);
    }

    @Override
    public Control[] getControls() {
        return new Control[]{controls.masterGain, controls.volume, controls.mute};
    }
}