package com.tungsten.fcl.terracotta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.R;
import com.tungsten.fcl.terracotta.profile.ProfileKind;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyObjectWrapper;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.InvocationDispatcher;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fcllibrary.component.ResultListener;

import net.burningtnt.terracotta.TerracottaAndroidAPI;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;

public class Terracotta {

    public enum TerracottaMode {
        HOST,
        GUEST
    }

    private static boolean initialized = false;
    private static TerracottaAndroidAPI.Metadata metadata = null;
    private static TerracottaMode mode = null;
    private static InvalidationListener notificationListener = null;

    private static final AtomicReference<TerracottaState.Ready> STATE_V = new AtomicReference<>(null);
    private static final ReadOnlyObjectWrapper<TerracottaState.Ready> STATE = new ReadOnlyObjectWrapper<>(STATE_V.get());
    private static final InvocationDispatcher<TerracottaState.Ready> STATE_D = InvocationDispatcher.runOn(Schedulers.androidUIThread(), STATE::set);

    private static final int TERRACOTTA_USER_NOTICE_VERSION = 1;

    @Nullable
    public static TerracottaMode getMode() {
        return mode;
    }

    public static ReadOnlyObjectProperty<TerracottaState.Ready> stateProperty() {
        return STATE.getReadOnlyProperty();
    }

    public static int getUserNoticeVersion() {
        return TERRACOTTA_USER_NOTICE_VERSION;
    }

    public static void initialize(Activity context) {
        if (initialized)
            return;

        notificationListener = observable -> {
            TerracottaState.Ready state = stateProperty().get();
            if (state != null && !(state instanceof TerracottaState.Waiting)) {
                String stateText = AndroidUtils.getLocalizedText(context, "terracotta_status_" + state);
                updateVpnNotificationState(context, stateText);
            }
        };

        metadata = TerracottaAndroidAPI.initialize(context, () -> context.runOnUiThread(() -> {
            startTerracottaVpn(context);
            startNotificationListener();
        }));

        Lang.thread(() -> {
            while (true) {
                TerracottaState.Ready state = STATE.get();
                int index = state == null ? -1 : state.index;
                String stateJson = TerracottaAndroidAPI.getState();
                TerracottaState.Ready object = JsonUtils.fromNonNullJson(stateJson, new TypeToken<TerracottaState.Ready>() {}.getType());
                TerracottaState.Ready next = object.index <= index ? null : object;
                if (next != null) {
                    compareAndSet(state, next);
                }
                LockSupport.parkNanos(500_000);
            }
        }, "Terracotta Background Daemon", true);

        initialized = true;
    }

    public static void setWaiting(Context context, boolean manual) {
        if (!initialized)
            return;

        stopNotificationListener();
        if (manual)
            stopTerracottaVpn(context);
        TerracottaAndroidAPI.setWaiting();
    }

    public static void setScanning(@Nullable String room, @Nullable String player) throws Exception {
        if (!initialized)
            throw new Exception("initialize Terracotta first!");
        if (!(stateProperty().get() instanceof TerracottaState.Waiting))
            throw new Exception("reset state to waiting first!");

        mode = TerracottaMode.HOST;
        TerracottaAndroidAPI.setScanning(room, player);
    }

    public static boolean setGuesting(String room, @Nullable String player) throws Exception {
        if (!initialized)
            throw new Exception("initialize Terracotta first!");
        if (!(stateProperty().get() instanceof TerracottaState.Waiting))
            throw new Exception("reset state to waiting first!");

        mode = TerracottaMode.GUEST;
        return TerracottaAndroidAPI.setGuesting(room, player);
    }

    public static String parseException(Context context, TerracottaState.Exception e) {
        return AndroidUtils.getLocalizedText(context, "terracotta_status_exception_desc_" + e.getType().name().toLowerCase(Locale.ROOT));
    }

    public static String parseProfileKind(Context context, ProfileKind kind) {
        return AndroidUtils.getLocalizedText(context, "terracotta_player_kind_" + kind.name().toLowerCase(Locale.ROOT));
    }

    public static String parseDifficulty(Context context, TerracottaState.GuestStarting.Difficulty difficulty) {
        return AndroidUtils.getLocalizedText(context, "terracotta_difficulty_" + difficulty.name().toLowerCase(Locale.ROOT));
    }

    @Nullable
    public static TerracottaAndroidAPI.RoomType parseRoomCode(String room) {
        if (!initialized || room == null)
            return null;

        return TerracottaAndroidAPI.parseRoomCode(room);
    }

    public static TerracottaAndroidAPI.Metadata getMetadata() {
        return metadata == null ? new TerracottaAndroidAPI.Metadata("unknown", 0, "unknown") : metadata;
    }

    @Nullable
    public static String collectLogs() {
        if (!initialized)
            return null;

        try (Reader reader = TerracottaAndroidAPI.collectLogs();
             StringWriter writer = new StringWriter()) {
            char[] buf = new char[4096];
            int n;
            while ((n = reader.read(buf)) != -1)
                writer.write(buf, 0, n);
            return writer.toString();
        } catch (IOException e) {
            Logging.LOG.log(Level.SEVERE, e.getMessage());
            return "Failed to collect logs: " + e.getMessage();
        }
    }

    /**
     * @deprecated This API is exposed for debug purpose.
     */
    @Deprecated
    public static void testNativePanic() {
        if (!initialized)
            return;

        TerracottaAndroidAPI.panic();
    }

    private static void startNotificationListener() {
        stateProperty().addListener(notificationListener);
    }

    private static void stopNotificationListener() {
        stateProperty().removeListener(notificationListener);
    }

    private static void startTerracottaVpn(Activity context) {
        Intent intent = VpnService.prepare(context);
        if (intent != null) {
            ResultListener.startActivityForResult(context, intent, RequestCodes.VPN_PERMISSION_CODE, (requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.VPN_PERMISSION_CODE) {
                    if (resultCode == Activity.RESULT_OK) {
                        Intent vpnIntent = new Intent(context, TerracottaVPNService.class).setAction(TerracottaVPNService.ACTION_START);
                        ContextCompat.startForegroundService(context, vpnIntent);
                    } else {
                        TerracottaAndroidAPI.getPendingVpnServiceRequest().reject();
                        setWaiting(context, true);
                        Toast.makeText(context, context.getString(R.string.terracotta_permission_vpn), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Intent vpnIntent = new Intent(context, TerracottaVPNService.class).setAction(TerracottaVPNService.ACTION_START);
            ContextCompat.startForegroundService(context, vpnIntent);
        }
    }

    private static void stopTerracottaVpn(Context context) {
        if (TerracottaVPNService.isRunning()) {
            Intent intent = new Intent(context, TerracottaVPNService.class).setAction(TerracottaVPNService.ACTION_STOP);
            ContextCompat.startForegroundService(context, intent);
        }
    }

    private static void updateVpnNotificationState(Context context, String stateText) {
        Intent intent = new Intent(context, TerracottaVPNService.class);
        intent.setAction(TerracottaVPNService.ACTION_UPDATE_STATE);
        intent.putExtra(TerracottaVPNService.EXTRA_STATE_TEXT, stateText);
        ContextCompat.startForegroundService(context, intent);
    }

    private static void compareAndSet(TerracottaState.Ready previous, TerracottaState.Ready next) {
        if (next == null)
            throw new AssertionError();

        if (STATE_V.compareAndSet(previous, next))
            STATE_D.accept(next);
    }
}
