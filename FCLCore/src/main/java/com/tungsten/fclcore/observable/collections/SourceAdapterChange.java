package com.tungsten.fclcore.observable.collections;

import java.util.List;

/**
 * Port of {@code SourceAdapterChange}：
 * 把底层列表的 Change 适配到另一个 ObservableList（通常是 ListProperty 本身）。
 */
public class SourceAdapterChange<E> extends ListChangeListener.Change<E> {

    private final ListChangeListener.Change<? extends E> change;

    public SourceAdapterChange(ObservableList<E> source, ListChangeListener.Change<? extends E> change) {
        super(source);
        this.change = change;
    }

    @Override
    public boolean next() {
        return change.next();
    }

    @Override
    public void reset() {
        change.reset();
    }

    @Override
    public int getFrom() {
        return change.getFrom();
    }

    @Override
    public int getTo() {
        return change.getTo();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> getRemoved() {
        return (List<E>) change.getRemoved();
    }

    @Override
    protected int[] getPermutation() {
        return change.getPermutation();
    }

    @Override
    public boolean wasUpdated() {
        return change.wasUpdated();
    }
}
