package com.tungsten.fcl.ui.manage;

import android.content.Context;

import com.tungsten.fcl.ui.PageManager;
import com.tungsten.fcl.ui.UIListener;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public class ManagePageManager extends PageManager {

    public static final int PAGE_ID_MANAGE_SETTING = 15000;
    public static final int PAGE_ID_MANAGE_INSTALL = 15001;
    public static final int PAGE_ID_MANAGE_MOD = 15002;
    public static final int PAGE_ID_MANAGE_SAVE = 15003;

    private static ManagePageManager instance;

    public static ManagePageManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ManagePageManager not initialized!");
        }
        return instance;
    }

    public ManagePageManager(Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        super(context, parent, defaultPageId, listener);
        instance = this;
    }

    @Override
    public void init(UIListener listener) {

    }

    @Override
    public ArrayList<FCLCommonPage> getAllPages() {
        return null;
    }
}
