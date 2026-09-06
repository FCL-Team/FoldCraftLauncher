package org.lwjgl.openal.jsound;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * The single Mixer exposed to javax.sound.sampled. Every line request is matched
 * against the PCM formats the OpenAL backend can carry.
 */
final class JSoundMixer implements Mixer {

    /** Mixer.Info has a protected constructor, so the backend ships its own subclass. */
    static final class Info extends Mixer.Info {
        Info() {
            super("MioJSound (OpenAL)",
                    "FoldCraftLauncher",
                    "javax.sound.sampled backend routed through LWJGL3 OpenAL",
                    "1.0");
        }
    }

    static final Mixer.Info INFO = new Info();

    private static final float[] RATES = {8000f, 11025f, 16000f, 22050f, 32000f, 44100f, 48000f};

    static final DataLine.Info SOURCE_INFO =
            new DataLine.Info(SourceDataLine.class, supportedFormats(false, false), 0, AudioSystem.NOT_SPECIFIED);
    static final DataLine.Info CLIP_INFO =
            new DataLine.Info(Clip.class, supportedFormats(false, false), 0, AudioSystem.NOT_SPECIFIED);
    static final DataLine.Info TARGET_INFO =
            new DataLine.Info(TargetDataLine.class, supportedFormats(true, false), 0, AudioSystem.NOT_SPECIFIED);

    private final CopyOnWriteArrayList<Line> openLines = new CopyOnWriteArrayList<>();

    private static AudioFormat[] supportedFormats(boolean capture, boolean includeClip) {
        java.util.List<AudioFormat> formats = new java.util.ArrayList<>();
        for (float rate : RATES) {
            for (int channels = 1; channels <= 2; channels++) {
                formats.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, channels,
                        channels * 2, rate, true));
                formats.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, channels,
                        channels * 2, rate, false));
                if (!capture) {
                    formats.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, rate, 16, channels,
                            channels * 2, rate, false));
                    formats.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 8, channels,
                            channels, rate, false));
                    formats.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, rate, 8, channels,
                            channels, rate, false));
                }
            }
        }
        return formats.toArray(new AudioFormat[0]);
    }

    void lineOpened(Line line) {
        openLines.add(line);
    }

    void lineClosed(Line line) {
        openLines.remove(line);
    }

    private static boolean isRequestedClassSupported(Line.Info info) {
        Class<?> lineClass = ((DataLine.Info) info).getLineClass();
        return SourceDataLine.class.isAssignableFrom(lineClass)
                || Clip.class.isAssignableFrom(lineClass)
                || TargetDataLine.class.isAssignableFrom(lineClass)
                || DataLine.class.isAssignableFrom(lineClass);
    }

    static boolean isSupportedLineInfo(Line.Info info) {
        if (!(info instanceof DataLine.Info) || !isRequestedClassSupported(info)) {
            return false;
        }
        AudioFormat[] requested = ((DataLine.Info) info).getFormats();
        if (requested.length == 0) {
            return true;
        }
        for (AudioFormat fmt : requested) {
            if (Pcm.isSupported(fmt)) {
                return true;
            }
        }
        return false;
    }

    private static AudioFormat concretize(AudioFormat fmt) {
        float rate = fmt.getSampleRate() == AudioSystem.NOT_SPECIFIED ? 44100f : fmt.getSampleRate();
        int bits = fmt.getSampleSizeInBits() == AudioSystem.NOT_SPECIFIED ? 16 : fmt.getSampleSizeInBits();
        int channels = fmt.getChannels() <= 0 ? 2 : fmt.getChannels();
        AudioFormat.Encoding enc = fmt.getEncoding() == null
                ? AudioFormat.Encoding.PCM_SIGNED : fmt.getEncoding();
        return new AudioFormat(enc, rate, bits, channels, channels * (bits / 8), rate, fmt.isBigEndian());
    }

    private static AudioFormat pickFormat(DataLine.Info info) {
        for (AudioFormat fmt : info.getFormats()) {
            if (Pcm.isSupported(fmt)) {
                return concretize(fmt);
            }
        }
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 2, 4, 44100f, false);
    }

    @Override
    public Mixer.Info getMixerInfo() {
        return INFO;
    }

    @Override
    public Line.Info[] getSourceLineInfo() {
        return new Line.Info[]{SOURCE_INFO, CLIP_INFO};
    }

    @Override
    public Line.Info[] getTargetLineInfo() {
        return new Line.Info[]{TARGET_INFO};
    }

    @Override
    public Line.Info[] getSourceLineInfo(Line.Info info) {
        return isSupportedLineInfo(info) ? new Line.Info[]{info} : new Line.Info[0];
    }

    @Override
    public Line.Info[] getTargetLineInfo(Line.Info info) {
        return isSupportedLineInfo(info) ? new Line.Info[]{info} : new Line.Info[0];
    }

    @Override
    public boolean isLineSupported(Line.Info info) {
        return isSupportedLineInfo(info);
    }

    @Override
    public Line getLine(Line.Info info) throws LineUnavailableException {
        if (!(info instanceof DataLine.Info) || !isSupportedLineInfo(info)) {
            throw new LineUnavailableException("Unsupported line: " + info);
        }
        DataLine.Info dataLineInfo = (DataLine.Info) info;
        Class<?> lineClass = dataLineInfo.getLineClass();
        AudioFormat format = pickFormat(dataLineInfo);
        Line line;
        if (Clip.class.isAssignableFrom(lineClass)) {
            line = new OpenALClip(this, format);
        } else if (TargetDataLine.class.isAssignableFrom(lineClass)) {
            line = new OpenALTargetDataLine(this, format);
        } else {
            line = new OpenALSourceDataLine(this, format);
        }
        return line;
    }

    @Override
    public int getMaxLines(Line.Info info) {
        return AudioSystem.NOT_SPECIFIED;
    }

    @Override
    public Line[] getSourceLines() {
        return filterLines(true);
    }

    @Override
    public Line[] getTargetLines() {
        return filterLines(false);
    }

    private Line[] filterLines(boolean source) {
        java.util.List<Line> result = new java.util.ArrayList<>();
        for (Line line : openLines) {
            boolean isSource = line instanceof OpenALSourceDataLine || line instanceof OpenALClip;
            if (source == isSource) {
                result.add(line);
            }
        }
        return result.toArray(new Line[0]);
    }

    @Override
    public void synchronize(Line[] lines, boolean maintainSync) {
        throw new IllegalArgumentException("Synchronization is not supported");
    }

    @Override
    public void unsynchronize(Line[] lines) {
        throw new IllegalArgumentException("Synchronization is not supported");
    }

    @Override
    public boolean isSynchronizationSupported(Line[] lines, boolean maintainSync) {
        return false;
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void addLineListener(javax.sound.sampled.LineListener listener) {
    }

    @Override
    public void removeLineListener(javax.sound.sampled.LineListener listener) {
    }

    @Override
    public boolean isControlSupported(Control.Type type) {
        return false;
    }

    @Override
    public Control getControl(Control.Type type) {
        throw new IllegalArgumentException("Unsupported control: " + type);
    }

    @Override
    public Control[] getControls() {
        return new Control[0];
    }

    @Override
    public Line.Info getLineInfo() {
        return new Line.Info(Mixer.class);
    }
}