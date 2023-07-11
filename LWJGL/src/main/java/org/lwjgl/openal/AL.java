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

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

/**
 * <p>
 * The AL class implements the actual creation code for linking to the native library
 * OpenAL.
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 */
public final class AL {
	/** ALCdevice instance. */
	static ALCdevice device;

	/** Current ALCcontext. */
	static ALCcontext context;

	/** Have we been created? */
	private static boolean created;

	static {
		Sys.initialize();
	}

	private AL() {
	}

	/**
	 * Native method to create AL instance
	 *
	 * @param oalPath Path to search for OpenAL library
	 */
	private static native void nCreate(String oalPath) throws LWJGLException;

	/**
	 * Native method to create AL instance from the Mac OS X 10.4 OpenAL framework.
	 * It is only defined in the Mac OS X native library.
	 */
	private static native void nCreateDefault() throws LWJGLException;

	/**
	 * Native method the destroy the AL
	 */
	private static native void nDestroy();

	/**
	 * @return true if AL has been created
	 */
	public static boolean isCreated() {
		return created;
	}

	/**
	 * Creates an OpenAL instance. Using this constructor will cause OpenAL to
	 * open the device using supplied device argument, and create a context using the context values
	 * supplied.
	 *
	 * @param deviceArguments Arguments supplied to native device
	 * @param contextFrequency Frequency for mixing output buffer, in units of Hz (Common values include 11025, 22050, and 44100).
	 * @param contextRefresh Refresh intervalls, in units of Hz.
	 * @param contextSynchronized Flag, indicating a synchronous context.*
	 */
	public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized)
		throws LWJGLException {
		create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
	}

	/**
	 * @param openDevice Whether to automatically open the device
	 * @see #create(String, int, int, boolean)
	 */
	public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice)
		throws LWJGLException {

		if (created)
			throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
		String libname;
		String[] library_names;
		switch (LWJGLUtil.getPlatform()) {
			case LWJGLUtil.PLATFORM_WINDOWS:
				if ( Sys.is64Bit() ) {
					libname = "OpenAL64";
					library_names = new String[]{"OpenAL64.dll"};
				} else {
					libname = "OpenAL32";
					library_names = new String[]{"OpenAL32.dll"};
				}
				break;
			case LWJGLUtil.PLATFORM_LINUX:
				libname = "openal";
				library_names = new String[]{"libopenal64.so", "libopenal.so", "libopenal.so.0"};
				break;
			case LWJGLUtil.PLATFORM_FCL:
				libname = "openal";
				library_names = new String[]{"libopenal.so"};
				break;
			case LWJGLUtil.PLATFORM_MACOSX:
				libname = "openal";
				library_names = new String[]{"openal.dylib"};
				break;
			default:
				throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
		}
		String[] oalPaths = LWJGLUtil.getLibraryPaths(libname, library_names, AL.class.getClassLoader());
		LWJGLUtil.log("Found " + oalPaths.length + " OpenAL paths");
		for ( String oalPath : oalPaths ) {
			try {
				nCreate(oalPath);
				created = true;
				init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
				break;
			} catch (LWJGLException e) {
				LWJGLUtil.log("Failed to load " + oalPath + ": " + e.getMessage());
			}
		}
		if (!created && LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX) {
			// Try to load OpenAL from the framework instead
			nCreateDefault();
			created = true;
			init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
		}
		if (!created)
			throw new LWJGLException("Could not locate OpenAL library.");
	}

	private static void init(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
		try {
			AL10.initNativeStubs();
			ALC10.initNativeStubs();

			if(openDevice) {
				device = ALC10.alcOpenDevice(deviceArguments);
				if (device == null) {
					throw new LWJGLException("Could not open ALC device");
				}

				if (contextFrequency == -1) {
					context = ALC10.alcCreateContext(device, null);
				} else {
					context = ALC10.alcCreateContext(device,
							ALCcontext.createAttributeList(contextFrequency, contextRefresh,
								contextSynchronized ? ALC10.ALC_TRUE : ALC10.ALC_FALSE));
				}
				ALC10.alcMakeContextCurrent(context);
			}
		} catch (LWJGLException e) {
			destroy();
			throw e;
		}

		ALC11.initialize();

		// Load EFX10 native stubs if ALC_EXT_EFX is supported.
		// Is there any situation where the current device supports ALC_EXT_EFX and one
		// later created by the user does not?
		// Do we have to call resetNativeStubs(EFX10.class); somewhere? Not done for AL11
		// either.
		// This can either be here or in ALC11, since ALC_EXT_EFX indirectly requires AL 1.1
		// for functions like alSource3i.
		if (ALC10.alcIsExtensionPresent(device, EFX10.ALC_EXT_EFX_NAME)){
		    EFX10.initNativeStubs();
		}
	}

	/**
	 * Creates an OpenAL instance. The empty create will cause OpenAL to
	 * open the default device, and create a context using default values.
	 * This method used to use default values that the OpenAL implementation
	 * chose but this produces unexpected results on some systems; so now
	 * it defaults to 44100Hz mixing @ 60Hz refresh.
	 */
	public static void create() throws LWJGLException {
		create(null, 44100, 60, false);
	}

	/**
	 * Exit cleanly by calling destroy.
	 */
	public static void destroy() {
		if (context != null) {
			ALC10.alcMakeContextCurrent(null);
			ALC10.alcDestroyContext(context);
			context = null;
		}
		if (device != null) {
			boolean result = ALC10.alcCloseDevice(device);
			device = null;
		}
		resetNativeStubs(AL10.class);
		resetNativeStubs(AL11.class);
		resetNativeStubs(ALC10.class);
		resetNativeStubs(ALC11.class);
		resetNativeStubs(EFX10.class);

		if (created)
			nDestroy();
		created = false;
	}

	private static native void resetNativeStubs(Class clazz);

	/**
	 * @return handle to the default AL context.
	 */
	public static ALCcontext getContext() {
		return context;
	}

	/**
	 * @return handle to the default AL device.
	 */
	public static ALCdevice getDevice() {
		return device;
	}
}
