package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.ListChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableList;
import com.tungsten.fclcore.observable.collections.SourceAdapterChange;
import com.tungsten.fclcore.observable.value.ChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplified reimplementation of {@code ListExpressionHelper}，
 * 供 {@link ListPropertyBase} 与 {@link ReadOnlyListPropertyBase} 复用。语义对齐：
 * <ul>
 * <li>整体替换（fireValueChangedEvent()）：invalidation 监听总是触发；change 监听与
 * list-change 监听仅在新旧引用不同（!=）时触发，list-change 收到一个
 * from=0、to=新 size、removed=旧内容的 wasReplaced 变更；</li>
 * <li>内容变更（fireValueChangedEvent(Change)）：invalidation 监听触发，change 监听
 * 收到 (current, current)，list-change 监听收到 SourceAdapterChange 包装的原始变更
 * （getList() 指向属性本身，与 JavaFX 一致）；</li>
 * <li>异常转交 uncaughtExceptionHandler；每个 list-change 监听前 reset。</li>
 * </ul>
 */
final class ListPropertyHelper<E> {

    private final ReadOnlyListProperty<E> property;
    private List<InvalidationListener> invalidationListeners;
    private List<ChangeListener<? super ObservableList<E>>> changeListeners;
    private List<ListChangeListener<? super E>> listChangeListeners;
    private ObservableList<E> currentValue;

    ListPropertyHelper(ReadOnlyListProperty<E> property) {
        this.property = property;
    }

    void addListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners == null) {
            invalidationListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(invalidationListeners);
        invalidationListeners.add(listener);
    }

    void removeListener(InvalidationListener listener) {
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }

    void addListener(ChangeListener<? super ObservableList<E>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (changeListeners == null) {
            changeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(changeListeners);
        if (changeListeners.isEmpty() && (listChangeListeners == null || listChangeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        changeListeners.add(listener);
    }

    void removeListener(ChangeListener<? super ObservableList<E>> listener) {
        if (changeListeners != null) {
            changeListeners.remove(listener);
        }
    }

    void addListener(ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (listChangeListeners == null) {
            listChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(listChangeListeners);
        if (listChangeListeners.isEmpty() && (changeListeners == null || changeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        listChangeListeners.add(listener);
    }

    void removeListener(ListChangeListener<? super E> listener) {
        if (listChangeListeners != null) {
            listChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

    /** 整体替换。 */
    void fireValueChangedEvent() {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(property);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        final boolean hasChange = changeListeners != null && !changeListeners.isEmpty();
        final boolean hasListChange = listChangeListeners != null && !listChangeListeners.isEmpty();
        if (hasChange || hasListChange) {
            final ObservableList<E> oldValue = currentValue;
            currentValue = property.getValue();
            if (currentValue != oldValue) {
                if (hasChange) {
                    for (ChangeListener<? super ObservableList<E>> listener : new ArrayList<>(changeListeners)) {
                        try {
                            listener.changed(property, oldValue, currentValue);
                        } catch (Exception e) {
                            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }
                }
                if (hasListChange) {
                    final int safeSize = (currentValue == null) ? 0 : currentValue.size();
                    final List<E> removed = (oldValue == null)
                            ? Collections.<E>emptyList()
                            : new ArrayList<>(oldValue);
                    final ListChangeListener.Change<E> change = new ReplaceAllChange(safeSize, removed);
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
        }
    }

    /** 内容变更。 */
    void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(property);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (changeListeners != null) {
            for (ChangeListener<? super ObservableList<E>> listener : new ArrayList<>(changeListeners)) {
                try {
                    listener.changed(property, currentValue, currentValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (listChangeListeners != null) {
            final SourceAdapterChange<E> adapter = new SourceAdapterChange<>(property, change);
            for (ListChangeListener<? super E> listener : new ArrayList<>(listChangeListeners)) {
                try {
                    adapter.reset();
                    listener.onChanged(adapter);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    /** 对应 JavaFX 的 NonIterableChange.GenericAddRemoveChange（仅整体替换场景）。 */
    private final class ReplaceAllChange extends ListChangeListener.Change<E> {

        private final int to;
        private final List<E> removed;
        private boolean onChange = false;

        ReplaceAllChange(int to, List<E> removed) {
            super(property);
            this.to = to;
            this.removed = removed;
        }

        @Override
        public boolean next() {
            if (onChange) {
                return false;
            }
            onChange = true;
            return true;
        }

        @Override
        public void reset() {
            onChange = false;
        }

        @Override
        public int getFrom() {
            return 0;
        }

        @Override
        public int getTo() {
            return to;
        }

        @Override
        public List<E> getRemoved() {
            return removed;
        }

        @Override
        protected int[] getPermutation() {
            return new int[0];
        }
    }
}
