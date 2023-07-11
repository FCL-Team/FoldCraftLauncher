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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

@Extension(postfix = "", className = "ARBVertexType2_10_10_10_REV")
public interface ARB_vertex_type_2_10_10_10_rev {

	/**
	 * Accepted by the &lt;type&gt; parameter of VertexAttribPointer, VertexPointer,
	 * NormalPointer, ColorPointer, SecondaryColorPointer, TexCoordPointer,
	 * VertexAttribP{1234}ui, VertexP*, TexCoordP*, MultiTexCoordP*, NormalP3ui,
	 * ColorP*, SecondaryColorP* and VertexAttribP*
	 */
	int GL_UNSIGNED_INT_2_10_10_10_REV = GL12.GL_UNSIGNED_INT_2_10_10_10_REV;
	int GL_INT_2_10_10_10_REV = 0x8D9F;

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexP2ui(@GLenum int type, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexP3ui(@GLenum int type, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexP4ui(@GLenum int type, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexP2uiv(@GLenum int type, @Check("2") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexP3uiv(@GLenum int type, @Check("3") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexP4uiv(@GLenum int type, @Check("4") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	void glTexCoordP1ui(@GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glTexCoordP2ui(@GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glTexCoordP3ui(@GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glTexCoordP4ui(@GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glTexCoordP1uiv(@GLenum int type, @Check("1") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glTexCoordP2uiv(@GLenum int type, @Check("2") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glTexCoordP3uiv(@GLenum int type, @Check("3") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glTexCoordP4uiv(@GLenum int type, @Check("4") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glMultiTexCoordP1ui(@GLenum int texture, @GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glMultiTexCoordP2ui(@GLenum int texture, @GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glMultiTexCoordP3ui(@GLenum int texture, @GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glMultiTexCoordP4ui(@GLenum int texture, @GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glMultiTexCoordP1uiv(@GLenum int texture, @GLenum int type, @Check("1") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glMultiTexCoordP2uiv(@GLenum int texture, @GLenum int type, @Check("2") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glMultiTexCoordP3uiv(@GLenum int texture, @GLenum int type, @Check("3") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glMultiTexCoordP4uiv(@GLenum int texture, @GLenum int type, @Check("4") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glNormalP3ui(@GLenum int type, @GLuint int coords);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("coords")
	void glNormalP3uiv(@GLenum int type, @Check("3") @Const @GLuint IntBuffer coords);

	@Reuse("GL33")
	@NoErrorCheck
	void glColorP3ui(@GLenum int type, @GLuint int color);

	@Reuse("GL33")
	@NoErrorCheck
	void glColorP4ui(@GLenum int type, @GLuint int color);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("color")
	void glColorP3uiv(@GLenum int type, @Check("3") @Const @GLuint IntBuffer color);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("color")
	void glColorP4uiv(@GLenum int type, @Check("4") @Const @GLuint IntBuffer color);

	@Reuse("GL33")
	@NoErrorCheck
	void glSecondaryColorP3ui(@GLenum int type, @GLuint int color);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("color")
	void glSecondaryColorP3uiv(@GLenum int type, @Check("3") @Const @GLuint IntBuffer color);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexAttribP1ui(@GLuint int index, @GLenum int type, boolean normalized, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexAttribP2ui(@GLuint int index, @GLenum int type, boolean normalized, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexAttribP3ui(@GLuint int index, @GLenum int type, boolean normalized, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	void glVertexAttribP4ui(@GLuint int index, @GLenum int type, boolean normalized, @GLuint int value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexAttribP1uiv(@GLuint int index, @GLenum int type, boolean normalized, @Check("1") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexAttribP2uiv(@GLuint int index, @GLenum int type, boolean normalized, @Check("2") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexAttribP3uiv(@GLuint int index, @GLenum int type, boolean normalized, @Check("3") @Const @GLuint IntBuffer value);

	@Reuse("GL33")
	@NoErrorCheck
	@StripPostfix("value")
	void glVertexAttribP4uiv(@GLuint int index, @GLenum int type, boolean normalized, @Check("4") @Const @GLuint IntBuffer value);

}