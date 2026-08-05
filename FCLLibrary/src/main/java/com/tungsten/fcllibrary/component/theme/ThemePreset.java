package com.tungsten.fcllibrary.component.theme;

import android.graphics.Color;

/**
 * 主题预设，提供一键切换主题配色方案。
 */
public enum ThemePreset {

    DEFAULT("Default", Color.parseColor("#7797CF"), Color.parseColor("#000000"), Color.parseColor("#FFFFFF")),

    LIQUID_GLASS("Liquid Glass", Color.parseColor("#0088FF"), Color.parseColor("#D6EAFA"), Color.parseColor("#1A5C9E")),

    SAKURA_PINK("Sakura Pink", Color.parseColor("#FFB7C5"), Color.parseColor("#FFE4E9"), Color.parseColor("#F0C0CC")),

    MINT_GREEN("Mint Green", Color.parseColor("#7ECB9A"), Color.parseColor("#D4F0DF"), Color.parseColor("#B8DCC8")),

    LAVENDER("Lavender", Color.parseColor("#B39DDB"), Color.parseColor("#E8E0F5"), Color.parseColor("#D1C4E9")),

    SUNSET_ORANGE("Sunset Orange", Color.parseColor("#FFAB76"), Color.parseColor("#FFE0CC"), Color.parseColor("#F0C8A8"));

    private final String name;
    private final int color;
    private final int color2;
    private final int color2Dark;

    ThemePreset(String name, int color, int color2, int color2Dark) {
        this.name = name;
        this.color = color;
        this.color2 = color2;
        this.color2Dark = color2Dark;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor2Dark() {
        return color2Dark;
    }
}