/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBComputeVariableGroupSize {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_INVOCATIONS_ARB = 0x9344,
		GL_MAX_COMPUTE_FIXED_GROUP_INVOCATIONS_ARB = 0x90EB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 *  GetFloati_v, GetDoublei_v and GetInteger64i_v:
	 */
	public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_SIZE_ARB = 0x9345,
		GL_MAX_COMPUTE_FIXED_GROUP_SIZE_ARB = 0x91BF;

	private ARBComputeVariableGroupSize() {}

	public static void glDispatchComputeGroupSizeARB(int num_groups_x, int num_groups_y, int num_groups_z, int group_size_x, int group_size_y, int group_size_z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDispatchComputeGroupSizeARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDispatchComputeGroupSizeARB(num_groups_x, num_groups_y, num_groups_z, group_size_x, group_size_y, group_size_z, function_pointer);
	}
	static native void nglDispatchComputeGroupSizeARB(int num_groups_x, int num_groups_y, int num_groups_z, int group_size_x, int group_size_y, int group_size_z, long function_pointer);
}
