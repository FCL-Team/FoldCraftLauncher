package com.tungsten.fcl.control.view

import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fclcore.fakefx.collections.FXCollections

/**
 * 按键编辑撤销/恢复历史（按组快照）。
 *
 * 每次按键级编辑操作（增/删/改、拖动/缩放写回、跨组复制/移动）生效前，对受影响的控件组
 * ViewData 做整组快照入栈，undo/redo 时整组回放。不使用 ControlButtonData.clone()：
 * clone 会重新生成 id 且深拷贝 style，快照需要保留 id（视图归属解析依赖）并保持 style
 * 为共享引用（样式管理器的后续修改须继续传导到恢复后的按键）。
 *
 * 条目按组校验：组被删除或控制器切换后，引用失效的条目在撤销/恢复时自动丢弃跳过。
 */
class EditHistory(private val viewManager: ViewManager) {

    /** 单组快照：组对象引用 + 该组当前 ViewData 的深拷贝 */
    data class GroupSnapshot(val group: ControlViewGroup, val data: ControlViewGroup.ViewData)

    /** 历史变化回调（记录/撤销/恢复/清空），ViewManager 用于刷新悬浮条置灰态 */
    var onChanged: Runnable? = null

    private val undoStack = ArrayList<List<GroupSnapshot>>()
    private val redoStack = ArrayList<List<GroupSnapshot>>()

    fun canUndo(): Boolean = undoStack.isNotEmpty()

    fun canRedo(): Boolean = redoStack.isNotEmpty()

    /**
     * 编辑操作生效前记录受影响组的当前状态（守卫通过后、改数据前调用）。
     * 数据未加载的组跳过；全部无效则不入栈（本次操作不产生历史）。
     */
    fun record(vararg groups: ControlViewGroup?) {
        val snapshots = groups.filterNotNull()
            .filter { it.isDataLoaded() }
            .map { GroupSnapshot(it, copyViewData(it.viewData)) }
        if (snapshots.isEmpty()) {
            return
        }
        redoStack.clear()
        undoStack.add(snapshots)
        if (undoStack.size > MAX_ENTRIES) {
            undoStack.removeAt(0)
        }
        notifyChanged()
    }

    fun clear() {
        if (undoStack.isEmpty() && redoStack.isEmpty()) {
            return
        }
        undoStack.clear()
        redoStack.clear()
        notifyChanged()
    }

    /** 撤销：回放到最近一次操作前，引用失效的条目自动跳过。返回是否发生了回放 */
    fun undo(): Boolean = replay(undoStack, redoStack)

    /** 恢复：回放到最近一次撤销前，引用失效的条目自动跳过。返回是否发生了回放 */
    fun redo(): Boolean = replay(redoStack, undoStack)

    private fun replay(from: ArrayList<List<GroupSnapshot>>, to: ArrayList<List<GroupSnapshot>>): Boolean {
        while (from.isNotEmpty()) {
            val entry = from.removeAt(from.size - 1)
            if (entry.any { !isValid(it.group) }) {
                continue
            }
            // 当前状态压入对侧栈（须在 apply 改数据前捕获）
            to.add(entry.map { GroupSnapshot(it.group, copyViewData(it.group.viewData)) })
            apply(entry)
            notifyChanged()
            return true
        }
        notifyChanged()
        return false
    }

    /** 条目引用的组必须仍属于当前控制器 */
    private fun isValid(group: ControlViewGroup): Boolean {
        val controller = viewManager.gameMenu.controller ?: return false
        return controller.viewGroups().contains(group)
    }

    /** 快照回放：数据写回组（再次拷贝，快照与现网数据不共享列表），视图由 ViewManager 重建 */
    private fun apply(entry: List<GroupSnapshot>) {
        // 写数据前先捕获各组当前的视图 id：撤销"添加/复制"时新增控件的 id 已不在
        // 现数据里，重建视图须按"改动前 ∪ 改动后"的并集摘除，否则视图残留
        val staleIds = entry.map { viewManager.collectGroupViewIds(it.group) }
        for (snapshot in entry) {
            val viewData = snapshot.group.viewData
            viewData.setButtonList(FXCollections.observableList(snapshot.data.buttonList().map { copyButton(it) }))
            viewData.setDirectionList(FXCollections.observableList(snapshot.data.directionList().map { copyDirection(it) }))
        }
        viewManager.restoreViewsForGroups(entry.map { it.group }, staleIds)
    }

    /** 拷贝 ViewData：保留 id、style 共享引用，baseInfo/event 深拷贝 */
    private fun copyViewData(src: ControlViewGroup.ViewData): ControlViewGroup.ViewData {
        val data = ControlViewGroup.ViewData()
        data.setButtonList(FXCollections.observableList(src.buttonList().map { copyButton(it) }))
        data.setDirectionList(FXCollections.observableList(src.directionList().map { copyDirection(it) }))
        return data
    }

    private fun copyButton(src: ControlButtonData): ControlButtonData {
        val data = ControlButtonData(src.id)
        data.setText(src.text)
        data.setStyle(src.style)
        data.setBaseInfo(src.baseInfo.clone())
        data.setEvent(src.event.clone())
        return data
    }

    private fun copyDirection(src: ControlDirectionData): ControlDirectionData {
        val data = ControlDirectionData(src.id)
        data.setStyle(src.style)
        data.setBaseInfo(src.baseInfo.clone())
        data.setEvent(src.event.clone())
        return data
    }

    private fun notifyChanged() {
        onChanged?.run()
    }

    companion object {
        /** 历史条目上限：快照体积小（每组几十键、每键几百字节），30 条足够回退到操作起点 */
        private const val MAX_ENTRIES = 30
    }
}
