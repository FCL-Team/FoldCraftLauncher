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
package org.lwjgl.util.mapped;

/**
 * Factory for mapped sets. A mapped set can be used as a Structure of Arrays by
 * linking together the view of two or more mapped objects. Changing the view
 * of the mapped set, changes the corresponding view of all the mapped objects in
 * the set.
 */
public class MappedSet {

	/**
	 * Creates a <code>MappedSet</code> by linking the specified <code>MappedObject</code>s.
	 *
	 * @return the mapped set.
	 */
	public static MappedSet2 create(MappedObject a, MappedObject b) {
		return new MappedSet2(a, b);
	}

	/**
	 * Creates a <code>MappedSet</code> by linking the specified <code>MappedObject</code>s.
	 *
	 * @return the mapped set.
	 */
	public static MappedSet3 create(MappedObject a, MappedObject b, MappedObject c) {
		return new MappedSet3(a, b, c);
	}

	/**
	 * Creates a <code>MappedSet</code> by linking the specified <code>MappedObject</code>s.
	 *
	 * @return the mapped set.
	 */
	public static MappedSet4 create(MappedObject a, MappedObject b, MappedObject c, MappedObject d) {
		return new MappedSet4(a, b, c, d);
	}

}