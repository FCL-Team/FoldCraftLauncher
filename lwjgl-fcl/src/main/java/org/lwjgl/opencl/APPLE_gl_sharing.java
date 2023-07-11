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

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opencl.*;

import java.nio.ByteBuffer;

@CLPlatformExtension
@CLDeviceExtension
@Extension(postfix = "APPLE", className = "APPLEGLSharing", nativeName = "cl_APPLE_gl_sharing")
public interface APPLE_gl_sharing {

	/**
	 * This enumerated value can be specified as part of the &lt;properties&gt; argument passed to clCreateContext
	 * to allow OpenCL compliant devices in an existing CGL share group to be used as the devices in
	 * the newly created CL context. GL objects that were allocated in the given CGL share group can
	 * now be shared between CL and GL.
	 */
	int CL_CONTEXT_PROPERTY_USE_CGL_SHAREGROUP_APPLE = 0x10000000;

	/**
	 * Returns a cl_device_id for the CL device associated with the virtual screen for
	 * the given CGL context.  Return type: cl_device_id
	 */
	int CL_CGL_DEVICE_FOR_CURRENT_VIRTUAL_SCREEN_APPLE = 0x10000002;

	/**
	 * Returns an array of cl_device_ids for the CL device(s) corresponding to
	 * the virtual screen(s) for the given CGL context.   Return type: cl_device_id[]
	 */
	int CL_CGL_DEVICES_FOR_SUPPORTED_VIRTUAL_SCREENS_APPLE = 0x10000003;

	/** Error code returned by clGetGLContextInfoAPPLE if an invalid platform_gl_ctx is provided */
	int CL_INVALID_GL_CONTEXT_APPLE = -1000;

	@Code(
		javaBeforeNative = "\t\tif ( param_value_size_ret == null && APIUtil.isDevicesParam(param_name) ) param_value_size_ret = APIUtil.getBufferPointer();",
		javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS && param_value != null && APIUtil.isDevicesParam(param_name) ) context.getParent().registerCLDevices(param_value, param_value_size_ret);"
	)
	@cl_int
	int clGetGLContextInfoAPPLE(@PointerWrapper("cl_context") CLContext context,
	                            @Check("1") @NativeType("cl_void") PointerBuffer platform_gl_ctx,
	                            @NativeType("cl_gl_platform_info") int param_name,
	                            @AutoSize(value = "param_value", canBeNull = true) @size_t long param_value_size,
	                            @OutParameter @Check(canBeNull = true) @cl_void ByteBuffer param_value,
	                            @OutParameter @Check(value = "1", canBeNull = true) @NativeType("size_t") PointerBuffer param_value_size_ret);

}