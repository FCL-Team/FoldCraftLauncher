package com.tungsten.fcl.control;

import com.tungsten.fcl.control.data.ControlViewGroup;

/**
 * 6.1 清理：旧 View 版新增/编辑分组弹窗实现已删除（由 MiuixEditViewGroupDialog 替代），
 * 仅保留 [Callback] 接口供 MiuixViewGroupDialog / MiuixEditViewGroupDialog 复用。
 */
public class EditViewGroupDialog {

    private EditViewGroupDialog() {
    }

    public interface Callback {
        void onPositive(String name, ControlViewGroup.Visibility visibility);
    }
}
