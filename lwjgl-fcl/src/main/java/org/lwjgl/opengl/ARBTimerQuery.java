/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTimerQuery {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 *  GetQueryiv:
	 */
	public static final int GL_TIME_ELAPSED = 0x88BF;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetQueryiv and QueryCounter.
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_TIMESTAMP = 0x8E28;

	private ARBTimerQuery() {}

	public static void glQueryCounter(int id, int target) {
		GL33.glQueryCounter(id, target);
	}

	public static void glGetQueryObject(int id, int pname, LongBuffer params) {
		GL33.glGetQueryObject(id, pname, params);
	}

	/** Overloads glGetQueryObjecti64v. */
	public static long glGetQueryObjecti64(int id, int pname) {
		return GL33.glGetQueryObjecti64(id, pname);
	}

	public static void glGetQueryObjectu(int id, int pname, LongBuffer params) {
		GL33.glGetQueryObjectu(id, pname, params);
	}

	/** Overloads glGetQueryObjectui64v. */
	public static long glGetQueryObjectui64(int id, int pname) {
		return GL33.glGetQueryObjectui64(id, pname);
	}
}
