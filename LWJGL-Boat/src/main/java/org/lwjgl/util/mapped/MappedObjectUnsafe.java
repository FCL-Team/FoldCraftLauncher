/*
 * Copyright (c) 2002-2011 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.util.mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import sun.misc.Unsafe;

/**
 * [INTERNAL USE ONLY]
 *
 * @author Riven
 */
final class MappedObjectUnsafe {

	static final Unsafe INSTANCE = getUnsafeInstance();

	private static final long BUFFER_ADDRESS_OFFSET  = getObjectFieldOffset(ByteBuffer.class, "address");
	private static final long BUFFER_CAPACITY_OFFSET = getObjectFieldOffset(ByteBuffer.class, "capacity");

	private static final ByteBuffer global = ByteBuffer.allocateDirect(4 * 1024);

	static ByteBuffer newBuffer(long address, int capacity) {
		if ( address <= 0L || capacity < 0 )
			throw new IllegalStateException("you almost crashed the jvm");

		ByteBuffer buffer = global.duplicate().order(ByteOrder.nativeOrder());
		INSTANCE.putLong(buffer, BUFFER_ADDRESS_OFFSET, address);
		INSTANCE.putInt(buffer, BUFFER_CAPACITY_OFFSET, capacity);
		buffer.position(0);
		buffer.limit(capacity);
		return buffer;
	}

	private static long getObjectFieldOffset(Class<?> type, String fieldName) {
		while ( type != null ) {
			try {
				return INSTANCE.objectFieldOffset(type.getDeclaredField(fieldName));
			} catch (Throwable t) {
				type = type.getSuperclass();
			}
		}

		throw new UnsupportedOperationException();
	}

	private static Unsafe getUnsafeInstance() {
		final Field[] fields = Unsafe.class.getDeclaredFields();

		/*
		Different runtimes use different names for the Unsafe singleton,
		so we cannot use .getDeclaredField and we scan instead. For example:

		Oracle: theUnsafe
		PERC : m_unsafe_instance
		Android: THE_ONE
		*/
		for ( Field field : fields ) {
			if ( !field.getType().equals(Unsafe.class) )
				continue;

			final int modifiers = field.getModifiers();
			if ( !(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) )
				continue;

			field.setAccessible(true);
			try {
				return (Unsafe)field.get(null);
			} catch (IllegalAccessException e) {
				// ignore
			}
			break;
		}

		throw new UnsupportedOperationException();
	}

}