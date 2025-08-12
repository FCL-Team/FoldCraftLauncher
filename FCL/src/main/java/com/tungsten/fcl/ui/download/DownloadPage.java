package com.tungsten.fcl.ui.download;

import static com.tungsten.fcl.ui.download.DownloadPageManager.PAGE_ID_DOWNLOAD_MOD;
import static com.tungsten.fcl.ui.download.DownloadPageManager.PAGE_ID_DOWNLOAD_MODPACK;
import static com.tungsten.fcl.ui.download.DownloadPageManager.PAGE_ID_DOWNLOAD_RESOURCE_PACK;
import static com.tungsten.fcl.ui.download.DownloadPageManager.PAGE_ID_DOWNLOAD_SHADER_PACK;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.tungsten.fcl.R;
import com.tungsten.fcl.databinding.PageDownloadBinding;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.ui.manage.ManageUI;
import com.tungsten.fcl.ui.version.Versions;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;

public class DownloadPage extends FCLCommonPage implements ManageUI.VersionLoadable, View.OnClickListener {

    protected RemoteModRepository repository;
    private final IntegerProperty pageOffset = new SimpleIntegerProperty(0);
    private final IntegerProperty pageCount = new SimpleIntegerProperty(-1);
    private final RemoteModVersionPage.DownloadCallback callback;
    protected final BooleanProperty supportChinese = new SimpleBooleanProperty();
    private final ObjectProperty<Profile.ProfileVersion> version = new SimpleObjectProperty<>();
    protected final ListProperty<String> downloadSources = new SimpleListProperty<>(this, "downloadSources", FXCollections.observableArrayList());
    protected final StringProperty downloadSource = new SimpleStringProperty();
    private final StringProperty gameVersion = new SimpleStringProperty(this, "gameVersion", "");
    private final ObjectProperty<CategoryIndented> category = new SimpleObjectProperty<>(this, "category", new CategoryIndented(0, null));
    private final ObjectProperty<RemoteModRepository.SortType> sortType = new SimpleObjectProperty<>(this, "sortType", RemoteModRepository.SortType.POPULARITY);
    private TaskExecutor executor;
    private Runnable retrySearch;
    private RemoteModListAdapter adapter;

    private ScrollView searchLayout;

    private FCLEditText nameEditText;
    private FCLTextView sourceText;
    private FCLSpinner<String> sourceSpinner;
    private FCLSpinner<String> gameVersionSpinner;
    private FCLSpinner<CategoryIndented> categorySpinner;
    private FCLSpinner<RemoteModRepository.SortType> sortSpinner;

    private FCLButton search;

    private LinearLayoutCompat listLayout;
    private FCLTextView page;
    private FCLButton next;
    private FCLButton previous;
    private FCLButton first;
    private FCLButton last;
    private ListView listView;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;

    PageDownloadBinding binding;
    ModLoaderType selectedModLoader;
    private final DownloadProvider downloadProvider;

    public void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            search.setEnabled(!loading);
            nameEditText.setEnabled(!loading);
            sourceSpinner.setEnabled(!loading);
            gameVersionSpinner.setEnabled(!loading);
            categorySpinner.setEnabled(!loading);
            sortSpinner.setEnabled(!loading);
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            listLayout.setVisibility(loading ? View.GONE : View.VISIBLE);
            listView.setVisibility(loading ? View.GONE : View.VISIBLE);
            if (loading) {
                retry.setVisibility(View.GONE);
            }
        });
    }

    public void setFailed() {
        Schedulers.androidUIThread().execute(() -> {
            retry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            listLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        });
    }

    public void search() {
        search(gameVersion.get(),
                category.get().getCategory(),
                pageOffset.get(),
                Objects.requireNonNull(nameEditText.getText()).toString(),
                sortType.get());
    }

    public void search(String userGameVersion, RemoteModRepository.Category category, int pageOffset, String searchFilter, RemoteModRepository.SortType sort) {
        retrySearch = null;
        setLoading(true);
        if (executor != null && !executor.isCancelled()) {
            executor.cancel();
        }
        executor = Task.supplyAsync(() -> {
                    RemoteModRepository.SearchResult result = repository.search(downloadProvider, userGameVersion, category, pageOffset, 50, searchFilter, sort, RemoteModRepository.SortOrder.DESC);
                    ArrayList<RemoteMod> list = (ArrayList<RemoteMod>) result.getResults().collect(Collectors.toList());
                    if (DownloadPage.this instanceof ModDownloadPage && selectedModLoader != null) {
                        list = (ArrayList<RemoteMod>) list.parallelStream().filter(mod -> {
                            try {
                                return mod.getData().loadVersions(repository).flatMap(v -> v.getLoaders().stream()).collect(Collectors.toList()).contains(selectedModLoader);
                            } catch (Throwable ignore) {
                            }
                            return true;
                        }).collect(Collectors.toList());
                    }
                    pageCount.set(result.getTotalPages());
                    return list;
                })
                .whenComplete(Schedulers.androidUIThread(), (list, exception) -> {
                    setLoading(false);
                    if (exception == null) {
                        adapter = new RemoteModListAdapter(getContext(), this, list, mod -> {
                            RemoteModInfoPage page = new RemoteModInfoPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon_info, this, mod, version.get(), callback);
                            DownloadPageManager.getInstance().showTempPage(page);
                        });
                        listView.setAdapter(adapter);
                    } else {
                        setFailed();
                        pageCount.set(-1);
                        retrySearch = () -> search(userGameVersion, category, pageOffset, searchFilter, sort);
                    }
                }).executor(true);
    }

    protected String getLocalizedCategory(String category) {
        return AndroidUtils.getLocalizedText(getContext(), "curse_category_" + category);
    }

    protected String getLocalizedCategoryIndent(CategoryIndented category) {
        return StringUtils.repeats(' ', category.getIndent() * 4) +
                (category.getCategory() == null
                        ? getContext().getString(R.string.curse_category_0)
                        : getLocalizedCategory(category.getCategory().getId()));
    }

    protected String getLocalizedOfficialPage() {
        return getContext().getString(R.string.mods_curseforge);
    }

    public DownloadPage(Context context, int id, FCLUILayout parent, int resId, RemoteModRepository repository) {
        super(context, id, parent, resId);
        this.repository = repository;
        this.downloadProvider = DownloadProviders.getDownloadProvider();
        switch (id) {
            case PAGE_ID_DOWNLOAD_MODPACK:
                this.callback = ((profile, version, file) -> Versions.downloadModpackImpl(context, parent, profile, file));
                break;
            case PAGE_ID_DOWNLOAD_MOD:
                this.callback = (profile, version, file) -> download(context, profile, version, file, "mods");
                break;
            case PAGE_ID_DOWNLOAD_RESOURCE_PACK:
                this.callback = (profile, version, file) -> download(context, profile, version, file, "resourcepacks");
                break;
            case PAGE_ID_DOWNLOAD_SHADER_PACK:
                this.callback = (profile, version, file) -> download(context, profile, version, file, "shaderpacks");
                break;
            default:
                this.callback = null;
                break;
        }

        if (repository != null) {
            create();
        }
    }

    public void create() {
        binding = PageDownloadBinding.bind(getContentView());
        searchLayout = findViewById(R.id.search_layout);
        ThemeEngine.getInstance().registerEvent(searchLayout, () -> searchLayout.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));

        search = findViewById(R.id.search);
        search.setOnClickListener(this);

        nameEditText = findViewById(R.id.name);
        sourceText = findViewById(R.id.download_source_text);
        sourceSpinner = findViewById(R.id.download_source);
        gameVersionSpinner = findViewById(R.id.game_version);
        categorySpinner = findViewById(R.id.category);
        sortSpinner = findViewById(R.id.sort);

        listLayout = findViewById(R.id.list_layout);
        page = findViewById(R.id.page);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        first.setOnClickListener(this);
        last.setOnClickListener(this);
        retry.setOnClickListener(this);

        nameEditText.setHint(supportChinese.get() ? getContext().getString(R.string.search_hint_chinese) : getContext().getString(R.string.search_hint_english));

        sourceText.setVisibility(downloadSources.getSize() > 1 ? View.VISIBLE : View.GONE);
        sourceSpinner.setVisibility(downloadSources.getSize() > 1 ? View.VISIBLE : View.GONE);
        if (downloadSources.getSize() > 1) {
            sourceSpinner.setDataList(new ArrayList<>(downloadSources));
            ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, new ArrayList<>(downloadSources));
            sourceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            sourceSpinner.setAdapter(sourceAdapter);
            sourceSpinner.setSelection(downloadSource.get().equals(getContext().getString(R.string.mods_modrinth)) ? 1 : 0);
            FXUtils.bindSelection(sourceSpinner, downloadSource);
        }

        gameVersionSpinner.setDataList(new ArrayList<>(Arrays.stream(RemoteModRepository.DEFAULT_GAME_VERSIONS).collect(Collectors.toList())));
        ArrayAdapter<String> gameVersionAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, new ArrayList<>(Arrays.stream(RemoteModRepository.DEFAULT_GAME_VERSIONS).collect(Collectors.toList())));
        gameVersionAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        gameVersionSpinner.setAdapter(gameVersionAdapter);
        gameVersionSpinner.setSelection(0);
        FXUtils.bindSelection(gameVersionSpinner, gameVersion);

        ArrayList<CategoryIndented> categoryDataList = new ArrayList<>();
        categoryDataList.add(new CategoryIndented(0, null));
        categorySpinner.setDataList(categoryDataList);
        ArrayList<String> categoryStringList = categoryDataList.stream().map(this::getLocalizedCategoryIndent).collect(Collectors.toCollection(ArrayList::new));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, categoryStringList);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);
        FXUtils.bindSelection(categorySpinner, category);
        downloadSource.addListener(observable -> refreshCategory(true));

        sortSpinner.setDataList(new ArrayList<>(Arrays.stream(RemoteModRepository.SortType.values()).collect(Collectors.toList())));
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(getContext().getString(R.string.curse_sort_popularity));
        sorts.add(getContext().getString(R.string.curse_sort_name));
        sorts.add(getContext().getString(R.string.curse_sort_date_created));
        sorts.add(getContext().getString(R.string.curse_sort_last_updated));
        sorts.add(getContext().getString(R.string.curse_sort_author));
        sorts.add(getContext().getString(R.string.curse_sort_total_downloads));
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, sorts);
        sortAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setSelection(0);
        FXUtils.bindSelection(sortSpinner, sortType);

        pageOffset.addListener(observable -> page.setText(AndroidUtils.getLocalizedText(
                getContext(), "search_page_n", pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()
        )));
        pageCount.addListener(observable -> page.setText(AndroidUtils.getLocalizedText(
                getContext(), "search_page_n", pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()
        )));

        refreshCategory(false);
        search("", null, 0, "", RemoteModRepository.SortType.POPULARITY);
    }

    private static void download(Context context, Profile profile, @Nullable String version, RemoteMod.Version file, String subdirectoryName) {
        if (version == null) version = profile.getSelectedVersion();

        Path runDirectory = profile.getRepository().hasVersion(version) ? profile.getRepository().getRunDirectory(version).toPath() : profile.getRepository().getBaseDirectory().toPath();

        DownloadAddonDialog dialog = new DownloadAddonDialog(context, file.getFile().getFilename(), name -> {
            Path dest = runDirectory.resolve(subdirectoryName).resolve(name);

            TaskDialog taskDialog = new TaskDialog(context, new TaskCancellationAction(AppCompatDialog::dismiss));
            taskDialog.setTitle(context.getString(R.string.message_downloading));
            Schedulers.androidUIThread().execute(() -> {
                TaskExecutor executor = Task.composeAsync(() -> {
                    FileDownloadTask task = new FileDownloadTask(NetworkUtils.toURL(file.getFile().getUrl()), dest.toFile());
                    task.setName(file.getName());
                    return task;
                }).whenComplete(Schedulers.androidUIThread(), exception -> {
                    if (exception != null) {
                        if (exception instanceof CancellationException) {
                            Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show();
                        } else {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setCancelable(false);
                            builder.setTitle(context.getString(R.string.install_failed_downloading));
                            builder.setMessage(DownloadProviders.localizeErrorMessage(context, exception));
                            builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                            builder.create().show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show();
                    }
                }).executor();
                taskDialog.setExecutor(executor);
                taskDialog.show();
                executor.start();
            });
        });
        dialog.show();
    }

    @Override
    public void loadVersion(Profile profile, String version) {
        this.version.set(new Profile.ProfileVersion(profile, version));
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == search) {
            pageOffset.set(0);
            search();
        }
        if (v == next && pageCount.get() > 1 && pageOffset.get() < pageCount.get() - 1) {
            pageOffset.set(pageOffset.get() + 1);
            search();
        }
        if (v == previous && pageOffset.get() > 0) {
            pageOffset.set(pageOffset.get() - 1);
            search();
        }
        if (v == first && pageCount.get() != 0 && pageCount.get() != -1) {
            pageOffset.set(0);
            search();
        }
        if (v == last && pageCount.get() != 0 && pageCount.get() != -1) {
            pageOffset.set(pageCount.get() - 1);
            search();
        }
        if (v == retry && retrySearch != null) {
            retrySearch.run();
        }
    }

    public RemoteModRepository getRepository() {
        return repository;
    }

    private static class CategoryIndented {
        private final int indent;
        private final RemoteModRepository.Category category;

        public CategoryIndented(int indent, RemoteModRepository.Category category) {
            this.indent = indent;
            this.category = category;
        }

        public int getIndent() {
            return indent;
        }

        public RemoteModRepository.Category getCategory() {
            return category;
        }
    }

    private static void resolveCategory(RemoteModRepository.Category category, int indent, List<CategoryIndented> result) {
        result.add(new CategoryIndented(indent, category));
        for (RemoteModRepository.Category subcategory : category.getSubcategories()) {
            resolveCategory(subcategory, indent + 1, result);
        }
    }

    private void refreshCategory(boolean search) {
        Task.supplyAsync(() -> repository.getCategories())
                .thenAcceptAsync(Schedulers.androidUIThread(), categories -> {
                    ArrayList<CategoryIndented> result = new ArrayList<>();
                    result.add(new CategoryIndented(0, null));
                    for (RemoteModRepository.Category category : Lang.toIterable(categories)) {
                        resolveCategory(category, 0, result);
                    }
                    categorySpinner.setDataList(result);
                    ArrayList<String> resultStr = result.stream().map(this::getLocalizedCategoryIndent).collect(Collectors.toCollection(ArrayList::new));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, resultStr);
                    adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
                    categorySpinner.setAdapter(adapter);
                    FXUtils.unbindSelection(categorySpinner, category);
                    categorySpinner.setSelection(0);
                    category.set(result.get(0));
                    FXUtils.bindSelection(categorySpinner, category);
                    if (search) search();
                }).start();
    }
}
