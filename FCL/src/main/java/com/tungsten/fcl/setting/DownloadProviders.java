/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2021  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.setting;

import static com.tungsten.fcl.setting.ConfigHolder.config;
import static com.tungsten.fclcore.task.FetchTask.DEFAULT_CONCURRENCY;

import android.content.Context;

import com.tungsten.fcl.R;
import com.mio.util.AndroidUtilKt;
import com.tungsten.fcl.util.FXUtils;
import com.tungsten.fclcore.download.ArtifactMalformedException;
import com.tungsten.fclcore.download.AutoDownloadProvider;
import com.tungsten.fclcore.download.BMCLAPIDownloadProvider;
import com.tungsten.fclcore.download.DownloadProvider;
import com.tungsten.fclcore.download.DownloadProviderWrapper;
import com.tungsten.fclcore.download.MojangDownloadProvider;
import com.tungsten.fclcore.task.DownloadException;
import com.tungsten.fclcore.task.FetchTask;
import com.tungsten.fclcore.util.StringUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;
import com.tungsten.fcllibrary.util.LocaleUtils;

import javax.net.ssl.SSLHandshakeException;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.CancellationException;

public final class DownloadProviders {
    private DownloadProviders() {
    }

    private static final DownloadProviderWrapper PROVIDER_WRAPPER;

    private static final DownloadProvider MOJANG_PROVIDER;
    private static final BMCLAPIDownloadProvider BMCLAPI_PROVIDER;
    private static final DownloadProvider DEFAULT_PROVIDER;

    static {
        String bmclapiRoot = "https://bmclapi2.bangbang93.com";
        BMCLAPI_PROVIDER = new BMCLAPIDownloadProvider(bmclapiRoot);
        MOJANG_PROVIDER = new MojangDownloadProvider();
        DEFAULT_PROVIDER = createDownloadProvider(DownloadSource.DEFAULT, DownloadSource.DEFAULT);
        PROVIDER_WRAPPER = new DownloadProviderWrapper(DEFAULT_PROVIDER);
    }

    /**
     * 初始化下载源设置，并同步下载线程数设置。
     */
    public static void init() {
        FXUtils.observeWeak(() -> FetchTask.setDownloadExecutorConcurrency(
                        config().getAutoDownloadThreads() ? DEFAULT_CONCURRENCY : config().getDownloadThreads()),
                config().autoDownloadThreadsProperty(), config().downloadThreadsProperty());

        FXUtils.observeWeak(() -> PROVIDER_WRAPPER.setProvider(createDownloadProvider(
                toDownloadSource(config().getVersionListSource()),
                toDownloadSource(config().getFileDownloadSource()))),
                config().versionListSourceProperty(), config().fileDownloadSourceProperty());
    }

    private static DownloadSource toDownloadSource(String value) {
        try {
            return DownloadSource.valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return DownloadSource.DEFAULT;
        }
    }

    /**
     * 按版本列表与文件下载两种偏好创建带候选链的下载源。
     */
    private static DownloadProvider createDownloadProvider(DownloadSource versionListSource, DownloadSource fileDownloadSource) {
        return new AutoDownloadProvider(
                getCandidates(versionListSource),
                getCandidates(fileDownloadSource));
    }

    /**
     * 按偏好排序下载源候选列表。
     */
    private static List<DownloadProvider> getCandidates(DownloadSource source) {
        DownloadSource normalized = source != null ? source : DownloadSource.DEFAULT;
        switch (normalized) {
            case DEFAULT:
                return LocaleUtils.IS_CHINA_MAINLAND
                        ? List.of(BMCLAPI_PROVIDER, MOJANG_PROVIDER)
                        : List.of(MOJANG_PROVIDER, BMCLAPI_PROVIDER);
            case OFFICIAL:
                return List.of(MOJANG_PROVIDER);
            case MIRROR:
                return List.of(BMCLAPI_PROVIDER, MOJANG_PROVIDER);
            default:
                return List.of(MOJANG_PROVIDER);
        }
    }

    /**
     * Get current primary preferred download provider
     */
    public static DownloadProvider getDownloadProvider() {
        return PROVIDER_WRAPPER;
    }

    public static String localizeErrorMessage(Context context, Throwable exception) {
        if (exception instanceof DownloadException) {
            URL url = ((DownloadException) exception).getUrl();
            if (exception.getCause() instanceof SocketTimeoutException) {
                return context.getString(R.string.install_failed_downloading_timeout, url);
            } else if (exception.getCause() instanceof ResponseCodeException) {
                ResponseCodeException responseCodeException = (ResponseCodeException) exception.getCause();
                if (AndroidUtilKt.hasStringId(context, "download_code_" + responseCodeException.getResponseCode())) {
                    return AndroidUtilKt.getLocalizedText(context, "download_code_" + responseCodeException.getResponseCode(), url);
                } else {
                    return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + StringUtils.getStackTrace(exception.getCause());
                }
            } else if (exception.getCause() instanceof FileNotFoundException) {
                return context.getString(R.string.download_code_404, url);
            } else if (exception.getCause() instanceof AccessDeniedException) {
                return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + context.getString(R.string.exception_access_denied, ((AccessDeniedException) exception.getCause()).getFile());
            } else if (exception.getCause() instanceof ArtifactMalformedException) {
                return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + context.getString(R.string.exception_artifact_malformed);
            } else if (exception.getCause() instanceof SSLHandshakeException && !(exception.getCause().getMessage() != null && exception.getCause().getMessage().contains("Remote host terminated"))) {
                if (exception.getCause().getMessage() != null && (exception.getCause().getMessage().contains("No name matching") || exception.getCause().getMessage().contains("No subject alternative DNS name matching"))) {
                    return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + context.getString(R.string.exception_dns_pollution);
                }
                return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + context.getString(R.string.exception_ssl_handshake);
            } else {
                return context.getString(R.string.install_failed_downloading_detail, url) + "\n" + StringUtils.getStackTrace(exception.getCause());
            }
        } else if (exception instanceof ArtifactMalformedException) {
            return context.getString(R.string.exception_artifact_malformed);
        } else if (exception instanceof CancellationException) {
            return context.getString(R.string.message_cancelled);
        }
        return StringUtils.getStackTrace(exception);
    }
}
