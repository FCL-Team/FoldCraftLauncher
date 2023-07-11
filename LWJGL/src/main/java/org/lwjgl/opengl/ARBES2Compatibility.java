/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBES2Compatibility {

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_SHADER_COMPILER = 0x8DFA,
		GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9,
		GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		GL_MAX_VARYING_VECTORS = 0x8DFC,
		GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A,
		GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;

	/**
	 * Accepted by the &lt;type&gt; parameter of VertexAttribPointer: 
	 */
	public static final int GL_FIXED = 0x140C;

	/**
	 *  Accepted by the &lt;precisiontype&gt; parameter of
	 *  GetShaderPrecisionFormat:
	 */
	public static final int GL_LOW_FLOAT = 0x8DF0,
		GL_MEDIUM_FLOAT = 0x8DF1,
		GL_HIGH_FLOAT = 0x8DF2,
		GL_LOW_INT = 0x8DF3,
		GL_MEDIUM_INT = 0x8DF4,
		GL_HIGH_INT = 0x8DF5;

	/**
	 * Accepted by the &lt;format&gt; parameter of most commands taking sized internal formats: 
	 */
	public static final int GL_RGB565 = 0x8D62;

	private ARBES2Compatibility() {}

	public static void glReleaseShaderCompiler() {
		GL41.glReleaseShaderCompiler();
	}

	public static void glShaderBinary(IntBuffer shaders, int binaryformat, ByteBuffer binary) {
		GL41.glShaderBinary(shaders, binaryformat, binary);
	}

	public static void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		GL41.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
	}

	public static void glDepthRangef(float n, float f) {
		GL41.glDepthRangef(n, f);
	}

	public static void glClearDepthf(float d) {
		GL41.glClearDepthf(d);
	}
}
