/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBDebugOutput {

	/**
	 *  Tokens accepted by the &lt;target&gt; parameters of Enable, Disable,
	 *  and IsEnabled:
	 */
	public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS_ARB = 0x8242;

	/**
	 *  Tokens accepted by the &lt;value&gt; parameters of GetBooleanv,
	 *  GetIntegerv, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_DEBUG_MESSAGE_LENGTH_ARB = 0x9143,
		GL_MAX_DEBUG_LOGGED_MESSAGES_ARB = 0x9144,
		GL_DEBUG_LOGGED_MESSAGES_ARB = 0x9145,
		GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH_ARB = 0x8243;

	/**
	 * Tokens accepted by the &lt;pname&gt; parameter of GetPointerv: 
	 */
	public static final int GL_DEBUG_CALLBACK_FUNCTION_ARB = 0x8244,
		GL_DEBUG_CALLBACK_USER_PARAM_ARB = 0x8245;

	/**
	 *  Tokens accepted or provided by the &lt;source&gt; parameters of
	 *  DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB,
	 *  and the &lt;sources&gt; parameter of GetDebugMessageLogARB:
	 */
	public static final int GL_DEBUG_SOURCE_API_ARB = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM_ARB = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER_ARB = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY_ARB = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION_ARB = 0x824A,
		GL_DEBUG_SOURCE_OTHER_ARB = 0x824B;

	/**
	 *  Tokens accepted or provided by the &lt;type&gt; parameters of
	 *  DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB,
	 *  and the &lt;types&gt; parameter of GetDebugMessageLogARB:
	 */
	public static final int GL_DEBUG_TYPE_ERROR_ARB = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR_ARB = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR_ARB = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY_ARB = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE_ARB = 0x8250,
		GL_DEBUG_TYPE_OTHER_ARB = 0x8251;

	/**
	 *  Tokens accepted or provided by the &lt;severity&gt; parameters of
	 *  DebugMessageControlARB, DebugMessageInsertARB and DEBUGPROCARB
	 *  callback functions, and the &lt;severities&gt; parameter of
	 *  GetDebugMessageLogARB:
	 */
	public static final int GL_DEBUG_SEVERITY_HIGH_ARB = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM_ARB = 0x9147,
		GL_DEBUG_SEVERITY_LOW_ARB = 0x9148;

	private ARBDebugOutput() {}

	public static void glDebugMessageControlARB(int source, int type, int severity, IntBuffer ids, boolean enabled) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageControlARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (ids != null)
			BufferChecks.checkDirect(ids);
		nglDebugMessageControlARB(source, type, severity, (ids == null ? 0 : ids.remaining()), MemoryUtil.getAddressSafe(ids), enabled, function_pointer);
	}
	static native void nglDebugMessageControlARB(int source, int type, int severity, int ids_count, long ids, boolean enabled, long function_pointer);

	public static void glDebugMessageInsertARB(int source, int type, int id, int severity, ByteBuffer buf) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageInsertARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buf);
		nglDebugMessageInsertARB(source, type, id, severity, buf.remaining(), MemoryUtil.getAddress(buf), function_pointer);
	}
	static native void nglDebugMessageInsertARB(int source, int type, int id, int severity, int buf_length, long buf, long function_pointer);

	/** Overloads glDebugMessageInsertARB. */
	public static void glDebugMessageInsertARB(int source, int type, int id, int severity, CharSequence buf) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageInsertARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDebugMessageInsertARB(source, type, id, severity, buf.length(), APIUtil.getBuffer(caps, buf), function_pointer);
	}

	/**
	 *  The {@code ARBDebugOutputCallback.Handler} implementation passed to this method will be used for
	 *  ARB_debug_output messages. If callback is null, any previously registered handler for the current
	 *  thread will be unregistered and stop receiving messages.
	 * <p>
	 *  @param callback the callback function to use
	 */
	public static void glDebugMessageCallbackARB(ARBDebugOutputCallback callback) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDebugMessageCallbackARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		long userParam = callback == null ? 0 : CallbackUtil.createGlobalRef(callback.getHandler());
		CallbackUtil.registerContextCallbackARB(userParam);
		nglDebugMessageCallbackARB(callback == null ? 0 : callback.getPointer(), userParam, function_pointer);
	}
	static native void nglDebugMessageCallbackARB(long callback, long userParam, long function_pointer);

	public static int glGetDebugMessageLogARB(int count, IntBuffer sources, IntBuffer types, IntBuffer ids, IntBuffer severities, IntBuffer lengths, ByteBuffer messageLog) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDebugMessageLogARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (sources != null)
			BufferChecks.checkBuffer(sources, count);
		if (types != null)
			BufferChecks.checkBuffer(types, count);
		if (ids != null)
			BufferChecks.checkBuffer(ids, count);
		if (severities != null)
			BufferChecks.checkBuffer(severities, count);
		if (lengths != null)
			BufferChecks.checkBuffer(lengths, count);
		if (messageLog != null)
			BufferChecks.checkDirect(messageLog);
		int __result = nglGetDebugMessageLogARB(count, (messageLog == null ? 0 : messageLog.remaining()), MemoryUtil.getAddressSafe(sources), MemoryUtil.getAddressSafe(types), MemoryUtil.getAddressSafe(ids), MemoryUtil.getAddressSafe(severities), MemoryUtil.getAddressSafe(lengths), MemoryUtil.getAddressSafe(messageLog), function_pointer);
		return __result;
	}
	static native int nglGetDebugMessageLogARB(int count, int messageLog_logSize, long sources, long types, long ids, long severities, long lengths, long messageLog, long function_pointer);
}
