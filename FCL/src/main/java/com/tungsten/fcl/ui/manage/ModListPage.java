package com.tungsten.fcl.ui.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.DownloadPageManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ResultListener;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ModListPage extends FCLCommonPage implements ManageUI.VersionLoadable {

    private final BooleanProperty modded = new SimpleBooleanProperty(this, "modded", false);

    private ModManager modManager;
    private LibraryAnalyzer libraryAnalyzer;
    private Profile profile;
    private String versionId;

    public ModListPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    public void refresh() {
        //loadMods(modManager);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String version) {
        this.profile = profile;
        this.versionId = version;

        libraryAnalyzer = LibraryAnalyzer.analyze(profile.getRepository().getResolvedPreservingPatchesVersion(version));
        modded.set(libraryAnalyzer.hasModLoader());
        //loadMods(profile.getRepository().getModManager(version));
    }

    /*
    private CompletableFuture<?> loadMods(ModManager modManager) {
        this.modManager = modManager;
        return CompletableFuture.supplyAsync(() -> {
            try {
                synchronized (ModListPage.this) {
                    runInFX(() -> loadingProperty().set(true));
                    modManager.refreshMods();
                    return new ArrayList<>(modManager.getMods());
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }, Schedulers.defaultScheduler()).whenCompleteAsync((list, exception) -> {
            loadingProperty().set(false);
            if (exception == null)
                itemsProperty().setAll(list.stream().map(ModListPageSkin.ModInfoObject::new).sorted().collect(Collectors.toList()));
            else
                getProperties().remove(ModListPage.class);

            System.gc();
        }, Platform::runLater);
    }

    public void add() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".jar");
        suffix.add(".zip");
        suffix.add(".litemod");
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FILE_CHOOSER);
        builder.setTitle(getContext().getString(R.string.mods_choose_mod));
        builder.setSuffix(suffix);
        builder.setSelectionMode(SelectionMode.MULTIPLE_SELECTION);
        builder.create().browse(getActivity(), RequestCodes.SELECT_MODS_CODE, (requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_MODS_CODE && resultCode == Activity.RESULT_OK && data != null) {
                List<File> res = FileBrowser.getSelectedFiles(data).stream().filter(Objects::nonNull).map(File::new).collect(Collectors.toList());

                // It's guaranteed that succeeded and failed are thread safe here.
                List<String> succeeded = new ArrayList<>(res.size());
                List<String> failed = new ArrayList<>();

                Task.runAsync(() -> {
                    for (File file : res) {
                        try {
                            modManager.addMod(file.toPath());
                            succeeded.add(file.getName());
                        } catch (Exception e) {
                            Logging.LOG.log(Level.WARNING, "Unable to add mod " + file, e);
                            failed.add(file.getName());

                            // Actually addMod will not throw exceptions because FileChooser has already filtered files.
                        }
                    }
                }).withRunAsync(Schedulers.androidUIThread(), () -> {
                    List<String> prompt = new ArrayList<>(1);
                    if (!succeeded.isEmpty())
                        prompt.add(AndroidUtils.getLocalizedText(getContext(), "mods_add_success", String.join(", ", succeeded)));
                    if (!failed.isEmpty())
                        prompt.add(AndroidUtils.getLocalizedText(getContext(), "mods_add_failed", String.join(", ", failed)));
                    FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                    builder1.setCancelable(false);
                    builder1.setAlertLevel(failed.isEmpty() ? FCLAlertDialog.AlertLevel.INFO : FCLAlertDialog.AlertLevel.ALERT);
                    builder1.setTitle(getContext().getString(R.string.mods_add));
                    builder1.setMessage(String.join("\n", prompt));
                    builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                    builder1.create().show();
                    loadMods(modManager);
                }).start();
            }
        });
    }

    public void removeSelected(ObservableList<ModListPageSkin.ModInfoObject> selectedItems) {
        try {
            modManager.removeMods(selectedItems.stream()
                    .filter(Objects::nonNull)
                    .map(ModListPageSkin.ModInfoObject::getModInfo)
                    .toArray(LocalModFile[]::new));
            loadMods(modManager);
        } catch (IOException ignore) {
            // Fail to remove mods if the game is running or the mod is absent.
        }
    }

    public void enableSelected(ObservableList<ModListPageSkin.ModInfoObject> selectedItems) {
        selectedItems.stream()
                .filter(Objects::nonNull)
                .map(ModListPageSkin.ModInfoObject::getModInfo)
                .forEach(info -> info.setActive(true));
    }

    public void disableSelected(ObservableList<ModListPageSkin.ModInfoObject> selectedItems) {
        selectedItems.stream()
                .filter(Objects::nonNull)
                .map(ModListPageSkin.ModInfoObject::getModInfo)
                .forEach(info -> info.setActive(false));
    }

    public void openModFolder() {
        FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
        builder.setLibMode(LibMode.FILE_BROWSER);
        builder.setInitDir(new File(profile.getRepository().getRunDirectory(versionId), "mods").getAbsolutePath());
        builder.create().browse(getActivity(), RequestCodes.BROWSE_DIR_CODE, null);
    }

    public void checkUpdates() {
        Controllers.taskDialog(Task
                        .composeAsync(() -> {
                            Optional<String> gameVersion = profile.getRepository().getGameVersion(versionId);
                            if (gameVersion.isPresent()) {
                                return new ModCheckUpdatesTask(gameVersion.get(), modManager.getMods());
                            }
                            return null;
                        })
                        .whenComplete(Schedulers.javafx(), (result, exception) -> {
                            if (exception != null) {
                                Controllers.dialog("Failed to check updates", "failed", MessageDialogPane.MessageType.ERROR);
                            } else {
                                Controllers.navigate(new ModUpdatesPage(modManager, result));
                            }
                        })
                        .withStagesHint(Collections.singletonList("mods.check_updates")),
                i18n("update.checking"), TaskCancellationAction.NORMAL);
    }

    public void download() {
        MainActivity.getInstance().refreshMenuView(null);
        MainActivity.getInstance().download.setSelected(true);
        DownloadPageManager.getInstance().switchPage(DownloadPageManager.PAGE_ID_DOWNLOAD_MOD);
    }

    public void rollback(LocalModFile from, LocalModFile to) {
        try {
            modManager.rollback(from, to);
            refresh();
        } catch (IOException ex) {
            Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isModded() {
        return modded.get();
    }

    public BooleanProperty moddedProperty() {
        return modded;
    }

    public void setModded(boolean modded) {
        this.modded.set(modded);
    }

     */
}
