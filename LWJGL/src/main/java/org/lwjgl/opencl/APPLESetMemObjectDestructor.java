/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class APPLESetMemObjectDestructor {

	private APPLESetMemObjectDestructor() {}

	public static int clSetMemObjectDestructorAPPLE(CLMem memobj, CLMemObjectDestructorCallback pfn_notify) {
		long function_pointer = CLCapabilities.clSetMemObjectDestructorAPPLE;
		BufferChecks.checkFunctionAddress(function_pointer);
		long user_data = CallbackUtil.createGlobalRef(pfn_notify);
		int __result = 0;
		try {
			__result = nclSetMemObjectDestructorAPPLE(memobj.getPointer(), pfn_notify.getPointer(), user_data, function_pointer);
			return __result;
		} finally {
			CallbackUtil.checkCallback(__result, user_data);
		}
	}
	static native int nclSetMemObjectDestructorAPPLE(long memobj, long pfn_notify, long user_data, long function_pointer);
}
