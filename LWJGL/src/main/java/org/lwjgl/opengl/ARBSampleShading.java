/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBSampleShading {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_SAMPLE_SHADING_ARB = 0x8C36;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev,
	 *  GetIntegerv, and GetFloatv:
	 */
	public static final int GL_MIN_SAMPLE_SHADING_VALUE_ARB = 0x8C37;

	private ARBSampleShading() {}

	public static void glMinSampleShadingARB(float value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMinSampleShadingARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMinSampleShadingARB(value, function_pointer);
	}
	static native void nglMinSampleShadingARB(float value, long function_pointer);
}
