/*!A dlopen library that bypasses mobile system limitation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (C) 2020-present, TBOOX Open Source Group.
 *
 * @author      ruki
 * @file        prefix.h
 *
 */
#ifndef BY_PREFIX_H
#define BY_PREFIX_H

#ifdef __cplusplus
extern "C" {
#endif

/* //////////////////////////////////////////////////////////////////////////////////////
 * includes
 */
#include <stdio.h>
#include <ctype.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#if defined(__ANDROID__)
#   include <jni.h>
#   include <android/log.h>
#elif defined(__ENVIRONMENT_IPHONE_OS_VERSION_MIN_REQUIRED__)
#   include <asl.h>
#endif

/* //////////////////////////////////////////////////////////////////////////////////////
 * macros
 */

// bool values
#ifdef __ANDROID__
#   define by_true                              ((by_bool_t)JNI_TRUE)
#   define by_false                             ((by_bool_t)JNI_FALSE)
#else
#   define by_true                              ((by_bool_t)1)
#   define by_false                             ((by_bool_t)0)
#endif

// null
#ifdef __cplusplus
#   define by_null                              (0)
#else
#   define by_null                              ((by_pointer_t)0)
#endif

// print
#define by_print(fmt, arg ...)                  by_printf(fmt "\n", ## arg)

// trace
#ifdef BY_DEBUG
#   define by_tracef(fmt, arg ...)              by_printf(fmt, ## arg)
#   define by_trace(fmt, arg ...)               by_printf(fmt "\n", ## arg)
#   define by_trace_line(fmt, arg ...)          by_printf(fmt " at func: %s, line: %d, file: %s\n", ##arg, __FUNCTION__, __LINE__, __FILE__)
#else
#   define by_tracef(...)
#   define by_trace(...)
#   define by_trace_line(...)
#endif

// check
#define by_check_return(x)                      do { if (!(x)) return ; } while (0)
#define by_check_return_val(x, v)               do { if (!(x)) return (v); } while (0)
#define by_check_goto(x, b)                     do { if (!(x)) goto b; } while (0)
#define by_check_break(x)                       { if (!(x)) break ; }
#define by_check_continue(x)                    { if (!(x)) continue ; }

// assert
#ifdef BY_DEBUG
#   define by_assert(x)                         do { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); } } while(0)
#   define by_assert_return(x)                  do { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); return ; } } while(0)
#   define by_assert_return_val(x, v)           do { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); return (v); } } while(0)
#   define by_assert_goto(x, b)                 do { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); goto b; } } while(0)
#   define by_assert_break(x)                   { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); break ; } }
#   define by_assert_continue(x)                { if (!(x)) {by_trace_line("[assert]: expr: %s", #x); continue ; } }
#   define by_assert_and_check_return(x)        by_assert_return(x)
#   define by_assert_and_check_return_val(x, v) by_assert_return_val(x, v)
#   define by_assert_and_check_goto(x, b)       by_assert_goto(x, b)
#   define by_assert_and_check_break(x)         by_assert_break(x)
#   define by_assert_and_check_continue(x)      by_assert_continue(x)
#else
#   define by_assert(x)
#   define by_assert_return(x)
#   define by_assert_return_val(x, v)
#   define by_assert_goto(x, b)
#   define by_assert_break(x)
#   define by_assert_continue(x)
#   define by_assert_and_check_return(x)        by_check_return(x)
#   define by_assert_and_check_return_val(x, v) by_check_return_val(x, v)
#   define by_assert_and_check_goto(x, b)       by_check_goto(x, b)
#   define by_assert_and_check_break(x)         by_check_break(x)
#   define by_assert_and_check_continue(x)      by_check_continue(x)
#endif

// printf 
#if defined(__ANDROID__)
#   define by_printf(fmt, arg ...)  __android_log_print(ANDROID_LOG_INFO, "byOpen", fmt, ## arg)
#elif defined(__ENVIRONMENT_IPHONE_OS_VERSION_MIN_REQUIRED__)
#   define by_printf(fmt, arg ...)  asl_log(by_null, by_null, ASL_LEVEL_WARNING, "[byOpen]: " fmt, ## arg);
#else
#   define by_printf(fmt, arg ...)  printf(fmt, ## arg)
#endif

/* arch
 *
 * gcc builtin macros for gcc -dM -E - < /dev/null
 *
 * .e.g gcc -m64 -dM -E - < /dev/null | grep 64
 * .e.g gcc -m32 -dM -E - < /dev/null | grep 86
 * .e.g gcc -march=armv6 -dM -E - < /dev/null | grep ARM
 */
#if defined(__i386) \
    || defined(__i686) \
    || defined(__i386__) \
    || defined(__i686__) \
    || defined(_M_IX86)
#   define BY_ARCH_x86
#elif defined(__x86_64) \
    || defined(__amd64__) \
    || defined(__amd64) \
    || defined(_M_IA64) \
    || defined(_M_X64)
#   define BY_ARCH_x64
#elif defined(__arm__) || defined(__arm64) || defined(__arm64__) || (defined(__aarch64__) && __aarch64__)
#   define BY_ARCH_ARM
#   if defined(__ARM64_ARCH_8__)
#       define BY_ARCH_ARM64
#       define BY_ARCH_ARM_v8
#   elif defined(__ARM_ARCH_7A__)
#       define BY_ARCH_ARM_v7A
#   elif defined(__ARM_ARCH_7__)
#       define BY_ARCH_ARM_v7
#   elif defined(__ARM_ARCH_6__)
#       define BY_ARCH_ARM_v6
#   elif defined(__ARM_ARCH_5TE__)
#       define BY_ARCH_ARM_v5te
#   elif defined(__ARM_ARCH_5__)
#       define BY_ARCH_ARM_v5
#   elif defined(__ARM_ARCH_4T__)
#       define BY_ARCH_ARM_v4t
#   elif defined(__ARM_ARCH)
#       define BY_ARCH_ARM_VERSION          __ARM_ARCH
#       if __ARM_ARCH >= 8
#           define BY_ARCH_ARM_v8
#           if defined(__arm64) || defined(__arm64__)
#               define BY_ARCH_ARM64
#           elif (defined(__aarch64__) && __aarch64__)
#               define BY_ARCH_ARM64
#           endif
#       elif __ARM_ARCH >= 7
#           define BY_ARCH_ARM_v7
#       elif __ARM_ARCH >= 6
#           define BY_ARCH_ARM_v6
#       else
#           define BY_ARCH_ARM_v5
#       endif
#   elif defined(__aarch64__) && __aarch64__
#       define BY_ARCH_ARM_v8
#       define BY_ARCH_ARM64
#   else 
#       error unknown arm arch version
#   endif
#   if !defined(BY_ARCH_ARM64) && (defined(__arm64) || defined(__arm64__) || (defined(__aarch64__) && __aarch64__))
#       define BY_ARCH_ARM64
#   endif
#   if defined(__thumb__)
#       define BY_ARCH_ARM_THUMB
#   endif
#   if defined(__ARM_NEON__)
#       define BY_ARCH_ARM_NEON
#   endif 
#else
#   error unknown arch
#endif

/* //////////////////////////////////////////////////////////////////////////////////////
 * types
 */

// basic
typedef signed int                  by_int_t;
typedef unsigned int                by_uint_t;
typedef signed long                 by_long_t;
typedef unsigned long               by_ulong_t;
typedef by_ulong_t                  by_size_t;
typedef by_int_t                    by_bool_t;
typedef signed char                 by_int8_t;
typedef by_int8_t                   by_sint8_t;
typedef unsigned char               by_uint8_t;
typedef signed short                by_int16_t;
typedef by_int16_t                  by_sint16_t;
typedef unsigned short              by_uint16_t;
typedef by_int_t                    by_int32_t;
typedef by_int32_t                  by_sint32_t;
typedef by_uint_t                   by_uint32_t;
typedef char                        by_char_t;
typedef by_int32_t                  by_wchar_t;
typedef by_int32_t                  by_uchar_t;
typedef by_uint8_t                  by_byte_t;
typedef void                        by_void_t;
typedef by_void_t*                  by_pointer_t;
typedef by_void_t const*            by_cpointer_t;
typedef by_pointer_t                by_handle_t;
typedef signed long long            by_int64_t;
typedef unsigned long long          by_uint64_t;
typedef by_int64_t                  by_sint64_t;
typedef by_sint64_t                 by_hong_t;
typedef by_uint64_t                 by_hize_t;
typedef float                       by_float_t;
typedef double                      by_double_t;

#ifdef __cplusplus
}
#endif
#endif
