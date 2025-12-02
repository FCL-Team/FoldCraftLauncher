package net.burningtnt.terracotta;

import android.content.Context;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>An API to handle Terracotta Android.</p>
 *
 * <p>Unlike normal JNI bindings, relocating this class to another package is supported.</p>
 *
 * <h1>State Definition</h1>
 *
 * <p>For Android platform, developers must invoke {@link #initialize} with a {@link VpnServiceCallback} to initialize the rust backend.
 * Then, {@link #getState()}, {@link #setWaiting()}, {@link #setGuesting}, {@link #setScanning} are available to hook states from Terracotta.</p>
 *
 * <p>All methods here are thread-safe and can be invoked concurrently from multiple threads.</p>
 *
 * <p>For each state, self-increased {@code index} and {@code state} fields are provided.
 * A state with a greater {@code index} should be considered as a new state, while {@code state} reveals the type of the specific state.</p>
 *
 * <p>For state definitions, view <a href="https://github.com/HMCL-dev/HMCL/blob/main/HMCL/src/main/java/org/jackhuang/hmcl/terracotta/TerracottaState.java#L108-L120">all subclasses of Ready</a></p>
 *
 * <h1>VpnService</h1>
 *
 * <p>Terracotta will submit VpnService Requests when EasyTier is acquiring one.
 * To configure the callback for receiving requests, see {@link #initialize}</p>
 *
 * <p>When receiving one, {@link #getPendingVpnServiceRequest()} is available to get the pending request.
 * Developer must make sure either {@link VpnServiceRequest#startVpnService} or {@link VpnServiceRequest#reject()} is invoked,
 * or Terracotta would stuck and EasyTier cannot submit a new VpnService Request.</p>
 *
 * <p>The VpnServiceRequest must be fulfilled in 30 seconds, or it will be considered as timeout.</p>
 *
 * <h1>Panic</h1>
 *
 * <p>A RuntimeException will be thrown if an error occured in native level.</p>
 */
public final class TerracottaAndroidAPI {
    /**
     * <p>Callback for receiving VpnService Requests</p>
     *
     * <p>When receiving one, {@link #getPendingVpnServiceRequest()} is available to get the pending request.
     * Developer must make sure either {@link VpnServiceRequest#startVpnService} or {@link VpnServiceRequest#reject()} is invoked,
     * or Terracotta would stuck and EasyTier cannot submit a new VpnService Request.</p>
     *
     * @implNote The VpnServiceRequest must be fulfilled in 30 seconds, or it will be considered as timeout.
     */
    public interface VpnServiceCallback {
        void onStartVpnService();
    }

    /**
     * <p>A VpnService Request submitted by Terracotta. See {@link VpnServiceCallback}</p>
     */
    public interface VpnServiceRequest {
        /**
         * Create a Vpn Connection and fulfill the VpnService Request.
         *
         * @param builder A pre-configured VpnService builder.
         * @return The established Vpn Connection. Developers must close this file descriptor after EasyTier exits.
         * @throws RuntimeException if {@link VpnService.Builder#establish()} returns null.
         * @implNote Developers is able to configure the builder before passing it into Terracotta
         * to fully-custom the connection.
         */
        ParcelFileDescriptor startVpnService(VpnService.Builder builder);

        /**
         * <p>Reject the VpnServiceRequest.</p>
         */
        void reject();
    }

    /**
     * <p>Metadata of Terracotta Android</p>
     */
    public static final class Metadata {
        private final String terracottaVersion;

        private final long terracottaCompileTime;

        private final String easyTierVersion;

        public Metadata(String terracottaVersion, long terracottaCompileTime, String easyTierVersion) {
            this.terracottaVersion = terracottaVersion;
            this.terracottaCompileTime = terracottaCompileTime;
            this.easyTierVersion = easyTierVersion;
        }

        /**
         * @return Terracotta Android version.
         */
        public String getTerracottaVersion() {
            return terracottaVersion;
        }

        /**
         * @return Terracotta Android compile timestamp. The format is identical with {@link System#currentTimeMillis()}.
         */
        public long getTerracottaCompileTime() {
            return terracottaCompileTime;
        }

        /**
         * @return EasyTier version.
         */
        public String getEasyTierVersion() {
            return easyTierVersion;
        }
    }

    static {
        System.setProperty("net.burningtnt.terracotta.native_location", TerracottaAndroidAPI.class.getName().replace('.', '/'));
        System.loadLibrary("terracotta");
    }

    private static volatile VpnServiceRequest pendingRequest = null;

    private static final class RuntimeContext {
        private final VpnServiceCallback vpnServiceCallback;

        private final RandomAccessFile logging;

        public RuntimeContext(VpnServiceCallback vpnServiceCallback, RandomAccessFile logging) {
            this.vpnServiceCallback = vpnServiceCallback;
            this.logging = logging;
        }
    }

    private static volatile RuntimeContext runtimeContext = null;

    /**
     * <p>Get current pending VpnService Request.</p>
     *
     * @return The pending VpnService Request.
     * @throws IllegalStateException if no pending VpnService Request exists.
     */
    public static VpnServiceRequest getPendingVpnServiceRequest() {
        VpnServiceRequest handle = pendingRequest;
        if (handle == null) {
            throw new IllegalStateException("There's no pending VpnService request.");
        }
        return handle;
    }

    /**
     * <p>Initialize the Terracotta Android.</p>
     *
     * @param context  An Android context object.
     * @param callback A callback to handle VpnService for EasyTier. See {@link VpnServiceCallback} for more information.
     */
    public static synchronized Metadata initialize(Context context, VpnServiceCallback callback) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(callback, "callback");

        if (runtimeContext != null) {
            throw new IllegalStateException("Terracotta Android has already started.");
        }

        File root = new File(context.getFilesDir(), "net.burningtnt.terracotta");

        File base = new File(root, "rs");
        base.mkdirs();
        if (!base.isDirectory()) {
            throw new RuntimeException("Cannot create net.burningtnt.terracotta/rs directory.");
        }

        RandomAccessFile logging;
        int fd;
        try {
            logging = new RandomAccessFile(new File(root, "application.log"), "rw");
            fd = ParcelFileDescriptor.dup(logging.getFD()).detachFd();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int code = start0(base.getAbsolutePath(), fd);
        if (code != 0) {
            throw new RuntimeException("Cannot start Terracotta Android: " + code);
        }

        runtimeContext = new RuntimeContext(callback, logging);

        String[] parts = getMetadata0().split("\0", 4);
        if (parts.length != 3) {
            throw new AssertionError("Should NOT be here.");
        }
        return new Metadata(parts[0], Long.parseLong(parts[1]), parts[2]);
    }

    /**
     * <p>Fetch current state from Terracotta Android.</p>
     *
     * @return A json representing the current state. See {@link TerracottaAndroidAPI} for state definitions.
     * @throws IllegalStateException if Terracotta Android hasn't been initialized.
     * @implNote Usually, this method doesn't take a long time to fetch states.
     * However, when initializing the EasyTier, state fetching may block for ~1 seconds.
     */
    public static String getState() {
        assertStarted();
        return getState0();
    }

    /**
     * <p>Set Terracotta Android into 'waiting' state.</p>
     *
     * @throws IllegalStateException if Terracotta Android hasn't been initialized.
     * @implNote Usually, this method doesn't take a long time to fetch states.
     * However, when initializing the EasyTier, state fetching may block for ~1 seconds.
     */
    public static void setWaiting() {
        assertStarted();
        setWaiting0();
    }

    /**
     * <p>Set Terracotta Android into 'host-scanning' state.</p>
     *
     * @param player the player's name. A default value will be taken if it's null.
     * @throws IllegalStateException if Terracotta Android hasn't been initialized.
     * @implNote Usually, this method doesn't take a long time to fetch states.
     * However, when initializing the EasyTier, state fetching may block for ~1 seconds.
     */
    public static void setScanning(@Nullable String room, @Nullable String player) {
        assertStarted();
        setScanning0(room, player);
    }

    /**
     * <p>Set Terracotta Android into 'guest-connecting' state.</p>
     *
     * @param room   the room code. False will be returned if it's invalid.
     * @param player the player's name. A default value will be taken if it's null.
     * @return True if room code is valid, false otherwise.
     * @throws IllegalStateException if Terracotta Android hasn't been initialized.
     * @throws NullPointerException  if room is null.
     * @implNote Usually, this method doesn't take a long time to fetch states.
     * However, when initializing the EasyTier, state fetching may block for ~1 seconds.
     */
    public static boolean setGuesting(String room, @Nullable String player) {
        Objects.requireNonNull(room, "room");

        assertStarted();
        return setGuesting0(room, player);
    }

    /**
     * Room types supported by Terracotta Android
     */
    public enum RoomType {
        TERRACOTTA_LEGACY, PCL2CE, SCAFFOLDING
    }

    /**
     * <p>Parse the given room code.</p>
     *
     * @param room A room code.
     * @return The type of the room, or null if invalid.
     * @throws NullPointerException if room is null
     * @implNote Though this method is fast enough to be invoked in UI thread,
     * dispatch all invocation to a separated worker is better in order to
     * prevent potential UI freezing issues.
     */
    public static RoomType parseRoomCode(String room) {
        Objects.requireNonNull(room, "room");

        assertStarted();
        switch (verifyRoomCode0(room)) {
            case -1:
                return null;
            case 1:
                return RoomType.TERRACOTTA_LEGACY;
            case 2:
                return RoomType.PCL2CE;
            case 3:
                return RoomType.SCAFFOLDING;
            default:
                throw new AssertionError("Should NOT be here.");
        }
    }

    /**
     * <p>Collect logs of Terracotta Android.</p>
     *
     * <p>Developers must immediately copy all data out of the returned reader and close it.
     * Otherwise all methods, except 'parseRoomCode', will be blocked.</p>
     *
     * @return A reader containing logs.
     * @throws RuntimeException if logging export is unsupported.
     */
    public static Reader collectLogs() throws IOException {
        assertStarted();

        long ptr = prepareExportLogs0();
        if (ptr == 0) {
            throw new RuntimeException("Cannot export logs from Terracotta Android.");
        }

        RandomAccessFile file = runtimeContext.logging;
        file.seek(0);

        return new BufferedReader(new InputStreamReader(new InputStream() {
            private final AtomicBoolean closed = new AtomicBoolean(false);

            @Override
            public int read() throws IOException {
                assertOpen();
                return file.read();
            }

            @Override
            public int read(byte[] b) throws IOException {
                assertOpen();
                return file.read(b);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                assertOpen();
                return file.read(b, off, len);
            }

            @Override
            public int available() throws IOException {
                assertOpen();
                return Math.toIntExact(file.length() - file.getFilePointer());
            }

            @Override
            public void close() throws IOException {
                super.close();
                cleanup();
            }

            @Override
            protected void finalize() throws Throwable {
                super.finalize();
                cleanup();
            }

            private void assertOpen() throws IOException {
                if (closed.get()) {
                    throw new IOException("Stream has already been closed");
                }
            }

            private void cleanup() {
                if (closed.compareAndSet(false, true)) {
                    finishExportLogs0(ptr);
                }
            }
        }, StandardCharsets.UTF_8));
    }

    /**
     * @deprecated This API is exposed for debug purpose.
     */
    @Deprecated
    public static void panic() {
        panic0();
        throw new AssertionError("Should NOT be here: A RuntimeException should be thrown in panic0");
    }

    private static final long FD_PENDING = ((long) Integer.MAX_VALUE) + 1;
    private static final long FD_REJECT = FD_PENDING + 1;
    ;
    @SuppressWarnings("unused") // Native callback
    private static int onVpnServiceStateChanged(byte ip1, byte ip2, byte ip3, byte ip4, short network_length, String cidr) throws UnknownHostException {
        if (pendingRequest != null) {
            throw new AssertionError("Should NOT be here.");
        }

        AtomicLong fd = new AtomicLong(FD_PENDING);
        InetAddress address = InetAddress.getByAddress(new byte[]{ip1, ip2, ip3, ip4});

        pendingRequest = new VpnServiceRequest() {
            @Override
            public ParcelFileDescriptor startVpnService(VpnService.Builder builder) {
                builder.addAddress(address, network_length)
                        .addDnsServer("223.5.5.5")
                        .addDnsServer("114.114.114.114");

                if (!cidr.isEmpty()) {
                    for (String part : cidr.split("\0")) {
                        String[] parts = part.split("/", 3);
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Illegal CIDR: " + Arrays.toString(parts));
                        }
                        builder.addRoute(parts[0], Integer.parseInt(parts[1]));
                    }
                }

                ParcelFileDescriptor connection = builder.establish();
                if (connection == null) {
                    throw new RuntimeException("Cannot establish a VPN connection.");
                }

                fd.set(connection.getFd());
                return connection;
            }

            @Override
            public void reject() {
                fd.set(FD_REJECT);
            }
        };

        TerracottaAndroidAPI.runtimeContext.vpnServiceCallback.onStartVpnService();

        long timestamp = System.currentTimeMillis();
        while (true) {
            long value = fd.get();
            if (value == FD_PENDING) {
                if (System.currentTimeMillis() - timestamp >= 30000) {
                    Log.wtf("TerracottaAndroidAPI", "VpnService Request hasn't been fulfilled in 30s.");
                    throw new IllegalStateException();
                }
                Thread.yield();
            } else if (value == FD_REJECT) {
                pendingRequest = null;
                throw new IllegalStateException();
            } else {
                pendingRequest = null;

                if ((int) value != value) {
                    throw new AssertionError("Should NOT be here.");
                }
                return (int) value;
            }
        }
    }

    private static void assertStarted() {
        if (runtimeContext == null) {
            throw new IllegalStateException("Terracotta Android hasn't started yet.");
        }
    }

    private static native int start0(String baseDir, int loggingFD);

    private static native String getState0();

    private static native void setWaiting0();

    private static native void setScanning0(String room, String player);

    private static native boolean setGuesting0(String room, String player);

    private static native int verifyRoomCode0(String room);

    private static native String getMetadata0();

    private static native long prepareExportLogs0();

    private static native void finishExportLogs0(long pointer);

    private static native void panic0();
}