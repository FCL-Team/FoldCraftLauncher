/*
 * Copyright (c) 2002-2008 LWJGL Project
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
package org.lwjgl.openal;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferChecks;
import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

/**
 *
 * <p>
 * ALC introduces the notion of a Device. A Device can be, depending on the
 * implementation, a hardware device, or a daemon/OS service/actual server. This
 * mechanism also permits different drivers (and hardware) to coexist within the same
 * system, as well as allowing several applications to share system resources for audio,
 * including a single hardware output device. The details are left to the
 * implementation, which has to map the available backends to unique device
 * specifiers (represented as strings).
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 2286 $
 * $Id: ALC.java 2286 2006-03-23 19:32:21 +0000 (to, 23 mar 2006) matzon $
 */
public final class ALC10 {

	/** List of active contexts */
	static final HashMap<Long, ALCcontext> contexts = new HashMap<Long, ALCcontext>();

	/** List of active devices */
	static final HashMap<Long, ALCdevice> devices = new HashMap<Long, ALCdevice>();

	/** Bad value */
	public static final int ALC_INVALID = 0;

	/** Boolean False */
	public static final int ALC_FALSE = 0;

	/** Boolean True */
	public static final int ALC_TRUE = 1;

	/** Errors: No Error */
	public static final int ALC_NO_ERROR = ALC_FALSE;

	/** Major version query. */
	public static final int ALC_MAJOR_VERSION = 0x1000;

	/** Minor version query. */
	public static final int ALC_MINOR_VERSION = 0x1001;

	/**
	 * The size required for the zero-terminated attributes list, for the current context.
	 **/
	public static final int ALC_ATTRIBUTES_SIZE = 0x1002;

	/**
	 * Expects a destination of ALC_CURRENT_ATTRIBUTES_SIZE,
	 * and provides the attribute list for the current context of the specified device.
	 */
	public static final int ALC_ALL_ATTRIBUTES = 0x1003;

	/** The specifier string for the default device */
	public static final int ALC_DEFAULT_DEVICE_SPECIFIER = 0x1004;

	/** The specifier string for the device */
	public static final int ALC_DEVICE_SPECIFIER = 0x1005;

	/** The extensions string for diagnostics and printing */
	public static final int ALC_EXTENSIONS = 0x1006;

	/** Frequency for mixing output buffer, in units of Hz. */
	public static final int ALC_FREQUENCY = 0x1007;

	/** Refresh intervalls, in units of Hz. */
	public static final int ALC_REFRESH = 0x1008;

	/** Flag, indicating a synchronous context. */
	public static final int ALC_SYNC = 0x1009;

	/** The device argument does not name a valid device */
	public static final int ALC_INVALID_DEVICE = 0xA001;

	/** The context argument does not name a valid context */
	public static final int ALC_INVALID_CONTEXT = 0xA002;

	/**
	 * A function was called at inappropriate time, or in an inappropriate way,
	 * causing an illegal state. This can be an incompatible ALenum, object ID,
	 * and/or function.
	 */
	public static final int ALC_INVALID_ENUM = 0xA003;

	/**
	 * Illegal value passed as an argument to an AL call.
	 * Applies to parameter values, but not to enumerations.
	 */
	public static final int ALC_INVALID_VALUE = 0xA004;

	/**
	 * A function could not be completed, because there is not enough
	 * memory available.
	 */
	public static final int ALC_OUT_OF_MEMORY = 0xA005;

	static native void initNativeStubs() throws LWJGLException;

	/**
	 * The application can obtain certain strings from ALC.
	 *
	 * <code>ALC_DEFAULT_DEVICE_SPECIFIER</code> - The specifer string for the default device
	 * <code>ALC_DEVICE_SPECIFIER</code> - The specifer string for the device
	 * <code>ALC_EXTENSIONS</code> - The extensions string for diagnostics and printing.
	 *
	 * In addition, printable error message strings are provided for all valid error tokens,
	 * including <code>ALC_NO_ERROR</code>,<code>ALC_INVALID_DEVICE</code>, <code>ALC_INVALID_CONTEXT</code>,
	 * <code>ALC_INVALID_ENUM</code>, <code>ALC_INVALID_VALUE</code>.
	 *
	 * @param pname Property to get
	 * @return String property from device
	 */
	public static String alcGetString(ALCdevice device, int pname) {
		ByteBuffer buffer = nalcGetString(getDevice(device), pname);
		Util.checkALCError(device);
		return MemoryUtil.decodeUTF8(buffer);
	}
	static native ByteBuffer nalcGetString(long device, int pname);

	/**
	 * The application can query ALC for information using an integer query function.
	* For some tokens, <code>null</code> is a legal deviceHandle. In other cases, specifying a <code>null</code>
	* device will generate an <code>ALC_INVALID_DEVICE</code> error. The application has to
	* specify the size of the destination buffer provided. A <code>null</code> destination or a zero
	* size parameter will cause ALC to ignore the query.
	*
	* <code>ALC_MAJOR_VERSION</code> - Major version query.
	* <code>ALC_MINOR_VERSION</code> - Minor version query.
	* <code>ALC_ATTRIBUTES_SIZE</code> - The size required for the zero-terminated attributes list,
	* for the current context. <code>null</code> is an invalid device. <code>null</code> (no current context
	* for the specified device) is legal.
	* <code>ALC_ALL_ATTRIBUTES</code> - Expects a destination of <code>ALC_CURRENT_ATTRIBUTES_SIZE</code>,
	* and provides the attribute list for the current context of the specified device.
	* <code>null</code> is an invalid device. <code>null</code> (no current context for the specified device)
	* will return the default attributes defined by the specified device.
	 *
	 * @param pname Property to get
	 * @param integerdata ByteBuffer to write integers to
	 */
	public static void alcGetInteger(ALCdevice device, int pname, IntBuffer integerdata) {
		BufferChecks.checkDirect(integerdata);
		nalcGetIntegerv(getDevice(device), pname, integerdata.remaining(), MemoryUtil.getAddress(integerdata));
		Util.checkALCError(device);
	}
	static native void nalcGetIntegerv(long device, int pname, int size, long integerdata);

	/**
	 * The <code>alcOpenDevice</code> function allows the application (i.e. the client program) to
	* connect to a device (i.e. the server).
	*
	* If the function returns <code>null</code>, then no sound driver/device has been found. The
	* argument is a null terminated string that requests a certain device or device
	* configuration. If <code>null</code> is specified, the implementation will provide an
	* implementation specific default.
	 *
	 * @param devicename name of device to open
	 * @return opened device, or null
	 */
	public static ALCdevice alcOpenDevice(String devicename) {
		ByteBuffer buffer = MemoryUtil.encodeUTF8(devicename);
		long device_address = nalcOpenDevice(MemoryUtil.getAddressSafe(buffer));
		if(device_address != 0) {
			ALCdevice device = new ALCdevice(device_address);
			synchronized (ALC10.devices) {
				devices.put(device_address, device);
			}
			return device;
		}
		return null;
	}
	static native long nalcOpenDevice(long devicename);

	/**
	 * The <code>alcCloseDevice</code> function allows the application (i.e. the client program) to
	* disconnect from a device (i.e. the server).
	*
	* If deviceHandle is <code>null</code> or invalid, an <code>ALC_INVALID_DEVICE</code> error will be
	* generated. Once closed, a deviceHandle is invalid.
	 *
	 * @param device address of native device to close
	 */
	public static boolean alcCloseDevice(ALCdevice device) {
		boolean result = nalcCloseDevice(getDevice(device));
		synchronized (devices) {
			device.setInvalid();
			devices.remove(new Long(device.device));
		}
		return result;

	}
	static native boolean nalcCloseDevice(long device);

	/**
	 * A context is created using <code>alcCreateContext</code>. The device parameter has to be a valid
	* device. The attribute list can be <code>null</code>, or a zero terminated list of integer pairs
	* composed of valid ALC attribute tokens and requested values.
	*
	* Context creation will fail if the application requests attributes that, by themselves,
	* can not be provided. Context creation will fail if the combination of specified
	* attributes can not be provided. Context creation will fail if a specified attribute, or
	* the combination of attributes, does not match the default values for unspecified
	* attributes.
	 *
	 * @param device address of device to associate context to
	 * @param attrList Buffer to read attributes from
	 * @return New context, or null if creation failed
	 */
	public static ALCcontext alcCreateContext(ALCdevice device, IntBuffer attrList) {
		long context_address = nalcCreateContext(getDevice(device), MemoryUtil.getAddressSafe(attrList));
		Util.checkALCError(device);

		if(context_address != 0) {
			ALCcontext context = new ALCcontext(context_address);
			synchronized (ALC10.contexts) {
				contexts.put(context_address, context);
				device.addContext(context);
			}
			return context;
		}
		return null;
	}
	static native long nalcCreateContext(long device, long attrList);

	/**
	 * To make a Context current with respect to AL Operation (state changes by issueing
	* commands), <code>alcMakeContextCurrent</code> is used. The context parameter can be <code>null</code>
	* or a valid context pointer. The operation will apply to the device that the context
	* was created for.
	*
	* For each OS process (usually this means for each application), only one context can
	* be current at any given time. All AL commands apply to the current context.
	* Commands that affect objects shared among contexts (e.g. buffers) have side effects
	* on other contexts.
	 *
	 * @param context address of context to make current
	 * @return true if successfull, false if not
	 */
	public static int alcMakeContextCurrent(ALCcontext context) {
		return nalcMakeContextCurrent(getContext(context));
	}
	static native int nalcMakeContextCurrent(long context);

	/**
	 * The current context is the only context accessible to state changes by AL commands
	 * (aside from state changes affecting shared objects). However, multiple contexts can
	 * be processed at the same time. To indicate that a context should be processed (i.e.
	 * that internal execution state like offset increments are supposed to be performed),
	 * the application has to use <code>alcProcessContext</code>.
	 *
	 * Repeated calls to <code>alcProcessContext</code> are legal, and do not affect a context that is
	 * already marked as processing. The default state of a context created by
	 * alcCreateContext is that it is not marked as processing.
	 */
	public static void alcProcessContext(ALCcontext context) {
		nalcProcessContext(getContext(context));
	}
	static native void nalcProcessContext(long context);

	/**
	 * The application can query for, and obtain an handle to, the current context for the
	* application. If there is no current context, <code>null</code> is returned.
	 *
	 * @return Current ALCcontext
	 */
	public static ALCcontext alcGetCurrentContext() {
		ALCcontext context = null;
		long context_address = nalcGetCurrentContext();
		if(context_address != 0) {
			synchronized (ALC10.contexts) {
				context = ALC10.contexts.get(context_address);
			}
		}
		return context;
	}
	static native long nalcGetCurrentContext();

	/**
	 * The application can query for, and obtain an handle to, the device of a given context.
	 *
	 * @param context address of context to get device for
	 */
	public static ALCdevice alcGetContextsDevice(ALCcontext context) {
		ALCdevice device = null;
		long device_address = nalcGetContextsDevice(getContext(context));
		if (device_address != 0) {
			synchronized (ALC10.devices) {
				device = ALC10.devices.get(device_address);
			}
		}
		return device;
	}
	static native long nalcGetContextsDevice(long context);

	/**
	 * The application can suspend any context from processing (including the current
	 * one). To indicate that a context should be suspended from processing (i.e. that
	 * internal execution state like offset increments is not supposed to be changed), the
	 * application has to use <code>alcSuspendContext</code>.
	 *
	 * Repeated calls to <code>alcSuspendContext</code> are legal, and do not affect a context that is
	 * already marked as suspended. The default state of a context created by
	 * <code>alcCreateContext</code> is that it is marked as suspended.
	 *
	 * @param context address of context to suspend
	 */
	public static void alcSuspendContext(ALCcontext context) {
		nalcSuspendContext(getContext(context));
	}
	static native void nalcSuspendContext(long context);

	/**
	 * The correct way to destroy a context is to first release it using <code>alcMakeCurrent</code> and
	* <code>null</code>. Applications should not attempt to destroy a current context.
	 *
	 * @param context address of context to Destroy
	 */
	public static void alcDestroyContext(ALCcontext context) {
		synchronized(ALC10.contexts) {
			ALCdevice device = alcGetContextsDevice(context);
			nalcDestroyContext(getContext(context));
			device.removeContext(context);
			context.setInvalid();
		}
	}
	static native void nalcDestroyContext(long context);

	/**
	 * ALC uses the same conventions and mechanisms as AL for error handling. In
	* particular, ALC does not use conventions derived from X11 (GLX) or Windows
	* (WGL). The <code>alcGetError</code> function can be used to query ALC errors.
	*
	* Error conditions are specific to the device.
	*
	* ALC_NO_ERROR - The device handle or specifier does name an accessible driver/server.
	* <code>ALC_INVALID_DEVICE</code> - The Context argument does not name a valid context.
	* <code>ALC_INVALID_CONTEXT</code> - The Context argument does not name a valid context.
	* <code>ALC_INVALID_ENUM</code> - A token used is not valid, or not applicable.
	* <code>ALC_INVALID_VALUE</code> - An value (e.g. attribute) is not valid, or not applicable.
	 *
	 * @return Errorcode from ALC statemachine
	 */
	public static int alcGetError(ALCdevice device) {
		return nalcGetError(getDevice(device));
	}
	static native int nalcGetError(long device);

	/**
	* Verify that a given extension is available for the current context and the device it
	* is associated with.
	* A <code>null</code> name argument returns <code>ALC_FALSE</code>, as do invalid and unsupported string
	* tokens.
	 *
	 * @param extName name of extension to find
	 * @return true if extension is available, false if not
	 */
	public static boolean alcIsExtensionPresent(ALCdevice device, String extName) {
		ByteBuffer buffer = MemoryUtil.encodeASCII(extName);
		boolean result = nalcIsExtensionPresent(getDevice(device), MemoryUtil.getAddress(buffer));
		Util.checkALCError(device);
		return result;
	}
	private static native boolean nalcIsExtensionPresent(long device, long extName);

	/**
	 * Enumeration/token values are device independend, but tokens defined for
	* extensions might not be present for a given device. But only the tokens defined
	* by the AL core are guaranteed. Availability of extension tokens dependends on the ALC extension.
	*
	* Specifying a <code>null</code> name parameter will cause an <code>ALC_INVALID_VALUE</code> error.
	 *
	 * @param enumName name of enum to find
	 * @return value of enumeration
	 */
	public static int alcGetEnumValue(ALCdevice device, String enumName) {
		ByteBuffer buffer = MemoryUtil.encodeASCII(enumName);
		int result = nalcGetEnumValue(getDevice(device), MemoryUtil.getAddress(buffer));
		Util.checkALCError(device);
		return result;
	}
	private static native int nalcGetEnumValue(long device, long enumName);

	static long getDevice(ALCdevice device) {
		if(device != null) {
			Util.checkALCValidDevice(device);
			return device.device;
		}
		return 0L;
	}

	static long getContext(ALCcontext context) {
		if(context != null) {
			Util.checkALCValidContext(context);
			return context.context;
		}
		return 0L;
	}

}
