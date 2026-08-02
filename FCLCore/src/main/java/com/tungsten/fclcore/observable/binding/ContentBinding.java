package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.WeakListener;
import com.tungsten.fclcore.observable.collections.ListChangeListener;
import com.tungsten.fclcore.observable.collections.ObservableList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simplified reimplementation of {@code fakefx.binding.ContentBinding}：
 * 单向内容绑定，把 source 列表的每次变更重放到 target 列表。
 * target 只持弱引用，被 GC 后自动从 source 摘除。
 */
final class ContentBinding {

    private static void checkParameters(Object property1, Object property2) {
        Objects.requireNonNull(property1, "Both parameters must be specified.");
        Objects.requireNonNull(property2, "Both parameters must be specified.");
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    static <E> Object bind(List<E> list1, ObservableList<? extends E> list2) {
        checkParameters(list1, list2);
        final ListContentBinding<E> binding = new ListContentBinding<>(list1);
        list1.clear();
        list1.addAll(list2);
        list2.addListener(binding);
        return binding;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static void unbind(Object obj1, Object obj2) {
        checkParameters(obj1, obj2);
        if ((obj1 instanceof List) && (obj2 instanceof ObservableList)) {
            final ListContentBinding binding = new ListContentBinding((List) obj1);
            ((ObservableList) obj2).removeListener(binding);
        }
    }

    private static final class ListContentBinding<E> implements ListChangeListener<E>, WeakListener {

        private final WeakReference<List<E>> listRef;
        private final int hashCode;

        ListContentBinding(List<E> list) {
            this.listRef = new WeakReference<>(list);
            this.hashCode = System.identityHashCode(list);
        }

        @Override
        public void onChanged(Change<? extends E> change) {
            final List<E> list = listRef.get();
            if (list == null) {
                change.getList().removeListener(this);
                return;
            }
            while (change.next()) {
                if (change.wasPermutated()) {
                    list.subList(change.getFrom(), change.getTo()).clear();
                    list.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo()));
                } else if (change.wasUpdated()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        list.set(i, change.getList().get(i));
                    }
                } else {
                    if (change.wasRemoved()) {
                        list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (change.wasAdded()) {
                        list.addAll(change.getFrom(), new ArrayList<>(change.getAddedSubList()));
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return listRef.get() == null;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof ListContentBinding) {
                final ListContentBinding<?> other = (ListContentBinding<?>) obj;
                final List<E> list1 = listRef.get();
                final List<?> list2 = other.listRef.get();
                return (list1 != null) && (list1 == list2);
            }
            return false;
        }
    }
}
