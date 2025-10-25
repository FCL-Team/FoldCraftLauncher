package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.StringUtils.parseColorEscapes;
import static com.tungsten.fcllibrary.util.LocaleUtils.formatDateTime;

import android.app.Activity;
import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.game.World;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.versioning.VersionNumber;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.IOException;
import java.time.Instant;

public class WorldListItem {
    private final Context context;
    private final Activity activity;
    private final FCLUILayout parent;
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty subtitle = new SimpleStringProperty();
    private final World world;

    public WorldListItem(Context context, Activity activity, FCLUILayout parent, World world) {
        this.context = context;

        this.activity = activity;

        this.parent = parent;

        this.world = world;

        title.set(parseColorEscapes(world.getWorldName()));

        subtitle.set(AndroidUtils.getLocalizedText(context, "world_description", world.getFileName(), formatDateTime(context, Instant.ofEpochMilli(world.getLastPlayed())), world.getGameVersion() == null ? context.getString(R.string.message_unknown) : world.getGameVersion()));
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public void export() {
        FileBrowser.Builder builder = new FileBrowser.Builder(context);
        builder.setLibMode(LibMode.FOLDER_CHOOSER);
        builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
        builder.create().browse(activity, RequestCodes.SELECT_WORLD_EXPORT_CODE, ((requestCode, resultCode, data) -> {
            if (requestCode == RequestCodes.SELECT_WORLD_EXPORT_CODE && resultCode == Activity.RESULT_OK && data != null) {
                String path = FileBrowser.getSelectedFiles(data).get(0);
                WorldExportDialog dialog = new WorldExportDialog(context, world, path);
                dialog.show();
            }
        }));
    }

    public void manageDatapacks() {
        if (world.getGameVersion() == null || // old game will not write game version to level.dat
                (VersionNumber.isIntVersionNumber(world.getGameVersion()) // we don't parse snapshot version
                        && VersionNumber.asVersion(world.getGameVersion()).compareTo(VersionNumber.asVersion("1.13")) < 0)) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder.setMessage(context.getString(R.string.world_datapack_1_13));
            builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
            builder.create().show();
            return;
        }
        DatapackListPage page = new DatapackListPage(context, PageManager.PAGE_ID_TEMP, parent, R.layout.page_datapack_list, world.getWorldName(), world.getFile());
        ManagePageManager.getInstance().showTempPage(page);
    }

    public void showInfo() {
        try {
            WorldInfoPage page = new WorldInfoPage(context, PageManager.PAGE_ID_TEMP, parent, R.layout.page_manage_world_info, world);
            ManagePageManager.getInstance().showTempPage(page);
        } catch (Exception e) {
            // TODO
        }
    }

    public void delete() {
        new FCLAlertDialog.Builder(context)
                .setMessage(context.getString(R.string.version_manage_remove_confirm, world.getWorldName()))
                .setPositiveButton(() -> {
                    try {
                        FileUtils.forceDelete(world.getFile().toFile());
                    } catch (Exception ignore) {
                    }
                    WorldListPage page = (WorldListPage) ManagePageManager.getInstance().getPageById(ManagePageManager.PAGE_ID_MANAGE_WORLD);
                    page.refresh();
                })
                .setNegativeButton(null)
                .create()
                .show();
    }
}