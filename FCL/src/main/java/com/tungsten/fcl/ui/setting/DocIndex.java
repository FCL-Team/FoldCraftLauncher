package com.tungsten.fcl.ui.setting;

import android.content.Context;

import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;

public class DocIndex {

    private final String category;
    private final boolean visible;
    private final ArrayList<DisplayName> displayName;
    private final ArrayList<Item> item;

    public DocIndex(String category, boolean visible, ArrayList<DisplayName> displayName, ArrayList<Item> item) {
        this.category = category;
        this.visible = visible;
        this.displayName = displayName;
        this.item = item;
    }

    public String getCategory() {
        return category;
    }

    public boolean isVisible() {
        return visible;
    }

    public ArrayList<DisplayName> getDisplayName() {
        return displayName;
    }

    public ArrayList<Item> getItem() {
        return item;
    }

    public String getDisplayName(Context context) {
        for (DisplayName name : displayName) {
            if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(name.getLang())) {
                return name.getName();
            }
        }
        return category;
    }

    public static class DisplayName {

        private final String lang;
        private final String name;

        public DisplayName(String lang, String name) {
            this.lang = lang;
            this.name = name;
        }

        public String getLang() {
            return lang;
        }

        public String getName() {
            return name;
        }

    }

    public static class Item {

        private final String lang;
        private final String title;
        private final String subtitle;
        private final String author;
        private final String path;

        public Item(String lang, String title, String subtitle, String author, String path) {
            this.lang = lang;
            this.title = title;
            this.subtitle = subtitle;
            this.author = author;
            this.path = path;
        }

        public String getLang() {
            return lang;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getAuthor() {
            return author;
        }

        public String getPath() {
            return path;
        }

        public boolean isVisible(Context context) {
            return LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(lang);
        }

    }

}
