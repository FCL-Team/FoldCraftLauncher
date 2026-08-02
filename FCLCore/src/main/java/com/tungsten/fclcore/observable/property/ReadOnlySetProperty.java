package com.tungsten.fclcore.observable.property;

import com.tungsten.fclcore.observable.collections.ObservableSet;

/**
 * Minimal reimplementation of {@code ReadOnlySetProperty}
 * （合并 SetExpression 的委托方法）：本身即 ObservableSet 视图。
 */
public abstract class ReadOnlySetProperty<E> implements ReadOnlyProperty<ObservableSet<E>>, ObservableSet<E> {

    public ReadOnlySetProperty() {
    }

    @Override
    public ObservableSet<E> getValue() {
        return get();
    }

    public abstract ObservableSet<E> get();

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
    public java.util.Iterator<E> iterator() {
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
    public boolean containsAll(java.util.Collection<?> col) {
        return get().containsAll(col);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends E> col) {
        return get().addAll(col);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> col) {
        return get().retainAll(col);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> col) {
        return get().removeAll(col);
    }

    @Override
    public void clear() {
        get().clear();
    }

    @Override
    public boolean equals(Object other) {
        return get().equals(other);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }

    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("ReadOnlySetProperty [");
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
