package com.tungsten.fcl.ui;

import android.content.Context;

import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.ui.FCLCommonPage;
import com.tungsten.fcllibrary.component.ui.FCLTempPage;
import com.tungsten.fcllibrary.component.view.FCLUILayout;

import java.util.ArrayList;
import java.util.logging.Level;

public abstract class PageManager {

    public static final int PAGE_ID_TEMP = -10000;

    private final Context context;
    private final FCLUILayout parent;
    private final int defaultPageId;

    public final ArrayList<FCLCommonPage> allPages;
    private FCLCommonPage currentPage;

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

    public abstract void init(UIListener listener);

    public abstract ArrayList<FCLCommonPage> getAllPages();

    public FCLCommonPage createPageById(int id){
        return null;
    }

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
                targetPage = createPageById(id);
                if (targetPage == null){
                    throw new IllegalStateException("Wrong page id, this should not happen!");
                }
            }
            if (currentPage != null && currentPage != targetPage) {
                if (currentPage.isShowing()) {
                    currentPage.onStop();
                }
                if (currentPage.getCurrentTempPage() != null && currentPage.getCurrentTempPage().isShowing()) {
                    currentPage.getCurrentTempPage().onStop();
                }
            }
            if (targetPage.getCurrentTempPage() != null) {
                targetPage.getCurrentTempPage().restart();
                targetPage.getCurrentTempPage().onRestart();
            } else {
                targetPage.onStart();
            }
            currentPage = targetPage;
        } else {
            Logging.LOG.log(Level.WARNING, "No page!");
        }
    }

    public void showTempPage(FCLTempPage fclTempPage) {
        if (currentPage != null) {
            if (currentPage.isShowing()) {
                currentPage.onStop();
            }
            if (currentPage.getAllTempPages().size() > 0 &&
                    currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1) != null &&
                    currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1).isShowing()) {
                currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1).onStop();
            }
            fclTempPage.onStart();
            currentPage.getAllTempPages().add(fclTempPage);
            currentPage.setCurrentTempPage(fclTempPage);
        }
    }

    public boolean canReturn() {
        return currentPage.getCurrentTempPage() != null;
    }

    public void dismissCurrentTempPage() {
        if (currentPage != null && currentPage.getCurrentTempPage() != null) {
            currentPage.getCurrentTempPage().dismiss();
            currentPage.getAllTempPages().remove(currentPage.getAllTempPages().size() - 1);
            if (currentPage.getAllTempPages().size() > 0) {
                currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1).restart();
                currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1).onRestart();
                currentPage.setCurrentTempPage(currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1));
            } else {
                currentPage.onStart();
                currentPage.setCurrentTempPage(null);
            }
        }
    }

    public void dismissAllTempPagesCreatedByPage(int id) {
        FCLCommonPage commonPage = getPageById(id);
        if (commonPage.getCurrentTempPage() != null) {
            commonPage.getCurrentTempPage().dismiss();
        }
        commonPage.getAllTempPages().clear();
        commonPage.setCurrentTempPage(null);
        if (currentPage == commonPage) {
            commonPage.onStart();
        }
    }

    public void dismissAllTempPages() {
        for (FCLCommonPage page : allPages) {
            dismissAllTempPagesCreatedByPage(page.getId());
        }
    }

    public void onPause() {
        for (FCLCommonPage page : allPages) {
            page.onPause();
            for (FCLTempPage tempPage : page.getAllTempPages()) {
                tempPage.onPause();
            }
        }
    }

    public void onResume() {
        for (FCLCommonPage page : allPages) {
            page.onResume();
            for (FCLTempPage tempPage : page.getAllTempPages()) {
                tempPage.onResume();
            }
        }
    }

}
