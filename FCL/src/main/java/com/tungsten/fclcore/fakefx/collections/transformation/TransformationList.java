package com.tungsten.fclcore.fakefx.collections.transformation;

import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.ObservableListBase;
import com.tungsten.fclcore.fakefx.collections.WeakListChangeListener;

import java.util.List;

public abstract class TransformationList<E, F> extends ObservableListBase<E> implements ObservableList<E> {

    /**
     * Contains the source list of this transformation list.
     * This is never null and should be used to directly access source list content
     */
    private ObservableList<? extends F> source;
    /**
     * This field contains the result of expression "source instanceof {@link ObservableList}".
     * If this is true, it is possible to do transforms online.
     */
    private ListChangeListener<F> sourceListener;

    /**
     * Creates a new Transformation list wrapped around the source list.
     * @param source the wrapped list
     */
    @SuppressWarnings("unchecked")
    protected TransformationList(ObservableList<? extends F> source) {
        if (source == null) {
            throw new NullPointerException();
        }
        this.source = source;
        source.addListener(new WeakListChangeListener<>(getListener()));
    }

    /**
     * The source list specified in the constructor of this transformation list.
     * @return The List that is directly wrapped by this TransformationList
     */
    public final ObservableList<? extends F> getSource() {
        return source;
    }

    /**
     * Checks whether the provided list is in the chain under this
     * {@code TransformationList}.
     *
     * This means the list is either the direct source as returned by
     * {@link #getSource()} or the direct source is a {@code TransformationList},
     * and the list is in it's transformation chain.
     * @param list the list to check
     * @return true if the list is in the transformation chain as specified above.
     */
    public final boolean isInTransformationChain(ObservableList<?> list) {
        if (source == list) {
            return true;
        }
        List<?> currentSource = source;
        while(currentSource instanceof TransformationList) {
            currentSource = ((TransformationList)currentSource).source;
            if (currentSource == list) {
                return true;
            }
        }
        return false;
    }

    private ListChangeListener<F> getListener() {
        if (sourceListener == null) {
            sourceListener = c -> {
                TransformationList.this.sourceChanged(c);
            };
        }
        return sourceListener;
    }

    /**
     * Called when a change from the source is triggered.
     * @param c the change
     */
    protected abstract void sourceChanged(ListChangeListener.Change<? extends F> c);

    /**
     * Maps the index of this list's element to an index in the direct source list.
     * @param index the index in this list
     * @return the index of the element's origin in the source list
     * @see #getSource()
     */
    public abstract int getSourceIndex(int index);

    /**
     * Maps the index of this list's element to an index of the provided {@code list}.
     *
     * The {@code list} must be in the transformation chain.
     *
     * @param list a list from the transformation chain
     * @param index the index of an element in this list
     * @return the index of the element's origin in the provided list
     * @see #isInTransformationChain(ObservableList)
     */
    public final int getSourceIndexFor(ObservableList<?> list, int index) {
        if (!isInTransformationChain(list)) {
            throw new IllegalArgumentException("Provided list is not in the transformation chain of this"
                    + "transformation list");
        }
        List<?> currentSource = source;
        int idx = getSourceIndex(index);
        while(currentSource != list && currentSource instanceof TransformationList) {
            final TransformationList tSource = (TransformationList)currentSource;
            idx = tSource.getSourceIndex(idx);
            currentSource = tSource.source;
        }
        return idx;
    }

    /**
     * Maps the index of the direct source list's element to an index in this list.
     * @param index the index in the source list
     * @return the index of the element in this list if it is contained
     * in this list or negative value otherwise
     * @see #getSource()
     * @see #getSourceIndex(int)
     *
     * @since 9
     */
    public abstract int getViewIndex(int index);
}
