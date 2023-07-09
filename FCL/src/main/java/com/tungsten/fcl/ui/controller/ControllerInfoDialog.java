package com.tungsten.fcl.ui.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerInfoDialog extends FCLDialog implements View.OnClickListener {

    private final boolean create;
    private final Controller controller;
    private final Callback callback;

    private FCLEditText editName;

    private FCLEditText editVersion;
    private FCLEditText editAuthor;
    private FCLEditText editDescription;

    private FCLButton positive;
    private FCLButton negative;

    public ControllerInfoDialog(@NonNull Context context, boolean create, Controller controller, Callback callback) {
        super(context);
        this.create = create;
        this.controller = controller;
        this.callback = callback;
        setContentView(R.layout.dialog_controller_info);
        setCancelable(false);

        FCLTextView titleView = findViewById(R.id.title);
        titleView.setText(create ? getContext().getString(R.string.control_create) : getContext().getString(R.string.control_info_edit));

        editName = findViewById(R.id.name);

        FCLLinearLayout moreInfoLayout = findViewById(R.id.more_info_layout);
        editVersion = findViewById(R.id.version);
        editAuthor = findViewById(R.id.author);
        editDescription = findViewById(R.id.description);

        editName.setText(controller.getName());
        editVersion.setText(controller.getVersion());
        editAuthor.setText(controller.getAuthor());
        editDescription.setText(controller.getDescription());

        FCLCheckBox moreInfo = findViewById(R.id.more_info);
        moreInfo.addCheckedChangeListener();

        moreInfoLayout.visibilityProperty().bind(moreInfo.checkProperty());

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            List<String> nameList = Controllers.getControllers().stream().map(Controller::getName).collect(Collectors.toList());
            if (!create) {
                nameList.remove(controller.getName());
            }
            if (!OperatingSystem.isNameValid(editName.getText().toString()) || editName.getText().toString().equals("Error")) {
                Toast.makeText(getContext(), getContext().getString(R.string.control_info_name_invalid), Toast.LENGTH_SHORT).show();
            } else if (nameList.contains(editName.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.control_info_name_exist), Toast.LENGTH_SHORT).show();
            } else {
                Controller controller = new Controller(editName.getText().toString(),
                        editVersion.getText().toString(),
                        editAuthor.getText().toString(),
                        editDescription.getText().toString(),
                        this.controller.getControllerVersion());
                callback.onInfoGenerate(controller);
                dismiss();
            }
        }
        if (view == negative) {
            dismiss();
        }
    }

    public interface Callback {
        void onInfoGenerate(Controller controller);
    }
}
