/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
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
package com.tungsten.fclcore.task;

import static com.tungsten.fclcore.util.Lang.threadPool;

import com.tungsten.fclcore.event.Event;
import com.tungsten.fclcore.event.EventBus;
import com.tungsten.fclcore.util.CacheRepository;
import com.tungsten.fclcore.util.DigestUtils;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.ToStringBuilder;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;

import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class FetchTask<T> extends Task<T> {
    protected final List<URL> urls;
    protected final int retry;
    protected boolean caching;
    protected CacheRepository repository = CacheRepository.getInstance();

    /** 当前下载进度的字节信息（供 UI 显示"已下载/总量"） */
    private long downloadedBytes = 0;
    private long totalBytes = -1;

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public FetchTask(List<URL> urls, int retry) {
        Objects.requireNonNull(urls);

        this.urls = urls.stream().filter(Objects::nonNull).collect(Collectors.toList());
        this.retry = retry;

        if (this.urls.isEmpty())
            throw new IllegalArgumentException("At least one URL is required");

        setExecutor(download());
    }

    public void setCaching(boolean caching) {
        this.caching = caching;
    }

    public void setCacheRepository(CacheRepository repository) {
        this.repository = repository;
    }

    protected void beforeDownload(URL url) throws IOException {}

    protected abstract void useCachedResult(Path cachedFile) throws IOException;

    protected abstract EnumCheckETag shouldCheckETag();

    protected abstract Context getContext(URLConnection conn, boolean checkETag, @Nullable String bmclapiHash) throws IOException;

    @Override
    public void execute() throws Exception {
        Exception exception = null;
        URL failedURL = null;
        boolean checkETag;
        switch (shouldCheckETag()) {
            case CHECK_E_TAG: checkETag = true; break;
            case NOT_CHECK_E_TAG: checkETag = false; break;
            default: return;
        }

        int repeat = 0;
        download: for (URL url : urls) {
            // 慢速节点切换次数按 URL 候选统计，同一候选的多次重试共享预算
            BMCLAPIRetryPolicy retryPolicy = new BMCLAPIRetryPolicy();
            for (int retryTime = 0; retryTime < retry; retryTime++) {
                if (isCancelled()) {
                    break download;
                }

                List<String> redirects = null;
                List<Map<String, List<String>>> redirectResponses = new ArrayList<>();
                try {
                    beforeDownload(url);

                    updateProgress(0);

                    URLConnection conn = NetworkUtils.createConnection(url);
                    if (checkETag) repository.injectConnection(conn);

                    String bmclapiHash = null;
                    if (conn instanceof HttpURLConnection) {
                        redirects = new ArrayList<>();

                        HttpURLConnection httpConnection = NetworkUtils.resolveConnection((HttpURLConnection) conn, redirects, redirectResponses);
                        conn = httpConnection;
                        int responseCode = httpConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                            // Handle cache
                            try {
                                Path cache = repository.getCachedRemoteFile(conn);
                                useCachedResult(cache);
                                return;
                            } catch (IOException e) {
                                Logging.LOG.log(Level.WARNING, "Unable to use cached file, redownload " + url, e);
                                repository.removeRemoteEntry(conn);
                                // Now we must reconnect the server since 304 may result in empty content,
                                // if we want to redownload the file, we must reconnect the server without etag settings.
                                retryTime--;
                                continue;
                            }
                        } else if (responseCode / 100 == 4) {
                            throw new FileNotFoundException(url.toString());
                        } else if (responseCode / 100 != 2) {
                            throw new ResponseCodeException(url, responseCode);
                        }

                        // BMCLAPI 主控的 302 响应会携带 x-bmclapi-hash（目标文件 SHA-1），
                        // 用于识别 BMCLAPI 链路：命中哈希缓存可直接跳过下载，慢速切换节点时也能校验完整性
                        bmclapiHash = resolveBMCLAPIHash(redirectResponses, httpConnection.getHeaderFields());
                        if (bmclapiHash != null) {
                            Optional<Path> cache = repository.checkExistentFile(null, "SHA-1", bmclapiHash);
                            if (cache.isPresent()) {
                                Logging.LOG.log(Level.FINER, "Downloading " + url + " skipped, BMCLAPI hash cache hit: " + bmclapiHash);
                                useCachedResult(cache.get());
                                return;
                            }
                        }
                    }

                    long contentLength = conn.getContentLengthLong();
                    totalBytes = contentLength;
                    // 保留最后一次重试应对切换节点后的网络错误，避免在末次尝试上白白放弃已建立的连接
                    boolean armSlowNodeRetry = bmclapiHash != null && retryTime < retry - 1;
                    try (Context context = getContext(conn, checkETag, bmclapiHash); InputStream stream = conn.getInputStream()) {
                        long lastDownloaded = 0, downloaded = 0;
                        if (armSlowNodeRetry)
                            retryPolicy.startAttempt(System.nanoTime());
                        byte[] buffer = new byte[IOUtils.DEFAULT_BUFFER_SIZE];
                        while (true) {
                            if (isCancelled()) break;

                            int len = stream.read(buffer);
                            if (len == -1) break;

                            context.write(buffer, 0, len);

                            downloaded += len;
                            downloadedBytes = downloaded;

                            if (contentLength >= 0) {
                                // Update progress information per second
                                updateProgress(downloaded, contentLength);
                            }

                            updateDownloadSpeed((int) (downloaded - lastDownloaded));
                            lastDownloaded = downloaded;

                            // 持续低速且剩余内容按当前速度还需下载超过一个观测窗口时，
                            // 主动放弃当前连接，经常规重试流程重新请求主控分配新节点
                            if (armSlowNodeRetry && retryPolicy.shouldRetry(downloaded, contentLength, System.nanoTime()))
                                throw new SocketTimeoutException("BMCLAPI node is too slow to complete the remaining download in time");
                        }

                        if (isCancelled()) break download;

                        updateDownloadSpeed((int) (downloaded - lastDownloaded));

                        // 取消与大小校验之间存在竞态窗口（取消瞬间已读部分数据），
                        // 取消时不能视为下载失败，否则取消会被包装成 IOException 报错
                        if (isCancelled()) break download;
                        if (contentLength >= 0 && downloaded != contentLength)
                            throw new IOException("Unexpected file size: " + downloaded + ", expected: " + contentLength);

                        context.withResult(true);
                    }

                    return;
                } catch (FileNotFoundException ex) {
                    failedURL = url;
                    exception = ex;
                    Logging.LOG.log(Level.WARNING, "Failed to download " + url + ", not found" + ((redirects == null || redirects.isEmpty()) ? "" : ", redirects: " + redirects), ex);

                    break; // we will not try this URL again
                } catch (IOException ex) {
                    failedURL = url;
                    exception = ex;
                    Logging.LOG.log(Level.WARNING, "Failed to download " + url + ", repeat times: " + (++repeat) + ((redirects == null || redirects.isEmpty()) ? "" : ", redirects: " + redirects), ex);
                    // 取消优先于重试：取消期间的异常统一以取消处理
                    if (isCancelled())
                        throw new CancellationException();
                    // 仍有下一次重试时先退避等待，避免服务器限流/掐流时连续快速重试继续失败
                    if (retryTime < retry - 1) {
                        awaitRetryDelay(retryTime);
                    }
                }
            }
        }

        if (exception != null) {
            if (isCancelled())
                throw new CancellationException();
            throw new DownloadException(failedURL, exception);
        }
    }

    /** 重试退避序列（毫秒）：第 1 次失败后 500ms，之后 2s、5s，更多次保持 5s */
    private static final long[] RETRY_DELAYS = {500, 2000, 5000};

    /**
     * 重试前的退避等待。分片轮询 isCancelled()，
     * 等待期间取消任务可在单个分片内退出，不会拖慢取消响应。
     */
    private void awaitRetryDelay(int retryTime) {
        long delay = RETRY_DELAYS[Math.min(retryTime, RETRY_DELAYS.length - 1)];
        long deadline = System.currentTimeMillis() + delay;
        while (!isCancelled() && System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(Math.min(100, deadline - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static final Timer timer = new Timer("DownloadSpeedRecorder", true);
    private static final AtomicInteger downloadSpeed = new AtomicInteger(0);
    public static final EventBus speedEvent = new EventBus();

    static {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                speedEvent.channel(SpeedEvent.class).fireEvent(new SpeedEvent(speedEvent, downloadSpeed.getAndSet(0)));
            }
        }, 0, 1000);
    }

    private static void updateDownloadSpeed(int speed) {
        downloadSpeed.addAndGet(speed);
    }

    public static class SpeedEvent extends Event {
        private final int speed;

        public SpeedEvent(Object source, int speed) {
            super(source);

            this.speed = speed;
        }

        /**
         * Download speed in byte/sec.
         * @return download speed
         */
        public int getSpeed() {
            return speed;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("speed", speed).toString();
        }
    }

    /**
     * 按重定向链顺序收集 x-bmclapi-hash（BMCLAPI 主控在 302 响应上携带的目标文件 SHA-1）。
     * 首个有效哈希生效，链上出现互相冲突的哈希说明响应异常，抛错走常规重试重新分配节点。
     */
    static String resolveBMCLAPIHash(List<Map<String, List<String>>> redirectResponses,
                                     Map<String, List<String>> finalHeaders) throws IOException {
        String result = null;
        for (Map<String, List<String>> headers : redirectResponses) {
            result = collectBMCLAPIHash(result, headers);
        }
        if (finalHeaders != null)
            result = collectBMCLAPIHash(result, finalHeaders);
        return result;
    }

    private static String collectBMCLAPIHash(@Nullable String current, @Nullable Map<String, List<String>> headers) throws IOException {
        if (headers == null)
            return current;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() == null || !"x-bmclapi-hash".equalsIgnoreCase(entry.getKey()))
                continue;
            List<String> values = entry.getValue();
            if (values == null || values.isEmpty() || !DigestUtils.isSha1Digest(values.get(0)))
                continue;
            String hash = values.get(0);
            if (current != null && !current.equalsIgnoreCase(hash))
                throw new IOException("Conflicting BMCLAPI hashes in redirect chain: " + current + " vs " + hash);
            return current != null ? current : hash;
        }
        return current;
    }

    /**
     * BMCLAPI 慢速节点重试策略。
     * 以固定时长的窗口观测实际下载速度，当速度持续过低、且剩余内容按当前速度还需下载超过
     * 一个完整窗口时判定节点过慢，由调用方抛出异常走常规重试流程（重新请求主控分配新节点）。
     * 按预计剩余时间判断可避免小文件和接近完成的下载无谓切换节点；长度未知的响应无法估算，不启用。
     */
    static final class BMCLAPIRetryPolicy {
        /** 单个 URL 候选内最多因慢速切换节点的次数 */
        static final int MAX_SLOW_RETRIES = 2;
        /** 每个连接的观测窗口时长 */
        static final long WINDOW_NANOS = TimeUnit.SECONDS.toNanos(10);
        /** 不触发慢速判定的最低速度（字节/秒） */
        static final long MIN_BYTES_PER_SECOND = 128 * 1024;

        /** 当前观测窗口的开始时间（单调时钟） */
        private long started;
        /** 当前观测窗口开始前已累计的字节数 */
        private long downloaded;
        /** 该策略在候选生命周期内已请求的节点切换次数 */
        private int retries;

        /** 开始新的观测窗口，保留候选已用的慢速切换次数 */
        void startAttempt(long now) {
            this.started = now;
            this.downloaded = 0;
        }

        boolean shouldRetry(long downloaded, long contentLength, long now) {
            if (retries >= MAX_SLOW_RETRIES || contentLength <= downloaded)
                return false;

            long elapsed = now - started;
            if (elapsed < WINDOW_NANOS)
                return false;

            double bytesPerSecond = (downloaded - this.downloaded) * (double) TimeUnit.SECONDS.toNanos(1) / elapsed;
            this.started = now;
            this.downloaded = downloaded;
            if (bytesPerSecond >= MIN_BYTES_PER_SECOND
                    || contentLength - downloaded <= bytesPerSecond * WINDOW_NANOS / TimeUnit.SECONDS.toNanos(1))
                return false;

            retries++;
            return true;
        }
    }

    protected static abstract class Context implements Closeable {
        private boolean success;

        public abstract void write(byte[] buffer, int offset, int len) throws IOException;

        public final void withResult(boolean success) {
            this.success = success;
        }

        protected boolean isSuccess() {
            return success;
        }
    }

    protected enum EnumCheckETag {
        CHECK_E_TAG,
        NOT_CHECK_E_TAG,
        CACHED
    }

    protected static final class DownloadState {
        private final int startPosition;
        private final int endPosition;
        private final int currentPosition;
        private final boolean finished;

        public DownloadState(int startPosition, int endPosition, int currentPosition) {
            if (currentPosition < startPosition || currentPosition > endPosition) {
                throw new IllegalArgumentException("Illegal download state: start " + startPosition + ", end " + endPosition + ", cur " + currentPosition);
            }
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.currentPosition = currentPosition;
            finished = currentPosition == endPosition;
        }

        public int getStartPosition() {
            return startPosition;
        }

        public int getEndPosition() {
            return endPosition;
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public boolean isFinished() {
            return finished;
        }
    }

    protected static final class DownloadMission {

    }

    public static int DEFAULT_CONCURRENCY = Math.min(Runtime.getRuntime().availableProcessors() * 4, 64);
    private static int downloadExecutorConcurrency = DEFAULT_CONCURRENCY;
    private static volatile ThreadPoolExecutor DOWNLOAD_EXECUTOR;

    /**
     * Get singleton instance of the thread pool for file downloading.
     *
     * @return Thread pool for FetchTask
     */
    protected static ExecutorService download() {
        if (DOWNLOAD_EXECUTOR == null) {
            synchronized (Schedulers.class) {
                if (DOWNLOAD_EXECUTOR == null) {
                    DOWNLOAD_EXECUTOR = threadPool("Download", true, downloadExecutorConcurrency, 10, TimeUnit.SECONDS);
                }
            }
        }

        return DOWNLOAD_EXECUTOR;
    }

    public static void setDownloadExecutorConcurrency(int concurrency) {
        concurrency = Math.max(concurrency, 1);
        synchronized (Schedulers.class) {
            downloadExecutorConcurrency = concurrency;
            ThreadPoolExecutor downloadExecutor = DOWNLOAD_EXECUTOR;
            if (downloadExecutor != null) {
                if (downloadExecutor.getMaximumPoolSize() <= concurrency) {
                    downloadExecutor.setMaximumPoolSize(concurrency);
                    downloadExecutor.setCorePoolSize(concurrency);
                } else {
                    downloadExecutor.setCorePoolSize(concurrency);
                    downloadExecutor.setMaximumPoolSize(concurrency);
                }
            }
        }
    }

    public static int getDownloadExecutorConcurrency() {
        synchronized (Schedulers.class) {
            return downloadExecutorConcurrency;
        }
    }
}
