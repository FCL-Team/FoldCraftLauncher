package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ObservableList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Simplified reimplementation of {@code fakefx.beans.property.ReadOnlyListProperty}
 * （合并 ListExpression 的委托方法）：本身即 ObservableList 视图，所有列表操作
 * 委托给 get() 返回的底层列表。
 */
public abstract class ReadOnlyListProperty<E> implements ReadOnlyProperty<ObservableList<E>>, ObservableList<E> {

    public ReadOnlyListProperty() {
    }

    @Override
    public ObservableList<E> getValue() {
        return get();
    }

    public abstract ObservableList<E> get();

    public int getSize() {
        return size();
    }

    public abstract ReadOnlyIntegerProperty sizeProperty();

    public abstract ReadOnlyBooleanProperty emptyProperty();

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public boolean isEmpty() {
        return get().isEmpty();
    }

    @Override
    public boolean contains(Object obj) {
        return get().contains(obj);
    }

    @Override
    public Iterator<E> iterator() {
        return get().iterator();
    }

    @Override
    public Object[] toArray() {
        return get().toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return get().toArray(array);
    }

    @Override
    public boolean add(E element) {
        return get().add(element);
    }

    @Override
    public boolean remove(Object obj) {
        return get().remove(obj);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return get().containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        return get().addAll(elements);
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> elements) {
        return get().addAll(i, elements);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return get().removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return get().retainAll(objects);
    }

    @Override
    public void clear() {
        get().clear();
    }

    @Override
    public E get(int i) {
        return get().get(i);
    }

    @Override
    public E set(int i, E element) {
        return get().set(i, element);
    }

    @Override
    public void add(int i, E element) {
        get().add(i, element);
    }

    @Override
    public E remove(int i) {
        return get().remove(i);
    }

    @Override
    public int indexOf(Object obj) {
        return get().indexOf(obj);
    }

    @Override
    public int lastIndexOf(Object obj) {
        return get().lastIndexOf(obj);
    }

    @Override
    public ListIterator<E> listIterator() {
        return get().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int i) {
        return get().listIterator(i);
    }

    @Override
    public List<E> subList(int from, int to) {
        return get().subList(from, to);
    }

    @Override
    public boolean addAll(E... elements) {
        return get().addAll(elements);
    }

    @Override
    public boolean setAll(E... elements) {
        return get().setAll(elements);
    }

    @Override
    public boolean setAll(Collection<? extends E> elements) {
        return get().setAll(elements);
    }

    @Override
    public boolean removeAll(E... elements) {
        return get().removeAll(elements);
    }

    @Override
    public boolean retainAll(E... elements) {
        return get().retainAll(elements);
    }

    @Override
    public void remove(int from, int to) {
        get().remove(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        final List<?> list = (List<?>) obj;
        if (size() != list.size()) {
            return false;
        }
        final Iterator<E> e1 = listIterator();
        final Iterator<?> e2 = list.listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlyListProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}
