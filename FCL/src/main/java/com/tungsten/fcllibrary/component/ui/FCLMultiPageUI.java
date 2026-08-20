package com.tungsten.fcllibrary.component.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fclcore.task.Task;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * 多页面 UI 基类：内层 ViewPager2 承载普通页面（tab 点击平滑滑动切换），
 * 覆盖层承载临时页（导航栈）。
 * <p>
 * 页面无生命周期，随 ViewPager2 创建/销毁（不保留状态）：
 * 子类实现 {@link #getPageCount()} / {@link #createPage(int)} 提供页面工厂，
 * 在 onCreate 中调用 {@link #setupPages(ViewGroup, TabLayout)} 挂载页面容器。
 */
public abstract class FCLMultiPageUI extends FCLCommonUI {

    /**
     * 临时页切换动画时长（毫秒）
     */
    private static final int TEMP_PAGE_ANIM_DURATION = 200;

    /**
     * 上次 onPageSelected 的页面位置，用于过滤 ViewPager2 重复 dispatch 当前页（如软键盘弹出等布局变化）
     */
    private int lastSelectedPosition = -1;

    /**
     * 页面位置 → 页面实例注册表，页面被回收时清出（不保留状态）
     */
    private final ArrayList<FCLPage> pageRegistry = new ArrayList<>();

    private final ArrayList<FCLPage> tempPageStack = new ArrayList<>();

    private ViewPager2 pagePager;
    private FrameLayout overlay;

    public FCLMultiPageUI(Context context, @LayoutRes int id) {
        super(context, id);
    }

    /**
     * 子类在 onCreate 中调用：把内层 ViewPager2 与临时页覆盖层装入 container，
     * 若提供 tabLayout 则用 TabLayoutMediator 联动（tab 点击平滑滑动切换）。
     */
    protected void setupPages(ViewGroup container, TabLayout tabLayout) {
        pagePager = new ViewPager2(getContext());
        pagePager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pagePager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        // 禁用滑动手势：页面内滚动内容与滑动切换冲突，仅通过 tab / showPage 切换
        pagePager.setUserInputEnabled(false);
        pagePager.setAdapter(new PageAdapter());
        container.addView(pagePager);

        overlay = new FrameLayout(getContext());
        overlay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlay.setVisibility(View.GONE);
        container.addView(overlay);

        if (tabLayout != null) {
            // tab 由布局 XML 静态定义（TabItem），此处仅接管点击：瞬时切换（不创建中间页），
            // 过渡动画由 onPageSelected 处理
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    pagePager.setCurrentItem(tab.getPosition(), false);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        // 切换页面时清空临时页（临时页属于当前页面上下文）
        pagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                dismissAllTempPages();
                // tab 高亮同步
                if (tabLayout != null) {
                    TabLayout.Tab tab = tabLayout.getTabAt(position);
                    if (tab != null && tabLayout.getSelectedTabPosition() != position) {
                        tab.select();
                    }
                }
                // 页面切换过渡动画：不创建中间页（瞬时跳转），直接对目标页做淡入 + 上滑进入。
                // 同步执行（不 post）：onPageSelected 时页面已挂载但尚未绘制，置透明发生在首帧绘制前，
                // 不会出现先显示后消失的闪烁。
                // 仅在页面位置真正变化时播放：ViewPager2 在布局变化（如软键盘弹出、页面内容刷新）
                // 后会重新 dispatch 当前页，此时不播放动画避免页面闪烁
                if (position != lastSelectedPosition) {
                    FCLPage page = getPage(position);
                    if (page != null) {
                        View contentView = page.getContentView();
                        contentView.animate().cancel();
                        contentView.setAlpha(0f);
                        contentView.setTranslationY(contentView.getResources().getDisplayMetrics().density * 30f);
                        contentView.animate().alpha(1f).translationY(0f).setDuration(250).start();
                    }
                }
                lastSelectedPosition = position;
            }
        });
    }

    /**
     * 页面数量（对应 tab 数或 ViewPager2 页数）
     */
    public abstract int getPageCount();

    /**
     * 按位置创建页面（页面 id 由子类页面常量决定）
     */
    public abstract FCLPage createPage(int position);

    /**
     * tab 标题，无 tab 的 UI 返回 null
     */
    public String[] getTabTitles() {
        return null;
    }

    /**
     * 获取指定位置的页面，不存在则创建（页面创建即完成初始化）
     */
    public FCLPage getPage(int position) {
        while (pageRegistry.size() <= position) {
            pageRegistry.add(null);
        }
        if (pageRegistry.get(position) == null) {
            FCLPage page = createPage(position);
            pageRegistry.set(position, page);
            onPageCreated(page);
        }
        return pageRegistry.get(position);
    }

    /**
     * 页面创建后回调，子类可分发版本等上下文数据
     */
    protected void onPageCreated(FCLPage page) {

    }

    /**
     * 遍历已创建的页面（不触发创建）
     */
    public void forEachCreatedPage(Consumer<FCLPage> action) {
        for (FCLPage page : pageRegistry) {
            if (page != null) {
                action.accept(page);
            }
        }
    }

    /**
     * 切换到指定位置页面（替代原 switchPage）
     */
    public void showPage(int position) {
        if (pagePager != null) {
            pagePager.setCurrentItem(position, false);
        }
    }

    public int getCurrentPagePosition() {
        return pagePager == null ? 0 : pagePager.getCurrentItem();
    }

    public boolean canReturn() {
        return !tempPageStack.isEmpty();
    }

    /**
     * 在覆盖层上显示临时页并压入导航栈（隐藏下层页面，临时页独占显示）
     */
    public void showTempPage(FCLPage page) {
        if (overlay == null) return;
        // 隐藏当前栈顶临时页与内层页面，避免透明背景下层内容透出（原 PageManager 机制隐藏当前页与栈顶临时页）
        if (!tempPageStack.isEmpty()) {
            tempPageStack.get(tempPageStack.size() - 1).getContentView().setVisibility(View.GONE);
        }
        if (pagePager != null) {
            pagePager.setVisibility(View.GONE);
        }
        // 新临时页淡入
        View view = page.getContentView();
        view.setAlpha(0f);
        overlay.setVisibility(View.VISIBLE);
        overlay.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.animate().alpha(1f).setDuration(TEMP_PAGE_ANIM_DURATION).start();
        tempPageStack.add(page);
    }

    /**
     * 弹栈顶临时页（淡出后移除并恢复下层）
     */
    public void dismissCurrentTempPage() {
        if (tempPageStack.isEmpty()) return;
        FCLPage page = tempPageStack.remove(tempPageStack.size() - 1);
        View view = page.getContentView();
        view.animate().alpha(0f).setDuration(TEMP_PAGE_ANIM_DURATION).withEndAction(() -> {
            overlay.removeView(view);
            if (!tempPageStack.isEmpty()) {
                // 恢复下层临时页显示
                tempPageStack.get(tempPageStack.size() - 1).getContentView().setVisibility(View.VISIBLE);
            }
            if (tempPageStack.isEmpty()) {
                overlay.setVisibility(View.GONE);
                // 临时页全部关闭后恢复内层页面显示
                if (pagePager != null) {
                    pagePager.setVisibility(View.VISIBLE);
                }
            }
        }).start();
    }

    /**
     * 清空全部临时页
     */
    public void dismissAllTempPages() {
        while (!tempPageStack.isEmpty()) {
            dismissCurrentTempPage();
        }
    }

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    @Override
    public abstract Task<?> refresh(Object... param);

    @Override
    public void onBackPressed() {
        if (canReturn()) {
            dismissCurrentTempPage();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 内层 ViewPager2 适配器：页面随创建/销毁（不保留状态）
     */
    private class PageAdapter extends RecyclerView.Adapter<PageAdapter.Holder> {

        private class Holder extends RecyclerView.ViewHolder {
            final FrameLayout container;
            int boundPosition = -1;

            Holder(FrameLayout container) {
                super(container);
                this.container = container;
            }
        }

        @Override
        public int getItemCount() {
            return getPageCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            // ViewPager2 要求页面直接子 View 必须 MATCH_PARENT
            FrameLayout container = new FrameLayout(parent.getContext());
            container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new Holder(container);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.boundPosition = position;
            holder.container.removeAllViews();
            View contentView = getPage(position).getContentView();
            // 防御：GapWorker 预取可能将同一页面实例挂到其他容器（预取 bind 与正式 bind 竞争），
            // 先解除旧 parent，避免 addView 抛 "child already has a parent"
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            holder.container.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        public void onViewRecycled(Holder holder) {
            // 页面被回收即销毁（不保留状态），下次进入全新创建
            if (holder.boundPosition >= 0 && holder.boundPosition < pageRegistry.size()) {
                pageRegistry.set(holder.boundPosition, null);
            }
        }
    }
}
