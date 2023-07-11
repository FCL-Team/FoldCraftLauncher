package com.tungsten.fcl.ui.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.tungsten.fcl.R;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.game.World;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.task.TaskListener;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;

import java.io.File;
import java.nio.file.Paths;

public class WorldExportDialog extends FCLDialog implements View.OnClickListener {

    private final World world;
    private final String parent;

    private FCLEditText editFileName;
    private FCLEditText editName;
    private FCLButton positive;
    private FCLButton negative;

    @SuppressLint("SetTextI18n")
    public WorldExportDialog(@NonNull Context context, World world, String parent) {
        super(context);
        this.world = world;
        this.parent = parent;
        setCancelable(false);
        setContentView(R.layout.dialog_world_export);

        editFileName = findViewById(R.id.file_name);
        editName = findViewById(R.id.name);
        editFileName.setStringValue(world.getWorldName() + ".zip");
        editName.setStringValue(world.getWorldName());
        editFileName.setText(world.getWorldName() + ".zip");
        editName.setText(world.getWorldName());
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        positive.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        editName.getStringValue().isEmpty()
                                || StringUtils.isBlank(editFileName.getStringValue())
                                || !OperatingSystem.isNameValid(editFileName.getStringValue())
                                || new File(parent, editFileName.getStringValue()).exists(),
                editName.stringProperty().isEmpty(), editFileName.stringProperty()));
    }

    @Override
    public void onClick(View v) {
        if (v == positive) {
            TaskDialog taskDialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
            taskDialog.setTitle(getContext().getString(R.string.message_doing));

            Task<?> task = Task.runAsync(AndroidUtils.getLocalizedText(getContext(), "world.export.wizard", editName.getStringValue()), () -> world.export(Paths.get(new File(parent, editFileName.getText().toString()).getAbsolutePath()), editName.getStringValue()));
            TaskExecutor executor = task.executor(new TaskListener() {
                @Override
                public void onStop(boolean success, TaskExecutor executor) {
                    Schedulers.androidUIThread().execute(() -> {
                        if (success) {
                            FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                            builder1.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                            builder1.setCancelable(false);
                            builder1.setMessage(getContext().getString(R.string.message_success));
                            builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> ManagePageManager.getInstance().dismissAllTempPagesCreatedByPage(ManagePageManager.PAGE_ID_MANAGE_MANAGE));
                            builder1.create().show();
                        } else {
                            if (executor.getException() == null)
                                return;
                            String appendix = StringUtils.getStackTrace(executor.getException());
                            FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                            builder1.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder1.setCancelable(false);
                            builder1.setTitle(getContext().getString(R.string.message_failed));
                            builder1.setMessage(appendix);
                            builder1.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                            builder1.create().show();
                        }
                    });
                }
            });
            taskDialog.setExecutor(executor);
            taskDialog.show();
            executor.start();
            dismiss();
        }
        if (v == negative) {
            dismiss();
        }
    }
}
