package com.tungsten.fclcore.util.fakefx.fx;

public interface Observable {

    void addListener(InvalidationListener listener);

    void removeListener(InvalidationListener listener);
}