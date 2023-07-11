/*
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.lwjgl.opengles;

import java.util.Iterator;

/**
 * A hash map using primitive ints as keys rather than objects.
 *
 * @author Justin Couch
 * @author Alex Chaffee (alex@apache.org)
 * @author Stephen Colebourne
 * @author Nathan Sweet
 */
final class FastIntMap<V> implements Iterable<FastIntMap.Entry<V>> {

	private Entry[] table;
	private int size, mask, capacity, threshold;

	/** Same as: FastIntMap(16, 0.75f); */
	FastIntMap() {
		this(16, 0.75f);
	}

	/** Same as: FastIntMap(initialCapacity, 0.75f); */
	FastIntMap(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}

	FastIntMap(int initialCapacity, float loadFactor) {
		if ( initialCapacity > 1 << 30 ) throw new IllegalArgumentException("initialCapacity is too large.");
		if ( initialCapacity < 0 ) throw new IllegalArgumentException("initialCapacity must be greater than zero.");
		if ( loadFactor <= 0 ) throw new IllegalArgumentException("initialCapacity must be greater than zero.");
		capacity = 1;
		while ( capacity < initialCapacity )
			capacity <<= 1;
		this.threshold = (int)(capacity * loadFactor);
		this.table = new Entry[capacity];
		this.mask = capacity - 1;
	}

	private int index(final int key) {
		return index(key, mask);
	}

	private static int index(final int key, final int mask) {
		return key & mask;
	}

	public V put(int key, V value) {
		final Entry<V>[] table = this.table;
		int index = index(key);

		// Check if key already exists.
		for ( Entry<V> e = table[index]; e != null; e = e.next ) {
			if ( e.key != key ) continue;
			V oldValue = e.value;
			e.value = value;
			return oldValue;
		}

		table[index] = new Entry<V>(key, value, table[index]);

		if ( size++ >= threshold )
			rehash(table);

		return null;
	}

	private void rehash(final Entry<V>[] table) {
		final int newCapacity = 2 * capacity;
		final int newMask = newCapacity - 1;

		final Entry<V>[] newTable = new Entry[newCapacity];

		for ( int i = 0, index; i < table.length; i++ ) {
			Entry<V> e = table[i];
			if ( e == null ) continue;
			do {
				final Entry<V> next = e.next;
				index = index(e.key, newMask);
				e.next = newTable[index];
				newTable[index] = e;
				e = next;
			} while ( e != null );
		}

		this.table = newTable;
		capacity = newCapacity;
		mask = newMask;
		threshold *= 2;
	}

	public V get(int key) {
		final int index = index(key);
		for ( Entry<V> e = table[index]; e != null; e = e.next )
			if ( e.key == key ) return e.value;
		return null;
	}

	public boolean containsValue(Object value) {
		final Entry<V>[] table = this.table;
		for ( int i = table.length - 1; i >= 0; i-- )
			for ( Entry<V> e = table[i]; e != null; e = e.next )
				if ( e.value.equals(value) ) return true;
		return false;
	}

	public boolean containsKey(int key) {
		final int index = index(key);
		for ( Entry<V> e = table[index]; e != null; e = e.next )
			if ( e.key == key ) return true;
		return false;
	}

	public V remove(int key) {
		final int index = index(key);

		Entry<V> prev = table[index];
		Entry<V> e = prev;
		while ( e != null ) {
			Entry<V> next = e.next;
			if ( e.key == key ) {
				size--;
				if ( prev == e )
					table[index] = next;
				else
					prev.next = next;
				return e.value;
			}
			prev = e;
			e = next;
		}
		return null;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		final Entry<V>[] table = this.table;
		for ( int index = table.length - 1; index >= 0; index-- )
			table[index] = null;
		size = 0;
	}

	public EntryIterator iterator() {
		return new EntryIterator();
	}

	public class EntryIterator implements Iterator<Entry<V>> {

		private int nextIndex;
		private Entry<V> current;

		EntryIterator() {
			reset();
		}

		public void reset() {
			current = null;
			// Find first bucket.
			final Entry<V>[] table = FastIntMap.this.table;
			int i;
			for ( i = table.length - 1; i >= 0; i-- )
				if ( table[i] != null ) break;
			nextIndex = i;
		}

		public boolean hasNext() {
			if ( nextIndex >= 0 ) return true;
			Entry e = current;
			return e != null && e.next != null;
		}

		public Entry<V> next() {
			// Next entry in current bucket.
			Entry<V> e = current;
			if ( e != null ) {
				e = e.next;
				if ( e != null ) {
					current = e;
					return e;
				}
			}
			// Use the bucket at nextIndex and find the next nextIndex.
			final Entry<V>[] table = FastIntMap.this.table;
			int i = nextIndex;
			e = current = table[i];
			while ( --i >= 0 )
				if ( table[i] != null ) break;
			nextIndex = i;
			return e;
		}

		public void remove() {
			FastIntMap.this.remove(current.key);
		}
	}

	static final class Entry<T> {

		final int key;
		T value;
		Entry<T> next;

		Entry(int key, T value, Entry<T> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public int getKey() {
			return key;
		}

		public T getValue() {
			return value;
		}

	}

}