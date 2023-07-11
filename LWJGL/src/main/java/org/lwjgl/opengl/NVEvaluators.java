/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVEvaluators {

	public static final int GL_EVAL_2D_NV = 0x86C0,
		GL_EVAL_TRIANGULAR_2D_NV = 0x86C1,
		GL_MAP_TESSELLATION_NV = 0x86C2,
		GL_MAP_ATTRIB_U_ORDER_NV = 0x86C3,
		GL_MAP_ATTRIB_V_ORDER_NV = 0x86C4,
		GL_EVAL_FRACTIONAL_TESSELLATION_NV = 0x86C5,
		GL_EVAL_VERTEX_ATTRIB0_NV = 0x86C6,
		GL_EVAL_VERTEX_ATTRIB1_NV = 0x86C7,
		GL_EVAL_VERTEX_ATTRIB2_NV = 0x86C8,
		GL_EVAL_VERTEX_ATTRIB3_NV = 0x86C9,
		GL_EVAL_VERTEX_ATTRIB4_NV = 0x86CA,
		GL_EVAL_VERTEX_ATTRIB5_NV = 0x86CB,
		GL_EVAL_VERTEX_ATTRIB6_NV = 0x86CC,
		GL_EVAL_VERTEX_ATTRIB7_NV = 0x86CD,
		GL_EVAL_VERTEX_ATTRIB8_NV = 0x86CE,
		GL_EVAL_VERTEX_ATTRIB9_NV = 0x86CF,
		GL_EVAL_VERTEX_ATTRIB10_NV = 0x86D0,
		GL_EVAL_VERTEX_ATTRIB11_NV = 0x86D1,
		GL_EVAL_VERTEX_ATTRIB12_NV = 0x86D2,
		GL_EVAL_VERTEX_ATTRIB13_NV = 0x86D3,
		GL_EVAL_VERTEX_ATTRIB14_NV = 0x86D4,
		GL_EVAL_VERTEX_ATTRIB15_NV = 0x86D5,
		GL_MAX_MAP_TESSELLATION_NV = 0x86D6,
		GL_MAX_RATIONAL_EVAL_ORDER_NV = 0x86D7;

	private NVEvaluators() {}

	public static void glGetMapControlPointsNV(int target, int index, int type, int ustride, int vstride, boolean packed, FloatBuffer pPoints) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapControlPointsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPoints);
		nglGetMapControlPointsNV(target, index, type, ustride, vstride, packed, MemoryUtil.getAddress(pPoints), function_pointer);
	}
	static native void nglGetMapControlPointsNV(int target, int index, int type, int ustride, int vstride, boolean packed, long pPoints, long function_pointer);

	public static void glMapControlPointsNV(int target, int index, int type, int ustride, int vstride, int uorder, int vorder, boolean packed, FloatBuffer pPoints) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapControlPointsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPoints);
		nglMapControlPointsNV(target, index, type, ustride, vstride, uorder, vorder, packed, MemoryUtil.getAddress(pPoints), function_pointer);
	}
	static native void nglMapControlPointsNV(int target, int index, int type, int ustride, int vstride, int uorder, int vorder, boolean packed, long pPoints, long function_pointer);

	public static void glMapParameterNV(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglMapParameterfvNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglMapParameterfvNV(int target, int pname, long params, long function_pointer);

	public static void glMapParameterNV(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglMapParameterivNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglMapParameterivNV(int target, int pname, long params, long function_pointer);

	public static void glGetMapParameterNV(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMapParameterfvNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMapParameterfvNV(int target, int pname, long params, long function_pointer);

	public static void glGetMapParameterNV(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMapParameterivNV(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMapParameterivNV(int target, int pname, long params, long function_pointer);

	public static void glGetMapAttribParameterNV(int target, int index, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapAttribParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMapAttribParameterfvNV(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMapAttribParameterfvNV(int target, int index, int pname, long params, long function_pointer);

	public static void glGetMapAttribParameterNV(int target, int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMapAttribParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMapAttribParameterivNV(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMapAttribParameterivNV(int target, int index, int pname, long params, long function_pointer);

	public static void glEvalMapsNV(int target, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEvalMapsNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEvalMapsNV(target, mode, function_pointer);
	}
	static native void nglEvalMapsNV(int target, int mode, long function_pointer);
}
