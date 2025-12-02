package com.tungsten.fcl.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.terracotta.Terracotta;
import com.tungsten.fcl.terracotta.profile.TerracottaProfile;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.List;

public class MultiPlayerProfileAdapter extends FCLAdapter {

    private List<TerracottaProfile> profileList;

    public MultiPlayerProfileAdapter(Context context, List<TerracottaProfile> profileList) {
        super(context);
        this.profileList = profileList;
    }

    public void refreshList(List<TerracottaProfile> profileList) {
        this.profileList = profileList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        FCLTextView name;
        FCLTextView type;
        FCLTextView vendor;
    }

    @Override
    public int getCount() {
        return profileList.size();
    }

    @Override
    public Object getItem(int i) {
        return profileList.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_terracotta_profile, null);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.type = view.findViewById(R.id.type);
            viewHolder.vendor = view.findViewById(R.id.vendor);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        TerracottaProfile profile = profileList.get(i);
        viewHolder.name.setText(profile.getName());
        viewHolder.type.setText(Terracotta.parseProfileKind(getContext(), profile.getType()));
        viewHolder.vendor.setText(profile.getVendor());
        return view;
    }
}
