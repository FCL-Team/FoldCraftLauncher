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
package org.lwjgl.opengles;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * Utility class for OpenGL ES API calls.
 *
 * @author spasi
 */
final class APIUtil {

	private static final int INITIAL_BUFFER_SIZE  = 256;
	private static final int INITIAL_LENGTHS_SIZE = 4;

	private static final int BUFFERS_SIZE = 32;

	private static final ThreadLocal<char[]> arrayTL = new ThreadLocal<char[]>() {
		protected char[] initialValue() { return new char[INITIAL_BUFFER_SIZE]; }
	};

	private static final ThreadLocal<ByteBuffer> bufferTL = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() { return BufferUtils.createByteBuffer(INITIAL_BUFFER_SIZE); }
	};

	private static final ThreadLocal<PointerBuffer> bufferPointerTL = new ThreadLocal<PointerBuffer>() {
		protected PointerBuffer initialValue() { return BufferUtils.createPointerBuffer(INITIAL_BUFFER_SIZE); }
	};

	private static final ThreadLocal<IntBuffer> lengthsTL = new ThreadLocal<IntBuffer>() {
		protected IntBuffer initialValue() { return BufferUtils.createIntBuffer(INITIAL_LENGTHS_SIZE); }
	};

	private static final ThreadLocal<Buffers> buffersTL = new ThreadLocal<Buffers>() {
		protected Buffers initialValue() { return new Buffers(); }
	};

	private APIUtil() {
	}

	private static char[] getArray(final int size) {
		char[] array = arrayTL.get();

		if ( array.length < size ) {
			int sizeNew = array.length << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			array = new char[size];
			arrayTL.set(array);
		}

		return array;
	}

	static ByteBuffer getBufferByte(final int size) {
		ByteBuffer buffer = bufferTL.get();

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			buffer = BufferUtils.createByteBuffer(size);
			bufferTL.set(buffer);
		} else
			buffer.clear();

		return buffer;
	}

	private static ByteBuffer getBufferByteOffset(final int size) {
		ByteBuffer buffer = bufferTL.get();

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			final ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
			bufferNew.put(buffer);
			bufferTL.set(buffer = bufferNew);
		} else {
			buffer.position(buffer.limit());
			buffer.limit(buffer.capacity());
		}

		return buffer;
	}

	static PointerBuffer getBufferPointer(final int size) {
		PointerBuffer buffer = bufferPointerTL.get();

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			buffer = BufferUtils.createPointerBuffer(size);
			bufferPointerTL.set(buffer);
		} else
			buffer.clear();

		return buffer;
	}

	static IntBuffer getBufferInt() { return buffersTL.get().ints; }

	static LongBuffer getBufferLong() { return buffersTL.get().longs; }

	static FloatBuffer getBufferFloat() { return buffersTL.get().floats; }

	static IntBuffer getLengths() {
		return getLengths(1);
	}

	static IntBuffer getLengths(final int size) {
		IntBuffer lengths = lengthsTL.get();

		if ( lengths.capacity() < size ) {
			int sizeNew = lengths.capacity();
			while ( sizeNew < size )
				sizeNew <<= 1;

			lengths = BufferUtils.createIntBuffer(size);
			lengthsTL.set(lengths);
		} else
			lengths.clear();

		return lengths;
	}

	/**
	 * Simple ASCII encoding.
	 *
	 * @param buffer The target buffer
	 * @param string The source string
	 */
	private static ByteBuffer encode(final ByteBuffer buffer, final CharSequence string) {
		for ( int i = 0; i < string.length(); i++ ) {
			final char c = string.charAt(i);
			if ( LWJGLUtil.DEBUG && 0x80 <= c ) // Silently ignore and map to 0x1A.
				buffer.put((byte)0x1A);
			else
				buffer.put((byte)c);
		}

		return buffer;
	}

	/**
	 * Reads a byte string from the specified buffer.
	 *
	 * @param buffer
	 *
	 * @return the buffer as a String.
	 */
	static String getString(final ByteBuffer buffer) {
		final int length = buffer.remaining();
		final char[] charArray = getArray(length);

		for ( int i = buffer.position(); i < buffer.limit(); i++ )
			charArray[i - buffer.position()] = (char)buffer.get(i);

		return new String(charArray, 0, length);
	}

	/**
	 * Returns a buffer containing the specified string as bytes.
	 *
	 * @param string
	 *
	 * @return the String as a ByteBuffer
	 */
	static long getBuffer(final CharSequence string) {
		final ByteBuffer buffer = encode(getBufferByte(string.length()), string);
		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	/**
	 * Returns a buffer containing the specified string as bytes, starting at the specified offset.
	 *
	 * @param string
	 *
	 * @return the String as a ByteBuffer
	 */
	static long getBuffer(final CharSequence string, final int offset) {
		final ByteBuffer buffer = encode(getBufferByteOffset(offset + string.length()), string);
		buffer.flip();
		return MemoryUtil.getAddress(buffer);
	}

	/**
	 * Returns a buffer containing the specified string as bytes, including null-termination.
	 *
	 * @param string
	 *
	 * @return the String as a ByteBuffer
	 */
	static long getBufferNT(final CharSequence string) {
		final ByteBuffer buffer = encode(getBufferByte(string.length() + 1), string);
		buffer.put((byte)0);
		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	static int getTotalLength(final CharSequence[] strings) {
		int length = 0;
		for ( CharSequence string : strings )
			length += string.length();

		return length;
	}

	/**
	 * Returns a buffer containing the specified strings as bytes.
	 *
	 * @param strings
	 *
	 * @return the Strings as a ByteBuffer
	 */
	static long getBuffer(final CharSequence[] strings) {
		final ByteBuffer buffer = getBufferByte(getTotalLength(strings));

		for ( CharSequence string : strings )
			encode(buffer, string);

		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	/**
	 * Returns a buffer containing the specified strings as bytes, including null-termination.
	 *
	 * @param strings
	 *
	 * @return the Strings as a ByteBuffer
	 */
	static long getBufferNT(final CharSequence[] strings) {
		final ByteBuffer buffer = getBufferByte(getTotalLength(strings) + strings.length);

		for ( CharSequence string : strings ) {
			encode(buffer, string);
			buffer.put((byte)0);
		}

		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	/**
	 * Returns a buffer containing the lengths of the specified strings.
	 *
	 * @param strings
	 *
	 * @return the String lengths in an IntBuffer
	 */
	static long getLengths(final CharSequence[] strings) {
		IntBuffer buffer = getLengths(strings.length);

		for ( CharSequence string : strings )
			buffer.put(string.length());

		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	static long getInt(final int value) {
		return MemoryUtil.getAddress(getBufferInt().put(0, value), 0);
	}

	static long getBufferByte0() {
		return MemoryUtil.getAddress0(getBufferByte(0));
	}

	private static class Buffers {

		final IntBuffer   ints;
		final LongBuffer  longs;
		final FloatBuffer floats;

		Buffers() {
			ints = BufferUtils.createIntBuffer(BUFFERS_SIZE);
			longs = BufferUtils.createLongBuffer(BUFFERS_SIZE);
			floats = BufferUtils.createFloatBuffer(BUFFERS_SIZE);
		}

	}

}