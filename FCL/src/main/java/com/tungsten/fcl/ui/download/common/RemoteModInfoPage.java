package com.tungsten.fcl.ui.download.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.setting.Profiles;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.ui.download.DownloadUI;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.download.LibraryAnalyzer;
import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModLoaderType;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.SimpleMultimap;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.versioning.VersionNumber;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.util.LocaleUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RemoteModInfoPage extends FCLPage implements View.OnClickListener {

    private final RemoteModRepository repository;
    private final ModTranslations translations;
    private final RemoteMod addon;
    private final RemoteModVersionPage.DownloadCallback callback;
    private final DownloadPage page;

    private SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> versions;

    private LinearLayout layout;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private ListView versionListView;
    private FCLImageView icon;
    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView description;
    private FCLTextView mcmod;
    private FCLImageButton website;
    private FCLProgressBar screenshotLoading;
    private FCLImageView screenshotRetry;
    private FCLTextView screenshotNoResult;
    private RecyclerView screenshotView;
    private FCLEditText search;

    private String recommendedVersion;

    /**
     * 原始版本数据（未按游戏版本分类），目录/版本切换后重算推荐版本用
     */
    private List<RemoteMod.Version> allVersions;

    public RemoteModInfoPage(Context context, int id, DownloadPage page, RemoteMod addon, @Nullable RemoteModVersionPage.DownloadCallback callback) {
        super(context, id, R.layout.page_download_addon_info);

        this.page = page;
        this.repository = page.repository;
        this.addon = addon;
        this.translations = ModTranslations.getTranslationsByRepositoryType(repository.getType());
        this.callback = callback;

        create();

        // 原 onStart 逻辑：页面构造即填充内容并加载
        icon.setImageDrawable(null);
        Glide.with(getContext()).load(addon.getIconUrl()).into(icon);
        ModTranslations.Mod mod = translations.getModByCurseForgeId(addon.getSlug());
        mcmod.setVisibility(mod == null ? View.GONE : View.VISIBLE);
        name.setText(mod != null && LocaleUtils.isChinese(getContext()) ? mod.getDisplayName() : addon.getTitle());
        description.setText(addon.getDescription());
        List<String> categories = addon.getCategories().stream().map(page::getLocalizedCategory).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        categories.forEach(it -> stringBuilder.append(it).append("   "));
        String tag = StringUtils.removeSuffix(stringBuilder.toString(), "   ");
        this.tag.setText(tag);

        loadModVersions();
        loadScreenshots();
    }

    public void create() {
        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);

        versionListView = findViewById(R.id.version_list);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        description = findViewById(R.id.description);
        mcmod = findViewById(R.id.mcmod);
        website = findViewById(R.id.website);
        screenshotView = findViewById(R.id.screenshot_recyclerView);
        screenshotLoading = findViewById(R.id.screenshot_loading);
        screenshotRetry = findViewById(R.id.screenshot_retry);
        screenshotNoResult = findViewById(R.id.screenshot_no_result);
        search = findViewById(R.id.search);

        retry.setOnClickListener(this);
        mcmod.setOnClickListener(this);
        website.setOnClickListener(this);

        ThemeEngine.getInstance().registerEvent(versionListView, () -> versionListView.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));

        search.stringProperty().addListener(observable -> loadGameVersions());
    }

    private void loadGameVersions() {
        List<String> list = versions.keys().stream()
                .sorted(Collections.reverseOrder(VersionNumber::compare))
                .filter(it -> it.contains(Optional.ofNullable(search.getStringValue()).orElse("")))
                .collect(Collectors.toList());
        if (list.contains(recommendedVersion)) {
            list.remove(recommendedVersion);
            list.add(0, recommendedVersion);
        }
        ModGameVersionAdapter adapter = new ModGameVersionAdapter(getContext(), list, v -> {
            RemoteModVersionPage page = new RemoteModVersionPage(getContext(), FCLPage.PAGE_ID_TEMP, new ArrayList<>(versions.get(v)), callback, RemoteModInfoPage.this.page);
            UIManager.getInstance().getDownloadUI().showTempPage(page);
        });
        versionListView.setAdapter(adapter);
    }

    private void loadModVersions() {
        setLoading(true);

        Task.supplyAsync(() -> addon.getData().loadVersions(repository).collect(Collectors.toList()))
                .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                    if (exception == null) {
                        this.allVersions = result;
                        reloadVersions();
                        checkInstalled();
                    } else {
                        setFailed();
                    }
                    setLoading(false);
                }).start();
    }

    /**
     * 按当前选中的目录/版本重算推荐版本并刷新版本列表。
     * 推荐版本在构造加载时计算，页面存续期间目录/版本可能在其他页面被切换
     * （此时下载页不可见），由 DownloadUI 重新可见时调用
     */
    public void reloadVersions() {
        if (allVersions == null) return;
        recommendedVersion = null;
        this.versions = sortVersions(allVersions);
        loadGameVersions();
    }

    private void loadScreenshots() {
        setScreenshotLoading(true);

        Task.supplyAsync(() -> addon.getData().loadScreenshots(repository)).whenComplete(Schedulers.androidUIThread(), ((result, exception) -> {
            if (exception == null) {
                if (result.isEmpty()) {
                    screenshotNoResult.setVisibility(View.VISIBLE);
                } else {
                    RemoteModScreenshotAdapter adapter = new RemoteModScreenshotAdapter(getContext(), result);
                    screenshotView.setLayoutManager(new LinearLayoutManager(getContext()));
                    screenshotView.setAdapter(adapter);
                }
            } else {
                setScreenshotFailed();
            }
            setScreenshotLoading(false);
        })).start();
    }

    private void checkInstalled() {
        Task.supplyAsync(() -> {
            String remoteName = addon.getTitle().replace(" ", "").toLowerCase();
            List<LocalModFile> modFiles = Profiles.getSelectedProfile().getRepository().getModManager(Profiles.getSelectedVersion()).getMods().parallelStream().filter(localModFile -> {
                String localName = localModFile.getName().replace(" ", "").toLowerCase();
                return remoteName.contains(localName);
            }).collect(Collectors.toList());
            for (LocalModFile localModFile : modFiles) {
                try {
                    Optional<RemoteMod.Version> remoteVersion = repository.getRemoteVersionByLocalFile(localModFile, localModFile.getFile());
                    if (remoteVersion.isPresent()) {
                        String modId = remoteVersion.get().modid();
                        if (addon.getModID().equals(modId)) {
                            return remoteVersion.get();
                        }
                    }
                } catch (Throwable ignore) {
                }
            }
            return null;
        }).whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
            if (exception == null && result != null) {
                name.setText(String.format("[%s] %s", getContext().getString(R.string.installed), name.getText()));
            }
        }).start();
    }

    private SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> sortVersions(List<RemoteMod.Version> versions) {
        SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> classifiedVersions
                = new SimpleMultimap<>(HashMap::new, ArrayList::new);
        for (RemoteMod.Version version : versions) {
            for (String gameVersion : version.gameVersions()) {
                classifiedVersions.put(gameVersion, version);
            }
        }

        for (String gameVersion : classifiedVersions.keys()) {
            List<RemoteMod.Version> versionList = classifiedVersions.get(gameVersion);
            versionList.sort(Comparator.comparing(RemoteMod.Version::datePublished).reversed());
        }
        if (page.getPageId() != DownloadUI.PAGE_ID_DOWNLOAD_MODPACK) {
            Profile profile = Profiles.getSelectedProfile();
            if (profile.getSelectedVersion() != null) {
                LibraryAnalyzer analyzer = LibraryAnalyzer.analyze(profile.getRepository().getResolvedPreservingPatchesVersion(profile.getSelectedVersion()), profile.getSelectedVersion());
                Set<ModLoaderType> modLoaders = analyzer.getModLoaders();
                String mcv = analyzer.getVersion(LibraryAnalyzer.LibraryType.MINECRAFT).orElse("");

                if (classifiedVersions.keys().contains(mcv)) {
                    classifiedVersions.get(mcv).stream().filter(v -> {
                        if (page.getPageId() == DownloadUI.PAGE_ID_DOWNLOAD_MOD) {
                            for (ModLoaderType loader : v.loaders()) {
                                if (modLoaders.contains(loader)) {
                                    recommendedVersion = getContext().getString(R.string.recommend_version) + ": " + mcv + " " + loader.name();
                                    return true;
                                }
                            }
                        } else {
                            recommendedVersion = getContext().getString(R.string.recommend_version) + ": " + mcv;
                            return true;
                        }
                        return false;
                    }).forEach(v -> classifiedVersions.put(recommendedVersion, v));
                }
            }
        }
        return classifiedVersions;
    }

    public void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            layout.setVisibility(loading ? View.GONE : View.VISIBLE);
            if (loading) {
                retry.setVisibility(View.GONE);
            }
        });
    }

    public void setFailed() {
        Schedulers.androidUIThread().execute(() -> {
            retry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        });
    }

    private void setScreenshotLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            screenshotLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
            if (loading) {
                screenshotRetry.setVisibility(View.GONE);
            }
        });
    }

    private void setScreenshotFailed() {
        Schedulers.androidUIThread().execute(() -> {
            screenshotRetry.setVisibility(View.VISIBLE);
            screenshotLoading.setVisibility(View.GONE);
        });
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == retry) {
            loadModVersions();
        }
        if (v == mcmod) {
            ModTranslations.Mod mod = translations.getModByCurseForgeId(addon.getSlug());
            if (mod != null) {
                String url = translations.getMcmodUrl(mod);
                AndroidUtilKt.openLink(getContext(), url);
            }
        }
        if (v == website && StringUtils.isNotBlank(addon.getPageUrl())) {
            AndroidUtilKt.openLink(getContext(), addon.getPageUrl());
        }
        if (v == screenshotRetry) {
            loadScreenshots();
        }
    }
}
