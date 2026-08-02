package com.tungsten.fcl.control;

/**
 * 6.1 清理：旧 View 版新增快捷输入文本弹窗实现已删除（由 MiuixAddInputTextDialog 替代），
 * 仅保留 [Callback] 接口供 MiuixQuickInputDialog / MiuixAddInputTextDialog 复用。
 */
public class AddInputTextDialog {

    private AddInputTextDialog() {
    }

    public interface Callback {
        void onTextAdd();
    }
}
