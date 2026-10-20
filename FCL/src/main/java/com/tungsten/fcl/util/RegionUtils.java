/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2026 huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fcl.util;

import static com.tungsten.fclcore.util.Logging.LOG;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tungsten.fcl.FCLApp;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.util.io.NetworkUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * 地区判定，移植自 HMCL 的 RegionUtils（HMCL-dev/HMCL#7017），并合入 #7020 对 UTC+8 非中国大陆时区的排除。
 * <ul>
 *     <li>设备地区（时区 + 系统地区，不联网）：用于镜像源先后、联机节点筛选等偏好；</li>
 *     <li>网络地区（设备地区为中国大陆时再查询出口 IP 归属地）：用于按相关法律法规限制游戏内容下载与陶瓦联机。
 *     只在用户同意用户协议与隐私政策后联网查询；查询失败、返回异常、尚未完成或用户尚未同意时一律按中国大陆处理。</li>
 * </ul>
 */
public final class RegionUtils {

    /** 我的世界中国版官网，受限提示引导用户前往 */
    public static final String CHINA_EDITION_URL = "https://mc.163.com/";

    private static final String NETWORK_INFO_URL = "https://ipwho.is";

    private static final int UTC_8_OFFSET_SECONDS = 8 * 3600;

    /** 同为 UTC+8 但不属于中国大陆的时区：命中时不联网查询，直接判定网络不在中国大陆（HMCL #7020） */
    private static final Set<String> NOT_CHINA_MAINLAND_REGION_TIMEZONES = Set.of(
            "Antarctica/Casey",
            "Asia/Hong_Kong",
            "Asia/Irkutsk",
            "Asia/Kuching",
            "Asia/Macau",
            "Asia/Makassar",
            "Asia/Manila",
            "Asia/Singapore",
            "Asia/Taipei",
            "Asia/Ulaanbaatar",
            "Australia/Perth"
    );

    private static final boolean IS_CHINA_MAINLAND_MACHINE = checkMachineIsChinaMainland();

    private static volatile CompletableFuture<Boolean> chinaMainlandNetwork;

    private RegionUtils() {
    }

    /**
     * 启动器启动时预先发起网络地区检测，进入下载、联机页面时结果通常已就绪
     */
    public static void init() {
        chinaMainlandNetworkAsync();
    }

    private static boolean checkMachineIsChinaMainland() {
        if ("Asia/Shanghai".equals(ZoneId.systemDefault().getId()))
            return true;

        // 时区为 UTC+8 时再看系统地区
        if (ZonedDateTime.now().getOffset().getTotalSeconds() == UTC_8_OFFSET_SECONDS)
            return "CN".equals(Locale.getDefault().getCountry());

        return false;
    }

    /**
     * 设备地区是否为中国大陆（仅凭时区与系统地区判断，不联网）
     */
    public static boolean isChinaMainlandMachine() {
        return IS_CHINA_MAINLAND_MACHINE;
    }

    /** 设备地区为中国大陆、当前仍为 UTC+8 且不在已知的非中国大陆时区时，才需要联网确认 */
    private static boolean shouldCheckNetwork() {
        return IS_CHINA_MAINLAND_MACHINE
                && ZonedDateTime.now().getOffset().getTotalSeconds() == UTC_8_OFFSET_SECONDS
                && !NOT_CHINA_MAINLAND_REGION_TIMEZONES.contains(ZoneId.systemDefault().getId());
    }

    private static boolean checkChinaMainlandNetwork() {
        try {
            LOG.info("Fetching network information");
            JsonObject response = JsonParser.parseString(NetworkUtils.doGet(NetworkUtils.toURL(NETWORK_INFO_URL))).getAsJsonObject();
            JsonElement success = response.get("success");
            JsonElement countryCode = response.get("country_code");
            String country = countryCode != null && countryCode.isJsonPrimitive() ? countryCode.getAsString() : null;
            boolean result = success == null || !success.isJsonPrimitive() || !success.getAsBoolean()
                    || "CN".equalsIgnoreCase(country);
            LOG.info("Network country: " + country + ", treated as China Mainland: " + result);
            return result;
        } catch (Throwable e) {
            LOG.log(Level.WARNING, "Failed to get the network information", e);
            return true;
        }
    }

    /**
     * 网络地区检测结果，true 表示按中国大陆处理（检测失败同样为 true）。
     * 仅在 {@link #shouldCheckNetwork()} 成立且用户已同意用户协议与隐私政策时于 IO 线程联网查询，
     * 不需要查询时立即得到 false；结果在进程内缓存。
     */
    public static CompletableFuture<Boolean> chinaMainlandNetworkAsync() {
        CompletableFuture<Boolean> future = chinaMainlandNetwork;
        if (future != null)
            return future;

        synchronized (RegionUtils.class) {
            future = chinaMainlandNetwork;
            if (future == null) {
                if (!shouldCheckNetwork()) {
                    future = CompletableFuture.completedFuture(false);
                } else if (!LegalAgreement.isAccepted(FCLApp.getAppContext())) {
                    // 同意前不联网：按中国大陆处理且不缓存，同意后再真正检测
                    return CompletableFuture.completedFuture(true);
                } else {
                    future = CompletableFuture.supplyAsync(RegionUtils::checkChinaMainlandNetwork, Schedulers.io());
                }
                chinaMainlandNetwork = future;
            }
            return future;
        }
    }

    /**
     * 网络地区是否为中国大陆（非阻塞，可在主线程调用）：检测尚未完成时按中国大陆处理。
     * 需要在检测完成后刷新界面时使用 {@link #observeChinaMainlandNetwork(Consumer)}。
     */
    public static boolean isChinaMainlandNetwork() {
        return chinaMainlandNetworkAsync().getNow(true);
    }

    /**
     * 在主线程调用：立即以当前结果回调一次（检测未完成时按中国大陆处理）；
     * 检测仍在进行时，完成后再在主线程以最终结果回调一次。
     */
    public static void observeChinaMainlandNetwork(Consumer<Boolean> callback) {
        CompletableFuture<Boolean> future = chinaMainlandNetworkAsync();
        Boolean result = future.getNow(null);
        if (result != null) {
            callback.accept(result);
        } else {
            callback.accept(true);
            future.thenAcceptAsync(callback, Schedulers.androidUIThread());
        }
    }
}
