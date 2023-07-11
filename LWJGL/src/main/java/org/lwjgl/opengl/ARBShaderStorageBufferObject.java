/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBShaderStorageBufferObject {

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetIntegeri_v,
	 *  GetBooleanv, GetInteger64v, GetFloatv, GetDoublev, GetBooleani_v,
	 *  GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 *  GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_SHADER_STORAGE_BUFFER_START = 0x90D4,
		GL_SHADER_STORAGE_BUFFER_SIZE = 0x90D5;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D6,
		GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90D7,
		GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90D8,
		GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9,
		GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA,
		GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB,
		GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC,
		GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD,
		GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE,
		GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF;

	/**
	 * Accepted in the &lt;barriers&gt; bitfield in glMemoryBarrier: 
	 */
	public static final int GL_SHADER_STORAGE_BARRIER_BIT = 0x2000;

	/**
	 *  Alias for the existing token
	 *  MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS:
	 */
	public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39;

	private ARBShaderStorageBufferObject() {}

	public static void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
		GL43.glShaderStorageBlockBinding(program, storageBlockIndex, storageBlockBinding);
	}
}
