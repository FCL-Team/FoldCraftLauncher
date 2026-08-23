package com.tungsten.fclcore.fakefx.collections;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.binding.ExpressionHelperBase;

import java.util.Arrays;

/**
 */
public abstract class ArrayListenerHelper<T extends ObservableArray<T>> extends ExpressionHelperBase {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static methods

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper helper, T observable, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleInvalidation(observable, listener) : helper.addListener(listener);
    }

    public static  ArrayListenerHelper removeListener(ArrayListenerHelper helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper helper, T observable, ArrayChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleChange(observable, listener) : helper.addListener(listener);
    }

    public static  ArrayListenerHelper removeListener(ArrayListenerHelper helper, ArrayChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static  void fireValueChangedEvent(ArrayListenerHelper helper, boolean sizeChanged, int from, int to) {
        if (helper != null && (from < to || sizeChanged)) {
            helper.fireValueChangedEvent(sizeChanged, from, to);
        }
    }

    public static  boolean hasListeners(ArrayListenerHelper helper) {
        return helper != null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Common implementations

    protected final T observable;

    public ArrayListenerHelper(T observable) {
        this.observable = observable;
    }

    protected abstract ArrayListenerHelper addListener(InvalidationListener listener);
    protected abstract ArrayListenerHelper removeListener(InvalidationListener listener);

    protected abstract ArrayListenerHelper addListener(ArrayChangeListener<T> listener);
    protected abstract ArrayListenerHelper removeListener(ArrayChangeListener<T> listener);

    protected abstract void fireValueChangedEvent(boolean sizeChanged, int from, int to);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementations

    private static class SingleInvalidation<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {

        private final InvalidationListener listener;

        private SingleInvalidation(T observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override
        protected ArrayListenerHelper addListener(InvalidationListener listener) {
            return new Generic(observable, this.listener, listener);
        }

        @Override
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected ArrayListenerHelper addListener(ArrayChangeListener listener) {
            return new Generic(observable, this.listener, listener);
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            try {
                listener.invalidated(observable);
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
    }

    private static class SingleChange<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {

        private final ArrayChangeListener listener;

        private SingleChange(T observable, ArrayChangeListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override
        protected ArrayListenerHelper addListener(InvalidationListener listener) {
            return new Generic(observable, listener, this.listener);
        }

        @Override
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected ArrayListenerHelper addListener(ArrayChangeListener listener) {
            return new Generic(observable, this.listener, listener);
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            try {
                listener.onChanged(observable, sizeChanged, from, to);
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
    }

    private static class Generic<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {

        private InvalidationListener[] invalidationListeners;
        private ArrayChangeListener[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(T observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(T observable, ArrayChangeListener listener0, ArrayChangeListener listener1) {
            super(observable);
            this.changeListeners = new ArrayChangeListener[] {listener0, listener1};
            this.changeSize = 2;
        }

        private Generic(T observable, InvalidationListener invalidationListener, ArrayChangeListener changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ArrayChangeListener[] {changeListener};
            this.changeSize = 1;
        }

        @Override
        protected Generic addListener(InvalidationListener listener) {
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
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
            if (invalidationListeners != null) {
                for (int index = 0; index < invalidationSize; index++) {
                    if (listener.equals(invalidationListeners[index])) {
                        if (invalidationSize == 1) {
                            if (changeSize == 1) {
                                return new SingleChange(observable, changeListeners[0]);
                            }
                            invalidationListeners = null;
                            invalidationSize = 0;
                        } else if ((invalidationSize == 2) && (changeSize == 0)) {
                            return new SingleInvalidation(observable, invalidationListeners[1-index]);
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
        protected ArrayListenerHelper addListener(ArrayChangeListener<T> listener) {
            if (changeListeners == null) {
                changeListeners = new ArrayChangeListener[] {listener};
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
            return this;
        }

        @Override
        protected ArrayListenerHelper removeListener(ArrayChangeListener<T> listener) {
            if (changeListeners != null) {
                for (int index = 0; index < changeSize; index++) {
                    if (listener.equals(changeListeners[index])) {
                        if (changeSize == 1) {
                            if (invalidationSize == 1) {
                                return new SingleInvalidation(observable, invalidationListeners[0]);
                            }
                            changeListeners = null;
                            changeSize = 0;
                        } else if ((changeSize == 2) && (invalidationSize == 0)) {
                            return new SingleChange(observable, changeListeners[1-index]);
                        } else {
                            final int numMoved = changeSize - index - 1;
                            final ArrayChangeListener[] oldListeners = changeListeners;
                            if (locked) {
                                changeListeners = new ArrayChangeListener[changeListeners.length];
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
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            final InvalidationListener[] curInvalidationList = invalidationListeners;
            final int curInvalidationSize = invalidationSize;
            final ArrayChangeListener[] curChangeList = changeListeners;
            final int curChangeSize = changeSize;

            try {
                locked = true;
                for (int i = 0; i < curInvalidationSize; i++) {
                    try {
                        curInvalidationList[i].invalidated(observable);
                    } catch (Exception e) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    }
                }
                for (int i = 0; i < curChangeSize; i++) {
                    try {
                        curChangeList[i].onChanged(observable, sizeChanged, from, to);
                    } catch (Exception e) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    }
                }
            } finally {
                locked = false;
            }
        }
    }
}
