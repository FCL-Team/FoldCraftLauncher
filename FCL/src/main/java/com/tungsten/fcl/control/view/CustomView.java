package com.tungsten.fcl.control.view;

import com.tungsten.fcl.control.data.CustomControl;

public interface CustomView {
    /** 编辑模式参考组（ghost）控件的透明度 */
    float GHOST_ALPHA = 0.2f;

    CustomControl.ViewType getType();
    String getViewId();

    /**
     * 标记为编辑模式参考组控件：半透明显示、不响应触摸、不参与吸附
     */
    void setGhost(boolean ghost);
    boolean isGhost();

    /**
     * 编辑模式选中态：显示选中框与缩放手柄，再次轻点弹出编辑对话框
     */
    void setSelected(boolean selected);
    boolean isSelected();
    void switchParentVisibility();
    void removeListener();
}
