/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.opengles;

import java.nio.Buffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.*;

final class StateTracker {

	private static StateTracker tracker = new StateTracker();

	int elementArrayBuffer;
	int arrayBuffer;
	int pixelPackBuffer;
	int pixelUnpackBuffer;

	Buffer[] glVertexAttribPointer_buffer;

	private final FastIntMap<VAOState> vaoMap = new FastIntMap<VAOState>();

	int vertexArrayObject;

	StateTracker() {
	}

	void init() {
		glVertexAttribPointer_buffer = new Buffer[glGetInteger(GL_MAX_VERTEX_ATTRIBS)];
	}

	static StateTracker getTracker() {
		return tracker;
	}

	static void bindBuffer(int target, int buffer) {
		final StateTracker tracker = getTracker();

		switch ( target ) {
			case GL_ARRAY_BUFFER:
				tracker.arrayBuffer = buffer;
				break;
			case GL_ELEMENT_ARRAY_BUFFER:
				tracker.elementArrayBuffer = buffer;
				break;
			case GL_PIXEL_PACK_BUFFER:
				tracker.pixelPackBuffer = buffer;
				break;
			case GL_PIXEL_UNPACK_BUFFER:
				tracker.pixelUnpackBuffer = buffer;
				break;
		}
	}

	static void bindVAO(final int array) {
		final FastIntMap<VAOState> vaoMap = tracker.vaoMap;
		if ( !vaoMap.containsKey(array) )
			vaoMap.put(array, new VAOState());

		tracker.vertexArrayObject = array;
	}

	static void deleteVAO(final IntBuffer arrays) {
		for ( int i = arrays.position(); i < arrays.limit(); i++ )
			deleteVAO(arrays.get(i));
	}

	static void deleteVAO(final int array) {
		tracker.vaoMap.remove(array);

		if ( tracker.vertexArrayObject == array )
			tracker.vertexArrayObject = 0;
	}

	/**
	 * Simple class to help us track VAO state. Currently
	 * only ELEMENT_ARRAY_BUFFER_BINDING is tracked, since
	 * that's the only state we check from tables 6.6-6.9.
	 */
	private static class VAOState {

		int elementArrayBuffer;

	}

}