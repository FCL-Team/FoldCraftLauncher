/*
 * Copyright (c) 2002-2010 LWJGL Project
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
package org.lwjgl.opencl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opencl.api.CLImageFormat;
import org.lwjgl.opencl.api.Filter;
import org.lwjgl.opengl.Drawable;

import java.nio.IntBuffer;
import java.util.List;

/**
 * This class is a wrapper around a cl_context pointer.
 *
 * @author Spasi
 */
public final class CLContext extends CLObjectChild<CLPlatform> {

	private static final CLContextUtil util = (CLContextUtil)CLPlatform.getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");

	private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
	private final CLObjectRegistry<CLMem>          clMems;
	private final CLObjectRegistry<CLSampler>      clSamplers;
	private final CLObjectRegistry<CLProgram>      clPrograms;
	private final CLObjectRegistry<CLEvent>        clEvents;

	private long
		contextCallback,
		printfCallback;

	CLContext(final long pointer, final CLPlatform platform) {
		super(pointer, platform);

		// We do not need to register the context with the platform,
		// there is no API that returns cl_context, except clCreateContext.

		if ( isValid() ) {
			clCommandQueues = new CLObjectRegistry<CLCommandQueue>();
			clMems = new CLObjectRegistry<CLMem>();
			clSamplers = new CLObjectRegistry<CLSampler>();
			clPrograms = new CLObjectRegistry<CLProgram>();
			clEvents = new CLObjectRegistry<CLEvent>();
		} else {
			clCommandQueues = null;
			clMems = null;
			clSamplers = null;
			clPrograms = null;
			clEvents = null;
		}
	}

	/**
	 * Returns a CLCommandQueue associated with this context.
	 *
	 * @param id the command queue object id
	 *
	 * @return the CLCommandQueue object
	 */
	public CLCommandQueue getCLCommandQueue(final long id) { return clCommandQueues.getObject(id); }

	/**
	 * Returns a CLMem associated with this context.
	 *
	 * @param id the memory object id
	 *
	 * @return the CLMem object
	 */
	public CLMem getCLMem(final long id) { return clMems.getObject(id); }

	/**
	 * Returns a CLSampler associated with this context.
	 *
	 * @param id the sampler object id
	 *
	 * @return the CLSampler object
	 */
	public CLSampler getCLSampler(final long id) { return clSamplers.getObject(id); }

	/**
	 * Returns a CLProgram associated with this context.
	 *
	 * @param id the program object id
	 *
	 * @return the CLProgram object
	 */
	public CLProgram getCLProgram(final long id) { return clPrograms.getObject(id); }

	/**
	 * Returns a user CLEvent associated with this context.
	 *
	 * @param id the event object id
	 *
	 * @return the CLEvent object
	 */
	public CLEvent getCLEvent(final long id) { return clEvents.getObject(id); }

	// ---------------[ UTILITY METHODS ]---------------

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform    the platform to use
	 * @param devices     the devices to use
	 * @param errcode_ret the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final IntBuffer errcode_ret) throws LWJGLException {
		return create(platform, devices, null, null, errcode_ret);
	}

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform    the platform to use
	 * @param devices     the devices to use
	 * @param pfn_notify  the context callback function
	 * @param errcode_ret the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final IntBuffer errcode_ret) throws LWJGLException {
		return create(platform, devices, pfn_notify, null, errcode_ret);
	}

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform       the platform to use
	 * @param devices        the devices to use
	 * @param share_drawable the OpenGL drawable to share objects with
	 * @param errcode_ret    the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext create(final CLPlatform platform, final List<CLDevice> devices, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
		return util.create(platform, devices, pfn_notify, share_drawable, errcode_ret);
	}

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform    the platform to use
	 * @param device_type the device type to use
	 * @param errcode_ret the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext createFromType(final CLPlatform platform, final long device_type, final IntBuffer errcode_ret) throws LWJGLException {
		return util.createFromType(platform, device_type, null, null, errcode_ret);
	}

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform    the platform to use
	 * @param device_type the device type to use
	 * @param pfn_notify  the context callback function
	 * @param errcode_ret the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final IntBuffer errcode_ret) throws LWJGLException {
		return util.createFromType(platform, device_type, pfn_notify, null, errcode_ret);
	}

	/**
	 * Creates a new CLContext.
	 *
	 * @param platform       the platform to use
	 * @param device_type    the device type to use
	 * @param share_drawable the OpenGL drawable to share objects with
	 * @param errcode_ret    the error code result
	 *
	 * @return the new CLContext
	 *
	 * @throws LWJGLException if an exception occurs while creating the context
	 */
	public static CLContext createFromType(final CLPlatform platform, final long device_type, final CLContextCallback pfn_notify, final Drawable share_drawable, final IntBuffer errcode_ret) throws LWJGLException {
		return util.createFromType(platform, device_type, pfn_notify, share_drawable, errcode_ret);
	}

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getInfoInt(int param_name) {
		return util.getInfoInt(this, param_name);
	}

	/**
	 * Returns the list of devices in context.
	 *
	 * @return the list of devices
	 */
	public List<CLDevice> getInfoDevices() {
		return util.getInfoDevices(this);
	}

	public List<CLImageFormat> getSupportedImageFormats(final long flags, final int image_type) {
		return getSupportedImageFormats(flags, image_type, null);
	}

	public List<CLImageFormat> getSupportedImageFormats(final long flags, final int image_type, final Filter<CLImageFormat> filter) {
		return util.getSupportedImageFormats(this, flags, image_type, filter);
	}

	/** CLContext utility methods interface. */
	interface CLContextUtil extends InfoUtil<CLContext> {

		List<CLDevice> getInfoDevices(CLContext context);

		CLContext create(CLPlatform platform, List<CLDevice> devices, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException;

		CLContext createFromType(CLPlatform platform, long device_type, CLContextCallback pfn_notify, Drawable share_drawable, IntBuffer errcode_ret) throws LWJGLException;

		List<CLImageFormat> getSupportedImageFormats(CLContext context, final long flags, final int image_type, Filter<CLImageFormat> filter);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() { return clCommandQueues; }

	CLObjectRegistry<CLMem> getCLMemRegistry() { return clMems; }

	CLObjectRegistry<CLSampler> getCLSamplerRegistry() { return clSamplers; }

	CLObjectRegistry<CLProgram> getCLProgramRegistry() { return clPrograms; }

	CLObjectRegistry<CLEvent> getCLEventRegistry() { return clEvents; }

	private boolean checkCallback(final long callback, final int result) {
		if ( result == 0 && (callback == 0 || isValid()) )
			return true;

		if ( callback != 0 )
			CallbackUtil.deleteGlobalRef(callback);
		return false;
	}

	/**
	 * Associates this context with the specified context callback reference. If the context
	 * is invalid, the callback reference is deleted. NO-OP if user_data is 0.
	 *
	 * @param callback the context callback pointer
	 */
	void setContextCallback(final long callback) {
		if ( checkCallback(callback, 0) )
			this.contextCallback = callback;
	}

	/**
	 * Associates this context with the specified printf callback reference. If the context
	 * is invalid, the callback reference is deleted. NO-OP if user_data is 0.
	 *
	 * @param callback the printf callback pointer
	 */
	void setPrintfCallback(final long callback, final int result) {
		if ( checkCallback(callback, result) )
			this.printfCallback = callback;
	}

	/**
	 * Decrements the context's reference count. If the reference
	 * count hits zero, it also deletes
	 * any callback objects associated with it.
	 */
	void releaseImpl() {
		if ( release() > 0 )
			return;

		if ( contextCallback != 0 )
			CallbackUtil.deleteGlobalRef(contextCallback);
		if ( printfCallback != 0 )
			CallbackUtil.deleteGlobalRef(printfCallback);
	}

}