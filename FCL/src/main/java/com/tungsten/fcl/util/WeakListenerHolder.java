/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
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

import java.util.ArrayList;
import java.util.List;

/**
 * 弱注册（EventBus registerWeak 等）的强引用兜底容器：只要本持有者存活，
 * 被弱引用包装的目标就不会被 GC 提前回收。
 *
 * <p>阶段 4c：observable 体系的 WeakXxxListener 工厂方法已随 fakefx 移除删除，
 * 本类仅保留通用的 add/remove 引用管理。</p>
 */
public class WeakListenerHolder {
    private final List<Object> refs = new ArrayList<>(0);

    public WeakListenerHolder() {
    }

    public void add(Object obj) {
        refs.add(obj);
    }

    public boolean remove(Object obj) {
        return refs.remove(obj);
    }
}
