package com.tungsten.fcl.ui.manage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tungsten.fcl.R;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fcllibrary.component.FCLAdapter;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

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

        this.listProperty.addListener((InvalidationListener) observable -> {
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
        FCLImageView icon;
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
            viewHolder.icon = view.findViewById(R.id.icon);
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
        viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{selectedItemsProperty.contains(modInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor()}));
        ThemeEngine.getInstance().registerEvent(viewHolder.parent, () -> viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{selectedItemsProperty.contains(modInfoObject) ? ThemeEngine.getInstance().getTheme().getColor() : ThemeEngine.getInstance().getTheme().getLtColor()})));
        viewHolder.parent.setOnClickListener(v -> {
            if (selectedItemsProperty.contains(modInfoObject)) {
                fromSelf = true;
                selectedItemsProperty.remove(modInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()}));
            } else {
                fromSelf = true;
                selectedItemsProperty.add(modInfoObject);
                fromSelf = false;
                viewHolder.parent.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getColor()}));
            }
        });
        viewHolder.checkBox.addCheckedChangeListener();
        if (viewHolder.booleanProperty != null) {
            viewHolder.checkBox.checkProperty().unbindBidirectional(viewHolder.booleanProperty);
        }
        viewHolder.checkBox.checkProperty().bindBidirectional(viewHolder.booleanProperty = modInfoObject.getActive());
        viewHolder.icon.setTag(i);
        viewHolder.icon.setImageBitmap(null);
        viewHolder.icon.setVisibility(View.GONE);
        viewHolder.name.setText(modInfoObject.getTitle());
        String tag = getTag(modInfoObject);
        viewHolder.tag.setText(tag);
        viewHolder.tag.setVisibility(tag.equals("") ? View.GONE : View.VISIBLE);
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
        Task.supplyAsync(() -> {
            for (RemoteMod.Type type : RemoteMod.Type.values()) {
                try {
                    if (modInfoObject.getRemoteMod() == null) {
                        Optional<RemoteMod.Version> remoteVersion = type.getRemoteModRepository().getRemoteVersionByLocalFile(modInfoObject.getModInfo(), modInfoObject.getModInfo().getFile());
                        if (remoteVersion.isPresent()) {
                            RemoteMod remoteMod = type.getRemoteModRepository().getModById(remoteVersion.get().getModid());
                            modInfoObject.getModInfo().setRemoteVersion(remoteVersion.get());
                            modInfoObject.setRemoteMod(remoteMod);
                        } else {
                            continue;
                        }
                    }
                    return modInfoObject.getRemoteMod();
                } catch (Throwable ignore) {
                }
            }
            return null;
        }).whenComplete(Schedulers.androidUIThread(), (remoteMod, exception) -> {
            if ((int) viewHolder.icon.getTag() == i && remoteMod != null) {
                viewHolder.icon.setVisibility(View.VISIBLE);
                Glide.with(viewHolder.icon).load(remoteMod.getIconUrl()).into(viewHolder.icon);
                viewHolder.name.setText(remoteMod.getTitle());
            }
        }).start();
        return view;
    }

    private String getTag(ModListPage.ModInfoObject modInfoObject) {
        StringBuilder stringBuilder = new StringBuilder();
        String modLoaderType = getModLoader(modInfoObject.getModInfo().getModLoaderType());
        stringBuilder.append(modLoaderType);
        if (modInfoObject.getMod() != null && LocaleUtils.isChinese(getContext())) {
            String pre = modLoaderType.equals("") ? "" : "   ";
            stringBuilder.append(pre).append(modInfoObject.getMod().getDisplayName());
        }
        return stringBuilder.toString();
    }

    private String getModLoader(ModLoaderType modLoaderType) {
        switch (modLoaderType) {
            case FORGE:
                return getContext().getString(R.string.install_installer_forge);
            case NEO_FORGED:
                return getContext().getString(R.string.install_installer_neoforge);
            case FABRIC:
                return getContext().getString(R.string.install_installer_fabric);
            case LITE_LOADER:
                return getContext().getString(R.string.install_installer_liteloader);
            case QUILT:
                return getContext().getString(R.string.install_installer_quilt);
            default:
                return "";
        }
    }
}
