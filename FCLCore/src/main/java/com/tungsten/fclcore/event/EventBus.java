package com.tungsten.fclcore.event;

import com.tungsten.fclcore.util.Logging;

import java.util.concurrent.ConcurrentHashMap;

public final class EventBus {

    private final ConcurrentHashMap<Class<?>, EventManager<?>> events = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> EventManager<T> channel(Class<T> clazz) {
        return (EventManager<T>) events.computeIfAbsent(clazz, ignored -> new EventManager<>());
    }

    @SuppressWarnings("unchecked")
    public Event.Result fireEvent(Event obj) {
        Logging.LOG.info(obj + " gets fired");

        return channel((Class<Event>) obj.getClass()).fireEvent(obj);
    }

    public static final EventBus EVENT_BUS = new EventBus();
}
