package com.tungsten.fcl.upgrade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mio.promo.QuarkPromo;
import com.tungsten.fcl.R;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.gson.JsonUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateChecker {

    public static final String UPDATE_CHECK_URL = "https://raw.githubusercontent.com/FCL-Team/FoldCraftLauncher/main/version_map.json";
    public static final String UPDATE_CHECK_URL_CN = "https://gitee.com/fcl-team/FCL-Repo/raw/main/res/version_map.json";

    private static UpdateChecker instance;

    public static UpdateChecker getInstance() {
        if (instance == null) {
            instance = new UpdateChecker();
        }
        return instance;
    }

    /** 检查更新进行中：同步 CAS 置位，快速连点在异步任务起跑前即被忽略 */
    private final AtomicBoolean checking = new AtomicBoolean(false);

    /** 拉取当前版本更新内容进行中：同上防抖 */
    private final AtomicBoolean changelogLoading = new AtomicBoolean(false);

    public boolean isChecking() {
        return checking.get();
    }

    public UpdateChecker() {

    }

    public Task<?> checkManually(Context context) {
        return check(context, true, true);
    }

    /**
     * 拉取版本表并弹出当前版本的更新内容（设置页"查看更新内容"入口）。
     * 版本表中没有当前版本条目（官方下架或自定义构建）时给出提示。
     */
    public Task<?> showCurrentChangelog(Context context) {
        if (!changelogLoading.compareAndSet(false, true)) {
            return Task.runAsync(() -> {});
        }
        return Task.runAsync(() -> {
            try {
                String res = NetworkUtils.doGet(NetworkUtils.toURL(LocaleUtils.isChinese(context) ? UPDATE_CHECK_URL_CN : UPDATE_CHECK_URL));
                ArrayList<RemoteVersion> versions = JsonUtils.GSON.fromJson(res, new TypeToken<ArrayList<RemoteVersion>>(){}.getType());
                RemoteVersion current = versions.stream()
                        .filter(version -> version.getVersionCode() == getCurrentVersionCode(context))
                        .findFirst()
                        .orElse(null);
                if (current == null || current.getDescription() == null || current.getDescription().isEmpty()) {
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(context, context.getString(R.string.update_changelog_not_found), Toast.LENGTH_SHORT).show());
                    return;
                }
                Schedulers.androidUIThread().execute(() -> new ChangelogDialog(context, current).show());
            } finally {
                changelogLoading.set(false);
            }
        });
    }

    public Task<?> checkAuto(Context context) {
        return check(context, false, false);
    }

    public Task<?> check(Context context, boolean showBeta, boolean showAlert) {
        if (!checking.compareAndSet(false, true)) {
            return Task.runAsync(() -> {});
        }
        return Task.runAsync(() -> {
            try {
                if (showAlert) {
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(context, context.getString(R.string.update_checking), Toast.LENGTH_SHORT).show());
                }
                String res = NetworkUtils.doGet(NetworkUtils.toURL(LocaleUtils.isChinese(context) ? UPDATE_CHECK_URL_CN : UPDATE_CHECK_URL));
                ArrayList<RemoteVersion> versions = JsonUtils.GSON.fromJson(res, new TypeToken<ArrayList<RemoteVersion>>(){}.getType());
                // 顺带缓存最新网盘链接，供夸克网盘推广弹窗使用；拉取失败不会走到这里，沿用本地缓存
                versions.stream()
                        .max(Comparator.comparingInt(RemoteVersion::getVersionCode))
                        .ifPresent(version -> QuarkPromo.updateNetdiskUrl(context, version.getNetdiskUrl()));
                for (RemoteVersion version : versions) {
                    if (version.getVersionCode() > getCurrentVersionCode(context)) {
                        if (showBeta || !version.isBeta()) {
                            if (showBeta || !isIgnore(context, version.getVersionCode())) {
                                showUpdateDialog(context, version);
                            }
                            return;
                        }
                    }
                }
                if (showAlert) {
                    Schedulers.androidUIThread().execute(() -> Toast.makeText(context, context.getString(R.string.update_not_exist), Toast.LENGTH_SHORT).show());
                }
            } finally {
                checking.set(false);
            }
        });
    }

    public static int getCurrentVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't get current version code");
        }
    }

    private void showUpdateDialog(Context context, RemoteVersion version) {
        Schedulers.androidUIThread().execute(() -> {
            UpdateDialog dialog = new UpdateDialog(context, version);
            dialog.show();
        });
    }

    public static boolean isIgnore(Context context, int code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ignore_update", -1) == code;
    }

    public static void setIgnore(Context context, int code) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ignore_update", code);
        editor.apply();
    }

}
