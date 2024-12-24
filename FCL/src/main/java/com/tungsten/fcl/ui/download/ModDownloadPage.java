package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.text.Html;
import android.view.View;

import androidx.core.text.HtmlCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.LocalizedRemoteModRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RuntimeUtils;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository;
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;

public class ModDownloadPage extends DownloadPage {
    private ModManager modManager;

    public ModDownloadPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId, null);

        repository = new Repository();

        supportChinese.set(true);
        downloadSources.get().setAll(context.getString(R.string.mods_curseforge), context.getString(R.string.mods_modrinth));
        if (CurseForgeRemoteModRepository.isAvailable())
            downloadSource.set(context.getString(R.string.mods_curseforge));
        else
            downloadSource.set(context.getString(R.string.mods_modrinth));

        create();
        View showIncompatible = findViewById(R.id.show_incompatible);
        showIncompatible.setVisibility(View.VISIBLE);
        showIncompatible.setOnClickListener(v -> {
            try {
                FCLAlertDialog dialog = new FCLAlertDialog(context);
                dialog.setMessage(Html.fromHtml(IOUtils.readFullyAsString(context.getAssets().open("incompatible_mod_list.html")), 0));
                dialog.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                dialog.show();
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        });
    }

    @Override
    public void loadVersion(Profile profile, String version) {
        super.loadVersion(profile, version);
        modManager = Profiles.getSelectedProfile().getRepository().getModManager(Profiles.getSelectedVersion());
    }

    public ModManager getModManager() {
        return modManager;
    }

    private class Repository extends LocalizedRemoteModRepository {

        @Override
        protected RemoteModRepository getBackedRemoteModRepository() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return ModrinthRemoteModRepository.MODS;
            } else {
                return CurseForgeRemoteModRepository.MODS;
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
