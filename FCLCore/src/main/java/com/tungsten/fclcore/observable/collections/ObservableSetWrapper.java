package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.WeakListener;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Simplified reimplementation of {@code ObservableSetWrapper}：
 * add/remove/clear 在实际变更时触发 SetChange 事件；分发前先发 invalidation 监听。
 */
public class ObservableSetWrapper<E> extends AbstractSet<E> implements ObservableSet<E> {

    private final Set<E> backingSet;
    private List<InvalidationListener> invalidationListeners;
    private List<SetChangeListener<? super E>> setChangeListeners;

    public ObservableSetWrapper(Set<E> set) {
        this.backingSet = set;
    }

    @Override
    public void addListener(InvalidationListener listener) {
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
    public void removeListener(InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (invalidationListeners != null) {
            invalidationListeners.remove(listener);
        }
    }

    @Override
    public void addListener(SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (setChangeListeners == null) {
            setChangeListeners = new ArrayList<>(1);
        }
        purgeGarbageCollected(setChangeListeners);
        setChangeListeners.add(listener);
    }

    @Override
    public void removeListener(SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (setChangeListeners != null) {
            setChangeListeners.remove(listener);
        }
    }

    private static void purgeGarbageCollected(List<?> listeners) {
        listeners.removeIf(l -> l instanceof WeakListener && ((WeakListener) l).wasGarbageCollected());
    }

    private void fireChange(E added, E removed) {
        if (invalidationListeners != null) {
            for (InvalidationListener listener : new ArrayList<>(invalidationListeners)) {
                try {
                    listener.invalidated(this);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
        if (setChangeListeners != null) {
            final SetChangeListener.Change<E> change = new SimpleChange(added, removed);
            for (SetChangeListener<? super E> listener : new ArrayList<>(setChangeListeners)) {
                try {
                    listener.onChanged(change);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }
    }

    private class SimpleChange extends SetChangeListener.Change<E> {

        private final E added;
        private final E removed;

        SimpleChange(E added, E removed) {
            super(ObservableSetWrapper.this);
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

        @Override
        public String toString() {
            if (wasAdded()) {
                return "added " + added;
            }
            return "removed " + removed;
        }
    }

    @Override
    public boolean add(E e) {
        boolean ret = backingSet.add(e);
        if (ret) {
            fireChange(e, null);
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        boolean ret = backingSet.remove(o);
        if (ret) {
            fireChange(null, (E) o);
        }
        return ret;
    }

    @Override
    public void clear() {
        for (E e : new ArrayList<>(backingSet)) {
            remove(e);
        }
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> backingIt = backingSet.iterator();
        return new Iterator<E>() {

            private E last;

            @Override
            public boolean hasNext() {
                return backingIt.hasNext();
            }

            @Override
            public E next() {
                return last = backingIt.next();
            }

            @Override
            public void remove() {
                backingIt.remove();
                fireChange(null, last);
            }
        };
    }

    @Override
    public int size() {
        return backingSet.size();
    }

    @Override
    public String toString() {
        return backingSet.toString();
    }
}
