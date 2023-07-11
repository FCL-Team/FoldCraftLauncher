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
 * A TypeVisitor that translates types (and optional native type annotations) to
 * the native type string.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
import org.lwjgl.PointerBuffer;

import java.lang.annotation.Annotation;
import java.nio.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * $Id$
 * <p/>
 * A TypeVisitor that translates (annotated) TypeMirrors to native types
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 */
public class NativeTypeTranslator extends SimpleTypeVisitor6<Void, Void> {

	private       Collection<Class> native_types;
	private       boolean           is_indirect;
	private final Element           declaration;
	private final TypeMap           type_map;

	public NativeTypeTranslator(TypeMap type_map, Element declaration) {
		this.declaration = declaration;
		this.type_map = type_map;
	}

	public String getSignature() {
		return getSignature(false);
	}

	public String getSignature(final boolean skipConst) {
		StringBuilder signature = new StringBuilder();
		if ( !skipConst && declaration.getAnnotation(Const.class) != null ) {
			signature.append("const ");
		}

		if ( declaration.getAnnotation(PointerWrapper.class) != null ) {
			signature.append(declaration.getAnnotation(PointerWrapper.class).value());
		} else if ( declaration.getAnnotation(NativeType.class) != null ) {
			signature.append(declaration.getAnnotation(NativeType.class).value());
		} else {
			// Use the name of the native type annotation as the C type name
			signature.append(getAnnotationType().getSimpleName());
		}

		if ( is_indirect ) {
			signature.append(" *");
		}
		return signature.toString();
	}

	public Class getAnnotationType() {
		if ( native_types.size() != 1 ) {
			throw new RuntimeException("Expected only one native type for declaration " + declaration
			                           + ", but got " + native_types.size());
		}
		return native_types.iterator().next();
	}

	@Override
	public Void visitArray(ArrayType t, Void o) {
		final Class<?> type = Utils.getJavaType(t).getComponentType();

		if ( CharSequence.class.isAssignableFrom(type) ) {
			is_indirect = true;
			native_types = new ArrayList<Class>();
			native_types.add(type_map.getStringArrayType());
		} else if ( Buffer.class.isAssignableFrom(type) ) {
			is_indirect = true;
			native_types = new ArrayList<Class>();
			native_types.add(type_map.getByteBufferArrayType());
		} else if ( org.lwjgl.PointerWrapper.class.isAssignableFrom(type) ) {
			is_indirect = false;
		} else {
			throw new RuntimeException(t + " is not allowed");
		}
		return DEFAULT_VALUE;
	}

	public static TypeKind getPrimitiveKindFromBufferClass(Class c) {
		if ( IntBuffer.class.equals(c) ) {
			return TypeKind.INT;
		} else if ( DoubleBuffer.class.equals(c) ) {
			return TypeKind.DOUBLE;
		} else if ( ShortBuffer.class.equals(c) ) {
			return TypeKind.SHORT;
		} else if ( ByteBuffer.class.equals(c) || PointerBuffer.class.equals(c) ) {
			return TypeKind.BYTE;
		} else if ( FloatBuffer.class.equals(c) ) {
			return TypeKind.FLOAT;
		} else if ( LongBuffer.class.equals(c) ) {
			return TypeKind.LONG;
		} else {
			throw new RuntimeException(c + " is not allowed");
		}
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Annotation> getClassFromType(DeclaredType t) {
		try {
			return (Class<? extends Annotation>)Class.forName(t.toString());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void getNativeTypeFromAnnotatedPrimitiveType(TypeKind kind) {
		native_types = translateAnnotations();
		if ( native_types.isEmpty() ) {
			native_types.add(type_map.getNativeTypeFromPrimitiveType(kind));
		}
	}

	private void visitClassType(DeclaredType t) {
		is_indirect = true;

		Class<?> c = getClassFromType(t);
		if ( String.class.equals(c) ) {
			native_types = new ArrayList<Class>();
			native_types.add(type_map.getStringElementType());
		} else if ( Buffer.class.equals(c) ) {
			native_types = new ArrayList<Class>();
			native_types.add(type_map.getVoidType());
		} else if ( Buffer.class.isAssignableFrom(c) || PointerBuffer.class.isAssignableFrom(c) ) {
			TypeKind kind = getPrimitiveKindFromBufferClass(c);
			getNativeTypeFromAnnotatedPrimitiveType(kind);
		} else if ( org.lwjgl.PointerWrapper.class.isAssignableFrom(c) ) {
			native_types = new ArrayList<Class>();
			native_types.add(PointerWrapper.class);

			is_indirect = false;
		} else {
			throw new RuntimeException(t + " is not allowed");
		}
	}

	@Override
	public Void visitPrimitive(PrimitiveType t, Void p) {
		getNativeTypeFromAnnotatedPrimitiveType(t.getKind());
		return DEFAULT_VALUE;
	}

	private void visitInterfaceType(DeclaredType t) {
		// See ARB_debug_label.glObjectPtrLabel
		Class<?> c = getClassFromType(t);
		if ( org.lwjgl.PointerWrapper.class.isAssignableFrom(c) ) {
			native_types = new ArrayList<Class>();
			native_types.add(PointerWrapper.class);

			is_indirect = false;
		} else {
			throw new RuntimeException(t + " is not allowed");
		}
	}

	@Override
	public Void visitDeclared(DeclaredType t, Void p) {
		if ( t.asElement().getKind().isInterface() ) {
			visitInterfaceType(t);
		} else if ( t.asElement().getKind().isClass() ) {
			visitClassType(t);
		}
		return DEFAULT_VALUE;
	}

	/* Check if the annotation is itself annotated with a certain annotation type
	 * @discuss compare (DeclaredType).getAnnotation(Class) and (Element).getAnnotation(Class), they mean different Annotation's.
	 */
	public static <T extends Annotation> T getAnnotation(AnnotationMirror annotation, Class<T> type) {
		return annotation.getAnnotationType().asElement().getAnnotation(type);
	}

	private static Class translateAnnotation(AnnotationMirror annotation) {
		NativeType native_type = getAnnotation(annotation, NativeType.class);
		if ( native_type != null ) {
			return getClassFromType(annotation.getAnnotationType());
		} else {
			return null;
		}
	}

	private List<Class> translateAnnotations() {
		List<Class> result = new ArrayList<Class>();
		for ( AnnotationMirror annotation : Utils.getSortedAnnotations(declaration.getAnnotationMirrors()) ) {
			Class translated_result = translateAnnotation(annotation);
			if ( translated_result != null ) {
				result.add(translated_result);
			}
		}
		return result;
	}

	@Override
	public Void visitNoType(NoType t, Void p) {
		native_types = translateAnnotations();
		if ( native_types.isEmpty() ) {
			native_types.add(void.class);
		}
		return DEFAULT_VALUE;
	}

}
