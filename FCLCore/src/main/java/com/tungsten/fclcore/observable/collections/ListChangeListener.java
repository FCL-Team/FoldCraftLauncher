package com.tungsten.fclcore.observable.collections;

import java.util.Collections;
import java.util.List;

/**
 * Minimal reimplementation of {@code fakefx.collections.ListChangeListener}，
 * Change 的 wasAdded/wasRemoved/wasReplaced/wasPermutated/wasUpdated 语义逐条对齐。
 */
@FunctionalInterface
public interface ListChangeListener<E> {

    abstract class Change<E> {

        private final ObservableList<E> list;

        public abstract boolean next();

        public abstract void reset();

        public Change(ObservableList<E> list) {
            this.list = list;
        }

        public ObservableList<E> getList() {
            return list;
        }

        public abstract int getFrom();

        public abstract int getTo();

        public abstract List<E> getRemoved();

        public boolean wasPermutated() {
            return getPermutation().length != 0;
        }

        public boolean wasAdded() {
            return !wasPermutated() && !wasUpdated() && getFrom() < getTo();
        }

        public boolean wasRemoved() {
            return !getRemoved().isEmpty();
        }

        public boolean wasReplaced() {
            return wasAdded() && wasRemoved();
        }

        public boolean wasUpdated() {
            return false;
        }

        public List<E> getAddedSubList() {
            return wasAdded() ? getList().subList(getFrom(), getTo()) : Collections.<E>emptyList();
        }

        public int getRemovedSize() {
            return getRemoved().size();
        }

        public int getAddedSize() {
            return wasAdded() ? getTo() - getFrom() : 0;
        }

        protected abstract int[] getPermutation();

        public int getPermutation(int i) {
            if (!wasPermutated()) {
                throw new IllegalStateException("Not a permutation change");
            }
            return getPermutation()[i - getFrom()];
        }
    }

    void onChanged(Change<? extends E> c);
}
