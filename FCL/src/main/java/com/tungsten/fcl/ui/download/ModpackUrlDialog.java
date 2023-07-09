package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

public class ModpackUrlDialog extends FCLDialog implements View.OnClickListener {

    private final Callback callback;

    private FCLEditText editText;
    private FCLButton positive;
    private FCLButton negative;

    public ModpackUrlDialog(@NonNull Context context, Callback callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_modpack_url);

        editText = findViewById(R.id.url);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (StringUtils.isNotBlank(editText.getText().toString())) {
                callback.onPositive(editText.getText().toString());
                dismiss();
            }
        }
        if (v == negative) {
            dismiss();
        }
    }

    public interface Callback {
        void onPositive(String urlString);
    }
}
