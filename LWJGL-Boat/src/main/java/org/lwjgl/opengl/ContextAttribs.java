/*
 * Copyright (c) 2002-2014 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLUtil;

import java.nio.IntBuffer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * This class represents the context attributes passed to CreateContextAttribs of the ARB_create_context extension.
 * <p/>
 * The attributes supported are described in the following extensions:<br>
 * <ul>
 * <li><a href="http://www.opengl.org/registry/specs/ARB/wgl_create_context.txt">WGL_ARB_create_context(_profile)</a> and <a href="http://www.opengl.org/registry/specs/ARB/glx_create_context.txt">GLX_ARB_create_context(_profile)</a></li>
 * <li><a href="http://www.opengl.org/registry/specs/ARB/wgl_create_context_robustness.txt">WGL_ARB_create_context_robustness</a> and <a href="http://www.opengl.org/registry/specs/ARB/glx_create_context_robustness.txt">GLX_ARB_create_context_robustness</a></li>
 * <li><a href="http://www.opengl.org/registry/specs/ARB/wgl_robustness_isolation.txt">WGL_ARB_robustness_isolation</a> and <a href="http://www.opengl.org/registry/specs/ARB/glx_robustness_isolation.txt">GLX_ARB_robustness_isolation</a></li>
 * <li><a href="http://www.opengl.org/registry/specs/ARB/wgl_create_context_es2_profile.txt">WGL_EXT_create_context_es2_profile</a> and <a href="http://www.opengl.org/registry/specs/ARB/glx_create_context_es2_profile.txt">GLX_EXT_create_context_es2_profile</a></li>
 * <li><a href="http://www.opengl.org/registry/specs/ARB/context_flush_control.txt">KHR_context_flush_control</a></li>
 * </ul>
 * <p/>
 * Use of this class is optional. If an OpenGL context is created without passing an instance of this class
 * (or ARB_create_context is not supported), the old context creation code will be used. Support for debug and forward
 * compatible mobes is not guaranteed by the OpenGL implementation. Developers may encounter debug contexts being the same
 * as non-debug contexts or forward compatible contexts having support for deprecated functionality.
 * <p/>
 * If the {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB} flag is used, LWJGL will not load the deprecated functionality (as defined in the OpenGL 3.0
 * specification), even if the driver exposes the corresponding entry points.
 * <p/>
 * This extension is not supported on MacOS X. However, in order to enable the GL 3.2 context on MacOS X 10.7 or newer, an instance of this class must be passed
 * to LWJGL. The only valid configuration is <code>ContextAttribs(3, 2, CONTEXT_CORE_PROFILE_BIT_ARB)</code>, anything else will be ignored.
 *
 * @author spasi <spasi@users.sourceforge.net>
 */
public final class ContextAttribs {

	// ATTRIBUTES

	public static final int CONTEXT_MAJOR_VERSION_ARB = 0x2091;
	public static final int CONTEXT_MINOR_VERSION_ARB = 0x2092;

	public static final int CONTEXT_PROFILE_MASK_ARB = 0x9126,
		CONTEXT_CORE_PROFILE_BIT_ARB                 = 0x00000001,
		CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB        = 0x00000002,
		CONTEXT_ES2_PROFILE_BIT_EXT                  = 0x00000004;

	public static final int CONTEXT_FLAGS_ARB = 0x2094,
		CONTEXT_DEBUG_BIT_ARB                 = 0x0001,
		CONTEXT_FORWARD_COMPATIBLE_BIT_ARB    = 0x0002,
		CONTEXT_ROBUST_ACCESS_BIT_ARB         = 0x00000004,
		CONTEXT_RESET_ISOLATION_BIT_ARB       = 0x00000008;

	public static final int CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB = 0x8256,
		NO_RESET_NOTIFICATION_ARB                                   = 0x8261,
		LOSE_CONTEXT_ON_RESET_ARB                                   = 0x8252;

	public static final int CONTEXT_RELEASE_BEHABIOR_ARB = 0x2097,
		CONTEXT_RELEASE_BEHAVIOR_NONE_ARB                = 0x0000,
		CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB               = 0x2098;

	public static final int CONTEXT_LAYER_PLANE_ARB = 0x2093; // WGL-only

	// STATE

	private int majorVersion;
	private int minorVersion;

	private int profileMask;
	private int contextFlags;

	private int contextResetNotificationStrategy = NO_RESET_NOTIFICATION_ARB;
	private int contextReleaseBehavior           = CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB;

	private int layerPlane;

	// CONSTRUCTORS

	/** Creates the default ContextAttribs instance. No special attributes will be used when creating the OpenGL context. */
	public ContextAttribs() {
		this(1, 0);
	}

	/** Creates a ContextAttribs instance for the given OpenGL version. */
	public ContextAttribs(int majorVersion, int minorVersion) {
		this(majorVersion, minorVersion, 0, 0);
	}

	/**
	 * Creates a new ContextAttribs instance with the given attributes.
	 *
	 * @param majorVersion the major OpenGL version
	 * @param minorVersion the minor OpenGL version
	 * @param profileMask  the context profile mask. One of:<br>{@link #CONTEXT_CORE_PROFILE_BIT_ARB}, {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB}, {@link #CONTEXT_ES2_PROFILE_BIT_EXT}
	 */
	public ContextAttribs(int majorVersion, int minorVersion, int profileMask) {
		this(majorVersion, minorVersion, 0, profileMask);
	}

	/**
	 * Creates a new ContextAttribs instance with the given attributes.
	 *
	 * @param majorVersion the major OpenGL version
	 * @param minorVersion the minor OpenGL version
	 * @param profileMask  the context profile mask. One of:<br>{@link #CONTEXT_CORE_PROFILE_BIT_ARB}, {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB}, {@link #CONTEXT_ES2_PROFILE_BIT_EXT}
	 * @param contextFlags the context flags, a bitfield value. One or more of:<br>{@link #CONTEXT_DEBUG_BIT_ARB}, {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB}, {@link #CONTEXT_ROBUST_ACCESS_BIT_ARB}, {@link #CONTEXT_RESET_ISOLATION_BIT_ARB}
	 */
	public ContextAttribs(int majorVersion, int minorVersion, int profileMask, int contextFlags) {
		if ( majorVersion < 0 || 4 < majorVersion ||
		     minorVersion < 0 ||
		     (majorVersion == 4 && 5 < minorVersion) ||
		     (majorVersion == 3 && 3 < minorVersion) ||
		     (majorVersion == 2 && 1 < minorVersion) ||
		     (majorVersion == 1 && 5 < minorVersion) )
			throw new IllegalArgumentException("Invalid OpenGL version specified: " + majorVersion + '.' + minorVersion);

		if ( LWJGLUtil.CHECKS ) {
			if ( 1 < Integer.bitCount(profileMask) || CONTEXT_ES2_PROFILE_BIT_EXT < profileMask )
				throw new IllegalArgumentException("Invalid profile mask specified: " + Integer.toBinaryString(profileMask));

			if ( 0xF < contextFlags )
				throw new IllegalArgumentException("Invalid context flags specified: " + Integer.toBinaryString(profileMask));
		}

		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;

		this.profileMask = profileMask;
		this.contextFlags = contextFlags;
	}

	// Copy constructor
	private ContextAttribs(ContextAttribs other) {
		this.majorVersion = other.majorVersion;
		this.minorVersion = other.minorVersion;

		this.profileMask = other.profileMask;
		this.contextFlags = other.contextFlags;

		this.contextResetNotificationStrategy = other.contextResetNotificationStrategy;
		this.contextReleaseBehavior = other.contextReleaseBehavior;

		this.layerPlane = other.layerPlane;
	}

	// GETTERS

	/** Returns the {@link #CONTEXT_MAJOR_VERSION_ARB} value. */
	public int getMajorVersion() {
		return majorVersion;
	}

	/** Returns the {@link #CONTEXT_MINOR_VERSION_ARB} value. */
	public int getMinorVersion() {
		return minorVersion;
	}

	/** Returns the {@link #CONTEXT_PROFILE_MASK_ARB} value. */
	public int getProfileMask() {
		return profileMask;
	}

	private boolean hasMask(int mask) {
		return profileMask == mask;
	}

	/** Returns true if the {@link #CONTEXT_CORE_PROFILE_BIT_ARB} has been set. */
	public boolean isProfileCore() {
		return hasMask(CONTEXT_CORE_PROFILE_BIT_ARB);
	}

	/** Returns true if the {@link #CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB} has been set. */
	public boolean isProfileCompatibility() {
		return hasMask(CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB);
	}

	/** Returns true if the {@link #CONTEXT_ES2_PROFILE_BIT_EXT} has been set. */
	public boolean isProfileES() {
		return hasMask(CONTEXT_ES2_PROFILE_BIT_EXT);
	}

	/** Returns the {@link #CONTEXT_FLAGS_ARB} value. */
	public int getContextFlags() {
		return contextFlags;
	}

	private boolean hasFlag(int flag) {
		return (contextFlags & flag) != 0;
	}

	/** Returns true if the {@link #CONTEXT_DEBUG_BIT_ARB} has been set. */
	public boolean isDebug() {
		return hasFlag(CONTEXT_DEBUG_BIT_ARB);
	}

	/** Returns true if the {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB} has been set. */
	public boolean isForwardCompatible() {
		return hasFlag(CONTEXT_FORWARD_COMPATIBLE_BIT_ARB);
	}

	/** Returns true if the {@link #CONTEXT_ROBUST_ACCESS_BIT_ARB} has been set. */
	public boolean isRobustAccess() { return hasFlag(CONTEXT_ROBUST_ACCESS_BIT_ARB); }

	/** Returns true if the {@link #CONTEXT_RESET_ISOLATION_BIT_ARB} has been set. */
	public boolean isContextResetIsolation() {
		return hasFlag(CONTEXT_RESET_ISOLATION_BIT_ARB);
	}

	/** Returns the {@link #CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB} value. */
	public int getContextResetNotificationStrategy() {
		return contextResetNotificationStrategy;
	}

	/**
	 * Returns true if the {@link #CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB} has been set to {@link #LOSE_CONTEXT_ON_RESET_ARB}.
	 *
	 * @deprecated use {@link #getContextResetNotificationStrategy} instead
	 */
	public boolean isLoseContextOnReset() { return contextResetNotificationStrategy == LOSE_CONTEXT_ON_RESET_ARB; }

	/** Returns the {@link #CONTEXT_RELEASE_BEHABIOR_ARB} value. */
	public int getContextReleaseBehavior() {
		return contextReleaseBehavior;
	}

	/** Returns the {@link #CONTEXT_LAYER_PLANE_ARB} value. */
	public int getLayerPlane() {
		return layerPlane;
	}

	// CHAIN CONFIGURATION PATTERN

	private ContextAttribs toggleMask(int mask, boolean value) {
		if ( value == hasMask(mask) )
			return this;

		ContextAttribs attribs = new ContextAttribs(this);
		attribs.profileMask = value ? mask : 0;
		return attribs;
	}

	/**
	 * Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_CORE_PROFILE_BIT_ARB} bit in {@link #CONTEXT_PROFILE_MASK_ARB} set to the given value.
	 * If {@code profileCore} is true, all other bits in the mask are cleared.
	 */
	public ContextAttribs withProfileCore(boolean profileCore) {
		if ( majorVersion < 3 || (majorVersion == 3 && minorVersion < 2) )
			throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");

		return toggleMask(CONTEXT_CORE_PROFILE_BIT_ARB, profileCore);
	}

	/**
	 * Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB} bit in {@link #CONTEXT_PROFILE_MASK_ARB} set to the given value.
	 * If {@code profileCompatibility} is true, all other bits in the mask are cleared.
	 */
	public ContextAttribs withProfileCompatibility(boolean profileCompatibility) {
		if ( majorVersion < 3 || (majorVersion == 3 && minorVersion < 2) )
			throw new IllegalArgumentException("Profiles are only supported on OpenGL version 3.2 or higher.");

		return toggleMask(CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB, profileCompatibility);
	}

	/**
	 * Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_ES2_PROFILE_BIT_EXT} bit in {@link #CONTEXT_PROFILE_MASK_ARB} set to the given value.
	 * If {@code profileES} is true, all other bits in the mask are cleared.
	 */
	public ContextAttribs withProfileES(boolean profileES) {
		if ( !(majorVersion == 2 && minorVersion == 0) )
			throw new IllegalArgumentException("The OpenGL ES profile is only supported on OpenGL version 2.0.");

		return toggleMask(CONTEXT_ES2_PROFILE_BIT_EXT, profileES);
	}

	private ContextAttribs toggleFlag(int flag, boolean value) {
		if ( value == hasFlag(flag) )
			return this;

		ContextAttribs attribs = new ContextAttribs(this);
		attribs.contextFlags ^= flag; // toggle bit
		return attribs;
	}

	/** Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_DEBUG_BIT_ARB} bit in {@link #CONTEXT_FLAGS_ARB} set to the given value. */
	public ContextAttribs withDebug(boolean debug) { return toggleFlag(CONTEXT_DEBUG_BIT_ARB, debug); }

	/** Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_FORWARD_COMPATIBLE_BIT_ARB} bit in {@link #CONTEXT_FLAGS_ARB} set to the given value. */
	public ContextAttribs withForwardCompatible(boolean forwardCompatible) { return toggleFlag(CONTEXT_FORWARD_COMPATIBLE_BIT_ARB, forwardCompatible); }

	/** Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_ROBUST_ACCESS_BIT_ARB} bit in {@link #CONTEXT_FLAGS_ARB} set to the given value. */
	public ContextAttribs withRobustAccess(boolean robustAccess) { return toggleFlag(CONTEXT_ROBUST_ACCESS_BIT_ARB, robustAccess); }

	/** Returns a new {@code ContextAttribs} instance with the {@link #CONTEXT_RESET_ISOLATION_BIT_ARB} bit in {@link #CONTEXT_FLAGS_ARB} set to the given value. */
	public ContextAttribs withContextResetIsolation(boolean contextResetIsolation) { return toggleFlag(CONTEXT_RESET_ISOLATION_BIT_ARB, contextResetIsolation); }

	/**
	 * Returns a ContextAttribs instance with {@link #CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB} set to the given strategy. The default context reset notification
	 * strategy is {@link #NO_RESET_NOTIFICATION_ARB}.
	 *
	 * @param strategy the context reset notification strategy. One of:<br>{@link #NO_RESET_NOTIFICATION_ARB}, {@link #LOSE_CONTEXT_ON_RESET_ARB}
	 *
	 * @return the new ContextAttribs
	 */
	public ContextAttribs withResetNotificationStrategy(int strategy) {
		if ( strategy == contextResetNotificationStrategy )
			return this;

		if ( LWJGLUtil.CHECKS && !(strategy == NO_RESET_NOTIFICATION_ARB || strategy == LOSE_CONTEXT_ON_RESET_ARB) )
			throw new IllegalArgumentException("Invalid context reset notification strategy specified: 0x" + LWJGLUtil.toHexString(strategy));

		ContextAttribs attribs = new ContextAttribs(this);
		attribs.contextResetNotificationStrategy = strategy;
		return attribs;
	}

	/**
	 * Returns a ContextAttribs instance with {@link #CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB} set to {@link #LOSE_CONTEXT_ON_RESET_ARB} if the parameter is
	 * true or to {@link #NO_RESET_NOTIFICATION_ARB} if the parameter is false.
	 *
	 * @param loseContextOnReset the context reset notification strategy
	 *
	 * @return the new ContextAttribs
	 *
	 * @deprecated use {@link #withResetNotificationStrategy} instead
	 */
	public ContextAttribs withLoseContextOnReset(boolean loseContextOnReset) {
		return withResetNotificationStrategy(loseContextOnReset ? LOSE_CONTEXT_ON_RESET_ARB : NO_RESET_NOTIFICATION_ARB);
	}

	/**
	 * Returns a ContextAttribs instance with {@link #CONTEXT_RELEASE_BEHABIOR_ARB} set to the given behavior. The default context release behavior is
	 * {@link #CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB}.
	 *
	 * @param behavior the context release behavior. One of:<br>{@link #CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB}, {@link #CONTEXT_RELEASE_BEHAVIOR_NONE_ARB}
	 *
	 * @return the new ContextAttribs
	 */
	public ContextAttribs withContextReleaseBehavior(int behavior) {
		if ( behavior == contextReleaseBehavior )
			return this;

		if ( LWJGLUtil.CHECKS && !(behavior == CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB || behavior == CONTEXT_RELEASE_BEHAVIOR_NONE_ARB) )
			throw new IllegalArgumentException("Invalid context release behavior specified: 0x" + LWJGLUtil.toHexString(behavior));

		ContextAttribs attribs = new ContextAttribs(this);
		attribs.contextReleaseBehavior = behavior;
		return attribs;
	}

	/** Returns a new {@code ContextAttribs} instance with {@link #CONTEXT_LAYER_PLANE_ARB} set to the given value. */
	public ContextAttribs withLayer(int layerPlane) {
		if ( LWJGLUtil.getPlatform() != LWJGLUtil.PLATFORM_WINDOWS )
			throw new IllegalArgumentException("The CONTEXT_LAYER_PLANE_ARB attribute is supported only on the Windows platform.");

		if ( layerPlane == this.layerPlane )
			return this;

		if ( layerPlane < 0 )
			throw new IllegalArgumentException("Invalid layer plane specified: " + layerPlane);

		ContextAttribs attribs = new ContextAttribs(this);
		attribs.layerPlane = layerPlane;
		return attribs;
	}

	IntBuffer getAttribList() {
		if ( LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX )
			return null;

		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>(8);

		if ( !(majorVersion == 1 && minorVersion == 0) ) {
			map.put(CONTEXT_MAJOR_VERSION_ARB, majorVersion);
			map.put(CONTEXT_MINOR_VERSION_ARB, minorVersion);
		}

		if ( contextFlags != 0 )
			map.put(CONTEXT_FLAGS_ARB, contextFlags);

		if ( profileMask != 0 )
			map.put(CONTEXT_PROFILE_MASK_ARB, profileMask);

		if ( contextResetNotificationStrategy != NO_RESET_NOTIFICATION_ARB )
			map.put(CONTEXT_RESET_NOTIFICATION_STRATEGY_ARB, contextResetNotificationStrategy);

		if ( contextReleaseBehavior != CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB )
			map.put(CONTEXT_RELEASE_BEHABIOR_ARB, contextReleaseBehavior);

		if ( layerPlane != 0 )
			map.put(CONTEXT_LAYER_PLANE_ARB, layerPlane);

		if ( map.isEmpty() )
			return null;

		IntBuffer attribs = BufferUtils.createIntBuffer((map.size() * 2) + 1);
		for ( Entry<Integer, Integer> attrib : map.entrySet() ) {
			attribs
				.put(attrib.getKey())
				.put(attrib.getValue());
		}
		attribs.put(0);
		attribs.rewind();
		return attribs;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(32);

		sb.append("ContextAttribs:");
		sb.append(" Version=").append(majorVersion).append('.').append(minorVersion);

		if ( profileMask != 0 ) {
			sb.append(", Profile=");
			if ( hasMask(CONTEXT_CORE_PROFILE_BIT_ARB) )
				sb.append("CORE");
			else if ( hasMask(CONTEXT_COMPATIBILITY_PROFILE_BIT_ARB) )
				sb.append("COMPATIBLITY");
			else if ( hasMask(CONTEXT_ES2_PROFILE_BIT_EXT) )
				sb.append("ES2");
			else
				sb.append("*unknown*");
		}

		if ( contextFlags != 0 ) {
			if ( hasFlag(CONTEXT_DEBUG_BIT_ARB) )
				sb.append(", DEBUG");
			if ( hasFlag(CONTEXT_FORWARD_COMPATIBLE_BIT_ARB) )
				sb.append(", FORWARD_COMPATIBLE");
			if ( hasFlag(CONTEXT_ROBUST_ACCESS_BIT_ARB) )
				sb.append(", ROBUST_ACCESS");
			if ( hasFlag(CONTEXT_RESET_ISOLATION_BIT_ARB) )
				sb.append(", RESET_ISOLATION");
		}

		if ( contextResetNotificationStrategy != NO_RESET_NOTIFICATION_ARB )
			sb.append(", LOSE_CONTEXT_ON_RESET");
		if ( contextReleaseBehavior != CONTEXT_RELEASE_BEHAVIOR_FLUSH_ARB )
			sb.append(", RELEASE_BEHAVIOR_NONE");

		if ( layerPlane != 0 )
			sb.append(", Layer=").append(layerPlane);

		return sb.toString();
	}

}