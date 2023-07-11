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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.lang.UnsatisfiedLinkError;

/**
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */
final class LinuxSysImplementation extends J2SESysImplementation {
	private static final int JNI_VERSION = 19;

	static {
		// Load libawt.so and libmawt.so, needed for libjawt.so
		java.awt.Toolkit.getDefaultToolkit();
		
		// manually load libjawt.so into vm, needed since Java 7
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					System.loadLibrary("jawt");
				} catch (UnsatisfiedLinkError e) {
			        // catch and ignore an already loaded in another classloader 
					// exception, as vm already has it loaded
			    }
				return null;
			}
		});
	}

	public int getRequiredJNIVersion() {
		return JNI_VERSION;
	}

	public boolean openURL(final String url) {
		// Linux may as well resort to pure Java hackery, as there's no Linux native way of doing it
		// right anyway.

		String[] browsers = {"sensible-browser", "xdg-open", "google-chrome", "chromium", "firefox", "iceweasel", "mozilla", "opera", "konqueror", "nautilus", "galeon", "netscape"};

		for ( final String browser : browsers ) {
			try {
				LWJGLUtil.execPrivileged(new String[] { browser, url });
				return true;
			} catch (Exception e) {
				// Ignore
				e.printStackTrace(System.err);
			}
		}

		// Seems to have failed
		return false;
	}

	public boolean has64Bit() {
		return true;
	}
}
