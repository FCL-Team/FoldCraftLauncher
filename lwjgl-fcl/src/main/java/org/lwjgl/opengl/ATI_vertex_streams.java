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

public interface ATI_vertex_streams {
	int GL_MAX_VERTEX_STREAMS_ATI = 0x876B;
	int GL_VERTEX_SOURCE_ATI = 0x876C;
	int GL_VERTEX_STREAM0_ATI = 0x876D;
	int GL_VERTEX_STREAM1_ATI = 0x876E;
	int GL_VERTEX_STREAM2_ATI = 0x876F;
	int GL_VERTEX_STREAM3_ATI = 0x8770;
	int GL_VERTEX_STREAM4_ATI = 0x8771;
	int GL_VERTEX_STREAM5_ATI = 0x8772;
	int GL_VERTEX_STREAM6_ATI = 0x8773;
	int GL_VERTEX_STREAM7_ATI = 0x8774;

	@NoErrorCheck
	void glVertexStream2fATI(@GLenum int stream, float x, float y);

	@NoErrorCheck
	void glVertexStream2dATI(@GLenum int stream, double x, double y);

	@NoErrorCheck
	void glVertexStream2iATI(@GLenum int stream, int x, int y);

	@NoErrorCheck
	void glVertexStream2sATI(@GLenum int stream, short x, short y);

	@NoErrorCheck
	void glVertexStream3fATI(@GLenum int stream, float x, float y, float z);

	@NoErrorCheck
	void glVertexStream3dATI(@GLenum int stream, double x, double y, double z);

	@NoErrorCheck
	void glVertexStream3iATI(@GLenum int stream, int x, int y, int z);

	@NoErrorCheck
	void glVertexStream3sATI(@GLenum int stream, short x, short y, short z);

	@NoErrorCheck
	void glVertexStream4fATI(@GLenum int stream, float x, float y, float z, float w);

	@NoErrorCheck
	void glVertexStream4dATI(@GLenum int stream, double x, double y, double z, double w);

	@NoErrorCheck
	void glVertexStream4iATI(@GLenum int stream, int x, int y, int z, int w);

	@NoErrorCheck
	void glVertexStream4sATI(@GLenum int stream, short x, short y, short z, short w);

	@NoErrorCheck
	void glNormalStream3bATI(@GLenum int stream, byte x, byte y, byte z);

	@NoErrorCheck
	void glNormalStream3fATI(@GLenum int stream, float x, float y, float z);

	@NoErrorCheck
	void glNormalStream3dATI(@GLenum int stream, double x, double y, double z);

	@NoErrorCheck
	void glNormalStream3iATI(@GLenum int stream, int x, int y, int z);

	@NoErrorCheck
	void glNormalStream3sATI(@GLenum int stream, short x, short y, short z);

	void glClientActiveVertexStreamATI(@GLenum int stream);

	void glVertexBlendEnvfATI(@GLenum int pname, float param);

	void glVertexBlendEnviATI(@GLenum int pname, int param);
}
