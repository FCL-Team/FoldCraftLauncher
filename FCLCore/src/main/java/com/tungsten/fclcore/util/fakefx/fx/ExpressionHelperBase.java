package com.tungsten.fclcore.util.fakefx.fx;

import java.util.function.Predicate;

public class ExpressionHelperBase {

    protected static int trim(int size, Object[] listeners) {
        Predicate<Object> p = t -> t instanceof WeakListener &&
                ((WeakListener)t).wasGarbageCollected();
        int index = 0;
        for (; index < size; index++) {
            if (p.test(listeners[index])) {
                break;
            }
        }
        if (index < size) {
            for (int src = index + 1; src < size; src++) {
                if (!p.test(listeners[src])) {
                    listeners[index++] = listeners[src];
                }
            }
            int oldSize = size;
            size = index;
            for (; index < oldSize; index++) {
                listeners[index] = null;
            }
        }

        return size;
    }

}