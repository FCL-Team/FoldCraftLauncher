package com.tungsten.fcl.ui.download;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.LocalizedRemoteModRepository;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository;
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class ShaderPackDownloadPage extends DownloadPage {

    public ShaderPackDownloadPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId, null);

        repository = new Repository();

        supportChinese.set(false);
        downloadSources.get().setAll(context.getString(R.string.mods_curseforge), context.getString(R.string.mods_modrinth));
        downloadSource.set(context.getString(R.string.mods_modrinth));

        create();
    }

    private class Repository extends LocalizedRemoteModRepository {

        @Override
        protected RemoteModRepository getBackedRemoteModRepository() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return ModrinthRemoteModRepository.SHADER_PACKS;
            } else {
                return CurseForgeRemoteModRepository.SHADER_PACKS;
            }
        }

        @Override
        protected SortType getBackedRemoteModRepositorySortOrder() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return SortType.NAME;
            } else {
                return SortType.POPULARITY;
            }
        }

        @Override
        public Type getType() {
            return Type.MOD;
        }
    }

    @Override
    protected String getLocalizedCategory(String category) {
        if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
            return AndroidUtils.getLocalizedText(getContext(), "modrinth_category_" + category.replaceAll("-", "_"));
        } else {
            return AndroidUtils.getLocalizedText(getContext(), "curse_category_" + category);
        }
    }

    @Override
    protected String getLocalizedOfficialPage() {
        return downloadSource.get();
    }
}
