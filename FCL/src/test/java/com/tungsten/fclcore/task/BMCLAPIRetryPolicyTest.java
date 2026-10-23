package com.tungsten.fclcore.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 用显式传入的单调时间测试慢速节点判定，不依赖真实时钟。
 */
public class BMCLAPIRetryPolicyTest {

    /** 策略单调时间下的 1 秒 */
    private static final long SECOND = TimeUnit.SECONDS.toNanos(1);
    /** 仅测观测窗口行为时使用的大剩余下载量 */
    private static final long CONTENT_LENGTH = 100 * 1024 * 1024;

    /** 新连接必须先获得完整的观测窗口才有资格切换节点 */
    @Test
    public void waitsForCompleteObservationWindow() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(SECOND);
        assertFalse(policy.shouldRetry(1, CONTENT_LENGTH, 10 * SECOND));
        assertTrue(policy.shouldRetry(1, CONTENT_LENGTH, 11 * SECOND));
    }

    /** 前一个高速窗口不能掩盖之后持续低速的窗口 */
    @Test
    public void measuresSuccessiveWindowsIndependently() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        long downloaded = 128 * 1024 * 10;
        assertFalse(policy.shouldRetry(downloaded, CONTENT_LENGTH, 10 * SECOND));
        assertFalse(policy.shouldRetry(downloaded + 1, CONTENT_LENGTH, 19 * SECOND));
        assertTrue(policy.shouldRetry(downloaded + 1, CONTENT_LENGTH, 20 * SECOND));
    }

    /** 采样延迟时按实际经过的时长计算速度，而非名义窗口长度 */
    @Test
    public void accountsForDelayedSamples() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        assertTrue(policy.shouldRetry(128 * 1024 * 10, CONTENT_LENGTH, 20 * SECOND));
    }

    /** 高速窗口不消耗切换预算；预算跨连接累计且总数有界 */
    @Test
    public void retainsRetryBudgetAcrossAttempts() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        long windowBytes = 128 * 1024 * 10;
        for (int window = 1; window <= 5; window++) {
            assertFalse(policy.shouldRetry(windowBytes * window, CONTENT_LENGTH, window * 10 * SECOND));
        }
        assertTrue(policy.shouldRetry(windowBytes * 5 + 1, CONTENT_LENGTH, 60 * SECOND));

        policy.startAttempt(100 * SECOND);
        assertFalse(policy.shouldRetry(1, CONTENT_LENGTH, 109 * SECOND));
        assertTrue(policy.shouldRetry(1, CONTENT_LENGTH, 110 * SECOND));

        policy.startAttempt(200 * SECOND);
        assertFalse(policy.shouldRetry(1, CONTENT_LENGTH, 210 * SECOND));
        assertFalse(policy.shouldRetry(1, CONTENT_LENGTH, 220 * SECOND));
    }

    /** 小文件和接近完成的大文件应在切换节点前直接下完 */
    @Test
    public void finishesRemainingContentBeforeReselecting() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        assertFalse(policy.shouldRetry(8 * 1024, 12 * 1024, 10 * SECOND));

        policy.startAttempt(0);
        assertFalse(policy.shouldRetry(64 * 1024 * 10, 64 * 1024 * 20, 10 * SECOND));
        assertFalse(policy.shouldRetry(64 * 1024 * 20, 64 * 1024 * 20, 20 * SECOND));

        policy.startAttempt(0);
        assertTrue(policy.shouldRetry(64 * 1024 * 10, CONTENT_LENGTH, 10 * SECOND));
    }

    /** 小文件剩余内容确实遥遥无期时同样切换节点 */
    @Test
    public void reselectsStalledSmallFiles() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        assertTrue(policy.shouldRetry(1024, 12 * 1024, 10 * SECOND));
    }

    /** 长度未知的响应无法估算完成时间，不能据此放弃响应 */
    @Test
    public void keepsUnknownLengthResponses() {
        FetchTask.BMCLAPIRetryPolicy policy = new FetchTask.BMCLAPIRetryPolicy();
        policy.startAttempt(0);
        assertFalse(policy.shouldRetry(1, -1, 10 * SECOND));
    }
}
