package com.tungsten.fcl.ui.version;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;

public class AddProfileDialog extends FCLDialog implements View.OnClickListener {

    private FCLEditText editText;
    private FCLTextView pathText;
    private FCLImageButton editPath;
    private FCLButton positive;
    private FCLButton negative;

    public AddProfileDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_add_profile);
        setCancelable(false);
        editText = findViewById(R.id.name);
        pathText = findViewById(R.id.path);
        editPath = findViewById(R.id.edit);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        editPath.setOnClickListener(this);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == editPath) {
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, null, true, files -> {
                pathText.setText(files.get(0));
            });
        }
        if (view == positive) {
            if (StringUtils.isBlank(editText.getText().toString()) || StringUtils.isBlank(pathText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.input_not_empty), Toast.LENGTH_SHORT).show();
            } else if (Profiles.getProfiles().stream().anyMatch(profile -> profile.getName().equals(editText.getText().toString()))) {
                Toast.makeText(getContext(), getContext().getString(R.string.profile_already_exist), Toast.LENGTH_SHORT).show();
            } else {
                Profiles.getProfiles().add(new Profile(editText.getText().toString(), new File(pathText.getText().toString())));
                ((VersionListPage) VersionPageManager.getInstance().getAllPages().get(0)).refreshProfile();
                dismiss();
            }
        }
        if (view == negative) {
            dismiss();
        }
    }
}
