package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.mod.ModAdviser;
import com.tungsten.fclcore.mod.ModpackExportInfo;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLCheckBoxTreeAdapter;
import com.tungsten.fcllibrary.component.FCLCheckBoxTreeItem;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModpackFileSelectionPage extends FCLTempPage implements View.OnClickListener {

    private final Profile profile;
    private final String version;
    private final String type;
    private final ModpackExportInfo.Options options;
    private final ModAdviser adviser;
    private final ModpackExportInfo exportInfo;
    private final File file;

    private FCLCheckBoxTreeItem<String> rootItem;

    private FCLProgressBar progressBar;
    private ListView listView;
    private FCLButton next;

    public ModpackFileSelectionPage(Context context, int id, FCLUILayout parent, int resId, Profile profile, String version, String type, ModpackExportInfo.Options options, ModAdviser adviser, ModpackExportInfo exportInfo, File file) {
        super(context, id, parent, resId);
        this.profile = profile;
        this.version = version;
        this.type = type;
        this.options = options;
        this.adviser = adviser;
        this.exportInfo = exportInfo;
        this.file = file;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        progressBar = findViewById(R.id.progress);
        listView = findViewById(R.id.list);
        ThemeEngine.getInstance().registerEvent(listView, () -> listView.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        next.setVisibility(View.GONE);

        new Thread(() -> {
            this.rootItem = getTreeItem(profile.getRepository().getRunDirectory(version), "minecraft");

            Schedulers.androidUIThread().execute(() -> {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);

                ObservableList<FCLCheckBoxTreeItem<String>> list = FXCollections.observableArrayList();
                list.add(rootItem);
                FCLCheckBoxTreeAdapter<String> adapter = new FCLCheckBoxTreeAdapter<>(getContext(), list);
                listView.setAdapter(adapter);
            });
        }).start();
    }

    private void finish() {
        ArrayList<String> list = new ArrayList<>();
        getFilesNeeded(rootItem, "minecraft", list);

        
    }

    private FCLCheckBoxTreeItem<String> getTreeItem(File file, String basePath) {
        if (!file.exists())
            return null;

        ModAdviser.ModSuggestion state = ModAdviser.ModSuggestion.SUGGESTED;
        if (basePath.length() > "minecraft/".length()) {
            state = adviser.advise(StringUtils.substringAfter(basePath, "minecraft/") + (file.isDirectory() ? "/" : ""), file.isDirectory());
            if (file.isFile() && Objects.equals(FileUtils.getNameWithoutExtension(file), version)) // Ignore <version>.json, <version>.jar
                state = ModAdviser.ModSuggestion.HIDDEN;
            if (file.isDirectory() && Objects.equals(file.getName(), version + "-natives")) // Ignore <version>-natives
                state = ModAdviser.ModSuggestion.HIDDEN;
            if (state == ModAdviser.ModSuggestion.HIDDEN)
                return null;
        }

        ObservableList<FCLCheckBoxTreeItem<String>> list = FXCollections.observableArrayList();
        FCLCheckBoxTreeItem<String> item = new FCLCheckBoxTreeItem<>(StringUtils.substringAfterLast(basePath, "/"), null, list);
        if (state == ModAdviser.ModSuggestion.SUGGESTED)
            item.setSelected(true);

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File it : files) {
                    FCLCheckBoxTreeItem<String> subItem = getTreeItem(it, basePath + "/" + it.getName());
                    if (subItem != null) {
                        item.setSelected(subItem.isSelected() || item.isSelected());
                        if (!subItem.isSelected()) {
                            item.setIndeterminate(true);
                        }
                        subItem.selectedProperty().addListener(observable -> item.checkProperty());
                        subItem.indeterminateProperty().addListener(observable -> item.checkProperty());
                        item.getSubItem().add(subItem);
                    }
                }
            }
            if (!item.isSelected()) item.setIndeterminate(false);

            // Empty folder need not to be displayed.
            if (item.getSubItem().size() == 0) {
                return null;
            }
        }

        if (TRANSLATION.containsKey(basePath)) {
            item.setComment(TRANSLATION.get(basePath));
        }
        item.setExpanded("minecraft".equals(basePath));

        return item;
    }

    private void getFilesNeeded(FCLCheckBoxTreeItem<String> item, String basePath, List<String> list) {
        if (item == null) return;
        if (item.isSelected() || item.isIndeterminate()) {
            if (basePath.length() > "minecraft/".length())
                list.add(StringUtils.substringAfter(basePath, "minecraft/"));
            item.getSubItem().forEach(it -> getFilesNeeded(it, basePath + "/" + it.getData(), list));
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            finish();
        }
    }

    private final Map<String, String> TRANSLATION = mapOf(
            pair("minecraft/fclversion.cfg", getContext().getString(R.string.modpack_files_fclversion_cfg)),
            pair("minecraft/servers.dat", getContext().getString(R.string.modpack_files_servers_dat)),
            pair("minecraft/saves", getContext().getString(R.string.modpack_files_saves)),
            pair("minecraft/mods", getContext().getString(R.string.modpack_files_mods)),
            pair("minecraft/config", getContext().getString(R.string.modpack_files_config)),
            pair("minecraft/liteconfig", getContext().getString(R.string.modpack_files_liteconfig)),
            pair("minecraft/resourcepacks", getContext().getString(R.string.modpack_files_resourcepacks)),
            pair("minecraft/resources", getContext().getString(R.string.modpack_files_resourcepacks)),
            pair("minecraft/options.txt", getContext().getString(R.string.modpack_files_options_txt)),
            pair("minecraft/optionsshaders.txt", getContext().getString(R.string.modpack_files_optionsshaders_txt)),
            pair("minecraft/mods/VoxelMods", getContext().getString(R.string.modpack_files_mods_voxelmods)),
            pair("minecraft/dumps", getContext().getString(R.string.modpack_files_dumps)),
            pair("minecraft/blueprints", getContext().getString(R.string.modpack_files_blueprints)),
            pair("minecraft/scripts", getContext().getString(R.string.modpack_files_scripts))
    );
}
