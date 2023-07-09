package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.tungsten.fcllibrary.util.LocaleUtils;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLImageView;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    private final RemoteModDownloadPage.DownloadCallback callback;
    private final DownloadPage page;

    private List<RemoteMod> dependencies;
    private SimpleMultimap<String, RemoteMod.Version> versions;

    private ConstraintLayout layout;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private FCLLinearLayout dependencyLayout;
    private ListView versionListView;
    private ListView dependencyListView;
    private FCLImageView icon;
    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView description;
    private FCLImageButton website;

    public RemoteModInfoPage(Context context, int id, FCLUILayout parent, int resId, DownloadPage page, RemoteMod addon, Profile.ProfileVersion version, @Nullable RemoteModDownloadPage.DownloadCallback callback) {
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

        dependencyLayout = findViewById(R.id.dependency_layout);
        versionListView = findViewById(R.id.version_list);
        dependencyListView = findViewById(R.id.dependency_list);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        description = findViewById(R.id.description);
        website = findViewById(R.id.website);

        retry.setOnClickListener(this);
        website.setOnClickListener(this);

        ThemeEngine.getInstance().registerEvent(versionListView, () -> versionListView.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));
    }

    @Override
    public void onStart() {
        super.onStart();

        icon.setImageDrawable(null);
        new Thread(() -> {
            try {
                URL url = new URL(addon.getIconUrl());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap icon = BitmapFactory.decodeStream(inputStream);
                Schedulers.androidUIThread().execute(() -> RemoteModInfoPage.this.icon.setImageBitmap(icon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        ModTranslations.Mod mod = translations.getModByCurseForgeId(addon.getSlug());
        name.setText(mod != null && LocaleUtils.isChinese(getContext()) ? mod.getDisplayName() : addon.getTitle());
        description.setText(addon.getDescription());
        List<String> categories = addon.getCategories().stream().map(page::getLocalizedCategory).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        categories.forEach(it -> stringBuilder.append(it).append("   "));
        String tag = StringUtils.removeSuffix(stringBuilder.toString(), "   ");
        this.tag.setText(tag);

        loadModVersions();
    }

    private void loadDependencies() {
        dependencyLayout.setVisibility(dependencies.size() > 0 ? View.VISIBLE : View.GONE);
        DependencyAdapter adapter = new DependencyAdapter(getContext(), page, dependencies, mod -> {
            RemoteModInfoPage page = new RemoteModInfoPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon_info, this.page, mod, version, callback);
            DownloadPageManager.getInstance().showTempPage(page);
        });
        dependencyListView.setAdapter(adapter);
    }

    private void loadGameVersions() {
        ModGameVersionAdapter adapter = new ModGameVersionAdapter(getContext(), versions.keys().stream()
                .sorted(VersionNumber.VERSION_COMPARATOR.reversed())
                .collect(Collectors.toList()), v -> {
            RemoteModDownloadPage page = new RemoteModDownloadPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon, new ArrayList<>(versions.get(v)), version, callback);
            DownloadPageManager.getInstance().showTempPage(page);
        });
        versionListView.setAdapter(adapter);
    }

    private void loadModVersions() {
        setLoading(true);

        Task.allOf(
                        Task.supplyAsync(() -> addon.getData().loadDependencies(repository)),
                        Task.supplyAsync(() -> {
                            Stream<RemoteMod.Version> versions = addon.getData().loadVersions(repository);
                            return sortVersions(versions);
                        }))
                .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                    setLoading(false);
                    if (exception == null) {
                        @SuppressWarnings("unchecked")
                        List<RemoteMod> dependencies = (List<RemoteMod>) result.get(0);
                        @SuppressWarnings("unchecked")
                        SimpleMultimap<String, RemoteMod.Version> versions = (SimpleMultimap<String, RemoteMod.Version>) result.get(1);

                        this.dependencies = dependencies;
                        this.versions = versions;

                        loadDependencies();
                        loadGameVersions();
                    } else {
                        setFailed();
                    }
                }).start();
    }

    private SimpleMultimap<String, RemoteMod.Version> sortVersions(Stream<RemoteMod.Version> versions) {
        SimpleMultimap<String, RemoteMod.Version> classifiedVersions
                = new SimpleMultimap<>(HashMap::new, ArrayList::new);
        versions.forEach(version -> {
            for (String gameVersion : version.getGameVersions()) {
                classifiedVersions.put(gameVersion, version);
            }
        });

        for (String gameVersion : classifiedVersions.keys()) {
            List<RemoteMod.Version> versionList = (List<RemoteMod.Version>) classifiedVersions.get(gameVersion);
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
        if (v == website && StringUtils.isNotBlank(addon.getPageUrl())) {
            AndroidUtils.openLink(getContext(), addon.getPageUrl());
        }
    }
}
