package com.tungsten.fcl.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ViewGroupAdapter extends FCLAdapter {

    private final ObservableList<ControlViewGroup> list;
    private final GameMenu menu;

    public ViewGroupAdapter(Context context, ObservableList<ControlViewGroup> list, GameMenu menu) {
        super(context);
        this.list = list;
        this.menu = menu;
    }

    static class ViewHolder {
        FCLTextView name;
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
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.edit = view.findViewById(R.id.edit);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ControlViewGroup group = list.get(i);
        viewHolder.name.setText(group.getName());
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
        return view;
    }
}
