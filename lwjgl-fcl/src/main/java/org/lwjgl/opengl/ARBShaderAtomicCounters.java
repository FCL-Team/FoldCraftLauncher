/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBShaderAtomicCounters {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindBufferBase and BindBufferRange: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 *  GetFloati_v, GetDoublei_v, GetInteger64i_v, GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, GetDoublev, and GetActiveAtomicCounterBufferiv:
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_64v: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2,
		GL_ATOMIC_COUNTER_BUFFER_SIZE = 0x92C3;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE = 0x92C4,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 0x92C5,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 0x92C6,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 0x92C7,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 0x92C8,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 0x92CA,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 0x92CB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 0x92CC,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 0x92CD,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE,
		GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 0x92CF,
		GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 0x92D0,
		GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 0x92D1,
		GL_MAX_VERTEX_ATOMIC_COUNTERS = 0x92D2,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 0x92D3,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 0x92D4,
		GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 0x92D5,
		GL_MAX_FRAGMENT_ATOMIC_COUNTERS = 0x92D6,
		GL_MAX_COMBINED_ATOMIC_COUNTERS = 0x92D7,
		GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE = 0x92D8,
		GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 0x92DC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformsiv: 
	 */
	public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA;

	/**
	 * Returned in &lt;params&gt; by GetActiveUniform and GetActiveUniformsiv: 
	 */
	public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB;

	private ARBShaderAtomicCounters() {}

	public static void glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname, IntBuffer params) {
		GL42.glGetActiveAtomicCounterBuffer(program, bufferIndex, pname, params);
	}

	/** Overloads glGetActiveAtomicCounterBufferiv. */
	public static int glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname) {
		return GL42.glGetActiveAtomicCounterBuffer(program, bufferIndex, pname);
	}
}
