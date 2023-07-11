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
import org.lwjgl.util.generator.opengl.*;

import java.nio.*;

public interface ARB_vertex_blend {

	int GL_MAX_VERTEX_UNITS_ARB = 0x86A4;
	int GL_ACTIVE_VERTEX_UNITS_ARB = 0x86A5;
	int GL_WEIGHT_SUM_UNITY_ARB = 0x86A6;
	int GL_VERTEX_BLEND_ARB = 0x86A7;
	int GL_CURRENT_WEIGHT_ARB = 0x86A8;
	int GL_WEIGHT_ARRAY_TYPE_ARB = 0x86A9;
	int GL_WEIGHT_ARRAY_STRIDE_ARB = 0x86AA;
	int GL_WEIGHT_ARRAY_SIZE_ARB = 0x86AB;
	int GL_WEIGHT_ARRAY_POINTER_ARB = 0x86AC;
	int GL_WEIGHT_ARRAY_ARB = 0x86AD;
	int GL_MODELVIEW0_ARB = 0x1700;
	int GL_MODELVIEW1_ARB = 0x850a;
	int GL_MODELVIEW2_ARB = 0x8722;
	int GL_MODELVIEW3_ARB = 0x8723;
	int GL_MODELVIEW4_ARB = 0x8724;
	int GL_MODELVIEW5_ARB = 0x8725;
	int GL_MODELVIEW6_ARB = 0x8726;
	int GL_MODELVIEW7_ARB = 0x8727;
	int GL_MODELVIEW8_ARB = 0x8728;
	int GL_MODELVIEW9_ARB = 0x8729;
	int GL_MODELVIEW10_ARB = 0x872A;
	int GL_MODELVIEW11_ARB = 0x872B;
	int GL_MODELVIEW12_ARB = 0x872C;
	int GL_MODELVIEW13_ARB = 0x872D;
	int GL_MODELVIEW14_ARB = 0x872E;
	int GL_MODELVIEW15_ARB = 0x872F;
	int GL_MODELVIEW16_ARB = 0x8730;
	int GL_MODELVIEW17_ARB = 0x8731;
	int GL_MODELVIEW18_ARB = 0x8732;
	int GL_MODELVIEW19_ARB = 0x8733;
	int GL_MODELVIEW20_ARB = 0x8734;
	int GL_MODELVIEW21_ARB = 0x8735;
	int GL_MODELVIEW22_ARB = 0x8736;
	int GL_MODELVIEW23_ARB = 0x8737;
	int GL_MODELVIEW24_ARB = 0x8738;
	int GL_MODELVIEW25_ARB = 0x8739;
	int GL_MODELVIEW26_ARB = 0x873A;
	int GL_MODELVIEW27_ARB = 0x873B;
	int GL_MODELVIEW28_ARB = 0x873C;
	int GL_MODELVIEW29_ARB = 0x873D;
	int GL_MODELVIEW30_ARB = 0x873E;
	int GL_MODELVIEW31_ARB = 0x873F;

	@StripPostfix("pWeights")
	void glWeightbvARB(@AutoSize("pWeights") int size, ByteBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightsvARB(@AutoSize("pWeights") int size, ShortBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightivARB(@AutoSize("pWeights") int size, IntBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightfvARB(@AutoSize("pWeights") int size, FloatBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightdvARB(@AutoSize("pWeights") int size, DoubleBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightubvARB(@AutoSize("pWeights") int size, @GLubyte ByteBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightusvARB(@AutoSize("pWeights") int size, @GLushort ShortBuffer pWeights);

	@StripPostfix("pWeights")
	void glWeightuivARB(@AutoSize("pWeights") int size, @GLuint IntBuffer pWeights);

	void glWeightPointerARB(int size, @AutoType("pPointer") @GLenum int type, @GLsizei int stride,
	                        @CachedReference
	                        @BufferObject(BufferKind.ArrayVBO)
	                        @Check
	                        @Const
	                        @GLbyte
	                        @GLubyte
	                        @GLshort
	                        @GLushort
	                        @GLint
	                        @GLuint
	                        @GLfloat
	                        @GLdouble Buffer pPointer);

	void glVertexBlendARB(int count);
}
