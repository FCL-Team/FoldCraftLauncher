package com.tungsten.fcllibrary.component.dialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLDialog extends AppCompatDialog {

    public FCLDialog(@NonNull Context context) {
        super(context);
        ThemeEngine.getInstance().applyFullscreen(this, ThemeEngine.getInstance().getTheme().isFullscreen());
    }

}
