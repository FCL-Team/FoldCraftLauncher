package org.lwjgl.openal.jsound;

import org.lwjgl.openal.ALC11;

import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * javax.sound.sampled.TargetDataLine (microphone input) backed by ALC capture.
 * Restricted to formats ALC can deliver natively: 16-bit signed PCM (either
 * endianness, converted in place on read) and 8-bit unsigned PCM.
 */
final class OpenALTargetDataLine extends LineBase implements TargetDataLine {

    private final OpenALEngine engine = OpenALEngine.get();

    private volatile AudioFormat format;
    private volatile CaptureVoice voice;
    private volatile boolean running;
    private volatile int bufferBytes;
    private volatile boolean swapEndian;

    OpenALTargetDataLine(JSoundMixer mixer, AudioFormat initialFormat) {
        super(mixer);
        this.format = initialFormat;
    }

    @Override
    public void open(AudioFormat fmt, int bufferSize) throws LineUnavailableException {
        if (open) {
            throw new IllegalStateException("Line is already open");
        }
        if (fmt == null || !Pcm.isSupportedForCapture(fmt)) {
            throw new LineUnavailableException("Unsupported capture format: " + fmt);
        }
        engine.awaitReady();
        if (!engine.isCaptureSupported()) {
            throw new LineUnavailableException("OpenAL capture (ALC_EXT_CAPTURE) is not available");
        }
        int frameSize = Pcm.frameSize(fmt);
        int ringBytes = bufferSize <= 0 ? frameSize * 44100 : Math.max(bufferSize, frameSize * 4096);
        final int alFormat = Pcm.openAlFormat(fmt);
        final int deviceFrames = ringBytes / frameSize;
        try {
            voice = engine.call(() -> {
                long device = ALC11.alcCaptureOpenDevice((ByteBuffer) null,
                        Math.max(1, Math.round(fmt.getSampleRate())), alFormat, deviceFrames);
                if (device == 0L) {
                    throw new IllegalStateException("alcCaptureOpenDevice failed");
                }
                CaptureVoice v = new CaptureVoice(engine, device, alFormat, frameSize, fmt.getSampleRate(), ringBytes);
                engine.voices().add(v);
                return v;
            });
        } catch (RuntimeException e) {
            throw new LineUnavailableException("Failed to open the OpenAL capture device: " + e.getMessage());
        }
        format = fmt;
        swapEndian = fmt.getSampleSizeInBits() == 16 && fmt.isBigEndian();
        bufferBytes = ringBytes;
        open = true;
        mixer.lineOpened(this);
        fire(LineEvent.Type.OPEN);
    }

    @Override
    public void open(AudioFormat fmt) throws LineUnavailableException {
        open(fmt, -1);
    }

    @Override
    public void close() {
        if (!open) {
            return;
        }
        open = false;
        running = false;
        CaptureVoice current = voice;
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

    private CaptureVoice requireVoice() {
        if (!open) {
            throw new IllegalStateException("Line is not open");
        }
        CaptureVoice current = voice;
        if (current == null) {
            throw new IllegalStateException("Line is not open");
        }
        return current;
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (len == 0) {
            return 0;
        }
        if (b == null || off < 0 || len < 0 || off + len > b.length) {
            throw new ArrayIndexOutOfBoundsException("off=" + off + ", len=" + len + ", buffer length=" + (b == null ? 0 : b.length));
        }
        CaptureVoice current = requireVoice();
        int n;
        try {
            n = current.read(b, off, len);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        }
        if (n > 0 && swapEndian) {
            for (int i = off; i + 1 < off + n; i += 2) {
                byte tmp = b[i];
                b[i] = b[i + 1];
                b[i + 1] = tmp;
            }
        }
        return n;
    }

    @Override
    public void start() {
        CaptureVoice current = requireVoice();
        current.start();
        running = true;
        fire(LineEvent.Type.START);
    }

    @Override
    public void stop() {
        CaptureVoice current = requireVoice();
        current.stop();
        running = false;
        fire(LineEvent.Type.STOP);
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
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public int getBufferSize() {
        return bufferBytes;
    }

    @Override
    public int available() {
        CaptureVoice current = voice;
        return current == null ? 0 : current.availableBytes();
    }

    @Override
    public int getFramePosition() {
        return 0;
    }

    @Override
    public long getLongFramePosition() {
        return 0L;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0L;
    }

    @Override
    public void drain() {
    }

    @Override
    public void flush() {
    }

    @Override
    public float getLevel() {
        return javax.sound.sampled.AudioSystem.NOT_SPECIFIED;
    }

    @Override
    protected Line.Info specificLineInfo() {
        return new DataLine.Info(TargetDataLine.class, format);
    }

    @Override
    protected void applyGain(float gain) {
        // Capture lines have no output gain.
    }
}
