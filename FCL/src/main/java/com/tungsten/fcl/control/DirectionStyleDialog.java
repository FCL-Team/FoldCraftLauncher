package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlDirectionStyle;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectionStyleDialog extends FCLDialog implements View.OnClickListener {

    private final Controller controller;
    private final boolean select;
    private final boolean allowForeign;
    private final ControlDirectionStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton editStyle;
    private FCLButton positive;

    private ListView listView;

    public interface Callback {
        void onStyleSelect(ControlDirectionStyle style);
    }

    public DirectionStyleDialog(@NonNull Context context, Controller controller, boolean select, boolean allowForeign, @Nullable ControlDirectionStyle initStyle, Callback callback) {
        super(context);
        this.controller = controller;
        this.select = select;
        this.allowForeign = allowForeign;
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

    /**
     * 本布局样式 + （选择模式下）其他布局的样式：跨布局条目标注来源布局，
     * 与本地同名的不再重复展示；选中确认时导入复制进当前布局。
     */
    private List<DirectionStyleAdapter.Entry> buildEntries() {
        List<DirectionStyleAdapter.Entry> entries = new ArrayList<>();
        for (ControlDirectionStyle style : controller.getDirectionStyles()) {
            entries.add(new DirectionStyleAdapter.Entry(style, null));
        }
        if (select && allowForeign) {
            Set<String> ownNames = new HashSet<>();
            for (ControlDirectionStyle style : controller.getDirectionStyles()) {
                ownNames.add(style.getName());
            }
            for (Controller other : Controllers.getControllers()) {
                if (other == controller) {
                    continue;
                }
                for (ControlDirectionStyle style : other.getDirectionStyles()) {
                    if (!ownNames.contains(style.getName())) {
                        entries.add(new DirectionStyleAdapter.Entry(style, other.getName()));
                    }
                }
            }
        }
        return entries;
    }

    public void refreshList() {
        adapter = new DirectionStyleAdapter(getContext(), controller, buildEntries(), select, initStyle, this::refreshList);
        listView.setAdapter(adapter);
        if (initStyle != null)
            listView.setSelection(adapter.indexOfLocal(initStyle.getName()));
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), controller, null, false, style -> {
                controller.addOrUpdateDirectionStyle(style);
                controller.saveToDisk();
                refreshList();
            });
            dialog.show();
        }
        if (v == editStyle) {
            DirectionStyleAdapter.Entry entry = adapter.getSelectedEntry();
            if (entry == null) {
                return;
            }
            ControlDirectionStyle selected = entry.style;
            if (entry.sourceName != null) {
                // 跨布局样式先导入复制为本布局样式，再编辑本地副本
                controller.addOrUpdateDirectionStyle(selected.clone());
                controller.saveToDisk();
                selected = controller.findDirectionStyleByName(selected.getName());
                refreshList();
            }
            final ControlDirectionStyle before = selected;
            AddDirectionStyleDialog dialog = new AddDirectionStyleDialog(getContext(), controller, before, true, style -> {
                // update the registered style in place: keeps object identity and list position,
                // so all directions holding it re-render through style observation (renames included)
                before.setAll(style);
                controller.saveToDisk();
                refreshList();
                adapter.setSelectedStyle(before);
            });
            dialog.show();
        }
        if (v == positive) {
            dismiss();
            if (callback != null && select) {
                DirectionStyleAdapter.Entry entry = adapter.getSelectedEntry();
                if (entry == null) {
                    return;
                }
                ControlDirectionStyle selected = entry.style;
                if (entry.sourceName != null) {
                    controller.addOrUpdateDirectionStyle(selected.clone());
                    controller.saveToDisk();
                    selected = controller.findDirectionStyleByName(selected.getName());
                }
                callback.onStyleSelect(selected);
            }
        }
    }
}
