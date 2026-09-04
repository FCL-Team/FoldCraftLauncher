package com.tungsten.fcl.control.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tungsten.fcl.setting.Controller
import com.tungsten.fcl.util.Constants
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.fakefx.collections.FXCollections
import com.tungsten.fclcore.util.io.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID

/**
 * 控制数据（按键/方向键布局）手写 JSON 序列化的往返与格式验证。
 *
 * 覆盖背景：控制器序列化从「每层新建 Gson + TypeToken 递归解析」改为
 * 各 Serializer 直接相互调用 + JsonArray 手写循环（消除 Gson 类型解析
 * 死循环与 io 线程池占用）。本测试确保：
 * - 往返一致：serialize → deserialize 后各字段不变（含列表字段）；
 * - 输出结构：列表字段保持 JSON 数组、percentage 保持 reference/size 结构；
 * - 格式兼容：assets 默认控制器样例（既有 JSON 文件格式）可被手写
 *   Serializer 完整解析；
 * - 保存合并：并发 saveToDisk 不丢数据、不崩溃。
 */
@RunWith(AndroidJUnit4::class)
class ControlDataSerializationTest {

    // ==================== Event（按键事件，含列表字段） ====================

    @Test
    fun buttonEventRoundTrip() {
        val event = ButtonEventData.Event()
        event.setAutoKeep(true)
        event.setAutoClick(true)
        event.setOutputText("hello")
        event.setOutputKeycodes(FXCollections.observableArrayList(17, 32))
        event.setBindViewGroup(FXCollections.observableArrayList("group1", "group2"))

        val json = ButtonEventData.Event.Serializer().serialize(event, null, null) as JsonObject
        // 列表字段必须是 JSON 数组（防止回退到 TypeToken 解析模式）
        assertTrue(json.get("outputKeycodes").isJsonArray)
        assertTrue(json.get("bindViewGroup").isJsonArray)

        val restored = ButtonEventData.Event.Serializer().deserialize(json, null, null)
        assertEquals(event.isAutoKeep, restored.isAutoKeep)
        assertEquals(event.isAutoClick, restored.isAutoClick)
        assertEquals(event.getOutputText(), restored.getOutputText())
        assertEquals(event.outputKeycodesList(), restored.outputKeycodesList())
        assertEquals(event.bindViewGroupList(), restored.bindViewGroupList())
    }

    @Test
    fun buttonEventDataRoundTrip() {
        val data = ButtonEventData()
        data.setPointerFollow(true)
        data.setMovable(true)
        data.getPressEvent().setOutputKeycodes(FXCollections.observableArrayList(1, 2, 3))
        data.getClickEvent().setQuickInput(true)

        val json = ButtonEventData.Serializer().serialize(data, null, null) as JsonObject
        assertTrue(json.get("pressEvent").isJsonObject)
        assertTrue(json.getAsJsonObject("pressEvent").get("outputKeycodes").isJsonArray)

        val restored = ButtonEventData.Serializer().deserialize(json, null, null)
        assertEquals(data.isPointerFollow(), restored.isPointerFollow())
        assertEquals(data.isMovable(), restored.isMovable())
        assertEquals(data.getPressEvent().outputKeycodesList(), restored.getPressEvent().outputKeycodesList())
        assertEquals(data.getClickEvent().isQuickInput(), restored.getClickEvent().isQuickInput())
    }

    // ==================== BaseInfoData（含 percentage 结构） ====================

    @Test
    fun baseInfoRoundTrip() {
        val info = BaseInfoData()
        info.setXPosition(800)
        info.setYPosition(750)
        info.setAbsoluteWidth(120)
        info.getPercentageWidth().setReference(BaseInfoData.PercentageSize.Reference.SCREEN_HEIGHT)
        info.getPercentageWidth().setSize(140)

        val json = BaseInfoData.Serializer().serialize(info, null, null) as JsonObject
        // percentage 输出保持 {reference, size} 结构（既有文件格式）
        val percentage = json.getAsJsonObject("percentageWidth")
        assertEquals("SCREEN_HEIGHT", percentage.get("reference").asString)
        assertEquals(140, percentage.get("size").asInt)

        val restored = BaseInfoData.Serializer().deserialize(json, null, null)
        assertEquals(800, restored.getXPosition())
        assertEquals(750, restored.getYPosition())
        assertEquals(120, restored.getAbsoluteWidth())
        assertEquals(BaseInfoData.PercentageSize.Reference.SCREEN_HEIGHT, restored.getPercentageWidth().getReference())
        assertEquals(140, restored.getPercentageWidth().getSize())
    }

    // ==================== DirectionEventData（方向键码数组） ====================

    @Test
    fun directionEventRoundTrip() {
        val event = DirectionEventData()
        event.setUpKeycode(FXCollections.observableArrayList(17))
        event.setDownKeycode(FXCollections.observableArrayList(31, 32))
        event.setFollowOption(DirectionEventData.FollowOption.FOLLOW)
        event.setSneak(false)
        event.setSneakKeycode(42)

        val json = DirectionEventData.Serializer().serialize(event, null, null) as JsonObject
        assertTrue(json.get("upKeycode").isJsonArray)
        assertTrue(json.get("downKeycode").isJsonArray)

        val restored = DirectionEventData.Serializer().deserialize(json, null, null)
        assertEquals(event.upKeycodeList(), restored.upKeycodeList())
        assertEquals(event.downKeycodeList(), restored.downKeycodeList())
        assertEquals(event.getFollowOption(), restored.getFollowOption())
        assertEquals(event.isSneak(), restored.isSneak())
        assertEquals(event.getSneakKeycode(), restored.getSneakKeycode())
    }

    // ==================== ControlButtonData（含 style/baseInfo/event 嵌套） ====================

    @Test
    fun controlButtonRoundTrip() {
        ButtonStyles.init()
        val button = ControlButtonData(UUID.randomUUID().toString())
        button.setText("测试按钮")
        button.setStyle(ButtonStyles.findStyleByName("Default"))
        button.getBaseInfo().setAbsoluteWidth(120)
        button.getEvent().getClickEvent().setQuickInput(true)

        val json = ControlButtonData.Serializer().serialize(button, null, null) as JsonObject
        assertEquals("Default", json.get("style").asString)

        val restored = ControlButtonData.Serializer().deserialize(json, null, null)
        assertEquals("测试按钮", restored.getText())
        assertEquals(120, restored.getBaseInfo().getAbsoluteWidth())
        assertTrue(restored.getEvent().getClickEvent().isQuickInput())
    }

    // ==================== 完整控制器往返 ====================

    @Test
    fun controllerRoundTrip() {
        ButtonStyles.init()
        DirectionStyles.init()
        val viewGroup = ControlViewGroup(UUID.randomUUID().toString())
        viewGroup.setName("测试布局")
        val button = ControlButtonData(UUID.randomUUID().toString())
        button.setText("A")
        viewGroup.getViewData().setButtonList(FXCollections.observableArrayList(button))

        val controller = Controller(
            UUID.randomUUID().toString(), "测试控制器", "1.0", 1, "author", "desc",
            Constants.CONTROLLER_VERSION, FXCollections.observableArrayList(viewGroup)
        )

        val json = Controller.Serializer().serialize(controller, null, null) as JsonObject
        assertTrue(json.get("viewGroups").isJsonArray)
        assertTrue(json.getAsJsonArray("viewGroups")[0].asJsonObject.get("viewData").isJsonObject)

        val restored = Controller.Serializer().deserialize(json, null, null)
        assertEquals("测试控制器", restored.getName())
        assertEquals(1, restored.viewGroups().size)
        assertEquals("测试布局", restored.viewGroups()[0].getName())
        assertEquals("A", restored.viewGroups()[0].getViewData().buttonList()[0].getText())
    }

    // ==================== 既有文件格式兼容（assets 默认控制器样例） ====================

    @Test
    fun assetSampleParsesWithHandWrittenSerializers() {
        ButtonStyles.init()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val json = context.assets.open("controllers/00000000.json").bufferedReader().use { it.readText() }
        val root = JsonParser.parseString(json) as JsonObject
        val button = root.getAsJsonArray("viewGroups")[0].asJsonObject
            .getAsJsonObject("viewData").getAsJsonArray("buttonList")[0]

        val restored = ControlButtonData.Serializer().deserialize(button, null, null)
        // 样例 baseInfo.percentageWidth = {reference: SCREEN_HEIGHT, size: 140}
        assertEquals(140, restored.getBaseInfo().getPercentageWidth().getSize())
        assertEquals(
            BaseInfoData.PercentageSize.Reference.SCREEN_HEIGHT,
            restored.getBaseInfo().getPercentageWidth().getReference()
        )
        // 样例 event 按键键码为 JSON 数组
        assertTrue(restored.getEvent().getPressEvent().outputKeycodesList().isNotEmpty() ||
                restored.getEvent().getClickEvent().outputKeycodesList().isNotEmpty() ||
                restored.getEvent().getDoubleClickEvent().outputKeycodesList().isNotEmpty())
    }

    // ==================== 并发保存合并 ====================

    @Test
    fun concurrentSaveToDiskMergesTasks() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val controllerDir = File(context.cacheDir, "controller_test")
        controllerDir.mkdirs()
        // 测试进程未初始化 FCLPath，指向可写临时目录
        FCLPath.CONTROLLER_DIR = controllerDir.absolutePath

        val viewGroup = ControlViewGroup(UUID.randomUUID().toString())
        viewGroup.setName("merge")
        val controller = Controller(
            UUID.randomUUID().toString(), "合并保存测试", "1.0", 1, "author", "desc",
            Constants.CONTROLLER_VERSION, FXCollections.observableArrayList(viewGroup)
        )

        // 模拟布局编辑频繁自动保存：并发触发 50 次
        val threads = (0 until 50).map { Thread { controller.saveToDisk() } }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        Thread.sleep(3000) // 等待后台保存任务完成

        val file = File(FCLPath.CONTROLLER_DIR, controller.getFileName())
        assertTrue("控制器文件应已保存", file.exists())
        val restored = Controller.Serializer().deserialize(JsonParser.parseString(FileUtils.readText(file)), null, null)
        assertEquals("合并保存测试", restored.getName())
        assertEquals("merge", restored.viewGroups()[0].getName())
        file.delete()
    }
}