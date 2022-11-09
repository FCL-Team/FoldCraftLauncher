package com.tungsten.fcl.ui.version;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;
import java.util.ArrayList;

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
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FOLDER_CHOOSER);
            builder.setTitle(getContext().getString(R.string.profile_select));
            builder.create().browse(UIManager.getInstance().getVersionUI().getActivity(), RequestCodes.SELECT_PROFILE_CODE, (requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_PROFILE_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> strings = FileBrowser.getSelectedFiles(data);
                    pathText.setText(strings.get(0));
                }
            });
        }
        if (view == positive) {
            if (StringUtils.isBlank(editText.getText().toString()) || StringUtils.isBlank(pathText.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.profile_add_alert), Toast.LENGTH_SHORT).show();
            } else {
                Profiles.getProfiles().add(new Profile(editText.getText().toString(), new File(pathText.getText().toString())));
                UIManager.getInstance().getVersionUI().refreshProfile();
                dismiss();
            }
        }
        if (view == negative) {
            dismiss();
        }
    }
}
