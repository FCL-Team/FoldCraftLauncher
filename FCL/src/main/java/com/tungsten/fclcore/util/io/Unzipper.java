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
package com.tungsten.fclcore.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public final class Unzipper {
    private final Path zipFile, dest;
    private boolean replaceExistentFile = false;
    private boolean terminateIfSubDirectoryNotExists = false;
    private String subDirectory = "/";
    private FileFilter filter = null;
    private Charset encoding = StandardCharsets.UTF_8;
    private ProgressCallback progressCallback = null;

    /**
     * Decompress the given zip file to a directory.
     *
     * @param zipFile the input zip file to be uncompressed
     * @param destDir the dest directory to hold uncompressed files
     */
    public Unzipper(Path zipFile, Path destDir) {
        this.zipFile = zipFile;
        this.dest = destDir;
    }

    /**
     * Decompress the given zip file to a directory.
     *
     * @param zipFile the input zip file to be uncompressed
     * @param destDir the dest directory to hold uncompressed files
     */
    public Unzipper(File zipFile, File destDir) {
        this(zipFile.toPath(), destDir.toPath());
    }

    /**
     * True if replace the existent files in destination directory,
     * otherwise those conflict files will be ignored.
     */
    public Unzipper setReplaceExistentFile(boolean replaceExistentFile) {
        this.replaceExistentFile = replaceExistentFile;
        return this;
    }

    /**
     * Will be called for every entry in the zip file.
     * Callback returns false if you want leave the specific file uncompressed.
     */
    public Unzipper setFilter(FileFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Will only uncompress files in the "subDirectory", their path will be also affected.
     * <p>
     * For example, if you set subDirectory to /META-INF, files in /META-INF/ will be
     * uncompressed to the destination directory without creating META-INF folder.
     * <p>
     * Default value: "/"
     */
    public Unzipper setSubDirectory(String subDirectory) {
        this.subDirectory = FileUtils.normalizePath(subDirectory);
        return this;
    }

    public Unzipper setEncoding(Charset encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * 解压进度回调，可选。调用时机为每个文件条目处理完之后（含被过滤器跳过的）。
     */
    public Unzipper setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
        return this;
    }

    public Unzipper setTerminateIfSubDirectoryNotExists() {
        this.terminateIfSubDirectoryNotExists = true;
        return this;
    }

    /**
     * Decompress the given zip file to a directory.
     *
     * @throws IOException if zip file is malformed or filesystem error.
     */
    public void unzip() throws IOException {
        Files.createDirectories(dest);
        try (FileSystem fs = CompressingUtils.readonly(zipFile).setEncoding(encoding).setAutoDetectEncoding(true).build()) {
            Path root = fs.getPath(subDirectory);
            if (!root.isAbsolute() || (subDirectory.length() > 1 && subDirectory.endsWith("/")))
                throw new IllegalArgumentException("Subdirectory for unzipper must be absolute");

            if (terminateIfSubDirectoryNotExists && Files.notExists(root))
                return;

            // 条目总数通过一次轻量遍历统计（只走目录结构，不解压数据），过滤器不参与统计，
            // 避免带副作用的过滤器（如计算哈希）被重复执行
            long total = progressCallback == null ? 0 : countEntries(root);
            long[] done = {0};
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attrs) throws IOException {
                    String relativePath = root.relativize(file).toString();
                    Path destFile = dest.resolve(relativePath);
                    if (filter == null || filter.accept(file, false, destFile, relativePath)) {
                        try {
                            Files.copy(file, destFile, replaceExistentFile ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
                        } catch (FileAlreadyExistsException e) {
                            if (replaceExistentFile)
                                throw e;
                        }
                    }
                    if (progressCallback != null && total > 0)
                        progressCallback.onUpdate(++done[0], total, relativePath);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) throws IOException {
                    String relativePath = root.relativize(dir).toString();
                    Path dirToCreate = dest.resolve(relativePath);
                    if (filter != null && !filter.accept(dir, true, dirToCreate, relativePath))
                        return FileVisitResult.SKIP_SUBTREE;
                    Files.createDirectories(dirToCreate);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    /**
     * 统计子目录内的文件条目数，只遍历目录结构，不解压数据
     */
    private long countEntries(Path root) throws IOException {
        long[] count = {0};
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                count[0]++;
                return FileVisitResult.CONTINUE;
            }
        });
        return count[0];
    }

    public interface FileFilter {
        boolean accept(Path zipEntry, boolean isDirectory, Path destFile, String entryPath) throws IOException;
    }

    public interface ProgressCallback {
        /**
         * @param done      已处理的文件条目数（含被过滤器跳过的）
         * @param total     文件条目总数
         * @param entryPath 当前条目相对于解压子目录的路径
         */
        void onUpdate(long done, long total, String entryPath);
    }
}