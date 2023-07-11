/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVSystemTime {

	private NVSystemTime() {}

	static native void initNativeStubs() throws LWJGLException;

	public static long glGetSystemTimeFrequencyNV() {
		long __result = nglGetSystemTimeFrequencyNV();
		return __result;
	}
	static native long nglGetSystemTimeFrequencyNV();

	public static long glGetSystemTimeNV() {
		long __result = nglGetSystemTimeNV();
		return __result;
	}
	static native long nglGetSystemTimeNV();
}
