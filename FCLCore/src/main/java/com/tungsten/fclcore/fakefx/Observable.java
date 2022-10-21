package com.tungsten.fclcore.fakefx;

public interface Observable {

    void addListener(InvalidationListener listener);

    void removeListener(InvalidationListener listener);
}