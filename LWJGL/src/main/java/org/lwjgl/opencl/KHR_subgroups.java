/*
 * Copyright (c) 2002-2013 LWJGL Project
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
import org.lwjgl.util.generator.opencl.cl_int;
import org.lwjgl.util.generator.opencl.cl_void;
import org.lwjgl.util.generator.opencl.size_t;

import java.nio.ByteBuffer;

@CLDeviceExtension
public interface KHR_subgroups {

	@cl_int
	int clGetKernelSubGroupInfoKHR(@PointerWrapper("cl_kernel") CLKernel kernel,
	                               @PointerWrapper(value = "cl_device_id", canBeNull = true) CLDevice device,
	                               @NativeType("cl_kernel_sub_group_info") int param_name,
	                               @AutoSize(value = "input_value", canBeNull = true) @size_t long input_value_size,
	                               @Check(canBeNull = true) @Const @cl_void ByteBuffer input_value,
	                               @AutoSize(value = "param_value", canBeNull = true) @size_t long param_value_size,
	                               @OutParameter @Check(canBeNull = true) @cl_void ByteBuffer param_value,
	                               @OutParameter @Check(value = "1", canBeNull = true) @NativeType("size_t") PointerBuffer param_value_size_ret);

}