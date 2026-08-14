package com.tungsten.fcl.ui;

import android.content.Context;
import android.view.View;

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

    public PageManager (Context context, FCLUILayout parent, int defaultPageId) {
        this.context = context;
        this.parent = parent;
        this.defaultPageId = defaultPageId;

        init();
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

    public abstract void init();

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

    /** 页面显示/隐藏直接操作视图（页面无生命周期方法） */
    private void showPage(FCLCommonPage page) {
        page.getContentView().setVisibility(View.VISIBLE);
    }

    private void hidePage(FCLCommonPage page) {
        if (page.isShowing()) {
            page.getContentView().setVisibility(View.GONE);
        }
    }

    private void showTempPageView(FCLTempPage page) {
        page.getContentView().setVisibility(View.VISIBLE);
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
                hidePage(currentPage);
                if (currentPage.getCurrentTempPage() != null) {
                    currentPage.getCurrentTempPage().getContentView().setVisibility(View.GONE);
                }
            }
            if (targetPage.getCurrentTempPage() != null) {
                showTempPageView(targetPage.getCurrentTempPage());
            } else {
                showPage(targetPage);
            }
            currentPage = targetPage;
        } else {
            Logging.LOG.log(Level.WARNING, "No page!");
        }
    }

    public void showTempPage(FCLTempPage fclTempPage) {
        if (currentPage != null) {
            hidePage(currentPage);
            if (currentPage.getAllTempPages().size() > 0 &&
                    currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1) != null) {
                currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1).getContentView().setVisibility(View.GONE);
            }
            showTempPageView(fclTempPage);
            currentPage.getAllTempPages().add(fclTempPage);
            currentPage.setCurrentTempPage(fclTempPage);
        }
    }

    public boolean canReturn() {
        return currentPage.getCurrentTempPage() != null;
    }

    public void dismissCurrentTempPage() {
        if (currentPage != null && currentPage.getCurrentTempPage() != null) {
            parent.removeView(currentPage.getCurrentTempPage().getContentView());
            currentPage.getAllTempPages().remove(currentPage.getAllTempPages().size() - 1);
            if (currentPage.getAllTempPages().size() > 0) {
                showTempPageView(currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1));
                currentPage.setCurrentTempPage(currentPage.getAllTempPages().get(currentPage.getAllTempPages().size() - 1));
            } else {
                showPage(currentPage);
                currentPage.setCurrentTempPage(null);
            }
        }
    }

    public void dismissAllTempPagesCreatedByPage(int id) {
        FCLCommonPage commonPage = getPageById(id);
        if (commonPage.getCurrentTempPage() != null) {
            parent.removeView(commonPage.getCurrentTempPage().getContentView());
        }
        commonPage.getAllTempPages().clear();
        commonPage.setCurrentTempPage(null);
        if (currentPage == commonPage) {
            showPage(commonPage);
        }
    }

    public void dismissAllTempPages() {
        for (FCLCommonPage page : allPages) {
            dismissAllTempPagesCreatedByPage(page.getId());
        }
    }

}
