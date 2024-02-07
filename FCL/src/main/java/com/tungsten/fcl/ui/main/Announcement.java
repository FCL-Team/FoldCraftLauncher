package com.tungsten.fcl.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;

public class Announcement {

    private final int id;
    private final boolean significant;
    private final String specificLang;
    private final ArrayList<Content> title;
    private final String date;
    private final ArrayList<Content> content;

    public Announcement(int id, boolean significant, String specificLang, ArrayList<Content> title, String date, ArrayList<Content> content) {
        this.id = id;
        this.significant = significant;
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

    public String getSpecificLang() {
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
        if (title.size() == 0) {
            throw new IllegalStateException("No title list!");
        }
        for (Content c : title) {
            if (c.getLang().equals(LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString())) {
                return c.getText();
            }
        }
        return title.get(0).getText();
    }

    public String getDisplayContent(Context context) {
        if (content.size() == 0) {
            throw new IllegalStateException("No content list!");
        }
        for (Content c : content) {
            if (c.getLang().equals(LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString())) {
                return c.getText();
            }
        }
        return content.get(0).getText();
    }

    public boolean shouldDisplay(Context context) {
        if (!specificLang.equals("") && !specificLang.equals(LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString()))
            return false;
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
