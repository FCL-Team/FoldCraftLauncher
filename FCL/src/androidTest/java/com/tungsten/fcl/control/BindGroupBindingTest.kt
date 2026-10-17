package com.tungsten.fcl.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.JsonObject
import com.tungsten.fcl.control.data.ButtonEventData
import com.tungsten.fcl.control.data.ControlButtonData
import com.tungsten.fcl.control.data.ControlDirectionData
import com.tungsten.fcl.control.data.ControlViewGroup
import com.tungsten.fclcore.fakefx.collections.FXCollections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

/**
 * 按键绑定控件组显隐的格式与数据语义验证：
 * - 绑定串模式解析（无后缀=按下切换、:show=按下显示、:hide=按下隐藏、未绑定）；
 * - 模式编码为绑定后缀，与解析互为逆操作；
 * - 带后缀的绑定列表经 Event 序列化往返后原样保留（落盘格式兼容）；
 * - 控件组合并的数据语义：目标组收全源组控件、源组清空、控件 id 不变。
 */
@RunWith(AndroidJUnit4::class)
class BindGroupBindingTest {

    @Test
    fun parseModeIndexModes() {
        assertEquals(1, BindGroupDialog.parseModeIndex(listOf("g1"), "g1"))
        assertEquals(2, BindGroupDialog.parseModeIndex(listOf("g1:show"), "g1"))
        assertEquals(3, BindGroupDialog.parseModeIndex(listOf("g1:hide"), "g1"))
        assertEquals(0, BindGroupDialog.parseModeIndex(emptyList(), "g1"))
        // 其他组的绑定不影响本组
        assertEquals(0, BindGroupDialog.parseModeIndex(listOf("g2:show"), "g1"))
        assertEquals(1, BindGroupDialog.parseModeIndex(listOf("g2", "g1"), "g1"))
    }

    @Test
    fun encodeModeSuffixModes() {
        assertNull(BindGroupDialog.encodeModeSuffix(0))
        assertEquals("", BindGroupDialog.encodeModeSuffix(1))
        assertEquals(":show", BindGroupDialog.encodeModeSuffix(2))
        assertEquals(":hide", BindGroupDialog.encodeModeSuffix(3))
    }

    @Test
    fun encodeParseRoundTrip() {
        // 0（不绑定）编码为 null 表示不写入绑定列表，不存在可回环的绑定串
        assertNull(BindGroupDialog.encodeModeSuffix(0))
        for (mode in 1..3) {
            val suffix = BindGroupDialog.encodeModeSuffix(mode)
            val binding = if (suffix.isNullOrEmpty()) "g" else "g$suffix"
            assertEquals(mode, BindGroupDialog.parseModeIndex(listOf(binding), "g"))
        }
    }

    @Test
    fun bindGroupWithSuffixSerializationRoundTrip() {
        val event = ButtonEventData.Event()
        event.setBindViewGroup(FXCollections.observableArrayList("g1", "g2:show", "g3:hide"))

        val json = ButtonEventData.Event.Serializer().serialize(event, null, null) as JsonObject
        val array = json.getAsJsonArray("bindViewGroup")
        assertEquals(3, array.size())
        assertEquals("g1", array[0].asString)
        assertEquals("g2:show", array[1].asString)
        assertEquals("g3:hide", array[2].asString)

        val restored = ButtonEventData.Event.Serializer().deserialize(json, null, null)
        assertEquals(listOf("g1", "g2:show", "g3:hide"), restored.bindViewGroupList())
    }

    /** 复刻编辑对话框保存链：BindGroupDialog 改编辑模型 → getView()=data.clone() → setEvent 换引用，绑定不得丢失 */
    @Test
    fun editDialogEventSaveChainPreservesBind() {
        val model = ControlButtonData(UUID.randomUUID().toString())
        model.getEvent().getPressEvent().setBindViewGroup(FXCollections.observableArrayList("grp:show"))

        val view = model.clone() as ControlButtonData
        val target = ControlButtonData(UUID.randomUUID().toString())
        target.setEvent(view.getEvent())

        assertEquals(listOf("grp:show"), target.getEvent().getPressEvent().bindViewGroupList())
    }

    @Test
    fun mergeGroupDataSemantics() {        val source = ControlViewGroup(UUID.randomUUID().toString())
        val target = ControlViewGroup(UUID.randomUUID().toString())
        val button1 = ControlButtonData(UUID.randomUUID().toString())
        val button2 = ControlButtonData(UUID.randomUUID().toString())
        val direction = ControlDirectionData(UUID.randomUUID().toString())
        source.getViewData().addButton(button1)
        source.getViewData().addButton(button2)
        source.getViewData().addDirection(direction)

        // mergeViewGroup 的数据操作：转移副本引用后清空源组
        for (button in ArrayList(source.getViewData().buttonList())) {
            target.getViewData().addButton(button)
        }
        for (item in ArrayList(source.getViewData().directionList())) {
            target.getViewData().addDirection(item)
        }
        source.getViewData().buttonList().clear()
        source.getViewData().directionList().clear()

        assertEquals(listOf(button1.id, button2.id), target.getViewData().buttonList().map { it.id })
        assertEquals(listOf(direction.id), target.getViewData().directionList().map { it.id })
        assertTrue(source.getViewData().buttonList().isEmpty())
        assertTrue(source.getViewData().directionList().isEmpty())
    }
}
