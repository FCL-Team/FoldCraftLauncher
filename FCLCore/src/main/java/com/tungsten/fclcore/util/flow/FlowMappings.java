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
package com.tungsten.fclcore.util.flow;

import static com.tungsten.fclcore.util.Logging.LOG;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Level;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * StateFlow 的映射工具（对齐原 {@code BindingMapping} 语义）。
 *
 * - {@link #map}：同步映射。源值未变化不重复计算（StateFlow 去重），
 *   计算回调在发射线程同步执行。
 * - {@link #asyncMap}：异步映射。仅最新一次计算结果生效（代际守卫），
 *   计算失败保留旧值并记警告。
 *
 * 生命周期：内部订阅对目标 flow 仅持弱引用，目标被 GC 后下一次发射时
 * 自动取消订阅——对齐原绑定弱监听被 GC 摘除的语义。
 */
public final class FlowMappings {
    private FlowMappings() {
    }

    public static <S, T> StateFlow<T> map(StateFlow<S> source, Function<? super S, ? extends T> mapper) {
        MutableStateFlow<T> target = StateFlowKt.MutableStateFlow(mapper.apply(source.getValue()));
        WeakReference<MutableStateFlow<T>> ref = new WeakReference<>(target);
        AtomicReference<FlowSubscriptions.Subscription> holder = new AtomicReference<>();
        holder.set(FlowSubscriptions.subscribe(source, value -> {
            MutableStateFlow<T> t = ref.get();
            if (t == null) {
                FlowSubscriptions.Subscription subscription = holder.get();
                if (subscription != null) {
                    subscription.cancel();
                }
                return;
            }
            t.setValue(mapper.apply(value));
        }));
        return target;
    }

    public static <S, T> StateFlow<T> asyncMap(StateFlow<S> source, Function<? super S, ? extends CompletableFuture<? extends T>> mapper, T initial) {
        MutableStateFlow<T> target = StateFlowKt.MutableStateFlow(initial);
        WeakReference<MutableStateFlow<T>> ref = new WeakReference<>(target);
        AtomicLong generation = new AtomicLong();
        AtomicReference<FlowSubscriptions.Subscription> holder = new AtomicReference<>();
        holder.set(FlowSubscriptions.subscribeWithCurrent(source, value -> {
            MutableStateFlow<T> t = ref.get();
            if (t == null) {
                FlowSubscriptions.Subscription subscription = holder.get();
                if (subscription != null) {
                    subscription.cancel();
                }
                return;
            }
            long gen = generation.incrementAndGet();
            mapper.apply(value).handle((result, e) -> {
                if (e == null) {
                    if (generation.get() == gen) {
                        MutableStateFlow<T> current = ref.get();
                        if (current != null) {
                            current.setValue(result);
                        }
                    }
                } else {
                    LOG.log(Level.WARNING, "Failed to compute mapped value", e);
                }
                return null;
            });
        }));
        return target;
    }
}
