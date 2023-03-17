package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.HorizontalListView;

public class ButtonStyleDialog extends FCLDialog implements View.OnClickListener {

    private FCLButton addStyle;
    private FCLButton positive;

    private HorizontalListView listView;

    public ButtonStyleDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_manage_button_style);
        setCancelable(false);

        addStyle = findViewById(R.id.add_style);
        positive = findViewById(R.id.positive);
        addStyle.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList();
    }

    public void refreshList() {
        ButtonStyleAdapter adapter = new ButtonStyleAdapter(getContext(), ButtonStyles.getStyles(), false);
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
        }
    }
}
