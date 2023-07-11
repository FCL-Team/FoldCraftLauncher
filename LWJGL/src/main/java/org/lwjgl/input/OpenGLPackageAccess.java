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
package org.lwjgl.input;

import org.lwjgl.opengl.InputImplementation;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedActionException;

/**
 * This class contains utilities for accessing the org.lwjgl.opengl
 * package through (privileged) reflection.
 */
final class OpenGLPackageAccess {
	static final Object global_lock;

	static {
		try {
			global_lock = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				public Object run() throws Exception {
					Field lock_field = Class.forName("org.lwjgl.opengl.GlobalLock").getDeclaredField("lock");
					lock_field.setAccessible(true);
					return lock_field.get(null);
				}
			});
		} catch (PrivilegedActionException e) {
			throw new Error(e);
		}
	}

	static InputImplementation createImplementation() {
		/* Use reflection since we can't make Display.getImplementation
		 * public
		 */
		try {
			return AccessController.doPrivileged(new PrivilegedExceptionAction<InputImplementation>() {
				public InputImplementation run() throws Exception {
					Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation");
					getImplementation_method.setAccessible(true);
					return (InputImplementation)getImplementation_method.invoke(null);
				}
			});
		} catch (PrivilegedActionException e) {
			throw new Error(e);
		}
	}
}
