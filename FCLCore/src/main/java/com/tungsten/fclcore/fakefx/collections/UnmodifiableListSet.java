package com.tungsten.fclcore.fakefx.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

/**
 * A special unmodifiable implementation of Set which wraps a List.
 * <strong>It does not check for uniqueness!</strong> There are
 * several places in our implementation (Node.lookupAll and
 * ObservableSetWrapper are two such places) where we want to use
 * a List for speed of insertion and will be in a position to ensure
 * that the List is unique without having the overhead of hashing,
 * but want to present an unmodifiable Set in the public API.
 */
public final class UnmodifiableListSet<E> extends AbstractSet<E> {
    private List<E> backingList;

    public UnmodifiableListSet(List<E> backingList) {
        if (backingList == null) throw new NullPointerException();
        this.backingList = backingList;
    }

    /**
     * Required implementation that returns an iterator. Note that I
     * don't just return backingList.iterator() because doing so would
     * open up a whole through which developers could remove items from
     * this supposedly unmodifiable set. So the iterator is wrapped
     * such that it throws an exception on remove.
     */
    @Override public Iterator<E> iterator() {
        final Iterator<E> itr = backingList.iterator();
        return new Iterator<E>() {
            @Override public boolean hasNext() {
                return itr.hasNext();
            }

            @Override public E next() {
                return itr.next();
            }

            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override public int size() {
        return backingList.size();
    }
}
