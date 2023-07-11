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
 * A TypeVisitor that translates (annotated) TypeMirrors to
 * postfixes.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

import java.lang.annotation.Annotation;
import java.nio.*;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.SimpleTypeVisitor6;

public class PostfixTranslator extends SimpleTypeVisitor6<Void, Void> {
	private final StringBuilder signature = new StringBuilder();
	private final Element declaration;
	private final TypeMap type_map;

	public PostfixTranslator(TypeMap type_map, Element declaration) {
		this.declaration = declaration;
		this.type_map = type_map;
	}

	public String getSignature() {
		return signature.toString();
	}

	private static TypeKind getPrimitiveKindFromBufferClass(Class c) {
		if ( IntBuffer.class.equals(c) || int.class.equals(c) )
			return TypeKind.INT;
		else if ( DoubleBuffer.class.equals(c) || double.class.equals(c) )
			return TypeKind.DOUBLE;
		else if ( ShortBuffer.class.equals(c) || short.class.equals(c) )
			return TypeKind.SHORT;
		else if ( ByteBuffer.class.equals(c) || byte.class.equals(c) )
			return TypeKind.BYTE;
		else if ( FloatBuffer.class.equals(c) || float.class.equals(c) )
			return TypeKind.FLOAT;
		else if ( LongBuffer.class.equals(c) || long.class.equals(c) )
			return TypeKind.LONG;
		else
			throw new RuntimeException(c + " is not allowed");
	}

	private void visitClassType(DeclaredType t) {
		Class<?> c = NativeTypeTranslator.getClassFromType(t);
		TypeKind kind = getPrimitiveKindFromBufferClass(c);
		visitPrimitiveTypeKind(kind);
	}

	@Override
	public Void visitDeclared(DeclaredType t, Void o) {
		if ( t.asElement().getKind().isClass() )
			visitClassType(t);
		return DEFAULT_VALUE;
	}

	private boolean translateAnnotation(AnnotationMirror annotation) {
		NativeType native_type = NativeTypeTranslator.getAnnotation(annotation, NativeType.class);
		if ( native_type != null ) {
			Class<? extends Annotation> annotation_class = NativeTypeTranslator.getClassFromType(annotation.getAnnotationType());
			signature.append(type_map.translateAnnotation(annotation_class));
			return true;
		} else
			return false;
	}

	private boolean translateAnnotations() {
		boolean result = false;
		for ( AnnotationMirror annotation : Utils.getSortedAnnotations(declaration.getAnnotationMirrors()) )
			if ( translateAnnotation(annotation) ) {
				if ( result )
					throw new RuntimeException("Multiple native types");
				result = true;
			}
		return result;
	}

	@Override
	public Void visitPrimitive(PrimitiveType t, Void o) {
		visitPrimitiveTypeKind(t.getKind());
		return DEFAULT_VALUE;
	}

	private void visitPrimitiveTypeKind(TypeKind kind) {
		boolean annotated_translation = translateAnnotations();
		if ( annotated_translation )
			return;
		// No annotation type was specified, fall back to default
		String type;
		switch ( kind ) {
			case INT:
				type = "i";
				break;
			case DOUBLE:
				type = "d";
				break;
			case FLOAT:
				type = "f";
				break;
			case SHORT:
				type = "s";
				break;
			case BYTE:
				type = "b";
				break;
			case LONG:
				type = "i64";
				break;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
		signature.append(type);
	}
}