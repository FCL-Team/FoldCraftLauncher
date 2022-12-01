package com.tungsten.fcl.util;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.WebActivity;
import com.tungsten.fclcore.util.io.FileUtils;

import java.io.File;

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
        if (resId != 0 && context.getString(resId) != null) {
            return context.getString(resId);
        } else {
            return key;
        }
    }

    public static boolean hasStringId(Context context, String key) {
        int resId = context.getResources().getIdentifier(key, "string", context.getPackageName());
        return resId != 0 && context.getString(resId) != null;
    }

}
