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

import java.nio.ByteBuffer;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Method;

import org.lwjgl.opengl.Display;

/**
 * <p>
 * @author $Author$
 * @version $Revision$
 * $Id$
 */
final class WindowsSysImplementation extends DefaultSysImplementation {
	private static final int JNI_VERSION = 24;

	static {
		Sys.initialize();
	}

	public int getRequiredJNIVersion() {
		return JNI_VERSION;
	}

	public long getTimerResolution() {
		return 1000;
	}

	public long getTime() {
		return nGetTime();
	}
	private static native long nGetTime();

	public boolean has64Bit() {
		return true;
	}

	private static long getHwnd() {
		if (!Display.isCreated())
			return 0;
		/* Use reflection since we can't make Display.getImplementation
		 * public
		 */
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Long>() {
				public Long run() throws Exception {
					Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation");
					getImplementation_method.setAccessible(true);
					Object display_impl = getImplementation_method.invoke(null);
					Class<?> WindowsDisplay_class = Class.forName("org.lwjgl.opengl.WindowsDisplay");
					Method getHwnd_method = WindowsDisplay_class.getDeclaredMethod("getHwnd");
					getHwnd_method.setAccessible(true);
					return (Long)getHwnd_method.invoke(display_impl);
				}
			});
		} catch (PrivilegedActionException e) {
			throw new Error(e);
		}
	}

	public void alert(String title, String message) {
		if(!Display.isCreated()) {
			initCommonControls();
		}

		LWJGLUtil.log(String.format("*** Alert *** %s\n%s\n", title, message));

		final ByteBuffer titleText = MemoryUtil.encodeUTF16(title);
		final ByteBuffer messageText = MemoryUtil.encodeUTF16(message);
		nAlert(getHwnd(), MemoryUtil.getAddress(titleText), MemoryUtil.getAddress(messageText));
	}
	private static native void nAlert(long parent_hwnd, long title, long message);
	private static native void initCommonControls();

	public boolean openURL(final String url) {
		try {
			LWJGLUtil.execPrivileged(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
			return true;
		} catch (Exception e) {
			LWJGLUtil.log("Failed to open url (" + url + "): " + e.getMessage());
			return false;
		}
	}

	public String getClipboard() {
		return nGetClipboard();
	}
	private static native String nGetClipboard();
}
