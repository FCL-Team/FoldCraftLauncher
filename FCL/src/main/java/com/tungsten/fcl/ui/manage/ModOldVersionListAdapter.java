package com.tungsten.fcl.ui.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.List;

public class ModOldVersionListAdapter extends FCLAdapter {

    private final ModRollbackDialog dialog;
    private final List<LocalModFile> list;
    private final ModRollbackDialog.Callback callback;

    public ModOldVersionListAdapter(Context context, ModRollbackDialog dialog, List<LocalModFile> list, ModRollbackDialog.Callback callback) {
        super(context);
        this.dialog = dialog;
        this.list = list;
        this.callback = callback;
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView version;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = new FCLLinearLayout(getContext());
            viewHolder.parent = new FCLLinearLayout(getContext());
            viewHolder.version = new FCLTextView(getContext());
            ((FCLLinearLayout) view).addView(viewHolder.parent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = ConvertUtils.dip2px(getContext(), 10);
            viewHolder.parent.setPadding(padding, padding, padding, padding);
            viewHolder.parent.setBackground(getContext().getDrawable(R.drawable.bg_container_transparent_clickable));
            viewHolder.parent.addView(viewHolder.version, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewHolder.version.setSingleLine(true);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        LocalModFile localModFile = list.get(i);
        viewHolder.parent.setOnClickListener(v -> {
            dialog.dismiss();
            callback.onOldVersionSelect(localModFile);
        });
        viewHolder.version.setText(localModFile.getVersion());
        return view;
    }
}
