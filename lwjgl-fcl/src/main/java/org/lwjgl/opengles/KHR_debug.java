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
package org.lwjgl.opengles;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface KHR_debug {

	/**
	 * Tokens accepted by the &lt;target&gt; parameters of Enable, Disable, and
	 * IsEnabled:
	 */
	int GL_DEBUG_OUTPUT             = 0x92E0,
		GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;

	/** Returned by GetIntegerv when &lt;pname&gt; is CONTEXT_FLAGS: */
	int GL_CONTEXT_FLAG_DEBUG_BIT = 0x00000002;

	/**
	 * Tokens accepted by the &lt;value&gt; parameters of GetBooleanv, GetIntegerv,
	 * GetFloatv, GetDoublev and GetInteger64v:
	 */
	int GL_MAX_DEBUG_MESSAGE_LENGTH         = 0x9143,
		GL_MAX_DEBUG_LOGGED_MESSAGES        = 0x9144,
		GL_DEBUG_LOGGED_MESSAGES            = 0x9145,
		GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243,
		GL_MAX_DEBUG_GROUP_STACK_DEPTH      = 0x826C,
		GL_DEBUG_GROUP_STACK_DEPTH          = 0x826D,
		GL_MAX_LABEL_LENGTH                 = 0x82E8;

	/** Tokens accepted by the &lt;pname&gt; parameter of GetPointerv: */
	int GL_DEBUG_CALLBACK_FUNCTION   = 0x8244,
		GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;

	/**
	 * Tokens accepted or provided by the &lt;source&gt; parameters of
	 * DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;sources&gt;
	 * parameter of GetDebugMessageLog:
	 */
	int GL_DEBUG_SOURCE_API             = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM   = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY     = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION     = 0x824A,
		GL_DEBUG_SOURCE_OTHER           = 0x824B;

	/**
	 * Tokens accepted or provided by the &lt;type&gt; parameters of
	 * DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;types&gt;
	 * parameter of GetDebugMessageLog:
	 */
	int GL_DEBUG_TYPE_ERROR               = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR  = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY         = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE         = 0x8250,
		GL_DEBUG_TYPE_OTHER               = 0x8251,
		GL_DEBUG_TYPE_MARKER              = 0x8268;

	/**
	 * Tokens accepted or provided by the &lt;type&gt; parameters of
	 * DebugMessageControl and DEBUGPROC, and the &lt;types&gt; parameter of
	 * GetDebugMessageLog:
	 */
	int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269,
		GL_DEBUG_TYPE_POP_GROUP  = 0x826A;

	/**
	 * Tokens accepted or provided by the &lt;severity&gt; parameters of
	 * DebugMessageControl, DebugMessageInsert and DEBUGPROC callback functions,
	 * and the &lt;severities&gt; parameter of GetDebugMessageLog:
	 */
	int GL_DEBUG_SEVERITY_HIGH         = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM       = 0x9147,
		GL_DEBUG_SEVERITY_LOW          = 0x9148,
		GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;

	/** Returned by GetError: */
	int GL_STACK_UNDERFLOW = 0x0504,
		GL_STACK_OVERFLOW  = 0x0503;

	/**
	 * Tokens accepted or provided by the &lt;identifier&gt; parameters of
	 * ObjectLabel and GetObjectLabel:
	 */
	int GL_BUFFER           = 0x82E0,
		GL_SHADER           = 0x82E1,
		GL_PROGRAM          = 0x82E2,
		GL_QUERY            = 0x82E3,
		GL_PROGRAM_PIPELINE = 0x82E4,
		GL_SAMPLER          = 0x82E6,
		GL_DISPLAY_LIST     = 0x82E7;

	// -----------------------------

	void glDebugMessageControl(@GLenum int source,
	                           @GLenum int type,
	                           @GLenum int severity,
	                           @AutoSize(value = "ids", canBeNull = true) @GLsizei int count,
	                           @Check(canBeNull = true) @Const @GLuint IntBuffer ids,
	                           boolean enabled);

	void glDebugMessageInsert(@GLenum int source,
	                          @GLenum int type,
	                          @GLuint int id,
	                          @GLenum int severity,
	                          @AutoSize("buf") @GLsizei int length,
	                          @Const @GLchar ByteBuffer buf);

	@Alternate("glDebugMessageInsert")
	void glDebugMessageInsert(@GLenum int source,
	                          @GLenum int type,
	                          @GLuint int id,
	                          @GLenum int severity,
	                          @Constant("buf.length()") @GLsizei int length,
	                          CharSequence buf);

	/**
	 * The {@code KHRDebugCallback.Handler} implementation passed to this method will be used for
	 * KHR_debug messages. If callback is null, any previously registered handler for the current
	 * thread will be unregistered and stop receiving messages.
	 *
	 * @param callback the callback function to use
	 */
	@Code(
		// Create a GlobalRef to the callback object and register it with the current context.
		javaBeforeNative = "\t\tlong userParam = callback == null ? 0 : CallbackUtil.createGlobalRef(callback.getHandler());\n" +
		                   "\t\tCallbackUtil.registerContextCallbackKHR(userParam);"
	)
	void glDebugMessageCallback(@PointerWrapper(value = "GLDEBUGPROC", canBeNull = true) KHRDebugCallback callback,
	                            @Constant("userParam") @PointerWrapper("GLvoid *") long userParam);

	@GLuint
	int glGetDebugMessageLog(@GLuint int count,
	                         @AutoSize(value = "messageLog", canBeNull = true) @GLsizei int bufsize,
	                         @Check(value = "count", canBeNull = true) @GLenum IntBuffer sources,
	                         @Check(value = "count", canBeNull = true) @GLenum IntBuffer types,
	                         @Check(value = "count", canBeNull = true) @GLuint IntBuffer ids,
	                         @Check(value = "count", canBeNull = true) @GLenum IntBuffer severities,
	                         @Check(value = "count", canBeNull = true) @GLsizei IntBuffer lengths,
	                         @Check(canBeNull = true) @OutParameter @GLchar ByteBuffer messageLog);

	// Not really useful and a pain to implement in Java
	// void glGetPointerv(@GLenum int pname, void** params);

	void glPushDebugGroup(@GLenum int source, @GLuint int id, @AutoSize("message") @GLsizei int length,
	                      @Const @GLchar ByteBuffer message);

	@Alternate("glPushDebugGroup")
	void glPushDebugGroup(@GLenum int source, @GLuint int id, @Constant("message.length()") @GLsizei int length,
	                      CharSequence message);

	void glPopDebugGroup();

	void glObjectLabel(@GLenum int identifier, @GLuint int name, @AutoSize(value = "label", canBeNull = true) @GLsizei int length,
	                   @Check(canBeNull = true) @Const @GLchar ByteBuffer label);

	@Alternate("glObjectLabel")
	void glObjectLabel(@GLenum int identifier, @GLuint int name, @Constant("label.length()") @GLsizei int length,
	                   CharSequence label);

	void glGetObjectLabel(@GLenum int identifier, @GLuint int name, @AutoSize("label") @GLsizei int bufSize,
	                      @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                      @OutParameter @GLchar ByteBuffer label);

	@Alternate("glGetObjectLabel")
	@GLreturn(value = "label", maxLength = "bufSize")
	void glGetObjectLabel2(@GLenum int identifier, @GLuint int name, @GLsizei int bufSize,
	                       @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(label_length)") IntBuffer length,
	                       @OutParameter @GLchar ByteBuffer label);

	void glObjectPtrLabel(@PointerWrapper("GLvoid *") org.lwjgl.PointerWrapper ptr, @AutoSize(value = "label", canBeNull = true) @GLsizei int length,
	                      @Check(canBeNull = true) @Const @GLchar ByteBuffer label);

	@Alternate("glObjectPtrLabel")
	void glObjectPtrLabel(@PointerWrapper("GLvoid *") org.lwjgl.PointerWrapper ptr, @Constant("label.length()") @GLsizei int length,
	                      CharSequence label);

	void glGetObjectPtrLabel(@PointerWrapper("GLvoid *") org.lwjgl.PointerWrapper ptr, @AutoSize("label") @GLsizei int bufSize,
	                         @OutParameter @Check(value = "1", canBeNull = true) @GLsizei IntBuffer length,
	                         @OutParameter @GLchar ByteBuffer label);

	@Alternate("glGetObjectPtrLabel")
	@GLreturn(value = "label", maxLength = "bufSize")
	void glGetObjectPtrLabel2(@PointerWrapper("GLvoid *") org.lwjgl.PointerWrapper ptr, @GLsizei int bufSize,
	                          @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(label_length)") IntBuffer length,
	                          @OutParameter @GLchar ByteBuffer label);

}