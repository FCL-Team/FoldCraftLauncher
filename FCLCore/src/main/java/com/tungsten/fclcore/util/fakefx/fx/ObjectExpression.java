package com.tungsten.fclcore.util.fakefx.fx;

public abstract class ObjectExpression<T> implements ObservableObjectValue<T> {

    @Override
    public T getValue() {
        return get();
    }

    public ObjectExpression() {
    }
}
