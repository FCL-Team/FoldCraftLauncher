package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.StringUtils.parseColorEscapes;
import static com.tungsten.fcllibrary.util.LocaleUtils.formatDateTime;

import android.app.Activity;
import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.ui.compose.dialog.MiuixWorldExportDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.game.World;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.time.Instant;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class WorldListItem {
    private final Context context;
    private final Activity activity;
    private final FCLUILayout parent;
    private final MutableStateFlow<String> title = StateFlowKt.MutableStateFlow(null);
    private final MutableStateFlow<String> subtitle = StateFlowKt.MutableStateFlow(null);
    private final World world;

    public WorldListItem(Context context, Activity activity, FCLUILayout parent, World world) {
        this.context = context;

        this.activity = activity;

        this.parent = parent;

        this.world = world;

        title.setValue(parseColorEscapes(world.getWorldName()));

        subtitle.setValue(AndroidUtils.getLocalizedText(context, "world_description", world.getFileName(), formatDateTime(context, Instant.ofEpochMilli(world.getLastPlayed())), world.getGameVersion() == null ? context.getString(R.string.message_unknown) : world.getGameVersion()));
    }

    public MutableStateFlow<String> titleFlow() {
        return title;
    }

    public MutableStateFlow<String> subtitleFlow() {
        return subtitle;
    }

    public void export() {
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true, files -> {
            if (files == null) return;
            new MiuixWorldExportDialog(context, world, files.get(0)).show();
        });
    }
}