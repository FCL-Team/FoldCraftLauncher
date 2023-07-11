/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.opengles;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.OpenGLException;

import static org.lwjgl.opengles.EGL.*;
import static org.lwjgl.opengles.GLES20.*;

/**
 * Simple utility class.
 *
 * @author Spasi
 */
public final class Util {

	private Util() {
	}

	/**
	 * Checks for OpenGL ES errors.
	 *
	 * @throws OpenGLException
	 *          if GLES20.glGetError() returns anything else than GLES20.GL_NO_ERROR
	 */
	public static void checkGLError() throws OpenGLException {
		int err = glGetError();
		if ( err != GL_NO_ERROR )
			throw new OpenGLException(err);
	}

	/**
	 * Translates a GL error code to a String describing the error.
	 *
	 * @param error_code the OpenGL ES error code
	 *
	 * @return the error description
	 */
	public static String translateGLErrorString(int error_code) {
		switch ( error_code ) {
			case GL_NO_ERROR:
				return "No error";
			case GL_INVALID_ENUM:
				return "Invalid enum";
			case GL_INVALID_VALUE:
				return "Invalid value";
			case GL_INVALID_OPERATION:
				return "Invalid operation";
			case GL_OUT_OF_MEMORY:
				return "Out of memory";
			default:
				return null;
		}
	}

	/**
	 * Checks for EGL errors.
	 *
	 * @throws LWJGLException if EGL.eglGetError() returns anything else than EGL.EGL_SUCCESS
	 */
	static void checkEGLError() throws LWJGLException {
		int err = eglGetError();
		if ( err != EGL_SUCCESS )
			throw new LWJGLException(translateEGLErrorString(err));
	}

	/**
	 * Translates an EGL error code to a String describing the error.
	 *
	 * @param error_code the EGL error code
	 *
	 * @return the error description
	 */
	static String translateEGLErrorString(int error_code) {
		switch ( error_code ) {
			case EGL_NOT_INITIALIZED:
				return "EGL not initialized";
			case EGL_BAD_ACCESS:
				return "Bad access";
			case EGL_BAD_ALLOC:
				return "Bad allocation";
			case EGL_BAD_ATTRIBUTE:
				return "Bad attribute";
			case EGL_BAD_CONFIG:
				return "Bad config";
			case EGL_BAD_CONTEXT:
				return "Bad EGL context";
			case EGL_BAD_CURRENT_SURFACE:
				return "Bad current EGL surface";
			case EGL_BAD_DISPLAY:
				return "Bad EGL display";
			case EGL_BAD_MATCH:
				return "Bad match";
			case EGL_BAD_NATIVE_PIXMAP:
				return "Bad native pixmap";
			case EGL_BAD_NATIVE_WINDOW:
				return "Bad native window";
			case EGL_BAD_PARAMETER:
				return "Bad parameter";
			case EGL_BAD_SURFACE:
				return "Bad EGL surface";
			case EGL_CONTEXT_LOST:
				return "EGL context lost";
			default:
				return null;
		}
	}

}