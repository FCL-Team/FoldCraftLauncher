package org.lwjgl.openal.jsound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.sampled.LineUnavailableException;

/**
 * Owns the single ALC device/context that backs every javax.sound line, and drives
 * all OpenAL calls from one dedicated worker thread (OpenAL contexts are per-thread).
 * The worker thread is only started when the first line is opened, so games that
 * never touch javax.sound pay nothing.
 */
final class OpenALEngine {

    private static final long READY_TIMEOUT_MS = 5000;
    private static final long SYNC_TIMEOUT_MS = 5000;

    static final OpenALEngine INSTANCE = new OpenALEngine();

    private final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();
    private final List<Voice> voices = new CopyOnWriteArrayList<>();
    private final Object wake = new Object();
    private final CountDownLatch readyLatch = new CountDownLatch(1);
    private final AtomicBoolean workerStarted = new AtomicBoolean(false);

    private volatile Thread worker;
    private volatile boolean ready;
    private volatile boolean failed;
    private volatile boolean captureSupported;
    private volatile boolean shutdown;

    private long device;
    private long context;

    private OpenALEngine() {
    }

    static OpenALEngine get() {
        return INSTANCE;
    }

    List<Voice> voices() {
        return voices;
    }

    boolean isCaptureSupported() {
        return captureSupported;
    }

    /** Blocks until the device/context is usable or definitively failed. */
    void awaitReady() throws LineUnavailableException {
        if (ready) {
            return;
        }
        if (shutdown) {
            throw new LineUnavailableException("JSound engine is shutting down");
        }
        ensureWorkerStarted();
        try {
            if (!readyLatch.await(READY_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new LineUnavailableException("Timed out waiting for the OpenAL device");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LineUnavailableException("Interrupted while waiting for the OpenAL device");
        }
        if (failed) {
            throw new LineUnavailableException("OpenAL device is not available, see the launcher log");
        }
    }

    void submit(Runnable task) {
        tasks.add(task);
        wake();
    }

    void runSync(Runnable task) {
        AtomicReference<Throwable> error = new AtomicReference<>();
        runSyncChecked(() -> {
            task.run();
            return null;
        }, error);
        Throwable t = error.get();
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if (t instanceof Error) {
            throw (Error) t;
        }
        if (t != null) {
            throw new RuntimeException(t);
        }
    }

    <T> T call(Callable<T> task) {
        AtomicReference<Throwable> error = new AtomicReference<>();
        T result = runSyncChecked(task, error);
        Throwable t = error.get();
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if (t instanceof Error) {
            throw (Error) t;
        }
        if (t != null) {
            throw new RuntimeException(t);
        }
        return result;
    }

    private <T> T runSyncChecked(Callable<T> task, AtomicReference<Throwable> error) {
        Thread current = Thread.currentThread();
        Thread engineThread = worker;
        if (engineThread != null && current == engineThread) {
            return callQuietly(task, error);
        }
        if (failed) {
            throw new IllegalStateException("OpenAL device is not available");
        }
        final AtomicReference<Object> result = new AtomicReference<>();
        CountDownLatch done = new CountDownLatch(1);
        tasks.add(() -> {
            try {
                result.set(callQuietly(task, error));
            } finally {
                done.countDown();
            }
        });
        wake();
        try {
            if (!done.await(SYNC_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException("Timed out waiting for the OpenAL worker thread");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the OpenAL worker thread");
        }
        @SuppressWarnings("unchecked")
        T value = (T) result.get();
        return value;
    }

    private static <T> T callQuietly(Callable<T> task, AtomicReference<Throwable> error) {
        try {
            return task.call();
        } catch (Throwable t) {
            error.compareAndSet(null, t);
            return null;
        }
    }

    void wake() {
        synchronized (wake) {
            wake.notifyAll();
        }
    }

    void shutdown() {
        shutdown = true;
        wake();
    }

    private void ensureWorkerStarted() {
        if (workerStarted.compareAndSet(false, true)) {
            Thread t = new Thread(this::run, "MioJSound-OpenAL");
            t.setDaemon(true);
            worker = t;
            t.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                shutdown();
                try {
                    t.join(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "JSound-Shutdown"));
        }
    }

    private void run() {
        boolean ok = false;
        for (int attempt = 0; attempt < 3 && !ok && !shutdown; attempt++) {
            ok = setupContext();
            if (!ok) {
                sleepQuiet(400);
            }
        }
        if (!ok) {
            failed = true;
            readyLatch.countDown();
            JSoundLog.error("[JSound] OpenAL device could not be opened, javax.sound.sampled stays unavailable");
            return;
        }
        ready = true;
        readyLatch.countDown();
        JSoundLog.info("[JSound] OpenAL device ready, javax.sound.sampled is now routed to OpenAL");
        while (!shutdown) {
            drainTasks();
            tickVoices();
            long waitMs = tasks.isEmpty() && voices.isEmpty() ? 200 : 10;
            synchronized (wake) {
                try {
                    wake.wait(waitMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        destroyContext();
    }

    private boolean setupContext() {
        try {
            long dev = ALC10.alcOpenDevice((ByteBuffer) null);
            if (dev == 0L) {
                JSoundLog.error("[JSound] alcOpenDevice failed");
                return false;
            }
            // Loads the OpenAL native library itself (honours org.lwjgl.openal.libname).
            ALCCapabilities alcCaps = ALC.createCapabilities(dev);
            if (!alcCaps.OpenALC10) {
                ALC10.alcCloseDevice(dev);
                JSoundLog.error("[JSound] OpenAL implementation does not support ALC 1.0");
                return false;
            }
            long ctx = ALC10.alcCreateContext(dev, (IntBuffer) null);
            if (ctx == 0L) {
                ALC10.alcCloseDevice(dev);
                JSoundLog.error("[JSound] alcCreateContext failed");
                return false;
            }
            if (!ALC10.alcMakeContextCurrent(ctx)) {
                ALC10.alcDestroyContext(ctx);
                ALC10.alcCloseDevice(dev);
                JSoundLog.error("[JSound] alcMakeContextCurrent failed");
                return false;
            }
            AL.createCapabilities(alcCaps);
            device = dev;
            context = ctx;
            captureSupported = alcCaps.ALC_EXT_CAPTURE;
            return true;
        } catch (Throwable t) {
            JSoundLog.error("[JSound] OpenAL context setup failed", t);
            return false;
        }
    }

    private void destroyContext() {
        try {
            if (context != 0L) {
                ALC10.alcMakeContextCurrent(0L);
                ALC10.alcDestroyContext(context);
                context = 0L;
            }
            if (device != 0L) {
                ALC10.alcCloseDevice(device);
                device = 0L;
            }
        } catch (Throwable t) {
            JSoundLog.error("[JSound] Error while closing the OpenAL device", t);
        }
    }

    private void drainTasks() {
        Runnable task;
        while ((task = tasks.poll()) != null) {
            try {
                task.run();
            } catch (Throwable t) {
                JSoundLog.error("[JSound] Worker task failed", t);
            }
        }
    }

    private void tickVoices() {
        for (Voice voice : voices) {
            try {
                voice.tick();
            } catch (Throwable t) {
                JSoundLog.error("[JSound] Voice tick failed", t);
            }
        }
    }

    static ByteBuffer allocate(int bytes) {
        return BufferUtils.createByteBuffer(bytes);
    }

    private static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}