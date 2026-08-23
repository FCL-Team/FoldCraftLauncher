package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class ObservableListBase<E> extends AbstractList<E>  implements ObservableList<E> {

    private ListListenerHelper<E> listenerHelper;
    private final ListChangeBuilder<E> changeBuilder = new ListChangeBuilder<E>(this);

    /**
     * Creates a default {@code ObservableListBase}.
     */
    public ObservableListBase() {
    }

    /**
     * Adds a new update operation to the change.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param pos the position in the list where the updated element resides.
     */
    protected final void nextUpdate(int pos) {
        changeBuilder.nextUpdate(pos);
    }

    /**
     * Adds a new set operation to the change.
     * Equivalent to {@code nextRemove(idx); nextAdd(idx, idx + 1); }.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param idx the index of the item that was set
     * @param old the old value at the {@code idx} position.
     */
    protected final void nextSet(int idx, E old) {
        changeBuilder.nextSet(idx, old);
    }

    /**
     * Adds a new replace operation to the change.
     * Equivalent to {@code nextRemove(from, removed); nextAdd(from, to); }
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param from the index where the items were replaced
     * @param to the end index (exclusive) of the range where the new items reside
     * @param removed the list of items that were removed
     */
    protected final void nextReplace(int from, int to, List<? extends E> removed) {
        changeBuilder.nextReplace(from, to, removed);
    }

    /**
     * Adds a new remove operation to the change with multiple items removed.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param idx the index where the items were removed
     * @param removed the list of items that were removed
     */
    protected final void nextRemove(int idx, List<? extends E> removed) {
        changeBuilder.nextRemove(idx, removed);
    }

    /**
     * Adds a new remove operation to the change with single item removed.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param idx the index where the item was removed
     * @param removed the item that was removed
     */
    protected final void nextRemove(int idx, E removed) {
        changeBuilder.nextRemove(idx, removed);
    }

    /**
     * Adds a new permutation operation to the change.
     * The permutation on index {@code "i"} contains the index, where the item from the index {@code "i"} was moved.
     * <p>It's not necessary to provide the smallest permutation possible. It's correct to always call this method
     * with {@code nextPermutation(0, size(), permutation); }
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param from marks the beginning (inclusive) of the range that was permutated
     * @param to marks the end (exclusive) of the range that was permutated
     * @param perm the permutation in that range. Even if {@code from != 0}, the array should
     * contain the indexes of the list. Therefore, such permutation would not contain indexes of range {@code (0, from)}
     */
    protected final void nextPermutation(int from, int to, int[] perm) {
        changeBuilder.nextPermutation(from, to, perm);
    }

    /**
     * Adds a new add operation to the change.
     * There's no need to provide the list of added items as they can be found directly in the list
     * under the specified indexes.
     * <p><strong>Note</strong>: needs to be called inside {@code beginChange()} / {@code endChange()} block.
     * <p><strong>Note</strong>: needs to reflect the <em>current</em> state of the list.
     * @param from marks the beginning (inclusive) of the range that was added
     * @param to marks the end (exclusive) of the range that was added
     */
    protected final void nextAdd(int from, int to) {
        changeBuilder.nextAdd(from, to);
    }

    /**
     * Begins a change block.
     *
     * Must be called before any of the {@code next*} methods is called.
     * For every {@code beginChange()}, there must be a corresponding {@link #endChange() } call.
     * <p>{@code beginChange()} calls can be nested in a {@code beginChange()}/{@code endChange()} block.
     *
     * @see #endChange()
     */
    protected final void beginChange() {
        changeBuilder.beginChange();
    }

    /**
     * Ends the change block.
     *
     * If the block is the outer-most block for the {@code ObservableList}, the
     * {@code Change} is constructed and all listeners are notified.
     * <p> Ending a nested block doesn't fire a notification.
     *
     * @see #beginChange()
     */
    protected final void endChange() {
        changeBuilder.endChange();
    }

    @Override
    public final void addListener(InvalidationListener listener) {
        listenerHelper = ListListenerHelper.addListener(listenerHelper, listener);
    }

    @Override
    public final void removeListener(InvalidationListener listener) {
        listenerHelper = ListListenerHelper.removeListener(listenerHelper, listener);
    }

    @Override
    public final void addListener(ListChangeListener<? super E> listener) {
        listenerHelper = ListListenerHelper.addListener(listenerHelper, listener);
    }

    @Override
    public final void removeListener(ListChangeListener<? super E> listener) {
        listenerHelper = ListListenerHelper.removeListener(listenerHelper, listener);
    }

    /**
     * Notifies all listeners of a change
     * @param change an object representing the change that was done
     */
    protected final void fireChange(ListChangeListener.Change<? extends E> change) {
        ListListenerHelper.fireValueChangedEvent(listenerHelper, change);
    }

    /**
     * Returns true if there are some listeners registered for this list.
     * @return true if there is a listener for this list
     */
    protected final boolean hasListeners() {
        return ListListenerHelper.hasListeners(listenerHelper);
    }

    @Override
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }

    @Override
    public boolean setAll(E... elements) {
        return setAll(Arrays.asList(elements));
    }

    @Override
    public boolean setAll(Collection<? extends E> col) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(E... elements) {
        return removeAll(Arrays.asList(elements));
    }

    @Override
    public boolean retainAll(E... elements) {
        return retainAll(Arrays.asList(elements));
    }

    @Override
    public void remove(int from, int to) {
        removeRange(from, to);
    }
}
