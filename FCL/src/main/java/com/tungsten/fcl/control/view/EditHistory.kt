package com.tungsten.fcl.control.view

import com.tungsten.fcl.control.data.BaseInfoData
import com.tungsten.fcl.control.data.ButtonEventData
import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlButtonStyle
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.ControlDirectionStyle
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fcl.control.data.DirectionEventData
import com.tungsten.fclcore.fakefx.collections.FXCollections

/**
 * 按键编辑撤销/恢复历史（按组快照）。
 *
 * 每次按键级编辑操作（增/删/改、拖动/缩放写回、跨组复制/移动）生效前，对受影响的控件组
 * ViewData 做整组快照入栈，undo/redo 时整组回放。
 *
 * 快照不持有 ControlButtonData/ControlDirectionData 实例：数据实例会在样式对象上注册监听，
 * 快照复用实例会让被丢弃的数据被样式对象长期引用；快照只保存 id、文字、样式引用、位置尺寸与
 * 事件的拷贝，回放时重新构造数据实例。不使用 clone()：clone 会重新生成 id 且深拷贝 style，
 * 快照需要保留 id（视图归属解析依赖）并保持 style 为共享引用（样式管理器的后续修改须继续
 * 传导到恢复后的按键）。
 *
 * 条目按组校验：组被删除或控制器切换后，引用失效的条目在撤销/恢复时自动丢弃跳过。
 */
class EditHistory(private val viewManager: ViewManager) {

    /** 单组快照：组对象引用 + 该组控件数据的独立拷贝 */
    private data class GroupSnapshot(
        val group: ControlViewGroup,
        val buttons: List<ButtonSnapshot>,
        val directions: List<DirectionSnapshot>,
    )

    /** 按键快照：id 与 style 引用保留，文字/位置尺寸/事件深拷贝 */
    private data class ButtonSnapshot(
        val id: String,
        val text: String?,
        val style: ControlButtonStyle?,
        val baseInfo: BaseInfoData,
        val event: ButtonEventData,
    )

    /** 方向键快照：id 与 style 引用保留，位置尺寸/事件深拷贝 */
    private data class DirectionSnapshot(
        val id: String,
        val style: ControlDirectionStyle?,
        val baseInfo: BaseInfoData,
        val event: DirectionEventData,
    )

    /** 历史变化回调（记录/撤销/恢复/清空），用于刷新操作区按钮的可用状态 */
    var onChanged: Runnable? = null

    private val undoStack = ArrayList<List<GroupSnapshot>>()
    private val redoStack = ArrayList<List<GroupSnapshot>>()

    fun canUndo(): Boolean = undoStack.isNotEmpty()

    fun canRedo(): Boolean = redoStack.isNotEmpty()

    /**
     * 编辑操作生效前记录受影响组的当前状态（守卫通过后、改数据前调用）。
     * 数据未加载的组跳过；任何新编辑都会清空恢复栈，即使本次没有可记录的组。
     */
    fun record(vararg groups: ControlViewGroup?) {
        val snapshots = groups.filterNotNull()
            .filter { it.isDataLoaded() }
            .map { snapshotOf(it) }
        redoStack.clear()
        if (snapshots.isNotEmpty()) {
            undoStack.add(snapshots)
            if (undoStack.size > MAX_ENTRIES) {
                undoStack.removeAt(0)
            }
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
            to.add(entry.map { snapshotOf(it.group) })
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

    /** 快照回放：数据写回组（快照内容与现网数据不共享对象），视图由 ViewManager 重建 */
    private fun apply(entry: List<GroupSnapshot>) {
        // 写数据前先捕获各组当前的视图 id：撤销"添加/复制"时新增控件的 id 已不在
        // 恢复后的数据里，重建视图须按"改动前 ∪ 改动后"的并集摘除，否则视图残留
        val restores = LinkedHashMap<ControlViewGroup, Set<String>>()
        for (snapshot in entry) {
            restores[snapshot.group] = viewManager.collectGroupViewIds(snapshot.group)
        }
        for (snapshot in entry) {
            val viewData = snapshot.group.viewData
            // 旧数据将被整表替换：先断开样式监听，避免样式对象继续持有废弃实例
            viewData.buttonList().forEach { it.detachStyleListener() }
            viewData.directionList().forEach { it.detachStyleListener() }
            viewData.setButtonList(FXCollections.observableList(snapshot.buttons.map { it.toData() }))
            viewData.setDirectionList(FXCollections.observableList(snapshot.directions.map { it.toData() }))
        }
        viewManager.restoreViewsForGroups(restores)
    }

    /** 组当前数据快照：style 保留共享引用，其余字段独立拷贝 */
    private fun snapshotOf(group: ControlViewGroup): GroupSnapshot {
        val viewData = group.viewData
        return GroupSnapshot(
            group = group,
            buttons = viewData.buttonList().map {
                ButtonSnapshot(it.id, it.text, it.style, it.baseInfo.clone(), it.event.clone())
            },
            directions = viewData.directionList().map {
                DirectionSnapshot(it.id, it.style, it.baseInfo.clone(), it.event.clone())
            },
        )
    }

    private fun ButtonSnapshot.toData(): ControlButtonData = ControlButtonData(id).apply {
        text = this@toData.text
        style = this@toData.style
        baseInfo = this@toData.baseInfo.clone()
        event = this@toData.event.clone()
    }

    private fun DirectionSnapshot.toData(): ControlDirectionData = ControlDirectionData(id).apply {
        style = this@toData.style
        baseInfo = this@toData.baseInfo.clone()
        event = this@toData.event.clone()
    }

    private fun notifyChanged() {
        onChanged?.run()
    }

    companion object {
        /** 历史条目上限：快照体积小（每组几十键、每键几百字节），30 条足够回退到操作起点 */
        private const val MAX_ENTRIES = 30
    }
}
