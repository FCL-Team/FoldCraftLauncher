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

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

import java.nio.ByteBuffer;

import static org.lwjgl.opencl.CL10.*;

/**
 * Base implementation of information utility classes.
 *
 * @author Spasi
 */
abstract class InfoUtilAbstract<T extends CLObject> implements InfoUtil<T> {

	protected InfoUtilAbstract() {
	}

	protected abstract int getInfo(T object, int param_name, ByteBuffer param_value, PointerBuffer param_value_size_ret);

	// Optional

	protected int getInfoSizeArraySize(final T object, final int param_name) {
		throw new UnsupportedOperationException();
	}

	protected PointerBuffer getSizesBuffer(final T object, final int param_name) {
		final int size = getInfoSizeArraySize(object, param_name);

		final PointerBuffer buffer = APIUtil.getBufferPointer(size);
		buffer.limit(size);

		getInfo(object, param_name, buffer.getBuffer(), null);

		return buffer;
	}

	public int getInfoInt(final T object, final int param_name) {
		object.checkValid();

		final ByteBuffer buffer = APIUtil.getBufferByte(4);
		getInfo(object, param_name, buffer, null);

		return buffer.getInt(0);
	}

	public long getInfoSize(final T object, final int param_name) {
		object.checkValid();

		final PointerBuffer buffer = APIUtil.getBufferPointer();
		getInfo(object, param_name, buffer.getBuffer(), null);

		return buffer.get(0);
	}

	public long[] getInfoSizeArray(final T object, final int param_name) {
		object.checkValid();

		final int size = getInfoSizeArraySize(object, param_name);
		final PointerBuffer buffer = APIUtil.getBufferPointer(size);

		getInfo(object, param_name, buffer.getBuffer(), null);

		final long[] array = new long[size];
		for ( int i = 0; i < size; i++ )
			array[i] = buffer.get(i);

		return array;
	}

	public long getInfoLong(final T object, final int param_name) {
		object.checkValid();

		final ByteBuffer buffer = APIUtil.getBufferByte(8);
		getInfo(object, param_name, buffer, null);

		return buffer.getLong(0);
	}

	public String getInfoString(final T object, final int param_name) {
		object.checkValid();

		final int bytes = getSizeRet(object, param_name);
		if ( bytes <= 1 )
			return null;

		final ByteBuffer buffer = APIUtil.getBufferByte(bytes);
		getInfo(object, param_name, buffer, null);

		buffer.limit(bytes - 1); // Exclude null-termination
		return APIUtil.getString(buffer);
	}

	protected final int getSizeRet(final T object, final int param_name) {
		final PointerBuffer bytes = APIUtil.getBufferPointer();
		final int errcode = getInfo(object, param_name, null, bytes);
		if ( errcode != CL_SUCCESS )
			throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));

		return (int)bytes.get(0);
	}

}