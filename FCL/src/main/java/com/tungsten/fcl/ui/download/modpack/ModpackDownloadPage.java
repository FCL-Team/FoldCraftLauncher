package com.tungsten.fcl.ui.download.modpack;

import android.content.Context;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.LocalizedRemoteModRepository;
import com.tungsten.fcl.ui.download.common.DownloadPage;
import com.tungsten.fcl.ui.version.Versions;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository;
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

public class ModpackDownloadPage extends DownloadPage {

    private FCLButton installModpack;
    private FCLImageView translate;

    public ModpackDownloadPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId, null);

        repository = new Repository();

        supportChinese.set(true);
        downloadSources.get().setAll(context.getString(R.string.mods_curseforge), context.getString(R.string.mods_modrinth));
        downloadSource.set(context.getString(R.string.mods_modrinth));

        create();
    }

    private class Repository extends LocalizedRemoteModRepository {

        @Override
        protected RemoteModRepository getBackedRemoteModRepository() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return ModrinthRemoteModRepository.MODPACKS;
            } else {
                return CurseForgeRemoteModRepository.MODPACKS;
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
            return Type.MODPACK;
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

    @Override
    public void onCreate() {
        super.onCreate();
        installModpack = findViewById(R.id.install_modpack);
        translate = findViewById(R.id.translate);
        installModpack.setVisibility(View.VISIBLE);
        if (LocaleUtils.isChinese(getContext())) {
            translate.setVisibility(View.VISIBLE);
        } else {
            translate.setVisibility(View.GONE);
        }
        installModpack.setOnClickListener(this);
        translate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == installModpack) {
            Versions.importModpack(getContext(), getParent());
        }
        if (v == translate) {
            showTranslationDialog();
        }
    }
}
