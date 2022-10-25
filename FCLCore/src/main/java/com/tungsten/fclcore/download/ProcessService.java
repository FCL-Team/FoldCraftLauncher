package com.tungsten.fclcore.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclauncher.FCLPath;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ProcessService extends Service {

    public static final int PROCESS_SERVICE_PORT = 6868;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FCLPath.loadPaths(getApplicationContext());
        String[] commands = intent.getExtras().getStringArray("commands");
        startProcess(getApplicationContext(), commands);
        return super.onStartCommand(intent, flags, startId);
    }

    public void startProcess(Context context, String[] args) {
        FCLBridgeCallback callback = new FCLBridgeCallback() {
            @Override
            public void onCursorModeChange(int mode) {
                // Ignore
            }

            @Override
            public void onExit(int code) {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    socket.connect(new InetSocketAddress("127.0.0.1", PROCESS_SERVICE_PORT));
                    byte[] data = (code + "").getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    socket.send(packet);
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
        FCLConfig config = new FCLConfig(
                context,
                null,
                FCLPath.LOG_DIR,
                FCLPath.JAVA_8_PATH,
                FCLPath.CACHE_DIR,
                null,
                args,
                callback);
        FCLauncher.launchAPIInstaller(config);
    }

    @Override
    public void onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}
