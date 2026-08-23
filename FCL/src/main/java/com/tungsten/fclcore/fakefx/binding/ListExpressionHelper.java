package com.tungsten.fclcore.fakefx.binding;

import java.util.Arrays;
import java.util.List;

import static com.tungsten.fclcore.fakefx.collections.ListChangeListener.Change;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableListValue;
import com.tungsten.fclcore.fakefx.collections.FXCollections;
import com.tungsten.fclcore.fakefx.collections.ListChangeListener;
import com.tungsten.fclcore.fakefx.collections.NonIterableChange;
import com.tungsten.fclcore.fakefx.collections.ObservableList;
import com.tungsten.fclcore.fakefx.collections.SourceAdapterChange;

public abstract class ListExpressionHelper<E> extends ExpressionHelperBase {


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static methods

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> helper, ObservableListValue<E> observable, InvalidationListener listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        observable.getValue(); // validate observable
        return (helper == null)? new SingleInvalidation<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> helper, ObservableListValue<E> observable, ChangeListener<? super ObservableList<E>> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleChange<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> helper, ChangeListener<? super ObservableList<E>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> ListExpressionHelper<E> addListener(ListExpressionHelper<E> helper, ObservableListValue<E> observable, ListChangeListener<? super E> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleListChange<E>(observable, listener) : helper.addListener(listener);
    }

    public static <E> ListExpressionHelper<E> removeListener(ListExpressionHelper<E> helper, ListChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <E> void fireValueChangedEvent(ListExpressionHelper<E> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    public static <E> void fireValueChangedEvent(ListExpressionHelper<E> helper, Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Common implementations

    protected final ObservableListValue<E> observable;

    protected ListExpressionHelper(ObservableListValue<E> observable) {
        this.observable = observable;
    }

    protected abstract ListExpressionHelper<E> addListener(InvalidationListener listener);
    protected abstract ListExpressionHelper<E> removeListener(InvalidationListener listener);

    protected abstract ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> listener);
    protected abstract ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> listener);

    protected abstract ListExpressionHelper<E> addListener(ListChangeListener<? super E> listener);
    protected abstract ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listener);

    protected abstract void fireValueChangedEvent();
    protected abstract void fireValueChangedEvent(Change<? extends E> change);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementations

    private static class SingleInvalidation<E> extends ListExpressionHelper<E> {

        private final InvalidationListener listener;

        private SingleInvalidation(ObservableListValue<E> observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> listener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            listener.invalidated(observable);
        }

        @Override
        protected void fireValueChangedEvent(Change<? extends E> change) {
            listener.invalidated(observable);
        }
    }

    private static class SingleChange<E> extends ListExpressionHelper<E> {

        private final ChangeListener<? super ObservableList<E>> listener;
        private ObservableList<E> currentValue;

        private SingleChange(ObservableListValue<E> observable, ChangeListener<? super ObservableList<E>> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableList<E> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                listener.changed(observable, oldValue, currentValue);
            }
        }

        @Override
        protected void fireValueChangedEvent(Change<? extends E> change) {
            listener.changed(observable, currentValue, currentValue);
        }
    }

    private static class SingleListChange<E> extends ListExpressionHelper<E> {

        private final ListChangeListener<? super E> listener;
        private ObservableList<E> currentValue;

        private SingleListChange(ObservableListValue<E> observable, ListChangeListener<? super E> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> listener) {
            return new Generic<E>(observable, listener, this.listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> listener) {
            return this;
        }

        @Override
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listener) {
            return new Generic<E>(observable, this.listener, listener);
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableList<E> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                final int safeSize = (currentValue == null)? 0 : currentValue.size();
                final ObservableList<E> safeOldValue = (oldValue == null)?
                        FXCollections.<E>emptyObservableList()
                        : FXCollections.unmodifiableObservableList(oldValue);
                final Change<E> change = new NonIterableChange.GenericAddRemoveChange<E>(0, safeSize, safeOldValue, observable);
                listener.onChanged(change);
            }
        }

        @Override
        protected void fireValueChangedEvent(final Change<? extends E> change) {
            listener.onChanged(new SourceAdapterChange<>(observable, change));
        }
    }

    private static class Generic<E> extends ListExpressionHelper<E> {

        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableList<E>>[] changeListeners;
        private ListChangeListener<? super E>[] listChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int listChangeSize;
        private boolean locked;
        private ObservableList<E> currentValue;

        private Generic(ObservableListValue<E> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableListValue<E> observable, ChangeListener<? super ObservableList<E>> listener0, ChangeListener<? super ObservableList<E>> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[] {listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableListValue<E> observable, ListChangeListener<? super E> listener0, ListChangeListener<? super E> listener1) {
            super(observable);
            this.listChangeListeners = new ListChangeListener[] {listener0, listener1};
            this.listChangeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableListValue<E> observable, InvalidationListener invalidationListener, ChangeListener<? super ObservableList<E>> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableListValue<E> observable, InvalidationListener invalidationListener, ListChangeListener<? super E> listChangeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.listChangeListeners = new ListChangeListener[] {listChangeListener};
            this.listChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableListValue<E> observable, ChangeListener<? super ObservableList<E>> changeListener, ListChangeListener<? super E> listChangeListener) {
            super(observable);
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.listChangeListeners = new ListChangeListener[] {listChangeListener};
            this.listChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        @Override
        protected ListExpressionHelper<E> addListener(InvalidationListener listener) {
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
        protected ListExpressionHelper<E> removeListener(InvalidationListener listener) {
            if (invalidationListeners != null) {
                for (int index = 0; index < invalidationSize; index++) {
                    if (listener.equals(invalidationListeners[index])) {
                        if (invalidationSize == 1) {
                            if ((changeSize == 1) && (listChangeSize == 0)) {
                                return new SingleChange<E>(observable, changeListeners[0]);
                            } else if ((changeSize == 0) && (listChangeSize == 1)) {
                                return new SingleListChange<E>(observable, listChangeListeners[0]);
                            }
                            invalidationListeners = null;
                            invalidationSize = 0;
                        } else if ((invalidationSize == 2) && (changeSize == 0) && (listChangeSize == 0)) {
                            return new SingleInvalidation<E>(observable, invalidationListeners[1-index]);
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
        protected ListExpressionHelper<E> addListener(ChangeListener<? super ObservableList<E>> listener) {
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
        protected ListExpressionHelper<E> removeListener(ChangeListener<? super ObservableList<E>> listener) {
            if (changeListeners != null) {
                for (int index = 0; index < changeSize; index++) {
                    if (listener.equals(changeListeners[index])) {
                        if (changeSize == 1) {
                            if ((invalidationSize == 1) && (listChangeSize == 0)) {
                                return new SingleInvalidation<E>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (listChangeSize == 1)) {
                                return new SingleListChange<E>(observable, listChangeListeners[0]);
                            }
                            changeListeners = null;
                            changeSize = 0;
                        } else if ((changeSize == 2) && (invalidationSize == 0) && (listChangeSize == 0)) {
                            return new SingleChange<E>(observable, changeListeners[1-index]);
                        } else {
                            final int numMoved = changeSize - index - 1;
                            final ChangeListener<? super ObservableList<E>>[] oldListeners = changeListeners;
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
        protected ListExpressionHelper<E> addListener(ListChangeListener<? super E> listener) {
            if (listChangeListeners == null) {
                listChangeListeners = new ListChangeListener[] {listener};
                listChangeSize = 1;
            } else {
                final int oldCapacity = listChangeListeners.length;
                if (locked) {
                    final int newCapacity = (listChangeSize < oldCapacity)? oldCapacity : (oldCapacity * 3)/2 + 1;
                    listChangeListeners = Arrays.copyOf(listChangeListeners, newCapacity);
                } else if (listChangeSize == oldCapacity) {
                    listChangeSize = trim(listChangeSize, listChangeListeners);
                    if (listChangeSize == oldCapacity) {
                        final int newCapacity = (oldCapacity * 3)/2 + 1;
                        listChangeListeners = Arrays.copyOf(listChangeListeners, newCapacity);
                    }
                }
                listChangeListeners[listChangeSize++] = listener;
            }
            if (listChangeSize == 1) {
                currentValue = observable.getValue();
            }
            return this;
        }

        @Override
        protected ListExpressionHelper<E> removeListener(ListChangeListener<? super E> listener) {
            if (listChangeListeners != null) {
                for (int index = 0; index < listChangeSize; index++) {
                    if (listener.equals(listChangeListeners[index])) {
                        if (listChangeSize == 1) {
                            if ((invalidationSize == 1) && (changeSize == 0)) {
                                return new SingleInvalidation<E>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (changeSize == 1)) {
                                return new SingleChange<E>(observable, changeListeners[0]);
                            }
                            listChangeListeners = null;
                            listChangeSize = 0;
                        } else if ((listChangeSize == 2) && (invalidationSize == 0) && (changeSize == 0)) {
                            return new SingleListChange<E>(observable, listChangeListeners[1-index]);
                        } else {
                            final int numMoved = listChangeSize - index - 1;
                            final ListChangeListener<? super E>[] oldListeners = listChangeListeners;
                            if (locked) {
                                listChangeListeners = new ListChangeListener[listChangeListeners.length];
                                System.arraycopy(oldListeners, 0, listChangeListeners, 0, index+1);
                            }
                            if (numMoved > 0) {
                                System.arraycopy(oldListeners, index+1, listChangeListeners, index, numMoved);
                            }
                            listChangeSize--;
                            if (!locked) {
                                listChangeListeners[listChangeSize] = null; // Let gc do its work
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
            if ((changeSize == 0) && (listChangeSize == 0)) {
                notifyListeners(currentValue, null, false);
            } else {
                final ObservableList<E> oldValue = currentValue;
                currentValue = observable.getValue();
                if (currentValue != oldValue) {
                    Change<E> change = null;
                    if (listChangeSize > 0) {
                        final int safeSize = (currentValue == null)? 0 : currentValue.size();
                        final ObservableList<E> safeOldValue = (oldValue == null)?
                                FXCollections.<E>emptyObservableList()
                                : FXCollections.unmodifiableObservableList(oldValue);
                        change = new NonIterableChange.GenericAddRemoveChange(0, safeSize, safeOldValue, observable);
                    }
                    notifyListeners(oldValue, change, false);
                } else {
                    notifyListeners(currentValue, null, true);
                }
            }
        }

        @Override
        protected void fireValueChangedEvent(final Change<? extends E> change) {
            final Change<E> mappedChange = (listChangeSize == 0)? null : new SourceAdapterChange<>(observable, change);
            notifyListeners(currentValue, mappedChange, false);
        }

        private void notifyListeners(ObservableList<E> oldValue, Change<E> change, boolean noChange) {
            final InvalidationListener[] curInvalidationList = invalidationListeners;
            final int curInvalidationSize = invalidationSize;
            final ChangeListener<? super ObservableList<E>>[] curChangeList = changeListeners;
            final int curChangeSize = changeSize;
            final ListChangeListener<? super E>[] curListChangeList = listChangeListeners;
            final int curListChangeSize = listChangeSize;
            try {
                locked = true;
                for (int i = 0; i < curInvalidationSize; i++) {
                    curInvalidationList[i].invalidated(observable);
                }
                if (!noChange) {
                    for (int i = 0; i < curChangeSize; i++) {
                        curChangeList[i].changed(observable, oldValue, currentValue);
                    }
                    if (change != null) {
                        for (int i = 0; i < curListChangeSize; i++) {
                            change.reset();
                            curListChangeList[i].onChanged(change);
                        }
                    }
                }
            } finally {
                locked = false;
            }
        }

    }
}
