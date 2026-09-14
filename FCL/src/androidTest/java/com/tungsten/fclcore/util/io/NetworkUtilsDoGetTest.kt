package com.tungsten.fclcore.util.io

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.net.URL

/**
 * NetworkUtils.doGet 候选地址依次回退的行为。
 */
@RunWith(AndroidJUnit4::class)
class NetworkUtilsDoGetTest {

    private val badUrl = URL("https://invalid.host.fcl-test/nothing.json")
    private val goodUrl = URL("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json")

    @Test(timeout = 60_000)
    fun doGetFallsBackToLaterCandidate() {
        val content = NetworkUtils.doGet(listOf(badUrl, goodUrl))
        assertTrue(content.contains("latest"))
    }

    @Test(timeout = 60_000)
    fun doGetThrowsWithSuppressedWhenAllFail() {
        try {
            NetworkUtils.doGet(listOf(badUrl, badUrl))
            fail("全部候选失败时应抛出 IOException")
        } catch (expected: IOException) {
            // 两个候选各失败一次，聚合异常携带两个被抑制异常
            assertEquals(2, expected.suppressed.size)
        }
    }

    @Test(timeout = 60_000)
    fun doGetThrowsWhenNoCandidate() {
        try {
            NetworkUtils.doGet(emptyList())
            fail("无候选时应抛出 IOException")
        } catch (expected: IOException) {
            assertEquals("No candidate URL", expected.message)
        }
    }
}
