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
@Extension(postfix = "KHR", className = "KHRGLSharing")
public interface KHR_gl_sharing {

	/**
	 * Returned by clCreateContext, clCreateContextFromType, and
	 * clGetGLContextInfoKHR when an invalid OpenGL context or share group
	 * object handle is specified in &lt;properties&gt;:
	 */
	int CL_INVALID_GL_SHAREGROUP_REFERENCE_KHR = -1000;

	/** Accepted as the &lt;param_name&gt; argument of clGetGLContextInfoKHR: */
	int CL_CURRENT_DEVICE_FOR_GL_CONTEXT_KHR = 0x2006,
		CL_DEVICES_FOR_GL_CONTEXT_KHR = 0x2007;

	/**
	 * Accepted as an attribute name in the 'properties' argument of
	 * clCreateContext and clCreateContextFromType:
	 */
	int CL_GL_CONTEXT_KHR = 0x2008,
		CL_EGL_DISPLAY_KHR = 0x2009,
		CL_GLX_DISPLAY_KHR = 0x200A,
		CL_WGL_HDC_KHR = 0x200B,
		CL_CGL_SHAREGROUP_KHR = 0x200C;

	@Code(
		javaBeforeNative = "\t\tif ( param_value_size_ret == null && APIUtil.isDevicesParam(param_name) ) param_value_size_ret = APIUtil.getBufferPointer();",
		javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS && param_value != null && APIUtil.isDevicesParam(param_name) ) APIUtil.getCLPlatform(properties).registerCLDevices(param_value, param_value_size_ret);"
	)
	@cl_int
	int clGetGLContextInfoKHR(@NullTerminated @Const @NativeType("cl_context_properties") PointerBuffer properties,
	                          @NativeType("cl_gl_context_info") int param_name,
	                          @AutoSize(value = "param_value", canBeNull = true) @size_t long param_value_size,
	                          @OutParameter @Check(canBeNull = true) @cl_void ByteBuffer param_value,
	                          @OutParameter @Check(value = "1", canBeNull = true) @NativeType("size_t") PointerBuffer param_value_size_ret);

}