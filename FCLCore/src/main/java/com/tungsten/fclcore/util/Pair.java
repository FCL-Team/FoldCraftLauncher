package com.tungsten.fclcore.util;

import java.util.Map;
import java.util.Objects;

public final class Pair<K, V> implements Map.Entry<K, V> {

    public static <K, V> Pair<K, V> pair(K key, V value) {
        return new Pair<>(key, value);
    }

    private K key;
    private V value;

    private Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V original = this.value;
        this.value = value;
        return original;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.key);
        hash = 23 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
