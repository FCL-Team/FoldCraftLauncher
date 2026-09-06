package org.lwjgl.openal.jsound;

import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.FloatControl;

/**
 * Volume controls shared by the line implementations. MASTER_GAIN (dB) and VOLUME
 * (0..1) combine into one AL_GAIN value applied on the worker thread.
 */
final class LineControls {

    private static final float MAX_LINEAR = 4f;

    private final OpenALEngine engine;
    private final VoiceRef voiceRef;

    final FloatControl masterGain = new FloatControl(FloatControl.Type.MASTER_GAIN,
            -80f, 6.0206f, 0.01f, -1, 0f, "dB") {
        @Override
        public void setValue(float newValue) {
            super.setValue(Math.max(getMinimum(), Math.min(getMaximum(), newValue)));
            apply();
        }
    };

    final FloatControl volume = new FloatControl(FloatControl.Type.VOLUME,
            0f, 1f, 0.01f, -1, 1f, "") {
        @Override
        public void setValue(float newValue) {
            super.setValue(Math.max(getMinimum(), Math.min(getMaximum(), newValue)));
            apply();
        }
    };

    final BooleanControl mute = new BooleanControl(BooleanControl.Type.MUTE, false) {
        @Override
        public void setValue(boolean newValue) {
            super.setValue(newValue);
            apply();
        }
    };

    LineControls(OpenALEngine engine, VoiceRef voiceRef) {
        this.engine = engine;
        this.voiceRef = voiceRef;
    }

    void apply() {
        if (mute.getValue()) {
            setLinearGain(0f);
            return;
        }
        float db = masterGain.getValue();
        setLinearGain((float) Math.pow(10d, db / 20d) * volume.getValue());
    }

    private void setLinearGain(float linear) {
        final float gain = Math.max(0f, Math.min(MAX_LINEAR, linear));
        engine.submit(() -> voiceRef.applyGain(gain));
    }

    /** Lets a voice expose its current OpenAL source id (may be absent before open). */
    interface VoiceRef {
        void applyGain(float gain);
    }
}