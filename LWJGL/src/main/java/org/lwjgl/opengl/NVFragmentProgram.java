/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVFragmentProgram extends NVProgram {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, by the
	 *  &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	 *  and by the &lt;target&gt; parameter of BindProgramNV, LoadProgramNV,
	 *  ProgramLocalParameter4dARB, ProgramLocalParameter4dvARB,
	 *  ProgramLocalParameter4fARB, ProgramLocalParameter4fvARB,
	 *  GetProgramLocalParameterdvARB, and GetProgramLocalParameterfvARB:
	 */
	public static final int GL_FRAGMENT_PROGRAM_NV = 0x8870;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_TEXTURE_COORDS_NV = 0x8871,
		GL_MAX_TEXTURE_IMAGE_UNITS_NV = 0x8872,
		GL_FRAGMENT_PROGRAM_BINDING_NV = 0x8873,
		GL_MAX_FRAGMENT_PROGRAM_LOCAL_PARAMETERS_NV = 0x8868;

	private NVFragmentProgram() {}

	public static void glProgramNamedParameter4fNV(int id, ByteBuffer name, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramNamedParameter4fNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		nglProgramNamedParameter4fNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
	}
	static native void nglProgramNamedParameter4fNV(int id, int name_length, long name, float x, float y, float z, float w, long function_pointer);

	public static void glProgramNamedParameter4dNV(int id, ByteBuffer name, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramNamedParameter4dNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		nglProgramNamedParameter4dNV(id, name.remaining(), MemoryUtil.getAddress(name), x, y, z, w, function_pointer);
	}
	static native void nglProgramNamedParameter4dNV(int id, int name_length, long name, double x, double y, double z, double w, long function_pointer);

	public static void glGetProgramNamedParameterNV(int id, ByteBuffer name, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramNamedParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramNamedParameterfvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramNamedParameterfvNV(int id, int name_length, long name, long params, long function_pointer);

	public static void glGetProgramNamedParameterNV(int id, ByteBuffer name, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramNamedParameterdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkBuffer(params, 4);
		nglGetProgramNamedParameterdvNV(id, name.remaining(), MemoryUtil.getAddress(name), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramNamedParameterdvNV(int id, int name_length, long name, long params, long function_pointer);
}
