package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.util.FutureCallback;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

public class WorldNameDialog extends FCLDialog implements View.OnClickListener {

    private final FutureCallback<String> callback;

    private FCLEditText editText;
    private FCLButton positive;
    private FCLButton negative;

    public WorldNameDialog(@NonNull Context context, String name, FutureCallback<String> callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_world_name);

        editText = findViewById(R.id.name);
        editText.setText(name);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    private void setLoading(boolean loading) {
        editText.setEnabled(!loading);
        positive.setEnabled(!loading);
        negative.setEnabled(!loading);
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            setLoading(true);
            callback.call(editText.getText().toString(), () -> {
                setLoading(false);
                dismiss();
            }, s -> {
                setLoading(false);
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            });
        }
        if (v == negative) {
            dismiss();
        }
    }
}
