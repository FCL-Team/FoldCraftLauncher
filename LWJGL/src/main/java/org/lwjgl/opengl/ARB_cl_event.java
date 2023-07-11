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

import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.util.generator.Extension;
import org.lwjgl.util.generator.Imports;
import org.lwjgl.util.generator.PointerWrapper;
import org.lwjgl.util.generator.opengl.GLbitfield;

@Imports("org.lwjgl.opencl.*")
@Extension(postfix = "ARB", className = "ARBCLEvent")
public interface ARB_cl_event {

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE. */
	int GL_SYNC_CL_EVENT_ARB = 0x8240;

	/** Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_CONDITION. */
	int GL_SYNC_CL_EVENT_COMPLETE_ARB = 0x8241;

	@PointerWrapper("GLsync")
	GLSync glCreateSyncFromCLeventARB(@PointerWrapper("cl_context") CLContext context, @PointerWrapper("cl_event") CLEvent event, @GLbitfield int flags);

}