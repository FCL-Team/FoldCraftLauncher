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

import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * This class provides utility methods for allocating cache-line-aligned
 * NIO buffers. The CPU cache line size is detected using a micro-benchmark
 * that exploits the performation degredation that occurs when different
 * threads write to different locations of the same cache line. The detection
 * should be reasonably robust on both the server and client VM, but there
 * are a few system properties that can be used to tune it.
 *
 * @author Spasi
 */
public final class CacheUtil {

	private static final int CACHE_LINE_SIZE;

	static {
		final Integer size = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineSize"); // forces a specific cache line size

		if ( size != null ) {
			if ( size < 1 )
				throw new IllegalStateException("Invalid CacheLineSize specified: " + size);
			CACHE_LINE_SIZE = size;
		} else if ( Runtime.getRuntime().availableProcessors() == 1 ) { // We cannot use false sharing to detect it
			/*
			    Spasi:

			    I have implemented a single-threaded benchmark for this, but it requires
			    lots of memory allocations and could not tune it for both the client and
			    server VM. It's not a big deal anyway, 64 bytes should be ok for any
			    single-core CPU.
			*/
			if ( LWJGLUtil.DEBUG )
				LWJGLUtil.log("Cannot detect cache line size on single-core CPUs, assuming 64 bytes.");
			CACHE_LINE_SIZE = 64;
		} else
			CACHE_LINE_SIZE = CacheLineSize.getCacheLineSize();
	}

	private CacheUtil() {
	}

	/**
	 * Returns the CPU cache line size, in number of bytes.
	 *
	 * @return the cache line size
	 */
	public static int getCacheLineSize() {
		return CACHE_LINE_SIZE;
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned bytebuffer with the specified size.
	 *
	 * @param size The size, in bytes
	 *
	 * @return a ByteBuffer
	 */
	public static ByteBuffer createByteBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(size + CACHE_LINE_SIZE);

		// Align to cache line.
		if ( MemoryUtil.getAddress(buffer) % CACHE_LINE_SIZE != 0 ) {
			// Round up to cache line boundary
			buffer.position(CACHE_LINE_SIZE - (int)(MemoryUtil.getAddress(buffer) & (CACHE_LINE_SIZE - 1)));
		}

		buffer.limit(buffer.position() + size);
		return buffer.slice().order(ByteOrder.nativeOrder());
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned shortbuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in shorts
	 *
	 * @return a ShortBuffer
	 */
	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned charbuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in chars
	 *
	 * @return an CharBuffer
	 */
	public static CharBuffer createCharBuffer(int size) {
		return createByteBuffer(size << 1).asCharBuffer();
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned intbuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in ints
	 *
	 * @return an IntBuffer
	 */
	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned longbuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in longs
	 *
	 * @return an LongBuffer
	 */
	public static LongBuffer createLongBuffer(int size) {
		return createByteBuffer(size << 3).asLongBuffer();
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned floatbuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in floats
	 *
	 * @return a FloatBuffer
	 */
	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}

	/**
	 * Construct a direct, native-ordered and cache-line-aligned doublebuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in floats
	 *
	 * @return a FloatBuffer
	 */
	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << 3).asDoubleBuffer();
	}

	/**
	 * Construct a cache-line-aligned PointerBuffer with the specified number
	 * of elements.
	 *
	 * @param size The size, in memory addresses
	 *
	 * @return a PointerBuffer
	 */
	public static PointerBuffer createPointerBuffer(int size) {
		return new PointerBuffer(createByteBuffer(size * PointerBuffer.getPointerSize()));
	}

}