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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLchar;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ARB_debug_output {

	/**
	 * Tokens accepted by the &lt;target&gt; parameters of Enable, Disable,
	 * and IsEnabled:
	 */
	int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB = 0x8242;

	/**
	 * Tokens accepted by the &lt;value&gt; parameters of GetBooleanv,
	 * GetIntegerv, GetFloatv, and GetDoublev:
	 */
	int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB = 0x9143,
		GL_MAX_DEBUG_LOGGED_MESSAGES_ARB = 0x9144,
		GL_DEBUG_LOGGED_MESSAGES_ARB = 0x9145,
		GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB = 0x8243;

	/** Tokens accepted by the &lt;pname&gt; parameter of GetPointerv: */
	int GL_DEBUG_CALLBACK_FUNCTION_ARB = 0x8244,
		GL_DEBUG_CALLBACK_USER_PARAM_ARB = 0x8245;

	/**
	 * Tokens accepted or provided by the &lt;source&gt; parameters of
	 * DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB,
	 * and the &lt;sources&gt; parameter of GetDebugMessageLogARB:
	 */
	int GL_DEBUG_SOURCE_API_ARB = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION_ARB = 0x824A,
		GL_DEBUG_SOURCE_OTHER_ARB = 0x824B;

	/**
	 * Tokens accepted or provided by the &lt;type&gt; parameters of
	 * DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB,
	 * and the &lt;types&gt; parameter of GetDebugMessageLogARB:
	 */
	int GL_DEBUG_TYPE_ERROR_ARB = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY_ARB = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE_ARB = 0x8250,
		GL_DEBUG_TYPE_OTHER_ARB = 0x8251;

	/**
	 * Tokens accepted or provided by the &lt;severity&gt; parameters of
	 * DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB
	 * callback functions, and the &lt;severities&gt; parameter of
	 * GetDebugMessageLogARB:
	 */
	int GL_DEBUG_SEVERITY_HIGH_ARB = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM_ARB = 0x9147,
		GL_DEBUG_SEVERITY_LOW_ARB = 0x9148;

	void glDebugMessageControlARB(@GLenum int source,
	                              @GLenum int type,
	                              @GLenum int severity,
	                              @AutoSize(value = "ids", canBeNull = true) @GLsizei int count,
	                              @Check(canBeNull = true) @Const @GLuint IntBuffer ids,
	                              boolean enabled);

	void glDebugMessageInsertARB(@GLenum int source, @GLenum int type, @GLuint int id, @GLenum int severity, @AutoSize("buf") @GLsizei int length, @Const @GLchar ByteBuffer buf);

	@Alternate("glDebugMessageInsertARB")
	void glDebugMessageInsertARB(@GLenum int source, @GLenum int type, @GLuint int id, @GLenum int severity, @Constant("buf.length()") @GLsizei int length, CharSequence buf);

	/**
	 * The {@code ARBDebugOutputCallback.Handler} implementation passed to this method will be used for
	 * ARB_debug_output messages. If callback is null, any previously registered handler for the current
	 * thread will be unregistered and stop receiving messages.
	 *
	 * @param callback the callback function to use
	 */
	@Code(
		// Create a GlobalRef to the callback object and register it with the current context.
		javaBeforeNative = "\t\tlong userParam = callback == null ? 0 : CallbackUtil.createGlobalRef(callback.getHandler());\n" +
		                   "\t\tCallbackUtil.registerContextCallbackARB(userParam);"
	)
	void glDebugMessageCallbackARB(@PointerWrapper(value = "GLDEBUGPROCARB", canBeNull = true) ARBDebugOutputCallback callback,
	                               @Constant("userParam") @PointerWrapper("GLvoid *") long userParam);

	@GLuint
	int glGetDebugMessageLogARB(@GLuint int count,
	                            @AutoSize(value = "messageLog", canBeNull = true) @GLsizei int logSize,
	                            @Check(value = "count", canBeNull = true) @GLenum IntBuffer sources,
	                            @Check(value = "count", canBeNull = true) @GLenum IntBuffer types,
	                            @Check(value = "count", canBeNull = true) @GLuint IntBuffer ids,
	                            @Check(value = "count", canBeNull = true) @GLenum IntBuffer severities,
	                            @Check(value = "count", canBeNull = true) @GLsizei IntBuffer lengths,
	                            @Check(canBeNull = true) @OutParameter @GLchar ByteBuffer messageLog);

	// Not really useful and a pain to implement in Java
	//void glGetPointerv(@GLenum int pname, void**params);

}