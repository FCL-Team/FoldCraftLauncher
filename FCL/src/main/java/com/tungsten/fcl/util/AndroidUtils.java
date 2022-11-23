package com.tungsten.fcl.util;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.tungsten.fcl.R;

public class AndroidUtils {

    public static void openLink(Context context, String link) {
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void copyText(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(null, text);
        clip.setPrimaryClip(data);
        Toast.makeText(context, context.getString(R.string.message_copy), Toast.LENGTH_SHORT).show();
    }

}
