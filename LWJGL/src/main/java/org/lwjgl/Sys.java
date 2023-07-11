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
package org.lwjgl;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;

import org.lwjgl.input.Mouse;

/**
 * <p>
 * System class (named Sys so as not to conflict with java.lang.System)
 * </p>
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */
public final class Sys {
	/** The native library name */
	private static final String JNI_LIBRARY_NAME = "lwjgl2";

	/** Current version of library */
	private static final String VERSION = "2.9.3";

	private static final String POSTFIX64BIT = "64";

	/** The implementation instance to delegate platform specific behavior to */
	private static final SysImplementation implementation;
	private static final boolean is64Bit;

	private static void doLoadLibrary(final String lib_name) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				String library_path = System.getProperty("org.lwjgl.librarypath");
				if (library_path != null) {
					System.load(library_path + File.separator + LWJGLUtil.mapLibraryName(lib_name));
				} else {
					System.loadLibrary(lib_name);
				}
				return null;
			}
		});
	}

	private static void loadLibrary(final String lib_name) {
		// actively try to load 64bit libs on 64bit architectures first
		String osArch = System.getProperty("os.arch");
		boolean try64First = LWJGLUtil.getPlatform() != LWJGLUtil.PLATFORM_MACOSX && ("amd64".equals(osArch) || "x86_64".equals(osArch));

		Error err = null;
		if ( try64First ) {
			try {
				doLoadLibrary(lib_name + POSTFIX64BIT);
				return;
			} catch (UnsatisfiedLinkError e) {
				err = e;
			}
		}
		
		// fallback to loading the "old way"
		try {
			doLoadLibrary(lib_name);
		} catch (UnsatisfiedLinkError e) {
			if ( try64First )
				throw err;

			if (implementation.has64Bit()) {
				try {
					doLoadLibrary(lib_name + POSTFIX64BIT);
					return;
				} catch (UnsatisfiedLinkError e2) {
					LWJGLUtil.log("Failed to load 64 bit library: " + e2.getMessage());
				}
			}

			// Throw original error
			throw e;
		}
	}

	static {
		implementation = createImplementation();
		loadLibrary(JNI_LIBRARY_NAME);
		is64Bit = implementation.getPointerSize() == 8;

		int native_jni_version = implementation.getJNIVersion();
		int required_version = implementation.getRequiredJNIVersion();
		if (native_jni_version != required_version)
			throw new LinkageError("Version mismatch: jar version is '" + required_version +
                             "', native library version is '" + native_jni_version + "'");
		implementation.setDebug(LWJGLUtil.DEBUG);
	}

	private static SysImplementation createImplementation() {
		switch (LWJGLUtil.getPlatform()) {
			case LWJGLUtil.PLATFORM_LINUX:
				return new LinuxSysImplementation();
			case LWJGLUtil.PLATFORM_WINDOWS:
				return new WindowsSysImplementation();
			case LWJGLUtil.PLATFORM_MACOSX:
				return new MacOSXSysImplementation();
			case LWJGLUtil.PLATFORM_FCL:
				return new FCLSysImplementation();
			default:
				throw new IllegalStateException("Unsupported platform");
		}
	}

	/**
	 * No constructor for Sys.
	 */
	private Sys() {
	}

	/**
	 * Return the version of the core LWJGL libraries as a String.
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Initialization. This is just a dummy method to trigger the static constructor.
	 */
	public static void initialize() {
	}

	/** Returns true if a 64bit implementation was loaded. */
	public static boolean is64Bit() {
		return is64Bit;
	}

	/**
	 * Obtains the number of ticks that the hires timer does in a second. This method is fast;
	 * it should be called as frequently as possible, as it recalibrates the timer.
	 *
	 * @return timer resolution in ticks per second or 0 if no timer is present.
	 */
	public static long getTimerResolution() {
		return implementation.getTimerResolution();
	}

	/**
	 * Gets the current value of the hires timer, in ticks. When the Sys class is first loaded
	 * the hires timer is reset to 0. If no hires timer is present then this method will always
	 * return 0.<p><strong>NOTEZ BIEN</strong> that the hires timer WILL wrap around.
	 *
	 * @return the current hires time, in ticks (always >= 0)
	 */
	public static long getTime() {
		return implementation.getTime() & 0x7FFFFFFFFFFFFFFFL;
	}

	/**
	 * Attempt to display a modal alert to the user. This method should be used
	 * when a game fails to initialize properly or crashes out losing its display
	 * in the process. It is provided because AWT may not be available on the target
	 * platform, although on Mac and Linux and other platforms supporting AWT we
	 * delegate the task to AWT instead of doing it ourselves.
	 * <p>
	 * The alert should display the title and the message and then the current
	 * thread should block until the user dismisses the alert - typically with an
	 * OK button click.
	 * <p>
	 * It may be that the user's system has no windowing system installed for some
	 * reason, in which case this method may do nothing at all, or attempt to provide
	 * some console output.
	 *
	 * @param title The title of the alert. We suggest using the title of your game.
	 * @param message The message text for the alert.
	 */
	public static void alert(String title, String message) {
		boolean grabbed = Mouse.isGrabbed();
		if (grabbed) {
			Mouse.setGrabbed(false);
		}
		if (title == null)
			title = "";
		if (message == null)
			message = "";
		implementation.alert(title, message);
		if (grabbed) {
			Mouse.setGrabbed(true);
		}
	}

	/**
	 * Open the system web browser and point it at the specified URL. It is recommended
	 * that this not be called whilst your game is running, but on application exit in
	 * a shutdown hook, as the screen resolution will not be reset when the browser is
	 * brought into view.
	 * <p>
	 * There is no guarantee that this will work, nor that we can detect if it has
	 * failed - hence we don't return success code or throw an Exception. This is just a
	 * best attempt at opening the URL given - don't rely on it to work!
	 * <p>
	 * @param url The URL. Ensure that the URL is properly encoded.
	 * @return false if we are CERTAIN the call has failed
	 */
	public static boolean openURL(String url) {
		// Attempt to use Webstart if we have it available
		try {
			// Lookup the javax.jnlp.BasicService object
			final Class<?> serviceManagerClass = Class.forName("javax.jnlp.ServiceManager");
			Method lookupMethod = AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() {
				public Method run() throws Exception {
					return serviceManagerClass.getMethod("lookup", String.class);
				}
			});
			Object basicService = lookupMethod.invoke(serviceManagerClass, new Object[] {"javax.jnlp.BasicService"});
			final Class<?> basicServiceClass = Class.forName("javax.jnlp.BasicService");
			Method showDocumentMethod = AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() {
				public Method run() throws Exception {
					return basicServiceClass.getMethod("showDocument", URL.class);
				}
			});
			try {
				Boolean ret = (Boolean)showDocumentMethod.invoke(basicService, new URL(url));
				return ret;
			} catch (MalformedURLException e) {
				e.printStackTrace(System.err);
				return false;
			}
		} catch (Exception ue) {
			return implementation.openURL(url);
		}
	}

	/**
	 * Get the contents of the system clipboard. The system might not have a
	 * clipboard (particularly if it doesn't even have a keyboard) in which case
	 * we return null. Otherwise we return a String, which may be the empty
	 * string "".
	 *
	 * @return a String, or null if there is no system clipboard.
	 */
	public static String getClipboard() {
		return implementation.getClipboard();
	}
}
