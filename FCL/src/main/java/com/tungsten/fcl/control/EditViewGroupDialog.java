package com.tungsten.fcl.control;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLSpinner;

import java.util.ArrayList;
import java.util.Objects;

public class EditViewGroupDialog extends FCLDialog implements View.OnClickListener {

    private final GameMenu menu;
    private final ControlViewGroup viewGroup;
    private final Callback callback;

    private FCLEditText editText;
    private FCLSpinner<String> visibilitySpinner;

    private FCLButton positive;
    private FCLButton negative;

    public interface Callback {
        void onPositive(String name, ControlViewGroup.Visibility visibility);

        /** 是否支持把该组控件合并到其他组：无内容可合并的入口（新建组）返回 false，对话框不显示合并按钮 */
        default boolean supportsMerge() {
            return false;
        }

        /** 把该组全部控件合并（移动）到所选目标组，保留原组 */
        default void onMergeToGroup(ControlViewGroup source) {}
    }

    public EditViewGroupDialog(@NonNull Context context, GameMenu menu, ControlViewGroup viewGroup, Callback callback) {
        super(context);
        this.menu = menu;
        this.viewGroup = viewGroup;
        this.callback = callback;
        setCancelable(false);
        setContentView(R.layout.dialog_edit_view_group);

        editText = findViewById(R.id.name);
        visibilitySpinner = findViewById(R.id.visibility);
        ArrayList<String> visibilityString = new ArrayList<>();
        visibilityString.add(getContext().getString(R.string.menu_control_view_group_visible));
        visibilityString.add(getContext().getString(R.string.menu_control_view_group_invisible));
        visibilitySpinner.setItems(visibilityString);

        editText.setText(viewGroup.getName());
        visibilitySpinner.setSelection(viewGroup.getVisibility() == ControlViewGroup.Visibility.VISIBLE ? 0 : 1);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        FCLButton merge = findViewById(R.id.merge);
        merge.setVisibility(callback.supportsMerge() ? View.VISIBLE : View.GONE);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
        merge.setOnClickListener(v -> {
            dismiss();
            callback.onMergeToGroup(viewGroup);
        });
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            if (menu.getController().viewGroups().stream().anyMatch(it -> it.getName().equals(Objects.requireNonNull(editText.getText()).toString()) && !viewGroup.getName().equals(editText.getText().toString()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.menu_control_view_group_exist), Toast.LENGTH_SHORT).show();
            } else if (StringUtils.isBlank(Objects.requireNonNull(editText.getText()).toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.menu_control_view_group_empty), Toast.LENGTH_SHORT).show();
            } else {
                dismiss();
                callback.onPositive(editText.getText().toString(), visibilitySpinner.getSelectedIndex() == 0 ? ControlViewGroup.Visibility.VISIBLE : ControlViewGroup.Visibility.INVISIBLE);
            }
        }
        if (v == negative) {
            dismiss();
        }
    }
}
