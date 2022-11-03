package com.tungsten.fclcore.fakefx.collections;

import java.util.Collections;
import java.util.List;

/**
 * Interface that receives notifications of changes to an ObservableList.
 *
 * @param <E> the list element type
 * @see Change
 * @since JavaFX 2.0
 */
@FunctionalInterface
public interface ListChangeListener<E> {

    /**
     * Represents a report of changes done to an {@link ObservableList}. The change may consist of one or more actual
     * changes and must be iterated by calling the {@link #next()} method.
     *
     * Each change must be one of the following:
     * <ul>
     * <li><b>Permutation change</b> : {@link #wasPermutated()} returns true in this case.
     * The permutation happened at range between {@link #getFrom() from} (inclusive) and {@link #getTo() to} (exclusive) and
     * can be queried by calling {@link #getPermutation(int)} method.
     * <li><b>Add or remove change</b> : In this case, at least one of the {@link #wasAdded()}, {@link #wasRemoved()} returns true.
     * If both methods return true, {@link #wasReplaced()} will also return true.
     * <p>The {@link #getRemoved()} method returns a list of elements that have been
     * replaced or removed from the list.
     * <p> The range between {@link #getFrom() from} (inclusive) and {@link #getTo() to} (exclusive)
     * denotes the sublist of the list that contain new elements. Note that this is a half-open
     * interval, so if no elements were added, {@code getFrom()} is equal to {@code getTo()}.
     * <p>It is possible to get a list of added elements by calling getAddedSubList().
     * <p>Note that in order to maintain correct indexes of the separate add/remove changes, these changes
     * <b>must</b> be sorted by their {@code from} index.
     * <li><b>Update change</b> : {@link #wasUpdated()} return true on an update change.
     * All elements between {@link #getFrom() from} (inclusive) and {@link #getTo() to} (exclusive) were updated.
     * </ul>
     *
     * <b>Important:</b> It's necessary to call {@link #next()} method before calling
     * any other method of {@code Change}. The same applies after calling {@link #reset()}.
     * The only methods that works at any time is {@link #getList()}.
     *
     *<p>
     * Typical usage is to observe changes on an ObservableList in order
     * to hook or unhook (or add or remove a listener) or in order to maintain
     * some invariant on every element in that ObservableList. A common code
     * pattern for doing this looks something like the following:<br>
     *
     * <blockquote><pre>
     * ObservableList&lt;Item&gt; theList = ...;
     *
     * theList.addListener(new ListChangeListener&lt;Item&gt;() {
     *     public void onChanged(Change&lt;Item&gt; c) {
     *         while (c.next()) {
     *             if (c.wasPermutated()) {
     *                 for (int i = c.getFrom(); i &lt; c.getTo(); ++i) {
     *                      //permutate
     *                 }
     *             } else if (c.wasUpdated()) {
     *                      //update item
     *             } else {
     *                 for (Item remitem : c.getRemoved()) {
     *                     remitem.remove(Outer.this);
     *                 }
     *                 for (Item additem : c.getAddedSubList()) {
     *                     additem.add(Outer.this);
     *                 }
     *             }
     *         }
     *     });
     *
     * }</pre></blockquote>
     * <p>
     * <b>Warning:</b> This class directly accesses the source list to acquire information about the changes.
     * <br> This effectively makes the Change object invalid when another change occurs on the list.
     * <br> For this reason it is <b>not safe to use this class on a different thread</b>.
     * <br> It also means <b>the source list cannot be modified inside the listener</b> since that would invalidate this Change object
     * for all subsequent listeners.
     * <p>
     * Note: in case the change contains multiple changes of different type, these changes must be in the following order:
     * <em> permutation change(s), add or remove changes, update changes </em>
     * This is because permutation changes cannot go after add/remove changes as they would change the position of added elements.
     * And on the other hand, update changes must go after add/remove changes because they refer with their indexes to the current
     * state of the list, which means with all add/remove changes applied.
     * @param <E> the list element type
     * @since JavaFX 2.0
     */
    public abstract static class Change<E> {
        private final ObservableList<E> list;

        /**
         * Goes to the next change.
         * The Change instance, in its initial state, is invalid and requires a call to {@code next()} before
         * calling other methods. The first {@code next()} call will make this object
         * represent the first change.
         * @return {@code true} if switched to the next change, {@code false} if this is the last change
         */
        public abstract boolean next();

        /**
         * Resets to the initial stage. After this call, {@link #next()} must be called
         * before working with the first change.
         */
        public abstract void reset();

        /**
         * Constructs a new Change instance on the given list.
         * @param list The list that was changed
         */
        public Change(ObservableList<E> list) {
            this.list = list;
        }

        /**
         * The source list of the change.
         * @return a list that was changed
         */
        public ObservableList<E> getList() {
            return list;
        }

        /**
         * If {@link #wasAdded()} is true, the interval contains all the values that were added.
         * If {@link #wasPermutated()} is true, the interval marks the values that were permutated.
         * If {@link #wasRemoved()} is true and {@code wasAdded} is false, {@link #getFrom()} and {@link #getTo()}
         * should return the same number - the place where the removed elements were positioned in the list.
         * @return a beginning (inclusive) of an interval related to the change
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public abstract int getFrom();

        /**
         * The end of the change interval.
         * @return an end (exclusive) of an interval related to the change
         * @throws IllegalStateException if this Change instance is in initial state
         * @see #getFrom()
         */
        public abstract int getTo();

        /**
         * An immutable list of removed/replaced elements. If no elements
         * were removed from the list, an empty list is returned.
         * @return a list with all the removed elements
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public abstract List<E> getRemoved();

        /**
         * Indicates if the change was only a permutation.
         * @return {@code true} if the change was just a permutation
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public boolean wasPermutated() {
            return getPermutation().length != 0;
        }

        /**
         * Indicates if elements were added during this change.
         * @return {@code true} if something was added to the list
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public boolean wasAdded() {
            return !wasPermutated() && !wasUpdated() && getFrom() < getTo();
        }

        /**
         * Indicates if elements were removed during this change.
         * Note that using set will also produce a change with {@code wasRemoved()} returning
         * true. See {@link #wasReplaced()}.
         * @return {@code true} if something was removed from the list
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public boolean wasRemoved() {
            return !getRemoved().isEmpty();
        }

        /**
         * Indicates if elements were replaced during this change.
         * This is usually true when set is called on the list.
         * Set operation will act like remove and add operation at the same time.
         * <p>
         * Usually, it's not necessary to use this method directly.
         * Handling remove operation and then add operation, as in the example in
         * the {@link Change} class javadoc, will effectively handle the set operation.
         *
         * @return same as {@code wasAdded() && wasRemoved()}
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public boolean wasReplaced() {
            return wasAdded() && wasRemoved();
        }

        /**
         * Indicates that the elements between {@link #getFrom()} (inclusive)
         * to {@link #getTo()} exclusive has changed.
         * This is the only optional event type and may not be
         * fired by all ObservableLists.
         * @return {@code true} if the current change is an update change
         * @since JavaFX 2.1
         */
        public boolean wasUpdated() {
            return false;
        }

        /**
         * Returns a subList view of the list that contains only the elements added. This is actually a shortcut to
         * <code>c.getList().subList(c.getFrom(), c.getTo());</code>
         *
         * <pre>{@code
         * for (Node n : change.getAddedSubList()) {
         *       // do something
         * }
         * }</pre>
         * @return the newly created sublist view that contains all the added elements
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public List<E> getAddedSubList() {
            return wasAdded()? getList().subList(getFrom(), getTo()) : Collections.<E>emptyList();
        }

        /**
         * Returns the size of {@link #getRemoved()} list.
         * @return the number of removed items
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public int getRemovedSize() {
            return getRemoved().size();
        }

        /**
         * Returns the size of the interval that was added.
         * @return the number of added items
         * @throws IllegalStateException if this Change instance is in initial state
         */
        public int getAddedSize() {
            return wasAdded() ? getTo() - getFrom() : 0;
        }

        /**
         * If this change is a permutation, it returns an integer array
         * that describes the permutation.
         * This array maps directly from the previous indexes to the new ones.
         * This method is not publicly accessible and therefore can return an array safely.
         * The 0 index of the array corresponds to index {@link #getFrom()} of the list. The same applies
         * for the last index and {@link #getTo()}.
         * The method is used by {@link #wasPermutated() } and {@link #getPermutation(int)} methods.
         * @return empty array if this is not permutation or an integer array containing the permutation
         * @throws IllegalStateException if this Change instance is in initial state
         */
        protected abstract int[] getPermutation();

        /**
         * This method allows developers to observe the permutations that occurred in this change. In order to get the
         * new position of an element, you must call:
         * <pre>
         *    change.getPermutation(oldIndex);
         * </pre>
         *
         * Note: default implementation of this method takes the information
         * from {@link #getPermutation()} method. You don't have to override this method.
         * @param i the old index that contained the element prior to this change
         * @return the new index of the same element
         * @throws IndexOutOfBoundsException if i is out of the bounds of the list
         * @throws IllegalStateException if this is not a permutation change
         */
        public int getPermutation(int i) {
            if (!wasPermutated()) {
                throw new IllegalStateException("Not a permutation change");
            }
            return getPermutation()[i - getFrom()];
        }

    }
    /**
     * Called after a change has been made to an ObservableList.
     *
     * @param c an object representing the change that was done
     * @see Change
     */
    public void onChanged(Change<? extends E> c);
}
