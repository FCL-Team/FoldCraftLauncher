package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.util.FutureCallback;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

import java.util.concurrent.CompletableFuture;

public class RenameVersionDialog extends FCLDialog implements View.OnClickListener {

    private final FutureCallback<String> callback;
    private final CompletableFuture<String> future = new CompletableFuture<>();

    private FCLEditText editText;
    private FCLButton positive;
    private FCLButton negative;

    public RenameVersionDialog(@NonNull Context context, String oldName, FutureCallback<String> callback) {
        super(context);
        setContentView(R.layout.dialog_rename_version);
        setCancelable(false);
        this.callback = callback;
        editText = findViewById(R.id.new_name);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        editText.setText(oldName);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            positive.setEnabled(false);
            negative.setEnabled(false);
            callback.call(editText.getText().toString(), () -> {
                positive.setEnabled(true);
                negative.setEnabled(true);
                future.complete(editText.getText().toString());
                dismiss();
            }, msg -> {
                positive.setEnabled(true);
                negative.setEnabled(true);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            });
        }
        if (view == negative) {
            dismiss();
        }
    }

    public CompletableFuture<String> getFuture() {
        return future;
    }
}
