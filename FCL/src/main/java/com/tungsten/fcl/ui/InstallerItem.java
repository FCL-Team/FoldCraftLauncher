package com.tungsten.fcl.ui;

import static com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.CLEANROOM;
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
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.util.versioning.GameVersionNumber;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class InstallerItem {

    private final Context context;
    private final String id;
    private final String name;
    private final Drawable icon;
    public final MutableStateFlow<String> libraryVersion = StateFlowKt.MutableStateFlow(null);
    public final MutableStateFlow<String> incompatibleLibraryName = StateFlowKt.MutableStateFlow(null);
    public final MutableStateFlow<String> dependencyName = StateFlowKt.MutableStateFlow(null);
    public final MutableStateFlow<Boolean> incompatibleWithGame = StateFlowKt.MutableStateFlow(false);
    public final MutableStateFlow<Boolean> removable = StateFlowKt.MutableStateFlow(false);
    public final MutableStateFlow<Boolean> upgradable = StateFlowKt.MutableStateFlow(false);
    public final MutableStateFlow<Boolean> installable = StateFlowKt.MutableStateFlow(true);
    public final MutableStateFlow<Runnable> removeAction = StateFlowKt.MutableStateFlow(null);
    public final MutableStateFlow<Runnable> action = StateFlowKt.MutableStateFlow(null);

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
        this.libraryVersion.setValue(libraryVersion);
        this.incompatibleWithGame.setValue(incompatibleWithGame);
        this.removable.setValue(removable);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDrawable(Context context, LibraryAnalyzer.LibraryType id) {
        switch (id) {
            case FORGE:
                return context.getDrawable(R.drawable.img_forge);
            case CLEANROOM:
                return context.getDrawable(R.drawable.img_cleanroom);
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
        public final InstallerItem cleanroom;
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
            cleanroom = new InstallerItem(context, CLEANROOM);
            neoForge = new InstallerItem(context, NEO_FORGE);
            liteLoader = new InstallerItem(context, LITELOADER);
            optiFine = new InstallerItem(context, OPTIFINE);
            quilt = new InstallerItem(context, QUILT);
            quiltApi = new InstallerItem(context, QUILT_API);

            mutualIncompatible(forge, fabric, quilt, neoForge, cleanroom);
            addIncompatibles(optiFine, fabric, quilt, neoForge, cleanroom);
            addIncompatibles(liteLoader, fabric, quilt, neoForge, cleanroom);
            addIncompatibles(fabricApi, forge, quiltApi, neoForge, liteLoader, optiFine, cleanroom);
            addIncompatibles(quiltApi, forge, fabric, fabricApi, neoForge, liteLoader, optiFine, cleanroom);

            // 原为注册在各 item.libraryVersion 上的强 InvalidationListener：强捕获 group，
            // 由页面视图 → skin → item → flow 订阅链保持存活，对齐原监听器的可达性
            Runnable listener = () -> {
                for (Map.Entry<InstallerItem, Set<InstallerItem>> entry : incompatibleMap.entrySet()) {
                    InstallerItem item = entry.getKey();

                    String incompatibleId = null;
                    for (InstallerItem other : entry.getValue()) {
                        if (other.libraryVersion.getValue() != null) {
                            incompatibleId = other.id;
                            break;
                        }
                    }

                    item.incompatibleLibraryName.setValue(incompatibleId);
                }
            };
            for (InstallerItem item : incompatibleMap.keySet()) {
                FlowSubscriptions.subscribe(item.libraryVersion, v -> listener.run());
            }

            Supplier<String> fabricApiDependency = () -> {
                if (fabric.libraryVersion.getValue() == null) return FABRIC.getPatchId();
                else return null;
            };
            fabricApi.dependencyName.setValue(fabricApiDependency.get());
            FlowSubscriptions.subscribe(fabric.libraryVersion, v -> fabricApi.dependencyName.setValue(fabricApiDependency.get()));

            Supplier<String> quiltApiDependency = () -> {
                if (quilt.libraryVersion.getValue() == null) return QUILT.getPatchId();
                else return null;
            };
            quiltApi.dependencyName.setValue(quiltApiDependency.get());
            FlowSubscriptions.subscribe(quilt.libraryVersion, v -> quiltApi.dependencyName.setValue(quiltApiDependency.get()));

            if (gameVersion == null) {
                this.libraries = new InstallerItem[]{forge, neoForge, liteLoader, optiFine, fabric, fabricApi, quilt, quiltApi, cleanroom};
            } else if (gameVersion.equals("1.12.2")) {
                this.libraries = new InstallerItem[]{forge, cleanroom, liteLoader, optiFine};
            } else if (GameVersionNumber.compare(gameVersion, "1.13") < 0) {
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
        private final FCLTextView state;
        private final FCLImageButton remove;
        private final FCLImageButton select;

        @SuppressLint("UseCompatLoadingForDrawables")
        public InstallerItemSkin(Context context, InstallerItem installerItem) {
            this.installerItem = installerItem;

            parent = (LinearLayoutCompat) LayoutInflater.from(context).inflate(R.layout.view_installer_item, null);
            item = parent.findViewById(R.id.item);
            FCLImageView icon = parent.findViewById(R.id.icon);
            FCLTextView name = parent.findViewById(R.id.name);
            state = parent.findViewById(R.id.state);
            remove = parent.findViewById(R.id.remove);
            select = parent.findViewById(R.id.select);

            ColorStateList colorStateList = new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()});
            ThemeEngine.getInstance().registerEvent(item, () -> item.setBackgroundTintList(colorStateList));
            icon.setBackground(installerItem.getIcon());
            name.setText(installerItem.getName());
            state.stringFlow().setValue(computeStateText());
            remove.visibilityFlow().setValue(installerItem.removable.getValue());
            select.visibilityFlow().setValue(computeSelectVisibility());
            select.imageFlow().setValue(computeSelectImage());
            // 原 bind(...) 对目标是弱引用：视图回收后即不再更新。订阅回调经 WeakReference
            // 触达 skin，对齐该弱监听语义；skin 由视图 OnClickListener 强持有，页面存活期间必然可达
            WeakReference<InstallerItemSkin> ref = new WeakReference<>(this);
            Consumer<Object> stateUpdater = v -> {
                InstallerItemSkin skin = ref.get();
                if (skin != null) skin.state.stringFlow().setValue(skin.computeStateText());
            };
            FlowSubscriptions.subscribe(installerItem.incompatibleLibraryName, stateUpdater);
            FlowSubscriptions.subscribe(installerItem.incompatibleWithGame, stateUpdater);
            FlowSubscriptions.subscribe(installerItem.libraryVersion, stateUpdater);
            FlowSubscriptions.subscribe(installerItem.removable, v -> {
                InstallerItemSkin skin = ref.get();
                if (skin != null) skin.remove.visibilityFlow().setValue(v);
            });
            Consumer<Object> selectVisibilityUpdater = v -> {
                InstallerItemSkin skin = ref.get();
                if (skin != null) skin.select.visibilityFlow().setValue(skin.computeSelectVisibility());
            };
            FlowSubscriptions.subscribe(installerItem.installable, selectVisibilityUpdater);
            FlowSubscriptions.subscribe(installerItem.incompatibleLibraryName, selectVisibilityUpdater);
            FlowSubscriptions.subscribe(installerItem.upgradable, v -> {
                InstallerItemSkin skin = ref.get();
                if (skin != null) skin.select.imageFlow().setValue(skin.computeSelectImage());
            });
            item.setOnClickListener(this);
            remove.setOnClickListener(this);
            select.setOnClickListener(this);
        }

        private String computeStateText() {
            Context context = parent.getContext();
            String incompatibleWith = installerItem.incompatibleLibraryName.getValue();
            String version = installerItem.libraryVersion.getValue();
            if (installerItem.incompatibleWithGame.getValue()) {
                return AndroidUtils.getLocalizedText(context, "install_installer_change_version", version);
            } else if (incompatibleWith != null) {
                return AndroidUtils.getLocalizedText(context, "install_installer_incompatible", AndroidUtils.getLocalizedText(context, "install_installer_" + incompatibleWith.replace("-", "_")));
            } else if (version == null) {
                return context.getString(R.string.install_installer_not_installed);
            } else {
                return version;
            }
        }

        private boolean computeSelectVisibility() {
            return installerItem.installable.getValue() && installerItem.incompatibleLibraryName.getValue() == null;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private Drawable computeSelectImage() {
            Context context = parent.getContext();
            return installerItem.upgradable.getValue()
                    ? context.getDrawable(R.drawable.ic_baseline_update_24)
                    : context.getDrawable(R.drawable.ic_baseline_arrow_forward_24);
        }

        public View getView() {
            return parent;
        }

        @Override
        public void onClick(View view) {
            if (view == item || view == select) {
                if (select.getVisibilityValue()) {
                    installerItem.action.getValue().run();
                }
            }
            if (view == remove) {
                Runnable runnable = installerItem.removeAction.getValue();
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }
}
