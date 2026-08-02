package com.tungsten.fclcore.observable.collections;

import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Minimal reimplementation of {@code FXCollections}，
 * 只保留外部实际用到的工厂方法。
 */
public class FXCollections {

    private FXCollections() {
    }

    public static <E> ObservableList<E> observableList(List<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new ObservableListWrapper<>(list);
    }

    public static <E> ObservableList<E> observableList(List<E> list, Callback<E, Observable[]> extractor) {
        if (list == null || extractor == null) {
            throw new NullPointerException();
        }
        return new ObservableListWrapper<>(list, extractor);
    }

    public static <E> ObservableList<E> observableArrayList() {
        return observableList(new ArrayList<>());
    }

    public static <E> ObservableList<E> observableArrayList(Callback<E, Observable[]> extractor) {
        return observableList(new ArrayList<>(), extractor);
    }

    public static <E> ObservableList<E> observableArrayList(E... items) {
        return observableList(new ArrayList<>(Arrays.asList(items)));
    }

    public static <E> ObservableList<E> observableArrayList(Collection<? extends E> col) {
        return observableList(new ArrayList<>(col));
    }

    public static <K, V> ObservableMap<K, V> observableMap(Map<K, V> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        return new ObservableMapWrapper<>(map);
    }

    public static <K, V> ObservableMap<K, V> observableHashMap() {
        return observableMap(new HashMap<>());
    }

    public static <E> ObservableSet<E> observableSet(Set<E> set) {
        if (set == null) {
            throw new NullPointerException();
        }
        return new ObservableSetWrapper<>(set);
    }

    public static <E> ObservableSet<E> observableSet(E... elements) {
        if (elements == null) {
            throw new NullPointerException();
        }
        Set<E> set = new HashSet<>(elements.length);
        Collections.addAll(set, elements);
        return new ObservableSetWrapper<>(set);
    }

    public static <E> ObservableList<E> emptyObservableList() {
        return observableList(Collections.emptyList());
    }

    public static <E> ObservableList<E> unmodifiableObservableList(ObservableList<E> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableObservableListImpl<>(list);
    }

    private static class UnmodifiableObservableListImpl<T> extends ObservableListBase<T> implements ObservableList<T> {

        private final ObservableList<T> backingList;
        private final ListChangeListener<T> listener;

        public UnmodifiableObservableListImpl(ObservableList<T> backingList) {
            this.backingList = backingList;
            listener = c -> fireChange(new SourceAdapterChange<>(UnmodifiableObservableListImpl.this, c));
            this.backingList.addListener(new WeakListChangeListener<>(listener));
        }

        @Override
        public T get(int index) {
            return backingList.get(index);
        }

        @Override
        public int size() {
            return backingList.size();
        }

        @Override
        protected void doAdd(int index, T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected T doSet(int index, T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected T doRemove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean setAll(Collection<? extends T> col) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(T... elements) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }
    }
}
