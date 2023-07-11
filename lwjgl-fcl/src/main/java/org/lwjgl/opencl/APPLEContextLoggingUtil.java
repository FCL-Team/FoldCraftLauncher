/*
 * Copyright (c) 2002-2010 LWJGL Project
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

import java.nio.ByteBuffer;

/**
 * Utility class that provides CLContextCallback implementations that use
 * the APPLE_ContextLoggingFunctions callback functions.
 * <p/>
 * TODO: Test this class
 *
 * @author Spasi
 */
public final class APPLEContextLoggingUtil {

	/** Sends all log messages to the Apple System Logger. */
	public static final CLContextCallback SYSTEM_LOG_CALLBACK;

	/** Sends all log messages to the file descriptor stdout. */
	public static final CLContextCallback STD_OUT_CALLBACK;

	/** Sends all log messages to the file descriptor stderr. */
	public static final CLContextCallback STD_ERR_CALLBACK;

	static {
		if ( CLCapabilities.CL_APPLE_ContextLoggingFunctions ) {
			SYSTEM_LOG_CALLBACK = new CLContextCallback(CallbackUtil.getLogMessageToSystemLogAPPLE()) {
				protected void handleMessage(final String errinfo, final ByteBuffer private_info) { throw new UnsupportedOperationException(); }
			};

			STD_OUT_CALLBACK = new CLContextCallback(CallbackUtil.getLogMessageToStdoutAPPLE()) {
				protected void handleMessage(final String errinfo, final ByteBuffer private_info) { throw new UnsupportedOperationException(); }
			};

			STD_ERR_CALLBACK = new CLContextCallback(CallbackUtil.getLogMessageToStderrAPPLE()) {
				protected void handleMessage(final String errinfo, final ByteBuffer private_info) { throw new UnsupportedOperationException(); }
			};
		} else {
			SYSTEM_LOG_CALLBACK = null;
			STD_OUT_CALLBACK = null;
			STD_ERR_CALLBACK = null;
		}
	}

	private APPLEContextLoggingUtil() {}

}