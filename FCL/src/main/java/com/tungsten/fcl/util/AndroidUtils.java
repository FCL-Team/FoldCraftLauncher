package com.tungsten.fcl.util;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.WebActivity;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;

@SuppressLint("DiscouragedApi")
public class AndroidUtils {

    public static void openLink(Context context, String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void openLinkWithBuiltinWebView(Context context, String link) {
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", link);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(null, text);
        clip.setPrimaryClip(data);
        Toast.makeText(context, context.getString(R.string.message_copy), Toast.LENGTH_SHORT).show();
    }

    public static void clearWebViewCache(Context context) {
        File cache = context.getDir("webview", 0);
        FileUtils.deleteDirectoryQuietly(cache);
        CookieManager.getInstance().removeAllCookies(null);
    }

    public static String getLocalizedText(Context context, String key, Object... formatArgs) {
        return String.format(getLocalizedText(context, key), formatArgs);
    }

    public static String getLocalizedText(Context context, String key) {
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());
        if (resId != 0) {
            return context.getString(resId);
        } else {
            return key;
        }
    }

    public static boolean hasStringId(Context context, String key) {
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());
        return resId != 0;
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    public static int getScreenWidth(Activity context) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("theme", MODE_PRIVATE);
        boolean fullscreen = sharedPreferences.getBoolean("fullscreen", false);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getRealSize(point);
        if (fullscreen || SDK_INT < Build.VERSION_CODES.P) {
            return point.x;
        } else {
            try {
                Rect notchRect;
                if (SDK_INT >= Build.VERSION_CODES.S) {
                    notchRect = Objects.requireNonNull(wm.getCurrentWindowMetrics().getWindowInsets().getDisplayCutout()).getBoundingRects().get(0);
                } else {
                    notchRect = Objects.requireNonNull(context.getWindow().getDecorView().getRootWindowInsets().getDisplayCutout()).getBoundingRects().get(0);
                }
                return point.x - Math.min(notchRect.width(), notchRect.height());
            } catch (Exception e) {
                return point.x;
            }
        }
    }

    @SuppressWarnings("resource")
    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

    public static String copyFileToDir(Activity activity, Uri uri, File destDir) {
        String name = new File(uri.getPath()).getName();
        File dest = new File(destDir, name);
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new IOException("Failed to open content stream");
            }
            try (FileOutputStream outputStream = new FileOutputStream(dest)) {
                IOUtils.copyTo(inputStream, outputStream);
            }
            inputStream.close();
        } catch (Exception e) {

        }
        return dest.getAbsolutePath();
    }

    public static String copyFile(Activity activity, Uri uri, File dest) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new IOException("Failed to open content stream");
            }
            try (FileOutputStream outputStream = new FileOutputStream(dest)) {
                IOUtils.copyTo(inputStream, outputStream);
            }
            inputStream.close();
        } catch (Exception e) {

        }
        return dest.getAbsolutePath();
    }

    public static boolean isDocUri(Uri uri) {
        return Objects.equals(uri.getScheme(), ContentResolver.SCHEME_FILE) || Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT);
    }

    public static boolean isAdrenoGPU() {
        EGLDisplay eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to get EGL display");
            return false;
        }

        if (!EGL14.eglInitialize(eglDisplay, null, 0, null, 0)) {
            Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to initialize EGL");
            return false;
        }

        int[] eglAttributes = new int[]{
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_NONE
        };

        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(eglDisplay, eglAttributes, 0, configs, 0, 1, numConfigs, 0) || numConfigs[0] == 0) {
            EGL14.eglTerminate(eglDisplay);
            Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to choose an EGL config");
            return false;
        }

        int[] contextAttributes = new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };

        EGLContext context = EGL14.eglCreateContext(eglDisplay, configs[0], EGL14.EGL_NO_CONTEXT, contextAttributes, 0);
        if (context == EGL14.EGL_NO_CONTEXT) {
            EGL14.eglTerminate(eglDisplay);
            Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to create EGL context");
            return false;
        }

        if (!EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, context)) {
            EGL14.eglDestroyContext(eglDisplay, context);
            EGL14.eglTerminate(eglDisplay);
            Logging.LOG.log(Level.SEVERE, "CheckVendor: Failed to make EGL context current");
            return false;
        }

        String vendor = GLES20.glGetString(GLES20.GL_VENDOR);
        String renderer = GLES20.glGetString(GLES20.GL_RENDERER);
        boolean isAdreno = (vendor != null && renderer != null &&
                vendor.equalsIgnoreCase("Qualcomm") &&
                renderer.toLowerCase().contains("adreno"));

        // Cleanup
        EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(eglDisplay, context);
        EGL14.eglTerminate(eglDisplay);
        Logging.LOG.log(Level.SEVERE, "CheckVendor: Running on Adreno GPU:" + isAdreno);
        return isAdreno;
    }

}
