package com.tungsten.fcl.ui.download.modpack;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fclcore.mod.Modpack;
import com.tungsten.fclcore.mod.server.ServerModpackManifest;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.io.IOException;

public class RemoteModpackPage extends ModpackPage {

    private final String updateVersion;
    private final ServerModpackManifest manifest;

    private Modpack modpack;

    public RemoteModpackPage(Context context, int id, Profile profile, String updateVersion, ServerModpackManifest manifest) {
        super(context, id, R.layout.page_modpack, profile);
        this.updateVersion = updateVersion;
        this.manifest = manifest;

        // 原 onStart 逻辑：页面构造即解析服务端模组包
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);

        try {
            modpack = manifest.toModpack(null);
        } catch (IOException e) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setCancelable(false);
            builder.setTitle(getContext().getString(R.string.message_error));
            builder.setMessage(getContext().getString(R.string.modpack_type_server_malformed));
            builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), () -> {
                if (updateVersion == null) {
                    UIManager.getInstance().getDownloadUI().dismissCurrentTempPage();
                } else {
                    UIManager.getInstance().getManageUI().dismissCurrentTempPage();
                }
            });
            builder.create().show();
            return;
        }

        progressBar.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        describe.setVisibility(View.VISIBLE);

        name.setText(manifest.getName());
        version.setText(manifest.getVersion());
        author.setText(manifest.getAuthor());

        if (updateVersion != null) {
            editText.setText(updateVersion);
            editText.setEnabled(false);
        } else {
            editText.setText(manifest.getName().trim());
        }
    }

    @Override
    protected void onInstall() {
        String name;
        if (updateVersion != null) {
            name = updateVersion;
        } else {
            String str = editText.getText().toString();
            if (StringUtils.isBlank(str)) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show();
                return;
            } else if (profile.getRepository().versionIdConflicts(str)) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_already_exists), Toast.LENGTH_SHORT).show();
                return;
            } else if (!FCLGameRepository.isValidVersionId(str)) {
                Toast.makeText(getContext(), getContext().getString(R.string.install_new_game_malformed), Toast.LENGTH_SHORT).show();
                return;
            }
            name = str;
        }
        Task<?> task;
        if (updateVersion == null) {
            task = ModpackInstaller.getModpackInstallTask(getContext(), profile, manifest, modpack, name);
        } else {
            task = ModpackInstaller.getModpackInstallTask(getContext(), profile, updateVersion, null, manifest, modpack, name);
        }
        ModpackInstaller.installModpack(getContext(), task, updateVersion != null);
    }

    @Override
    protected void onDescribe() {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setCancelable(false);
        builder.setTitle(getContext().getString(R.string.modpack_description));
        CharSequence charSequence = Html.fromHtml(manifest.getDescription(), 0);
        builder.setMessage(charSequence);
        builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
        builder.create().show();
    }
}
