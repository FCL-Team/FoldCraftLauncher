package com.tungsten.fcl.ui.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModInfoDialog extends FCLDialog implements View.OnClickListener {

    private final ModListPage.ModInfoObject modInfoObject;

    private FCLImageView icon;
    private FCLTextView name;
    private FCLTextView version;
    private FCLTextView fileName;
    private FCLTextView description;

    private FCLButton website;
    private FCLButton positive;

    @SuppressLint("UseCompatLoadingForDrawables")
    public ModInfoDialog(@NonNull Context context, ModListPage.ModInfoObject modInfoObject) {
        super(context);
        this.modInfoObject = modInfoObject;
        setCancelable(false);
        setContentView(R.layout.dialog_mod_info);

        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        version = findViewById(R.id.version);
        fileName = findViewById(R.id.file_name);
        description = findViewById(R.id.description);

        website = findViewById(R.id.website);
        positive = findViewById(R.id.positive);
        website.setOnClickListener(this);
        positive.setOnClickListener(this);

        if (StringUtils.isNotBlank(modInfoObject.getModInfo().getLogoPath())) {
            Task.supplyAsync(() -> {
                try (FileSystem fs = CompressingUtils.createReadOnlyZipFileSystem(modInfoObject.getModInfo().getFile())) {
                    Path iconPath = fs.getPath(modInfoObject.getModInfo().getLogoPath());
                    if (Files.exists(iconPath)) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Files.copy(iconPath, stream);
                        return new ByteArrayInputStream(stream.toByteArray());
                    }
                }
                return null;
            }).whenComplete(Schedulers.androidUIThread(), (stream, exception) -> {
                if (stream != null) {
                    icon.setImageBitmap(BitmapFactory.decodeStream(stream));
                } else {
                    icon.setImageDrawable(getContext().getDrawable(R.drawable.img_command));
                }
            }).start();
        }

        name.setText(modInfoObject.getModInfo().getName());
        version.setText(getTag(modInfoObject));
        fileName.setText(FileUtils.getName(modInfoObject.getModInfo().getFile()));
        description.setText(modInfoObject.getModInfo().getDescription().toString());

        website.setVisibility(StringUtils.isNotBlank(modInfoObject.getModInfo().getUrl()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == website && StringUtils.isNotBlank(modInfoObject.getModInfo().getUrl())) {
            AndroidUtils.openLink(getContext(), modInfoObject.getModInfo().getUrl());
        }
        if (v == positive) {
            dismiss();
        }
    }

    private String getTag(ModListPage.ModInfoObject modInfoObject) {
        String modLoaderType = getModLoader(modInfoObject.getModInfo().getModLoaderType());
        String split = modLoaderType.equals("") ? "" : "   ";
        return modLoaderType + split + modInfoObject.getModInfo().getVersion();
    }

    private String getModLoader(ModLoaderType modLoaderType) {
        switch (modLoaderType) {
            case FORGE:
                return getContext().getString(R.string.install_installer_forge);
            case NEO_FORGED:
                return getContext().getString(R.string.install_installer_neoforge);
            case FABRIC:
                return getContext().getString(R.string.install_installer_fabric);
            case LITE_LOADER:
                return getContext().getString(R.string.install_installer_liteloader);
            case QUILT:
                return getContext().getString(R.string.install_installer_quilt);
            default:
                return "";
        }
    }
}
