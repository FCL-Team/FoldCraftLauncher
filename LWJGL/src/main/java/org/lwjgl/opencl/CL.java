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

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.Sys;

import java.nio.ByteBuffer;

/**
 * LWJGL users must use this class to initialize OpenCL
 * before using any other class in the org.lwjgl.opencl package.
 *
 * @author Spasi
 */
public final class CL {

	private static boolean created;

	static {
		Sys.initialize();
	}

	private CL() {
	}

	/**
	 * Native method to create CL instance
	 *
	 * @param oclPaths Array of strings containing paths to search for OpenCL library
	 */
	private static native void nCreate(String oclPaths) throws LWJGLException;

	/**
	 * Native method to create CL instance from the Mac OS X 10.4 OpenCL framework.
	 * It is only defined in the Mac OS X native library.
	 */
	private static native void nCreateDefault() throws LWJGLException;

	/** Native method the destroy the CL */
	private static native void nDestroy();

	/** @return true if CL has been created */
	public static boolean isCreated() {
		return created;
	}

	public static void create() throws LWJGLException {
		if ( created )
			return;
		//throw new IllegalStateException("OpenCL has already been created.");

		final String libname;
		final String[] library_names;
		switch ( LWJGLUtil.getPlatform() ) {
			case LWJGLUtil.PLATFORM_WINDOWS:
				libname = "OpenCL";
				library_names = new String[] { "OpenCL.dll" };
				break;
			case LWJGLUtil.PLATFORM_LINUX:
				libname = "OpenCL";
				library_names = new String[] { "libOpenCL64.so", "libOpenCL.so" }; // TODO: Fix this
				break;
			case LWJGLUtil.PLATFORM_MACOSX:
				libname = "OpenCL";
				library_names = new String[] { "OpenCL.dylib" }; // TODO: Fix this
				break;
			default:
				throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
		}

		final String[] oclPaths = LWJGLUtil.getLibraryPaths(libname, library_names, CL.class.getClassLoader());
		LWJGLUtil.log("Found " + oclPaths.length + " OpenCL paths");
		for ( String oclPath : oclPaths ) {
			try {
				nCreate(oclPath);
				created = true;
				break;
			} catch (LWJGLException e) {
				LWJGLUtil.log("Failed to load " + oclPath + ": " + e.getMessage());
			}
		}

		if ( !created && LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX ) {
			// Try to load OpenCL from the framework instead
			nCreateDefault();
			created = true;
		}

		if ( !created )
			throw new LWJGLException("Could not locate OpenCL library.");

		if ( !CLCapabilities.OpenCL10 )
			throw new RuntimeException("OpenCL 1.0 not supported.");
	}

	public static void destroy() {
	}

	/**
	 * Helper method to get a pointer to a named function with aliases in the OpenCL library.
	 *
	 * @param aliases the function name aliases.
	 *
	 * @return the function pointer address
	 */
	static long getFunctionAddress(String[] aliases) {
		for ( String aliase : aliases ) {
			long address = getFunctionAddress(aliase);
			if ( address != 0 )
				return address;
		}
		return 0;
	}

	/** Helper method to get a pointer to a named function in the OpenCL library. */
	static long getFunctionAddress(String name) {
		ByteBuffer buffer = MemoryUtil.encodeASCII(name);
		return ngetFunctionAddress(MemoryUtil.getAddress(buffer));
	}
	private static native long ngetFunctionAddress(long name);

	static native ByteBuffer getHostBuffer(final long address, final int size);

	private static native void resetNativeStubs(Class clazz);

}