package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlButtonStyle;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLSpinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ButtonStyleDialog extends FCLDialog implements View.OnClickListener {

    private final Controller controller;
    private final boolean select;
    private final boolean allowForeign;
    private final ControlButtonStyle initStyle;
    private final Callback callback;

    private FCLButton addStyle;
    private FCLButton positive;
    private FCLSpinner<String> filterSpinner;
    private String filterSource;

    private ListView listView;

    public interface Callback {
        void onStyleSelect(ControlButtonStyle style);
    }

    public ButtonStyleDialog(@NonNull Context context, Controller controller, boolean select, boolean allowForeign, @Nullable ControlButtonStyle initStyle, Callback callback) {
        super(context);
        this.controller = controller;
        this.select = select;
        this.allowForeign = allowForeign;
        this.initStyle = initStyle;
        this.callback = callback;
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        setContentView(R.layout.dialog_manage_button_style);
        setCancelable(false);

        addStyle = findViewById(R.id.add_style);
        positive = findViewById(R.id.positive);
        filterSpinner = findViewById(R.id.filter_spinner);
        addStyle.setOnClickListener(this);
        positive.setOnClickListener(this);

        listView = findViewById(R.id.list);
        refreshList();
        setupFilterSpinner();

        if (!select) {
            filterSpinner.setVisibility(View.GONE);
        }
    }

    private ButtonStyleAdapter adapter;

    /**
     * 本布局样式 + （选择模式下）其他布局的样式：跨布局条目标注来源布局，
     * 与本地同名的不再重复展示；选中确认时导入复制进当前布局。
     */
    private List<ButtonStyleAdapter.Entry> buildEntries() {
        List<ButtonStyleAdapter.Entry> entries = new ArrayList<>();
        for (ControlButtonStyle style : controller.getButtonStyles()) {
            entries.add(new ButtonStyleAdapter.Entry(style, null));
        }
        if (select && allowForeign) {
            Set<String> ownNames = new HashSet<>();
            for (ControlButtonStyle style : controller.getButtonStyles()) {
                ownNames.add(style.getName());
            }
            for (Controller other : Controllers.getControllers()) {
                if (other == controller) {
                    continue;
                }
                for (ControlButtonStyle style : other.getButtonStyles()) {
                    if (!ownNames.contains(style.getName())) {
                        entries.add(new ButtonStyleAdapter.Entry(style, other.getName()));
                    }
                }
            }
        }
        return entries;
    }

    public void refreshList() {
        adapter = new ButtonStyleAdapter(getContext(), controller, buildEntries(), select, initStyle, this::refreshList, this::openEditDialog);
        adapter.setFilter(filterSource);
        listView.setAdapter(adapter);
        if (initStyle != null)
            listView.setSelection(adapter.indexOfLocal(initStyle.getName()));
    }

    /**
     * 来源筛选：全部 / 本布局 / 各来源布局。仅选择模式且允许跨布局导入时展示。
     */
    private void setupFilterSpinner() {
        if (!(select && allowForeign)) {
            return;
        }
        List<String> options = new ArrayList<>();
        options.add(getContext().getString(R.string.style_filter_all));
        options.add(getContext().getString(R.string.menu_controls_local));
        List<String> sources = new ArrayList<>();
        for (ButtonStyleAdapter.Entry entry : adapter.getEntries()) {
            if (entry.sourceName != null && !sources.contains(entry.sourceName)) {
                sources.add(entry.sourceName);
                options.add(entry.sourceName);
            }
        }
        filterSpinner.setItems(options);
        filterSpinner.setSelection(0);
        filterSpinner.setOnItemSelectedListener((index, item) -> {
            filterSource = index == 0 ? null : (index == 1 ? ButtonStyleAdapter.FILTER_LOCAL : sources.get(index - 2));
            adapter.setFilter(filterSource);
        });
        filterSpinner.setVisibility(View.VISIBLE);
    }

    /**
     * 条目编辑图标回调：就地更新注册表中的样式对象，保持对象同一性，
     * 所有持有该样式的按键经观察自动重渲染（改名也生效）。
     */
    private void openEditDialog(ControlButtonStyle before) {
        AddButtonStyleDialog dialog = new AddButtonStyleDialog(getContext(), controller, before, true, style -> {
            before.setAll(style);
            controller.saveToDisk();
            refreshList();
            adapter.setSelectedStyle(before);
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == addStyle) {
            AddButtonStyleDialog dialog = new AddButtonStyleDialog(getContext(), controller, null, false, style -> {
                controller.addOrUpdateButtonStyle(style);
                controller.saveToDisk();
                refreshList();
            });
            dialog.show();
        }
        if (v == positive) {
            dismiss();
            if (callback != null && select) {
                ButtonStyleAdapter.Entry entry = adapter.getSelectedEntry();
                if (entry == null) {
                    return;
                }
                ControlButtonStyle selected = entry.style;
                if (entry.sourceName != null) {
                    controller.addOrUpdateButtonStyle(selected.clone());
                    controller.saveToDisk();
                    selected = controller.findButtonStyleByName(selected.getName());
                }
                callback.onStyleSelect(selected);
            }
        }
    }
}
