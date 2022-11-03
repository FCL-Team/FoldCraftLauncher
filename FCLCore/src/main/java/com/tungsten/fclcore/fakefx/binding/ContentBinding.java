package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.WeakListener;
import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class ContentBinding {

    private static void checkParameters(Object property1, Object property2) {
        if ((property1 == null) || (property2 == null)) {
            throw new NullPointerException("Both parameters must be specified.");
        }
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    public static <E> Object bind(List<E> list1, ObservableList<? extends E> list2) {
        checkParameters(list1, list2);
        final ListContentBinding<E> contentBinding = new ListContentBinding<E>(list1);
        if (list1 instanceof ObservableList) {
            ((ObservableList) list1).setAll(list2);
        } else {
            list1.clear();
            list1.addAll(list2);
        }
        list2.removeListener(contentBinding);
        list2.addListener(contentBinding);
        return contentBinding;
    }

    public static <E> Object bind(Set<E> set1, ObservableSet<? extends E> set2) {
        checkParameters(set1, set2);
        final SetContentBinding<E> contentBinding = new SetContentBinding<E>(set1);
        set1.clear();
        set1.addAll(set2);
        set2.removeListener(contentBinding);
        set2.addListener(contentBinding);
        return contentBinding;
    }

    public static <K, V> Object bind(Map<K, V> map1, ObservableMap<? extends K, ? extends V> map2) {
        checkParameters(map1, map2);
        final MapContentBinding<K, V> contentBinding = new MapContentBinding<K, V>(map1);
        map1.clear();
        map1.putAll(map2);
        map2.removeListener(contentBinding);
        map2.addListener(contentBinding);
        return contentBinding;
    }

    public static void unbind(Object obj1, Object obj2) {
        checkParameters(obj1, obj2);
        if ((obj1 instanceof List) && (obj2 instanceof ObservableList)) {
            ((ObservableList)obj2).removeListener(new ListContentBinding((List)obj1));
        } else if ((obj1 instanceof Set) && (obj2 instanceof ObservableSet)) {
            ((ObservableSet)obj2).removeListener(new SetContentBinding((Set)obj1));
        } else if ((obj1 instanceof Map) && (obj2 instanceof ObservableMap)) {
            ((ObservableMap)obj2).removeListener(new MapContentBinding((Map)obj1));
        }
    }

    private static class ListContentBinding<E> implements ListChangeListener<E>, WeakListener {

        private final WeakReference<List<E>> listRef;

        public ListContentBinding(List<E> list) {
            this.listRef = new WeakReference<List<E>>(list);
        }

        @Override
        public void onChanged(Change<? extends E> change) {
            final List<E> list = listRef.get();
            if (list == null) {
                change.getList().removeListener(this);
            } else {
                while (change.next()) {
                    if (change.wasPermutated()) {
                        list.subList(change.getFrom(), change.getTo()).clear();
                        list.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo()));
                    } else {
                        if (change.wasRemoved()) {
                            list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                        }
                        if (change.wasAdded()) {
                            list.addAll(change.getFrom(), change.getAddedSubList());
                        }
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
            final List<E> list = listRef.get();
            return (list == null)? 0 : list.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final List<E> list1 = listRef.get();
            if (list1 == null) {
                return false;
            }

            if (obj instanceof ListContentBinding) {
                final ListContentBinding<?> other = (ListContentBinding<?>) obj;
                final List<?> list2 = other.listRef.get();
                return list1 == list2;
            }
            return false;
        }
    }

    private static class SetContentBinding<E> implements SetChangeListener<E>, WeakListener {

        private final WeakReference<Set<E>> setRef;

        public SetContentBinding(Set<E> set) {
            this.setRef = new WeakReference<Set<E>>(set);
        }

        @Override
        public void onChanged(Change<? extends E> change) {
            final Set<E> set = setRef.get();
            if (set == null) {
                change.getSet().removeListener(this);
            } else {
                if (change.wasRemoved()) {
                    set.remove(change.getElementRemoved());
                } else {
                    set.add(change.getElementAdded());
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return setRef.get() == null;
        }

        @Override
        public int hashCode() {
            final Set<E> set = setRef.get();
            return (set == null)? 0 : set.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Set<E> set1 = setRef.get();
            if (set1 == null) {
                return false;
            }

            if (obj instanceof SetContentBinding) {
                final SetContentBinding<?> other = (SetContentBinding<?>) obj;
                final Set<?> set2 = other.setRef.get();
                return set1 == set2;
            }
            return false;
        }
    }

    private static class MapContentBinding<K, V> implements MapChangeListener<K, V>, WeakListener {

        private final WeakReference<Map<K, V>> mapRef;

        public MapContentBinding(Map<K, V> map) {
            this.mapRef = new WeakReference<Map<K, V>>(map);
        }

        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            final Map<K, V> map = mapRef.get();
            if (map == null) {
                change.getMap().removeListener(this);
            } else {
                if (change.wasRemoved()) {
                    map.remove(change.getKey());
                }
                if (change.wasAdded()) {
                    map.put(change.getKey(), change.getValueAdded());
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return mapRef.get() == null;
        }

        @Override
        public int hashCode() {
            final Map<K, V> map = mapRef.get();
            return (map == null)? 0 : map.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Map<K, V> map1 = mapRef.get();
            if (map1 == null) {
                return false;
            }

            if (obj instanceof MapContentBinding) {
                final MapContentBinding<?, ?> other = (MapContentBinding<?, ?>) obj;
                final Map<?, ?> map2 = other.mapRef.get();
                return map1 == map2;
            }
            return false;
        }
    }
}
