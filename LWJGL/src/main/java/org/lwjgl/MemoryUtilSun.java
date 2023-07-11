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
package org.lwjgl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;

import sun.misc.Unsafe;
import sun.reflect.FieldAccessor;

/**
 * MemoryUtil.Accessor implementations that depend on sun.misc.
 * We use reflection to grab these, so that we can compile on JDKs
 * that do not support sun.misc.
 *
 * @author Spasi
 */
final class MemoryUtilSun {

	private MemoryUtilSun() {
	}

	/** Implementation using sun.misc.Unsafe. */
	private static class AccessorUnsafe implements MemoryUtil.Accessor {

		private final Unsafe unsafe;
		private final long   address;

		AccessorUnsafe() {
			try {
				unsafe = getUnsafeInstance();
				address = unsafe.objectFieldOffset(MemoryUtil.getAddressField());
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public long getAddress(final Buffer buffer) {
			return unsafe.getLong(buffer, address);
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

	/** Implementation using reflection on ByteBuffer, FieldAccessor is used directly. */
	private static class AccessorReflectFast implements MemoryUtil.Accessor {

		private final FieldAccessor addressAccessor;

		AccessorReflectFast() {
			Field address;
			try {
				address = MemoryUtil.getAddressField();
			} catch (NoSuchFieldException e) {
				throw new UnsupportedOperationException(e);
			}
			address.setAccessible(true);

			try {
				Method m = Field.class.getDeclaredMethod("acquireFieldAccessor", boolean.class);
				m.setAccessible(true);
				addressAccessor = (FieldAccessor)m.invoke(address, true);
			} catch (Exception e) {
				throw new UnsupportedOperationException(e);
			}
		}

		public long getAddress(final Buffer buffer) {
			return addressAccessor.getLong(buffer);
		}

	}

}