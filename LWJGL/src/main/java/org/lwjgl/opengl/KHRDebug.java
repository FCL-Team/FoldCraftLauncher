/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class KHRDebug {

	/**
	 *  Tokens accepted by the &lt;target&gt; parameters of Enable, Disable, and
	 *  IsEnabled:
	 */
	public static final int GL_DEBUG_OUTPUT = 0x92E0,
		GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;

	/**
	 * Returned by GetIntegerv when &lt;pname&gt; is CONTEXT_FLAGS: 
	 */
	public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 0x2;

	/**
	 *  Tokens accepted by the &lt;value&gt; parameters of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 0x9143,
		GL_MAX_DEBUG_LOGGED_MESSAGES = 0x9144,
		GL_DEBUG_LOGGED_MESSAGES = 0x9145,
		GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243,
		GL_MAX_DEBUG_GROUP_STACK_DEPTH = 0x826C,
		GL_DEBUG_GROUP_STACK_DEPTH = 0x826D,
		GL_MAX_LABEL_LENGTH = 0x82E8;

	/**
	 * Tokens accepted by the &lt;pname&gt; parameter of GetPointerv: 
	 */
	public static final int GL_DEBUG_CALLBACK_FUNCTION = 0x8244,
		GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;

	/**
	 *  Tokens accepted or provided by the &lt;source&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;sources&gt;
	 *  parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_SOURCE_API = 0x8246,
		GL_DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247,
		GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248,
		GL_DEBUG_SOURCE_THIRD_PARTY = 0x8249,
		GL_DEBUG_SOURCE_APPLICATION = 0x824A,
		GL_DEBUG_SOURCE_OTHER = 0x824B;

	/**
	 *  Tokens accepted or provided by the &lt;type&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC, and the &lt;types&gt;
	 *  parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_TYPE_ERROR = 0x824C,
		GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D,
		GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824E,
		GL_DEBUG_TYPE_PORTABILITY = 0x824F,
		GL_DEBUG_TYPE_PERFORMANCE = 0x8250,
		GL_DEBUG_TYPE_OTHER = 0x8251,
		GL_DEBUG_TYPE_MARKER = 0x8268;

	/**
	 *  Tokens accepted or provided by the &lt;type&gt; parameters of
	 *  DebugMessageControl and DEBUGPROC, and the &lt;types&gt; parameter of
	 *  GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269,
		GL_DEBUG_TYPE_POP_GROUP = 0x826A;

	/**
	 *  Tokens accepted or provided by the &lt;severity&gt; parameters of
	 *  DebugMessageControl, DebugMessageInsert and DEBUGPROC callback functions,
	 *  and the &lt;severities&gt; parameter of GetDebugMessageLog:
	 */
	public static final int GL_DEBUG_SEVERITY_HIGH = 0x9146,
		GL_DEBUG_SEVERITY_MEDIUM = 0x9147,
		GL_DEBUG_SEVERITY_LOW = 0x9148,
		GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;

	/**
	 *  Tokens accepted or provided by the &lt;identifier&gt; parameters of
	 *  ObjectLabel and GetObjectLabel:
	 */
	public static final int GL_BUFFER = 0x82E0,
		GL_SHADER = 0x82E1,
		GL_PROGRAM = 0x82E2,
		GL_QUERY = 0x82E3,
		GL_PROGRAM_PIPELINE = 0x82E4,
		GL_SAMPLER = 0x82E6,
		GL_DISPLAY_LIST = 0x82E7;

	private KHRDebug() {}

	public static void glDebugMessageControl(int source, int type, int severity, IntBuffer ids, boolean enabled) {
		GL43.glDebugMessageControl(source, type, severity, ids, enabled);
	}

	public static void glDebugMessageInsert(int source, int type, int id, int severity, ByteBuffer buf) {
		GL43.glDebugMessageInsert(source, type, id, severity, buf);
	}

	/** Overloads glDebugMessageInsert. */
	public static void glDebugMessageInsert(int source, int type, int id, int severity, CharSequence buf) {
		GL43.glDebugMessageInsert(source, type, id, severity, buf);
	}

	/**
	 *  The {@code KHRDebugCallback.Handler} implementation passed to this method will be used for
	 *  KHR_debug messages. If callback is null, any previously registered handler for the current
	 *  thread will be unregistered and stop receiving messages.
	 * <p>
	 *  @param callback the callback function to use
	 */
	public static void glDebugMessageCallback(KHRDebugCallback callback) {
		GL43.glDebugMessageCallback(callback);
	}

	public static int glGetDebugMessageLog(int count, IntBuffer sources, IntBuffer types, IntBuffer ids, IntBuffer severities, IntBuffer lengths, ByteBuffer messageLog) {
		return GL43.glGetDebugMessageLog(count, sources, types, ids, severities, lengths, messageLog);
	}

	public static void glPushDebugGroup(int source, int id, ByteBuffer message) {
		GL43.glPushDebugGroup(source, id, message);
	}

	/** Overloads glPushDebugGroup. */
	public static void glPushDebugGroup(int source, int id, CharSequence message) {
		GL43.glPushDebugGroup(source, id, message);
	}

	public static void glPopDebugGroup() {
		GL43.glPopDebugGroup();
	}

	public static void glObjectLabel(int identifier, int name, ByteBuffer label) {
		GL43.glObjectLabel(identifier, name, label);
	}

	/** Overloads glObjectLabel. */
	public static void glObjectLabel(int identifier, int name, CharSequence label) {
		GL43.glObjectLabel(identifier, name, label);
	}

	public static void glGetObjectLabel(int identifier, int name, IntBuffer length, ByteBuffer label) {
		GL43.glGetObjectLabel(identifier, name, length, label);
	}

	/** Overloads glGetObjectLabel. */
	public static String glGetObjectLabel(int identifier, int name, int bufSize) {
		return GL43.glGetObjectLabel(identifier, name, bufSize);
	}

	public static void glObjectPtrLabel(PointerWrapper ptr, ByteBuffer label) {
		GL43.glObjectPtrLabel(ptr, label);
	}

	/** Overloads glObjectPtrLabel. */
	public static void glObjectPtrLabel(PointerWrapper ptr, CharSequence label) {
		GL43.glObjectPtrLabel(ptr, label);
	}

	public static void glGetObjectPtrLabel(PointerWrapper ptr, IntBuffer length, ByteBuffer label) {
		GL43.glGetObjectPtrLabel(ptr, length, label);
	}

	/** Overloads glGetObjectPtrLabel. */
	public static String glGetObjectPtrLabel(PointerWrapper ptr, int bufSize) {
		return GL43.glGetObjectPtrLabel(ptr, bufSize);
	}
}
