package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.ObservableSet;
import com.tungsten.fclcore.observable.collections.SetChangeListener;
import com.tungsten.fclcore.observable.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified reimplementation of {@code SetExpressionHelper}，语义对齐：
 * <ul>
 * <li>整体替换：invalidation 总是触发；change/set-change 仅在新旧引用不同（!=）时触发，
 * set-change 逐元素先收旧元素 removed、再收新元素 added；</li>
 * <li>内容变更：invalidation 触发，change 收到 (current, current)，set-change 收到原始变更。</li>
 * </ul>
 */
final class SetPropertyHelper<E> {

    private final ReadOnlySetProperty<E> property;
    private List<InvalidationListener> invalidationListeners;
    private List<ChangeListener<? super ObservableSet<E>>> changeListeners;
    private List<SetChangeListener<? super E>> setChangeListeners;
    private ObservableSet<E> currentValue;

    SetPropertyHelper(ReadOnlySetProperty<E> property) {
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

    void addListener(ChangeListener<? super ObservableSet<E>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (changeListeners == null) {
            changeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(changeListeners);
        if (changeListeners.isEmpty() && (setChangeListeners == null || setChangeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        changeListeners.add(listener);
    }

    void removeListener(ChangeListener<? super ObservableSet<E>> listener) {
        if (changeListeners != null) {
            changeListeners.remove(listener);
        }
    }

    void addListener(SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (setChangeListeners == null) {
            setChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(setChangeListeners);
        if (setChangeListeners.isEmpty() && (changeListeners == null || changeListeners.isEmpty())) {
            currentValue = property.getValue();
        }
        setChangeListeners.add(listener);
    }

    void removeListener(SetChangeListener<? super E> listener) {
        if (setChangeListeners != null) {
            setChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

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
        final boolean hasSetChange = setChangeListeners != null && !setChangeListeners.isEmpty();
        if (hasChange || hasSetChange) {
            final ObservableSet<E> oldValue = currentValue;
            currentValue = property.getValue();
            if (currentValue != oldValue) {
                if (hasChange) {
                    for (ChangeListener<? super ObservableSet<E>> listener : new ArrayList<>(changeListeners)) {
                        try {
                            listener.changed(property, oldValue, currentValue);
                        } catch (Exception e) {
                            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                        }
                    }
                }
                if (hasSetChange) {
                    final List<SetChangeListener.Change<E>> changes = new ArrayList<>();
                    if (oldValue != null) {
                        for (E element : oldValue) {
                            changes.add(new SimpleChange(null, element));
                        }
                    }
                    if (currentValue != null) {
                        for (E element : currentValue) {
                            changes.add(new SimpleChange(element, null));
                        }
                    }
                    for (SetChangeListener.Change<E> change : changes) {
                        for (SetChangeListener<? super E> listener : new ArrayList<>(setChangeListeners)) {
                            try {
                                listener.onChanged(change);
                            } catch (Exception e) {
                                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
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
            for (ChangeListener<? super ObservableSet<E>> listener : new ArrayList<>(changeListeners)) {
                try {
                    listener.changed(property, currentValue, currentValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (setChangeListeners != null) {
            for (SetChangeListener<? super E> listener : new ArrayList<>(setChangeListeners)) {
                try {
                    listener.onChanged(change);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    private final class SimpleChange extends SetChangeListener.Change<E> {

        private final E added;
        private final E removed;

        SimpleChange(E added, E removed) {
            super(property);
            this.added = added;
            this.removed = removed;
        }

        @Override
        public E getElementAdded() {
            return added;
        }

        @Override
        public E getElementRemoved() {
            return removed;
        }
    }
}
