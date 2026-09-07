// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi
#pragma once

#ifndef NSBYPASS_NSBYPASS_H
#define NSBYPASS_NSBYPASS_H

#include <android/dlext.h>

#include <stdint.h>
#include <stdbool.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Some basics are in order.
 * See https://source.android.com/docs/core/architecture/vndk/linker-namespace to find what this
 * bypasses.
 *
 * There are two namespaces you can reliably access without creating your own. the classloader
 * namespace, and the default namespace
 * https://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:art/libnativeloader/library_namespaces.cpp
 * Classloader namespace is names like clns-XX with XX being integers. This is provided to every
 * application on startup automatically and looks through your nativeLibraryDir and specified
 * libraries defined as "public API" by android. This is what you have been running in the entire
 * time.
 *
 *
 * g_default_namespace is the private API namespace where you can access the private API libs.
 * It's also the default if it wasn't obvious.
 * https://android.googlesource.com/platform/bionic/%2B/1ffec1cc4d0e283bb1ff6f49843769a3493b8d73/linker/dlfcn.cpp#294
 * It is typically accessed by passing &dlopen as the caller_addr, to trick the linker into
 * thinking we are calling from the default namespace.
 *
 */

// Dirs where system libs are stored.
#define SYSTEM_LIBS_PATH "/system/:/system_ext/:/data/:/vendor/:/apex/"

/*
  Note:
    You may have found that escapeNs does not have search paths within your app native dir and thus
    cannot properly resolve any NEEDED's from there. Make your own namespace. I suggest creating
    a SHARED ns with parent escapeNs. Simply add your nativeLibraryDir to default_library_path and
    it will be appended to the list that it will search for NEEDEDs!

    or yknow, preload them. dlopen them before the lib that needs them, assuming its not isolated.

    There's no not janky way of doing this, just take the tiny memory duplication from a whole two
    namespaces existing. It's better than shoving it into the library.
 */

/**
 * A namespace that has access to SYSTEM_LIBS_PATH.
 * Fun fact: bylaws/liblinkernsbypass coined this term
 */
struct android_namespace_t* get_escape_namespace();

/**
 * @brief dlopen, but you can specify which namespace to open the library in.
 *
 * IMPORTANT: If `name` is compiled with the '-z global' linker flag and RTLD_GLOBAL is supplied in `flags` the library will be added to the namespace's LD_PRELOAD list
 * @source bylaws/liblinkernsbypass but I can't find the code responsible for this ¯\(ツ)/¯
 * @param ns Specified namespace to open the library in
 */
void* linker_ns_dlopen(const char* name, int flag, struct android_namespace_t* ns);
/**
 * linker_ns_dlopen, but it patches the SONAME to another one in case another namespace that your
 * specified namespace is linked to loaded it but you want another copy
 * (like libvulkan.so, the vulkan loader, to load custom driver)
 * @param libPath Path to library to load, can be relative but idk where $PWD is [this is fed to open()]
 * @param patchedLibDir Directory to "store" the patched file (may not be saved to disk, its a gamble)
 * @param flag dlopen flags
 * @param ns Specified namespace to open the library in
 */
void* linker_ns_dlopen_unique(const char* libPath, const char* patchedLibDir, int flag, struct android_namespace_t* ns);

/**
 * Create a new android linker namespace (not linux namespaces, these only affect the linker)
 * @param name Name of namespace
 * @param ld_library_path Prioritized path to look for SONAMEs
 * @param default_library_path Default path to look for SONAMEs
 * @param type Namespace type flags, see enums in nsbypass_t.h
 * @param permitted_when_isolated_path See ANDROID_NAMESPACE_TYPE_ISOLATED
 * @param parent_namespace Namespace to inherit loaded SONAMEs and search paths from, if NULL, uses
 * namespace of caller_addr. If that's also NULL, uses g_anonymous_namespace (which is default
 * namespace unless a namespace was created with flag ANDROID_NAMESPACE_TYPE_ALSO_USED_AS_ANONYMOUS
 * in the process, I can't find a way to check for this.)
 * @param caller_addr __builtin_return_address(0) for current namespace or &dlopen for default
 */
struct android_namespace_t* private_create_namespace(
        const char* name,
        const char* ld_library_path,
        const char* default_library_path,
        uint64_t type,
        const char* permitted_when_isolated_path,
        struct android_namespace_t* parent_namespace,
        const void* caller_addr);

/**
 * Allow a namespace to look in another namespace for specific SONAMEs. Does not affect search paths.
 * @param from Namespace that will be allowed to look in "to" for the specified "shared_libs_sonames"
 * @param to Additional namespace that "from" is allowed to look into for the specified
 * "shared_libs_sonames". If NULL, it defaults to default namespace.
 * @param shared_libs_sonames Colon-seperated list of SONAMEs (NOT PATHS)
 * @return Whether successful.
 */
bool private_link_namespaces(
        struct android_namespace_t* from,
        struct android_namespace_t* to,
        const char* shared_libs_sonames);

/**
 * Allow a namespace to look in another namespace for SONAMEs. Does not affect search paths.
 * @param from Namespace that will be allowed to look in "to"
 * @param to Additional namespace that "from" is allowed to look into, unlike
 * private_link_namespaces, passing NULL results in an error.
 * @return Whether successful.
 */
bool private_link_namespaces_all_libs(
        struct android_namespace_t* from,
        struct android_namespace_t* to);

/*
 * Some namespaces are
 * "(default)" for g_default_namespace
 * "sphal" "vendor" "default" for vendor namespace, used in android_load_sphal_library
 *
 * Android doesn't let you see the ld.config.txt file so this is pretty much a guessing game.
 * See https://cs.android.com/android/platform/superproject/+/android-latest-release:system/linkerconfig/
 */
/**
 * Get an exported namespace
 * @param name The namespace to get
 * @return The namespace you got
 */
struct android_namespace_t* private_get_exported_namespace(
        const char* name);

/**
 * Pretty much just the normal dlclose, you don't need this, it's here for consistency
 */
int private_dlclose(void* handle);

/**
 * dlopen but now you can edit caller_addr
 * @param caller_addr __builtin_return_address(0) for current namespace or &dlopen for default
 * @return
 */
void* private_dlopen(
        const char* filename,
        int flags,
        const void* caller_addr);

/**
 * private_dlopen but now you can edit extinfo
 * @param extinfo See dlext.h in the NDK for whats android_dlextinfo
 * @param caller_addr __builtin_return_address(0) for current namespace or &dlsym for default
 * @return
 */
void* private_dlopen_ext(
        const char* filename,
        int flags,
        const android_dlextinfo* extinfo,
        const void* caller_addr);

/**
 * dlsym but now you can edit caller_addr
 * @param caller_addr __builtin_return_address(0) for current namespace or &dlsym for default
 * @return
 */
void* private_dlsym(
        void* handle,
        const char* symbol,
        const void* caller_addr);

#ifdef __cplusplus
}
#endif

#endif //NSBYPASS_NSBYPASS_H
