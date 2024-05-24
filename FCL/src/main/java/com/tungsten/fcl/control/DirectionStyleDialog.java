package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.HorizontalListView;

public class DirectionStyleDialog extends FCLDialog implements View.OnClickListener {

    private final boolean select;
    private final ControlDirectionStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton editStyle;
    private FCLButton positive;

    private HorizontalListView listView;

    public interface Callback {
        void onStyleSelect(ControlDirectionStyle style);
    }

    public DirectionStyleDialog(@NonNull Context context, boolean select, @Nullable ControlDirectionStyle initStyle, Callback callback) {
        super(context);
        this.select = select;
        this.initStyle = initStyle;
        this.callback = callback;
        setContentView(R.layout.dialog_manage_direction_style);
        setCancelable(false);

        addStyle = findViewById(R.id.add_style);
        editStyle = findViewById(R.id.edit_style);
        positive = findViewById(R.id.positive);
        addStyle.setOnClickListener(this);
        editStyle.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList();

        if (!select) {
            editStyle.setVisibility(View.GONE);
        }
    }

    private DirectionStyleAdapter adapter;

    public void refreshList() {
        adapter = new DirectionStyleAdapter(getContext(), DirectionStyles.getStyles(), select, initStyle);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), null, false, style -> {
                DirectionStyles.addStyle(style);
                refreshList();
            });
            dialog.show();
        }
        if (v == editStyle) {
            AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), adapter.getSelectedStyle(), true, style -> {
                DirectionStyles.removeStyles(adapter.getSelectedStyle());
                DirectionStyles.addStyle(style);
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
