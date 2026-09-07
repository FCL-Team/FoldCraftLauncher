// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi

/*
 * Exists solely for compatibility.
 */

#include <dlfcn.h>
#ifdef __cplusplus
extern "C" {
#endif

#include <androidnsbypass/nsbypass.h>
#include <liblinkernsbypass_compat/android_linker_ns.h>

#ifdef __cplusplus
}
#endif

bool linkernsbypass_load_status() {
    return get_escape_namespace();
}

struct android_namespace_t *android_create_namespace(const char *name,
        const char *ld_library_path,
        const char *default_library_path,
        uint64_t type,
        const char *permitted_when_isolated_path,
        android_namespace_t *parent_namespace) {
    auto caller{__builtin_return_address(0)};
    return private_create_namespace(name, ld_library_path, default_library_path, type,
            permitted_when_isolated_path, parent_namespace, caller);
}

struct android_namespace_t *android_create_namespace_escape(const char *name,
        const char *ld_library_path,
        const char *default_library_path,
        uint64_t type,
        const char *permitted_when_isolated_path,
        android_namespace_t *parent_namespace) {
    auto caller{reinterpret_cast<void *>(&dlopen)};
    return private_create_namespace(name, ld_library_path, default_library_path, type,
            permitted_when_isolated_path, parent_namespace, caller);
}

android_get_exported_namespace_t android_get_exported_namespace = private_get_exported_namespace;
android_link_namespaces_all_libs_t android_link_namespaces_all_libs = private_link_namespaces_all_libs;
android_link_namespaces_t android_link_namespaces = private_link_namespaces;

// Not 1:1 with real implementation.
// escapeNs has SYSTEM_LIBS_PATH in default_library_path and permitted_when_isolated_path
// Real implementation has those two fields NULL
// This only affects SONAME look up paths so it should not break any code, if anything it should
// fix broken code.
bool linkernsbypass_link_namespace_to_default_all_libs(struct android_namespace_t *to){
    // escapeNs should be set by constructor or the process has exited already
    // therefore this should never return a false if you didn't pass a nullptr.
    return private_link_namespaces_all_libs(to, get_escape_namespace());
}

void* linkernsbypass_namespace_dlopen(
        const char* filename,
        int flags,
        struct android_namespace_t* ns) {
    return linker_ns_dlopen(filename, flags, ns);
}

void *linkernsbypass_namespace_dlopen_unique(const char *libPath, const char *libTargetDir, int flags, struct android_namespace_t *ns){
    return linker_ns_dlopen_unique(
            libPath,
            libTargetDir,
            flags,
            ns);
}
