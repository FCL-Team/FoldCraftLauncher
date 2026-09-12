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
import com.tungsten.fclcore.mod.LocalModFile;
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
import com.tungsten.fclcore.util.Logging;
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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Collectors;

import kotlin.Unit;

/**
 * 下载页：5 个下载模式（Mod/整合包/资源包/世界/光影）共享同一个页面实例，
 * 通过 {@link #switchType(int)} 切换 repository、下载回调与特有控件，
 * 各模式的搜索状态由 ViewModel 按页面 id 保存，切回时直接恢复。
 * 下载源支持聚合（CurseForge + Modrinth 并行检索、交错合并）或单源检索。
 */
public class DownloadPage extends FCLPage implements View.OnClickListener {

    private static final int SEARCH_PAGE_SIZE = 30;

    private int pageId = PAGE_ID_DOWNLOAD_MOD;
    protected RemoteModRepository repository;
    /** 聚合搜索使用的两个固定源仓库（仅本地化模式构建） */
    private LocalizedRepository aggregateCurseRepository;
    private LocalizedRepository aggregateModrinthRepository;
    private RemoteModVersionPage.DownloadCallback callback;
    private final IntegerProperty pageOffset = new SimpleIntegerProperty(0);
    private final IntegerProperty pageCount = new SimpleIntegerProperty(-1);
    protected final BooleanProperty supportChinese = new SimpleBooleanProperty();
    protected final ListProperty<String> downloadSources = new SimpleListProperty<>(this, "downloadSources", FXCollections.observableArrayList());
    protected final StringProperty downloadSource = new SimpleStringProperty();
    private final StringProperty gameVersion = new SimpleStringProperty(this, "gameVersion", "");
    private final ObjectProperty<Object> category = new SimpleObjectProperty<>(this, "category", null);
    private final ObjectProperty<RemoteModRepository.SortType> sortType = new SimpleObjectProperty<>(this, "sortType", RemoteModRepository.SortType.POPULARITY);
    private TaskExecutor executor;
    private Runnable retrySearch;
    private RemoteModListAdapter adapter;

    private ScrollView searchLayout;

    private FCLEditText nameEditText;
    private FCLTextView sourceText;
    private FCLSpinner<String> sourceSpinner;
    private FCLSpinner<String> gameVersionSpinner;
    private FCLSpinner<String> categorySpinner;
    /** 分类数据（与 spinner 显示的本地化文本按下标对应）：本地化模式为 DownloadCategory，世界模式为 CategoryIndented */
    private final ArrayList<Object> categoryData = new ArrayList<>();
    private FCLSpinner<String> sortSpinner;
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
     * 下载源变化时重置页码并重新搜索（分类为统一静态表，随源切换无需刷新）
     */
    private final InvalidationListener sourceListener = observable -> {
        pageOffset.set(0);
        search();
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
                setupRepositories(ModrinthRemoteModRepository.MODPACKS, CurseForgeRemoteModRepository.MODPACKS, RemoteModRepository.Type.MODPACK);
                break;
            case PAGE_ID_DOWNLOAD_MOD:
                setupRepositories(ModrinthRemoteModRepository.MODS, CurseForgeRemoteModRepository.MODS, RemoteModRepository.Type.MOD);
                break;
            case PAGE_ID_DOWNLOAD_RESOURCE_PACK:
                setupRepositories(ModrinthRemoteModRepository.RESOURCE_PACKS, CurseForgeRemoteModRepository.RESOURCE_PACKS, RemoteModRepository.Type.MOD);
                break;
            case PAGE_ID_DOWNLOAD_SHADER_PACK:
                setupRepositories(ModrinthRemoteModRepository.SHADER_PACKS, CurseForgeRemoteModRepository.SHADER_PACKS, RemoteModRepository.Type.MOD);
                break;
            default:
                repository = CurseForgeRemoteModRepository.WORLDS;
                aggregateCurseRepository = null;
                aggregateModrinthRepository = null;
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
            downloadSources.get().setAll(getContext().getString(R.string.mods_aggregate), getContext().getString(R.string.mods_curseforge), getContext().getString(R.string.mods_modrinth));
            downloadSource.set(getContext().getString(R.string.mods_aggregate));
        } else {
            downloadSources.clear();
            downloadSource.set(getContext().getString(R.string.mods_curseforge));
        }
        if (searchState.source != null) {
            downloadSource.set(searchState.source);
        }
        initSourceSpinner();
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
            applyModLoader(searchState.modLoaderPosition);
        }

        // 恢复该模式的搜索条件（搜索框/游戏版本/排序；分类在分类列表就绪后恢复）
        nameEditText.setText(searchState.searchFilter);
        int versionIndex = Math.max(versionList.indexOf(searchState.userGameVersion), 0);
        gameVersionSpinner.setSelection(versionIndex);
        gameVersion.set(versionList.get(versionIndex));
        sortSpinner.setSelection(searchState.sortType.ordinal());
        sortType.set(searchState.sortType);

        // 刷新分类并恢复搜索状态（有结果直接恢复，不重新搜索）
        if (pageId == PAGE_ID_DOWNLOAD_WORLD) {
            refreshCategory(false);
        } else {
            // 本地化模式：分类走统一静态表（CurseForge/Modrinth id 成对），无需联网加载
            setupUnifiedCategories();
        }
        if (searchState.result != null) {
            restoreResult();
        } else {
            search(searchState.userGameVersion, searchState.category, searchState.pageOffset, searchState.searchFilter, searchState.sortType);
        }
    }

    /**
     * 本地化模式：按当前模式构建统一分类表并恢复上次选中的分类
     */
    private void setupUnifiedCategories() {
        categoryData.clear();
        categoryData.addAll(DownloadCategory.forPageId(pageId));
        ArrayList<String> labels = categoryData.stream().map(this::categoryLabel).collect(Collectors.toCollection(ArrayList::new));
        categorySpinner.setItems(labels);
        int index = 0;
        if (searchState.category instanceof DownloadCategory dc) {
            int i = categoryData.indexOf(dc);
            if (i >= 0) {
                index = i;
            }
        }
        categorySpinner.setSelection(index);
        category.set(categoryData.get(index));
    }

    /** 分类条目的显示文本 */
    private String categoryLabel(Object entry) {
        if (entry instanceof DownloadCategory dc) {
            if (dc.isAll()) {
                return getContext().getString(R.string.curse_category_0);
            }
            String localized = AndroidUtilKt.getLocalizedText(getContext(), dc.getKey());
            return localized.startsWith("curse_category_") || localized.startsWith("modrinth_category_") ? dc.getFallback() : localized;
        }
        return getLocalizedCategoryIndent((CategoryIndented) entry);
    }

    /**
     * 下载源 spinner 初始化/刷新（数据与显隐随模式变化）
     */
    private void initSourceSpinner() {
        sourceText.setVisibility(downloadSources.getSize() > 1 ? View.VISIBLE : View.GONE);
        sourceSpinner.setVisibility(downloadSources.getSize() > 1 ? View.VISIBLE : View.GONE);
        if (downloadSources.getSize() > 1) {
            sourceSpinner.setItems(new ArrayList<>(downloadSources));
            sourceSpinner.setSelection(sourceIndexOf(downloadSource.get()));
            sourceSpinner.setOnItemSelectedListener((index, item) -> downloadSource.set(item));
        }
    }

    /** 下载源在下拉项中的下标（聚合/CurseForge/Modrinth），未知值按聚合处理 */
    private int sourceIndexOf(String source) {
        if (getContext().getString(R.string.mods_curseforge).equals(source)) return 1;
        if (getContext().getString(R.string.mods_modrinth).equals(source)) return 2;
        return 0;
    }

    /**
     * 按模式构建仓库：跟随下载源的动态仓库 + 聚合搜索的两个固定源仓库
     */
    private void setupRepositories(RemoteModRepository modrinth, RemoteModRepository curse, RemoteModRepository.Type type) {
        repository = new LocalizedRepository(modrinth, curse, type, null);
        aggregateCurseRepository = new LocalizedRepository(modrinth, curse, type, getContext().getString(R.string.mods_curseforge));
        aggregateModrinthRepository = new LocalizedRepository(modrinth, curse, type, getContext().getString(R.string.mods_modrinth));
    }

    /**
     * 本地化仓库（Modrinth/CurseForge 双源，按模式指定仓库与类型）；
     * pinnedSource 非空时固定为该源（聚合搜索的两个分支各持有一个固定实例），为空时跟随页面选中的下载源
     */
    private class LocalizedRepository extends LocalizedRemoteModRepository {
        private final RemoteModRepository modrinthRepository;
        private final RemoteModRepository curseRepository;
        private final Type type;
        private final String pinnedSource;

        LocalizedRepository(RemoteModRepository modrinthRepository, RemoteModRepository curseRepository, Type type, String pinnedSource) {
            this.modrinthRepository = modrinthRepository;
            this.curseRepository = curseRepository;
            this.type = type;
            this.pinnedSource = pinnedSource;
        }

        private boolean useModrinth() {
            String modrinth = getContext().getString(R.string.mods_modrinth);
            return modrinth.equals(pinnedSource != null ? pinnedSource : downloadSource.get());
        }

        @Override
        protected RemoteModRepository getBackedRemoteModRepository() {
            if (useModrinth()) {
                return modrinthRepository;
            } else {
                return curseRepository;
            }
        }

        @Override
        protected SortType getBackedRemoteModRepositorySortOrder() {
            if (useModrinth()) {
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

    /** 当前是否为聚合搜索（下载源选中聚合，仅本地化模式可选） */
    private boolean isAggregate() {
        return pageId != PAGE_ID_DOWNLOAD_WORLD && getContext().getString(R.string.mods_aggregate).equals(downloadSource.get());
    }

    /**
     * 条目对应的仓库：聚合模式按条目自身来源路由，其余模式返回当前仓库
     */
    RemoteModRepository repositoryFor(RemoteMod mod) {
        if (isAggregate()) {
            return mod.getData() instanceof CurseAddon ? aggregateCurseRepository : aggregateModrinthRepository;
        }
        return repository;
    }

    /** 聚合模式下条目的来源标注（CurseForge/Modrinth 平台名），单源模式返回空串 */
    String getSourceLabel(RemoteMod mod) {
        if (!isAggregate()) return "";
        return getContext().getString(mod.getData() instanceof CurseAddon ? R.string.mods_curseforge : R.string.mods_modrinth);
    }

    /**
     * 本地文件反查远程版本：聚合模式依次尝试 Modrinth 与 CurseForge 源（命中与未命中均有缓存），
     * 其余模式使用当前仓库
     */
    Optional<RemoteMod.Version> getRemoteVersionByLocalFile(LocalModFile localModFile, Path file) throws IOException {
        if (isAggregate()) {
            Optional<RemoteMod.Version> result = aggregateModrinthRepository.getRemoteVersionByLocalFile(localModFile, file);
            return result.isPresent() ? result : aggregateCurseRepository.getRemoteVersionByLocalFile(localModFile, file);
        }
        return repository.getRemoteVersionByLocalFile(localModFile, file);
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
                category.get(),
                pageOffset.get(),
                Objects.requireNonNull(nameEditText.getText()).toString(),
                sortType.get());
    }

    public void search(String userGameVersion, Object category, int pageOffset, String searchFilter, RemoteModRepository.SortType sort) {
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
                    SearchOutcome outcome;
                    if (isAggregate()) {
                        outcome = searchAggregated(userGameVersion, category instanceof DownloadCategory dc ? dc : null, pageOffset, searchFilter, sort);
                    } else {
                        RemoteModRepository.SearchResult result = repository.search(downloadProvider, userGameVersion, toSingleSourceCategory(category), pageOffset, SEARCH_PAGE_SIZE, searchFilter, sort, RemoteModRepository.SortOrder.DESC);
                        outcome = new SearchOutcome((ArrayList<RemoteMod>) result.getResults().collect(Collectors.toList()), result.getTotalPages(), null);
                    }
                    ArrayList<RemoteMod> list = outcome.mods();
                    if (pageId == PAGE_ID_DOWNLOAD_MOD && selectedModLoader != null) {
                        list = (ArrayList<RemoteMod>) list.parallelStream().filter(mod -> {
                            try {
                                return mod.getData().loadVersions(repositoryFor(mod)).flatMap(v -> v.loaders().stream()).collect(Collectors.toCollection(ArrayList::new)).contains(selectedModLoader);
                            } catch (Throwable ignore) {
                            }
                            return true;
                        }).collect(Collectors.toList());
                    }
                    pageCount.set(outcome.totalPages());
                    return outcome.withMods(list);
                })
                .whenComplete(Schedulers.androidUIThread(), (outcome, exception) -> {
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
                        searchState.result = outcome.mods();
                        searchState.pageCount = pageCount.get();
                        adapter = createAdapter(outcome.mods());
                        searchState.adapter = adapter;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                        if (outcome.partialWarning() != null) {
                            Toast.makeText(getContext(), outcome.partialWarning(), Toast.LENGTH_LONG).show();
                        }
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

    /**
     * 聚合搜索：两源并行检索后交错合并（同一序号 CurseForge 在前），并做跨平台保守合并
     * （归一化 slug/标题一致且两平台更新时间接近才隐藏其一，否则两个源的版本都显示）；
     * 分类筛选按各源自身的 id 过滤（仅一侧有该分类时只检索该侧）；
     * 单源失败不阻断另一源（降级为单源结果并提示），双源均失败时抛出异常。
     * 参考 ResourceSearcher（https://github.com/Meloong-Git/PCL/blob/main/Plain%20Craft%20Launcher%202/Modules/Resource/ResourceSearcher.vb）
     */
    private SearchOutcome searchAggregated(String userGameVersion, @Nullable DownloadCategory category, int pageOffset, String searchFilter, RemoteModRepository.SortType sort) throws IOException {
        // 「全部」或未选分类时两源都搜；单侧分类只搜拥有它的源
        CompletableFuture<RemoteModRepository.SearchResult> curseFuture = category == null || category.isAll() || category.getCfId() != null
                ? CompletableFuture.supplyAsync(
                        () -> searchUnchecked(aggregateCurseRepository, downloadProvider, userGameVersion, toCurseCategory(category), pageOffset, searchFilter, sort),
                        Schedulers.io())
                : null;
        CompletableFuture<RemoteModRepository.SearchResult> modrinthFuture = category == null || category.isAll() || category.getMrId() != null
                ? CompletableFuture.supplyAsync(
                        () -> searchUnchecked(aggregateModrinthRepository, downloadProvider, userGameVersion, toModrinthCategory(category), pageOffset, searchFilter, sort),
                        Schedulers.io())
                : null;

        RemoteModRepository.SearchResult curseResult = null;
        RemoteModRepository.SearchResult modrinthResult = null;
        IOException curseError = null;
        IOException modrinthError = null;
        if (curseFuture != null) {
            try {
                curseResult = joinSearch(curseFuture);
            } catch (IOException e) {
                curseError = e;
                Logging.LOG.log(Level.WARNING, "聚合搜索 CurseForge 源失败", e);
            }
        }
        if (modrinthFuture != null) {
            try {
                modrinthResult = joinSearch(modrinthFuture);
            } catch (IOException e) {
                modrinthError = e;
                Logging.LOG.log(Level.WARNING, "聚合搜索 Modrinth 源失败", e);
            }
        }
        if (curseResult == null && modrinthResult == null) {
            if (curseError != null) {
                throw curseError;
            }
            if (modrinthError != null) {
                throw modrinthError;
            }
            throw new IOException("Aggregated search returned no result");
        }

        List<RemoteMod> curseMods = curseResult == null ? List.of() : curseResult.getResults().collect(Collectors.toList());
        List<RemoteMod> modrinthMods = modrinthResult == null ? List.of() : modrinthResult.getResults().collect(Collectors.toList());
        ArrayList<RemoteMod> merged = new ArrayList<>(curseMods.size() + modrinthMods.size());
        Map<String, Instant> seenKeys = new HashMap<>();
        for (int i = 0; i < Math.max(curseMods.size(), modrinthMods.size()); i++) {
            if (i < curseMods.size()) addIfDistinct(merged, seenKeys, curseMods.get(i));
            if (i < modrinthMods.size()) addIfDistinct(merged, seenKeys, modrinthMods.get(i));
        }

        int totalPages = Math.max(curseResult == null ? 0 : curseResult.getTotalPages(), modrinthResult == null ? 0 : modrinthResult.getTotalPages());
        String warning = null;
        if (curseError != null) {
            warning = getContext().getString(R.string.search_aggregate_partial, getContext().getString(R.string.mods_curseforge), getContext().getString(R.string.mods_modrinth));
        } else if (modrinthError != null) {
            warning = getContext().getString(R.string.search_aggregate_partial, getContext().getString(R.string.mods_modrinth), getContext().getString(R.string.mods_curseforge));
        }
        return new SearchOutcome(merged, totalPages, warning);
    }

    /** 单源检索的分类参数：世界模式为原生分类直传，本地化模式按当前下载源取统一条目的对应平台 id */
    private RemoteModRepository.Category toSingleSourceCategory(Object entry) {
        if (entry instanceof RemoteModRepository.Category nativeCategory) {
            return nativeCategory;
        }
        return isModrinthSourceSelected() ? toModrinthCategory(entry) : toCurseCategory(entry);
    }

    /** 统一分类条目转 CurseForge 检索分类（无对应分类返回 null；检索时读取 self 的 id） */
    private static RemoteModRepository.Category toCurseCategory(Object entry) {
        if (!(entry instanceof DownloadCategory dc) || dc.getCfId() == null) {
            return null;
        }
        CurseAddon.Category self = new CurseAddon.Category(dc.getCfId(), 432, dc.getFallback(), "", "", "", Instant.EPOCH, false, 0, 0);
        return new RemoteModRepository.Category(self, String.valueOf(dc.getCfId()), List.of());
    }

    /** 统一分类条目转 Modrinth 检索分类（无对应分类返回 null；检索时仅读取 id） */
    private static RemoteModRepository.Category toModrinthCategory(Object entry) {
        if (!(entry instanceof DownloadCategory dc) || dc.getMrId() == null) {
            return null;
        }
        return new RemoteModRepository.Category(null, dc.getMrId(), List.of());
    }

    /** 当前是否选中 Modrinth 单源 */
    private boolean isModrinthSourceSelected() {
        return pageId != PAGE_ID_DOWNLOAD_WORLD && getContext().getString(R.string.mods_modrinth).equals(downloadSource.get());
    }

    /** 在异步线程执行单源搜索：Supplier 不允许受检异常，IOException 包装为 UncheckedIOException 传递 */
    private static RemoteModRepository.SearchResult searchUnchecked(RemoteModRepository repository, DownloadProvider downloadProvider, String gameVersion, RemoteModRepository.Category category, int pageOffset, String searchFilter, RemoteModRepository.SortType sort) {
        try {
            return repository.search(downloadProvider, gameVersion, category, pageOffset, SEARCH_PAGE_SIZE, searchFilter, sort, RemoteModRepository.SortOrder.DESC);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** 取异步搜索结果，统一包装为 IOException */
    private static RemoteModRepository.SearchResult joinSearch(CompletableFuture<RemoteModRepository.SearchResult> future) throws IOException {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (cause instanceof UncheckedIOException unchecked && unchecked.getCause() != null) {
                cause = unchecked.getCause();
            }
            if (cause instanceof IOException io) {
                throw io;
            }
            throw new IOException(cause);
        }
    }

    /** 条目唯一键：仅保留字母数字并转小写（空串返回 null，表示不参与去重） */
    private static String normalizeKey(String value) {
        if (value == null) return null;
        StringBuilder result = new StringBuilder(value.length());
        for (char c : value.toCharArray()) {
            if (Character.isLetterOrDigit(c)) result.append(Character.toLowerCase(c));
        }
        String normalized = result.toString();
        return normalized.isEmpty() ? null : normalized;
    }

    /** 跨平台重复条目的合并窗口：最近更新时间相差不超过该天数才视为同一工程的同步数据（PCL 的保守合并策略） */
    private static final long MERGE_WINDOW_DAYS = 7;

    /** 条目的最近更新时间（用于跨平台合并的同步性确认；数据不可得返回 null） */
    private static Instant lastModified(RemoteMod mod) {
        Object data = mod.getData();
        if (data instanceof CurseAddon addon) {
            return addon.dateModified();
        }
        if (data instanceof ModrinthRemoteModRepository.Project project) {
            return project.updated();
        }
        return null;
    }

    /**
     * 跨平台合并判定：归一化后的 slug 或标题一致，且两平台最近更新时间相差不超过
     * {@link #MERGE_WINDOW_DAYS} 天（同一工程的同步数据）才隐藏其一（保留先出现的条目）；
     * 数据不同步或时间不可得时两个平台的条目都显示，由用户自行选择前置/依赖更完整的源。
     */
    private static void addIfDistinct(ArrayList<RemoteMod> merged, Map<String, Instant> seenKeys, RemoteMod mod) {
        String slugKey = normalizeKey(mod.getSlug());
        String titleKey = normalizeKey(mod.getTitle());
        String hit = null;
        if (slugKey != null && seenKeys.containsKey("slug:" + slugKey)) {
            hit = "slug:" + slugKey;
        } else if (titleKey != null && seenKeys.containsKey("title:" + titleKey)) {
            hit = "title:" + titleKey;
        }
        if (hit != null) {
            Instant existing = seenKeys.get(hit);
            Instant modified = lastModified(mod);
            if (existing == null || modified == null
                    || Math.abs(Duration.between(existing, modified).toDays()) > MERGE_WINDOW_DAYS) {
                merged.add(mod);
            }
            return;
        }
        Instant modified = lastModified(mod);
        if (slugKey != null) {
            seenKeys.put("slug:" + slugKey, modified);
        }
        if (titleKey != null) {
            seenKeys.put("title:" + titleKey, modified);
        }
        merged.add(mod);
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
        // 复用缓存 adapter 时本地安装状态可能已变化，重新检测
        refreshInstalledState();
    }

    protected String getLocalizedCategoryIndent(CategoryIndented indented) {
        if (indented.category() == null) {
            return getContext().getString(R.string.curse_category_0);
        }
        StringBuilder result = new StringBuilder();
        result.append(StringUtils.repeats(' ', indented.indent() * 4));

        Object self = indented.category().self();
        boolean modrinth = self instanceof ModrinthRemoteModRepository.Category;
        String localized = getLocalizedCategory(indented.category().id(), modrinth);
        if (localized.startsWith("curse_category_") || localized.startsWith("modrinth_category_")) {
            // 未命中本地化，回退到平台原始名
            if (self instanceof CurseAddon.Category curseCategory) {
                result.append(curseCategory.name());
            } else if (self instanceof ModrinthRemoteModRepository.Category modrinthCategory) {
                result.append(modrinthCategory.name());
            }
        } else {
            result.append(localized);
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
        gameVersionSpinner.setItems(versionList);
        gameVersionSpinner.setSelection(0);
        gameVersionSpinner.setOnItemSelectedListener((index, item) -> gameVersion.set(item));

        // 占位分类（switchType 时按模式重建：本地化模式为统一静态表，世界模式为动态 CF 分类树）
        categoryData.add(DownloadCategory.ALL);
        ArrayList<String> categoryStringList = categoryData.stream().map(this::categoryLabel).collect(Collectors.toCollection(ArrayList::new));
        categorySpinner.setItems(categoryStringList);
        categorySpinner.setSelection(0);
        category.set(categoryData.get(0));
        categorySpinner.setOnItemSelectedListener((index, item) -> category.set(categoryData.get(index)));
        downloadSource.addListener(sourceListener);

        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(getContext().getString(R.string.curse_sort_popularity));
        sorts.add(getContext().getString(R.string.curse_sort_name));
        sorts.add(getContext().getString(R.string.curse_sort_date_created));
        sorts.add(getContext().getString(R.string.curse_sort_last_updated));
        sorts.add(getContext().getString(R.string.curse_sort_author));
        sorts.add(getContext().getString(R.string.curse_sort_total_downloads));
        // 条目为本地化文本，按下标映射到 SortType（顺序与枚举 values() 一致）
        sortSpinner.setItems(sorts);
        sortSpinner.setSelection(0);
        sortSpinner.setOnItemSelectedListener((index, item) -> sortType.set(RemoteModRepository.SortType.values()[index]));
        pageOffset.addListener(observable -> getActivity().runOnUiThread(() -> page.setText(getContext().getString(R.string.search_page_n, pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()))));
        pageCount.addListener(observable -> getActivity().runOnUiThread(() -> page.setText(getContext().getString(R.string.search_page_n, pageOffset.get() + 1, pageCount.get() == -1 ? "-" : pageCount.getValue().toString()))));

        // Mod 模式特有的加载器筛选
        List<String> modLoaderList = new ArrayList<>();
        modLoaderList.add(getContext().getString(R.string.curse_category_0));
        modLoaderList.add("Forge");
        modLoaderList.add("NeoForge");
        modLoaderList.add("Fabric");
        modLoaderList.add("Quilt");
        binding.modloader.setItems(modLoaderList);
        binding.modloader.setOnItemSelectedListener((index, item) -> applyModLoader(index));
    }

    /**
     * 应用加载器筛选（记录搜索状态并映射枚举；模式恢复时也会调用以同步选中值）
     */
    private void applyModLoader(int position) {
        searchState.modLoaderPosition = position;
        switch (position) {
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
                break;
            default:
                selectedModLoader = null;
                break;
        }
    }

    /**
     * 下载到当前选中游戏目录的指定子目录（版本未选中时落到根目录）
     */
    private void download(Context context, RemoteMod.Version file, String subdirectoryName) {
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
                        refreshInstalledState();
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
    public void downloadWithDependencies(Context context, Profile profile, @Nullable String version, RemoteMod.Version file, String subdirectoryName) {
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

    /** 提交单个模组文件到下载队列：队列标题与保存文件均使用原始文件名；成功完成后刷新安装状态 */
    private void submitModDownload(Context context, String filename, RemoteMod.Version version, Path modsDirectory) {
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
            } else if (exception == null) {
                refreshInstalledState();
            }
        }).executor();
        DownloadManager.submit(filename, fileTask, executor);
        executor.start();
    }

    /**
     * 本地已安装模组变化后刷新列表"已安装"标记（adapter 内部有变化检测，重复调用无害）
     */
    private void refreshInstalledState() {
        if (adapter != null) {
            adapter.refreshInstalledState();
        }
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

    /** 一次搜索的产物：结果条目、总页数与可选的部分失败提示（聚合模式单源失败时非空） */
    private record SearchOutcome(ArrayList<RemoteMod> mods, int totalPages, String partialWarning) {
        SearchOutcome withMods(ArrayList<RemoteMod> mods) {
            return new SearchOutcome(mods, totalPages, partialWarning);
        }
    }

    private static void resolveCategory(RemoteModRepository.Category category, int indent, List<CategoryIndented> result) {
        result.add(new CategoryIndented(indent, category));
        for (RemoteModRepository.Category subcategory : category.subcategories()) {
            resolveCategory(subcategory, indent + 1, result);
        }
    }

    /**
     * 世界模式：异步加载 CurseForge 分类树并恢复选中（世界无 Modrinth 源，不参与统一分类表）
     */
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
            categoryData.clear();
            categoryData.addAll(result);
            ArrayList<String> resultStr = result.stream().map(this::getLocalizedCategoryIndent).collect(Collectors.toCollection(ArrayList::new));
            categorySpinner.setItems(resultStr);
            categorySpinner.setSelection(0);
            category.set(result.get(0));
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

    /** 按分类 id 查本地化名：modrinth 为 true 时用 Modrinth 键前缀（资源包分类名中的 "+" 需去除），否则用 CurseForge 键前缀 */
    private String getLocalizedCategory(String category, boolean modrinth) {
        if (modrinth) {
            String key = "modrinth_category_" + category.replace("-", "_");
            if (pageId == PAGE_ID_DOWNLOAD_RESOURCE_PACK) {
                key = key.replaceAll("\\+", "");
            }
            return AndroidUtilKt.getLocalizedText(getContext(), key);
        }
        return AndroidUtilKt.getLocalizedText(getContext(), "curse_category_" + category);
    }

    /** 条目分类的本地化名：聚合模式按条目自身平台选键前缀，其余模式按当前下载源 */
    protected String getLocalizedCategory(RemoteMod mod, String category) {
        boolean modrinth = isAggregate() ? !(mod.getData() instanceof CurseAddon) : isModrinthSourceSelected();
        return getLocalizedCategory(category, modrinth);
    }

    public void jumpToModPage(RemoteMod mod) {
        String source = mod.getData() instanceof CurseAddon
                ? getContext().getString(R.string.mods_curseforge)
                : getContext().getString(R.string.mods_modrinth);
        sourceSpinner.setSelection(sourceIndexOf(source));
        downloadSource.set(source);
        RemoteModInfoPage page = new RemoteModInfoPage(getContext(), FCLPage.PAGE_ID_TEMP, this, mod, callback);
        UIManager.getInstance().getDownloadUI().showTempPage(page);
    }
}
