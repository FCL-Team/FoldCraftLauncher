package com.tungsten.fcl.ui.version;

import static com.tungsten.fclcore.util.Lang.threadPool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.ui.UIManager;
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
        FCLTextView subtitle;
        FCLImageButton rename;
        FCLImageButton copy;
        FCLImageButton browse;
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
            viewHolder.subtitle = view.findViewById(R.id.subtitle);
            viewHolder.rename = view.findViewById(R.id.rename);
            viewHolder.copy = view.findViewById(R.id.copy);
            viewHolder.browse = view.findViewById(R.id.browse);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        VersionListItem versionListItem = list.get(i);
        versionListItem.checkSelection();
        viewHolder.radioButton.setChecked(versionListItem.selectedProperty().get());
        viewHolder.icon.setBackground(versionListItem.getDrawable());
        viewHolder.title.setText(versionListItem.getVersion());
        viewHolder.subtitle.setText(versionListItem.getLibraries());
        viewHolder.radioButton.setOnClickListener(view1 -> {
            versionListItem.getProfile().setSelectedVersion(versionListItem.getVersion());
            MainActivity.getInstance().refresh(null).start();
            notifyDataSetChanged();
        });
        viewHolder.rename.setOnClickListener(view1 -> {
            Versions.renameVersion(getContext(), versionListItem.getProfile(), versionListItem.getVersion());
        });
        viewHolder.copy.setOnClickListener(view1 -> {

        });
        viewHolder.browse.setOnClickListener(view1 -> {
            Versions.openFolder(UIManager.getInstance().getVersionUI().getActivity(), versionListItem.getProfile(), versionListItem.getVersion());
        });
        viewHolder.delete.setOnClickListener(view1 -> {
            Versions.deleteVersion(getContext(), versionListItem.getProfile(), versionListItem.getVersion());
        });
        return view;
    }
}
