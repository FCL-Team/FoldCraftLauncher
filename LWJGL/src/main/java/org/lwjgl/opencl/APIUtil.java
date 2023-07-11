/*
 * Copyright (c) 2002-2010 LWJGL Project
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
package org.lwjgl.opencl;

import org.lwjgl.*;
import org.lwjgl.opencl.FastLongMap.Entry;

import java.nio.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static org.lwjgl.opencl.APPLEGLSharing.*;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.EXTDeviceFission.*;
import static org.lwjgl.opencl.KHRGLSharing.*;

/**
 * Utility class for OpenCL API calls.
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

	private static final ThreadLocal<ByteBuffer> bufferByteTL = new ThreadLocal<ByteBuffer>() {
		protected ByteBuffer initialValue() { return BufferUtils.createByteBuffer(INITIAL_BUFFER_SIZE); }
	};

	private static final ThreadLocal<PointerBuffer> bufferPointerTL = new ThreadLocal<PointerBuffer>() {
		protected PointerBuffer initialValue() { return BufferUtils.createPointerBuffer(INITIAL_BUFFER_SIZE); }
	};

	private static final ThreadLocal<PointerBuffer> lengthsTL = new ThreadLocal<PointerBuffer>() {
		protected PointerBuffer initialValue() { return BufferUtils.createPointerBuffer(INITIAL_LENGTHS_SIZE); }
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
		ByteBuffer buffer = bufferByteTL.get();

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			buffer = BufferUtils.createByteBuffer(size);
			bufferByteTL.set(buffer);
		} else
			buffer.clear();

		return buffer;
	}

	private static ByteBuffer getBufferByteOffset(final int size) {
		ByteBuffer buffer = bufferByteTL.get();

		if ( buffer.capacity() < size ) {
			int sizeNew = buffer.capacity() << 1;
			while ( sizeNew < size )
				sizeNew <<= 1;

			final ByteBuffer bufferNew = BufferUtils.createByteBuffer(size);
			bufferNew.put(buffer);
			bufferByteTL.set(buffer = bufferNew);
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

	static ShortBuffer getBufferShort() { return buffersTL.get().shorts; }

	static IntBuffer getBufferInt() { return buffersTL.get().ints; }

	static IntBuffer getBufferIntDebug() { return buffersTL.get().intsDebug; }

	static LongBuffer getBufferLong() { return buffersTL.get().longs; }

	static FloatBuffer getBufferFloat() { return buffersTL.get().floats; }

	static DoubleBuffer getBufferDouble() { return buffersTL.get().doubles; }

	static PointerBuffer getBufferPointer() { return buffersTL.get().pointers; }

	static PointerBuffer getLengths() {
		return getLengths(1);
	}

	static PointerBuffer getLengths(final int size) {
		PointerBuffer lengths = lengthsTL.get();

		if ( lengths.capacity() < size ) {
			int sizeNew = lengths.capacity();
			while ( sizeNew < size )
				sizeNew <<= 1;

			lengths = BufferUtils.createPointerBuffer(size);
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
	 * @return the String lengths in a PointerBuffer
	 */
	static long getLengths(final CharSequence[] strings) {
		PointerBuffer buffer = getLengths(strings.length);

		for ( CharSequence string : strings )
			buffer.put(string.length());

		buffer.flip();
		return MemoryUtil.getAddress0(buffer);
	}

	/**
	 * Returns a buffer containing the lengths of the specified buffers.
	 *
	 * @param buffers the buffer array
	 *
	 * @return the buffer lengths in a PointerBuffer
	 */
	static long getLengths(final ByteBuffer[] buffers) {
		PointerBuffer lengths = getLengths(buffers.length);

		for ( ByteBuffer buffer : buffers )
			lengths.put(buffer.remaining());

		lengths.flip();
		return MemoryUtil.getAddress0(lengths);
	}

	static int getSize(final PointerBuffer lengths) {
		long size = 0;
		for ( int i = lengths.position(); i < lengths.limit(); i++ )
			size += lengths.get(i);

		return (int)size;
	}

	static long getPointer(final PointerWrapper pointer) {
		return MemoryUtil.getAddress0(getBufferPointer().put(0, pointer));
	}

	static long getPointerSafe(final PointerWrapper pointer) {
		return MemoryUtil.getAddress0(getBufferPointer().put(0, pointer == null ? 0L : pointer.getPointer()));
	}

	private static class Buffers {

		final ShortBuffer shorts;
		final IntBuffer   ints;
		final IntBuffer   intsDebug;
		final LongBuffer  longs;

		final FloatBuffer  floats;
		final DoubleBuffer doubles;

		final PointerBuffer pointers;

		Buffers() {
			shorts = BufferUtils.createShortBuffer(BUFFERS_SIZE);
			ints = BufferUtils.createIntBuffer(BUFFERS_SIZE);
			intsDebug = BufferUtils.createIntBuffer(1);
			longs = BufferUtils.createLongBuffer(BUFFERS_SIZE);

			floats = BufferUtils.createFloatBuffer(BUFFERS_SIZE);
			doubles = BufferUtils.createDoubleBuffer(BUFFERS_SIZE);

			pointers = BufferUtils.createPointerBuffer(BUFFERS_SIZE);
		}

	}

	/* ------------------------------------------------------------------------
	---------------------------------------------------------------------------
					OPENCL API UTILITIES BELOW
	---------------------------------------------------------------------------
	------------------------------------------------------------------------ */

	static Set<String> getExtensions(final String extensionList) {
		final Set<String> extensions = new HashSet<String>();

		if ( extensionList != null ) {
			final StringTokenizer tokenizer = new StringTokenizer(extensionList);
			while ( tokenizer.hasMoreTokens() )
				extensions.add(tokenizer.nextToken());
		}

		return extensions;
	}

	static boolean isDevicesParam(final int param_name) {
		switch ( param_name ) {
			case CL_CONTEXT_DEVICES:
			case CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR:
			case CL_DEVICES_FOR_GL_CONTEXT_KHR:
			case CL_CGL_DEVICE_FOR_CURRENT_VIRTUAL_SCREEN_APPLE:
			case CL_CGL_DEVICES_FOR_SUPPORTED_VIRTUAL_SCREENS_APPLE:
				return true;
		}

		return false;
	}

	static CLPlatform getCLPlatform(final PointerBuffer properties) {
		long platformID = 0;

		final int keys = properties.remaining() / 2;
		for ( int k = 0; k < keys; k++ ) {
			final long key = properties.get(k << 1);
			if ( key == 0 )
				break;

			if ( key == CL_CONTEXT_PLATFORM ) {
				platformID = properties.get((k << 1) + 1);
				break;
			}
		}

		if ( platformID == 0 )
			throw new IllegalArgumentException("Could not find CL_CONTEXT_PLATFORM in cl_context_properties.");

		final CLPlatform platform = CLPlatform.getCLPlatform(platformID);
		if ( platform == null )
			throw new IllegalStateException("Could not find a valid CLPlatform. Make sure clGetPlatformIDs has been used before.");

		return platform;
	}

	static ByteBuffer getNativeKernelArgs(final long user_func_ref, final CLMem[] clMems, final long[] sizes) {
		final ByteBuffer args = getBufferByte(8 + 4 + (clMems == null ? 0 : clMems.length * (4 + PointerBuffer.getPointerSize())));

		args.putLong(0, user_func_ref);
		if ( clMems == null )
			args.putInt(8, 0);
		else {
			args.putInt(8, clMems.length);
			int byteIndex = 12;
			for ( int i = 0; i < clMems.length; i++ ) {
				if ( LWJGLUtil.DEBUG && !clMems[i].isValid() )
					throw new IllegalArgumentException("An invalid CLMem object was specified.");
				args.putInt(byteIndex, (int)sizes[i]); // CLMem size
				byteIndex += (4 + PointerBuffer.getPointerSize()); // Skip size and make room for the pointer
			}
		}

		return args;
	}

	// ------------------------------------------------------------------------------------

	/**
	 * Releases all sub-devices created from the specified CLDevice.
	 *
	 * @param device the CLDevice to clear
	 */
	static void releaseObjects(final CLDevice device) {
		// Release objects only if we're about to hit 0.
		if ( !device.isValid() || device.getReferenceCount() > 1 )
			return;

		releaseObjects(device.getSubCLDeviceRegistry(), DESTRUCTOR_CLSubDevice);
	}

	/**
	 * Releases all objects contained in the specified CLContext.
	 *
	 * @param context the CLContext to clear
	 */
	static void releaseObjects(final CLContext context) {
		// Release objects only if we're about to hit 0.
		if ( !context.isValid() || context.getReferenceCount() > 1 )
			return;

		releaseObjects(context.getCLEventRegistry(), DESTRUCTOR_CLEvent);
		releaseObjects(context.getCLProgramRegistry(), DESTRUCTOR_CLProgram);
		releaseObjects(context.getCLSamplerRegistry(), DESTRUCTOR_CLSampler);
		releaseObjects(context.getCLMemRegistry(), DESTRUCTOR_CLMem);
		releaseObjects(context.getCLCommandQueueRegistry(), DESTRUCTOR_CLCommandQueue);
	}

	/**
	 * Releases all objects contained in the specified CLProgram.
	 *
	 * @param program the CLProgram to clear
	 */
	static void releaseObjects(final CLProgram program) {
		// Release objects only if we're about to hit 0.
		if ( !program.isValid() || program.getReferenceCount() > 1 )
			return;

		releaseObjects(program.getCLKernelRegistry(), DESTRUCTOR_CLKernel);
	}

	/**
	 * Releases all objects contained in the specified CLCommandQueue.
	 *
	 * @param queue the CLCommandQueue to clear
	 */
	static void releaseObjects(final CLCommandQueue queue) {
		// Release objects only if we're about to hit 0.
		if ( !queue.isValid() || queue.getReferenceCount() > 1 )
			return;

		releaseObjects(queue.getCLEventRegistry(), DESTRUCTOR_CLEvent);
	}

	private static <T extends CLObjectChild> void releaseObjects(final CLObjectRegistry<T> registry, final ObjectDestructor<T> destructor) {
		if ( registry.isEmpty() )
			return;

		for ( Entry<T> entry : registry.getAll() ) {
			final T object = entry.value;
			while ( object.isValid() )
				destructor.release(object);
		}
	}

	private static final ObjectDestructor<CLDevice>       DESTRUCTOR_CLSubDevice    = new ObjectDestructor<CLDevice>() {
		public void release(final CLDevice object) { clReleaseDeviceEXT(object); }
	};
	private static final ObjectDestructor<CLMem>          DESTRUCTOR_CLMem          = new ObjectDestructor<CLMem>() {
		public void release(final CLMem object) { clReleaseMemObject(object); }
	};
	private static final ObjectDestructor<CLCommandQueue> DESTRUCTOR_CLCommandQueue = new ObjectDestructor<CLCommandQueue>() {
		public void release(final CLCommandQueue object) { clReleaseCommandQueue(object); }
	};
	private static final ObjectDestructor<CLSampler>      DESTRUCTOR_CLSampler      = new ObjectDestructor<CLSampler>() {
		public void release(final CLSampler object) { clReleaseSampler(object); }
	};
	private static final ObjectDestructor<CLProgram>      DESTRUCTOR_CLProgram      = new ObjectDestructor<CLProgram>() {
		public void release(final CLProgram object) { clReleaseProgram(object); }
	};
	private static final ObjectDestructor<CLKernel>       DESTRUCTOR_CLKernel       = new ObjectDestructor<CLKernel>() {
		public void release(final CLKernel object) { clReleaseKernel(object); }
	};
	private static final ObjectDestructor<CLEvent>        DESTRUCTOR_CLEvent        = new ObjectDestructor<CLEvent>() {
		public void release(final CLEvent object) { clReleaseEvent(object); }
	};

	private interface ObjectDestructor<T extends CLObjectChild> {

		void release(T object);

	}

}