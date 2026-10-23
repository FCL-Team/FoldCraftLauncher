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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * 地区判定，移植自 HMCL 的 RegionUtils（HMCL-dev/HMCL#7017），并合入 #7020 对 UTC+8 非中国大陆时区的排除。
 * 仅凭本机时区与系统地区判断，不联网、不查询网络归属地（FoldCraftLauncher#1955）。
 */
public final class RegionUtils {

    private static final int UTC_8_OFFSET_SECONDS = 8 * 3600;

    private static final boolean IS_CHINA_MAINLAND_MACHINE = checkMachineIsChinaMainland();

    private RegionUtils() {
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
}
