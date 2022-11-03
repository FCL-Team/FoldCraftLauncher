package com.tungsten.fclcore.fakefx.beans.binding;

import com.tungsten.fclcore.fakefx.beans.value.ObservableValue;
import com.tungsten.fclcore.fakefx.collections.ObservableList;

public interface Binding<T> extends ObservableValue<T> {

    boolean isValid();

    void invalidate();

    ObservableList<?> getDependencies();

    void dispose();

}
