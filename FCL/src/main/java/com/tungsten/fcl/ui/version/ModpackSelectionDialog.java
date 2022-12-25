package com.tungsten.fcl.ui.version;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;

public class ModpackSelectionDialog extends FCLDialog {

    public ModpackSelectionDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_modpack_selection);
        setCancelable(false);
    }
}
