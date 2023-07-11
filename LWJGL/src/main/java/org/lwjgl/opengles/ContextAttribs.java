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

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengles.EGL.*;

/**
 * This class represents the context attributes passed to EGL's eglCreateContext.
 * The only attribute allowed is EGL_CONTEXT_CLIENT_VERSION and it must be 2 or 3 (LWJGL does not support GLES 1.x).
 */
public final class ContextAttribs {

	private int version;

	public ContextAttribs() {
		this(2);
	}

	public ContextAttribs(final int version) {
		if ( 3 < version )
			throw new IllegalArgumentException("Invalid OpenGL ES version specified: " + version);

		this.version = version;
	}

	private ContextAttribs(final ContextAttribs attribs) {
		this.version = attribs.version;
	}

	public int getVersion() {
		return version;
	}

	public IntBuffer getAttribList() {
		int attribCount = 1;

		final IntBuffer attribs = BufferUtils.createIntBuffer((attribCount * 2) + 1);

		attribs.put(EGL_CONTEXT_CLIENT_VERSION).put(version);

		attribs.put(EGL_NONE);
		attribs.rewind();
		return attribs;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(32);

		sb.append("ContextAttribs:");
		sb.append(" Version=").append(version);

		return sb.toString();
	}

}