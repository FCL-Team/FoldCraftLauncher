package com.tungsten.fcl.control.view;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.EditViewDialog;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.data.ControlButtonData;
import com.tungsten.fcl.control.data.ControlDirectionData;
import com.tungsten.fcl.control.data.ControlViewGroup;
import com.tungsten.fcl.control.data.CustomControl;
import com.tungsten.fcl.setting.Controller;
import com.tungsten.fcl.setting.Controllers;
import com.tungsten.fcllibrary.ui.ProgressDialog;
import com.tungsten.fcllibrary.util.ConvertUtils;

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

    /** 编辑模式当前选中的控件（缩放手柄与悬浮操作栏的目标） */
    private CustomView selectedView;
    private ControlEditBar editBar;

    public void selectView(CustomView view) {
        if (selectedView == view) return;
        if (selectedView != null) {
            selectedView.setSelected(false);
        }
        selectedView = view;
        if (view != null) {
            view.setSelected(true);
            positionEditBar(view);
        } else {
            editBar.hideAnimated();
        }
    }

    public void clearSelection() {
        selectView(null);
    }

    /** 拖动/缩放开始时隐藏操作栏，避免悬空 */
    public void hideEditBar() {
        editBar.hideAnimated();
    }

    /** 拖动/缩放结束后重新定位显示操作栏 */
    public void showEditBar(CustomView view) {
        if (selectedView == view) {
            positionEditBar(view);
        }
    }

    /** 操作栏定位：控件下方居中（防出屏，底部放不下时移到上方） */
    private void positionEditBar(CustomView view) {
        View target = (View) view;
        editBar.showAnimated();
        editBar.post(() -> {
            if (editBar.getVisibility() != View.VISIBLE || editBar.getMeasuredWidth() == 0) {
                return;
            }
            int barWidth = editBar.getMeasuredWidth();
            int barHeight = editBar.getMeasuredHeight();
            int margin = (int) (24 * gameMenu.getActivity().getResources().getDisplayMetrics().density);
            int screenW = gameMenu.getBaseLayout().getWidth();
            int screenH = gameMenu.getBaseLayout().getHeight();
            float x = target.getX() + target.getWidth() / 2f - barWidth / 2f;
            x = Math.max(0, Math.min(screenW - barWidth, x));
            float y = target.getY() + target.getHeight() + margin;
            if (y + barHeight > screenH) {
                y = target.getY() - margin - barHeight;
            }
            editBar.setX(Math.max(0, x));
            editBar.setY(Math.max(0, y));
        });
    }

    /**
     * 编辑确认后重新定位操作栏：数据更新经异步链刷新控件（监听投递 → view.post 定位 → 布局），
     * 立即定位会读到旧坐标，须等控件下一次布局完成后再定位
     */
    private void positionEditBarAfterUpdate(CustomView view) {
        View target = (View) view;
        View.OnLayoutChangeListener listener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int l, int t, int r, int b, int ol, int ot, int or, int ob) {
                target.removeOnLayoutChangeListener(this);
                target.post(() -> {
                    if (selectedView == view) {
                        positionEditBar(view);
                    }
                });
            }
        };
        target.addOnLayoutChangeListener(listener);
    }

    private void setupEditBar() {
        editBar = new ControlEditBar(gameMenu.getActivity());
        // 远高于按键的 z 序，低于菜单悬浮球
        editBar.setElevation(1500f);
        editBar.setVisibility(View.GONE);
        editBar.setListener(new ControlEditBar.Listener() {
            @Override
            public void onSettings() {
                openEditDialog();
            }

            @Override
            public void onCopy() {
                if (selectedView instanceof ControlButton) {
                    addView(((ControlButton) selectedView).getData().cloneView());
                } else if (selectedView instanceof ControlDirection) {
                    addView(((ControlDirection) selectedView).getData().cloneView());
                }
            }

            @Override
            public void onDelete() {
                if (selectedView instanceof ControlButton) {
                    removeView(((ControlButton) selectedView).getData());
                } else if (selectedView instanceof ControlDirection) {
                    removeView(((ControlDirection) selectedView).getData());
                }
            }
        });
        gameMenu.getBaseLayout().addView(editBar);
    }

    /** 打开选中控件的编辑对话框（设置/复制/删除中的"设置"） */
    private void openEditDialog() {
        if (selectedView instanceof ControlButton) {
            ControlButton button = (ControlButton) selectedView;
            EditViewDialog dialog = new EditViewDialog(gameMenu.getActivity(), button.getData().clone(), gameMenu, new EditViewDialog.Callback() {
                @Override
                public void onPositive(CustomControl view) {
                    ControlButtonData newData = ((ControlButtonData) view).clone();
                    button.getData().setText(newData.getText());
                    button.getData().setBaseInfo(newData.getBaseInfo());
                    button.getData().setStyle(newData.getStyle());
                    button.getData().setEvent(newData.getEvent());
                    saveController();
                    if (selectedView == button) {
                        positionEditBarAfterUpdate(button);
                    }
                }

                @Override
                public void onClone(CustomControl view) {
                    addView(view);
                }

                @Override
                public void onDelete() {
                    removeView(button.getData());
                }
            }, true);
            dialog.show();
        } else if (selectedView instanceof ControlDirection) {
            ControlDirection direction = (ControlDirection) selectedView;
            EditViewDialog dialog = new EditViewDialog(gameMenu.getActivity(), direction.getData().clone(), gameMenu, new EditViewDialog.Callback() {
                @Override
                public void onPositive(CustomControl view) {
                    ControlDirectionData newData = ((ControlDirectionData) view).clone();
                    direction.getData().setBaseInfo(newData.getBaseInfo());
                    direction.getData().setStyle(newData.getStyle());
                    direction.getData().setEvent(newData.getEvent());
                    saveController();
                    if (selectedView == direction) {
                        positionEditBarAfterUpdate(direction);
                    }
                }

                @Override
                public void onClone(CustomControl view) {
                    addView(view);
                }

                @Override
                public void onDelete() {
                    removeView(direction.getData());
                }
            }, true);
            dialog.show();
        }
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
        setupEditBar();
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
                loadView(control, true, 0f, false);
            } else {
                Toast.makeText(gameMenu.getActivity(), gameMenu.getActivity().getString(R.string.edit_view_no_group), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void removeView(CustomControl control) {
        if (gameMenu.getViewGroup() != null && gameMenu.isEditMode()) {
            clearSelection();
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

    private void loadView(CustomControl control, boolean parentVisibility, float zOrder, boolean ghost) {
        if (control instanceof ControlButtonData data) {
            ControlButton button = new ControlButton(gameMenu.getActivity(), gameMenu, view -> {
                ((ControlButton) view).setParentVisibility(parentVisibility);
                ((ControlButton) view).setData(data);
            });
            button.setTranslationZ(zOrder);
            button.setGhost(ghost);
            gameMenu.getBaseLayout().addView(button);
        } else {
            ControlDirectionData data = (ControlDirectionData) control;
            ControlDirection direction = new ControlDirection(gameMenu.getActivity(), gameMenu, false, view -> {
                ((ControlDirection) view).setParentVisibility(parentVisibility);
                ((ControlDirection) view).setData(data);
            });
            direction.setTranslationZ(zOrder);
            direction.setGhost(ghost);
            gameMenu.getBaseLayout().addView(direction);
        }
    }

    public void saveController() {
        gameMenu.getController().saveToDisk();
    }

    /**
     * 编辑拖动吸附：以期望位置为基准，在阈值内与兄弟控件对齐并绘制参考线。
     * 同边对齐（左↔左、右↔右等）吸附为贴齐；邻接对齐（左右相邻、上下相邻）吸附保持吸附间距的间隔。
     * 期望位置每帧由手指屏幕坐标独立计算，吸附修正不参与下一帧基准，
     * 避免"吸附→弹回"的循环抖动。
     *
     * @return 修正后的 x/y
     */
    public float[] snapPosition(CustomView self, float desiredX, float desiredY) {
        View view = (View) self;
        int areaWidth = gameMenu.getBaseLayout().getWidth();
        int areaHeight = gameMenu.getBaseLayout().getHeight();
        float x = Math.max(0, Math.min(areaWidth - view.getWidth(), desiredX));
        float y = Math.max(0, Math.min(areaHeight - view.getHeight(), desiredY));
        if (!gameMenu.getMenuSetting().isAutoFit()) {
            gameMenu.getTouchPad().removeLine(0);
            gameMenu.getTouchPad().removeLine(1);
            return new float[]{x, y};
        }
        float threshold = Math.max(
                ConvertUtils.dip2px(gameMenu.getActivity(), gameMenu.getMenuSetting().getAutoFitDist()),
                ConvertUtils.dip2px(gameMenu.getActivity(), 2));

        float bestX = threshold;
        float bestY = threshold;
        float lineX = 0;
        float lineY = 0;
        float selfEdgeX = 0;
        float selfEdgeY = 0;
        boolean hasX = false;
        boolean hasY = false;
        ViewGroup parent = gameMenu.getBaseLayout();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getVisibility() != View.VISIBLE
                    || child == self
                    || (!(child instanceof ControlButton) && !(child instanceof ControlDirection))
                    || ((CustomView) child).isGhost()) {
                continue;
            }
            // x 轴吸附线：同边对齐（左↔左、右↔右）贴齐，邻接对齐（自身左↔目标右、自身右↔目标左）保持 threshold 间隔
            float selfLeft = x;
            float selfRight = x + view.getWidth();
            float childLeft = child.getX();
            float childRight = child.getX() + child.getWidth();
            float[] linesX = {childLeft, childRight + threshold, childRight, childLeft - threshold};
            float[] edgesX = {selfLeft, selfLeft, selfRight, selfRight};
            for (int k = 0; k < linesX.length; k++) {
                float d = Math.abs(linesX[k] - edgesX[k]);
                if (d <= bestX) {
                    bestX = d;
                    hasX = true;
                    lineX = linesX[k];
                    selfEdgeX = edgesX[k];
                }
            }
            // y 轴吸附线：同边对齐贴齐，邻接对齐（自身上↔目标下、自身下↔目标上）保持 threshold 间隔
            float selfTop = y;
            float selfBottom = y + view.getHeight();
            float childTop = child.getY();
            float childBottom = child.getY() + child.getHeight();
            float[] linesY = {childTop, childBottom + threshold, childBottom, childTop - threshold};
            float[] edgesY = {selfTop, selfTop, selfBottom, selfBottom};
            for (int k = 0; k < linesY.length; k++) {
                float d = Math.abs(linesY[k] - edgesY[k]);
                if (d <= bestY) {
                    bestY = d;
                    hasY = true;
                    lineY = linesY[k];
                    selfEdgeY = edgesY[k];
                }
            }
        }
        if (hasX) {
            x += lineX - selfEdgeX;
            gameMenu.getTouchPad().drawLine(0, Math.round(lineX), Math.round(selfEdgeX));
        } else {
            gameMenu.getTouchPad().removeLine(0);
        }
        if (hasY) {
            y += lineY - selfEdgeY;
            gameMenu.getTouchPad().drawLine(1, Math.round(lineY), Math.round(selfEdgeY));
        } else {
            gameMenu.getTouchPad().removeLine(1);
        }
        return new float[]{x, y};
    }

    /** 计算控件合成透明度：一键隐藏 > 参考组 > 全局不透明度 */
    public float resolveAlpha(CustomView view) {
        if (gameMenu.isHideAllViews()) {
            return 0f;
        }
        float base = view.isGhost() ? CustomView.GHOST_ALPHA : 1f;
        return base * gameMenu.getMenuSetting().getControlsOpacity() / 100f;
    }

    /** 全局不透明度变化时刷新全部控件 */
    public void applyControlsOpacity() {
        for (int i = 0; i < gameMenu.getBaseLayout().getChildCount(); i++) {
            View view = gameMenu.getBaseLayout().getChildAt(i);
            if (view instanceof CustomView) {
                view.setAlpha(resolveAlpha((CustomView) view));
            }
        }
    }

    /**
     * 滑动链：查找包含父容器坐标点的可滑动联动按钮（排除 exclude 与参考组），
     * 手指从联动按钮滑出后命中谁，就触发谁的按下。
     */
    public ControlButton findSwipableButtonAt(float x, float y, CustomView exclude) {
        for (int i = gameMenu.getBaseLayout().getChildCount() - 1; i >= 0; i--) {
            View view = gameMenu.getBaseLayout().getChildAt(i);
            if (!(view instanceof ControlButton button)
                    || button == exclude
                    || button.getVisibility() != View.VISIBLE
                    || !button.getData().getEvent().isSwipable()) {
                continue;
            }
            if (x >= button.getX() && x <= button.getX() + button.getWidth()
                    && y >= button.getY() && y <= button.getY() + button.getHeight()) {
                return button;
            }
        }
        return null;
    }

    /**
     * 需要渲染的布局：编辑模式为当前布局 + 未被编辑面板隐藏的其他布局（参考组，可多组同时显示），
     * 游戏模式为全部可见布局（隐藏布局不加载，bindViewGroup 事件唤起时按需加载）。
     */
    private List<ControlViewGroup> targets() {
        Controller controller = gameMenu.getController();
        if (controller == null) return Collections.emptyList();
        if (gameMenu.isEditMode()) {
            if (gameMenu.getViewGroup() == null) return Collections.emptyList();
            ArrayList<ControlViewGroup> list = new ArrayList<>();
            for (ControlViewGroup group : controller.viewGroups()) {
                // 参考组可被编辑面板临时隐藏；当前编辑组始终渲染
                if (group != gameMenu.getViewGroup() && !gameMenu.isEditorGroupHidden(group)) {
                    list.add(group);
                }
            }
            list.add(gameMenu.getViewGroup());
            return list;
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
     * 编辑模式的参考组（ghost）压到所有操作组之下，异步乱序加载也不遮挡当前组。
     */
    private void renderGroup(ControlViewGroup group) {
        boolean ghost = gameMenu.isEditMode() && group != gameMenu.getViewGroup();
        float zOrder = ghost ? -2f : gameMenu.getController().viewGroups().indexOf(group) * 2f;
        group.getViewData().buttonList().forEach(data -> loadView(data, true, zOrder, ghost));
        group.getViewData().directionList().forEach(data -> loadView(data, true, zOrder, ghost));
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
        clearSelection();
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