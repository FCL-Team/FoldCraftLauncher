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

import java.nio.*;

public interface ATI_envmap_bumpmap {
	int GL_BUMP_ROT_MATRIX_ATI = 0x8775;
	int GL_BUMP_ROT_MATRIX_SIZE_ATI = 0x8776;
	int GL_BUMP_NUM_TEX_UNITS_ATI = 0x8777;
	int GL_BUMP_TEX_UNITS_ATI = 0x8778;
	int GL_DUDV_ATI = 0x8779;
	int GL_DU8DV8_ATI = 0x877A;
	int GL_BUMP_ENVMAP_ATI = 0x877B;
	int GL_BUMP_TARGET_ATI = 0x877C;

	@StripPostfix("param")
	void glTexBumpParameterfvATI(@GLenum int pname, @Check("4") @Const FloatBuffer param);

	@StripPostfix("param")
	void glTexBumpParameterivATI(@GLenum int pname, @Check("4") @Const IntBuffer param);

	@StripPostfix("param")
	void glGetTexBumpParameterfvATI(@GLenum int pname, @OutParameter @Check("4") FloatBuffer param);

	@StripPostfix("param")
	void glGetTexBumpParameterivATI(@GLenum int pname, @OutParameter @Check("4") IntBuffer param);
}
