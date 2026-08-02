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

import com.tungsten.fclcore.util.function.ExceptionalFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 带异步加载与失效刷新的键值缓存（纯 StateFlow 实现）。
 *
 * 语义对齐原 {@code ObservableOptionalCache}/{@code ObservableCache}：
 * - 未命中或已失效的键在建立非静默 flow 时触发异步加载；加载完成/ {@link #put} /
 *   {@link #invalidate} 后所有已建立的 flow 同步到各自键的最新缓存值；
 * - 同一键的并发加载去重（{@code pendings}）；加载失败仅回调异常处理器，不改动缓存；
 * - {@link #invalidate} 仅标记失效并触发后续重取，flow 在重取完成前保持旧值可见。
 *
 * 与原实现的唯一结构差异：原实现用共享失效信号让所有绑定各自重算，
 * 这里按键直接推送当前缓存值——消费方观察到的值序列一致。
 */
public class FlowOptionalCache<K, V, E extends Exception> {

    private final ExceptionalFunction<K, Optional<V>, E> source;
    private final BiConsumer<K, Throwable> exceptionHandler;
    private final Executor executor;
    private final Map<K, Optional<V>> cache = new HashMap<>();
    private final Map<K, CompletableFuture<Optional<V>>> pendings = new HashMap<>();
    private final Map<K, Boolean> invalidated = new HashMap<>();
    private final Map<K, KeyFlow> flows = new HashMap<>();

    private final class KeyFlow {
        final MutableStateFlow<Optional<V>> flow;
        final boolean quiet;

        KeyFlow(MutableStateFlow<Optional<V>> flow, boolean quiet) {
            this.flow = flow;
            this.quiet = quiet;
        }
    }

    public FlowOptionalCache(ExceptionalFunction<K, Optional<V>, E> source, BiConsumer<K, Throwable> exceptionHandler, Executor executor) {
        this.source = source;
        this.exceptionHandler = exceptionHandler;
        this.executor = executor;
    }

    /** 已缓存则返回缓存值，否则 empty；不触发加载。 */
    public Optional<V> getImmediately(K key) {
        synchronized (this) {
            Optional<V> cached = cache.get(key);
            return cached == null ? Optional.empty() : cached;
        }
    }

    public void put(K key, V value) {
        synchronized (this) {
            cache.put(key, Optional.of(value));
            invalidated.remove(key);
        }
        onCacheChanged();
    }

    public void invalidate(K key) {
        synchronized (this) {
            if (cache.containsKey(key)) {
                invalidated.put(key, Boolean.TRUE);
            }
        }
        onCacheChanged();
    }

    /** 建立键的 StateFlow（非静默）：缺值/已失效时立即触发异步加载。 */
    public StateFlow<Optional<V>> bindingFlow(K key) {
        return bindingFlow(key, false);
    }

    /**
     * @param quiet 为 true 时建立 flow 不触发加载（仅观察后续变化），
     *              对齐原 {@code binding(key, true)} 语义。
     */
    public StateFlow<Optional<V>> bindingFlow(K key, boolean quiet) {
        KeyFlow keyFlow;
        boolean refresh = false;
        synchronized (this) {
            keyFlow = flows.get(key);
            if (keyFlow == null) {
                Optional<V> cached = cache.get(key);
                Optional<V> value = cached == null ? Optional.empty() : cached;
                refresh = !quiet && (cached == null || invalidated.containsKey(key));
                keyFlow = new KeyFlow(StateFlowKt.MutableStateFlow(value), quiet);
                flows.put(key, keyFlow);
            } else if (!quiet) {
                // 已有 flow 时的非静默访问同样承担"缺值/失效即触发加载"语义
                Optional<V> cached = cache.get(key);
                refresh = cached == null || invalidated.containsKey(key);
            }
        }
        if (refresh) {
            query(key, executor);
        }
        return keyFlow.flow;
    }

    private void query(K key, Executor executor) {
        CompletableFuture<Optional<V>> future;
        synchronized (this) {
            CompletableFuture<Optional<V>> prev = pendings.get(key);
            if (prev != null) {
                return;
            }
            future = new CompletableFuture<>();
            pendings.put(key, future);
        }

        CompletableFuture<Optional<V>> finalFuture = future;
        executor.execute(() -> {
            Optional<V> result;
            try {
                result = source.apply(key);
            } catch (Throwable ex) {
                synchronized (this) {
                    pendings.remove(key);
                }
                exceptionHandler.accept(key, ex);
                finalFuture.completeExceptionally(ex);
                return;
            }

            synchronized (this) {
                cache.put(key, result);
                invalidated.remove(key);
                pendings.remove(key, finalFuture);
            }
            finalFuture.complete(result);
            onCacheChanged();
        });
    }

    /** 缓存任意变动后，把各键最新值推送到已建立的 flow，并补触发非静默键的加载。 */
    private void onCacheChanged() {
        List<Map.Entry<K, KeyFlow>> snapshot;
        synchronized (this) {
            snapshot = new ArrayList<>(flows.entrySet());
        }
        for (Map.Entry<K, KeyFlow> entry : snapshot) {
            K key = entry.getKey();
            KeyFlow keyFlow = entry.getValue();
            Optional<V> value;
            boolean refresh;
            synchronized (this) {
                Optional<V> cached = cache.get(key);
                value = cached == null ? Optional.empty() : cached;
                refresh = !keyFlow.quiet && (cached == null || invalidated.containsKey(key));
            }
            keyFlow.flow.setValue(value);
            if (refresh) {
                query(key, executor);
            }
        }
    }
}
