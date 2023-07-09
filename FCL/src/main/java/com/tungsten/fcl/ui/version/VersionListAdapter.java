package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLRadioButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.ArrayList;

public class VersionListAdapter extends FCLAdapter {

    private final ArrayList<VersionListItem> list;

    public VersionListAdapter(Context context, ArrayList<VersionListItem> list) {
        super(context);
        this.list = list;
    }

    static class ViewHolder {
        FCLRadioButton radioButton;
        AppCompatImageView icon;
        FCLTextView title;
        FCLTextView tag;
        FCLTextView subtitle;
        FCLImageButton delete;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_version, null);
            viewHolder.radioButton = view.findViewById(R.id.radio);
            viewHolder.icon = view.findViewById(R.id.icon);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.tag = view.findViewById(R.id.tag);
            viewHolder.subtitle = view.findViewById(R.id.subtitle);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        VersionListItem versionListItem = list.get(i);
        viewHolder.radioButton.checkProperty().unbind();
        viewHolder.radioButton.checkProperty().bind(versionListItem.selectedProperty());
        viewHolder.icon.setBackground(versionListItem.getDrawable());
        viewHolder.title.setText(versionListItem.getVersion());
        viewHolder.tag.setVisibility(versionListItem.getTag() == null ? View.GONE : View.VISIBLE);
        if (versionListItem.getTag() != null) {
            viewHolder.tag.setText(versionListItem.getTag());
        }
        viewHolder.subtitle.setText(versionListItem.getLibraries());
        viewHolder.radioButton.setOnClickListener(view1 -> versionListItem.getProfile().setSelectedVersion(versionListItem.getVersion()));
        viewHolder.delete.setOnClickListener(view1 -> Versions.deleteVersion(getContext(), versionListItem.getProfile(), versionListItem.getVersion()));
        return view;
    }
}
