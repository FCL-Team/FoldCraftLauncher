package com.tungsten.fcl.ui.manage;

import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Task;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModCheckUpdatesTask extends Task<List<LocalModFile.ModUpdate>> {
    private final String gameVersion;
    private final Collection<LocalModFile> mods;
    private final Collection<Collection<Task<LocalModFile.ModUpdate>>> dependents;

    public ModCheckUpdatesTask(String gameVersion, Collection<LocalModFile> mods) {
        this.gameVersion = gameVersion;
        this.mods = mods;

        dependents = mods.stream()
                .parallel()
                .map(mod ->
                        Arrays.stream(RemoteMod.Type.values())
                                .map(type ->
                                        Task.supplyAsync(() -> mod.checkUpdates(gameVersion, type.getRemoteModRepository()))
                                                .setSignificance(TaskSignificance.MAJOR)
                                                .setName(String.format("%s (%s)", mod.getFileName(), type.name())).withCounter("mods.check_updates")
                                )
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        setStage("mods.check_updates");
        getProperties().put("total", dependents.size() * RemoteMod.Type.values().length);
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
    public Collection<? extends Task<?>> getDependents() {
        return dependents.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public boolean isRelyingOnDependents() {
        return false;
    }

    @Override
    public void execute() throws Exception {
        setResult(dependents.stream()
                .map(tasks -> tasks.stream()
                        .filter(task -> task.getResult() != null)
                        .map(Task::getResult)
                        .filter(modUpdate -> !modUpdate.getCandidates().isEmpty())
                        .max(Comparator.comparing((LocalModFile.ModUpdate modUpdate) -> modUpdate.getCandidates().get(0).getDatePublished()))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
    }
}