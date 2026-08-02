package com.tungsten.fcl.ui.controller;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import com.google.gson.GsonBuilder;
import com.mio.util.DialogUtilKt;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.ControllerActivity;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.compose.dialog.MiuixControllerInfoDialog;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.LayoutConverter;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.ui.ProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import kotlin.Unit;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class ControllerManagePage extends FCLCommonPage implements View.OnClickListener {

    private final MutableStateFlow<Boolean> refreshProperty;

    private MutableStateFlow<Controller> selectedController;

    public Controller getSelectedController() {
        return selectedController.getValue();
    }

    public void setSelectedController(Controller selectedController) {
        this.selectedController.setValue(selectedController);
    }

    private ListView listView;

    private LinearLayoutCompat importController;
    private LinearLayoutCompat createController;
    private LinearLayoutCompat downloadController;

    private FCLButton upload;
    private FCLButton share;
    private FCLButton editInfo;
    private FCLButton editController;

    public ControllerManagePage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
        refreshProperty = StateFlowKt.MutableStateFlow(false);
        create();
    }

    public void create() {
        Controllers.addCallback(this::init);
    }

    private void init() {
        selectedController = StateFlowKt.MutableStateFlow(null);
        // 阶段 4a：Controllers 列表已 StateFlow 化；任何变化（含元素冒泡）重新校验选中项
        FlowSubscriptions.subscribe(Controllers.controllersSignalFlow(), signal -> validateSelectedController());
        // 对齐原 invalidated()：选中项变化后重新校验（列表为空置 null，不在列表中回退到第一项）
        FlowSubscriptions.subscribe(selectedController, v -> validateSelectedController());
        if (!Controllers.getControllers().isEmpty()) {
            selectedController.setValue(Controllers.getControllers().get(0));
        } else {
            selectedController.setValue(Controllers.DEFAULT_CONTROLLER);
        }

        listView = findViewById(R.id.controller_list);
        importController = findViewById(R.id.import_controller);
        createController = findViewById(R.id.create_controller);
        downloadController = findViewById(R.id.download_controller);
        importController.setOnClickListener(this);
        createController.setOnClickListener(this);
        downloadController.setOnClickListener(this);

        FCLLinearLayout infoLayout = findViewById(R.id.info_layout);
        infoLayout.visibilityFlow().setValue(selectedController.getValue() != null);
        FlowSubscriptions.subscribe(selectedController, v -> infoLayout.visibilityFlow().setValue(selectedController.getValue() != null));

        FCLTextView nameText = findViewById(R.id.name);
        FCLTextView versionText = findViewById(R.id.version);
        FCLTextView authorText = findViewById(R.id.author);
        FCLTextView descriptionText = findViewById(R.id.description);
        nameText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getName());
        FlowSubscriptions.subscribe(selectedController, v -> nameText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getName()));
        FlowSubscriptions.subscribe(refreshProperty, v -> nameText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getName()));
        versionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getVersion());
        FlowSubscriptions.subscribe(selectedController, v -> versionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getVersion()));
        FlowSubscriptions.subscribe(refreshProperty, v -> versionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getVersion()));
        authorText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getAuthor());
        FlowSubscriptions.subscribe(selectedController, v -> authorText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getAuthor()));
        FlowSubscriptions.subscribe(refreshProperty, v -> authorText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getAuthor()));
        descriptionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getDescription());
        FlowSubscriptions.subscribe(selectedController, v -> descriptionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getDescription()));
        FlowSubscriptions.subscribe(refreshProperty, v -> descriptionText.stringFlow().setValue(selectedController.getValue() == null ? "" : selectedController.getValue().getDescription()));

        upload = findViewById(R.id.upload);
        share = findViewById(R.id.share);
        editInfo = findViewById(R.id.edit_info);
        editController = findViewById(R.id.edit_controller);
        upload.setOnClickListener(this);
        share.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        editController.setOnClickListener(this);

        refreshList();

        FCLProgressBar progress = findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
    }

    private void validateSelectedController() {
        if (!Controllers.isInitialized()) return;

        Controller controller = selectedController.getValue();
        if (Controllers.getControllers().isEmpty()) {
            if (controller != null) {
                selectedController.setValue(null);
            }
        } else {
            if (!Controllers.getControllers().contains(controller)) {
                selectedController.setValue(Controllers.getControllers().get(0));
            }
        }
    }

    private void refreshList() {
        EditableControllerListAdapter adapter = new EditableControllerListAdapter(getContext(), Controllers.getControllers());
        listView.setAdapter(adapter);
    }

    public void addController(Controller controller) {
        Schedulers.androidUIThread().execute(() -> {
            Controllers.addController(controller);
            refreshList();
            selectedController.setValue(controller);
        });
    }

    public void removeController(Controller controller) {
        Schedulers.androidUIThread().execute(() -> {
            Controllers.removeControllers(controller);
            refreshList();
            if (controller == selectedController.getValue()) {
                selectedController.setValue(null);
            }
        });
    }

    public void changeControllerInfo(Controller old, Controller newValue) {
        old.setName(newValue.getName());
        old.setVersion(newValue.getVersion());
        old.setVersionCode(newValue.getVersionCode());
        old.setAuthor(newValue.getAuthor());
        old.setDescription(newValue.getDescription());

        if (!old.getId().equals(newValue.getId())) {
            try {
                old.changeId(newValue.getId());
            } catch (IOException e) {
                Logging.LOG.log(Level.SEVERE, "Failed to change controller id!", e.getMessage());
            }
        }

        refreshProperty.setValue(!refreshProperty.getValue());
        old.saveToDisk();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == importController) {
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".json");
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, (files) -> {
                String path = files.get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
                }
                try {
                    String content = FileUtils.readText(new File(path));
                    Controller controller = new GsonBuilder().setPrettyPrinting().create().fromJson(content, Controller.class);
                    if (controller.getName().equals("Error")) {
                        Toast.makeText(getContext(), getContext().getString(R.string.control_import_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        addController(controller);
                    }
                } catch (Throwable e) {
                    DialogUtilKt.showErrorDialog(getContext(), getContext().getString(R.string.control_import_failed) + "\n" + e.getMessage());
                    Logging.LOG.log(Level.SEVERE, "Failed to import controller", e);
                }
            });
        }
        if (view == createController) {
            MiuixControllerInfoDialog dialog = new MiuixControllerInfoDialog(getContext(), true, new Controller(""), this::addController);
            dialog.show();
        }
        if (view == downloadController) {
            UIManager.getInstance().getControllerUI().getPageManager().switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO);
        }
        if (view == upload) {
            ControllerUploadPage page = new ControllerUploadPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_controller_upload, selectedController.getValue());
            ControllerPageManager.getInstance().showTempPage(page);
        }
        if (view == share) {
            DialogUtilKt.showItemSelectionDialog(
                    getContext(),
                    getContext().getString(R.string.control_share_choose),
                    List.of(getContext().getString(R.string.control_share_direct),
                            getContext().getString(R.string.control_share_zl2)),
                    true,
                    (index, selected) -> {
                        if (selected.equals(getContext().getString(R.string.control_share_direct))) {
                            shareDirect();
                        } else if (selected.equals(getContext().getString(R.string.control_share_zl2))) {
                            shareAsZl2();
                        }
                        return Unit.INSTANCE;
                    }
            );
        }
        if (view == editInfo) {
            MiuixControllerInfoDialog dialog = new MiuixControllerInfoDialog(getContext(), false, selectedController.getValue(), (controller) -> changeControllerInfo(selectedController.getValue(), controller));
            dialog.show();
        }
        if (view == editController) {
            Intent intent = new Intent(getContext(), ControllerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("controller", getSelectedController().getId());
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 直接分享原始 FCL 控制布局 JSON 文件。
     */
    private void shareDirect() {
        File file = new File(FCLPath.CONTROLLER_DIR, getSelectedController().getFileName());
        shareFile(file, R.string.control_share, AndroidUtils.getMimeType(file.getAbsolutePath()));
    }

    /**
     * 转换为 ZL2 格式后分享。
     */
    private void shareAsZl2() {
        if (!LayoutConverter.isSupported()) {
            Toast.makeText(getContext(), R.string.control_convert_unsupported, Toast.LENGTH_LONG).show();
            return;
        }
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.show();
        Controller controller = getSelectedController();
        File input = new File(FCLPath.CONTROLLER_DIR, controller.getFileName());
        // 输出到公共目录 FCL/share/，便于文件管理器定位
        File output = new File(FCLPath.SHARE_DIR, controller.getId() + "_zl2.json");
        //noinspection ResultOfMethodCallIgnored
        output.delete();
        Task.supplyAsync(() -> {
            return LayoutConverter.convertFclToZl2(input, output);
        }).thenAcceptAsync(Schedulers.androidUIThread(), (ExceptionalConsumer<String, Exception>) error -> {
            if (error == null) {
                shareFile(output, R.string.control_share_zl2_title, "application/json");
            } else {
                DialogUtilKt.showErrorDialog(getContext(), getContext().getString(R.string.control_convert_failed) + "\n" + error);
            }
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            dialog.dismiss();
            if (exception != null) {
                Logging.LOG.log(Level.SEVERE, "Failed to convert controller to ZL2", exception);
                DialogUtilKt.showErrorDialog(getContext(), getContext().getString(R.string.control_convert_failed) + "\n" + exception.getMessage());
            }
        }).start();
    }

    /**
     * 通过系统分享面板分享指定文件。
     */
    private void shareFile(File file, int chooserTitleRes, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getString(com.tungsten.fcllibrary.R.string.file_browser_provider), file);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().startActivity(Intent.createChooser(intent, getContext().getString(chooserTitleRes)));
    }
}
