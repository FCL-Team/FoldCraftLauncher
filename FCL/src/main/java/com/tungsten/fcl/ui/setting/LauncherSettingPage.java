package com.tungsten.fcl.ui.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Lang.thread;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.upgrade.UpdateChecker;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.fakefx.beans.binding.Bindings;
import com.tungsten.fclcore.task.FetchTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.browser.FileBrowser;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SelectionMode;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.component.view.FCLCheckBox;
import com.tungsten.fcllibrary.component.view.FCLSeekBar;
import com.tungsten.fcllibrary.component.view.FCLSpinner;
import com.tungsten.fcllibrary.component.view.FCLSwitch;
import com.tungsten.fcllibrary.component.view.FCLTextView;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

public class LauncherSettingPage extends FCLCommonPage implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    private FCLSpinner<String> language;
    private FCLButton checkUpdate;
    private FCLButton clearCache;
    private FCLButton exportLog;
    private FCLButton theme;
    private FCLButton ltBackground;
    private FCLButton dkBackground;
    private FCLButton resetTheme;
    private FCLButton resetLtBackground;
    private FCLButton resetDkBackground;
    private FCLSwitch ignoreNotch;
    private FCLCheckBox autoSource;
    private FCLSpinner<String> versionList;
    private FCLSpinner<String> downloadType;
    private FCLCheckBox autoThreads;
    private FCLSeekBar threads;
    private FCLTextView threadsText;

    public LauncherSettingPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        language = findViewById(R.id.language);
        checkUpdate = findViewById(R.id.check_update);
        clearCache = findViewById(R.id.clear_cache);
        exportLog = findViewById(R.id.export_log);
        theme = findViewById(R.id.theme);
        ltBackground = findViewById(R.id.background_lt);
        dkBackground = findViewById(R.id.background_dk);
        resetTheme = findViewById(R.id.reset_theme);
        resetLtBackground = findViewById(R.id.reset_background_lt);
        resetDkBackground = findViewById(R.id.reset_background_dk);
        ignoreNotch = findViewById(R.id.ignore_notch);
        autoSource = findViewById(R.id.check_auto_source);
        versionList = findViewById(R.id.source_auto);
        downloadType = findViewById(R.id.source);
        autoThreads = findViewById(R.id.check_auto_threads);
        threads = findViewById(R.id.threads);
        threadsText = findViewById(R.id.threads_text);

        checkUpdate.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        exportLog.setOnClickListener(this);
        theme.setOnClickListener(this);
        ltBackground.setOnClickListener(this);
        dkBackground.setOnClickListener(this);
        resetTheme.setOnClickListener(this);
        resetLtBackground.setOnClickListener(this);
        resetDkBackground.setOnClickListener(this);

        ArrayList<String> languageList = new ArrayList<>();
        languageList.add(getContext().getString(R.string.settings_launcher_language_system));
        languageList.add(getContext().getString(R.string.settings_launcher_language_english));
        languageList.add(getContext().getString(R.string.settings_launcher_language_simplified_chinese));
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, languageList);
        languageAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        language.setAdapter(languageAdapter);
        language.setSelection(LocaleUtils.getLanguage(getContext()));
        language.setOnItemSelectedListener(this);

        ignoreNotch.setChecked(ThemeEngine.getInstance().getTheme().isFullscreen());
        ignoreNotch.setOnCheckedChangeListener(this);

        autoSource.setChecked(config().autoChooseDownloadTypeProperty().get());
        autoSource.addCheckedChangeListener();
        autoSource.checkProperty().bindBidirectional(config().autoChooseDownloadTypeProperty());
        versionList.visibilityProperty().bind(autoSource.checkProperty());
        downloadType.visibilityProperty().bind(autoSource.checkProperty().not());
        versionList.setDataList(new ArrayList<>(DownloadProviders.providersById.keySet()));
        ArrayList<String> versionListSourceList = new ArrayList<>();
        versionListSourceList.add(getContext().getString(R.string.download_provider_official));
        versionListSourceList.add(getContext().getString(R.string.download_provider_balanced));
        versionListSourceList.add(getContext().getString(R.string.download_provider_mirror));
        ArrayAdapter<String> versionListAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, versionListSourceList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        versionList.setAdapter(versionListAdapter);
        versionList.setSelection(getSourcePosition(config().versionListSourceProperty().get()));
        FXUtils.bindSelection(versionList, config().versionListSourceProperty());
        downloadType.setDataList(new ArrayList<>(DownloadProviders.rawProviders.keySet()));
        ArrayList<String> downloadTypeList = new ArrayList<>();
        downloadTypeList.add(getContext().getString(R.string.download_provider_mojang));
        downloadTypeList.add(getContext().getString(R.string.download_provider_bmclapi));
        downloadTypeList.add(getContext().getString(R.string.download_provider_mcbbs));
        ArrayAdapter<String> downloadTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, downloadTypeList);
        downloadTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        downloadType.setAdapter(downloadTypeAdapter);
        downloadType.setSelection(getSourcePosition(config().downloadTypeProperty().get()));
        FXUtils.bindSelection(downloadType, config().downloadTypeProperty());
        autoThreads.setChecked(config().getAutoDownloadThreads());
        autoThreads.addCheckedChangeListener();
        autoThreads.checkProperty().bindBidirectional(config().autoDownloadThreadsProperty());
        autoThreads.checkProperty().addListener(observable -> {
            if (autoThreads.isChecked()) {
                config().downloadThreadsProperty().set(FetchTask.DEFAULT_CONCURRENCY);
            }
        });
        threads.setProgress(config().getDownloadThreads());
        threads.addProgressListener();
        threads.progressProperty().bindBidirectional(config().downloadThreadsProperty());
        threadsText.stringProperty().bind(Bindings.createStringBinding(() -> threads.getProgress() + "", threads.progressProperty()));

        if (System.currentTimeMillis() - getLastClearCacheTime() >= 3 * ONE_DAY) {
            FileUtils.cleanDirectoryQuietly(new File(FCLPath.CACHE_DIR).getParentFile());
            setLastClearCacheTime(System.currentTimeMillis());
        }
    }

    public long getLastClearCacheTime() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("clear_cache", 0L);
    }

    public void setLastClearCacheTime(long time) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("launcher", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("clear_cache", time);
        editor.apply();
    }

    private int getSourcePosition(String source) {
        switch (source) {
            case "official":
            case "mojang":
                return 0;
            case "mirror":
            case "mcbbs":
                return 2;
            default:
                return 1;
        }
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == checkUpdate && !UpdateChecker.getInstance().isChecking()) {
            UpdateChecker.getInstance().checkManually(getContext()).whenComplete(Schedulers.androidUIThread(), e -> {
                if (e != null) {
                    FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                    builder.setMessage(getContext().getString(R.string.update_check_failed) + "\n" + e);
                    builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                    builder.create().show();
                }
            }).start();
        }
        if (v == clearCache) {
            FileUtils.cleanDirectoryQuietly(new File(FCLPath.CACHE_DIR).getParentFile());
        }
        if (v == exportLog) {
            thread(() -> {
                Path logFile = new File(new File(FCLPath.SHARED_COMMON_DIR).getParent(), "fcl-exported-logs-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss")) + ".log").toPath().toAbsolutePath();
                LOG.info("Exporting logs to " + logFile);
                try {
                    Files.write(logFile, Logging.getRawLogs());
                } catch (IOException e) {
                    Schedulers.androidUIThread().execute(() -> {
                        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                        builder.setMessage(getContext().getString(R.string.settings_launcher_launcher_log_export_failed) + "\n" + e);
                        builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                        builder.create().show();
                    });
                    LOG.log(Level.WARNING, "Failed to export logs", e);
                    return;
                }
                Schedulers.androidUIThread().execute(() -> {
                    FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setAlertLevel(FCLAlertDialog.AlertLevel.INFO);
                    builder.setMessage(AndroidUtils.getLocalizedText(getContext(), "settings_launcher_launcher_log_export_success", logFile));
                    builder.setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), null);
                    builder.create().show();
                });
            });
        }
        if (v == theme) {
            FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), ThemeEngine.getInstance().getTheme().getColor(), new FCLColorPickerDialog.Listener() {
                @Override
                public void onColorChanged(int color) {
                    ThemeEngine.getInstance().applyColor(color);
                }

                @Override
                public void onPositive(int destColor) {
                    ThemeEngine.getInstance().applyAndSave(getContext(), destColor);
                }

                @Override
                public void onNegative(int initColor) {
                    ThemeEngine.getInstance().applyColor(initColor);
                }
            });
            dialog.show();
        }
        if (v == ltBackground) {
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FILE_CHOOSER);
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            builder.setSuffix(suffix);
            builder.create().browse(getActivity(), RequestCodes.SELECT_LAUNCHER_BACKGROUND_CODE, ((requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_LAUNCHER_BACKGROUND_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    String path = FileBrowser.getSelectedFiles(data).get(0);
                    ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).background, path, null);
                }
            }));
        }
        if (v == dkBackground) {
            FileBrowser.Builder builder = new FileBrowser.Builder(getContext());
            builder.setLibMode(LibMode.FILE_CHOOSER);
            builder.setSelectionMode(SelectionMode.SINGLE_SELECTION);
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            builder.setSuffix(suffix);
            builder.create().browse(getActivity(), RequestCodes.SELECT_LAUNCHER_BACKGROUND_CODE, ((requestCode, resultCode, data) -> {
                if (requestCode == RequestCodes.SELECT_LAUNCHER_BACKGROUND_CODE && resultCode == Activity.RESULT_OK && data != null) {
                    String path = FileBrowser.getSelectedFiles(data).get(0);
                    ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).background, null, path);
                }
            }));
        }
        if (v == resetTheme) {
            ThemeEngine.getInstance().applyAndSave(getContext(), getContext().getColor(R.color.default_theme_color));
        }
        if (v == resetLtBackground) {
            new Thread(() -> {
                if (!new File(FCLPath.LT_BACKGROUND_PATH).delete() && new File(FCLPath.LT_BACKGROUND_PATH).exists())
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show());

                Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).background, null, null));
            }).start();
        }
        if (v == resetDkBackground) {
            new Thread(() -> {
                if (!new File(FCLPath.DK_BACKGROUND_PATH).delete() && new File(FCLPath.DK_BACKGROUND_PATH).exists())
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show());

                Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).background, null, null));
            }).start();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == language) {
            LocaleUtils.changeLanguage(getContext(), position);
            LocaleUtils.setLanguage(getContext());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == ignoreNotch) {
            ThemeEngine.getInstance().applyAndSave(getContext(), getActivity().getWindow(), isChecked);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }
    }
}
