package com.tungsten.fcl.ui.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.platform.OperatingSystem;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLTextView;

public class ControllerInfoDialog extends FCLDialog implements View.OnClickListener {

    private final boolean create;
    private final Controller controller;
    private final Callback callback;

    private FCLEditText editName;

    private FCLEditText editVersion;
    private FCLEditText editVersionCode;
    private FCLEditText editAuthor;
    private FCLEditText editDescription;

    private FCLButton positive;
    private FCLButton negative;

    @SuppressLint("SetTextI18n")
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
        editVersionCode = findViewById(R.id.version_code);
        editAuthor = findViewById(R.id.author);
        editDescription = findViewById(R.id.description);

        editVersionCode.setIntegerFilter(1);

        editName.setText(controller.getName());
        editVersion.setText(controller.getVersion());
        editVersionCode.setText(controller.getVersionCode() + "");
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
            if (!OperatingSystem.isNameValid(editName.getText().toString()) || editName.getText().toString().equals("Error")) {
                Toast.makeText(getContext(), getContext().getString(R.string.control_info_name_invalid), Toast.LENGTH_SHORT).show();
            } else {
                String id = this.controller.getId();
                if (!editAuthor.getText().toString().equals(this.controller.getAuthor())) {
                    id = Controller.generateRandomId();
                }
                Controller controller = new Controller(id,
                        editName.getText().toString(),
                        editVersion.getText().toString(),
                        Integer.parseInt(StringUtils.isBlank(editVersionCode.getText().toString()) ? "1" : editVersionCode.getText().toString()),
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
