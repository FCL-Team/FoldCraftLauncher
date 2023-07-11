/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTDeviceFission {

	/**
	 *  Accepted as a property name in the &lt;properties&gt; parameter of
	 *  clCreateSubDeviceEXT:
	 */
	public static final int CL_DEVICE_PARTITION_EQUALLY_EXT = 0x4050,
		CL_DEVICE_PARTITION_BY_COUNTS_EXT = 0x4051,
		CL_DEVICE_PARTITION_BY_NAMES_EXT = 0x4052,
		CL_DEVICE_PARTITION_BY_AFFINITY_DOMAIN_EXT = 0x4053;

	/**
	 *  Accepted as a property name, when accompanying the
	 *  CL_DEVICE_PARITION_BY_AFFINITY_DOMAIN_EXT property, in the &lt;properties&gt;
	 *  parameter of clCreateSubDeviceEXT:
	 */
	public static final int CL_AFFINITY_DOMAIN_L1_CACHE_EXT = 0x1,
		CL_AFFINITY_DOMAIN_L2_CACHE_EXT = 0x2,
		CL_AFFINITY_DOMAIN_L3_CACHE_EXT = 0x3,
		CL_AFFINITY_DOMAIN_L4_CACHE_EXT = 0x4,
		CL_AFFINITY_DOMAIN_NUMA_EXT = 0x10,
		CL_AFFINITY_DOMAIN_NEXT_FISSIONABLE_EXT = 0x100;

	/**
	 *  Accepted as a property being queried in the &lt;param_name&gt; argument of
	 *  clGetDeviceInfo:
	 */
	public static final int CL_DEVICE_PARENT_DEVICE_EXT = 0x4054,
		CL_DEVICE_PARITION_TYPES_EXT = 0x4055,
		CL_DEVICE_AFFINITY_DOMAINS_EXT = 0x4056,
		CL_DEVICE_REFERENCE_COUNT_EXT = 0x4057,
		CL_DEVICE_PARTITION_STYLE_EXT = 0x4058;

	/**
	 *  Accepted as the property list terminator in the &lt;properties&gt; parameter of
	 *  clCreateSubDeviceEXT:
	 */
	public static final int CL_PROPERTIES_LIST_END_EXT = 0x0;

	/**
	 *  Accepted as the partition counts list terminator in the &lt;properties&gt;
	 *  parameter of clCreateSubDeviceEXT:
	 */
	public static final int CL_PARTITION_BY_COUNTS_LIST_END_EXT = 0x0;

	/**
	 *  Accepted as the partition names list terminator in the &lt;properties&gt;
	 *  parameter of clCreateSubDeviceEXT:
	 */
	public static final int CL_PARTITION_BY_NAMES_LIST_END_EXT = 0xFFFFFFFF;

	/**
	 *  Returned by clCreateSubDevicesEXT when the indicated partition scheme is
	 *  supported by the implementation, but the implementation can not further
	 *  partition the device in this way.
	 */
	public static final int CL_DEVICE_PARTITION_FAILED_EXT = 0xFFFFFBDF;

	/**
	 *  Returned by clCreateSubDevicesEXT when the total number of compute units
	 *  requested exceeds CL_DEVICE_MAX_COMPUTE_UNITS, or the number of compute
	 *  units for any one sub-device is less than 1.
	 */
	public static final int CL_INVALID_PARTITION_COUNT_EXT = 0xFFFFFBDE;

	/**
	 *  Returned by clCreateSubDevicesEXT when a compute unit name appearing in a
	 *  name list following CL_DEVICE_PARTITION_BY_NAMES_EXT is not in range.
	 */
	public static final int CL_INVALID_PARTITION_NAME_EXT = 0xFFFFFBDD;

	private EXTDeviceFission() {}

	public static int clRetainDeviceEXT(CLDevice device) {
		long function_pointer = CLCapabilities.clRetainDeviceEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nclRetainDeviceEXT(device.getPointer(), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) device.retain();
		return __result;
	}
	static native int nclRetainDeviceEXT(long device, long function_pointer);

	/**
	 *  Warning: LWJGL will not automatically release any objects associated with sub-devices.
	 *  The user is responsible for tracking and releasing everything prior to calling this method.
	 * <p>
	 *  @param device the parent CLDevice
	 * <p>
	 *  @return the error code
	 */
	public static int clReleaseDeviceEXT(CLDevice device) {
		long function_pointer = CLCapabilities.clReleaseDeviceEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		APIUtil.releaseObjects(device);
		int __result = nclReleaseDeviceEXT(device.getPointer(), function_pointer);
		if ( __result == CL10.CL_SUCCESS ) device.release();
		return __result;
	}
	static native int nclReleaseDeviceEXT(long device, long function_pointer);

	public static int clCreateSubDevicesEXT(CLDevice in_device, LongBuffer properties, PointerBuffer out_devices, IntBuffer num_devices) {
		long function_pointer = CLCapabilities.clCreateSubDevicesEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(properties);
		BufferChecks.checkNullTerminated(properties);
		if (out_devices != null)
			BufferChecks.checkDirect(out_devices);
		if (num_devices != null)
			BufferChecks.checkBuffer(num_devices, 1);
		int __result = nclCreateSubDevicesEXT(in_device.getPointer(), MemoryUtil.getAddress(properties), (out_devices == null ? 0 : out_devices.remaining()), MemoryUtil.getAddressSafe(out_devices), MemoryUtil.getAddressSafe(num_devices), function_pointer);
		if ( __result == CL10.CL_SUCCESS && out_devices != null ) in_device.registerSubCLDevices(out_devices);
		return __result;
	}
	static native int nclCreateSubDevicesEXT(long in_device, long properties, int out_devices_num_entries, long out_devices, long num_devices, long function_pointer);
}
