package com.tungsten.fcl.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ButtonEventData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.databinding.ItemBindGroupBinding;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 按键绑定控件组显隐对话框：为每个控件组选择按下时的行为——
 * 不绑定 / 按下切换（默认绑定方式）/ 按下显示 / 按下隐藏。
 */
public class BindGroupDialog extends FCLDialog {

    /** 与 {@link com.tungsten.fcl.control.view.ControlButton} 的绑定解析保持一致：id 或 id:show / id:hide */
    private final ButtonEventData.Event event;

    private final List<FCLSpinner<String>> modeSpinners = new ArrayList<>();
    private final List<ControlViewGroup> groups = new ArrayList<>();

    public BindGroupDialog(@NonNull Context context, GameMenu menu, ButtonEventData.Event event) {
        super(context);
        this.event = event;
        setCancelable(false);
        setContentView(R.layout.dialog_bind_group);

        LinearLayout container = findViewById(R.id.container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        List<ControlViewGroup> groups = new ArrayList<>();
        if (menu != null && menu.getController() != null) {
            groups.addAll(menu.getController().viewGroups());
        }
        this.groups.addAll(groups);
        List<String> bound = event.bindViewGroupList();
        for (ControlViewGroup group : groups) {
            ItemBindGroupBinding binding = ItemBindGroupBinding.inflate(LayoutInflater.from(context));
            binding.name.setText(group.getName());

            FCLSpinner<String> spinner = binding.mode;
            ArrayList<String> modes = new ArrayList<>();
            modes.add(context.getString(R.string.bind_group_mode_none));
            modes.add(context.getString(R.string.bind_group_mode_toggle));
            modes.add(context.getString(R.string.bind_group_mode_show));
            modes.add(context.getString(R.string.bind_group_mode_hide));
            spinner.setItems(modes);
            spinner.setSelection(parseModeIndex(bound, group.getId()));
            container.addView(binding.getRoot(), params);

            modeSpinners.add(spinner);
        }

        findViewById(R.id.negative).setOnClickListener(v -> dismiss());

        findViewById(R.id.positive).setOnClickListener(v -> {
            List<String> bindings = new ArrayList<>();
            for (int i = 0; i < groups.size(); i++) {
                String suffix = encodeModeSuffix(modeSpinners.get(i).getSelectedIndex());
                if (suffix != null) {
                    bindings.add(groups.get(i).getId() + suffix);
                }
            }
            event.setBindViewGroup(FXCollections.observableList(bindings));
            dismiss();
        });
    }

    private static int parseModeIndex(List<String> bindings, String groupId) {
        for (String bind : bindings) {
            if (bind.equals(groupId)) return 1;
            if (bind.equals(groupId + ":show")) return 2;
            if (bind.equals(groupId + ":hide")) return 3;
        }
        return 0;
    }

    /** 0=不绑定，1=按下切换（空后缀），2=按下显示，3=按下隐藏；null 表示不写入绑定 */
    @Nullable
    private static String encodeModeSuffix(int index) {
        switch (index) {
            case 1:
                return "";
            case 2:
                return ":show";
            case 3:
                return ":hide";
            default:
                return null;
        }
    }
}
