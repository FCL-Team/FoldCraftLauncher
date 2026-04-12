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

import static org.lwjgl.util.mapped.MappedObjectUnsafe.INSTANCE;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * [INTERNAL USE ONLY]
 * <p/>
 * Helper class used by the bytecode transformer.
 *
 * @author Riven
 */
public class MappedHelper {

	public static void setup(MappedObject mo, ByteBuffer buffer, int align, int sizeof) {
		if ( LWJGLUtil.CHECKS && mo.baseAddress != 0L )
			throw new IllegalStateException("this method should not be called by user-code");

		if ( LWJGLUtil.CHECKS && !buffer.isDirect() )
			throw new IllegalArgumentException("bytebuffer must be direct");
		mo.preventGC = buffer;

		if ( LWJGLUtil.CHECKS && align <= 0 )
			throw new IllegalArgumentException("invalid alignment");

		if ( LWJGLUtil.CHECKS && (sizeof <= 0 || sizeof % align != 0) )
			throw new IllegalStateException("sizeof not a multiple of alignment");

		long addr = MemoryUtil.getAddress(buffer);
		if ( LWJGLUtil.CHECKS && addr % align != 0 )
			throw new IllegalStateException("buffer address not aligned on " + align + " bytes");

		mo.baseAddress = mo.viewAddress = addr;
	}

	public static void checkAddress(long viewAddress, MappedObject mapped) {
		mapped.checkAddress(viewAddress);
	}

	public static void put_views(MappedSet2 set, int view) {
		set.view(view);
	}

	public static void put_views(MappedSet3 set, int view) {
		set.view(view);
	}

	public static void put_views(MappedSet4 set, int view) {
		set.view(view);
	}

	public static void put_view(MappedObject mapped, int view, int sizeof) {
		mapped.setViewAddress(mapped.baseAddress + view * sizeof);
	}

	public static int get_view(MappedObject mapped, int sizeof) {
		return (int)(mapped.viewAddress - mapped.baseAddress) / sizeof;
	}

	public static void put_view_shift(MappedObject mapped, int view, int sizeof_shift) {
		mapped.setViewAddress(mapped.baseAddress + (view << sizeof_shift));
	}

	public static int get_view_shift(MappedObject mapped, int sizeof_shift) {
		return ((int)(mapped.viewAddress - mapped.baseAddress)) >> sizeof_shift;
	}

	public static void put_view_next(MappedObject mapped, int sizeof) {
		mapped.setViewAddress(mapped.viewAddress + sizeof);
	}

	public static MappedObject dup(MappedObject src, MappedObject dst) {
		dst.baseAddress = src.baseAddress;
		dst.viewAddress = src.viewAddress;
		dst.preventGC = src.preventGC;
		return dst;
	}

	public static MappedObject slice(MappedObject src, MappedObject dst) {
		dst.baseAddress = src.viewAddress; // !
		dst.viewAddress = src.viewAddress;
		dst.preventGC = src.preventGC;
		return dst;
	}

	public static void copy(MappedObject src, MappedObject dst, int bytes) {
		if ( MappedObject.CHECKS ) {
			src.checkRange(bytes);
			dst.checkRange(bytes);
		}

		INSTANCE.copyMemory(src.viewAddress, dst.viewAddress, bytes);
	}

	public static ByteBuffer newBuffer(long address, int capacity) {
		return MappedObjectUnsafe.newBuffer(address, capacity);
	}

	// ---- primitive fields read/write

	// byte

	public static void bput(byte value, long addr) {
		INSTANCE.putByte(addr, value);
	}

	public static void bput(MappedObject mapped, byte value, int fieldOffset) {
		INSTANCE.putByte(mapped.viewAddress + fieldOffset, value);
	}

	public static byte bget(long addr) {
		return INSTANCE.getByte(addr);
	}

	public static byte bget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getByte(mapped.viewAddress + fieldOffset);
	}

	public static void bvput(byte value, long addr) {
		INSTANCE.putByteVolatile(null, addr, value);
	}

	public static void bvput(MappedObject mapped, byte value, int fieldOffset) {
		INSTANCE.putByteVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static byte bvget(long addr) {
		return INSTANCE.getByteVolatile(null, addr);
	}

	public static byte bvget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getByteVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// short

	public static void sput(short value, long addr) {
		INSTANCE.putShort(addr, value);
	}

	public static void sput(MappedObject mapped, short value, int fieldOffset) {
		INSTANCE.putShort(mapped.viewAddress + fieldOffset, value);
	}

	public static short sget(long addr) {
		return INSTANCE.getShort(addr);
	}

	public static short sget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getShort(mapped.viewAddress + fieldOffset);
	}

	public static void svput(short value, long addr) {
		INSTANCE.putShortVolatile(null, addr, value);
	}

	public static void svput(MappedObject mapped, short value, int fieldOffset) {
		INSTANCE.putShortVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static short svget(long addr) {
		return INSTANCE.getShortVolatile(null, addr);
	}

	public static short svget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getShortVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// char

	public static void cput(char value, long addr) {
		INSTANCE.putChar(addr, value);
	}

	public static void cput(MappedObject mapped, char value, int fieldOffset) {
		INSTANCE.putChar(mapped.viewAddress + fieldOffset, value);
	}

	public static char cget(long addr) {
		return INSTANCE.getChar(addr);
	}

	public static char cget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getChar(mapped.viewAddress + fieldOffset);
	}

	public static void cvput(char value, long addr) {
		INSTANCE.putCharVolatile(null, addr, value);
	}

	public static void cvput(MappedObject mapped, char value, int fieldOffset) {
		INSTANCE.putCharVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static char cvget(long addr) {
		return INSTANCE.getCharVolatile(null, addr);
	}

	public static char cvget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getCharVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// int

	public static void iput(int value, long addr) {
		INSTANCE.putInt(addr, value);
	}

	public static void iput(MappedObject mapped, int value, int fieldOffset) {
		INSTANCE.putInt(mapped.viewAddress + fieldOffset, value);
	}

	public static int iget(long address) {
		return INSTANCE.getInt(address);
	}

	public static int iget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getInt(mapped.viewAddress + fieldOffset);
	}

	public static void ivput(int value, long addr) {
		INSTANCE.putIntVolatile(null, addr, value);
	}

	public static void ivput(MappedObject mapped, int value, int fieldOffset) {
		INSTANCE.putIntVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static int ivget(long address) {
		return INSTANCE.getIntVolatile(null, address);
	}

	public static int ivget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getIntVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// float

	public static void fput(float value, long addr) {
		INSTANCE.putFloat(addr, value);
	}

	public static void fput(MappedObject mapped, float value, int fieldOffset) {
		INSTANCE.putFloat(mapped.viewAddress + fieldOffset, value);
	}

	public static float fget(long addr) {
		return INSTANCE.getFloat(addr);
	}

	public static float fget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getFloat(mapped.viewAddress + fieldOffset);
	}

	public static void fvput(float value, long addr) {
		INSTANCE.putFloatVolatile(null, addr, value);
	}

	public static void fvput(MappedObject mapped, float value, int fieldOffset) {
		INSTANCE.putFloatVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static float fvget(long addr) {
		return INSTANCE.getFloatVolatile(null, addr);
	}

	public static float fvget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getFloatVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// long

	public static void jput(long value, long addr) {
		INSTANCE.putLong(addr, value);
	}

	public static void jput(MappedObject mapped, long value, int fieldOffset) {
		INSTANCE.putLong(mapped.viewAddress + fieldOffset, value);
	}

	public static long jget(long addr) {
		return INSTANCE.getLong(addr);
	}

	public static long jget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getLong(mapped.viewAddress + fieldOffset);
	}

	public static void jvput(long value, long addr) {
		INSTANCE.putLongVolatile(null, addr, value);
	}

	public static void jvput(MappedObject mapped, long value, int fieldOffset) {
		INSTANCE.putLongVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static long jvget(long addr) {
		return INSTANCE.getLongVolatile(null, addr);
	}

	public static long jvget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getLongVolatile(null, mapped.viewAddress + fieldOffset);
	}

	// address

	public static void aput(long value, long addr) {
		INSTANCE.putAddress(addr, value);
	}

	public static void aput(MappedObject mapped, long value, int fieldOffset) {
		INSTANCE.putAddress(mapped.viewAddress + fieldOffset, value);
	}

	public static long aget(long addr) {
		return INSTANCE.getAddress(addr);
	}

	public static long aget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getAddress(mapped.viewAddress + fieldOffset);
	}

	// double

	public static void dput(double value, long addr) {
		INSTANCE.putDouble(addr, value);
	}

	public static void dput(MappedObject mapped, double value, int fieldOffset) {
		INSTANCE.putDouble(mapped.viewAddress + fieldOffset, value);
	}

	public static double dget(long addr) {
		return INSTANCE.getDouble(addr);
	}

	public static double dget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getDouble(mapped.viewAddress + fieldOffset);
	}

	public static void dvput(double value, long addr) {
		INSTANCE.putDoubleVolatile(null, addr, value);
	}

	public static void dvput(MappedObject mapped, double value, int fieldOffset) {
		INSTANCE.putDoubleVolatile(null, mapped.viewAddress + fieldOffset, value);
	}

	public static double dvget(long addr) {
		return INSTANCE.getDoubleVolatile(null, addr);
	}

	public static double dvget(MappedObject mapped, int fieldOffset) {
		return INSTANCE.getDoubleVolatile(null, mapped.viewAddress + fieldOffset);
	}

}