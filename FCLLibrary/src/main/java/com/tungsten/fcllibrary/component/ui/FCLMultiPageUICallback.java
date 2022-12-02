package com.tungsten.fcllibrary.component.ui;

import java.util.ArrayList;

public interface FCLMultiPageUICallback {
    void initPages();
    ArrayList<FCLBasePage> getAllPages();
    FCLBasePage getPage(int id);
}
