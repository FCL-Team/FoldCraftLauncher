package com.tungsten.fcl.ui;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FABRIC;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FABRIC_API;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FORGE;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.LITELOADER;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.NEO_FORGE;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.OPTIFINE;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.QUILT;
import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.QUILT_API;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.util.versioning.VersionNumber;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstallerItem {

    private final Context context;
    private final String id;
    private final String name;
    private final Drawable icon;
    public final StringProperty libraryVersion = new SimpleStringProperty();
    public final StringProperty incompatibleLibraryName = new SimpleStringProperty();
    public final StringProperty dependencyName = new SimpleStringProperty();
    public final BooleanProperty incompatibleWithGame = new SimpleBooleanProperty();
    public final BooleanProperty removable = new SimpleBooleanProperty();
    public final BooleanProperty upgradable = new SimpleBooleanProperty(false);
    public final BooleanProperty installable = new SimpleBooleanProperty(true);
    public final ObjectProperty<Runnable> removeAction = new SimpleObjectProperty<>();
    public final ObjectProperty<Runnable> action = new SimpleObjectProperty<>();

    public InstallerItem(Context context, LibraryAnalyzer.LibraryType id) {
        this.context = context;
        this.id = id.getPatchId();
        this.name = AndroidUtils.getLocalizedText(context, "install_installer_" + id.getPatchId().replace(".", "_").replace("-", "_"));
        this.icon = getDrawable(context, id);
    }

    public String getLibraryId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setState(String libraryVersion, boolean incompatibleWithGame, boolean removable) {
        this.libraryVersion.set(libraryVersion);
        this.incompatibleWithGame.set(incompatibleWithGame);
        this.removable.set(removable);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDrawable(Context context, LibraryAnalyzer.LibraryType id) {
        switch (id) {
            case FORGE:
                return context.getDrawable(R.drawable.img_forge);
            case NEO_FORGE:
                return context.getDrawable(R.drawable.img_neoforge);
            case LITELOADER:
                return context.getDrawable(R.drawable.img_chicken);
            case OPTIFINE:
                return context.getDrawable(R.drawable.img_optifine);
            case FABRIC:
            case FABRIC_API:
                return context.getDrawable(R.drawable.img_fabric);
            case QUILT:
            case QUILT_API:
                return context.getDrawable(R.drawable.img_quilt);
            default:
                return context.getDrawable(R.drawable.img_grass);
        }
    }

    public View createView() {
        InstallerItemSkin skin = new InstallerItemSkin(context, this);
        return skin.getView();
    }

    public final static class InstallerItemGroup {
        private final Context context;

        public final InstallerItem fabric;
        public final InstallerItem fabricApi;
        public final InstallerItem forge;
        public final InstallerItem neoForge;
        public final InstallerItem liteLoader;
        public final InstallerItem optiFine;
        public final InstallerItem quilt;
        public final InstallerItem quiltApi;

        private final InstallerItem[] libraries;

        private final HashMap<InstallerItem, Set<InstallerItem>> incompatibleMap = new HashMap<>();

        private Set<InstallerItem> getIncompatibles(InstallerItem item) {
            return incompatibleMap.computeIfAbsent(item, it -> new HashSet<>());
        }

        private void addIncompatibles(InstallerItem item, InstallerItem... others) {
            Set<InstallerItem> set = getIncompatibles(item);
            for (InstallerItem other : others) {
                set.add(other);
                getIncompatibles(other).add(item);
            }
        }

        private void mutualIncompatible(InstallerItem... items) {
            for (InstallerItem item : items) {
                Set<InstallerItem> set = getIncompatibles(item);

                for (InstallerItem item2 : items) {
                    if (item2 != item) {
                        set.add(item2);
                    }
                }
            }
        }

        public InstallerItemGroup(Context context, String gameVersion) {
            this.context = context;

            fabric = new InstallerItem(context, FABRIC);
            fabricApi = new InstallerItem(context, FABRIC_API);
            forge = new InstallerItem(context, FORGE);
            neoForge = new InstallerItem(context, NEO_FORGE);
            liteLoader = new InstallerItem(context, LITELOADER);
            optiFine = new InstallerItem(context, OPTIFINE);
            quilt = new InstallerItem(context, QUILT);
            quiltApi = new InstallerItem(context, QUILT_API);

            mutualIncompatible(forge, fabric, quilt, neoForge);
            addIncompatibles(optiFine, fabric, quilt, neoForge);
            addIncompatibles(liteLoader, fabric, quilt, neoForge);
            addIncompatibles(fabricApi, forge, quiltApi, neoForge, liteLoader, optiFine);
            addIncompatibles(quiltApi, forge, fabric, fabricApi, neoForge, liteLoader, optiFine);

            InvalidationListener listener = o -> {
                for (Map.Entry<InstallerItem, Set<InstallerItem>> entry : incompatibleMap.entrySet()) {
                    InstallerItem item = entry.getKey();

                    String incompatibleId = null;
                    for (InstallerItem other : entry.getValue()) {
                        if (other.libraryVersion.get() != null) {
                            incompatibleId = other.id;
                            break;
                        }
                    }

                    item.incompatibleLibraryName.set(incompatibleId);
                }
            };
            for (InstallerItem item : incompatibleMap.keySet()) {
                item.libraryVersion.addListener(listener);
            }

            fabricApi.dependencyName.bind(Bindings.createStringBinding(() -> {
                if (fabric.libraryVersion.get() == null) return FABRIC.getPatchId();
                else return null;
            }, fabric.libraryVersion));

            quiltApi.dependencyName.bind(Bindings.createStringBinding(() -> {
                if (quilt.libraryVersion.get() == null) return QUILT.getPatchId();
                else return null;
            }, quilt.libraryVersion));

            if (gameVersion == null) {
                this.libraries = new InstallerItem[]{forge, neoForge, liteLoader, optiFine, fabric, fabricApi, quilt, quiltApi};
            } else if (VersionNumber.compare(gameVersion, "1.13") < 0) {
                this.libraries = new InstallerItem[]{forge, liteLoader, optiFine};
            } else {
                this.libraries = new InstallerItem[]{forge, neoForge, optiFine, fabric, fabricApi, quilt, quiltApi};
            }
        }

        public InstallerItem[] getLibraries() {
            return libraries;
        }

        public View getView() {
            LinearLayoutCompat parent = new LinearLayoutCompat(context);
            parent.setOrientation(LinearLayoutCompat.VERTICAL);
            boolean first = true;
            for (InstallerItem installerItem : getLibraries()) {
                View view = installerItem.createView();
                if (first) {
                    first = false;
                } else {
                    view.setPadding(0, ConvertUtils.dip2px(context, 10), 0, 0);
                }
                parent.addView(view);
            }
            return parent;
        }
    }

    public static class InstallerItemSkin implements View.OnClickListener {

        private final InstallerItem installerItem;

        private final LinearLayoutCompat parent;
        private final FCLLinearLayout item;
        private final FCLImageButton remove;
        private final FCLImageButton select;

        @SuppressLint("UseCompatLoadingForDrawables")
        public InstallerItemSkin(Context context, InstallerItem installerItem) {
            this.installerItem = installerItem;

            parent = (LinearLayoutCompat) LayoutInflater.from(context).inflate(R.layout.view_installer_item, null);
            item = parent.findViewById(R.id.item);
            FCLImageView icon = parent.findViewById(R.id.icon);
            FCLTextView name = parent.findViewById(R.id.name);
            FCLTextView state = parent.findViewById(R.id.state);
            remove = parent.findViewById(R.id.remove);
            select = parent.findViewById(R.id.select);

            ColorStateList colorStateList = new ColorStateList(new int[][]{ { } }, new int[]{ ThemeEngine.getInstance().getTheme().getLtColor() });
            ThemeEngine.getInstance().registerEvent(item, () -> item.setBackgroundTintList(colorStateList));
            icon.setBackground(installerItem.getIcon());
            name.setText(installerItem.getName());
            state.stringProperty().bind(Bindings.createStringBinding(() -> {
                String incompatibleWith = installerItem.incompatibleLibraryName.get();
                String version = installerItem.libraryVersion.get();
                if (installerItem.incompatibleWithGame.get()) {
                    return AndroidUtils.getLocalizedText(context, "install_installer_change_version", version);
                } else if (incompatibleWith != null) {
                    return AndroidUtils.getLocalizedText(context, "install_installer_incompatible", AndroidUtils.getLocalizedText(context, "install_installer_" + incompatibleWith.replace("-", "_")));
                } else if (version == null) {
                    return context.getString(R.string.install_installer_not_installed);
                } else {
                    return version;
                }
            }, installerItem.incompatibleLibraryName, installerItem.incompatibleWithGame, installerItem.libraryVersion));
            remove.visibilityProperty().bind(installerItem.removable);
            select.visibilityProperty().bind(Bindings.createBooleanBinding(
                    () -> installerItem.installable.get() && installerItem.incompatibleLibraryName.get() == null,
                    installerItem.installable, installerItem.incompatibleLibraryName));
            select.imageProperty().bind(Bindings.createObjectBinding(() -> installerItem.upgradable.get()
                            ? context.getDrawable(R.drawable.ic_baseline_update_24)
                            : context.getDrawable(R.drawable.ic_baseline_arrow_forward_24),
                    installerItem.upgradable));
            item.setOnClickListener(this);
            remove.setOnClickListener(this);
            select.setOnClickListener(this);
        }

        public View getView() {
            return parent;
        }

        @Override
        public void onClick(View view) {
            if (view == item || view == select) {
                if (select.getVisibilityValue()) {
                    installerItem.action.get().run();
                }
            }
            if (view == remove) {
                Runnable runnable = installerItem.removeAction.get();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }
}
