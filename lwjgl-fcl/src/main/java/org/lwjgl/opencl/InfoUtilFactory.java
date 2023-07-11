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
import org.lwjgl.opencl.api.CLBufferRegion;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL10GL.*;
import static org.lwjgl.opencl.CL11.*;

/**
 * This class contains concrete InfoUtil implementations for our CLObject
 * class. The public CLObject classes are grabbing these via reflection,
 * so that they can be compiled for the generator.
 *
 * @author Spasi
 */
final class InfoUtilFactory {

	private InfoUtilFactory() {}

	static final InfoUtil<CLCommandQueue> CL_COMMAND_QUEUE_UTIL = new InfoUtilAbstract<CLCommandQueue>() {
		protected int getInfo(final CLCommandQueue object, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetCommandQueueInfo(object, param_name, param_value, null);
		}
	};

	static final CLContext.CLContextUtil CL_CONTEXT_UTIL = new CLContextUtil();
	private static final class CLContextUtil extends InfoUtilAbstract<CLContext> implements CLContext.CLContextUtil {

		protected int getInfo(final CLContext context, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetContextInfo(context, param_name, param_value, param_value_size_ret);
		}

		public List<CLDevice> getInfoDevices(final CLContext context) {
			context.checkValid();

			final int num_devices;

			if ( CLCapabilities.getPlatformCapabilities(context.getParent()).OpenCL11 )
				num_devices = getInfoInt(context, CL_CONTEXT_NUM_DEVICES);
			else {
				final PointerBuffer size_ret = APIUtil.getBufferPointer();
				clGetContextInfo(context, CL_CONTEXT_DEVICES, null, size_ret);
				num_devices = (int)(size_ret.get(0) / PointerBuffer.getPointerSize());
			}

			final PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
			clGetContextInfo(context, CL_CONTEXT_DEVICES, deviceIDs.getBuffer(), null);

			final List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
			for ( int i = 0; i < num_devices; i++ )
				devices.add(context.getParent().getCLDevice(deviceIDs.get(i)));

			return devices.size() == 0 ? null : devices;

		}

		/** Custom clCreateContext implementation (reuses APIUtil.getBufferPointer) */
		public CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException {
			final int propertyCount = 2 + (share_drawable == null ? 0 : 4) + 1;

			final PointerBuffer properties = APIUtil.getBufferPointer(propertyCount + devices.size());
			properties.put(CL_CONTEXT_PLATFORM).put(platform);
			if ( share_drawable != null )
				share_drawable.setCLSharingProperties(properties);
			properties.put(0);

			properties.position(propertyCount); // Make sure we're at the right offset, setCLSharingProperties might not use all 4 positions.
			for ( CLDevice device : devices )
				properties.put(device);

			final long function_pointer = CLCapabilities.clCreateContext;
			BufferChecks.checkFunctionAddress(function_pointer);
			if ( errcode_ret != null )
				BufferChecks.checkBuffer(errcode_ret, 1);
			else if ( LWJGLUtil.DEBUG )
				errcode_ret = APIUtil.getBufferInt();
			final long user_data = pfn_notify == null || pfn_notify.isCustom() ? 0 : CallbackUtil.createGlobalRef(pfn_notify);
			CLContext __result = null;
			try {
				__result = new CLContext(nclCreateContext(MemoryUtil.getAddress0(properties.getBuffer()), devices.size(), MemoryUtil.getAddress(properties, propertyCount), pfn_notify == null ? 0 : pfn_notify.getPointer(), user_data, MemoryUtil.getAddressSafe(errcode_ret), function_pointer), platform);
				if ( LWJGLUtil.DEBUG )
					Util.checkCLError(errcode_ret.get(0));
				return __result;
			} finally {
				if ( __result != null ) __result.setContextCallback(user_data);
			}
		}

		public CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
			final int propertyCount = 2 + (share_drawable == null ? 0 : 4) + 1;

			final PointerBuffer properties = APIUtil.getBufferPointer(propertyCount);
			properties.put(CL_CONTEXT_PLATFORM).put(platform);
			if ( share_drawable != null )
				share_drawable.setCLSharingProperties(properties);
			properties.put(0);
			properties.flip();

			return clCreateContextFromType(properties, device_type, pfn_notify, errcode_ret);
		}

		public List<CLImageFormat> getSupportedImageFormats(final CLContext context, final long flags, final int image_type, final Filter<CLImageFormat> filter) {
			final IntBuffer numBuffer = APIUtil.getBufferInt();
			clGetSupportedImageFormats(context, flags, image_type, null, numBuffer);

			final int num_image_formats = numBuffer.get(0);
			if ( num_image_formats == 0 )
				return null;

			final ByteBuffer formatBuffer = BufferUtils.createByteBuffer(num_image_formats * CLImageFormat.STRUCT_SIZE);
			clGetSupportedImageFormats(context, flags, image_type, formatBuffer, null);

			final List<CLImageFormat> formats = new ArrayList<CLImageFormat>(num_image_formats);
			for ( int i = 0; i < num_image_formats; i++ ) {
				final int offset = num_image_formats * CLImageFormat.STRUCT_SIZE;
				final CLImageFormat format = new CLImageFormat(
					formatBuffer.getInt(offset),
					formatBuffer.getInt(offset + 4)
				);
				if ( filter == null || filter.accept(format) )
					formats.add(format);
			}

			return formats.size() == 0 ? null : formats;
		}

	}

	static final InfoUtil<CLDevice> CL_DEVICE_UTIL = new CLDeviceUtil();
	private static final class CLDeviceUtil extends InfoUtilAbstract<CLDevice> {

		protected int getInfo(final CLDevice device, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetDeviceInfo(device, param_name, param_value, param_value_size_ret);
		}

		protected int getInfoSizeArraySize(final CLDevice device, final int param_name) {
			switch ( param_name ) {
				case CL_DEVICE_MAX_WORK_ITEM_SIZES:
					return getInfoInt(device, CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS);
				default:
					throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
			}
		}

	}

	static final CLEvent.CLEventUtil CL_EVENT_UTIL = new CLEventUtil();
	private static final class CLEventUtil extends InfoUtilAbstract<CLEvent> implements CLEvent.CLEventUtil {

		protected int getInfo(final CLEvent event, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetEventInfo(event, param_name, param_value, param_value_size_ret);
		}

		public long getProfilingInfoLong(final CLEvent event, final int param_name) {
			event.checkValid();

			final ByteBuffer buffer = APIUtil.getBufferByte(8);
			clGetEventProfilingInfo(event, param_name, buffer, null);

			return buffer.getLong(0);
		}

	}

	static final CLKernel.CLKernelUtil CL_KERNEL_UTIL = new CLKernelUtil();
	private static final class CLKernelUtil extends InfoUtilAbstract<CLKernel> implements CLKernel.CLKernelUtil {

		public void setArg(final CLKernel kernel, final int index, final byte value) {
			clSetKernelArg(kernel, index, 1, APIUtil.getBufferByte(1).put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final short value) {
			clSetKernelArg(kernel, index, 2, APIUtil.getBufferShort().put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final int value) {
			clSetKernelArg(kernel, index, 4, APIUtil.getBufferInt().put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final long value) {
			clSetKernelArg(kernel, index, 8, APIUtil.getBufferLong().put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final float value) {
			clSetKernelArg(kernel, index, 4, APIUtil.getBufferFloat().put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final double value) {
			clSetKernelArg(kernel, index, 8, APIUtil.getBufferDouble().put(0, value));
		}

		public void setArg(final CLKernel kernel, final int index, final CLObject value) {
			clSetKernelArg(kernel, index, value);
		}

		public void setArgSize(final CLKernel kernel, final int index, final long size) {
			clSetKernelArg(kernel, index, size);
		}

		protected int getInfo(final CLKernel kernel, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetKernelInfo(kernel, param_name, param_value, param_value_size_ret);
		}

		public long getWorkGroupInfoSize(final CLKernel kernel, final CLDevice device, final int param_name) {
			device.checkValid();

			final PointerBuffer buffer = APIUtil.getBufferPointer();
			clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);

			return buffer.get(0);
		}

		public long[] getWorkGroupInfoSizeArray(final CLKernel kernel, final CLDevice device, final int param_name) {
			device.checkValid();

			final int size;
			switch ( param_name ) {
				case CL_KERNEL_COMPILE_WORK_GROUP_SIZE:
					size = 3;
					break;
				default:
					throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
			}

			final PointerBuffer buffer = APIUtil.getBufferPointer(size);

			clGetKernelWorkGroupInfo(kernel, device, param_name, buffer.getBuffer(), null);

			final long[] array = new long[size];
			for ( int i = 0; i < size; i++ )
				array[i] = buffer.get(i);

			return array;
		}

		public long getWorkGroupInfoLong(final CLKernel kernel, final CLDevice device, final int param_name) {
			device.checkValid();

			final ByteBuffer buffer = APIUtil.getBufferByte(8);
			clGetKernelWorkGroupInfo(kernel, device, param_name, buffer, null);

			return buffer.getLong(0);
		}

	}

	static final CLMem.CLMemUtil CL_MEM_UTIL = new CLMemUtil();
	private static final class CLMemUtil extends InfoUtilAbstract<CLMem> implements CLMem.CLMemUtil {

		protected int getInfo(final CLMem mem, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetMemObjectInfo(mem, param_name, param_value, param_value_size_ret);
		}

		public CLMem createImage2D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_row_pitch, final Buffer host_ptr, IntBuffer errcode_ret) {
			final ByteBuffer formatBuffer = APIUtil.getBufferByte(2 * 4);
			formatBuffer.putInt(0, image_format.getChannelOrder());
			formatBuffer.putInt(4, image_format.getChannelType());

			final long function_pointer = CLCapabilities.clCreateImage2D;
			BufferChecks.checkFunctionAddress(function_pointer);
			if ( errcode_ret != null )
				BufferChecks.checkBuffer(errcode_ret, 1);
			else if ( LWJGLUtil.DEBUG )
				errcode_ret = APIUtil.getBufferInt();

			CLMem __result = new CLMem(nclCreateImage2D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_row_pitch, MemoryUtil.getAddress0Safe(host_ptr) +
			                                                                                                                                                             (host_ptr != null ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage2DSize(formatBuffer, image_width, image_height, image_row_pitch)) : 0),
			                                            MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
			if ( LWJGLUtil.DEBUG )
				Util.checkCLError(errcode_ret.get(0));
			return __result;
		}

		public CLMem createImage3D(final CLContext context, final long flags, final CLImageFormat image_format, final long image_width, final long image_height, final long image_depth, final long image_row_pitch, final long image_slice_pitch, final Buffer host_ptr, IntBuffer errcode_ret) {
			final ByteBuffer formatBuffer = APIUtil.getBufferByte(2 * 4);
			formatBuffer.putInt(0, image_format.getChannelOrder());
			formatBuffer.putInt(4, image_format.getChannelType());

			final long function_pointer = CLCapabilities.clCreateImage3D;
			BufferChecks.checkFunctionAddress(function_pointer);
			if ( errcode_ret != null )
				BufferChecks.checkBuffer(errcode_ret, 1);
			else if ( LWJGLUtil.DEBUG )
				errcode_ret = APIUtil.getBufferInt();

			CLMem __result = new CLMem(nclCreateImage3D(context.getPointer(), flags, MemoryUtil.getAddress(formatBuffer, 0), image_width, image_height, image_depth, image_row_pitch, image_slice_pitch, MemoryUtil.getAddress0Safe(host_ptr) +
			                                                                                                                                                                                             (host_ptr != null ? BufferChecks.checkBuffer(host_ptr, CLChecks.calculateImage3DSize(formatBuffer, image_width, image_height, image_depth, image_row_pitch, image_slice_pitch)) : 0),
			                                            MemoryUtil.getAddressSafe(errcode_ret), function_pointer), context);
			if ( LWJGLUtil.DEBUG )
				Util.checkCLError(errcode_ret.get(0));
			return __result;
		}

		public CLMem createSubBuffer(final CLMem mem, final long flags, final int buffer_create_type, final CLBufferRegion buffer_create_info, final IntBuffer errcode_ret) {
			final PointerBuffer infoBuffer = APIUtil.getBufferPointer(2);

			infoBuffer.put(buffer_create_info.getOrigin());
			infoBuffer.put(buffer_create_info.getSize());

			return clCreateSubBuffer(mem, flags, buffer_create_type, infoBuffer.getBuffer(), errcode_ret);
		}

		public ByteBuffer getInfoHostBuffer(final CLMem mem) {
			mem.checkValid();

			if ( LWJGLUtil.DEBUG ) {
				final long mem_flags = getInfoLong(mem, CL_MEM_FLAGS);
				if ( (mem_flags & CL_MEM_USE_HOST_PTR) != CL_MEM_USE_HOST_PTR )
					throw new IllegalArgumentException("The specified CLMem object does not use host memory.");
			}

			final long size = getInfoSize(mem, CL_MEM_SIZE);
			if ( size == 0 )
				return null;

			final long address = getInfoSize(mem, CL_MEM_HOST_PTR);

			return CL.getHostBuffer(address, (int)size);
		}

		public long getImageInfoSize(final CLMem mem, final int param_name) {
			mem.checkValid();

			final PointerBuffer buffer = APIUtil.getBufferPointer();
			clGetImageInfo(mem, param_name, buffer.getBuffer(), null);

			return buffer.get(0);
		}

		public CLImageFormat getImageInfoFormat(final CLMem mem) {
			mem.checkValid();

			final ByteBuffer format = APIUtil.getBufferByte(2 * 4);

			clGetImageInfo(mem, CL_IMAGE_FORMAT, format, null);

			return new CLImageFormat(format.getInt(0), format.getInt(4));
		}

		public int getImageInfoFormat(final CLMem mem, final int index) {
			mem.checkValid();

			final ByteBuffer format = APIUtil.getBufferByte(2 * 4);

			clGetImageInfo(mem, CL_IMAGE_FORMAT, format, null);

			return format.getInt(index << 2);
		}

		public int getGLObjectType(final CLMem mem) {
			mem.checkValid();

			final IntBuffer buffer = APIUtil.getBufferInt();
			clGetGLObjectInfo(mem, buffer, null);

			return buffer.get(0);
		}

		public int getGLObjectName(final CLMem mem) {
			mem.checkValid();

			final IntBuffer buffer = APIUtil.getBufferInt();
			clGetGLObjectInfo(mem, null, buffer);

			return buffer.get(0);
		}

		public int getGLTextureInfoInt(final CLMem mem, final int param_name) {
			mem.checkValid();

			final ByteBuffer buffer = APIUtil.getBufferByte(4);
			clGetGLTextureInfo(mem, param_name, buffer, null);

			return buffer.getInt(0);
		}

	}

	static final CLPlatform.CLPlatformUtil CL_PLATFORM_UTIL = new CLPlatformUtil();
	private static final class CLPlatformUtil extends InfoUtilAbstract<CLPlatform> implements CLPlatform.CLPlatformUtil {

		protected int getInfo(final CLPlatform platform, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetPlatformInfo(platform, param_name, param_value, param_value_size_ret);
		}

		public List<CLPlatform> getPlatforms(final Filter<CLPlatform> filter) {
			final IntBuffer numBuffer = APIUtil.getBufferInt();
			clGetPlatformIDs(null, numBuffer);

			final int num_platforms = numBuffer.get(0);
			if ( num_platforms == 0 )
				return null;

			final PointerBuffer platformIDs = APIUtil.getBufferPointer(num_platforms);
			clGetPlatformIDs(platformIDs, null);

			final List<CLPlatform> platforms = new ArrayList<CLPlatform>(num_platforms);
			for ( int i = 0; i < num_platforms; i++ ) {
				final CLPlatform platform = CLPlatform.getCLPlatform(platformIDs.get(i));
				if ( filter == null || filter.accept(platform) )
					platforms.add(platform);
			}

			return platforms.size() == 0 ? null : platforms;
		}

		public List<CLDevice> getDevices(final CLPlatform platform, final int device_type, final Filter<CLDevice> filter) {
			platform.checkValid();

			final IntBuffer numBuffer = APIUtil.getBufferInt();
			clGetDeviceIDs(platform, device_type, null, numBuffer);

			final int num_devices = numBuffer.get(0);
			if ( num_devices == 0 )
				return null;

			final PointerBuffer deviceIDs = APIUtil.getBufferPointer(num_devices);
			clGetDeviceIDs(platform, device_type, deviceIDs, null);

			final List<CLDevice> devices = new ArrayList<CLDevice>(num_devices);
			for ( int i = 0; i < num_devices; i++ ) {
				final CLDevice device = platform.getCLDevice(deviceIDs.get(i));
				if ( filter == null || filter.accept(device) )
					devices.add(device);
			}

			return devices.size() == 0 ? null : devices;
		}

	}

	static final CLProgram.CLProgramUtil CL_PROGRAM_UTIL = new CLProgramUtil();
	private static final class CLProgramUtil extends InfoUtilAbstract<CLProgram> implements CLProgram.CLProgramUtil {

		protected int getInfo(final CLProgram program, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetProgramInfo(program, param_name, param_value, param_value_size_ret);
		}

		protected int getInfoSizeArraySize(final CLProgram program, final int param_name) {
			switch ( param_name ) {
				case CL_PROGRAM_BINARY_SIZES:
					return getInfoInt(program, CL_PROGRAM_NUM_DEVICES);
				default:
					throw new IllegalArgumentException("Unsupported parameter: " + LWJGLUtil.toHexString(param_name));
			}
		}

		public CLKernel[] createKernelsInProgram(final CLProgram program) {
			final IntBuffer numBuffer = APIUtil.getBufferInt();
			clCreateKernelsInProgram(program, null, numBuffer);

			final int num_kernels = numBuffer.get(0);
			if ( num_kernels == 0 )
				return null;

			final PointerBuffer kernelIDs = APIUtil.getBufferPointer(num_kernels);
			clCreateKernelsInProgram(program, kernelIDs, null);

			final CLKernel[] kernels = new CLKernel[num_kernels];
			for ( int i = 0; i < num_kernels; i++ )
				kernels[i] = program.getCLKernel(kernelIDs.get(i));

			return kernels;
		}

		public CLDevice[] getInfoDevices(final CLProgram program) {
			program.checkValid();

			final int size = getInfoInt(program, CL_PROGRAM_NUM_DEVICES);
			final PointerBuffer buffer = APIUtil.getBufferPointer(size);

			clGetProgramInfo(program, CL_PROGRAM_DEVICES, buffer.getBuffer(), null);

			final CLPlatform platform = program.getParent().getParent();
			final CLDevice[] array = new CLDevice[size];
			for ( int i = 0; i < size; i++ )
				array[i] = platform.getCLDevice(buffer.get(i));

			return array;
		}

		public ByteBuffer getInfoBinaries(final CLProgram program, ByteBuffer target) {
			program.checkValid();

			final PointerBuffer sizes = getSizesBuffer(program, CL_PROGRAM_BINARY_SIZES);

			int totalSize = 0;
			for ( int i = 0; i < sizes.limit(); i++ )
				totalSize += sizes.get(i);

			if ( target == null )
				target = BufferUtils.createByteBuffer(totalSize);
			else if ( LWJGLUtil.DEBUG )
				BufferChecks.checkBuffer(target, totalSize);

			clGetProgramInfo(program, sizes, target, null);

			return target;
		}

		public ByteBuffer[] getInfoBinaries(final CLProgram program, ByteBuffer[] target) {
			program.checkValid();

			if ( target == null ) {
				final PointerBuffer sizes = getSizesBuffer(program, CL_PROGRAM_BINARY_SIZES);

				target = new ByteBuffer[sizes.remaining()];
				for ( int i = 0; i < sizes.remaining(); i++ )
					target[i] = BufferUtils.createByteBuffer((int)sizes.get(i));
			} else if ( LWJGLUtil.DEBUG ) {
				final PointerBuffer sizes = getSizesBuffer(program, CL_PROGRAM_BINARY_SIZES);

				if ( target.length < sizes.remaining() )
					throw new IllegalArgumentException("The target array is not big enough: " + sizes.remaining() + " buffers are required.");

				for ( int i = 0; i < target.length; i++ )
					BufferChecks.checkBuffer(target[i], (int)sizes.get(i));
			}

			clGetProgramInfo(program, target, null);

			return target;
		}

		public String getBuildInfoString(final CLProgram program, final CLDevice device, final int param_name) {
			program.checkValid();

			final int bytes = getBuildSizeRet(program, device, param_name);
			if ( bytes <= 1 )
				return null;

			final ByteBuffer buffer = APIUtil.getBufferByte(bytes);
			clGetProgramBuildInfo(program, device, param_name, buffer, null);

			buffer.limit(bytes - 1); // Exclude null-termination
			return APIUtil.getString(buffer);
		}

		public int getBuildInfoInt(final CLProgram program, final CLDevice device, final int param_name) {
			program.checkValid();

			final ByteBuffer buffer = APIUtil.getBufferByte(4);
			clGetProgramBuildInfo(program, device, param_name, buffer, null);

			return buffer.getInt(0);
		}

		private static int getBuildSizeRet(final CLProgram program, final CLDevice device, final int param_name) {
			final PointerBuffer bytes = APIUtil.getBufferPointer();
			final int errcode = clGetProgramBuildInfo(program, device, param_name, null, bytes);
			if ( errcode != CL_SUCCESS )
				throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name));

			return (int)bytes.get(0);
		}

	}

	static final InfoUtil<CLSampler> CL_SAMPLER_UTIL = new InfoUtilAbstract<CLSampler>() {
		protected int getInfo(final CLSampler sampler, final int param_name, final ByteBuffer param_value, final PointerBuffer param_value_size_ret) {
			return clGetSamplerInfo(sampler, param_name, param_value, param_value_size_ret);
		}
	};

}