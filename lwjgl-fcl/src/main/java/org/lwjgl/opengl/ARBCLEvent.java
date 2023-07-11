/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;
import org.lwjgl.opencl.*;

public final class ARBCLEvent {

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE. 
	 */
	public static final int GL_SYNC_CL_EVENT_ARB = 0x8240;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_CONDITION. 
	 */
	public static final int GL_SYNC_CL_EVENT_COMPLETE_ARB = 0x8241;

	private ARBCLEvent() {}

	public static GLSync glCreateSyncFromCLeventARB(CLContext context, CLEvent event, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateSyncFromCLeventARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLSync __result = new GLSync(nglCreateSyncFromCLeventARB(context.getPointer(), event.getPointer(), flags, function_pointer));
		return __result;
	}
	static native long nglCreateSyncFromCLeventARB(long context, long event, int flags, long function_pointer);
}
