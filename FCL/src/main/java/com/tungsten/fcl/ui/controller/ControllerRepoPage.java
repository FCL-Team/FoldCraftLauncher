package com.tungsten.fcl.ui.controller;

import static com.tungsten.fclcore.util.Logging.LOG;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.download.ControllerCategory;
import com.tungsten.fcl.control.download.ControllerIndex;
import com.tungsten.fcl.control.download.ControllerVersion;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.TaskDialog;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.TaskCancellationAction;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.FileDownloadTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.task.TaskExecutor;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.function.ExceptionalConsumer;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.gson.fakefx.factories.JavaFxPropertyTypeAdapterFactory;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLEditText;
import com.tungsten.fcllibrary.component.view.FCLImageButton;
import com.tungsten.fcllibrary.component.view.FCLProgressBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ControllerRepoPage extends FCLCommonPage implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String CONTROLLER_GITHUB = "https://raw.githubusercontent.com/FCL-Team/FCL-Controllers/main/";
    public static final String CONTROLLER_GIT_CN = "https://gitee.com/fcl-team/FCL-Controllers/raw/main/";

    private final ObjectProperty<ControllerCategory> categoryProperty = new SimpleObjectProperty<>(new ControllerCategory(0, null));
    private boolean refreshCategory = true;

    private ScrollView searchLayout;

    private FCLEditText nameEditText;
    private AppCompatSpinner sourceSpinner;
    private AppCompatSpinner langSpinner;
    private FCLSpinner<ControllerCategory> categorySpinner;
    private AppCompatSpinner deviceSpinner;

    private FCLButton check;
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

    private void checkUpdate(int source, boolean toast) {
        check.setEnabled(false);
        String head = source == 0 ? CONTROLLER_GITHUB : CONTROLLER_GIT_CN;
        String indexUrl = head + "index.json";
        if (toast)
            Toast.makeText(getContext(), getContext().getString(R.string.update_checking), Toast.LENGTH_SHORT).show();
        Task.supplyAsync(() -> {
            ArrayList<String[]> data = new ArrayList<>();
            String indexStr = NetworkUtils.doGet(NetworkUtils.toURL(indexUrl));
            ArrayList<ControllerIndex> indexes = JsonUtils.GSON.fromJson(indexStr, new TypeToken<ArrayList<ControllerIndex>>(){}.getType());
            for (Controller controller : Controllers.getControllers()) {
                ControllerIndex index = indexes.stream().filter(i -> i.getId().equals(controller.getId())).findFirst().orElse(null);
                if (index != null) {
                    String versionStr = NetworkUtils.doGet(NetworkUtils.toURL(head + "repo_json/" + index.getId() + "/version.json"));
                    ControllerVersion version = JsonUtils.GSON.fromJson(versionStr, ControllerVersion.class);
                    if (version.getLatest().getVersionCode() > controller.getVersionCode()) {
                        String[] d = new String[] {
                                controller.getId(),
                                controller.getName(),
                                controller.getVersion(),
                                version.getLatest().getVersionName(),
                                head + "repo_json/" + index.getId() + "/versions/" + version.getLatest().getVersionCode() + ".json"
                        };
                        data.add(d);
                    }
                }
            }
            return data;
        }).thenAcceptAsync(Schedulers.androidUIThread(), (ExceptionalConsumer<ArrayList<String[]>, Exception>) s -> {
            if (s.isEmpty()) {
                if (toast)
                    Toast.makeText(getContext(), getContext().getString(R.string.update_not_exist), Toast.LENGTH_SHORT).show();
            } else {
                for (String[] data : s) {
                    FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                    builder.setTitle(getContext().getString(R.string.update_exist));
                    builder.setMessage(data[1] + ": " + data[2] + " ===> " + data[3]);
                    builder.setPositiveButton(getContext().getString(R.string.update), () -> downloadFile(data[0], data[4]));
                    builder.setNegativeButton(null);
                    builder.create().show();
                }
            }
        }).whenComplete(Schedulers.androidUIThread(), exception -> {
            check.setEnabled(true);
            if (exception != null && toast) {
                Toast.makeText(getContext(), getContext().getString(R.string.update_check_failed), Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    private void downloadFile(String id, String url) {
        FileUtils.deleteDirectoryQuietly(new File(FCLPath.CACHE_DIR + "/control"));
        String destPath = FCLPath.CONTROLLER_DIR + "/" + id + ".json";
        String cache = FCLPath.CACHE_DIR + "/control/" + id + ".json";
        boolean exist = new File(destPath).exists();
        Controller old = exist ? Controllers.findControllerById(id) : null;
        TaskDialog taskDialog = new TaskDialog(getContext(), new TaskCancellationAction(AppCompatDialog::dismiss));
        taskDialog.setTitle(getContext().getString(R.string.message_downloading));
        TaskExecutor executor = Task.composeAsync(() -> {
            if (exist && old != null) {
                FileUtils.copyFile(new File(destPath), new File(cache));
                ((ControllerManagePage) ControllerPageManager.getInstance().getPageById(ControllerPageManager.PAGE_ID_CONTROLLER_MANAGER)).removeController(old);
            }
            FileDownloadTask task = new FileDownloadTask(NetworkUtils.toURL(url), new File(destPath));
            task.setName(id);
            return task;
        }).whenComplete(Schedulers.defaultScheduler(), exception -> {
            if (exception != null) {
                if (new File(cache).exists()) {
                    FileUtils.copyFile(new File(cache), new File(destPath));
                    ((ControllerManagePage) ControllerPageManager.getInstance().getPageById(ControllerPageManager.PAGE_ID_CONTROLLER_MANAGER)).addController(old);
                }
                Schedulers.androidUIThread().execute(() -> {
                    if (exception instanceof CancellationException) {
                        Toast.makeText(getContext(), getContext().getString(R.string.message_cancelled), Toast.LENGTH_SHORT).show();
                    } else {
                        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                        builder.setCancelable(false);
                        builder.setTitle(getContext().getString(R.string.install_failed_downloading));
                        builder.setMessage(DownloadProviders.localizeErrorMessage(getContext(), exception));
                        builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                        builder.create().show();
                    }
                });
            } else {
                FileUtils.deleteDirectoryQuietly(new File(FCLPath.CACHE_DIR + "/control"));
                Controller controller = new GsonBuilder()
                        .registerTypeAdapterFactory(new JavaFxPropertyTypeAdapterFactory(true, true))
                        .setPrettyPrinting()
                        .create().fromJson(FileUtils.readText(new File(destPath)), Controller.class);
                ((ControllerManagePage) ControllerPageManager.getInstance().getPageById(ControllerPageManager.PAGE_ID_CONTROLLER_MANAGER)).addController(controller);
                Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.install_success), Toast.LENGTH_SHORT).show());
            }
        }).executor();
        taskDialog.setExecutor(executor);
        taskDialog.show();
        executor.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        searchLayout = findViewById(R.id.search_layout);
        ThemeEngine.getInstance().registerEvent(searchLayout, () -> searchLayout.setBackgroundTintList(new ColorStateList(new int[][] { { } }, new int[] { ThemeEngine.getInstance().getTheme().getLtColor() })));

        check = findViewById(R.id.check);
        search = findViewById(R.id.search);
        check.setOnClickListener(this);
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
        Controllers.addCallback(() -> checkUpdate(LocaleUtils.isChinese(getContext()) ? 1 : 0, false));
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
        if (view == check) {
            checkUpdate(sourceSpinner == null ? (LocaleUtils.isChinese(getContext()) ? 1 : 0) : sourceSpinner.getSelectedItemPosition(), true);
        }
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
