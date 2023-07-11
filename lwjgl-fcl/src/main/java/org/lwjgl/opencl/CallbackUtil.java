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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that handles OpenCL API callbacks.
 *
 * @author Spasi
 */
final class CallbackUtil {

	private static final Map<CLContext, Long> contextUserData = new HashMap<CLContext, Long>();

	private CallbackUtil() {}

	/**
	 * Creates a new global reference to the specified Object.
	 *
	 * @param obj the Object
	 *
	 * @return the GlobalRef memory address or 0 if the Object is null.
	 */
	static long createGlobalRef(final Object obj) {
		return obj == null ? 0 : ncreateGlobalRef(obj);
	}

	/**
	 * Creates a new global reference to the specified Object.
	 *
	 * @param obj the Object
	 *
	 * @return the GlobalRef memory address.
	 */
	private static native long ncreateGlobalRef(Object obj);

	/**
	 * Deletes a global reference.
	 *
	 * @param ref the GlobalRef memory address.
	 */
	static native void deleteGlobalRef(long ref);

	/**
	 * Deletes the global reference represented by user_data if an OpenCL error occured.
	 *
	 * @param errcode   the error code
	 * @param user_data the GlobalRef memory address
	 */
	static void checkCallback(final int errcode, final long user_data) {
		if ( errcode != 0x0 && user_data != 0 )
			deleteGlobalRef(user_data);
	}

	/* [ Context callback functionality ]
		This is a little weird, so here's an explanation of what's happening for future reference:
		Before making the clCreateContext call we create a global reference to the CLContextCallback object (using JNI's NewGlobalRef function).
		We pass that global reference to the user_data parameter of clCreateContext. If clCreateContext returns anything but CL_SUCCESS, we
		immediately destroy the global reference to avoid the memory leak. If the new context was created successfully, we associate the context
		with the global reference in the contextUserData HashMap. On a future call to clReleaseContext, we clear that association and destroy the
		global reference (if the reference count is 0).
	*/

	/**
	 * Returns the memory address of the native function we pass to clCreateContext(FromType).
	 *
	 * @return the callback function address
	 */
	static native long getContextCallback();

	/* [ Other callback functionality ]
		The other callbacks are simpler. We create the GlobalRef before passing the callback,
		we delete it when we receive the callback call.
	*/

	/**
	 * Returns the memory address of the native function we pass to clSetMemObjectDestructorCallback.
	 *
	 * @return the callback function address
	 */
	static native long getMemObjectDestructorCallback();

	/**
	 * Returns the memory address of the native function we pass to clBuildProgram.
	 *
	 * @return the callback function address
	 */
	static native long getProgramCallback();

	/**
	 * Returns the memory address of the native function we pass to clEnqueueNativeKernel.
	 *
	 * @return the callback function address
	 */
	static native long getNativeKernelCallback();

	/**
	 * Returns the memory address of the native function we pass to clSetEventCallback.
	 *
	 * @return the callback function address
	 */
	static native long getEventCallback();

	/**
	 * Returns the memory address of the native function we pass to clSetPrintfCallback.
	 *
	 * @return the callback function address
	 */
	static native long getPrintfCallback();

	/**
	 * Returns the memory address of the native function we pass to clCreateContext(FromType),
	 * when <code>APPLEContextLoggingUtil.SYSTEM_LOG_CALLBACK</code> is used.
	 *
	 * @return the callback function address
	 *
	 * @see APPLEContextLoggingUtil#SYSTEM_LOG_CALLBACK
	 */
	static native long getLogMessageToSystemLogAPPLE();

	/**
	 * Returns the memory address of the native function we pass to clCreateContext(FromType),
	 * when <code>APPLEContextLoggingUtil.STD_OUT_CALLBACK</code> is used.
	 *
	 * @return the callback function address
	 *
	 * @see APPLEContextLoggingUtil#STD_OUT_CALLBACK
	 */
	static native long getLogMessageToStdoutAPPLE();

	/**
	 * Returns the memory address of the native function we pass to clCreateContext(FromType),
	 * when <code>APPLEContextLoggingUtil.STD_ERR_CALLBACK</code> is used.
	 *
	 * @return the callback function address
	 *
	 * @see APPLEContextLoggingUtil#STD_ERR_CALLBACK
	 */
	static native long getLogMessageToStderrAPPLE();

}