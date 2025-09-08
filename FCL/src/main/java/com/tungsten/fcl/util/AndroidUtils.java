package com.tungsten.fcl.util;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.mio.util.DisplayUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.WebActivity;
import com.tungsten.fclauncher.utils.FCLPath;
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
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        if (componentName != null) {
            context.startActivity(Intent.createChooser(intent, ""));
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("FCL Clipboard", link);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.open_link_failed), Toast.LENGTH_LONG).show();
        }
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
        return DisplayUtil.currentDisplayMetrics.heightPixels;
    }

    public static int getScreenWidth(Activity context) {
        return DisplayUtil.currentDisplayMetrics.widthPixels;
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

    public static String getFileName(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return uri.getLastPathSegment();
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        if (columnIndex == -1) return uri.getLastPathSegment();
        String fileName = cursor.getString(columnIndex);
        cursor.close();
        return fileName;
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
