package org.lwjgl.openal.jsound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/** javax.sound.sampled.SourceDataLine backed by an OpenAL streaming source. */
final class OpenALSourceDataLine extends LineBase implements SourceDataLine {

    private final OpenALEngine engine = OpenALEngine.get();

    private volatile AudioFormat format;
    private volatile StreamVoice voice;
    private volatile boolean running;
    private volatile int bufferBytes;

    OpenALSourceDataLine(JSoundMixer mixer, AudioFormat initialFormat) {
        super(mixer);
        this.format = initialFormat;
    }

    @Override
    public void open(AudioFormat fmt, int bufferSize) throws LineUnavailableException {
        if (open) {
            throw new IllegalStateException("Line is already open");
        }
        if (fmt == null || !Pcm.isSupported(fmt)) {
            throw new LineUnavailableException("Unsupported audio format: " + fmt);
        }
        engine.awaitReady();
        int frameSize = Pcm.frameSize(fmt);
        int rate = Math.max(1, Math.round(fmt.getSampleRate()));
        int framesPerChunk = Math.max(1, rate * StreamVoice.CHUNK_MS / 1000);
        int chunk = framesPerChunk * frameSize;
        int internal = Math.max(bufferSize <= 0 ? chunk * StreamVoice.NUM_BUFFERS : bufferSize, chunk * 3);
        internal = ((internal + frameSize - 1) / frameSize) * frameSize;

        final Runnable onStart = () -> {
            running = true;
            fire(LineEvent.Type.START);
        };
        final Runnable onStop = () -> {
            running = false;
            fire(LineEvent.Type.STOP);
        };
        final int alFormat = Pcm.openAlFormat(fmt);
        try {
            voice = engine.call(() -> {
                StreamVoice v = new StreamVoice(engine, fmt, alFormat, onStart, onStop);
                engine.voices().add(v);
                return v;
            });
        } catch (RuntimeException e) {
            throw new LineUnavailableException("Failed to create the OpenAL source: " + e.getMessage());
        }
        bufferBytes = internal;
        format = fmt;
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
        StreamVoice current = voice;
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

    private StreamVoice requireVoice() {
        if (!open) {
            throw new IllegalStateException("Line is not open");
        }
        StreamVoice current = voice;
        if (current == null) {
            throw new IllegalStateException("Line is not open");
        }
        return current;
    }

    @Override
    public int write(byte[] b, int off, int len) {
        if (len == 0) {
            return 0;
        }
        if (b == null || off < 0 || len < 0 || off + len > b.length) {
            throw new ArrayIndexOutOfBoundsException("off=" + off + ", len=" + len + ", buffer length=" + (b == null ? 0 : b.length));
        }
        StreamVoice current = requireVoice();
        try {
            current.write(b, off, len);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Write interrupted", e);
        }
        return len;
    }

    @Override
    public void drain() {
        if (open) {
            StreamVoice current = voice;
            if (current != null) {
                current.drain();
            }
        }
    }

    @Override
    public void flush() {
        if (open) {
            StreamVoice current = voice;
            if (current != null) {
                current.flush();
            }
        }
    }

    @Override
    public void start() {
        if (open) {
            StreamVoice current = voice;
            if (current != null) {
                current.start();
            }
        }
    }

    @Override
    public void stop() {
        if (open) {
            StreamVoice current = voice;
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
    public AudioFormat getFormat() {
        return format;
    }

    @Override
    public int getBufferSize() {
        return bufferBytes;
    }

    @Override
    public int available() {
        StreamVoice current = voice;
        return current == null ? 0 : current.availableBytes();
    }

    @Override
    public int getFramePosition() {
        return (int) getLongFramePosition();
    }

    @Override
    public long getLongFramePosition() {
        StreamVoice current = voice;
        if (current == null) {
            return 0L;
        }
        return current.consumedBytes() / Pcm.frameSize(format);
    }

    @Override
    public long getMicrosecondPosition() {
        long frames = getLongFramePosition();
        long rate = Math.max(1L, (long) format.getSampleRate());
        return frames * 1000000L / rate;
    }

    @Override
    public float getLevel() {
        return javax.sound.sampled.AudioSystem.NOT_SPECIFIED;
    }

    @Override
    protected Line.Info specificLineInfo() {
        return new DataLine.Info(SourceDataLine.class, format);
    }

    @Override
    protected void applyGain(float gain) {
        StreamVoice current = voice;
        if (current != null && open) {
            current.applyGain(gain);
        }
    }
}
