package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

/**
 * A CLObjectChild container.
 *
 * @author Spasi
 */
class CLObjectRegistry<T extends CLObjectChild> {

	private FastLongMap<T> registry;

	CLObjectRegistry() {
	}

	final boolean isEmpty() {
		return registry == null || registry.isEmpty();
	}

	final T getObject(final long id) {
		return registry == null ? null : registry.get(id);
	}

	final boolean hasObject(final long id) {
		return registry != null && registry.containsKey(id);
	}

	final Iterable<FastLongMap.Entry<T>> getAll() {
		return registry;
	}

	void registerObject(final T object) {
		final FastLongMap<T> map = getMap();
		final Long key = object.getPointer();

		if ( LWJGLUtil.DEBUG && map.containsKey(key) )
			throw new IllegalStateException("Duplicate object found: " + object.getClass() + " - " + key);

		getMap().put(object.getPointer(), object);
	}

	void unregisterObject(final T object) {
		getMap().remove(object.getPointerUnsafe());
	}

	private FastLongMap<T> getMap() {
		if ( registry == null )
			registry = new FastLongMap<T>();

		return registry;
	}

}