package com.tungsten.fcl.ui.manage;

import static com.tungsten.fclcore.util.Logging.LOG;
import static com.tungsten.fclcore.util.StringUtils.isNotBlank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.game.FCLGameRepository;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.DownloadUI;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.game.Version;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import kotlin.Unit;

public class ModListPage extends FCLPage implements ManageUI.VersionLoadable, View.OnClickListener {

    /**
     * 增量加载时每解析多少个模组刷新一次列表
     */
    private static final int BATCH_SIZE = 16;

    private final BooleanProperty modded = new SimpleBooleanProperty(this, "modded", false);
    private final ListProperty<ModInfoObject> itemsProperty = new SimpleListProperty<>(FXCollections.observableArrayList());
    /**
     * 已解析的全部模组（未过滤），勾选 enabled/disabled 时直接在其中筛选，仅在 UI 线程访问
     */
    private final List<ModInfoObject> allMods = new ArrayList<>();

    private ModManager modManager;
    private Profile profile;
    private String versionId;

    private boolean isSearching = false;

    private FCLTextView warningText;
    private ScrollView left;
    private CoordinatorLayout right;
    private FCLEditText searchBar;
    private FCLLinearLayout normalGroup;
    private FCLLinearLayout selectedGroup;
    private FCLButton addButton;
    private FCLButton checkUpdateAllButton;
    private FCLButton checkUpdateButton;
    private FCLButton refreshButton;
    private FCLButton deleteButton;
    private FCLButton selectAllButton;
    private FCLButton selectInvertButton;
    private FCLButton cancelButton;
    private FCLProgressBar progressBar;
    private RecyclerView recyclerView;

    private FCLCheckBox enabled;
    private FCLCheckBox disabled;

    private final LocalModListAdapter adapter;

    public ModListPage(Context context, int id, int resId) {
        super(context, id, resId);
        adapter = new LocalModListAdapter(getContext(), this, () -> {
            calculateMod();
            return Unit.INSTANCE;
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setRecyclerView(recyclerView);
        Bindings.bindContent(adapter.listProperty(), itemsProperty);

        adapter.selectedItemsProperty().addListener((InvalidationListener) observable -> switchLayout(adapter.selectedItemsProperty().getSize() > 0));

        moddedProperty().addListener(observable -> setEnable(isModded()));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        warningText = findViewById(R.id.warning);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        searchBar = findViewById(R.id.search_filter);
        normalGroup = findViewById(R.id.normal_layout);
        selectedGroup = findViewById(R.id.selected_layout);
        addButton = findViewById(R.id.add);
        checkUpdateAllButton = findViewById(R.id.check_update_all);
        checkUpdateButton = findViewById(R.id.check_update);
        refreshButton = findViewById(R.id.refresh);
        deleteButton = findViewById(R.id.delete);
        selectAllButton = findViewById(R.id.select_all);
        selectInvertButton = findViewById(R.id.select_invert);
        cancelButton = findViewById(R.id.cancel);
        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.list);
        enabled = findViewById(R.id.enabled);
        disabled = findViewById(R.id.disabled);

        addButton.setOnClickListener(this);
        checkUpdateAllButton.setOnClickListener(this);
        checkUpdateButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        selectInvertButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        CompoundButton.OnCheckedChangeListener listener = (compoundButton, b) -> {
            // 直接在已加载的模组列表中筛选，避免重新扫描磁盘
            itemsProperty.setAll(filterMods(allMods));
            if (isSearching) {
                search();
            }
        };
        enabled.setOnCheckedChangeListener(listener);
        disabled.setOnCheckedChangeListener(listener);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == addButton) {
            add();
        }
        if (v == checkUpdateAllButton) {
            checkUpdateAllButton.setFocusable(false);
            checkUpdates(false);
        }
        if (v == checkUpdateButton) {
            checkUpdateButton.setFocusable(false);
            checkUpdates(true);
        }
        if (v == refreshButton) {
            refresh();
        }
        if (v == deleteButton) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(getContext().getString(R.string.button_remove_confirm));
            builder.setPositiveButton(getContext().getString(R.string.button_remove), () -> removeSelected(adapter.selectedItemsProperty()));
            builder.setNegativeButton(null);
            builder.create().show();
        }
        if (v == selectAllButton) {
            adapter.selectAll();
        }
        if (v == selectInvertButton) {
            adapter.selectInvert();
        }
        if (v == cancelButton) {
            adapter.selectedItemsProperty().clear();
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void loadVersion(Profile profile, String version) {
        // 同一版本重复加载（如从其他页面返回时 ManageUI.onStart 触发）直接跳过，
        // 避免每次显示都全量重扫模组 zip：上百个模组时解析耗时长，
        // 且与上一次扫描交错时 calculateMod 在主线程触发 getMods 会 ANR。
        // 模组增删/更新/回滚等数据变更路径内部已有显式 loadMods 刷新。
        if (profile == this.profile && Objects.equals(version, this.versionId) && modManager != null) {
            return;
        }

        this.profile = profile;
        this.versionId = version;

        switchLayout(false);
        adapter.selectedItemsProperty().clear();
        cancelSearch();

        FCLGameRepository repository = profile.getRepository();
        Version resolved = repository.getResolvedPreservingPatchesVersion(versionId);
        LibraryAnalyzer libraryAnalyzer = LibraryAnalyzer.analyze(resolved, repository.getGameVersion(resolved).orElse(null));
        setModded(libraryAnalyzer.hasModLoader());
        loadMods(profile.getRepository().getModManager(version));
    }

    private void setEnable(boolean enable) {
        if (enable) {
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.VISIBLE);
            warningText.setVisibility(View.GONE);
        } else {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            warningText.setVisibility(View.VISIBLE);
        }
    }

    private void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            if (loading) {
                cancelSearch();
                searchBar.setEnabled(false);
                addButton.setEnabled(false);
                checkUpdateAllButton.setEnabled(false);
                checkUpdateButton.setEnabled(false);
                refreshButton.setEnabled(false);
                deleteButton.setEnabled(false);
                selectAllButton.setEnabled(false);
                selectInvertButton.setEnabled(false);
                cancelButton.setEnabled(false);
                // 禁用筛选复选框，避免加载中勾选触发列表全量重绘（跑马灯文字重置）
                enabled.setEnabled(false);
                disabled.setEnabled(false);
                // 加载期间保持列表可见，已解析的模组会分批显示出来
                // 禁用 itemAnimator，避免逐条插入时动画堆积卡顿
                recyclerView.setItemAnimator(null);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                searchBar.setEnabled(true);
                addButton.setEnabled(true);
                checkUpdateAllButton.setEnabled(true);
                checkUpdateButton.setEnabled(true);
                refreshButton.setEnabled(true);
                deleteButton.setEnabled(true);
                selectAllButton.setEnabled(true);
                selectInvertButton.setEnabled(true);
                cancelButton.setEnabled(true);
                enabled.setEnabled(true);
                disabled.setEnabled(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                cancelSearch();
            }
        });
    }

    private void switchLayout(boolean select) {
        if (select) {
            normalGroup.setVisibility(View.GONE);
            selectedGroup.setVisibility(View.VISIBLE);
        } else {
            normalGroup.setVisibility(View.VISIBLE);
            selectedGroup.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        loadMods(modManager);
    }

    private void loadMods(ModManager modManager) {
        this.modManager = modManager;
        CompletableFuture.supplyAsync(() -> {
            try {
                synchronized (ModListPage.this) {
                    setLoading(true);
                    // 清空旧列表，避免切换版本/刷新时旧内容与增量内容混合显示
                    Schedulers.androidUIThread().execute(() -> {
                        allMods.clear();
                        itemsProperty.clear();
                    });
                    // 边扫描边分批把已解析的模组追加到列表末尾显示，无需等待全部加载完成
                    List<ModInfoObject> pending = new ArrayList<>();
                    modManager.refreshMods(mod -> {
                        pending.add(new ModInfoObject(getContext(), mod));
                        if (pending.size() >= BATCH_SIZE) {
                            List<ModInfoObject> batch = new ArrayList<>(pending);
                            pending.clear();
                            Schedulers.androidUIThread().execute(() -> {
                                allMods.addAll(batch);
                                itemsProperty.addAll(filterMods(batch));
                            });
                        }
                    });
                    if (!pending.isEmpty()) {
                        List<ModInfoObject> batch = new ArrayList<>(pending);
                        Schedulers.androidUIThread().execute(() -> {
                            allMods.addAll(batch);
                            itemsProperty.addAll(filterMods(batch));
                        });
                    }
                    return null;
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }, Schedulers.defaultScheduler()).whenCompleteAsync((result, exception) -> {
            // 已被更新的 loadMods 取代时跳过，避免旧扫描回调操作新状态（如主线程触发未加载实例的 getMods）
            if (this.modManager != modManager) return;
            setLoading(false);
            if (exception == null)
                try {
                    // 增量阶段已把全部模组追加进列表，无需再整体刷新列表
                    calculateMod();
                    showBrokenModsDialog();
                } catch (Throwable e) {
                    LOG.log(Level.SEVERE, "Failed to load local mod list", e);
                }
            else
                LOG.log(Level.SEVERE, "Failed to load local mod list", exception);
        }, Schedulers.androidUIThread());
    }

    /**
     * 按 enabled/disabled 复选框过滤模组列表，仅在 UI 线程调用
     */
    private List<ModInfoObject> filterMods(List<ModInfoObject> list) {
        return list.stream().filter(modInfoObject -> {
            boolean active = modInfoObject.getModInfo().isActive();
            return (enabled.isChecked() && active) || (disabled.isChecked() && !active);
        }).collect(Collectors.toList());
    }

    /**
     * 加载完成后若有损坏的模组文件，弹对话框列出并询问是否删除（仅在 UI 线程调用）
     */
    private void showBrokenModsDialog() {
        List<Path> brokenFiles = modManager.getBrokenFiles();
        if (brokenFiles.isEmpty()) return;
        String names = brokenFiles.stream()
                .map(FileUtils::getName)
                .collect(Collectors.joining("\n"));
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setMessage(getContext().getString(R.string.message_broken_mods, names));
        builder.setPositiveButton(getContext().getString(R.string.button_remove), () -> deleteBrokenMods(brokenFiles));
        builder.setNegativeButton(getContext().getString(R.string.button_cancel), null);
        builder.create().show();
    }

    /**
     * 删除损坏的模组文件（UI 线程）
     */
    private void deleteBrokenMods(List<Path> brokenFiles) {
        try {
            for (Path file : brokenFiles) {
                Files.deleteIfExists(file);
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to delete broken mod files", e);
            Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public void add() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".jar");
        suffix.add(".zip");
        suffix.add(".litemod");
        MainActivity.getInstance().fileLauncher.launchMultiSelection(null, suffix, files -> {
            if (files == null) return;
            List<Object> res = files.stream().map(Uri::parse).filter(Objects::nonNull).map(uri -> {
                if (AndroidUtilKt.isDocUri(uri)) {
                    return uri;
                } else {
                    return new File(uri.toString());
                }
            }).collect(Collectors.toList());

            // It's guaranteed that succeeded and failed are thread safe here.
            List<String> succeeded = new ArrayList<>(res.size());
            List<String> failed = new ArrayList<>();

            Task.runAsync(() -> {
                for (Object obj : res) {
                    if (obj instanceof File file) {
                        try {
                            modManager.addMod(file.toPath());
                            succeeded.add(file.getName());
                        } catch (Exception e) {
                            LOG.log(Level.WARNING, "Unable to add mod " + file, e);
                            failed.add(file.getName());

                            // Actually addMod will not throw exceptions because FileChooser has already filtered files.
                        }
                    } else {
                        try {
                            Uri uri = (Uri) obj;
                            String name = AndroidUtilKt.getFileName(getActivity(), uri);
                            modManager.addMod(getActivity(), uri, name);
                            succeeded.add(name);
                        } catch (Exception e) {
                            LOG.log(Level.WARNING, "Unable to add mod " + obj.toString(), e);
                            failed.add(obj.toString());

                            // Actually addMod will not throw exceptions because FileChooser has already filtered files.
                        }
                    }
                }
            }).withRunAsync(Schedulers.androidUIThread(), () -> {
                List<String> prompt = new ArrayList<>(1);
                if (!succeeded.isEmpty())
                    prompt.add(getContext().getString(R.string.mods_add_success, String.join(", ", succeeded)));
                if (!failed.isEmpty())
                    prompt.add(getContext().getString(R.string.mods_add_failed, String.join(", ", failed)));
                FCLAlertDialog.Builder builder1 = new FCLAlertDialog.Builder(getContext());
                builder1.setCancelable(false);
                builder1.setAlertLevel(failed.isEmpty() ? FCLAlertDialog.AlertLevel.INFO : FCLAlertDialog.AlertLevel.ALERT);
                builder1.setTitle(getContext().getString(R.string.mods_add));
                builder1.setMessage(String.join("\n", prompt));
                builder1.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                builder1.create().show();
                loadMods(modManager);
            }).start();
        });
    }

    public void removeSelected(ObservableList<ModInfoObject> selectedItems) {
        try {
            modManager.removeMods(selectedItems.stream()
                    .filter(Objects::nonNull)
                    .map(ModInfoObject::getModInfo)
                    .toArray(LocalModFile[]::new));
            loadMods(modManager);
        } catch (IOException ignore) {
            // Fail to remove mods if the game is running or the mod is absent.
        }
    }

    public void checkUpdates(boolean isSelected) {
        Runnable action = () -> {
            TaskDialog dialog = new TaskDialog(getContext(), TaskCancellationAction.NORMAL);
            dialog.setTitle(getContext().getString(R.string.update_checking));

            Task<?> task = Task
                    .composeAsync(() -> {
                        Optional<String> gameVersion = profile.getRepository().getGameVersion(versionId);
                        if (gameVersion.isPresent()) {
                            if (isSelected) {
                                return new ModCheckUpdatesTask(gameVersion.get(), adapter.selectedItemsProperty().stream()
                                        .filter(Objects::nonNull)
                                        .map(ModInfoObject::getModInfo)
                                        .collect(Collectors.toList()));
                            } else {
                                return new ModCheckUpdatesTask(gameVersion.get(), modManager.getMods());
                            }
                        }
                        return null;
                    })
                    .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                        checkUpdateAllButton.setFocusable(true);
                        checkUpdateButton.setFocusable(true);
                        if (exception instanceof CancellationException) return;
                        if (exception != null || result == null) {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setTitle(getContext().getString(R.string.message_failed));
                            builder.setMessage("Failed to check updates");
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                            builder.create().show();
                        } else if (result.isEmpty()) {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                            builder.setMessage(getContext().getString(R.string.mods_check_updates_empty));
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                            builder.create().show();
                        } else {
                            ModUpdatesPage page = new ModUpdatesPage(getContext(), FCLPage.PAGE_ID_TEMP, R.layout.page_mod_update, this, modManager, result);
                            UIManager.getInstance().getManageUI().showTempPage(page);
                        }
                    })
                    .withStagesHint(Collections.singletonList("mods.check_updates"));
            TaskExecutor executor = task.executor();
            dialog.setExecutor(executor);
            dialog.show();
            executor.start();
        };

        if (profile.getRepository().isModpack(versionId)) {
            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
            builder.setMessage(getContext().getString(R.string.mods_update_modpack_mod_warning));
            builder.setPositiveButton(action::run);
            builder.setNegativeButton(null);
            builder.create().show();
        } else {
            action.run();
        }
    }

    public void download() {
        MainActivity.getInstance().refreshMenuView(null);
        MainActivity.getInstance().binding.download.setSelected(true);
        UIManager.getInstance().getDownloadUI().showDownloadPage(DownloadUI.PAGE_ID_DOWNLOAD_MOD);
    }

    public void rollback(LocalModFile from, LocalModFile to) {
        try {
            modManager.rollback(from, to);
            refresh();
        } catch (IOException ex) {
            Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelSearch() {
        if (isSearching) {
            isSearching = false;
            searchBar.setText("");
            Bindings.bindContent(adapter.listProperty(), itemsProperty);
        }
    }

    private void search() {
        isSearching = true;
        adapter.selectedItemsProperty().clear();

        Bindings.unbindContent(adapter.listProperty(), itemsProperty);

        String queryString = searchBar.getText().toString();
        if (StringUtils.isBlank(queryString)) {
            adapter.listProperty().setAll(itemsProperty.get());
        } else {
            adapter.listProperty().clear();

            Predicate<String> predicate;
            if (queryString.startsWith("regex:")) {
                try {
                    Pattern pattern = Pattern.compile(queryString.substring("regex:".length()));
                    predicate = s -> pattern.matcher(s).find();
                } catch (Throwable e) {
                    LOG.log(Level.WARNING, "Illegal regular expression", e);
                    return;
                }
            } else {
                String lowerQueryString = queryString.toLowerCase(Locale.ROOT);
                predicate = s -> s.toLowerCase(Locale.ROOT).contains(lowerQueryString);
            }

            // 一次性 setAll 整体替换，避免逐条 add 触发多次列表通知
            List<ModInfoObject> filtered = itemsProperty.get().stream().filter(item ->
                    predicate.test(item.getModInfo().getFileName()) || (item.getRemoteMod() != null && predicate.test(item.getRemoteMod().getTitle()))
            ).collect(Collectors.toList());
            adapter.listProperty().setAll(filtered);
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateMod() {
        try {
            List<LocalModFile> mods = modManager.getMods();
            long activeCount = mods.stream().filter(LocalModFile::isActive).count();
            enabled.setText(getContext().getString(R.string.enabled) + " (" + activeCount + ")");
            disabled.setText(getContext().getString(R.string.disabled) + " (" + (mods.size() - activeCount) + ")");
        } catch (Exception ignore) {
            enabled.setText(getContext().getString(R.string.enabled));
            disabled.setText(getContext().getString(R.string.disabled));
        }
    }

    public static class ModInfoObject {

        private final BooleanProperty active;
        private final LocalModFile localModFile;
        private final String title;
        private final String message;
        private final ModTranslations.Mod mod;
        private RemoteMod remoteMod;

        ModInfoObject(Context context, LocalModFile localModFile) {
            this.localModFile = localModFile;
            this.active = localModFile.activeProperty();

            StringBuilder title = new StringBuilder(localModFile.getName());
            if (isNotBlank(localModFile.getVersion()))
                title.append(" ").append(localModFile.getVersion());
            this.title = title.toString();

            StringBuilder message = new StringBuilder(localModFile.getFileName());
            if (isNotBlank(localModFile.getGameVersion()))
                message.append(", ").append(context.getString(R.string.archive_game_version)).append(": ").append(localModFile.getGameVersion());
            if (isNotBlank(localModFile.getAuthors()))
                message.append(", ").append(context.getString(R.string.archive_author)).append(": ").append(localModFile.getAuthors());
            this.message = message.toString();

            this.mod = ModTranslations.MOD.getMod(localModFile.getId(), localModFile.getName());
        }

        public BooleanProperty getActive() {
            return active;
        }

        String getTitle() {
            return title;
        }

        String getSubtitle() {
            return message;
        }

        LocalModFile getModInfo() {
            return localModFile;
        }

        public ModTranslations.Mod getMod() {
            return mod;
        }

        public RemoteMod getRemoteMod() {
            return remoteMod;
        }

        public void setRemoteMod(RemoteMod remoteMod) {
            this.remoteMod = remoteMod;
        }
    }

    public boolean isModded() {
        return modded.get();
    }

    public BooleanProperty moddedProperty() {
        return modded;
    }

    public void setModded(boolean modded) {
        this.modded.set(modded);
    }
}
