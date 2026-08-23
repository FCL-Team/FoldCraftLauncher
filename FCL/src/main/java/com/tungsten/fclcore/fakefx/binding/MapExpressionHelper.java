package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.value.ChangeListener;
import com.tungsten.fclcore.fakefx.beans.value.ObservableMapValue;
import com.tungsten.fclcore.fakefx.collections.MapChangeListener;
import com.tungsten.fclcore.fakefx.collections.ObservableMap;

import java.util.Arrays;
import java.util.Map;

/**
*/
public abstract class MapExpressionHelper<K, V> extends ExpressionHelperBase {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static methods

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, InvalidationListener listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        observable.getValue(); // validate observable
        return (helper == null)? new SingleInvalidation<K, V>(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleChange<K, V>(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, ChangeListener<? super ObservableMap<K, V>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener) {
        if ((observable == null) || (listener == null)) {
            throw new NullPointerException();
        }
        return (helper == null)? new SingleMapChange<K, V>(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, MapChangeListener<? super K, ? super V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return (helper == null)? null : helper.removeListener(listener);
    }

    public static <K, V> void fireValueChangedEvent(MapExpressionHelper<K, V> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    public static <K, V> void fireValueChangedEvent(MapExpressionHelper<K, V> helper, MapChangeListener.Change<? extends K, ? extends V> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Common implementations

    protected final ObservableMapValue<K, V> observable;

    protected MapExpressionHelper(ObservableMapValue<K, V> observable) {
        this.observable = observable;
    }

    protected abstract MapExpressionHelper<K, V> addListener(InvalidationListener listener);
    protected abstract MapExpressionHelper<K, V> removeListener(InvalidationListener listener);

    protected abstract MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener);
    protected abstract MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener);

    protected abstract MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener);
    protected abstract MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener);

    protected abstract void fireValueChangedEvent();
    protected abstract void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementations

    private static class SingleInvalidation<K, V> extends MapExpressionHelper<K, V> {

        private final InvalidationListener listener;

        private SingleInvalidation(ObservableMapValue<K, V> observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            listener.invalidated(observable);
        }

        @Override
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            listener.invalidated(observable);
        }
    }

    private static class SingleChange<K, V> extends MapExpressionHelper<K, V> {

        private final ChangeListener<? super ObservableMap<K, V>> listener;
        private ObservableMap<K, V> currentValue;

        private SingleChange(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic<K, V>(observable, listener, this.listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            return this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableMap<K, V> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                listener.changed(observable, oldValue, currentValue);
            }
        }

        @Override
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            listener.changed(observable, currentValue, currentValue);
        }
    }

    private static class SingleMapChange<K, V> extends MapExpressionHelper<K, V> {

        private final MapChangeListener<? super K, ? super V> listener;
        private ObservableMap<K, V> currentValue;

        private SingleMapChange(ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue();
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic<K, V>(observable, listener, this.listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic<K, V>(observable, listener, this.listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return this;
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic<K, V>(observable, this.listener, listener);
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            return (listener.equals(this.listener))? null : this;
        }

        @Override
        protected void fireValueChangedEvent() {
            final ObservableMap<K, V> oldValue = currentValue;
            currentValue = observable.getValue();
            if (currentValue != oldValue) {
                final SimpleChange<K, V> change = new SimpleChange<K, V>(observable);
                if (currentValue == null) {
                    for (final Map.Entry<K, V> element : oldValue.entrySet()) {
                        listener.onChanged(change.setRemoved(element.getKey(), element.getValue()));
                    }
                } else if (oldValue == null) {
                    for (final Map.Entry<K, V> element : currentValue.entrySet()) {
                        listener.onChanged(change.setAdded(element.getKey(), element.getValue()));
                    }
                } else {
                    for (final Map.Entry<K, V> element : oldValue.entrySet()) {
                        final K key = element.getKey();
                        final V oldEntry = element.getValue();
                        if (currentValue.containsKey(key)) {
                            final V newEntry = currentValue.get(key);
                            if (oldEntry == null ? newEntry != null : !newEntry.equals(oldEntry)) {
                                listener.onChanged(change.setPut(key, oldEntry, newEntry));
                            }
                        } else {
                            listener.onChanged(change.setRemoved(key, oldEntry));
                        }
                    }
                    for (final Map.Entry<K, V> element : currentValue.entrySet()) {
                        final K key = element.getKey();
                        if (!oldValue.containsKey(key)) {
                            listener.onChanged(change.setAdded(key, element.getValue()));
                        }
                    }
                }
            }
        }

        @Override
        protected void fireValueChangedEvent(final MapChangeListener.Change<? extends K, ? extends V> change) {
            listener.onChanged(new SimpleChange<K, V>(observable, change));
        }
    }

    private static class Generic<K, V> extends MapExpressionHelper<K, V> {

        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableMap<K, V>>[] changeListeners;
        private MapChangeListener<? super K, ? super V>[] mapChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int mapChangeSize;
        private boolean locked;
        private ObservableMap<K, V> currentValue;

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener0, ChangeListener<? super ObservableMap<K, V>> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[] {listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener0, MapChangeListener<? super K, ? super V> listener1) {
            super(observable);
            this.mapChangeListeners = new MapChangeListener[] {listener0, listener1};
            this.mapChangeSize = 2;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener invalidationListener, ChangeListener<? super ObservableMap<K, V>> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener invalidationListener, MapChangeListener<? super K, ? super V> listChangeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[] {invalidationListener};
            this.invalidationSize = 1;
            this.mapChangeListeners = new MapChangeListener[] {listChangeListener};
            this.mapChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        private Generic(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> changeListener, MapChangeListener<? super K, ? super V> listChangeListener) {
            super(observable);
            this.changeListeners = new ChangeListener[] {changeListener};
            this.changeSize = 1;
            this.mapChangeListeners = new MapChangeListener[] {listChangeListener};
            this.mapChangeSize = 1;
            this.currentValue = observable.getValue();
        }

        @Override
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
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
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            if (invalidationListeners != null) {
                for (int index = 0; index < invalidationSize; index++) {
                    if (listener.equals(invalidationListeners[index])) {
                        if (invalidationSize == 1) {
                            if ((changeSize == 1) && (mapChangeSize == 0)) {
                                return new SingleChange<K, V>(observable, changeListeners[0]);
                            } else if ((changeSize == 0) && (mapChangeSize == 1)) {
                                return new SingleMapChange<K, V>(observable, mapChangeListeners[0]);
                            }
                            invalidationListeners = null;
                            invalidationSize = 0;
                        } else if ((invalidationSize == 2) && (changeSize == 0) && (mapChangeSize == 0)) {
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
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
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
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            if (changeListeners != null) {
                for (int index = 0; index < changeSize; index++) {
                    if (listener.equals(changeListeners[index])) {
                        if (changeSize == 1) {
                            if ((invalidationSize == 1) && (mapChangeSize == 0)) {
                                return new SingleInvalidation<K, V>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (mapChangeSize == 1)) {
                                return new SingleMapChange<K, V>(observable, mapChangeListeners[0]);
                            }
                            changeListeners = null;
                            changeSize = 0;
                        } else if ((changeSize == 2) && (invalidationSize == 0) && (mapChangeSize == 0)) {
                            return new SingleChange<>(observable, changeListeners[1-index]);
                        } else {
                            final int numMoved = changeSize - index - 1;
                            final ChangeListener<? super ObservableMap<K, V>>[] oldListeners = changeListeners;
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
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            if (mapChangeListeners == null) {
                mapChangeListeners = new MapChangeListener[] {listener};
                mapChangeSize = 1;
            } else {
                final int oldCapacity = mapChangeListeners.length;
                if (locked) {
                    final int newCapacity = (mapChangeSize < oldCapacity)? oldCapacity : (oldCapacity * 3)/2 + 1;
                    mapChangeListeners = Arrays.copyOf(mapChangeListeners, newCapacity);
                } else if (mapChangeSize == oldCapacity) {
                    mapChangeSize = trim(mapChangeSize, mapChangeListeners);
                    if (mapChangeSize == oldCapacity) {
                        final int newCapacity = (oldCapacity * 3)/2 + 1;
                        mapChangeListeners = Arrays.copyOf(mapChangeListeners, newCapacity);
                    }
                }
                mapChangeListeners[mapChangeSize++] = listener;
            }
            if (mapChangeSize == 1) {
                currentValue = observable.getValue();
            }
            return this;
        }

        @Override
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            if (mapChangeListeners != null) {
                for (int index = 0; index < mapChangeSize; index++) {
                    if (listener.equals(mapChangeListeners[index])) {
                        if (mapChangeSize == 1) {
                            if ((invalidationSize == 1) && (changeSize == 0)) {
                                return new SingleInvalidation<K, V>(observable, invalidationListeners[0]);
                            } else if ((invalidationSize == 0) && (changeSize == 1)) {
                                return new SingleChange<K, V>(observable, changeListeners[0]);
                            }
                            mapChangeListeners = null;
                            mapChangeSize = 0;
                        } else if ((mapChangeSize == 2) && (invalidationSize == 0) && (changeSize == 0)) {
                            return new SingleMapChange<>(observable, mapChangeListeners[1-index]);
                        } else {
                            final int numMoved = mapChangeSize - index - 1;
                            final MapChangeListener<? super K, ? super V>[] oldListeners = mapChangeListeners;
                            if (locked) {
                                mapChangeListeners = new MapChangeListener[mapChangeListeners.length];
                                System.arraycopy(oldListeners, 0, mapChangeListeners, 0, index+1);
                            }
                            if (numMoved > 0) {
                                System.arraycopy(oldListeners, index+1, mapChangeListeners, index, numMoved);
                            }
                            mapChangeSize--;
                            if (!locked) {
                                mapChangeListeners[mapChangeSize] = null; // Let gc do its work
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
            if ((changeSize == 0) && (mapChangeSize == 0)) {
                notifyListeners(currentValue, null);
            } else {
                final ObservableMap<K, V> oldValue = currentValue;
                currentValue = observable.getValue();
                notifyListeners(oldValue, null);
            }
        }

        @Override
        protected void fireValueChangedEvent(final MapChangeListener.Change<? extends K, ? extends V> change) {
            final SimpleChange<K, V> mappedChange = (mapChangeSize == 0)? null : new SimpleChange<K, V>(observable, change);
            notifyListeners(currentValue, mappedChange);
        }

        private void notifyListeners(ObservableMap<K, V> oldValue, SimpleChange<K, V> change) {
            final InvalidationListener[] curInvalidationList = invalidationListeners;
            final int curInvalidationSize = invalidationSize;
            final ChangeListener<? super ObservableMap<K, V>>[] curChangeList = changeListeners;
            final int curChangeSize = changeSize;
            final MapChangeListener<? super K, ? super V>[] curListChangeList = mapChangeListeners;
            final int curListChangeSize = mapChangeSize;
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
                            change = new SimpleChange<K, V>(observable);
                            if (currentValue == null) {
                                for (final Map.Entry<K, V> element : oldValue.entrySet()) {
                                    change.setRemoved(element.getKey(), element.getValue());
                                    for (int i = 0; i < curListChangeSize; i++) {
                                        curListChangeList[i].onChanged(change);
                                    }
                                }
                            } else if (oldValue == null) {
                                for (final Map.Entry<K, V> element : currentValue.entrySet()) {
                                    change.setAdded(element.getKey(), element.getValue());
                                    for (int i = 0; i < curListChangeSize; i++) {
                                        curListChangeList[i].onChanged(change);
                                    }
                                }
                            } else {
                                for (final Map.Entry<K, V> element : oldValue.entrySet()) {
                                    final K key = element.getKey();
                                    final V oldEntry = element.getValue();
                                    if (currentValue.containsKey(key)) {
                                        final V newEntry = currentValue.get(key);
                                        if (oldEntry == null ? newEntry != null : !newEntry.equals(oldEntry)) {
                                            change.setPut(key, oldEntry, newEntry);
                                            for (int i = 0; i < curListChangeSize; i++) {
                                                curListChangeList[i].onChanged(change);
                                            }
                                        }
                                    } else {
                                        change.setRemoved(key, oldEntry);
                                        for (int i = 0; i < curListChangeSize; i++) {
                                            curListChangeList[i].onChanged(change);
                                        }
                                    }
                                }
                                for (final Map.Entry<K, V> element : currentValue.entrySet()) {
                                    final K key = element.getKey();
                                    if (!oldValue.containsKey(key)) {
                                        change.setAdded(key, element.getValue());
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

    public static class SimpleChange<K, V> extends MapChangeListener.Change<K, V> {

        private K key;
        private V old;
        private V added;
        private boolean removeOp;
        private boolean addOp;

        public SimpleChange(ObservableMap<K, V> set) {
            super(set);
        }

        public SimpleChange(ObservableMap<K, V> set, MapChangeListener.Change<? extends K, ? extends V> source) {
            super(set);
            key = source.getKey();
            old = source.getValueRemoved();
            added = source.getValueAdded();
            addOp = source.wasAdded();
            removeOp = source.wasRemoved();
        }

        public SimpleChange<K, V> setRemoved(K key, V old) {
            this.key = key;
            this.old = old;
            this.added = null;
            addOp = false;
            removeOp = true;
            return this;
        }

        public SimpleChange<K, V> setAdded(K key, V added) {
            this.key = key;
            this.old = null;
            this.added = added;
            addOp = true;
            removeOp = false;
            return this;
        }

        public SimpleChange<K, V> setPut(K key, V old, V added) {
            this.key = key;
            this.old = old;
            this.added = added;
            addOp = true;
            removeOp = true;
            return this;
        }

        @Override
        public boolean wasAdded() {
            return addOp;
        }

        @Override
        public boolean wasRemoved() {
            return removeOp;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValueAdded() {
            return added;
        }

        @Override
        public V getValueRemoved() {
            return old;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (addOp) {
                if (removeOp) {
                    builder.append("replaced ").append(old).append(" by ").append(added);
                } else {
                    builder.append("added ").append(added);
                }
            } else {
                builder.append("removed ").append(old);
            }
            builder.append(" at key ").append(key);
            return builder.toString();
        }
    }
}
