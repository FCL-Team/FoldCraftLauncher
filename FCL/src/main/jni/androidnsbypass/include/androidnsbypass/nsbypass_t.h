// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi
#pragma once

/*
 * This file contains various definitions of private API things.
 * It's kept separate in case you need only this.
 */

#ifndef NSBYPASS_NSBYPASS_T_H
#define NSBYPASS_NSBYPASS_T_H

#include <android/dlext.h>

#ifdef __cplusplus
extern "C" {
#endif

// https://cs.android.com/android/platform/superproject/+/android-9.0.0_r1:bionic/linker/dlfcn.cpp;l=48-68
typedef struct android_namespace_t* (*private_create_namespace_t)(
        const char* name,
        const char* ld_library_path,
        const char* default_library_path,
        uint64_t type,
        const char* permitted_when_isolated_path,
        struct android_namespace_t* parent_namespace,
        const void* caller_addr);

typedef bool (*private_link_namespaces_t)(
        struct android_namespace_t* from,
        struct android_namespace_t* to,
        const char* shared_libs_sonames);

typedef bool (*private_link_namespaces_all_libs_t)(
        struct android_namespace_t* from,
        struct android_namespace_t* to);

typedef struct android_namespace_t* (*private_get_exported_namespace_t)(
        const char* name);

// https://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:bionic/linker/dlfcn.cpp;drc=fda4c10ddf33a1c4cb56c58fae98dd9c2239fdc9;l=82-85
typedef int (*private_dlclose_function_t)(
        void *handle);

typedef void *(*private_dlopen_function_t)(
        const char* filename,
        int flags,
        const void* caller_addr);

typedef void *(*private_dlsym_function_t)(
        void* handle,
        const char* symbol,
        const void* caller_addr);

// http://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:bionic/linker/dlfcn.cpp;drc=fda4c10ddf33a1c4cb56c58fae98dd9c2239fdc9;l=58-61
// Just pass __builtin_return_address(0); for caller_addr
// extinfo is nullable and doing so is equivalent to calling __loader_dlopen
typedef void *(*private_dlopen_ext_function_t)(const char* filename,
        int flags,
        const android_dlextinfo* extinfo,
        const void* caller_addr);

// https://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:bionic/libdl/libdl.cpp;drc=a493fe415304efd19f089cbfc7d78c9db7d7263c;l=135-138
typedef void *(*android_dlopen_ext_t)(
        const char* filename,
        int flag,
        const android_dlextinfo* extinfo);

// https://cs.android.com/android/platform/superproject/+/android-latest-release:bionic/libdl/libdl_android.cpp;drc=8e5de06bc59b02641a9fb4a86f921f9534a3bef5;l=117-119
typedef struct android_namespace_t *(*android_get_exported_namespace_t)(
        const char* name);

// https://cs.android.com/android/platform/superproject/+/0a492a4685377d41fef2b12e9af4ebfa6feef9c2:art/libnativeloader/include/nativeloader/dlext_namespaces.h;l=25;bpv=1;bpt=1
enum {
    /*
      Regular namespaces have no restrictions on where they load libraries from.
     */
    ANDROID_NAMESPACE_TYPE_REGULAR = 0,

    // https://cs.android.com/android/platform/superproject/+/android-latest-release:art/libnativeloader/native_loader_namespace.cpp;drc=447ed047cb98b07803f550bca9c833ff11b8d1f1;l=109
    /*
      All apps are in isolated namespaces by default (clns-XX). They are able to load only from
      specified paths at namespace creation, that is a union of ld_library_path,
      default_library_path and permitted_when_isolated_path. This is known as its search paths.
     */
    ANDROID_NAMESPACE_TYPE_ISOLATED = 1,

    /*
      Inherit the callers' search paths, namespace links, and loaded SONAMEs. This is a clone, not
      sharing. They maintain separate lists after creation.
      This means this flag is only relevant on creation.

      WARNING: Search paths and links are not copied on 7.x.
      Android did not update their comments to reflect this.
      See old https://cs.android.com/android/platform/superproject/+/android-7.1.2_r39:bionic/linker/linker.cpp;drc=8c43445152e3372ea284b65845012fdfe7270f82;l=2602-2603
      See new https://cs.android.com/android/platform/superproject/+/android-8.0.0_r1:bionic/linker/linker.cpp;drc=ec5ddc0a2334aaf7a36cbf99deb668e13e5cd717;l=2191-2202
      Commit https://cs.android.com/android/_/android/platform/bionic/+/ec43dd6c36d75014c4e4dc592dd67ab20033a76a

      The inheritance of loaded SONAMEs means all SONAMEs opened by the caller at creation time
      is accessible by dlsym on RTLD_DEFAULT or RTLD_NEXT.

      This can be mixed with ISOLATED or REGULAR.
     */
    ANDROID_NAMESPACE_TYPE_SHARED = 2,

    /*
      See https://cs.android.com/android/platform/superproject/+/android-17.0.0_r1:bionic/linker/linker.cpp;drc=ca1e7187c08ccba8da4b613d2b348fd7efa4e2b7;l=228-244
      See https://cs.android.com/android/platform/superproject/+/android-17.0.0_r1:bionic/linker/linker.cpp;drc=ca1e7187c08ccba8da4b613d2b348fd7efa4e2b7;l=1488-1503

      This is a workaround for some sort of bug that lets a hardcoded list of private api libraries
      load into g_default_namespace instead of this one as a fallback.

      Don't know why you would want to use the system version of those specific libs instead of
      shipping your own, please don't use this.
     */
    ANDROID_NAMESPACE_TYPE_EXEMPT_LIST_ENABLED = 0x08000000,

    /*
      Tells the linker that this is the g_anonymous_namespace to be used for this process.
      The anonymous namespace is used as fallback for when caller_addr can't be resolved
      to a namespace.

      Such a case is when calling from JITted code which exists outside the bounds of an ELF files'
      memory mapping, thus the linker cannot resolve what SONAME said code is executing from and
      thus cannot find which namespace to use.

      Trying to create a 2nd one will result in an error.
     */
    ANDROID_NAMESPACE_TYPE_ALSO_USED_AS_ANONYMOUS = 0x10000000,

    /*
      Convenience combo.
     */
    ANDROID_NAMESPACE_TYPE_SHARED_ISOLATED =
    ANDROID_NAMESPACE_TYPE_SHARED | ANDROID_NAMESPACE_TYPE_ISOLATED,
};

#ifdef __cplusplus
}
#endif

#endif //NSBYPASS_NSBYPASS_T_H
