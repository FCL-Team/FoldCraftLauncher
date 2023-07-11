/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBProvokingVertex {

	/**
	 * Accepted by the &lt;mode&gt; parameter of ProvokingVertex: 
	 */
	public static final int GL_FIRST_VERTEX_CONVENTION = 0x8E4D,
		GL_LAST_VERTEX_CONVENTION = 0x8E4E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROVOKING_VERTEX = 0x8E4F,
		GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 0x8E4C;

	private ARBProvokingVertex() {}

	public static void glProvokingVertex(int mode) {
		GL32.glProvokingVertex(mode);
	}
}
