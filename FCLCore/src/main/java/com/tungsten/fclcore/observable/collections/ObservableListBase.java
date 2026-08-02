package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simplified reimplementation of {@code ObservableListBase} +
 * {@code ModifiableObservableListBase}（合并为一层），语义对齐：
 * <ul>
 * <li>add/set/remove/addAll/removeAll/retainAll/setAll/removeRange 等 mutation
 * 都会在 beginChange/endChange 块内记录子变更，最外层 endChange 时合并为一次
 * Change 事件分发；</li>
 * <li>无监听器时不分发事件（与 JavaFX 一致）；</li>
 * <li>分发前先触发 invalidation 监听，再逐个 ListChangeListener（每个之前 reset）；</li>
 * <li>监听抛出的异常转交当前线程的 uncaughtExceptionHandler。</li>
 * </ul>
 * 与 JavaFX 的差异：子变更不做相邻合并优化（监听者按序遍历的结果等价）。
 */
public abstract class ObservableListBase<E> extends AbstractList<E> implements ObservableList<E> {

    // ================================================================================================================
    // 监听者管理（对应 JavaFX 的 ListListenerHelper）

    private List<InvalidationListener> invalidationListeners;
    private List<ListChangeListener<? super E>> listChangeListeners;

    @Override
    public final void addListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners == null) {
            invalidationListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(invalidationListeners);
        invalidationListeners.add(listener);
    }

    @Override
    public final void removeListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }

    @Override
    public final void addListener(ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (listChangeListeners == null) {
            listChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(listChangeListeners);
        listChangeListeners.add(listener);
    }

    @Override
    public final void removeListener(ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (listChangeListeners != null) {
            listChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

    protected final boolean hasListeners() {
        return (invalidationListeners != null && !invalidationListeners.isEmpty())
                || (listChangeListeners != null && !listChangeListeners.isEmpty());
    }

    protected final void fireChange(ListChangeListener.Change<? extends E> change) {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(this);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (listChangeListeners != null) {
            for (ListChangeListener<? super E> listener : new ArrayList<>(listChangeListeners)) {
                try {
                    change.reset();
                    listener.onChanged(change);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    // ================================================================================================================
    // 变更构建（对应 JavaFX 的 ListChangeBuilder，简化为逐条记录子变更）

    private static final int ADD = 0;
    private static final int REMOVE = 1;
    private static final int REPLACE = 2;
    private static final int UPDATE = 3;
    private static final int PERM = 4;

    private static final class SubChange<E> {
        final int kind;
        final int from;
        final int to;
        final List<E> removed;
        final int[] perm;

        SubChange(int kind, int from, int to, List<E> removed, int[] perm) {
            this.kind = kind;
            this.from = from;
            this.to = to;
            this.removed = removed;
            this.perm = perm;
        }
    }

    private int changeLock;
    private List<SubChange<E>> pendingChanges;

    protected final void beginChange() {
        if (changeLock++ == 0) {
            pendingChanges = new ArrayList<>();
        }
    }

    protected final void endChange() {
        if (changeLock == 0) {
            throw new IllegalStateException("Called endChange() without calling beginChange() first");
        }
        if (--changeLock == 0) {
            final List<SubChange<E>> changes = pendingChanges;
            pendingChanges = null;
            if (!changes.isEmpty() && hasListeners()) {
                fireChange(new ComposedChange<>(this, changes));
            }
        }
    }

    private List<SubChange<E>> requirePending() {
        if (changeLock == 0) {
            throw new IllegalStateException("Called nextXxx() without calling beginChange() first");
        }
        return pendingChanges;
    }

    protected final void nextUpdate(int pos) {
        requirePending().add(new SubChange<>(UPDATE, pos, pos + 1, null, null));
    }

    protected final void nextSet(int idx, E old) {
        requirePending().add(new SubChange<>(REPLACE, idx, idx + 1,
                new ArrayList<>(Collections.singletonList(old)), null));
    }

    protected final void nextReplace(int from, int to, List<? extends E> removed) {
        requirePending().add(new SubChange<>(REPLACE, from, to, new ArrayList<>(removed), null));
    }

    protected final void nextRemove(int idx, List<? extends E> removed) {
        requirePending().add(new SubChange<>(REMOVE, idx, idx, new ArrayList<>(removed), null));
    }

    protected final void nextRemove(int idx, E removed) {
        requirePending().add(new SubChange<>(REMOVE, idx, idx,
                new ArrayList<>(Collections.singletonList(removed)), null));
    }

    protected final void nextPermutation(int from, int to, int[] perm) {
        requirePending().add(new SubChange<>(PERM, from, to, null, perm.clone()));
    }

    protected final void nextAdd(int from, int to) {
        requirePending().add(new SubChange<>(ADD, from, to, null, null));
    }

    private static final class ComposedChange<E> extends ListChangeListener.Change<E> {

        private final List<SubChange<E>> subChanges;
        private int cursor = -1;

        ComposedChange(ObservableList<E> list, List<SubChange<E>> subChanges) {
            super(list);
            this.subChanges = subChanges;
        }

        @Override
        public boolean next() {
            return ++cursor < subChanges.size();
        }

        @Override
        public void reset() {
            cursor = -1;
        }

        private SubChange<E> current() {
            if (cursor < 0 || cursor >= subChanges.size()) {
                throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change");
            }
            return subChanges.get(cursor);
        }

        @Override
        public int getFrom() {
            return current().from;
        }

        @Override
        public int getTo() {
            return current().to;
        }

        @Override
        public List<E> getRemoved() {
            final List<E> removed = current().removed;
            return removed == null ? Collections.<E>emptyList() : removed;
        }

        @Override
        protected int[] getPermutation() {
            final int[] perm = current().perm;
            return perm == null ? new int[0] : perm;
        }

        @Override
        public boolean wasUpdated() {
            return current().kind == UPDATE;
        }
    }

    // ================================================================================================================
    // mutation 入口（对应 JavaFX 的 ModifiableObservableListBase）

    @Override
    public void add(int index, E element) {
        doAdd(index, element);
        beginChange();
        nextAdd(index, index + 1);
        ++modCount;
        endChange();
    }

    @Override
    public E set(int index, E element) {
        E old = doSet(index, element);
        beginChange();
        nextSet(index, old);
        endChange();
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i != -1) {
            remove(i);
            return true;
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E old = doRemove(index);
        beginChange();
        nextRemove(index, old);
        ++modCount;
        endChange();
        return old;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        beginChange();
        try {
            return super.addAll(c);
        } finally {
            endChange();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        beginChange();
        try {
            return super.addAll(index, c);
        } finally {
            endChange();
        }
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        beginChange();
        try {
            super.removeRange(fromIndex, toIndex);
        } finally {
            endChange();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        beginChange();
        try {
            return super.removeAll(c);
        } finally {
            endChange();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        beginChange();
        try {
            return super.retainAll(c);
        } finally {
            endChange();
        }
    }

    @Override
    public boolean setAll(Collection<? extends E> col) {
        if (isEmpty() && col.isEmpty()) {
            return false;
        }
        beginChange();
        try {
            clear();
            addAll(col);
            return true;
        } finally {
            endChange();
        }
    }

    @Override
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }

    @Override
    public boolean setAll(E... elements) {
        return setAll(Arrays.asList(elements));
    }

    @Override
    public boolean removeAll(E... elements) {
        return removeAll(Arrays.asList(elements));
    }

    @Override
    public boolean retainAll(E... elements) {
        return retainAll(Arrays.asList(elements));
    }

    @Override
    public void remove(int from, int to) {
        removeRange(from, to);
    }

    @Override
    public abstract E get(int index);

    @Override
    public abstract int size();

    protected abstract void doAdd(int index, E element);

    protected abstract E doSet(int index, E element);

    protected abstract E doRemove(int index);
}
