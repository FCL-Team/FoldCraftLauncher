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
package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Utility methods for OpenCL
 *
 * @author Spasi
 */
public final class Util {

	/** Maps OpenCL error token values to their String representations. */
	private static final Map<Integer, String> CL_ERROR_TOKENS = LWJGLUtil.getClassTokens(new LWJGLUtil.TokenFilter() {
		public boolean accept(final Field field, final int value) {
			return value < 0; // Currently, all OpenCL errors have negative values.
		}
	}, null, CL10.class, CL11.class, KHRGLSharing.class, KHRICD.class, APPLEGLSharing.class, EXTDeviceFission.class);

	private Util() {
	}

	public static void checkCLError(final int errcode) {
		if ( errcode != CL10.CL_SUCCESS )
			throwCLError(errcode);
	}

	private static void throwCLError(final int errcode) {
		String errname = CL_ERROR_TOKENS.get(errcode);
		if ( errname == null )
			errname = "UNKNOWN";
		throw new OpenCLException("Error Code: " + errname + " (" + LWJGLUtil.toHexString(errcode) + ")");
	}

}