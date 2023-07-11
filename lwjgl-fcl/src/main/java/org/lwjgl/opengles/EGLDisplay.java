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

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import static org.lwjgl.opengles.EGL.*;

/** EGLDisplay wrapper class. */
public final class EGLDisplay extends PointerWrapperAbstract {

	private int majorVersion;
	private int minorVersion;

	private Set<String> extensions;

	private boolean initialized;

	EGLDisplay(final long pointer) throws LWJGLException {
		super(pointer);

		initialize();

		/*final EGLConfig[] configs = eglGetConfigs(this, null, APIUtil.getBufferInt());
		for ( EGLConfig config : configs ) {
			System.out.println(config);
			System.out.println("");
		}*/
	}

	/**
	 * Returns the major EGL version of this EGL display.
	 *
	 * @return the major EGL version
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * Returns the minor EGL version of this EGL display.
	 *
	 * @return the minor EGL version
	 */
	public int getMinorVersion() {
		return minorVersion;
	}

	/**
	 * Returns true if the specified EGL extension is supported by this EGL display.
	 *
	 * @param eglExtension the EGL extension
	 *
	 * @return true if the extension is supported
	 */
	public boolean isExtensionSupported(final String eglExtension) {
		checkInitialized();
		if ( extensions == null ) {
			extensions = new HashSet<String>(16);

			final StringTokenizer tokenizer = new StringTokenizer(eglQueryString(this, EGL_EXTENSIONS));
			while ( tokenizer.hasMoreTokens() )
				extensions.add(tokenizer.nextToken());
		}

		return extensions.contains(eglExtension);
	}

	boolean isInitialized() {
		return initialized;
	}

	private void initialize() throws LWJGLException {
		IntBuffer version = APIUtil.getBufferInt();
		eglInitialize(this, version);

		majorVersion = version.get(0);
		minorVersion = version.get(1);

		initialized = true;
	}

	private void checkInitialized() {
		if ( !initialized )
			throw new IllegalStateException("The EGL display needs to be initialized first.");
	}

	/** Release the resources associated with this EGL display. */
	public void terminate() throws LWJGLException {
		eglTerminate(this);

		majorVersion = 0;
		minorVersion = 0;

		initialized = false;
	}

	/**
	 * Returns a string describing some aspect of the EGL implementation running on the specified display.
	 *
	 * @param name the value to query
	 *
	 * @return the description
	 */
	public String query(int name) {
		checkInitialized();
		return eglQueryString(this, name);
	}

	int getConfigsNum() throws LWJGLException {
		checkInitialized();
		return eglGetConfigsNum(this);
	}

	EGLConfig[] getConfigs(EGLConfig[] configs, IntBuffer num_config) throws LWJGLException {
		checkInitialized();
		return eglGetConfigs(this, configs, num_config);
	}

	int getConfigNum(IntBuffer attrib_list) throws LWJGLException {
		checkInitialized();
		return eglChooseConfigNum(this, attrib_list);
	}

	/** Returns the available EGL configs on this display that satisfy the specified list of attributes. */
	public EGLConfig[] chooseConfig(IntBuffer attrib_list, EGLConfig[] configs, IntBuffer num_config) throws LWJGLException {
		checkInitialized();
		return eglChooseConfig(this, attrib_list, configs, num_config);
	}

	/**
	 * Creates an on-screen rendering surface on this EGL display.
	 *
	 * @param config      the EGL config
	 * @param window      the native window handle
	 * @param attrib_list an attribute list (may be null)
	 *
	 * @return the EGL surface
	 */
	public EGLSurface createWindowSurface(EGLConfig config, long window, IntBuffer attrib_list) throws LWJGLException {
		checkInitialized();

		if ( config.getDisplay() != this )
			throw new IllegalArgumentException("Invalid EGL config specified.");

		return eglCreateWindowSurface(this, config, window, attrib_list);
	}

	EGLSurface createPbufferSurface(EGLConfig config, IntBuffer attrib_list) throws LWJGLException {
		checkInitialized();

		if ( config.getDisplay() != this )
			throw new IllegalArgumentException("Invalid EGL config specified.");

		return eglCreatePbufferSurface(this, config, attrib_list);
	}

	public EGLContext createContext(EGLConfig config, EGLContext shareContext, IntBuffer attrib_list) throws LWJGLException {
		checkInitialized();

		if ( config.getDisplay() != this )
			throw new IllegalStateException("Invalid EGL config specified.");

		if ( shareContext != null && shareContext.getDisplay() != this )
			throw new IllegalStateException("Invalid shared EGL context specified.");

		return eglCreateContext(this, config, shareContext, attrib_list);
	}

	public void setSwapInterval(final int interval) throws LWJGLException {
		eglSwapInterval(this, interval);
	}

	public boolean equals(final Object obj) {
		if ( obj == null || !(obj instanceof EGLDisplay) )
			return false;

		return getPointer() == ((EGLDisplay)obj).getPointer();
	}

}