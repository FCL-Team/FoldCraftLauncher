package com.tungsten.fclcore.fakefx.collections;

import java.util.List;
import com.tungsten.fclcore.fakefx.collections.ListChangeListener.Change;

public class SourceAdapterChange<E> extends ListChangeListener.Change<E> {
    private final Change<? extends E> change;
    private int[] perm;

    public SourceAdapterChange(ObservableList<E> list, Change<? extends E> change) {
        super(list);
        this.change = change;
    }

    @Override
    public boolean next() {
        perm = null;
        return change.next();
    }

    @Override
    public void reset() {
        change.reset();
    }

    @Override
    public int getTo() {
        return change.getTo();
    }

    @Override
    public List<E> getRemoved() {
        return (List<E>) change.getRemoved();
    }

    @Override
    public int getFrom() {
        return change.getFrom();
    }

    @Override
    public boolean wasUpdated() {
        return change.wasUpdated();
    }

    @Override
    protected int[] getPermutation() {
        if (perm == null) {
            if (change.wasPermutated()) {
                final int from = change.getFrom();
                final int n = change.getTo() - from;
                perm = new int[n];
                for (int i=0; i<n; i++) {
                    perm[i] = change.getPermutation(from + i);
                }
            } else {
                perm = new int[0];
            }
        }
        return perm;
    }

    @Override
    public String toString() {
        return change.toString();
    }

}
