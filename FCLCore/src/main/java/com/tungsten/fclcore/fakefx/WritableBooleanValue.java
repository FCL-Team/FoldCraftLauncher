package com.tungsten.fclcore.fakefx;

public interface WritableBooleanValue extends WritableValue<Boolean> {

    boolean get();

    void set(boolean value);

    @Override
    void setValue(Boolean value);

}