package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.HorizontalListView;

public class ButtonStyleDialog extends FCLDialog implements View.OnClickListener {

    private final boolean select;
    private final ControlButtonStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton positive;

    private HorizontalListView listView;

    public interface Callback {
        void onStyleSelect(ControlButtonStyle style);
    }

    public ButtonStyleDialog(@NonNull Context context, boolean select, @Nullable ControlButtonStyle initStyle, Callback callback) {
        super(context);
        this.select = select;
        this.initStyle = initStyle;
        this.callback = callback;
        setContentView(R.layout.dialog_manage_button_style);
        setCancelable(false);

        addStyle = findViewById(R.id.add_style);
        positive = findViewById(R.id.positive);
        addStyle.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList();
    }

    private ButtonStyleAdapter adapter;

    public void refreshList() {
        adapter = new ButtonStyleAdapter(getContext(), ButtonStyles.getStyles(), select, initStyle);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            AddButtonStyleDialog dialog = new AddButtonStyleDialog(getContext(), style -> {
                ButtonStyles.addStyle(style);
                refreshList();
            });
            dialog.show();
        }
        if (v == positive) {
            dismiss();
            if (callback != null && select) {
                callback.onStyleSelect(adapter.getSelectedStyle());
            }
        }
    }
}
