package com.tungsten.fclcore.fakefx.util;

import com.tungsten.fclcore.fakefx.beans.NamedArg;

import java.io.Serializable;

 /**
  * <p>A convenience class to represent name-value pairs.</p>
  * @since JavaFX 2.0
  */
public class Pair<K,V> implements Serializable{

    /**
     * Key of this <code>Pair</code>.
     */
    private K key;

    /**
     * Gets the key for this pair.
     * @return key for this pair
     */
    public K getKey() { return key; }

    /**
     * Value of this this <code>Pair</code>.
     */
    private V value;

    /**
     * Gets the value for this pair.
     * @return value for this pair
     */
    public V getValue() { return value; }

    /**
     * Creates a new pair
     * @param key The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(@NamedArg("key") K key, @NamedArg("value") V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * <p><code>String</code> representation of this
     * <code>Pair</code>.</p>
     *
     * <p>The default name/value delimiter '=' is always used.</p>
     *
     *  @return <code>String</code> representation of this <code>Pair</code>
     */
    @Override
    public String toString() {
        return key + "=" + value;
    }

    /**
     * <p>Generate a hash code for this <code>Pair</code>.</p>
     *
     * <p>The hash code is calculated using both the name and
     * the value of the <code>Pair</code>.</p>
     *
     * @return hash code for this <code>Pair</code>
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (key != null ? key.hashCode() : 0);
        hash = 31 * hash + (value != null ? value.hashCode() : 0);
        return hash;
    }

     /**
      * <p>Test this <code>Pair</code> for equality with another
      * <code>Object</code>.</p>
      *
      * <p>If the <code>Object</code> to be tested is not a
      * <code>Pair</code> or is <code>null</code>, then this method
      * returns <code>false</code>.</p>
      *
      * <p>Two <code>Pair</code>s are considered equal if and only if
      * both the names and values are equal.</p>
      *
      * @param o the <code>Object</code> to test for
      * equality with this <code>Pair</code>
      * @return <code>true</code> if the given <code>Object</code> is
      * equal to this <code>Pair</code> else <code>false</code>
      */
     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (o instanceof Pair) {
             Pair pair = (Pair) o;
             if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
             if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
             return true;
         }
         return false;
     }
 }

