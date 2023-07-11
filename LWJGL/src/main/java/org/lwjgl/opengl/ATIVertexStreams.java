/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIVertexStreams {

	public static final int GL_MAX_VERTEX_STREAMS_ATI = 0x876B,
		GL_VERTEX_SOURCE_ATI = 0x876C,
		GL_VERTEX_STREAM0_ATI = 0x876D,
		GL_VERTEX_STREAM1_ATI = 0x876E,
		GL_VERTEX_STREAM2_ATI = 0x876F,
		GL_VERTEX_STREAM3_ATI = 0x8770,
		GL_VERTEX_STREAM4_ATI = 0x8771,
		GL_VERTEX_STREAM5_ATI = 0x8772,
		GL_VERTEX_STREAM6_ATI = 0x8773,
		GL_VERTEX_STREAM7_ATI = 0x8774;

	private ATIVertexStreams() {}

	public static void glVertexStream2fATI(int stream, float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream2fATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream2fATI(stream, x, y, function_pointer);
	}
	static native void nglVertexStream2fATI(int stream, float x, float y, long function_pointer);

	public static void glVertexStream2dATI(int stream, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream2dATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream2dATI(stream, x, y, function_pointer);
	}
	static native void nglVertexStream2dATI(int stream, double x, double y, long function_pointer);

	public static void glVertexStream2iATI(int stream, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream2iATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream2iATI(stream, x, y, function_pointer);
	}
	static native void nglVertexStream2iATI(int stream, int x, int y, long function_pointer);

	public static void glVertexStream2sATI(int stream, short x, short y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream2sATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream2sATI(stream, x, y, function_pointer);
	}
	static native void nglVertexStream2sATI(int stream, short x, short y, long function_pointer);

	public static void glVertexStream3fATI(int stream, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream3fATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream3fATI(stream, x, y, z, function_pointer);
	}
	static native void nglVertexStream3fATI(int stream, float x, float y, float z, long function_pointer);

	public static void glVertexStream3dATI(int stream, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream3dATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream3dATI(stream, x, y, z, function_pointer);
	}
	static native void nglVertexStream3dATI(int stream, double x, double y, double z, long function_pointer);

	public static void glVertexStream3iATI(int stream, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream3iATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream3iATI(stream, x, y, z, function_pointer);
	}
	static native void nglVertexStream3iATI(int stream, int x, int y, int z, long function_pointer);

	public static void glVertexStream3sATI(int stream, short x, short y, short z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream3sATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream3sATI(stream, x, y, z, function_pointer);
	}
	static native void nglVertexStream3sATI(int stream, short x, short y, short z, long function_pointer);

	public static void glVertexStream4fATI(int stream, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream4fATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream4fATI(stream, x, y, z, w, function_pointer);
	}
	static native void nglVertexStream4fATI(int stream, float x, float y, float z, float w, long function_pointer);

	public static void glVertexStream4dATI(int stream, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream4dATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream4dATI(stream, x, y, z, w, function_pointer);
	}
	static native void nglVertexStream4dATI(int stream, double x, double y, double z, double w, long function_pointer);

	public static void glVertexStream4iATI(int stream, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream4iATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream4iATI(stream, x, y, z, w, function_pointer);
	}
	static native void nglVertexStream4iATI(int stream, int x, int y, int z, int w, long function_pointer);

	public static void glVertexStream4sATI(int stream, short x, short y, short z, short w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexStream4sATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexStream4sATI(stream, x, y, z, w, function_pointer);
	}
	static native void nglVertexStream4sATI(int stream, short x, short y, short z, short w, long function_pointer);

	public static void glNormalStream3bATI(int stream, byte x, byte y, byte z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalStream3bATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalStream3bATI(stream, x, y, z, function_pointer);
	}
	static native void nglNormalStream3bATI(int stream, byte x, byte y, byte z, long function_pointer);

	public static void glNormalStream3fATI(int stream, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalStream3fATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalStream3fATI(stream, x, y, z, function_pointer);
	}
	static native void nglNormalStream3fATI(int stream, float x, float y, float z, long function_pointer);

	public static void glNormalStream3dATI(int stream, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalStream3dATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalStream3dATI(stream, x, y, z, function_pointer);
	}
	static native void nglNormalStream3dATI(int stream, double x, double y, double z, long function_pointer);

	public static void glNormalStream3iATI(int stream, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalStream3iATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalStream3iATI(stream, x, y, z, function_pointer);
	}
	static native void nglNormalStream3iATI(int stream, int x, int y, int z, long function_pointer);

	public static void glNormalStream3sATI(int stream, short x, short y, short z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalStream3sATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalStream3sATI(stream, x, y, z, function_pointer);
	}
	static native void nglNormalStream3sATI(int stream, short x, short y, short z, long function_pointer);

	public static void glClientActiveVertexStreamATI(int stream) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClientActiveVertexStreamATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClientActiveVertexStreamATI(stream, function_pointer);
	}
	static native void nglClientActiveVertexStreamATI(int stream, long function_pointer);

	public static void glVertexBlendEnvfATI(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexBlendEnvfATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexBlendEnvfATI(pname, param, function_pointer);
	}
	static native void nglVertexBlendEnvfATI(int pname, float param, long function_pointer);

	public static void glVertexBlendEnviATI(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexBlendEnviATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexBlendEnviATI(pname, param, function_pointer);
	}
	static native void nglVertexBlendEnviATI(int pname, int param, long function_pointer);
}
