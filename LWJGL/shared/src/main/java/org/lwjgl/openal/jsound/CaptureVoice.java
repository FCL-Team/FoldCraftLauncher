package org.lwjgl.openal.jsound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * TargetDataLine backing using the ALC capture API (ALC_EXT_CAPTURE). The worker
 * thread periodically drains the capture device into a ring buffer that readers
 * consume. tick() already runs on the worker thread, so ALC state is touched
 * only there (start/stop go through the engine to preserve that guarantee).
 */
final class CaptureVoice implements Voice {

    private final long captureDevice;
    private final int frameSize;
    private final OpenALEngine engine;

    // ring buffer state, all guarded by lock
    private final Object lock = new Object();
    private final byte[] ring;
    private final ByteBuffer pullScratch;
    private final IntBuffer sampleQuery;
    private int readPos;
    private int buffered;
    private volatile boolean capturing;
    private volatile boolean destroyed;

    CaptureVoice(OpenALEngine engine, long captureDevice, int alFormat, int frameSize, float sampleRate, int ringBytes) {
        this.engine = engine;
        this.captureDevice = captureDevice;
        this.frameSize = frameSize;
        this.ring = new byte[Math.max(ringBytes, frameSize * 1024)];
        this.pullScratch = BufferUtils.createByteBuffer(frameSize * 4096);
        this.sampleQuery = BufferUtils.createIntBuffer(1);
    }

    void start() {
        engine.runSync(() -> {
            ALC11.alcCaptureStart(captureDevice);
            capturing = true;
        });
    }

    void stop() {
        engine.runSync(() -> {
            capturing = false;
            ALC11.alcCaptureStop(captureDevice);
        });
    }

    int availableBytes() {
        synchronized (lock) {
            return buffered;
        }
    }

    int read(byte[] b, int off, int len) throws InterruptedException {
        synchronized (lock) {
            while (buffered < len && !destroyed) {
                lock.wait(100);
            }
            if (destroyed && buffered < len) {
                len = buffered - buffered % frameSize;
            }
            if (len <= 0) {
                return 0;
            }
            int copied = 0;
            while (copied < len) {
                int n = Math.min(len - copied, ring.length - readPos);
                System.arraycopy(ring, readPos, b, off + copied, n);
                readPos = (readPos + n) % ring.length;
                buffered -= n;
                copied += n;
            }
            return copied;
        }
    }

    @Override
    public void tick() {
        if (!capturing || destroyed) {
            return;
        }
        sampleQuery.clear();
        ALC10.alcGetIntegerv(captureDevice, ALC11.ALC_CAPTURE_SAMPLES, sampleQuery);
        int samples = sampleQuery.get(0);
        if (samples <= 0) {
            return;
        }
        synchronized (lock) {
            int spaceFrames = (ring.length - buffered) / frameSize;
            int frames = Math.min(samples, spaceFrames);
            if (frames <= 0) {
                // Reader is too slow: leave samples in the device buffer.
                return;
            }
            int bytes = frames * frameSize;
            pullScratch.clear();
            ALC11.alcCaptureSamples(captureDevice, pullScratch, frames);
            copyRingRound(readPos, buffered, pullScratch, bytes);
            buffered += bytes;
            lock.notifyAll();
        }
    }

    private void copyRingRound(int ringOffset, int existing, ByteBuffer src, int bytes) {
        int writeAt = (readPos + existing) % ring.length;
        int tail = Math.min(bytes, ring.length - writeAt);
        src.get(ring, writeAt, tail);
        if (bytes > tail) {
            src.get(ring, 0, bytes - tail);
        }
    }

    @Override
    public void destroy() {
        destroyed = true;
        synchronized (lock) {
            lock.notifyAll();
        }
        try {
            ALC11.alcCaptureStop(captureDevice);
            ALC11.alcCaptureCloseDevice(captureDevice);
        } catch (Throwable t) {
            JSoundLog.error("[JSound] Error closing capture device", t);
        }
    }
}