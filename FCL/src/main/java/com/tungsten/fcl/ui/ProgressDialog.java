package com.tungsten.fcl.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;

public class ProgressDialog extends FCLDialog {
    public ProgressDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        FCLProgressBar progressBar = new FCLProgressBar(context);
        setContentView(progressBar);
        show();
    }
}
