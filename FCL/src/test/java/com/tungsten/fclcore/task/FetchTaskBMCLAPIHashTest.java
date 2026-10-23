package com.tungsten.fclcore.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 测试重定向链上 x-bmclapi-hash 的收集规则：首个有效哈希生效、冲突报错、无效值忽略。
 */
public class FetchTaskBMCLAPIHashTest {

    private static final String HASH_A = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private static final String HASH_B = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";

    /** 模拟 HttpURLConnection 的响应头：键大小写随意且含有状态行的 null 键（收集方按 entrySet 忽略大小写匹配） */
    private static Map<String, List<String>> headers(Object... pairs) {
        Map<String, List<String>> result = new java.util.LinkedHashMap<>();
        for (int i = 0; i + 1 < pairs.length; i += 2) {
            result.put((String) pairs[i], Collections.singletonList((String) pairs[i + 1]));
        }
        result.put(null, Collections.singletonList("HTTP/1.1 302 Found"));
        return result;
    }

    /** 主控 302 上的哈希生效，链上后续节点重复返回（哪怕大小写不同）不影响结果 */
    @Test
    public void firstHashWinsAcrossChain() throws IOException {
        List<Map<String, List<String>>> redirects = new ArrayList<>();
        redirects.add(headers("X-BMCLAPI-Hash", HASH_A));
        redirects.add(headers("Location", "/storage"));

        assertEquals(HASH_A, FetchTask.resolveBMCLAPIHash(redirects, headers("x-bmclapi-hash", HASH_A.toUpperCase())));
    }

    /** 链上出现互相冲突的哈希说明响应异常，应报错触发重试 */
    @Test
    public void conflictingHashThrows() throws IOException {
        List<Map<String, List<String>>> redirects = new ArrayList<>();
        redirects.add(headers("X-BMCLAPI-Hash", HASH_A));
        redirects.add(headers("X-BMCLAPI-Hash", HASH_B));

        try {
            FetchTask.resolveBMCLAPIHash(redirects, null);
            fail("Expected IOException for conflicting hashes");
        } catch (IOException e) {
            // expected
        }
    }

    /** 非 SHA-1 格式的哈希值视为无哈希，最后一个响应可提供哈希 */
    @Test
    public void invalidValuesIgnoredAndFinalResponseUsed() throws IOException {
        List<Map<String, List<String>>> redirects = new ArrayList<>();
        redirects.add(headers("X-BMCLAPI-Hash", "not-a-hash"));
        redirects.add(headers("Location", "/storage"));

        assertEquals(HASH_A, FetchTask.resolveBMCLAPIHash(redirects, headers("X-BMCLAPI-Hash", HASH_A)));
    }

    /** 整条链都没有哈希（非 BMCLAPI 下载）返回 null */
    @Test
    public void missingHashReturnsNull() throws IOException {
        List<Map<String, List<String>>> redirects = new ArrayList<>();
        redirects.add(headers("Location", "/file"));

        assertNull(FetchTask.resolveBMCLAPIHash(redirects, headers("Content-Length", "1")));
        assertNull(FetchTask.resolveBMCLAPIHash(redirects, null));
    }
}
