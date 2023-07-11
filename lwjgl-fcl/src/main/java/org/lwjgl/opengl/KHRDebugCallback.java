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
package org.lwjgl.opengl;

import org.lwjgl.PointerWrapperAbstract;

/**
 * Instances of this class are needed to use the callback functionality of the KHR_debug extension.
 * Users of this class may provide implementations of the {@code Handler} interface to receive notifications.
 * The same {@code Handler} instance may be used by different contexts but it is not recommended.
 * Handler notifications are synchronized.
 *
 * @author Spasi
 */
public final class KHRDebugCallback extends PointerWrapperAbstract {

	/** Severity levels. */
	private static final int
		GL_DEBUG_SEVERITY_HIGH         = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM       = 0x9147,
		GL_DEBUG_SEVERITY_LOW          = 0x9148,
		GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;

	/** Sources. */
	private static final int
		GL_DEBUG_SOURCE_API             = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM   = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY     = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION     = 0x824A,
		GL_DEBUG_SOURCE_OTHER           = 0x824B;

	/** Types. */
	private static final int
		GL_DEBUG_TYPE_ERROR               = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR  = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY         = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE         = 0x8250,
		GL_DEBUG_TYPE_OTHER               = 0x8251,
		GL_DEBUG_TYPE_MARKER              = 0x8268;

	private static final long CALLBACK_POINTER;

	static {
		long pointer = 0;
		try {
			// Call reflectively so that we can compile this class for the Generator.
			pointer = (Long)Class.forName("org.lwjgl.opengl.CallbackUtil").getDeclaredMethod("getDebugCallbackKHR").invoke(null);
		} catch (Exception e) {
			// ignore
		}
		CALLBACK_POINTER = pointer;
	}

	private final Handler handler;

	/**
	 * Creates an KHRebugCallback with a default callback handler.
	 * The default handler will simply print the message on System.err.
	 */
	public KHRDebugCallback() {
		this(new Handler() {
			public void handleMessage(final int source, final int type, final int id, final int severity, final String message) {
				System.err.println("[LWJGL] KHR_debug message");
				System.err.println("\tID: " + id);

				String description;
				switch ( source ) {
					case GL_DEBUG_SOURCE_API:
						description = "API";
						break;
					case GL_DEBUG_SOURCE_WINDOW_SYSTEM:
						description = "WINDOW SYSTEM";
						break;
					case GL_DEBUG_SOURCE_SHADER_COMPILER:
						description = "SHADER COMPILER";
						break;
					case GL_DEBUG_SOURCE_THIRD_PARTY:
						description = "THIRD PARTY";
						break;
					case GL_DEBUG_SOURCE_APPLICATION:
						description = "APPLICATION";
						break;
					case GL_DEBUG_SOURCE_OTHER:
						description = "OTHER";
						break;
					default:
						description = printUnknownToken(source);
				}
				System.err.println("\tSource: " + description);

				switch ( type ) {
					case GL_DEBUG_TYPE_ERROR:
						description = "ERROR";
						break;
					case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
						description = "DEPRECATED BEHAVIOR";
						break;
					case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
						description = "UNDEFINED BEHAVIOR";
						break;
					case GL_DEBUG_TYPE_PORTABILITY:
						description = "PORTABILITY";
						break;
					case GL_DEBUG_TYPE_PERFORMANCE:
						description = "PERFORMANCE";
						break;
					case GL_DEBUG_TYPE_OTHER:
						description = "OTHER";
						break;
					case GL_DEBUG_TYPE_MARKER:
						description = "MARKER";
						break;
					default:
						description = printUnknownToken(type);
				}
				System.err.println("\tType: " + description);

				switch ( severity ) {
					case GL_DEBUG_SEVERITY_HIGH:
						description = "HIGH";
						break;
					case GL_DEBUG_SEVERITY_MEDIUM:
						description = "MEDIUM";
						break;
					case GL_DEBUG_SEVERITY_LOW:
						description = "LOW";
						break;
					case GL_DEBUG_SEVERITY_NOTIFICATION:
						description = "NOTIFICATION";
						break;
					default:
						description = printUnknownToken(severity);
				}
				System.err.println("\tSeverity: " + description);

				System.err.println("\tMessage: " + message);
			}

			private String printUnknownToken(final int token) {
				return "Unknown (0x" + Integer.toHexString(token).toUpperCase() + ")";
			}
		});
	}

	/**
	 * Creates an ARBDebugOutputCallback with the specified callback handler.
	 * The handler's {@code handleMessage} method will be called whenever
	 * debug output is generated by the GL.
	 *
	 * @param handler the callback handler
	 */
	public KHRDebugCallback(final Handler handler) {
		super(CALLBACK_POINTER);

		this.handler = handler;
	}

	Handler getHandler() {
		return handler;
	}

	/** Implementations of this interface can be used to receive ARB_debug_output notifications. */
	public interface Handler {

		/**
		 * This method will be called when an ARB_debug_output message is generated.
		 *
		 * @param source   the message source
		 * @param type     the message type
		 * @param id       the message ID
		 * @param severity the message severity
		 * @param message  the string representation of the message.
		 */
		void handleMessage(int source, int type, int id, int severity, String message);

	}

}