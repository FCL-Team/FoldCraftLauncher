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
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import static org.lwjgl.opengles.GLES20.*;

/**
 * <p/>
 * Manages GL contexts. Before any rendering is done by a LWJGL system, a call should be made to GLContext.useContext() with a
 * context. This will ensure that GLContext has an accurate reflection of the current context's capabilities and function
 * pointers.
 * <p/>
 * This class is thread-safe in the sense that multiple threads can safely call all public methods. The class is also
 * thread-aware in the sense that it tracks a per-thread current context (including capabilities and function pointers).
 * That way, multiple threads can have multiple contexts current and render to them concurrently.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3279 $
 *          $Id: GLContext.java 3279 2010-03-11 21:06:49Z spasi $
 */
public final class GLContext {

	/** Maps threads to their current context's ContextCapabilities, if any */
	private static final ThreadLocal<ContextCapabilities> current_capabilities = new ThreadLocal<ContextCapabilities>();

	/**
	 * The getCapabilities() method is a potential hot spot in any LWJGL application, since
	 * it is needed for context capability discovery (e.g. is OpenGL 2.0 supported?), and
	 * for the function pointers of gl functions. However, the 'current_capabilities' ThreadLocal
	 * is (relatively) expensive to look up, and since most OpenGL applications use are single threaded
	 * rendering, the following two is an optimization for this case.
	 * <p/>
	 * ThreadLocals can be thought of as a mapping between threads and values, so the idea
	 * is to use a lock-less cache of mappings between threads and the current ContextCapabilities. The cache
	 * could be any size, but in our case, we want a single sized cache for optimal performance
	 * in the single threaded case.
	 * <p/>
	 * 'fast_path_cache' is the most recent ContextCapabilities (potentially null) and its owner. By
	 * recent I mean the last thread setting the value in setCapabilities(). When getCapabilities()
	 * is called, a check to see if the current is the owner of the ContextCapabilities instance in
	 * fast_path_cache. If so, the instance is returned, if not, some thread has since taken ownership
	 * of the cache entry and the slower current_capabilities ThreadLocal is queried instead.
	 * <p/>
	 * No locks are needed in get/setCapabilities, because even though fast_path_cache can be accessed
	 * from multiple threads at once, we are guaranteed by the JVM spec that its value is always valid.
	 * Furthermore, if the ownership test in getCapabilities() succeeds, the cache entry can only contain
	 * the correct ContextCapabilites (that is, the one from getThreadLocalCapabilites()),
	 * since no other thread can set the owner to anyone else than itself.
	 */
	private static CapabilitiesCacheEntry fast_path_cache = new CapabilitiesCacheEntry();

	/**
	 * Simple lock-free cache of CapabilitesEntryCache to avoid allocating more than one
	 * cache entry per thread
	 */
	private static final ThreadLocal<CapabilitiesCacheEntry> thread_cache_entries = new ThreadLocal<CapabilitiesCacheEntry>();

	/**
	 * The weak mapping from context Object instances to ContextCapabilities. Used
	 * to avoid recreating a ContextCapabilities every time a context is made current.
	 */
	private static final Map<Object, ContextCapabilities> capability_cache = new WeakHashMap<Object, ContextCapabilities>();

	/** Reference count of the native opengl implementation library */
	private static int     gl_ref_count;
	private static boolean did_auto_load;

	static {
		Sys.initialize();
	}

	/**
	 * Get the current capabilities instance. It contains the flags used
	 * to test for support of a particular extension.
	 *
	 * @return The current capabilities instance.
	 */
	public static ContextCapabilities getCapabilities() {
		CapabilitiesCacheEntry recent_cache_entry = fast_path_cache;
		// Check owner of cache entry
		if ( recent_cache_entry.owner == Thread.currentThread() ) {
			/* The owner ship test succeeded, so the cache must contain the current ContextCapabilities instance
			 * assert recent_cache_entry.capabilities == getThreadLocalCapabilities();
			 */
			return recent_cache_entry.capabilities;
		} else // Some other thread has written to the cache since, and we fall back to the slower path
			return getThreadLocalCapabilities();
	}

	private static ContextCapabilities getThreadLocalCapabilities() {
		return current_capabilities.get();
	}

	/**
	 * Set the current capabilities instance. It contains the flags used
	 * to test for support of a particular extension.
	 *
	 * @return The current capabilities instance.
	 */
	static void setCapabilities(ContextCapabilities capabilities) {
		current_capabilities.set(capabilities);

		CapabilitiesCacheEntry thread_cache_entry = thread_cache_entries.get();
		if ( thread_cache_entry == null ) {
			thread_cache_entry = new CapabilitiesCacheEntry();
			thread_cache_entries.set(thread_cache_entry);
		}
		thread_cache_entry.owner = Thread.currentThread();
		thread_cache_entry.capabilities = capabilities;

		fast_path_cache = thread_cache_entry;
	}

	/**
	 * Determine which extensions are available and returns the context profile mask. Helper method to ContextCapabilities.
	 *
	 * @param supported_extensions the Set to fill with the available extension names
	 *
	 * @return the context profile mask, will be 0 for any version < 3.2
	 */
	static void getSupportedExtensions(final Set<String> supported_extensions) {
		// Detect OpenGL version first
		final String version = glGetString(GL_VERSION);
		if ( version == null )
			throw new IllegalStateException("glGetString(GL_VERSION) returned null - possibly caused by missing current context.");

		final String VERSION_PREFIX = "OpenGL ES ";
		final StringTokenizer version_tokenizer = new StringTokenizer(version.substring(VERSION_PREFIX.length()), ". ");

		int majorVersion = 0;
		int minorVersion = 0;
		try {
			majorVersion = Integer.parseInt(version_tokenizer.nextToken());
			minorVersion = Integer.parseInt(version_tokenizer.nextToken());
		} catch (NumberFormatException e) {
			LWJGLUtil.log("The major and/or minor OpenGL version is malformed: " + e.getMessage());
		}

		// ----------------------[ 2.X ]----------------------
		if ( 3 <= majorVersion )
			supported_extensions.add("OpenGLES30");
		if ( 2 <= majorVersion )
			supported_extensions.add("OpenGLES20");

		// Parse EXTENSIONS string
		final String extensions_string = glGetString(GL_EXTENSIONS);
		if ( extensions_string == null )
			throw new IllegalStateException("glGetString(GL_EXTENSIONS) returned null - is there a context current?");

		final StringTokenizer tokenizer = new StringTokenizer(extensions_string);
		while ( tokenizer.hasMoreTokens() )
			supported_extensions.add(tokenizer.nextToken());
	}

	/**
	 * Helper method to ContextCapabilities. It will try to initialize the native stubs,
	 * and remove the given extension name from the extension set if the initialization fails.
	 */
	static void initNativeStubs(final Class extension_class, Set<String> supported_extensions, String ext_name) {
		//resetNativeStubs(extension_class);
		if ( supported_extensions.contains(ext_name) ) {
			try {
				doInitNativeStubs(extension_class);
			} catch (LWJGLException e) {
				LWJGLUtil.log("Failed to initialize extension " + extension_class + " - exception: " + e);
				supported_extensions.remove(ext_name);
			}
		}
	}

	static void doInitNativeStubs(final Class<?> extension_class) throws LWJGLException {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				public Object run() throws Exception {
					Method init_stubs_method = extension_class.getDeclaredMethod("initNativeStubs");
					init_stubs_method.invoke(null);
					return null;
				}
			});
		} catch (PrivilegedActionException e) {
			final Throwable c = e.getCause();
			if ( c instanceof InvocationTargetException )
				throw new LWJGLException(c.getCause());
			else
				throw new LWJGLException(c);
		}
	}

	/**
	 * Makes a GL context the current LWJGL context by loading GL function pointers. The context must be current before a call to
	 * this method! Instead it simply ensures that the current context is reflected accurately by GLContext's extension caps and
	 * function pointers. Use useContext(null) when no context is active. <p>If the context is the same as last time, then this is
	 * a no-op. <p>If the context has not been encountered before it will be fully initialized from scratch. Otherwise a cached set
	 * of caps and function pointers will be used. <p>The reference to the context is held in a weak reference; therefore if no
	 * strong reference exists to the GL context it will automatically be forgotten by the VM at an indeterminate point in the
	 * future, freeing up a little RAM.
	 *
	 * @param context The context object, which uniquely identifies a GL context. If context is null, the native stubs are
	 *                unloaded.
	 *
	 * @throws LWJGLException if context non-null, and the gl library can't be loaded or the basic GL11 functions can't be loaded
	 */
	public static synchronized void useContext(Object context) throws LWJGLException {
		if ( context == null ) {
			// Moved this to the shutdown hook
			ContextCapabilities.unloadAllStubs();
			setCapabilities(null);
			if ( did_auto_load )
				unloadOpenGLLibrary();
			return;
		}

		if ( gl_ref_count == 0 ) {
			loadOpenGLLibrary();
			did_auto_load = true;
		}

		try {
			ContextCapabilities capabilities = capability_cache.get(context);
			if ( capabilities == null ) {
				/*
				 * The capabilities object registers itself as current. This behaviour is caused
				 * by a chicken-and-egg situation where the constructor needs to call GL functions
				 * as part of its capability discovery, but GL functions cannot be called before
				 * a capabilities object has been set.
				 */
				new ContextCapabilities();
				capability_cache.put(context, getCapabilities());
			} else
				setCapabilities(capabilities);
		} catch (LWJGLException e) {
			if ( did_auto_load )
				unloadOpenGLLibrary();
			throw e;

		}
	}

	/** If the OpenGL reference count is 0, the library is loaded. The reference count is then incremented. */
	public static synchronized void loadOpenGLLibrary() throws LWJGLException {
		if ( gl_ref_count == 0 )
			nLoadOpenGLLibrary();
		gl_ref_count++;
	}

	private static native void nLoadOpenGLLibrary() throws LWJGLException;

	/** The OpenGL library reference count is decremented, and if it reaches 0, the library is unloaded. */
	public static synchronized void unloadOpenGLLibrary() {
		gl_ref_count--;
		/*
		 * Unload the native OpenGL library unless we're on linux, since
		 * some drivers (NVIDIA proprietary) crash on exit when unloading the library.
		 */
		if ( gl_ref_count == 0 && LWJGLUtil.getPlatform() != LWJGLUtil.PLATFORM_LINUX )
			nUnloadOpenGLLibrary();
	}

	private static native void nUnloadOpenGLLibrary();

	/** Native method to clear native stub bindings */
	static native void resetNativeStubs(Class clazz);

	private static final class CapabilitiesCacheEntry {

		Thread              owner;
		ContextCapabilities capabilities;
	}
}
