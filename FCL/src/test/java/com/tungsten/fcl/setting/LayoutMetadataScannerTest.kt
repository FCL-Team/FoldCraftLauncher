package com.tungsten.fcl.setting

import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.File

/**
 * 布局元数据扫描器：与完整 JSON 解析结果逐字段比对。
 */
class LayoutMetadataScannerTest {

    private fun res(name: String): File =
        File(javaClass.getResource("/controlconverter/$name")!!.toURI())

    @Test
    fun realLayoutMetadataMatchesFullParse() {
        val file = res("real_4bf3d919_fcl.json")
        val meta = LayoutMetadataScanner.scan(file, null)
        val root = JsonParser.parseString(file.readText()).asJsonObject

        assertEquals(root["id"].asString, meta.id)
        assertEquals(root["name"].asString, meta.name)
        assertEquals(root["version"].asString, meta.version)
        assertEquals(root["author"].asString, meta.author)
        assertEquals(root["description"].asString, meta.description)
        assertEquals(root["versionCode"].asInt, meta.versionCode)
        assertEquals(root["controllerVersion"].asInt, meta.controllerVersion)
        assertNull(meta.viewDataJson)

        val groups = root["viewGroups"].asJsonArray
        assertEquals(groups.size(), meta.groups.size)
        groups.forEachIndexed { index, element ->
            val obj = element.asJsonObject
            val info = meta.groups[index]
            assertEquals(obj["id"].asString, info.id)
            assertEquals(obj["name"].asString, info.name)
            assertEquals(obj["visibility"].asString, info.visibility)
        }

        // 样式原始片段可被独立解析，内容与完整解析一致
        assertEquals(root["buttonStyles"], JsonParser.parseString(meta.buttonStylesJson))
        assertEquals(root["directionStyles"], JsonParser.parseString(meta.directionStylesJson))
    }

    @Test
    fun targetViewDataMatchesFullParse() {
        val file = res("real_4bf3d919_fcl.json")
        val root = JsonParser.parseString(file.readText()).asJsonObject
        val groups = root["viewGroups"].asJsonArray
        for (index in intArrayOf(0, 7, groups.size() - 1)) {
            val target = groups[index].asJsonObject
            val meta = LayoutMetadataScanner.scan(file, target["id"].asString)
            assertNotNull(meta.viewDataJson)
            assertEquals(target["viewData"], JsonParser.parseString(meta.viewDataJson))
        }
    }

    @Test
    fun smallLayoutMetadata() {
        val file = res("test_fcl_layout.json")
        val meta = LayoutMetadataScanner.scan(file, null)
        val root = JsonParser.parseString(file.readText()).asJsonObject
        assertEquals(root["id"].asString, meta.id)
        assertEquals(root["name"].asString, meta.name)
        assertEquals(root["viewGroups"].asJsonArray.size(), meta.groups.size)
    }

    @Test
    fun escapedStringsAndBom() {
        val file = File.createTempFile("controller", ".json")
        try {
            file.writeText(
                "\uFEFF{\"id\":\"abc\",\"name\":\"a\\\"b\\\\c\\u4e2d\",\"version\":\"1\\n2\"," +
                    "\"versionCode\":3,\"controllerVersion\":4,\"viewGroups\":[" +
                    "{\"id\":\"g1\",\"name\":\"G\\t1\",\"visibility\":\"INVISIBLE\",\"viewData\":{}}]}"
            )
            val meta = LayoutMetadataScanner.scan(file, null)
            assertEquals("abc", meta.id)
            assertEquals("a\"b\\c中", meta.name)
            assertEquals("1\n2", meta.version)
            assertEquals(3, meta.versionCode)
            assertEquals(4, meta.controllerVersion)
            assertEquals(1, meta.groups.size)
            assertEquals("g1", meta.groups[0].id)
            assertEquals("G\t1", meta.groups[0].name)
            assertEquals("INVISIBLE", meta.groups[0].visibility)
        } finally {
            file.delete()
        }
    }

    @Test
    fun unknownFieldsAndGroupFieldsSkipped() {
        val file = File.createTempFile("controller", ".json")
        try {
            file.writeText(
                "{\"id\":\"abc\",\"extra\":{\"deep\":[1,2,{\"x\":\"y\"}]},\"viewGroups\":[" +
                    "{\"viewData\":{\"buttonList\":[{\"id\":\"b1\"}]},\"id\":\"g1\",\"name\":\"G\",\"other\":[true,false,null]}]}"
            )
            val meta = LayoutMetadataScanner.scan(file, "g1")
            assertEquals("abc", meta.id)
            assertEquals(1, meta.groups.size)
            assertEquals("g1", meta.groups[0].id)
            // id 位于 viewData 之后：与完整解析一致，不读取该组按键数据
            assertNull(meta.viewDataJson)
        } finally {
            file.delete()
        }
    }
}
