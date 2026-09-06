package org.lwjgl.openal.jsound;

import org.lwjgl.openal.AL10;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioFormat;

/**
 * SourceDataLine backing: PCM written by the mod thread is chunked into small
 * buffers that the worker thread feeds into a ring of OpenAL queue buffers.
 * Backpressure and buffer recycling are coordinated through a simple pool lock,
 * so the mod thread never touches OpenAL state directly.
 *
 * Buffer ring invariant (worker thread only): slots [0, queuedCount) are queued
 * to the OpenAL source, slots [queuedCount, NUM_BUFFERS) are free; queuedLens /
 * queuedArrays are only meaningful for the queued region.
 */
final class StreamVoice implements Voice, LineControls.VoiceRef {

    static final int NUM_BUFFERS = 6;
    static final int CHUNK_MS = 50;

    final int source;
    final int alFormat;
    final int frameSize;
    final int chunkBytes;

    private final OpenALEngine engine;
    private final AudioFormat format;
    private final Runnable onStart;
    private final Runnable onStop;

    /** Full chunks waiting to be handed to OpenAL. Producer: writer. Consumer: worker. */
    private final ConcurrentLinkedQueue<Chunk> pending = new ConcurrentLinkedQueue<>();
    private final AtomicInteger pendingCount = new AtomicInteger();

    /** Recycled chunk arrays. Producer: worker. Consumer: writer. */
    private final Object poolLock = new Object();
    private final ArrayDeque<byte[]> freeChunks = new ArrayDeque<>();

    // worker-thread state
    private final int[] ringBufs = new int[NUM_BUFFERS];
    private final int[] queuedLens = new int[NUM_BUFFERS];
    private final byte[][] queuedArrays = new byte[NUM_BUFFERS][];
    /** Scratch direct buffer: OpenAL requires direct memory for the data pointer. */
    private final ByteBuffer uploadScratch;
    private int queuedCount;
    private boolean started;
    private boolean playing;

    // shared snapshots
    private volatile long writtenBytes;
    private volatile long consumedBytes;

    // writer-thread staging
    private final byte[] stage;
    private int stagePos;
    private volatile boolean destroyed;

    StreamVoice(OpenALEngine engine, AudioFormat format, int alFormat, Runnable onStart, Runnable onStop) {
        this.engine = engine;
        this.format = format;
        this.alFormat = alFormat;
        this.onStart = onStart;
        this.onStop = onStop;
        this.frameSize = Pcm.frameSize(format);
        int rate = Math.round(format.getSampleRate());
        int framesPerChunk = Math.max(1, rate * CHUNK_MS / 1000);
        this.chunkBytes = framesPerChunk * frameSize;
        this.stage = new byte[chunkBytes];
        this.uploadScratch = OpenALEngine.allocate(chunkBytes);
        for (int i = 0; i < NUM_BUFFERS; i++) {
            freeChunks.add(new byte[chunkBytes]);
        }
        for (int i = 0; i < NUM_BUFFERS; i++) {
            ringBufs[i] = AL10.alGenBuffers();
        }
        this.source = AL10.alGenSources();
    }

    // ------------------------------------------------------------------ writer side

    void write(byte[] b, int off, int len) throws InterruptedException {
        int remaining = len;
        int pos = off;
        while (remaining > 0) {
            int n = Math.min(remaining, chunkBytes - stagePos);
            System.arraycopy(b, pos, stage, stagePos, n);
            stagePos += n;
            pos += n;
            remaining -= n;
            writtenBytes += n;
            if (stagePos == chunkBytes) {
                pushStage();
            }
        }
        // A short final chunk (still frame aligned) is flushed right away so that
        // small writes are not stuck waiting for the next call to complete a chunk.
        if (stagePos >= frameSize) {
            pushStage();
        }
        engine.wake();
    }

    private void pushStage() throws InterruptedException {
        byte[] out = takeFreeChunk();
        int len = Pcm.encode(format, stage, 0, stagePos, out);
        pending.add(new Chunk(out, len));
        pendingCount.incrementAndGet();
        stagePos = 0;
        engine.wake();
    }

    private byte[] takeFreeChunk() throws InterruptedException {
        synchronized (poolLock) {
            while (freeChunks.isEmpty()) {
                if (destroyed) {
                    throw new IllegalStateException("Line is closed");
                }
                poolLock.wait(100);
            }
            return freeChunks.poll();
        }
    }

    long writtenBytes() {
        return writtenBytes;
    }

    long consumedBytes() {
        return consumedBytes;
    }

    int availableBytes() {
        int free = Math.max(0, NUM_BUFFERS - pendingCount.get() - queuedCount);
        return free * chunkBytes + (chunkBytes - stagePos);
    }

    void drain() {
        long deadline = System.currentTimeMillis() + 15000;
        while (!destroyed && writtenBytes - consumedBytes > 0 && System.currentTimeMillis() < deadline) {
            engine.wake();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    void flush() {
        stagePos = 0;
        engine.runSync(() -> {
            stopSourceInternal();
            clearPending();
        });
    }

    void start() {
        engine.runSync(() -> {
            started = true;
            if (!playing && queuedCount > 0) {
                AL10.alSourcePlay(source);
                playing = true;
                onStart.run();
            }
        });
    }

    void stop() {
        engine.runSync(this::stopSourceInternal);
    }

    private void stopSourceInternal() {
        started = false;
        if (playing) {
            AL10.alSourceStop(source);
            playing = false;
            onStop.run();
        }
    }

    @Override
    public void applyGain(float gain) {
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
    }

    // ------------------------------------------------------------------ worker side

    @Override
    public void tick() {
        // Reclaim buffers the device finished playing.
        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        while (processed-- > 0) {
            int buf = AL10.alSourceUnqueueBuffers(source);
            for (int i = 0; i < queuedCount; i++) {
                if (ringBufs[i] == buf) {
                    consumedBytes += queuedLens[i];
                    recycle(queuedArrays[i]);
                    releaseQueued(i, buf);
                    break;
                }
            }
        }

        // Feed free OpenAL buffers with pending chunks.
        Chunk chunk;
        while (queuedCount < NUM_BUFFERS && (chunk = pending.poll()) != null) {
            pendingCount.decrementAndGet();
            int buf = ringBufs[queuedCount];
            uploadScratch.clear();
            uploadScratch.put(chunk.data, 0, chunk.len);
            uploadScratch.flip();
            AL10.alBufferData(buf, alFormat, uploadScratch, Math.round(format.getSampleRate()));
            AL10.alSourceQueueBuffers(source, buf);
            queuedLens[queuedCount] = chunk.len;
            queuedArrays[queuedCount] = chunk.data;
            queuedCount++;
        }

        int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
        if (!playing && started && queuedCount > 0 && state != AL10.AL_PLAYING) {
            AL10.alSourcePlay(source);
            playing = true;
            onStart.run();
        }
        if (playing && queuedCount == 0 && pending.isEmpty() && state != AL10.AL_PLAYING) {
            // The stream ran dry: report STOP so the writer can pick up again later.
            playing = false;
            onStop.run();
        }
    }

    private void clearPending() {
        Chunk chunk;
        while ((chunk = pending.poll()) != null) {
            pendingCount.decrementAndGet();
            recycle(chunk.data);
        }
        // After alSourceStop every queued buffer counts as processed.
        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        while (processed-- > 0) {
            int buf = AL10.alSourceUnqueueBuffers(source);
            for (int i = 0; i < queuedCount; i++) {
                if (ringBufs[i] == buf) {
                    recycle(queuedArrays[i]);
                    releaseQueued(i, buf);
                    break;
                }
            }
        }
    }

    /** Removes the queued entry at {@code index}; the buffer id returns to the free tail. */
    private void releaseQueued(int index, int buf) {
        for (int i = index; i < queuedCount - 1; i++) {
            ringBufs[i] = ringBufs[i + 1];
            queuedLens[i] = queuedLens[i + 1];
            queuedArrays[i] = queuedArrays[i + 1];
        }
        queuedCount--;
        ringBufs[queuedCount] = buf;
        queuedLens[queuedCount] = 0;
        queuedArrays[queuedCount] = null;
    }

    private void recycle(byte[] chunk) {
        if (chunk == null) {
            return;
        }
        synchronized (poolLock) {
            freeChunks.add(chunk);
            poolLock.notifyAll();
        }
    }

    @Override
    public void destroy() {
        destroyed = true;
        synchronized (poolLock) {
            poolLock.notifyAll();
        }
        AL10.alSourceStop(source);
        clearPending();
        for (int i = 0; i < NUM_BUFFERS; i++) {
            if (ringBufs[i] >= 0) {
                AL10.alDeleteBuffers(ringBufs[i]);
                ringBufs[i] = -1;
            }
        }
        AL10.alDeleteSources(source);
    }

    private static final class Chunk {
        final byte[] data;
        final int len;

        Chunk(byte[] data, int len) {
            this.data = data;
            this.len = len;
        }
    }
}
