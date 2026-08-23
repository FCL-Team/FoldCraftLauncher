package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.WeakListener;
import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

/**
 */
public class BidirectionalContentBinding {

    private static void checkParameters(Object property1, Object property2) {
        if ((property1 == null) || (property2 == null)) {
            throw new NullPointerException("Both parameters must be specified.");
        }
        if (property1 == property2) {
            throw new IllegalArgumentException("Cannot bind object to itself");
        }
    }

    public static <E> Object bind(ObservableList<E> list1, ObservableList<E> list2) {
        checkParameters(list1, list2);
        final ListContentBinding<E> binding = new ListContentBinding<E>(list1, list2);
        list1.setAll(list2);
        list1.addListener(binding);
        list2.addListener(binding);
        return binding;
    }

    public static <E> Object bind(ObservableSet<E> set1, ObservableSet<E> set2) {
        checkParameters(set1, set2);
        final SetContentBinding<E> binding = new SetContentBinding<E>(set1, set2);
        set1.clear();
        set1.addAll(set2);
        set1.addListener(binding);
        set2.addListener(binding);
        return binding;
    }

    public static <K, V> Object bind(ObservableMap<K, V> map1, ObservableMap<K, V> map2) {
        checkParameters(map1, map2);
        final MapContentBinding<K, V> binding = new MapContentBinding<K, V>(map1, map2);
        map1.clear();
        map1.putAll(map2);
        map1.addListener(binding);
        map2.addListener(binding);
        return binding;
    }

    public static void unbind(Object obj1, Object obj2) {
        checkParameters(obj1, obj2);
        if ((obj1 instanceof ObservableList) && (obj2 instanceof ObservableList)) {
            final ObservableList list1 = (ObservableList)obj1;
            final ObservableList list2 = (ObservableList)obj2;
            final ListContentBinding binding = new ListContentBinding(list1, list2);
            list1.removeListener(binding);
            list2.removeListener(binding);
        } else if ((obj1 instanceof ObservableSet) && (obj2 instanceof ObservableSet)) {
            final ObservableSet set1 = (ObservableSet)obj1;
            final ObservableSet set2 = (ObservableSet)obj2;
            final SetContentBinding binding = new SetContentBinding(set1, set2);
            set1.removeListener(binding);
            set2.removeListener(binding);
        } else if ((obj1 instanceof ObservableMap) && (obj2 instanceof ObservableMap)) {
            final ObservableMap map1 = (ObservableMap)obj1;
            final ObservableMap map2 = (ObservableMap)obj2;
            final MapContentBinding binding = new MapContentBinding(map1, map2);
            map1.removeListener(binding);
            map2.removeListener(binding);
        }
    }

    private static class ListContentBinding<E> implements ListChangeListener<E>, WeakListener {

        private final WeakReference<ObservableList<E>> propertyRef1;
        private final WeakReference<ObservableList<E>> propertyRef2;

        private boolean updating = false;


        public ListContentBinding(ObservableList<E> list1, ObservableList<E> list2) {
            propertyRef1 = new WeakReference<ObservableList<E>>(list1);
            propertyRef2 = new WeakReference<ObservableList<E>>(list2);
        }

        @Override
        public void onChanged(Change<? extends E> change) {
            if (!updating) {
                final ObservableList<E> list1 = propertyRef1.get();
                final ObservableList<E> list2 = propertyRef2.get();
                if ((list1 == null) || (list2 == null)) {
                    if (list1 != null) {
                        list1.removeListener(this);
                    }
                    if (list2 != null) {
                        list2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        final ObservableList<E> dest = (list1 ==  change.getList())? list2 : list1;
                        while (change.next()) {
                            if (change.wasPermutated()) {
                                dest.remove(change.getFrom(), change.getTo());
                                dest.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo()));
                            } else {
                                if (change.wasRemoved()) {
                                    dest.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
                                }
                                if (change.wasAdded()) {
                                    dest.addAll(change.getFrom(), change.getAddedSubList());
                                }
                            }
                        }
                    } finally {
                        updating = false;
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return (propertyRef1.get() == null) || (propertyRef2.get() == null);
        }

        @Override
        public int hashCode() {
            final ObservableList<E> list1 = propertyRef1.get();
            final ObservableList<E> list2 = propertyRef2.get();
            final int hc1 = (list1 == null)? 0 : list1.hashCode();
            final int hc2 = (list2 == null)? 0 : list2.hashCode();
            return hc1 * hc2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Object propertyA1 = propertyRef1.get();
            final Object propertyA2 = propertyRef2.get();
            if ((propertyA1 == null) || (propertyA2 == null)) {
                return false;
            }

            if (obj instanceof ListContentBinding) {
                final ListContentBinding otherBinding = (ListContentBinding) obj;
                final Object propertyB1 = otherBinding.propertyRef1.get();
                final Object propertyB2 = otherBinding.propertyRef2.get();
                if ((propertyB1 == null) || (propertyB2 == null)) {
                    return false;
                }

                if ((propertyA1 == propertyB1) && (propertyA2 == propertyB2)) {
                    return true;
                }
                if ((propertyA1 == propertyB2) && (propertyA2 == propertyB1)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class SetContentBinding<E> implements SetChangeListener<E>, WeakListener {

        private final WeakReference<ObservableSet<E>> propertyRef1;
        private final WeakReference<ObservableSet<E>> propertyRef2;

        private boolean updating = false;


        public SetContentBinding(ObservableSet<E> list1, ObservableSet<E> list2) {
            propertyRef1 = new WeakReference<ObservableSet<E>>(list1);
            propertyRef2 = new WeakReference<ObservableSet<E>>(list2);
        }

        @Override
        public void onChanged(Change<? extends E> change) {
            if (!updating) {
                final ObservableSet<E> set1 = propertyRef1.get();
                final ObservableSet<E> set2 = propertyRef2.get();
                if ((set1 == null) || (set2 == null)) {
                    if (set1 != null) {
                        set1.removeListener(this);
                    }
                    if (set2 != null) {
                        set2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        final Set<E> dest = (set1 == change.getSet())? set2 : set1;
                        if (change.wasRemoved()) {
                            dest.remove(change.getElementRemoved());
                        } else {
                            dest.add(change.getElementAdded());
                        }
                    } finally {
                        updating = false;
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return (propertyRef1.get() == null) || (propertyRef2.get() == null);
        }

        @Override
        public int hashCode() {
            final ObservableSet<E> set1 = propertyRef1.get();
            final ObservableSet<E> set2 = propertyRef2.get();
            final int hc1 = (set1 == null)? 0 : set1.hashCode();
            final int hc2 = (set2 == null)? 0 : set2.hashCode();
            return hc1 * hc2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Object propertyA1 = propertyRef1.get();
            final Object propertyA2 = propertyRef2.get();
            if ((propertyA1 == null) || (propertyA2 == null)) {
                return false;
            }

            if (obj instanceof SetContentBinding) {
                final SetContentBinding otherBinding = (SetContentBinding) obj;
                final Object propertyB1 = otherBinding.propertyRef1.get();
                final Object propertyB2 = otherBinding.propertyRef2.get();
                if ((propertyB1 == null) || (propertyB2 == null)) {
                    return false;
                }

                if ((propertyA1 == propertyB1) && (propertyA2 == propertyB2)) {
                    return true;
                }
                if ((propertyA1 == propertyB2) && (propertyA2 == propertyB1)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class MapContentBinding<K, V> implements MapChangeListener<K, V>, WeakListener {

        private final WeakReference<ObservableMap<K, V>> propertyRef1;
        private final WeakReference<ObservableMap<K, V>> propertyRef2;

        private boolean updating = false;


        public MapContentBinding(ObservableMap<K, V> list1, ObservableMap<K, V> list2) {
            propertyRef1 = new WeakReference<ObservableMap<K, V>>(list1);
            propertyRef2 = new WeakReference<ObservableMap<K, V>>(list2);
        }

        @Override
        public void onChanged(Change<? extends K, ? extends V> change) {
            if (!updating) {
                final ObservableMap<K, V> map1 = propertyRef1.get();
                final ObservableMap<K, V> map2 = propertyRef2.get();
                if ((map1 == null) || (map2 == null)) {
                    if (map1 != null) {
                        map1.removeListener(this);
                    }
                    if (map2 != null) {
                        map2.removeListener(this);
                    }
                } else {
                    try {
                        updating = true;
                        final Map<K, V> dest = (map1 == change.getMap())? map2 : map1;
                        if (change.wasRemoved()) {
                            dest.remove(change.getKey());
                        }
                        if (change.wasAdded()) {
                            dest.put(change.getKey(), change.getValueAdded());
                        }
                    } finally {
                        updating = false;
                    }
                }
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return (propertyRef1.get() == null) || (propertyRef2.get() == null);
        }

        @Override
        public int hashCode() {
            final ObservableMap<K, V> map1 = propertyRef1.get();
            final ObservableMap<K, V> map2 = propertyRef2.get();
            final int hc1 = (map1 == null)? 0 : map1.hashCode();
            final int hc2 = (map2 == null)? 0 : map2.hashCode();
            return hc1 * hc2;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final Object propertyA1 = propertyRef1.get();
            final Object propertyA2 = propertyRef2.get();
            if ((propertyA1 == null) || (propertyA2 == null)) {
                return false;
            }

            if (obj instanceof MapContentBinding) {
                final MapContentBinding otherBinding = (MapContentBinding) obj;
                final Object propertyB1 = otherBinding.propertyRef1.get();
                final Object propertyB2 = otherBinding.propertyRef2.get();
                if ((propertyB1 == null) || (propertyB2 == null)) {
                    return false;
                }

                if ((propertyA1 == propertyB1) && (propertyA2 == propertyB2)) {
                    return true;
                }
                if ((propertyA1 == propertyB2) && (propertyA2 == propertyB1)) {
                    return true;
                }
            }
            return false;
        }
    }

}
