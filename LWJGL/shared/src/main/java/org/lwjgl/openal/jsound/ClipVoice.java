package org.lwjgl.openal.jsound;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Clip;

/**
 * Clip backing: the whole PCM payload lives in one OpenAL buffer. Loop counts are
 * handled by the worker thread so line listeners can be fired on transitions.
 */
final class ClipVoice implements Voice, LineControls.VoiceRef {

    final int source;
    final int buffer;
    final int alFormat;
    final int framesTotal;
    final int frameSize;

    private final OpenALEngine engine;
    private final float sampleRate;
    private final Runnable onStart;
    private final Runnable onStop;

    // worker-thread state
    private boolean playing;
    /** Remaining full-buffer wraps after the initial pass; -1 means loop forever. */
    private int wrapsRemaining;
    private int loopStart;
    private int loopEnd = -1;
    private int passes;
    private int lastOffset;

    private volatile long framePos;

    ClipVoice(OpenALEngine engine, AudioFormat format, int alFormat,
              byte[] alData, int alLength, Runnable onStart, Runnable onStop) {
        this.engine = engine;
        this.sampleRate = format.getSampleRate();
        this.frameSize = Pcm.frameSize(format);
        this.framesTotal = alLength / this.frameSize;
        this.alFormat = alFormat;
        this.onStart = onStart;
        this.onStop = onStop;
        this.buffer = AL10.alGenBuffers();
        // OpenAL requires direct memory for the data pointer.
        ByteBuffer upload = OpenALEngine.allocate(alLength);
        upload.put(alData, 0, alLength);
        upload.flip();
        AL10.alBufferData(buffer, alFormat, upload, Math.round(sampleRate));
        this.source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
    }

    int getFrameLength() {
        return framesTotal;
    }

    long getMicrosecondLength() {
        return secondsToMicros(framesTotal);
    }

    long getFramePosition() {
        return framePos;
    }

    long getMicrosecondPosition() {
        return secondsToMicros(framePos);
    }

    void setFramePosition(int frames) {
        engine.runSync(() -> {
            int target = Math.max(0, Math.min(framesTotal, frames));
            passes = target / Math.max(1, framesTotal);
            AL10.alSourcei(source, AL11.AL_SAMPLE_OFFSET, target);
            framePos = target;
            lastOffset = target;
        });
    }

    void setLoopPoints(int start, int end) {
        engine.runSync(() -> {
            this.loopStart = Math.max(0, start);
            this.loopEnd = end < 0 ? -1 : Math.min(framesTotal, end);
        });
    }

    void loop(int count) {
        engine.runSync(() -> this.wrapsRemaining = count);
    }

    void start() {
        engine.runSync(() -> {
            if (playing) {
                return;
            }
            boolean fullRange = loopStart == 0 && (loopEnd < 0 || loopEnd >= framesTotal);
            // AL looping handles full-range wraps natively; sub-range loops are
            // seeked back by tick(). A looping source never reaches AL_STOPPED,
            // which makes the wrap count below fully deterministic.
            AL10.alSourcei(source, AL10.AL_LOOPING,
                    fullRange || (loopStart == 0 && loopEnd < 0) ? AL10.AL_TRUE : AL10.AL_FALSE);
            int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
            if (state == AL10.AL_STOPPED && AL10.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET) >= framesTotal) {
                AL10.alSourcei(source, AL11.AL_SAMPLE_OFFSET, loopStart);
            }
            playing = true;
            AL10.alSourcePlay(source);
            onStart.run();
        });
    }

    void stop() {
        engine.runSync(() -> {
            if (playing) {
                AL10.alSourceStop(source);
                playing = false;
                onStop.run();
            }
        });
    }

    @Override
    public void applyGain(float gain) {
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
    }

    @Override
    public void tick() {
        if (!playing) {
            return;
        }
        int offset = AL10.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET);
        if (offset < lastOffset) {
            // Sample offset wrapped: one full pass has completed. javax.sound's
            // loop(count) means count+1 total plays, so a wrap with no remaining
            // wraps finishes playback.
            passes++;
            if (wrapsRemaining == 0) {
                playing = false;
                framePos = (long) passes * framesTotal;
                AL10.alSourceStop(source);
                onStop.run();
                return;
            }
            if (wrapsRemaining > 0) {
                wrapsRemaining--;
            }
        }
        lastOffset = offset;
        framePos = (long) passes * framesTotal + offset;

        int end = loopEnd < 0 ? framesTotal : loopEnd;
        if (loopEnd >= 0 && offset >= loopEnd) {
            // Sub-range loop point reached: seek back or finish.
            if (wrapsRemaining == 0) {
                playing = false;
                framePos = end;
                AL10.alSourceStop(source);
                onStop.run();
            } else {
                if (wrapsRemaining > 0) {
                    wrapsRemaining--;
                }
                passes++;
                AL10.alSourcei(source, AL11.AL_SAMPLE_OFFSET, loopStart);
                lastOffset = loopStart;
                if (AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
                    AL10.alSourcePlay(source);
                }
            }
        }
    }

    @Override
    public void destroy() {
        AL10.alSourceStop(source);
        AL10.alSourcei(source, AL10.AL_BUFFER, 0);
        AL10.alDeleteBuffers(buffer);
        AL10.alDeleteSources(source);
    }

    private long secondsToMicros(long frames) {
        long rate = Math.max(1L, (long) sampleRate);
        return frames * 1000000L / rate;
    }
}
