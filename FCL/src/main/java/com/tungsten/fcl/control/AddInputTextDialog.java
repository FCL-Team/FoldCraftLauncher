package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.QuickInputTexts;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

import java.util.Objects;

public class AddInputTextDialog extends FCLDialog implements View.OnClickListener {

    private final Callback callback;

    private FCLButton positive;
    private FCLButton negative;
    private FCLEditText editText;
    private FCLEditText remarks;

    public interface Callback {
        void onTextAdd();
    }

    public AddInputTextDialog(@NonNull Context context, Callback callback) {
        super(context);
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_add_input_text);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        editText = findViewById(R.id.text);
        remarks = findViewById(R.id.remarks);
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (StringUtils.isBlank(Objects.requireNonNull(editText.getText()).toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.quick_input_empty), Toast.LENGTH_SHORT).show();
            } else if (QuickInputTexts.getInputTexts().contains(editText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.quick_input_exist), Toast.LENGTH_SHORT).show();
            } else {
                String remarksText = Objects.requireNonNull(remarks.getText()).toString();
                QuickInputTexts.addInputText(StringUtils.isNotBlank(remarksText) ? remarksText + "&*&" + editText.getText().toString() : editText.getText().toString());
                callback.onTextAdd();
                dismiss();
            }
        }
        if (v == negative) {
            dismiss();
        }
    }
}
