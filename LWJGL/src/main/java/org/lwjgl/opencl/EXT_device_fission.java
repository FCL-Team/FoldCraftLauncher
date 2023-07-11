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
import org.lwjgl.util.generator.opencl.cl_int;
import org.lwjgl.util.generator.opencl.cl_uint;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

@CLDeviceExtension
public interface EXT_device_fission {

	/**
	 * Accepted as a property name in the &lt;properties&gt; parameter of
	 * clCreateSubDeviceEXT:
	 */
	int CL_DEVICE_PARTITION_EQUALLY_EXT = 0x4050,
		CL_DEVICE_PARTITION_BY_COUNTS_EXT = 0x4051,
		CL_DEVICE_PARTITION_BY_NAMES_EXT = 0x4052,
		CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN_EXT = 0x4053;

	/**
	 * Accepted as a property name, when accompanying the
	 * CL_DEVICE_PARITION_BY_AFFINITY_DOMAIN_EXT property, in the &lt;properties&gt;
	 * parameter of clCreateSubDeviceEXT:
	 */
	int CL_AFFINITY_DOMAIN_L1_CACHE_EXT = 0x1,
		CL_AFFINITY_DOMAIN_L2_CACHE_EXT = 0x2,
		CL_AFFINITY_DOMAIN_L3_CACHE_EXT = 0x3,
		CL_AFFINITY_DOMAIN_L4_CACHE_EXT = 0x4,
		CL_AFFINITY_DOMAIN_NUMA_EXT = 0x10,
		CL_AFFINITY_DOMAIN_NEXT_FISSIONABLE_EXT = 0x100;

	/**
	 * Accepted as a property being queried in the &lt;param_name&gt; argument of
	 * clGetDeviceInfo:
	 */
	int CL_DEVICE_PARENT_DEVICE_EXT = 0x4054,
		CL_DEVICE_PARITION_TYPES_EXT = 0x4055,
		CL_DEVICE_AFFINITY_DOMAINS_EXT = 0x4056,
		CL_DEVICE_REFERENCE_COUNT_EXT = 0x4057,
		CL_DEVICE_PARTITION_STYLE_EXT = 0x4058;

	/**
	 * Accepted as the property list terminator in the &lt;properties&gt; parameter of
	 * clCreateSubDeviceEXT:
	 */
	int CL_PROPERTIES_LIST_END_EXT = 0x0;

	/**
	 * Accepted as the partition counts list terminator in the &lt;properties&gt;
	 * parameter of clCreateSubDeviceEXT:
	 */
	int CL_PARTITION_BY_COUNTS_LIST_END_EXT = 0x0;

	/**
	 * Accepted as the partition names list terminator in the &lt;properties&gt;
	 * parameter of clCreateSubDeviceEXT:
	 */
	int CL_PARTITION_BY_NAMES_LIST_END_EXT = -1;

	/**
	 * Returned by clCreateSubDevicesEXT when the indicated partition scheme is
	 * supported by the implementation, but the implementation can not further
	 * partition the device in this way.
	 */
	int CL_DEVICE_PARTITION_FAILED_EXT = -1057;

	/**
	 * Returned by clCreateSubDevicesEXT when the total number of compute units
	 * requested exceeds CL_DEVICE_MAX_COMPUTE_UNITS, or the number of compute
	 * units for any one sub-device is less than 1.
	 */
	int CL_INVALID_PARTITION_COUNT_EXT = -1058;

	/**
	 * Returned by clCreateSubDevicesEXT when a compute unit name appearing in a
	 * name list following CL_DEVICE_PARTITION_BY_NAMES_EXT is not in range.
	 */
	int CL_INVALID_PARTITION_NAME_EXT = -1059;

	@Code(javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS ) device.retain();")
	@cl_int
	int clRetainDeviceEXT(@PointerWrapper("cl_device_id") CLDevice device);

	/**
	 * Warning: LWJGL will not automatically release any objects associated with sub-devices.
	 * The user is responsible for tracking and releasing everything prior to calling this method.
	 *
	 * @param device the parent CLDevice
	 *
	 * @return the error code
	 */
	@Code(
		javaBeforeNative = "\t\tAPIUtil.releaseObjects(device);",
		javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS ) device.release();"
	)
	@cl_int
	int clReleaseDeviceEXT(@PointerWrapper("cl_device_id") CLDevice device);

	@Code(javaAfterNative = "\t\tif ( __result == CL10.CL_SUCCESS && out_devices != null ) in_device.registerSubCLDevices(out_devices);")
	@cl_int
	int clCreateSubDevicesEXT(
		@PointerWrapper("cl_device_id") CLDevice in_device,
		@NullTerminated @Const @NativeType("cl_device_partition_property_ext") LongBuffer properties,
		@AutoSize(value = "out_devices", canBeNull = true) @cl_uint int num_entries,
		@OutParameter @Check(canBeNull = true) @NativeType("cl_device_id") PointerBuffer out_devices,
		@OutParameter @Check(value = "1", canBeNull = true) @cl_uint IntBuffer num_devices);

}