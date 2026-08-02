package com.tungsten.fcl.util;

import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.property.ObjectProperty;
import com.tungsten.fclcore.observable.property.SimpleBooleanProperty;
import com.tungsten.fclcore.observable.property.SimpleObjectProperty;
import com.tungsten.fclcore.util.flow.FlowSubscriptions;

import kotlinx.coroutines.flow.StateFlow;

/**
 * StateFlow → FCL 内部 observable 的迁移接缝。
 *
 * <p>用于 FCLCore 公开 API 已 StateFlow 化、而 FCL 内部状态（Config/列表/旧 View 绑定）
 * 仍走 observable 的过渡期场景：把 Flow 镜像回 Observable/Property，
 * 保持下游绑定与 extractor 冒泡语义不变。</p>
 *
 * <p>生命周期说明：镜像订阅不可取消，存活期与进程一致（等价于原代码中被
 * {@code bind}/{@code Bindings} 长期持有的绑定对象）；调用点均为应用级长寿命对象
 * （Accounts/Config 静态列表、MainActivity 绑定）。新代码请勿以此桥扩散 observable 使用面，
 * 应直接消费 StateFlow。</p>
 */
public final class FlowObservables {
    private FlowObservables() {
    }

    /**
     * 把 StateFlow 镜像为一个随发射翻转的 {@link Observable}（失效信号），
     * 跳过当前值（对齐 addListener 语义）。可用作 {@code Bindings.create*Binding} 的依赖。
     */
    public static Observable toObservable(StateFlow<?> flow) {
        SimpleBooleanProperty signal = new SimpleBooleanProperty(FlowObservables.class, "flowSignal", false);
        FlowSubscriptions.subscribe(flow, value -> signal.set(!signal.get()));
        return signal;
    }

    /**
     * 把 StateFlow 镜像为一个 {@link ObjectProperty}：初值取当前值，随后跟踪每次发射。
     */
    public static <T> ObjectProperty<T> toProperty(StateFlow<T> flow) {
        SimpleObjectProperty<T> property = new SimpleObjectProperty<>(FlowObservables.class, "flowValue", flow.getValue());
        FlowSubscriptions.subscribe(flow, property::set);
        return property;
    }
}
