package com.tungsten.fcl.control.download;

import java.util.ArrayList;

public class ControllerVersion {

    private final int screenshot;
    private final String description;
    private final String author;
    private final VersionInfo latest;
    private final ArrayList<VersionInfo> history;

    public ControllerVersion(int screenshot, String description, String author, VersionInfo latest, ArrayList<VersionInfo> history) {
        this.screenshot = screenshot;
        this.description = description;
        this.author = author;
        this.latest = latest;
        this.history = history;
    }

    public int getScreenshot() {
        return screenshot;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public VersionInfo getLatest() {
        return latest;
    }

    public ArrayList<VersionInfo> getHistory() {
        return history;
    }

    public static class VersionInfo {

        private final int versionCode;
        private final String versionName;

        public VersionInfo(int versionCode, String versionName) {
            this.versionCode = versionCode;
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public String getVersionName() {
            return versionName;
        }
    }

}
