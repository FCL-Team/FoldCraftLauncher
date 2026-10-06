package com.tungsten.fcllibrary.browser;

import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.browser.options.LibMode;
import com.tungsten.fcllibrary.browser.options.SortMode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.DirectoryFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

public class FileOperator {

    /**
     * 列出目录内容：目录恒排前，组内按指定方式排序；可选择过滤隐藏文件与后缀
     */
    public static List<File> getFileList(Path path, FileBrowser fileBrowser, SortMode sortMode, boolean showHidden) {
        List<File> list = new ArrayList<>();
        File[] files = path.toFile().listFiles();
        List<File> rawList = files != null ? new ArrayList<>(Arrays.asList(files)) : new ArrayList<>();
        if (!showHidden) {
            rawList.removeIf(file -> file.getName().startsWith("."));
        }
        rawList.sort(getComparator(sortMode));
        rawList.sort(DirectoryFileComparator.DIRECTORY_COMPARATOR);
        List<File> filterList = new ArrayList<>();
        if (!fileBrowser.getSuffix().isEmpty()) {
            for (File file : rawList) {
                if (file.isFile()) {
                    for (String suffix : fileBrowser.getSuffix()) {
                        if (file.getName().endsWith(suffix)) {
                            filterList.add(file);
                            break;
                        }
                    }
                } else {
                    filterList.add(file);
                }
            }
        } else {
            filterList.addAll(rawList);
        }
        if (fileBrowser.getLibMode() == LibMode.FOLDER_CHOOSER) {
            for (File file : rawList) {
                if (file.isDirectory()) {
                    list.add(file);
                }
            }
        } else {
            list.addAll(filterList);
        }
        return list;
    }

    private static Comparator<File> getComparator(SortMode sortMode) {
        if (sortMode == null) {
            return NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
        }
        return switch (sortMode) {
            case SIZE -> Comparator.comparingLong(File::length);
            case DATE -> Comparator.comparingLong(File::lastModified).reversed();
            default -> NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
        };
    }

    /**
     * 校验文件名是否可用于新建或重命名：非空且不含路径分隔符
     */
    public static boolean isValidFileName(String name) {
        return name != null && !name.trim().isEmpty()
                && !name.contains(File.separator) && !name.contains("/");
    }

    /**
     * 为目录中不重名的目标名生成候选名：重名时保留扩展名并追加 " (n)" 序号
     */
    public static String resolveConflict(File dir, String name) {
        String base = name;
        String ext = "";
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            base = name.substring(0, dot);
            ext = name.substring(dot);
        }
        String candidate = name;
        int index = 0;
        while (new File(dir, candidate).exists()) {
            index++;
            candidate = base + " (" + index + ")" + ext;
        }
        return candidate;
    }

    public static void createDirectory(File parent, String name) throws IOException {
        File dir = new File(parent, name);
        if (dir.exists()) {
            throw new IOException("Already exists: " + dir);
        }
        if (!dir.mkdir() && !dir.isDirectory()) {
            throw new IOException("Failed to create directory: " + dir);
        }
    }

    public static void createFile(File parent, String name) throws IOException {
        File file = new File(parent, name);
        if (file.exists()) {
            throw new IOException("Already exists: " + file);
        }
        if (!file.createNewFile() && !file.isFile()) {
            throw new IOException("Failed to create file: " + file);
        }
    }

    public static void rename(File file, String newName) throws IOException {
        File target = new File(file.getParentFile(), newName);
        if (target.exists()) {
            throw new IOException("Already exists: " + target);
        }
        Files.move(file.toPath(), target.toPath());
    }

    public static void delete(File file) throws IOException {
        FileUtils.forceDelete(file);
    }

    /**
     * 复制文件/目录到目标目录，重名自动追加序号，返回成功数量
     */
    public static int copyTo(List<File> files, File targetDir) {
        int count = 0;
        for (File file : files) {
            try {
                File target = new File(targetDir, resolveConflict(targetDir, file.getName()));
                if (file.isDirectory()) {
                    FileUtils.copyDirectory(file, target);
                } else {
                    FileUtils.copyFile(file, target);
                }
                count++;
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Failed to copy file: " + file, e);
            }
        }
        return count;
    }

    /**
     * 移动文件/目录到目标目录，重名自动追加序号，返回成功数量
     */
    public static int moveTo(List<File> files, File targetDir) {
        int count = 0;
        for (File file : files) {
            try {
                File target = new File(targetDir, resolveConflict(targetDir, file.getName()));
                if (file.isDirectory()) {
                    FileUtils.moveDirectory(file, target);
                } else {
                    FileUtils.moveFile(file, target);
                }
                count++;
            } catch (IOException e) {
                Logging.LOG.log(Level.WARNING, "Failed to move file: " + file, e);
            }
        }
        return count;
    }

}
