/*
 * Copyright (c) 2002-2010 LWJGL Project
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
package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opencl.CLDeviceExtension;
import org.lwjgl.util.generator.opencl.cl_bitfield;
import org.lwjgl.util.generator.opencl.cl_int;
import org.lwjgl.util.generator.opencl.cl_uint;

@CLDeviceExtension
public interface EXT_migrate_memobject {

	/**
	 * Besides a value of zero, the following cl_mem_migration_flags_ext values are
	 * allowed:
	 */
	int CL_MIGRATE_MEM_OBJECT_HOST_EXT = 0x1;

	/**
	 * Returned in the &lt;param_value&gt; parameter of the clGetEventInfo when
	 * &lt;param_name&gt; is CL_EVENT_COMMAND_TYPE:
	 */
	int CL_COMMAND_MIGRATE_MEM_OBJECT_EXT = 0x4040;

	@Code(javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);")
	@cl_int
	int clEnqueueMigrateMemObjectEXT(@PointerWrapper("cl_command_queue") CLCommandQueue command_queue,
	                                 @AutoSize("mem_objects") @cl_uint int num_mem_objects,
	                                 @Check("1") @Const @NativeType("cl_mem") PointerBuffer mem_objects,
	                                 @cl_bitfield @NativeType("cl_mem_migration_flags_ext") long flags,
	                                 @AutoSize(value = "event_wait_list", canBeNull = true) @cl_uint int num_events_in_wait_list,
	                                 @Check(canBeNull = true) @Const @NativeType("cl_event") PointerBuffer event_wait_list,
	                                 @OutParameter @Check(value = "1", canBeNull = true) @NativeType("cl_event") PointerBuffer event);

	@Alternate("clEnqueueMigrateMemObjectEXT")
	@Code(javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS ) command_queue.registerCLEvent(event);")
	@cl_int
	int clEnqueueMigrateMemObjectEXT(@PointerWrapper("cl_command_queue") CLCommandQueue command_queue,
	                                 @Constant("1") @cl_uint int num_mem_objects,
	                                 @Constant(value = "APIUtil.getPointer(mem_object)", keepParam = true) CLMem mem_object,
	                                 @cl_bitfield @NativeType("cl_mem_migration_flags_ext") long flags,
	                                 @AutoSize(value = "event_wait_list", canBeNull = true) @cl_uint int num_events_in_wait_list,
	                                 @Check(canBeNull = true) @Const @NativeType("cl_event") PointerBuffer event_wait_list,
	                                 @OutParameter @Check(value = "1", canBeNull = true) @NativeType("cl_event") PointerBuffer event);

}