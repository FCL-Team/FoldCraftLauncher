/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBComputeShader {

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned in the
	 *  &lt;params&gt; parameter by GetShaderiv:
	 */
	public static final int GL_COMPUTE_SHADER = 0x91B9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 0x91BB,
		GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91BC,
		GL_MAX_COMPUTE_IMAGE_UNIFORMS = 0x91BD,
		GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262,
		GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263,
		GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264,
		GL_MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265,
		GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266,
		GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 0x90EB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 *  GetFloati_v, GetDoublei_v and GetInteger64i_v:
	 */
	public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE,
		GL_MAX_COMPUTE_WORK_GROUP_SIZE = 0x91BF;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: 
	 */
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_DISPATCH_INDIRECT_BUFFER = 0x90EE;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF;

	/**
	 * Accepted by the &lt;stages&gt; parameter of UseProgramStages: 
	 */
	public static final int GL_COMPUTE_SHADER_BIT = 0x20;

	private ARBComputeShader() {}

	public static void glDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
		GL43.glDispatchCompute(num_groups_x, num_groups_y, num_groups_z);
	}

	public static void glDispatchComputeIndirect(long indirect) {
		GL43.glDispatchComputeIndirect(indirect);
	}
}
