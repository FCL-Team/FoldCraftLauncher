package com.tungsten.fcl.control;

import com.tungsten.fcl.control.data.CustomControl;

/**
 * 6.1 清理：旧 View 版控件编辑弹窗实现已删除（由 MiuixEditViewDialog 替代），
 * 仅保留 [Callback] 接口供 MiuixEditViewDialog 与游戏内调用点
 * （GameMenu / ControlButton / ControlDirection）复用。
 */
public class EditViewDialog {

    private EditViewDialog() {
    }

    public interface Callback {
        void onPositive(CustomControl view);

        void onClone(CustomControl view);

        default void onDelete() {
        }
    }
}
