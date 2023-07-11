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

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

public interface NV_video_capture {

	/**
	 * Accepted by the &lt;target&gt; parameters of BindBufferARB, BufferDataARB,
	 * BufferSubDataARB, MapBufferARB, UnmapBufferARB, GetBufferSubDataARB,
	 * GetBufferParameterivARB, and GetBufferPointervARB:
	 */
	int GL_VIDEO_BUFFER_NV = 0x9020;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_VIDEO_BUFFER_BINDING_NV = 0x9021;

	/**
	 * Accepted by the &lt;frame_region&gt; parameter of
	 * BindVideoCaptureStreamBufferNV, and BindVideoCaptureStreamTextureNV:
	 */
	int GL_FIELD_UPPER_NV = 0x9022,
		GL_FIELD_LOWER_NV = 0x9023;

	/** Accepted by the &lt;pname&gt; parameter of GetVideoCaptureivNV: */
	int GL_NUM_VIDEO_CAPTURE_STREAMS_NV        = 0x9024,
		GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV = 0x9025;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetVideoCaptureStream{i,f,d}vNV:
	 */
	int GL_LAST_VIDEO_CAPTURE_STATUS_NV        = 0x9027,
		GL_VIDEO_BUFFER_PITCH_NV               = 0x9028,
		GL_VIDEO_CAPTURE_FRAME_WIDTH_NV        = 0x9038,
		GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV       = 0x9039,
		GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV = 0x903A,
		GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV = 0x903B,
		GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV   = 0x9026;

	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetVideoCaptureStream{i,f,d}vNV and as the &lt;pname&gt; parameter of
	 * VideoCaptureStreamParameter{i,f,d}vNV:
	 */
	int GL_VIDEO_COLOR_CONVERSION_MATRIX_NV = 0x9029,
		GL_VIDEO_COLOR_CONVERSION_MAX_NV    = 0x902A,
		GL_VIDEO_COLOR_CONVERSION_MIN_NV    = 0x902B,
		GL_VIDEO_COLOR_CONVERSION_OFFSET_NV = 0x902C,
		GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV  = 0x902D,
		GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV  = 0x903C;
	/** Returned by VideoCaptureNV: */
	int GL_PARTIAL_SUCCESS_NV = 0x902E;

	/**
	 * Returned by VideoCaptureNV and GetVideoCaptureStream{i,f,d}vNV
	 * when &lt;pname&gt; is LAST_VIDEO_CAPTURE_STATUS_NV:
	 */
	int GL_SUCCESS_NV = 0x902F,
		GL_FAILURE_NV = 0x9030;

	/**
	 * Accepted in the &lt;params&gt; parameter of
	 * VideoCaptureStreamParameter{i,f,d}vNV when &lt;pname&gt; is
	 * VIDEO_BUFFER_INTERNAL_FORMAT_NV and returned by
	 * GetVideoCaptureStream{i,f,d}vNV when &lt;pname&gt; is
	 * VIDEO_BUFFER_INTERNAL_FORMAT_NV:
	 */
	int GL_YCBYCR8_422_NV                           = 0x9031,
		GL_YCBAYCR8A_4224_NV                        = 0x9032,
		GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV            = 0x9033,
		GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV = 0x9034,
		GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV            = 0x9035,
		GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV = 0x9036,
		GL_Z4Y12Z4CB12Z4CR12_444_NV                 = 0x9037;

	/*
	 * Accepted in the attribute list of the GLX reply to the
	 * glXEnumerateVideoCaptureDevicesNV command:
	 */
	/*int GLX_DEVICE_ID_NV = 0x20CD;*/

	/** Accepted by the &lt;attribute&gt; parameter of NVPresentVideoUtil.glQueryContextNV: */
	int GL_NUM_VIDEO_CAPTURE_SLOTS_NV = 0x20CF;

	/**
	 * Accepted by the &lt;attribute&gt; parameter of
	 * glQueryVideoCaptureDeviceNV:
	 */
	int GL_UNIQUE_ID_NV = 0x20CE;

	void glBeginVideoCaptureNV(@GLuint int video_capture_slot);

	void glBindVideoCaptureStreamBufferNV(@GLuint int video_capture_slot,
	                                      @GLuint int stream, @GLenum int frame_region,
	                                      @GLintptrARB long offset);

	void glBindVideoCaptureStreamTextureNV(@GLuint int video_capture_slot,
	                                       @GLuint int stream, @GLenum int frame_region,
	                                       @GLenum int target, @GLuint int texture);

	void glEndVideoCaptureNV(@GLuint int video_capture_slot);

	@StripPostfix("params")
	void glGetVideoCaptureivNV(@GLuint int video_capture_slot, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetVideoCaptureivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoCaptureivNV2(@GLuint int video_capture_slot, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetVideoCaptureStreamivNV(@GLuint int video_capture_slot,
	                                 @GLuint int stream, @GLenum int pname,
	                                 @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetVideoCaptureStreamivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoCaptureStreamivNV2(@GLuint int video_capture_slot,
	                                  @GLuint int stream, @GLenum int pname,
	                                  @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetVideoCaptureStreamfvNV(@GLuint int video_capture_slot,
	                                 @GLuint int stream, @GLenum int pname,
	                                 @OutParameter @Check("1") FloatBuffer params);

	@Alternate("glGetVideoCaptureStreamfvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoCaptureStreamfvNV2(@GLuint int video_capture_slot,
	                                  @GLuint int stream, @GLenum int pname,
	                                  @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetVideoCaptureStreamdvNV(@GLuint int video_capture_slot,
	                                 @GLuint int stream, @GLenum int pname,
	                                 @OutParameter @Check("1") DoubleBuffer params);

	@Alternate("glGetVideoCaptureStreamdvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetVideoCaptureStreamdvNV2(@GLuint int video_capture_slot,
	                                  @GLuint int stream, @GLenum int pname,
	                                  @OutParameter DoubleBuffer params);

	@GLenum
	int glVideoCaptureNV(@GLuint int video_capture_slot,
	                     @OutParameter @Check("1") @GLuint IntBuffer sequence_num,
	                     @OutParameter @Check("1") @GLuint64EXT LongBuffer capture_time);

	@StripPostfix("params")
	void glVideoCaptureStreamParameterivNV(@GLuint int video_capture_slot, @GLuint int stream, @GLenum int pname, @Const @Check("16") IntBuffer params);

	@StripPostfix("params")
	void glVideoCaptureStreamParameterfvNV(@GLuint int video_capture_slot, @GLuint int stream, @GLenum int pname, @Const @Check("16") FloatBuffer params);

	@StripPostfix("params")
	void glVideoCaptureStreamParameterdvNV(@GLuint int video_capture_slot, @GLuint int stream, @GLenum int pname, @Const @Check("16") DoubleBuffer params);

}