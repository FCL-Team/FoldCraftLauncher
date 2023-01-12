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

#ifndef _AL_TEST_H
#define _AL_TEST_H

#ifdef _WIN32
#include <windows.h>
#endif

#include <jni.h>
#include "common_tools.h"

#ifdef __cplusplus
extern "C" {
#endif

#if defined(_WIN32)
 #ifdef _OPENAL32LIB
  #define ALCAPI __declspec(dllexport)
 #else
  #define ALCAPI __declspec(dllimport)
 #endif

 #define ALCAPIENTRY __cdecl
#else
  #define ALCAPI
  #define ALCAPIENTRY

#endif

#ifdef _WIN32
 #ifdef _OPENAL32LIB
  #define ALAPI __declspec(dllexport)
 #else
  #define ALAPI __declspec(dllimport)
 #endif
 #define ALAPIENTRY __cdecl
 #define AL_CALLBACK
#else
 #define ALAPI
 #define ALAPIENTRY
 #define AL_CALLBACK
#endif

#define INITGUID
#define OPENAL

// ALC typedefs
typedef struct ALCdevice_struct ALCdevice;
typedef struct ALCcontext_struct ALCcontext;
/** 8-bit boolean */
typedef char ALCboolean;

/** character */
typedef char ALCchar;

/** signed 8-bit 2's complement integer */
typedef char ALCbyte;

/** unsigned 8-bit integer */
typedef unsigned char ALCubyte;

/** signed 16-bit 2's complement integer */
typedef short ALCshort;

/** unsigned 16-bit integer */
typedef unsigned short ALCushort;

/** signed 32-bit 2's complement integer */
typedef int ALCint;

/** unsigned 32-bit integer */
typedef unsigned int ALCuint;

/** non-negative 32-bit binary integer size */
typedef int ALCsizei;

/** enumerated 32-bit value */
typedef int ALCenum;

/** 32-bit IEEE754 floating-point */
typedef float ALCfloat;

/** 64-bit IEEE754 floating-point */
typedef double ALCdouble;

/** void type (for opaque pointers only) */
typedef void ALCvoid;

// AL typedefs
/** 8-bit boolean */
typedef char ALboolean;

/** character */
typedef char ALchar;

/** signed 8-bit 2's complement integer */
typedef char ALbyte;

/** unsigned 8-bit integer */
typedef unsigned char ALubyte;

/** signed 16-bit 2's complement integer */
typedef short ALshort;

/** unsigned 16-bit integer */
typedef unsigned short ALushort;

/** signed 32-bit 2's complement integer */
typedef int ALint;

/** unsigned 32-bit integer */
typedef unsigned int ALuint;

/** non-negative 32-bit binary integer size */
typedef int ALsizei;

/** enumerated 32-bit value */
typedef int ALenum;

/** 32-bit IEEE754 floating-point */
typedef float ALfloat;

/** 64-bit IEEE754 floating-point */
typedef double ALdouble;

/** void type (for opaque pointers only) */
typedef void ALvoid;

void* extal_GetProcAddress(const char* function);
void extal_InitializeClass(JNIEnv *env, jclass clazz, int num_functions, JavaMethodAndExtFunction *functions);

/* Platform dependent functions */
void *extal_NativeGetFunctionPointer(const char *function);
void extal_LoadLibrary(JNIEnv *env, jstring path);
void extal_UnloadLibrary();

#ifdef __cplusplus
}
#endif

#endif
