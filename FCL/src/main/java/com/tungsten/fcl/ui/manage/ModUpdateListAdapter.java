package com.tungsten.fcl.ui.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fclcore.util.flow.FlowBindings;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ModUpdateListAdapter extends FCLAdapter {

    private final FlowList<ModUpdatesPage.ModUpdateObject> list;

    public ModUpdateListAdapter(Context context, FlowList<ModUpdatesPage.ModUpdateObject> list) {
        super(context);
        this.list = list;
    }

    private static class ViewHolder {
        FCLCheckBox checkBox;
        FCLTextView file;
        FCLTextView source;
        FCLTextView desc;
        FlowSubscriptions.Subscription checkSubscription;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get().get(i);
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
        ModUpdatesPage.ModUpdateObject modUpdateObject = list.get().get(i);
        viewHolder.checkBox.addCheckedChangeListener();
        if (viewHolder.checkSubscription != null) {
            viewHolder.checkSubscription.cancel();
        }
        viewHolder.checkSubscription = FlowBindings.bindBidirectional(viewHolder.checkBox.checkFlow(), modUpdateObject.enabledFlow());
        viewHolder.file.setText(modUpdateObject.getFileName());
        viewHolder.source.setText(modUpdateObject.getSource());
        viewHolder.desc.setText(modUpdateObject.getCurrentVersion() + "  ->  " + modUpdateObject.getTargetVersion());
        return view;
    }
}
