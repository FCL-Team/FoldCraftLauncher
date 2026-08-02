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
package com.tungsten.fclcore.util.observable;

import com.tungsten.fclcore.observable.value.ChangeListener;
import com.tungsten.fclcore.observable.value.ObservableValue;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * observable → StateFlow 的迁移接缝（FCLCore 内部使用）。
 *
 * 仅供 FCLCore 内部把既有的惰性 {@link ObservableValue}（如账户纹理的 ObjectBinding）
 * 镜像为 StateFlow 暴露给新的公开 API；调用方侧请直接使用 StateFlow，
 * 不要以此桥扩散 observable 类型的使用面。
 *
 * 生命周期：镜像订阅持有强引用，存活期与源 {@link ObservableValue} 一致；
 * 源被 GC 时整条镜像链一并回收（与源绑定的既有弱监听语义兼容）。
 */
public final class FlowBridge {
    private FlowBridge() {
    }

    /**
     * 把 {@link ObservableValue} 镜像为 {@link StateFlow}：初值取当前值，
     * 之后每次变更（ChangeListener 语义，值实际变化才触发）同步到 Flow。
     */
    public static <T> StateFlow<T> asStateFlow(ObservableValue<T> value) {
        MutableStateFlow<T> flow = StateFlowKt.MutableStateFlow(value.getValue());
        value.addListener((ChangeListener<T>) (observable, oldValue, newValue) -> flow.setValue(newValue));
        return flow;
    }
}
