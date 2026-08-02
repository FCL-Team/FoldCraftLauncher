package com.tungsten.fcl.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/**
 * 快照式可变列表（阶段 4b）：替代 control/data 域的 ObservableList。
 *
 * <p>所有变更方法先拷贝再整体替换为不可变快照并发射到 {@link #flow()}，
 * 订阅方收到的是全新的不可变 {@link List}（对齐 4a 各域 StateFlow&lt;List&gt; 快照语义）。
 * StateFlow 同值（内容 equals）不发射，与原 ObservableList 同内容 setAll 也发事件的
 * 差异仅在"无变化不通知"，消费方（存盘/刷新）均幂等，无行为差异。</p>
 *
 * <p>与 fakefx 一致：不做隐式线程切换，发射线程即订阅回调线程
 * （经 {@code FlowSubscriptions} 订阅时）。</p>
 */
public final class FlowList<T> implements Iterable<T> {

    private final MutableStateFlow<List<T>> flow;

    public FlowList() {
        this(Collections.emptyList());
    }

    public FlowList(List<? extends T> initial) {
        flow = StateFlowKt.MutableStateFlow(snapshot(initial));
    }

    private static <T> List<T> snapshot(Collection<? extends T> items) {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    /** 列表快照流（每次变更发射新的不可变快照）。 */
    public StateFlow<List<T>> flow() {
        return flow;
    }

    /** 当前不可变快照。 */
    public List<T> get() {
        return flow.getValue();
    }

    public void setAll(Collection<? extends T> items) {
        flow.setValue(snapshot(items));
    }

    public boolean add(T item) {
        List<T> list = new ArrayList<>(flow.getValue());
        list.add(item);
        flow.setValue(snapshot(list));
        return true;
    }

    public void add(int index, T item) {
        List<T> list = new ArrayList<>(flow.getValue());
        list.add(index, item);
        flow.setValue(snapshot(list));
    }

    public boolean addAll(Collection<? extends T> items) {
        if (items.isEmpty())
            return false;
        List<T> list = new ArrayList<>(flow.getValue());
        list.addAll(items);
        flow.setValue(snapshot(list));
        return true;
    }

    public boolean remove(Object item) {
        List<T> list = new ArrayList<>(flow.getValue());
        if (!list.remove(item))
            return false;
        flow.setValue(snapshot(list));
        return true;
    }

    public T removeAt(int index) {
        List<T> list = new ArrayList<>(flow.getValue());
        T removed = list.remove(index);
        flow.setValue(snapshot(list));
        return removed;
    }

    public void swap(int i, int j) {
        List<T> list = new ArrayList<>(flow.getValue());
        Collections.swap(list, i, j);
        flow.setValue(snapshot(list));
    }

    public void clear() {
        if (flow.getValue().isEmpty())
            return;
        flow.setValue(Collections.emptyList());
    }

    public int size() {
        return flow.getValue().size();
    }

    public boolean isEmpty() {
        return flow.getValue().isEmpty();
    }

    public boolean contains(Object item) {
        return flow.getValue().contains(item);
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return flow.getValue().iterator();
    }
}
