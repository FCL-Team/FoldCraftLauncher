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

import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * This class is a wrapper around a cl_mem pointer.
 *
 * @author Spasi
 */
public final class CLMem extends CLObjectChild<CLContext> {

	private static final CLMemUtil util = (CLMemUtil)CLPlatform.getInfoUtilInstance(CLMem.class, "CL_MEM_UTIL");

	CLMem(final long pointer, final CLContext context) {
		super(pointer, context);
		if ( isValid() )
			context.getCLMemRegistry().registerObject(this);
	}

	// ---------------[ UTILITY METHODS ]---------------

	/**
	 * Creates a new 2D image object.
	 *
	 * @param context         the context on which to create the image object
	 * @param flags           the memory object flags
	 * @param image_format    the image format
	 * @param image_width     the image width
	 * @param image_height    the image height
	 * @param image_row_pitch the image row pitch
	 * @param host_ptr        the host buffer from which to read image data (optional)
	 * @param errcode_ret     the error code result
	 *
	 * @return the new CLMem object
	 */
	public static CLMem createImage2D(final CLContext context, final long flags, final CLImageFormat image_format,
	                                  final long image_width, final long image_height, final long image_row_pitch,
	                                  final Buffer host_ptr, final IntBuffer errcode_ret) {
		return util.createImage2D(context, flags, image_format, image_width, image_height, image_row_pitch, host_ptr, errcode_ret);
	}

	/**
	 * Creates a new 3D image object.
	 *
	 * @param context           the context on which to create the image object
	 * @param flags             the memory object flags
	 * @param image_format      the image format
	 * @param image_width       the image width
	 * @param image_height      the image height
	 * @param image_depth       the image depth
	 * @param image_row_pitch   the image row pitch
	 * @param image_slice_pitch the image slice pitch
	 * @param host_ptr          the host buffer from which to read image data (optional)
	 * @param errcode_ret       the error code result
	 *
	 * @return the new CLMem object
	 */
	public static CLMem createImage3D(final CLContext context, final long flags, final CLImageFormat image_format,
	                                  final long image_width, final long image_height, final long image_depth, final long image_row_pitch, final long image_slice_pitch,
	                                  final Buffer host_ptr, final IntBuffer errcode_ret) {
		return util.createImage3D(context, flags, image_format, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, host_ptr, errcode_ret);
	}

	public CLMem createSubBuffer(final long flags, final int buffer_create_type, final CLBufferRegion buffer_create_info, final IntBuffer errcode_ret) {
		return util.createSubBuffer(this, flags, buffer_create_type, buffer_create_info, errcode_ret);
	}

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getInfoInt(int param_name) {
		return util.getInfoInt(this, param_name);
	}

	/**
	 * Returns the size_t value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getInfoSize(int param_name) {
		return util.getInfoSize(this, param_name);
	}

	/**
	 * Returns the long value of the specified parameter. Can be used
	 * for both cl_ulong and cl_bitfield parameters.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getInfoLong(int param_name) {
		return util.getInfoLong(this, param_name);
	}

	/**
	 * Returns a direct ByteBuffer instance that points to the host
	 * memory that backs this CLMem object. Applicable only to CLMem
	 * objects that were created with the CL_MEM_USE_HOST_PTR flag.
	 *
	 * @return the host memory ByteBuffer
	 */
	public ByteBuffer getInfoHostBuffer() {
		return util.getInfoHostBuffer(this);
	}

	// clGetImageInfo methods

	/**
	 * Returns the size_t value of the specified parameter. Applicable to image objects only.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getImageInfoSize(int param_name) {
		return util.getImageInfoSize(this, param_name);
	}

	/**
	 * Returns the image format. Applicable to image objects only.
	 *
	 * @return the parameter value
	 */
	public CLImageFormat getImageFormat() {
		return util.getImageInfoFormat(this);
	}

	/**
	 * Returns the image channel order. Applicable to image objects only.
	 *
	 * @return the parameter value
	 */
	public int getImageChannelOrder() {
		return util.getImageInfoFormat(this, 0);
	}

	/**
	 * Returns the image channel type. Applicable to image objects only.
	 *
	 * @return the parameter value
	 */
	public int getImageChannelType() {
		return util.getImageInfoFormat(this, 1);
	}

	// clGetGLObjectInfo methods

	/**
	 * Returns the GL object type. Applicable to CLMem objects
	 * that have been created GL objects only.
	 *
	 * @return the parameter value
	 */
	public int getGLObjectType() {
		return util.getGLObjectType(this);
	}

	/**
	 * Returns the GL object name. Applicable to CLMem objects
	 * that have been created GL objects only.
	 *
	 * @return the parameter value
	 */
	public int getGLObjectName() {
		return util.getGLObjectName(this);
	}

	// clGetGLTextureInfo methods

	/**
	 * Returns the int value of the specified parameter. Applicable to CLMem objects
	 * that have been created by GL textures only.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getGLTextureInfoInt(int param_name) {
		return util.getGLTextureInfoInt(this, param_name);
	}

	/** CLMem utility methods interface. */
	interface CLMemUtil extends InfoUtil<CLMem> {

		CLMem createImage2D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_row_pitch, Buffer host_ptr, IntBuffer errcode_ret);

		CLMem createImage3D(CLContext context, long flags, CLImageFormat image_format, long image_width, long image_height, long image_depth, long image_row_pitch, long image_slice_pitch, Buffer host_ptr, IntBuffer errcode_ret);

		CLMem createSubBuffer(CLMem mem, long flags, int buffer_create_type, CLBufferRegion buffer_create_info, IntBuffer errcode_ret);

		ByteBuffer getInfoHostBuffer(CLMem mem);

		long getImageInfoSize(CLMem mem, int param_name);

		CLImageFormat getImageInfoFormat(CLMem mem);

		int getImageInfoFormat(CLMem mem, int index);

		int getGLObjectType(CLMem mem);

		int getGLObjectName(CLMem mem);

		int getGLTextureInfoInt(CLMem mem, int param_name);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	/**
	 * Sub-buffer factory. clCreateSubBuffer may return an existing CLMem.
	 *
	 * @param pointer the sub-buffer id
	 * @param context the context in which the sub-buffer was created
	 *
	 * @return the CLMem that represents the sub-buffer
	 */
	static CLMem create(final long pointer, final CLContext context) {
		CLMem clMem = context.getCLMemRegistry().getObject(pointer);
		if ( clMem == null )
			clMem = new CLMem(pointer, context);
		else
			clMem.retain();

		return clMem;
	}

	int release() {
		try {
			return super.release();
		} finally {
			if ( !isValid() )
				getParent().getCLMemRegistry().unregisterObject(this);
		}
	}

}