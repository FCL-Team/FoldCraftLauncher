package com.tungsten.fcl.ui.manage;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.util.FlowList;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclcore.mod.Datapack;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class DatapackListPage extends FCLTempPage implements View.OnClickListener {

    private final FlowList<DatapackInfoObject> items = new FlowList<>();

    private final Path worldDir;
    private final Datapack datapack;

    private FCLButton deleteButton;
    private FCLButton enableButton;
    private FCLButton disableButton;
    private FCLButton addButton;
    private FCLButton refreshButton;
    private FCLProgressBar progressBar;
    private ListView listView;
    private DatapackListAdapter adapter;

    public DatapackListPage(Context context, int id, FCLUILayout parent, int resId, String worldName, Path worldDir) {
        super(context, id, parent, resId);
        this.worldDir = worldDir;

        datapack = new Datapack(worldDir.resolve("datapacks"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        deleteButton = findViewById(R.id.delete);
        enableButton = findViewById(R.id.enable);
        disableButton = findViewById(R.id.disable);
        addButton = findViewById(R.id.add);
        refreshButton = findViewById(R.id.refresh);
        progressBar = findViewById(R.id.progress);
        listView = findViewById(R.id.list);

        deleteButton.setOnClickListener(this);
        enableButton.setOnClickListener(this);
        disableButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new DatapackListAdapter(getContext());
        listView.setAdapter(adapter);
        // 对齐 Bindings.bindContent：先同步当前快照，再跟随后续变化推入 adapter。
        FlowSubscriptions.subscribeWithCurrent(items.flow(), list -> adapter.listProperty().setAll(list));

        refresh();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View v) {
        if (v == deleteButton) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(getContext().getString(R.string.button_remove_confirm));
            builder.setPositiveButton(getContext().getString(R.string.button_remove), () -> removeSelected(adapter.selectedItemsProperty().get()));
            builder.setNegativeButton(null);
            builder.create().show();
        }
        if (v == enableButton) {
            enableSelected(adapter.selectedItemsProperty().get());
        }
        if (v == disableButton) {
            disableSelected(adapter.selectedItemsProperty().get());
        }
        if (v == addButton) {
            add();
        }
        if (v == refreshButton) {
            refresh();
        }
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        listView.setVisibility(loading ? View.GONE : View.VISIBLE);
        deleteButton.setEnabled(!loading);
        enableButton.setEnabled(!loading);
        disableButton.setEnabled(!loading);
        addButton.setEnabled(!loading);
        refreshButton.setEnabled(!loading);
    }

    private void installSingleDatapack(File datapack) throws IOException {
        Datapack zip = new Datapack(datapack.toPath());
        zip.loadFromZip();
        zip.installTo(worldDir);
    }

    boolean first = true;

    public void refresh() {
        setLoading(true);
        adapter.selectedItemsProperty().clear();
        Task.runAsync(datapack::loadFromDir)
                .withRunAsync(Schedulers.androidUIThread(), () -> {
                    if (first) {
                        // Datapack.info 已 StateFlow 化：映射为 DatapackInfoObject 快照写入 FlowList，
                        // 供 adapter 链路消费；发射在后台加载线程，
                        // 统一切回 UI 线程再更新列表。
                        FlowSubscriptions.subscribeWithCurrent(datapack.infoFlow(), packs ->
                                Schedulers.androidUIThread().execute(() ->
                                        items.setAll(packs.stream().map(DatapackInfoObject::new).collect(Collectors.toList()))));
                        first = false;
                    }
                    setLoading(false);
                })
                .start();
    }

    public void add() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".zip");
        MainActivity.getInstance().fileLauncher.launchMultiSelection(null,suffix,files->{
            List<File> res = files.stream().map(File::new).collect(Collectors.toList());
            FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
            builder1.setCancelable(false);
            builder1.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
            builder1.setMessage(getContext().getString(R.string.datapack_add));
            FCLAlertDialog installDialog = builder1.create();
            installDialog.show();
            new Thread(() -> {
                res.forEach(it -> {
                    try {
                        installSingleDatapack(it);
                    } catch (IOException e) {
                        Logging.LOG.log(Level.WARNING, "Unable to parse datapack file " + datapack, e);
                    }
                });
                Schedulers.androidUIThread().execute(() -> {
                    installDialog.dismiss();
                    refresh();
                });
            }).start();
        });
    }

    void removeSelected(List<DatapackInfoObject> selectedItems) {
        List<DatapackInfoObject> items = new ArrayList<>(selectedItems);
        items.stream()
                .map(DatapackInfoObject::getPackInfo)
                .forEach(pack -> {
                    try {
                        datapack.deletePack(pack);
                    } catch (IOException e) {
                        // Fail to remove mods if the game is running or the datapack is absent.
                        Logging.LOG.warning("Failed to delete datapack " + pack);
                    }
                });
    }

    void enableSelected(List<DatapackInfoObject> selectedItems) {
        selectedItems.stream()
                .map(DatapackInfoObject::getPackInfo)
                .forEach(info -> info.setActive(true));
    }

    void disableSelected(List<DatapackInfoObject> selectedItems) {
        selectedItems.stream()
                .map(DatapackInfoObject::getPackInfo)
                .forEach(info -> info.setActive(false));
    }

    public static class DatapackInfoObject {
        private final Datapack.Pack packInfo;

        DatapackInfoObject(Datapack.Pack packInfo) {
            this.packInfo = packInfo;
        }

        String getTitle() {
            return packInfo.getId();
        }

        String getSubtitle() {
            return StringUtils.parseColorEscapes(packInfo.getDescription().toString());
        }

        Datapack.Pack getPackInfo() {
            return packInfo;
        }
    }
}
