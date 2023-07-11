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

import org.lwjgl.opengl.GLSync;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

public interface APPLE_sync {

	/** Accepted as the &lt;pname&gt; parameter of GetInteger64vAPPLE: */
	int GL_MAX_SERVER_WAIT_TIMEOUT_APPLE = 0x9111;

	/** Accepted as the &lt;pname&gt; parameter of GetSyncivAPPLE: */
	int GL_OBJECT_TYPE_APPLE = 0x9112,
		SYNC_CONDITION_APPLE = 0x9113,
		SYNC_STATUS_APPLE    = 0x9114,
		SYNC_FLAGS_APPLE     = 0x9115;

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE_APPLE: */
	int GL_SYNC_FENCE_APPLE = 0x9116;

	/** Returned in &lt;values&gt; for GetSyncivAPPLE &lt;pname&gt; SYNC_CONDITION_APPLE: */
	int GL_SYNC_GPU_COMMANDS_COMPLETE_APPLE = 0x9117;

	/** Returned in &lt;values&gt; for GetSyncivAPPLE &lt;pname&gt; SYNC_STATUS_APPLE: */
	int GL_UNSIGNALED_APPLE = 0x9118,
		SIGNALED_APPLE      = 0x9119;

	/** Accepted in the &lt;flags&gt; parameter of ClientWaitSyncAPPLE: */
	int GL_SYNC_FLUSH_COMMANDS_BIT_APPLE = 0x00000001;

	/** Accepted in the &lt;timeout&gt; parameter of WaitSyncAPPLE: */
	long GL_TIMEOUT_IGNORED_APPLE = 0xFFFFFFFFFFFFFFFFl;

	/** Returned by ClientWaitSyncAPPLE: */
	int GL_ALREADY_SIGNALED_APPLE = 0x911A,
		TIMEOUT_EXPIRED_APPLE     = 0x911B,
		CONDITION_SATISFIED_APPLE = 0x911C,
		WAIT_FAILED_APPLE         = 0x911D;

	/**
	 * Accepted by the &lt;type&gt; parameter of LabelObjectEXT and
	 * GetObjectLabelEXT:
	 */
	int GL_SYNC_OBJECT_APPLE = 0x8A53;

	@PointerWrapper("GLsync")
	GLSync glFenceSyncAPPLE(@GLenum int condition, @GLbitfield int flags);

	boolean glIsSyncAPPLE(@PointerWrapper("GLsync") GLSync sync);

	void glDeleteSyncAPPLE(@PointerWrapper("GLsync") GLSync sync);

	@GLenum
	int glClientWaitSyncAPPLE(@PointerWrapper("GLsync") GLSync sync, @GLbitfield int flags, @GLuint64 long timeout);

	void glWaitSyncAPPLE(@PointerWrapper("GLsync") GLSync sync, @GLbitfield int flags, @GLuint64 long timeout);

	@StripPostfix("params")
	void glGetInteger64vAPPLE(@GLenum int pname, @OutParameter @Check("1") @GLint64 LongBuffer params);

	@Alternate("glGetInteger64vAPPLE")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetInteger64vAPPLE2(@GLenum int pname, @OutParameter @GLint64 LongBuffer params);

	@StripPostfix("values")
	void glGetSyncivAPPLE(@PointerWrapper("GLsync") GLSync sync, @GLenum int pname, @AutoSize("values") @GLsizei int bufSize,
	                      @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                      @OutParameter IntBuffer values);

	@Alternate("glGetSyncivAPPLE")
	@GLreturn("values")
	@StripPostfix(value = "values", hasPostfix = false)
	void glGetSyncivAPPLE2(@PointerWrapper("GLsync") GLSync sync, @GLenum int pname, @Constant("1") @GLsizei int bufSize,
	                       @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                       @OutParameter IntBuffer values);

}