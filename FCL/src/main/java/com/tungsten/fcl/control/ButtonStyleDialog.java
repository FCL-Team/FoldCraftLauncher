package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonStyles;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

public class ButtonStyleDialog extends FCLDialog implements View.OnClickListener {

    private final boolean select;
    private final ControlButtonStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton editStyle;
    private FCLButton positive;

    private ListView listView;
    private GameMenu menu;

    public interface Callback {
        void onStyleSelect(ControlButtonStyle style);
    }

    public ButtonStyleDialog(@NonNull Context context, boolean select, @Nullable ControlButtonStyle initStyle, Callback callback) {
        super(context);
        this.select = select;
        this.initStyle = initStyle;
        this.callback = callback;
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        setContentView(R.layout.dialog_manage_button_style);
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

    private ButtonStyleAdapter adapter;

    public void refreshList() {
        adapter = new ButtonStyleAdapter(getContext(), ButtonStyles.getStyles(), select, initStyle);
        listView.setAdapter(adapter);
        listView.setSelection(ButtonStyles.findStyleIndexByName(initStyle.getName()));
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            AddButtonStyleDialog dialog = new AddButtonStyleDialog(getContext(), null, false, style -> {
                ButtonStyles.addStyle(style);
                refreshList();
            });
            dialog.show();
        }
        if (v == editStyle) {
            AddButtonStyleDialog dialog = new AddButtonStyleDialog(getContext(), adapter.getSelectedStyle(), true, style -> {
                ControlButtonStyle before = adapter.getSelectedStyle();
                int i = ButtonStyles.getStyles().indexOf(before);
                String beforeName = before.getName();
                ButtonStyles.removeStyles(before);
                ButtonStyles.addStyle(style, i);
                refreshList();
                adapter.setSelectedStyle(style);
                if (menu != null) {
                    ControlViewGroup viewGroup = menu.getViewGroup();
                    if (viewGroup != null) {
                        viewGroup.getViewData().buttonList().forEach(it -> {
                            String name = it.getStyle().getName();
                            if (name.equals(style.getName()) || name.equals(beforeName)) {
                                it.setStyle(style);
                            }
                        });
                    }
                }
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

    public void setGameMenu(GameMenu menu) {
        this.menu = menu;
    }
}
