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

import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.Check;
import org.lwjgl.util.generator.OutParameter;
import org.lwjgl.util.generator.StripPostfix;
import org.lwjgl.util.generator.opengl.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

public interface NV_present_video {

	/**
	 * Accepted by the &lt;type&gt; parameter of PresentFrameKeyedNV and
	 * PresentFrameDualFillNV:
	 */
	int GL_FRAME_NV = 0x8E26,
		FIELDS_NV   = 0x8E27;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVideoivNV, GetVideouivNV,
	 * GetVideoi64vNV, GetVideoui64vNV:
	 */
	int GL_CURRENT_TIME_NV     = 0x8E28,
		GL_NUM_FILL_STREAMS_NV = 0x8E29;

	/** Accepted by the &lt;target&gt; parameter of GetQueryiv: */
	int GL_PRESENT_TIME_NV     = 0x8E2A,
		GL_PRESENT_DURATION_NV = 0x8E2B;

	/** Accepted by the &lt;attribute&gt; parameter of NVPresentVideoUtil.glQueryContextNV: */
	int GL_NUM_VIDEO_SLOTS_NV = 0x20F0; // GLX_NUM_VIDEO_SLOTS_NV & WGL_NUM_VIDEO_SLOTS_NV

	void glPresentFrameKeyedNV(@GLuint int video_slot,
	                           @GLuint64EXT long minPresentTime,
	                           @GLuint int beginPresentTimeId,
	                           @GLuint int presentDurationId,
	                           @GLenum int type,
	                           @GLenum int target0, @GLuint int fill0, @GLuint int key0,
	                           @GLenum int target1, @GLuint int fill1, @GLuint int key1);

	void glPresentFrameDualFillNV(@GLuint int video_slot,
	                              @GLuint64EXT long minPresentTime,
	                              @GLuint int beginPresentTimeId,
	                              @GLuint int presentDurationId,
	                              @GLenum int type,
	                              @GLenum int target0, @GLuint int fill0,
	                              @GLenum int target1, @GLuint int fill1,
	                              @GLenum int target2, @GLuint int fill2,
	                              @GLenum int target3, @GLuint int fill3);

	@StripPostfix("params")
	void glGetVideoivNV(@GLuint int video_slot, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetVideoivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoivNV2(@GLuint int video_slot, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetVideouivNV(@GLuint int video_slot, @GLenum int pname, @OutParameter @Check("1") @GLuint IntBuffer params);

	@Alternate("glGetVideouivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideouivNV2(@GLuint int video_slot, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

	@StripPostfix("params")
	void glGetVideoi64vNV(@GLuint int video_slot, @GLenum int pname, @OutParameter @Check("1") @GLint64EXT LongBuffer params);

	@Alternate("glGetVideoi64vNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoi64vNV2(@GLuint int video_slot, @GLenum int pname, @OutParameter @GLint64EXT LongBuffer params);

	@StripPostfix("params")
	void glGetVideoui64vNV(@GLuint int video_slot, @GLenum int pname, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	@Alternate("glGetVideoui64vNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoui64vNV2(@GLuint int video_slot, @GLenum int pname, @OutParameter @GLuint64EXT LongBuffer params);

}