/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBQueryBufferObject {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetQueryObjectiv, GetQueryObjectuiv,
	 *  GetQueryObjecti64v and GetQueryObjectui64v:
	 */
	public static final int GL_QUERY_RESULT_NO_WAIT = 0x9194;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, MapBufferRange, GetBufferSubData,
	 *  GetBufferParameteriv, GetBufferParameteri64v, GetBufferPointerv,
	 *  ClearBufferSubData, and the &lt;readtarget&gt; and &lt;writetarget&gt; parameters of
	 *  CopyBufferSubData:
	 */
	public static final int GL_QUERY_BUFFER = 0x9192;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_QUERY_BUFFER_BINDING = 0x9193;

	/**
	 * Accepted in the &lt;barriers&gt; bitfield in MemoryBarrier: 
	 */
	public static final int GL_QUERY_BUFFER_BARRIER_BIT = 0x8000;

	private ARBQueryBufferObject() {}
}
