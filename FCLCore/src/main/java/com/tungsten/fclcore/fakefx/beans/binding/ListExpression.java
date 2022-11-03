package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyBooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.ReadOnlyIntegerProperty;
import com.tungsten.fclcore.fakefx.beans.value.ObservableIntegerValue;
import com.tungsten.fclcore.fakefx.beans.value.ObservableListValue;
import com.tungsten.fclcore.fakefx.binding.StringFormatter;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class ListExpression<E> implements ObservableListValue<E> {

    private static final ObservableList EMPTY_LIST = FXCollections.emptyObservableList();

    /**
     * Creates a default {@code ListExpression}.
     */
    public ListExpression() {
    }

    @Override
    public ObservableList<E> getValue() {
        return get();
    }

    public static <E> ListExpression<E> listExpression(final ObservableListValue<E> value) {
        if (value == null) {
            throw new NullPointerException("List must be specified.");
        }
        return value instanceof ListExpression ? (ListExpression<E>) value
                : new ListBinding<E>() {
            {
                super.bind(value);
            }

            @Override
            public void dispose() {
                super.unbind(value);
            }

            @Override
            protected ObservableList<E> computeValue() {
                return value.get();
            }

            @Override
            public ObservableList<ObservableListValue<E>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    /**
     * The size of the list
     * @return the size
     */
    public int getSize() {
        return size();
    }

    /**
     * An integer property that represents the size of the list.
     * @return the property
     */
    public abstract ReadOnlyIntegerProperty sizeProperty();

    /**
     * A boolean property that is {@code true}, if the list is empty.
     * @return the {@code ReadOnlyBooleanProperty}
     *
     */
    public abstract ReadOnlyBooleanProperty emptyProperty();

    /**
     * Creates a new {@link ObjectBinding} that contains the element at the specified position.
     * If {@code index} points behind the list, the {@code ObjectBinding} contains {@code null}.
     *
     * @param index the index of the element
     * @return the {@code ObjectBinding}
     * @throws IllegalArgumentException if {@code index < 0}
     */
    public ObjectBinding<E> valueAt(int index) {
        return Bindings.valueAt(this, index);
    }

    /**
     * Creates a new {@link ObjectBinding} that contains the element at the specified position.
     * If {@code index} points outside of the list, the {@code ObjectBinding} contains {@code null}.
     *
     * @param index the index of the element
     * @return the {@code ObjectBinding}
     * @throws NullPointerException if {@code index} is {@code null}
     */
    public ObjectBinding<E> valueAt(ObservableIntegerValue index) {
        return Bindings.valueAt(this, index);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this list is equal to
     * another {@link ObservableList}.
     *
     * @param other
     *            the other {@code ObservableList}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    public BooleanBinding isEqualTo(final ObservableList<?> other) {
        return Bindings.equal(this, other);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if this list is not equal to
     * another {@link ObservableList}.
     *
     * @param other
     *            the other {@code ObservableList}
     * @return the new {@code BooleanBinding}
     * @throws NullPointerException
     *             if {@code other} is {@code null}
     */
    public BooleanBinding isNotEqualTo(final ObservableList<?> other) {
        return Bindings.notEqual(this, other);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if the wrapped list is {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    /**
     * Creates a new {@link BooleanBinding} that holds {@code true} if the wrapped list is not {@code null}.
     *
     * @return the new {@code BooleanBinding}
     */
    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    @Override
    public int size() {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.size() : list.size();
    }

    @Override
    public boolean isEmpty() {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.isEmpty() : list.isEmpty();
    }

    @Override
    public boolean contains(Object obj) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.contains(obj) : list.contains(obj);
    }

    @Override
    public Iterator<E> iterator() {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.iterator() : list.iterator();
    }

    @Override
    public Object[] toArray() {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.toArray() : list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        final ObservableList<E> list = get();
        return (list == null)? (T[]) EMPTY_LIST.toArray(array) : list.toArray(array);
     }

    @Override
    public boolean add(E element) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.add(element) : list.add(element);
    }

    @Override
    public boolean remove(Object obj) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.remove(obj) : list.remove(obj);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.contains(objects) : list.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.addAll(elements) : list.addAll(elements);
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.addAll(i, elements) : list.addAll(i, elements);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.removeAll(objects) : list.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.retainAll(objects) : list.retainAll(objects);
    }

    @Override
    public void clear() {
        final ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.clear();
        } else {
            list.clear();
        }
    }

    @Override
    public E get(int i) {
        final ObservableList<E> list = get();
        return (list == null)? (E) EMPTY_LIST.get(i) : list.get(i);
    }

    @Override
    public E set(int i, E element) {
        final ObservableList<E> list = get();
        return (list == null)? (E) EMPTY_LIST.set(i, element) : list.set(i, element);
    }

    @Override
    public void add(int i, E element) {
        final ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.add(i, element);
        } else {
            list.add(i, element);
        }
    }

    @Override
    public E remove(int i) {
        final ObservableList<E> list = get();
        return (list == null)? (E) EMPTY_LIST.remove(i) : list.remove(i);
    }

    @Override
    public int indexOf(Object obj) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.indexOf(obj) : list.indexOf(obj);
    }

    @Override
    public int lastIndexOf(Object obj) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.lastIndexOf(obj) : list.lastIndexOf(obj);
    }

    @Override
    public ListIterator<E> listIterator() {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.listIterator() : list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.listIterator(i) : list.listIterator(i);
    }

    @Override
    public List<E> subList(int from, int to) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.subList(from, to) : list.subList(from, to);
    }

    @Override
    public boolean addAll(E... elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.addAll(elements) : list.addAll(elements);
    }

    @Override
    public boolean setAll(E... elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.setAll(elements) : list.setAll(elements);
    }

    @Override
    public boolean setAll(Collection<? extends E> elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.setAll(elements) : list.setAll(elements);
    }

    @Override
    public boolean removeAll(E... elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.removeAll(elements) : list.removeAll(elements);
    }

    @Override
    public boolean retainAll(E... elements) {
        final ObservableList<E> list = get();
        return (list == null)? EMPTY_LIST.retainAll(elements) : list.retainAll(elements);
    }

    @Override
    public void remove(int from, int to) {
        final ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.remove(from, to);
        } else {
            list.remove(from, to);
        }
    }

}
