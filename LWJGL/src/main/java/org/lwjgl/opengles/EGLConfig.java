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
import org.lwjgl.PointerWrapperAbstract;

import static org.lwjgl.opengles.EGL.*;

/** EGLConfig wrapper class. */
public final class EGLConfig extends PointerWrapperAbstract {

	private final EGLDisplay display;

	private final int configID;

	EGLConfig(final EGLDisplay display, final long pointer) throws LWJGLException {
		super(pointer);

		this.display = display;
		this.configID = getAttribute(EGL_CONFIG_ID);
	}

	/**
	 * Returns the EGL display from which this EGL config was retrieved.
	 *
	 * @return the EGL display
	 */
	public EGLDisplay getDisplay() {
			return display;
	}

	/**
	 * Returns the EGL_CONFIG_ID attribute of this EGLConfig.
	 *
	 * @return the EGL_CONFIG_ID
	 */
	public int getConfigID() {
			return configID;
	}

	/**
	 * Returns the value of the specified EGL config attribute.
	 *
	 * @param attribute the attribute
	 *
	 * @return the attribute value
	 */
	public int getAttribute(final int attribute) throws LWJGLException {
			return eglGetConfigAttrib(display, this, attribute);
	}

	public boolean equals(final Object obj) {
		if ( obj == null || !(obj instanceof EGLConfig) )
			return false;

		return getPointer() == ((EGLConfig)obj).getPointer();
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder(512);

		sb.append("EGLConfig (").append(configID).append(")");
		sb.append("\n------------");

		try {
			sb.append("\nEGL_LEVEL").append(": ").append(getAttribute(EGL_LEVEL));
			sb.append("\nEGL_RENDERABLE_TYPE").append(": ").append(Integer.toBinaryString(getAttribute(EGL_RENDERABLE_TYPE)));
			sb.append("\nEGL_NATIVE_RENDERABLE").append(": ").append(getAttribute(EGL_NATIVE_RENDERABLE) == EGL_TRUE);
			sb.append("\nEGL_SURFACE_TYPE").append(": ").append(Integer.toBinaryString(getAttribute(EGL_SURFACE_TYPE)));
		} catch (LWJGLException e) {
		}

		final PixelFormat.Attrib[] attribEnums = PixelFormat.Attrib.values();
		for ( PixelFormat.Attrib attribEnum : attribEnums ) {
			if ( attribEnum.isSurfaceAttrib() )
				continue;

			try {
				final int attrib = getAttribute(attribEnum.getEGLAttrib());
				sb.append("\nEGL_").append(attribEnum.name()).append(": ").append(attrib);
			} catch (LWJGLException e) {
				//System.out.println("Failed to retrieve: " + attribEnum.name());
				// Ignore, can happen when querying unsupported attributes (e.g. extension ones)
			}
		}

		return sb.toString();
	}

}