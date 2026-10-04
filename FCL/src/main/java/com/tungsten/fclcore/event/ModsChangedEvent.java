package com.tungsten.fclcore.event;

import com.tungsten.fclcore.mod.LocalModFile;
import com.tungsten.fclcore.mod.ModManager;

/**
 * 模组目录发生变化（如外部新增模组文件）时由 {@link ModManager} 发布的事件。
 */
public class ModsChangedEvent extends Event {

    /**
     * 增量新增的模组信息；null 表示无法增量描述，监听方应整体重扫。
     */
    private final LocalModFile modFile;

    public ModsChangedEvent(ModManager source, LocalModFile modFile) {
        super(source);
        this.modFile = modFile;
    }

    public ModManager getModManager() {
        return (ModManager) getSource();
    }

    public LocalModFile getModFile() {
        return modFile;
    }
}
