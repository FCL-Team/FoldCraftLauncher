package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

public class DownloadAddonDialog extends FCLDialog implements View.OnClickListener {

    private final Callback callback;

    private FCLEditText editText;
    private FCLButton positive;
    private FCLButton negative;

    public DownloadAddonDialog(@NonNull Context context, String name, Callback callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_download_addon);

        editText = findViewById(R.id.name);
        editText.setText(name);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (!OperatingSystem.isNameValid(editText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show();
            } else {
                callback.onPositive(editText.getText().toString());
                dismiss();
            }
        }
        if (v == negative) {
            dismiss();
        }
    }

    public interface Callback {
        void onPositive(String name);
    }
}
