package com.tungsten.fclcore.util.flow;

import java.lang.ref.WeakReference;

import kotlinx.coroutines.flow.MutableStateFlow;

/**
 * StateFlow 双向绑定工具（替代 observable 时代的 {@code bindBidirectional}）。
 *
 * <p>语义对齐：绑定时先把 b 的当前值同步到 a（对齐 BidirectionalBinding 的
 * property1 ← property2 初值同步），随后任一侧变化同步到另一侧；
 * StateFlow 同值不发射 + updating 守卫双重防回环。</p>
 *
 * <p>两侧均以弱引用持有：任一侧 owner（视图/模型）被 GC 后绑定自然失效，
 * 对齐原双向绑定的弱引用语义，不会把视图泄漏进共享订阅作用域。</p>
 *
 * <p>返回可取消句柄（两个方向一起取消），对齐 {@code unbindBidirectional}；
 * 长寿命绑定（设置页）可忽略返回值。</p>
 */
public final class FlowBindings {
    private FlowBindings() {
    }

    public static <T> FlowSubscriptions.Subscription bindBidirectional(MutableStateFlow<T> a, MutableStateFlow<T> b) {
        a.setValue(b.getValue());
        boolean[] updating = { false };
        WeakReference<MutableStateFlow<T>> aRef = new WeakReference<>(a);
        WeakReference<MutableStateFlow<T>> bRef = new WeakReference<>(b);
        FlowSubscriptions.Subscription ab = FlowSubscriptions.subscribe(a, v -> {
            if (!updating[0]) {
                updating[0] = true;
                MutableStateFlow<T> bb = bRef.get();
                if (bb != null) {
                    bb.setValue(v);
                }
                updating[0] = false;
            }
        });
        FlowSubscriptions.Subscription ba = FlowSubscriptions.subscribe(b, v -> {
            if (!updating[0]) {
                updating[0] = true;
                MutableStateFlow<T> aa = aRef.get();
                if (aa != null) {
                    aa.setValue(v);
                }
                updating[0] = false;
            }
        });
        return () -> {
            ab.cancel();
            ba.cancel();
        };
    }
}
