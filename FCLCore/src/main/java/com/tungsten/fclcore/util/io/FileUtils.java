package com.tungsten.fclcore.util.io;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.tungsten.fclcore.util.Lang;
import com.tungsten.fclcore.util.StringUtils;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getNameWithoutExtension(String fileName) {
        return StringUtils.substringBeforeLast(fileName, '.');
    }

    public static String getNameWithoutExtension(File file) {
        return StringUtils.substringBeforeLast(file.getName(), '.');
    }

    public static String getExtension(File file) {
        return StringUtils.substringAfterLast(file.getName(), '.');
    }

    public static String normalizePath(String path) {
        return StringUtils.addPrefix(StringUtils.removeSuffix(path, "/", "\\"), "/");
    }

    public static String getName(String path) {
        return StringUtils.removeSuffix(new File(path).getName(), "/", "\\");
    }

    public static String readText(String file) throws IOException {
        return readText(new File(file), UTF_8);
    }

    public static String readText(File file) throws IOException {
        return readText(file, UTF_8);
    }

    public static String readText(File file, Charset charset) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte [inputStream.available()];
        inputStream.read(bytes);
        inputStream.close();
        return new String(bytes, charset);
    }

    public static void writeText(String file, String text) throws IOException {
        writeBytes(new File(file), text);
    }

    public static void writeText(File file, String text) throws IOException {
        writeBytes(file, text);
    }

    public static void writeBytes(File file, String text) throws IOException {
        String parent = file.getParent();
        makeDirectory(parent);
        makeFile(file.getAbsolutePath());
        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
        fileWriter.write(text);
        fileWriter.close();
    }

    public static void deleteDirectory(String directory) throws IOException {
        if (!new File(directory).exists())
            return;

        cleanDirectory(directory);

        if (!new File(directory).delete()) {
            String message = "Unable to delete directory " + directory + ".";

            throw new IOException(message);
        }
    }

    public static boolean deleteDirectoryQuietly(String directory) {
        try {
            deleteDirectory(directory);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean copyDirectory(String src, String dest) throws IOException {
        File srcDir = new File(src);
        File destDir = new File(dest);
        if (!srcDir.isDirectory()) {
            return false;
        }
        if (!destDir.isDirectory() && !destDir.mkdirs()) {
            return false;
        }
        File[] files = srcDir.listFiles();
        if (files == null) {
            return true;
        }
        for (File file : files) {
            File destFile = new File(destDir, file.getName());
            if (file.isFile()) {
                if (!copyFile(file.getAbsolutePath(), destFile.getAbsolutePath())) {
                    return false;
                }
            }
            else if (file.isDirectory()) {
                if (!copyDirectory(file.getAbsolutePath(), destFile.getAbsolutePath())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean copyDirectoryQuietly(String src, String dest) {
        try {
            copyDirectory(src, dest);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void cleanDirectory(String directory) throws IOException {
        if (!new File(directory).exists()) {
            if (!makeDirectory(directory))
                throw new IOException("Failed to create directory: " + directory);
            return;
        }

        if (!new File(directory).isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = new File(directory).listFiles();
        if (files == null)
            throw new IOException("Failed to list contents of " + directory);

        IOException exception = null;
        for (File file : files)
            try {
                forceDelete(file.getAbsolutePath());
            } catch (IOException ioe) {
                exception = ioe;
            }

        if (null != exception)
            throw exception;
    }

    public static boolean cleanDirectoryQuietly(String directory) {
        try {
            cleanDirectory(directory);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void forceDelete(String file) throws IOException {
        if (new File(file).isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = new File(file).exists();
            if (!new File(file).delete()) {
                if (!filePresent)
                    throw new FileNotFoundException("File does not exist: " + file);
                throw new IOException("Unable to delete file: " + file);
            }
        }
    }

    public static boolean copyFile(String srcFile, String destFile) throws IOException {
        Objects.requireNonNull(srcFile, "Source must not be null");
        Objects.requireNonNull(destFile, "Destination must not be null");
        File src = new File(srcFile);
        File dest = new File(destFile);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(src));
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
        byte[] flush = new byte[1024];
        int len;
        while ((len = inputStream.read(flush)) != -1) {
            outputStream.write(flush,0,len);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return true;
    }

    public static boolean copyFileQuietly(String srcFile, String destFile) {
        try {
            copyFile(srcFile, destFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean moveFile(String srcFile, String destFile) throws IOException {
        boolean copy = copyFile(srcFile, destFile);
        boolean delete = new File(srcFile).delete();
        return copy && delete;
    }

    public static boolean makeDirectory(String directory) {
        new File(directory).mkdirs();
        return new File(directory).isDirectory();
    }

    public static boolean makeFile(String file) {
        return makeDirectory(new File(file).getParent()) && (new File(file).exists() || Lang.test(new File(file)::createNewFile));
    }

    public static boolean rename(String path, String newName) {
        File file = new File(path);
        String newPath = path.substring(0, path.lastIndexOf("/") + 1) + newName;
        File newFile = new File(newPath);
        return file.renameTo(newFile);
    }

    public static List<File> listFilesByExtension(String file, String extension) {
        List<File> result = new ArrayList<>();
        File[] files = new File(file).listFiles();
        if (files != null)
            for (File it : files)
                if (extension.equals(getExtension(it)))
                    result.add(it);
        return result;
    }
}
