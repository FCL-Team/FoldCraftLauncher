package com.tungsten.fclcore.fakefx.event;

import java.util.Set;

public interface CompositeEventTarget extends EventTarget {
    Set<EventTarget> getTargets();

    boolean containsTarget(EventTarget target);
}
