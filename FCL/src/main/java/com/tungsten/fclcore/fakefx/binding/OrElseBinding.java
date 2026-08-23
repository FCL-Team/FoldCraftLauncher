package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;

import java.util.Objects;

public class OrElseBinding<T> extends LazyObjectBinding<T> {

    private final ObservableValue<T> source;
    private final T constant;

    public OrElseBinding(ObservableValue<T> source, T constant) {
        this.source = Objects.requireNonNull(source, "source cannot be null");
        this.constant = constant;
    }

    @Override
    protected T computeValue() {
        T value = source.getValue();

        return value == null ? constant : value;
    }

    @Override
    protected Subscription observeSources() {
        return Subscription.subscribeInvalidations(source, this::invalidate); // start observing source
    }
}
