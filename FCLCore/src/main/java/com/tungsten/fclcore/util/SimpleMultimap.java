package com.tungsten.fclcore.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A simple implementation of Multimap.
 * Just a combination of map and set.
 */
public final class SimpleMultimap<K, V> {

    private final Map<K, Collection<V>> map;
    private final Supplier<Collection<V>> valuer;

    public SimpleMultimap(Supplier<Map<K, Collection<V>>> mapper, Supplier<Collection<V>> valuer) {
        this.map = mapper.get();
        this.valuer = valuer;
    }

    public int size() {
        return values().size();
    }

    public Set<K> keys() {
        return map.keySet();
    }

    public Collection<V> values() {
        Collection<V> res = valuer.get();
        for (Map.Entry<K, Collection<V>> entry : map.entrySet())
            res.addAll(entry.getValue());
        return res;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(K key) {
        return map.containsKey(key) && !map.get(key).isEmpty();
    }

    public Collection<V> get(K key) {
        return map.computeIfAbsent(key, any -> valuer.get());
    }

    public void put(K key, V value) {
        Collection<V> set = get(key);
        set.add(value);
    }

    public void putAll(K key, Collection<? extends V> value) {
        Collection<V> set = get(key);
        set.addAll(value);
    }

    public Collection<V> removeKey(K key) {
        return map.remove(key);
    }

    public boolean removeValue(V value) {
        boolean flag = false;
        for (Collection<V> c : map.values())
            flag |= c.remove(value);
        return flag;
    }

    public boolean removeValue(K key, V value) {
        return get(key).remove(value);
    }

    public void clear() {
        map.clear();
    }

    public void clear(K key) {
        if (map.containsKey(key))
            map.get(key).clear();
        else
            map.put(key, valuer.get());
    }
}
