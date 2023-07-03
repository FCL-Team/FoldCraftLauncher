package com.tungsten.fcl.game;

import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.util.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class LocalizedRemoteModRepository implements RemoteModRepository {

    protected abstract RemoteModRepository getBackedRemoteModRepository();

    @Override
    public Stream<RemoteMod> search(String gameVersion, Category category, int pageOffset, int pageSize, String searchFilter, SortType sort, SortOrder sortOrder) throws IOException {
        String newSearchFilter;
        if (StringUtils.containsChinese(searchFilter)) {
            ModTranslations modTranslations = ModTranslations.getTranslationsByRepositoryType(getType());
            List<ModTranslations.Mod> mods = modTranslations.searchMod(searchFilter);
            List<String> searchFilters = new ArrayList<>();
            int count = 0;
            for (ModTranslations.Mod mod : mods) {
                String englishName = mod.getName();
                if (StringUtils.isNotBlank(mod.getSubname())) {
                    englishName = mod.getSubname();
                }

                searchFilters.add(englishName);

                count++;
                if (count >= 3) break;
            }
            newSearchFilter = String.join(" ", searchFilters);
        } else {
            newSearchFilter = searchFilter;
        }

        return getBackedRemoteModRepository().search(gameVersion, category, pageOffset, pageSize, newSearchFilter, sort, sortOrder);
    }

    @Override
    public Stream<Category> getCategories() throws IOException {
        return getBackedRemoteModRepository().getCategories();
    }

    @Override
    public Optional<RemoteMod.Version> getRemoteVersionByLocalFile(LocalModFile localModFile, Path file) throws IOException {
        return getBackedRemoteModRepository().getRemoteVersionByLocalFile(localModFile, file);
    }

    @Override
    public RemoteMod getModById(String id) throws IOException {
        return getBackedRemoteModRepository().getModById(id);
    }

    @Override
    public RemoteMod.File getModFile(String modId, String fileId) throws IOException {
        return getBackedRemoteModRepository().getModFile(modId, fileId);
    }

    @Override
    public Stream<RemoteMod.Version> getRemoteVersionsById(String id) throws IOException {
        return getBackedRemoteModRepository().getRemoteVersionsById(id);
    }
}
