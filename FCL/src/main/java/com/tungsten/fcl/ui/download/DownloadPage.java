package com.tungsten.fcl.ui.download;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.tungsten.fcl.R;
import com.tungsten.fcl.setting.Profile;
import com.tungsten.fcl.ui.manage.ManageUI;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ListProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleListProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleStringProperty;
import com.tungsten.fclcore.fakefx.beans.property.StringProperty;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.mod.RemoteMod;
import com.tungsten.fclcore.mod.RemoteModRepository;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DownloadPage extends FCLCommonPage implements ManageUI.VersionLoadable, View.OnClickListener {

    protected RemoteModRepository repository;
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
    private FCLSpinner<String> sourceSpinner;
    private FCLSpinner<String> gameVersionSpinner;
    private FCLSpinner<CategoryIndented> categorySpinner;
    private FCLSpinner<RemoteModRepository.SortType> sortSpinner;

    private FCLButton search;

    private ListView listView;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;

    public void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            search.setEnabled(!loading);
            nameEditText.setEnabled(!loading);
            sourceSpinner.setEnabled(!loading);
            gameVersionSpinner.setEnabled(!loading);
            categorySpinner.setEnabled(!loading);
            sortSpinner.setEnabled(!loading);
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
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
            listView.setVisibility(View.GONE);
        });
    }

    public void search() {
        search(gameVersion.get(),
                category.get().getCategory(),
                0,
                Objects.requireNonNull(nameEditText.getText()).toString(),
                sortType.get());
    }

    public void search(String userGameVersion, RemoteModRepository.Category category, int pageOffset, String searchFilter, RemoteModRepository.SortType sort) {
        retrySearch = null;
        setLoading(true);
        if (executor != null && !executor.isCancelled()) {
            executor.cancel();
        }
        executor = Task.supplyAsync(() -> repository.search(userGameVersion, category, pageOffset, 50, searchFilter, sort, RemoteModRepository.SortOrder.DESC))
                .whenComplete(Schedulers.androidUIThread(), (result, exception) -> {
                    setLoading(false);
                    if (exception == null) {
                        ArrayList<RemoteMod> list = result.collect(Collectors.toCollection(ArrayList::new));
                        adapter = new RemoteModListAdapter(getContext(), this, list, mod -> {

                        });
                        listView.setAdapter(adapter);
                    } else {
                        setFailed();
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

    public DownloadPage(Context context, int id, FCLUILayout parent, int resId, RemoteModRepository repository) {
        super(context, id, parent, resId);
        this.repository = repository;

        if (repository != null) {
            create();
        }
    }

    public void create() {
        searchLayout = findViewById(R.id.search_layout);
        ThemeEngine.getInstance().registerEvent(searchLayout, () -> searchLayout.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));

        search = findViewById(R.id.search);
        search.setOnClickListener(this);

        nameEditText = findViewById(R.id.name);
        sourceSpinner = findViewById(R.id.download_source);
        gameVersionSpinner = findViewById(R.id.game_version);
        categorySpinner = findViewById(R.id.category);
        sortSpinner = findViewById(R.id.sort);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);

        nameEditText.setHint(supportChinese.get() ? getContext().getString(R.string.search_hint_chinese) : getContext().getString(R.string.search_hint_english));

        sourceSpinner.setVisibility(downloadSources.getSize() > 1 ? View.VISIBLE : View.GONE);
        if (downloadSources.getSize() > 1) {
            sourceSpinner.setDataList(new ArrayList<>(downloadSources));
            ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new ArrayList<>(downloadSources));
            sourceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            sourceSpinner.setAdapter(sourceAdapter);
            sourceSpinner.setSelection(downloadSource.get().equals(getContext().getString(R.string.mods_modrinth)) ? 1 : 0);
            FXUtils.bindSelection(sourceSpinner, downloadSource);
        }

        gameVersionSpinner.setDataList(new ArrayList<>(Arrays.stream(RemoteModRepository.DEFAULT_GAME_VERSIONS).collect(Collectors.toList())));
        ArrayAdapter<String> gameVersionAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, new ArrayList<>(Arrays.stream(RemoteModRepository.DEFAULT_GAME_VERSIONS).collect(Collectors.toList())));
        gameVersionAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        gameVersionSpinner.setAdapter(gameVersionAdapter);
        gameVersionSpinner.setSelection(0);
        FXUtils.bindSelection(gameVersionSpinner, gameVersion);

        ArrayList<CategoryIndented> categoryDataList = new ArrayList<>();
        categoryDataList.add(new CategoryIndented(0, null));
        categorySpinner.setDataList(categoryDataList);
        ArrayList<String> categoryStringList = categoryDataList.stream().map(this::getLocalizedCategoryIndent).collect(Collectors.toCollection(ArrayList::new));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, categoryStringList);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setSelection(0);
        FXUtils.bindSelection(categorySpinner, category);
        downloadSource.addListener(observable -> Task.supplyAsync(() -> {
            setLoading(true);
            return repository.getCategories();
        }).thenAcceptAsync(Schedulers.androidUIThread(), categories -> {
            ArrayList<CategoryIndented> result = new ArrayList<>();
            result.add(new CategoryIndented(0, null));
            for (RemoteModRepository.Category category : Lang.toIterable(categories)) {
                resolveCategory(category, 0, result);
            }
            categorySpinner.setDataList(result);
            ArrayList<String> resultStr = result.stream().map(this::getLocalizedCategoryIndent).collect(Collectors.toCollection(ArrayList::new));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, resultStr);
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            categorySpinner.setAdapter(adapter);
            FXUtils.unbindSelection(categorySpinner, category);
            categorySpinner.setSelection(0);
            category.set(result.get(0));
            FXUtils.bindSelection(categorySpinner, category);
            search();
        }).start());

        sortSpinner.setDataList(new ArrayList<>(Arrays.stream(RemoteModRepository.SortType.values()).collect(Collectors.toList())));
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add(getContext().getString(R.string.curse_sort_popularity));
        sorts.add(getContext().getString(R.string.curse_sort_name));
        sorts.add(getContext().getString(R.string.curse_sort_date_created));
        sorts.add(getContext().getString(R.string.curse_sort_last_updated));
        sorts.add(getContext().getString(R.string.curse_sort_author));
        sorts.add(getContext().getString(R.string.curse_sort_total_downloads));
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, sorts);
        sortAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setSelection(0);
        FXUtils.bindSelection(sortSpinner, sortType);

        search("", null, 0, "", RemoteModRepository.SortType.POPULARITY);
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
            search();
        }
        if (v == retry && retrySearch != null) {
            retrySearch.run();
        }
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
}
