package com.tungsten.fcl.ui.controller;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.download.ControllerCategory;
import com.tungsten.fcl.control.download.ControllerIndex;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControllerRepoPage extends FCLCommonPage implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String CONTROLLER_GITHUB = "https://raw.githubusercontent.com/FCL-Team/FCL-Controllers/main/";
    public static final String CONTROLLER_GIT_CN = "";

    private final ObjectProperty<ControllerCategory> categoryProperty = new SimpleObjectProperty<>(new ControllerCategory(0, null));
    private boolean refreshCategory = true;

    private ScrollView searchLayout;

    private FCLEditText nameEditText;
    private AppCompatSpinner sourceSpinner;
    private AppCompatSpinner langSpinner;
    private FCLSpinner<ControllerCategory> categorySpinner;
    private AppCompatSpinner deviceSpinner;

    private FCLButton search;

    private ListView listView;
    private FCLProgressBar progressBar;
    private FCLImageButton retry;

    public ControllerRepoPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    public void setLoading(boolean loading) {
        Schedulers.androidUIThread().execute(() -> {
            search.setEnabled(!loading);
            nameEditText.setEnabled(!loading);
            sourceSpinner.setEnabled(!loading);
            langSpinner.setEnabled(!loading);
            categorySpinner.setEnabled(!loading);
            deviceSpinner.setEnabled(!loading);
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            listView.setVisibility(!loading ? View.VISIBLE : View.GONE);
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

    private void search() {
        search(nameEditText.getText().toString(), langSpinner.getSelectedItemPosition(), sourceSpinner.getSelectedItemPosition(), categoryProperty == null ? 0 : categoryProperty.get().getId(), deviceSpinner.getSelectedItemPosition());
    }

    private void search(String name, int lang, int source, int category, int device) {
        setLoading(true);
        String head = source == 0 ? CONTROLLER_GITHUB : CONTROLLER_GIT_CN;
        String indexUrl = head + "index.json";
        String categoryUrl = head + "category.json";
        Task.supplyAsync(() -> {
            String indexStr = NetworkUtils.doGet(NetworkUtils.toURL(indexUrl));
            String categoryStr = NetworkUtils.doGet(NetworkUtils.toURL(categoryUrl));
            ArrayList<ControllerIndex> indexes = new ArrayList<>();
            ArrayList<ControllerIndex> allIndexes = JsonUtils.GSON.fromJson(indexStr, new TypeToken<ArrayList<ControllerIndex>>(){}.getType());
            ArrayList<ControllerCategory> categories = JsonUtils.GSON.fromJson(categoryStr, new TypeToken<ArrayList<ControllerCategory>>(){}.getType());
            categories.add(0, new ControllerCategory(0, null));
            allIndexes.forEach(i -> {
                if ((i.getLang().equals("all") || lang == 0 || LocaleUtils.getLocale(LocaleUtils.getLanguage(getContext())).toString().contains(i.getLang())) &&
                        i.getDevice().contains(device) &&
                        (category == 0 || i.getCategories().contains(category))) {
                    indexes.add(i);
                }
            });
            return new Object[] { searchControl(name, indexes), categories };
        }).thenAcceptAsync(Schedulers.androidUIThread(), (ExceptionalConsumer<Object[], Exception>) s -> {
            ArrayList<ControllerIndex> indexes = (ArrayList<ControllerIndex>) s[0];
            ArrayList<ControllerCategory> categories = (ArrayList<ControllerCategory>) s[1];
            refreshCategories(categories);
            ControllerListAdapter adapter = new ControllerListAdapter(getContext(), source, categories, indexes, mod -> {
                ControllerDownloadPage page = new ControllerDownloadPage(getContext(), PageManager.PAGE_ID_TEMP, getParent(), R.layout.page_controller_download, source, ControllerCategory.getLocaledCategories(getContext(), categories, mod.getCategories()), mod);
                ControllerPageManager.getInstance().showTempPage(page);
            });
            listView.setAdapter(adapter);
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            setLoading(false);
            if (exception != null) {
                setFailed();
                refreshCategory = true;
            }
        }).start();
    }

    private ArrayList<ControllerIndex> searchControl(String queryString, ArrayList<ControllerIndex> list) {
        ArrayList<ControllerIndex> result = new ArrayList<>();
        if (StringUtils.isBlank(queryString)) {
            result.addAll(list);
        } else {
            Predicate<String> predicate;
            if (queryString.startsWith("regex:")) {
                try {
                    Pattern pattern = Pattern.compile(queryString.substring("regex:".length()));
                    predicate = s -> pattern.matcher(s).find();
                } catch (Throwable e) {
                    LOG.log(Level.WARNING, "Illegal regular expression", e);
                    return result;
                }
            } else {
                String lowerQueryString = queryString.toLowerCase(Locale.ROOT);
                predicate = s -> s.toLowerCase(Locale.ROOT).contains(lowerQueryString);
            }
            for (ControllerIndex index : list) {
                if (predicate.test(index.getName())) {
                    result.add(index);
                }
            }
        }
        return result;
    }

    private void refreshCategories(ArrayList<ControllerCategory> categoryDataList) {
        if (refreshCategory) {
            FXUtils.unbindSelection(categorySpinner, categoryProperty);
            categoryProperty.set(new ControllerCategory(0, null));
            categorySpinner.setDataList(categoryDataList);
            ArrayList<String> categoryStringList = categoryDataList.stream().map(c -> c.getText(getContext())).collect(Collectors.toCollection(ArrayList::new));
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, categoryStringList);
            categoryAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
            categorySpinner.setAdapter(categoryAdapter);
            categorySpinner.setSelection(0);
            FXUtils.bindSelection(categorySpinner, categoryProperty);
            refreshCategory = false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        searchLayout = findViewById(R.id.search_layout);
        ThemeEngine.getInstance().registerEvent(searchLayout, () -> searchLayout.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));

        search = findViewById(R.id.search);
        search.setOnClickListener(this);

        nameEditText = findViewById(R.id.name);
        sourceSpinner = findViewById(R.id.download_source);
        langSpinner = findViewById(R.id.lang);
        categorySpinner = findViewById(R.id.category);
        deviceSpinner = findViewById(R.id.device);

        ArrayList<String> sources = new ArrayList<>();
        sources.add(getContext().getString(R.string.control_download_source_github));
        sources.add(getContext().getString(R.string.control_download_source_cn));
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, sources);
        sourceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        sourceSpinner.setAdapter(sourceAdapter);
        sourceSpinner.setSelection(LocaleUtils.isChinese(getContext()) ? 1 : 0);
        sourceSpinner.setOnItemSelectedListener(this);

        ArrayList<String> lang = new ArrayList<>();
        lang.add(getContext().getString(R.string.curse_category_0));
        lang.add(getContext().getString(R.string.control_download_lang_current));
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, lang);
        langAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        langSpinner.setAdapter(langAdapter);
        langSpinner.setSelection(0);

        ArrayList<String> devices = new ArrayList<>();
        devices.add(getContext().getString(R.string.control_download_device_phone));
        devices.add(getContext().getString(R.string.control_download_device_pad));
        devices.add(getContext().getString(R.string.control_download_device_other));
        ArrayAdapter<String> deviceAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, devices);
        deviceAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        deviceSpinner.setAdapter(deviceAdapter);
        deviceSpinner.setSelection(0);

        listView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        retry = findViewById(R.id.retry);
        retry.setOnClickListener(this);

        search();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == search || view == retry) {
            search();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        refreshCategory = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
