package com.tungsten.fcl.ui.manage;

import android.content.Context;
import com.tungsten.fcl.ui.UIManager;
import android.view.View;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fclcore.mod.ModpackExportInfo;
import com.tungsten.fclcore.mod.curse.CurseForgeModpackExportTask;
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackExportTask;
import com.tungsten.fclcore.mod.modrinth.ModrinthModpackExportTask;
import com.tungsten.fclcore.mod.multimc.MultiMCModpackExportTask;
import com.tungsten.fclcore.mod.server.ServerModpackExportTask;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

public class ModpackTypeSelectionPage extends FCLPage implements View.OnClickListener {

    private final Profile profile;
    private final String version;

    private FCLLinearLayout mcbbs;
    private FCLLinearLayout multimc;
    private FCLLinearLayout server;
    private FCLLinearLayout curseforge;
    private FCLLinearLayout modrinth;

    public ModpackTypeSelectionPage(Context context, int id, Profile profile, String version) {
        super(context, id, R.layout.page_modpack_type);
        this.profile = profile;
        this.version = version;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mcbbs = findViewById(R.id.mcbbs);
        multimc = findViewById(R.id.multimc);
        server = findViewById(R.id.server);
        curseforge = findViewById(R.id.curseforge);
        modrinth = findViewById(R.id.modrinth);
        mcbbs.setOnClickListener(this);
        multimc.setOnClickListener(this);
        server.setOnClickListener(this);
        curseforge.setOnClickListener(this);
        modrinth.setOnClickListener(this);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }


    @Override
    public void onClick(View v) {
        String type = null;
        ModpackExportInfo.Options options = null;
        if (v == mcbbs) {
            type = MODPACK_TYPE_MCBBS;
            options = McbbsModpackExportTask.OPTION;
        }
        if (v == multimc) {
            type = MODPACK_TYPE_MULTIMC;
            options = MultiMCModpackExportTask.OPTION;
        }
        if (v == server) {
            type = MODPACK_TYPE_SERVER;
            options = ServerModpackExportTask.OPTION;
        }
        if (v == curseforge) {
            type = MODPACK_TYPE_CURSEFORGE;
            options = CurseForgeModpackExportTask.OPTION;
        }
        if (v == modrinth) {
            type = MODPACK_TYPE_MODRINTH;
            options = ModrinthModpackExportTask.OPTION;
        }
        ModpackInfoPage page = new ModpackInfoPage(getContext(), FCLPage.PAGE_ID_TEMP, profile, version, type, options);
        UIManager.getInstance().getManageUI().showTempPage(page);
    }

    public static final String MODPACK_TYPE_MCBBS = "mcbbs";
    public static final String MODPACK_TYPE_MULTIMC = "multimc";
    public static final String MODPACK_TYPE_SERVER = "server";
    public static final String MODPACK_TYPE_CURSEFORGE = "curseforge";
    public static final String MODPACK_TYPE_MODRINTH = "modrinth";

    /**
     * 返回导出文件的扩展名，Modrinth 整合包为 .mrpack，其余为 .zip。
     */
    public static String getModpackFileExtension(String type) {
        return MODPACK_TYPE_MODRINTH.equals(type) ? ".mrpack" : ".zip";
    }
}
