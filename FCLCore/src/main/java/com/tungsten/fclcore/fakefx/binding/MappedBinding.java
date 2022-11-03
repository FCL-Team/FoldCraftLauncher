package com.tungsten.fclcore.fakefx.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;

import java.util.Objects;
import java.util.function.Function;

public class MappedBinding<S, T> extends LazyObjectBinding<T> {

    private final ObservableValue<S> source;
    private final Function<? super S, ? extends T> mapper;

    public MappedBinding(ObservableValue<S> source, Function<? super S, ? extends T> mapper) {
        this.source = Objects.requireNonNull(source, "source cannot be null");
        this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
    }

    @Override
    protected T computeValue() {
        S value = source.getValue();

        return value == null ? null : mapper.apply(value);
    }

    @Override
    protected Subscription observeSources() {
        return Subscription.subscribeInvalidations(source, this::invalidate); // start observing source
    }
}
