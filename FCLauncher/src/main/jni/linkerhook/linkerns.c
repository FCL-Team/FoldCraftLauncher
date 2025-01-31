//
// Created by Vera-Firefly on 17.01.2025.
//

#include <android/dlext.h>
#include <string.h>
#include <stdio.h>
#include "linkerhook.h"

__attribute__((visibility("default"), used))
void linker_hook_set_handles(void* handle, void* dlopen_ext, void* get_namespace) {
    return set_handles(handle, dlopen_ext, get_namespace);
}

__attribute__((visibility("default"), used))
void* android_dlopen_ext(const char* filename, int flags, const android_dlextinfo* extinfo) {
    return dlopen_ext(filename, flags, extinfo);
}

__attribute__((visibility("default"), used))
void* android_load_sphal_library(const char* filename, int flags) {
    return load_sphal_library(filename, flags);
}

__attribute__((visibility("default"), used))
uint64_t atrace_get_enabled_tags() {
    return hook_atrace_get_enabled_tags();
}