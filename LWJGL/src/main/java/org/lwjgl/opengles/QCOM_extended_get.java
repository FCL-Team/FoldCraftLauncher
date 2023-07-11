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

import org.lwjgl.util.generator.AutoSize;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.OutParameter;
import org.lwjgl.util.generator.Result;
import org.lwjgl.util.generator.opengl.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface QCOM_extended_get {

	/** Accepted by the &lt;pname&gt; parameter of ExtGetTexLevelParameterivQCOM */
	int GL_TEXTURE_WIDTH_QCOM           = 0x8BD2,
		GL_TEXTURE_HEIGHT_QCOM          = 0x8BD3,
		GL_TEXTURE_DEPTH_QCOM           = 0x8BD4,
		GL_TEXTURE_INTERNAL_FORMAT_QCOM = 0x8BD5,
		GL_TEXTURE_FORMAT_QCOM          = 0x8BD6,
		GL_TEXTURE_TYPE_QCOM            = 0x8BD7,
		GL_TEXTURE_IMAGE_VALID_QCOM     = 0x8BD8,
		GL_TEXTURE_NUM_LEVELS_QCOM      = 0x8BD9,
		GL_TEXTURE_TARGET_QCOM          = 0x8BDA,
		GL_TEXTURE_OBJECT_VALID_QCOM    = 0x8BDB;

	/** Accepted by the &lt;pname&gt; parameter of ExtTexObjectStateOverrideiQCOM */
	int GL_STATE_RESTORE = 0x8BDC;

	void glExtGetTexturesQCOM(@OutParameter @Check("1") @GLuint IntBuffer textures,
	                          @AutoSize("textures") int maxTextures,
	                          @OutParameter @Check("1") IntBuffer numTextures);

	void glExtGetBuffersQCOM(@OutParameter @Check("1") @GLuint IntBuffer buffers,
	                         @AutoSize("buffers") int maxBuffers,
	                         @OutParameter @Check("1") IntBuffer numBuffers);

	void glExtGetRenderbuffersQCOM(@OutParameter @Check("1") @GLuint IntBuffer renderbuffers,
	                               @AutoSize("renderbuffers") int maxRenderbuffers,
	                               @OutParameter @Check("1") IntBuffer numRenderbuffers);

	void glExtGetFramebuffersQCOM(@OutParameter @Check("1") @GLuint IntBuffer framebuffers,
	                              @AutoSize("framebuffers") int maxFramebuffers,
	                              @OutParameter @Check("1") IntBuffer numFramebuffers);

	void glExtGetTexLevelParameterivQCOM(@GLuint int texture, @GLenum int face, int level,
	                                     @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	void glExtTexObjectStateOverrideiQCOM(@GLenum int target, @GLenum int pname, int param);

	void glExtGetTexSubImageQCOM(@GLenum int target, int level,
	                             int xoffset, int yoffset, int zoffset,
	                             @GLsizei int width, @GLsizei int height, @GLsizei int depth,
	                             @GLenum int format, @GLenum int type,
	                             @OutParameter
	                             @Check("GLChecks.calculateImageStorage(texels, format, type, width, height, depth)")
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLfloat Buffer texels);

	void glExtGetBufferPointervQCOM(@GLenum int target, @Result @GLvoid ByteBuffer params);

}