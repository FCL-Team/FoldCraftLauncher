package com.tungsten.fcl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.tungsten.fclauncher.FCLConfig;
import com.tungsten.fclauncher.FCLauncher;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            //AssetsUtils.getInstance(MainActivity.this).copyOnMainThread("java",getFilesDir() + "/java");
            //AssetsUtils.getInstance(MainActivity.this).copyOnMainThread("app_runtime",getFilesDir().getParent() + "/app_runtime");
        }).start();

        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                try {
                    init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1000);
            }
        } else {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                try {
                    init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("a")
                        .setPositiveButton("OK", (dialog1, which) ->
                                requestPermission())
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
        }
    }

    private void init() throws IOException {
        String[] args3 = new String[] {
                getFilesDir() + "/java/bin/java",
                "-cp",
                "/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-jemalloc.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-tinyfd.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-opengl.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-openal.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-glfw.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl-stb.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl3/lwjgl.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/patchy/1.3.9/patchy-1.3.9.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/google/guava/guava/21.0/guava-21.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/brigadier/1.0.17/brigadier-1.0.17.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/authlib/2.1.28/authlib-2.1.28.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/ca/weblite/java-objc-bridge/1.0.0/java-objc-bridge-1.0.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/ca/weblite/java-objc-bridge/1.0.0/java-objc-bridge-1.0.0.jar:/storage/emulated/0/HMCLPE/.minecraft/versions/1.16.5/1.16.5.jar",
                "-Dfml.earlyprogresswindow=false",
                "-Dorg.lwjgl.util.Debug=true",
                "-Dorg.lwjgl.util.DebugFunctions=true",
                "-Dorg.lwjgl.util.DebugLoader=true",
                "-Dos.name=Linux",
                "-Dlwjgl.platform=FCL",
                "-Dorg.lwjgl.opengl.libname=${glLibName}",
                "-Djava.io.tmpdir=/data/user/0/com.tungsten.fcl/cache",
                "-Dminecraft.launcher.brand=FCL",
                "-Dminecraft.launcher.version=1.0.0",
                "-Xms1024M",
                "-Xmx1024M",
                "net.minecraft.client.main.Main",
                "--username",
                "Tunsgten",
                "--version",
                "1.16.5",
                "--gameDir",
                "/storage/emulated/0/HMCLPE/.minecraft/versions/1.16.5",
                "--assetsDir",
                "/storage/emulated/0/HMCLPE/.minecraft/assets",
                "--assetIndex",
                "1.16",
                "--uuid",
                "410efec2-313f-4346-aa2a-7708597ff92f",
                "--accessToken",
                "eyJhbGciOiJIUzI1NiJ9.eyJ4dWlkIjoiMjUzNTQyNTU3Mzk1OTc0MCIsImFnZyI6IkFkdWx0Iiwic3ViIjoiNzcyMDU1YzMtMjNhMi00NmE0LThjOTItODRiOTE5NDVkOTFhIiwibmJmIjoxNjY1NTc0MzExLCJhdXRoIjoiWEJPWCIsInJvbGVzIjpbXSwiaXNzIjoiYXV0aGVudGljYXRpb24iLCJleHAiOjE2NjU2NjA3MTEsImlhdCI6MTY2NTU3NDMxMSwicGxhdGZvcm0iOiJVTktOT1dOIiwieXVpZCI6IjFhYTRlNDM5MGVmMTgwZWE1M2Q2MjBkMzEyM2Y5YzE3In0.8jpLk9Wr_g-SxeJrHtGC7vSxu54I6gw-0g6hSdQiSYk",
                "--userType",
                "mojang",
                "--versionType",
                "release",
                "--width",
                "2000",
                "--height",
                "1200"
        };

        String[] args2 = new String[] {
                getFilesDir() + "/java/bin/java",
                "-cp",
                "/data/user/0/com.tungsten.fcl/app_runtime/lwjgl2/lwjgl.jar:/data/user/0/com.tungsten.fcl/app_runtime/lwjgl2/lwjgl_util.jar:/storage/emulated/0/HMCLPE/.minecraft/versions/1.7.10/1.7.10.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/netty/1.8.8/netty-1.8.8.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/realms/1.3.5/realms-1.3.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/google/guava/guava/15.0/guava-15.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/com/mojang/authlib/1.5.21/authlib-1.5.21.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:/storage/emulated/0/HMCLPE/.minecraft/libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar",
                "-Dfml.earlyprogresswindow=false",
                "-Dorg.lwjgl.util.Debug=true",
                "-Dorg.lwjgl.util.DebugFunctions=true",
                "-Dorg.lwjgl.util.DebugLoader=true",
                "-Dos.name=Linux",
                "-Dlwjgl.platform=FCL",
                "-Dorg.lwjgl.opengl.libname=%s",
                "-Djava.io.tmpdir=/data/user/0/com.tungsten.fcl/cache",
                "-Dminecraft.launcher.brand=FCL",
                "-Dminecraft.launcher.version=1.0.0",
                "-Xms1024M",
                "-Xmx1024M",
                "net.minecraft.client.main.Main",
                "--username",
                "Tunsgten",
                "--version",
                "1.7.10",
                "--gameDir",
                "/storage/emulated/0/HMCLPE/.minecraft/versions/1.7.10",
                "--assetsDir",
                "/storage/emulated/0/HMCLPE/.minecraft/assets",
                "--assetIndex",
                "1.7.10",
                "--uuid",
                "410efec2-313f-4346-aa2a-7708597ff92f",
                "--accessToken",
                "eyJhbGciOiJIUzI1NiJ9.eyJ4dWlkIjoiMjUzNTQyNTU3Mzk1OTc0MCIsImFnZyI6IkFkdWx0Iiwic3ViIjoiNzcyMDU1YzMtMjNhMi00NmE0LThjOTItODRiOTE5NDVkOTFhIiwibmJmIjoxNjY1NTc0MzExLCJhdXRoIjoiWEJPWCIsInJvbGVzIjpbXSwiaXNzIjoiYXV0aGVudGljYXRpb24iLCJleHAiOjE2NjU2NjA3MTEsImlhdCI6MTY2NTU3NDMxMSwicGxhdGZvcm0iOiJVTktOT1dOIiwieXVpZCI6IjFhYTRlNDM5MGVmMTgwZWE1M2Q2MjBkMzEyM2Y5YzE3In0.8jpLk9Wr_g-SxeJrHtGC7vSxu54I6gw-0g6hSdQiSYk",
                "--userProperties",
                "{}",
                "--userType",
                "mojang",
                "--width",
                "2000",
                "--height",
                "1200"
        };

        TextureView textureView = findViewById(R.id.main);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                FCLConfig config = new FCLConfig(MainActivity.this,
                        new Surface(surfaceTexture),
                        getExternalFilesDir("log").getAbsolutePath(),
                        getFilesDir() + "/java",
                        Environment.getExternalStorageDirectory() + "/HMCLPE/.minecraft",
                        FCLConfig.Renderer.RENDERER_GL4ES,
                        args3,
                        new FCLBridgeCallback() {
                            @Override
                            public void onCursorModeChange(int mode) {

                            }

                            @Override
                            public void onExit(int code) {

                            }
                        });
                FCLauncher.launchMinecraft(config);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

            }
        });
    }
}