package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.ModTranslations;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.SimpleMultimap;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.versioning.VersionNumber;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RemoteModInfoPage extends FCLTempPage implements View.OnClickListener {

    private final RemoteModRepository repository;
    private final ModTranslations translations;
    private final RemoteMod addon;
    private final Profile.ProfileVersion version;
    private final RemoteModVersionPage.DownloadCallback callback;
    private final DownloadPage page;

    private SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> versions;

    private FCLLinearLayout layout;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private ListView versionListView;
    private FCLImageView icon;
    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView description;
    private FCLTextView mcmod;
    private FCLImageButton website;
    private FCLLinearLayout screenshotLayout;
    private FCLProgressBar screenshotLoading;
    private FCLImageView screenshotRetry;
    private FCLTextView screenshotNoResult;

    public RemoteModInfoPage(Context context, int id, FCLUILayout parent, int resId, DownloadPage page, RemoteMod addon, Profile.ProfileVersion version, @Nullable RemoteModVersionPage.DownloadCallback callback) {
        super(context, id, parent, resId);

        this.page = page;
        this.repository = page.repository;
        this.addon = addon;
        this.translations = ModTranslations.getTranslationsByRepositoryType(repository.getType());
        this.version = version;
        this.callback = callback;

        create();
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
        screenshotLayout = findViewById(R.id.screenshot_layout);
        screenshotLoading = findViewById(R.id.screenshot_loading);
        screenshotRetry = findViewById(R.id.screenshot_retry);
        screenshotNoResult = findViewById(R.id.screenshot_no_result);

        retry.setOnClickListener(this);
        mcmod.setOnClickListener(this);
        website.setOnClickListener(this);

        ThemeEngine.getInstance().registerEvent(versionListView, () -> versionListView.setBackgroundTintList(new ColorStateList(new int[][]{{}}, new int[]{ThemeEngine.getInstance().getTheme().getLtColor()})));
    }

    @Override
    public void onStart() {
        super.onStart();

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

    private void loadGameVersions() {
        ModGameVersionAdapter adapter = new ModGameVersionAdapter(getContext(), versions.keys().stream()
                .sorted(Collections.reverseOrder(VersionNumber::compare))
                .collect(Collectors.toList()), v -> {
            RemoteModVersionPage page = new RemoteModVersionPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon_version, new ArrayList<>(versions.get(v)), version, callback, RemoteModInfoPage.this.page);
            DownloadPageManager.getInstance().showTempPage(page);
        });
        versionListView.setAdapter(adapter);
    }

    private void loadModVersions() {
        setLoading(true);

        Task.supplyAsync(() -> {
            Stream<RemoteMod.Version> versions = addon.getData().loadVersions(repository);
            return sortVersions(versions);
        }).whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
            if (exception == null) {
                this.versions = result;
                loadGameVersions();
            } else {
                setFailed();
            }
            setLoading(false);
        }).start();
    }

    private void loadScreenshots() {
        setScreenshotLoading(true);

        Task.supplyAsync(() -> addon.getData().loadScreenshots(repository)).whenComplete(Schedulers.androidUIThread(), ((result, exception) -> {
            if (exception == null) {
                if (result.isEmpty()) {
                    screenshotNoResult.setVisibility(View.VISIBLE);
                } else {
                    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                    for (RemoteMod.Screenshot screenshot : result) {
                        FCLLinearLayout screenshotView = (FCLLinearLayout) layoutInflater.inflate(R.layout.view_mod_screenshot, null, false);

                        loadScreenshotImage(
                                screenshot.getImageUrl(),
                                screenshotView.findViewById(R.id.loading),
                                screenshotView.findViewById(R.id.screenshot),
                                screenshotView.findViewById(R.id.retry)
                        );

                        showScreenshotText(screenshot.getTitle(), screenshotView.findViewById(R.id.title));
                        showScreenshotText(screenshot.getDescription(), screenshotView.findViewById(R.id.description));

                        screenshotLayout.addView(screenshotView);
                    }
                }
            } else {
                setScreenshotFailed();
            }
            setScreenshotLoading(false);
        })).start();
    }

    private void loadScreenshotImage(String imageUrl, FCLProgressBar loadingView, FCLImageView imageView, FCLImageView reloadView) {
        loadingView.setVisibility(View.VISIBLE);
        reloadView.setVisibility(View.GONE);
        Glide.with(imageView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, @androidx.annotation.Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        reloadView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        loadingView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);

        reloadView.setOnClickListener(v -> loadScreenshotImage(imageUrl, loadingView, imageView, reloadView));
    }

    private void showScreenshotText(String text, FCLTextView textView) {
        if (StringUtils.isNotBlank(text)) textView.setText(text);
        else textView.setVisibility(View.GONE);
    }

    private SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> sortVersions(Stream<RemoteMod.Version> versions) {
        SimpleMultimap<String, RemoteMod.Version, List<RemoteMod.Version>> classifiedVersions
                = new SimpleMultimap<>(HashMap::new, ArrayList::new);
        versions.forEach(version -> {
            for (String gameVersion : version.getGameVersions()) {
                classifiedVersions.put(gameVersion, version);
            }
        });

        for (String gameVersion : classifiedVersions.keys()) {
            List<RemoteMod.Version> versionList = classifiedVersions.get(gameVersion);
            versionList.sort(Comparator.comparing(RemoteMod.Version::getDatePublished).reversed());
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
    public void onRestart() {

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
                AndroidUtils.openLink(getContext(), url);
            }
        }
        if (v == website && StringUtils.isNotBlank(addon.getPageUrl())) {
            AndroidUtils.openLink(getContext(), addon.getPageUrl());
        }
        if (v == screenshotRetry) {
            loadScreenshots();
        }
    }
}
