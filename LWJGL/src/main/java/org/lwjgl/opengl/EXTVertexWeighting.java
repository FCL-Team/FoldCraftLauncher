/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTVertexWeighting {

	public static final int GL_MODELVIEW0_STACK_DEPTH_EXT = 0xBA3,
		GL_MODELVIEW1_STACK_DEPTH_EXT = 0x8502,
		GL_MODELVIEW0_MATRIX_EXT = 0xBA6,
		GL_MODELVIEW1_MATRIX_EXT = 0x8506,
		GL_VERTEX_WEIGHTING_EXT = 0x8509,
		GL_MODELVIEW0_EXT = 0x1700,
		GL_MODELVIEW1_EXT = 0x850A,
		GL_CURRENT_VERTEX_WEIGHT_EXT = 0x850B,
		GL_VERTEX_WEIGHT_ARRAY_EXT = 0x850C,
		GL_VERTEX_WEIGHT_ARRAY_SIZE_EXT = 0x850D,
		GL_VERTEX_WEIGHT_ARRAY_TYPE_EXT = 0x850E,
		GL_VERTEX_WEIGHT_ARRAY_STRIDE_EXT = 0x850F,
		GL_VERTEX_WEIGHT_ARRAY_POINTER_EXT = 0x8510;

	private EXTVertexWeighting() {}

	public static void glVertexWeightfEXT(float weight) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexWeightfEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexWeightfEXT(weight, function_pointer);
	}
	static native void nglVertexWeightfEXT(float weight, long function_pointer);

	public static void glVertexWeightPointerEXT(int size, int stride, FloatBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexWeightPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = pPointer;
		nglVertexWeightPointerEXT(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglVertexWeightPointerEXT(int size, int type, int stride, long pPointer, long function_pointer);
	public static void glVertexWeightPointerEXT(int size, int type, int stride, long pPointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexWeightPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexWeightPointerEXTBO(size, type, stride, pPointer_buffer_offset, function_pointer);
	}
	static native void nglVertexWeightPointerEXTBO(int size, int type, int stride, long pPointer_buffer_offset, long function_pointer);
}
