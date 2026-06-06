//
// Created by maks on 11.05.2026.
//

#ifndef POJAVLAUNCHER_ANDROID_NAMESPACE_FUNC_H
#define POJAVLAUNCHER_ANDROID_NAMESPACE_FUNC_H

#include <stdint.h>
#include <stdbool.h>

typedef struct android_namespace_t* (*android_create_namespace_t)(
        const char* name, const char* ld_library_path, const char* default_library_path, uint64_t type,
        const char* permitted_when_isolated_path, struct android_namespace_t* parent);

typedef void* (*android_link_namespaces_t)(struct android_namespace_t* namespace_from,
                                           struct android_namespace_t* namespace_to,
                                           const char* shared_libs_sonames);

typedef void* (*loader_dlopen_t)(const char* filename, int flags, const void* caller_addr);

typedef int (*dlclose_impl)(void* handle);

typedef struct {
    void* dl_handle;
    dlclose_impl close;
    android_create_namespace_t create_namespace;
    android_link_namespaces_t link_namespaces;
} android_ldfuncs_t;

bool locate_namespace_funcs(android_ldfuncs_t* funcs);

#endif //POJAVLAUNCHER_ANDROID_NAMESPACE_FUNC_H
