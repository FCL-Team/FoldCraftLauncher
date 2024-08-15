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

import java.util.function.Consumer;

public class EditDialog extends FCLDialog implements View.OnClickListener {

    private final Consumer<String> callback;

    private FCLEditText editText;
    private FCLButton positive;
    private FCLButton negative;

    public EditDialog(@NonNull Context context, Consumer<String> callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_edit);

        editText = findViewById(R.id.name);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            String s = editText.getText().toString();
            if (!s.trim().equals("")) {
                callback.accept(s);
                dismiss();
            }
        }
        if (v == negative) {
            dismiss();
        }
    }

    public FCLEditText getEditText() {
        return editText;
    }
}
