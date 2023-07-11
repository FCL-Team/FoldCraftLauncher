/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexAttribBinding {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib*v: 
	 */
	public static final int GL_VERTEX_ATTRIB_BINDING = 0x82D4,
		GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 *  GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	public static final int GL_VERTEX_BINDING_DIVISOR = 0x82D6,
		GL_VERTEX_BINDING_OFFSET = 0x82D7,
		GL_VERTEX_BINDING_STRIDE = 0x82D8;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, ... 
	 */
	public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9,
		GL_MAX_VERTEX_ATTRIB_BINDINGS = 0x82DA;

	private ARBVertexAttribBinding() {}

	public static void glBindVertexBuffer(int bindingindex, int buffer, long offset, int stride) {
		GL43.glBindVertexBuffer(bindingindex, buffer, offset, stride);
	}

	public static void glVertexAttribFormat(int attribindex, int size, int type, boolean normalized, int relativeoffset) {
		GL43.glVertexAttribFormat(attribindex, size, type, normalized, relativeoffset);
	}

	public static void glVertexAttribIFormat(int attribindex, int size, int type, int relativeoffset) {
		GL43.glVertexAttribIFormat(attribindex, size, type, relativeoffset);
	}

	public static void glVertexAttribLFormat(int attribindex, int size, int type, int relativeoffset) {
		GL43.glVertexAttribLFormat(attribindex, size, type, relativeoffset);
	}

	public static void glVertexAttribBinding(int attribindex, int bindingindex) {
		GL43.glVertexAttribBinding(attribindex, bindingindex);
	}

	public static void glVertexBindingDivisor(int bindingindex, int divisor) {
		GL43.glVertexBindingDivisor(bindingindex, divisor);
	}
}
