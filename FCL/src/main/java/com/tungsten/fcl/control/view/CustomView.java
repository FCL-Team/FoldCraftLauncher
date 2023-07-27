package com.tungsten.fcl.control.view;

import com.tungsten.fcl.control.data.CustomControl;

public interface CustomView {
    CustomControl.ViewType getType();
    String getViewId();
    void switchParentVisibility();
    void removeListener();
}
