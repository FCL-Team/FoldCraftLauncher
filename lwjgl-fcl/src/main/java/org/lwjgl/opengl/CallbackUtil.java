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
package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that handles OpenGL API callbacks.
 *
 * @author Spasi
 */
final class CallbackUtil {

	/** Context -> Long */
	private static final Map<ContextCapabilities, Long> contextUserParamsARB = new HashMap<ContextCapabilities, Long>();
	/** Context -> Long */
	private static final Map<ContextCapabilities, Long> contextUserParamsAMD = new HashMap<ContextCapabilities, Long>();
	/** Context -> Long */
	private static final Map<ContextCapabilities, Long> contextUserParamsKHR = new HashMap<ContextCapabilities, Long>();

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
	private static native void deleteGlobalRef(long ref);

	// --------- [ XXX_debug_output ] ---------

	/**
	 * Associates the current OpenGL context with the specified global reference. If there
	 * is no context current, the global reference is deleted and an exception is thrown.
	 * Any previous callback registrations will be cleared.
	 *
	 * @param userParam the global reference pointer
	 */
	private static void registerContextCallback(final long userParam, final Map<ContextCapabilities, Long> contextUserData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		if ( caps == null ) {
			deleteGlobalRef(userParam);
			throw new IllegalStateException("No context is current.");
		}

		final Long userParam_old = contextUserData.remove(caps);
		if ( userParam_old != null )
			deleteGlobalRef(userParam_old);

		if ( userParam != 0 )
			contextUserData.put(caps, userParam);
	}

	/**
	 * Releases references to any callbacks associated with the specified GL context.
	 *
	 * @param context the Context to unregister
	 */
	static void unregisterCallbacks(final Object context) {
		// TODO: This is never called for custom contexts. Need to fix for LWJGL 3.0
		final ContextCapabilities caps = GLContext.getCapabilities(context);

		Long userParam = contextUserParamsARB.remove(caps);
		if ( userParam != null )
			deleteGlobalRef(userParam);

		userParam = contextUserParamsAMD.remove(caps);
		if ( userParam != null )
			deleteGlobalRef(userParam);

		userParam = contextUserParamsKHR.remove(caps);
		if ( userParam != null )
			deleteGlobalRef(userParam);
	}

	// --------- [ ARB_debug_output ] ---------

	/**
	 * Returns the memory address of the native function we pass to glDebugMessageCallbackARB.
	 *
	 * @return the callback function address
	 */
	static native long getDebugOutputCallbackARB();

	/**
	 * Associates the current OpenGL context with the specified global reference. If there
	 * is no context current, the global reference is deleted and an exception is thrown.
	 * Any previous callback registrations will be cleared.
	 *
	 * @param userParam the global reference pointer
	 */
	static void registerContextCallbackARB(final long userParam) {
		registerContextCallback(userParam, contextUserParamsARB);
	}

	// --------- [ AMD_debug_output ] ---------

	/**
	 * Returns the memory address of the native function we pass to glDebugMessageCallbackAMD.
	 *
	 * @return the callback function address
	 */
	static native long getDebugOutputCallbackAMD();

	/**
	 * Associates the current OpenGL context with the specified global reference. If there
	 * is no context current, the global reference is deleted and an exception is thrown.
	 * Any previous callback registrations will be cleared.
	 *
	 * @param userParam the global reference pointer
	 */
	static void registerContextCallbackAMD(final long userParam) {
		registerContextCallback(userParam, contextUserParamsAMD);
	}

	// --------- [ KHR_debug ] ---------

	/**
	 * Returns the memory address of the native function we pass to glDebugMessageCallback.
	 *
	 * @return the callback function address
	 */
	static native long getDebugCallbackKHR();

	/**
	 * Associates the current OpenGL context with the specified global reference. If there
	 * is no context current, the global reference is deleted and an exception is thrown.
	 * Any previous callback registrations will be cleared.
	 *
	 * @param userParam the global reference pointer
	 */
	static void registerContextCallbackKHR(final long userParam) {
		registerContextCallback(userParam, contextUserParamsKHR);
	}

}