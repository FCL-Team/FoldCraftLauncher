package com.tungsten.fcl.ui.setting;

import static android.content.Context.MODE_PRIVATE;
import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Lang.thread;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;

import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.databinding.PageSettingLauncherBinding;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.upgrade.UpdateChecker;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fcl.util.RequestCodes;
import com.tungsten.fclauncher.utils.FCLPath;
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
import com.tungsten.fcllibrary.component.theme.Theme;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

public class LauncherSettingPage extends FCLCommonPage implements View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;
    private PageSettingLauncherBinding binding;
    private boolean isFirst = true;
    private SharedPreferences sharedPreferences;

    public LauncherSettingPage(Context context, int id, FCLUILayout parent, int resId) {
        super(context, id, parent, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binding = PageSettingLauncherBinding.bind(getContentView());
        sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);

        binding.checkUpdate.setOnClickListener(this);
        binding.clearCache.setOnClickListener(this);
        binding.exportLog.setOnClickListener(this);
        binding.requestAudioRecord.setOnClickListener(this);
        binding.theme.setOnClickListener(this);
        binding.theme2.setOnClickListener(this);
        binding.theme2Dark.setOnClickListener(this);
        binding.backgroundLt.setOnClickListener(this);
        binding.backgroundDk.setOnClickListener(this);
        binding.backgroundLive.setOnClickListener(this);
        binding.cursor.setOnClickListener(this);
        binding.menuIcon.setOnClickListener(this);
        binding.resetTheme.setOnClickListener(this);
        binding.resetTheme2.setOnClickListener(this);
        binding.fetchBackgroundColor.setOnClickListener(this);
        binding.fetchBackgroundColor2.setOnClickListener(this);
        binding.resetBackgroundLt.setOnClickListener(this);
        binding.resetBackgroundDk.setOnClickListener(this);
        binding.resetBackgroundLive.setOnClickListener(this);
        binding.resetCursor.setOnClickListener(this);
        binding.resetMenuIcon.setOnClickListener(this);

        binding.theme.setSelected(true);
        binding.theme2.setSelected(true);
        binding.theme2Dark.setSelected(true);
        binding.resetTheme.setSelected(true);
        binding.resetTheme2.setSelected(true);
        binding.fetchBackgroundColor.setSelected(true);
        binding.fetchBackgroundColor2.setSelected(true);
        binding.fetchBackgroundColor2Dark.setSelected(true);

        ArrayList<String> languageList = new ArrayList<>();
        languageList.add(getContext().getString(R.string.settings_launcher_language_system));
        languageList.add(getContext().getString(R.string.settings_launcher_language_english));
        languageList.add(getContext().getString(R.string.settings_launcher_language_simplified_chinese));
        languageList.add(getContext().getString(R.string.settings_launcher_language_russian));
        languageList.add(getContext().getString(R.string.settings_launcher_language_brazilian_portuguese));
        languageList.add(getContext().getString(R.string.settings_launcher_language_persian));
        languageList.add(getContext().getString(R.string.settings_launcher_language_ukrainian));
        languageList.add(getContext().getString(R.string.settings_launcher_language_german));
        languageList.add(getContext().getString(R.string.settings_launcher_language_traditional_chinese_hk));
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, languageList);
        languageAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.language.setAdapter(languageAdapter);
        binding.language.setSelection(LocaleUtils.getLanguage(getContext()));
        binding.language.setOnItemSelectedListener(this);

        ArrayList<String> themeModeList = new ArrayList<>();
        themeModeList.add(getContext().getString(R.string.settings_launcher_theme_mode_follow));
        themeModeList.add(getContext().getString(R.string.settings_launcher_theme_mode_light));
        themeModeList.add(getContext().getString(R.string.settings_launcher_theme_mode_dark));
        ArrayAdapter<String> themeModeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, themeModeList);
        themeModeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.themeMode.setAdapter(themeModeAdapter);
        binding.themeMode.setSelection(sharedPreferences.getInt("themeMode", 0));
        binding.themeMode.setOnItemSelectedListener(this);

        binding.autoExitLauncher.setChecked(sharedPreferences.getBoolean("autoExitLauncher", false));
        binding.autoExitLauncher.setOnCheckedChangeListener(this);

        binding.ignoreNotch.setChecked(ThemeEngine.getInstance().getTheme().isFullscreen());
        binding.ignoreNotch.setOnCheckedChangeListener(this);

        binding.closeSkinModel.setChecked(ThemeEngine.getInstance().getTheme().isCloseSkinModel());
        binding.closeSkinModel.setOnCheckedChangeListener(this);

        binding.videoBackgroundVolume.setProgress(sharedPreferences.getInt("videoBackgroundVolume", 100));
        binding.videoBackgroundVolume.setOnSeekBarChangeListener(this);

        binding.animationSpeed.setProgress(ThemeEngine.getInstance().getTheme().getAnimationSpeed());
        binding.animationSpeed.addProgressListener();
        binding.animationSpeed.progressProperty().bindBidirectional(ThemeEngine.getInstance().getTheme().animationSpeedProperty());
        ThemeEngine.getInstance().getTheme().animationSpeedProperty().addListener(observable -> Theme.saveTheme(getContext(), ThemeEngine.getInstance().getTheme()));

        binding.vibrationDuration.setProgress(sharedPreferences.getInt("vibrationDuration", 100));
        binding.vibrationDuration.addProgressListener();
        binding.vibrationDuration.progressProperty().addListener(observable -> sharedPreferences.edit().putInt("vibrationDuration", binding.vibrationDuration.getProgress()).apply());
        binding.disableFullscreenInput.setChecked(sharedPreferences.getBoolean("disableFullscreenInput", true));
        binding.disableFullscreenInput.setOnCheckedChangeListener(this);

        binding.editLauncherName.setText(sharedPreferences.getString("custom_launcher_name", getContext().getString(R.string.app_name)));
        binding.editLauncherName.addTextWatcher(s -> {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("custom_launcher_name", s);
            edit.apply();
        });

        binding.allowScreenshots.setChecked(sharedPreferences.getBoolean("allowScreenshots", false));
        binding.allowScreenshots.setOnCheckedChangeListener(this);

        binding.checkAutoSource.setChecked(config().autoChooseDownloadTypeProperty().get());
        binding.checkAutoSource.addCheckedChangeListener();
        binding.checkAutoSource.checkProperty().bindBidirectional(config().autoChooseDownloadTypeProperty());
        binding.sourceAuto.visibilityProperty().bind(binding.checkAutoSource.checkProperty());
        binding.source.visibilityProperty().bind(binding.checkAutoSource.checkProperty().not());
        binding.sourceAuto.setDataList(new ArrayList<>(DownloadProviders.providersById.keySet()));
        ArrayList<String> versionListSourceList = new ArrayList<>();
        versionListSourceList.add(getContext().getString(R.string.download_provider_official));
        versionListSourceList.add(getContext().getString(R.string.download_provider_balanced));
        versionListSourceList.add(getContext().getString(R.string.download_provider_mirror));
        ArrayAdapter<String> versionListAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, versionListSourceList);
        versionListAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.sourceAuto.setAdapter(versionListAdapter);
        binding.sourceAuto.setSelection(getSourcePosition(config().versionListSourceProperty().get()));
        FXUtils.bindSelection(binding.sourceAuto, config().versionListSourceProperty());
        binding.source.setDataList(new ArrayList<>(DownloadProviders.rawProviders.keySet()));
        ArrayList<String> downloadTypeList = new ArrayList<>();
        downloadTypeList.add(getContext().getString(R.string.download_provider_mojang));
        downloadTypeList.add(getContext().getString(R.string.download_provider_bmclapi));
        ArrayAdapter<String> downloadTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner_auto_tint, downloadTypeList);
        downloadTypeAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.source.setAdapter(downloadTypeAdapter);
        binding.source.setSelection(getSourcePosition(config().downloadTypeProperty().get()));
        FXUtils.bindSelection(binding.source, config().downloadTypeProperty());
        binding.checkAutoThreads.setChecked(config().getAutoDownloadThreads());
        binding.checkAutoThreads.addCheckedChangeListener();
        binding.checkAutoThreads.checkProperty().bindBidirectional(config().autoDownloadThreadsProperty());
        binding.checkAutoThreads.checkProperty().addListener(observable -> {
            if (binding.checkAutoThreads.isChecked()) {
                config().downloadThreadsProperty().set(FetchTask.DEFAULT_CONCURRENCY);
            }
        });
        binding.threads.setProgress(config().getDownloadThreads());
        binding.threads.addProgressListener();
        binding.threads.progressProperty().bindBidirectional(config().downloadThreadsProperty());

        if (System.currentTimeMillis() - getLastClearCacheTime() >= 3 * ONE_DAY) {
            FileUtils.cleanDirectoryQuietly(new File(FCLPath.CACHE_DIR).getParentFile());
            setLastClearCacheTime(System.currentTimeMillis());
        }
    }

    public long getLastClearCacheTime() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("launcher", MODE_PRIVATE);
        return sharedPreferences.getLong("clear_cache", 0L);
    }

    public void setLastClearCacheTime(long time) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("launcher", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("clear_cache", time);
        editor.apply();
    }

    private int getSourcePosition(String source) {
        return switch (source) {
            case "official", "mojang" -> 0;
            case "mirror" -> 2;
            default -> 1;
        };
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.checkUpdate && !UpdateChecker.getInstance().isChecking()) {
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
        if (v == binding.clearCache) {
            FileUtils.cleanDirectoryQuietly(new File(FCLPath.CACHE_DIR).getParentFile());
        }
        if (v == binding.exportLog) {
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
        if (v == binding.requestAudioRecord) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.getInstance(), Manifest.permission.RECORD_AUDIO)) {
                    MainActivity.getInstance().permissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
                } else {
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        if (v == binding.theme) {
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
        if (v == binding.theme2) {
            FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), ThemeEngine.getInstance().getTheme()._getColor2(), new FCLColorPickerDialog.Listener() {
                @Override
                public void onColorChanged(int color) {
                    ThemeEngine.getInstance().applyColor2(color);
                }

                @Override
                public void onPositive(int destColor) {
                    ThemeEngine.getInstance().applyAndSave2(getContext(), destColor);
                }

                @Override
                public void onNegative(int initColor) {
                    ThemeEngine.getInstance().applyColor2(initColor);
                }
            });
            dialog.show();
        }
        if (v == binding.theme2Dark) {
            FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), ThemeEngine.getInstance().getTheme().getColor2Dark(), new FCLColorPickerDialog.Listener() {
                @Override
                public void onColorChanged(int color) {
                    ThemeEngine.getInstance().applyColor2Dark(color);
                }

                @Override
                public void onPositive(int destColor) {
                    ThemeEngine.getInstance().applyAndSave2Dark(getContext(), destColor);
                }

                @Override
                public void onNegative(int initColor) {
                    ThemeEngine.getInstance().applyColor2Dark(initColor);
                }
            });
            dialog.show();
        }
        if (v == binding.backgroundLt || v == binding.backgroundDk) {
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            suffix.add(".jpg");
            suffix.add(".jpeg");
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
                String path = files.get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
                }
                ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).binding.background, v == binding.backgroundLt ? path : null, v == binding.backgroundDk ? path : null);
            });
        }
        if (v == binding.backgroundLive) {
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".mp4");
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
                String path = files.get(0);
                Uri uri = Uri.parse(path);
                if (AndroidUtils.isDocUri(uri)) {
                    AndroidUtils.copyFile(getActivity(), uri, new File(FCLPath.LIVE_BACKGROUND_PATH));
                } else {
                    try {
                        FileUtils.copyFile(new File(path), new File(FCLPath.LIVE_BACKGROUND_PATH));
                    } catch (IOException ignore) {
                    }
                }
                MainActivity.getInstance().setupLiveBackground();
            });
        }
        if (v == binding.cursor) {
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            suffix.add(".gif");
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
                String path = files.get(0);
                Uri uri = Uri.parse(path);
                String type = AndroidUtils.getFileName(getContext(), uri);
                if (type.endsWith(".gif")) {
                    type = "gif";
                } else {
                    type = "png";
                }
                deleteCursorFile();
                if (AndroidUtils.isDocUri(uri)) {
                    AndroidUtils.copyFile(getActivity(), uri, new File(FCLPath.FILES_DIR, "cursor." + type));
                } else {
                    try {
                        FileUtils.copyFile(new File(path), new File(FCLPath.FILES_DIR, "cursor." + type));
                    } catch (IOException ignore) {
                    }
                }
            });
        }
        if (v == binding.menuIcon) {
            ArrayList<String> suffix = new ArrayList<>();
            suffix.add(".png");
            suffix.add(".gif");
            MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
                String path = files.get(0);
                Uri uri = Uri.parse(path);
                String type = AndroidUtils.getFileName(getContext(), uri);
                if (type.endsWith(".gif")) {
                    type = "gif";
                } else {
                    type = "png";
                }
                deleteMenuIconFile();
                if (AndroidUtils.isDocUri(uri)) {
                    AndroidUtils.copyFile(getActivity(), uri, new File(FCLPath.FILES_DIR, "menu_icon." + type));
                } else {
                    try {
                        FileUtils.copyFile(new File(path), new File(FCLPath.FILES_DIR, "menu_icon." + type));
                    } catch (IOException ignore) {
                    }
                }
            });
        }
        if (v == binding.resetTheme) {
            ThemeEngine.getInstance().applyAndSave(getContext(), getContext().getColor(R.color.default_theme_color));
        }
        if (v == binding.resetTheme2) {
            ThemeEngine.getInstance().applyAndSave2(getContext(), Color.parseColor("#000000"));
        }
        if (v == binding.resetTheme2Dark) {
            ThemeEngine.getInstance().applyAndSave2Dark(getContext(), Color.parseColor("#000000"));
        }
        if (v == binding.resetBackgroundLive) {
            try {
                FileUtils.forceDelete(new File(FCLPath.LIVE_BACKGROUND_PATH));
                MainActivity.getInstance().setupLiveBackground();
            } catch (IOException ignore) {
            }
        }
        if (v == binding.fetchBackgroundColor || v == binding.fetchBackgroundColor2 || v == binding.fetchBackgroundColor2Dark) {
            boolean isDarkMode = (getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

            Bitmap bitmap = (isDarkMode ?
                    ThemeEngine.getInstance().theme.getBackgroundDk() :
                    ThemeEngine.getInstance().theme.getBackgroundLt()
            ).getBitmap();

            if (bitmap != null) {
                Palette palette = Palette.from(bitmap).generate();
                int dominantColor = palette.getDominantColor(getContext().getColor(R.color.default_theme_color));
                if (v == binding.fetchBackgroundColor) {
                    int color = palette.getMutedColor(dominantColor);
                    if (ThemeEngine.getInstance().getTheme().getColor() == color) {
                        color = palette.getLightVibrantColor(dominantColor);
                    }
                    ThemeEngine.getInstance().applyAndSave(getContext(), color);
                } else if (v == binding.fetchBackgroundColor2) {
                    ThemeEngine.getInstance().applyAndSave2(getContext(), palette.getVibrantColor(dominantColor));
                } else {
                    ThemeEngine.getInstance().applyAndSave2Dark(getContext(), palette.getVibrantColor(dominantColor));
                }
            }
        }
        if (v == binding.resetBackgroundLt) {
            new Thread(() -> {
                if (!new File(FCLPath.LT_BACKGROUND_PATH).delete() && new File(FCLPath.LT_BACKGROUND_PATH).exists())
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show());

                Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).binding.background, null, null));
            }).start();
        }
        if (v == binding.resetBackgroundDk) {
            new Thread(() -> {
                if (!new File(FCLPath.DK_BACKGROUND_PATH).delete() && new File(FCLPath.DK_BACKGROUND_PATH).exists())
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show());

                Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).binding.background, null, null));
            }).start();
        }
        if (v == binding.resetCursor) {
            deleteCursorFile();
        }
        if (v == binding.resetMenuIcon) {
            deleteMenuIconFile();
        }
    }

    private static void deleteMenuIconFile() {
        try {
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "menu_icon.png"));
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "menu_icon.gif"));
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to delete menu icon", e);
        }
    }

    private void deleteCursorFile() {
        try {
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "cursor.png"));
            Files.deleteIfExists(Paths.get(FCLPath.FILES_DIR, "cursor.gif"));
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Failed to delete cursor", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == binding.language) {
            LocaleUtils.changeLanguage(getContext(), position);
            LocaleUtils.setLanguage(getContext());
            if (!isFirst) {
                new FCLAlertDialog.Builder(getContext())
                        .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                        .setMessage(getContext().getString(R.string.message_warn_restart_after_change))
                        .setNegativeButton(getContext().getString(com.tungsten.fcllibrary.R.string.dialog_positive), () -> {

                        })
                        .create()
                        .show();
            } else {
                isFirst = false;
            }
        } else if (parent == binding.themeMode) {
            sharedPreferences.edit().putInt("themeMode", position).apply();
            int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            if (position != 0) {
                mode = position == 1 ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
            }
            AppCompatDelegate.setDefaultNightMode(mode);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
        if (v == binding.ignoreNotch) {
            ThemeEngine.getInstance().applyAndSave(getContext(), getActivity().getWindow(), isChecked);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        } else if (v == binding.closeSkinModel) {
            ThemeEngine.getInstance().getTheme().setiIgnoreSkinContainer(isChecked);
            Theme.saveTheme(getContext(), ThemeEngine.getInstance().getTheme());
        } else if (v == binding.disableFullscreenInput) {
            sharedPreferences.edit().putBoolean("disableFullscreenInput", isChecked).apply();
        } else if (v == binding.autoExitLauncher) {
            sharedPreferences.edit().putBoolean("autoExitLauncher", isChecked).apply();
        } else if (v == binding.allowScreenshots) {
            sharedPreferences.edit().putBoolean("allowScreenshots", isChecked).apply();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == binding.videoBackgroundVolume) {
            sharedPreferences.edit().putInt("videoBackgroundVolume", progress).apply();
            MainActivity.getInstance().setLiveBackgroundVolume();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
