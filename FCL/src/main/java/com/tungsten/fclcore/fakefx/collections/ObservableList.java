package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.collections.transformation.FilteredList;
import com.tungsten.fclcore.fakefx.collections.transformation.SortedList;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface ObservableList<E> extends List<E>, Observable {
    /**
     * Add a listener to this observable list.
     * @param listener the listener for listening to the list changes
     */
    public void addListener(ListChangeListener<? super E> listener);

    /**
     * Tries to remove a listener from this observable list. If the listener is not
     * attached to this list, nothing happens.
     * @param listener a listener to remove
     */
    public void removeListener(ListChangeListener<? super E> listener);

    /**
     * A convenience method for var-arg addition of elements.
     * @param elements the elements to add
     * @return true (as specified by Collection.add(E))
     */
    public boolean addAll(E... elements);

    /**
     * Clears the ObservableList and adds all the elements passed as var-args.
     * @param elements the elements to set
     * @return true (as specified by Collection.add(E))
     * @throws NullPointerException if the specified arguments contain one or more null elements
     */
    public boolean setAll(E... elements);

    /**
     * Clears the ObservableList and adds all elements from the collection.
     * @param col the collection with elements that will be added to this observableArrayList
     * @return true (as specified by Collection.add(E))
     * @throws NullPointerException if the specified collection contains one or more null elements
     */
    public boolean setAll(Collection<? extends E> col);

    /**
     * A convenience method for var-arg usage of the {@link #removeAll(Collection) removeAll} method.
     * @param elements the elements to be removed
     * @return true if list changed as a result of this call
     */
    public boolean removeAll(E... elements);

    /**
     * A convenience method for var-arg usage of the {@link #retainAll(Collection) retainAll} method.
     * @param elements the elements to be retained
     * @return true if list changed as a result of this call
     */
    public boolean retainAll(E... elements);

    /**
     * A simplified way of calling {@code sublist(from, to).clear()}. As this is a common operation,
     * ObservableList has this method for convenient usage.
     * @param from the start of the range to remove (inclusive)
     * @param to the end of the range to remove (exclusive)
     * @throws IndexOutOfBoundsException if an illegal range is provided
     */
    public void remove(int from, int to);

    /**
     * Creates a {@link FilteredList} wrapper of this list using
     * the specified predicate.
     * @param predicate the predicate to use
     * @return new {@code FilteredList}
     * @since JavaFX 8.0
     */
    public default FilteredList<E> filtered(Predicate<E> predicate) {
        return new FilteredList<>(this, predicate);
    }

    /**
     * Creates a {@link SortedList} wrapper of this list using
     * the specified comparator.
     * @param comparator the comparator to use or null for unordered List
     * @return new {@code SortedList}
     * @since JavaFX 8.0
     */
    public default SortedList<E> sorted(Comparator<E> comparator) {
        return new SortedList<>(this, comparator);
    }

    /**
     * Creates a {@link SortedList} wrapper of this list with the natural
     * ordering.
     * @return new {@code SortedList}
     * @since JavaFX 8.0
     */
    public default SortedList<E> sorted() {
        Comparator naturalOrder = new Comparator<E>() {

            @Override
            public int compare(E o1, E o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }

                if (o1 instanceof Comparable) {
                    return ((Comparable) o1).compareTo(o2);
                }

                return Collator.getInstance().compare(o1.toString(), o2.toString());
            }
        };
        return sorted(naturalOrder);
    }
}
