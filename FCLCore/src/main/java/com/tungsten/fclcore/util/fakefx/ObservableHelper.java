package com.tungsten.fclcore.util.fakefx;

import com.tungsten.fclcore.fakefx.beans.InvalidationListener;
import com.tungsten.fclcore.fakefx.beans.Observable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Helper class for implementing {@link Observable}.
 */
public class ObservableHelper implements Observable, InvalidationListener {

    private List<InvalidationListener> listeners = new CopyOnWriteArrayList<>();
    private Observable source;

    public ObservableHelper() {
        this.source = this;
    }

    public ObservableHelper(Observable source) {
        this.source = source;
    }

    /**
     * This method can be called from any thread.
     */
    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    /**
     * This method can be called from any thread.
     */
    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public void invalidate() {
        listeners.forEach(it -> it.invalidated(source));
    }

    @Override
    public void invalidated(Observable observable) {
        this.invalidate();
    }

    public void receiveUpdatesFrom(Observable observable) {
        observable.removeListener(this); // remove the previously added listener(if any)
        observable.addListener(this);
    }
}
