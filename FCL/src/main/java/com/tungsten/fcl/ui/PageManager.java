package com.tungsten.fcl.ui;

import android.content.Context;

import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;

public abstract class PageManager {

    private final Context context;
    private final FCLUILayout parent;
    private final int defaultPageId;

    private final ArrayList<FCLCommonPage> allPages;
    private FCLCommonPage currentPage;

    private final ArrayList<FCLTempPage> allTempPages = new ArrayList<>();
    private FCLTempPage currentTempPage;

    public PageManager (Context context, FCLUILayout parent, int defaultPageId, UIListener listener) {
        this.context = context;
        this.parent = parent;
        this.defaultPageId = defaultPageId;

        init(listener);
        allPages = getAllPages();
        if (allPages.size() > 0) {
            switchPage(defaultPageId);
        }
    }

    public Context getContext() {
        return context;
    }

    public FCLUILayout getParent() {
        return parent;
    }

    public int getDefaultPageId() {
        return defaultPageId;
    }

    public FCLCommonPage getCurrentPage() {
        return currentPage;
    }

    public ArrayList<FCLTempPage> getAllTempPages() {
        return allTempPages;
    }

    public FCLTempPage getCurrentTempPage() {
        return currentTempPage;
    }

    public abstract void init(UIListener listener);

    public abstract ArrayList<FCLCommonPage> getAllPages();

    public FCLCommonPage getPageById(int id) {
        for (FCLCommonPage page : allPages) {
            if (page.getId() == id) {
                return page;
            }
        }
        return null;
    }

    public void switchPage(int id) {
        if (allPages.size() > 0) {
            FCLCommonPage targetPage = getPageById(id);
            if (targetPage == null) {
                throw new IllegalStateException("Wrong page id, this should not happen!");
            }
            if (currentTempPage != null) {
                currentTempPage.onStop();
            }
            allTempPages.clear();
            if (currentPage != null && currentPage != targetPage && currentPage.isShowing()) {
                currentPage.onStop();
            }
            targetPage.onStart();
            currentPage = targetPage;
        }
    }

    public void showTempPage(FCLTempPage fclTempPage) {
        if (currentPage != null && currentPage.isShowing()) {
            currentPage.onStop();
        }
        if (allTempPages.size() > 0 && allTempPages.get(allTempPages.size() - 1) != null && allTempPages.get(allTempPages.size() - 1).isShowing()) {
            allTempPages.get(allTempPages.size() - 1).onStop();
        }
        fclTempPage.onStart();
        allTempPages.add(fclTempPage);
        currentTempPage = fclTempPage;
    }

    public void dismissCurrentTempPage() {
        if (currentTempPage != null) {
            currentTempPage.dismiss();
            allTempPages.remove(allTempPages.size() - 1);
            if (allTempPages.size() > 0) {
                allTempPages.get(allTempPages.size() - 1).onRestart();
                currentTempPage = allTempPages.get(allTempPages.size() - 1);
            } else {
                if (currentPage != null && !currentPage.isShowing()) {
                    currentPage.onStart();
                }
                currentTempPage = null;
            }
        }
    }

    public void onPause() {
        for (FCLCommonPage page : allPages) {
            page.onPause();
        }
        for (FCLTempPage page : allTempPages) {
            page.onPause();
        }
    }

    public void onResume() {
        for (FCLCommonPage page : allPages) {
            page.onResume();
        }
        for (FCLTempPage page : allTempPages) {
            page.onResume();
        }
    }

}
