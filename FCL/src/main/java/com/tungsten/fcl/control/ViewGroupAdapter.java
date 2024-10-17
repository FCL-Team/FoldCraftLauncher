package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mio.util.AnimUtil;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.Collections;
import java.util.stream.Collectors;

public class ViewGroupAdapter extends FCLAdapter {

    private final ObservableList<ControlViewGroup> list;
    private final GameMenu menu;
    private final boolean select;

    private final ObservableList<ControlViewGroup> selectedGroups;
    private ObservableList<String> selectedIds;

    public ObservableList<ControlViewGroup> getSelectedGroups() {
        return selectedGroups;
    }

    public ViewGroupAdapter(Context context, ObservableList<ControlViewGroup> list, GameMenu menu, boolean select, ObservableList<ControlViewGroup> selectedGroups) {
        super(context);
        this.list = list;
        this.menu = menu;
        this.select = select;
        this.selectedGroups = selectedGroups;

        this.selectedIds = FXCollections.observableList(selectedGroups.stream().map(ControlViewGroup::getId).collect(Collectors.toList()));
        selectedGroups.addListener((InvalidationListener)  i -> selectedIds = FXCollections.observableList(selectedGroups.stream().map(ControlViewGroup::getId).collect(Collectors.toList())));
    }

    static class ViewHolder {
        FCLCheckBox checkBox;
        FCLTextView name;
        FCLImageButton up;
        FCLImageButton down;
        FCLImageButton edit;
        FCLImageButton delete;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_view_group, null);
            viewHolder.checkBox = view.findViewById(R.id.check);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.up = view.findViewById(R.id.up);
            viewHolder.down = view.findViewById(R.id.down);
            viewHolder.edit = view.findViewById(R.id.edit);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ControlViewGroup group = list.get(i);
        viewHolder.name.setText(group.getName());
        if (select) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.up.setVisibility(View.GONE);
            viewHolder.down.setVisibility(View.GONE);
            viewHolder.edit.setVisibility(View.GONE);
            viewHolder.delete.setVisibility(View.GONE);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.up.setVisibility(View.VISIBLE);
            viewHolder.down.setVisibility(View.VISIBLE);
            viewHolder.edit.setVisibility(View.VISIBLE);
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(selectedIds.contains(group.getId()));
        viewHolder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (!selectedIds.contains(group.getId())) {
                    selectedGroups.add(group);
                }
            } else {
                selectedGroups.removeIf(it -> it.getId().equals(group.getId()));
            }
        });
        View.OnClickListener upDownListener = v -> {
            int pos = v == viewHolder.up ? i - 1 : i + 1;
            if (pos < 0 || pos > list.size() - 1) {
                return;
            }
            Collections.swap(list, i, pos);
            menu.getController().updateViewGroup(group);
            notifyDataSetChanged();
        };
        viewHolder.up.setOnClickListener(upDownListener);
        viewHolder.down.setOnClickListener(upDownListener);
        viewHolder.edit.setOnClickListener(v -> {
            EditViewGroupDialog dialog = new EditViewGroupDialog(getContext(), menu, group, (n, vi) -> {
                group.setName(n);
                group.setVisibility(vi);
                menu.getController().updateViewGroup(group);
                notifyDataSetChanged();
            });
            dialog.show();
        });
        viewHolder.delete.setOnClickListener(v -> {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder.setMessage(getContext().getString(R.string.menu_control_view_group_delete));
            builder.setPositiveButton(() -> {
                menu.getController().removeViewGroup(group);
                notifyDataSetChanged();
            });
            builder.setNegativeButton(null);
            builder.create().show();
        });
        AnimUtil.playTranslationX(view, ThemeEngine.getInstance().getTheme().getAnimationSpeed() * 30L, -100f, 0f).start();
        return view;
    }
}
