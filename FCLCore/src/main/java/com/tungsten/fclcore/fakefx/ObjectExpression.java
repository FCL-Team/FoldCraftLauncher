package com.tungsten.fclcore.fakefx;

public abstract class ObjectExpression<T> implements ObservableObjectValue<T> {

    @Override
    public T getValue() {
        return get();
    }

    public ObjectExpression() {
    }
}
