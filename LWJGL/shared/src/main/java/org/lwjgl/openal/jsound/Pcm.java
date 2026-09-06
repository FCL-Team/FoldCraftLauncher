package org.lwjgl.openal.jsound;

import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;

/**
 * PCM helpers: decides which AudioFormats the OpenAL backend can carry and
 * converts Java-side PCM into the little-endian/unsigned layout OpenAL expects.
 */
final class Pcm {

    private Pcm() {
    }

    /** True when the format can be handed to OpenAL after an in-place byte conversion. */
    static boolean isSupported(AudioFormat fmt) {
        if (fmt == null) {
            return false;
        }
        Encoding enc = fmt.getEncoding();
        boolean pcm = enc.equals(Encoding.PCM_SIGNED) || enc.equals(Encoding.PCM_UNSIGNED);
        if (!pcm) {
            return false;
        }
        int channels = fmt.getChannels();
        if (channels < 1 || channels > 2) {
            return false;
        }
        int bits = fmt.getSampleSizeInBits();
        if (bits != AudioSystem.NOT_SPECIFIED && bits != 8 && bits != 16) {
            return false;
        }
        float rate = fmt.getSampleRate();
        if (rate != AudioSystem.NOT_SPECIFIED && (rate < 4000f || rate > 192000f)) {
            return false;
        }
        return true;
    }

    /** Capture lines must match what ALC delivers natively: 16-bit signed or 8-bit unsigned. */
    static boolean isSupportedForCapture(AudioFormat fmt) {
        if (!isSupported(fmt)) {
            return false;
        }
        int bits = fmt.getSampleSizeInBits();
        if (bits == 8) {
            return fmt.getEncoding().equals(Encoding.PCM_UNSIGNED);
        }
        return fmt.getEncoding().equals(Encoding.PCM_SIGNED);
    }

    /** True when raw bytes must be rewritten before feeding OpenAL. */
    static boolean needsTransform(AudioFormat fmt) {
        if (fmt.getSampleSizeInBits() == 16) {
            return fmt.isBigEndian() || fmt.getEncoding().equals(Encoding.PCM_UNSIGNED);
        }
        if (fmt.getSampleSizeInBits() == 8) {
            return fmt.getEncoding().equals(Encoding.PCM_SIGNED);
        }
        return false;
    }

    /** AL_FORMAT_* constant for a concrete supported format, or 0. */
    static int openAlFormat(AudioFormat fmt) {
        int bits = fmt.getSampleSizeInBits();
        int channels = fmt.getChannels();
        if (bits == 8) {
            return channels == 2 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_MONO8;
        }
        if (bits == 16) {
            return channels == 2 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
        }
        return 0;
    }

    static int frameSize(AudioFormat fmt) {
        int bits = fmt.getSampleSizeInBits() == AudioSystem.NOT_SPECIFIED ? 16 : fmt.getSampleSizeInBits();
        int channels = Math.max(1, fmt.getChannels());
        return channels * (bits / 8);
    }

    /**
     * Converts {@code len} bytes from {@code src} into OpenAL layout inside {@code dst}
     * (which must have at least {@code len} bytes remaining). Returns the byte count written.
     */
    static int encode(AudioFormat fmt, byte[] src, int srcOff, int len, byte[] dst) {
        if (!needsTransform(fmt)) {
            System.arraycopy(src, srcOff, dst, 0, len);
            return len;
        }
        int bits = fmt.getSampleSizeInBits();
        if (bits == 16) {
            boolean swap = fmt.isBigEndian();
            boolean unsigned = fmt.getEncoding().equals(Encoding.PCM_UNSIGNED);
            for (int i = 0; i + 1 < len; i += 2) {
                int lo = src[srcOff + i] & 0xFF;
                int hi = src[srcOff + i + 1] & 0xFF;
                int v = swap ? ((lo << 8) | hi) : ((hi << 8) | lo);
                if (unsigned) {
                    v = (v - 32768) & 0xFFFF;
                }
                dst[i] = (byte) (v & 0xFF);
                dst[i + 1] = (byte) ((v >> 8) & 0xFF);
            }
        } else {
            // 8-bit: OpenAL wants unsigned; Java signed 8-bit flips the high bit
            for (int i = 0; i < len; i++) {
                dst[i] = (byte) (src[srcOff + i] ^ 0x80);
            }
        }
        return len;
    }
}