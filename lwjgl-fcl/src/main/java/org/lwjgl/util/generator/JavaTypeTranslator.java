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
package org.lwjgl.util.generator;

import java.nio.ByteBuffer;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * A TypeVisitor that translates (annotated) TypeMirrors to java types
 * (represented by a Class)
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
public class JavaTypeTranslator extends SimpleTypeVisitor6<Void, Void> {

	private Class type;

	public Class getType() {
		return type;
	}

	@Override
	public Void visitArray(ArrayType t, Void o) {
		final TypeMirror componentType = t.getComponentType();
		try {
			final Class c = Class.forName(t.getComponentType().toString());
			if ( CharSequence.class.isAssignableFrom(c) || ByteBuffer.class.isAssignableFrom(c) || org.lwjgl.PointerWrapper.class.isAssignableFrom(c) ) {
				type = Class.forName("[L" + t.getComponentType() + ";");
			}
		} catch (ClassNotFoundException ex) {
			type = null;
		} finally {
			if ( type == null ) {
				if ( componentType instanceof PrimitiveType ) {
					type = getPrimitiveArrayClassFromKind(componentType.getKind());
				} else {
					throw new RuntimeException(t + " is not allowed");
				}
			}
			return DEFAULT_VALUE;
		}
	}

	public static Class getPrimitiveClassFromKind(TypeKind kind) {
		switch ( kind ) {
			case LONG:
				return long.class;
			case INT:
				return int.class;
			case DOUBLE:
				return double.class;
			case FLOAT:
				return float.class;
			case SHORT:
				return short.class;
			case BYTE:
				return byte.class;
			case BOOLEAN:
				return boolean.class;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
	}

	private static Class getPrimitiveArrayClassFromKind(TypeKind kind) {
		switch ( kind ) {
			case LONG:
				return long[].class;
			case INT:
				return int[].class;
			case DOUBLE:
				return double[].class;
			case FLOAT:
				return float[].class;
			case SHORT:
				return short[].class;
			case BYTE:
				return byte[].class;
			case BOOLEAN:
				return boolean[].class;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
	}

	@Override
	public Void visitPrimitive(PrimitiveType t, Void p) {
		type = getPrimitiveClassFromKind(t.getKind());
		return DEFAULT_VALUE;
	}

	@Override
	public Void visitDeclared(DeclaredType t, Void o) {
		if ( t.asElement().getKind().isClass() ) {
			visitClassType(t);
		} else if ( t.asElement().getKind().isInterface() ) {
			visitInterfaceType(t);
		} else {
			throw new RuntimeException(t.asElement().getKind() + " is not allowed");
		}
		return DEFAULT_VALUE;
	}

	private void visitClassType(DeclaredType t) {
		type = NativeTypeTranslator.getClassFromType(t);
	}

	private void visitInterfaceType(DeclaredType t) {
		type = NativeTypeTranslator.getClassFromType(t);
	}

	@Override
	public Void visitNoType(NoType t, Void p) {
		type = void.class;
		return DEFAULT_VALUE;
	}

}
