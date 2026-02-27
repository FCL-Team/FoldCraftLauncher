package com.tungsten.fcllibrary.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

public class LogSharingUtils {

    public static void showLogUploadSuccessDialog(Context context, String url) {
        new FCLAlertDialog.Builder(context)
                .setTitle(context.getString(R.string.log_upload_success_title))
                .setMessage(url)
                .setPositiveButton(context.getString(R.string.log_upload_copy), () -> copyToClipboard(context, url))
                .setNeutralButton(context.getString(R.string.log_upload_open), () -> openInBrowser(context, url))
                .setExtraButton(context.getString(R.string.log_upload_share), () -> shareLog(context, url))
                .setNegativeButton(context.getString(R.string.dialog_negative), null)
                .create().show();
    }

    public static String getLogUploadApiUrl(Context context) {
        if (LocaleUtils.isChinese(context)) {
            return "https://api.logshare.cn/1/log";
        } else {
            return "https://api.mclo.gs/1/log";
        }
    }

    private static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(null, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.crash_reporter_toast), Toast.LENGTH_SHORT).show();
        }
    }

    private static void openInBrowser(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void shareLog(Context context, String url) {
        try {
            String shareText = context.getString(R.string.log_upload_share_template, url);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.crash_reporter_share)));
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
