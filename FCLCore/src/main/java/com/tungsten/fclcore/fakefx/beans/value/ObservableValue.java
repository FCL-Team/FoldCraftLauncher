package com.tungsten.fclcore.fakefx.beans.value;

import com.tungsten.fclcore.fakefx.beans.Observable;
import com.tungsten.fclcore.fakefx.binding.FlatMappedBinding;
import com.tungsten.fclcore.fakefx.binding.MappedBinding;
import com.tungsten.fclcore.fakefx.binding.OrElseBinding;

import java.util.function.Function;

public interface ObservableValue<T> extends Observable {

    void addListener(ChangeListener<? super T> listener);

    void removeListener(ChangeListener<? super T> listener);

    T getValue();

    default <U> ObservableValue<U> map(Function<? super T, ? extends U> mapper) {
        return new MappedBinding<>(this, mapper);
    }

    default ObservableValue<T> orElse(T constant) {
        return new OrElseBinding<>(this, constant);
    }

    default <U> ObservableValue<U> flatMap(Function<? super T, ? extends ObservableValue<? extends U>> mapper) {
        return new FlatMappedBinding<>(this, mapper);
    }
}
