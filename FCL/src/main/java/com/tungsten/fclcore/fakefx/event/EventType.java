package com.tungsten.fclcore.fakefx.event;

// PENDING_DOC_REVIEW

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This class represents a specific event type associated with an {@code Event}.
 * <p>
 * Event types form a hierarchy with the {@link EventType#ROOT} (equals to
 * {@link Event#ANY}) as its root. This is useful in event filter / handler
 * registration where a single event filter / handler can be registered to a
 * super event type and will be receiving its sub type events as well.
 * Note that you cannot construct two different EventType objects with the same
 * name and parent.
 *
 * <p>
 * <b>Note about deserialization</b>: All EventTypes that are going to be deserialized
 * (e.g. as part of {@link Event} deserialization), need to exist at the time of
 * deserialization. Deserialization of EventType will not create new EventType
 * objects.
 *
 * @param <T> the event class to which this type applies
 * @since JavaFX 2.0
 */
public final class EventType<T extends Event> implements Serializable{

    /**
     * The root event type. All other event types are either direct or
     * indirect sub types of it. It is also the only event type which
     * has its super event type set to {@code null}.
     */
    public static final EventType<Event> ROOT =
            new EventType<Event>("EVENT", null);

    private WeakHashMap<EventType<? extends T>, Void> subTypes;

    private final EventType<? super T> superType;

    private final String name;

    /**
     * Constructs a new {@code EventType} with the {@code EventType.ROOT} as its
     * super type and the name set to {@code null}.
     * @deprecated Do not use this constructor, as only one such EventType can exist
     */
    @Deprecated
    public EventType() {
        this(ROOT, null);
    }

    /**
     * Constructs a new {@code EventType} with the specified name and the
     * {@code EventType.ROOT} as its super type.
     *
     * @param name the name
     * @throws IllegalArgumentException if an EventType with the same name and
     * {@link EventType#ROOT}/{@link Event#ANY} as parent
     */
    public EventType(final String name) {
        this(ROOT, name);
    }

    /**
     * Constructs a new {@code EventType} with the specified super type and
     * the name set to {@code null}.
     *
     * @param superType the event super type
     * @throws IllegalArgumentException if an EventType with "null" name and
     * under this supertype exists
     */
    public EventType(final EventType<? super T> superType) {
        this(superType, null);
    }

    /**
     * Constructs a new {@code EventType} with the specified super type and
     * name.
     *
     * @param superType the event super type
     * @param name the name
     * @throws IllegalArgumentException if an EventType with the same name and
     * superType exists
     */
    public EventType(final EventType<? super T> superType,
            final String name) {
        if (superType == null) {
            throw new NullPointerException(
                    "Event super type must not be null!");
        }

        this.superType = superType;
        this.name = name;
        superType.register(this);
    }

    /**
     * Internal constructor that skips various checks
     */
    EventType(final String name,
                      final EventType<? super T> superType) {
        this.superType = superType;
        this.name = name;
        if (superType != null) {
            if (superType.subTypes != null) {
                for (Iterator i = superType.subTypes.keySet().iterator(); i.hasNext();) {
                    EventType t  = (EventType) i.next();
                    if (name == null && t.name == null || (name != null && name.equals(t.name))) {
                        i.remove();
                    }
                }
            }
            superType.register(this);
        }
    }

    /**
     * Gets the super type of this event type. The returned value is
     * {@code null} only for the {@code EventType.ROOT}.
     *
     * @return the super type
     */
    public final EventType<? super T> getSuperType() {
        return superType;
    }

    /**
     * Gets the name of this event type.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns a string representation of this {@code EventType} object.
     * @return a string representation of this {@code EventType} object.
     */
    @Override
    public String toString() {
        return (name != null) ? name : super.toString();
    }

    private void register(EventType<? extends T> subType) {
        if (subTypes == null) {
            subTypes = new WeakHashMap<EventType<? extends T>, Void>();
        }
        for (EventType<? extends T> t : subTypes.keySet()) {
            if (((t.name == null && subType.name == null) || (t.name != null && t.name.equals(subType.name)))) {
                throw new IllegalArgumentException("EventType \"" + subType + "\""
                        + "with parent \"" + subType.getSuperType()+"\" already exists");
            }
        }
        subTypes.put(subType, null);
    }

    private Object writeReplace() throws ObjectStreamException {
        Deque<String> path = new LinkedList<String>();
        EventType<?> t = this;
        while (t != ROOT) {
            path.addFirst(t.name);
            t = t.superType;
        }
        return new EventTypeSerialization(new ArrayList<String>(path));
    }

    static class EventTypeSerialization implements Serializable {
        private List<String> path;

        public EventTypeSerialization(List<String> path) {
            this.path = path;
        }

        private Object readResolve() throws ObjectStreamException {
            EventType t = ROOT;
            for (int i = 0; i < path.size(); ++i) {
                String p = path.get(i);
                if (t.subTypes != null) {
                    EventType s = findSubType(t.subTypes.keySet(), p);
                    if (s == null) {
                        throw new InvalidObjectException("Cannot find event type \"" + p + "\" (of " + t + ")");
                    }
                    t = s;
                } else {
                    throw new InvalidObjectException("Cannot find event type \"" + p + "\" (of " + t + ")");
                }
            }
            return t;
        }

        private EventType findSubType(Set<EventType> subTypes, String name) {
            for (EventType t : subTypes) {
                if (((t.name == null && name == null) || (t.name != null && t.name.equals(name)))) {
                    return t;
                }
            }
            return null;
        }

    }
}
