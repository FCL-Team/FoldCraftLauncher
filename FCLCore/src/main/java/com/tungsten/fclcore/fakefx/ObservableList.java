package com.tungsten.fclcore.fakefx;

import java.util.Collection;
import java.util.List;

public interface ObservableList<E> extends List<E>, Observable {

    public boolean addAll(E... elements);

    public boolean setAll(E... elements);

    public boolean setAll(Collection<? extends E> col);

    public boolean removeAll(E... elements);

    public boolean retainAll(E... elements);

    public void remove(int from, int to);
}
