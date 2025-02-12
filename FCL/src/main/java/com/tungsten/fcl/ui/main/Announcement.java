package com.tungsten.fcl.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.tungsten.fcl.upgrade.UpdateChecker;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;

/**
 * @author Tungsten
 *
 * Announcement v2.
 */
public class Announcement {

    private final int id;
    private final boolean significant;
    private final boolean outdated;
    private final int minVersion;
    private final int maxVersion;
    private final ArrayList<String> specificLang;
    private final ArrayList<Content> title;
    private final String date;
    private final ArrayList<Content> content;

    public Announcement(int id, boolean significant, boolean outdated, int minVersion, int maxVersion, ArrayList<String> specificLang, ArrayList<Content> title, String date, ArrayList<Content> content) {
        this.id = id;
        this.significant = significant;
        this.outdated = outdated;
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.specificLang = specificLang;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public boolean isSignificant() {
        return significant;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public int getMinVersion() {
        return minVersion;
    }

    public int getMaxVersion() {
        return maxVersion;
    }

    public ArrayList<String> getSpecificLang() {
        return specificLang;
    }

    public ArrayList<Content> getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    public String getDisplayTitle(Context context) {
        if (title.isEmpty()) {
            throw new IllegalStateException("No title list!");
        }
        for (Content c : title) {
            if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(c.getLang())) {
                return c.getText();
            }
        }
        return title.get(0).getText();
    }

    public String getDisplayContent(Context context) {
        if (content.isEmpty()) {
            throw new IllegalStateException("No content list!");
        }
        for (Content c : content) {
            if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(c.getLang())) {
                return c.getText();
            }
        }
        return content.get(0).getText();
    }

    public boolean shouldDisplay(Context context) {
        if (outdated)
            return false;
        if (minVersion != -1 && minVersion > UpdateChecker.getCurrentVersionCode(context))
            return false;
        if (maxVersion != -1 && maxVersion < UpdateChecker.getCurrentVersionCode(context))
            return false;
        if (!specificLang.isEmpty()) {
            boolean cancel = true;
            for (String lang : specificLang) {
                if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(lang)) {
                    cancel = false;
                    break;
                }
            }
            if (cancel)
                return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ignore_announcement", 0) < id;
    }

    public void hide(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("launcher", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("ignore_announcement", id);
        editor.apply();
    }

    public final static class Content {

        private final String lang;
        private final String text;

        public Content(String lang, String text) {
            this.lang = lang;
            this.text = text;
        }

        public String getLang() {
            return lang;
        }

        public String getText() {
            return text;
        }

    }

}
