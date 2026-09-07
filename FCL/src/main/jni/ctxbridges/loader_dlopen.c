//
// Created by maks on 26.10.2024.
//
#include <dlfcn.h>
#include <linux/limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <androidnsbypass/nsbypass.h>
#include <androidnsbypass/nsbypass_t.h>
#include "global_state.h"

// 获取逃逸命名空间（懒创建；继承系统库路径，可访问私有 API 库）
static struct android_namespace_t* get_app_escape_ns() {
    if (!app_escapeNs) {
        app_escapeNs = private_create_namespace(
                "app-escapeNs",
                NULL,
                getenv("POJAV_NATIVEDIR"), // append to search path!
                ANDROID_NAMESPACE_TYPE_SHARED, // Inherit from escapeNs paths
                getenv("POJAV_NATIVEDIR"), // not needed, useless for non-isolate
                get_escape_namespace(), // Inherit from escapeNs so we get the system lib paths too
                __builtin_return_address(0));
    }
    return app_escapeNs;
}

// 普通 dlopen 失败时改用逃逸命名空间重试，可加载随驱动打包、默认命名空间不可见的库
static void* dlopen_with_fallback(char* name, int flags) {
    void* dl_handle = dlopen(name, flags);
    if (dl_handle != NULL) return dl_handle;
    dl_handle = linker_ns_dlopen(name, RTLD_LOCAL | RTLD_LAZY, get_app_escape_ns());
    if (dl_handle != NULL) return dl_handle;
    return NULL;
}

void* loader_dlopen(char* primaryName, char* secondaryName, int flags) {
    void* dl_handle;
    if(primaryName == NULL) goto secondary;

    dl_handle = dlopen_with_fallback(primaryName, flags);
    if(dl_handle != NULL) return dl_handle;

    if(secondaryName == NULL) goto dl_error;

    secondary:
    dl_handle = dlopen_with_fallback(secondaryName, flags);
    if(dl_handle == NULL) goto dl_error;
    return dl_handle;
    dl_error:
    printf("%s", dlerror());
    return NULL;
}