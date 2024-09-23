package com.tungsten.fcl.control.download;

import java.util.ArrayList;

public class ControllerIndex {

    private final String id;
    private final String lang;
    private final String name;
    private final String introduction;
    private final ArrayList<Integer> device;
    private final ArrayList<Integer> categories;

    public ControllerIndex(String id, String lang, String name, String introduction, ArrayList<Integer> device, ArrayList<Integer> categories) {
        this.id = id;
        this.lang = lang;
        this.name = name;
        this.introduction = introduction;
        this.device = device;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public ArrayList<Integer> getDevice() {
        return device;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }
}
