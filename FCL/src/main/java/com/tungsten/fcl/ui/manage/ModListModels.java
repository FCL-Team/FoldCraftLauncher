package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.StringUtils.isNotBlank;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * Mod 列表/更新页共享数据模型（原 ModListPage.ModInfoObject、
 * ModUpdatesPage.ModUpdateObject、ModUpdatesPage.ModUpdateTask 三个嵌套类，
 * 旧 View 页面删除后原样抽到本文件，public 签名与全部逻辑保持不变）。
 */
public final class ModListModels {

    private ModListModels() {
    }

    public static class ModInfoObject {

        private final StateFlow<Boolean> active;
        private final LocalModFile localModFile;
        private final String title;
        private final String message;
        private final ModTranslations.Mod mod;
        private RemoteMod remoteMod;

        public ModInfoObject(Context context, LocalModFile localModFile) {
            this.localModFile = localModFile;
            this.active = localModFile.activeFlow();

            StringBuilder title = new StringBuilder(localModFile.getName());
            if (isNotBlank(localModFile.getVersion()))
                title.append(" ").append(localModFile.getVersion());
            this.title = title.toString();

            StringBuilder message = new StringBuilder(localModFile.getFileName());
            if (isNotBlank(localModFile.getGameVersion()))
                message.append(", ").append(context.getString(R.string.archive_game_version)).append(": ").append(localModFile.getGameVersion());
            if (isNotBlank(localModFile.getAuthors()))
                message.append(", ").append(context.getString(R.string.archive_author)).append(": ").append(localModFile.getAuthors());
            this.message = message.toString();

            this.mod = ModTranslations.MOD.getMod(localModFile.getId(), localModFile.getName());
        }

        public StateFlow<Boolean> getActive() {
            return active;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return message;
        }

        public LocalModFile getModInfo() {
            return localModFile;
        }

        public ModTranslations.Mod getMod() {
            return mod;
        }

        public RemoteMod getRemoteMod() {
            return remoteMod;
        }

        public void setRemoteMod(RemoteMod remoteMod) {
            this.remoteMod = remoteMod;
        }
    }

    public static final class ModUpdateObject {
        public final LocalModFile.ModUpdate data;
        final MutableStateFlow<Boolean> enabled = StateFlowKt.MutableStateFlow(false);
        final MutableStateFlow<String> fileName = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> currentVersion = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> targetVersion = StateFlowKt.MutableStateFlow(null);
        final MutableStateFlow<String> source = StateFlowKt.MutableStateFlow(null);

        public ModUpdateObject(Context context, LocalModFile.ModUpdate data) {
            this.data = data;

            enabled.setValue(!data.getLocalMod().getModManager().isDisabled(data.getLocalMod().getFile()));
            fileName.setValue(data.getLocalMod().getFileName());
            currentVersion.setValue(data.getCurrentVersion().getVersion());
            targetVersion.setValue(data.getCandidates().get(0).getVersion());
            switch (data.getCurrentVersion().getSelf().getType()) {
                case CURSEFORGE:
                    source.setValue(context.getString(com.tungsten.fcl.R.string.mods_curseforge));
                    break;
                case MODRINTH:
                    source.setValue(context.getString(com.tungsten.fcl.R.string.mods_modrinth));
            }
        }

        public boolean isEnabled() {
            return enabled.getValue();
        }

        public MutableStateFlow<Boolean> enabledFlow() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled.setValue(enabled);
        }

        public String getFileName() {
            return fileName.getValue();
        }

        public MutableStateFlow<String> fileNameFlow() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName.setValue(fileName);
        }

        public String getCurrentVersion() {
            return currentVersion.getValue();
        }

        public MutableStateFlow<String> currentVersionFlow() {
            return currentVersion;
        }

        public void setCurrentVersion(String currentVersion) {
            this.currentVersion.setValue(currentVersion);
        }

        public String getTargetVersion() {
            return targetVersion.getValue();
        }

        public MutableStateFlow<String> targetVersionFlow() {
            return targetVersion;
        }

        public void setTargetVersion(String targetVersion) {
            this.targetVersion.setValue(targetVersion);
        }

        public String getSource() {
            return source.getValue();
        }

        public MutableStateFlow<String> sourceFlow() {
            return source;
        }

        public void setSource(String source) {
            this.source.setValue(source);
        }
    }

    public static class ModUpdateTask extends Task<Void> {
        private final Collection<Task<?>> dependents;
        private final List<LocalModFile> failedMods = new ArrayList<>();

        public ModUpdateTask(ModManager modManager, List<Pair<LocalModFile, RemoteMod.Version>> mods, boolean keepOldVersion) {
            setStage("mods.check_updates.update");
            getProperties().put("total", mods.size());

            this.dependents = new ArrayList<>();
            for (Pair<LocalModFile, RemoteMod.Version> mod : mods) {
                LocalModFile local = mod.getKey();
                RemoteMod.Version remote = mod.getValue();
                boolean isDisabled = local.getModManager().isDisabled(local.getFile());

                dependents.add(Task
                        .runAsync(Schedulers.androidUIThread(), () -> local.setOld(true))
                        .thenComposeAsync(() -> {
                            String fileName = remote.getFile().getFilename();
                            if (isDisabled)
                                fileName += ModManager.DISABLED_EXTENSION;

                            FileDownloadTask task = new FileDownloadTask(
                                    new URL(remote.getFile().getUrl()),
                                    modManager.getModsDirectory().resolve(fileName).toFile());

                            task.setName(remote.getName());
                            return task;
                        })
                        .whenComplete(Schedulers.androidUIThread(), exception -> {
                            if (exception != null) {
                                // restore state if failed
                                local.setOld(false);
                                if (isDisabled)
                                    local.disable();
                                failedMods.add(local);
                            } else {
                                if (!keepOldVersion) {
                                    local.getFile().toFile().delete();
                                }
                            }
                        })
                        .withCounter("mods.check_updates.update"));
            }
        }

        public List<LocalModFile> getFailedMods() {
            return failedMods;
        }

        @Override
        public Collection<Task<?>> getDependents() {
            return dependents;
        }

        @Override
        public boolean doPreExecute() {
            return true;
        }

        @Override
        public void preExecute() {
            notifyPropertiesChanged();
        }

        @Override
        public boolean isRelyingOnDependents() {
            return false;
        }

        @Override
        public void execute() throws Exception {
            if (!isDependentsSucceeded())
                throw getException();
        }
    }
}
