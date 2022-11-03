package com.tungsten.fclcore.fakefx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Abstract class that serves as a base class for {@link ObservableList} implementations that are modifiable.
 *
 * To implement a modifiable {@code ObservableList} class, you just need to implement the following set of methods:
 * <ul>
 * <li> {@link #get(int)  get(int)}
 * <li> {@link #size() size()}
 * <li> {@link #doAdd(int, Object) doAdd(int, Object)}
 * <li> {@link #doRemove(int) doRemove(int)}
 * <li> {@link #doSet(int, Object) doSet(int, Object)}
 * </ul>
 *
 * and the notifications and built and fired automatically for you.
 *
 * <p>Example of a simple {@code ObservableList} delegating to another {@code List} would look like this:
 *
 * <pre>
 *
 *   <strong>public class</strong> ArrayObservableList&lt;E&gt; <strong>extends</strong> ModifiableObservableList&lt;E&gt; {
 *
 *   <strong>private final List</strong>&lt;E&gt; delegate = new <strong>ArrayList</strong>&lt;&gt;();
 *
 *   <strong>public E</strong> get(int index) {
 *       <strong>return</strong> delegate.get(index);
 *   }
 *
 *   <strong>public int</strong> size() {
 *       <strong>return</strong> delegate.size();
 *   }
 *
 *   <strong>protected void</strong> doAdd(<strong>int</strong> index, <strong>E</strong> element) {
 *       delegate.add(index, element);
 *   }
 *
 *   <strong>protected E</strong> doSet(<strong>int</strong> index, <strong>E</strong> element) {
 *       <strong>return</strong> delegate.set(index, element);
 *   }
 *
 *   <strong>protected E</strong> doRemove(<strong>int</strong> index) {
 *       <strong>return</strong> delegate.remove(index);
 *   }
 *
 * </pre>
 *
 * @param <E> the type of the elements contained in the List
 * @see ObservableListBase
 * @since JavaFX 8.0
 */
public abstract class ModifiableObservableListBase<E> extends ObservableListBase<E> {

    /**
     * Creates a default {@code ModifiableObservableListBase}.
     */
    public ModifiableObservableListBase() {
    }

    @Override
    public boolean setAll(Collection<? extends E> col) {
        if (isEmpty() && col.isEmpty()) return false;
        beginChange();
        try {
            clear();
            addAll(col);
            return true;
        } finally {
            endChange();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        beginChange();
        try {
            boolean res = super.addAll(c);
            return res;
        } finally {
            endChange();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        beginChange();
        try {
            boolean res = super.addAll(index, c);
            return res;
        } finally {
            endChange();
        }
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        beginChange();
        try {
            super.removeRange(fromIndex, toIndex);
        } finally {
            endChange();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        beginChange();
        try {
            boolean res = super.removeAll(c);
            return res;
        } finally {
            endChange();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        beginChange();
        try {
            boolean res = super.retainAll(c);
            return res;
        } finally {
            endChange();
        }
    }

    @Override
    public void add(int index, E element) {
        doAdd(index, element);
        beginChange();
        nextAdd(index, index + 1);
        ++modCount;
        endChange();
    }

    @Override
    public E set(int index, E element) {
        E old = doSet(index, element);
        beginChange();
        nextSet(index, old);
        endChange();
        return old;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i != - 1) {
            remove(i);
            return true;
        }
        return false;
    }

    @Override
    public E remove(int index) {
        E old = doRemove(index);
        beginChange();
        nextRemove(index, old);
        ++modCount;
        endChange();
        return old;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new SubObservableList(super.subList(fromIndex, toIndex));
    }

    @Override
    public abstract E get(int index);

    @Override
    public abstract int size();

    /**
     * Adds the {@code element} to the List at the position of {@code index}.
     *
     * <p>For the description of possible exceptions, please refer to the documentation
     * of {@link #add(Object) } method.
     *
     * @param index the position where to add the element
     * @param element the element that will be added

     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified arguments contain one or
     * more null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         {@code (index < 0 || index > size())}
     */
    protected abstract void doAdd(int index, E element);

    /**
     * Sets the {@code element} in the List at the position of {@code index}.
     *
     * <p>For the description of possible exceptions, please refer to the documentation
     * of {@link #set(int, Object) } method.
     *
     * @param index the position where to set the element
     * @param element the element that will be set at the specified position
     * @return the old element at the specified position
     *
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified arguments contain one or
     * more null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         {@code (index < 0 || index >= size())}
     */
    protected abstract E doSet(int index, E element);

    /**
     * Removes the element at position of {@code index}.
     *
     * @param index the index of the removed element
     * @return the removed element
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     *         {@code (index < 0 || index >= size())}
     */
    protected abstract E doRemove(int index);

    private class SubObservableList implements List<E> {

        public SubObservableList(List<E> sublist) {
            this.sublist = sublist;
        }
        private List<E> sublist;

        @Override
        public int size() {
            return sublist.size();
        }

        @Override
        public boolean isEmpty() {
            return sublist.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return sublist.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return sublist.iterator();
        }

        @Override
        public Object[] toArray() {
            return sublist.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return sublist.toArray(a);
        }

        @Override
        public boolean add(E e) {
            return sublist.add(e);
        }

        @Override
        public boolean remove(Object o) {
            return sublist.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return sublist.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            beginChange();
            try {
                boolean res = sublist.addAll(c);
                return res;
            } finally {
                endChange();
            }
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            beginChange();
            try {
                boolean res = sublist.addAll(index, c);
                return res;
            } finally {
                endChange();
            }
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            beginChange();
            try {
                boolean res = sublist.removeAll(c);
                return res;
            } finally {
                endChange();
            }
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            beginChange();
            try {
                boolean res = sublist.retainAll(c);
                return res;
            } finally {
                endChange();
            }
        }

        @Override
        public void clear() {
            beginChange();
            try {
                sublist.clear();
            } finally {
                endChange();
            }
        }

        @Override
        public E get(int index) {
            return sublist.get(index);
        }

        @Override
        public E set(int index, E element) {
            return sublist.set(index, element);
        }

        @Override
        public void add(int index, E element) {
            sublist.add(index, element);
        }

        @Override
        public E remove(int index) {
            return sublist.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            return sublist.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return sublist.lastIndexOf(o);
        }

        @Override
        public ListIterator<E> listIterator() {
            return sublist.listIterator();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return sublist.listIterator(index);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return new SubObservableList(sublist.subList(fromIndex, toIndex));
        }

        @Override
        public boolean equals(Object obj) {
            return sublist.equals(obj);
        }

        @Override
        public int hashCode() {
            return sublist.hashCode();
        }

        @Override
        public String toString() {
            return sublist.toString();
        }
    }
}
