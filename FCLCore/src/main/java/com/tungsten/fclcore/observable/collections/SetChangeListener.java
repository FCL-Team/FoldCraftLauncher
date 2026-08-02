package com.tungsten.fclcore.observable.collections;

/**
 * Minimal reimplementation of {@code SetChangeListener}.
 */
@FunctionalInterface
public interface SetChangeListener<E> {

    abstract class Change<E> {

        private final ObservableSet<E> set;

        public Change(ObservableSet<E> set) {
            this.set = set;
        }

        public ObservableSet<E> getSet() {
            return set;
        }

        public abstract E getElementAdded();

        public abstract E getElementRemoved();

        public boolean wasAdded() {
            return getElementAdded() != null;
        }

        public boolean wasRemoved() {
            return getElementRemoved() != null;
        }
    }

    void onChanged(Change<? extends E> change);
}
