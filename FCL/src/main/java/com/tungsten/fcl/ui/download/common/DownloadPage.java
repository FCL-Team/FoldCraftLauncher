package com.tungsten.fcl.ui.download.common;

import static com.tungsten.fcl.ui.download.DownloadUI.PAGE_ID_DOWNLOAD_MOD;
import static com.tungsten.fcl.ui.download.DownloadUI.PAGE_ID_DOWNLOAD_MODPACK;
import static com.tungsten.fcl.ui.download.DownloadUI.PAGE_ID_DOWNLOAD_RESOURCE_PACK;
import static com.tungsten.fcl.ui.download.DownloadUI.PAGE_ID_DOWNLOAD_SHADER_PACK;
import static com.tungsten.fcl.ui.download.DownloadUI.PAGE_ID_DOWNLOAD_WORLD;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.databinding.PageDownloadBinding;
import com.tungsten.fcl.game.LocalizedRemoteModRepository;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.TranslationDialog;
import com.tungsten.fcl.ui.version.Versions;
import com.mio.download.DownloadManager;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
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
import com.tungsten.fclcore.mod.ModDependenciesResolver;
import com.tungsten.fclcore.mod.ModManager;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.mod.curse.CurseAddon;
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository;
import com.tungsten.fclcore.mod.modrinth.ModrinthRemoteModRepository;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.EditDialog;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;

import kotlin.Unit;

/**
 * 下载页：5 个下载模式（Mod/整合包/资源包/世界/光影）共享同一个页面实例，
 * 通过 {@link #switchType(int)} 切换 repository、下载回调与特有控件，
 * 各模式的搜索状态由 ViewModel 按页面 id 保存，切回时直接恢复。
 */
public class DownloadPage extends FCLPage implements View.OnClickListener {

    private int pageId = PAGE_ID_DOWNLOAD_MOD;
    protected RemoteModRepository repository;
    private RemoteModVersionPage.DownloadCallback callback;
    private final IntegerProperty pageOffset = new SimpleIntegerProperty(0);
    private final IntegerProperty pageCount = new SimpleIntegerProperty(-1);
    protected final BooleanProperty supportChinese = new SimpleBooleanProperty();
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
    private final ArrayList<String> versionList = new ArrayList<>();

    private FCLButton search;
    private FCLButton installModpack;
    private FCLButton translate;
    private LinearLayoutCompat listLayout;
    private FCLTextView page;
    private FCLButton next;
    private FCLButton previous;
    private FCLButton first;
    private FCLButton last;
    private RecyclerView recyclerView;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;

    protected PageDownloadBinding binding;
    protected ModLoaderType selectedModLoader;
    private final DownloadProvider downloadProvider;
    /**
     * 搜索状态（挂 Activity 的 ViewModel，模式切换与页面重建后恢复）
     */
    protected DownloadSearchViewModel.State searchState;
    /**
     * 下载源变化时重置页码、刷新分类并重新搜索（用户手动切换源时触发）
     */
    private final InvalidationListener sourceListener = observable -> {
        pageOffset.set(0);
        refreshCategory(true);
    };

    public DownloadPage(Context context) {
        super(context, FCLPage.PAGE_ID_TEMP, R.layout.page_download);
        this.downloadProvider = DownloadProviders.getDownloadProvider();
        create();
    }

    public int getPageId() {
        return pageId;
    }

    /**
     * 切换到指定下载模式：更新数据源、下载回调与特有控件，
     * 从 ViewModel 恢复该模式的搜索状态（有结果则不重新搜索）。
     */
    public void switchType(int pageId) {
        this.pageId = pageId;
        // 按模式获取搜索状态（各模式独立，避免恢复/写入到其他模式的状态）
        searchState = new ViewModelProvider(getActivity()).get(DownloadSearchViewModel.class).getState(pageId);

        // 数据源
        switch (pageId) {
            case PAGE_ID_DOWNLOAD_MODPACK:
                repository = new LocalizedRepository(ModrinthRemoteModRepository.MODPACKS, CurseForgeRemoteModRepository.MODPACKS, RemoteModRepository.Type.MODPACK);
                break;
            case PAGE_ID_DOWNLOAD_MOD:
                repository = new LocalizedRepository(ModrinthRemoteModRepository.MODS, CurseForgeRemoteModRepository.MODS, RemoteModRepository.Type.MOD);
                break;
            case PAGE_ID_DOWNLOAD_RESOURCE_PACK:
                repository = new LocalizedRepository(ModrinthRemoteModRepository.RESOURCE_PACKS, CurseForgeRemoteModRepository.RESOURCE_PACKS, RemoteModRepository.Type.MOD);
                break;
            case PAGE_ID_DOWNLOAD_SHADER_PACK:
                repository = new LocalizedRepository(ModrinthRemoteModRepository.SHADER_PACKS, CurseForgeRemoteModRepository.SHADER_PACKS, RemoteModRepository.Type.MOD);
                break;
            default:
                repository = CurseForgeRemoteModRepository.WORLDS;
                break;
        }

        // 下载回调（按模式决定安装目录）：触发下载时动态取当前选中的游戏目录与版本，
        // 避免页面存活期间切换目录后仍下载到旧目录
        switch (pageId) {
            case PAGE_ID_DOWNLOAD_MODPACK:
                callback = file -> Versions.downloadModpackImpl(getContext(), Profiles.getSelectedProfile(), file);
                break;
            case PAGE_ID_DOWNLOAD_MOD:
                callback = file -> download(getContext(), file, "mods");
                break;
            case PAGE_ID_DOWNLOAD_RESOURCE_PACK:
                callback = file -> download(getContext(), file, "resourcepacks");
                break;
            case PAGE_ID_DOWNLOAD_SHADER_PACK:
                callback = file -> download(getContext(), file, "shaderpacks");
                break;
            default:
                callback = null;
                break;
        }

        // 下载源（世界模式固定 CurseForge，无 Modrinth）。
        // 恢复期间临时移除监听，避免 downloadSource 变化触发 refreshCategory 的重复搜索
        downloadSource.removeListener(sourceListener);
        boolean localized = pageId != PAGE_ID_DOWNLOAD_WORLD;
        if (localized) {
            downloadSources.get().setAll(getContext().getString(R.string.mods_curseforge), getContext().getString(R.string.mods_modrinth));
            downloadSource.set(getContext().getString(R.string.mods_modrinth));
        } else {
            downloadSources.clear();
            downloadSource.set(getContext().getString(R.string.mods_curseforge));
        }
        initSourceSpinner();
        if (searchState.source != null) {
            downloadSource.set(searchState.source);
        }
        downloadSource.addListener(sourceListener);

        // 特有控件显隐
        boolean mod = pageId == PAGE_ID_DOWNLOAD_MOD;
        binding.modloader.setVisibility(mod ? View.VISIBLE : View.GONE);
        binding.modloaderText.setVisibility(mod ? View.VISIBLE : View.GONE);
        installModpack.setVisibility(pageId == PAGE_ID_DOWNLOAD_MODPACK ? View.VISIBLE : View.GONE);
        supportChinese.set(mod || pageId == PAGE_ID_DOWNLOAD_MODPACK);
        boolean chinese = LocaleUtils.isChinese(getContext());
        translate.setVisibility((mod || pageId == PAGE_ID_DOWNLOAD_MODPACK) && chinese ? View.VISIBLE : View.GONE);
        nameEditText.setHint(supportChinese.get() ? getContext().getString(R.string.search_hint_chinese) : getContext().getString(R.string.search_hint_english));
        if (mod) {
            binding.modloader.setSelection(searchState.modLoaderPosition);
        }

        // 恢复该模式的搜索条件（搜索框/游戏版本/排序；分类在分类列表就绪后恢复）
        nameEditText.setText(searchState.searchFilter);
        int versionIndex = versionList.indexOf(searchState.userGameVersion);
        gameVersionSpinner.setSelection(Math.max(versionIndex, 0));
        sortSpinner.setSelection(searchState.sortType.ordinal());

        // 刷新分类并恢复搜索状态（有结果直接恢复，不重新搜索）
        refreshCategory(false);
        if (searchState.result != null) {
            restoreResult();
        } else {
            search(searchState.userGameVersion, searchState.category, searchState.pageOffset, searchState.searchFilter, searchState.sortType);
        }
    }

    /**
     * 下载源 spinner 初始化/刷新（数据与显隐随模式变化）
     */
    private void initSourceSpinner() {
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
    }

    /**
     * 本地化仓库（Modrinth/CurseForge 双源，按模式指定仓库与类型）
     */
    private class LocalizedRepository extends LocalizedRemoteModRepository {
        private final RemoteModRepository modrinthRepository;
        private final RemoteModRepository curseRepository;
        private final Type type;

        LocalizedRepository(RemoteModRepository modrinthRepository, RemoteModRepository curseRepository, Type type) {
            this.modrinthRepository = modrinthRepository;
            this.curseRepository = curseRepository;
            this.type = type;
        }

        @Override
        protected RemoteModRepository getBackedRemoteModRepository() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return modrinthRepository;
            } else {
                return curseRepository;
            }
        }

        @Override
        protected SortType getBackedRemoteModRepositorySortOrder() {
            if (getContext().getString(R.string.mods_modrinth).equals(downloadSource.get())) {
                return SortType.NAME;
            } else {
                return SortType.POPULARITY;
            }
        }

        @Override
        public Type getType() {
            return type;
        }
    }

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
            recyclerView.setVisibility(loading ? View.GONE : View.VISIBLE);
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
            recyclerView.setVisibility(View.GONE);
        });
    }

    public void search() {
        search(gameVersion.get(),
                category.get().category(),
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
        // 保存搜索条件，模式切换后据此恢复
        searchState.userGameVersion = userGameVersion;
        searchState.category = category;
        searchState.pageOffset = pageOffset;
        searchState.searchFilter = searchFilter;
        searchState.sortType = sort;
        searchState.source = downloadSource.get();
        int searchPageId = pageId;
        executor = Task.supplyAsync(() -> {
                    RemoteModRepository.SearchResult result = repository.search(downloadProvider, userGameVersion, category, pageOffset, 30, searchFilter, sort, RemoteModRepository.SortOrder.DESC);
                    ArrayList<RemoteMod> list = (ArrayList<RemoteMod>) result.getResults().collect(Collectors.toList());
                    if (pageId == PAGE_ID_DOWNLOAD_MOD && selectedModLoader != null) {
                        list = (ArrayList<RemoteMod>) list.parallelStream().filter(mod -> {
                            try {
                                return mod.getData().loadVersions(repository).flatMap(v -> v.loaders().stream()).collect(Collectors.toCollection(ArrayList::new)).contains(selectedModLoader);
                            } catch (Throwable ignore) {
                            }
                            return true;
                        }).collect(Collectors.toList());
                    }
                    pageCount.set(result.getTotalPages());
                    return list;
                })
                .whenComplete(Schedulers.androidUIThread(), (list, exception) -> {
                    // 模式已切换时跳过过期回调，避免旧模式结果覆盖当前页面
                    if (searchPageId != pageId) {
                        return;
                    }
                    if (exception instanceof CancellationException) {
                        // 任务被取消（重新搜索/切换模式发起了新任务）：不改变界面状态
                        return;
                    }
                    setLoading(false);
                    if (exception == null) {
                        // 保存搜索结果与 adapter，切回该模式时直接恢复显示
                        searchState.result = list;
                        searchState.pageCount = pageCount.get();
                        adapter = createAdapter(list);
                        searchState.adapter = adapter;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        setFailed();
                        pageCount.set(-1);
                        searchState.result = null;
                        searchState.pageCount = -1;
                        searchState.adapter = null;
                        retrySearch = () -> search(userGameVersion, category, pageOffset, searchFilter, sort);
                    }
                }).executor(true);
    }

    private RemoteModListAdapter createAdapter(ArrayList<RemoteMod> list) {
        return new RemoteModListAdapter(getContext(), this, list, mod -> {
            RemoteModInfoPage page = new RemoteModInfoPage(getContext(), FCLPage.PAGE_ID_TEMP, this, mod, callback);
            UIManager.getInstance().getDownloadUI().showTempPage(page);
        });
    }

    /**
     * 恢复该模式上次的搜索结果（切换回时调用），不重新搜索；
     * 复用该模式缓存的 adapter 时不重建列表，避免 item 滑入动画重播
     */
    private void restoreResult() {
        setLoading(false);
        retry.setVisibility(View.GONE);
        pageOffset.set(searchState.pageOffset);
        pageCount.set(searchState.pageCount);
        adapter = searchState.adapter;
        if (adapter == null) {
            adapter = createAdapter(searchState.result);
            searchState.adapter = adapter;
        }
        // DownloadUI 被 ViewPager2 回收重建后 RecyclerView 是全新视图（无 LayoutManager），
        // 复用缓存的 adapter 时也要补上，否则列表不会渲染
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.setAdapter(adapter);
    }

    protected String getLocalizedCategoryIndent(CategoryIndented indented) {
        if (indented.category() == null) {
            return getContext().getString(R.string.curse_category_0);
        }
        StringBuilder result = new StringBuilder();
        result.append(StringUtils.repeats(' ', indented.indent() * 4));

        String localized = getLocalizedCategory(indented.category().id());
        if (!localized.startsWith("curse_category_")) {
            result.append(localized);
            return result.toString();
        }
        Object self = indented.category().self();
        if (self instanceof CurseAddon.Category curseCategory) {
            result.append(curseCategory.name());
        } else if (self instanceof ModrinthRemoteModRepository.Category modrinthCategory) {
            result.append(modrinthCategory.name());
        }
        return result.toString();
    }

    public void create() {
        binding = PageDownloadBinding.bind(getContentView());
        searchState = new ViewModelProvider(getActivity()).get(DownloadSearchViewModel.class).getState(getPageId());
        searchLayout = findViewById(R.id.search_layout);
        ThemeEngine.getInstance().registerEvent(searchLayout, () -> searchLayout.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));

        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        installModpack = findViewById(R.id.install_modpack);
        installModpack.setOnClickListener(this);
        translate = findViewById(R.id.translate);
        translate.setOnClickListener(this);

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
        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        first.setOnClickListener(this);
        last.setOnClickListener(this);
        retry.setOnClickListener(this);
        page.setOnClickListener(this);

        nameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            }
            return false;
        });
        nameEditText.setHint(supportChinese.get() ? getContext().getString(R.string.search_hint_chinese) : getContext().getString(R.string.search_hint_english));

        versionList.addAll(Arrays.stream(RemoteModRepository.DEFAULT_GAME_VERSIONS).collect(Collectors.toList()));
        versionList.add(0, "");
        gameVersionSpinner.setDataList(versionList);
        ArrayAdapter<String> gameVersionAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, versionList);
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
        downloadSource.addListener(sourceListener);

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
        pageOffset.addListener(observable -> getActivity().runOnUiThread(() -> page.setText(getContext().getString(R.string.search_page_n, pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()))));
        pageCount.addListener(observable -> getActivity().runOnUiThread(() -> page.setText(getContext().getString(R.string.search_page_n, pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()))));

        // Mod 模式特有的加载器筛选
        List<String> modLoaderList = new ArrayList<>();
        modLoaderList.add(getContext().getString(R.string.curse_category_0));
        modLoaderList.add("Forge");
        modLoaderList.add("NeoForge");
        modLoaderList.add("Fabric");
        modLoaderList.add("Quilt");
        ArrayAdapter<String> modLoaderAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, modLoaderList);
        modLoaderAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.modloader.setAdapter(modLoaderAdapter);
        binding.modloader.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchState.modLoaderPosition = position;
                switch (position) {
                    case 0:
                        selectedModLoader = null;
                        break;
                    case 1:
                        selectedModLoader = ModLoaderType.FORGE;
                        break;
                    case 2:
                        selectedModLoader = ModLoaderType.NEO_FORGED;
                        break;
                    case 3:
                        selectedModLoader = ModLoaderType.FABRIC;
                        break;
                    case 4:
                        selectedModLoader = ModLoaderType.QUILT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedModLoader = null;
            }
        });
    }

    /**
     * 下载到当前选中游戏目录的指定子目录（版本未选中时落到根目录）
     */
    private static void download(Context context, RemoteMod.Version file, String subdirectoryName) {
        Profile profile = Profiles.getSelectedProfile();
        String version = profile.getSelectedVersion();

        Path runDirectory = version != null && profile.getRepository().hasVersion(version) ? profile.getRepository().getRunDirectory(version).toPath() : profile.getRepository().getBaseDirectory().toPath();

        DownloadAddonDialog dialog = new DownloadAddonDialog(context, file.file().filename(), name -> {
            Path dest = runDirectory.resolve(subdirectoryName).resolve(name);

            FileDownloadTask fileTask = new FileDownloadTask(NetworkUtils.toURL(file.file().url()), dest.toFile());
            fileTask.setName(file.name());
            Task<Void> downloadTask = Task.composeAsync(() -> fileTask);
            TaskExecutor executor = downloadTask.whenComplete(Schedulers.androidUIThread(), exception -> {
                    if (exception != null) {
                        if (exception instanceof CancellationException) {
                            Toast.makeText(context, context.getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show();
                        } else {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setCancelable(false);
                            builder.setTitle(context.getString(R.string.install_failed_downloading));
                            builder.setMessage(DownloadProviders.localizeErrorMessage(context, exception));
                            builder.setNegativeButton(context.getString(com.tungsten.fcl.R.string.dialog_positive), null);
                            builder.create().show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.install_success), Toast.LENGTH_SHORT).show();
                    }
                }).executor();
                DownloadManager.submit(name, fileTask, executor);
                executor.start();
        });
        dialog.show();
    }

    /**
     * 一键下载：后台解析该模组的全部 REQUIRED 前置闭包（含传递依赖、防循环），
     * 解析完成后主模组与所有前置一起加入下载队列；
     * 不弹命名对话框，直接使用原始文件名；解析失败的前置跳过并提示数量。
     * 本地 mods 目录已安装的模组（含本体）通过当前下载源的反查接口去重跳过。
     */
    public static void downloadWithDependencies(Context context, Profile profile, @Nullable String version, RemoteMod.Version file, String subdirectoryName) {
        if (version == null) version = profile.getSelectedVersion();
        Path runDirectory = profile.getRepository().hasVersion(version) ? profile.getRepository().getRunDirectory(version).toPath() : profile.getRepository().getBaseDirectory().toPath();
        Path modsDirectory = runDirectory.resolve(subdirectoryName);

        Toast.makeText(context, context.getString(R.string.mods_dependency_resolving), Toast.LENGTH_SHORT).show();

        // 前置的兼容性以所选模组版本自身的 gameVersions / loaders 为准，解析完一起入队；
        // 本体已安装时跳过本体，前置仍会安装
        Task.supplyAsync(() -> ModDependenciesResolver.resolve(file, modsDirectory,
                file.self().getType().getRemoteModRepository()))
                .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                    if (exception != null || result == null)
                        return;
                    if (!result.rootInstalled()) {
                        submitModDownload(context, file.file().filename(), file, modsDirectory);
                    } else {
                        Toast.makeText(context, context.getString(R.string.mods_already_installed), Toast.LENGTH_SHORT).show();
                    }
                    for (ModDependenciesResolver.ResolvedDependency dep : result.dependencies()) {
                        submitModDownload(context, dep.version().file().filename(), dep.version(), modsDirectory);
                    }
                    if (result.installedSkipped() > 0) {
                        Toast.makeText(context, context.getString(R.string.mods_installed_skipped_note, result.installedSkipped()), Toast.LENGTH_SHORT).show();
                    }
                    if (!result.failedTitles().isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.mods_dependency_skipped_note, result.failedTitles().size()), Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }

    /** 提交单个模组文件到下载队列：队列标题与保存文件均使用原始文件名 */
    private static void submitModDownload(Context context, String filename, RemoteMod.Version version, Path modsDirectory) {
        Path dest = modsDirectory.resolve(filename);
        FileDownloadTask fileTask = new FileDownloadTask(NetworkUtils.toURL(version.file().url()), dest.toFile(), version.file().getIntegrityCheck());
        fileTask.setName(filename);
        Task<Void> downloadTask = Task.composeAsync(() -> fileTask);
        TaskExecutor executor = downloadTask.whenComplete(Schedulers.androidUIThread(), exception -> {
            if (exception != null && !(exception instanceof CancellationException)) {
                FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
                builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                builder.setCancelable(false);
                builder.setTitle(context.getString(R.string.install_failed_downloading));
                builder.setMessage(DownloadProviders.localizeErrorMessage(context, exception));
                builder.setNegativeButton(context.getString(com.tungsten.fcl.R.string.dialog_positive), null);
                builder.create().show();
            }
        }).executor();
        DownloadManager.submit(filename, fileTask, executor);
        executor.start();
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
        if (v == installModpack) {
            Versions.importModpack(getContext());
        }
        if (v == translate) {
            showTranslationDialog();
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
        if (v == page && pageCount.get() != 0 && pageCount.get() != -1) {
            new EditDialog(getContext(), s -> {
                try {
                    int i = Integer.parseInt(s);
                    if (i <= 0) {
                        i = 1;
                    } else if (i > pageCount.get()) {
                        i = pageCount.get();
                    }
                    pageOffset.set(i - 1);
                    search();
                } catch (Throwable ignore) {
                }
            }).show();
        }
    }

    public RemoteModRepository getRepository() {
        return repository;
    }

    private record CategoryIndented(int indent, RemoteModRepository.Category category) {
    }

    private static void resolveCategory(RemoteModRepository.Category category, int indent, List<CategoryIndented> result) {
        result.add(new CategoryIndented(indent, category));
        for (RemoteModRepository.Category subcategory : category.subcategories()) {
            resolveCategory(subcategory, indent + 1, result);
        }
    }

    private void refreshCategory(boolean search) {
        int refreshPageId = pageId;
        Task.supplyAsync(() -> repository.getCategories())
                .thenAcceptAsync(Schedulers.androidUIThread(), categories -> {
                    // 模式已切换时跳过过期回调，避免旧模式分类覆盖当前页面
                    if (refreshPageId != pageId) {
                        return;
                    }
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
                    // 恢复该模式上次的分类筛选（分类列表就绪后）
                    if (searchState.category != null) {
                        for (int i = 1; i < result.size(); i++) {
                            if (searchState.category.equals(result.get(i).category())) {
                                categorySpinner.setSelection(i);
                                category.set(result.get(i));
                                break;
                            }
                        }
                    }
                    if (search) search();
                }).start();
    }

    protected void showTranslationDialog() {
        new TranslationDialog(getContext(), repository, s -> {
            nameEditText.setText(s);
            search();
            return Unit.INSTANCE;
        }).show();
    }

    protected String getLocalizedCategory(String category) {
        if (pageId != PAGE_ID_DOWNLOAD_WORLD && downloadSource.get() != null
                && downloadSource.get().equals(getContext().getString(R.string.mods_modrinth))) {
            String key = "modrinth_category_" + category.replace("-", "_");
            if (pageId == PAGE_ID_DOWNLOAD_RESOURCE_PACK) {
                key = key.replaceAll("\\+", "");
            }
            return AndroidUtilKt.getLocalizedText(getContext(), key);
        }
        return AndroidUtilKt.getLocalizedText(getContext(), "curse_category_" + category);
    }

    public void jumpToModPage(RemoteMod mod) {
        if (mod.getData() instanceof CurseAddon) {
            sourceSpinner.setSelection(0);
            downloadSource.set(sourceSpinner.getItemAtPosition(0).toString());
        } else {
            sourceSpinner.setSelection(1);
            downloadSource.set(sourceSpinner.getItemAtPosition(1).toString());
        }
        RemoteModInfoPage page = new RemoteModInfoPage(getContext(), FCLPage.PAGE_ID_TEMP, this, mod, callback);
        UIManager.getInstance().getDownloadUI().showTempPage(page);
    }
}
