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
 * The interface to the OpenAL/OpenGL specific generator behaviour
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

public interface TypeMap {
	void printCapabilitiesInit(PrintWriter writer);
	String getCapabilities();
	String getAPIUtilParam(boolean comma);
	void printErrorCheckMethod(PrintWriter writer, ExecutableElement method, String tabs);
	String getRegisterNativesFunctionName();
	TypeKind getPrimitiveTypeFromNativeType(Class<? extends Annotation> native_type);
	String getTypedefPostfix();
	String getFunctionPrefix();
	void printNativeIncludes(PrintWriter writer);
	Class<? extends Annotation> getStringElementType();
	Class<? extends Annotation> getStringArrayType();
	Class<? extends Annotation> getByteBufferArrayType();
	Class[] getValidAnnotationTypes(Class type);
	Class<? extends Annotation> getVoidType();
	String translateAnnotation(Class<? extends Annotation> annotation_type);
	Class getNativeTypeFromPrimitiveType(TypeKind kind);
	String getAutoTypeFromAnnotation(AnnotationMirror annotation);
	Class<? extends Annotation> getInverseType(Class<? extends Annotation> type);
	Signedness getSignednessFromType(Class<? extends Annotation> type);
}
