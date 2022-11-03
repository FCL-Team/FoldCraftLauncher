package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.util.Callback;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public final class ObservableSequentialListWrapper<E> extends ModifiableObservableListBase<E> implements ObservableList<E>, SortableList<E>{
    private final List<E> backingList;
    private final ElementObserver elementObserver;
    private SortHelper helper;

    public ObservableSequentialListWrapper(List<E> list) {
        backingList = list;
        elementObserver = null;
    }

    public ObservableSequentialListWrapper(List<E> list, Callback<E, Observable[]> extractor) {
        backingList = list;
        this.elementObserver = new ElementObserver(extractor, new Callback<E, InvalidationListener>() {

            @Override
            public InvalidationListener call(final E e) {
                return new InvalidationListener() {

                    @Override
                    public void invalidated(Observable observable) {
                        beginChange();
                        int i = 0;
                        for (Iterator<?> it = backingList.iterator(); it.hasNext();) {
                            if (it.next() == e) {
                                nextUpdate(i);
                            }
                            ++i;
                        }
                        endChange();
                    }
                };
            }
        }, this);
        for (E e : backingList) {
            elementObserver.attachListener(e);
        }
    }

    @Override
    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return backingList.containsAll(c);
    }

    @Override
    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return new ListIterator<E>() {

            private final ListIterator<E> backingIt = backingList.listIterator(index);
            private E lastReturned;

            @Override
            public boolean hasNext() {
                return backingIt.hasNext();
            }

            @Override
            public E next() {
                return lastReturned = backingIt.next();
            }

            @Override
            public boolean hasPrevious() {
                return backingIt.hasPrevious();
            }

            @Override
            public E previous() {
                return lastReturned = backingIt.previous();
            }

            @Override
            public int nextIndex() {
                return backingIt.nextIndex();
            }

            @Override
            public int previousIndex() {
                return backingIt.previousIndex();
            }

            @Override
            public void remove() {
                beginChange();
                int idx = previousIndex();
                backingIt.remove();
                nextRemove(idx, lastReturned);
                endChange();
            }

            @Override
            public void set(E e) {
                beginChange();
                int idx = previousIndex();
                backingIt.set(e);
                nextSet(idx, lastReturned);
                endChange();
            }

            @Override
            public void add(E e) {
                beginChange();
                int idx = nextIndex();
                backingIt.add(e);
                nextAdd(idx, idx + 1);
                endChange();
            }
        };
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public E get(int index) {
        try {
            return backingList.listIterator(index).next();
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        try {
            beginChange();
            boolean modified = false;
            ListIterator<E> e1 = listIterator(index);
            Iterator<? extends E> e2 = c.iterator();
            while (e2.hasNext()) {
                e1.add(e2.next());
                modified = true;
            }
            endChange();
            return modified;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    @Override
    public int size() {
        return backingList.size();
    }

    @Override
    protected void doAdd(int index, E element) {
        try {
            backingList.listIterator(index).add(element);
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    @Override
    protected E doSet(int index, E element) {
        try {
            ListIterator<E> e = backingList.listIterator(index);
            E oldVal = e.next();
            e.set(element);
            return oldVal;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    @Override
    protected E doRemove(int index) {
        try {
            ListIterator<E> e = backingList.listIterator(index);
            E outCast = e.next();
            e.remove();
            return outCast;
        } catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        if (backingList.isEmpty()) {
            return;
        }
        int[] perm = getSortHelper().sort((List<? extends Comparable>)backingList);
        fireChange(new NonIterableChange.SimplePermutationChange<E>(0, size(), perm, this));
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        if (backingList.isEmpty()) {
            return;
        }
        int[] perm = getSortHelper().sort(backingList, comparator);
        fireChange(new NonIterableChange.SimplePermutationChange<E>(0, size(), perm, this));
    }

    private SortHelper getSortHelper() {
        if (helper == null) {
            helper = new SortHelper();
        }
        return helper;
    }

}
