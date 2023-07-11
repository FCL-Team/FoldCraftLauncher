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
import org.lwjgl.util.generator.PointerWrapper;
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.*;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

@Extension(postfix = "")
public interface ARB_sync {

	/** Accepted as the &lt;pname&gt; parameter of GetInteger64v: */
	int GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111;

	/** Accepted as the &lt;pname&gt; parameter of GetSynciv: */
	int GL_OBJECT_TYPE = 0x9112;
	int GL_SYNC_CONDITION = 0x9113;
	int GL_SYNC_STATUS = 0x9114;
	int GL_SYNC_FLAGS = 0x9115;

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE: */
	int GL_SYNC_FENCE = 0x9116;

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_CONDITION: */
	int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_STATUS: */
	int GL_UNSIGNALED = 0x9118;
	int GL_SIGNALED = 0x9119;

	/** Accepted in the &lt;flags&gt; parameter of ClientWaitSync: */
	int GL_SYNC_FLUSH_COMMANDS_BIT = 0x00000001;

	/** Accepted in the &lt;timeout&gt; parameter of WaitSync: */
	long GL_TIMEOUT_IGNORED = 0xFFFFFFFFFFFFFFFFl;

	/** Returned by ClientWaitSync: */
	int GL_ALREADY_SIGNALED = 0x911A;
	int GL_TIMEOUT_EXPIRED = 0x911B;
	int GL_CONDITION_SATISFIED = 0x911C;
	int GL_WAIT_FAILED = 0x911D;

	@Reuse("GL32")
	@PointerWrapper("GLsync")
	GLSync glFenceSync(@GLenum int condition, @GLbitfield int flags);

	@Reuse("GL32")
	boolean glIsSync(@PointerWrapper("GLsync") GLSync sync);

	@Reuse("GL32")
	void glDeleteSync(@PointerWrapper("GLsync") GLSync sync);

	@Reuse("GL32")
	@GLenum
	int glClientWaitSync(@PointerWrapper("GLsync") GLSync sync, @GLbitfield int flags, @GLuint64 long timeout);

	@Reuse("GL32")
	void glWaitSync(@PointerWrapper("GLsync") GLSync sync, @GLbitfield int flags, @GLuint64 long timeout);

	@Reuse("GL32")
	@StripPostfix("params")
	void glGetInteger64v(@GLenum int pname, @OutParameter @Check("1") @GLint64 LongBuffer params);

	@Reuse("GL32")
	@Alternate("glGetInteger64v")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetInteger64v2(@GLenum int pname, @OutParameter @GLint64 LongBuffer params);

	@Reuse("GL32")
	@StripPostfix("values")
	void glGetSynciv(@PointerWrapper("GLsync") GLSync sync, @GLenum int pname, @AutoSize("values") @GLsizei int bufSize,
	                 @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                 @OutParameter IntBuffer values);

	/** @deprecated Will be removed in 3.0. Use {@link #glGetSynci} instead. */
	@Alternate("glGetSynciv")
	@GLreturn("values")
	@StripPostfix("values")
	@Reuse(value = "GL32", method = "glGetSynci")
	@Deprecated
	void glGetSynciv2(@PointerWrapper("GLsync") GLSync sync, @GLenum int pname, @Constant("1") @GLsizei int bufSize,
	                  @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                  @OutParameter IntBuffer values);

	@Reuse("GL32")
	@Alternate("glGetSynciv")
	@GLreturn("values")
	@StripPostfix(value = "values", hasPostfix = false)
	void glGetSynciv3(@PointerWrapper("GLsync") GLSync sync, @GLenum int pname, @Constant("1") @GLsizei int bufSize,
	                  @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                  @OutParameter IntBuffer values);
}