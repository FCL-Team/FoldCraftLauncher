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

import static com.tungsten.fclcore.util.DigestUtils.digestToString;
import static com.tungsten.fclcore.util.DigestUtils.getDigest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

import org.jetbrains.annotations.Nullable;

import com.tungsten.fclcore.util.Hex;
import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fclcore.util.io.ChecksumMismatchException;
import com.tungsten.fclcore.util.io.CompressingUtils;
import com.tungsten.fclcore.util.io.FileUtils;
import com.tungsten.fclcore.util.io.IOUtils;
import com.tungsten.fclcore.util.io.NetworkUtils;
import com.tungsten.fclcore.util.io.ResponseCodeException;

/**
 * A task that can download a file online.
 */
public class FileDownloadTask extends FetchTask<Void> {

    public static class IntegrityCheck {
        private final String algorithm;
        private final String checksum;

        public IntegrityCheck(String algorithm, String checksum) {
            this.algorithm = requireNonNull(algorithm);
            this.checksum = requireNonNull(checksum);
        }

        public static IntegrityCheck of(String algorithm, String checksum) {
            if (checksum == null) return null;
            else return new IntegrityCheck(algorithm, checksum);
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public String getChecksum() {
            return checksum;
        }

        public MessageDigest createDigest() {
            return getDigest(algorithm);
        }

        public void performCheck(MessageDigest digest) throws ChecksumMismatchException {
            String actualChecksum = Hex.encodeHex(digest.digest());
            if (!checksum.equalsIgnoreCase(actualChecksum)) {
                throw new ChecksumMismatchException(algorithm, checksum, actualChecksum);
            }
        }
    }

    private final File file;
    private final IntegrityCheck integrityCheck;
    private Path candidate;
    private final ArrayList<IntegrityCheckHandler> integrityCheckHandlers = new ArrayList<>();
    /** 当前尝试正在写入的临时文件，getContext 创建，供并行续传阶段按绝对偏移写入 */
    private Path tempFile;

    /**
     * @param url the URL of remote file.
     * @param file the location that download to.
     */
    public FileDownloadTask(URL url, File file) {
        this(url, file, null);
    }

    /**
     * @param url the URL of remote file.
     * @param file the location that download to.
     * @param integrityCheck the integrity check to perform, null if no integrity check is to be performed
     */
    public FileDownloadTask(URL url, File file, IntegrityCheck integrityCheck) {
        this(Collections.singletonList(url), file, integrityCheck);
    }

    /**
     * @param url the URL of remote file.
     * @param file the location that download to.
     * @param integrityCheck the integrity check to perform, null if no integrity check is to be performed
     * @param retry the times for retrying if downloading fails.
     */
    public FileDownloadTask(URL url, File file, IntegrityCheck integrityCheck, int retry) {
        this(Collections.singletonList(url), file, integrityCheck, retry);
    }

    /**
     * Constructor.
     * @param urls urls of remote file, will be attempted in order.
     * @param file the location that download to.
     */
    public FileDownloadTask(List<URL> urls, File file) {
        this(urls, file, null);
    }

    /**
     * Constructor.
     * @param urls urls of remote file, will be attempted in order.
     * @param file the location that download to.
     * @param integrityCheck the integrity check to perform, null if no integrity check is to be performed
     */
    public FileDownloadTask(List<URL> urls, File file, IntegrityCheck integrityCheck) {
        this(urls, file, integrityCheck, 3);
    }

    /**
     * Constructor.
     * @param urls urls of remote file, will be attempted in order.
     * @param file the location that download to.
     * @param integrityCheck the integrity check to perform, null if no integrity check is to be performed
     * @param retry the times for retrying if downloading fails.
     */
    public FileDownloadTask(List<URL> urls, File file, IntegrityCheck integrityCheck, int retry) {
        super(urls, retry);
        this.file = file;
        this.integrityCheck = integrityCheck;

        setName(file.getName());
    }

    public File getFile() {
        return file;
    }

    public FileDownloadTask setCandidate(Path candidate) {
        this.candidate = candidate;
        return this;
    }

    public void addIntegrityCheckHandler(IntegrityCheckHandler handler) {
        integrityCheckHandlers.add(Objects.requireNonNull(handler));
    }

    @Override
    protected EnumCheckETag shouldCheckETag() {
        // Check cache
        if (integrityCheck != null && caching) {
            Optional<Path> cache = repository.checkExistentFile(candidate, integrityCheck.getAlgorithm(), integrityCheck.getChecksum());
            if (cache.isPresent()) {
                try {
                    FileUtils.copyFile(cache.get().toFile(), file);
                    Logging.LOG.log(Level.FINER, "Successfully verified file " + file + " from " + urls.get(0));
                    return EnumCheckETag.CACHED;
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Failed to copy cache files", e);
                }
            }
            return EnumCheckETag.NOT_CHECK_E_TAG;
        } else {
            return EnumCheckETag.CHECK_E_TAG;
        }
    }

    @Override
    protected void beforeDownload(URL url) {
        Logging.LOG.log(Level.FINER, "Downloading " + url + " to " + file);
    }

    @Override
    protected void useCachedResult(Path cache) throws IOException {
        FileUtils.copyFile(cache.toFile(), file);
    }

    @Override
    protected Context getContext(URLConnection conn, boolean checkETag, @Nullable String bmclapiHash) throws IOException {
        Path temp = Files.createTempFile(null, null);
        tempFile = temp;
        RandomAccessFile rFile = new RandomAccessFile(temp.toFile(), "rw");
        MessageDigest digest = integrityCheck == null ? null : integrityCheck.createDigest();

        return new Context() {
            @Override
            public void write(byte[] buffer, int offset, int len) throws IOException {
                if (digest != null) {
                    digest.update(buffer, offset, len);
                }

                rFile.write(buffer, offset, len);
            }

            @Override
            public void close() throws IOException {
                try {
                    rFile.close();
                } catch (IOException e) {
                    Logging.LOG.log(Level.WARNING, "Failed to close file: " + rFile, e);
                }

                if (!isSuccess()) {
                    try {
                        Files.delete(temp);
                    } catch (IOException e) {
                        Logging.LOG.log(Level.WARNING, "Failed to delete file: " + rFile, e);
                    }
                    return;
                }

                for (IntegrityCheckHandler handler : integrityCheckHandlers) {
                    handler.checkIntegrity(temp, file.toPath());
                }

                Files.deleteIfExists(file.toPath());
                if (!FileUtils.makeDirectory(file.getAbsoluteFile().getParentFile()))
                    throw new IOException("Unable to make parent directory " + file);

                try {
                    FileUtils.moveFile(temp.toFile(), file);
                } catch (Exception e) {
                    throw new IOException("Unable to move temp file from " + temp + " to " + file, e);
                }

                // Integrity check
                if (integrityCheck != null) {
                    integrityCheck.performCheck(digest);
                } else if (bmclapiHash != null) {
                    // 没有显式校验值时以 BMCLAPI 哈希兜底，防止节点切换/重试后落下损坏的文件
                    String actualHash = digestToString("SHA-1", temp);
                    if (!bmclapiHash.equalsIgnoreCase(actualHash))
                        throw new ChecksumMismatchException("SHA-1", bmclapiHash, actualHash);
                }

                if (caching && integrityCheck != null) {
                    try {
                        repository.cacheFile(file.toPath(), integrityCheck.getAlgorithm(), integrityCheck.getChecksum());
                    } catch (IOException e) {
                        Logging.LOG.log(Level.WARNING, "Failed to cache file", e);
                    }
                } else if (caching && bmclapiHash != null) {
                    try {
                        repository.cacheFile(file.toPath(), "SHA-1", bmclapiHash);
                    } catch (IOException e) {
                        Logging.LOG.log(Level.WARNING, "Failed to cache file", e);
                    }
                }

                if (checkETag) {
                    repository.cacheRemoteFile(file.toPath(), conn);
                }
            }
        };
    }

    /** 并行续传：单个分块的最小大小 */
    private static final long MIN_CHUNK_SIZE = 2L * 1024 * 1024;
    /** 并行续传：最大连接数 */
    private static final int MAX_CHUNK_CONNECTIONS = 8;
    /**
     * 并行续传：分块连接的读超时。按连接限速的服务器（网盘直链等）数据呈间歇性滴流，
     * 常规 10s 读超时会掐死尚在正常工作的分块连接，这里放宽到 30s。
     */
    private static final int CHUNK_READ_TIMEOUT = 30_000;

    /**
     * 单连接持续低速时的并行续传：把剩余区间均分为多个分块，用独立连接并行拉取，
     * 直接写入已含前缀数据的同一临时文件，完成后交回框架统一落盘与校验。
     * 按连接数限速的服务器（如网盘直链的 x-amz-limitrate）可借此成倍提速。
     * 分块失败按轮次重试且只补未完成区间、从已写偏移续传，全局进度严格单调，
     * 避免进度条反复跑满又清零重来。
     */
    @Override
    protected boolean downloadParallelRemainder(URL url, long downloaded, long contentLength, @Nullable String bmclapiHash) throws IOException {
        long remaining = contentLength - downloaded;
        int chunkCount = (int) Math.min(MAX_CHUNK_CONNECTIONS, (remaining + MIN_CHUNK_SIZE - 1) / MIN_CHUNK_SIZE);
        if (chunkCount <= 1)
            return false;

        // 以 1 字节 Range 请求确认服务端确实支持分块，避免切断单流后续传失败
        if (!probeRangeSupport(url, contentLength))
            return false;

        // tempFile 由默认 getContext 创建；若子类自建 Context 无法定位临时文件，放弃并行
        if (tempFile == null)
            return false;
        Path temp = tempFile;

        Logging.LOG.log(Level.FINER, "Downloading " + url + " switched to " + chunkCount + " connections for remaining " + remaining + " bytes");

        List<ChunkState> chunks = new ArrayList<>(chunkCount);
        for (int i = 0; i < chunkCount; i++) {
            long start = downloaded + remaining * i / chunkCount;
            long end = downloaded + remaining * (i + 1) / chunkCount - 1;
            chunks.add(new ChunkState(start, end));
        }

        ExecutorService pool = Lang.threadPool("DownloadChunk", true, chunkCount, 10, TimeUnit.SECONDS);
        try (FileChannel channel = FileChannel.open(temp, StandardOpenOption.WRITE)) {
            IOException failure = null;
            int round = 0;
            while (true) {
                List<Future<Void>> futures = new ArrayList<>();
                for (ChunkState chunk : chunks)
                    if (chunk.pos.get() <= chunk.end)
                        futures.add(pool.submit(new ChunkDownloader(channel, url, chunk, contentLength, downloaded, chunks, bmclapiHash)));
                try {
                    for (Future<Void> future : futures) {
                        try {
                            future.get();
                        } catch (ExecutionException e) {
                            Throwable cause = e.getCause();
                            if (cause instanceof CancellationException)
                                throw (CancellationException) cause;
                            if (cause instanceof IOException && failure == null)
                                failure = (IOException) cause;
                        }
                    }
                } finally {
                    // 本轮结束后再进入下一轮，此处仅确保不再有分块在写盘
                    for (Future<Void> future : futures)
                        future.cancel(true);
                }

                if (isCancelled())
                    throw new CancellationException();
                if (failure == null)
                    break;

                round++;
                if (round >= retry)
                    throw new IOException("Parallel chunk download failed after " + retry + " rounds", failure);
                Logging.LOG.log(Level.WARNING, "Retrying incomplete chunks of " + url + ", round " + (round + 1), failure);
                failure = null;
                awaitRetryDelay(round - 1);
                if (isCancelled())
                    throw new CancellationException();
            }

            long size = Files.size(temp);
            if (size != contentLength)
                throw new IOException("Unexpected file size after parallel download: " + size + ", expected: " + contentLength);
            setDownloadedBytes(contentLength);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted during parallel download", e);
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 用 1 字节 Range 请求探测服务端分块支持，并核对文件总大小未变化。
     */
    private boolean probeRangeSupport(URL url, long expectedTotal) {
        try {
            HttpURLConnection conn = NetworkUtils.createHttpConnection(url);
            conn.setRequestProperty("Range", "bytes=0-0");
            conn = NetworkUtils.resolveConnection(conn);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_PARTIAL)
                return false;
            try (InputStream ignored = conn.getInputStream()) {
                return parseContentRangeTotal(conn.getHeaderField("Content-Range")) == expectedTotal;
            }
        } catch (IOException e) {
            Logging.LOG.log(Level.WARNING, "Failed to probe range support of " + url, e);
            return false;
        }
    }

    /** 解析 Content-Range 响应头（形如 "bytes 0-1023/15263656"）中的总大小，格式异常返回 -1 */
    static long parseContentRangeTotal(@Nullable String contentRange) {
        if (contentRange == null)
            return -1;
        int slash = contentRange.lastIndexOf('/');
        if (slash < 0)
            return -1;
        try {
            return Long.parseLong(contentRange.substring(slash + 1).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 单个分块的状态：区间固定，pos 为已写入的下一绝对偏移。
     * 跨重试轮次持久，续传请求从已写位置继续，进度据此求和，保证严格单调。
     */
    private static final class ChunkState {
        final long start;
        final long end;
        final AtomicLong pos;

        ChunkState(long start, long end) {
            this.start = start;
            this.end = end;
            this.pos = new AtomicLong(start);
        }

        long done() {
            return pos.get() - start;
        }
    }

    /**
     * 单个分块的单次下载尝试：从已写偏移续传 [start, end] 区间，按绝对偏移写入共享临时文件。
     * 重试由外层按轮次统一调度；BMCLAPI 链路同时启用慢速节点切换。
     */
    private final class ChunkDownloader implements Callable<Void> {
        private final FileChannel channel;
        private final URL url;
        private final ChunkState chunk;
        private final long contentLength;
        /** 整个剩余区间的起点，用于换算全局进度 */
        private final long chunkBase;
        private final List<ChunkState> chunks;
        @Nullable
        private final String bmclapiHash;

        ChunkDownloader(FileChannel channel, URL url, ChunkState chunk, long contentLength,
                        long chunkBase, List<ChunkState> chunks, @Nullable String bmclapiHash) {
            this.channel = channel;
            this.url = url;
            this.chunk = chunk;
            this.contentLength = contentLength;
            this.chunkBase = chunkBase;
            this.chunks = chunks;
            this.bmclapiHash = bmclapiHash;
        }

        @Override
        public Void call() throws IOException {
            long from = chunk.pos.get();
            HttpURLConnection conn = NetworkUtils.createHttpConnection(url);
            conn.setRequestProperty("Range", "bytes=" + from + "-" + chunk.end);
            conn = NetworkUtils.resolveConnection(conn);
            // resolveConnection 每跳都会重置为常规超时，必须在解析完重定向后再放宽读超时
            conn.setReadTimeout(CHUNK_READ_TIMEOUT);
            int responseCode = conn.getResponseCode();
            if (responseCode / 100 == 4)
                throw new FileNotFoundException(url.toString());
            if (responseCode != HttpURLConnection.HTTP_PARTIAL)
                throw new ResponseCodeException(url, responseCode);

            // 校验服务端返回的区间：起点与总大小必须一致；终点被服务端合并扩大时无害，
            // 读取时以请求终点为上限截断
            String contentRange = conn.getHeaderField("Content-Range");
            if (contentRange == null || !contentRange.startsWith("bytes " + from + "-")
                    || parseContentRangeTotal(contentRange) != contentLength)
                throw new IOException("Unexpected Content-Range for chunk " + from + "-" + chunk.end + ": " + contentRange);

            FetchTask.BMCLAPIRetryPolicy retryPolicy = bmclapiHash == null ? null : new FetchTask.BMCLAPIRetryPolicy();
            long pos = from;
            try (InputStream stream = conn.getInputStream()) {
                if (retryPolicy != null)
                    retryPolicy.startAttempt(System.nanoTime());
                byte[] buffer = new byte[IOUtils.DEFAULT_BUFFER_SIZE];
                while (pos <= chunk.end) {
                    if (isCancelled())
                        throw new CancellationException();
                    int len = stream.read(buffer, 0, (int) Math.min(buffer.length, chunk.end - pos + 1));
                    if (len == -1)
                        break;

                    ByteBuffer slice = ByteBuffer.wrap(buffer, 0, len);
                    while (slice.hasRemaining())
                        pos += channel.write(slice, pos);
                    chunk.pos.set(pos);
                    reportProgress();
                    updateDownloadSpeed(len);

                    if (retryPolicy != null && retryPolicy.shouldRetry(pos - from, chunk.end - from + 1, System.nanoTime()))
                        throw new SocketTimeoutException("BMCLAPI node is too slow to complete the chunk in time");
                }
            }
            if (pos != chunk.end + 1)
                throw new IOException("Chunk " + from + "-" + chunk.end + " incomplete: " + (pos - from) + "/" + (chunk.end - from + 1));
            return null;
        }

        /** 全局进度 = 单流前缀 + 各分块实际已写位置之和；各分块只在向前推进时更新，不会回退 */
        private void reportProgress() {
            long sum = 0;
            for (ChunkState state : chunks)
                sum += state.done();
            long overall = Math.min(chunkBase + sum, contentLength);
            setDownloadedBytes(overall);
            updateProgress(overall, contentLength);
        }
    }

    public interface IntegrityCheckHandler {
        /**
         * Check whether the file is corrupted or not.
         * @param filePath the file locates in (maybe in temp directory)
         * @param destinationPath for real file name
         * @throws IOException if the file is corrupted
         */
        void checkIntegrity(Path filePath, Path destinationPath) throws IOException;
    }

    public static final IntegrityCheckHandler ZIP_INTEGRITY_CHECK_HANDLER = (filePath, destinationPath) -> {
        String ext = FileUtils.getExtension(destinationPath).toLowerCase(Locale.ROOT);
        if (ext.equals("zip") || ext.equals("jar")) {
            try (FileSystem ignored = CompressingUtils.createReadOnlyZipFileSystem(filePath)) {
                // test for zip format
            }
        }
    };
}
