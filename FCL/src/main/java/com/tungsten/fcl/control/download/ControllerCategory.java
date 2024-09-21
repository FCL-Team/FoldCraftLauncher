package com.tungsten.fcl.control.download;

import android.content.Context;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.util.LocaleUtils;

import java.util.ArrayList;

public class ControllerCategory {

    private final int id;
    private final ArrayList<Lang> lang;

    public ControllerCategory(int id, ArrayList<Lang> lang) {
        this.id = id;
        this.lang = lang;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Lang> getLang() {
        return lang;
    }

    public String getText(Context context) {
        if (getId() == 0 && getLang() == null) {
            return context.getString(R.string.curse_category_0);
        } else if (getLang() == null || getLang().isEmpty()) {
            return "Unknown category";
        } else {
            for (Lang l : getLang()) {
                if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(l.getLocale())) {
                    return l.getText();
                }
            }
            return getLang().get(0).getText();
        }
    }

    public static class Lang {

        private final String locale;
        private final String text;

        public Lang(String locale, String text) {
            this.locale = locale;
            this.text = text;
        }

        public String getLocale() {
            return locale;
        }

        public String getText() {
            return text;
        }
    }

    public static ArrayList<String> getLocaledCategories(Context context, ArrayList<ControllerCategory> categories, ArrayList<Integer> ids) {
        ArrayList<String> target = new ArrayList<>();
        for (int id : ids) {
            ControllerCategory category = categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (category != null) {
                boolean hasTrans = false;
                if (category.getLang() == null)
                    continue;
                for (Lang l : category.getLang()) {
                    if (LocaleUtils.getLocale(LocaleUtils.getLanguage(context)).toString().contains(l.getLocale())) {
                        target.add(l.getText());
                        hasTrans = true;
                        break;
                    }
                }
                if (!hasTrans && !category.getLang().isEmpty()) {
                    target.add(category.getLang().get(0).getText());
                }
            }
        }
        return target;
    }

}
