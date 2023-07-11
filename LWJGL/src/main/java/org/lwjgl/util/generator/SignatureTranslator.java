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

/**
 *
 * A TypeVisitor that translates types to JNI signatures.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

import org.lwjgl.PointerBuffer;
import org.lwjgl.PointerWrapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.util.SimpleTypeVisitor6;

class SignatureTranslator extends SimpleTypeVisitor6<Void, Void> {
	private final StringBuilder signature = new StringBuilder();

	SignatureTranslator() {}

	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	private static String getNativeNameFromClassName(String class_name) {
		return DOT_PATTERN.matcher(class_name).replaceAll("/");
	}

	public String getSignature() {
		return signature.toString();
	}

	@Override
	public Void visitArray(ArrayType t, Void o) {
		final Class type = Utils.getJavaType(t.getComponentType());
		if ( CharSequence.class.isAssignableFrom(type) )
			signature.append("J");
		else if ( Buffer.class.isAssignableFrom(type) )
			signature.append("[Ljava/nio/ByteBuffer;");
		else if ( PointerWrapper.class.isAssignableFrom(type) )
			signature.append("[L" + getNativeNameFromClassName(type.getName()) + ";");
		else
			throw new RuntimeException(t + " is not allowed");
		return DEFAULT_VALUE;
	}

	private void visitClassType(DeclaredType t) {
		Class type = NativeTypeTranslator.getClassFromType(t);

		if ( PointerWrapper.class.isAssignableFrom(type) || (Utils.isAddressableType(type) && !String.class.equals(type)) )
			signature.append("J");
		else {
			String type_name;
			if ( (CharSequence.class.isAssignableFrom(type) && !String.class.equals(type)) || CharSequence[].class.isAssignableFrom(type) || PointerBuffer.class.isAssignableFrom(type) )
				type_name = ByteBuffer.class.getName();
			else
				type_name = t.toString();

			signature.append("L");
			signature.append(getNativeNameFromClassName(type_name));
			signature.append(";");
		}
	}

	@Override
	public Void visitDeclared(DeclaredType t, Void o) {
		if ( t.asElement().getKind().isClass() )
			visitClassType(t);
		else if ( t.asElement().getKind().isInterface() )
			visitInterfaceType(t);
		return DEFAULT_VALUE;
	}

	private void visitInterfaceType(DeclaredType t) {
		Class type = NativeTypeTranslator.getClassFromType(t);
		if ( PointerWrapper.class.isAssignableFrom(type) )
			signature.append("J");
		else
			throw new RuntimeException(t + " is not allowed");
	}

	@Override
	public Void visitPrimitive(PrimitiveType t, Void o) {
		switch ( t.getKind() ) {
			case BOOLEAN:
				signature.append("Z");
				break;
			case INT:
				signature.append("I");
				break;
			case FLOAT:
				signature.append("F");
				break;
			case SHORT:
				signature.append("S");
				break;
			case DOUBLE:
				signature.append("D");
				break;
			case BYTE:
				signature.append("B");
				break;
			case LONG:
				signature.append("J");
				break;
			default:
				throw new RuntimeException("Unsupported type " + t);
		}
		return DEFAULT_VALUE;
	}

	@Override
	public Void visitNoType(NoType t, Void o) {
		signature.append("V");
		return DEFAULT_VALUE;
	}

}
