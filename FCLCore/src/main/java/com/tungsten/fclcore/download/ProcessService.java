package com.tungsten.fclcore.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class ProcessService extends Service {

    public static final int PROCESS_SERVICE_PORT = 29118;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ProcessService started!");
        String[] command = intent.getExtras().getStringArray("command");
        FCLConfig config = new FCLConfig(
                getApplicationContext(),
                getApplicationContext().getExternalFilesDir("log").getAbsolutePath(),
                getApplicationContext().getDir("runtime", 0).getAbsolutePath() + "/java/jre8",
                getApplicationContext().getCacheDir() + "/fclauncher",
                null,
                command);
        startProcess(config);
        return super.onStartCommand(intent, flags, startId);
    }

    public void startProcess(FCLConfig config) {
        FCLBridgeCallback callback = new FCLBridgeCallback() {
            @Override
            public void onCursorModeChange(int mode) {
                // Ignore
            }

            @Override
            public void onExit(int code) {
                sendCode(code);
            }
        };
        CompletableFuture<Integer> future = FCLauncher.launchAPIInstaller(config, callback);
        future.whenComplete((integer, throwable) -> sendCode(integer));
    }

    private void sendCode(int code) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(new InetSocketAddress("127.0.0.1", PROCESS_SERVICE_PORT));
            byte[] data = (code + "").getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.send(packet);
            socket.close();
            System.out.println("Code = " + code + ", send the code now!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        System.out.println("Destroy the service now!");
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
