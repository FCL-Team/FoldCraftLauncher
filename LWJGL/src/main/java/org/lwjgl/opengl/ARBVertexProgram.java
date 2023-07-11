/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexProgram extends ARBProgram {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, by the
	 *  &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	 *  and by the &lt;target&gt; parameter of ProgramStringARB, BindProgramARB,
	 *  ProgramEnvParameter4[df][v]ARB, ProgramLocalParameter4[df][v]ARB,
	 *  GetProgramEnvParameter[df]vARB, GetProgramLocalParameter[df]vARB,
	 *  GetProgramivARB, and GetProgramStringARB.
	 */
	public static final int GL_VERTEX_PROGRAM_ARB = 0x8620;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642,
		GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 0x8643,
		GL_COLOR_SUM_ARB = 0x8458;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib[dfi]vARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 0x8622,
		GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 0x8623,
		GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 0x8624,
		GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 0x8625,
		GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886A,
		GL_CURRENT_VERTEX_ATTRIB_ARB = 0x8626;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB: 
	 */
	public static final int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B0,
		GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B1,
		GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B2,
		GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B3;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_ATTRIBS_ARB = 0x8869;

	private ARBVertexProgram() {}

	public static void glVertexAttrib1sARB(int index, short x) {
		ARBVertexShader.glVertexAttrib1sARB(index, x);
	}

	public static void glVertexAttrib1fARB(int index, float x) {
		ARBVertexShader.glVertexAttrib1fARB(index, x);
	}

	public static void glVertexAttrib1dARB(int index, double x) {
		ARBVertexShader.glVertexAttrib1dARB(index, x);
	}

	public static void glVertexAttrib2sARB(int index, short x, short y) {
		ARBVertexShader.glVertexAttrib2sARB(index, x, y);
	}

	public static void glVertexAttrib2fARB(int index, float x, float y) {
		ARBVertexShader.glVertexAttrib2fARB(index, x, y);
	}

	public static void glVertexAttrib2dARB(int index, double x, double y) {
		ARBVertexShader.glVertexAttrib2dARB(index, x, y);
	}

	public static void glVertexAttrib3sARB(int index, short x, short y, short z) {
		ARBVertexShader.glVertexAttrib3sARB(index, x, y, z);
	}

	public static void glVertexAttrib3fARB(int index, float x, float y, float z) {
		ARBVertexShader.glVertexAttrib3fARB(index, x, y, z);
	}

	public static void glVertexAttrib3dARB(int index, double x, double y, double z) {
		ARBVertexShader.glVertexAttrib3dARB(index, x, y, z);
	}

	public static void glVertexAttrib4sARB(int index, short x, short y, short z, short w) {
		ARBVertexShader.glVertexAttrib4sARB(index, x, y, z, w);
	}

	public static void glVertexAttrib4fARB(int index, float x, float y, float z, float w) {
		ARBVertexShader.glVertexAttrib4fARB(index, x, y, z, w);
	}

	public static void glVertexAttrib4dARB(int index, double x, double y, double z, double w) {
		ARBVertexShader.glVertexAttrib4dARB(index, x, y, z, w);
	}

	public static void glVertexAttrib4NubARB(int index, byte x, byte y, byte z, byte w) {
		ARBVertexShader.glVertexAttrib4NubARB(index, x, y, z, w);
	}

	public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, DoubleBuffer buffer) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, normalized, stride, buffer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, unsigned, normalized, stride, buffer);
	}
	public static void glVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
		ARBVertexShader.glVertexAttribPointerARB(index, size, type, normalized, stride, buffer_buffer_offset);
	}

	public static void glEnableVertexAttribArrayARB(int index) {
		ARBVertexShader.glEnableVertexAttribArrayARB(index);
	}

	public static void glDisableVertexAttribArrayARB(int index) {
		ARBVertexShader.glDisableVertexAttribArrayARB(index);
	}

	public static void glGetVertexAttribARB(int index, int pname, FloatBuffer params) {
		ARBVertexShader.glGetVertexAttribARB(index, pname, params);
	}

	public static void glGetVertexAttribARB(int index, int pname, DoubleBuffer params) {
		ARBVertexShader.glGetVertexAttribARB(index, pname, params);
	}

	public static void glGetVertexAttribARB(int index, int pname, IntBuffer params) {
		ARBVertexShader.glGetVertexAttribARB(index, pname, params);
	}

	public static ByteBuffer glGetVertexAttribPointerARB(int index, int pname, long result_size) {
		return ARBVertexShader.glGetVertexAttribPointerARB(index, pname, result_size);
	}
}
