package com.tungsten.fclcore.download;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.logging.Level;

public class ProcessService extends Service {

    public static final int PROCESS_SERVICE_PORT = 29118;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] command = intent.getExtras().getStringArray("command");
        int java = intent.getExtras().getInt("java");
        String jre = "jre" + java;
        FCLConfig config = new FCLConfig(
                getApplicationContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/FCL/log",
                getApplicationContext().getDir("runtime", 0).getAbsolutePath() + "/java/" + jre,
                getApplicationContext().getCacheDir() + "/fclauncher",
                null,
                command
        );
        startProcess(config);
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean firstLog = true;

    public void startProcess(FCLConfig config) {
        FCLBridge bridge = FCLauncher.launchAPIInstaller(config);
        FCLBridgeCallback callback = new FCLBridgeCallback() {
            @Override
            public void onCursorModeChange(int mode) {
                // Ignore
            }

            @Override
            public void onHitResultTypeChange(int type) {
                // Ignore
            }

            @Override
            public void onLog(String log) {
                try {
                    if (firstLog) {
                        FileUtils.writeText(new File(bridge.getLogPath()), log + "\n");
                        firstLog = false;
                    } else {
                        FileUtils.writeTextWithAppendMode(new File(bridge.getLogPath()), log + "\n");
                    }
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Can't log game log to target file", e.getMessage());
                }
            }

            @Override
            public void onExit(int code) {
                sendCode(code);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(() -> bridge.execute(null, callback), 1000);
    }

    private void sendCode(int code) {
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
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
