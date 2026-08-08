package com.tungsten.fcl.ui.download.version;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fclcore.download.ArtifactMalformedException;
import com.tungsten.fclcore.download.DefaultDependencyManager;
import com.tungsten.fclcore.download.UnsupportedInstallationException;
import com.tungsten.fclcore.download.VersionMismatchException;
import com.tungsten.fclcore.download.game.GameAssetIndexDownloadTask;
import com.tungsten.fclcore.download.game.LibraryDownloadException;
import com.tungsten.fclcore.task.DownloadException;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.CancellationException;
import java.util.zip.ZipException;

/**
 * 安装失败原因弹窗（自遗留 VersionInstallInfoPage.alertFailureMessage 原样迁移，
 * 供 Compose 安装流程与保留原生安装器链路共用）。
 */
public final class InstallFailureAlert {

    private InstallFailureAlert() {
    }

    public static void alertFailureMessage(Context context, Exception exception, Runnable next) {
        FCLAlertDialog.Builder builder = new FCLAlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setAlertLevel(FCLAlertDialog.AlertLevel.ALERT);
        builder.setNegativeButton(context.getString(com.tungsten.fcllibrary.R.string.dialog_positive), next::run);
        String title;
        String msg;
        if (exception instanceof LibraryDownloadException) {
            String message = AndroidUtils.getLocalizedText(context, "launch_failed_download_library", ((LibraryDownloadException) exception).getLibrary().getName()) + "\n";
            if (exception.getCause() instanceof ResponseCodeException) {
                ResponseCodeException rce = (ResponseCodeException) exception.getCause();
                int responseCode = rce.getResponseCode();
                URL url = rce.getUrl();
                if (responseCode == 404)
                    message += AndroidUtils.getLocalizedText(context, "download_code_404", url);
                else
                    message += AndroidUtils.getLocalizedText(context, "download_failed", url, responseCode);
            } else {
                message += StringUtils.getStackTrace(exception.getCause());
            }
            title = context.getString(R.string.install_failed_downloading);
            msg = message;
        } else if (exception instanceof DownloadException) {
            URL url = ((DownloadException) exception).getUrl();
            if (exception.getCause() instanceof SocketTimeoutException) {
                title = context.getString(R.string.install_failed_downloading);
                msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_timeout", url);
            } else if (exception.getCause() instanceof ResponseCodeException) {
                ResponseCodeException responseCodeException = (ResponseCodeException) exception.getCause();
                if (AndroidUtils.hasStringId(context, "download_code_" + responseCodeException.getResponseCode())) {
                    title = context.getString(R.string.install_failed_downloading);
                    msg = AndroidUtils.getLocalizedText(context, "download_code_" + responseCodeException.getResponseCode(), url);
                } else {
                    title = context.getString(R.string.install_failed_downloading);
                    msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_detail", url);
                }
            } else {
                title = context.getString(R.string.install_failed_downloading);
                msg = AndroidUtils.getLocalizedText(context, "install_failed_downloading_detail", url) + "\n" + StringUtils.getStackTrace(exception.getCause());
            }
        } else if (exception instanceof UnsupportedInstallationException) {
            if (((UnsupportedInstallationException) exception).getReason() == UnsupportedInstallationException.FORGE_1_17_OPTIFINE_H1_PRE2) {
                title = context.getString(R.string.install_failed);
                msg = context.getString(R.string.install_failed_optifine_forge_1_17);
            } else {
                title = context.getString(R.string.install_failed);
                msg = context.getString(R.string.install_failed_optifine_conflict);
            }
        } else if (exception instanceof DefaultDependencyManager.UnsupportedLibraryInstallerException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.install_failed_install_online);
        } else if (exception instanceof ArtifactMalformedException || exception instanceof ZipException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.install_failed_malformed);
        } else if (exception instanceof GameAssetIndexDownloadTask.GameAssetIndexMalformedException) {
            title = context.getString(R.string.install_failed);
            msg = context.getString(R.string.assets_index_malformed);
        } else if (exception instanceof VersionMismatchException) {
            VersionMismatchException e = ((VersionMismatchException) exception);
            title = context.getString(R.string.install_failed);
            msg = AndroidUtils.getLocalizedText(context, "install_failed_version_mismatch", e.getExpect(), e.getActual());
        } else if (exception instanceof CancellationException) {
            // Ignore cancel
            title = "";
            msg = "";
        } else {
            title = context.getString(R.string.install_failed);
            msg = StringUtils.getStackTrace(exception);
        }
        builder.setTitle(title);
        builder.setMessage(msg);
        if (!(exception instanceof CancellationException)) {
            builder.create().show();
        }
    }
}
