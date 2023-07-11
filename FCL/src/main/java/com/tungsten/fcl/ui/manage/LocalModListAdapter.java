package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;

public class LocalModListAdapter extends FCLAdapter {

    private final ModListPage modListPage;

    private final ListProperty<ModListPage.ModInfoObject> listProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<ModListPage.ModInfoObject> selectedItemsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ListProperty<ModListPage.ModInfoObject> listProperty() {
        return listProperty;
    }

    public ListProperty<ModListPage.ModInfoObject> selectedItemsProperty() {
        return selectedItemsProperty;
    }

    public void selectAll() {
        selectedItemsProperty.clear();
        selectedItemsProperty.addAll(listProperty);
    }

    private boolean fromSelf = false;

    public LocalModListAdapter(Context context, ModListPage modListPage) {
        super(context);
        this.modListPage = modListPage;

        this.listProperty.addListener((InvalidationListener)  observable -> {
            fromSelf = true;
            selectedItemsProperty.clear();
            fromSelf = false;
            notifyDataSetChanged();
        });
        selectedItemsProperty.addListener((InvalidationListener) observable -> {
            if (!fromSelf) {
                notifyDataSetChanged();
            }
        });
    }

    private static class ViewHolder {
        FCLLinearLayout parent;
        FCLCheckBox checkBox;
        FCLTextView name;
        FCLTextView tag;
        FCLTextView description;
        FCLImageButton restore;
        FCLImageButton info;
        BooleanProperty booleanProperty;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_local_mod, null);
            viewHolder.parent = view.findViewById(R.id.parent);
            viewHolder.checkBox = view.findViewById(R.id.check);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.tag = view.findViewById(R.id.tag);
            viewHolder.description = view.findViewById(R.id.description);
            viewHolder.restore = view.findViewById(R.id.restore);
            viewHolder.info = view.findViewById(R.id.info);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ModListPage.ModInfoObject modInfoObject = listProperty.get(i);
        viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { selectedItemsProperty.contains(modInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor() }));
        ThemeEngine.getInstance().registerEvent(viewHolder.parent, () -> viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { selectedItemsProperty.contains(modInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor() })));
        viewHolder.parent.setOnClickListener(v -> {
            if (selectedItemsProperty.contains(modInfoObject)) {
                fromSelf = true;
                selectedItemsProperty.remove(modInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() }));
            } else {
                fromSelf = true;
                selectedItemsProperty.add(modInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getColor() }));
            }
        });
        viewHolder.checkBox.addCheckedChangeListener();
        if (viewHolder.booleanProperty != null) {
            viewHolder.checkBox.checkProperty().unbindBidirectional(viewHolder.booleanProperty);
        }
        viewHolder.checkBox.checkProperty().bindBidirectional(viewHolder.booleanProperty = modInfoObject.getActive());
        viewHolder.name.setText(modInfoObject.getTitle());
        if (modInfoObject.getMod() != null && LocaleUtils.isChinese(getContext())) {
            viewHolder.tag.setText(modInfoObject.getMod().getDisplayName());
            viewHolder.tag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tag.setText("");
            viewHolder.tag.setVisibility(View.GONE);
        }
        viewHolder.description.setText(modInfoObject.getSubtitle());
        viewHolder.restore.setVisibility(modInfoObject.getModInfo().getMod().getOldFiles().isEmpty() ? View.GONE : View.VISIBLE);
        viewHolder.restore.setOnClickListener(v -> {
            ModRollbackDialog dialog = new ModRollbackDialog(getContext(), new ArrayList<>(modInfoObject.getModInfo().getMod().getOldFiles()), localModFile -> {
                modListPage.rollback(modInfoObject.getModInfo(), localModFile);
                notifyDataSetChanged();
            });
            dialog.show();
        });
        viewHolder.info.setOnClickListener(v -> {
            ModInfoDialog dialog = new ModInfoDialog(getContext(), modInfoObject);
            dialog.show();
        });
        return view;
    }
}
