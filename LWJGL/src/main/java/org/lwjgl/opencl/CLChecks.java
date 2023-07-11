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
import static org.lwjgl.opencl.CL11.*;

/**
 * Utility class that provides runtime checks for OpenCL method calls.
 * TODO: Revisit this when Java 7.0 is released, there will be new Buffer API with 64bit indices/sizes.
 *
 * @author Spasi
 */
final class CLChecks {

	private CLChecks() {
	}

	/**
	 * Calculates the number of bytes in the specified cl_mem buffer rectangle region.
	 *
	 * @param offset      the host offset
	 * @param region      the rectangle region
	 * @param row_pitch   the host row pitch
	 * @param slice_pitch the host slice pitch
	 *
	 * @return the region size in bytes
	 */
	static int calculateBufferRectSize(final PointerBuffer offset, final PointerBuffer region, long row_pitch, long slice_pitch) {
		if ( !LWJGLUtil.CHECKS )
			return 0;

		final long x = offset.get(0);
		final long y = offset.get(1);
		final long z = offset.get(2);

		if ( LWJGLUtil.DEBUG && (x < 0 || y < 0 || z < 0) )
			throw new IllegalArgumentException("Invalid cl_mem host offset: " + x + ", " + y + ", " + z);

		final long w = region.get(0);
		final long h = region.get(1);
		final long d = region.get(2);

		if ( LWJGLUtil.DEBUG && (w < 1 || h < 1 || d < 1) )
			throw new IllegalArgumentException("Invalid cl_mem rectangle region dimensions: " + w + " x " + h + " x " + d);

		if ( row_pitch == 0 )
			row_pitch = w;
		else if ( LWJGLUtil.DEBUG && row_pitch < w )
			throw new IllegalArgumentException("Invalid host row pitch specified: " + row_pitch);

		if ( slice_pitch == 0 )
			slice_pitch = row_pitch * h;
		else if ( LWJGLUtil.DEBUG && slice_pitch < (row_pitch * h) )
			throw new IllegalArgumentException("Invalid host slice pitch specified: " + slice_pitch);

		return (int)((z * slice_pitch + y * row_pitch + x) + (w * h * d));
	}

	/**
	 * Calculates the number of bytes in the specified cl_mem image region.
	 * This implementation assumes 1 byte per element, because we cannot the
	 * image type.
	 *
	 * @param region      the image region
	 * @param row_pitch   the row pitch
	 * @param slice_pitch the slice pitch
	 *
	 * @return the region size in bytes
	 */
	static int calculateImageSize(final PointerBuffer region, long row_pitch, long slice_pitch) {
		if ( !LWJGLUtil.CHECKS )
			return 0;

		final long w = region.get(0);
		final long h = region.get(1);
		final long d = region.get(2);

		if ( LWJGLUtil.DEBUG && (w < 1 || h < 1 || d < 1) )
			throw new IllegalArgumentException("Invalid cl_mem image region dimensions: " + w + " x " + h + " x " + d);

		if ( row_pitch == 0 )
			row_pitch = w;
		else if ( LWJGLUtil.DEBUG && row_pitch < w )
			throw new IllegalArgumentException("Invalid row pitch specified: " + row_pitch);

		if ( slice_pitch == 0 )
			slice_pitch = row_pitch * h;
		else if ( LWJGLUtil.DEBUG && slice_pitch < (row_pitch * h) )
			throw new IllegalArgumentException("Invalid slice pitch specified: " + slice_pitch);

		return (int)(slice_pitch * d);

	}

	/**
	 * Calculates the number of bytes in the specified 2D image.
	 *
	 * @param format    the cl_image_format struct
	 * @param w         the image width
	 * @param h         the image height
	 * @param row_pitch the image row pitch
	 *
	 * @return the 2D image size in bytes
	 */
	static int calculateImage2DSize(final ByteBuffer format, final long w, final long h, long row_pitch) {
		if ( !LWJGLUtil.CHECKS )
			return 0;

		if ( LWJGLUtil.DEBUG && (w < 1 || h < 1) )
			throw new IllegalArgumentException("Invalid 2D image dimensions: " + w + " x " + h);

		final int elementSize = getElementSize(format);

		if ( row_pitch == 0 )
			row_pitch = w * elementSize;
		else if ( LWJGLUtil.DEBUG && ((row_pitch < w * elementSize) || (row_pitch % elementSize != 0)) )
			throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);

		return (int)(row_pitch * h);
	}

	/**
	 * Calculates the number of bytes in the specified 3D image.
	 *
	 * @param format      the cl_image_format struct
	 * @param w           the image width
	 * @param h           the image height
	 * @param d           the image depth
	 * @param row_pitch   the image row pitch
	 * @param slice_pitch the image slice pitch
	 *
	 * @return the 3D image size in bytes
	 */
	static int calculateImage3DSize(final ByteBuffer format, final long w, final long h, final long d, long row_pitch, long slice_pitch) {
		if ( !LWJGLUtil.CHECKS )
			return 0;

		if ( LWJGLUtil.DEBUG && (w < 1 || h < 1 || d < 2) )
			throw new IllegalArgumentException("Invalid 3D image dimensions: " + w + " x " + h + " x " + d);

		final int elementSize = getElementSize(format);

		if ( row_pitch == 0 )
			row_pitch = w * elementSize;
		else if ( LWJGLUtil.DEBUG && ((row_pitch < w * elementSize) || (row_pitch % elementSize != 0)) )
			throw new IllegalArgumentException("Invalid image_row_pitch specified: " + row_pitch);

		if ( slice_pitch == 0 )
			slice_pitch = row_pitch * h;
		else if ( LWJGLUtil.DEBUG && ((row_pitch < row_pitch * h) || (slice_pitch % row_pitch != 0)) )
			throw new IllegalArgumentException("Invalid image_slice_pitch specified: " + row_pitch);

		return (int)(slice_pitch * d);
	}

	/**
	 * Returns the number of bytes per element for the specified image format.
	 *
	 * @param format a cl_image_format struct.
	 *
	 * @return the number of bytes per image element
	 */
	private static int getElementSize(final ByteBuffer format) {
		final int channelOrder = format.getInt(format.position() + 0);
		final int channelType = format.getInt(format.position() + 4);

		return getChannelCount(channelOrder) * getChannelSize(channelType);
	}

	/**
	 * Returns the number of channels in the specified cl_channel_order.
	 *
	 * @param channelOrder the cl_channel_order
	 *
	 * @return the number of channels
	 */
	private static int getChannelCount(final int channelOrder) {
		switch ( channelOrder ) {
			case CL_R:
			case CL_A:
			case CL_INTENSITY:
			case CL_LUMINANCE:
			case CL_Rx:
				return 1;
			case CL_RG:
			case CL_RA:
			case CL_RGx:
				return 2;
			case CL_RGB:
			case CL_RGBx:
				return 3;
			case CL_RGBA:
			case CL_BGRA:
			case CL_ARGB:
				return 4;
			default:
				throw new IllegalArgumentException("Invalid cl_channel_order specified: " + LWJGLUtil.toHexString(channelOrder));
		}
	}

	/**
	 * Returns the number of bytes in the specified cl_channel_type.
	 *
	 * @param channelType the cl_channel_type
	 *
	 * @return the number of bytes
	 */
	private static int getChannelSize(final int channelType) {
		switch ( channelType ) {
			case CL_SNORM_INT8:
			case CL_UNORM_INT8:
			case CL_SIGNED_INT8:
			case CL_UNSIGNED_INT8:
				return 1;
			case CL_SNORM_INT16:
			case CL_UNORM_INT16:
			case CL_UNORM_SHORT_565:
			case CL_UNORM_SHORT_555:
			case CL_SIGNED_INT16:
			case CL_UNSIGNED_INT16:
			case CL_HALF_FLOAT:
				return 2;
			case CL_UNORM_INT_101010:
			case CL_SIGNED_INT32:
			case CL_UNSIGNED_INT32:
			case CL_FLOAT:
				return 4;
			default:
				throw new IllegalArgumentException("Invalid cl_channel_type specified: " + LWJGLUtil.toHexString(channelType));
		}
	}

}