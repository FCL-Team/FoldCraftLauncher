package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableSetValue;
import com.tungsten.fclcore.fakefx.collections.ObservableSet;
import com.tungsten.fclcore.fakefx.collections.SetChangeListener;

import java.util.Arrays;

/**
*/
public abstract class SetExpressionHelper<E> extends ExpressionHelperBase {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static methods

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, InvalidationListener listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        observable.getValue(); // validate observable
        return (helper == null)? new SingleInvalidation<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleChange<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, ChangeListener<? super ObservableSet<E>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, SetChangeListener<? super E> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleSetChange<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> helper, SetChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Common implementations

    protected final ObservableSetValue<E> observable;

    protected SetExpressionHelper(ObservableSetValue<E> observable) {
        this.observable = observable;
    }

    protected abstract SetExpressionHelper<E> addListener(InvalidationListener listener);
    protected abstract SetExpressionHelper<E> removeListener(InvalidationListener listener);

    protected abstract SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener);
    protected abstract SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener);

    protected abstract SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener);
    protected abstract SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener);

    protected abstract void fireValueChangedEvent();
    protected abstract void fireValueChangedEvent(SetChangeListener.Change<? extends E> change);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementations

    private static class SingleInvalidation<E> extends SetExpressionHelper<E> {

        private final InvalidationListener listener;

        private SingleInvalidation(ObservableSetValue<E> observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            listener.invalidated(observable);
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            listener.invalidated(observable);
        }
    }

    private static class SingleChange<E> extends SetExpressionHelper<E> {

        private final ChangeListener<? super ObservableSet<E>> listener;
        private ObservableSet<E> currentValue;

        private SingleChange(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableSet<E> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                listener.changed(observable, oldValue, currentValue);
            }
        }

        @Override
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            listener.changed(observable, currentValue, currentValue);
        }
    }

    private static class SingleSetChange<E> extends SetExpressionHelper<E> {

        private final SetChangeListener<? super E> listener;
        private ObservableSet<E> currentValue;

        private SingleSetChange(ObservableSetValue<E> observable, SetChangeListener<? super E> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableSet<E> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                final SimpleChange<E> change = new SimpleChange<E>(observable);
                if (currentValue == null) {
                    for (final E element : oldValue) {
                        listener.onChanged(change.setRemoved(element));
                    }
                } else if (oldValue == null) {
                    for (final E element : currentValue) {
                        listener.onChanged(change.setAdded(element));
                    }
                } else {
                    for (final E element : oldValue) {
                        if (!currentValue.contains(element)) {
                            listener.onChanged(change.setRemoved(element));
                        }
                    }
                    for (final E element : currentValue) {
                        if (!oldValue.contains(element)) {
                            listener.onChanged(change.setAdded(element));
                        }
                    }
                }
            }
        }

        @Override
        protected void fireValueChangedEvent(final SetChangeListener.Change<? extends E> change) {
            listener.onChanged(new SimpleChange<E>(observable, change));
        }
    }

    private static class Generic<E> extends SetExpressionHelper<E> {

        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableSet<E>>[] changeListeners;
        private SetChangeListener<? super E>[] setChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int setChangeSize;
        private boolean locked;
        private ObservableSet<E> currentValue;

        private Generic(ObservableSetValue<E> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener0, ChangeListener<? super ObservableSet<E>> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[] {listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableSetValue<E> observable, SetChangeListener<? super E> listener0, SetChangeListener<? super E> listener1) {
            super(observable);
            this.setChangeListeners = new SetChangeListener[] {listener0, listener1};
            this.setChangeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableSetValue<E> observable, InvalidationListener invalidationListener, ChangeListener<? super ObservableSet<E>> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableSetValue<E> observable, InvalidationListener invalidationListener, SetChangeListener<? super E> listChangeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.setChangeListeners = new SetChangeListener[] {listChangeListener};
            this.setChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> changeListener, SetChangeListener<? super E> listChangeListener) {
            super(observable);
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.setChangeListeners = new SetChangeListener[] {listChangeListener};
            this.setChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        @Override
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            if (invalidationListeners == null) {
                invalidationListeners = new InvalidationListener[] {listener};
                invalidationSize = 1;
            } else {
                final int oldCapacity = invalidationListeners.length;
                if (locked) {
                    final int newCapacity = (invalidationSize < oldCapacity)? oldCapacity : (oldCapacity * 3)/2 + 1;
                    invalidationListeners = Arrays.copyOf(invalidationListeners, newCapacity);
                } else if (invalidationSize == oldCapacity) {
                    invalidationSize = trim(invalidationSize, invalidationListeners);
                    if (invalidationSize == oldCapacity) {
                        final int newCapacity = (oldCapacity * 3)/2 + 1;
                        invalidationListeners = Arrays.copyOf(invalidationListeners, newCapacity);
                    }
                }
                invalidationListeners[invalidationSize++] = listener;
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            if (invalidationListeners != null) {
                for (int index = 0; index < invalidationSize; index++) {
                    if (listener.equals(invalidationListeners[index])) {
                        if (invalidationSize == 1) {
                            if ((changeSize == 1) && (setChangeSize == 0)) {
                                return new SingleChange<E>(observable, changeListeners[0]);
                            } else if ((changeSize == 0) && (setChangeSize == 1)) {
                                return new SingleSetChange<E>(observable, setChangeListeners[0]);
                            }
                            invalidationListeners = null;
                            invalidationSize = 0;
                        } else if ((invalidationSize == 2) && (changeSize == 0) && (setChangeSize == 0)) {
                            return new SingleInvalidation<>(observable, invalidationListeners[1-index]);
                        } else {
                            final int numMoved = invalidationSize - index - 1;
                            final InvalidationListener[] oldListeners = invalidationListeners;
                            if (locked) {
                                invalidationListeners = new InvalidationListener[invalidationListeners.length];
                                System.arraycopy(oldListeners, 0, invalidationListeners, 0, index+1);
                            }
                            if (numMoved > 0) {
                                System.arraycopy(oldListeners, index+1, invalidationListeners, index, numMoved);
                            }
                            invalidationSize--;
                            if (!locked) {
                                invalidationListeners[invalidationSize] = null; // Let gc do its work
                            }
                        }
                        break;
                    }
                }
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            if (changeListeners == null) {
                changeListeners = new ChangeListener[] {listener};
                changeSize = 1;
            } else {
                final int oldCapacity = changeListeners.length;
                if (locked) {
                    final int newCapacity = (changeSize < oldCapacity)? oldCapacity : (oldCapacity * 3)/2 + 1;
                    changeListeners = Arrays.copyOf(changeListeners, newCapacity);
                } else if (changeSize == oldCapacity) {
                    changeSize = trim(changeSize, changeListeners);
                    if (changeSize == oldCapacity) {
                        final int newCapacity = (oldCapacity * 3)/2 + 1;
                        changeListeners = Arrays.copyOf(changeListeners, newCapacity);
                    }
                }
                changeListeners[changeSize++] = listener;
            }
            if (changeSize == 1) {
                currentValue = observable.getValue();
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            if (changeListeners != null) {
                for (int index = 0; index < changeSize; index++) {
                    if (listener.equals(changeListeners[index])) {
                        if (changeSize == 1) {
                            if ((invalidationSize == 1) && (setChangeSize == 0)) {
                                return new SingleInvalidation<E>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (setChangeSize == 1)) {
                                return new SingleSetChange<E>(observable, setChangeListeners[0]);
                            }
                            changeListeners = null;
                            changeSize = 0;
                        } else if ((changeSize == 2) && (invalidationSize == 0) && (setChangeSize == 0)) {
                            return new SingleChange<>(observable, changeListeners[1-index]);
                        } else {
                            final int numMoved = changeSize - index - 1;
                            final ChangeListener<? super ObservableSet<E>>[] oldListeners = changeListeners;
                            if (locked) {
                                changeListeners = new ChangeListener[changeListeners.length];
                                System.arraycopy(oldListeners, 0, changeListeners, 0, index+1);
                            }
                            if (numMoved > 0) {
                                System.arraycopy(oldListeners, index+1, changeListeners, index, numMoved);
                            }
                            changeSize--;
                            if (!locked) {
                                changeListeners[changeSize] = null; // Let gc do its work
                            }
                        }
                        break;
                    }
                }
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            if (setChangeListeners == null) {
                setChangeListeners = new SetChangeListener[] {listener};
                setChangeSize = 1;
            } else {
                final int oldCapacity = setChangeListeners.length;
                if (locked) {
                    final int newCapacity = (setChangeSize < oldCapacity)? oldCapacity : (oldCapacity * 3)/2 + 1;
                    setChangeListeners = Arrays.copyOf(setChangeListeners, newCapacity);
                } else if (setChangeSize == oldCapacity) {
                    setChangeSize = trim(setChangeSize, setChangeListeners);
                    if (setChangeSize == oldCapacity) {
                        final int newCapacity = (oldCapacity * 3)/2 + 1;
                        setChangeListeners = Arrays.copyOf(setChangeListeners, newCapacity);
                    }
                }
                setChangeListeners[setChangeSize++] = listener;
            }
            if (setChangeSize == 1) {
                currentValue = observable.getValue();
            }
            return this;
        }

        @Override
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            if (setChangeListeners != null) {
                for (int index = 0; index < setChangeSize; index++) {
                    if (listener.equals(setChangeListeners[index])) {
                        if (setChangeSize == 1) {
                            if ((invalidationSize == 1) && (changeSize == 0)) {
                                return new SingleInvalidation<E>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (changeSize == 1)) {
                                return new SingleChange<E>(observable, changeListeners[0]);
                            }
                            setChangeListeners = null;
                            setChangeSize = 0;
                        } else if ((setChangeSize == 2) && (invalidationSize == 0) && (changeSize == 0)) {
                            return new SingleSetChange<>(observable, setChangeListeners[1-index]);
                        } else {
                            final int numMoved = setChangeSize - index - 1;
                            final SetChangeListener<? super E>[] oldListeners = setChangeListeners;
                            if (locked) {
                                setChangeListeners = new SetChangeListener[setChangeListeners.length];
                                System.arraycopy(oldListeners, 0, setChangeListeners, 0, index+1);
                            }
                            if (numMoved > 0) {
                                System.arraycopy(oldListeners, index+1, setChangeListeners, index, numMoved);
                            }
                            setChangeSize--;
                            if (!locked) {
                                setChangeListeners[setChangeSize] = null; // Let gc do its work
                            }
                        }
                        break;
                    }
                }
            }
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            if ((changeSize == 0) && (setChangeSize == 0)) {
                notifyListeners(currentValue, null);
            } else {
                final ObservableSet<E> oldValue = currentValue;
                currentValue = observable.getValue();
                notifyListeners(oldValue, null);
            }
        }

        @Override
        protected void fireValueChangedEvent(final SetChangeListener.Change<? extends E> change) {
            final SimpleChange<E> mappedChange = (setChangeSize == 0)? null : new SimpleChange<E>(observable, change);
            notifyListeners(currentValue, mappedChange);
        }

        private void notifyListeners(ObservableSet<E> oldValue, SimpleChange<E> change) {
            final InvalidationListener[] curInvalidationList = invalidationListeners;
            final int curInvalidationSize = invalidationSize;
            final ChangeListener<? super ObservableSet<E>>[] curChangeList = changeListeners;
            final int curChangeSize = changeSize;
            final SetChangeListener<? super E>[] curListChangeList = setChangeListeners;
            final int curListChangeSize = setChangeSize;
            try {
                locked = true;
                for (int i = 0; i < curInvalidationSize; i++) {
                    curInvalidationList[i].invalidated(observable);
                }
                if ((currentValue != oldValue) || (change != null)) {
                    for (int i = 0; i < curChangeSize; i++) {
                        curChangeList[i].changed(observable, oldValue, currentValue);
                    }
                    if (curListChangeSize > 0) {
                        if (change != null) {
                            for (int i = 0; i < curListChangeSize; i++) {
                                curListChangeList[i].onChanged(change);
                            }
                        } else {
                            change = new SimpleChange<E>(observable);
                            if (currentValue == null) {
                                for (final E element : oldValue) {
                                    change.setRemoved(element);
                                    for (int i = 0; i < curListChangeSize; i++) {
                                        curListChangeList[i].onChanged(change);
                                    }
                                }
                            } else if (oldValue == null) {
                                for (final E element : currentValue) {
                                    change.setAdded(element);
                                    for (int i = 0; i < curListChangeSize; i++) {
                                        curListChangeList[i].onChanged(change);
                                    }
                                }
                            } else {
                                for (final E element : oldValue) {
                                    if (!currentValue.contains(element)) {
                                        change.setRemoved(element);
                                        for (int i = 0; i < curListChangeSize; i++) {
                                            curListChangeList[i].onChanged(change);
                                        }
                                    }
                                }
                                for (final E element : currentValue) {
                                    if (!oldValue.contains(element)) {
                                        change.setAdded(element);
                                        for (int i = 0; i < curListChangeSize; i++) {
                                            curListChangeList[i].onChanged(change);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            } finally {
                locked = false;
            }
        }

    }

    public static class SimpleChange<E> extends SetChangeListener.Change<E> {

        private E old;
        private E added;
        private boolean addOp;

        public SimpleChange(ObservableSet<E> set) {
            super(set);
        }

        public SimpleChange(ObservableSet<E> set, SetChangeListener.Change<? extends E> source) {
            super(set);
            old = source.getElementRemoved();
            added = source.getElementAdded();
            addOp = source.wasAdded();
        }

        public SimpleChange<E> setRemoved(E old) {
            this.old = old;
            this.added = null;
            addOp = false;
            return this;
        }

        public SimpleChange<E> setAdded(E added) {
            this.old = null;
            this.added = added;
            addOp = true;
            return this;
        }

        @Override
        public boolean wasAdded() {
            return addOp;
        }

        @Override
        public boolean wasRemoved() {
            return !addOp;
        }

        @Override
        public E getElementAdded() {
            return added;
        }

        @Override
        public E getElementRemoved() {
            return old;
        }

        @Override
        public String toString() {
            return addOp ? "added " + added : "removed " + old;
        }

    }
}
