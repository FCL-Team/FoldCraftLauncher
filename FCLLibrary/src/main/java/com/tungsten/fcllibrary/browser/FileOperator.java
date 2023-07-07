package com.tungsten.fcllibrary.browser;

import com.tungsten.fcllibrary.browser.options.LibMode;

import org.apache.commons.io.comparator.DirectoryFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileOperator {

    public static List<File> getFileList(Path path, FileBrowser fileBrowser) {
        List<File> list = new ArrayList<>();
        File[] files = path.toFile().listFiles();
        List<File> rawList;
        if (files != null) {
            rawList = Arrays.asList(files);
        } else {
            rawList = Collections.emptyList();
        }
        Comparator<File> nameComparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
        Comparator<File> dirComparator = DirectoryFileComparator.DIRECTORY_COMPARATOR;
        rawList.sort(nameComparator);
        rawList.sort(dirComparator);
        List<File> filterList = new ArrayList<>();
        if (fileBrowser.getSuffix().size() > 0) {
            for (File file : rawList) {
                boolean add = false;
                if (file.isFile()) {
                    for (String suffix : fileBrowser.getSuffix()) {
                        add = file.getAbsolutePath().endsWith(suffix);
                        break;
                    }
                } else {
                    add = true;
                }
                if (add) {
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

}
