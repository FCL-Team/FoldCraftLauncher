package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.DirectionStyles;
import com.tungsten.fcl.ui.compose.dialog.ComposeDialogs;
import com.tungsten.fcl.ui.compose.dialog.MiuixAddDirectionStyleDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import kotlin.Unit;

public class DirectionStyleDialog extends FCLDialog implements View.OnClickListener {

    private final boolean select;
    private final ControlDirectionStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton editStyle;
    private FCLButton positive;

    private ListView listView;

    private GameMenu menu;

    public interface Callback {
        void onStyleSelect(ControlDirectionStyle style);
    }

    public DirectionStyleDialog(@NonNull Context context, boolean select, @Nullable ControlDirectionStyle initStyle, Callback callback) {
        super(context);
        this.select = select;
        this.initStyle = initStyle;
        this.callback = callback;
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
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
        if (initStyle != null)
            listView.setSelection(DirectionStyles.findStyleIndexByName(initStyle.getName()));
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            // 3.2 批 4 接入点：新增方向键样式弹窗按开关双分支（回调逻辑两分支一致）
            if (ComposeDialogs.USE_COMPOSE_ADD_DIRECTION_STYLE) {
                MiuixAddDirectionStyleDialog dialog = new MiuixAddDirectionStyleDialog(getContext(), null, false, style -> {
                    DirectionStyles.addStyle(style);
                    refreshList();
                    return Unit.INSTANCE;
                });
                dialog.show();
            } else {
                AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), null, false, style -> {
                    DirectionStyles.addStyle(style);
                    refreshList();
                });
                dialog.show();
            }
        }
        if (v == editStyle) {
            // 3.2 批 4 接入点：编辑方向键样式弹窗按开关双分支（回调逻辑与 GameMenu 透传两分支一致）
            if (ComposeDialogs.USE_COMPOSE_ADD_DIRECTION_STYLE) {
                MiuixAddDirectionStyleDialog dialog = new MiuixAddDirectionStyleDialog(getContext(), adapter.getSelectedStyle(), true, style -> {
                    ControlDirectionStyle before = adapter.getSelectedStyle();
                    int i = DirectionStyles.getStyles().indexOf(before);
                    String beforeName = before.getName();
                    DirectionStyles.removeStyles(before);
                    DirectionStyles.addStyle(style, i);
                    refreshList();
                    adapter.setSelectedStyle(style);
                    if (menu != null) {
                        ControlViewGroup viewGroup = menu.getViewGroup();
                        if (viewGroup != null) {
                            viewGroup.getViewData().directionList().forEach(it -> {
                                String name = it.getStyle().getName();
                                if (name.equals(style.getName()) || name.equals(beforeName)) {
                                    it.setStyle(style);
                                }
                            });
                        }
                    }
                    return Unit.INSTANCE;
                });
                dialog.setGameMenu(menu);
                dialog.show();
            } else {
                AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), adapter.getSelectedStyle(), true, style -> {
                    ControlDirectionStyle before = adapter.getSelectedStyle();
                    int i = DirectionStyles.getStyles().indexOf(before);
                    String beforeName = before.getName();
                    DirectionStyles.removeStyles(before);
                    DirectionStyles.addStyle(style, i);
                    refreshList();
                    adapter.setSelectedStyle(style);
                    if (menu != null) {
                        ControlViewGroup viewGroup = menu.getViewGroup();
                        if (viewGroup != null) {
                            viewGroup.getViewData().directionList().forEach(it -> {
                                String name = it.getStyle().getName();
                                if (name.equals(style.getName()) || name.equals(beforeName)) {
                                    it.setStyle(style);
                                }
                            });
                        }
                    }
                });
                dialog.setGameMenu(menu);
                dialog.show();
            }
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
