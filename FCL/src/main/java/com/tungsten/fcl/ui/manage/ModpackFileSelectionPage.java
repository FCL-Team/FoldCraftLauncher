package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Lang.mapOf;
import static com.tungsten.fclcore.util.Pair.pair;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.VersionSetting;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.mod.ModAdviser;
import com.tungsten.fclcore.mod.ModpackExportInfo;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackExportTask;
import com.tungsten.fclcore.mod.multimc.MultiMCInstanceConfiguration;
import com.tungsten.fclcore.mod.multimc.MultiMCModpackExportTask;
import com.tungsten.fclcore.mod.server.ServerModpackExportTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.FCLCheckBoxTreeAdapter;
import com.tungsten.fcllibrary.component.FCLCheckBoxTreeItem;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModpackFileSelectionPage extends FCLTempPage implements View.OnClickListener {

    private final Profile profile;
    private final String version;
    private final String modpackType;
    private final ModAdviser adviser;
    private final ModpackExportInfo exportInfo;
    private final File modpackFile;

    private FCLCheckBoxTreeItem<String> rootItem;

    private FCLProgressBar progressBar;
    private ListView listView;
    private FCLButton next;

    public ModpackFileSelectionPage(Context context, int id, FCLUILayout parent, int resId, Profile profile, String version, String type, ModAdviser adviser, ModpackExportInfo exportInfo, File file) {
        super(context, id, parent, resId);
        this.profile = profile;
        this.version = version;
        this.modpackType = type;
        this.adviser = adviser;
        this.exportInfo = exportInfo;
        this.modpackFile = file;
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
        exportInfo.setWhitelist(list);

        TaskDialog taskDialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
        taskDialog.setTitle(getContext().getString(R.string.message_doing));

        Task<?> task = getExportTask(modpackType, exportInfo, modpackFile);
        TaskExecutor executor = task.executor(new TaskListener() {
            @Override
            public void onStop(boolean success, TaskExecutor executor) {
                Schedulers.androidUIThread().execute(() -> {
                    if (success) {
                        FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                        builder1.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                        builder1.setCancelable(false);
                        builder1.setMessage(getContext().getString(R.string.message_success));
                        builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> ManagePageManager.getInstance().dismissAllTempPagesCreatedByPage(ManagePageManager.PAGE_ID_MANAGE_MANAGE));
                        builder1.create().show();
                    } else {
                        if (executor.getException() == null)
                            return;
                        String appendix = StringUtils.getStackTrace(executor.getException());
                        FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                        builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                        builder1.setCancelable(false);
                        builder1.setTitle(getContext().getString(R.string.message_failed));
                        builder1.setMessage(appendix);
                        builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                        builder1.create().show();
                    }

                });
            }
        });
        taskDialog.setExecutor(executor);
        taskDialog.show();
        executor.start();
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

    private Task<?> getExportTask(String modpackType, ModpackExportInfo exportInfo, File modpackFile) {
        return new Task<Object>() {
            Task<?> exportTask;

            @Override
            public boolean doPreExecute() {
                return true;
            }

            @Override
            public void preExecute() throws Exception {
                switch (modpackType) {
                    case ModpackTypeSelectionPage.MODPACK_TYPE_MCBBS:
                        exportTask = exportAsMcbbs(exportInfo, modpackFile);
                        break;
                    case ModpackTypeSelectionPage.MODPACK_TYPE_MULTIMC:
                        exportTask = exportAsMultiMC(exportInfo, modpackFile);
                        break;
                    case ModpackTypeSelectionPage.MODPACK_TYPE_SERVER:
                        exportTask = exportAsServer(exportInfo, modpackFile);
                        break;
                    default:
                        throw new IllegalStateException("Unrecognized modpack type " + modpackType);
                }

            }

            @Override
            public Collection<Task<?>> getDependents() {
                return Collections.singleton(exportTask);
            }

            @Override
            public void execute() throws Exception {

            }
        };
    }

    private Task<?> exportAsMcbbs(ModpackExportInfo exportInfo, File modpackFile) {
        return new Task<Void>() {
            Task<?> dependency = null;

            @Override
            public void execute() {
                dependency = new McbbsModpackExportTask(profile.getRepository(), version, exportInfo, modpackFile);
            }

            @Override
            public Collection<Task<?>> getDependencies() {
                return Collections.singleton(dependency);
            }
        };
    }

    private Task<?> exportAsMultiMC(ModpackExportInfo exportInfo, File modpackFile) {
        return new Task<Void>() {
            Task<?> dependency;

            @Override
            public void execute() {
                VersionSetting vs = profile.getVersionSetting(version);
                dependency = new MultiMCModpackExportTask(profile.getRepository(), version, exportInfo.getWhitelist(),
                        new MultiMCInstanceConfiguration(
                                "OneSix",
                                exportInfo.getName() + "-" + exportInfo.getVersion(),
                                null,
                                null,
                                "",
                                "",
                                null,
                                exportInfo.getDescription(),
                                null,
                                exportInfo.getJavaArguments(),
                                false,
                                854,
                                480,
                                vs.getMaxMemory(),
                                exportInfo.getMinMemory(),
                                false,
                                /* showConsoleOnError */ true,
                                /* autoCloseConsole */ false,
                                /* overrideMemory */ true,
                                /* overrideJavaLocation */ false,
                                /* overrideJavaArgs */ true,
                                /* overrideConsole */ true,
                                /* overrideCommands */ true,
                                /* overrideWindow */ true,
                                /* iconKey */ null // TODO
                        ), modpackFile);
            }

            @Override
            public Collection<Task<?>> getDependencies() {
                return Collections.singleton(dependency);
            }
        };
    }

    private Task<?> exportAsServer(ModpackExportInfo exportInfo, File modpackFile) {
        return new Task<Void>() {
            Task<?> dependency;

            @Override
            public void execute() {
                dependency = new ServerModpackExportTask(profile.getRepository(), version, exportInfo, modpackFile);
            }

            @Override
            public Collection<Task<?>> getDependencies() {
                return Collections.singleton(dependency);
            }
        };
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
