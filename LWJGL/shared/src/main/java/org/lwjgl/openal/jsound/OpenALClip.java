package org.lwjgl.openal.jsound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

/**
 * javax.sound.sampled.Clip backed by a single OpenAL buffer. The payload is
 * decoded fully into memory; streams that are not raw PCM are run through
 * AudioSystem's format conversion first (works when conversion SPIs are present).
 */
final class OpenALClip extends LineBase implements Clip {

    private static final int MAX_CLIP_BYTES = 256 * 1024 * 1024;

    private final OpenALEngine engine = OpenALEngine.get();

    private volatile AudioFormat format;
    private volatile ClipVoice voice;
    private volatile boolean running;
    private volatile int bufferBytes;

    OpenALClip(JSoundMixer mixer, AudioFormat initialFormat) {
        super(mixer);
        this.format = initialFormat;
    }

    @Override
    public void open(AudioInputStream stream) throws LineUnavailableException, IOException {
        AudioFormat src = stream.getFormat();
        if (Pcm.isSupported(src)) {
            openInternal(src, readAll(stream));
            return;
        }
        float rate = src.getSampleRate() == AudioSystem.NOT_SPECIFIED ? 44100f : src.getSampleRate();
        int channels = src.getChannels() <= 0 ? 2 : Math.min(2, src.getChannels());
        int frameSize = channels * 2;
        AudioFormat target = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, channels,
                frameSize, rate, false);
        try {
            AudioInputStream converted = AudioSystem.getAudioInputStream(target, stream);
            openInternal(target, readAll(converted));
        } catch (RuntimeException e) {
            throw new LineUnavailableException("Cannot convert audio format " + src + " to PCM: " + e.getMessage());
        }
    }

    @Override
    public void open(AudioFormat fmt, byte[] data, int offset, int size) throws LineUnavailableException {
        if (offset < 0 || size < 0 || offset + size > data.length) {
            throw new ArrayIndexOutOfBoundsException("offset=" + offset + ", size=" + size + ", length=" + data.length);
        }
        openInternal(fmt, Arrays.copyOfRange(data, offset, offset + size));
    }

    private synchronized void openInternal(AudioFormat fmt, byte[] raw) throws LineUnavailableException {
        if (open) {
            throw new IllegalStateException("Clip is already open");
        }
        int alFormat = Pcm.openAlFormat(fmt);
        if (alFormat == 0) {
            throw new LineUnavailableException("Unsupported audio format: " + fmt);
        }
        engine.awaitReady();
        final byte[] alData = Pcm.needsTransform(fmt) ? transformCopy(fmt, raw) : raw;

        final Runnable onStart = () -> {
            running = true;
            fire(LineEvent.Type.START);
        };
        final Runnable onStop = () -> {
            running = false;
            fire(LineEvent.Type.STOP);
        };
        try {
            voice = engine.call(() -> {
                ClipVoice v = new ClipVoice(engine, fmt, alFormat, alData, alData.length, onStart, onStop);
                engine.voices().add(v);
                return v;
            });
        } catch (RuntimeException e) {
            throw new LineUnavailableException("Failed to create the OpenAL clip: " + e.getMessage());
        }
        format = fmt;
        bufferBytes = alData.length;
        open = true;
        mixer.lineOpened(this);
        fire(LineEvent.Type.OPEN);
    }

    private static byte[] transformCopy(AudioFormat fmt, byte[] raw) {
        byte[] out = new byte[raw.length];
        Pcm.encode(fmt, raw, 0, raw.length, out);
        return out;
    }

    private static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1 << 16);
        byte[] buffer = new byte[65536];
        long total = 0;
        int n;
        while ((n = in.read(buffer)) > 0) {
            total += n;
            if (total > MAX_CLIP_BYTES) {
                throw new IOException("Clip data exceeds " + MAX_CLIP_BYTES + " bytes");
            }
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    @Override
    public void close() {
        if (!open) {
            return;
        }
        open = false;
        ClipVoice current = voice;
        voice = null;
        if (current != null) {
            try {
                engine.runSync(current::destroy);
            } catch (RuntimeException ignored) {
            }
            engine.voices().remove(current);
        }
        mixer.lineClosed(this);
        fire(LineEvent.Type.CLOSE);
    }

    private ClipVoice requireVoice() {
        ClipVoice current = voice;
        if (!open || current == null) {
            throw new IllegalStateException("Clip is not open");
        }
        return current;
    }

    @Override
    public void loop(int count) {
        if (open) {
            ClipVoice current = voice;
            if (current != null) {
                current.loop(count);
            }
        }
    }

    @Override
    public void setLoopPoints(int start, int end) {
        ClipVoice current = requireVoice();
        int frames = current.getFrameLength();
        if (start < 0 || start >= frames || (end >= 0 && end <= start) || end > frames) {
            throw new IllegalArgumentException("Invalid loop points: start=" + start + ", end=" + end);
        }
        current.setLoopPoints(start, end);
    }

    @Override
    public void start() {
        if (open) {
            ClipVoice current = voice;
            if (current != null) {
                current.start();
            }
        }
    }

    @Override
    public void stop() {
        if (open) {
            ClipVoice current = voice;
            if (current != null) {
                current.stop();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isActive() {
        return running;
    }

    @Override
    public int getFrameLength() {
        ClipVoice current = voice;
        return current == null ? AudioSystem.NOT_SPECIFIED : current.getFrameLength();
    }

    @Override
    public long getMicrosecondLength() {
        ClipVoice current = voice;
        return current == null ? AudioSystem.NOT_SPECIFIED : current.getMicrosecondLength();
    }

    @Override
    public int getFramePosition() {
        return (int) getLongFramePosition();
    }

    @Override
    public long getLongFramePosition() {
        ClipVoice current = voice;
        return current == null ? 0L : current.getFramePosition();
    }

    @Override
    public long getMicrosecondPosition() {
        ClipVoice current = voice;
        return current == null ? 0L : current.getMicrosecondPosition();
    }

    @Override
    public void setFramePosition(int frames) {
        if (open) {
            ClipVoice current = voice;
            if (current != null) {
                current.setFramePosition(frames);
            }
        }
    }

    @Override
    public void setMicrosecondPosition(long micros) {
        float rate = Math.max(1f, format.getSampleRate());
        setFramePosition((int) Math.min(Integer.MAX_VALUE, micros * rate / 1000000L));
    }

    @Override
    public void drain() {
    }

    @Override
    public void flush() {
    }

    @Override
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public int getBufferSize() {
        return bufferBytes;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public float getLevel() {
        return AudioSystem.NOT_SPECIFIED;
    }

    @Override
    protected Line.Info specificLineInfo() {
        return new DataLine.Info(Clip.class, format);
    }

    @Override
    protected void applyGain(float gain) {
        ClipVoice current = voice;
        if (current != null && open) {
            current.applyGain(gain);
        }
    }
}