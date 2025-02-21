package com.tungsten.fcl.ui.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ModUpdateListAdapter extends FCLAdapter {

    private final ObservableList<ModUpdatesPage.ModUpdateObject> list;

    public ModUpdateListAdapter(Context context, ObservableList<ModUpdatesPage.ModUpdateObject> list) {
        super(context);
        this.list = list;
    }

    private static class ViewHolder {
        FCLCheckBox checkBox;
        FCLTextView file;
        FCLTextView source;
        FCLTextView desc;
        BooleanProperty booleanProperty;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_update_mod, null);
            viewHolder.checkBox = view.findViewById(R.id.check);
            viewHolder.file = view.findViewById(R.id.name);
            viewHolder.source = view.findViewById(R.id.source);
            viewHolder.desc = view.findViewById(R.id.desc);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ModUpdatesPage.ModUpdateObject modUpdateObject = list.get(i);
        viewHolder.checkBox.addCheckedChangeListener();
        if (viewHolder.booleanProperty != null) {
            viewHolder.checkBox.checkProperty().unbindBidirectional(viewHolder.booleanProperty);
        }
        viewHolder.checkBox.checkProperty().bindBidirectional(viewHolder.booleanProperty = modUpdateObject.enabledProperty());
        viewHolder.file.setText(modUpdateObject.getFileName());
        viewHolder.source.setText(modUpdateObject.getSource());
        viewHolder.desc.setText(modUpdateObject.getCurrentVersion() + "  ->  " + modUpdateObject.getTargetVersion());
        return view;
    }
}
