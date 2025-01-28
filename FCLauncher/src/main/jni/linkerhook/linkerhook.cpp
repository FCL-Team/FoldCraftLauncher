//
// Created by Vera-Firefly on 17.01.2025.
//

#include <android/dlext.h>
#include <string.h>
#include <stdio.h>
#include <atomic>
#include "linkerhook.h"

static void* (*dlopen_ext_impl)(const char* filename, int flags, const android_dlextinfo* extinfo, const void* caller_addr);
static struct android_namespace_t* (*get_exported_namespace_impl)(const char* name);

static void* ready_handle;
static std::atomic<void*> global_ready_handle{nullptr};

static const char* supported_namespaces[] = {"sphal", "vendor", "default"};

void set_handles(void* handle, void* dlopen_ext, void* get_namespace) {
    ready_handle = handle;
    global_ready_handle.store(handle);
    dlopen_ext_impl = (decltype(dlopen_ext_impl))dlopen_ext;
    get_exported_namespace_impl = (decltype(get_exported_namespace_impl))get_namespace;
}

static void* checkIfGlobalReadyHandle() {
    void* handle = global_ready_handle.load();
    if (handle == nullptr)
    {
        fprintf(stderr, "Global ready handle is null, falling back to ready_handle.\n");
        return ready_handle;
    }
    return handle;
}

void* dlopen_ext(const char* filename, int flags, const android_dlextinfo* extinfo) {
    if (strstr(filename, "vulkan."))
        return checkIfGlobalReadyHandle();

    return dlopen_ext_impl(filename, flags, extinfo, reinterpret_cast<const void*>(&dlopen_ext));
}

void* load_sphal_library(const char* filename, int flags) {
    if (strstr(filename, "vulkan."))
        return checkIfGlobalReadyHandle();

    struct android_namespace_t* androidNamespace = nullptr;
    for (const char* namespace_name : supported_namespaces)
    {
        androidNamespace = get_exported_namespace_impl(namespace_name);
        if (androidNamespace != NULL) break;
    }

    android_dlextinfo extinfo = {
        .flags = ANDROID_DLEXT_USE_NAMESPACE,
        .library_namespace = androidNamespace
    };

    return dlopen_ext_impl(filename, flags, &extinfo, reinterpret_cast<const void*>(&dlopen_ext));
}

uint64_t hook_atrace_get_enabled_tags() {
    return 0;
}