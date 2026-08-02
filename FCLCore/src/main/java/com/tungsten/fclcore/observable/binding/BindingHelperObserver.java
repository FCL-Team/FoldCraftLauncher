package com.tungsten.fclcore.observable.binding;

import com.tungsten.fclcore.observable.InvalidationListener;
import com.tungsten.fclcore.observable.Observable;
import com.tungsten.fclcore.observable.WeakListener;

import java.lang.ref.WeakReference;

/**
 * Simplified reimplementation of {@code fakefx.binding.BindingHelperObserver}：
 * 依赖失效时调用 binding.invalidate()；binding 被 GC 后自动摘除。
 */
final class BindingHelperObserver implements InvalidationListener, WeakListener {

    interface Binding {
        void invalidate();
    }

    private final WeakReference<Binding> ref;

    BindingHelperObserver(Binding binding) {
        if (binding == null) {
            throw new NullPointerException("Binding has to be specified.");
        }
        this.ref = new WeakReference<>(binding);
    }

    @Override
    public void invalidated(Observable observable) {
        final Binding binding = ref.get();
        if (binding == null) {
            observable.removeListener(this);
        } else {
            binding.invalidate();
        }
    }

    @Override
    public boolean wasGarbageCollected() {
        return ref.get() == null;
    }
}
