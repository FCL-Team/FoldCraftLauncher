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

import org.lwjgl.PointerWrapperAbstract;

import java.nio.ByteBuffer;

/**
 * Instances of this class can be used to receive OpenCL context error notifications.
 *
 * @author Spasi
 */
public abstract class CLContextCallback extends PointerWrapperAbstract {

	private final boolean custom;

	protected CLContextCallback() {
		super(CallbackUtil.getContextCallback());
		custom = false;
	}

	/**
	 * This constructor allows non-LWJGL implementations.
	 *
	 * @param pointer
	 */
	protected CLContextCallback(final long pointer) {
		super(pointer);

		if ( pointer == 0 )
			throw new RuntimeException("Invalid callback function pointer specified.");

		custom = true;
	}

	final boolean isCustom() {
		return custom;
	}

	/**
	 * The callback method.
	 *
	 * @param errinfo      the error description
	 * @param private_info optional error data (may be null)
	 */
	protected abstract void handleMessage(String errinfo, ByteBuffer private_info);

}