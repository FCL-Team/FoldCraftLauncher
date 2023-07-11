/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class AMDPerformanceMonitor {

	/**
	 * Accepted by the &lt;pame&gt; parameter of GetPerfMonitorCounterInfoAMD 
	 */
	public static final int GL_COUNTER_TYPE_AMD = 0x8BC0,
		GL_COUNTER_RANGE_AMD = 0x8BC1;

	/**
	 *  Returned as a valid value in &lt;data&gt; parameter of
	 *  GetPerfMonitorCounterInfoAMD if &lt;pname&gt; = COUNTER_TYPE_AMD
	 */
	public static final int GL_UNSIGNED_INT64_AMD = 0x8BC2,
		GL_PERCENTAGE_AMD = 0x8BC3;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetPerfMonitorCounterDataAMD 
	 */
	public static final int GL_PERFMON_RESULT_AVAILABLE_AMD = 0x8BC4,
		GL_PERFMON_RESULT_SIZE_AMD = 0x8BC5,
		GL_PERFMON_RESULT_AMD = 0x8BC6;

	private AMDPerformanceMonitor() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGetPerfMonitorGroupsAMD(IntBuffer numGroups, IntBuffer groups) {
		if (numGroups != null)
			BufferChecks.checkBuffer(numGroups, 1);
		BufferChecks.checkDirect(groups);
		nglGetPerfMonitorGroupsAMD(MemoryUtil.getAddressSafe(numGroups), groups.remaining(), MemoryUtil.getAddress(groups));
	}
	static native void nglGetPerfMonitorGroupsAMD(long numGroups, int groups_groupsSize, long groups);

	public static void glGetPerfMonitorCountersAMD(int group, IntBuffer numCounters, IntBuffer maxActiveCounters, IntBuffer counters) {
		BufferChecks.checkBuffer(numCounters, 1);
		BufferChecks.checkBuffer(maxActiveCounters, 1);
		BufferChecks.checkDirect(counters);
		nglGetPerfMonitorCountersAMD(group, MemoryUtil.getAddress(numCounters), MemoryUtil.getAddress(maxActiveCounters), counters.remaining(), MemoryUtil.getAddress(counters));
	}
	static native void nglGetPerfMonitorCountersAMD(int group, long numCounters, long maxActiveCounters, int counters_countersSize, long counters);

	public static void glGetPerfMonitorGroupStringAMD(int group, IntBuffer length, ByteBuffer groupString) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(groupString);
		nglGetPerfMonitorGroupStringAMD(group, groupString.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(groupString));
	}
	static native void nglGetPerfMonitorGroupStringAMD(int group, int groupString_bufSize, long length, long groupString);

	/** Overloads glGetPerfMonitorGroupStringAMD. */
	public static String glGetPerfMonitorGroupStringAMD(int group, int bufSize) {
		IntBuffer groupString_length = APIUtil.getLengths();
		ByteBuffer groupString = APIUtil.getBufferByte(bufSize);
		nglGetPerfMonitorGroupStringAMD(group, bufSize, MemoryUtil.getAddress0(groupString_length), MemoryUtil.getAddress(groupString));
		groupString.limit(groupString_length.get(0));
		return APIUtil.getString(groupString);
	}

	public static void glGetPerfMonitorCounterStringAMD(int group, int counter, IntBuffer length, ByteBuffer counterString) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(counterString);
		nglGetPerfMonitorCounterStringAMD(group, counter, counterString.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(counterString));
	}
	static native void nglGetPerfMonitorCounterStringAMD(int group, int counter, int counterString_bufSize, long length, long counterString);

	/** Overloads glGetPerfMonitorCounterStringAMD. */
	public static String glGetPerfMonitorCounterStringAMD(int group, int counter, int bufSize) {
		IntBuffer counterString_length = APIUtil.getLengths();
		ByteBuffer counterString = APIUtil.getBufferByte(bufSize);
		nglGetPerfMonitorCounterStringAMD(group, counter, bufSize, MemoryUtil.getAddress0(counterString_length), MemoryUtil.getAddress(counterString));
		counterString.limit(counterString_length.get(0));
		return APIUtil.getString(counterString);
	}

	public static void glGetPerfMonitorCounterInfoAMD(int group, int counter, int pname, ByteBuffer data) {
		BufferChecks.checkBuffer(data, 16);
		nglGetPerfMonitorCounterInfoAMD(group, counter, pname, MemoryUtil.getAddress(data));
	}
	static native void nglGetPerfMonitorCounterInfoAMD(int group, int counter, int pname, long data);

	public static void glGenPerfMonitorsAMD(IntBuffer monitors) {
		BufferChecks.checkDirect(monitors);
		nglGenPerfMonitorsAMD(monitors.remaining(), MemoryUtil.getAddress(monitors));
	}
	static native void nglGenPerfMonitorsAMD(int monitors_n, long monitors);

	/** Overloads glGenPerfMonitorsAMD. */
	public static int glGenPerfMonitorsAMD() {
		IntBuffer monitors = APIUtil.getBufferInt();
		nglGenPerfMonitorsAMD(1, MemoryUtil.getAddress(monitors));
		return monitors.get(0);
	}

	public static void glDeletePerfMonitorsAMD(IntBuffer monitors) {
		BufferChecks.checkDirect(monitors);
		nglDeletePerfMonitorsAMD(monitors.remaining(), MemoryUtil.getAddress(monitors));
	}
	static native void nglDeletePerfMonitorsAMD(int monitors_n, long monitors);

	/** Overloads glDeletePerfMonitorsAMD. */
	public static void glDeletePerfMonitorsAMD(int monitor) {
		nglDeletePerfMonitorsAMD(1, APIUtil.getInt(monitor));
	}

	public static void glSelectPerfMonitorCountersAMD(int monitor, boolean enable, int group, IntBuffer counterList) {
		BufferChecks.checkDirect(counterList);
		nglSelectPerfMonitorCountersAMD(monitor, enable, group, counterList.remaining(), MemoryUtil.getAddress(counterList));
	}
	static native void nglSelectPerfMonitorCountersAMD(int monitor, boolean enable, int group, int counterList_numCounters, long counterList);

	/** Overloads glSelectPerfMonitorCountersAMD. */
	public static void glSelectPerfMonitorCountersAMD(int monitor, boolean enable, int group, int counter) {
		nglSelectPerfMonitorCountersAMD(monitor, enable, group, 1, APIUtil.getInt(counter));
	}

	public static void glBeginPerfMonitorAMD(int monitor) {
		nglBeginPerfMonitorAMD(monitor);
	}
	static native void nglBeginPerfMonitorAMD(int monitor);

	public static void glEndPerfMonitorAMD(int monitor) {
		nglEndPerfMonitorAMD(monitor);
	}
	static native void nglEndPerfMonitorAMD(int monitor);

	public static void glGetPerfMonitorCounterDataAMD(int monitor, int pname, IntBuffer data, IntBuffer bytesWritten) {
		BufferChecks.checkDirect(data);
		if (bytesWritten != null)
			BufferChecks.checkBuffer(bytesWritten, 1);
		nglGetPerfMonitorCounterDataAMD(monitor, pname, data.remaining(), MemoryUtil.getAddress(data), MemoryUtil.getAddressSafe(bytesWritten));
	}
	static native void nglGetPerfMonitorCounterDataAMD(int monitor, int pname, int data_dataSize, long data, long bytesWritten);

	/** Overloads glGetPerfMonitorCounterDataAMD. */
	public static int glGetPerfMonitorCounterDataAMD(int monitor, int pname) {
		IntBuffer data = APIUtil.getBufferInt();
		nglGetPerfMonitorCounterDataAMD(monitor, pname, 4, MemoryUtil.getAddress(data), 0L);
		return data.get(0);
	}
}
