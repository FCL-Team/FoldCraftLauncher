/*
 * Copyright (c) 2002-2012 LWJGL Project
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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLvoid;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.lang.model.type.TypeKind;

public interface ARB_multi_draw_indirect {

	@Reuse("GL43")
	void glMultiDrawArraysIndirect(@GLenum int mode,
	                               @BufferObject(BufferKind.IndirectBO) @Check("(stride == 0 ? 4 * 4 : stride) * primcount") @Const @GLvoid ByteBuffer indirect,
	                               @GLsizei int primcount,
	                               @GLsizei int stride);

	@Reuse("GL43")
	@Alternate("glMultiDrawArraysIndirect")
	void glMultiDrawArraysIndirect(@GLenum int mode,
	                               @BufferObject(BufferKind.IndirectBO) @Check("(stride == 0 ? 4 : stride >> 2) * primcount") @Const @GLvoid(TypeKind.INT) IntBuffer indirect,
	                               @GLsizei int primcount,
	                               @GLsizei int stride);

	@Reuse("GL43")
	void glMultiDrawElementsIndirect(@GLenum int mode,
	                                 @GLenum int type,
	                                 @BufferObject(BufferKind.IndirectBO) @Check("(stride == 0 ? 5 * 4 : stride) * primcount") @Const @GLvoid ByteBuffer indirect,
	                                 @GLsizei int primcount,
	                                 @GLsizei int stride);

	@Reuse("GL43")
	@Alternate("glMultiDrawElementsIndirect")
	void glMultiDrawElementsIndirect(@GLenum int mode,
	                                 @GLenum int type,
	                                 @BufferObject(BufferKind.IndirectBO) @Check("(stride == 0 ? 5 : stride >> 2) * primcount") @Const @GLvoid(TypeKind.INT) IntBuffer indirect,
	                                 @GLsizei int primcount,
	                                 @GLsizei int stride);

}