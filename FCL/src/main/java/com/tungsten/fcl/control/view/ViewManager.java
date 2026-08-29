package com.tungsten.fcl.control.view;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcllibrary.ui.ProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewManager {

/**
 * 进度框最短显示时长：布局数据解析很快（几十毫秒）时若立即关闭，用户看不到进度
 */
private static final long MIN_PROGRESS_VISIBLE_MS = 500;

/**
 * 正在申请加载数据的布局 id（防重复申请）
 */
private final Set<String> loadingGroups = new HashSet<>();

/**
 * 本轮布局数据加载进度（布局粒度，见 requestLoadGroup）
 */
private int loadTotal = 0;
private int loadCompleted = 0;
private ProgressDialog loadDialog;
private long loadDialogShowTime = 0;

    private final GameMenu gameMenu;

    public ViewManager(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public void setup() {
        // Initialize menu view
        MenuView menuView = new MenuView(gameMenu.getActivity());
        // 远高于按键的 z 序上限（按钮 113 + 组序 translationZ），保证菜单永远在最上层
        menuView.setElevation(2000f);
        menuView.setup(gameMenu);
        gameMenu.setMenuView(menuView);
        gameMenu.getBaseLayout().addView(menuView);
        menuView.initPosition();
        gameMenu.fpsText.initPosition();
        gameMenu.memoryText.initPosition();
        gameMenu.hideAllViewsProperty().addListener(observable -> menuView.setAlpha(gameMenu.isHideAllViews() ? 0 : 1));
        if (gameMenu.getMenuSetting().isHideMenuView()) {
            menuView.setVisibility(View.INVISIBLE);
        }
        // Initialize controller
        initializeController();
        gameMenu.controllerProperty().addListener(i -> initializeController());
        gameMenu.viewGroupProperty().addListener(i -> initializeController());
        gameMenu.editModeProperty().addListener(i -> initializeController());
    }

    public void addView(CustomControl control) {
        if (gameMenu.isEditMode()) {
            if (gameMenu.getViewGroup() != null) {
                if (!gameMenu.getViewGroup().isDataLoaded()) {
                    // 布局数据加载中：此时写入会被加载完成后的完整数据覆盖
                    Toast.makeText(gameMenu.getActivity(), gameMenu.getActivity().getString(R.string.message_data_is_loading), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (control instanceof ControlButtonData) {
                    gameMenu.getViewGroup().getViewData().addButton((ControlButtonData) control);
                } else {
                    gameMenu.getViewGroup().getViewData().addDirection((ControlDirectionData) control);
                }
                saveController();
                loadView(control, true, 0f);
            } else {
                Toast.makeText(gameMenu.getActivity(), gameMenu.getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void removeView(CustomControl control) {
        if (gameMenu.getViewGroup() != null && gameMenu.isEditMode()) {
            for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
                View view = gameMenu.getBaseLayout().getChildAt(i);
                if (view instanceof CustomView) {
                    if (control.getViewId().equals(((CustomView) view).getViewId())) {
                        gameMenu.getBaseLayout().removeView(view);
                        break;
                    }
                }
            }
            if (control instanceof ControlButtonData) {
                gameMenu.getViewGroup().getViewData().removeButton((ControlButtonData) control);
            } else {
                gameMenu.getViewGroup().getViewData().removeDirection((ControlDirectionData) control);
            }
            saveController();
        }
    }

    private void loadView(CustomControl control, boolean parentVisibility, float zOrder) {
        if (control instanceof ControlButtonData data) {
            ControlButton button = new ControlButton(gameMenu.getActivity(), gameMenu, view -> {
                ((ControlButton) view).setParentVisibility(parentVisibility);
                ((ControlButton) view).setData(data);
            });
            button.setTranslationZ(zOrder);
            gameMenu.getBaseLayout().addView(button);
        } else {
            ControlDirectionData data = (ControlDirectionData) control;
            ControlDirection direction = new ControlDirection(gameMenu.getActivity(), gameMenu, false, view -> {
                ((ControlDirection) view).setParentVisibility(parentVisibility);
                ((ControlDirection) view).setData(data);
            });
            direction.setTranslationZ(zOrder);
            gameMenu.getBaseLayout().addView(direction);
        }
    }

    public void saveController() {
        gameMenu.getController().saveToDisk();
    }

    /**
     * 需要渲染的布局：编辑模式为当前布局，游戏模式为全部可见布局
     * （隐藏布局不加载，bindViewGroup 事件唤起时按需加载）。
     */
    private List<ControlViewGroup> targets() {
        Controller controller = gameMenu.getController();
        if (controller == null) return Collections.emptyList();
        if (gameMenu.isEditMode()) {
            return gameMenu.getViewGroup() == null ? Collections.emptyList() : Collections.singletonList(gameMenu.getViewGroup());
        }
        ArrayList<ControlViewGroup> list = new ArrayList<>();
        for (ControlViewGroup group : controller.viewGroups()) {
            if (group.getVisibility() == ControlViewGroup.Visibility.VISIBLE) {
                list.add(group);
            }
        }
        return list;
    }

    public void initializeController() {
        removeAllCustomViews();
        loadingGroups.clear();
        loadTotal = 0;
        loadCompleted = 0;
        for (ControlViewGroup group : targets()) {
            if (!group.isDataLoaded()) {
                requestLoadGroup(group);
            } else {
                renderGroup(group);
            }
        }
        updateLoadProgress();
    }

    /**
     * 渲染一个布局的全部按键（数据必须已就绪）。
     * 组间 z 序按文件记录顺序固定（translationZ 步长需跨过按钮/方向键 1px 的 elevation 差），
     * 异步加载完成顺序不定，仅靠 addView 顺序会使后加载的背景组盖住操作组导致无法点击。
     */
    private void renderGroup(ControlViewGroup group) {
        float zOrder = gameMenu.getController().viewGroups().indexOf(group) * 2f;
        group.getViewData().buttonList().forEach(data -> loadView(data, true, zOrder));
        group.getViewData().directionList().forEach(data -> loadView(data, true, zOrder));
    }

    /** 该布局是否已有按键视图渲染在 baseLayout 上 */
    private boolean isGroupRendered(ControlViewGroup group) {
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            View view = gameMenu.getBaseLayout().getChildAt(i);
            if (view instanceof CustomView) {
                if (group.getViewData().buttonList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))
                        || group.getViewData().directionList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 异步申请加载布局按键数据（后台解析 + 主线程填充，Controllers 保证回调在主线程），
     * 进度随批量加载计数推进，就绪后若仍属于当前渲染目标则渲染。
     */
    private void requestLoadGroup(ControlViewGroup group) {
        if (loadingGroups.contains(group.getId())) return;
        loadingGroups.add(group.getId());
        loadTotal++;
        updateLoadProgress();
        Controllers.loadViewGroup(gameMenu.getController(), group, new Controllers.ViewGroupLoadCallback() {
            @Override
            public void onLoaded(ControlViewGroup viewGroup) {
                loadingGroups.remove(viewGroup.getId());
                loadCompleted++;
                updateLoadProgress();
                // 数据就绪：属于当前渲染目标时渲染；已渲染（并发重复回调）则跳过
                if (targets().contains(viewGroup) && !isGroupRendered(viewGroup)) {
                    renderGroup(viewGroup);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                loadingGroups.remove(group.getId());
                // 失败也推进进度，布局保持无按键
                loadCompleted++;
                updateLoadProgress();
                Log.e("ViewManager", "Failed to load view group " + group.getId(), e);
            }
        });
    }

    /**
     * bindViewGroup 主动唤起隐藏布局：独立一次性进度框，回调即关。
     * 不参与批量加载的共享计数，避免计数跨会话错位时进度框滞留不关。
     */
    private void requestLoadForBind(ControlViewGroup group) {
        if (loadingGroups.contains(group.getId())) return;
        loadingGroups.add(group.getId());
        ProgressDialog dialog = new ProgressDialog(gameMenu.getActivity());
        Controllers.loadViewGroup(gameMenu.getController(), group, new Controllers.ViewGroupLoadCallback() {
            @Override
            public void onLoaded(ControlViewGroup viewGroup) {
                dismissProgressDialog(dialog);
                loadingGroups.remove(viewGroup.getId());
                if (!isGroupRendered(viewGroup)) {
                    renderGroup(viewGroup);
                }
            }

            @Override
            public void onFailed(Throwable e) {
                dismissProgressDialog(dialog);
                loadingGroups.remove(group.getId());
                Log.e("ViewManager", "Failed to load view group " + group.getId(), e);
            }
        });
    }

    private void dismissProgressDialog(ProgressDialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void updateLoadProgress() {
        if (loadTotal == 0) {
            dismissLoadDialog();
            return;
        }
        if (loadDialog == null) {
            loadDialog = new ProgressDialog(gameMenu.getActivity());
            loadDialogShowTime = SystemClock.uptimeMillis();
        }
        if (loadCompleted >= loadTotal) {
            scheduleDismissLoadDialog();
        }
    }

    /** 加载完成：确保进度框至少显示最短时长再关闭（加载过快时用户也能看到） */
    private void scheduleDismissLoadDialog() {
        ProgressDialog dialog = loadDialog;
        if (dialog == null) return;
        long elapsed = SystemClock.uptimeMillis() - loadDialogShowTime;
        long delay = Math.max(0, MIN_PROGRESS_VISIBLE_MS - elapsed);
        if (delay == 0) {
            dismissLoadDialog();
            return;
        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().postDelayed(() -> {
                // 延迟期间可能已开启新会话，仅关闭仍是当前实例的对话框
                if (loadDialog == dialog) {
                    dismissLoadDialog();
                }
            }, delay);
        } else {
            dismissLoadDialog();
        }
    }

    private void dismissLoadDialog() {
        if (loadDialog != null) {
            loadDialog.dismiss();
            loadDialog = null;
        }
    }

    private void removeAllCustomViews() {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            if (gameMenu.getBaseLayout().getChildAt(i) instanceof CustomView) {
                views.add(gameMenu.getBaseLayout().getChildAt(i));
            }
        }
        for (View v : views) {
            ((CustomView) v).removeListener();
            gameMenu.getBaseLayout().removeView(v);
        }
    }

    public void switchViewGroupVisibility(ControlViewGroup viewGroup) {
        if (viewGroup == null)
            return;
        if (!viewGroup.isDataLoaded()) {
            // 布局数据未加载：独立进度框 + 按需加载，就绪后渲染为可见（loadingGroups 防连点重复申请）
            requestLoadForBind(viewGroup);
            return;
        }
        if (!isGroupRendered(viewGroup)) {
            // 数据已加载但视图未渲染（隐藏布局不在渲染目标中，或已被 initializeController 清除）：渲染为可见
            renderGroup(viewGroup);
            return;
        }
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            View view = gameMenu.getBaseLayout().getChildAt(i);
            if (view instanceof CustomView) {
                if (viewGroup.getViewData().buttonList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))
                        || viewGroup.getViewData().directionList().stream().anyMatch(it -> it.getId().equals(((CustomView) view).getViewId()))) {
                    ((CustomView) view).switchParentVisibility();
                }
            }
        }
    }

}