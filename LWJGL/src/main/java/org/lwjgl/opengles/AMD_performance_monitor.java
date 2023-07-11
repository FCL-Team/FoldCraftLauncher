/*
 * Copyright (c) 2002-2011 LWJGL Project
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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface AMD_performance_monitor {

	/** Accepted by the &lt;pame&gt; parameter of GetPerfMonitorCounterInfoAMD */
	int GL_COUNTER_TYPE_AMD  = 0x8BC0;
	int GL_COUNTER_RANGE_AMD = 0x8BC1;

	/**
	 * Returned as a valid value in &lt;data&gt; parameter of
	 * GetPerfMonitorCounterInfoAMD if &lt;pname&gt; = COUNTER_TYPE_AMD
	 */
	int GL_UNSIGNED_INT64_AMD = 0x8BC2;
	int GL_PERCENTAGE_AMD     = 0x8BC3;

	/** Accepted by the &lt;pname&gt; parameter of GetPerfMonitorCounterDataAMD */

	int GL_PERFMON_RESULT_AVAILABLE_AMD = 0x8BC4;
	int GL_PERFMON_RESULT_SIZE_AMD      = 0x8BC5;
	int GL_PERFMON_RESULT_AMD           = 0x8BC6;

	void glGetPerfMonitorGroupsAMD(@OutParameter @Check(value = "1", canBeNull = true) @GLint IntBuffer numGroups,
	                               @AutoSize("groups") @GLsizei int groupsSize, @GLuint IntBuffer groups);

	void glGetPerfMonitorCountersAMD(@GLuint int group,
	                                 @OutParameter @Check(value = "1") @GLint IntBuffer numCounters,
	                                 @OutParameter @Check(value = "1") @GLint IntBuffer maxActiveCounters,
	                                 @AutoSize("counters") @GLsizei int countersSize,
	                                 @GLuint IntBuffer counters);

	void glGetPerfMonitorGroupStringAMD(@GLuint int group,
	                                    @AutoSize("groupString") @GLsizei int bufSize,
	                                    @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                                    @OutParameter @GLchar ByteBuffer groupString);

	@Alternate("glGetPerfMonitorGroupStringAMD")
	@GLreturn(value = "groupString", maxLength = "bufSize")
	void glGetPerfMonitorGroupStringAMD2(@GLuint int group, @GLsizei int bufSize,
	                                     @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(groupString_length)") IntBuffer length,
	                                     @OutParameter @GLchar ByteBuffer groupString);

	void glGetPerfMonitorCounterStringAMD(@GLuint int group, @GLuint int counter, @AutoSize("counterString") @GLsizei int bufSize,
	                                      @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                                      @OutParameter @GLchar ByteBuffer counterString);

	@Alternate("glGetPerfMonitorCounterStringAMD")
	@GLreturn(value = "counterString", maxLength = "bufSize")
	void glGetPerfMonitorCounterStringAMD2(@GLuint int group, @GLuint int counter, @GLsizei int bufSize,
	                                       @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(counterString_length)") IntBuffer length,
	                                       @OutParameter @GLchar ByteBuffer counterString);

	void glGetPerfMonitorCounterInfoAMD(@GLuint int group, @GLuint int counter, @GLenum int pname, @Check(value = "16") @GLvoid ByteBuffer data);

	void glGenPerfMonitorsAMD(@AutoSize("monitors") @GLsizei int n, @OutParameter @GLuint IntBuffer monitors);

	@Alternate("glGenPerfMonitorsAMD")
	@GLreturn("monitors")
	void glGenPerfMonitorsAMD2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer monitors);

	void glDeletePerfMonitorsAMD(@AutoSize("monitors") @GLsizei int n, @GLuint IntBuffer monitors);

	@Alternate("glDeletePerfMonitorsAMD")
	void glDeletePerfMonitorsAMD(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(monitor)", keepParam = true) int monitor);

	void glSelectPerfMonitorCountersAMD(@GLuint int monitor, boolean enable, @GLuint int group, @AutoSize("counterList") int numCounters, @GLuint IntBuffer counterList);

	@Alternate("glSelectPerfMonitorCountersAMD")
	void glSelectPerfMonitorCountersAMD(@GLuint int monitor, boolean enable, @GLuint int group, @Constant("1") int numCounters, @Constant(value = "APIUtil.getInt(counter)", keepParam = true) int counter);

	void glBeginPerfMonitorAMD(@GLuint int monitor);

	void glEndPerfMonitorAMD(@GLuint int monitor);

	void glGetPerfMonitorCounterDataAMD(@GLuint int monitor, @GLenum int pname, @AutoSize("data") @GLsizei int dataSize,
	                                    @OutParameter @GLuint IntBuffer data,
	                                    @OutParameter @GLint @Check(value = "1", canBeNull = true) IntBuffer bytesWritten);

	@Alternate("glGetPerfMonitorCounterDataAMD")
	@GLreturn("data")
	void glGetPerfMonitorCounterDataAMD2(@GLuint int monitor, @GLenum int pname, @Constant("4") @GLsizei int dataSize,
	                                     @OutParameter @GLuint IntBuffer data,
	                                     @OutParameter @GLint @Constant("0L") IntBuffer bytesWritten);

}