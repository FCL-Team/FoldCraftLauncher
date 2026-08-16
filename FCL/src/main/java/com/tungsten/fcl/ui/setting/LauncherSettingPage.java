package com.tungsten.fcl.ui.setting;

import static android.content.Context.MODE_PRIVATE;
import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.util.Lang.thread;
import static com.tungsten.fclcore.util.Logging.LOG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mio.ui.adapter.SpacingItemDecoration;
import com.tungsten.fcl.R;
import com.tungsten.fcl.activity.MainActivity;
import com.tungsten.fcl.databinding.PageSettingLauncherBinding;
import com.tungsten.fcl.setting.DownloadProviders;
import com.tungsten.fcl.upgrade.UpdateChecker;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclauncher.utils.FCLPath;
import com.tungsten.fclcore.task.FetchTask;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;
import com.tungsten.fcllibrary.component.dialog.FCLColorPickerDialog;
import com.tungsten.fcllibrary.component.theme.ThemeData;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.component.ui.FCLPage;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.IntConsumer;
import java.util.logging.Level;

/**
 * 启动器设置页。设置项由 {@link LauncherSettingAdapter} 以 RecyclerView 行级复用渲染，
 * 页面只负责对话框、文件选择与权限等业务逻辑。
 */
public class LauncherSettingPage extends FCLPage implements LauncherSettingAdapter.Listener {

    private SharedPreferences sharedPreferences;

    public LauncherSettingPage(Context context, int id, int resId) {
        super(context, id, resId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getActivity().getSharedPreferences("launcher", MODE_PRIVATE);
        PageSettingLauncherBinding binding = PageSettingLauncherBinding.bind(getContentView());
        LauncherSettingAdapter adapter = new LauncherSettingAdapter(getContext(), this);
        binding.settingList.setLayoutManager(new LinearLayoutManager(getContext()));
        // 行间用间距分隔（ItemDecoration），最后一行不加
        binding.settingList.addItemDecoration(new SpacingItemDecoration((int) (8 * getContext().getResources().getDisplayMetrics().density)));
        binding.settingList.setAdapter(adapter);
        adapter.rebuild();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return null;
    }

    @Override
    public void onButtonClick(LauncherSettingTag tag) {
        switch (tag) {
            case CHECK_UPDATE:
                if (!UpdateChecker.getInstance().isChecking()) {
                    UpdateChecker.getInstance().checkManually(getContext()).whenComplete(Schedulers.androidUIThread(), e -> {
                        if (e != null) {
                            FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
                            builder.setMessage(getContext().getString(R.string.update_check_failed) + "\n" + e);
                            builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                            builder.create().show();
                        }
                    }).start();
                }
                break;
            case CLEAR_CACHE:
                FileUtils.cleanDirectoryQuietly(new File(FCLPath.CACHE_DIR).getParentFile());
                break;
            case EXPORT_LOG:
                exportLog();
                break;
            case REQUEST_AUDIO:
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
                break;
            case THEME_COLOR_SET:
                showColorPicker(ThemeEngine.getInstance().getTheme().getColor(),
                        color -> ThemeEngine.getInstance().applyColor(color),
                        color -> ThemeEngine.getInstance().applyAndSave(getContext(), color),
                        color -> ThemeEngine.getInstance().applyColor(color));
                break;
            case THEME_COLOR2_SET:
                showColorPicker(ThemeEngine.getInstance().getTheme()._getColor2(),
                        color -> ThemeEngine.getInstance().applyColor2(color),
                        color -> ThemeEngine.getInstance().applyAndSave2(getContext(), color),
                        color -> ThemeEngine.getInstance().applyColor2(color));
                break;
            case THEME_COLOR2_DARK_SET:
                showColorPicker(ThemeEngine.getInstance().getTheme().getColor2Dark(),
                        color -> ThemeEngine.getInstance().applyColor2Dark(color),
                        color -> ThemeEngine.getInstance().applyAndSave2Dark(getContext(), color),
                        color -> ThemeEngine.getInstance().applyColor2Dark(color));
                break;
            case BACKGROUND_LT_SET:
                selectImage(false);
                break;
            case BACKGROUND_DK_SET:
                selectImage(true);
                break;
            case BACKGROUND_LIVE_SET:
                selectLiveBackground();
                break;
            case CURSOR_SET:
                selectCursor();
                break;
            case MENU_ICON_SET:
                selectMenuIcon();
                break;
            case THEME_COLOR_RESET:
                ThemeEngine.getInstance().applyAndSave(getContext(), getContext().getColor(R.color.default_theme_color));
                break;
            case THEME_COLOR2_RESET:
                ThemeEngine.getInstance().applyAndSave2(getContext(), Color.parseColor("#000000"));
                break;
            case THEME_COLOR2_DARK_RESET:
                ThemeEngine.getInstance().applyAndSave2Dark(getContext(), Color.parseColor("#000000"));
                break;
            case BACKGROUND_LIVE_RESET:
                try {
                    FileUtils.forceDelete(new File(FCLPath.LIVE_BACKGROUND_PATH));
                    MainActivity.getInstance().setupLiveBackground();
                } catch (IOException ignore) {
                }
                break;
            case THEME_COLOR_FETCH:
            case THEME_COLOR2_FETCH:
            case THEME_COLOR2_DARK_FETCH:
                fetchBackgroundColor(tag);
                break;
            case BACKGROUND_LT_RESET:
                resetBackground(false);
                break;
            case BACKGROUND_DK_RESET:
                resetBackground(true);
                break;
            case CURSOR_RESET:
                deleteCursorFile();
                break;
            case MENU_ICON_RESET:
                deleteMenuIconFile();
                break;
            default:
                break;
        }
    }

    private void exportLog() {
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
                    builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
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
                builder.setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), null);
                builder.create().show();
            });
        });
    }

    private void showColorPicker(int initColor, IntConsumer apply, IntConsumer applyAndSave, IntConsumer restore) {
        FCLColorPickerDialog dialog = new FCLColorPickerDialog(getContext(), initColor, new FCLColorPickerDialog.Listener() {
            @Override
            public void onColorChanged(int color) {
                apply.accept(color);
            }

            @Override
            public void onPositive(int destColor) {
                applyAndSave.accept(destColor);
            }

            @Override
            public void onNegative(int initColor) {
                restore.accept(initColor);
            }
        });
        dialog.show();
    }

    private void selectImage(boolean isDk) {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".png");
        suffix.add(".jpg");
        suffix.add(".jpeg");
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
            if (files == null) return;
            String path = files.get(0);
            Uri uri = Uri.parse(path);
            if (AndroidUtils.isDocUri(uri)) {
                path = AndroidUtils.copyFileToDir(getActivity(), uri, new File(FCLPath.CACHE_DIR));
            }
            ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).binding.background, isDk ? null : path, isDk ? path : null);
        });
    }

    private void selectLiveBackground() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".mp4");
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
            if (files == null) return;
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

    private void selectCursor() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".png");
        suffix.add(".gif");
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
            if (files == null) return;
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

    private void selectMenuIcon() {
        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(".png");
        suffix.add(".gif");
        MainActivity.getInstance().fileLauncher.launchSingleSelection(null, suffix, files -> {
            if (files == null) return;
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

    private void fetchBackgroundColor(LauncherSettingTag tag) {
        boolean isDarkMode = ThemeEngine.isNightMode(getContext());

        Bitmap bitmap = (isDarkMode ?
                ThemeEngine.getInstance().getTheme().getBackgroundDk() :
                ThemeEngine.getInstance().getTheme().getBackgroundLt()
        ).getBitmap();

        if (bitmap != null) {
            Palette palette = Palette.from(bitmap).generate();
            int dominantColor = palette.getDominantColor(getContext().getColor(R.color.default_theme_color));
            if (tag == LauncherSettingTag.THEME_COLOR_FETCH) {
                int color = palette.getMutedColor(dominantColor);
                if (ThemeEngine.getInstance().getTheme().getColor() == color) {
                    color = palette.getLightVibrantColor(dominantColor);
                }
                ThemeEngine.getInstance().applyAndSave(getContext(), color);
            } else if (tag == LauncherSettingTag.THEME_COLOR2_FETCH) {
                ThemeEngine.getInstance().applyAndSave2(getContext(), palette.getVibrantColor(dominantColor));
            } else {
                ThemeEngine.getInstance().applyAndSave2Dark(getContext(), palette.getVibrantColor(dominantColor));
            }
        }
    }

    private void resetBackground(boolean isDk) {
        new Thread(() -> {
            File backgroundFile = new File(isDk ? FCLPath.DK_BACKGROUND_PATH : FCLPath.LT_BACKGROUND_PATH);
            if (!backgroundFile.delete() && backgroundFile.exists())
                Schedulers.androidUIThread().execute(() -> Toast.makeText(getContext(), getContext().getString(R.string.message_failed), Toast.LENGTH_SHORT).show());

            Schedulers.androidUIThread().execute(() -> ThemeEngine.getInstance().applyAndSave(getContext(), ((MainActivity) getActivity()).binding.background, null, null));
        }).start();
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
    public void onSwitchToggle(LauncherSettingTag tag, boolean checked) {
        switch (tag) {
            case SWITCH_AUTO_EXIT:
                sharedPreferences.edit().putBoolean("autoExitLauncher", checked).apply();
                break;
            case SWITCH_IGNORE_NOTCH:
                ThemeEngine.getInstance().applyAndSave(getContext(), getActivity().getWindow(), checked);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                break;
            case SWITCH_CLOSE_SKIN_MODEL:
                ThemeEngine.getInstance().setCloseSkinModel(checked);
                ThemeData.saveTheme(getContext(), ThemeEngine.getInstance().getTheme());
                break;
            case SWITCH_DISABLE_FULLSCREEN_INPUT:
                sharedPreferences.edit().putBoolean("disableFullscreenInput", checked).apply();
                break;
            case SWITCH_ALLOW_SCREENSHOTS:
                sharedPreferences.edit().putBoolean("allowScreenshots", checked).apply();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSpinnerSelect(LauncherSettingTag tag, int position) {
        switch (tag) {
            case SPINNER_LANGUAGE:
                // 初始化/复用 bind 的回调与当前值相同，忽略；实际切换才生效
                if (position == LocaleUtils.getLanguage(getContext())) return;
                LocaleUtils.changeLanguage(getContext(), position);
                LocaleUtils.setLanguage(getContext());
                new FCLAlertDialog.Builder(getContext())
                        .setAlertLevel(FCLAlertDialog.AlertLevel.INFO)
                        .setMessage(getContext().getString(R.string.message_warn_restart_after_change))
                        .setNegativeButton(getContext().getString(com.tungsten.fcl.R.string.dialog_positive), () -> {

                        })
                        .create()
                        .show();
                break;
            case SPINNER_THEME_MODE:
                sharedPreferences.edit().putInt("themeMode", position).apply();
                int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                if (position != 0) {
                    mode = position == 1 ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
                }
                AppCompatDelegate.setDefaultNightMode(mode);
                // configChanges 含 uiMode 时 AppCompat 不重建 Activity 也不更新 Resources 配置，
                // 亮暗切换需显式刷新主题控件与背景（ThemeEngine.isNightMode 读 themeMode 设置）
                ThemeEngine.getInstance().refreshTheme();
                break;
            case SPINNER_SOURCE_AUTO:
                config().versionListSourceProperty().set(new ArrayList<>(DownloadProviders.providersById.keySet()).get(position));
                break;
            case SPINNER_SOURCE:
                config().downloadTypeProperty().set(new ArrayList<>(DownloadProviders.rawProviders.keySet()).get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public void onSeekBarChange(LauncherSettingTag tag, int progress) {
        switch (tag) {
            case SEEKBAR_VIDEO_VOLUME:
                sharedPreferences.edit().putInt("videoBackgroundVolume", progress).apply();
                MainActivity.getInstance().setLiveBackgroundVolume();
                break;
            case SEEKBAR_ANIMATION_SPEED:
                ThemeEngine.getInstance().setAnimationSpeed(progress);
                ThemeData.saveTheme(getContext(), ThemeEngine.getInstance().getTheme());
                break;
            case SEEKBAR_VIBRATION:
                sharedPreferences.edit().putInt("vibrationDuration", progress).apply();
                break;
            case SEEKBAR_THREADS:
                config().downloadThreadsProperty().set(progress);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckToggle(LauncherSettingTag tag, boolean checked) {
        switch (tag) {
            case CHECK_AUTO_SOURCE:
                config().autoChooseDownloadTypeProperty().set(checked);
                break;
            case CHECK_AUTO_THREADS:
                config().autoDownloadThreadsProperty().set(checked);
                if (checked) {
                    config().downloadThreadsProperty().set(FetchTask.DEFAULT_CONCURRENCY);
                }
                break;
            default:
                break;
        }
    }
}
