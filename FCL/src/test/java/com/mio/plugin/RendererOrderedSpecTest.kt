package com.mio.plugin

import com.mio.plugin.RendererPlugin.OrderedOption
import com.mio.plugin.RendererPlugin.OrderedSpec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * MG 2.0.1 MultiDraw 方案偏好序的取值规整/继承展开回归测试。
 *
 * 期望值对齐 MobileGlues 上游 MobileGlues-cpp/config/settings.cpp 的默认集合，
 * 与 ZL2 #2165 OrderedChoiceEntryTest 钉死的官方行为一致。
 */
class RendererOrderedSpecTest {

    private fun specOf(key: String): OrderedSpec {
        val order = MG_MULTIDRAW_ORDERS.getValue(key)
        return OrderedSpec(
            options = order.map { OrderedOption(it, it) },
            defaultOrder = order,
            inheritFrom = if (key == "multidrawOrder") null else "multidrawOrder",
            nativeValue = MG_MULTIDRAW_NATIVES[key]
        )
    }

    @Test
    fun keepsDefaultOrder() {
        val global = specOf("multidrawOrder")
        val default = global.defaultOrder.joinToString(",")
        assertEquals(default, coerceOrder(null, global))
        assertEquals(default, coerceOrder(default, global))
    }

    @Test
    fun dropsUnknownDedupsAndAppendsMissing() {
        val global = specOf("multidrawOrder")
        assertEquals(
            "compute,native,multiarrays,multiindirect,multibasevertex,indirect,basevertex,unroll",
            coerceOrder("compute,native,unknown,compute,multiarrays", global)
        )
    }

    @Test
    fun functionEntryRejectsBackendsItDoesNotImplement() {
        // glMultiDrawArrays 只实现 multiarrays/multiindirect/unroll，其余候选剔除后按默认序补尾
        assertEquals(
            "multiindirect,multiarrays,unroll",
            coerceOrder("compute,multiindirect,basevertex", specOf("multidrawOrderArrays"))
        )
    }

    @Test
    fun expandingCustomGlobalOrderPerFunction() {
        val customGlobal = "compute,native,indirect,multiindirect,multibasevertex,multiarrays,basevertex,unroll"
        assertEquals(
            "multiarrays,multiindirect,unroll",
            expandOrder(customGlobal, specOf("multidrawOrderArrays"))
        )
        assertEquals(
            "multiarrays,indirect,multiindirect,multibasevertex,unroll",
            expandOrder(customGlobal, specOf("multidrawOrderElements"))
        )
        assertEquals(
            "compute,multibasevertex,indirect,multiindirect,basevertex,unroll",
            expandOrder(customGlobal, specOf("multidrawOrderElementsBaseVertex"))
        )
        assertEquals(
            "multiindirect,indirect",
            expandOrder(customGlobal, specOf("multidrawOrderArraysIndirect"))
        )
        assertEquals(
            "multiindirect,indirect",
            expandOrder(customGlobal, specOf("multidrawOrderElementsIndirect"))
        )
    }

    @Test
    fun expandsNativePseudoItemInParentOrder() {
        // 全局序含伪项 native 时，函数级条目把它替换为本入口的真实后端
        assertEquals(
            "multiarrays,multiindirect,unroll",
            expandOrder("native,multiindirect,compute", specOf("multidrawOrderArrays"))
        )
    }

    @Test
    fun pinnedAgainstUpstreamSets() {
        // 候选集合/默认序/native 映射与上游 settings.cpp 同源，防止随手改动
        assertEquals(
            listOf(
                "native", "multiindirect", "multibasevertex", "multiarrays",
                "indirect", "basevertex", "unroll", "compute"
            ),
            MG_MULTIDRAW_ORDERS.getValue("multidrawOrder")
        )
        assertEquals(listOf("multiarrays", "multiindirect", "unroll"), MG_MULTIDRAW_ORDERS.getValue("multidrawOrderArrays"))
        assertEquals(
            listOf("multiarrays", "multiindirect", "multibasevertex", "indirect", "unroll"),
            MG_MULTIDRAW_ORDERS.getValue("multidrawOrderElements")
        )
        assertEquals(
            listOf("multibasevertex", "multiindirect", "indirect", "basevertex", "unroll", "compute"),
            MG_MULTIDRAW_ORDERS.getValue("multidrawOrderElementsBaseVertex")
        )
        assertEquals(listOf("multiindirect", "indirect"), MG_MULTIDRAW_ORDERS.getValue("multidrawOrderArraysIndirect"))
        assertEquals(listOf("multiindirect", "indirect"), MG_MULTIDRAW_ORDERS.getValue("multidrawOrderElementsIndirect"))
        assertEquals("multiarrays", MG_MULTIDRAW_NATIVES.getValue("multidrawOrderArrays"))
        assertEquals("multiarrays", MG_MULTIDRAW_NATIVES.getValue("multidrawOrderElements"))
        assertEquals("multibasevertex", MG_MULTIDRAW_NATIVES.getValue("multidrawOrderElementsBaseVertex"))
        assertEquals("multiindirect", MG_MULTIDRAW_NATIVES.getValue("multidrawOrderArraysIndirect"))
        assertEquals("multiindirect", MG_MULTIDRAW_NATIVES.getValue("multidrawOrderElementsIndirect"))
    }

    @Test
    fun defaultOrderMustCoverOptions() {
        assertThrows(IllegalArgumentException::class.java) {
            OrderedSpec(
                options = listOf(OrderedOption("a", "a"), OrderedOption("b", "b")),
                defaultOrder = listOf("a")
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            // 继承条目必须提供 nativeValue
            OrderedSpec(
                options = listOf(OrderedOption("a", "a")),
                defaultOrder = listOf("a"),
                inheritFrom = "parent"
            )
        }
    }
}
