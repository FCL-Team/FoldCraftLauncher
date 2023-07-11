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
package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.*;

/**
 * Utility class for OpenGL API calls. Instances of APIUtil are created in ContextCapabilities,
 * so we have an instance per OpenGL context.
 *
 * @author spasi
 */
final class APIUtil {

	private static final int INITIAL_BUFFER_SIZE  = 256;
	private static final int INITIAL_LENGTHS_SIZE = 4;

	private static final int BUFFERS_SIZE = 32;

	private char[]     array;
	private ByteBuffer buffer;
	private IntBuffer  lengths;

	private final IntBuffer    ints;
	private final LongBuffer   longs;
	private final FloatBuffer  floats;
	private final DoubleBuffer doubles;

	APIUtil() {
		array = new char[INITIAL_BUFFER_SIZE];
		buffer = BufferUtils.createByteBuffer(INITIAL_BUFFER_SIZE);
		lengths = BufferUtils.createIntBuffer(INITIAL_LENGTHS_SIZE);

		ints = BufferUtils.createIntBuffer(BUFFERS_SIZE);
		longs = BufferUtils.createLongBuffer(BUFFERS_SIZE);

		floats = BufferUtils.createFloatBuffer(BUFFERS_SIZE);
		doubles = BufferUtils.createDoubleBuffer(BUFFERS_SIZE);
	}

	private static char[] getArray(final ContextCapabilities caps, final int size) {
		char[] array = caps.util.array;

		if ( array.length < size ) {
			int sizeNew = array.length << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			array = new char[size];
			caps.util.array = array;
		}

		return array;
	}

	static ByteBuffer getBufferByte(final ContextCapabilities caps, final int size) {
		ByteBuffer buffer = caps.util.buffer;

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			buffer = BufferUtils.createByteBuffer(size);
			caps.util.buffer = buffer;
		} else
			buffer.clear();

		return buffer;
	}

	private static ByteBuffer getBufferByteOffset(final ContextCapabilities caps, final int size) {
		ByteBuffer buffer = caps.util.buffer;

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			final ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
			bufferNew.put(buffer);
			caps.util.buffer = (buffer = bufferNew);
		} else {
			buffer.position(buffer.limit());
			buffer.limit(buffer.capacity());
		}

		return buffer;
	}

	static IntBuffer getBufferInt(final ContextCapabilities caps) { return caps.util.ints; }

	static LongBuffer getBufferLong(final ContextCapabilities caps) { return caps.util.longs; }

	static FloatBuffer getBufferFloat(final ContextCapabilities caps) { return caps.util.floats; }

	static DoubleBuffer getBufferDouble(final ContextCapabilities caps) { return caps.util.doubles; }

	static IntBuffer getLengths(final ContextCapabilities caps) {
		return getLengths(caps, 1);
	}

	static IntBuffer getLengths(final ContextCapabilities caps, final int size) {
		IntBuffer lengths = caps.util.lengths;

		if ( lengths.capacity() < size ) {
			int sizeNew = lengths.capacity();
			while ( sizeNew < size )
				sizeNew <<= 1;

			lengths = BufferUtils.createIntBuffer(size);
			caps.util.lengths = lengths;
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
	static String getString(final ContextCapabilities caps, final ByteBuffer buffer) {
		final int length = buffer.remaining();
		final char[] charArray = getArray(caps, length);

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
	static long getBuffer(final ContextCapabilities caps, final CharSequence string) {
		final ByteBuffer buffer = encode(getBufferByte(caps, string.length()), string);
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
	static long getBuffer(final ContextCapabilities caps, final CharSequence string, final int offset) {
		final ByteBuffer buffer = encode(getBufferByteOffset(caps, offset + string.length()), string);
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
	static long getBufferNT(final ContextCapabilities caps, final CharSequence string) {
		final ByteBuffer buffer = encode(getBufferByte(caps, string.length() + 1), string);
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
	static long getBuffer(final ContextCapabilities caps, final CharSequence[] strings) {
		final ByteBuffer buffer = getBufferByte(caps, getTotalLength(strings));

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
	static long getBufferNT(final ContextCapabilities caps, final CharSequence[] strings) {
		final ByteBuffer buffer = getBufferByte(caps, getTotalLength(strings) + strings.length);

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
	static long getLengths(final ContextCapabilities caps, final CharSequence[] strings) {
		IntBuffer buffer = getLengths(caps, strings.length);

		for ( CharSequence string : strings )
			buffer.put(string.length());

		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	static long getInt(final ContextCapabilities caps, final int value) {
		return MemoryUtil.getAddress0(getBufferInt(caps).put(0, value));
	}

	static long getBufferByte0(final ContextCapabilities caps) {
		return MemoryUtil.getAddress0(getBufferByte(caps, 0));
	}

}