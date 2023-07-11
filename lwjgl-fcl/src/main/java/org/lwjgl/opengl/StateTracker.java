/*
 * Copyright (c) 2002-2008 LWJGL Project
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

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL40.*;

final class StateTracker {

	private ReferencesStack references_stack;
	private final StateStack attrib_stack;

	private boolean insideBeginEnd;

	// VAOs are not shareable between contexts, no need to sync or make this static.
	private final FastIntMap<VAOState> vaoMap = new FastIntMap<VAOState>();

	StateTracker() {
		attrib_stack = new StateStack(0);
	}

	/** This is called after getting function addresses. */
	void init() {
		references_stack = new ReferencesStack();
	}

	static void setBeginEnd(ContextCapabilities caps, boolean inside) {
		caps.tracker.insideBeginEnd = inside;
	}

	boolean isBeginEnd() {
		return insideBeginEnd;
	}

	static void popAttrib(ContextCapabilities caps) {
		caps.tracker.doPopAttrib();
	}

	private void doPopAttrib() {
		references_stack.popState(attrib_stack.popState());
	}

	static void pushAttrib(ContextCapabilities caps, int mask) {
		caps.tracker.doPushAttrib(mask);
	}

	private void doPushAttrib(int mask) {
		attrib_stack.pushState(mask);
		references_stack.pushState();
	}

	static References getReferences(ContextCapabilities caps) {
		return caps.tracker.references_stack.getReferences();
	}

	static void bindBuffer(ContextCapabilities caps, int target, int buffer) {
		final BaseReferences references = getReferences(caps);
		switch ( target ) {
			case GL_ARRAY_BUFFER:
				references.arrayBuffer = buffer;
				break;
			case GL_ELEMENT_ARRAY_BUFFER:
				// When a vertex array object is currently bound, update
				// the VAO state instead of client state.
				if ( references.vertexArrayObject != 0 )
					caps.tracker.vaoMap.get(references.vertexArrayObject).elementArrayBuffer = buffer;
				else
					references.elementArrayBuffer = buffer;
				break;
			case GL_PIXEL_PACK_BUFFER:
				references.pixelPackBuffer = buffer;
				break;
			case GL_PIXEL_UNPACK_BUFFER:
				references.pixelUnpackBuffer = buffer;
				break;
			case GL_DRAW_INDIRECT_BUFFER:
				references.indirectBuffer = buffer;
				break;
		}
	}

	static void bindVAO(final ContextCapabilities caps, final int array) {
		final FastIntMap<VAOState> vaoMap = caps.tracker.vaoMap;
		if ( !vaoMap.containsKey(array) )
			vaoMap.put(array, new VAOState());

		getReferences(caps).vertexArrayObject = array;
	}

	static void deleteVAO(final ContextCapabilities caps, final IntBuffer arrays) {
		for ( int i = arrays.position(); i < arrays.limit(); i++ )
			deleteVAO(caps, arrays.get(i));
	}

	static void deleteVAO(final ContextCapabilities caps, final int array) {
		caps.tracker.vaoMap.remove(array);

		final BaseReferences references = getReferences(caps);
		if ( references.vertexArrayObject == array )
			references.vertexArrayObject = 0;
	}

	/**
	 * Returns the currently bound ELEMENT_ARRAY_BUFFER. If a vertex array
	 * object is currently bound, then the VAO state is returned instead
	 * of the client state.
	 *
	 * @return the currently bound ELEMENT_ARRAY_BUFFER.
	 */
	static int getElementArrayBufferBound(final ContextCapabilities caps) {
		final BaseReferences references = getReferences(caps);

		if ( references.vertexArrayObject == 0 )
			return references.elementArrayBuffer;
		else
			return caps.tracker.vaoMap.get(references.vertexArrayObject).elementArrayBuffer;
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