package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class WorldListAdapter extends FCLAdapter {

    private final ListProperty<WorldListItem> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<WorldListItem> listProperty() {
        return listProperty;
    }

    public WorldListAdapter(Context context) {
        super(context);

        listProperty.addListener((InvalidationListener) observable -> {
            notifyDataSetChanged();
        });
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLTextView name;
        FCLTextView description;
        FCLImageButton datapack;
        FCLImageButton export;
        FCLImageButton delete;
    }

    @Override
    public int getCount() {
        return listProperty.getSize();
    }

    @Override
    public Object getItem(int i) {
        return listProperty.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_world, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.description = view.findViewById(R.id.description);
            viewHolder.datapack = view.findViewById(R.id.datapack);
            viewHolder.export = view.findViewById(R.id.export);
            viewHolder.delete = view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        WorldListItem worldListItem = listProperty.get(i);
        viewHolder.parent.setOnClickListener(v -> worldListItem.showInfo());
        viewHolder.name.stringProperty().bind(worldListItem.titleProperty());
        viewHolder.description.stringProperty().bind(worldListItem.subtitleProperty());
        viewHolder.datapack.setOnClickListener(v -> worldListItem.manageDatapacks());
        viewHolder.export.setOnClickListener(v -> worldListItem.export());
        viewHolder.delete.setOnClickListener(v -> worldListItem.delete());
        return view;
    }
}
