package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIManager;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Pair;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLLinearLayout;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.ConvertUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

public class RemoteModDownloadPage extends FCLTempPage implements View.OnClickListener {

    public static final EnumMap<RemoteMod.DependencyType, String> STRING_ID_KEY = new EnumMap<>(Lang.mapOf(
            Pair.pair(RemoteMod.DependencyType.EMBEDDED, "mods_dependency_embedded"),
            Pair.pair(RemoteMod.DependencyType.OPTIONAL, "mods_dependency_optional"),
            Pair.pair(RemoteMod.DependencyType.REQUIRED, "mods_dependency_required"),
            Pair.pair(RemoteMod.DependencyType.TOOL, "mods_dependency_tool"),
            Pair.pair(RemoteMod.DependencyType.INCLUDE, "mods_dependency_include"),
            Pair.pair(RemoteMod.DependencyType.INCOMPATIBLE, "mods_dependency_incompatible"),
            Pair.pair(RemoteMod.DependencyType.BROKEN, "mods_dependency_broken")
    ));

    private final Profile.ProfileVersion version;
    private final RemoteMod.Version modVersion;
    private final RemoteModVersionPage.DownloadCallback callback;
    private final RemoteModVersionPage lastPage;
    private final DownloadPage downloadPage;

    private FCLTextView name;
    private FCLTextView tag;
    private FCLTextView date;
    private ScrollView dependencyLayout;
    private FCLLinearLayout dependencyContainer;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;
    private FCLButton download;
    private FCLButton saveAs;
    private FCLButton cancel;

    public RemoteModDownloadPage(Context context, int id, FCLUILayout parent, int resId, Profile.ProfileVersion version, RemoteMod.Version modVersion, RemoteModVersionPage.DownloadCallback callback, RemoteModVersionPage lastPage, DownloadPage downloadPage) {
        super(context, id, parent, resId);
        this.version = version;
        this.modVersion = modVersion;
        this.callback = callback;
        this.lastPage = lastPage;
        this.downloadPage = downloadPage;

        create();
    }

    private void loadDependencies(RemoteMod.Version version) {
        setLoading(true, false);
        Task.supplyAsync(() -> {
            EnumMap<RemoteMod.DependencyType, List<RemoteMod>> dependencies = new EnumMap<>(RemoteMod.DependencyType.class);
            for (RemoteMod.Dependency dependency : version.getDependencies()) {
                if (dependency.getType() == RemoteMod.DependencyType.INCOMPATIBLE || dependency.getType() == RemoteMod.DependencyType.BROKEN) {
                    continue;
                }

                if (!dependencies.containsKey(dependency.getType())) {
                    List<RemoteMod> list = new ArrayList<>();
                    dependencies.put(dependency.getType(), list);
                }
                Objects.requireNonNull(dependencies.get(dependency.getType())).add(dependency.load());
            }

            return dependencies;
        }).whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
            setLoading(false, result.keySet().size() > 0);
            if (exception == null) {
                if (result.keySet().size() > 0) {
                    loadDependencyList(result);
                }
            } else {
                setFailed();
                Toast.makeText(getContext(), getContext().getString(R.string.download_failed_refresh), Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void loadDependencyList(EnumMap<RemoteMod.DependencyType, List<RemoteMod>> dependencies) {
        dependencyContainer.removeAllViews();
        for (RemoteMod.DependencyType type : dependencies.keySet()) {
            View split = new View(getContext());
            split.setBackgroundColor(getContext().getColor(android.R.color.darker_gray));
            if (type != dependencies.keySet().toArray()[0]) {
                View preSplit = new View(getContext());
                preSplit.setBackgroundColor(getContext().getColor(android.R.color.darker_gray));
                dependencyContainer.addView(preSplit, ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(getContext(), 1));
            }
            String text = AndroidUtils.getLocalizedText(getContext(), STRING_ID_KEY.get(type));
            FCLTextView textView = new FCLTextView(getContext());
            int padding = ConvertUtils.dip2px(getContext(), 10);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(text);
            textView.setAutoTint(true);
            textView.setSingleLine(true);
            textView.setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            dependencyContainer.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dependencyContainer.addView(split, ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(getContext(), 1));
            ListView listView = new ListView(getContext());
            listView.setDivider(new ColorDrawable(getContext().getColor(android.R.color.darker_gray)));
            listView.setDividerHeight(ConvertUtils.dip2px(getContext(), 1));
            DependencyAdapter adapter = new DependencyAdapter(getContext(), downloadPage, dependencies.get(type), mod -> {
                RemoteModInfoPage page = new RemoteModInfoPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_download_addon_info, downloadPage, mod, version, callback);
                DownloadPageManager.getInstance().showTempPage(page);
            });
            listView.setAdapter(adapter);
            dependencyContainer.addView(listView, ViewGroup.LayoutParams.MATCH_PARENT, getListViewHeight(listView));
        }
    }

    private int getListViewHeight(ListView listView) {
        int count = listView.getAdapter().getCount();
        View view = listView.getAdapter().getView(0, null, listView);
        view.measure(0, 0);
        return (view.getMeasuredHeight() * count) + (listView.getDividerHeight() * (count - 1));
    }

    private void create() {
        name = findViewById(R.id.name);
        tag = findViewById(R.id.tag);
        date = findViewById(R.id.date);
        dependencyLayout = findViewById(R.id.dependency_layout);
        dependencyContainer = findViewById(R.id.dependency_container);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        download = findViewById(R.id.download);
        saveAs = findViewById(R.id.save_as);
        cancel = findViewById(R.id.cancel);
        retry.setOnClickListener(this);
        download.setOnClickListener(this);
        saveAs.setOnClickListener(this);
        cancel.setOnClickListener(this);

        ThemeEngine.getInstance().registerEvent(dependencyLayout, () -> dependencyLayout.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));
    }

    public void setLoading(boolean loading, boolean hasDependency) {
        Schedulers.androidUIThread().execute(() -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            dependencyLayout.setVisibility(loading || !hasDependency ? View.GONE : View.VISIBLE);
            if (loading) {
                retry.setVisibility(View.GONE);
            }
        });
    }

    public void setFailed() {
        Schedulers.androidUIThread().execute(() -> {
            retry.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            dependencyLayout.setVisibility(View.GONE);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        name.setText(modVersion.getName());
        tag.setText(ModVersionAdapter.getTag(getContext(), modVersion));
        date.setText(ModVersionAdapter.FORMATTER.format(modVersion.getDatePublished().toInstant()));

        loadDependencies(modVersion);
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onClick(View view) {
        if (view == retry) {
            loadDependencies(modVersion);
        }
        if (view == download) {
            lastPage.download(modVersion);
        }
        if (view == saveAs) {
            lastPage.saveAs(modVersion);
        }
        if (view == cancel) {
            UIManager.getInstance().onBackPressed();
        }
    }
}
