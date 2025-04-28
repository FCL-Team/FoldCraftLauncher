package com.tungsten.fcl.ui.controller;

import static com.tungsten.fcl.util.FXUtils.onInvalidating;

import android.app.Activity;
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
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.ControllerActivity;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public class ControllerManagePage extends FCLCommonPage implements View.OnClickListener {

    private final BooleanProperty refreshProperty;

    private ObjectProperty<Controller> selectedController;

    public Controller getSelectedController() {
        return selectedController.get();
    }

    public void setSelectedController(Controller selectedController) {
        this.selectedController.set(selectedController);
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
        refreshProperty = new SimpleBooleanProperty(false);
        create();
    }

    public void create() {
        Controllers.addCallback(this::init);
    }

    private void init() {
        selectedController = new SimpleObjectProperty<Controller>() {
            {
                Controllers.getControllers().addListener(onInvalidating(this::invalidated));
            }

            @Override
            protected void invalidated() {
                if (!Controllers.isInitialized()) return;

                Controller controller = get();
                if (Controllers.getControllers().isEmpty()) {
                    if (controller != null) {
                        set(null);
                    }
                } else {
                    if (!Controllers.getControllers().contains(controller)) {
                        set(Controllers.getControllers().get(0));
                    }
                }
            }
        };
        if (!Controllers.controllersProperty().isEmpty()) {
            selectedController.set(Controllers.controllersProperty().get(0));
        } else {
            selectedController.set(Controllers.DEFAULT_CONTROLLER);
        }

        listView = findViewById(R.id.controller_list);
        importController = findViewById(R.id.import_controller);
        createController = findViewById(R.id.create_controller);
        downloadController = findViewById(R.id.download_controller);
        importController.setOnClickListener(this);
        createController.setOnClickListener(this);
        downloadController.setOnClickListener(this);

        FCLLinearLayout infoLayout = findViewById(R.id.info_layout);
        infoLayout.visibilityProperty().bind(Bindings.createBooleanBinding(() -> selectedController.get() != null, selectedController));

        FCLTextView nameText = findViewById(R.id.name);
        FCLTextView versionText = findViewById(R.id.version);
        FCLTextView authorText = findViewById(R.id.author);
        FCLTextView descriptionText = findViewById(R.id.description);
        nameText.stringProperty().bind(Bindings.createStringBinding(() -> selectedController.get() == null ? "" : selectedController.get().getName(), selectedController, refreshProperty));
        versionText.stringProperty().bind(Bindings.createStringBinding(() -> selectedController.get() == null ? "" : selectedController.get().getVersion(), selectedController, refreshProperty));
        authorText.stringProperty().bind(Bindings.createStringBinding(() -> selectedController.get() == null ? "" : selectedController.get().getAuthor(), selectedController, refreshProperty));
        descriptionText.stringProperty().bind(Bindings.createStringBinding(() -> selectedController.get() == null ? "" : selectedController.get().getDescription(), selectedController, refreshProperty));

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

    private void refreshList() {
        EditableControllerListAdapter adapter = new EditableControllerListAdapter(getContext(), Controllers.controllersProperty());
        listView.setAdapter(adapter);
    }

    public void addController(Controller controller) {
        Schedulers.androidUIThread().execute(() -> {
            Controllers.addController(controller);
            refreshList();
            selectedController.set(controller);
        });
    }

    public void removeController(Controller controller) {
        Schedulers.androidUIThread().execute(() -> {
            Controllers.removeControllers(controller);
            refreshList();
            if (controller == selectedController.get()) {
                selectedController.set(null);
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

        refreshProperty.set(!refreshProperty.get());
        old.saveToDisk();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == importController) {
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FILE_CHOOSER);
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".json");
            builder.setSuffix(suffix);
            builder.setTitle(getContext().getString(R.string.control_import));
            builder.create().browse(getActivity(), RequestCodes.SELECT_CONTROLLER_CODE, ((requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_CONTROLLER_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    String path = FileBrowser.getSelectedFiles(data).get(0);
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
                    } catch (IOException e) {
                        Logging.LOG.log(Level.SEVERE, "Failed to import controller", e);
                    }
                }
            }));
        }
        if (view == createController) {
            ControllerInfoDialog dialog = new ControllerInfoDialog(getContext(), true, new Controller(""), this::addController);
            dialog.show();
        }
        if (view == downloadController) {
            UIManager.getInstance().getControllerUI().getPageManager().switchPage(ControllerPageManager.PAGE_ID_CONTROLLER_REPO);
        }
        if (view == upload) {
            ControllerUploadPage page = new ControllerUploadPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_controller_upload, selectedController.get());
            ControllerPageManager.getInstance().showTempPage(page);
        }
        if (view == share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            Uri uri = FileProvider.getUriForFile(getContext(), getContext().getString(com.tungsten.fcllibrary.R.string.file_browser_provider), new File(FCLPath.CONTROLLER_DIR, getSelectedController().getFileName()));
            intent.setType(AndroidUtils.getMimeType(FCLPath.CONTROLLER_DIR + "/" + getSelectedController().getFileName()));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            getActivity().startActivity(Intent.createChooser(intent, getContext().getString(R.string.control_share)));
        }
        if (view == editInfo) {
            ControllerInfoDialog dialog = new ControllerInfoDialog(getContext(), false, selectedController.get(), (controller) -> changeControllerInfo(selectedController.get(), controller));
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
}
