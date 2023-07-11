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
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface NV_transform_feedback2 {

	/** Accepted by the &lt;target&gt; parameter of BindTransformFeedbackNV: */

	int GL_TRANSFORM_FEEDBACK_NV = 0x8E22;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV = 0x8E23;
	int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV = 0x8E24;
	int GL_TRANSFORM_FEEDBACK_BINDING_NV = 0x8E25;

	void glBindTransformFeedbackNV(@GLenum int target, @GLuint int id);

	void glDeleteTransformFeedbacksNV(@AutoSize("ids") @GLsizei int n, @Const @GLuint IntBuffer ids);

	@Alternate("glDeleteTransformFeedbacksNV")
	void glDeleteTransformFeedbacksNV(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, id)", keepParam = true) int id);

	void glGenTransformFeedbacksNV(@AutoSize("ids") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	@Alternate("glGenTransformFeedbacksNV")
	@GLreturn("ids")
	void glGenTransformFeedbacksNV2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	boolean glIsTransformFeedbackNV(@GLuint int id);

	void glPauseTransformFeedbackNV();

	void glResumeTransformFeedbackNV();

	void glDrawTransformFeedbackNV(@GLenum int mode, @GLuint int id);

}